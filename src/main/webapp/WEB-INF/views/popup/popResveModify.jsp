<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="text" value="${ type eq 'resve' ? '예약' : '대기'}" />

<div class="pop-head">
	<h2>
		${text} 정보 수정
	</h2>
	<a href="#none" class="close layerClose">닫기</a> 
</div>

<div class="pop-container st2">	
	<!-- 팝업 컨텐츠 S -->
	<input type="hidden" id="type" value="${type}" />
	<table class="tbl-style">
		<colgroup>
			<col style="width:20%;">
			<col>
		</colgroup>
		<tbody>
			<tr>
				<th>신청인</th>
				<td>${type eq 'resve' ? item.RESVE_EMPNM : item.WAIT_EMPNM}
					(${type eq 'resve' ? item.RESVE_EMPNO : item.WAIT_EMPNO})
					&nbsp;/&nbsp;
					${type eq 'resve' ? item.RESVE_DEPTNM : item.WAIT_DEPTNM}
					
				</td>
			</tr>
			<tr>
				<th>예약일</th>
				<td>${item.RESVE_DE_TXT}</td>
			</tr>
			<tr>
				<th>시간</th>  
				<td>${item.RESVE_TM_TXT}</td>
			</tr>
			<tr>
				<th>사옥</th>
				<td>${item.BLD_NM}</td>
			</tr>
			<tr>
				<th>베드</th>
				<td>${item.BED_NM}</td>
			</tr>
			<tr>
				<th>관리사</th>
				<td>${item.MSSR_NCNM}</td>
			</tr>
			<tr>
				<th>상태</th>
				<td>
					<c:if test="${type eq 'resve' }">
						<select id="sttusCode">
							<%-- <c:if test="${item.SUCCS_YN ne 'Y'}"> --%> <%-- 승계된 예약은 취소불가 --%>
							<option value="STS02">예약취소</option>
							<%-- </c:if> --%>
							<option value="STS05">케어완료</option>
						</select>
					</c:if>
					<c:if test="${type ne 'resve' }">
						<select id="sttusCode">
							<option value="STS03">대기취소</option>							
						</select>
					</c:if>			
				</td>
			</tr>					
		</tbody>
	</table>	
	<div class="pop-btn-area">
		<button id="btnOk" class="pop-btn">확인</button>
		<button class="pop-btn gray layerClose">취소</button>
	</div>

	<!-- 팝업 컨텐츠 E -->						
</div><!-- //pop-container -->

