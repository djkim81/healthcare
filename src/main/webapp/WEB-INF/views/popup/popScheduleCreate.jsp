<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<div class="pop-head">
	<h2>헬스키퍼 근무 등록</h2>
	<a href="#none" class="close layerClose">닫기</a> 
</div>
<div class="pop-container">	
	<!-- 팝업 컨텐츠 S -->
	<table id="scheduleCreate_enter" class="tbl-style">
		<colgroup>
			<col style="width:15%;">
			<col>
		</colgroup>
		
			<tr>
				<th class = "required">사옥</th >
				<td>
					<select style="width:120px;" data-code-tyl="BLD" data-empty-str="선택" id="scheduleCreate_bldCombo"></select>
				</td>
			</tr>
			<tr>
				<th class = "required" >베드</th>
				<td>
					<select style="width:120px;" data-code-tyl="BED" data-code-tys="SK01"  data-empty-str="선택" id="scheduleCreate_bedCombo"></select>
				</td>
			</tr>
			<tr>
				<th  class = "required" >헬스키퍼</th>
				<td>
					<select style="width:120px;"  data-empty-str="선택" id="scheduleCreate_mssrCombo">	
					<option>선택</option>				
				   </select>
				</td>
			</tr>
			<tr>
				<th class = "required" >남성허용</th>
				<td>
					<select style="width:120px;"  data-empty-str="선택" id="scheduleCreate_allowCombo">	
					<option>선택</option>						
				   </select>
				</td>
			</tr>
		  <tbody id='scheduleCreate_Body'>	
			
		</tbody>
	</table>	

     <div class='rv-desc' style="text-align: center; display:none ;"><strong id="requiredMsg">취소는 시작 20분전 까지만</strong></div>
	<div class="pop-btn-area">
		<button id ="scheduleCreate_saveBtn" class="pop-btn">저장</button>
		<button class="pop-btn gray layerClose">취소</button>
	</div>

	<!-- 팝업 컨텐츠 E -->						
</div><!-- //pop-container -->
<script>
var popSchCreate = {
		init: function() {
			//$(".datepicker").datepicker();
			initDatepicker();
			/* $('#scheduleCreate_enter select[data-code-tyl]').empty();  */
			loadCodeSelect(undefined, '#scheduleCreate_enter'); //콤보박스 공통코드 세팅
			popSchCreate.combobox.bldComboEventBinding(); //사옥 콤보박스 변경 이벤트
			popSchCreate.combobox.mssrComboEventBinding(); //OP900-1220 헬스키퍼  변경 이벤트
			popSchCreate.datepicker.setDefaultValue(); //datepicker 기본값 세팅	
			popSchCreate.button.popSaveClickEvent(); //근무저장
			popSchCreate.button.rowAddClickEvent();//row추가
		},
		
		cmmnCode: {
			allCodeList: [],
			getAllCodeList: function() {
				$.ajax({
					url: ROOT + '/cmmn/allCodeList',
					success: function(res) {
						if (res.status === 200) {
							popSchCreate.cmmnCode.allCodeList = res.list;
						}
					},
					error: function(err) {
						console.error(err);
					}
				})
			},
			
			codeToName: function(code) {
				var allCodeList = popSchCreate.cmmnCode.allCodeList;
				
				if (allCodeList.length == 0) {
					popSchCreate.cmmnCode.getAllCodeList();
				}
				
				var codeName = '';
				for (var i in allCodeList) {
					if (allCodeList[i].CODE == code) {
						codeName = allCodeList[i].CODE_NM;
						break;
					}
				}	
				return codeName;
			}
		},
		
		datepicker: {
			setDefaultValue: function() { //기본 날짜 세팅
				var fromDate = moment().subtract(30, 'd').format('YYYY-MM-DD'); //30일 전 날짜
				var toDate = moment().format('YYYY-MM-DD'); //오늘 날짜
				/* $('input#scheduleCreate_from_date').val(fromDate);
				$('input#scheduleCreate_to_date').val(toDate); */
			}
		},
	
		combobox: {
			bldComboEventBinding: function() {
				$('#scheduleCreate_bldCombo').on('change', function() {
					var bldCode = $(this).val();
					popSchCreate.combobox.setMssrCombo(bldCode);
					//loadCodeSelect(undefined, $('#scheduleCreate_bedCombo').parent()); 
				})
			},	
			
			mssrComboEventBinding: function() {
				$('#scheduleCreate_mssrCombo').on('change', function() {
					var empno = ($(this).val()).substr(0,8);
					var sexdstm = ($(this).val()).substr(8,1);
					popSchCreate.combobox.setManCombo(sexdstm);
					//loadCodeSelect(undefined, $('#scheduleCreate_bedCombo').parent()); 
				})
			},	
			
			getMssrList: function(bldCode) {
				var deferred = $.Deferred();
				
				$.ajax({
					url: ROOT + '/mssr/getMssrList',
					type: 'POST',
					data: {
						bldCode: bldCode
					},
					success: function(res) {
						deferred.resolve(res.list);
					},
					error: function(err) {
						console.error(err);
						deferred.reject('');
					}
				});
				
				return deferred.promise();
			},
			
			setManCombo: function(sexdstm) {
					$('#scheduleCreate_allowCombo').empty();
					var mssrComboHtml = ['<option value="">선택</option>'];
					if(sexdstm=="F"){
						mssrComboHtml.push('<option>N</option>');
						mssrComboHtml.push('<option>Y</option>');
					}else{
						mssrComboHtml.push('<option>Y</option>');
					}

					$('#scheduleCreate_allowCombo').html(mssrComboHtml.join(''));
	
			},
			
			setMssrCombo: function(bldCode) {
		
				$.when(popSchCreate.combobox.getMssrList(bldCode)).done(function(result) {
					
					$('#scheduleCreate_mssrCombo').empty();
					var mssrComboHtml = ['<option value="">선택</option>'];
									
					if (result.length > 0 && bldCode) {
						for (var i in result) {
							mssrComboHtml.push('<option value="' + result[i].EMPNO + result[i].SEXDSTN + '">' + result[i].NCNM + '</option>');
						}
					}
						
					$('#scheduleCreate_mssrCombo').html(mssrComboHtml.join(''));
					
				});	
			}
			
		},
		
			
		
		 params: {	bldCode: "", //사옥코드
				    mssrEmpno:"", //관리사 코드 
				    bedCode: "" , //배드  
			        rowData:null,
			        rowCnt:"",
			        allowYn:"",
		}, 
		
		button:{
			popSaveClickEvent:function(){
				$("#scheduleCreate_saveBtn").on("click",function(){			
					
				    
					if(!popSchCreate.validation.required()){  // 필수입력 체크
						return false;
					}
					
					if(!popSchCreate.validation.timeCheck()){ //시작  종료시간    체크 
						return false;
					}
					
					confirmPopup('헬스키퍼 스케쥴을 등록 하시겠습니까?', function(){		 			
						$.ajax({
							url: ROOT + '/mssr/scheduleCreate',
							type: 'POST',
							data:{params:JSON.stringify(getParams())} , //여러건의 json param 전송  josn -> String 변환
							success : function(res){		
								scheduleList.list.renderScheduleList(); // 리스트제조회 
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
			
		   rowAddClickEvent:function(){			   
				  $.when(addRow()).done(function(cnt,setDate){
					//datepicker Element 추가시  datapicker 속성  재설정     
					$("#scheduleCreate_Body").find('.datepicker').datepicker("destroy");//.datepicker();
  				    initDatepicker();  
  				    
  				    loadCodeSelect( function(){ 
  				    var mm = moment(setDate,"YYYYMMDD");
  				    var fromDate = mm.format('YYYY-MM-DD');
  					var toDate = mm.add(1, 'M').format('YYYY-MM-DD'); //30일 전 날짜
  					
  					$('#scheduleCreate_start_date'+cnt).val(fromDate);
  					$('#scheduleCreate_end_date'+cnt).val(toDate);
  				    $('#scheduleCreate_startTime'+cnt).val(1);
  				   }
  				      , '#scheduleCreate_tr'+cnt);   
				  });
			}
		},
		
		validation: {
			 required:function(){ // 필수갑 체크 
				 var chk = true;
				 var returnMsg="등록된값이 없습니다"
					$(".required").nextAll("td").children("input,select").each(function(){
					  if ($(this).val()==null||$(this).val()==""){
						  returnMsg = $(this).parent().prev().text() +returnMsg; //th text get
						  $(".rv-desc").show();
						  $("#requiredMsg").text(returnMsg);
						  chk = false
						  return false;
					  };
					});  
				 return chk;
					 
			 },
			 
			 timeCheck: function(){ //시간 체크
				 var chk = true;
				 $('select[id^=scheduleCreate_startTime]').each(function(i,e){
					 var startTm = $('#'+e.id);
					 var endTm = $('#scheduleCreate_endTime' + e.id.replace('scheduleCreate_startTime',''));
					 
					 if(startTm.val()>endTm.val()){
						 //alertPopup('시작시간이 종료시간보다 클 수 없습니다.');
						 $("#requiredMsg").text(getMessage('error.timeCompareStartEnd'));
						 $(".rv-desc").show();
						 chk = false;
					 }						
				 })
				 return chk;
			 }
		}
	}

	function fnRowDelete(){  //row 삭제 
		    var id = event.target.parentNode.parentNode.id;
		    $('#'+id).remove();
			  $("#scheduleCreate_Body  tr").each(function(index){ // row 삭제시   row번호  재지정
				     rowidrest = this;
				     index += 1
				     $(rowidrest).children("th").text("근무시간 "+index)
				     $(rowidrest).attr({"id":"scheduleCreate_tr"+index ,"data-rowid":index});
				     $(rowidrest).children('td').children('input,select').attr("id",function(){ return "cheduleCreate_"+ this.name+ index })  
				});
			
		}
		
	function addRow (){ //row추가 
		var deferred = $.Deferred();
		var trCnt =$(".trschedule").length + 1 //추가되는 row 번호 발췌
		var lastdate = (trCnt==1)? moment() : moment($('.datepicker:last').val(),"YYYYMMDD");//추가되는 row 에 datepicker  초기화 일자 (상위로우에 마직막일자)  
		var scheduleListHtml = [];
		if(trCnt >5){ deferred.reject(''); return false;}
			scheduleListHtml.push('<tr id ="scheduleCreate_tr'+trCnt+'" class="trschedule" data-rowid = "'+trCnt+'" >');
			scheduleListHtml.push('<th class = "required">근무시간'+trCnt+'</th >');
			scheduleListHtml.push('<td>');
			scheduleListHtml.push('<input type="text"  id = "scheduleCreate_start_date'+trCnt+'" class="datepicker startDate" >');
			scheduleListHtml.push('<em class="fromto"> ~ </em>');
			scheduleListHtml.push('<input type="text" id = "scheduleCreate_end_date'+trCnt+'" class="datepicker endDate"> ');
			scheduleListHtml.push('<select   name ="startTime" data-code-tyl="RVT" data-code-tys="RVTSTART" id="scheduleCreate_startTime'+trCnt+'" ></select>');
			scheduleListHtml.push('<em class="fromto"> ~ </em>');
			scheduleListHtml.push('<select  name ="endTime"  data-code-tyl="RVT" data-code-tys="RVTEND"  id="scheduleCreate_endTime'+trCnt+'" ></select> ');
		if(trCnt == 1){
			scheduleListHtml.push('<button class="t-btn cr01" id="scheduleCreate_rowAddBtn" onclick="popSchCreate.button.rowAddClickEvent()" >추가</button></td></tr>');
		}else{
			scheduleListHtml.push("<button name='delBtn' class='t-btn cr01' onclick='fnRowDelete(event)'>삭제</button></td></tr>"); 
		}
		$('#scheduleCreate_Body').append(scheduleListHtml.join(''));
		 
		deferred.resolve(trCnt,lastdate);
		return deferred.promise();
	}
	
	function getParams(){  //기간  시간별 등록  ROW parma 생성
		var params = [] 
		
		$(".trschedule").each(function(){
			var rowid = $(this).data("rowid");
			var startTimeCode = Number($("#scheduleCreate_startTime"+rowid ).val());
			var endTimeCode  =  Number($("#scheduleCreate_endTime"+rowid ).val())
			for (var i = startTimeCode; i <= endTimeCode; i++) {
				params.push({
				    bldCode      : $("#scheduleCreate_bldCombo" ).val(),
			        mssrEmpno    : ($("#scheduleCreate_mssrCombo").val()).substr(0,8),
			        bedCode      : $("#scheduleCreate_bedCombo").val(),
			        allowYn      : $("#scheduleCreate_allowCombo").val(),	
					startDate    : $("#scheduleCreate_start_date"+rowid).val(), //근무 시작일
					endDate      : $("#scheduleCreate_end_date"+rowid).val(), //근무 종료일  */
					resveTime    : i, //시간코드  */
					//startTimeCode: $("#scheduleCreate_startTime"+rowid ).val(), //시작 시간 코드
					//endTimeCode  : $("#scheduleCreate_endTime"+rowid ).val(),	  // 종료 시간 코드 	 */
					
					
				});	
			}		
		});	
		return params;
	}

$(document).ready(function(){		
	popSchCreate.init();

})
  
</script>
