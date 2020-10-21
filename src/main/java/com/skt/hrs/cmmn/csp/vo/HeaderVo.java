package com.skt.hrs.cmmn.csp.vo;

public class HeaderVo {
	public String	RESULT;
	public String	RESULT_CODE;
	public String	RESULT_MESSAGE;
	public String	PLATFORM_MODUS;
	public String	TEMP_ENCRYPT_KEY;

	public String getRESULT() {
		return RESULT;
	}

	public void setRESULT(String rESULT) {
		RESULT = rESULT;
	}

	public String getRESULT_CODE() {
		return RESULT_CODE;
	}

	public void setRESULT_CODE(String rESULT_CODE) {
		RESULT_CODE = rESULT_CODE;
	}

	public String getRESULT_MESSAGE() {
		return RESULT_MESSAGE;
	}

	public void setRESULT_MESSAGE(String rESULT_MESSAGE) {
		RESULT_MESSAGE = rESULT_MESSAGE;
	}

	public String getPLATFORM_MODUS() {
		return PLATFORM_MODUS;
	}

	public void setPLATFORM_MODUS(String pLATFORM_MODUS) {
		PLATFORM_MODUS = pLATFORM_MODUS;
	}

	public String getTEMP_ENCRYPT_KEY() {
		return TEMP_ENCRYPT_KEY;
	}

	public void setTEMP_ENCRYPT_KEY(String tEMP_ENCRYPT_KEY) {
		TEMP_ENCRYPT_KEY = tEMP_ENCRYPT_KEY;
	}

//	@Override
	public String toString() {
		return "HeaderVo [RESULT=" + RESULT + ", RESULT_CODE=" + RESULT_CODE
				+ ", RESULT_MESSAGE=" + RESULT_MESSAGE + ", PLATFORM_MODUS="
				+ PLATFORM_MODUS + ", TEMP_ENCRYPT_KEY=" + TEMP_ENCRYPT_KEY
				+ "]";
	}
}
