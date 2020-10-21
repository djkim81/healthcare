package com.skt.hrs.cmmn.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.skt.hrs.cmmn.vo.CspVo;
import com.skt.hrs.cmmn.vo.ScheduleVo;

@Repository("scheduleDAO")
public class ScheduleDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(ScheduleDAO.class);
	
	@Resource(name="sqlSession")
	private SqlSession sqlSession;
	
	/**
	 * 
	 * @설명 : 아웃룩 일정 등록 대상 insert(예약등록)
	 * @작성일 : 2019.10.14
	 * @작성자 : 김대종
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public boolean insertScheduleSend(ScheduleVo vo) {
		int result = sqlSession.insert("cmmnScheduleMapper.insertScheduleSend", vo);
		if(result == 1) {
			return true;
		}
		return false;
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
	public boolean updateScheduleCancel(ScheduleVo vo) {
		int result = sqlSession.insert("cmmnScheduleMapper.updateScheduleCancel", vo);
		if(result == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @설명 : 아웃룩 일정 등록 대상 select 
	 * @작성일 : 2019.10.14
	 * @작성자 : 김대종
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public List selectScheduleSendList() {
		return sqlSession.selectList("cmmnScheduleMapper.selectScheduleSendList");
	}
	
	/**
	 * 
	 * @설명 : 아웃룩 일정 취소 대상 select 
	 * @작성일 : 2019.10.14
	 * @작성자 : 김대종
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public List selectScheduleCancelList() {
		return sqlSession.selectList("cmmnScheduleMapper.selectScheduleCancelList");
	}
	
	/**
	 * 
	 * @설명 : 아웃룩 일정 등록 요청 후 결과 update 
	 * @작성일 : 2019.10.14
	 * @작성자 : 김대종
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public boolean updateScheduleSendLog(ScheduleVo vo) {
		int result = sqlSession.update("cmmnScheduleMapper.updateScheduleSendLog", vo);
		if(result == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @설명 : 아웃룩 일정 취소 요청 후 결과 update 
	 * @작성일 : 2019.10.14
	 * @작성자 : 김대종
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public boolean updateScheduleCancelLog(ScheduleVo vo) {
		int result = sqlSession.update("cmmnScheduleMapper.updateScheduleCancelLog", vo);
		if(result == 1) {
			return true;
		}
		return false;
	}
	
	
	
}
