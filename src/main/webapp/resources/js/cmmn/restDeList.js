/**
 * 관리사 스케쥴 List
 */
var RestDeList = {
	// 초기화
	init: function() {
		// loadCodeSelect(); //콤보박스 공통코드 세팅
		RestDeList.datepicker.setDefaultValue();
	    RestDeList.list.renderRestDeList(); // 목록 조회 후 렌더
		RestDeList.button.listBtnClickEvent(); // 조회 버튼 클릭 이벤트
		RestDeList.button.restDeRegisterCreateBtnEvent();
		RestDeList.button.restDeRegisterDeleteBtnEvent();
	   /* RestDeList.button.restDeExcelDownBtnEvent(); */
		RestDeList.checkboxEvent.checkall();
	},
	
	datepicker: {
		setDefaultValue: function() { // 기본 날짜 세팅
			var fromDate = moment().format('YYYY-MM-DD'); // 오늘 날짜
			var toDate = moment().add(1, 'M').format('YYYY-MM-DD'); // 30일 전 날짜
			
			$('input#from_date').val(fromDate);
			$('input#to_date').val(toDate);
			
			RestDeList.list.params.fromDate = moment().format('YYYYMMDD');
			RestDeList.list.params.toDate = moment().add(1, 'M').format('YYYYMMDD');
		}
	},
	
	list: {
		
		// 목록 조회 시 사용하는 파라미터
		params: {
			pageNo: 1, // 조회할 페이지 번호
			rowPerPage: 10, // 한 페이지 당 조회할 ROW 수
			startRow: 0 // 조회 시작할 ROW
		},
		
		// 현재 조회된 데이터 저장
		dataList: [],
		
		// 예약 목록 조회
		selectRestDeList: function() {
			
			RestDeList.list.params.startRow = parseInt((RestDeList.list.params.pageNo - 1 ) * RestDeList.list.params.rowPerPage);
			var deferred = $.Deferred();
			
			$.ajax({
				url: ROOT + '/cmmn/selectRestDeList',
				data: RestDeList.list.params,
				success: function(res) {
					if (res.status === 200) {
							deferred.resolve(res);
							RestDeList.list.dataList = res.list;
					   	
						
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
		exportRestDeList: function() {
			
			RestDeList.list.params.startRow = parseInt((RestDeList.list.params.pageNo - 1 ) * RestDeList.list.params.rowPerPage);
			var deferred = $.Deferred();
			
			$.ajax({
				url: ROOT + '/cmmn/exportRestDeList',
				data: RestDeList.list.params,
				success: function(res) {
					if (res.status === 200) {
							deferred.resolve(res);
							RestDeList.list.dataList = res.list;
					   	
						
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
		 
		// 조회된 예약 목록 데이터를 가지고 화면에 목록 생성
		renderRestDeList: function() {
			$.when(RestDeList.list.selectRestDeList()).done(function(result) {

				$('tbody#restDeList').empty();
				
				var resultList = result.list;
				var restDeListHtml = [];
				var restdeDate = '';
				
				if(result.list == 0){
					restDeListHtml.push('<tr>');
					restDeListHtml.push('<td colspan=3 >검색 결과가 없습니다</td>');
					restDeListHtml.push('</tr>');
				}else{
					
					RestDeList.paging.params.totalCount = resultList[0].total_cnt; 
						for (var i in resultList) {	
							restdeDate =  moment(resultList[i].RESTDE_DATE,"YYYYMMDD",'ko'); 
							restDt = restdeDate.format('YYYY-MM-DD (ddd)');
							restDeListHtml.push('<tr>');
							restDeListHtml.push('	<td><input type="checkbox" value="'+ resultList[i].RESTDE_NO +'"></td>');
							restDeListHtml.push('	<td>' + restDt + '</td>');
							restDeListHtml.push('	<td><a name="modifyBtn" herf="#none" data-restdeno="'+resultList[i].RESTDE_NO+'" >' + resultList[i].RESTDE_NAME + '</td>');
							restDeListHtml.push('</tr>');
					}
					
				}
				$('tbody#restDeList').html(restDeListHtml.join(''))
				RestDeList.paging.renderPaging();
				RestDeList.button.restDeRegisterCreateBtnEvent();
				
			});
		}
	},
	
	
	paging: {
		// 페이징 처리를 위한 파라미터
		params: {
			totalCount: null, // list 의 전체 row count
			first: null, // 현재 보여질 첫페이지
			last: null, // 현재 보여질 마지막페이지
			prev: null, // 이전페이지
			next: null // 다음페이지
		},
		
		// 페이지 이동 영역 생성
		renderPaging: function() {
			
			var currentIndex = RestDeList.list.params.pageNo; // 현재 페이지 위치
			var rowPerPage = RestDeList.list.params.rowPerPage; // 페이지 당 레코드 수
			var totalCount = RestDeList.paging.params.totalCount; // list 의 전체
																	// row count
			var totalIndexCount = Math.ceil(totalCount / rowPerPage); // 전체
																		// 인덱스 수
			var currentBlock = Math.ceil(currentIndex / 10) // 현재 블럭의 시작 페이지 번호
			$("div#pagingArea").empty();
			
			var preStr = '';
			var postStr = '';
			var pageNumStr = '';
			
			
			var prev = (parseInt((currentIndex-1)/10)*10) - 9 > 0 ? (parseInt((currentIndex-1)/10)*10) - 9 : 1; // 이전
																												// 블록의
																												// 시작페이지
			var next = (parseInt((currentIndex-1)/10)+1) * 10 + 1 < totalIndexCount ? (parseInt((currentIndex-1)/10)+1) * 10 + 1 : totalIndexCount; // 다음
																																					// 블록의
																																					// 시작
																																					// 페이지
			var first = (currentBlock - 1) * 10 + 1; // 현재 블록의 시작 페이지
			var last = currentBlock * 10; // 현재 블록의 끝 페이지
			
			
			RestDeList.paging.params.first = first;
			RestDeList.paging.params.last = last;
			RestDeList.paging.params.prev = prev;
			RestDeList.paging.params.next = next;
			
			if (totalIndexCount > 10) { // 전체 인덱스가 10이 넘을 경우, first + prev 버튼
				preStr += '<a href="#none" class="first" id="firstBtn"><img src="' + IMG + '/common/btn_first.gif"></a>'
					   +  '<a href="#none" class="prev" id="prevBtn"><img src="' + IMG + '/common/btn_prev.gif"></a>';
			} else if (totalIndexCount <=10 && totalIndexCount > 1) { // 전체
																		// 인덱스가
																		// 10보다
																		// 작을
																		// 경우,
																		// first
																		// 버튼
				preStr += '<a href="#none" class="first" id="firstBtn"><img src="' + IMG + '/common/btn_first.gif"></a>';
			}

			
			if (totalIndexCount > 10) { // 전체 인덱스가 10이 넘을 경우, next + last 버튼
				postStr += '<a href="#none" class="next" id="nextBtn"><img src="' + IMG + '/common/btn_next.gif"></a>'
						+  '<a href="#none" class="last" id="lastBtn"><img src="' + IMG + '/common/btn_last.gif"></a>';
			} else if (totalIndexCount <=10 && totalIndexCount > 1) { // 전체
																		// 인덱스가
																		// 10보다
																		// 작을
																		// 경우,
																		// last
																		// 버튼
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
			
			RestDeList.paging.button.allPagingBtnEventBinding();
		},
		
		button: {
			allPagingBtnEventBinding: function() {
				RestDeList.paging.button.pageNumEvent();
				RestDeList.paging.button.prevBtnEvent();
				RestDeList.paging.button.nextBtnEvent();
				RestDeList.paging.button.firstBtnEvent();
				RestDeList.paging.button.lastBtnEvent();
			},
			
			pageNumEvent: function() {
				$('a.num').off();
				$('a.num').on('click', function() {	
					if (RestDeList.list.params.pageNo == parseInt(this.innerHTML)) {
						return false;
					}
					
					RestDeList.list.params.pageNo = parseInt(this.innerHTML);
					RestDeList.list.renderRestDeList();
				});
			},
			
			prevBtnEvent: function() {
				$('a#prevBtn').off();
				$('a#prevBtn').on('click', function() {
					if (RestDeList.list.params.pageNo == RestDeList.paging.params.prev) {
						return false;
					}
					RestDeList.list.params.pageNo = RestDeList.paging.params.prev;
					RestDeList.list.renderRestDeList();
				});
			},
			
			nextBtnEvent: function() {
				$('a#nextBtn').off();
				$('a#nextBtn').on('click', function() {
					if (RestDeList.list.params.pageNo == RestDeList.paging.params.next) {
						return false;
					}
					RestDeList.list.params.pageNo = RestDeList.paging.params.next;
					RestDeList.list.renderRestDeList();
				});
			},
			
			firstBtnEvent: function() {
				$('a#firstBtn').off();
				$('a#firstBtn').on('click', function() {
					if (RestDeList.list.params.pageNo == 1) {
						return false;
					}
				
					RestDeList.list.params.pageNo = 1;
					RestDeList.list.renderRestDeList();
				});
			},
			
			lastBtnEvent: function() {
				$('a#lastBtn').off();
				$('a#lastBtn').on('click', function() {
					var rowPerPage = RestDeList.list.params.rowPerPage; // 페이지 당
																		// 레코드 수
					var totalCount = RestDeList.paging.params.totalCount; // list
																			// 의 전체
																			// row
																			// count
					var totalIndexCount = Math.ceil(totalCount / rowPerPage); // 전체
																				// 인덱스
																				// 수
					
					if (RestDeList.list.params.pageNo == totalIndexCount) {
						return false;
					}
					
					RestDeList.list.params.pageNo = totalIndexCount;
					RestDeList.list.renderRestDeList();
				});
			}
		}
		
	},
	
	
	
	button: {
		
		// 조회 버튼 클릭 이벤트
		listBtnClickEvent: function() {
			$('button#listBtn').on('click', function(e) {
		
				RestDeList.validation.dateCheck();
				// 조회 페이지는 1로 초기화 param 세팅
				RestDeList.list.params.pageNo = 1;
			
				// 목록 조회 및 렌더 실행
				RestDeList.list.renderRestDeList();
			});
		},
		// 관리자등록
		restDeRegisterCreateBtnEvent: function(){
			
			$("#createBtn").off('click').on('click',function(){  
			  var popParam= { RESTDE_NO:$(this).data("restdeno")}
			    RestDeList.popup.showRestDeRegisterSavePopup(popParam);  
		   });
		  
		  $("a[name='modifyBtn']").on('click',function(){  
			  var  popParam= { RESTDE_NO:$(this).data("restdeno")}
			    RestDeList.popup.showRestDeRegisterSavePopup(popParam);  
		  });
		},
		restDeRegisterDeleteBtnEvent:function(){
			
			$("#deleteBtn").on('click',function(){  
				 
						  var delRestDeNo = [] ;
						  
						  var meassage = '';
						  $('tbody#restDeList input:checkbox:checked').each(function(){
							  
							  	delRestDeNo.push($(this).val());	
							 
						  });
						  
						  if(delRestDeNo.length == 0){
							  alertPopup('삭제할  휴일을  선택하세요.');
							  return false;
						  }
						  
						  confirmPopup('총' + delRestDeNo.length + '건을 삭제하시겠습니까?', function(){					  					
							$.ajax({
									url: ROOT + '/cmmn/restDeDelete',
									type: 'POST',
									data:{deleteRestNo:delRestDeNo.toString()}  ,
									success : function(res){
									
										RestDeList.list.renderRestDeList();
										$("#checkAll").prop('checked',false);
										alertPopup('삭제되었습니다.');
									},
									error : function(err) {
										console.error(err)
									}
							  });
						  })
						  
					  });					
			
		},

		restDeExcelDownBtnEvent:function(){
			  
			  $("button#excelDownBtn").on('click',function(){
				  RestDeList.list.exportRestDeList();
			  });	
		}
	},

	
	validation: {
		// 날짜 필드 값 체크
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
			
			RestDeList.list.params.fromDate = fromdt;
			RestDeList.list.params.toDate = todt;
			
			return true;
		}
	},
	
	
	popup: {
		showRestDeRegisterSavePopup: function(popParam) {
			$('#layer_pop15').load(ROOT + '/cmmn/pop/RestDeRegister',popParam, function(res) {
				openLayerPopup('layer_pop15');
			});
		}
	},
	checkboxEvent:{
		checkall:function(){
			
			$("#checkAll").on("change",function(){
				 var chkstat = $("#checkAll").prop("checked");
				$("#restDeList input[type=checkbox]").each(function(){
				
				    $(this).prop("checked",chkstat)
				});
					
					
				
			})
		}	
		
		
	}

}

$(document).ready(function() {	
	RestDeList.init();
});




