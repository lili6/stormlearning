package com.ad.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ad.model.TriggerTables;
import com.ad.util.DbUtil;

/**
 * 
 * 监控广告配置表线程
 */
public class DbTriggerThread  implements Runnable {
	
	public static Logger log = LoggerFactory.getLogger(DbTriggerThread.class);
	
	public static final String TABLE_NAME = "wg_ad.ad_trigger";
	
	public String test = "";
	
	//
	public static  Map<Integer,String> WgadAdvertMap = new Hashtable<Integer,String>();
	
	
	static{
		/**
		 *加载配置
		 */
		initAd("wg_ad.wgad_advert","id");
	}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized void initAd(String tableName,String platformName){
		Connection con =  DbUtil.getCon(platformName);
		Statement sta = null;
		ResultSet rs = null;
		String selectSql = "select id from "+tableName;
		try {
			sta = con.createStatement();
			rs  =  sta.executeQuery(selectSql);
			if(WgadAdvertMap.size() > 0){
				WgadAdvertMap.clear();
			}
			while (rs.next()){
				WgadAdvertMap.put(rs.getInt("id"), "1");
			}
			log.info("data source table "+tableName+" size :"+WgadAdvertMap.size());
		}	
		catch(Exception e){
			log.error(selectSql+"\t"+e.toString());
		}
	}
	
	/**
	 * @return
	 */
	public static synchronized Map<Integer,String> getWgadAdvert(){
		return WgadAdvertMap;
	}
	
	@Override
	public void run() {
		Connection con =  DbUtil.getCon("");
		String sql =  " select * from  " + TABLE_NAME + " limit 1 " ; 
		Statement sta = null;
		ResultSet rs = null;
		TriggerTables oldTable = new TriggerTables();
		TriggerTables newTable = new TriggerTables();
		try {
			sta = con.createStatement();
			rs  = sta.executeQuery(sql);
			while (rs.next()){
				oldTable.setWgadAdvert(rs.getInt(0));
			}
			
			while(true){
				Thread.sleep(10000);
				rs = sta.executeQuery(sql);
				while (rs.next()){
					newTable.setWgadAdvert(rs.getInt(1));
				}
				compareTable(oldTable, newTable);
			}
			
		}
		catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public static void compareTable(TriggerTables oldTable,TriggerTables newTable){
		if(oldTable.getWgadAdvert() != oldTable.getWgadAdvert()){
			initAd("wgad_advert","id");
			oldTable.setWgadAdvert(newTable.getWgadAdvert());
		}
		
	}
	
}
