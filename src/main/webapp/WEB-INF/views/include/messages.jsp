<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<script>
var messages = {
	'system.error' : '<spring:message code="system.error"  javaScriptEscape="true" />',
	'error.invalidRequest' : '<spring:message code="error.invalidRequest"  javaScriptEscape="true" />',
	'error.notAvailable' : '<spring:message code="error.notAvailable"  javaScriptEscape="true" />',
	'error.processFailure' : '<spring:message code="error.processFailure"  javaScriptEscape="true" />',
	'error.duplicateDayResve' : '<spring:message code="error.duplicateDayResve"  javaScriptEscape="true" />',
	'error.over20min' : '<spring:message code="error.over20min"  javaScriptEscape="true" />',
	'error.over20minCancel' : '<spring:message code="error.over20minCancel"  javaScriptEscape="true" />',
	'error.onlySameday' : '<spring:message code="error.onlySameday"  javaScriptEscape="true" />',
	'error.canceledResve' : '<spring:message code="error.canceledResve"  javaScriptEscape="true" />',
	'error.completeResve' : '<spring:message code="error.completeResve"  javaScriptEscape="true" />',
	'error.canNotSuccessionCancel' : '<spring:message code="error.canNotSuccessionCancel"  javaScriptEscape="true" />',	
	'error.resveNotFound' : '<spring:message code="error.resveNotFound" javaScriptEscape="true" />',
	'error.requireEmpno' : '<spring:message code="error.requireEmpno" javaScriptEscape="true" />',
	'error.invalidStartDate' : '<spring:message code="error.invalidStartDate" javaScriptEscape="true" />',
	'error.invalidEndDate' : '<spring:message code="error.invalidEndDate" javaScriptEscape="true" />',
	'error.dateCompareStartEnd' : '<spring:message code="error.dateCompareStartEnd" javaScriptEscape="true" />',
	'error.timeCompareStartEnd' : '<spring:message code="error.timeCompareStartEnd" javaScriptEscape="true" />',
	'error.alreadyConfirm' : '<spring:message code="error.alreadyConfirm" javaScriptEscape="true" />',
	'error.reqireAgree' : '<spring:message code="error.reqireAgree" javaScriptEscape="true" />'	
}

/**
 *  메시지 코드로 메시지 구하기
 *   - ex) getMessage('error.invalidRequest');
 *   - ex) getMessage('error.invalidRequest', arg1);
 *   - ex) getMessage('error.invalidRequest', [arg1, arg2, arg3 ...]);
 */
function getMessage(code, args){
	var result = messages[code] === undefined? code : messages[code];
	if(args){
		if(Array.isArray(args)){
			for(var i=0; i<args.length; i++){
				result = result.split('{'+i+'}').join(args[i]);
			}
		}else{
			result = result.split('{0}').join(param);
		}
	}		
	return result;
}
</script>