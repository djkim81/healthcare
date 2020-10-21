<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<div class="pop-head">
	<h2>코드 등록</h2>
	<a href="#none" class="close layerClose">닫기</a> 
</div>
<div class="pop-container">
	<!-- 팝업 컨텐츠 S -->
	<table id="codeManageCreater_enter" class="tbl-style">
		<colgroup>
			<col style="width:30%;">
			<col>
		</colgroup>
		<tr>
			<th class="required" >코드타입(대)</th>
			<td>
				<select id="codeType_createVal" data-code-type="TYL"> 
					<option value="" selected="selected">선택</option>
				</select>
			</td>
		</tr>
		<tr>
			<th class="selectiveRequired smallCate">코드타입(소)</th>
			<td class="smallCate">
				<select id="codeTys_createVal" data-code-tyl="BLD" data-empty-str="선택"></select>
			</td>
		</tr>
		<tr>
			<th class="required" >코드값</th>
			<td>
				<input id="code_createVal" type="text" placeholder="코드값을 입력하세요." style="ime-mode:disabled; text-transform: uppercase;">
			</td>
		</tr>
		<tr>
			<th class="required" >코드명</th>
			<td>
				<input id="codeNm_createVal" type="text" placeholder="코드명을 입력하세요." style="ime-mode:active;">
			</td>
		</tr>
	</table>
	
	<div class='rv-desc' style='text-align: center; display:none;'>
		<strong id="requiredMsg"></strong>
	</div>
	<div class="pop-btn-area">
<!-- 		<button id ="scheduleCreate_saveBtn" class="pop-btn">저장</button> -->
		<button id ="codeManageCreate_saveBtn" class="pop-btn">저장</button>
		<button class="pop-btn gray layerClose">취소</button>
	</div>
</div><!-- //pop-container -->
<script>
var popCodeManageCreate = {
	init: function() {
		popCodeManageCreate.comboBox.comboDataSet(undefined, "#codeManageCreater_enter");
		popCodeManageCreate.comboBox.tysComboBoxSet(); //tyl 베드 선택 시 활성화 이벤트
		popCodeManageCreate.comboBox.tylComboEventBinding(); //tyl 이벤트 세팅
		loadCodeSelect(undefined, '#codeManageCreater_enter'); //콤보박스 공통코드 세팅
		popCodeManageCreate.button.popSaveClickEvent(); //저장 시 이벤트 - validation 및 코드 중복체크 로직 필요
	},
		
// 		---START------------------------------------------------------------------------------------------------
	comboBox: {
		comboDataSet: function(cb, selector) {
			var $container = selector ? $(selector) : $(document);
			$container.find('select[data-code-type]').each(function(idx, select) {
				var cType = $(select).data('code-type'); //코드타입(대)
				var tyl = $("#codeType_createVal").val();
				var tys = $("#codeTys_createVal").val();
				var empStr = $(select).data('empty-str');
				
				if(tyl === 'BLD') {
					$(select).on('change', function() {
						$('[data-code-tyl=BED]').data('code-tys', $(this).val()).empty();
					});
				}
				
				$.ajax({
					url: ROOT + '/cmmn/codeKindList',
					data: {codeType: cType, codeTyl: tyl, codeTys: tys || ''},
					success : function(res) {
    						if(res.status === 200) {
							var options = res.list.map(function(data) {
									return $('<option>').val(data.CODE).text(data.CODE_NM);
								});
							if(empStr) {
								$(select).append($('<option>').val("").text(empStr));
							}
							$(select).append(options);
							
							if(cb) cb(); //콜백이 있다면 실행
						}
					},
					error : function(err) {
						console.error(err)
					}
				});
			});
		},
		
		tysComboBoxSet: function() {
			if($('#codeType_createVal').val() === 'BED') {
				$('.smallCate').show();
				$('#codeTys_createVal').attr('disabled', false);
			} else {

				$('.smallCate').hide();
				$('#codeTys_createVal option:eq("")').attr('selected', true);
				$('#codeTys_createVal').attr('disabled', true);
			}
		},
		
		tylComboEventBinding: function() {
			$('#codeType_createVal').on('change', function() {

				popCodeManageCreate.comboBox.tysComboBoxSet();
			});
		}
	},
	
	params: {
		codeTyl: "",
		codeTys: "",
		code: "",
		codeNm: "",
		regEmpno: "",
		regDt: "",
		rowData:null,
		rowCnt:""
	}, 
	
	button: {
		popSaveClickEvent: function() {
			$("#codeManageCreate_saveBtn").on("click", function() {
			    
				if(!popCodeManageCreate.validation.required()) {
					return false;
				}
		
				$.when(popCodeManageCreate.validation.duplicateCheck())
					.then(function(retVal){
						if(retVal) {
							confirmPopup('코드를 등록 하시겠습니까?', function() {

								$.ajax({
									url: ROOT + '/cmmn/insertCodeManage',
									type: 'POST',
									data: {
										codeTyl: $("#codeType_createVal").val(), 
										codeTys: $("#codeTys_createVal").val(),
										code: $("#code_createVal").val().replace(/\s/gi,""),
										codeNm: $("#codeNm_createVal").val()
										},
									success : function(res) {
										codeManageList.list.renderCodeManageList();
										alertPopup('등록 되었습니다.');

									},
									error : function(err) {
										alertPopup(err.responseText);
									}
								}); 
							});
						};
					})
				
				

				
			});
		}
	},
// 		----END-------------------------------------------------------------------------------------------------
		
	validation: {
		required: function() {
			 var chk = true;
			 var returnMsg=" 등록된 값이 없습니다";
				$(".required").nextAll("td").children("input,select").each(function() {
					if($(this).val() == null || $(this).val() == "") {
						if($("#codeType_createVal").val() == "BED" && ($("#codeTys_createVal").val() == null || $("#codeTys_createVal").val() == "")) {
							returnMsg = $("#codeTys_createVal").parent().prev().text() + returnMsg;
						} else {
							returnMsg = $(this).parent().prev().text() + returnMsg;
						}
						$(".rv-desc").show();
						$("#requiredMsg").text(returnMsg);
						chk = false;
						return false;
					};
				});  
			 return chk;
		},
		
		duplicateCheck: function() {
			var returnMsg = "";
			var retVal = false;
			var deferred = $.Deferred();
			$.ajax({
				url: ROOT + '/cmmn/selectCodeManageDuplicateCount',
				type: 'POST',
				data: {codeTyl: $("#codeType_createVal").val(), 
					   codeTys: $("#codeTys_createVal").val(),
					   code: $("#code_createVal").val()
					   },
				success : function(result) {
					if(result.status === 200) {

						if(parseInt(result.customs.duplCnt) > 0) {
							returnMsg = "중복된 코드입니다.";
							$(".rv-desc").show();
							$("#requiredMsg").text(returnMsg);
							retVal = false;
						} else {
							$(".rv-desc").hide();
							retVal = true;
						}

					}
					deferred.resolve(retVal);
				},
				error : function(err) {
					console.error(err)
				}
			});
			
			return deferred;

		}
	}
}

$(document).ready(function() {
	popCodeManageCreate.init();
});
</script>
