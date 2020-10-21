package com.skt.hrs.cmmn.vo;

import java.util.Date;

public class ScheduleVo {
	
	private int no; //일련변호
	private String resveEmpno; //예약자 사번
	private int resveNo; //예약번호
	private String resveTm; //예약시간
	private String bldCode; //사옥코드
	private String sendYn; // 일정등록성공여부
	private Date sendDt; //일정등록일시
	private String oId; //일정등록ID(일정등록요청 후 리턴ID)
	private String oChangekey; //일정등록Key(일정등록요청 후 리턴Key)
	private String resveCancl; //예약취소여부
	private String canclYn; //일정취소여부
	private Date canclDt; //일정취소일시 
	private String sendRslt; //일정등록전송결과
	private String canclRslt; //일정취소전송결과
	private Date regDt; //저장일시
	private Date updtDt; //수정일시
	private int sendCnt; //등록재시도횟수
	private int canclCnt; //취소재시도횟수
	
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getResveEmpno() {
		return resveEmpno;
	}
	public void setResveEmpno(String resveEmpno) {
		this.resveEmpno = resveEmpno;
	}
	public int getResveNo() {
		return resveNo;
	}
	public void setResveNo(int resveNo) {
		this.resveNo = resveNo;
	}
	public String getResveTm() {
		return resveTm;
	}
	public void setResveTm(String resveTm) {
		this.resveTm = resveTm;
	}
	public String getBldCode() {
		return bldCode;
	}
	public void setBldCode(String bldCode) {
		this.bldCode = bldCode;
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
	public String getoId() {
		return oId;
	}
	public void setoId(String oId) {
		this.oId = oId;
	}
	public String getoChangekey() {
		return oChangekey;
	}
	public void setoChangekey(String oChangekey) {
		this.oChangekey = oChangekey;
	}
	public String getResveCancl() {
		return resveCancl;
	}
	public void setResveCancl(String resveCancl) {
		this.resveCancl = resveCancl;
	}
	public String getCanclYn() {
		return canclYn;
	}
	public void setCanclYn(String canclYn) {
		this.canclYn = canclYn;
	}
	public Date getCanclDt() {
		return canclDt;
	}
	public void setCanclDt(Date canclDt) {
		this.canclDt = canclDt;
	}
	public String getSendRslt() {
		return sendRslt;
	}
	public void setSendRslt(String sendRslt) {
		this.sendRslt = sendRslt;
	}
	public String getCanclRslt() {
		return canclRslt;
	}
	public void setCanclRslt(String canclRslt) {
		this.canclRslt = canclRslt;
	}
	public Date getRegDt() {
		return regDt;
	}
	public void setRegDt(Date regDt) {
		this.regDt = regDt;
	}
	public Date getUpdtDt() {
		return updtDt;
	}
	public void setUpdtDt(Date updtDt) {
		this.updtDt = updtDt;
	}
	public int getSendCnt() {
		return sendCnt;
	}
	public void setSendCnt(int sendCnt) {
		this.sendCnt = sendCnt;
	}
	public int getCanclCnt() {
		return canclCnt;
	}
	public void setCanclCnt(int canclCnt) {
		this.canclCnt = canclCnt;
	}
	
	
	
	
}
