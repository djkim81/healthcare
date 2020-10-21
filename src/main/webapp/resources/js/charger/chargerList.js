/**
 * 관리사 스케쥴 List
 */
var ChargerList = {
	// 초기화
	init: function() {
		loadCodeSelect(undefined, '.search_field'); //콤보박스 공통코드 세팅
	
	    ChargerList.list.renderChargerList(); //목록 조회 후 렌더
		ChargerList.button.listBtnClickEvent(); //조회 버튼 클릭 이벤트
		ChargerList.button.chargerRegisterCreateBtnEvent();
		ChargerList.button.chargerRegisterDeleteBtnEvent();
		ChargerList.checkboxEvent.checkall();
	},
	
	
	cmmnCode: {
		allCodeList: [],
		getAllCodeList: function() {
			$.ajax({
				url: ROOT + '/cmmn/allCodeList',
				success: function(res) {
					console.log('allCodeList', res);
					if (res.status === 200) {
						ChargerList.cmmnCode.allCodeList = res.list;
					}
				},
				error: function(err) {
					console.error(err);
				}
			})
		},
		codeToName: function(code) {
			var allCodeList = ChargerList.cmmnCode.allCodeList;
			
			if (allCodeList.length == 0) {
				ChargerList.cmmnCode.getAllCodeList();
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
			
			ChargerList.list.params.fromDate = moment().format('YYYYMMDD');
			ChargerList.list.params.toDate = moment().add(1, 'M').format('YYYYMMDD');
		}
	},
	
	list: {
		
		//목록 조회 시 사용하는 파라미터
		params: {
			pageNo: 1, //조회할 페이지 번호
			rowPerPage: 10, //한 페이지 당 조회할 ROW 수
			bldCode: '', //사옥코드
			authCode: '', //관리사 사번
			startRow: 0 //조회 시작할 ROW
		},
		
		//현재 조회된 데이터 저장
		dataList: [],
		
		//예약 목록 조회
		selectChargerList: function() {
			
			ChargerList.list.params.startRow = parseInt((ChargerList.list.params.pageNo - 1 ) * ChargerList.list.params.rowPerPage);
			console.log(ChargerList.list.params)
			var deferred = $.Deferred();
			
			$.ajax({
				url: ROOT + '/charger/selectChargerList',
				data: ChargerList.list.params,
				success: function(res) {
					console.log('ChargerList', res);
					if (res.status === 200) {
						   console.log(res)
							deferred.resolve(res);
							ChargerList.list.dataList = res.list;
					   	
						
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
		renderChargerList: function() {
			$.when(ChargerList.list.selectChargerList()).done(function(result) {

				$('tbody#chargerList').empty();
				
				var resultList = result.list;
				var chargerListHtml = [];
				var btnText = '';
				var btnClass = '';
				var resveDt = '';
				var sexdstn = '';
				
				ChargerList.paging.params.totalCount = resultList[0].total_cnt;
				if( resultList[0].total_cnt == 0){
					chargerListHtml.push('<tr>');
					chargerListHtml.push('<td colspan=8 >검색 결과가 없습니다</td>');
					chargerListHtml.push('</tr>');
				}else{
		
						for (var i in resultList) {						
							chargerListHtml.push('<tr>');
							chargerListHtml.push('	<td><input type="checkbox" value="'+ resultList[i].EMPNO +'"></td>');
							chargerListHtml.push('	<td>' +resultList[i].NUM + '</td>');
							chargerListHtml.push('	<td>' + resultList[i].EMPNO + '</td>');
							chargerListHtml.push('	<td><a name="modifyBtn" herf="#none" data-chargerempno="'+resultList[i].EMPNO+'" >' + resultList[i].HNAME + '</td>');
							chargerListHtml.push('	<td>' + resultList[i].DEPTNM + '</td>');
							chargerListHtml.push('	<td>' + resultList[i].AUTH_NAME + '</td>');
							chargerListHtml.push('	<td>' + resultList[i].BLD_NAME + '</td>');
							chargerListHtml.push('</tr>');
					}
					
				}
				$('tbody#chargerList').html(chargerListHtml.join(''))
				ChargerList.paging.renderPaging();
				ChargerList.button.chargerRegisterCreateBtnEvent();
				
			});
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
			
			var currentIndex = ChargerList.list.params.pageNo; //현재 페이지 위치
			var rowPerPage = ChargerList.list.params.rowPerPage; //페이지 당 레코드 수
			var totalCount = ChargerList.paging.params.totalCount; //list 의 전체 row count
			var totalIndexCount = Math.ceil(totalCount / rowPerPage); //전체 인덱스 수
			var currentBlock = Math.ceil(currentIndex / 10) //현재 블럭의 시작 페이지 번호
			/*console.log(currentIndex);
			console.log(totalCount);
			console.log(rowPerPage);
			console.log(rowPerPage);*/
			$("div#pagingArea").empty();
			
			var preStr = '';
			var postStr = '';
			var pageNumStr = '';
			
			
			var prev = (parseInt((currentIndex-1)/10)*10) - 9 > 0 ? (parseInt((currentIndex-1)/10)*10) - 9 : 1; //이전 블록의 시작페이지
			var next = (parseInt((currentIndex-1)/10)+1) * 10 + 1 < totalIndexCount ? (parseInt((currentIndex-1)/10)+1) * 10 + 1 : totalIndexCount; //다음 블록의 시작 페이지
			//var first = (parseInt((currentIndex-1) / 10) * 10) + 1;
			//var last = (parseInt(totalIndexCount/10) == parseInt(currentIndex/10)) ? totalIndexCount%10 : 10;
			var first = (currentBlock - 1) * 10 + 1; //현재 블록의 시작 페이지
			var last = currentBlock * 10; //현재 블록의 끝 페이지
			
			
			ChargerList.paging.params.first = first;
			ChargerList.paging.params.last = last;
			ChargerList.paging.params.prev = prev;
			ChargerList.paging.params.next = next;
			
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
			
			ChargerList.paging.button.allPagingBtnEventBinding();
		},
		
		button: {
			allPagingBtnEventBinding: function() {
				ChargerList.paging.button.pageNumEvent();
				ChargerList.paging.button.prevBtnEvent();
				ChargerList.paging.button.nextBtnEvent();
				ChargerList.paging.button.firstBtnEvent();
				ChargerList.paging.button.lastBtnEvent();
			},
			
			pageNumEvent: function() {
				$('a.num').off();
				$('a.num').on('click', function() {	
					if (ChargerList.list.params.pageNo == parseInt(this.innerHTML)) {
						return false;
					}
					
					ChargerList.list.params.pageNo = parseInt(this.innerHTML);
					ChargerList.list.renderChargerList();
				});
			},
			
			prevBtnEvent: function() {
				$('a#prevBtn').off();
				$('a#prevBtn').on('click', function() {
					if (ChargerList.list.params.pageNo == ChargerList.paging.params.prev) {
						return false;
					}
					console.log(ChargerList.paging.params.prev)
					ChargerList.list.params.pageNo = ChargerList.paging.params.prev;
					ChargerList.list.renderChargerList();
				});
			},
			
			nextBtnEvent: function() {
				$('a#nextBtn').off();
				$('a#nextBtn').on('click', function() {
					if (ChargerList.list.params.pageNo == ChargerList.paging.params.next) {
						return false;
					}
					console.log(ChargerList.paging.params.prev)
					ChargerList.list.params.pageNo = ChargerList.paging.params.next;
					ChargerList.list.renderChargerList();
				});
			},
			
			firstBtnEvent: function() {
				$('a#firstBtn').off();
				$('a#firstBtn').on('click', function() {
					if (ChargerList.list.params.pageNo == 1) {
						return false;
					}
				
					ChargerList.list.params.pageNo = 1;
					ChargerList.list.renderChargerList();
				});
			},
			
			lastBtnEvent: function() {
				$('a#lastBtn').off();
				$('a#lastBtn').on('click', function() {
					var rowPerPage = ChargerList.list.params.rowPerPage; //페이지 당 레코드 수
					var totalCount = ChargerList.paging.params.totalCount; //list 의 전체 row count
					var totalIndexCount = Math.ceil(totalCount / rowPerPage); //전체 인덱스 수
					
					if (ChargerList.list.params.pageNo == totalIndexCount) {
						return false;
					}
					
					ChargerList.list.params.pageNo = totalIndexCount;
					ChargerList.list.renderChargerList();
				});
			}
		}
		
	},
		
	button: {
		
		//조회 버튼 클릭 이벤트
		listBtnClickEvent: function() {
			$('button#listBtn').on('click', function(e) {
			/*	//날짜 validation 실행
				ChargerList.validation.dateCheck();*/
				
				//조회 페이지는 1로 초기화 param 세팅
				ChargerList.list.params.pageNo = 1;
				
				//사옥 param 세팅
				ChargerList.list.params.bldCode = $('#search_bldCombo').val();
				
				//관리사 param 세팅
				ChargerList.list.params.authCode = $('#search_authCombo').val();
				
				//목록 조회 및 렌더 실행
				ChargerList.list.renderChargerList();
			});
		},
		//관리자등록		
		chargerRegisterCreateBtnEvent: function(){
		  $("#createBtn").off('click').on('click',function(){  // 신규 등록
			   var popParam = { EMPNO:$(this).data("chargerempno")}
			    ChargerList.popup.showChargerRegisterSavePopup(popParam);  
		  });
		  
		  $("a[name='modifyBtn']").on('click',function(){  // 수정 
			   var popParam = { EMPNO:$(this).data("chargerempno")}
			    ChargerList.popup.showChargerRegisterSavePopup(popParam);  
		  });
		},
		chargerRegisterDeleteBtnEvent:function(){
		$("#deleteBtn").on('click',function(){  
					  var delEmpNo = [] ;
					  var meassage = '';
					  $('tbody#chargerList input:checkbox:checked').each(function(){
						  
						  	delEmpNo.push($(this).val());	
						 
					  });
					  
					  if(delEmpNo.length == 0){
						  alertPopup('삭제할 스케쥴을 선택하세요.');
						  return false;
					  }
					  
					  confirmPopup('총' + delEmpNo.length + '건을 삭제하시겠습니까?', function(){					  					
						$.ajax({
								url: ROOT + '/charger/chargerDelete',
								type: 'POST',
								data:{deleteEmpNo:delEmpNo.toString()}  ,
								success : function(res){
								
									ChargerList.list.renderChargerList();
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
			
			ChargerList.list.params.fromDate = fromdt;
			ChargerList.list.params.toDate = todt;
			
			return true;
		}
	},
	
	
	popup: {
		showChargerRegisterSavePopup: function(popParam) {
			$('#layer_pop10').load(ROOT + '/charger/pop/ChargerRegister',popParam, function(res) {
				openLayerPopup('layer_pop10');
			});
		}
	},
	checkboxEvent:{
		checkall:function(){
			
			$("#checkAll").on("change",function(){
				 var chkstat = $("#checkAll").prop("checked");
				$("#chargerList input[type=checkbox]").each(function(){
				
				    $(this).prop("checked",chkstat)
				});
					
					
				
			})
		}	
		
		
	}

}

$(document).ready(function() {	
	ChargerList.init();
});




