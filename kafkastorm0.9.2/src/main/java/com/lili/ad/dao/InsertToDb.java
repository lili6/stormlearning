package com.lili.ad.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lili.ad.model.Ad;
import com.lili.ad.util.DbUtil;

/**
 * 
 * 
 */
public class InsertToDb {

	public static Logger log = LoggerFactory.getLogger(InsertToDb.class);

	// private List<Ad> list = new ArrayList<Ad>();


	public static final String REAL_AD_TABLE = "wg_ad.wgad_stat_realtime_ad_new";

	public static  String timezone = "";
	
	public static  String platform = "";
	
	public static  String regokSql = "";
	
	
	// 实时表进入游戏的
	public static final String GAMESQL = "insert into "
			+ REAL_AD_TABLE
			+ "(adid,stat_time,regcnt,reggamecnt,serverid,uid,timezone,platform,country_id) "
			+ "values(?,?,1,1,?,?,?,?,?) ON DUPLICATE KEY UPDATE regcnt=regcnt+0  ";

	// 实时表只注册
	private static final String REGSQL = "insert into "
			+ REAL_AD_TABLE
			+ "(adid,stat_time,regcnt,uid,timezone,platform,country_id) values(?,?,1,?,?,?,?) ON DUPLICATE KEY UPDATE regcnt=regcnt+0 ";
    
	//北美mysql  58从库连接
	private  Connection con = DbUtil.getCon(DbUtil.defaultTopic);
	
	//查找用户最早进入游戏的mysql从库
	private  Connection notGbCon = DbUtil.getCon(DbUtil.topicName);

	

	/*
	 * public InsertToDbThread(List<Ad> list) { this.list = list; }
	 */
	static{
		timezone = (String)DbUtil.proMap.get(DbUtil.topicName+".timezone");
		platform = (String)DbUtil.proMap.get(DbUtil.topicName+".platform");
		initRegOkSql();
	}
	
	/**
	 * 拼接regok表的SQL
	 */
	static void initRegOkSql(){
		StringBuffer sql  = new StringBuffer();
		StringBuffer cloumns  = new StringBuffer();
		StringBuffer values  = new StringBuffer();
		for(int i = 0 ; i < Ad.TABLE_CLOUMN.length ; i ++){
			if( i != 0 ){
				cloumns.append(",");
				values.append(",");
			}
			cloumns.append(Ad.TABLE_CLOUMN[i]);
			values.append("?");
		}
		sql.append("insert into "+Ad.TABLE_NAME+"(");
		sql.append(cloumns+")values(");
		sql.append(values+") ON DUPLICATE KEY UPDATE ad_count = ad_count + 1 ");
		regokSql = sql.toString();
		
	}
	

	public  void run(List<Ad> list) {
		 
		/*try {
			if( null == con  ||  con.isClosed() ){
				con = DbUtil.getCon(DbUtil.defaultTopic); 
			}
			if( null == notGbCon  ||  notGbCon.isClosed() ){
				notGbCon = DbUtil.getCon(DbUtil.topicName);  
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("数据库重新连接失败"+e.toString());
			
			
		con.isValid(timeout)
		}	*/
		  con = DbUtil.getCon(DbUtil.defaultTopic); 
		  
		  notGbCon = DbUtil.getCon(DbUtil.topicName); 
		
		  PreparedStatement gamePstmt = DbUtil.getPrepareSt(con, GAMESQL);
		
		  PreparedStatement regPstmt = DbUtil.getPrepareSt(con, REGSQL);
		  
		  PreparedStatement regokPstmt = DbUtil.getPrepareSt(con, regokSql);
		  
		  
		  
		  //mysql会关掉长连接
		/*if(null == con) {
			gamePstmt = DbUtil.getPrepareSt(con, gameSql);
			regpstmt = DbUtil.getPrepareSt(con, regSql);
		}
		*/
		
		// 用户登陆
		Map<String, Integer> userMap = new HashMap<String, Integer>();
		
		// 广告配置
		Map<String, Integer> advertMap = new HashMap<String, Integer>();
				
		relationTable rT =  new relationTable();
		
		// key为adid value为gameid
		
		int gameCount = 0;

		int regCount = 0;
		
		int regOkCount = 0;
		
		String adId = "";
		
		
		List<Ad> tmpList = new ArrayList<Ad>();
		
		//原始表
		
		for (Ad ad : list) {
			/**
			 * 插入到原始表  public static final String TABLE_CLOUMN[] = { "reg_date", "reg_ip", "uid","last_domain", "ad_id", "sub_id", "reg_country", "put_date","plat_type" };
			 * 
			 */
			try{
				regokPstmt.setTimestamp(1, ad.getRegDate());
				regokPstmt.setString(2, ad.getRegIp());
				regokPstmt.setString(3, ad.getUid());
				regokPstmt.setString(4, ad.getLastDomain());
				regokPstmt.setString(5, ad.getAdId());
				regokPstmt.setString(6, ad.getSubId());
				regokPstmt.setInt(7, ad.getRegCountryId());
				regokPstmt.setTimestamp(8, ad.getPutDate());
				regokPstmt.setString(9, ad.getPlatType());
				regokPstmt.addBatch();
			}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error("regok:"+e.toString());
			}
			regOkCount ++;
			
			 if(StringUtils.isBlank(ad.getAdId()) ||  ad.getAdId().equals("undefined") ||  ad.getAdId().equals("null")){ 
				 continue;
			 }
			 tmpList.add(ad);
		}
		
		
		/**
		 * 关联后的表 
		 */
		
		advertMap = rT.getWgadvert(con,tmpList);
		
		//key为gameid#userid value为serverid
		
		userMap = rT.getLoginUser(notGbCon,tmpList,advertMap);
		
		for (Ad ad : tmpList) {
			//广告ID
			adId = ad.getAdId();
			
			//通过广告rccid能关联
			if(advertMap.containsKey(adId)){
				//userMap包含uid # gameId
				
				if (userMap.containsKey(ad.getUid()+"#"+advertMap.get(adId))) {
					try {
						gamePstmt.setString(1, adId);
						gamePstmt.setTimestamp(2, ad.getHour());
						gamePstmt.setInt(3, userMap.get(ad.getUid()+"#"+advertMap.get(adId)));
						gamePstmt.setString(4, ad.getUid());
						gamePstmt.setString(5, timezone);
						gamePstmt.setString(6, platform);
						gamePstmt.setInt(7, ad.getRegCountryId());
						gamePstmt.addBatch();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						log.error("ad real time rccid:"+e.toString());
					}

					gameCount++;
				}
				else {
					try {
						regPstmt.setString(1, ad.getAdId());
						regPstmt.setTimestamp(2, ad.getHour());
						regPstmt.setString(3, ad.getUid());
						regPstmt.setString(4, timezone);
						regPstmt.setString(5, platform);
						regPstmt.setInt(6, ad.getRegCountryId());
						regPstmt.addBatch();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						log.error("ad real time login game:"+e.toString());
					}
					regCount++;
				}
			}

			
		}

		try {
			
			if (regOkCount > 0) {
				regOkCount = regokPstmt.executeBatch().length;
			}
			
			if (gameCount > 0) {
				gameCount = gamePstmt.executeBatch().length;
			}
			if (regCount > 0) {
				regCount = regPstmt.executeBatch().length;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
		}
		finally{
			DbUtil.CloseSta(gamePstmt);
			DbUtil.CloseSta(regPstmt);
			DbUtil.CloseSta(regokPstmt);
		}
		
		log.info("影响 "+Ad.TABLE_NAME+ " 表 [ " + (regOkCount) + "]条数据");
		log.info("影响"+REAL_AD_TABLE+ " 表 [ " + (gameCount + regCount) + "]条数据");
		

	}

}
