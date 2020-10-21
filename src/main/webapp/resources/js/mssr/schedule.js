/**
 * 관리사 스케쥴 List
 */
var scheduleList = {
	// 초기화
	init: function() {
		loadCodeSelect(); //콤보박스 공통코드 세팅
		scheduleList.combobox.bldComboEventBinding(); //사옥 콤보박스 변경 이벤트
		scheduleList.datepicker.setDefaultValue(); //datepicker 기본값 세팅
	    scheduleList.list.renderScheduleList(); //목록 조회 후 렌더
		scheduleList.button.listBtnClickEvent(); //조회 버튼 클릭 이벤트
		scheduleList.button.scheduleCreateBtnEvent();
		scheduleList.button.scheduleDeleteBtnEvent();
		scheduleList.checkboxEvent.checkall();
	},
	
	
	cmmnCode: {
		allCodeList: [],
		getAllCodeList: function() {
			$.ajax({
				url: ROOT + '/cmmn/allCodeList',
				success: function(res) {
					if (res.status === 200) {
						scheduleList.cmmnCode.allCodeList = res.list;
					}
				},
				error: function(err) {
					console.error(err);
				}
			})
		},
		codeToName: function(code) {
			var allCodeList = scheduleList.cmmnCode.allCodeList;
			
			if (allCodeList.length == 0) {
				scheduleList.cmmnCode.getAllCodeList();
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
			var fromDate = moment().format('YYYY-MM-DD'); //오늘 날짜
			var toDate = moment().add(1, 'M').format('YYYY-MM-DD'); //30일 전 날짜
			
			$('input#from_date').val(fromDate);
			$('input#to_date').val(toDate);
			
			scheduleList.list.params.fromDate = moment().format('YYYYMMDD');
			scheduleList.list.params.toDate = moment().add(1, 'M').format('YYYYMMDD');
		}
	},
	
	
	
	combobox: {
		bldComboEventBinding: function() {
			$('#bldCombo').on('change', function() {
				var bldCode = $(this).val();
				scheduleList.combobox.setMssrCombo(bldCode);
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
		setMssrCombo: function(bldCode) {
			
			$.when(scheduleList.combobox.getMssrList(bldCode)).done(function(result) {
				
				$('#mssrCombo').empty();
				var mssrComboHtml = ['<option value="">전체</option>'];
								
				if (result.length > 0 && bldCode) {
					for (var i in result) {
						mssrComboHtml.push('<option value="' + result[i].EMPNO + '">' + result[i].NCNM + '</option>');
					}
				}
					
				$('#mssrCombo').html(mssrComboHtml.join(''));
				
			});
			
		}
		
	},
	
	list: {
		
		//목록 조회 시 사용하는 파라미터
		params: {
			pageNo: 1, //조회할 페이지 번호
			rowPerPage: 10, //한 페이지 당 조회할 ROW 수
			fromDate: '', //조회 시작 날짜
			toDate: '', //조회 끝 날짜
			bldCode: '', //사옥코드
			mssrEmpno: '', //관리사 사번
			startRow: 0 //조회 시작할 ROW
		},
		
		//현재 조회된 데이터 저장
		dataList: [],
		
		//예약 목록 조회
		selectScheduleList: function() {
			
			scheduleList.list.params.startRow = parseInt((scheduleList.list.params.pageNo - 1 ) * scheduleList.list.params.rowPerPage);
			
			var deferred = $.Deferred();
			
			$.ajax({
				url: ROOT + '/mssr/selectScheduleList',
				data: scheduleList.list.params,
				success: function(res) {
					if (res.status === 200) {
						
							deferred.resolve(res);
							scheduleList.list.dataList = res.list;
					   	
						
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
		
		
		//조회된 예약 목록 데이터를 가지고 화면에 목록 생성
		renderScheduleList: function() {
			$.when(scheduleList.list.selectScheduleList()).done(function(result) {

				$('tbody#scheduleList').empty();
				
				var resultList = result.list;
				var scheduleListHtml = [];
				var btnText = '';
				var btnClass = '';
				var resveDt = '';
				var sexdstn = '';
				var resve_de =  '' ;
				scheduleList.paging.params.totalCount = result.customs.totalCount;
				if(result.customs.totalCount == 0){
					scheduleListHtml.push('<tr>');
					scheduleListHtml.push('<td colspan=9 >검색 결과가 없습니다</td>');
					scheduleListHtml.push('</tr>');
				}else{
					
						for (var i in resultList) {
						
						resve_de =  moment(resultList[i].RESVE_DE,"YYYYMMDD",'ko'); 
                        resveDt=resve_de.format('YYYY-MM-DD (ddd)');
						//resveDt = resve_de.substr(0,4) + '-' + resve_de.substr(4,2) + '-' + resve_de.substr(6,2);
						
						var sexdstn = (resultList[i].MSSR_SEXDSTN == 'M') ? '남' : '여';
						var convertedTime = scheduleList.list.convertTime(resultList[i].RESVE_TM_LIST);
						var regemp = resultList[i].REG_EMPNO;
						if(resultList[i].UPDT_EMPNO!=undefined){
							regemp = resultList[i].UPDT_EMPNO;
						}
	  				
						scheduleListHtml.push('<tr>');
						scheduleListHtml.push('	<td><input type="checkbox" value="'+ resultList[i].RESVE_NO +'"></td>');
						scheduleListHtml.push('	<td>' + resveDt + '</td>');
						scheduleListHtml.push('	<td>' + resultList[i].BLD_NM + '</td>');
						scheduleListHtml.push('	<td><a name="modifyBtn" herf="#none" data-resveno="'+resultList[i].RESVE_NO+'" >' + resultList[i].MSSR_NCNM + '</td>');
						scheduleListHtml.push('	<td>' + resultList[i].BED_NM + '</td>');
						scheduleListHtml.push('	<td>' + sexdstn + '</td>');
						scheduleListHtml.push('	<td>' + convertedTime + '</td>');
						scheduleListHtml.push('	<td>' + regemp + '</td>');
						scheduleListHtml.push('	<td>' + resultList[i].ALLOW_YN + '</td>');
						//scheduleListHtml.push('	<td><button name="modifyBtn" data-resveno="'+resultList[i].RESVE_NO+'"  class="t-btn cr01">수정</button></td>');
						scheduleListHtml.push('</tr>');
						
					}
					
					
				}
				$('tbody#scheduleList').html(scheduleListHtml.join(''))
				scheduleList.paging.renderPaging();
				
				scheduleList.button.scheduleModifyBtnEvent();
				
				
			});
		},
		
		//현재 리스트에서 예약번호로 해당 ROW 의 데이터를 가져옴 
		 
		getRowData: function(resveNo) {
			var rowDataList = scheduleList.list.dataList;
			var rowData;
			for (var i in rowDataList) {
				if (rowDataList[i].RESVE_NO == resveNo) {
					rowData = {RESVE_NO:rowDataList[i].RESVE_NO,
							   BLD_CODE:rowDataList[i].BLD_CODE,
							   BED_CODE:rowDataList[i].BED_CODE,
							   ALLOW_YN:rowDataList[i].ALLOW_YN,
							   RESVE_DE:rowDataList[i].RESVE_DE,
							   MSSR_EMPNO:rowDataList[i].MSSR_EMPNO,
							   MSSR_NCNM:rowDataList[i].MSSR_NCNM,
							   BED_NM:rowDataList[i].BED_NM,
							   RESVE_NO_LIST:rowDataList[i].RESVE_NO_LIST, 
							   RESVE_TM:rowDataList[i].RESVE_TM,
							   RESVE_TM:rowDataList[i].ALLOW_YN,
							   RESVE_TM_LIST:rowDataList[i].RESVE_TM_LIST,
							   RESVE_COMPT_CNT:rowDataList[i].RESVE_COMPT_CNT,
							   CARE_COMPT_CNT:rowDataList[i].CARE_COMPT_CNT
					        } 
					break;
				}
			}
			
			return rowData;
		},
		
		//리스트 조회 시 가져온 시간 데이터[ex)1,2,4,5 ...] 를
		//실제 시간 범위[09:30~11:00  12:30~14:00 ...]로 변경하여 표시하는 함수
		convertTime: function(timeListStr) {
			
			var tList = timeListStr.split(',');
			var tListLength = tList.length;

			var returnTime = '';
			var firstNum = parseInt(tList[0]);
			var lastNum = parseInt(tList[tListLength-1]);
			var countNum = parseInt(tList[0]);
			
			for (var i in tList) {
				
				if (firstNum == countNum) {
					returnTime = getRealTime(tList[i]).start;
				}
				
				if (countNum == lastNum) {
					returnTime = returnTime + '~' + getRealTime(tList[i]).end;
					break;
				}
				
				if (firstNum != (parseInt(i)+1) && ((i+1) != tListLength)) {
					
					if ((countNum+i) < tList[i]) {
						returnTime += '~' + getRealTime(tList[i-1]).end + '<br/>' + getRealTime(tList[i]).start;
						firstNum = tList[i+1];
					}
					
				}
				countNum = parseInt(countNum) + 1 + (tList[i]-countNum);
				
				if (countNum > lastNum) {
					returnTime = returnTime + '~' + getRealTime(tList[i]).end;
				}
			}
			return returnTime;
		}

	},
	
	
	paging: {
		//페이징 처리를 위한 파라미터
		params: {
			totalCount: null, //list 의 전체 row count
			first: null, //현재 보여질 첫페이지
			last: null, //현재 보여질 마지막페이지
			prev: null, //이전페이지
			next: null //다음페이지
		},
		
		//페이지 이동 영역 생성
		renderPaging: function() {
			
			var currentIndex = scheduleList.list.params.pageNo; //현재 페이지 위치
			var rowPerPage = scheduleList.list.params.rowPerPage; //페이지 당 레코드 수
			var totalCount = scheduleList.paging.params.totalCount; //list 의 전체 row count
			var totalIndexCount = Math.ceil(totalCount / rowPerPage); //전체 인덱스 수
			var currentBlock = Math.ceil(currentIndex / 10) //현재 블럭의 시작 페이지 번호
			$("div#pagingArea").empty();
			
			var preStr = '';
			var postStr = '';
			var pageNumStr = '';
			
			
			var prev = (parseInt((currentIndex-1)/10)*10) - 9 > 0 ? (parseInt((currentIndex-1)/10)*10) - 9 : 1; //이전 블록의 시작페이지
			var next = (parseInt((currentIndex-1)/10)+1) * 10 + 1 < totalIndexCount ? (parseInt((currentIndex-1)/10)+1) * 10 + 1 : totalIndexCount; //다음 블록의 시작 페이지
			var first = (currentBlock - 1) * 10 + 1; //현재 블록의 시작 페이지
			var last = currentBlock * 10; //현재 블록의 끝 페이지
			
			
			scheduleList.paging.params.first = first;
			scheduleList.paging.params.last = last;
			scheduleList.paging.params.prev = prev;
			scheduleList.paging.params.next = next;
			
			if (totalIndexCount > 10) { //전체 인덱스가 10이 넘을 경우, first + prev 버튼
				preStr += '<a href="#none" class="first" id="firstBtn"><img src="' + IMG + '/common/btn_first.gif"></a>'
					   +  '<a href="#none" class="prev" id="prevBtn"><img src="' + IMG + '/common/btn_prev.gif"></a>';
			} else if (totalIndexCount <=10 && totalIndexCount > 1) { //전체 인덱스가 10보다 작을 경우, first 버튼
				preStr += '<a href="#none" class="first" id="firstBtn"><img src="' + IMG + '/common/btn_first.gif"></a>';
			}

			
			if (totalIndexCount > 10) { //전체 인덱스가 10이 넘을 경우, next + last 버튼
				postStr += '<a href="#none" class="next" id="nextBtn"><img src="' + IMG + '/common/btn_next.gif"></a>'
						+  '<a href="#none" class="last" id="lastBtn"><img src="' + IMG + '/common/btn_last.gif"></a>';
			} else if (totalIndexCount <=10 && totalIndexCount > 1) { //전체 인덱스가 10보다 작을 경우, last 버튼
				postStr += '<a href="#none" class="last" id="lastBtn"><img src="' + IMG + '/common/btn_last.gif"></a>';
			}
			
			for (var i= first; i<= last; i++) {
				if (i > totalIndexCount) {
					break;
				}
				
				if (i != currentIndex) {
					pageNumStr += '<a href="#none" class="num">' + i + '</a>';
				} else {
					pageNumStr += '<a href="#none" class="num selected">' + i + '</a>';
				}
			}
			
			$("div#pagingArea").append(preStr + pageNumStr + postStr);
			
			scheduleList.paging.button.allPagingBtnEventBinding();
		},
		
		button: {
			allPagingBtnEventBinding: function() {
				scheduleList.paging.button.pageNumEvent();
				scheduleList.paging.button.prevBtnEvent();
				scheduleList.paging.button.nextBtnEvent();
				scheduleList.paging.button.firstBtnEvent();
				scheduleList.paging.button.lastBtnEvent();
			},
			
			pageNumEvent: function() {
				$('a.num').off();
				$('a.num').on('click', function() {	
					if (scheduleList.list.params.pageNo == parseInt(this.innerHTML)) {
						return false;
					}
					
					scheduleList.list.params.pageNo = parseInt(this.innerHTML);
					scheduleList.list.renderScheduleList();
				});
			},
			
			prevBtnEvent: function() {
				$('a#prevBtn').off();
				$('a#prevBtn').on('click', function() {
					if (scheduleList.list.params.pageNo == scheduleList.paging.params.prev) {
						return false;
					}
					scheduleList.list.params.pageNo = scheduleList.paging.params.prev;
					scheduleList.list.renderScheduleList();
				});
			},
			
			nextBtnEvent: function() {
				$('a#nextBtn').off();
				$('a#nextBtn').on('click', function() {
					if (scheduleList.list.params.pageNo == scheduleList.paging.params.next) {
						return false;
					}
					scheduleList.list.params.pageNo = scheduleList.paging.params.next;
					scheduleList.list.renderScheduleList();
				});
			},
			
			firstBtnEvent: function() {
				$('a#firstBtn').off();
				$('a#firstBtn').on('click', function() {
					if (scheduleList.list.params.pageNo == 1) {
						return false;
					}
				
					scheduleList.list.params.pageNo = 1;
					scheduleList.list.renderScheduleList();
				});
			},
			
			lastBtnEvent: function() {
				$('a#lastBtn').off();
				$('a#lastBtn').on('click', function() {
					var rowPerPage = scheduleList.list.params.rowPerPage; //페이지 당 레코드 수
					var totalCount = scheduleList.paging.params.totalCount; //list 의 전체 row count
					var totalIndexCount = Math.ceil(totalCount / rowPerPage); //전체 인덱스 수
					
					if (scheduleList.list.params.pageNo == totalIndexCount) {
						return false;
					}
					
					scheduleList.list.params.pageNo = totalIndexCount;
					scheduleList.list.renderScheduleList();
				});
			}
		}
		
	},
	
	
	
	button: {
		
		//조회 버튼 클릭 이벤트
		listBtnClickEvent: function() {
			$('button#listBtn').on('click', function(e) {
				//날짜 validation 실행
				scheduleList.validation.dateCheck();
				
				//조회 페이지는 1로 초기화 param 세팅
				scheduleList.list.params.pageNo = 1;
				
				//사옥 param 세팅
				scheduleList.list.params.bldCode = $('#bldCombo').val();
				
				//관리사 param 세팅
				scheduleList.list.params.mssrEmpno = $('#mssrCombo').val();
				
				//목록 조회 및 렌더 실행
				scheduleList.list.renderScheduleList();
			});
		},
		//관리사스케줄등록
		scheduleCreateBtnEvent: function(){
		  $("button#createBtn").on('click',function(){
			  scheduleList.popup.showScheduleSavePopup();  
		  });
		  
		 
		},
		scheduleModifyBtnEvent: function() {
			 $("a[name='modifyBtn']").on('click',function(){
				  var resveNo = $(this).data("resveno")
				  scheduleList.popup.showRowScheduleSavePopup(resveNo);  
			  });	
		},
		//TODO: 메시지 처리
		scheduleDeleteBtnEvent:function(){
			  
			  $("button#deleteBtn").on('click',function(){
				  var params = [] ;
				  var data = null;
				  var resveNoSplit = null;
				  var delItemCnt = 0;
				  var resveItemCnt = 0;
				  var careItemCnt = 0;
				  var meassage = '';
				  $('tbody#scheduleList input:checkbox:checked').each(function(){
					
					  data = scheduleList.list.getRowData($(this).val())
				     
					  resveItemCnt+=Number(data.RESVE_COMPT_CNT);
					  careItemCnt+=Number(data.CARE_COMPT_CNT);
					  resveNoSplit = data.RESVE_NO_LIST.split(",")
					 for (var i = 0; i < resveNoSplit.length; i++) {
						 delItemCnt++;  
						 params.push({resveDate : data.RESVE_DE , mssrCode :data.MSSR_EMPNO, bldCode : data.BLD_CODE , RESVE_NO:resveNoSplit[i]});	
					 }
					 
				  });
				  
				  if(params.length == 0){
					  alertPopup('삭제할 스케쥴을 선택하세요.');
					  return false;
				  }
				  
				  if(careItemCnt > 0){
						alertPopup('삭제되는 시간에 이미 \n케어완료 된 건이 있습니다.\n근무시간 확인 후 재 요청 바랍니다.');
						 return false;
				  }
				  
				  if(resveItemCnt > 0){
					  meassage = '스케쥴 삭제 시 기존 예약 '+resveItemCnt +'건이\n 자동 취소 됩니다. 삭제하시겠습니까';
				  }
				  
				  confirmPopup( meassage||'총' + delItemCnt + '건을 삭제하시겠습니까?', function(){					  					
					$.ajax({
							url: ROOT + '/mssr/scheduleDelete',
							type: 'POST',
							data:{params:JSON.stringify(params)}  ,
							success : function(res){			
								scheduleList.list.renderScheduleList();
								$("#checkAll").prop('checked',false);
								alertPopup('삭제되었습니다.');
							},
							error : function(err) {
								console.error(err)
							}
					  });
				  })
				  
			  });	
			
		}
	},
	
	refresh :{ 
		inputbox:function(){
			$("#checkAll").prop('checked',false);
			
		}
		
	},
	
	validation: {
		//날짜 필드 값 체크
		dateCheck: function() {
			var fromDate = $('input#from_date').val().trim().split('-');
			var toDate = $('input#to_date').val().trim().split('-');
			
			var fromdt = fromDate[0] + fromDate[1] + fromDate[2];
			var todt = toDate[0] + toDate[1] + toDate[2];
			
			if (fromdt.length !== 8) {
				alert('시작날짜 형식이 잘못되었습니다.');
				return false;
			}
			
			if (todt.length !== 8) {
				alert('종료날짜 형식이 잘못되었습니다.');
				return false;
			}
			
			if (fromdt > todt) {
				alert('시작날짜가 종료날짜가 클 수 없습니다.');
				return false;
			}
			
			scheduleList.list.params.fromDate = fromdt;
			scheduleList.list.params.toDate = todt;
			
			return true;
		}
	},
	
	
	popup: {
		showScheduleSavePopup: function() {
			
			$('#layer_pop06').load(ROOT + '/mssr/pop/scheduleCreate',null, function(res) {
				openLayerPopup('layer_pop06');
			});
		},
        showRowScheduleSavePopup: function(resveno) {	
			$('#layer_pop07').load(ROOT + '/mssr/pop/scheduleModify',scheduleList.list.getRowData(resveno), function(res) {
				openLayerPopup('layer_pop07');
			});
		}

	},
	checkboxEvent:{
		checkall:function(){
			
			$("#checkAll").on("change",function(){
				 var chkstat = $("#checkAll").prop("checked");
				$("#scheduleList input[type=checkbox]").each(function(){
				
				    $(this).prop("checked",chkstat)
				});
					
				
			})
		}	
		
		
	}

}

$(document).ready(function() {	
	scheduleList.init();
});




