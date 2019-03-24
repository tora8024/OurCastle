package com.ourcastle.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadConfigFile {
	

	static Logger logger = LoggerFactory.getLogger(ReadConfigFile.class);
//	System.getProperty("jboss.server.config.dir")+"\\"+
	String propFile="OurCastle.properties";
	
	public  String getPropValues(String key) throws IOException {
		 
		String result = "";
		Properties prop = new Properties();
		try{
			
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFile); 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				logger.error("property file '" + propFile + "' not found in the classpath");
			}

			result = prop.getProperty(key);
			result=new String(result.getBytes("ISO-8859-1"),"gbk"); 
			
		}catch(Exception e){
			logger.error("load property file '" + propFile + "' fail");
			return "";
		}
		try{
			result = prop.getProperty(key);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("getProperty fail, key=" + key );
			return "";
		} 
		
		logger.info("getProperty by key:" + key +"==>value:"+result);
		return result;

	}
	
	public static void main (String[] aa){
		ReadConfigFile efe=new ReadConfigFile();
//		efe.propFile="D:/jboss-as-7.1.1.Final/standalone/configuration/OurCastle.properties";
		try {
			logger.info(efe.getPropValues("storeName"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
