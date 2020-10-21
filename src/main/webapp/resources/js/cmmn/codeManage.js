/**
 * 공통코드 List
 */
$(document).ready(function() {
	codeManageList.init();
});

var codeManageList = {
	//화면 초기화
	init: function() { 
		//대분류 sBox 만들기
		codeManageList.comboDataSet(function() { codeManageList.comboBox.bedComboBoxSet(); }, "#search_table");
		//대분류 onChange 추가 
		//	- BED선택 시 중분류 sBox 만들기
		codeManageList.comboBox.bedComboEventBinding();
		codeManageList.list.renderCodeManageList();
		//선택 조건에 맞는 select 만들기
		codeManageList.button.listBtnClickEvent(); //조회 버튼 클릭 이벤트
		codeManageList.button.codeManageCreateBtnEvent(); //등록 버튼 클릭 이벤트
		codeManageList.button.codeManageDeleteBtnEvent(); //삭제 버튼 클릭 이벤트
		codeManageList.checkboxEvent.checkall();


	},
	
	comboDataSet: function(cb, selector) {
		var $container = selector ? $(selector) : $(document);

		$container.find('select[data-code-type]').each(function(idx, select) {
			var cType = $(select).data('code-type');	//코드타입(대)
			var tyl = $("#codeTyl").val();
			var tys = $("#codeTys").val();
			var empStr = $(select).data('empty-str');

			if(tyl === 'BLD') {
				$(select).on('change', function() {
					$('[data-code-tyl=BED]').data('code-tys', $(this).val()).empty();
				});
			}
			//init시엔 selectBox들이 동시에 만들어짐 - 변수 선택 불가
			$.ajax({
				url: ROOT + '/cmmn/codeKindList',
				data: {codeType: cType, codeTyl: tyl, codeTys: tys || ''},
				success : function(res) {
					if(res.status === 200) {
						var options = res.list.map(function(data) {
							return $('<option>').val(data.CODE).text(data.CODE_NM);
						});
						
						if(empStr) {
							$(select).append($('<option>').val("").text(empStr));
						}
						$(select).append(options);
						
						if(cb) cb(); //콜백이 있다면 실행
					}
				},
				error : function(err) {
					console.error(err)
				}
			});
		});
	},
	
	//콤보박스 세팅
	comboBox: {
		//대분류에서 베드 선택 시 사옥 선택 콤보박스 표출Y/N
		bedComboEventBinding: function() {
			$('#codeType').on('change', function() {
				codeManageList.comboBox.bedComboBoxSet();
			});
		},
		
		bedComboBoxSet: function() {
			if($('#codeType').val() === 'BED') {
				$('.midCate').show();
				$('#codeTys').prop('disabled', false);
			} else {
				$('.midCate').hide();
				$('#codeTys').prop('disabled', true);
				$('#codeTys option:first').prop('selected', true);
			}
		}
	},
	
	list: {
		//목록 조회 시 사용하는 파라미터
		params: {
			pageNo: 1, //조회할 페이지 번호
			rowPerPage: 10, //한 페이지 당 조회할 ROW 수
			startRow: 0 //조회 시작할 ROW
		},
		
		//현재 조회된 데이터 저장
		dataList: [],
		
		//예약 목록 조회
		selectCodeManageList: function() {
			codeManageList.list.params.startRow = parseInt((codeManageList.list.params.pageNo - 1 ) * codeManageList.list.params.rowPerPage);
			var deferred = $.Deferred();
			$.ajax({
				url: ROOT + '/cmmn/selectCodeManageList',
				data: codeManageList.list.params,
				success: function(res) {
					if (res.status === 200) {
						deferred.resolve(res);
						codeManageList.list.dataList = res.list;
					} else {
						deferred.reject("");
					}
				},
				
				error: function(err) {
					console.error(err);
					deferred.reject("");
				}
			});
			
			return deferred.promise();
		},
		
		//조회된 예약 목록 데이터를 가지고 화면에 목록 생성
		renderCodeManageList: function() {
			$.when(codeManageList.list.selectCodeManageList()).done(function(result) {
				$('tbody#codeManageList').empty();
				var resultList = result.list;
				var codeManageListHtml = [];
				codeManageList.paging.params.totalCount = result.customs.totalCount;
				if(result.customs.totalCount == 0) {
					codeManageListHtml.push('<tr>');
					codeManageListHtml.push('<td colspan=8 >검색 결과가 없습니다</td>');
					codeManageListHtml.push('</tr>');
				} else {
					for(var i in resultList) {
						var reg_dt =  moment(resultList[i].REG_DT,"YYYYMMDD hh:mm:ss",'ko'); 
	                    var regDt = reg_dt.format('YYYY-MM-DD (ddd)');
	                    var updt_dt =  moment(resultList[i].UPDT_DT,"YYYYMMDD",'ko'); 
	                    var updtDt = updt_dt.format('YYYY-MM-DD (ddd)');
						codeManageListHtml.push('<tr>');
						codeManageListHtml.push('	<td><input id="codeKey" type="checkbox" value="'+ resultList[i].CODE_TYL + '-' + resultList[i].CODE_TYS + '-' + resultList[i].CODE +'"></td>');
						codeManageListHtml.push('	<td>' + resultList[i].CODE_TYL_NM + '</td>');
						codeManageListHtml.push('	<td>' + resultList[i].CODE_TYS_NM + '</td>');
						codeManageListHtml.push('	<td>' + resultList[i].CODE + '</td>');
						codeManageListHtml.push('	<td>' + resultList[i].CODE_NM + '</td>');
						codeManageListHtml.push('	<td><button name="modifyBtn" class="t-btn cr01" onclick="javascript:codeManageList.popup.showCodeManageModifyPopup(\''+resultList[i].CODE_TYL + '\',\'' + resultList[i].CODE_TYS + '\',\'' + resultList[i].CODE +'\');">수정</button></td>');
						codeManageListHtml.push('</tr>');
					}
				}
				
				$('tbody#codeManageList').html(codeManageListHtml.join(''));

				codeManageList.paging.renderPaging();
			});
		},
		
		//현재 리스트에서 예약번호로 해당 ROW 의 데이터를 가져옴 
		getRowData: function(codeTypeVal, codeTysVal, codeVal) {
			var rowDataList = codeManageList.list.dataList;
			var rowData;

			for (var i in rowDataList) {
				if (rowDataList[i].CODE_TYL == codeTypeVal && 
					rowDataList[i].CODE_TYS == codeTysVal && 
					rowDataList[i].CODE == codeVal) {
					
					rowData = ({CODE_TYL :rowDataList[i].CODE_TYL,
							   CODE_TYL_NM :rowDataList[i].CODE_TYL_NM,
							   CODE_TYS :rowDataList[i].CODE_TYS,
							   CODE_TYS_NM :rowDataList[i].CODE_TYS_NM,
							   CODE :rowDataList[i].CODE,
							   CODE_NM :rowDataList[i].CODE_NM,
							   CODE_DC :rowDataList[i].CODE_DC,
							   CODE_ORDR :rowDataList[i].CODE_ORDR
							   });
					break;
				}
			}
			
			return rowData;
		}
	}, //End of list
	
	button: {
		//조회 버튼 클릭 이벤트
		listBtnClickEvent: function() {
			$('button#listBtn').on('click', function(e) {
				//조회 페이지는 1로 초기화 param 세팅
				codeManageList.list.params.pageNo = 1;
				
				//사옥 param 세팅
				codeManageList.list.params.codeType = $('#codeType').val();
				
				//관리사 param 세팅
				codeManageList.list.params.codeTys = $('#codeTys').val();
				
				//목록 조회 및 렌더 실행
				codeManageList.list.renderCodeManageList();
			});
		},
		
		//등록 버튼 클릭 이벤트
		codeManageCreateBtnEvent: function() {
			$("button#createBtn").on('click', function() {
				codeManageList.popup.showCodeManageCreatePopup();  
			});
		},
	//삭제 버튼 클릭 이벤트
		codeManageDeleteBtnEvent: function() {
			$("button#deleteBtn").on('click',function() {
				var params = [];
				var data = null;
				var resveNoSplit = null;
				var delItemCnt = 0;
				var meassage = '';
				$('tbody#codeManageList input:checkbox:checked').each(function() {
					delItemCnt++;  
					var delkeySet = $(this).val().split('-');
					params.push({ codeTyl: delkeySet[0], codeTys: delkeySet[1], code: delkeySet[2] });

				});
				 
				if(params.length == 0){
					alertPopup('삭제할 코드를 선택하세요.');
					return false;
				}
				
				confirmPopup(meassage || '총' + delItemCnt + '건을 삭제하시겠습니까?', function() {
					$.ajax({
						url: ROOT + '/cmmn/updateDelYCodeManageList',
						type: 'POST',
						data: { params:JSON.stringify(params) },
						success: function(res) {
							codeManageList.list.renderCodeManageList();
							$("#checkAll").prop('checked', false);
							alertPopup('삭제되었습니다.');
						},
						error: function(err) {
							console.error(err)
						}
					});
				});
			});
		}
	}, //End of button
	
	popup: {
		showCodeManageCreatePopup: function() {
			$('#layer_pop11').load(ROOT + '/cmmn/pop/codeManageCreate', null, function(res) {
				openLayerPopup('layer_pop11');
			});
		},
		
        showCodeManageModifyPopup: function(codeTypeVal, codeTysVal, codeVal) {
        	var rowData = codeManageList.list.getRowData(codeTypeVal, codeTysVal, codeVal);
			$('#layer_pop12').load(ROOT + '/cmmn/pop/codeManageModify', rowData, function(res) {
				openLayerPopup('layer_pop12');
			});
		}
	}, //End of popup
	
	checkboxEvent: {
		checkall:function() {
			$("#checkAll").on("change",function() {
				var chkstat = $("#checkAll").prop("checked");
				$("#codeManageList input[type=checkbox]").each(function() {
				    $(this).prop("checked",chkstat)
				});
			});
		}
	}, //End of checkboxEvent selectAllRow
	
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
			
			var currentIndex = codeManageList.list.params.pageNo; //현재 페이지 위치
			var rowPerPage = codeManageList.list.params.rowPerPage; //페이지 당 레코드 수
			var totalCount = codeManageList.paging.params.totalCount; //list 의 전체 row count
			var totalIndexCount = Math.ceil(totalCount / rowPerPage); //전체 인덱스 수
			var currentBlock = Math.ceil(currentIndex / 10); //현재 블럭의 시작 페이지 번호
			$("div#pagingArea").empty();
			
			var preStr = '';
			var postStr = '';
			var pageNumStr = '';
			
			var prev = (parseInt((currentIndex-1)/10)*10) - 9 > 0 ? (parseInt((currentIndex-1)/10)*10) - 9 : 1; //이전 블록의 시작페이지
			var next = (parseInt((currentIndex-1)/10)+1) * 10 + 1 < totalIndexCount ? (parseInt((currentIndex-1)/10)+1) * 10 + 1 : totalIndexCount; //다음 블록의 시작 페이지
			var first = (currentBlock - 1) * 10 + 1; //현재 블록의 시작 페이지
			var last = currentBlock * 10; //현재 블록의 끝 페이지
			
			codeManageList.paging.params.first = first;
			codeManageList.paging.params.last = last;
			codeManageList.paging.params.prev = prev;
			codeManageList.paging.params.next = next;
			
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
			codeManageList.paging.button.allPagingBtnEventBinding();
		},
		
		button: {
			allPagingBtnEventBinding: function() {
				codeManageList.paging.button.pageNumEvent();
				codeManageList.paging.button.prevBtnEvent();
				codeManageList.paging.button.nextBtnEvent();
				codeManageList.paging.button.firstBtnEvent();
				codeManageList.paging.button.lastBtnEvent();
			},
			
			pageNumEvent: function() {
				$('a.num').off();
				$('a.num').on('click', function() {
					if (codeManageList.list.params.pageNo == parseInt(this.innerHTML)) {
						return false;
					}
					
					codeManageList.list.params.pageNo = parseInt(this.innerHTML);
					codeManageList.list.renderCodeManageList();
				});
			},
			
			prevBtnEvent: function() {
				$('a#prevBtn').off();
				$('a#prevBtn').on('click', function() {
					if (codeManageList.list.params.pageNo == codeManageList.paging.params.prev) {
						return false;
					}
					codeManageList.list.params.pageNo = codeManageList.paging.params.prev;
					codeManageList.list.renderCodeManageList();
				});
			},
			
			nextBtnEvent: function() {
				$('a#nextBtn').off();
				$('a#nextBtn').on('click', function() {
					if (codeManageList.list.params.pageNo == codeManageList.paging.params.next) {
						return false;
					}
					codeManageList.list.params.pageNo = codeManageList.paging.params.next;
					codeManageList.list.renderCodeManageList();
				});
			},
			
			firstBtnEvent: function() {
				$('a#firstBtn').off();
				$('a#firstBtn').on('click', function() {
					if (codeManageList.list.params.pageNo == 1) {
						return false;
					}
				
					codeManageList.list.params.pageNo = 1;
					codeManageList.list.renderCodeManageList();
				});
			},
			
			lastBtnEvent: function() {
				$('a#lastBtn').off();
				$('a#lastBtn').on('click', function() {
					var rowPerPage = codeManageList.list.params.rowPerPage; //페이지 당 레코드 수
					var totalCount = codeManageList.paging.params.totalCount; //list 의 전체 row count
					var totalIndexCount = Math.ceil(totalCount / rowPerPage); //전체 인덱스 수
					
					if (codeManageList.list.params.pageNo == totalIndexCount) {
						return false;
					}
					
					codeManageList.list.params.pageNo = totalIndexCount;
					codeManageList.list.renderCodeManageList();
				});
			}
		}
	} //End of paging
} //End of codeManage

