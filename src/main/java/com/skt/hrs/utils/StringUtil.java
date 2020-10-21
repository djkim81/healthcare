package com.skt.hrs.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.Clob;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

public class StringUtil {
	public static String null2string(String obj) {
		String str = "";
		
		if(obj != null && obj != "") {
			str = obj;
		} 
		
		return str;
	}
	
	public static String object2string(Object obj) {
		String str = "";

		if(obj instanceof String) {
			str = String.valueOf(obj);;
		} else if(obj instanceof Integer) {
			str = String.valueOf(obj);
		} else if(obj instanceof Long) {
			str = String.valueOf(obj);
		} else if(obj instanceof BigDecimal) {
			str = String.valueOf(obj);
		} else {
			return str;
		}
		
		return str;
	}
	
	public static int object2int(Object obj) {
		int val = 0;
		if(obj instanceof Integer) {
			val = ((Integer) obj).intValue();
		} else if(obj instanceof String) {
			val = Integer.parseInt((String)obj);
		}
		return val;
	}
	
	public static int string2int(String obj) {
		int val = 0;
		if(obj instanceof String) {
			val = Integer.parseInt(obj);
		} 
		return val;
	}
	
	public static Long string2Long(String obj) {
		Long val = 0L;
		if(obj instanceof String) {
			val = Long.parseLong(obj);
		} 
		return val;
	}
	
	
	public static String object2string(Object obj, String value) {
		String str = "";
		
		if(obj instanceof String) {
			str = String.valueOf(obj);
		} else if(obj instanceof Integer) {
			str = String.valueOf(obj);
		} else if(obj instanceof Long) {
			str = String.valueOf(obj);
		} else {
			return str;
		}
		
		return str;
	}
	
	public static long object2long(Object obj, long value) {
		long val = 0;

		if(obj instanceof String) {
			val = Long.valueOf(value);
		} 
		
		return val;
	}
	
	public static int null2int(Object obj, int value) {
		int val = 0;

		if(obj == null) {
			val = Integer.valueOf(value);
		} else if( obj instanceof String) {
			val = Integer.parseInt(String.valueOf(obj));
		} else {
			val = ((Integer) obj).intValue();
		}
		return val;
	}
	
	public static String string2Null(String obj) {
		String str = obj;
		if(str.equals("null") || str.equals("undefined")) str = null;
		
		return str;
	}
	
	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}
	
	public static boolean isMapNullOrEmpty(Map<?,?>  m ) {
	    return m == null || m.isEmpty();
	}
	
	/**
	 * <pre>
	 * 개행문자를 <br/> 태그로 변경
	 * </pre>
	 * 
	 * @param text
	 * @return text
	 */
	public static String escapeNewlineToBr(String text) {
		String str = text;
		if (StringUtils.isEmpty(str)) return str;
		
		if (str.indexOf("\\r") >= 0) str = str.replaceAll("\\\\r", "\r");
		if (str.indexOf("\\n") >= 0) str = str.replaceAll("\\\\n", "\n");
//		if (str.indexOf("\r\r") >= 0) str = str.replaceAll("\\r{1,}", "\r"); // "\r\r" 처리위해 추가
		
		if (str.indexOf("\r\n") >= 0 || str.indexOf("\n") >= 0 || str.indexOf("\r") >= 0) {
			str = str.replaceAll("\\r\\n", "<br/>");
			str = str.replaceAll("\\r", "<br/>");
			str = str.replaceAll("\\n", "<br/>");
		}
		return str;
	}
	
	public static String cleanXSS(String value) {
		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		value = value.replaceAll("'", "&#39;");
		value = value.replaceAll("eval\\((.*)\\)", "");
		value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		value = value.replaceAll("<script", "");
		return value;
	}
	
	public static String cleanSearchXSS(String value) {
		if(value == null) {
			value = "";
		} else {
			value = value.replaceAll("<", "").replaceAll(">", "");
			value = value.replaceAll("\\(", "").replaceAll("\\)", "");
			value = value.replaceAll("'", "");
			value = value.replaceAll("eval\\((.*)\\)", "");
			value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "");
			value = value.replaceAll("<script", "");
		}
		return value;
	}
	
	public static String clobToString(Clob clob) throws Exception{

		
		StringBuffer sb = new StringBuffer();
		if(clob != null) {
			String str = "";
			
			BufferedReader br = new BufferedReader(clob.getCharacterStream());
			while((str = br.readLine()) != null) {
				sb.append(str);
			}
		}
		
		return sb.toString();		
	}
	
	/**
	 * 모든 HTML 태그를 제거하고 반환한다.
	 * 
	 * @param html
	 * @throws Exception  
	 */
	public static String removeTag(String html) throws Exception {
		html = html.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
		return html.replaceAll("&nbsp;","<br />");
	}
	
	public static ByteArrayOutputStream stream2String(HttpServletRequest request) throws Exception {
		
		ServletInputStream inputStream = request.getInputStream();
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		final ServletInputStream in = request.getInputStream();
		byte[] buf = new byte[1024];
		int r;
		while ((r = in.read(buf)) != -1) {
		    data.write(buf, 0, r);
		}
		
		return data;
	}
	
	public static String insertComma(String str) {
		
		str = String.format("%,.3", str);
		
		return str; 
	}
	
	public static  String convertHtmlchars(String htmlstr) {
		String convert = new String();
		convert = replace(htmlstr, "<", "&lt;");
		convert = replace(convert, ">", "&gt;");
		convert = replace(convert, "\"", "&quot;");
		convert = replace(convert, "&nbsp;", "&amp;nbsp;");
		return convert;
	}
	public static String replace(String original, String oldstr, String newstr)
	{
		String convert = new String();
		int pos = 0;
		int begin = 0;
		pos = original.indexOf(oldstr);

		if(pos == -1)
			return original;

		while(pos != -1)
		{
			convert = convert + original.substring(begin, pos) + newstr;
			begin = pos + oldstr.length();
			pos = original.indexOf(oldstr, begin);
		}
		convert = convert + original.substring(begin);

		return convert;
	}
	
	public static String substringByte(String str, int size) {		
		byte[] bytes = str.getBytes();
		if(bytes.length > size ) {    
			str = new String(bytes, 0, size );
		}
		return str;
	}

	public static String getStartTime(String date,String time) {
		String startDate = new String();
		startDate = date.substring(0, 4)+"/"+date.substring(4, 6)+"/"+date.substring(6,8);
		
		String[] startTime = time.split("~");
		
		return startDate+" "+startTime[0];
	}
	
	public static String getEndTime(String date,String time) {
		String endDate = new String();
		endDate = date.substring(0, 4)+"/"+date.substring(4, 6)+"/"+date.substring(6,8);
		
		String[] endTime = time.split("~");
		
		return endDate+" "+endTime[1];
	}
}
