<%@ page contentType="text/html; charset=utf-8" %>
<script>
var SESSION = {
	EMPNO : '${sessionScope.LoginVo.empno}',
	HNAME : '${sessionScope.LoginVo.hname}',
	SEX : '${sessionScope.LoginVo.tSex}',
	PLACE: '${sessionScope.LoginVo.place}',
	AGREE: '${sessionScope.LoginVo.hrsAgree}'
}
var header = {
	// 이번달 카운트 (2주카운트로 변경되며, 사용안함)
	getMonthCount : function(){
		$.ajax({
			url: ROOT + '/resve/monthCnt',
			data: {},
			success : function(res){				
				$('.header .user-desc #resveCnt').text(res.item.RESVE_CNT);
				$('.header .user-desc #waitCnt').text(res.item.WAIT_CNT);
				$('.header .user-desc').show();
				
			},
			error : function(err) {
				console.error(err)
			}
		})
	},
	// 2주 예약/대기건수
	// 예약 : 근무취소/완료건 제외 2주간
	// 대기 : 근무취소/완료/시간이지난(대기중상태로 끝난) 건 제외
	get2WeeksCount : function(){
		$.ajax({
			url: ROOT + '/resve/2WeeksCnt',
			data: {},
			success : function(res){				
				$('.header .user-desc #resveCnt').text(res.item.RESVE_CNT);
				$('.header .user-desc #waitCnt').text(res.item.WAIT_CNT);
				$('.header .user-desc').show();
				
			},
			error : function(err) {
				console.error(err)
			}
		})
	},
	goToList : function(e){		
		if(e.target.id == 'waitCnt'){
			location.href = ROOT + '/resve/list?from=waitCnt';
		}else{
			location.href = ROOT + '/resve/list?from=resveCnt';
		}
	},
	//급여공제 동의 여부 확인
	checkAgree : function(){
		//var agreeYn = '${sessionScope.LoginVo.hrsAgree}';		
		
		if(SESSION.AGREE != 'Y'){
			$('#layer_pop_agree').load(ROOT + '/resources/html/agree.html', function(res){
				openLayerPopup('layer_pop_agree');
			});
		}
	}
	
}
$(document).ready(function(){		
	$('.gnb-menu li').each(function(i,e){		
		if($(this).find('a').attr('href') == location.pathname){
			$(this).addClass('selected');	//gbn 메뉴 선택표시
		}		
	})
	
	$('.user-desc em').on('click', header.goToList)
	
	header.get2WeeksCount();
	
	setTimeout(header.checkAgree, 500);	//급여공제 동의 여부 확인
})

</script>

<div class="inner">
	<a href="${ROOT}/resve/status"><h1 class="logo">SKT헬스케어</h1></a>
	<ul class="gnb-menu">
		<li><a href="${ROOT}/resve/status"><i class="xi-calendar-check"></i>예약하기</a></li>
		<li><a href="${ROOT}/resve/list"><i class="xi-document"></i>예약현황</a></li>
		<c:if test="${!empty sessionScope.LoginVo.auth}">
		<li><a href="${ROOT}/mssr/schedule"><i class="xi-cog"></i>관리자</a></li>
		</c:if>		
	</ul>
	<p class="user-desc">
		<strong>${sessionScope.LoginVo.hname}<em>님</em></strong>
		<span>예약 <em id="resveCnt" style="text-decoration: underline; cursor:pointer">0</em> 건 / 대기 <em id="waitCnt" style="text-decoration: underline; cursor:pointer">0</em>건</span>
	</p>
</div>
