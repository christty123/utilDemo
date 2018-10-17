package com.util.json;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {

	private static Logger logger = Logger.getLogger(JsonUtil.class);

	private static Gson gson = null;

	static {		
		if (null == gson) {
			gson = new Gson();			
		}		
	}


	public static <T extends Serializable> T jsonToObjectWithGson(String json,Class<T> clazz){		  
		return gson.fromJson(json, clazz);
	}

	public static String ObjectToJsonWithGson(Object obj){	
		return gson.toJson(obj);
	}

	public static <T extends Serializable> T fromJson(String json, Class<T> clazz){
		T obj = null;
		try{
			obj = gson.fromJson(json, clazz);
		}catch(com.google.gson.JsonParseException e){
			logger.error(e.getMessage(), e);
		}
		return obj;
	}

	public static <T extends Serializable> T fromJson(String json, Type type){
		T obj = null;
		try{
			obj = gson.fromJson(json, type);
		}catch(com.google.gson.JsonParseException e){
			logger.error(e.getMessage(), e);
		}
		return obj;
	}

	public static String toJson(Object obj){
		String json=null;
		try{
			json = gson.toJson(obj);
		}catch(JsonIOException e){
			logger.error(e.getMessage(), e);
		}

		return json;
	}
	
	public static Map<String,String> jsonToMapWithStringValue(String jsonStr) {
		Type type = new TypeToken<Map<String, String>>() {}.getType();		
		return gson.fromJson(jsonStr, type);
	}
	
	public static Map<String,Long> jsonToMapWithLongValue(String jsonStr) {
		Type type = new TypeToken<Map<String, Long>>() {}.getType();		
		return gson.fromJson(jsonStr, type);
	}
	
}
