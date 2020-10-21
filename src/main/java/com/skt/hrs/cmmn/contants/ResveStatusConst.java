package com.skt.hrs.cmmn.contants;

public abstract interface ResveStatusConst {

	/**
	 * 화면에 표시되는 상태값
	 * @author P149365
	 *
	 */
	public static enum VIEWSTATUS{
		RESVE_POSBL,	//예약가능
		RESVE_COMPT,	//예약완료
		RESVE_IMPRTY,	//예약불가		
		WAIT_POSBL,		//대기가능
		WAIT,			//대기중
		RESVE_CANCL,	//예약취소
		WAIT_CANCL,		//대기취소
		COMPT,			//완료
		NOSHOW_COMPT,	//노쇼 당일 확인처리 가능(확인처리를 하지않으면 패널티)
		NOSHOW,			//노쇼
		RESVE_IMPRTY_FULL,	//예약불가(예약/대기가 모두 찬 경우)
		RESVE_IMPRTY_SEX	//예약불가(남성이 여성관리자선택불가)
	}
	
	/**
	 * 내부적으로 사용되는 상태값
	 * @author P149365
	 *
	 */
	public static enum DBSTATUS{
		WORK("STS00"),			//근무
		RESVE_COMPT("STS01"),	//예약완료
		RESVE_CANCL("STS02"),	//예약취소
		WAIT_COMPT("STS03"),	//대기완료
		WAIT_CANCL("STS04"),	//대기취소
		COMPT("STS05"),			//(케어)완료
		NOSHOW("STS06"),		//노쇼
		NOSHOW_NOPANELTY("STS07"),		//노쇼패널티없음
		WORK_CANCL("STS99");	//근무취소
		
		private String code = "";
		
		private DBSTATUS(String code) {
			this.code = code;
		}
		
		public String toString() {
			return this.code;
		}
	}
}
