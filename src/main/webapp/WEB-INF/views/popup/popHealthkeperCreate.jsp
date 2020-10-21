<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<div class="pop-head">
	<h2>헬스키퍼 등록</h2>
	<a href="#none" class="close layerClose">닫기</a> 
</div>
<div class="pop-container">	
	<!-- 팝업 컨텐츠 S -->
	<table id="healthkeperCreate_enter" class="tbl-style">
		<colgroup>
			<col style="width:20%;">
			<col style="width:30%;">
			
		</colgroup>
		
			<tr>
				<th class = "required">사옥</th >
				<td>
					<select style="width:120px;" data-code-tyl="BLD" data-empty-str="선택" id="healthkeperCreate_bldCombo"></select>
				</td>
			</tr>
			<tr>
				<th  class = "required" >이름</th>
				<td>
					<input style="width:115px;"  data-empty-str="관리사본명" id="healthkeperCreate_mssrName" maxlength="10">	
				</td>
			</tr>
			<tr>
				<th  class = "required" >닉네임</th>
				<td>
					<input style="width:115px;"  data-empty-str="관리사" id="healthkeperCreate_mssrNCName" maxlength="10">						
				</td>
			</tr>
			<tr>
				<th  class = "required" >성별</th>
				<td>
					<select style="width:120px;" id="sexdstnCombo">
						<option value="">선택</option>
						<option value="M">남</option>
						<option value="F">여</option>
					</select>
				</td>
			</tr>
	</table>	

	<div class='rv-desc' style="text-align: center; display:none ;"><strong id="requiredMsg"></strong></div>
	<div class="pop-btn-area">
		<button id ="healthkeperCreate_saveBtn" class="pop-btn">저장</button>
		<button class="pop-btn gray layerClose">취소</button>
	</div>

	<!-- 팝업 컨텐츠 E -->						
</div><!-- //pop-container -->
<script>
var popHealthkeperCreate = {
		init: function() {
			$('#healthkeperCreate_enter select[data-code-tyl]').empty(); 
			loadCodeSelect(undefined, '#healthkeperCreate_enter'); //콤보박스 공통코드 세팅
			popHealthkeperCreate.button.popSaveClickEvent();
		},	
	
		combobox: {
			bldComboEventBinding: function() {
				$('#healthkeperCreate_bldCombo').on('change', function() {
					var bldCode = $(this).val();
					popHealthkeperCreate.combobox.setMssrCombo(bldCode);
					loadCodeSelect(undefined, $('#healthkeperCreate_bedCombo').parent()); 
					
				})
				
			} 	
			
		},
		
		button:{
			popSaveClickEvent:function(){
				$("#healthkeperCreate_saveBtn").on("click",function(){			
					 
					if(!popHealthkeperCreate.validation.required()){
						return false;
					}

					var params = {	"bldCode" : $("#healthkeperCreate_bldCombo" ).val(),
									"mssrName" : $("#healthkeperCreate_mssrName" ).val(),
					    			"mssrNCName" : $("#healthkeperCreate_mssrNCName").val(),
						    		"sexdstn" : $("#sexdstnCombo").val()};
					
					confirmPopup('헬스키퍼를 등록 하시겠습니까?', function(){		 			
						$.ajax({
							url: ROOT + '/mssr/healthkeperCreate',
							type: 'POST',
							data: params ,
							success : function(res){
								healthkeperList.list.renderhealthkeperList();
								closeLayerPopup();
							},
							error : function(err) {
								var json = JSON.parse(err.responseText);
								alertPopup(json.message);
							}
						}); 
				    }); 
				});
			}
		},
		validation: {
				 required:function(){
					 var chk = true;
					 var returnMsg=" 등록된 값이 없습니다"
						$(".required").nextAll("td").children("input,select").each(function(){
								
						  if ($(this).val()==null||$(this).val()==""){
							  returnMsg = $(this).parent().prev().text() +returnMsg;
							  $(".rv-desc").show();
							  $("#requiredMsg").text(returnMsg);
							  chk = false
							  return false;
						  };
						});  
					 return chk;
						 
				 }
	}
}

function getParams(){
	var params = []; 
			params.push({
			    bldCode : $("#healthkeperCreate_bldCombo" ).val(),
			    mssrName : $("#healthkeperCreate_mssrName").val(),
			    mssrNCName : $("#healthkeperCreate_mssrNCName").val(),
			    sexdstn : $("#sexdstnCombo").val()
			});	
	return params;
}

$(document).ready(function(){		
	
	popHealthkeperCreate.init();
	
	
})
  
</script>
