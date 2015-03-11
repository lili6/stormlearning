package com.lili.ad.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lili.ad.model.Entity;

public class DataUtil {
	
	public static Logger log = LoggerFactory.getLogger(DataUtil.class);
	
	public static Entity getObject(String line,Entity ei){
    	String[] lineData = line.split(ei.getSeparative());
    	int dataLength =  lineData.length;
    	Map<Integer,Method> map = getFunctionByData(dataLength,ei);
		for(int i = 0 ; i < dataLength ; i++){
			try {
				map.get(i).invoke(ei, lineData[i]);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(i+"\t"+lineData[i]);
				log.error(e.toString());
			}
		}
		return ei;
		
    }
    
   
    public static Map<Integer,Method> getFunctionByData(int dataLength,Entity ei){
    	Map<Integer,Method> map = new HashMap<Integer,Method>();
    	Method[] method = null;
    	Field[] field = null;
		try {
			method = ei.getClass().getDeclaredMethods();
			field = ei.getClass().getDeclaredFields();
			if( dataLength > field.length ){
				log.error("长度超出");
				return null;
			}
			for(int i = 0 ; i < dataLength ; i++){
				for( Method md : method ){
					if(md.getName().contains("set") && md.getName().toLowerCase().endsWith(field[i].getName().toLowerCase())){
						map.put(i, md);
					}
				}
			}
		} 
		catch (SecurityException e) {
			e.printStackTrace();
			log.error(e.toString());
		} 
    	return map;
    	
    }  
 
}
