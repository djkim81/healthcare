package com.skt.hrs.cmmn.contants;

public enum CspContents {
	
	SMS("SCBH000001", "SMS")
	,MAIL("SCBH000005", "MAIL")
	,MEMO("SCBH000006", "MEMO");
	
	private String keyValue;
	private String KeyStr;
	
	private CspContents(String keyValue, String KeyStr) {
		this.keyValue = keyValue;
		this.KeyStr = KeyStr;
		
	}

	public String value() { 
		return keyValue;
	}
	
	public String keyStrValue() { 
		return KeyStr;
	}
}

