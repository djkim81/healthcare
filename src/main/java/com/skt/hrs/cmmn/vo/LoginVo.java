package com.skt.hrs.cmmn.vo;

import java.io.Serializable;

public class LoginVo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String empno;	//사번
	private String hname;	//성명	
	private String auth;	//권한 (시스템관리자,SKT 담당, 자회사담당, 구성원)
	private String tSex;	//성별
	private String place;	//근무장소코드
	private String hrsAgree;	//(급여공제)동의여부
	
	public String getEmpno() {
		return empno;
	}
	public void setEmpno(String empno) {
		this.empno = empno;
	}
	public String getHname() {
		return hname;
	}
	public void setHname(String hname) {
		this.hname = hname;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public String gettSex() {
		return tSex;
	}
	public void settSex(String tSex) {
		this.tSex = tSex;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getHrsAgree() {
		return hrsAgree;
	}
	public void setHrsAgree(String hrsAgree) {
		this.hrsAgree = hrsAgree;
	}
	
	@Override
	public String toString() {
		return "[empno=" + empno + ", hname=" + hname + ", auth=" + auth + ", tSex=" + tSex + ", place=" + place
				+ ", hrsAgree=" + hrsAgree + "]";
	}	
	
	
	
}
