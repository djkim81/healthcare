<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<!DOCTYPE html>
<html lang="ko">
	<head>
		<jsp:include page="${JSP}/include/resource.jsp" />
		<jsp:include page="${JSP}/include/messages.jsp" />		
		<script src="${JS}/resve/resveMgmt.js"></script>
	</head>
	<body>	
		<div class="wrap">

			<header class="header">
				<jsp:include page="${JSP}/include/header.jsp" />
			</header><!-- //header -->

			<div class="content">
				<ul class="tab-design">
					<li rel="tab1"><a href="${ROOT}/mssr/schedule">헬스키퍼 스케쥴 관리</a></li>
					<li rel="tab2" class="on"><a href="${ROOT}/resveMgmt/list">예약 정보 조회 및 변경</a></li>
					<li rel="tab3"><a href="${ROOT}/mssr/healthkeper">헬스키퍼 관리</a></li>			
					<li rel="tab4"><a href="${ROOT}/charger/chargerList">담당자 관리</a></li>
					<li rel="tab5"><a href="${ROOT}/mssr/mssrblacklist">No-Show 관리</a></li>
					<li rel="tab6"><a href="${ROOT}/cmmn/restDeList">휴일 관리</a></li>
					<li rel="tab7"><a href="${ROOT}/cmmn/codeManage">공통코드 관리</a></li>
				</ul>
				<div class="sub-tit">
					<h2>예약 정보 조회 및 변경</h2>
				</div>
								
				
				<div class="search_field_wrap">
					<div class="search_field_area">
						<table class="search_field">
							<tbody>
								<tr>
									<th><strong class="stit">기간</strong></th>
									<td>
										<input type="text" class="datepicker startDate" id="from_date">
										<em class="fromto"> ~ </em>
										<input type="text" class="datepicker endDate" id="to_date">
									</td>
									<th><strong class="stit">사옥</strong></th>
									<td>
										<select data-code-tyl="BLD" data-empty-str="전체"></select>
									</td>
									<th><strong class="stit">베드</strong></th>
									<td>
										<select id="cbbBedCode" data-code-tyl="BED" data-empty-str="전체">
											<!-- <option>전체</option> -->
										</select>
									</td>
									<th><strong class="stit">상태</strong></th>
									<td>
										<select data-code-tyl="STS" data-empty-str="전체"></select>
									</td>
									<td><input id="empnm" type="text" placeholder="이름입력"></td>
								</tr>
							</tbody>
						</table>
					</div><!-- //searchTableArea -->
					<div class="search_btn_area">
						<button class="search_btn" id="listBtn"><i class="xi-search xi-x"></i>조회</button>
					</div><!-- //search_btn_area -->
				</div><!-- // search_field_wrap -->
		
				<div class="sub-tit">
					<div class="f-right">
						<button id="btnExcel" class="btn">엑셀</button>				
					</div>
				</div>
				
				<table class="tbl-style t_center">
					<colgroup>						
						<col style="width:8%;">
						<col style="width:8%;">
						<col style="width:9%;">
						<col style="width:7%;">
						<col style="width:7%;">
						<col style="width:6%;">
						<col style="width:9%;">
						<col style="width:12%;">
						<col style="width:5%;">
						<col style="width:9%;">
						<col style="width:12%;">
						<col style="width:5%;">
					</colgroup>
					<thead>
						<tr>
							<th rowspan="2">예약일</th>
							<th rowspan="2">예약시간</th>
							<th rowspan="2">사옥</th>
							<th rowspan="2">헬스키퍼</th>
							<th rowspan="2">베드</th>
							<th rowspan="2">진행상태</th>
							<th colspan="3">예 약</th>
							<th colspan="3">대 기</th>							
						</tr>
						<tr>
							<th style="border-left:1px solid #ddd;">신청인</th>
							<th>일시<br/>부서</th>
							<th>급여공제<br/>동의여부</th>
							<th>신청인</th>
							<th>일시<br/>부서</th>
							<th>급여공제<br/>동의여부</th>
						</tr>
					</thead>
					<tbody id="resveMgmt">
						<!-- 테이블 영역 -->
					</tbody>
				</table>

				<div class="paging_area" id="pagingArea">
					<!-- 페이징 영역 -->
				</div>
				
				
			</div><!-- //content -->

			<footer class="footer">				
				<jsp:include page="${JSP}/include/footer.jsp" />
			</footer><!-- //footer -->

		</div><!-- //wrap -->

		<!-- 레이어팝업 : S -->
		<div class="layer">
			<jsp:include page="${JSP}/include/layer.jsp" />
		</div><!-- //layer -->
		<!-- 레이어팝업 : E -->

	</body>
</html>