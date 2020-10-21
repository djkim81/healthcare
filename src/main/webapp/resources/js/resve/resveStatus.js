/**
 * 예약현황
 */
var resveStatus = {
	data : {
		beds : [],
		selectedDate : undefined
	},
	// 초기화
	init: function(){
		
		resveStatus.calendar.render()	// 달력 렌더링
		.then(function(){
			loadCodeSelect(function(){		// 공통코드 로드
				if(SESSION.PLACE){				
					$('[data-code-tyl=BLD] option[value='+SESSION.PLACE+']').attr('selected', true);	// default사옥선택
				}
				resveStatus.fillBeds()		// bed목록 조회
				.then(function(){
					$('.month-calendar .today span').trigger('click');
					$('.cal-day.sat,.cal-day.sun,.cal-day.rest').find('span').off('click');	//트리깅 후 주말클릭이벤트 삭제
				});
				
			});	
					
			$('[data-code-tyl=BLD]').on('change', resveStatus.bldOnChange);	// 사옥변경이벤트 바인딩
			
			$('.loca-btn').on('click', function(){		// 사옥위치 클릭 이벤트 바인딩
				resveStatus.pop.floor($('[data-code-tyl=BLD]').val());
			})
		})
		
					
	},
	// 사옥변경 이벤트 리스너
	bldOnChange : function(e){
		
		resveStatus.fillBeds(e.target.value)
			.then(function(){
				//$('.month-calendar .today span').trigger('click');	//오늘선택
				$('.month-calendar .selected span').trigger('click');
				$('[data-code-tyl=BLD]').trigger('blur');								
			})
	},
	// 해당사옥의 bed목록 조회
	fillBeds : function(bldCode){
		bldCode = bldCode || $('[data-code-tyl="BLD"').val();
		
		return $.ajax({
			url: ROOT + '/cmmn/codeList',
			data: {codeTyl: "BED", codeTys: bldCode},
			success : function(res){
				resveStatus.data.beds = res.list;
			},
			error : function(err) {
				console.error(err)
			}
		});
	},			
	
	// 예약 등록
	regist : function(resveNo){
		$.ajax({
			url: ROOT + '/resve/regist',
			type: 'POST',
			data: {resveNo: resveNo},			
			success : function(res){
				resveStatus.table.refresh();
				closeLayerPopup();
			},
			error : function(err) {
				console.error(err)
			}
		});
	},
	// 대기등록
	wait : function(resveNo) {
		$.ajax({
			url: ROOT + '/resve/wait',
			type: 'POST',
			data: {resveNo: resveNo},
			success : function(res){
				resveStatus.table.refresh();
				closeLayerPopup();
			},
			error : function(err) {
				console.error(err)
			}
		});
	},
	// 예약/대기 취소
	cancel : function(resveNo, cancelGbn) {
		$.ajax({
			url: ROOT + '/resve/cancel',
			type: 'POST',
			data: {resveNo: resveNo, cancelGbn: cancelGbn},
			success : function(res){
				resveStatus.table.refresh();
				closeLayerPopup();
			},
			error : function(err) {
				console.error(err)
			}
		});
	},
	// 완료처리
	complete: function(resveNo) {
		$.ajax({
			url: ROOT + '/resve/complete',
			type: 'POST',
			data: {resveNo: resveNo},
			success : function(res){
				resveStatus.table.refresh();
				closeLayerPopup();
			},
			error : function(err) {
				console.error(err)
			}
		});
	}
	
};

/**
 * 상단 2주달력
 */
resveStatus.calendar =  {
	getRestDe : function(fromDate) {
		var startDt = moment(fromDate) || moment();
		var endDt = startDt.clone().add(2, 'w');
		
		var params = {
			pageNo: 1, //조회할 페이지 번호
			rowPerPage: 14, //한 페이지 당 조회할 ROW 수
			startRow: 0, //조회 시작할 ROW
			fromDate: startDt.format('YYYYMMDD'),
			toDate: endDt.format('YYYYMMDD')
		}
		
		return $.ajax({
			url: ROOT + '/cmmn/selectRestDeList',
			data: params			
		});
				
	},
	// 오늘부터 이후 2주조회
	getAfter2Weeks : function(fromDate) {
			
		var dates = [];
		
		return resveStatus.calendar.getRestDe()
		.then(function(res){
			
			
			var startDt = moment(fromDate) || moment();
			var endDt = startDt.clone().add(2, 'w');
			
			while(!startDt.isSame(endDt)){
				var d = {
					date: startDt.toDate(),
					yyyymmdd: startDt.format('YYYYMMDD'),
					year: startDt.year(),
					month: startDt.month()+1,
					day: startDt.date(),
					weekday: startDt.isoWeekday(),
					weekdayName: startDt.format('ddd').toUpperCase()
				}
				
				dates.push(d);
				
				startDt = startDt.add(1, 'days');
			}	
			
			$.each(res.list, function(i, restDe){
				dates.filter(function(e, i){
					if(e.yyyymmdd === restDe.RESTDE_DATE){
						dates[i].restDe = restDe
					}
				})
			})
			
			return dates;
			
		})

	},
	// 렌더링
	render : function(){
							
		return resveStatus.calendar.getAfter2Weeks()
		.then(function(dates){
			var elements = [];
			
			dates.forEach(function(d, i){
				
				var $td = $('<td>');
				var $div = $('<div>').addClass('cal-day');
				var $em = $('<em>').text(d.weekdayName);
				var $span = $('<span>').text(d.day);
				
				if(i==0 || d.day==1) {
					// 년월 입력
					var $tdCopy = $td.clone();
					var $divCopy = $div.clone().addClass('month');
					var $emCopy = $em.clone().text(d.year);
					var $spanCopy = $span.clone().text(d.month);
					
					elements.push(
							$tdCopy.append($divCopy.append($emCopy).append($spanCopy))					
					);
					if(i==0){
						// 오늘
						$div.removeClass('month').addClass('today');
						$em = $('<em>').text(d.weekdayName);
						$span = $('<span>').text(d.day);
					}
				}
				
				if(d.weekday == 6) $div.addClass('sat');
				if(d.weekday == 7) $div.addClass('sun');
				if(d.restDe) $div.addClass('rest').attr('title', d.restDe.RESTDE_NAME);
				
				// 데이터 및 클릭 이벤트 바인딩
				$span.data('data', d);
				
				//일단 클릭이벤트 모두 바인딩 후 휴일만 off (주말이어도 최초 트리깅을위해)
				//if(d.weekday != 6 && d.weekday !=7)		
					$span.on('click', resveStatus.calendar.click);
					
				
				
				elements.push(
					$td.append($div.append($em).append($span))					
				);
			});
			
			$('.month-calendar tbody tr').append(elements);		
			
			$( ".loca-btn" ).tooltip();
		})
		
	},
	// 클릭이벤트 리스너
	click: function(e){
		
		var data = $(e.target).data('data');			
		
		// 예전퍼블
		//var str = moment(data.date).locale('ko').format('YYYY년 M월 DD일(ddd)') + ' 예약 : ' + $('[data-code-tyl="BLD"] option:selected').text();
		//$('.sub-tit h3').text(str);
					
		$('.cal-day.selected').removeClass('selected');
		$(e.target).parents('div:eq(0)').addClass('selected');
		
		resveStatus.table.init();
		resveStatus.table.getStatus($(e.target).data('data').yyyymmdd)
		resveStatus.data.selectedDate = $(e.target).data('data');
		
		// 주말 여부
		if(resveStatus.data.selectedDate.weekday == 6 || 
				resveStatus.data.selectedDate.weekday == 7){
			$('.reservation-table tbody').hide();
			$('.weekend-info').show();
		}else{
			$('.reservation-table tbody').show();
			$('.weekend-info').hide();
		}
	}
};


/**
 * 예약 테이블
 */
resveStatus.table = {
	// 초기화
	init: function(){
		var bedList = resveStatus.data.beds;
		var trList = [];
		bedList.forEach(function(bed){
			var $tr = $('<tr>').data(bed);
			var $th = $('<th>').text(bed.CODE_NM);
			$tr.append($th);
			
			for(var i=1; i<=8; i++){
				var $td = $('<td>').addClass(bed.CODE + '-' + i);
				$tr.append($td);
			}
			trList.push($tr);
		})
		$('.reservation-table tbody').empty().append(trList);	
	},
	// 새로고침
	refresh : function(){
		resveStatus.table.init();
		resveStatus.table.getStatus(resveStatus.data.selectedDate.yyyymmdd);
		header.get2WeeksCount();
	},
	// 예약현황조회
	getStatus: function(yyyymmdd){
		if(!yyyymmdd){
			yyyymmdd = moment().format('YYYYMMDD');
		}
						
		$.ajax({				
			url: ROOT + '/resve/getStatus',
			data: {resveDe: yyyymmdd, bldCode: $('[data-code-tyl="BLD"').val()},
			success : function(res){
				
				var list = res.list;
				
				var last = undefined;

				function getButton(status){
					return {
						//예약가능
						'RESVE_POSBL' : $('<button>').append('<i class="xi-check-circle-o">').append('예약가능').addClass('rbtn cr1').on('click', resveStatus.pop.regist),	
						//예약완료
						'RESVE_COMPT' : $('<button>').append('<i class="xi-calendar-check">').append('예약완료').addClass('rbtn cr3').on('click', resveStatus.pop.cancel),	
						//예약불가
						'RESVE_IMPRTY' : $('<span>').text('예약불가').addClass('reservation-not-default'),
						//예약불가(예약/대기 모두 찬 경우)
						'RESVE_IMPRTY_FULL' : $('<span>').text('예약불가').addClass('reservation-not').attr('title', '이미 예약/대기가 마감되었습니다.'),
						//예약불가(여성구성원선택불가)
						'RESVE_IMPRTY_SEX' : $('<span>').text('예약불가').addClass('reservation-not').attr('title', '남성구성원은 여성헬스키퍼 예약이 불가능합니다.'),
						//대기가능
						'WAIT_POSBL' : $('<button>').append('<i class="xi-alarm-o">').append('대기가능').addClass('rbtn cr4').on('click', resveStatus.pop.wait),
						//대기중
						'WAIT' : $('<button>').append('<i class="img-icon01">').append('대기중').addClass('rbtn cr2').on('click', resveStatus.pop.cancel),
						//예약취소
						'RESVE_CANCL' : '',
						//대기취소
						'WAIT_CANCL' : '',
						//완료
						'COMPT' :  $('<span>').text('케어완료').addClass('care-com'),
						//노쇼 완료가능
						'NOSHOW_COMPT' : $('<button>').append('<i class="xi-calendar-check">').append('예약완료').addClass('rbtn cr3').on('click', resveStatus.pop.noshowConfirm),
						//노쇼
						'NOSHOW' : $('<button>').append('<i class="xi-calendar-check">').append('예약완료').addClass('rbtn cr3'),
					}[status]
				}
				
				// 근무가 하나도 없는경우 주말이미지 보여줌
				if(!list || list.length ==0){
					$('.reservation-table tbody').hide();
					$('.weekend-info').show();
					
					return false;
				}
				
				// 예약현황
				list.forEach(function(stts){
					
					var $td = $('.' + stts.BED_CODE + '-' + stts.RESVE_TM).data('data', stts)
								.attr('id', 'resve-'+stts.RESVE_NO);
					
					var $p = $('<p class="top">');
					var $span_name = $('<span>').addClass(stts.MSSR_SEXDSTN == 'F' ? 'women' : 'men').text(stts.MSSR_NCNM);
					var $status = getButton(stts.LAST_STTUS);
					
					if(!last || 									// last가 없거나
						last.RESVE_TM != (stts.RESVE_TM-1) || 		// last가 현재시간-1이 아니거나(연속되지않거나) (같은날 떨어진근무)
						last.MSSR_EMPNO != stts.MSSR_EMPNO ||		// 관리사가 다른경우							
						last.BED_CODE != stts.BED_CODE){			// 베드가 다른경우							
						
						$td.addClass('start');
						$p.append($span_name);
						
					}else{
						$td.addClass('on');
						
					}
					
					$td.empty().append($p).append($status)
					
					last = stts;
				});		
				
				$( ".reservation-not" ).tooltip();	//툴팁
				
			},
			error : function(err) {
				console.error(err)					
			}
		});			
	}
}


/**
 * 팝업
 */
resveStatus.pop  =  {
	// 예약신청 팝업 호출
	regist : function(e){
		
		if(SESSION.AGREE != 'Y'){
			$.alert({text: getMessage('error.reqireAgree')});
			return false;
		}
		
		var data = $(e.target).parents('td').data('data');		

		// 시간체크
		if(!checkBefore20min(data.RESVE_DE, data.RESVE_TM)){
			$.alert({
				text: getMessage('error.over20min'),
				callback: resveStatus.table.refresh
			});
			return false;
		}
		
		// 기존예약체크
		var filtered = $('td[id^=resve-]').filter(function(i, td){
			
			var status = $(td).data('data').LAST_STTUS;
			return status == "RESVE_COMPT" || 	//예약완료
				status == "WAIT" || 			//대기중
				status == 'COMPT' || 			//완료
				status == 'NOSHOW_COMPT' || 	//사후확인가능
				status == 'NOSHOW';				//노쇼
		});
		if(filtered.length != 0){
			var dateStr = moment(data.RESVE_DE).locale('ko').format('YYYY-MM-DD(ddd)');
			$.alert({
				text: getMessage('error.duplicateDayResve', [dateStr]),
				callback: resveStatus.table.refresh
			});
			return false;
			
		}
			
		$('#layer_pop01').load(
			ROOT + '/resve/pop/regist', 
			{
				resveNo : data.RESVE_NO, 
				resveDe: resveStatus.data.selectedDate.yyyymmdd
			}, 
			function(res){
				
				$('#layer_pop01 .comm-info').css('padding', '12px 20px');
				$('#layer_pop01 .comm-info').css('text-align', 'left');
				
				$('#layer_pop01 #btnOk').on('click', function(){
					//closeLayerPopup();
					
					$.alert({
						text: '케어 시작 20분 전까지만 취소가 가능하며,\nNo-show 시 2주간 예약이 불가합니다.',
						desc: '확인버튼 클릭시 예약이 완료됩니다.',
						callback: function(){
							resveStatus.regist(data.RESVE_NO);
						},
						icon: false
					});
	
					
				});	
				openLayerPopup('layer_pop01');
			}
		);
		
	},
	// 대기신청 팝업 호출
	wait : function(e){
		
		if(SESSION.AGREE != 'Y'){
			$.alert({text: getMessage('error.reqireAgree')});
			return false;
		}
		
		var data = $(e.target).parents('td').data('data');

		// 시간체크
		if(!checkBefore20min(data.RESVE_DE, data.RESVE_TM)){
			$.alert({
				text: getMessage('error.over20min'),				
				callback: resveStatus.table.refresh
			});
			return false;
		}
		
		// 기존예약체크
		var filtered = $('td[id^=resve]').filter(function(i, td){
			var status = $(td).data('data').LAST_STTUS;
			return status == "RESVE_COMPT" || 	//예약완료
				status == "WAIT" || 			//대기중
				status == 'COMPT' || 			//완료
				status == 'NOSHOW_COMPT' || 	//사후확인가능
				status == 'NOSHOW';				//노쇼
		});
		if(filtered.length != 0){
			var dateStr = moment(data.RESVE_DE).locale('ko').format('YYYY-MM-DD(ddd)');
			$.alert({
				text: getMessage('error.duplicateDayResve', [dateStr]),
				callback: resveStatus.table.refresh
			});
			return false;
			
		}
		
				
		$('#layer_pop02').load(
			ROOT + '/resve/pop/wait', 
			{
				resveNo : data.RESVE_NO, 
				resveDe: resveStatus.data.selectedDate.yyyymmdd
			}, 
			function(res){
				$('#layer_pop02 #btnOk').on('click', function(){
					resveStatus.wait(data.RESVE_NO);
				});	
				openLayerPopup('layer_pop02');
			}
		);	
		
	},
	// 예약/대기 취소 팝업 호출
	cancel : function(e){
		
		var data = $(e.target).parents('td').data('data');

		// 시간체크
		if(!checkBefore20min(data.RESVE_DE, data.RESVE_TM)){
			$.alert({
				text: getMessage('error.over20minCancel'),				
				callback: resveStatus.table.refresh
			});			
			return false;
		}
						
		$('#layer_pop03').load(ROOT + '/resve/pop/cancel', {resveNo : data.RESVE_NO, cancelGbn: data.LAST_STTUS}, function(res){
			$('#layer_pop03 #btnOk').on('click', function(){
				resveStatus.cancel(data.RESVE_NO, data.LAST_STTUS);
			});
			openLayerPopup('layer_pop03');
		});
	},
	// 예약 사후완료 처리
	noshowConfirm : function(e){	
		
		var data = $(e.target).parents('td').data('data');

		// 시간체크 (20분전부터 시작시간 전까지는 alert, 시작시간 이후에는 사후완료)
		if(!checkBefore20min(data.RESVE_DE, data.RESVE_TM)){
			
			var resveDatetime = moment(data.RESVE_DE + " " + getRealTime(data.RESVE_TM).start, 'YYYYMMDD HH:mm').toDate();	// 케어시작시간 date타입

			// 20분전 ~ 케어시작시간 사이라면 alert
			if(new Date() < resveDatetime){				
				$.alert({
					text: getMessage('error.over20minCancel'),				
					callback: resveStatus.table.refresh
				});
				return false;
			}
		}
				
		$('#layer_pop05').load(ROOT + '/resve/pop/noshowConfirm', {resveNo : data.RESVE_NO}, function(res){
			$('#layer_pop05 #btnOk').on('click', function(){
				resveStatus.complete(data.RESVE_NO);
			});				
			openLayerPopup('layer_pop05');
		});
	},
	floor : function(bldCode){
		bldCode = bldCode || $('[data-code-tyl=BLD]').val();
		$('#layer_pop_floor').load(ROOT + '/resources/html/floor.html', function(res){
			
			$('#layer_pop_floor img').attr('src', IMG+'/floor/'+bldCode+'.jpg');
			setTimeout(function(){				
				openLayerPopup('layer_pop_floor');
			}, 300);
		});
	}
}



// 예약,대기,취소 가능한 시간인지 체크 (대상시각 20분전)
function checkBefore20min(resveDe, resveTm){
	resveTm = getRealTime(resveTm).start;
	var targetDate = moment(resveDe + " " + resveTm, 'YYYYMMDD HH:mm').subtract(20,'minutes').toDate();	//예약시간 20분전
	var now = new Date();

	return now.getTime() <= targetDate.getTime();
}


$(document).ready(function(){	
	resveStatus.init();
})