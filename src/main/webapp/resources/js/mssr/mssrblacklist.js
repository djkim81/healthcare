/**
 * NO-SHOW(블랙리스트) List
 */
var mssrblacklistList = {
	// 초기화
	init: function() {
		loadCodeSelect(); 
		mssrblacklistList.list.rendermssrblacklistList(); //목록 조회 후 렌더
		mssrblacklistList.button.listBtnClickEvent(); //조회 버튼 클릭 이벤트
		mssrblacklistList.initEnter();//input Enter
	},

	initEnter : function(){
		$('#userName').keypress(function(data){
			if(data.which == 13){//Enter key pressed
					$('#listBtn').click();//Trigger search button click event
			}
		});

	},

	list: {
		
		//목록 조회 시 사용하는 파라미터
		params: {
			pageNo: 1, //조회할 페이지 번호
			rowPerPage: 10, //한 페이지 당 조회할 ROW 수
			bldCode: '', //사옥코드
			userName: '',//사용자 이름
			startRow: 0, //조회 시작할 ROW
			resveEmpno: '',
			rowData: [],
		},
		
		//현재 조회된 데이터 저장
		dataList: [],
		
		//NO-SHOW 조회
		selectMssrblacklistList: function() {
			
			mssrblacklistList.list.params.startRow = parseInt((mssrblacklistList.list.params.pageNo - 1 ) * mssrblacklistList.list.params.rowPerPage);
			
			var deferred = $.Deferred();
			
			$.ajax({
				url: ROOT + '/mssr/selectMssrblacklistList',
				data: mssrblacklistList.list.params,
				success: function(res) {
					
					if (res.status === 200) {
						deferred.resolve(res);
						mssrblacklistList.list.dataList = res.list;
						
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
		
		
		//조회된 NO-SHOW 현황 데이터를 가지고 화면에 목록 생성
		rendermssrblacklistList: function() {
			$.when(mssrblacklistList.list.selectMssrblacklistList()).done(function(result) {

				$('tbody#mssrblacklistList').empty();
				
				var resultList = result.list;
				var mssrblacklistListHtml = [];
				var btnText1 = '';
				var btnClass1 = '';
				var btnText2 = '';
				var btnClass2 = '';

				mssrblacklistList.paging.params.totalCount = result.customs.totalCount;
				
				if(mssrblacklistList.paging.params.totalCount == 0){
					mssrblacklistListHtml.push('<tr>');
					mssrblacklistListHtml.push('<td colspan=9 >검색 결과가 없습니다</td>');
					mssrblacklistListHtml.push('</tr>');
				}else{
					for (var i in resultList) {

						var bldCodeList = resultList[i].BLD_CODE;
						var bldCodelist = bldCodeList.split('|');

						var summaryList = resultList[i].SUMMARY;
						var summarylist = summaryList.split('|');
						
						var test = resultList[i].RESVE_NO;
												
						btnText1 = '케어완료';
						btnClass1 = 'blacklistCancelBtn';
						btnText2 = '패널티 미적용';
						btnClass2 = 'blacklistNoshowBtn';

						mssrblacklistListHtml.push('<tr>');
						mssrblacklistListHtml.push('<td>' + resultList[i].RESVE_EMPNO + '</td>');
						mssrblacklistListHtml.push('<td>' + resultList[i].USER_NAME + '</td>');
						mssrblacklistListHtml.push('<td>' + resultList[i].USER_DEPT + '</td>');
						mssrblacklistListHtml.push('<td>' + resultList[i].START_DT + '</td>');
						mssrblacklistListHtml.push('<td>' + resultList[i].END_DT + '</td>');
						mssrblacklistListHtml.push('<td>' + bldCodelist[0] );
							for(j=1; j<bldCodelist.length; j++){
								mssrblacklistListHtml.push('</br>' + bldCodelist[j] );
							}
						mssrblacklistListHtml.push('</td>');
						mssrblacklistListHtml.push('<td>' + summarylist[0] );
							for(j=1; j<summarylist.length; j++){
								mssrblacklistListHtml.push('</br>' + summarylist[j] );
							}
						mssrblacklistListHtml.push('</td>');
						mssrblacklistListHtml.push('<td><button class="t-btn cr01 resveModifyBtn '  + btnClass1 + '" data-mssrblacklistno="' + resultList[i].RESVE_EMPNO + '">' + btnText1 + '</button></td>');
						mssrblacklistListHtml.push('<td><button class="t-btn ' + btnClass2 + '" data-mssrblacklistno="' + resultList[i].RESVE_EMPNO + '">' + btnText2 + '</button></td>');
						mssrblacklistListHtml.push('</tr>');
					}
				}
				
				$('tbody#mssrblacklistList').html(mssrblacklistListHtml.join(''));
					mssrblacklistList.paging.renderPaging();

					mssrblacklistList.button.blacklistCancelBtnEvent();
					mssrblacklistList.button.blacklistNoshowBtnEvent();

			});
		},
		
		//해당 ROW 의 RESVE_NO 가져옴
		getRowData: function(mssrblacklistNo) {
			var rowDataList = mssrblacklistList.list.dataList;
			

			for(i=0; i<rowDataList.length; i++){
				if (rowDataList[i].RESVE_EMPNO == mssrblacklistNo) {
					var resveNoList = rowDataList[i].RESVE_NO;
					mssrblacklistList.list.params.rowData = resveNoList;
					break;
				}
			}
			
			return resveNoList;
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
			
			var currentIndex = mssrblacklistList.list.params.pageNo; //현재 페이지 위치
			var rowPerPage = mssrblacklistList.list.params.rowPerPage; //페이지 당 레코드 수
			var totalCount = mssrblacklistList.paging.params.totalCount; //list 의 전체 row count
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
			
			
			mssrblacklistList.paging.params.first = first;
			mssrblacklistList.paging.params.last = last;
			mssrblacklistList.paging.params.prev = prev;
			mssrblacklistList.paging.params.next = next;
			
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
			
			mssrblacklistList.paging.button.allPagingBtnEventBinding();
		},
		
		button: {
			allPagingBtnEventBinding: function() {
				mssrblacklistList.paging.button.pageNumEvent();
				mssrblacklistList.paging.button.prevBtnEvent();
				mssrblacklistList.paging.button.nextBtnEvent();
				mssrblacklistList.paging.button.firstBtnEvent();
				mssrblacklistList.paging.button.lastBtnEvent();
			},
			
			pageNumEvent: function() {
				$('a.num').off();
				$('a.num').on('click', function() {	
					if (mssrblacklistList.list.params.pageNo == parseInt(this.innerHTML)) {
						return false;
					}
					
					mssrblacklistList.list.params.pageNo = parseInt(this.innerHTML);
					mssrblacklistList.list.rendermssrblacklistList();
				});
			},
			
			prevBtnEvent: function() {
				$('a#prevBtn').off();
				$('a#prevBtn').on('click', function() {
					if (mssrblacklistList.list.params.pageNo == mssrblacklistList.paging.params.prev) {
						return false;
					}
					mssrblacklistList.list.params.pageNo = mssrblacklistList.paging.params.prev;
					mssrblacklistList.list.rendermssrblacklistList();
				});
			},
			
			nextBtnEvent: function() {
				$('a#nextBtn').off();
				$('a#nextBtn').on('click', function() {
					if (mssrblacklistList.list.params.pageNo == mssrblacklistList.paging.params.next) {
						return false;
					}
					mssrblacklistList.list.params.pageNo = mssrblacklistList.paging.params.next;
					mssrblacklistList.list.rendermssrblacklistList();
				});
			},
			
			firstBtnEvent: function() {
				$('a#firstBtn').off();
				$('a#firstBtn').on('click', function() {
					if (mssrblacklistList.list.params.pageNo == 1) {
						return false;
					}
				
					mssrblacklistList.list.params.pageNo = 1;
					mssrblacklistList.list.rendermssrblacklistList();
				});
			},
			
			lastBtnEvent: function() {
				$('a#lastBtn').off();
				$('a#lastBtn').on('click', function() {
					var rowPerPage = mssrblacklistList.list.params.rowPerPage; //페이지 당 레코드 수
					var totalCount = mssrblacklistList.paging.params.totalCount; //list 의 전체 row count
					var totalIndexCount = Math.ceil(totalCount / rowPerPage); //전체 인덱스 수
					
					if (mssrblacklistList.list.params.pageNo == totalIndexCount) {
						return false;
					}
					
					mssrblacklistList.list.params.pageNo = totalIndexCount;
					mssrblacklistList.list.rendermssrblacklistList();
				});
			}
		}
		
	},
	
	button: {

		//조회 버튼 클릭 이벤트
		listBtnClickEvent: function() {
			$('button#listBtn').on('click', function(e) {
				mssrblacklistList.list.params.pageNo = 1;
				mssrblacklistList.list.params.bldCode = $('#bldCombo').val();
				mssrblacklistList.list.params.userName = $('#userName').val();
				mssrblacklistList.list.rendermssrblacklistList();
			});
		},

		blacklistCancelBtnEvent: function() {
			$('button.blacklistCancelBtn').on('click', function(e) {
				var btn = $(this);
				var meassage = '';

				mssrblacklistList.list.params.resveEmpno = btn.data('mssrblacklistno');
				mssrblacklistList.list.params.rowData = mssrblacklistList.list.getRowData(btn.data('mssrblacklistno'));

				confirmPopup( meassage||'케어완료 처리하시겠습니까?', function(){					  					
					$.ajax({
							url: ROOT + '/mssr/mssrblacklistcareDelete',
							type: 'POST',
							data: mssrblacklistList.list.params,
							success : function(res){
								mssrblacklistList.list.rendermssrblacklistList();
								$.alert({text:'처리되었습니다.', icon: false});
							},
							error : function(err) {
								console.error(err)
							}
					  });
					})
					
			});
		},

		blacklistNoshowBtnEvent: function() {
			$('button.blacklistNoshowBtn').on('click', function(e) {
				var btn = $(this);
				var meassage = '';

				mssrblacklistList.list.params.resveEmpno = btn.data('mssrblacklistno');
				mssrblacklistList.list.params.rowData = mssrblacklistList.list.getRowData(btn.data('mssrblacklistno'));
				
				confirmPopup( meassage||'2주 패널티 제외하시겠습니까?', function(){					  					
					$.ajax({
							url: ROOT + '/mssr/mssrblacklistnoshowDelete',
							type: 'POST',
							data: mssrblacklistList.list.params,
							success : function(res){
								mssrblacklistList.list.rendermssrblacklistList();
								$.alert({text:'처리되었습니다.', icon: false});
							},
							error : function(err) {
								console.error(err)
							}
					  });
					})
				
		
			});
		},
		
		
	},		

}

$(document).ready(function() {	
	mssrblacklistList.init();
});




