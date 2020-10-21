<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="pop-head">
	<h2>헬스케어 대기 신청</h2>
	<a href="#none" class="close layerClose">닫기</a> 
</div>
<div class="pop-container st2">	
	<!-- 팝업 컨텐츠 S -->
	<p class="pop-desc icon02"><strong>대기</strong> 하시겠습니까?</p>
	
	<!-- 
	<table class="tbl-style">
		<colgroup>
			<col style="width:30%;">
			<col>
		</colgroup>
		<tbody>
			<tr>
				<th>예약일</th>
				<td id="resveWait_resveDe"></td>
			</tr>
			<tr>
				<th>헬스키퍼</th>
				<td id="resveWait_mssr"></td>
			</tr>
			<tr>
				<th>베드</th>
				<td id="resveWait_bed"></td>
			</tr>
			<tr>
				<th>예약시각</th>
				<td id="resveWait_resveTm"></td>
			</tr>
		</tbody>
	</table>
	-->
	
	<ul class="reservation-list">
		<li><span class="icon01" id="resveWait_resveDe"></span></li>
		<li><span class="icon02" id="resveWait_resveTm"></span></li>
		<li><span class="icon03" id="resveWait_mssr"></span></li>
		<li><span class="icon04" id="resveWait_bed"></span></li>
	</ul>
	

	<div class="pop-btn-area">
		<button id="btnOk" class="pop-btn">확인</button>
		<button class="pop-btn gray layerClose">취소</button>
	</div>

	<!-- 팝업 컨텐츠 E -->						
</div><!-- //pop-container -->
<script>

var resveNo = '${resveNo}';
var data = $('#resve-'+resveNo).data('data');

$(document).ready(function(){		
	
	$('#resveWait_resveDe').text(moment(data.RESVE_DE).format('YYYY-MM-DD'));
	//$('#resveWait_mssr').text(data.MSSR_NCNM + '(' + (data.MSSR_SEXDSTN =='M' ? '남':'여') +')');
	$('#resveWait_mssr').text(data.MSSR_NCNM);
	$('#resveWait_bed').text(data.BED_NM);
	
	var realTime = getRealTime(data.RESVE_TM);
	$('#resveWait_resveTm').text(realTime.start + '~' + realTime.end);
})

</script>

