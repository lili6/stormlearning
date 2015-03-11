package com.lili.ad.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lili.ad.task.AdLogBolt;




public class DbUtil {

	public static Logger log = LoggerFactory.getLogger(DbUtil.class);
	
	
	public static  String defaultTopic  ;
	
	public static final int NUM_RETRIES = 10;
	
	//1000 ms 
	public static final int SLEEP_TIME = 1000;
	
	/**
	 * 
	 */
	public static clojure.lang.PersistentHashMap proMap ;
	
	//主题名
	
	public static  String topicName;
	
	//登陆用户表名
	
	public static String userGameTable;
	
	/**
	 * 加载jdbc相关信息
	 */
	static{
		proMap = AdLogBolt.proMap;
		topicName = (String)proMap.get("topic");
		defaultTopic = (String)proMap.get("default");
		userGameTable = (String)proMap.get(topicName+".usergametable");
	}
	
	
	/**
	 * 根据topic选择不同的数据源
	 * @param topic
	 * @return
	 */
	public static Connection getCon(String topic){
		
		if(null == proMap){
			proMap = AdLogBolt.proMap;
		}
		Connection conn = null;
		String driverName = (String)proMap.get("dataSource."+topic+".driverClassName");
		String url = (String)proMap.get("dataSource."+topic+".url");
		String user = (String)proMap.get("dataSource."+topic+".username");
		String passwd = (String)proMap.get("dataSource."+topic+".password");
		try {
			Class.forName(driverName);
			conn = DriverManager.getConnection(url, user, passwd);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("连接数据库异常:   url:[ "+url+" ] , user [ "+user+" ] , password [ "+passwd+" ]");
		}
		return conn;
	}
	
	public static Statement getSt(Connection con){
		Statement st  = null;
		try {
			st = con.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return st;
	}
	
	public static PreparedStatement getPrepareSt(Connection con,String sql){
		PreparedStatement st  = null;
		try {
			st = con.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return st;
	}
	/**
	 * 
	 * @param type 1 gamebox,2 tagame,3gamefuse
	 * @return
	 */
	
	public static Connection getRetryCon(String platformName) {
		Connection con = null;
		for (int tries = 1; tries <= NUM_RETRIES; tries++) {
			con = getCon(platformName);
			if (null != con) {
				break;
			}
			try { // Sleep
				Thread.sleep(SLEEP_TIME);
			} 
			catch (InterruptedException e) {
				log.error(e.toString());
			}
		} 
		return con; 
	}
	
	 /**
	  * 关闭连接
	  * @param con
	  * @param sta
	  */
     public static void CloseConAndSta(Connection con,Statement  sta){
    	 if(null != con ){
    		 try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
    	 }
    	 if(null != sta ){
    		 try {
				sta.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	 }
    	 
     }
     
     /**
	  * 关闭连接
	  * @param sta
	  */
     public static void CloseCon(Connection  con){
    	
    	 if(null != con ){
    		 try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	 }
    	 
     }
     
     /**
	  * 关闭连接
	  * @param sta
	  */
     public static void CloseSta(Statement  sta){
    	
    	 if(null != sta ){
    		 try {
				sta.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	 }
    	 
     }
     
 }
