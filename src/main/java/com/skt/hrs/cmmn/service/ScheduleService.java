package com.skt.hrs.cmmn.service;

import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.pub.core.entity.ResponseResult;
import com.skt.hrs.cmmn.dao.ScheduleDAO;
import com.skt.hrs.cmmn.vo.CspVo;
import com.skt.hrs.cmmn.vo.ScheduleVo;

@Service("scheduleService")
public class ScheduleService {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name="scheduleDAO")
	private ScheduleDAO scheduleDAO;
	
	@Resource(name="messageSource")
	MessageSource messageSource;

	/**
	 * 
	 * @설명 : 아웃룩 일정 등록 insert(예약등록)
	 * @작성일 : 2019.10.14
	 * @작성자 : 김대종
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult insertScheduleSend(Map param) {
		ResponseResult result = new ResponseResult();
		
		ScheduleVo scheduleVo = new ScheduleVo();
		scheduleVo.setResveEmpno(param.get("targetEmpno").toString());
		scheduleVo.setResveNo(Integer.parseInt(param.get("RESVE_NO").toString()));
		scheduleVo.setResveTm(param.get("RESVE_TM").toString());
		scheduleVo.setBldCode(param.get("BLD_CODE").toString());
		
		result.setItemOne(scheduleDAO.insertScheduleSend(scheduleVo));
		return result;
	}
	
	/**
	 * 
	 * @설명 : 아웃룩 일정 취소 update(예약취소)
	 * @작성일 : 2019.10.14
	 * @작성자 : 김대종
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult updateScheduleCancel(Map param) {
		ResponseResult result = new ResponseResult();
		
		ScheduleVo scheduleVo = new ScheduleVo();
		
		scheduleVo.setResveEmpno(param.get("targetEmpno").toString());
		scheduleVo.setResveNo(Integer.parseInt(param.get("RESVE_NO").toString()));
		scheduleVo.setResveCancl("Y");
		
		
		result.setItemOne(scheduleDAO.updateScheduleCancel(scheduleVo));
		return result;
	} 
	

	/**
	  * 
	 * @설명 : 아웃룩 일정 등록 대상 리스트
	 * @작성일 : 2019.10.14
	 * @작성자 : 김대종
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult selectScheduleSendList(){
		ResponseResult result = new ResponseResult();
		result.setItemList(scheduleDAO.selectScheduleSendList());
		return result;
	}
	
	/**
	  * 
	 * @설명 : 아웃룩 일정 취소 대상 리스트
	 * @작성일 : 2019.10.14
	 * @작성자 : 김대종
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult selectScheduleCancelList(){
		ResponseResult result = new ResponseResult();
		result.setItemList(scheduleDAO.selectScheduleCancelList());
		return result;
	}
	
	/**
	  * 
	 * @설명 : 아웃룩 일정 등록 결과 update
	 * @작성일 : 2019.10.14
	 * @작성자 : 김대종
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult updateScheduleSendLog(ScheduleVo scheduleVo) {
		ResponseResult result = new ResponseResult();
		result.setItemOne(scheduleDAO.updateScheduleSendLog(scheduleVo));
		return result;
	}
	
	/**
	  * 
	 * @설명 : 아웃룩 일정 취소 결과 update
	 * @작성일 : 2019.10.14
	 * @작성자 : 김대종
	 * @return
	 * @변경이력 :
	 */
	public ResponseResult updateScheduleCancelLog(ScheduleVo scheduleVo) {
		ResponseResult result = new ResponseResult();
		result.setItemOne(scheduleDAO.updateScheduleCancelLog(scheduleVo));
		return result;
	}
}
