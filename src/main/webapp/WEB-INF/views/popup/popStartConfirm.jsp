<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<div class="pop-head">
	<h2>헬스케어 시작 확인</h2>
	<a href="#none" class="close layerClose">닫기</a> 
</div>
<div class="pop-container st2">	
	<!-- 팝업 컨텐츠 S -->

	<p class="pop-desc icon04"><strong id="empnm"></strong>님.<br/> 헬스케어 이용을 <strong>시작</strong> 하시겠습니까?</p>

	<ul class="reservation-list">
		<li><span class="icon01" id="resveConfirm_resveDe"></span></li>
		<li><span class="icon02" id="resveConfirm_resveTm"></span></li>
		<li><span class="icon03" id="resveConfirm_mssr"></span></li>
		<li><span class="icon04" id="resveConfirm_bed"></span></li>
	</ul>
	
	<!-- 
	<div style="text-align:center; margin-top: 10px; font-size: 14px">
		<span>※ 케어시간은 <strong>휴게시간으로 근무등록</strong> 바랍니다.</span>
	</div> 
	-->
	<p class="comm-info"><i class="xi-error-o"></i>케어시간은 휴게시간으로 근무등록 바랍니다.</p>
	
	<div class="pop-btn-area">
		<button id="btnOk" class="pop-btn">확인</button>
		<button id="btnCancel" class="pop-btn gray layerClose">취소</button>
	</div>

	<!-- 팝업 컨텐츠 E -->						
</div><!-- //pop-container -->

<script>

$(document).ready(function(){		
	var item = '${item}';
	var data = JSON.parse(item);
	
	if(data){
		
		if(data.COMPT_YN != 'Y'){
			$('#resveConfirm_resveDe').text(moment(data.RESVE_DE).format('YYYY-MM-DD'));
			//$('#resveConfirm_mssr').text(data.MSSR_NCNM + '(' + (data.MSSR_SEXDSTN =='M' ? '남':'여') +')');
			$('#resveConfirm_mssr').text(data.MSSR_NCNM);
			$('#resveConfirm_bed').text(data.BED_NM);
			$('#empnm').text(data.RESVE_EMPNM);
			
			var realTime = getRealTime(data.RESVE_TM);
			$('#resveConfirm_resveTm').text(realTime.start + '~' + realTime.end);
			
			$('#layer_pop04 #btnOk').on('click', function(){			
				resveConfirm.start(data.RESVE_NO);
				
			});
			
			// 취소클릭시
			$('#layer_pop04 #btnCancel').on('click', function(){	
				$(document).off('keypress');	//엔터이벤트삭제
				$('#txtResveEmpno').val('');
				$('#txtResveEmpno').focus();	//포커스
			})
			
			// 엔터이벤트
			$(document).one('keypress', function(e){
				if(e.keyCode == 13){					
					$('#layer_pop04 #btnOk').trigger('click');				
			    }
			})
		}else{
			$.alert({
				text: getMessage('error.alreadyConfirm'),
				callback: function(){
					resveConfirm.table.refresh();
					$('#txtResveEmpno').val('');
					$('#txtResveEmpno').focus();							
				}
			});
		}
		
		
	}else{		
		//alertPopup(getMessage('error.resveNotFound'), resveConfirm.table.refresh);	//예약이 존재하지 않습니다.	
		$.alert({
			text: getMessage('error.resveNotFound'),
			callback: function(){
				resveConfirm.table.refresh();
				$('#txtResveEmpno').val('');
				$('#txtResveEmpno').focus();							
			}
		});
	}
	
})

</script>