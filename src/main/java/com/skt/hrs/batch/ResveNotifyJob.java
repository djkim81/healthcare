package com.skt.hrs.batch;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.pub.core.entity.ResponseResult;
import com.skt.hrs.cmmn.service.CspService;
import com.skt.hrs.resve.service.ResveStatusService;

public class ResveNotifyJob {

	@Autowired
	ResveStatusService resveStatusService;
	
	@Autowired
	CspService cspService;
	
	
	@Autowired
	MessageSource messageSource;
	
	Logger logger = LoggerFactory.getLogger("resveNotiJobLogger");
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void execute() {
		String opt = System.getProperty("hrs.batch");
		if(opt != null && opt.equals("true")) {
			try {
				
				List<Map> list = resveStatusService.selectResveNotifyList().getList();
				logger.info("케어 30분전 알림 Job 수행 시작 - " + list.size() + "건");
				for(Map m : list) {
					logger.info("일자:{}, 시간:{}, 사옥:{}, 베드:{}, 사번:{} ",
							m.get("RESVE_DE"),
							m.get("RESVE_TM"),
							m.get("BLD_CODE"),
							m.get("BED_CODE"),
							m.get("RESVE_EMPNO"));
					
					m.put("targetEmpno", m.get("RESVE_EMPNO"));							
					
					ResponseResult insertResult = cspService.insertCspSMS(m, "csp.sms.resveNotify", Locale.KOREAN);
					logger.info("CSP테이블 INSERT 결과 : "+ insertResult.getItem());
				}
				
			}catch(Exception e) {
				logger.error("{} Reservation Notify Job Error Message : {} ", ResveNotifyJob.class.getSimpleName(), e.getMessage());
			}
		}
	}
}
