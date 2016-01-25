
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML><!-- #BeginTemplate "/Templates/conflict.dwt" -->
<!--# set echo="url" -->
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<HEAD>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
String lastModification = "";
String languePage = globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session);
String usrAction = "";
String servletContext = request.getContextPath();
String mainServletPath = (String)request.getAttribute("mainServletPath");

if (mainServletPath == null) {
	mainServletPath = "";
}
String selectedIdValue = "";
int tableHeight = 243;
String subTableWidth = "100%";
String formAction = servletContext + mainServletPath;

String btnValLabel = "Valider";
String btnCanLabel = "Annuler";
if("DE".equals(languePage)) {
	btnValLabel = "Best&auml;tigen";
	btnCanLabel = "Annulieren";
}
%>
<SCRIPT language="JavaScript">
var langue = "<%=languePage%>";
</SCRIPT>
<%
String MSG_PROCESS_OK = "The process successfully started.";
if ("FR".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "La tâche a démarré avec succès.";
} else if ("DE".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "Prozess erfolgreich gestartet.";
}
%>
<!-- #BeginEditable "zoneInit" -->
<%
globaz.hermes.db.gestion.HEInputAnnonceViewBean viewBean = (globaz.hermes.db.gestion.HEInputAnnonceViewBean)request.getAttribute("viewBean");
%>

<!-- #EndEditable -->
<!-- #BeginEditable "zoneBusiness" --><!-- #EndEditable -->
<META name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<TITLE></TITLE>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/params.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/actionsForButtons.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/swap.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/shortKeys.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/autocomplete.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/selectionPopup.js"></SCRIPT>
<SCRIPT language="javascript">
<!--
var errorObj = new Object();
errorObj.text = "";

function showErrors() {
	if (errorObj.text != "") {
		showModalDialog('<%=servletContext%>/errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');
	}
}
-->
</SCRIPT>
<!--
 ************************
 * Fonctions js à faire *
 * - init()             *
 * - onOk()             *
 * - onCancel()         *
 ************************
 -->
<!-- #BeginEditable "zoneScripts" -->
<script language="JavaScript">
function init() { }
function onOk() { }
function onCancel() { }
</script>
<!-- #EndEditable -->
<SCRIPT>
<!--
function doActionOk() {
	onOk();
}

function doActionCancel() {
	onCancel();
}
-->
</SCRIPT>
</HEAD>
<BODY onload="this.focus();init();showErrors();" onKeyDown="keyDown();actionKeyDown();" onKeyUp="keyUp();actionKeyUp();">
<TABLE bgcolor="#B3C4DB" cellspacing="0" cellpadding="0" width="100%" height="<%=tableHeight%>">
	<TBODY>
		<TR>
			<TH colspan="3" height="10" class="title">
				<!-- #BeginEditable "zoneTitle" -->
      <!-- #EndEditable -->
      		</TH>
		</TR>
		<TR>
			<TD bgcolor="gray" colspan="3" height="0"></TD>
		</TR>
		<TR>
			<TD colspan="3">&nbsp;</TD>
		</TR>
		<TR>
			<TD width="5">&nbsp;</TD>
			<TD valign="top">
			<FORM name="mainForm" action="<%=formAction%>">
        <TABLE border="0" cellspacing="0" cellpadding="0" width="<%=subTableWidth%>">
			<TBODY>
			  <!-- #BeginEditable "zoneMain" -->

		<!-- #EndEditable -->
   		<%
		if ("yes".equalsIgnoreCase(request.getParameter("processStarted"))) {
		%>
		<TR class="title">
			<TD colspan="2" style="color:white; text-align:center">
			<SPAN style="color:palegreen">&gt;</SPAN> <%=MSG_PROCESS_OK%> <SPAN style="color:palegreen">&lt;</SPAN>
			</TD>
		</TR>
		<%
		}
		%>

			</TBODY>
        </TABLE>
		<INPUT type="hidden" name="selectedId" value="<%=selectedIdValue%>">
		<INPUT Type="hidden" name="userAction" value="<%=usrAction%>">
		<INPUT type="hidden" name="_method" value='<%=request.getParameter("_method")%>'>
		<INPUT type="hidden" name="_valid" value='<%=request.getParameter("_valid")%>'>
		<INPUT type="hidden" name="_sl" value="">
	</FORM>
			</TD>
			<TD width="5">&nbsp;</TD>
		</TR>
		<TR>
			<TD colspan="3" height="13">&nbsp;</TD>
		</TR>
		<TR>
			<TD bgcolor="#FFFFFF" colspan="3" align="right" height="18">
				<INPUT class="btnCtrl" id="btnVal" type="button" value="<%=btnValLabel%>" onclick="doActionOk()" active="true">
				<INPUT class="btnCtrl" id="btnCan" type="button" value="<%=btnCanLabel%>" onclick="doActionCancel(); action(ROLLBACK);" active="true">
			</TD>
		</TR>
		<TR>
			<TD bgcolor="#FFFFFF"></TD>
			<TD bgcolor="#FFFFFF" colspan="2" align="left"><FONT  color="#FF0000">
				<% if (viewBean.getMsgType().equals (viewBean.ERROR) == true) {%>
					<script>
						errorObj.text = "<%=globaz.framework.util.FWTextFormatter.slash(globaz.framework.util.FWTextFormatter.newLineToBr(viewBean.getMessage()), '\"')%>";
						<%
							viewBean.setMessage("");
							viewBean.setMsgType(viewBean.OK);
						%>
					</script>
				<% } %>
			</FONT></TD>
		</TR>
	</TBODY>
</TABLE>
<!-- #BeginEditable "zoneEndPage" --> <!-- #EndEditable -->

</BODY>
<!-- #EndTemplate --></HTML>
