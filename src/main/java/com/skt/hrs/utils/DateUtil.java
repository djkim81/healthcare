package com.skt.hrs.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.skt.hrs.cmmn.exception.HrsException;

public class DateUtil {

	/**
	 * 
	 * @설명 : 오늘날짜를 YYYYMMDD형식으로 리턴
	 * @작성일 : 2019.09.05
	 * @작성자 : P149365
	 * @return
	 * @변경이력 :
	 */
	public static String getYYYYYMMDD() {
		return new SimpleDateFormat("yyyyMMdd").format(new Date());
	}
	
	/**
	 * 
	 * @설명 : 
	 * @작성일 : 2019.09.09
	 * @작성자 : P149365
	 * @param yyyymmdd
	 * @return
	 * @변경이력 :
	 */
	public static String yyyymmdd2HumanReadable(String yyyymmdd) {
		try{
			Date date = new SimpleDateFormat("yyyyMMdd").parse(yyyymmdd);
			return new SimpleDateFormat("yyyy-MM-dd").format(date);
		}catch(Exception e) {
			throw new HrsException("invalid date");
		}
	}
	
	public static String yyyymmdd2HumanReadableWithWeekday(String yyyymmdd) {
		try{
			Date date = new SimpleDateFormat("yyyyMMdd").parse(yyyymmdd);
			return new SimpleDateFormat("yyyy-MM-dd(EEE)", Locale.KOREAN).format(date);
		}catch(Exception e) {
			throw new HrsException("invalid date");
		}
	}
	
	/**
	 * 
	 * @설명 : HRS 시스템의 시간(1~9)를 실제 시간으로 변경 
	 * @작성일 : 2019.09.04
	 * @작성자 : P149365
	 * @param yyyymmdd
	 * @param hrsTm
	 * @변경이력 : 2019.09.25 첫타임 및 근무시간 1~8로 변경
	 */
	public static Date hrsDtToRealDt(String yyyymmdd, String hrsTm) {
		try {
			//Date startDate = new SimpleDateFormat("yyyyMMddHHmm").parse(yyyymmdd + "0830");
			//Date startDate = new SimpleDateFormat("yyyyMMddHHmm").parse(yyyymmdd + "0800");	
			Date startDate = new SimpleDateFormat("yyyyMMddHHmm").parse(yyyymmdd + "0900");	//첫타임 변경
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			cal.add(Calendar.HOUR, Integer.parseInt(hrsTm));
			return cal.getTime();
		}catch(Exception e) {
			throw new HrsException("invalid date");
		}
	}
	
	/**
	 * 
	 * @설명 : 현재시간 이후의 resveTm(1~9) 조회 
	 * @작성일 : 2019.09.18
	 * @작성자 : P149365
	 * @return
	 * @변경이력 : 2019.09.25 첫타임 및 근무시간 1~8로 변경
	 */
	public static int getNextResveTm() {
		try {
			SimpleDateFormat f = new SimpleDateFormat("HHmm");
			Date time = f.parse(f.format(new Date()));
			//Date startDate = f.parse("0830");
			//Date startDate = f.parse("0800");
			Date startDate = f.parse("0900");	//첫타임 변경
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			int i=1;
			//for(; i<=9; i++) {	//근무시간 8타임으로 변경
			for(; i<=8; i++) {
				cal.add(Calendar.HOUR, 1);
				if(cal.getTime().compareTo(time) > 0) {
					break;
				}
			}
			return i;
		}catch(Exception e) {
			throw new HrsException("invalid date");
		}
	}
	
	/**
	 * 
	 * @설명 : 현재시점에서의 과거 여부
	 * @작성일 : 2019.09.04
	 * @작성자 : P149365
	 * @param compareDate
	 * @return
	 * @변경이력 :
	 */
	public static boolean isPast(Date compareDate) {
		Date nowDt = new Date();
		return compareDate.getTime() <= nowDt.getTime();
	}
	
	/**
	 * 
	 * @설명 : 현재시각이 예약시간 -min 보다 과거인지 판단 
	 * @작성일 : 2019.09.05
	 * @작성자 : P149365
	 * @param compareDate
	 * @return
	 * @변경이력 :
	 */
	public static boolean isPastBeforeMin(Date compareDate, int min) {
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(compareDate);
		cal.add(Calendar.MINUTE, -min);
		cal.add(Calendar.SECOND, 59);	//(min이 20이라면) 09:30 건에 대하여 09:10:59초까지 가능하게 함 
		Date beforeMin = cal.getTime();
		return new Date().getTime() <= beforeMin.getTime(); 
	}
	
	/**
	 * 
	 * @설명 : 주말여부 
	 * @작성일 : 2019.09.20
	 * @작성자 : P149365
	 * @param yyyymmmdd
	 * @param format
	 * @return boolean
	 * @변경이력 :
	 */
	public static boolean isWeekend(String yyyymmmdd, String format) {
		
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(StringUtil.isEmpty(format) ? "yyyyMMdd" : format) ;
		    Date nDate = dateFormat.parse(yyyymmmdd) ;
		     
		    Calendar cal = Calendar.getInstance() ;
		    cal.setTime(nDate);
		     
		    int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;
		    
		    return dayNum == 1 || dayNum == 7;
		}catch(Exception e) {
			throw new HrsException("invalid date");
		}
	     
	    
	}
	
	/**
	 * 
	 * @설명 :  일자 계산  
	 * @작성일 : 2019.09.15
	 * @작성자 : P150113
	 * @param strart
	 * @param end
	 * @return
	 * @변경이력 :
	 */
	public static int getDateDiff(String strart,  String end) {
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");  
	    long diff = 0;
		try {
			Date startDate = dateFormater.parse(strart);
			Date endDate = dateFormater.parse(end);
            diff = endDate.getTime() - startDate.getTime();
            return (int)(diff/(24 * 60 * 60 * 1000));
	    }catch(Exception e) {
			throw new HrsException("invalid date");
		
	    }
	}
	
	/**
	 * 
	 * @설명 :  일자 계산  
	 * @작성일 : 2019.09.15
	 * @작성자 : P150113
	 * @param strart
	 * @param end
	 * @return
	 * @변경이력 :
	 */
	public static int getDateDiff(String strart,  String end, String format) {
		SimpleDateFormat dateFormater = new SimpleDateFormat(format);  
	    long diff = 0;
		try {
			Date startDate = dateFormater.parse(strart);
			Date endDate = dateFormater.parse(end);
            diff = endDate.getTime() - startDate.getTime();
            return (int)(diff/(24 * 60 * 60 * 1000));
	    }catch(Exception e) {
			throw new HrsException("invalid date");
		
	    }
	}
	
	/**
	 * 
	 * @설명 :  일자 계산  
	 * @작성일 : 2019.09.15
	 * @작성자 : P150113
	 * @param strart
	 * @param end
	 * @return
	 * @변경이력 :
	 */
	public static String getDateAdd(String yyyymmdd,  int day) {
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");  
		Calendar cal = Calendar.getInstance(); 
		try {
			Date startDate = dateFormater.parse(yyyymmdd);
			cal.setTime(startDate);
			cal.add(Calendar.DATE, day);
			return new SimpleDateFormat("yyyyMMdd").format(cal.getTime()) ;       
	    }catch(Exception e) {
			throw new HrsException("invalid date");
		
	    }
	}
}
