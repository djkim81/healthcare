/**
 * 관리사 등록 List
 */
var healthkeperList = {
	// 초기화
	init: function() {
		loadCodeSelect(); //콤보박스 공통코드 세팅
		healthkeperList.combobox.bldComboSelect();
		healthkeperList.list.renderhealthkeperList(); //목록 조회 후 렌더
		healthkeperList.button.listBtnClickEvent(); //조회 버튼 클릭 이벤트
		healthkeperList.button.healthkeperCreateBtnEvent();
		
	},
	
	combobox: {
		bldComboSelect: function() {
			$('#bldCombo').on('change', function() {
				var bldCode = $(this).val();
			})
		},
	
		setHffcCombo: function(bldCode) {
		
				$('#hffcCombo').empty();
				var hffcComboHtml = ['<option value="">전체</option>'];

				hffcComboHtml.push('<option value="Y">Y</option>');
				hffcComboHtml.push('<option value="N">N</option>');
	
				$('#hffcCombo').html(hffcComboHtml.join(''));
				
			
		}
		
	},

	list: {
		
		//목록 조회 시 사용하는 파라미터
		params: {
			pageNo: 1, //조회할 페이지 번호
			rowPerPage: 10, //한 페이지 당 조회할 ROW 수
			bldCode: '', //사옥코드
			startRow: 0, //조회 시작할 ROW
			hffc_yn : '' //재직여부
		},
		
		//현재 조회된 데이터 저장
		dataList: [],
		
		//헬스키퍼 조회
		selectHealthkeperList: function() {
			
			healthkeperList.list.params.startRow = parseInt((healthkeperList.list.params.pageNo - 1 ) * healthkeperList.list.params.rowPerPage);
			
			var deferred = $.Deferred();
			
			$.ajax({
				url: ROOT + '/mssr/selectHealthkeperList',
				data: healthkeperList.list.params,
				success: function(res) {
					if (res.status === 200) {
						deferred.resolve(res);
						healthkeperList.list.dataList = res.list;
						
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
		
		
		//조회된 관리사 현황 데이터를 가지고 화면에 목록 생성
		renderhealthkeperList: function() {
			$.when(healthkeperList.list.selectHealthkeperList()).done(function(result) {

				$('tbody#healthkeperList').empty();
				
				var resultList = result.list;
				var healthkeperListHtml = [];
				var regDt = '';
				var sexdstn = '';
				
				healthkeperList.paging.params.totalCount = result.customs.totalCount;
				
				if(result.customs.totalCount == 0){
					healthkeperListHtml.push('<tr>');
					healthkeperListHtml.push('<td colspan=7 >검색 결과가 없습니다</td>');
					healthkeperListHtml.push('</tr>');
				}else{
					for (var i in resultList) {
						
						var reg_dt = resultList[i].REG_DT;
						regDt = reg_dt//.substr(0,10);
						
						var sexdstn = (resultList[i].MSSR_SEXDSTN == 'M') ? '남' : '여';
						
						healthkeperListHtml.push('<tr>');
						healthkeperListHtml.push('	<td>' + resultList[i].MSSR_EMPNO + '</td>');
						healthkeperListHtml.push('	<td>' + resultList[i].BLD_NM + '</td>');
						healthkeperListHtml.push('	<td>' + resultList[i].MSSR_NAME + '</td>');
						healthkeperListHtml.push('	<td><a name="modifyBtn" herf="#none" data-healthkeperno="'+resultList[i].MSSR_EMPNO+'" >' + resultList[i].MSSR_NCNM + '</td>');
						healthkeperListHtml.push('	<td>' + sexdstn + '</td>');
						healthkeperListHtml.push('	<td>' + regDt + '</td>');
						healthkeperListHtml.push('	<td>' + resultList[i].HFFC_YN + '</td>');
						healthkeperListHtml.push('</tr>');
					}
				}
				
				$('tbody#healthkeperList').html(healthkeperListHtml.join(''));
				healthkeperList.paging.renderPaging();

				healthkeperList.button.healthkeperModifyBtnEvent();
			});
		},
		
		//현재 리스트에서 예약번호로 해당 ROW 의 데이터를 가져옴
		getRowData: function(healthkeperNo) {
			var rowDataList = healthkeperList.list.dataList;
			var rowData;
			
			for (var i in rowDataList) {
				if (rowDataList[i].MSSR_EMPNO == healthkeperNo) {
					rowData = rowDataList[i];
					break;
				}
			}
			
			return rowData;
		},

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
			
			var currentIndex = healthkeperList.list.params.pageNo; //현재 페이지 위치
			var rowPerPage = healthkeperList.list.params.rowPerPage; //페이지 당 레코드 수
			var totalCount = healthkeperList.paging.params.totalCount; //list 의 전체 row count
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
			
			
			healthkeperList.paging.params.first = first;
			healthkeperList.paging.params.last = last;
			healthkeperList.paging.params.prev = prev;
			healthkeperList.paging.params.next = next;
			
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

			for (var i=first; i<(first+last); i++) {
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
			
			healthkeperList.paging.button.allPagingBtnEventBinding();
		},
		
		button: {
			allPagingBtnEventBinding: function() {
				healthkeperList.paging.button.pageNumEvent();
				healthkeperList.paging.button.prevBtnEvent();
				healthkeperList.paging.button.nextBtnEvent();
				healthkeperList.paging.button.firstBtnEvent();
				healthkeperList.paging.button.lastBtnEvent();
			},
			
			pageNumEvent: function() {
				$('a.num').off();
				$('a.num').on('click', function() {	
					if (healthkeperList.list.params.pageNo == parseInt(this.innerHTML)) {
						return false;
					}
					
					healthkeperList.list.params.pageNo = parseInt(this.innerHTML);
					healthkeperList.list.renderhealthkeperList();
				});
			},
			
			prevBtnEvent: function() {
				$('a#prevBtn').off();
				$('a#prevBtn').on('click', function() {
					if (healthkeperList.list.params.pageNo == healthkeperList.paging.params.prev) {
						return false;
					}
					healthkeperList.list.params.pageNo = healthkeperList.paging.params.prev;
					healthkeperList.list.renderhealthkeperList();
				});
			},
			
			nextBtnEvent: function() {
				$('a#nextBtn').off();
				$('a#nextBtn').on('click', function() {
					if (healthkeperList.list.params.pageNo == healthkeperList.paging.params.next) {
						return false;
					}
					healthkeperList.list.params.pageNo = healthkeperList.paging.params.next;
					healthkeperList.list.renderhealthkeperList();
				});
			},
			
			firstBtnEvent: function() {
				$('a#firstBtn').off();
				$('a#firstBtn').on('click', function() {
					if (healthkeperList.list.params.pageNo == 1) {
						return false;
					}
				
					healthkeperList.list.params.pageNo = 1;
					healthkeperList.list.renderhealthkeperList();
				});
			},
			
			lastBtnEvent: function() {
				$('a#lastBtn').off();
				$('a#lastBtn').on('click', function() {
					var rowPerPage = healthkeperList.list.params.rowPerPage; //페이지 당 레코드 수
					var totalCount = healthkeperList.paging.params.totalCount; //list 의 전체 row count
					var totalIndexCount = Math.ceil(totalCount / rowPerPage); //전체 인덱스 수
					
					if (healthkeperList.list.params.pageNo == totalIndexCount) {
						return false;
					}
					
					healthkeperList.list.params.pageNo = totalIndexCount;
					healthkeperList.list.renderhealthkeperList();
				});
			}
		}
		
	},
	
	
	
	button: {
		
		//조회 버튼 클릭 이벤트
		listBtnClickEvent: function() {
			$('button#listBtn').on('click', function(e) {
				healthkeperList.list.params.pageNo = 1;
				healthkeperList.list.params.bldCode = $('#bldCombo').val(); //사옥 param 세팅
				healthkeperList.list.params.hffc_yn = $('#hffcCombo').val(); //재직여부 param 세팅
				healthkeperList.list.renderhealthkeperList();
			});
		},
		//관리사 등록
		healthkeperCreateBtnEvent: function(){
		  $("button#createBtn").on('click',function(){
			  healthkeperList.popup.showhealthkeperSavePopup();  
		  });
		},
		healthkeperModifyBtnEvent: function() {
			 $("a[name='modifyBtn']").on('click',function(){
				  var healthkeperNo = $(this).data("healthkeperno")
				  healthkeperList.popup.showRowhealthkeperSavePopup(healthkeperNo);  
			  });	
		},
		
	},		
	
	popup: {
		showhealthkeperSavePopup: function() {
			
			$('#layer_pop16').load(ROOT + '/mssr/pop/healthkeperCreate',null, function(res) {
				openLayerPopup('layer_pop16');
			});
		},
        showRowhealthkeperSavePopup: function(healthkeperno) {	
			$('#layer_pop17').load(ROOT + '/mssr/pop/healthkeperModify',healthkeperList.list.getRowData(healthkeperno), function(res) {
				openLayerPopup('layer_pop17');
			});
		}

	}

}

$(document).ready(function() {	
	healthkeperList.init();
});




