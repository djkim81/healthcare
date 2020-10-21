package com.skt.hrs.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pub.core.exception.EppltException;


public final class JsonUtils {
	private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
	
	/**
	 * single object
	 */
	private JsonUtils (){}
	 

	public static Map stringToJsonMap(String json) {
		return (Map) stringToObject(json, HashMap.class);
	}	
	

	public static Object stringToJsonClass(String json, Class clazz) {
		return stringToObject(json, clazz);
	}
	

	public static String jsonToString(Object jsonObject) {
		return objectToString(jsonObject);
	}
	

	public static String objectToString(Object json) {
		ObjectMapper om = new ObjectMapper();
		try {
			return om.writeValueAsString(json);
		} catch (JsonMappingException e) {
			throw new EppltException("objectToString JsonMappingException",e); 
		} catch (IOException e) {
			throw new EppltException("objectToString IOException",e); 
		}
	}
	
	public static <T> T stringToObject(String jsonString) {
		return (T) stringToObject(jsonString, HashMap.class);
	}
	

	public static <T> T stringToObject(String jsonString, Class<T> valueType) {
		try {
			ObjectMapper om = new ObjectMapper();
			return (T) om.readValue(jsonString, valueType);
		} catch (JsonParseException e) {
			throw new EppltException("objectToString JsonParseException",e); 
		} catch (JsonMappingException e) {
			throw new EppltException("objectToString JsonMappingException",e); 
		} catch (IOException e) {
			throw new EppltException("objectToString IOException",e); 
		}
	}
	

	public static String callbackObjectToString(String callback, Object json) {
		String reval = objectToString(json);
		if (!"".equals(callback)) {
			reval = callback + "(" + reval + ")";
		}
		return reval;
	}
	
	public static String xmlStringToJsonString(String xmlString){
		JSONObject xmlJSONObj;
		String xmlJSONObjString = "";
		try {
			xmlJSONObj = XML.toJSONObject(xmlString);
			xmlJSONObjString = xmlJSONObj.toString();
			
		} catch (JSONException e) {
			logger.error("Exception :: {} ", e);
		}
		return xmlJSONObjString;
	}
	

	public static JSONObject xmlStringToJsonObject(String xmlString){
		JSONObject xmlJSONObj = null;
		try {
			xmlJSONObj = XML.toJSONObject(xmlString);
			
		} catch (JSONException e) {
			logger.error("Exception :: {} ", e);
		}
		return xmlJSONObj;
	}

}
