package com.skt.hrs.cmmn.csp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skt.hrs.cmmn.csp.vo.HttpStatVo;
import com.skt.hrs.cmmn.csp.vo.HeaderVo;
import com.skt.hrs.cmmn.csp.vo.ResultVo;



public class CspUtil {
	private static final Logger logger = LoggerFactory.getLogger(CspUtil.class);
	// XML 내의 Header 정보를 가져옴
	public static HeaderVo getHeader(Element head) {
		HeaderVo vo = new HeaderVo();

		Element	RESULT			= head.getChild("RESULT");
		Element	RESULT_CODE		= head.getChild("RESULT_CODE");
		Element	RESULT_MESSAGE	= head.getChild("RESULT_MESSAGE");
		Element	PLATFORM_MODUS	= head.getChild("PLATFORM_MODUS");
		Element	TEMP_ENCRYPT_KEY= head.getChild("TEMP_ENCRYPT_KEY");

		if(RESULT!=null) vo.setRESULT(RESULT.getValue());
		if(RESULT_CODE!=null) vo.setRESULT_CODE(RESULT_CODE.getValue());
		if(RESULT_MESSAGE!=null) vo.setRESULT_MESSAGE(RESULT_MESSAGE.getValue());
		if(PLATFORM_MODUS!=null) vo.setPLATFORM_MODUS(PLATFORM_MODUS.getValue());
		if(TEMP_ENCRYPT_KEY!=null) vo.setTEMP_ENCRYPT_KEY(TEMP_ENCRYPT_KEY.getValue());

		return vo;
	}

	public static String getParms(String[][] paramLt) {
		StringBuffer sb = new StringBuffer();

		for(int i=0; i<paramLt.length; i++) {
			if(i==0)sb.append("?");
			else	sb.append("&");
			sb.append(paramLt[i][0]);
			sb.append("=");
			sb.append(paramLt[i][1]);
		}

		return sb.toString();
	}

//	public static List<NameValuePair> getPostParms(String[][] paramLt) {
//		List<NameValuePair> params = new ArrayList<NameValuePair>();
	public static List getPostParms(String[][] paramLt) {
		List params = new ArrayList();

		for(int i=0; i<paramLt.length; i++) {
			params.add(new BasicNameValuePair(paramLt[i][0], paramLt[i][1]));
		}

		return params;
	}

	// Get Method 를 통한 XML 데이터 조회
	public static String GET(String URL, String[][] paramLt) throws Exception {
		HttpClient		client	= HttpClients.createDefault();

		HttpGet			get		= new HttpGet(URL+CspUtil.getParms(paramLt));


		
		// Create a custom response handler
//		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
		ResponseHandler responseHandler = new ResponseHandler() {
//			public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
			public Object handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}
		};

		return (String)client.execute(get, responseHandler);
	}

	// Get Method 를 통한 XML 데이터 조회
	public static HttpStatVo GET(String URL, String[][] paramLt, int timeout) throws Exception {
		RequestConfig		config	= RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).build();
		CloseableHttpClient	client	= HttpClientBuilder.create().setDefaultRequestConfig(config).build();

		

		HttpGet				get		= new HttpGet(URL+CspUtil.getParms(paramLt));

		// Create a custom response handler
//		ResponseHandler<HttpStatVo> responseHandler = new ResponseHandler<HttpStatVo>() {
		ResponseHandler responseHandler = new ResponseHandler() {
//			public HttpStatVo handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
			public Object handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
				HttpStatVo	vo = new HttpStatVo();
				int status = response.getStatusLine().getStatusCode();
				vo.setStatus(status);

				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					
					vo.setRtnText(entity != null ? EntityUtils.toString(entity) : null );
				} else {
					vo.setEx(new ClientProtocolException("Unexpected response status: " + status));
				}

				return vo;
			}
		};

		return (HttpStatVo)client.execute(get, responseHandler);
	}

	// Post Method 를 통한 XML 데이터 조회
	public static String POST(String URL, String[][] paramLt) throws Exception {
		HttpClient	client	= HttpClients.createDefault();
		HttpPost	post	= new HttpPost(URL);

		// Create a custom response handler
//		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
		ResponseHandler responseHandler = new ResponseHandler() {
//			public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
			public Object handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException("Unexpected response status: " + status);
				}
			}
		};

//		List<NameValuePair> params = CspUtil.getPostParms(paramLt);
		List params = CspUtil.getPostParms(paramLt);

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, Config.encode);
		post.setEntity(entity);

		return (String)client.execute(post, responseHandler);
	}

	// Post Method 를 통한 XML 데이터 조회
	public static HttpStatVo POST(String URL, String[][] paramLt, int timeout) throws Exception {
		RequestConfig		config	= RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).build();
		CloseableHttpClient	client	= HttpClientBuilder.create().setDefaultRequestConfig(config).build();
		HttpPost	post	= new HttpPost(URL);

		// Create a custom response handler
//		ResponseHandler<HttpStatVo> responseHandler = new ResponseHandler<HttpStatVo>() {
		ResponseHandler responseHandler = new ResponseHandler() {
//			public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
//			public HttpStatVo handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
			public Object handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
				HttpStatVo	vo = new HttpStatVo();
				int status = response.getStatusLine().getStatusCode();
				vo.setStatus(status);

				if (status >= 200 && status < 300) {
					HttpEntity entity = response.getEntity();
					vo.setRtnText(entity != null ? EntityUtils.toString(entity) : null );
				} else {
					vo.setEx(new ClientProtocolException("Unexpected response status: " + status));
				}

				return vo;
			}
		};

//		List<NameValuePair> params = CspUtil.getPostParms(paramLt);
		List params = CspUtil.getPostParms(paramLt);

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, Config.encode);
		post.setEntity(entity);

		return (HttpStatVo)client.execute(post, responseHandler);
	}

	// Post Method 를 통한 XML 데이터 조회
	public static Document getDocument(String URL, String[][] paramLt, String Method) {
		Document doc = null;

		try {
			String cont = null;
			if("POST".equals(Method)) cont = POST(URL, paramLt);
			else cont = GET(URL, paramLt);

			if(!"".equals(nvl(cont))) {
				SAXBuilder	builder	= new SAXBuilder();
				doc = builder.build(new ByteArrayInputStream(cont.toString().getBytes(Config.encode)));
			}
		} catch(Exception e) {
			logger.error("Exception :: {} ", e);
		}

		return doc;
	}

	// Post Method 를 통한 XML 데이터 조회
	public static Document getDocument(String URL, String[][] paramLt, String Method, int timeout) {
		Document doc = null;

		try {
			HttpStatVo hsv = new HttpStatVo();
			if("POST".equals(Method)) hsv = POST(URL, paramLt, timeout);
			else hsv = GET(URL, paramLt, timeout);

			if(hsv.getStatus()!=200) {
			}

			if(!"".equals(nvl(hsv.getRtnText()))) {
				SAXBuilder	builder	= new SAXBuilder();
				doc = builder.build(new ByteArrayInputStream(hsv.getRtnText().getBytes(Config.encode)));
			}
		} catch(SocketTimeoutException e) {
			SAXBuilder	builder	= new SAXBuilder();
			try {
				doc = builder.build(new ByteArrayInputStream(getFailXml(e.getMessage()).getBytes(Config.encode)));
			} catch (Exception e1) {
				logger.error("Exception :: {} ", e1);
			}
		} catch(Exception e) {
			logger.error("Exception :: {} ", e);
		}

		return doc;
	}

	// XML Element 의 값을 꺼내옴
	public static String getValue(HeaderVo hvo, Element elmt) {
		if(elmt==null) return "";

		String	val = elmt.getValue();

		try {
			if("BP".equals(hvo.getPLATFORM_MODUS())) {
				val = CryptoUtil.decrypt(val, hvo.getTEMP_ENCRYPT_KEY());
			} else if(!"SE".equals(hvo.getPLATFORM_MODUS())) {
				val = CryptoUtil.decrypt(val, Config.decryptKey);
			}
		} catch (Exception e) {
			logger.error("Exception :: {} ", e);
		}
		return val;
	}

	// XML Element 의 값을 복호화 하여 꺼내옴
	public static String getValue(String key, Element elmt) {
		if(elmt==null) return "";

		String val = "";
		try {
			val = CryptoUtil.decrypt(elmt.getValue(), key);
		} catch (Exception e) {
			logger.error("Exception :: {} ", e);
		}

		return val;
	}

	public static ResultVo getVoData(String URL, String[][] paramLt, String METHOD, int timeout) {
		ResultVo	vo = new ResultVo();
		Document doc = null;
		
		try {
			doc = CspUtil.getDocument(URL, paramLt, METHOD, timeout);
			if(doc != null) {
				Element root	= doc.getRootElement();
				Element head	= root.getChild("HEADER");
				//		Element body	= root.getChild("BODY");

				if(head!=null) {
					vo.setHEADER(CspUtil.getHeader(head));
				}
			}
		} catch (Exception e) {
			logger.error("Exception :: {} ", e);
		}

//		if(body!=null) {
//			vo.setBODY(getBody(vo.getHEADER(), body));
//		}

		return vo;
	}

	public static String nvl(String param) {
		if(param==null) return "";
		else return param;
	}

//	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void setData(Object obj, HeaderVo hvo, Element body, String elmtNm) {

		Element	elmt = body.getChild(elmtNm);
		if(elmt!=null) {
			Class	oClass	= obj.getClass();
			Method method;
			try {
				method = oClass.getDeclaredMethod("set"+elmtNm, new Class[]{String.class});
				method.invoke(obj, new Object[]{CspUtil.getValue(hvo, elmt)});
			} catch (IllegalAccessException e) {
				logger.error("Exception :: {} ", e);
			} catch (IllegalArgumentException e) {
				logger.error("Exception :: {} ", e);
			} catch (InvocationTargetException e) {
				logger.error("Exception :: {} ", e);
			} catch (NoSuchMethodException e) {
				logger.error("Exception :: {} ", e);
			} catch (SecurityException e) {
				logger.error("Exception :: {} ", e);
			}
		}
	}

	public static String getFailXml(String code, String message) {
		StringBuffer buff = new StringBuffer();

		buff.append("<RESPONSE>")
			.append("	<HEADER>")
			.append("		<RESULT>F</RESULT>")
			.append("		<RESULT_CODE>CUST_EXP").append((code!=null?"_"+code:"")).append("</RESULT_CODE>")
			.append("		<RESULT_MESSAGE>").append(message).append("</RESULT_MESSAGE>")
			.append("		<PLATFORM_MODUS>NR</PLATFORM_MODUS>")
			.append("	</HEADER>")
			.append("</RESPONSE>");

		return buff.toString();
	}

	public static String getFailXml(String message) {
		return getFailXml(null, message);
	}

	public static String chgSec(long start, long end) {
		return (Double.parseDouble(Long.toString(end-start))/1000)+" sec";
	}
}






























