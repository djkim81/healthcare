package com.skt.hrs.resve.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.pub.core.entity.DataEntity;
import com.skt.hrs.cmmn.exception.HrsException;


@Repository("resveConfirmDAO")
@SuppressWarnings({"rawtypes", "unused"})
public class ResveConfirmDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(ResveConfirmDAO.class);


	@Resource(name="sqlSession")
	private SqlSession sqlSession;
	

	/**
	 * 
	 * @설명 : 근무현황 조회 
	 * @작성일 : 2019.08.29
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */	
	public List selectWorkList(DataEntity param) {
		return sqlSession.selectList("resveConfirmMapper.selectWorkList", param);
	}
	
	/**
	 * 
	 * @설명 : 완료처리 대상 예약조회 
	 * @작성일 : 2019.09.11
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public Map selectConfirmTarget(DataEntity param) {
		return sqlSession.selectOne("resveConfirmMapper.selectConfirmTarget", param);
	}
	
}
