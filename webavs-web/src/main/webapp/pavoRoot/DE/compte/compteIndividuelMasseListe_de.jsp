<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<!--# set echo="url" -->
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<HEAD>
<%
String languePage = globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session);
String selectedIdValue = "";
String userActionValue = "";
String servletContext = request.getContextPath();
String mainServletPath = (String)request.getAttribute("mainServletPath");
if (mainServletPath == null) {
	mainServletPath = "";
}
String formAction = servletContext + mainServletPath;
int tableHeight = 243;
String subTableWidth = "100%";
String idEcran = "CCI2006";
String processStarted = request.getParameter("process");
boolean processLaunched = "launched".equalsIgnoreCase(processStarted);
%>

<%
		globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
		globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
		String userActionUpd = request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".modifier";
    	globaz.pavo.db.compte.CIMasseListeViewBean viewBean = (globaz.pavo.db.compte.CIMasseListeViewBean)session.getAttribute ("viewBean");
		userActionValue = "pavo.compte.compteIndividuel.compareMasseSalarialeListeExecuter.executer";
		String eMailAddress = objSession.getUserEMail()!=null?objSession.getUserEMail():"";
		

%>

<META name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<TITLE></TITLE>
<SCRIPT language="JavaScript">
var langue = "<%=languePage%>"; 

</SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/params.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/actionsForButtons.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/swap.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/shortKeys.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="javascript"> 
var errorObj = new Object();
errorObj.text = "";

function showErrors() {
	if (errorObj.text != "") {
		showModalDialog('errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
	}
}
</SCRIPT>
<style type="text/css">
.visible {
	visibility: hidden;
	display: none;
}
</style>
<% 
/*
A faire:
function add() {}
function upd() {}
function validate() {}
function cancel() {}
function del() {}
function init(){}
*/
%>

</HEAD>
<BODY onload="this.focus();showErrors();" onKeyDown="keyDown();actionKeyDown();" onKeyUp="keyUp();actionKeyUp();">
<TABLE bgcolor="#B3C4DB" cellspacing="0" cellpadding="0" width="100%" height="<%=tableHeight%>">
	<TBODY>
		<TR>
			<TH colspan="3" height="10" class="title">
				<DIV style="width: 100%">
					<SPAN style="float:right; width:100px; font-weight: normal;text-align:right; font-size:8pt"><%=(null==idEcran)?"":idEcran%></SPAN>
			Vergleichung der Lohnsumme ausdrucken
				</DIV>
			</TH>
		</TR>
		<TR height="0">
			<TD bgcolor="gray" colspan="3" style="height:1px"></TD>
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
						
						<TR>
							<td>E-Mail Adresse</td>
							<TD><input type="text" size="40" name="email" value="<%=eMailAddress%>"></TD>
						</TR>
						<TR>
							<TD>Beginnjahr</TD>
							<TD><input type="text" size="4" maxlength="4" name="anneeDebut"></TD>
						</TR>
						<TR>
							<TD>Endjahr</TD>
							<TD><input type="text" size="4" maxlength="4" name="anneeFin"></TD>
						</TR>
						
					</TBODY>
				</TABLE>
				<INPUT type="hidden" name="selectedId" value="<%=selectedIdValue%>">
				<INPUT type="hidden" name="userAction" value="<%=userActionValue%>">
				<INPUT type="hidden" name="_method" value='<%=request.getParameter("_method")%>'>
				<INPUT type="hidden" name="_valid" value='<%=request.getParameter("_valid")%>'>
				<INPUT type="hidden" name="_sl" value="">
			</FORM>
			</TD>
			<TD width="5">&nbsp;</TD>
		</TR>
		</TR>
		<% if (processLaunched) {%>
		<tr>
			<td colspan="3" style="height: 2em; color: white; font-weight: bold; text-align: center;background-color: green">
			
			<ct:FWLabel key="FW_PROCESS_STARTED"/></td>
			
		</tr>
		<% } %>
		<TR>
			<%=processLaunched %>
			<%if(!processLaunched){%>
				<TD bgcolor="#FFFFFF" colspan="3" align="center"><INPUT name="Ok" type="button" value="Ok" style="width:60" onClick="document.forms[0].submit();"></TD>
			<%}%>
		</TR>
		<TR>
			<TD bgcolor="#FFFFFF"></TD>
			<TD bgcolor="#FFFFFF" colspan="2" align="left"><FONT  color="#FF0000">
				<% if (globaz.framework.bean.FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {%>
					<script>
						errorObj.text = "<%=globaz.framework.util.FWTextFormatter.slash(globaz.framework.util.FWTextFormatter.newLineToBr(viewBean.getMessage()), '\"')%>";
						<%
							viewBean.setMessage("");
							viewBean.setMsgType(globaz.framework.bean.FWViewBeanInterface.OK);
						%>
					</script>
				<% } %>
			</FONT></TD>
		</TR>
	</TBODY>
</TABLE>
<!-- p align="right"><input type="button" name="Button" value="Lancer" onClick="document.forms[0].submit();"></p-->
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> 
</BODY>
</HTML>