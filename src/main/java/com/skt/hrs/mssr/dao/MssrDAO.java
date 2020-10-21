package com.skt.hrs.mssr.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.pub.core.entity.DataEntity;


@Repository("mssrDAO")
public class MssrDAO {

	private static final Logger logger = LoggerFactory.getLogger(MssrDAO.class);


	@Resource(name="sqlSession")
	private SqlSession sqlSession;
	
	/**
	 * 
	 * @설명 : 관리사 콤보 목록 조회 
	 * @작성일 : 2019.09.04
	 * @작성자 : LEE.J.H
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public List getMssrList(DataEntity param) {
		return sqlSession.selectList("mssrMapper.getMssrList", param);
	}
	
	
	
	/**
	 * 
	 * @설명 : 관리사 스케쥴 리스트 조회 
	 * @작성일 : 2019.09.05
	 * @작성자 : LEE.J.H
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public List selectScheduleList(DataEntity param) {
		return sqlSession.selectList("mssrMapper.selectScheduleList", param);
	}
	
	
	
	/**
	 * 
	 * @설명 : 관리사 스케쥴 리스트 TOTAL COUNT 조회 
	 * @작성일 : 2019.09.05
	 * @작성자 : LEE.J.H
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public int selectScheduleListTotalCount(DataEntity param) {
		return sqlSession.selectOne("mssrMapper.selectScheduleListTotalCount", param);
	}

	/**
	 * 
	 * @설명 : 관리사 스케쥴 등록 
	 * @작성일 : 2019.09.10
	 * @작성자 : P150113
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public boolean insertSchedule(DataEntity param) {
		 int result = sqlSession.insert("mssrMapper.insertSchedule", param);
		if(result == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @설명 : 관리사 스케쥴 상태 등록 
	 * @작성일 : 2019.09.10
	 * @작성자 : P150113
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public boolean insertResveHist(DataEntity param) {
		 int result = sqlSession.insert("mssrMapper.insertResveHist", param);
		if(result == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @설명 : 관리사 스케쥴 삭제 변경
	 * @작성일 : 2019.09.10
	 * @작성자 : P150113
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public boolean deleteResve(DataEntity param) {
		int result = sqlSession.update("mssrMapper.deleteResve", param);
		if(result == 1) {
			return true;
		}
		return false;
	}

	
	/**
	 * 
	 * @설명 : 관리사 스케쥴 상세 조회 
	 * @작성일 : 2019.09.05
	 * @작성자 : LEE.J.H
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public List selectScheduleDetail(DataEntity param) {
		return sqlSession.selectList("mssrMapper.selectScheduleDetail", param);
	}
	
	/**
	 * 
	 * @설명 : 관리사 스케쥴 상세 조회 
	 * @작성일 : 2019.09.27
	 * @작성자 : P150113
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public Map selectResveItem(DataEntity param) {
		return sqlSession.selectOne("mssrMapper.selectResveItem", param);
	}
	/**
	 * 
	 * @설명 : 관리사 관리화면 SMS 정보 
	 * @작성일 : 2019.09.27
	 * @작성자 : LEE.J.H
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public Map selectSmsInfoGet(DataEntity param) {
		return sqlSession.selectOne("mssrMapper.selectSmsInfoGet", param);
	}
	
	
	
	/**
	 * 
	 * @설명 : 관리사 스케쥴 중복 예약 체크 
	 * @작성일 : 2019.09.20
	 * @작성자 : P150113
	 * @param param
	 * @return 10: 에약일 시간관리시 예약여부 , 20:예약일 시간 BED 사용여부 
	 * @변경이력 :
 	 */
	public Map selectResveCheck(DataEntity param) {
		return sqlSession.selectOne("mssrMapper.selectResveCheck", param);
	}
	
	/**
	 * 
	 * @설명 : 휴일체크
	 * @작성일 : 2019.10.18
	 * @작성자 : P150113
	 * @param param
	 * @return count
	 * @변경이력 :
	 * */
	public Map selectRestCheck(DataEntity param) { 
		  return sqlSession.selectOne("mssrMapper.selectRestCheck", param); 
	  }
	
}
