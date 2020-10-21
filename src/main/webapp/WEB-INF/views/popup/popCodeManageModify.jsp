<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<div class="pop-head">
	<h2>코드 수정</h2>
	<a href="#none" class="close layerClose">닫기</a> 
</div>
<div class="pop-container">
	<!-- 팝업 컨텐츠 S -->
	<table id="codeManageCreater_enter" class="tbl-style">
		<colgroup>
			<col style="width:30%;">
			<col>
		</colgroup>
		<tr>
			<th>코드타입(대)</th>
			<td id="tdCodeTyl"></td>
		</tr>
		<tr class="smallCate">
			<th>코드타입(소)</th>
			<td id="tdCodeTys"></td>
		</tr>
		<tr>
			<th>코드값</th>
			<td id="tdCode"></td>
		</tr>
		<tr>
			<th class="required" >코드명</th>
			<td>
				<input id="codeNm_modifyVal" type="text" placeholder="코드명을 입력하세요." style="ime-mode:active;">
			</td>
		</tr>
	</table>
	
	<div class='rv-desc' style='text-align: center; display:none;'><strong id="requiredMsg"></strong></div>
	<div class="pop-btn-area">
<!-- 		<button id ="scheduleCreate_saveBtn" class="pop-btn">저장</button> -->
		<button id ="codeManageModify_saveBtn" class="pop-btn">저장</button>
		<button class="pop-btn gray layerClose">취소</button>
	</div>
</div>
<script>
var popCodeManageModify = {
	init: function() {
		popCodeManageModify.button.popSaveClickEvent();
	},
	
	params: {},
	
	button: {
		popSaveClickEvent: function() {
			$("#codeManageModify_saveBtn").on("click", function() {
				
				if(!popCodeManageModify.validation()) {
					return false;
				}
				
				confirmPopup('코드를 수정 하시겠습니까?', function() {
					$.ajax({
						url: ROOT + '/cmmn/updateCodeManageModify',
						type: 'POST',
						data: {
							codeTyl: popCodeManageModify.params.codeType, 
							codeTys: popCodeManageModify.params.codeTys,
							code: popCodeManageModify.params.code,
							codeNm: $("#codeNm_modifyVal").val()
						},
						success : function(res) {
							console.log('modify',res);
							codeManageList.list.renderCodeManageList();
							alertPopup('수정 되었습니다.');
						},
						error : function(err) {
							alertPopup(err.responseText);
						}
					}); 
				});
			});
		}
	},
	
	validation: function() {
		 var chk = true;
		 var returnMsg=" 등록된 값이 없습니다";
			$(".required").nextAll("td").children("input,select").each(function() {
				if($(this).val() == null || $(this).val() == "") {
					returnMsg = $(this).parent().prev().text() + returnMsg;
					$(".rv-desc").show();
					$("#requiredMsg").text(returnMsg);
					chk = false
					return false;
				};
			});  
		 return chk;
	}
}

$(document).ready(function() {
	var item = '${item}';
	var data = JSON.parse(item);
	popCodeManageModify.init();
	
	$("#tdCodeTyl").text(data.CODE_TYL_NM);
	$("#tdCodeTys").text(data.CODE_TYS_NM);
	$('#tdCode').text(data.CODE)
	$('#codeNm_modifyVal').val(data.CODE_NM)
	popCodeManageModify.params.codeType = data.CODE_TYL;
	popCodeManageModify.params.codeTys = data.CODE_TYS;
	popCodeManageModify.params.code = data.CODE;
	popCodeManageModify.params.codeNm = data.CODE_NM;
	
	if(data.CODE_TYL === "BED") {
		$('.smallCate').show();
	} else {
		$('.smallCate').hide();
	}
});
</script>
