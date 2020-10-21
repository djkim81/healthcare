package com.skt.hrs.cmmn.dao;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.pub.core.entity.DataEntity;


@Repository("codeDAO")
public class CodeDAO {

	private static final Logger logger = LoggerFactory.getLogger(CodeDAO.class);


	@Resource(name="sqlSession")
	private SqlSession sqlSession;
	

	/**
	 * @설명 : 공통코드 조회
	 * @작성일 : 2019.08.27
	 * @작성자 : P149365
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public List selectCodeList(DataEntity param) {
		return sqlSession.selectList("cmmnCodeMapper.selectCodeList", param);
	}
	
	/**
	 * @설명 : 공통코드 전체 조회
	 * @작성일 : 2019.08.30
	 * @작성자 : LEE.J.H
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public List selectAllCodeList(DataEntity param) {
		return sqlSession.selectList("cmmnCodeMapper.selectAllCodeList", param);
	}

	/**
	 * @설명 : 공통 코드 종류 조회
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public List selectCodeKindList(DataEntity param) {
		return sqlSession.selectList("cmmnCodeMapper.selectCodeKindList", param);
	}
	
	/**
	 * @설명 : 공통코드 리스트 조회
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public List selectCodeManageList(DataEntity param) {
		return sqlSession.selectList("cmmnCodeMapper.selectCodeManageList", param);
	}
	
	/**
	 * @설명 : 공통코드 리스트 TOTAL COUNT 조회
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public int selectCodeManageListTotalCount(DataEntity param) {
		return sqlSession.selectOne("cmmnCodeMapper.selectCodeManageListTotalCount", param);
	}
	
	/**
	 * @설명 : 공통코드 등록
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public boolean insertCodeManage(DataEntity param) {
		int result = sqlSession.insert("cmmnCodeMapper.insertCodeManage", param);
		if(result == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * @설명 : 공통코드 목록 논리 삭제
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public boolean updateDelYCodeManageList(DataEntity param) {
		int result = sqlSession.update("cmmnCodeMapper.updateDelYCodeManageList", param);
		if(result == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * @설명 : 공통코드 수정
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public boolean updateCodeManageModify(DataEntity param) {
		int result = sqlSession.update("cmmnCodeMapper.updateCodeManageModify", param);
		if(result == 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * @설명 : 공통코드 중복 COUNT 조회
	 * @작성일 : 2019.10.11
	 * @작성자 : P150114
	 * @param param
	 * @return
	 * @변경이력 :
	 */
	public int selectCodeManageDuplicateCount(DataEntity param) {
		return sqlSession.selectOne("cmmnCodeMapper.selectCodeManageDuplicateCount", param);
	}
	
}
