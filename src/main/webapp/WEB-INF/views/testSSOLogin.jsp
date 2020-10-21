<%@ page contentType="text/html; charset=utf-8" %>

<!DOCTYPE html>
<html lang="ko">
	<head>
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>SKT헬스케어예약시스템</title>					
	</head>
	<body>

		<div class="wrap">
			<form action="" method="post">
				*테스트 계정<br/><p/>
<!-- 				* 유준희 사용 계정 -->
				<input id="id1111573" name="id" type="radio" value="1111573" /><label for="id1111573">김지현 (1111573, 여, SKT-타워)</label><p/>
				<input id="id1109271" name="id" type="radio" value="1109271" /><label for="id1109271">장현춘 (1109271, 남, SKT-타워)</label><p/>
				<input id="id1110530" name="id" type="radio" value="1110530" /><label for="id1110530">이정 (1110530, 남, SKT-타워)</label><p/><br/>
<!-- 				<input id="id149365" name="id" type="radio" value="P149365"  /><label for="id149365">유준희 (P149365, 남, SKT-타워,관리자)</label><br/> -->
				<input id="id6" name="id" type="radio" value="1110617" /><label for="id6">김가상4 (1110617, 남, SKT-타워)</label><br/>
				<input id="id7" name="id" type="radio" value="1110618" /><label for="id7">김가상5 (1110618, 남, SKT-타워)</label><br/><p/>
<!-- 				*이영호 사용 계정 -->
				<br/>
<!-- 				<input id="id150113" name="id" type="radio" value="P150113"  /><label for="id150113">이영호 (P150113, 남, SKT-타워,관리자)</label><br/> -->
				<input id="id8" name="id" type="radio" value="1110619" /><label for="id8">김가상6 (1110619, 남, SKT-타워)</label><br/>
				<input id="id9" name="id" type="radio" value="1110620" /><label for="id9">김가상7 (1110620, 남, SKT-타워)</label><br/><p/>
<!-- 				*김현이 사용 계정 -->
				<br/>
<!-- 				<input id="id149080" name="id" type="radio" value="P149080" /><label for="id149080">김현이 (P149080, 남, SKT-타워,관리자)</label><br/>	 -->
				<input id="id11" name="id" type="radio" value="1110622" /><label for="id11">김가상9 (1110622, 남, SKT-타워)</label><br/>
<!-- 				*양혜리님 사용 계정 -->
<!-- 				<br/> -->
<!-- 				<input id="id142820" name="id" type="radio" value="P142820" /><label for="id142820">양혜리 (P142820, 여, SKT-타워,관리자)</label><br/>	 -->
				<input id="id12" name="id" type="radio" value="1070577" /><label for="id12">김가상0 (1070577, 남, SKT-타워)</label><br/>
				<input id="id13" name="id" type="radio" value="1070578" /><label for="id13">김가상1 (1070578, 남, SKT-타워)</label><br/>
				<input id="id14" name="id" type="radio" value="1070579" /><label for="id14">김가상2 (1070579, 남, SKT-타워)</label><br/><p/>
				<p/>
<!-- 				*김대종 사용 계정 -->
				<br/>
<!-- 				<input id="id021090" name="id" type="radio" value="P021090" /><label for="id021090">김대종 (P021090, 남, SKT-타워,관리자)</label><br/>	 -->
<!-- 				<input id="id1901080" name="id" type="radio" value="1901080" /><label for="id1901080">김대종 (1901080, 남, 분당사옥)</label><br/>	 -->

 				*패널티 대상 
				<br/>
					<input id="id10" name="id" type="radio" value="1110621" /><label for="id10">김가상8 (1110621, 남, SKT-타워)</label><br/><p/>
				<br/>
				<input id="id" name="id" type="radio" /><label for="id">직접입력</label> &nbsp;<input id="myid" type="text" placeholder="사번" onclick="setCheck();"></input>
				<p/>
				<a href="javascript:login()">테스트 로그인</a>				
			</form>
		</div>		

	</body>
	<script>
		function login(){
			var id = document.querySelector('input[name="id"]:checked').value;
			
			if(id=='on'){
				id = document.getElementById('myid').value;
			}
			
			if(!id){
				alertPopup('사용자를 선택하거나, 사번을 입력하세요.');
				return false;
			}
			
			 
			var request = new XMLHttpRequest();
			request.onreadystatechange= function () {
			    if (request.readyState==4) {
			    	console.log('testLogin', request)
			    	if(request.responseURL){
			    		location.href=request.responseURL;
			    	}else{
			    		//ie
				    	location.href = '${ROOT}/resve/status';
			    	}
			    }
			}
			request.open("POST", "${ROOT}/testSSOLogin", true);
			request.setRequestHeader("TEST_SM_USER", id);			
			request.send();
		}
		
		function setCheck(){			
			document.getElementById('id').checked = true;
		}
		
		
	</script>
</html>