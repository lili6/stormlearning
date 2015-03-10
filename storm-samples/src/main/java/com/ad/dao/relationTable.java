package com.ad.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ad.model.Ad;
import com.ad.util.DbUtil;

public class relationTable {
	
	public static Logger log = LoggerFactory.getLogger(relationTable.class);
	
	public static final  String GAMESQL = DbUtil.userGameTable;

	public static final String ADSQL = " select id,game_id from wg_ad.wgad_advert where id = ?  ";
	
	private PreparedStatement advertStmt = null;
	
	private PreparedStatement gameStmt = null;
	
	/**
	 * 
	 * @param con
	 * @param ad Map key : uid#game_id
	 * @return
	 */
    public  Map<String,Integer> getLoginUser(Connection con , List<Ad> ad,Map<String, Integer> advertMap){
    	long beginTime = System.currentTimeMillis();
    	gameStmt = DbUtil.getPrepareSt(con, GAMESQL);
    	Map<String,Integer> map = new HashMap<String, Integer>(); 
    	for (Ad adt : ad) {
    		if(null == advertMap.get(adt.getAdId())){
    			continue;
    		}
    		try {
    			//第一个参数uid
	    		gameStmt.setString(1,adt.getUid());
	    		gameStmt.setInt(2,advertMap.get(adt.getAdId()));
	    		ResultSet rs = gameStmt.executeQuery();
				while (rs.next()){
					if( StringUtils.isNotBlank(rs.getString("uid")) && ! "null".equals(rs.getString("uid") ) ){
						//key为uid加gameid
						map.put(adt.getUid()+"#"+advertMap.get(adt.getAdId()), rs.getInt("serverid"));
					}
				}	
    		}
			catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error("游戏sql uid"+adt.getUid()+"#"+advertMap.get(adt.getAdId())+"\t"+e.toString());
			}
		}
    	long endTime = System.currentTimeMillis();
    	log.info("查找用户首次登陆服用时:"+((endTime - beginTime)/1000)+"s" );
    	DbUtil.CloseSta(gameStmt);
		return map;
    }
   /**
    * 
    * @param con
    * @param ad
    * @return
    */
    public  Map<String,Integer> getWgadvert(Connection con,List<Ad> ad){
        advertStmt = DbUtil.getPrepareSt(con, ADSQL);
    	Map<String,Integer> map = new HashMap<String, Integer>(); 
    	int  gameId = 0 ;
		for(Ad adt : ad){
			try {
				advertStmt.setString(1, adt.getAdId());
				ResultSet rs = advertStmt.executeQuery();
				int i = 0;
				while (rs.next()){
					gameId = rs.getInt(2);
					i ++;
				}
				if(i > 0){
					// key: adid ,value : gameid
					map.put(adt.getAdId(),gameId);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				log.error("广告sql rccid"+adt.getAdId()+"\t"+e.toString());
			}
		}
		DbUtil.CloseSta(advertStmt);
		return map;
    }
}
