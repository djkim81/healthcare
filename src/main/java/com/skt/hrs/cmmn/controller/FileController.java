package com.skt.hrs.cmmn.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.skt.hrs.utils.DownloadView;

@Controller
@RequestMapping(value = "/")
public class FileController {
	
	@Value("#{prop['FILE.DOWNLOAD.PATH']}")
	private String fileDownloadPath;
	
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@RequestMapping(value = "/fileDownload")
	public  void fileDownload(
			  @RequestParam("document_nm") String document_nm
			, HttpSession sess
			, HttpServletRequest req
			, HttpServletResponse res
			, ModelAndView mav) throws Exception 
			{
				try {
					//String filePath ="C:\\Source\\JAVA\\HRS\\hrs\\src\\main\\webapp\\resources\\manual\\";
					String fileRealName = "hrsguide.pdf";
					String fileViewName = "사용자가이드.pdf";
					
					DownloadView fileDown = new DownloadView(); //파일다운로드 객체생성
					fileDown.filDown(req, res, fileDownloadPath , fileRealName, fileViewName); //파일다운로드 
		
					
				} catch (Exception e) {
					e.printStackTrace();
					logger.error(e.toString());
				}
			}
}
