<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<div class="pop-head">
	<h2>관리자등록/수정</h2>
	<a href="#none" class="close layerClose">닫기</a> 
</div>
<div class="pop-container">	
	<!-- 팝업 컨텐츠 S -->
	<table id="chargerRegister_enter" class="tbl-style">
		<colgroup>
			<col style="width:25%;">
			<col>
		</colgroup>
		<tbody>
			<tr>
			<th class = "required"  >사번</th>
				<td id ='tdEmp'>
				  <input id="txt_Empno" type="text" disabled>
				 <button id ="chargerRegister_getEmpInfo"  class="t-btn cr01" >사원검색</button>
				</td>
			</tr>
			<tr>
				<th>이름</th>
				<td id="chargerRegister_Name" ></td>
			</tr>
			<tr>
				<th>부서</th>
				<td id="chargerRegister_Dept" ></td>
			</tr>
			<tr>
				<th class = "required" >담당사옥</th>
				<td>
					<select style="width:120px;"  data-code-tyl="BLD" data-empty-str="선택" id="chargerRegister_bldCombo"></select>
									
				</td>
			</tr>
			<tr>
				<th class = "required" >권한</th>
				<td>
					<select style="width:120px;"  data-code-tyl="AUT" data-empty-str="권한"  id="chargerRegister_authCombo"></select>
				</td>
			</tr>
		</tbody>
	</table>	
     <div class='rv-desc' style="text-align: center; display:none ;"><strong id="requiredMsg"></strong></div>
	<div class="pop-btn-area">
		<button id ="chargerRegister_saveBtn"  class="pop-btn">저장</button>
		<button class="pop-btn gray layerClose">취소</button>
	</div>
     <input id="chargerRegister_chargerEmpNo" type="hidden">
	<!-- 팝업 컨텐츠 E -->						
</div><!-- //pop-container -->
<script>
var popRegCreate = {
		init: function() {
			
			popRegCreate.button.popSaveClickEvent();
			
		},
		
		 params: {	chargerEmpno:"", //담당자사번
			        bldCode: "", // 사옥코드     
				    authCode:"",  //권한코드
				    saveStat:""  // 등록수정 상태값
		}, 
		//예약 목록 조회
		selectChargerItem: function() {
			var deferred = $.Deferred();
			
			$.ajax({
				url: ROOT + '/charger/selectChargerItem',
				data: popRegCreate.params,
				success: function(res) {
					if (res.status === 200) {
						   deferred.resolve(res.item);
						
					} else {
						deferred.reject("");
					}
				},
				error: function(err) {
					console.error(err);
					deferred.reject("");
				}
			})
			
			return deferred.promise();
		},
		button:{
			popSaveClickEvent:function(){
				$("#chargerRegister_saveBtn").on("click",function(){			
					
					if(!popRegCreate.validation.required()){
						return false;
					}
					
					popRegCreate.params.bldCode = $("#chargerRegister_bldCombo").val();
				    popRegCreate.params.chargerEmpno = $("#chargerRegister_chargerEmpNo").val();
					popRegCreate.params.authCode = $("#chargerRegister_authCombo").val(); 
				
					
					confirmPopup('담당자 정보를  저장  하시겠습니까?', function(){		 			
			 		$.ajax({
							url: ROOT + '/charger/chargerSave',
							type: 'POST',
							data: popRegCreate.params ,
							success : function(res){
										
								ChargerList.list.renderChargerList();
								alertPopup('등록 되었습니다.');
								//closeLayerPopup();
							},
							error : function(err) {
								var json = JSON.parse(err.responseText);
								alertPopup(json.message);
							} 
						}); 
				    }); 
				});
			},
		   empCheckClickEvent:function(){
			  $("#chargerRegister_getEmpInfo").on('click',function(){
				  document.domain  = g_newDomain
				  om_OpenOrgChart({ callback: popRegCreate.validation.empDupCheck, title: '유저/부서 단일선택', data: null , oneSelect: true });
				 
			  }) 
			   			   
		   }
		},
        setparam:function(data){
        	$.when(	popRegCreate.selectChargerItem()).done(function(res){
        	   $("#tdEmp").text(res.EMPNO);
        	   $("#chargerRegister_chargerEmpNo").val(res.EMPNO);
	   		   $("#chargerRegister_Name").text(res.HNAME);
	   		   $("#chargerRegister_Dept").text(res.DEPTNM);
	   		   $("#chargerRegister_bldCombo").val(res.BLD_CODE);
	   		   $("#chargerRegister_authCombo").val(res.AUTH_CODE);
   		    
          })
            
		},		
		validation: {
			 required:function(){
				 var chk = true;
				 var returnMsg=" 입력 값이 없습니다"
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
					 
			 },
			 empDupCheck:function(result){
				 var orgchartObj = JSON.parse(result);
				 popRegCreate.params.chargerEmpno = orgchartObj[0].UserID.toUpperCase()
				 $("#txt_Empno").val(popRegCreate.params.chargerEmpno);
				 $("#chargerRegister_chargerEmpNo").val(popRegCreate.params.chargerEmpno)
				 $("#chargerRegister_Name").text(orgchartObj[0].UserName)
				 $("#chargerRegister_Dept").text(orgchartObj[0].DeptName) 
				$.when(orgchart_callback()).done(function(value){
				     if(value){
					  $("#requiredMsg").text("등록된 사번이 있습니다");
				     }
			     popRegCreate.validation.elementLock(value)	 
			   });	
		},
		elementLock:function(value){
		if(popRegCreate.params.saveStat =='C'){
			$(".rv-desc").css('display',(value)?'':'none');
			$("#chargerRegister_saveBtn").prop("disabled",value);
			
		}else{
			/* $("#chargerRegister_chargerEmpNo").prop("disabled",value); */
			$("#chargerRegister_getEmpInfo").css('display',(value)?'none':'');
		} 
			
		}
	}
}

function orgchart_callback(){ 	
	var deferred = $.Deferred();
    	$.ajax({
			url: ROOT + '/charger/chargerEmpNoCheck',
			type: 'POST',
			data: popRegCreate.params ,
			success : function(res){
				deferred.resolve(res.item);
			},
			error : function(err) {
				var json = JSON.parse(err.responseText);
				alertPopup(json.message);
			} 
		}); 

    return deferred.promise();
};


$(document).ready(function(){		
	var item = '${item}';
	var data = JSON.parse(item);
	popRegCreate.init();
	popRegCreate.params.chargerEmpno = data.EMPNO;

	loadCodeSelect( function(){
	if(data.EMPNO !="" ){
			popRegCreate.params.saveStat="U" 
			popRegCreate.setparam();
		    popRegCreate.validation.elementLock(true);
			
		}else{
			popRegCreate.params.saveStat="C"
		    popRegCreate.button.empCheckClickEvent();
		}	

	},"#chargerRegister_enter")
	
})
  
</script>
