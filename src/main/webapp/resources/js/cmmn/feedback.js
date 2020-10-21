/**
 * 문의/오류 신고하기
 */
var feedback = {
	// 팝업 호출
	popup : function(){
		$('#layer_pop14').load(
			ROOT + '/pop/feedback', {}, 
			function(res){
				$('#layer_pop14 #btnOk').on('click', function(){
					feedback.send();
				});	
				openLayerPopup('layer_pop14');
			}
		);	
	},
	// 신고하기
	send : function(){		
		var contents = $('#feedbackContents').val();
		
		if(!contents){
			alert('내용을 입력하세요.');
			return false;
		}
		
		$.ajax({
			url: ROOT + '/feedback',
			type: 'POST',
			data: {contents: contents},
			success : function(res){						
				//resveStatus.table.refresh();
				if(res.item > 0){
					$.alert({text:'등록하였습니다.', desc:'신속히 답변드리겠습니다.', icon: false});
				}
				closeLayerPopup();
			},
			error : function(err) {
				console.error(err)
			}
		});
	}
}