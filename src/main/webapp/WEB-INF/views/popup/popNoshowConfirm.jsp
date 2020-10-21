<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<div class="pop-head">
	<h2>헬스케어 사후 케어완료</h2>
	<a href="#none" class="close layerClose">닫기</a> 
</div>
<div class="pop-container st2">	
	<!-- 팝업 컨텐츠 S -->

	<p class="pop-desc icon05">예약하신 서비스를 <strong>완료</strong>처리 하시겠습니까?</p>
	
	<ul class="reservation-list">
		<li><span class="icon01" id="noshowConfirm_resveDe"></span></li>
		<li><span class="icon02" id="noshowConfirm_resveTm"></span></li>
		<li><span class="icon03" id="noshowConfirm_mssr"></span></li>
		<li><span class="icon04" id="noshowConfirm_bed"></span></li>
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
	
	$('#noshowConfirm_resveDe').text(moment(data.RESVE_DE).format('YYYY-MM-DD'));
	//$('#noshowConfirm_mssr').text(data.MSSR_NCNM + '(' + (data.MSSR_SEXDSTN =='M' ? '남':'여') +')');
	$('#noshowConfirm_mssr').text(data.MSSR_NCNM);
	$('#noshowConfirm_bed').text(data.BED_NM);
	
	var realTime = getRealTime(data.RESVE_TM);
	$('#noshowConfirm_resveTm').text(realTime.start + '~' + realTime.end);
})

</script>
