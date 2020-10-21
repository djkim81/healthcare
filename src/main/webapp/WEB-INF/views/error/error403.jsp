<%@ page contentType="text/html; charset=utf-8" %>
<!DOCTYPE html>
<html lang="ko">
	<head>
		<jsp:include page="${JSP}/include/resource.jsp" />
	</head>	

	<body>

		<div class="error-wrap">
			<div class="error-cont">
				<h1 class="fs2">해당 시스템에 대한<br><span>접속 가능한 사용자가 아닙니다.</span></h1>
				<p class="fs3">
					<strong>서비스 이용에 문제가 있을 경우<br>관리자(happy@happyhanool.com)에게 문의하세요.</strong>					
				</p>
			</div>
			<div class="pop-btn-area">
<!-- 				<button class="pop-btn" onclick="history.back();">이전</button> -->
				<button class="pop-btn" onclick="window.open('about:blank','_self').close();">닫기</button>
			</div>
		</div>

	</body>
</html>