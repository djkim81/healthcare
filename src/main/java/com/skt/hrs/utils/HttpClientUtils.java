package com.skt.hrs.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClients;


import com.pub.core.entity.DataEntity;
import com.pub.core.entity.ResponseResult;

public final class HttpClientUtils {
	
	private static final int HTTP_CONNECTION_TIMEOUT = 30; //second
	
	public static ResponseResult httpPostDataString( String requestURL, Map<String, String> map ) throws Exception {
		DataEntity param = new DataEntity(); 
		param.put("url", requestURL);
		param.put("param", map);
		return httpPostData(null, param);
		
	}
	
	public static ResponseResult httpPostData(HttpServletRequest req , DataEntity httpParam) throws IOException{
		
		CloseableHttpClient client= null;
		
		try{
			String url =httpParam.getString("url"); 
			client  = getClient(req, url, httpParam);
			
			HttpPost post = new HttpPost(url);
			
			Map reqParam = (Map)httpParam.get("param");

			if(reqParam != null){
				if("json".equals(reqParam.get("paramType").toString())){
					post.setHeader("Accept","application/json");
					post.setHeader("Content-type","application/json");
					post.setEntity(getStringEntity(reqParam));
					
				}else if("xml".equals(reqParam.get("paramType").toString())){
					StringEntity entity = new StringEntity(reqParam.get("paramXml").toString(), ContentType.create("application/xml", Consts.UTF_8));
					entity.setChunked(true);
					post.setEntity(entity);

				}else{
					post.setEntity(new UrlEncodedFormEntity(getParam(reqParam)));
				}
			}
			
			CloseableHttpResponse response = client.execute(post);
			
			return getResponseResult(response);
		}finally{
			if(client != null) client.close();
		}
	}
	
	
	private static ResponseResult getResponseResult(CloseableHttpResponse response) throws IOException {
		ResponseResult result =new ResponseResult();
		int statusCode = response.getStatusLine().getStatusCode(); 
		
		result.setStatus(statusCode);
		result.setItemOne(EntityUtils.toString(response.getEntity()));
		
		if (statusCode == HttpStatus.SC_OK) {
			result.setMessageCode("success");
		} else {
			result.setMessageCode("error");
		}
		return result;
	}

	private static List<NameValuePair> getParam(Map reqParam) {
		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
		Iterator iter = reqParam.entrySet().iterator();
		
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry) iter.next();
			nameValuePairList.add(new BasicNameValuePair(String.valueOf(entry.getKey()),String.valueOf(entry.getValue())));
		}
		
		return nameValuePairList;
	}
	
	private static StringEntity getStringEntity(Map reqParam) throws UnsupportedEncodingException {
		return new StringEntity(JsonUtils.objectToString(reqParam));
	}

	private static CloseableHttpClient getClient(HttpServletRequest req, String url, DataEntity httpParam) {
		CloseableHttpClient client ; 
		
		Map cookieParam = (Map)httpParam.get("cookie");
		
		CookieStore cookieStore = null; 
		String cookieDomain ="";
		if(req ==null){
			cookieDomain = httpParam.getString("cookieDomain");
		}else{
			cookieDomain = "."+getDomain(req);
		}
		
		if(cookieParam != null){
			cookieStore = getCookieStore(cookieStore, cookieParam,cookieDomain);
		}
		
		if(httpParam.getBoolean("ssoCookie")){
			cookieStore = getCookieStore(cookieStore, req ,cookieDomain);
		}
		
		int timeout = httpParam.getInt("timeout" , HTTP_CONNECTION_TIMEOUT) * 1000;
		
		RequestConfig config = RequestConfig.custom()
				.setConnectionRequestTimeout(timeout)
				.setConnectTimeout(timeout)
				.setSocketTimeout(timeout).build();
		
		SSLConnectionSocketFactory socketFactory = null; 
		
		if(cookieStore != null){
			client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore)
					.setDefaultRequestConfig(config).setSSLSocketFactory(socketFactory).build();
		}else{
			client = HttpClientBuilder.create().setDefaultRequestConfig(config).setSSLSocketFactory(socketFactory).build();
		}
		
		return client;
	}

	private static CookieStore getCookieStore(CookieStore cookieStore, Map cookieParam, String cookieDomain) {
		return getCookieStore(cookieStore, cookieParam, cookieDomain ,"/");
	}
	
	private static CookieStore getCookieStore(CookieStore cookieStore, Map cookieParam, String cookieDomain , String path) {
		cookieStore = cookieStore ==null? new BasicCookieStore() :cookieStore;
		
		Iterator iter = cookieParam.entrySet().iterator();
		BasicClientCookie cookie; 
		
		while(iter.hasNext()){
			Map.Entry entry = (Map.Entry) iter.next();
			
			cookie = new BasicClientCookie(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
			cookie.setDomain(cookieDomain);
			cookie.setPath(path);
			
			cookieStore.addCookie(cookie);
			
		}
		return cookieStore;
	}
	
	private static CookieStore getCookieStore(CookieStore cookieStore, HttpServletRequest req, String cookieDomain) {
		return getCookieStore(cookieStore, req, cookieDomain,"/");
	}
	
	private static CookieStore getCookieStore(CookieStore cookieStore, HttpServletRequest req, String cookieDomain , String path) {
		cookieStore = cookieStore ==null? new BasicCookieStore() :cookieStore;
		
		BasicClientCookie cookie; 
		
		for(Cookie tmpCookie : req.getCookies()){
			cookie = new BasicClientCookie(tmpCookie.getName(), tmpCookie.getValue());
			cookie.setDomain(cookieDomain);
			cookie.setPath(path);
			
			cookieStore.addCookie(cookie);
		}
		
		return cookieStore;
	}

	public static String getDomain(HttpServletRequest req) {
		String connDomain = req.getServerName();
		
		String[] nameArr = connDomain.split("\\.");
		
		if(nameArr.length > 1){
			connDomain =nameArr[nameArr.length-2] +"."+nameArr[nameArr.length-1];
		}
		return connDomain;
	}
	
}
