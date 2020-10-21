<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<!DOCTYPE html>
<html lang="ko">
	<head>
		<jsp:include page="${JSP}/include/resource.jsp" />
		<jsp:include page="${JSP}/include/messages.jsp" />		
		<script src="${JS}/resve/resveList.js"></script>		
	</head>
	<body>	
		<div class="wrap">

			<header class="header">
				<jsp:include page="${JSP}/include/header.jsp" />
			</header><!-- //header -->

			<div class="content">
				
				<div class="sub-tit">
					<h2>예약현황</h2>
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
									<th><strong class="stit">상태</strong></th>
									<td>
										<select data-code-tyl="STS" data-empty-str="전체" id="stsCombo">
										</select>
									</td>
								</tr>
							</tbody>
						</table>
					</div><!-- //searchTableArea -->
					<div class="search_btn_area">
						<button class="search_btn" id="listBtn"><i class="xi-search xi-x"></i>검색</button>
					</div><!-- //search_btn_area -->
				</div><!-- // search_field_wrap -->

				<table class="tbl-style t_center">
					<colgroup>
						<col style="width:13.5%;">
						<col style="width:13.5%;">
						<col style="width:13.5%;">
						<col style="width:13.5%;">
						<col style="width:13.5%;">
						<col style="width:13.5%;">						
						<col>
					</colgroup>
					<thead>
						<tr>
							<th>예약일</th>
							<th>예약시간</th>
							<th>사옥</th>
							<th>헬스키퍼</th>
							<th>베드</th>
							<th>신청일</th>
							<th>상태</th>							
						</tr>
					</thead>
					<tbody id="resveList">
						<!-- 예약현황 리스트 렌더 영역 -->
					</tbody>
				</table>

				<div class="paging_area" id="pagingArea">
					<!-- 페이징 렌더 영역 -->
				</div>
				
				<div class="rv-desc-list">
					<h3>안내사항</h3>
					<ul>
						<li class="icon01">금일부터 <strong class="red">2주까지의 케어</strong><strong>만 예약</strong>이 가능하며, <strong class="red">1건 당 5,000원</strong><strong>이 급여에서 차감</strong>됩니다.</li>
						<li class="icon02"><strong>예약 취소는</strong> <strong class="red">케어 시작 20분 전</strong><strong>까지만</strong> 가능하며, No-show 시 <strong class="red">2주간 예약이 불가</strong>합니다</li>
						<li class="icon03">예약자가 <strong>1명인 경우 대기 신청이 가능</strong>하며, 이전 예약자가 취소할 경우 대기자에게 <strong>예약완료 SMS가 발송</strong>됩니다.</li>
						<li class="icon04">케어 시작 <strong class="red">30분 전에서 20분 전 사이에</strong> <strong>예약완료 SMS</strong>를 받으신 대기자는 <strong>No-show 패널티가 없습니다.</strong></li>
					</ul>
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