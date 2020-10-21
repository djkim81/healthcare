<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<!DOCTYPE html>
<html lang="ko">
	<head>
		<jsp:include page="${JSP}/include/resource.jsp" />
		<jsp:include page="${JSP}/include/messages.jsp" />
		<script src="${JS}/resve/resveConfirm.js"></script>	
		<script>
			var ROOT = '${ROOT}';
		</script>
	</head>
	<body>
		<div class="wrap">
			<input id="bldCode" type="hidden" value="${sessionScope.LoginVo.place}" />
			<div class="content staff">

				<article class="content-in">
					
					<div class="tit-head">
						<h1 class="logo">SKT헬스케어</h1>
						<p class="tit-date">						
							<i class="xi-time-o"></i><span></span>
						</p>
					</div>

					<div class="month-area" style="display:none;">
						<div class="building-select">
							<!-- <select data-code-tyl="BLD"></select> -->
							<span class="bar"></span>
						</div>
						<div class="calendar-area">
							<table class="month-calendar">
								<tbody>
									<tr>		
										<!-- 2주 캘린더 렌더링영역 -->
									</tr>
								</tbody>
							</table>
						</div>
					</div><!-- //month-area -->

					<div class="number-search">
						<label>사번 입력</label>
						<select data-code-tyl="BLD" style="display:none"></select>
						<input id="txtResveEmpno" type="text" placeholder="사번을 입력해서 반드시 최종 확인해 주시기 바랍니다." style="ime-mode:disabled;">
						<button id="btnConfirm"><i class="xi-search xi-x"></i>확인</button>
					</div>

					<div class="reservation-table-wrap">
						<table class="reservation-table">
							<colgroup>
								<col>								
								<col style="width:11.5%">
								<col style="width:11.5%">
								<col style="width:11.5%">
								<col style="width:11.5%">
								<col style="width:11.5%">
								<col style="width:11.5%">
								<col style="width:11.5%">
								<col style="width:11.5%">
							</colgroup>
							<thead>
								<tr>
									<th>베드</th>
									<th>10:00~10:30</th>
									<th>11:00~11:30</th>
									<th>12:00~12:30</th>
									<th>13:00~13:30</th>
									<th>14:00~14:30</th>
									<th>15:00~15:30</th>
									<th>16:00~16:30</th>
									<th>17:00~17:30</th>
								</tr>
							</thead>
							<tbody>
								<!-- 예약현황 테이블 렌더영역 -->
							</tbody>
						</table>
					</div>	
					
					<div class="weekend-info">
						<p>금일은 헬스케어 <strong>서비스가 제공되지 않습니다.</strong></p>
					</div>
					
				</article><!-- //content-in -->
				
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