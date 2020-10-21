package com.skt.hrs.resve.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.pub.core.entity.DataEntity;


@Repository("resveMgmtDAO")
public class ResveMgmtDAO {

	private static final Logger logger = LoggerFactory.getLogger(ResveMgmtDAO.class);


	@Resource(name="sqlSession")
	private SqlSession sqlSession;
	
	

	/**
	 * 
	 * @설명 : 관리자 예약 현황 조회 
	 * @작성일 : 2019.09.23
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public List selectResveMgmtList(DataEntity param) {
		return sqlSession.selectList("resveMgmtMapper.selectResveMgmtList", param);
	}
	
	
	/**
	 * 
	 * @설명 : 관리자 예약 현황 TOTAL COUNT 조회 
	 * @작성일 : 2019.08.30
	 * @작성자 : LEE.J.H
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public int selectResveMgmtListTotalCount(DataEntity param) {
		return sqlSession.selectOne("resveMgmtMapper.selectResveMgmtListTotalCount", param);
	}
	
	/**
	 * 
	 * @설명 : 예약 단건조회 
	 * @작성일 : 2019.10.15
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public Map selectResveMgmtItem(DataEntity param) {
		return sqlSession.selectOne("resveMgmtMapper.selectResveMgmtItem", param);
	}
	
	/**
	 * 
	 * @설명 : 관리자 예약 현황 상세조회
	 * @작성일 : 2019.09.23
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public List selectResveMgmtDetailList(DataEntity param) {
		return sqlSession.selectList("resveMgmtMapper.selectResveMgmtDetailList", param);
	}
	
	/**
	 * 
	 * @설명 : 관리자 예약 현황 엑셀용 조회
	 * @작성일 : 2019.10.16
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public List selectResveMgmtListExcel(DataEntity param) {
		return sqlSession.selectList("resveMgmtMapper.selectResveMgmtListExcel", param);
	}
		
}
