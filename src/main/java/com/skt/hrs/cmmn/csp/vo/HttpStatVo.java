package com.skt.hrs.cmmn.csp.vo;

public class HttpStatVo {
	private	int			status;
	private	String		rtnText;
	private	String		exTyp;
	private	Throwable	ex;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRtnText() {
		return rtnText;
	}

	public void setRtnText(String rtnText) {
		this.rtnText = rtnText;
	}

	public String getExTyp() {
		if(exTyp==null) return "";
		else return exTyp;
	}

	public void setExTyp(String exTyp) {
		this.exTyp = exTyp;
	}

	public Throwable getEx() {
		return ex;
	}

	public void setEx(Throwable ex) {
		this.ex = ex;
	}
}
