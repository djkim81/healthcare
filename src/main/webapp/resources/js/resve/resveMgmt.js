/**
 * 예약 현황 조회 및 관리
 */
var resveMgmt = {
	// 초기화
	init: function() {
		loadCodeSelect(function(){
			resveMgmt.combobox.deleteStsOption(); //콤보박스 공통코드 세팅			
		});
		//resveMgmt.combobox.bldComboEventBinding();
		resveMgmt.datepicker.setDefaultValue(); //datepicker 기본값 세팅
		resveMgmt.list.renderResveList(); //목록 조회 후 렌더
		resveMgmt.button.listBtnClickEvent(); //조회 버튼 클릭 이벤트
		resveMgmt.button.excelBtnEvent();	//엑셀 버튼 클릭 이벤트
		resveMgmt.initEnter();//input Enter
	},
	
	initEnter : function(){
		$('#empnm').keypress(function(data){
			if(data.which == 13){//Enter key pressed
					$('#listBtn').click();//Trigger search button click event
			}
		});

	},
	
	cmmnCode: {
		allCodeList: [],
		getAllCodeList: function() {
			$.ajax({
				url: ROOT + '/cmmn/allCodeList',
				success: function(res) {
					if (res.status === 200) {
						resveMgmt.cmmnCode.allCodeList = res.list;
					}
				},
				error: function(err) {
					console.error(err);
				}
			})
		},
		codeToName: function(code) {
			var allCodeList = resveMgmt.cmmnCode.allCodeList;
			
			if (allCodeList.length == 0) {
				resveMgmt.cmmnCode.getAllCodeList();
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
			//var fromDate = moment().subtract(30, 'd').format('YYYY-MM-DD'); //30일 전 날짜
			var fromDate = moment().format('YYYY-MM-DD'); //2주전 날짜
			//var toDate = moment().format('YYYY-MM-DD'); //오늘 날짜
			var toDate = moment().add(2, 'w').format('YYYY-MM-DD'); //2주후 날짜
			
			$('input#from_date').val(fromDate);
			$('input#to_date').val(toDate);
			
			resveMgmt.list.params.fromDate = moment().format('YYYYMMDD');
			resveMgmt.list.params.toDate = moment().add(2, 'w').format('YYYYMMDD');
		}
	},
	
	
	combobox: {
		deleteStsOption: function() {
			$('.search_field [data-code-tyl="STS"] option[value="STS00"]').remove(); //'미예약' 삭제
			$('.search_field [data-code-tyl="STS"] option[value="STS99"]').remove(); //'근무취소' 삭제
			$('.search_field [data-code-tyl="STS"] option[value="STS02"]').remove(); //'예약취소' 삭제
			$('.search_field [data-code-tyl="STS"] option[value="STS04"]').remove(); //'대기취소' 삭제
		},
		bldComboEventBinding: function() {
			$('.search_field [data-code-tyl="BLD"]').on('change', function() {
				var bldCode = $(this).val();
				$('#cbbBedCode').attr('data-code-tyl', 'BED');
				$('#cbbBedCode').attr('data-code-tys', bldCode);
				loadCodeSelect(undefined,'#cbbBedCode');
			})
		}
	},
	
	
	
	list: {
		
		//목록 조회 시 사용하는 파라미터
		params: {
			pageNo: 1, 		//조회할 페이지 번호
			rowPerPage: 6, //한 페이지 당 조회할 ROW 수
			fromDate: '', 	//조회 시작 날짜
			toDate: '', 	//조회 끝 날짜
			bldCode: '',	//사옥코드
			bedCode: '',	//베드코드
			empnm: '',		//이름
			statusCode: '', //상태
			startRow: 0 	//조회 시작할 ROW
		},
		
		//현재 조회된 데이터 저장
		dataList: [],
		
		//예약 목록 조회
		selectResveList: function() {
			resveMgmt.list.params.startRow = parseInt((resveMgmt.list.params.pageNo - 1 ) * resveMgmt.list.params.rowPerPage);
			resveMgmt.list.params.bldCode =  $('.search_field [data-code-tyl="BLD"]').val();
			resveMgmt.list.params.bedCode = $('.search_field [data-code-tyl="BED"]').val();
			resveMgmt.list.params.empnm = $('#empnm').val();
			resveMgmt.list.params.statusCode = $('.search_field [data-code-tyl="STS"]').val();
			
			var deferred = $.Deferred();
			
			$.ajax({
				url: ROOT + '/resveMgmt/selectResveMgmtList',
				data: resveMgmt.list.params,
				success: function(res) {
					if (res.status === 200) {
						deferred.resolve(res);
						resveMgmt.list.dataList = res.list;
						
					} else {
						deferred.reject('');
					}
				},
				error: function(err) {
					console.error(err);
					deferred.reject('');
				}
			})
			
			return deferred.promise();
		},
		
		
		//조회된 예약 목록 데이터를 가지고 화면에 목록 생성
		renderResveList: function() {
			$.when(resveMgmt.list.selectResveList()).done(function(result) {

				$('tbody#resveMgmt').empty();
				
				var resultList = result.list;
												
				resveMgmt.paging.params.totalCount = result.customs.totalCount;
				
				var trArray = [];
				
				if(resultList.length == 0){
					var $tr = $('<tr>');
					$tr.append('	<td colspan="12">검색 결과가 없습니다.</td>');		
					trArray.push($tr);
				}else{
					
					for (var i in resultList) {
						var item = resultList[i];										
											
						var resve_de = item.RESVE_DE;
						resveDe = resve_de.substr(0,4) + '-' + resve_de.substr(4,2) + '-' + resve_de.substr(6,2);
						
						//20분전
						//var resve_tm_start = resultList[i].RESVE_TM_TXT.substr(0,5);	// 10:30~11:00 에서 10:30자르기						
						//var cancelDt = moment(resveDe + " " +resve_tm_start, 'YYYY-MM-DD HH:mm').subtract(20, 'minutes').toDate();
						// 금일(지난시간포함) 이후는 수정가능
						var canCancel = item.RESVE_DE >= moment().format('YYYYMMDD');
												
						var sttusNm = item.LAST_STTUS_NM;
						if(item.LAST_STTUS_CODE === 'STS07'){
							sttusNm = 'No-Show<br/>페널티없음';
						}else if(item.LAST_STTUS_CODE === 'STS03'){		// 대기중 -> 예약완료,대기중
							sttusNm = $('select[data-code-tyl=STS] option[value=STS01]').text() + '<br/> ' + sttusNm
						}else if(item.LAST_STTUS_CODE === 'STS04') {	// 대기취소 -> 예약완료
							sttusNm = $('select[data-code-tyl=STS] option[value=STS01]').text()// + '<br/> ' + sttusNm
						}
						
						var $tr = $('<tr>').data('data', item).attr('id', item.RESVE_NO);
						$tr.append('	<td rowspan="2">' + resveDe + '</td>');
						$tr.append('	<td rowspan="2">' + item.RESVE_TM_TXT + '</td>');
						$tr.append('	<td rowspan="2">' + item.BLD_NM + '</td>');
						$tr.append('	<td rowspan="2">' + item.MSSR_NCNM + '</td>');
						$tr.append('	<td rowspan="2">' + item.BED_NM + '</td>');
						$tr.append('	<td rowspan="2"><a class="link" href="javascript:resveMgmt.popup.detail('+ item.RESVE_NO +');">' + sttusNm + '</a></td>');					
						
						//예약자
						if(item.RESVE_EMPNM){
							
							if(!item || item.CANCL_YN=='Y' || item.COMPT_YN=='Y' || !canCancel){	// 변경불가
								$tr.append('	<td rowspan="2">' + item.RESVE_EMPNM + '<br/>('+ item.RESVE_EMPNO +')</td>');
							}else{
								$tr.append('	<td rowspan="2"><button class="t-btn cr01 resveModifyBtn" data-resveno="' + item.RESVE_NO + '">' + item.RESVE_EMPNM + '</button><br/>('+ item.RESVE_EMPNO +')</td>');
							}
							$tr.append('	<td>' + item.RESVE_DT + '</td>');
							$tr.append('	<td rowspan="2">' + (item.RESVE_AGREE_YN || 'N') + '</td>');
						}else{
							$tr.append('	<td rowspan="2"></td>');
							$tr.append('	<td></td>');
							$tr.append('	<td rowspan="2"></td>');
						}
						//대기자
						if(item.WAIT_EMPNM){		
							if(!item || item.CANCL_YN=='Y' || item.COMPT_YN=='Y' || !canCancel){	// 변경불가
								$tr.append('	<td rowspan="2">' + item.WAIT_EMPNM + '<br/>('+ item.WAIT_EMPNO +')</td>');
							}else{
								$tr.append('	<td rowspan="2"><button class="t-btn cr02 waitModifyBtn" data-resveno="' + item.RESVE_NO + '">' + item.WAIT_EMPNM + '</button><br/>('+ item.WAIT_EMPNO +')</td>');
							}							
							$tr.append('	<td>' + item.WAIT_DT + '</td>');
							$tr.append('	<td rowspan="2">' + (item.WAIT_AGREE_YN || 'N') + '</td>');
						}else{
							$tr.append('	<td rowspan="2"></td>');
							$tr.append('	<td></td>');
							$tr.append('	<td rowspan="2"></td>');
						}
						
						//$tr.append('	<td rowspan="2">' + item.COMPT_YN + '</td>');

						trArray.push($tr);
						
						var $tr2 = $('<tr>');
						if(item.RESVE_DEPTNM){						
							$tr2.append('	<td style="border-left:1px solid #ddd;">' + item.RESVE_DEPTNM + '</td>');
						}else{
							$tr2.append('	<td></td>');
						}
						
						if(item.WAIT_DEPTNM){			
							$tr2.append('	<td>' + item.WAIT_DEPTNM + '</td>');
						}else{
							$tr2.append('	<td></td>');
						}
						
						trArray.push($tr2);
						

						btnStyle = '';
					}
					
					
				}
				
				$('tbody#resveMgmt').append(trArray);
				resveMgmt.paging.renderPaging();
				
				resveMgmt.button.resveModifyBtnEvent();
				resveMgmt.button.waitModifyBtnEvent();
				
				
			});
		},
		
		//현재 리스트에서 예약번호로 해당 ROW 의 데이터를 가져옴
		getRowData: function(resveNo) {
			var rowDataList = resveMgmt.list.dataList;
			var rowData;
			
			for (var i in rowDataList) {
				if (rowDataList[i].RESVE_NO == resveNo) {
					rowData = rowDataList[i];
					break;
				}
			}
			
			return rowData;
		}

	},
	
	
	paging: {
		//페이징 처리를 위한 파라미터
		params: {
			totalCount: null, //list 의 전체 row count
			first: null, //첫페이지
			last: null, //마지막페이지
			prev: null, //이전페이지
			next: null //다음페이지
		},
		
		//페이지 이동 영역 생성
		renderPaging: function() {
			
			var currentIndex = resveMgmt.list.params.pageNo; //현재 페이지 위치
			var rowPerPage = resveMgmt.list.params.rowPerPage; //페이지 당 레코드 수
			var totalCount = resveMgmt.paging.params.totalCount; //list 의 전체 row count
			var totalIndexCount = Math.ceil(totalCount / rowPerPage); //전체 인덱스 수
			var currentBlock = Math.ceil(currentIndex / 10) //현재 블럭의 시작 페이지 번호
			
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
			
			resveMgmt.paging.params.first = first;
			resveMgmt.paging.params.last = last;
			resveMgmt.paging.params.prev = prev;
			resveMgmt.paging.params.next = next;
			
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


			for (var i=first; i<=last; i++) {
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
			
			resveMgmt.paging.button.allPagingBtnEventBinding();
		},
		
		button: {
			allPagingBtnEventBinding: function() {
				resveMgmt.paging.button.pageNumEvent();
				resveMgmt.paging.button.prevBtnEvent();
				resveMgmt.paging.button.nextBtnEvent();
				resveMgmt.paging.button.firstBtnEvent();
				resveMgmt.paging.button.lastBtnEvent();
			},
			
			pageNumEvent: function() {
				$('a.num').off();
				$('a.num').on('click', function() {
					if (resveMgmt.list.params.pageNo == parseInt(this.innerHTML)) {
						return false;
					}
					resveMgmt.list.params.pageNo = parseInt(this.innerHTML);
					resveMgmt.list.renderResveList();
				});
			},
			
			prevBtnEvent: function() {
				$('a#prevBtn').off();
				$('a#prevBtn').on('click', function() {
					if (resveMgmt.list.params.pageNo == resveMgmt.paging.params.prev) {
						return false;
					}
					resveMgmt.list.params.pageNo = resveMgmt.paging.params.prev;
					resveMgmt.list.renderResveList();
				});
			},
			
			nextBtnEvent: function() {
				$('a#nextBtn').off();
				$('a#nextBtn').on('click', function() {
					if (resveMgmt.list.params.pageNo == resveMgmt.paging.params.next) {
						return false;
					}
					resveMgmt.list.params.pageNo = resveMgmt.paging.params.next;
					resveMgmt.list.renderResveList();
				});
			},
			
			firstBtnEvent: function() {
				$('a#firstBtn').off();
				$('a#firstBtn').on('click', function() {
					if (resveMgmt.list.params.pageNo == 1) {
						return false;
					}
					resveMgmt.list.params.pageNo = 1;
					resveMgmt.list.renderResveList();
				});
			},
			
			lastBtnEvent: function() {
				$('a#lastBtn').off();
				$('a#lastBtn').on('click', function() {
					var rowPerPage = resveMgmt.list.params.rowPerPage; //페이지 당 레코드 수
					var totalCount = resveMgmt.paging.params.totalCount; //list 의 전체 row count
					var totalIndexCount = Math.ceil(totalCount / rowPerPage); //전체 인덱스 수
					
					if (resveMgmt.list.params.pageNo == totalIndexCount) {
						return false;
					}
					resveMgmt.list.params.pageNo = totalIndexCount;
					resveMgmt.list.renderResveList();
				});
			}
		}
		
	},
	
	
	
	button: {
		
		modifyBtnStatus: {
			type: '', //예약/대기
			rowData: {} //취소버튼의 rowData
		},
		
		//조회 버튼 클릭 이벤트
		listBtnClickEvent: function() {
			$('button#listBtn').on('click', function(e) {
				//날짜 validation 실행
				resveMgmt.validation.dateCheck();
				
				//조회 페이지는 1로 초기화 param 세팅
				resveMgmt.list.params.pageNo = 1;
				
				//상태 param 세팅
				resveMgmt.list.params.startRow = parseInt((resveMgmt.list.params.pageNo - 1 ) * resveMgmt.list.params.rowPerPage);
				resveMgmt.list.params.bldCode =  $('.search_field [data-code-tyl="BLD"]').val();
				resveMgmt.list.params.bedCode = $('.search_field [data-code-tyl="BED"]').val();
				resveMgmt.list.params.empnm = $('#empnm').val();
				resveMgmt.list.params.statusCode = $('.search_field [data-code-tyl="STS"]').val();
								
				//목록 조회 및 렌더 실행
				resveMgmt.list.renderResveList();
			});
		},
		
		//예약취소 버튼 클릭 이벤트
		resveModifyBtnEvent: function() {
			$('button.resveModifyBtn').off();
			$('button.resveModifyBtn').on('click', function(e) {
				var btn = $(this);
								
				resveMgmt.button.modifyBtnStatus.type = 'resve';
				resveMgmt.button.modifyBtnStatus.rowData =  resveMgmt.list.getRowData(btn.data('resveno'));
				
				resveMgmt.popup.showModifyPopup();
			});
		},
		
		//대기취소 버튼 클릭 이벤트
		waitModifyBtnEvent: function() {
			$('button.waitModifyBtn').off();
			$('button.waitModifyBtn').on('click', function(e) {
				var btn = $(this);
											
				resveMgmt.button.modifyBtnStatus.type = 'wait';
				resveMgmt.button.modifyBtnStatus.rowData =  resveMgmt.list.getRowData(btn.data('resveno'));
				
				resveMgmt.popup.showModifyPopup();
			});
		},
		excelBtnEvent : function() {
			$('#btnExcel').on('click', function(e) {
				
				resveMgmt.validation.dateCheck();				
				resveMgmt.list.params.bldCode =  $('.search_field [data-code-tyl="BLD"]').val();
				resveMgmt.list.params.bedCode = $('.search_field [data-code-tyl="BED"]').val();
				resveMgmt.list.params.empnm = $('#empnm').val();
				resveMgmt.list.params.statusCode = $('.search_field [data-code-tyl="STS"]').val();
				
				/*
				if(moment(resveMgmt.list.params.toDate).diff(moment(resveMgmt.list.params.fromDate), 'day') > 31){
					$.alert({text:'엑셀출력 기간은 최대 한달을 넘길 수 없습니다.'});
					return false;
				}
				*/
				location.href = ROOT + '/resveMgmt/excel?' + 
					$.param(						
						$.extend({
							bldNm : $('.search_field [data-code-tyl="BLD"] option:selected').text(),
							bedNm : $('.search_field [data-code-tyl="BED"] option:selected').text(),
							statusNm : $('.search_field [data-code-tyl="STS"] option:selected').text()
						}, resveMgmt.list.params)
					);
			})
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
				$.alert({text: getMessage('error.invalidStartDate')});
				return false;
			}
			
			if (todt.length !== 8) {
				$.alert({text: getMessage('error.invalidEndDate')});
				return false;
			}
			
			if (fromdt > todt) {
				$.alert({text: getMessage('error.dateCompareStartEnd')});
				return false;
			}
			
			resveMgmt.list.params.fromDate = fromdt;
			resveMgmt.list.params.toDate = todt;
			
			return true;
		}
	},
	
	
	popup: {
		showModifyPopup: function() {
			var rowData = resveMgmt.button.modifyBtnStatus.rowData;
			var type = resveMgmt.button.modifyBtnStatus.type;
			
			$('#layer_pop08').load(ROOT + '/resveMgmt/pop/modify', {resveNo : rowData.RESVE_NO, type: type}, function(res) {
				$('#layer_pop08 #btnOk').on('click', function() {
					$.confirm({
						text: $('#layer_pop08 #sttusCode option:selected').text() + ' 상태로 변경하시겠습니까?',
						callback: function(){
							resveMgmt.popup.confirmBtn(rowData.RESVE_NO, type, $('#layer_pop08 #sttusCode').val());
						}
					})

				});
				openLayerPopup('layer_pop08');
			});
		},
		
		//예약/대기 변경팝업 확인 버튼 클릭 시
		confirmBtn: function(resveNo, type, sttusCode) {

			$.ajax({
				url: ROOT + '/resveMgmt/modify',
				type: 'POST',
				data: {resveNo: resveNo, type: type, sttusCode: sttusCode},
				success : function(res){
					if (res.status === 200) {
						resveMgmt.list.renderResveList();
						//closeLayerPopup();
						$.alert({text:'처리되었습니다.', icon: false});
						header.get2WeeksCount();
					}
				},
				error : function(err) {
					console.error(err)
				}
			});
		},
		
		detail : function(resveNo){
			$('#layer_pop13').load(ROOT + '/resveMgmt/pop/detailHist', {resveNo : resveNo}, function(res){				
				openLayerPopup('layer_pop13');
			});
		}
	}

}



$(document).ready(function() {	
	resveMgmt.init();
});



//$(window).load(function() {
//	resveMgmt.combobox.deleteStsOption();
//});


