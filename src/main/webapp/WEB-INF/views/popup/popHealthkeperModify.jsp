<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<div class="pop-head">
	<h2>헬스키퍼 정보 수정</h2>
	<a href="#none" class="close layerClose">닫기</a> 
</div>
<div class="pop-container">	
	<!-- 팝업 컨텐츠 S -->

	<table id="healthkeperModify_enter" class="tbl-style" >
		<colgroup>
			<col style="width:25%;">
			<col>
		</colgroup>
		 <thead>
		 	<tr>
				<th>NO.</th>
				<td id='tdHealthkeperEmpno'></td>
			</tr>
			<tr>
				<th>사옥</th>
				<td id='tdHealthkeperBld'></td>
			</tr>
			<tr>
				<th>이름</th>
				<td id="tdMssName"></td>
			</tr>
			<tr>
				<th>닉네임</th>
				<td id="tdMssNCName"></td>
			</tr>
			<tr>
				<th>성별</th>
				<td id="tdSexdstn"></td>
			</tr>
		</thead>	
		<tbody id="healthkeperModify_Body" >
			
		</tbody>
	</table>
	<div class="pop-btn-area">
		<button id ="healthkeperModify_saveBtn" class="pop-btn">저장</button>
		<button class="pop-btn gray" onclick="closeLayerPopup();">취소</button>
	</div>
	
<script>
var popHealthkeperModify= {
		init: function() {
			popHealthkeperModify.button.popSaveClickEvent();
		
		},	
		params: {}, 
		
		//조회된 예약 목록 데이터를 가지고 화면에 목록 생성
		renderHealthkeperList: function() {
			 modifyRow();
		},
		button:{
			popSaveClickEvent:function(){
				
				$("#healthkeperModify_saveBtn").on("click",function(){	
					
					var ncnm= $("[name='mssr_ncnm']").val();
					var hffc= $("select[name='hffcCombo']").val();
					
					if(ncnm=="" || ncnm== null){
						ncnm = popHealthkeperModify.params.mssrNCName;
					}
					if(hffc=="" || hffc== null){
						hffc = popHealthkeperModify.params.mssrhffc;
					}
					
					var params = {	"mssrEmpno" : popHealthkeperModify.params.mssrEmpno,
			    			"hffc_yn" : hffc,
			    			"mssr_ncnm" : ncnm};
					
					$.ajax({
						url: ROOT + '/mssr/healthkeperModify',
						type: 'POST',
						data: params,
						success : function(res){
							alertPopup('수정 되었습니다.');
							healthkeperList.list.renderhealthkeperList();
							closeLayerPopup();
						},
						error : function(err) {
							console.error(err);
						}
					}); 
					
 				});
			}
					
	   },
		
}


function  modifyRow (){
	  var trCnt = $(".healthkeperModify").length +1;	
	  
	  var mssr_ncnm=[];
	  	mssr_ncnm.push('<input type="text" name="mssr_ncnm" placeholder='+popHealthkeperModify.params.mssrNCName+' style="color:black" maxlength="10">');
	  	$('#tdMssNCName').append(mssr_ncnm);
	  
	  
		 var healthkeperListHtml = [];
	            healthkeperListHtml.push('<tr><th style="text-align:center;">재직여부</th>');
				healthkeperListHtml.push('<td id="tdHffc">');
				healthkeperListHtml.push('<select style="width:120px;" id="hffcCombo" name="hffcCombo">');
				healthkeperListHtml.push('<option value="" selected disabled hidden>'+popHealthkeperModify.params.mssrhffc+'</option>');
				healthkeperListHtml.push('<option value="Y">Y</option>');
				healthkeperListHtml.push('<option value="N">N</option>');
			    healthkeperListHtml.push('</select></td></tr>');
            $('#healthkeperModify_Body').append(healthkeperListHtml.join(''));
    
 }


$(document).ready(function(){		
	var item = '${item}';
	var data = JSON.parse(item);
	popHealthkeperModify.init();
	popHealthkeperModify.params.bldName = data.BLD_NM;
	popHealthkeperModify.params.mssrEmpno = data.MSSR_EMPNO;
	popHealthkeperModify.params.mssrName = data.MSSR_NAME;
	popHealthkeperModify.params.mssrNCName = data.MSSR_NCNM;
	popHealthkeperModify.params.mssrSexdstn = data.MSSR_SEXDSTN;
	popHealthkeperModify.params.mssrhffc = data.HFFC_YN;
	popHealthkeperModify.renderHealthkeperList()
	var sexdstn = (data.MSSR_SEXDSTN == 'M') ? '남' : '여';
	$("#tdHealthkeperEmpno").text(data.MSSR_EMPNO);
	$("#tdHealthkeperBld").text(data.BLD_NM);
	$("#tdMssName").text(data.MSSR_NAME);
	$('#tdSexdstn').text(sexdstn)

	
})
  
</script>
