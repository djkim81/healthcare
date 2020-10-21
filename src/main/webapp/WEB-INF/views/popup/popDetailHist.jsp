<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="pop-head">
	<h2>예약 상세이력</h2>
	<a href="#none" class="close layerClose">닫기</a> 
</div>
<div class="pop-container st1">	
	<!-- 팝업 컨텐츠 S -->
	<!-- <p class="pop-desc icon02"><strong>대기</strong> 하시겠습니까?</p> -->
	
	
	<table class="tbl-style">
		<colgroup>
			<col style="width:20%;">
			<col style="width:35%;">
			<col style="width:35%;">
		</colgroup>
		<tbody>
			<tr>
				<th style="text-align:center">상태</th>
				<th style="text-align:center">날짜</th>
				<th style="text-align:center">이름</th>
			</tr>
			<c:forEach var="item" items="${list}" varStatus="status">
				<tr>					
					<td id="detailHist_status" style="text-align:center">
						<c:if test="${item.STTUS_CODE eq 'STS07'}">No-Show<br/>패널티없음</c:if>
						<c:if test="${item.STTUS_CODE ne 'STS07'}">${item.STTUS_CODE_NM}</c:if>					
					</td>
					<td id="detailHist_mssr" style="text-align:center">${item.REG_DT_STR}</td>
					<td id="detailHist_bed" style="text-align:center">${item.REG_EMPNM}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	
	<div class="pop-btn-area">
		<button id="btnOk" class="pop-btn gray layerClose">닫기</button>		
	</div>

	<!-- 팝업 컨텐츠 E -->						
</div><!-- //pop-container -->
<script>