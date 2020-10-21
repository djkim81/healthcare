package com.skt.hrs.resve.view;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.pub.core.entity.DataEntity;
import com.pub.core.util.HttpUtil;
import com.skt.hrs.resve.service.ResveMgmtService;
import com.skt.hrs.utils.DownloadView;
import com.skt.hrs.utils.StringUtil;

public class ResveMgmtExcelView extends AbstractXlsxView {

	@Autowired
	ResveMgmtService resveMgmtService;
	
	@Override
	protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		DataEntity param = HttpUtil.getServletRequestParam(request);
		
        String now = new SimpleDateFormat("yyyyMMddHHmm", Locale.KOREA).format(new Date());
     
        String excelName = "(헬스케어)예약현황_" + now + ".xlsx";
        Sheet worksheet = null;
        Row row = null;
        
        final int ROW_DOCUMENT_TITLE = 1;
        final int ROW_SEARCH_CONDITION = 3;
        final int ROW_HEADER = 4;
        final int ROW_LIST_START = 6;
        
        
        /****************
         *  폰트 정의
         ****************/
        // 문서제목 폰트
        Font documentTitleFont = (Font) workbook.createFont();
        documentTitleFont.setBold(true);
        documentTitleFont.setFontHeightInPoints((short) 14);
        
        // 헤더폰트
        Font headerFont = (Font) workbook.createFont();
        headerFont.setBold(true);
        
        
        /****************
         *  스타일 정의
         ****************/
        // 문서제목 스타일
        CellStyle documentTitleStyle = workbook.createCellStyle();
        documentTitleStyle.setAlignment(HorizontalAlignment.CENTER);
        documentTitleStyle.setFont(documentTitleFont);
        
        // 헤더 스타일
        CellStyle headerStyle = workbook.createCellStyle(); 
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LEMON_CHIFFON.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        
        //본문 가운데정렬 스타일
        CellStyle centerStyle = workbook.createCellStyle(); 
        centerStyle.setAlignment(HorizontalAlignment.CENTER);
        
        /****************
         *  Sheet 생성
         ****************/
        // 새로운 sheet를 생성한다.
        worksheet = workbook.createSheet("예약현황");
        
        /****************
         *  Cell 넓이 설정
         ****************/
        worksheet.setColumnWidth(0, 3000);	//예약일
        worksheet.setColumnWidth(1, 3000);	//예약시간
        worksheet.setColumnWidth(2, 3000);	//사옥
        worksheet.setColumnWidth(3, 3000);	//헬스키퍼
        worksheet.setColumnWidth(4, 2000);	//베드
        worksheet.setColumnWidth(5, 4000);	//진행상태
        worksheet.setColumnWidth(6, 3000);	//예약자사번
        worksheet.setColumnWidth(7, 3000);	//예약자성명
        worksheet.setColumnWidth(8, 3000);	//예약자부서명
        worksheet.setColumnWidth(9, 4000);	//신청일시
        worksheet.setColumnWidth(10, 2000);	//급여공제동의여부
        
        worksheet.setColumnWidth(11, 3000);	//대기자사번
        worksheet.setColumnWidth(12, 3000);	//대기자성명
        worksheet.setColumnWidth(13, 3000);	//대기자부서명
        worksheet.setColumnWidth(14, 4000);	//신청일시
        worksheet.setColumnWidth(15, 2000);	//급여공제동의여부
        
        /****************
         *  ROW 생성
         ****************/
        List<Map> list = (List<Map>) model.get("list");
         
        // 문서제목
        row = worksheet.createRow(ROW_DOCUMENT_TITLE);
        row.createCell(0).setCellValue("예약 목록");
        row.getCell(0).setCellStyle(documentTitleStyle);
        worksheet.addMergedRegion(new CellRangeAddress(ROW_DOCUMENT_TITLE, ROW_DOCUMENT_TITLE, 0, 13));	// 셀 병합 CellRangeAddress(시작 행, 끝 행, 시작 열, 끝 열)
         
        
        // 검색조건
        row = worksheet.createRow(ROW_SEARCH_CONDITION);
        String searchCondition = String.format("기간: %s ~ %s", param.getString("fromDate"), param.getString("toDate"));
        if(!StringUtil.isEmpty(param.getString("bldCode"))) {
        	searchCondition += ",  사옥: " + param.getString("bldNm");
        }
        if(!StringUtil.isEmpty(param.getString("bedCode"))) {
        	searchCondition += ",  베드: " + param.getString("bedNm");
        }
        if(!StringUtil.isEmpty(param.getString("statusCode"))) {
        	searchCondition += ",  상태: " + param.getString("statusNm");
        }
        if(!StringUtil.isEmpty(param.getString("empnm"))) {
        	searchCondition += ",  이름: " + param.getString("empnm");
        }
        row.createCell(0).setCellValue(searchCondition);
        
        // 헤더 설정
        row = worksheet.createRow(ROW_HEADER);
        
        row.createCell(0).setCellValue("예약일");
        row.createCell(1).setCellValue("예약시간");
        row.createCell(2).setCellValue("사옥");
        row.createCell(3).setCellValue("헬스키퍼");
        row.createCell(4).setCellValue("베드");
        row.createCell(5).setCellValue("진행상태");
        row.createCell(6).setCellValue("예 약");
        row.createCell(7).setCellValue("");
        row.createCell(8).setCellValue("");
        row.createCell(9).setCellValue("");
        row.createCell(10).setCellValue("");
        row.createCell(11).setCellValue("대 기");
        row.createCell(12).setCellValue("");
        row.createCell(13).setCellValue("");
        row.createCell(14).setCellValue("");
        row.createCell(15).setCellValue("");
        
        
        Iterator<Cell> cellIter = row.cellIterator();        
        while(cellIter.hasNext()) {
        	cellIter.next().setCellStyle(headerStyle);
        }
        
        row = worksheet.createRow(ROW_HEADER+1);
        row.createCell(0).setCellValue("");
        row.createCell(1).setCellValue("");
        row.createCell(2).setCellValue("");
        row.createCell(3).setCellValue("");
        row.createCell(4).setCellValue("");
        row.createCell(5).setCellValue("");
        row.createCell(6).setCellValue("사번");
        row.createCell(7).setCellValue("성명");
        row.createCell(8).setCellValue("부서명");
        row.createCell(9).setCellValue("신청일시");
        row.createCell(10).setCellValue("급여공제동의여부");
        row.createCell(11).setCellValue("사번");
        row.createCell(12).setCellValue("성명");
        row.createCell(13).setCellValue("부서명");
        row.createCell(14).setCellValue("신청일시");
        row.createCell(15).setCellValue("급여공제동의여부");
        
        cellIter = row.cellIterator();        
        while(cellIter.hasNext()) {
        	cellIter.next().setCellStyle(headerStyle);
        }
        
        worksheet.addMergedRegion(new CellRangeAddress(ROW_HEADER, ROW_HEADER+1, 0, 0));	//예약일 병합
        worksheet.addMergedRegion(new CellRangeAddress(ROW_HEADER, ROW_HEADER+1, 1, 1));	//예약시간 병합
        worksheet.addMergedRegion(new CellRangeAddress(ROW_HEADER, ROW_HEADER+1, 2, 2));	//사옥 병합
        worksheet.addMergedRegion(new CellRangeAddress(ROW_HEADER, ROW_HEADER+1, 3, 3));	//헬스키퍼 병합
        worksheet.addMergedRegion(new CellRangeAddress(ROW_HEADER, ROW_HEADER+1, 4, 4));	//베드 병합
        worksheet.addMergedRegion(new CellRangeAddress(ROW_HEADER, ROW_HEADER+1, 5, 5));	//진행상태 병합
        worksheet.addMergedRegion(new CellRangeAddress(ROW_HEADER, ROW_HEADER, 6, 10));		//예약 병합
        worksheet.addMergedRegion(new CellRangeAddress(ROW_HEADER, ROW_HEADER, 11, 15));	//대기 병합
        
         
        int rowIndex = ROW_LIST_START;
         
        // 각 해당하는 셀에 값과 스타일을 넣음
        for(Map<String, Object> map : list) {
            row = worksheet.createRow(rowIndex);
            row.createCell(0).setCellValue(map.getOrDefault("RESVE_DE", "").toString());
            row.createCell(1).setCellValue(map.getOrDefault("RESVE_TM_TXT", "").toString());
            row.createCell(2).setCellValue(map.getOrDefault("BLD_NM", "").toString());
            row.createCell(3).setCellValue(map.getOrDefault("MSSR_NCNM", "").toString());
            row.createCell(4).setCellValue(map.getOrDefault("BED_NM", "").toString());
            row.createCell(5).setCellValue(map.getOrDefault("LAST_STTUS_NM", "").toString());
            
            row.createCell(6).setCellValue(map.getOrDefault("RESVE_EMPNO", "").toString());            
            row.createCell(7).setCellValue(map.getOrDefault("RESVE_EMPNM", "").toString());
            row.createCell(8).setCellValue(map.getOrDefault("RESVE_DEPTNM", "").toString());
            row.createCell(9).setCellValue(map.getOrDefault("RESVE_DT", "").toString());
            row.createCell(10).setCellValue(map.getOrDefault("RESVE_AGREE_YN", 
            		(map.get("RESVE_EMPNO") == null || StringUtil.isEmpty(map.get("RESVE_EMPNO").toString())) ? "" : "N").toString());
            
            row.createCell(11).setCellValue(map.getOrDefault("WAIT_EMPNO", "").toString());
            row.createCell(12).setCellValue(map.getOrDefault("WAIT_EMPNM", "").toString());
            row.createCell(13).setCellValue(map.getOrDefault("WAIT_DEPTNM", "").toString());
            row.createCell(14).setCellValue(map.getOrDefault("WAIT_DT", "").toString());
            row.createCell(15).setCellValue(map.getOrDefault("WAIT_AGREE_YN", 
            		(map.get("WAIT_EMPNO") == null || StringUtil.isEmpty(map.get("WAIT_EMPNO").toString())) ? "" : "N").toString());
            
            //가운데 정렬 스타일 세팅
            row.getCell(4).setCellStyle(centerStyle);	//베드
            row.getCell(10).setCellStyle(centerStyle);	//(예약자)동의여부
            row.getCell(15).setCellStyle(centerStyle);	//(대기자)동의여부
            
            rowIndex++;
        }
         
         
        try {
        	String browser = DownloadView.getBrowser(request);
			String disposition = DownloadView.getDisposition(excelName, browser);
			response.setHeader("Content-Disposition", disposition);
            //response.setHeader("Content-Disposition", "attachement; filename=\""+ URLEncoder.encode(excelName, "UTF-8") + "\";charset=\"UTF-8\"");
          } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
    }

}
