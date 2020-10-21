<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<!DOCTYPE html>
<html lang="ko">
	<head>
		<jsp:include page="${JSP}/include/resource.jsp" />	
		<jsp:include page="${JSP}/include/messages.jsp" />	
		<script src="${JS}/mssr/schedule.js"></script>
	</head>
	<body>

		<div class="wrap">

			<header class="header">				
				<jsp:include page="${JSP}/include/header.jsp" />
			</header><!-- //header -->

			<div class="content">
			<ul class="tab-design">
				<li class="on" rel="tab1"><a href="${ROOT}/mssr/schedule">헬스키퍼 스케쥴 관리</a></li>
				<li rel="tab2"><a href="${ROOT}/resveMgmt/list">예약 정보 조회 및 변경</a></li>
				<li rel="tab3"><a href="${ROOT}/mssr/healthkeper">헬스키퍼 관리</a></li>	
				<li rel="tab4"><a href="${ROOT}/charger/chargerList">담당자 관리</a></li>
				<li rel="tab5"><a href="${ROOT}/mssr/mssrblacklist">No-Show 관리</a></li>
				<li rel="tab6"><a href="${ROOT}/cmmn/restDeList">휴일 관리</a></li>
				<li rel="tab7"><a href="${ROOT}/cmmn/codeManage">공통코드 관리</a></li>
			</ul>
		<!-- //header -->
				
				<div class="sub-tit">
					<h2>헬스키퍼 스케쥴 관리</h2>
				</div>
				
				<div class="search_field_wrap">
					<div class="search_field_area">
						<table class="search_field">
							<%-- <colgroup>
								<col width="80px">
								<col width="*">
								<col width="80px">
								<col width="*">
								<col width="80px">
								<col width="*">
							</colgroup> --%>
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
										<select style="width:120px;" data-code-tyl="BLD" data-empty-str="전체" id="bldCombo">
										</select>										
									</td>
									<th><strong class="stit">헬스키퍼</strong></th>
									<td>
										<select style="width:120px;" id="mssrCombo" >
											<option value="">전체</option>
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

				<div class="sub-tit">
					<div class="f-left" style="display:inline-block;">
						<button class="btn" id ="deleteBtn">삭제</button>						
					</div>
					<div class="f-right">
						<button class="btn" id ="createBtn">등록</button>						
					</div>
				</div>

				<table class="tbl-style t_center tbl-hover ">
					<colgroup>
						<col style="width:50px;">						
						<col style="width:12%;">
						<col style="width:12%;">
						<col style="width:12%;">
						<col style="width:12%;">
						<col style="width:12%;">
						<col style="width:12%;">
						<col style="width:12%;">
						<col style="width:12%;">
						<%-- <col style="width:15%;"> --%>
					</colgroup>
					<thead>
						<tr>
							<th><input id ='checkAll' type="checkbox"></th>
							<th>근무날짜</th>
							<th>사옥</th>
							<th>헬스키퍼</th>
							<th>베드</th>
							<th>성별</th>
							<th>근무시간</th>
							<th>등록자사번</th>
							<th>남성허용</th>
							<!-- <th>수정</th> -->
						</tr>
					</thead>
					<tbody id="scheduleList">
						<!-- <tr>
							<td><input type="checkbox"></td>
							<td>2019-08-21</td>
							<td>티타워</td>
							<td>James</td>
							<td>남</td>
							<td>09:30 ~ 11:00</td>
							<td><button class="t-btn cr01" onclick="e_layer_pop07('layer_pop07');">수정</button></td>
						</tr>
						<tr>
							<td><input type="checkbox"></td>
							<td>2019-08-21</td>
							<td>티타워</td>
							<td>James</td>
							<td>남</td>
							<td>09:30 ~ 11:00<br>14:30 ~ 15:00</td>
							<td><button class="t-btn cr01" onclick="e_layer_pop07('layer_pop07');">수정</button></td>
						</tr>		
						<tr>
							<td><input type="checkbox"></td>
							<td>2019-08-21</td>
							<td>티타워</td>
							<td>James</td>
							<td>남</td>
							<td>09:30 ~ 11:00</td>
							<td><button class="t-btn cr01" onclick="e_layer_pop07('layer_pop07');">수정</button></td>
						</tr>
						<tr>
							<td><input type="checkbox"></td>
							<td>2019-08-21</td>
							<td>티타워</td>
							<td>James</td>
							<td>남</td>
							<td>09:30 ~ 11:00</td>
							<td><button class="t-btn cr01" onclick="e_layer_pop07('layer_pop07');">수정</button></td>
						</tr>
						<tr>
							<td><input type="checkbox"></td>
							<td>2019-08-21</td>
							<td>티타워</td>
							<td>James</td>
							<td>남</td>
							<td>09:30 ~ 11:00</td>
							<td><button class="t-btn cr01" onclick="e_layer_pop07('layer_pop07');">수정</button></td>
						</tr> -->
					</tbody>
				</table>

				<div class="paging_area" id="pagingArea">
					<a href="#none" class="first"><img src="${IMG}/common/btn_first.gif"></a>
					<a href="#none" class="prev"><img src="${IMG}/common/btn_prev.gif"></a>
					<a href="#none" class="num selected">1</a>
					<a href="#none" class="num">2</a>
					<a href="#none" class="num">3</a>
					<a href="#none" class="num">4</a>
					<a href="#none" class="num">5</a>
					<a href="#none" class="num">6</a>
					<a href="#none" class="num">7</a>
					<a href="#none" class="num">8</a>
					<a href="#none" class="num">9</a>
					<a href="#none" class="num">10</a>
					<a href="#none" class="next"><img src="${IMG}/common/btn_next.gif"></a>
					<a href="#none" class="last"><img src="${IMG}/common/btn_last.gif"></a>
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