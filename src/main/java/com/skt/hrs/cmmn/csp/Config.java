package com.skt.hrs.cmmn.csp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {
	
	// Domain
	//@Value("${CSP.HOST}")
	public static String domain;

	// BP API Key
	//@Value("${CSP.API_KEY}")
	public static String apiKey;

	// BP 복호화 키
	//@Value("${CSP.DEC_KEY}")
	public static String decryptKey;

	// Encode
	//@Value("${CSP.ENCODE}")
	public static String encode;
	
	@Value("#{prop['CSP.HOST']}")
	public void setDomain(String domain) {
		Config.domain = domain;
	}

	@Value("#{prop['CSP.API_KEY']}")
	public void setApiKey(String apiKey) {
		Config.apiKey = apiKey;
	}
	
	@Value("#{prop['CSP.DEC_KEY']}")
	public void setDecryptKey(String decryptKey) {
		Config.decryptKey = decryptKey;
	}
	
	@Value("#{prop['CSP.ENCODE']}")
	public void setEncode(String encode) {
		Config.encode = encode;
	}
}
