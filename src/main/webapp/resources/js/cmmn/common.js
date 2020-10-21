$(function(){				

	// 달력UI
	$.datepicker.setDefaults({
		showOn: "both", // 버튼과 텍스트 필드 모두 캘린더를 보여준다.
		buttonImage: IMG + "/common/ico_date.png", // 버튼 이미지.
		dateFormat: "yy-mm-dd", // 텍스트 필드에 입력되는 날짜 형식.
		changeMonth: true ,
		changeYear: true,
		nextText: '다음 달', // next 아이콘의 툴팁.
		prevText: '이전 달', // prev 아이콘의 툴팁.
		monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
		monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
		dayNames: ['일','월','화','수','목','금','토'],
		dayNamesShort: ['일','월','화','수','목','금','토'],
		dayNamesMin: ['일','월','화','수','목','금','토'],
		showButtonPanel: true,
		currentText: '오늘' , // 오늘 날짜로 이동하는 버튼 패널
		closeText: '닫기',  // 닫기 버튼 패널
		showMonthAfterYear: true,
		beforeShowDay: function(date){
			// 주말 선택불가
			var day = date.getDay();
			return [(day != 0 && day != 6)];
		}	
	});
	
	// 오늘 버튼 클릭이벤트
	/*
	$.datepicker._gotoToday = function(id) {
		$(id).datepicker('setDate', new Date());	//오늘로 setDate
		$('.ui-datepicker-current-day').click(); 	//클릭 트리깅
	};
	*/

	initDatepicker();
	
	$.ajaxSetup({
		cache: false,
		dataFilter: function(data,type){
						
			try{
				var json = JSON.parse(data);
				if(json.status == 200){		
					return data;
				}else{
					
					function afterHandler(){
						try{
							if(typeof resveStatus == 'object') resveStatus.table.refresh();		//예약하기
							if(typeof resveList == 'object') resveList.list.renderResveList();		//예약현황
							if(typeof scheduleList == 'object') scheduleList.list.renderScheduleList();	//스케쥴관리
							if(typeof resveMgmt == 'object') resveMgmt.list.renderResveList();		//예약변경
							if(typeof healthkeperList == 'object') healthkeperList.list.renderhealthkeperList();	//헬스키퍼관리
							if(typeof ChargerList == 'object') ChargerList.list.renderChargerList();	//담당자
							if(typeof codeManageList == 'object') codeManageList.list.renderCodeManageList();	//공통코드
							if(typeof mssrblacklistList == 'object') mssrblacklistList.list.rendermssrblacklistList();	//노쇼관리
							if(typeof RestDeList == 'object') RestDeList.list.renderRestDeList();	//휴일관리
							if(typeof resveConfirm == 'object') resveConfirm.table.refresh();	//예약확인
						}catch(err){}
					}
					
					if(json.messageCode == 'system.error'){	// 시스템에러
						$.alert({
							text: getMessage('system.error'), 
							callback: afterHandler
						});
					}else{	// 비즈니스로직 에러
						
						if(json.messageCode == 'error.paneltyTarget'){	// 블랙리스트 오류 메시지 예외
							var textArr = json.message.split('\n\n');
							$.alert({
								text: textArr[0],
								subText: textArr[1].replace(/\n/g,'<br>'),
								callback: afterHandler
							});
						}else if(json.messageCode == 'error.canNotSuccessionCancel'){
							var textArr = json.message.split('\n');
							var subText = textArr.pop();
							var text = textArr.join('\n');							
							$.alert({
								text: text,
								subText: subText,
								callback: afterHandler
							});
						}else{							
							$.alert({
								text: json.message, 
								callback: afterHandler
							});
						}
						
						
							
					}
					if(json.status == 403){
						location.href = '/error/403';
					}
				}
			}catch(err){
				return data;
			}
			
		}
	})

	moment.locale('ko', {
	    weekdays: ["일요일","월요일","화요일","수요일","목요일","금요일","토요일"],
	    weekdaysShort: ["일","월","화","수","목","금","토"],
	});
	moment.locale('en');
	
	
	/**
	 *  URL로부터 get parameter를 구한다.
	 * $.getParam('myKey')
	 */
	$.getParam = function(name){
	    var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
	    return results ? results[1] : undefined;
	}
	
	/**
	 * Alert 메시지 출력
	 * 
	 * options = {
	 *	text: <h3/> 'alert 메시지'
	 *	desc: '하부 보조 메시지',
	 *	callback: 확인버튼 클릭시, 수행할 콜백함수
	 *	icon : false면 아이콘 숨김
	 *	subText: <h4/> alert 메시지와 desc 메시지 사이에 중간크기로 보여지는 메시지 (추가)
	 * }
	 */
	$.alert = function(options) {
		$('#layer_pop_alert').load(ROOT + '/resources/html/alert.html', function(res){		
			$('.alert-message h3').html(options.text.replace(/\n/g, '<br/>'));
			
			if(options.subText){
				$('.alert-message h4').html(options.subText);
			}
			if(options.desc){
				$('.alert-message p').text(options.desc);
			}
			
			if(options.icon == false)
				$('.alert-message h3').removeClass('alert');
				
			$('#layer_pop_alert .layerClose').one('click', options.callback);
			//$('#layer_pop_alert #btnOk').one('click', options.callback);
			openLayerPopup('layer_pop_alert');
			
			// 엔터입력시 확인버튼 클릭
			$(document).one('keypress', function(e){
				if(e.keyCode == 13){					
					$('#layer_pop_alert #btnOk').trigger('click');
			    }
			})
		});
	}
	
	/**
	 * Confirm 메시지 출력
	 * 
	 * options = {
	 *	text: 'confirm 메시지'
	 *	desc: '하부 보조 메시지',
	 *	callback: 확인버튼 클릭시, 수행할 콜백함수
	 *	icon : false면 아이콘 숨김,
	 *  okText: 확인버튼 텍스트,
	 *  cancelText : 취소버튼 텍스트
	 * }
	 */
	$.confirm = function(options) {
		$('#layer_pop_confirm').load(ROOT + '/resources/html/confirm.html', function(res){		
			$('.alert-message h3').html(options.text.replace(/\n/g, '<br/>'));
			if(options.desc){
				$('.alert-message p').html(options.desc.replace(/\n/g, '<br/>'));
			}
			
			if(options.icon == false)
				$('.alert-message h3').removeClass('confirm');
			
			if(options.okText)
				$('.pop-container #btnOk').text(options.okText);
			
			if(options.cancelText)
				$('.pop-container .layerClose').text(options.cancelText);
			
			$('#layer_pop_confirm #btnOk').one('click', options.callback);
			openLayerPopup('layer_pop_confirm');
		});
	}
	
});

function initDatepicker(){
	$(".startDate").each(function(i,picker){
		$(picker).datepicker({        
	        onSelect: function(selected) {
	          $(picker).parents('td:eq(0)').find(".endDate").datepicker("option","minDate", selected);
	        }
	    })
	});
	
	 $(".endDate").each(function(i,picker){
		 $(picker).datepicker({        
			 onSelect: function(selected, element) {
	        	$(picker).parents('td:eq(0)').find(".startDate").datepicker("option","maxDate", selected);
	        }
	    });
	 })
}

/**
 * 공통코드 select options 로드
 * @returns
 */
function loadCodeSelect(cb, selector){
	
	var $selector = selector ? $(selector) : $(document);
	if($selector.is('select')){
		var tyl = $selector.data('code-tyl');	//코드타입(대)
		var tys = $selector.data('code-tys');	//코드타입(소)
		var empStr = $selector.data('empty-str');
	
		if(empStr){
			$selector.append($('<option>').val("").text(empStr));
		}
		
		if(tyl==='BED' && !tys){
			return;
		}
		
		$.ajax({
			url: ROOT + '/cmmn/codeList',
			data: {codeTyl: tyl, codeTys: tys || ''},
			success : function(res){							
				if(res.status === 200){
					var options = res.list.map( function(data){
						return $('<option>').val(data.CODE).text(data.CODE_NM);
					})
					
					$selector.append(options);
					
					
					
					if(cb) cb();	//콜백이 있다면 실행
				}
				
			},
			error : function(err) {
				console.error(err)
			}
		})
	} else{
		$selector.find('select[data-code-tyl]').each(function(idx, select){
			var tyl = $(select).data('code-tyl');	//코드타입(대)
			var tys = $(select).data('code-tys');	//코드타입(소)
			var empStr = $(select).data('empty-str');
			
			if(empStr){
				$(select).append($('<option>').val("").text(empStr));
			}
			
			if(tyl==='BLD'){
				$(select).on('change', function(){
					$('[data-code-tyl=BED]').data('code-tys', $(this).val()).empty();
					loadCodeSelect(undefined, $('[data-code-tyl=BED]'));
				})
			}
			
			if(tyl==='BED' && !tys){
				return;
			}

			$.ajax({
				url: ROOT + '/cmmn/codeList',
				data: {codeTyl: tyl, codeTys: tys || ''},
				success : function(res){							
					if(res.status === 200){
						var options = res.list.map( function(data){
							return $('<option>').val(data.CODE).text(data.CODE_NM);
						})
						
						$(select).append(options);											
						
						if(cb) cb();	//콜백이 있다면 실행
					}
					
				},
				error : function(err) {
					console.error(err)
				}
			})
		})
	}
	
}

function openLayerPopup(id){
	$('.pop-layer').css('display', 'none'); 
	var temp = $('#' + id);
	temp.css('top','');
	temp.css('left','');
	var bg = temp.parents('bg');
	if (bg) {
		$('.layer').fadeIn();
	} else {
		temp.fadeIn();
	}
	temp.css('display', 'block');
	
	if (temp.outerHeight() < $(document).height()){ 
		temp.css('margin-top', '-' + temp.outerHeight() / 2 + 'px')
	}else {
		temp.css('top', '0px');
		temp.css('margin-top','');
	}
	
	if (temp.outerWidth() < $(document).width()) {
		temp.css('margin-left', '-' + temp.outerWidth() / 2 + 'px');
	} else {
		temp.css('left', '0px');
		temp.css('margin-left','');
	}

	//$("html").attr("style", "overflow-y:hidden");
	$("html").addClass("scroll");

	temp.find('.layerClose').click(function (e) {
		if (bg) {
			$('.layer').fadeOut();
		} else {
			temp.fadeOut();
		}
		e.preventDefault();
		$("html").attr("style", "overflow-y:auto");
		$("html").removeClass("scroll");
	});
/*
	$('.layer .bg').click(function (e) {
		$('.layer').fadeOut();
		e.preventDefault();
		$("html").attr("style", "overflow-y:auto");
		$("html").removeClass("scroll");
	});
	*/
}

function closeLayerPopup(){
	var temp = $('.pop-layer'); //$('#' + id);
	var bg = temp.parents('bg');
	
	if (bg) {
		$('.layer').fadeOut();
	} else {
		temp.fadeOut();
	}	
	$("html").attr("style", "overflow-y:auto");
	$("html").removeClass("scroll");
	temp.empty();
}

function getRealTime(number){
	return {
		1 : { start : '10:00', end : '10:30'},
		2 : { start : '11:00', end : '11:30'},
		3 : { start : '12:00', end : '12:30'},
		4 : { start : '13:00', end : '13:30'},
		5 : { start : '14:00', end : '14:30'},
		6 : { start : '15:00', end : '15:30'},
		7 : { start : '16:00', end : '16:30'},
		8 : { start : '17:00', end : '17:30'},
	}[number]
}

function alertPopup(title, contents, fn){
	$('#layer_pop_alert').load(ROOT + '/resources/html/alert.html', function(res){		
		$('.alert-message h3').html(title.replace(/\n/g, '<br/>'));
		if(contents){
			if($.isFunction(contents)){
				fn = contents;
			}else{
				$('.alert-message p').text(contents);
			}
		}
		//$('#layer_pop_alert #btnOk').one('click', fn);
		$('#layer_pop_alert .layerClose').one('click', fn);

		openLayerPopup('layer_pop_alert');
	});
}



function confirmPopup(title, contents, fn){	
	$('#layer_pop_confirm').load(ROOT + '/resources/html/confirm.html', function(res){		
		$('.alert-message h3').html(title.replace(/\n/g, '<br/>'));
		if(contents){
			if($.isFunction(contents)){
				fn = contents;
			}else{
				$('.alert-message p').text(contents);
			}
		}
		$('#layer_pop_confirm #btnOk').one('click', fn);
		openLayerPopup('layer_pop_confirm');
	});
}