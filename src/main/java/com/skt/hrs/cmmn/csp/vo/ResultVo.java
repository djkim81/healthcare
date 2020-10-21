package com.skt.hrs.cmmn.csp.vo;

public class ResultVo {
	public HeaderVo	HEADER;
	public Object	BODY;

	public HeaderVo getHEADER() {
		return HEADER;
	}

	public void setHEADER(HeaderVo hEADER) {
		HEADER = hEADER;
	}

	public Object getBODY() {
		return BODY;
	}

	public void setBODY(Object bODY) {
		BODY = bODY;
	}

//	@Override
	public String toString() {
		return "ResultVo [HEADER=" + HEADER + ", BODY=" + BODY + "]";
	}
}
