<%--custom tpl:insert page="/theme/capage.jtpl" --%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.framework.controller.FWAction"%><HTML>
<!--# set echo="url" -->
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
String languePage = globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session);
boolean bButtonFind = true;
String servletContext = request.getContextPath();
String mainServletPath = (String)request.getAttribute("mainServletPath");
String actionNew =  servletContext + mainServletPath + "?userAction=" + request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".afficher&_method=add";
//String actionFind = "javascript:document.forms[0].submit();";
boolean bButtonNew = objSession.hasRight("fx.user.userGroup.ajouter", FWSecureConstants.ADD);
int subTableHeight = 100;
String IFrameListHeight = "150";
String IFrameDetailHeight ="230";
if (mainServletPath == null) {
	mainServletPath = "";
}
String btnFindLabel = "Rechercher";
String btnNewLabel = "Nouveau";
if ("DE".equals(languePage)) {
	btnFindLabel = "Suchen";
	btnNewLabel = "Neu";
}
%>
<HEAD>
<SCRIPT language="JavaScript">
var langue = "<%=languePage%>"; 
</SCRIPT>
<%--custom tpl:put name="zoneInit" --%>
<%--custom /tpl:put --%>
<META name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<TITLE>userGroup_ca.jsp</TITLE>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/shortKeys.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/params.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/actionsForButtons.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/autocomplete.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/selectionPopup.js"></SCRIPT>
<script type="text/javascript" src="<%=servletContext%>/scripts/menu.js"></script>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
if ("FR" == langue) {
	shortKeys[82] = 'btnFind';//  R (Rechercher)
	shortKeys[78] = 'btnNew';//   N (Nouveau)
} else if ("DE" == langue) {
	shortKeys[83] = 'btnFind';//  S (Suchen)
	shortKeys[78] = 'btnNew';//   N (Neu)
}
function changeActionAndSubmit(newAction, target) {
  document.forms[0].action = newAction;
  document.forms[0].target = target;
  document.forms[0].submit();
}

function setUserAction(newAction) {
	document.forms[0].elements.userAction.value = newAction;
}

function setFormAction (newAction) {
	document.forms[0].action = newAction;
}
function loadFrames() {
	if(bFind) {
		document.forms[0].submit();
		// custom
		//document.fr_detail.location.href = detailLink;
	}
}

if(parent.fr_detail!=null) {
	top.fr_main.location.reload();
}

var timeWaiting = 1;
var timeWaitingId = -1;
var bFind = true;
var usrAction = "";
var servlet = "<%=(servletContext + mainServletPath)%>";
var detailLink = "";


var savedFindOnClick = 'undefined';
var savedNewOnClick = 'undefined';

function disableBtn(aBtn) {
	aBtn.onclick = '';
	//aBtn.style.display = 'none';
	aBtn.disabled = true;
}

function onClickNew() {
	// custom
	//disableBtn(document.all('btnNew'));
	var oBtnFind = document.all('btnFind');
	if (oBtnFind != null) {
		//disableBtn(oBtnFind);
	}
}

function onClickFind() {
	//disableBtn(document.all('btnFind'));
	var oBtnNew = document.all('btnNew');
	if (oBtnNew != null) {
		//disableBtn(oBtnNew);
	}
}

function showButtons() {
	var oBtnNew = document.all('btnNew');
	if (oBtnNew != null) {
		oBtnNew.onclick = savedNewOnClick;
		//oBtnNew.style.display = 'inline';
		oBtnNew.disabled = false;
	}

	var oBtnFind = document.all('btnFind');
	if (oBtnFind != null) {
		oBtnFind.onclick = savedFindOnClick;
		//oBtnFind.style.display = 'inline';
		oBtnFind.disabled = false;
	}
}

function setFocus() {
	var defaultFocusElement = document.forms[0].elements[0];
	if (defaultFocusElement == null) {
		return;
	}
	try {
		defaultFocusElement.focus();
	} catch (e) {
	
	}
}

// stop hiding -->
</SCRIPT>
<%--custom tpl:put name="zoneScripts" --%>
<script>
	usrAction = "fx.user.userGroup.lister";
	detailLink ="fx?userAction=fx.user.userGroup.afficher&_method=add&userId=<%=request.getParameter("selectedId")%>"; 
	
</script>


<%--custom /tpl:put --%>
</HEAD>
<BODY onLoad="this.focus(); setFocus();setFormAction(servlet);setUserAction(usrAction);document.getElementById('fr_list').onreadystatechange=fnStartInit;loadFrames();" onKeyDown="keyDown()" onKeyUp="keyUp();">
<FORM name="mainForm" target="fr_list">
<TABLE class="find" cellspacing="0">
	<TBODY style="border-width: 0px">
		<TR>
			<TH class="title" colspan="2">
				<%--custom tpl:put name="zoneTitle" --%>
				<DIV style="width: 100%">
					<SPAN class="idEcran">FX0103</SPAN>
					Groupes des utilisateurs
				</DIV>
<%--custom /tpl:put --%>
			</TH>
		</TR>
		<TR>
			<TD bgcolor="gray" colspan="2" height="0"></TD>
		</TR>
		<TR>
			<TD width="5">&nbsp;</TD>
			<TD>
				<TABLE border="0" height="<%=subTableHeight%>" cellspacing="0" cellpadding="0">
					<TBODY>
						<TR>
							<TD height="20">&nbsp;</TD>
						</TR>
						<%--custom tpl:put name="zoneMain" --%>
						<tr>
							<td>&nbsp;Groupe&nbsp;</td>
							<td><input type="text" name="forGroupLike" value="<%=(request.getParameter("forIdGroup")==null)?"":request.getParameter("forIdGroup")%>"></td>
							<td>&nbsp;Visa&nbsp;</td>
							<td><input type="text" name="forVisaLike" value="<%=(request.getParameter("forVisaLike")==null)?"":request.getParameter("forVisaLike")%>"></td>
							
							<td>&nbsp;Recherche exact</td>
							<td><input type="checkbox" name="exactSearch" checked ></td>
						</tr>
	 					<%-- /tpl:put --%>
						<TR>
							<TD height="20">
								<INPUT type="hidden" name="userAction" value="">
								<INPUT type="hidden" name="_type" value='<%=request.getParameter("_type")%>'>
								<INPUT type="hidden" name="_section" value='<%=request.getParameter("_section")%>'>
								<INPUT type="hidden" name="_dest" value='<%=request.getParameter("_dest")%>'>
								<INPUT type="hidden" name="_sl" value="">
								<INPUT type="hidden" name="_method" value="">
								<INPUT type="hidden" name="_valid" value="">
							</TD>
						</TR>
					</TBODY>
				</TABLE>
      			</TD>
		</TR>
		<TR>
			<TD bgcolor="#FFFFFF" colspan="2" align="right">
				<%if (bButtonFind) {%>
					<INPUT type="submit" name="btnFind" value="<%=btnFindLabel%>" onClick="onClickFind();showWaitingPopup()">
				<%} if (bButtonNew) {%>
					<!-- Custom -->
					<INPUT type="button" name="btnNew" value="<%=btnNewLabel%>" onClick="onClickNew();fr_detail.location.href='<%=actionNew%>'">
				<%}%>
			</TD>
		</TR>
	</TBODY>
</TABLE>
</FORM>
<div id="waitingPopup" style="width:120;height:50;position : absolute ; visibility : hidden">
<table border="0" cellspacing="0" cellpadding="0" bgColor="#FFFFFF" style="border: solid  1 black ; width:200;height:100%;">
<tr><td><img src="<%=request.getContextPath()%>/images/<%=languePage%>/labelRecherche.gif"></td><td><img src="<%=request.getContextPath()%>/images/points.gif"></td><td><img src="<%=request.getContextPath()%>/images/disc.gif"></td></tr>
</table>
</div>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<script>

	function showWaitingPopup() {
		if (timeWaiting != -1 && timeWaitingId == -1)
			timeWaitingId = setTimeout("if (document.getElementById('fr_list').readyState!='complete') document.getElementById('waitingPopup').style.visibility='visible';",timeWaiting*1000);		
		return true;
	}
	
	document.getElementById("waitingPopup").style.left = document.body.clientWidth/2 - document.getElementById("waitingPopup").offsetWidth/2;
	document.getElementById("waitingPopup").style.top  = getAnchorPosition("waitingPopup").y + <%=IFrameListHeight%>/2 - document.getElementById("waitingPopup").clientHeight/2;
	if (bFind)
		showWaitingPopup();

	function fnStartInit() {		
   		if (document.getElementById("fr_list").readyState=="complete") {
			showButtons();
			document.getElementById("waitingPopup").style.visibility="hidden";
			timeWaitingId = -1;
		}
	}
	oBtnFind = document.getElementById("btnFind");
	if (oBtnFind) {
		savedFindOnClick = oBtnFind.onclick;
	}
	oBtnNew = document.getElementById("btnNew");
	if (oBtnNew) {
		savedNewOnClick = oBtnNew.onclick;
	}

/*	if (document.getElementById("btnFind"))
		document.getElementById("btnFind").onclick=showWaitingPopup;*/
</script>


<IFRAME name="fr_list" scrolling="YES" style="border : solid 1px black; width:100%;"  height="<%=IFrameListHeight%>">
</IFRAME>
<PRE></PRE>
<IFRAME name="fr_detail" scrolling="NO" style="width:100%;"  height="<%=IFrameDetailHeight%>">
</IFRAME>
<%--custom tpl:put name="zoneVieuxBoutons" --%>
<ct:menuChange menuId="optionsBlank" displayId="options" showTab="menu"/>
<script>
	document.getElementsByName('fr_list')[0].style.setExpression("height","document.body.clientHeight-document.getElementsByTagName('table')[0].clientHeight-document.getElementsByName('fr_detail')[0].clientHeight-80");
</script>

<%--custom /tpl:put --%>
</BODY>
</HTML><%--custom /tpl:insert --%>