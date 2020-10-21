<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="pop-head">
	<h2>헬스케어 <span>예약</span> 신청</h2>
	<a href="#none" class="close layerClose">닫기</a> 
</div>
<div class="pop-container st2">	
	<!-- 팝업 컨텐츠 S -->
	
	<p class="pop-desc icon01"><strong>예약</strong> 하시겠습니까?</p>		
	
	<ul class="reservation-list">
		<li><span class="icon01" id="resveRegist_resveDe"></span></li>
		<li><span class="icon02" id="resveRegist_resveTm"></span></li>
		<li><span class="icon03" id="resveRegist_mssr"></span></li>
		<li><span class="icon04" id="resveRegist_bed"></span></li>
	</ul>
	
	
<!-- 	<div style="text-align:center; margin-top: 10px; font-size: 14px"> -->
<!-- 		<span>※ 케어 센터에서 꼭 <strong>본인 확인</strong> 후 입장해주세요.</span> -->
<!-- 	</div> -->
	
	<p class="comm-info">
		<i class="xi-error-o"></i>케어 센터에서 꼭 본인 확인 후 입장해주세요.<br/>
		<i class="xi-error-o"></i>케어시간은 휴게시간으로 등록바랍니다.
	</p>
	
	
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
	
	$('#resveRegist_resveDe').text(moment(data.RESVE_DE).format('YYYY-MM-DD'));
	//$('#resveRegist_mssr').text(data.MSSR_NCNM + '(' + (data.MSSR_SEXDSTN =='M' ? '남':'여') +')');
	$('#resveRegist_mssr').text(data.MSSR_NCNM);
	$('#resveRegist_bed').text(data.BED_NM);
	
	var realTime = getRealTime(data.RESVE_TM);
	$('#resveRegist_resveTm').text(realTime.start + '~' + realTime.end);
})

</script>

