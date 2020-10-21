var resveConfirm = {
	data : {
		beds : [],
		bldCode: undefined,
		selectedDate: undefined
	},
	init: function(){
		resveConfirm.calendar.render();	// 달력 렌더링
		loadCodeSelect(function(){		// 공통코드 로드
			$('[data-code-tyl=BLD] option[value='+resveConfirm.data.bldCode+']')
				.attr('selected', true);	// default사옥선택
			
			//default 사옥이 케어가능한 사옥이 아닌경우
			resveConfirm.data.bldCode = $('select[data-code-tyl=BLD] option:selected').val();
			
			resveConfirm.fillBeds(resveConfirm.data.bldCode)	// bed목록 조회
				.then(function(){
					$('.month-calendar .today span').trigger('click');
					$('.cal-day.sat,.cal-day.sun,.cal-day.rest').find('span').off('click');	//트리깅 후 주말클릭이벤트 삭제
				});	
		});	
			
		$('#btnConfirm').on('click', resveConfirm.pop.confirm);	//확인 버튼 이벤트
		$('[data-code-tyl=BLD]').on('change', resveConfirm.bldOnChange);	//사옥변경 이벤트	
		
		$('#txtResveEmpno').on('keypress', function(e){
			if(e.keyCode == 13) {
				resveConfirm.pop.confirm();
				$('#txtResveEmpno').trigger('blur');	// 입력창 포커스 제거
			}
		});
		
		setTimeout(function(){ $('#txtResveEmpno').focus();},300);	// 페이지 로드시 포커스
	},
	setHeader: function(yyyymmdd){
		var m = moment(yyyymmdd) || moment();
		var bldNm = $('select[data-code-tyl=BLD] option:selected').text();
		$('.tit-date span').empty().append(m.format('YYYY년 MM월 '))
		.append($('<em>').text(m.format('DD일('+ m.locale('ko').format('ddd')+') '+ bldNm + ' 예약')))
		.append('입니다.');
	},
	// 사옥변경 이벤트 리스너
	bldOnChange : function(e){
		resveConfirm.data.bldCode = e.target.value;
		resveConfirm.fillBeds(e.target.value)
			.then(function(){
				$('.month-calendar .today span').trigger('click');
				$('[data-code-tyl=BLD]').trigger('blur');	//ie에서 select가 선택된 상태 해지
			})
	},
	// 해당사옥의 bed목록 조회
	fillBeds : function(bldCode){				
		return $.ajax({
			url: ROOT + '/cmmn/codeList',
			data: {codeTyl: "BED", codeTys: bldCode},
			success : function(res){
				resveConfirm.data.beds = res.list;
			},
			error : function(err) {
				console.error(err)
			}
		});
	},	
	// 케어 시작 (완료처리)
	start: function(resveNo){
		$.ajax({
			url: ROOT + '/confirm/start',
			type: 'POST',
			data: {resveNo: resveNo},
			success: function(res){				
				resveConfirm.table.refresh();
				$('#txtResveEmpno').focus();
				$('#txtResveEmpno').val('');
				closeLayerPopup();
			},
			error: function(err){
				console.error(err)
			}
		})
	},
	// 팝업
	pop : {
		confirm : function(e){
			if($('#txtResveEmpno').is(':disabled')){	//주말 등의 이유로 disabled라면
				return;
			}
			if($('#txtResveEmpno').val().trim() == ""){
				$.alert({
					text: getMessage('error.requireEmpno'),
					callback: function(){
						$('#txtResveEmpno').focus();						
					}
				});
				
			}else if(resveConfirm.data.selectedDate.yyyymmdd != moment().format('YYYYMMDD')){
				$.alert({
					text: getMessage('error.onlySameday'),
					callback: function(){
						$('#txtResveEmpno').val('');
						$('#txtResveEmpno').focus();						
					}
				});
				
			}else{
				$('#layer_pop04').load(ROOT + '/confirm/pop/start',{
					resveEmpno : $('#txtResveEmpno').val().trim().toUpperCase(), 
					resveDe: resveConfirm.data.selectedDate.yyyymmdd,
					bldCode: resveConfirm.data.bldCode
				}, function(res, state, xhr){						
					openLayerPopup('layer_pop04');					
				});
			}
			
			
			
		}
	}
	
};

// 달력
resveConfirm.calendar = {
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
		
		var promise = $.Deferred();
		
		$.ajax({
			url: ROOT + '/cmmn/selectRestDeList',
			data: params,
			success : function(res){
				var retArray = [];
				$.each(res.list, function(){
					retArray.push(this.RESTDE_DATE);
				})
				promise.resolve(retArray)
			},
			error : function(err){
				console.log(err);
				
			}
		});
		
		return promise;
				
	},
	// 오늘부터 이후 2주조회
	getAfter2Weeks : function(fromDate) {
		var dates = [];	
		return resveConfirm.calendar.getRestDe()
		.then(function(restArr){
			
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
				
				if(restArr.indexOf(d.yyyymmdd)> -1) {					
					d.isRest = true;
				}
				
				dates.push(d);
				
				startDt = startDt.add(1, 'days');
			}	
			
			return dates;
		})
		
	},
	// 렌더링
	render : function(){
							
		var dates = resveConfirm.calendar.getAfter2Weeks()
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
				if(d.isRest) $div.addClass('rest');
				
				// 데이터 바인딩
				$span.data('data', d);
				
				//일단 클릭이벤트 모두 바인딩 후 휴일만 off (주말이어도 최초 트리깅을위해)
				///if(d.weekday != 6 && d.weekday !=7) 
					$span.on('click', resveConfirm.calendar.click);
				
				elements.push(
					$td.append($div.append($em).append($span))					
				);
			});
			
			$('.month-calendar tbody tr').append(elements);		
		});
			
	},
	click: function(e){
		var data = $(e.target).data('data');		
		$('.cal-day.selected').removeClass('selected');
		$(e.target).parents('div:eq(0)').addClass('selected');
		resveConfirm.data.selectedDate = $(e.target).data('data');
		resveConfirm.setHeader($(e.target).data('data').yyyymmdd);
		resveConfirm.table.init();
		resveConfirm.table.getResve($(e.target).data('data').yyyymmdd);
		
		// 주말 여부
		if(resveConfirm.data.selectedDate.weekday == 6 || 
				resveConfirm.data.selectedDate.weekday == 7){
			$('.reservation-table tbody').hide();
			$('.weekend-info').show();
			$('#txtResveEmpno').attr('disabled', true);
		}else{
			$('.reservation-table tbody').show();
			$('.weekend-info').hide();
			$('#txtResveEmpno').removeAttr('disabled');
		}
	}
}

// 현황테이블
resveConfirm.table = {
	init: function(){
		var bedList = resveConfirm.data.beds;
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
	refresh: function(){
		resveConfirm.table.init();
		resveConfirm.table.getResve();
	},
	getResve: function(yyyymmdd){
		if(!yyyymmdd){
			yyyymmdd = moment().format('YYYYMMDD');
		}
		
		$.ajax({
			url: ROOT + '/confirm/getResve',
			data: {resveDe: yyyymmdd, bldCode: resveConfirm.data.bldCode},
			success: function(res){				
				var list = res.list;
				
				var last = undefined;
				
				// 근무가 하나도 없는경우 주말이미지 보여줌
				if(!list || list.length ==0){
					$('.reservation-table tbody').hide();
					$('.weekend-info').show();
					
					return false;
				}
				
				
				function getButton(status){
					return {
						//예약가능
						'RESVE_POSBL' : $('<button>').append('<i class="xi-check-circle-o">').append('예약가능').addClass('rbtn cr1').hide(), // 요청사항으로 인한 hide()	
						//예약완료->예약중
						'RESVE_COMPT' : $('<button style="cursor: default;">').append('<i class="xi-calendar-check">').append('예약중').addClass('rbtn'),	
						//예약불가
						'RESVE_IMPRTY' : $('<span>').text('예약불가').addClass('reservation-not').hide(), // 요청사항으로 인한 hide()									
						//완료
						'COMPT' :  $('<span>').text('케어완료').addClass('reservation-not')						
					}[status]
				}
				
				list.forEach(function(work){
					
					var $td = $('.' + work.BED_CODE + '-' + work.RESVE_TM).data('data', work)
								.attr('id', 'resve-'+work.RESVE_NO);
					
					var $p = $('<p class="top">');
					var $span_name = $('<span>').addClass(work.MSSR_SEXDSTN == 'F' ? 'women' : 'men').text(work.MSSR_NCNM);
					var $status = getButton(work.LAST_STTUS);
					
					if(!last || 									// last가 없거나
						last.RESVE_TM != (work.RESVE_TM-1) || 		// last가 현재시간-1이 아니거나(연속되지않거나) (같은날 떨어진근무)
						last.MSSR_EMPNO != work.MSSR_EMPNO ||		// 관리사가 다른경우							
						last.BED_CODE != work.BED_CODE){			// 베드가 다른경우							
						
						$td.addClass('start');
						$p.append($span_name);
						
					}else{
						$td.addClass('on');						
					}
					
					$td.empty().append($p).append($status);

					last = work;
				});
			},
			error : function(err) {
				console.error(err.responseJSON)
			}
		})
	}	
}


function checkDateChange(){
	var today = moment().format('YYYYMMDD');
	var min = moment().format('mm')
	var selectedDay = resveConfirm.data.selectedDate.yyyymmdd;
	
	if(selectedDay < today){
		location.reload();	// 날짜가 바뀌었으면 페이지 리로드		
	}else if(Number(min) % 10 == 0){
		resveConfirm.table.refresh();	//상태 리프레시
		console.info('status resfresh!!');
	}
	
}

$(document).ready(function(){
	resveConfirm.data.bldCode = $('#bldCode').val()||'SK01';
	resveConfirm.init();
	window.timer = setInterval(checkDateChange, 1000*60);	//1분마다 체크
})