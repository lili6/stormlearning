package com.lili.ad.task;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import com.lili.ad.dao.InsertToDb;
import com.lili.ad.model.Ad;
import com.lili.ad.util.DataUtil;
import com.lili.ad.util.MyConfig;

public class AdLogBolt extends BaseBasicBolt {
	
	//线程池中最大的任务数
	public static final int  maxThred = 10 ;
	//线程池中最小的任务数
	public  static final int  minThred = 10 ;
	//空闲的线程数
	public  static final int  activeAlive = 0 ;

	public static Logger log = LoggerFactory.getLogger(AdLogBolt.class);


	PreparedStatement pst = null;

	//static DbThread dt = null;

	//public static final String hour = "xxx";
	// distinct 功能
	public static Map<String, String> distinctMap = new Hashtable<String, String>();
	
	//30条数据
	public static   int  countThreshold  ;
	
	//30s钟
	public static   int  countSed  ;
	
	
	private  long beginTime = 0l; 
	
	private  long endTime; 
	
	private List<Ad> list  =  new ArrayList<Ad>();
	
	private int threadCount = 0;
	
	/**
	 *  jdbc配置信息 map
	 */
	public static clojure.lang.PersistentHashMap proMap;
	

	
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		proMap =  (clojure.lang.PersistentHashMap)stormConf;
		countThreshold = Math.max(new Integer((String)proMap.get("count")),MyConfig.COUNT_THRESHOLD);
		countSed = Math.max(new Integer((String)proMap.get("timeout")),MyConfig.COUNT_SED);
	}

	/**
	 * 更新缓存	 *
	 * @param
	 */
	/*public synchronized void updateDistinctMap(Ad ad) {
		//
		Timestamp ts = null; 
        ts = Timestamp.valueOf(ad.getHour());   
		if (ad.getHour().equals(distinctMap.get(hour))) {
				distinctMap.put(ad.getUid()+"#"+ad.getAdId(), "");
				return;
		}	
		
		distinctMap.clear();
		distinctMap.put(ad.getUid()+"#"+ad.getAdId(), "");
		distinctMap.put(hour, ad.getHour());
	}
	
	public synchronized  Map<String, String> getDistinctMap(){
		return distinctMap;
	}
*/
	
	public synchronized void insertToDb(){
		new InsertToDb().run(list);
		list.clear();
	}
	
	public synchronized List<Ad> getList(){
		return list;
	}
	

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		beginTime = System.currentTimeMillis();
		//只触发一次 
		if(threadCount == 0){
		Thread thread = new Thread("insert_todb") {
			public void run() {
				log.info("启动线程");
				while (true) {
					try {
						Thread.currentThread().sleep(3000);
						endTime  =  System.currentTimeMillis();
						if(getList().size() >=  countThreshold || ( (getList().size() > 0  && (endTime - beginTime)/1000 > countSed ))){
							if (getList().size() >=  countThreshold){
								log.info("条数超过["+countThreshold+"] 开始提交");
							}
							else{
								log.info("时间超过["+countSed+"] 强制提交");
							}
							insertToDb();
						}
					} catch (InterruptedException e) {
						log.error(e.toString());
					}
				}
			}
		};
		thread.start();
		}
		threadCount  = 2;
		Ad ad = (Ad) DataUtil.getObject(input.getString(0), new Ad());
		getList().add(ad);

	}

	public static void writeDate(){
		
	}
	
	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		System.out.println("关闭时也不执行...............................................");
	}

	

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("ad"));
	}
	
	public static void main(String[] args) {
		  long beginTime = 0l; 
		
		  long endTime; 
		while(true){
			if(beginTime == 0l){
				beginTime = System.currentTimeMillis();
			}
			endTime = System.currentTimeMillis();
			if((endTime - beginTime)/1000 == 5){
				System.out.println("5秒");
				beginTime = System.currentTimeMillis();
				
			}
		}
	}

}