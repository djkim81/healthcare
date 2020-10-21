<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<div class="pop-head">
	<h2>휴일 등록/수정</h2>
	<a href="#none" class="close layerClose">닫기</a> 
</div>
<div class="pop-container">	
	<!-- 팝업 컨텐츠 S -->
	<table id="restDeRegister_enter" class="tbl-style">
		<colgroup>
			<col style="width:25%;">
			<col>
		</colgroup>
		<tbody>
			<tr>
			<th class = "required" >휴일 일자</th>
				<td id="restPeriod">	
					<input type="text" class="datepicker startDate" id="restDeRegister_start_date">
					<em class="fromto"> ~ </em>
					<input type="text" class="datepicker endDate" id="restDeRegister_end_date">
				</td>
			</tr>
			<tr>
			<th class = "required">휴일명</th>
				<td><input type="text" id="restDeRegister_Name"  >
				</td>
				
			</tr>
		</tbody>
	</table>	
     <div class='rv-desc' style="text-align: center; display:none ;"><strong id="requiredMsg"></strong></div>
	<div class="pop-btn-area">
		<button id ="restDeRegister_saveBtn"  class="pop-btn">저장</button>
		<button class="pop-btn gray layerClose">취소</button>
	</div>
      <input type="hidden" id="restDeRegister_No"  >
	<!-- 팝업 컨텐츠 E -->						
</div><!-- //pop-container -->
<script>
var popRestDeRegister = {
		init: function()  {
			//loadCodeSelect()
			popRestDeRegister.button.popSaveClickEvent();
			popRestDeRegister.datepicker.setDefaultValue();
		},

		datepicker: {
			setDefaultValue: function() { //기본 날짜 세팅
				var fromDate = moment().format('YYYY-MM-DD'); 
				var toDate = moment().format('YYYY-MM-DD'); 
			   $("#restDeRegister_enter").find('.datepicker').datepicker("destroy");
			    initDatepicker(); 
				$('input#restDeRegister_start_date').val(fromDate);
				$('input#restDeRegister_end_date').val(toDate);
			}
		}, 
		 params: {	restDeNo:"", // 휴일등록 코드
			        startDate:"", // 휴일 시작일
			        endDate:"", // 휴일 시작일
				    restDeName:"",  //휴일명
				    saveStat:""  // 등록수정 상태값
		}, 
		//예약 목록 조회
		selectRestDeItem: function() {		
			$.ajax({
				url: ROOT + '/cmmn/selectRestDeItem',
				data: popRestDeRegister.params,
				success: function(res) {
					if (res.status === 200) {
						   console.log(res)
						   popRestDeRegister.setparam(res.item);
					} 
				},
				error: function(err) {
					console.error(err);
				}
			})
		},
		button:{
			popSaveClickEvent:function( ){
				$("#restDeRegister_saveBtn").on("click",function(){			
					if(!popRestDeRegister.validation.required()){
						return false;
					}
					
					popRestDeRegister.params.restDeNo = $("#restDeRegister_No").val();
				    popRestDeRegister.params.restDeName = $("#restDeRegister_Name").val();
				    popRestDeRegister.params.startDate = $("#restDeRegister_start_date").val();
					popRestDeRegister.params.endDate = $("#restDeRegister_end_date").val(); 
					
					popRestDeRegister.validation.restDeDupCheck(function(msg){
					confirmPopup(msg||'휴일 정보를  저장  하시겠습니까?', function(){		 			
				  $.ajax({
								url: ROOT + '/cmmn/restDeSave',
								type: 'POST',
								data: popRestDeRegister.params ,
								success : function(res){
											
									RestDeList.list.renderRestDeList();
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
				});
			}
		   
		},
        setparam:function(data){  // 휴일 수정건 정보 셋잍   휴일 수정시에만 
	   		   $("#restDeRegister_No").val(data.RESTDE_NO);
	   		   $("#restDeRegister_Name").val(data.RESTDE_NAME);
	   		   $("#restPeriod").text(moment(data.RESTDE_DATE,"YYYYMMDD",'ko').format('YYYY-MM-DD (ddd)')); 
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
				 popRestDeRegister.validation.elementLock(chk)
				 return chk;	 
			 },
		elementLock:function(value){ // 등록 수정별  화면 제어
			if(popRestDeRegister.params.saveStat =='C'){
				$(".rv-desc").css('display',(value)?'':'none');
			}else{
				
				$("#restPeriod").text();
				$("#restDeRegister_start_date").prop("disabled",value);
				$("#restDeRegister_end_date").prop("disabled",value);
				$(".ui-datepicker-trigger").prop("disabled",value);
				
			} 		
		},
		restDeDupCheck:function(func){ // 휴일 중복 체크 및  휴일에 포함된 예약건 체크 
			   var stscode = '';
			$.ajax({
					url: ROOT + '/cmmn/restDeCheck',
					type: 'POST',
					data: popRestDeRegister.params ,
					success : function(res){
						   popRestDeRegister.validation.elementLock(false);
						    stscode  =   res.item.REST_STATUS.split(",")		
							if(res.item.DUP_YN == 'Y'){
								  $("#requiredMsg").text("등록된 휴일이 있습니다");
								  popRestDeRegister.validation.elementLock(true);
							 }else if( stscode.indexOf('STS01') >= 0 ) {
								 func('휴일 등록 시 기존 예약 '+res.item.STS01_CNT+'건이\n 자동 취소 됩니다. 저장하시겠습니까')
							 } else if( stscode.indexOf('STS00') >= 0 ){
								 func('휴일 등록 시 근무스케쥴이 \n 자동 취소 됩니다. 저장하시겠습니까')
							 }else{
								 func();
							 }
					}
				}); 
		   }	 
		}
	}


$(document).ready(function(){		
	var item = '${item}';
	var data = JSON.parse(item);
	console.log(data)
    popRestDeRegister.init();
	popRestDeRegister.params.restDeNo = data.RESTDE_NO;
 	if(data.RESTDE_NO !="" ){  //휴일  아이디 번호가 있으면 수정 없으며 저장  
			popRestDeRegister.params.saveStat="U"; 
			popRestDeRegister.selectRestDeItem();
		    popRestDeRegister.validation.elementLock(true);
		}else{
			popRestDeRegister.params.saveStat="C";
		}	 
})
  
</script>
