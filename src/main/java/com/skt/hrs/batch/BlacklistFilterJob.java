package com.skt.hrs.batch;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.skt.hrs.resve.service.ResveBlacklistService;
import com.skt.hrs.resve.service.ResveStatusService;

public class BlacklistFilterJob {
	
	@Autowired
	ResveBlacklistService blacklistService;
	
	@Autowired
	ResveStatusService resveStatusService;
	
	@Value("#{prop['PANELTY.WEEKS']}")
	private int paneltyWeeks;

	Logger logger = LoggerFactory.getLogger("blacklistFilterJobLogger");
	
	protected void execute() {	
		String opt = System.getProperty("hrs.batch");
		if(opt != null && opt.equals("true")) {
			try {
					
				// 블랙리스트 저장
				int cnt = blacklistService.insertBlacklist(paneltyWeeks).getItem();			
				logger.info("===> {} {} : {} " ,
						BlacklistFilterJob.class.getSimpleName(), 
						"필터링된 블랙리스트 카운트", 
						cnt);
				
				// 노쇼 이력 저장
				List noshowList = resveStatusService.insertNoShowHist().getList();			
				logger.info("===> {} {} : {} " ,
						BlacklistFilterJob.class.getSimpleName(), 
						"노쇼이력 저장 카운트", 
						noshowList.size());
				
			}catch(Exception e) {
				logger.error("{} BlacklistFilter Error Message : {} ", BlacklistFilterJob.class.getSimpleName(), e.getMessage());
			}
		}
	}
}


