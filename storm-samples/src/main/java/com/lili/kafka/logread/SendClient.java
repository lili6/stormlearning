package com.lili.kafka.logread;

import com.lili.kafka.simple.LogProducer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by lili on 2015/3/17.
 */
public class SendClient {
	public static final String topic = "daycharge_topic";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String path =        SendClient.class.getClassLoader().getResource("").getPath();
		System.out.print("path:::" +path);
//		readFileByLines(path+"Audit_Economic.2014-12-20-00.log");
		readFileByLines(path+"Audit_Economic_Yuanbao.2014-12-20-00.log");

	}

	private static void sendLog() {
		LogProducer producer = null;
		try{
			producer = new LogProducer();
			int i=0;
			String msg ;
			while(true){
				msg =    "I love U He love Me";
				producer.send(topic, msg);
				System.out.println("开始发送数据：："+ msg);
				i++;
				Thread.sleep(2000);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(producer != null){
				producer.close();
			}
		}
	}

	/**
	 * 以行为单位读取文件，常用于读面向行的格式化文件
	 */
	public static void readFileByLines(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				System.out.println("line " + line + ": " + tempString);
				char splitStr = 0x01;
				String[] strings = tempString.split(String.valueOf(splitStr));
				for (int i = 0 ; i <strings.length; i++){
					System.out.println("strings["+i+"]=" + strings[i].trim());
				}
				// 显示行号
//				System.out.println("line " + line + ": " + tempString);
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}


}
