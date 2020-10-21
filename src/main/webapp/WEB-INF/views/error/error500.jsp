<%@ page contentType="text/html; charset=utf-8" %>
<!DOCTYPE html>
<html lang="ko">
	<head>
		<jsp:include page="${JSP}/include/resource.jsp" />
	</head>	

	<body>

		<div class="error-wrap">
			<div class="error-cont">
				<strong class="fs1">죄송합니다.</strong>
				<h1 class="fs2">요청하신 <span>작업을 수행 중 오류가 발생하였습니다.</span></h1>
				<p class="fs3">					
					<strong>서비스 이용에 문제가 있을 경우<br>관리자(happy@happyhanool.com)에게 문의하세요.</strong>	.		
				</p>
			</div>
			<div class="pop-btn-area">
				<button class="pop-btn" onclick="history.back();">이전</button>
				<!-- <button class="pop-btn gray" onclick="window.open('','_self').close();;">닫기</button> -->				
			</div>
		</div>

	</body>
</html>