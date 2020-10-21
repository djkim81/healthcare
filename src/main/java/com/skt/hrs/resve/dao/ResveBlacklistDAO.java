package com.skt.hrs.resve.dao;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.pub.core.entity.DataEntity;


@Repository("resveBlacklistDAO")
@SuppressWarnings({"unused"})
public class ResveBlacklistDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(ResveBlacklistDAO.class);


	@Resource(name="sqlSession")
	private SqlSession sqlSession;
	
	
	/**
	 * 
	 * @설명 : 블랙리스트 등록 
	 * @작성일 : 2019.09.09
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public int insertBlacklist(int weeks) {
		return sqlSession.insert("resveBlacklistMapper.insertBlacklist", weeks);
	}
	
	/**
	 * 
	 * @설명 : 사번으로 블랙리스트대상인지 확인 
	 * @작성일 : 2019.09.09
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public Map selectBlacklistByEmpno(DataEntity param) {
		return sqlSession.selectOne("resveBlacklistMapper.selectBlacklistByEmpno", param);
	}
		
}
