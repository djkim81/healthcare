// //var ORGCHART_URL = "../OrgChart/orgchartfrm.htm?";
// var ORGCHART_URL = "/OrgChart/orgmap.aspx?1=1";

// var __ISIE = /msie/.test(navigator.userAgent.toLowerCase());
// __ISIE = /trident/.test(navigator.userAgent.toLowerCase());
// //if (!__ISIE) ORGCHART_URL = "/OrgChart/orgmap.aspx?1=1";
// var __ISIE6 = /msie 6/.test(navigator.userAgent.toLowerCase());
// var popupWindow = null;
// var sFeatures = "center:yes;dialogHide:yes;resizable:no;status:no;unadorned:yes;scroll:no;edge:sunken;";
// var sFeaturesParam = "";
// var vArguments = new Array();
// var g_orgLoadConfig = null;


//window.document.domain = "tndtdev.sktelecom.com";

// OrgChart URL
// iframe 안에 보여줄 OrgChart 주소및 IsDimmed 파라메터 값 지정
//var g_orgchartdimmed_url = '/OrgChart/orgmap.aspx?section=app&IsDimmed=true';
//var g_orgchartdimmed_url = 'http://VoP.sktelecom.com/OrgChart/orgmap.aspx?section=app&IsDimmed=true';
//var g_orgchartWindowOpen_url = 'http://tnetdev.sktelecom.com/wps/myproxy/http/portal2.sktelecom.com/thanksletter/OrgChart/orgmap.aspx?1=1';
//var g_orgchartWindowOpen_url = 'http://portal2.sktelecom.com/thanksletter/OrgChart/orgmap.aspx?1=1';
var g_orgchartWindowOpen_url = 'http://portal.sktelecom.com/OrgChart/orgmap.aspx?1=1';

//2013-12-06 부모창 포커스 최대한 막기위한 전역변수 추가
var popupWindow = null;
// OrgChart Data 전역변수
var g_orgLoadConfig = null;
// crossdomain
var g_newDomain = 'sktelecom.com';
var timeoutLoading;
var sizeDivW = 0;
var sizeDivH = 0;

// iframe 에 해당 조직도 띄우기
function om_OpenOrgChart(config) {
    if (!config && !config.callback) {
        alert('조직도 호출 설정이 잘못되었습니다.');

    }
    else {
        // 브라우저 체크하기
        var browserType = navigator.sayswho.toUpperCase();
		
		var appType = config.appType || "deptuser";
        appType = appType.toUpperCase();
        var returnType = config.returnType || "json";
        returnType = returnType.toUpperCase();

        // 조직도 app 에서 회사 선택 여부.. winform에서 호출시 값 정해주지 않으면 기본 true로 처리하도록

        // 2013-11-27 전사 유저 또는 부서 검색이 가능하도록 수정 searchAll == true이면 검색시 자동 전체 검색
        var IsSelectCompany = true;
        if (config.selectCompany != null)
            IsSelectCompany = config.selectCompany === true ? true : false;

	//var ForceCompany = '';
	//if (config.forceCompany != null)
	//    ForceCompany = config.forceCompany;

        // var IsSearchAll = false;
		
        // if (config.searchAll != null) {
            // IsSearchAll = config.searchAll === true ? true : false;
        // }
        var isChildSelectCompany = false;
        if (IsSelectCompany == false && config.childSelectCompany != null) {
            isChildSelectCompany = config.childSelectCompany === true ? true : false;
        }
		
		var isOneSelect = config.oneSelect === true ? true : false;
		
		var data = null;
        if (!isOneSelect) { data = config.data; }
		
		//config.Modal = config.Modal || false;
		config.Modal = true;

		var selectDeptCode = (typeof (config.selectDeptCode) == "string" && config.selectDeptCode.length > 0) ? config.selectDeptCode : "";
        var CompanyCodeForGroup = (typeof (config.CompanyCodeForGroup) == "string" && config.CompanyCodeForGroup.length > 0) ? config.CompanyCodeForGroup : "";
		
		//팝업 윈도우 크기
        var d_height = 540;
        var d_width = 780; //540, 280
        if (browserType.indexOf("SAFARI") > -1 || browserType.indexOf("FIREFOX") > -1) {
            d_width = 785;
        } else if (browserType.indexOf("CHROME") > -1) {
            d_width = 795;
        }

		if (appType === "DEPTUSER") {
            if (isOneSelect) {
                d_width = d_width - 180;
            }
            else {
                d_width = d_width + 70;
            }
        }
        else if (appType === "USER") {
            if (isOneSelect) {
                d_width = d_width - 170;
            }
            else {
                d_width = d_width + 50;
            }
        }
        else if (appType === "DEPT") {
            if (isOneSelect == true) {
                if (isChildSelectCompany == true) d_width = d_width - 240;
                else { d_width = d_width - 460; }
            } else { d_width = d_width - 240; }
        }
        else if (appType === "GROUP") {
            d_width = d_width - 460;

        }
        else { }
		
        if (config.custom) {
            if (config.custom.height) {
                d_height = config.custom.height;
            }
            else {
                alert("Custom 조직도는 height를 지정해줘야 합니다."); return false;
            }
        }
        //스크린의 크기
        var sw = (document.body.clientWidth / 2) - (d_width / 2); //브라우저 중앙위치
        var sh = screen.availHeight;

        //열 창의 포지션
        var px = sw + window.screenLeft  //현재브라우저 x위치
        var py = (sh - d_height) / 2;
		
		
		//팝업 SFeature
        var sFeatures = "height=" + d_height + "px,width=" + d_width + "px,resizable=no" + ",left=" + px + ",top=" + py;
 		
		//SURL
		var sURL = '' +g_orgchartWindowOpen_url;
        if (selectDeptCode != "")
            sURL += "&SelectDeptCode=" + selectDeptCode;
		if (selectDeptCode != "")
            sURL += "&CompanyCodeForGroup=" + CompanyCodeForGroup;
        if (config.langCode) {
            sURL += "&langCode=" + config.langCode;
        }
        try { sURL += "&ACCOUNT=" + g_OrgChart_UserID; } catch (e) { }
		
        // 크로스도메인
        if (g_newDomain != '') sURL += "&newDomain=" + encodeURI(g_newDomain);
		sURL += "&AppType=" + appType;
		
        var date = new Date();
	    if (config.app == 'mail')
		 sURL += "&_dc=" + date.getTime() + "&section=mail";
		else
		 sURL += "&_dc=" + date.getTime() + "&section=app"; 

	if(config.showToolbar != true)
		sURL += "&showToolbar=false"; //툴바영역 지정 여부 추가
	
	if(config.forceCompany != null)
		sURL += "&ForceCompany=" + config.forceCompany; //회사 고정 조직도 인증 모듈과 연계함

	if(config.title != null && config.title != "")
		sURL += "&title=" + encodeURIComponent(config.title); //툴바영역 지정 여부 추가

	if(config.searchAll != "")
		sURL += "&searchAll=" + config.searchAll;

	//vArguments
	var vArguments = new Array();
	vArguments['SelectCompany'] = IsSelectCompany;
	vArguments['ChildSelectCompany'] = isChildSelectCompany;
	// vArguments['SearchAll'] = IsSearchAll;
	vArguments['CallBack'] = config.callback;
	vArguments['CustomApp'] = config.custom || null;
	vArguments['ReturnType'] = returnType;
	//default: 미리 정의된 제목
	if (config.title != null && config.title != "") vArguments['OrgMapTitle'] = encodeURIComponent(config.title);
	//선택박스 이름
	if (config.resultbox) vArguments['ResultBoxName'] = config.resultbox;
	vArguments['OnlyOneSelect'] = isOneSelect;
	vArguments['AppType'] = appType;
	vArguments['SelectGroup'] = config.selectGroup == true ? true : false;
	vArguments['OrgMapData'] = data;
	vArguments['SelectDeptCode'] = selectDeptCode;
	vArguments['CompanyCodeForGroup'] = CompanyCodeForGroup;
if (config.forceCompany)
		vArguments['ForceCompany'] = config.forceCompany;
	if (config.defaultTitle && config.title != null && config.title != "")
		vArguments['Title'] = encodeURIComponent(config.title);
		
        g_orgLoadConfig = vArguments;
      //sURL += "&IsNewWindow=true";	
		if (popupWindow != null)
			popupWindow.close();

		window.onbeforeunload = function () {
			WindowCloseHanlder();
		}
		popupWindow = window.open(sURL, "orgchart", sFeatures);	
		function WindowCloseHanlder() {
			//window.alert('My Window is reloading');
			if (popupWindow != null)
				popupWindow.close();
		}

        
        
        // 2013-12-06 익스플로러는 모달로 그냥 띄워보기
        //if (navigator.userAgent.match(/msie/i) || navigator.userAgent.match(/Trident/i)) {
			
        //    sFeatures = "center:yes;dialogHide:yes;resizable:no;status:no;unadorned:yes;scroll:no;edge:sunken;";
        //    sFeatures += "dialogHeight:" + d_height + "px;dialogWidth:" + d_width + "px;";

        //    if (config.Modal == true) {
			
		       //sURL = sURL.replace('orgmap.aspx','orgchartfrm.htm');
        //        window.showModalDialog(sURL, vArguments, sFeatures);
         //   }
          //  else {
           //     window.showModelessDialog(sURL, vArguments, sFeatures);
           // }
        //} // 2013-12-06 익스플로러 제외하고는 팝업으로 띄우기
        //else {

			//sURL += "&IsNewWindow=true";	
			//if (popupWindow != null)
			//	popupWindow.close();

			//window.onbeforeunload = function () {
			//	WindowCloseHanlder();
			//}
					
		//	popupWindow = window.open(sURL, "orgchart", sFeatures);
				
			//function WindowCloseHanlder() {
				//window.alert('My Window is reloading');
				//if (popupWindow != null)
				//	popupWindow.close();
			//}

//            popupWindow.focus();
//            document.onmousedown = focusPopup;
//            document.onkeyup = focusPopup;
//            document.onmousemove = focusPopup;
        //}
    }
};

function focusPopup() {
    if (popupWindow && !popupWindow.closed)
        popupWindow.focus();
}

function CallBackFromNewWindow(returnVal) {
    alert(returnVal);
}
function SetDomain(newDomain) {

    g_newDomain = '';
    return;
}

navigator.sayswho = (function () {
    var ua = navigator.userAgent, tem,
    M = ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*([\d\.]+)/i) || [];
    if (/trident/i.test(M[1])) {
        tem = /\brv[ :]+(\d+(\.\d+)?)/g.exec(ua) || [];
        return 'IE ' + (tem[1] || '');
    }
    M = M[2] ? [M[1], M[2]] : [navigator.appName, navigator.appVersion, '-?'];
    if ((tem = ua.match(/version\/([\.\d]+)/i)) != null) M[2] = tem[1];
    return M.join(' ');
})();

function parseXML(strXML, tagName) {
    var parseTargetTagName = './person/';
    if (typeof (tagName) == "undefined") {
        tagName = "To";
    }
    parseTargetTagName = parseTargetTagName + tagName;
    if (!strXML || typeof strXML != "string") return null;
    var dom = null;
    if (window.ActiveXObject) {
        // IE
        try {
            dom = new ActiveXObject('Microsoft.XMLDOM');
            dom.async = false;
            if (!dom.loadXML(strXML)) // parse error ..
                window.alert(dom.parseError.reason + dom.parseError.srcText);
        }
        catch (e) {
            dom = null;
        }
    }
    else {
        alert("이 함수는 MS IE만 지원합니다.");
        return null;
    }

    var fields = [
	    { name: 'EntryType' },
	    { name: 'UserID' },
	    { name: 'EmpID' },
	    { name: 'CompanyCode' },
	    { name: 'CompanyName' },
	    { name: 'DeptCode' },
	    { name: 'DeptName' },
	    { name: 'GroupCode' },
	    { name: 'GroupName' },
	    { name: 'UserName' },
	    { name: 'DisplayName' },
	    { name: 'EmailAddress' },
        { name: 'TitCode' },
        { name: 'TitName' },
	    { name: 'JobName' },
	    { name: 'DutCode' },
	    { name: 'DutName' },
	    { name: 'LocCode' },
	    { name: 'LocName' },
        { name: 'EmpCode' },
        { name: 'EmpName' },
	    { name: 'MobileTel' },
	    { name: 'OfficeTel' },
        { name: 'OfficeTel2' },
	    { name: 'OfficeTelExt' },
	    { name: 'Fax' },
	    { name: 'CountryCode' },
        { name: 'CountryName' }
    ];

    if (!dom) {
        alert("XML String을 파싱하는데 에러가 발생했습니다.");
        return null;
    }
    else {
        var records = [];
        var ns = null;
        try {
            ns = dom.selectNodes(parseTargetTagName);
        }
        catch (e) {
            //alert("XML String을 파싱하는데 에러가 발생했습니다. (" + ex.message + ")");
        }
        if (!ns) return null;

        for (var i = 0, len = ns.length; i < len; i++) {
            var n = ns[i];
            var values = {};
            for (var j = 0, jlen = fields.length; j < jlen; j++) {
                var f = fields[j];
                var v = "";
                try {
                    v = n.selectSingleNode(f.name).text;
                }
                catch (e) {
                } // ignore
                values[f.name] = v;
            }
            records[records.length] = values;
        }
        return records;
    }
}