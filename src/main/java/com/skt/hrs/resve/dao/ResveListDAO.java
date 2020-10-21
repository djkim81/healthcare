package com.skt.hrs.resve.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.pub.core.entity.DataEntity;


@Repository("resveListDAO")
public class ResveListDAO {

	private static final Logger logger = LoggerFactory.getLogger(ResveListDAO.class);


	@Resource(name="sqlSession")
	private SqlSession sqlSession;
	
	

	/**
	 * 
	 * @설명 : 예약현황리스트 조회 
	 * @작성일 : 2019.08.29
	 * @작성자 : LEE.J.H
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public List selectResveList(DataEntity param) {
		return sqlSession.selectList("resveListMapper.selectResveList", param);
	}
	
	
	/**
	 * 
	 * @설명 : 예약현황리스트 TOTAL COUNT 조회 
	 * @작성일 : 2019.08.30
	 * @작성자 : LEE.J.H
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public int selectResveListTotalCount(DataEntity param) {
		return sqlSession.selectOne("resveListMapper.selectResveListTotalCount", param);
	}
	
	/**
	 * 
	 * @설명 : 예약 상세현황 조회
	 * @작성일 : 2019.09.18
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public List selectResveDetailList(DataEntity param) {
		return sqlSession.selectList("resveListMapper.selectResveDetailList", param);
	}
	
	
}
