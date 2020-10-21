package com.skt.hrs.cmmn.vo;

import java.util.Date;

import com.skt.hrs.utils.StringUtil;

public class CspVo {

	private int no;				//일련번호
	private String sendEmpno;	//발신자사번
	private String rcvEmpno;	//수신자사번
	private String cspType;		//CSP타입 S:sms M:메일
	private int resveNo;		//예약번호
	private String mssgHead;	//메세지 헤드
	private String mssgBody;	//메세지 내용
	private String sendYn;		//발신여부
	private Date sendDt;		//발신일시
	private String sendRslt;	//발신결과
	private Date regDt;			//등록일시
	private int retryCnt;		//발신재시도 횟수
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getSendEmpno() {
		return sendEmpno;
	}
	public void setSendEmpno(String sendEmpno) {
		this.sendEmpno = sendEmpno;
	}
	public String getRcvEmpno() {
		return rcvEmpno;
	}
	public void setRcvEmpno(String rcvEmpno) {
		this.rcvEmpno = rcvEmpno;
	}
	public String getCspType() {
		return cspType;
	}
	public void setCspType(String cspType) {
		this.cspType = cspType;
	}
	public int getResveNo() {
		return resveNo;
	}
	public void setResveNo(int resveNo) {
		this.resveNo = resveNo;
	}
	public String getMssgHead() {
		return mssgHead;
	}
	public void setMssgHead(String mssgHead) {
		this.mssgHead = mssgHead;
	}
	public String getMssgBody() {		
		return mssgBody;
	}
	public void setMssgBody(String mssgBody) {
//		if(this.cspType.equals("SMS")) {
//			mssgBody = StringUtil.substringByte(mssgBody, 80);
//		}
		this.mssgBody = mssgBody;
	}
	public String getSendYn() {
		return sendYn;
	}
	public void setSendYn(String sendYn) {
		this.sendYn = sendYn;
	}
	public Date getSendDt() {
		return sendDt;
	}
	public void setSendDt(Date sendDt) {
		this.sendDt = sendDt;
	}
	public String getSendRslt() {
		return sendRslt;
	}
	public void setSendRslt(String sendRslt) {
		this.sendRslt = sendRslt;
	}
	public Date getRegDt() {
		return regDt;
	}
	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}
	public int getRetryCnt() {
		return retryCnt;
	}
	public void setRetryCnt(int retryCnt) {
		this.retryCnt = retryCnt;
	}
	
	
}
