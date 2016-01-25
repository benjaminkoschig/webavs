<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<!--# set echo="url" -->
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<HEAD>
<% String usrAction = "";
%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<!-- #BeginEditable "zoneInit" -->
<%
String servletContext = request.getContextPath();
String languePage = globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session);
%>
<SCRIPT language="JavaScript">
var langue = "<%=languePage%>"; 
</SCRIPT>
<!-- #EndEditable -->
<!-- #BeginEditable "zoneBusiness" --><!-- #EndEditable -->
<META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<TITLE></TITLE>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/params.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/actions.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/swap.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/shortKeys.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<!--
 ************************
 * Fonctions js à faire *
 * - init()             *
 * - onOk()             *
 * - onCancel()         *
 ************************
 -->
<!-- #BeginEditable "zoneScripts" -->

<SCRIPT>
function init() {

}

function onOk() {
	returnValue = "SUPPRIMER_SUSPENS";
	window.close();
}

function onCancel() {
	window.close();
}
</SCRIPT>
<!-- #EndEditable -->
<SCRIPT>
function doActionOk() {
	onOk();
}

function doActionCancel() {
	onCancel();
}
</SCRIPT>
</HEAD>
<BODY onload="this.focus();init()" onKeyDown="keyDown();actionKeyDown();" onKeyUp="keyUp();actionKeyUp();">
	<TABLE bgcolor="#B3C4DB" cellspacing="0" cellpadding="0" width="100%" height="200">
		<TBODY>
			<TR>
				<TD colspan="3" height="10" class="title">
				<!-- #BeginEditable "zoneTitle" -->
					Suppression d'une écriture en suspens.
      				<!-- #EndEditable -->
      				</TD>

			</TR>
			<TR>
				<TD bgcolor="#FFFFFF" colspan="3" height="3"></TD>
			</TR>
			<TR>
				<TD colspan="3">&nbsp;</TD>
			</TR>
			<TR>
				<TD width="5" height="152">&nbsp;</TD>
				<TD valign="top" height="100" colspan="2">
					<FORM name="mainForm">
        					<TABLE border="0" cellspacing="0" cellpadding="0" >
							<TBODY>
			  				<!-- #BeginEditable "zoneMain" -->
								<TR>
									<TD>
										L'écriture que vous voulez supprimer est de type SUSPENS.
										<BR>
										Voulez vous mettre son type à SUSPENS_SUPPRIME ? : bouton <b>Valider</b>
										<BR>
										Voulez supprimer physiquement cette écriture ? : bouton <b>Annuler</b>
									</TD>
								</TR>
			  				<!-- #EndEditable -->
							</TBODY>
        					</TABLE>
						<INPUT Type="hidden" name="userAction" value="<%=usrAction%>">
						<INPUT type="hidden" name="_method" value='<%=request.getParameter("_method")%>'>
						<INPUT type="hidden" name="_valid" value='<%=request.getParameter("_valid")%>'>
						<INPUT type="hidden" name="_sl" value="">
					</FORM>
				</TD>
			</TR>
			<TR>
				<TD colspan="3" height="13">&nbsp;</TD>
			</TR>
			<TR>
				<TD bgcolor="#FFFFFF" colspan="3" align="right" height="18">
					<IMG name="btnOk" src="<%=request.getContextPath()%>/images/<%=languePage%>/btnValidate.gif" border="0" onClick="doActionOk()">
					<IMG name="btnCancel" src="<%=request.getContextPath()%>/images/<%=languePage%>/btnCancel.gif" border="0" onClick="doActionCancel()">
				</TD>
			</TR>
		</TBODY>
	</TABLE>
<!-- #BeginEditable "zoneEndPage" -->
<!-- #EndEditable -->
</BODY>
</HTML>