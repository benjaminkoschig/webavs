<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
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
	mainServletPath = "/naos";
}
String selectedIdValue = "";
int tableHeight = 243;
String subTableWidth = "100%";
String formAction = servletContext + mainServletPath;
String applicationId = ((globaz.globall.db.BSession)session.getAttribute(globaz.framework.servlets.FWServlet.OBJ_SESSION)).getApplicationId();

String idEcran = null;
%>
<SCRIPT language="JavaScript">
var langue = "<%=languePage%>"; 

function doActionCancel() {
	self.close();
}


function doActionOk() {
	document.forms[0].submit();
}
</SCRIPT>
<%
java.util.List services = globaz.jade.ged.client.JadeGedFacade.getServicesList();
java.util.ListIterator iter = null;
if (services != null) {
	iter = services.listIterator();
}
%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Content-Style-Type" content="text/css"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Cache-Control" content="no-cache" /> 
<link rel="stylesheet" type="text/css" href="../../theme/master.css"/>
</head>
<body>
<FORM name="mainForm" action="<%=formAction%>">
<TABLE bgcolor="#B3C4DB" cellspacing="0" cellpadding="0" width="100%" height="<%=tableHeight%>">
	<TBODY>
		<TR>
			<TH colspan="3" height="10" class="title">
				<DIV style="width: 100%">
					<SPAN class="idEcran"><%=(null==idEcran)?"":idEcran%></SPAN>
					<ct:FWLabel key="FW_GED_SERVICE_CHOICE"/> 
				</DIV>
      		</TH>
		</TR>
		<TR>
			<TD colspan="3">&nbsp;</TD>
		</TR>
		<TR>
			<TD width="5">&nbsp;</TD>
			<TD valign="top">
			<ct:FWLabel key="FW_GED_SERVICE_PLEASE_CHOOSE"/>
			<% if (iter == null) {%>
				(iter: <%=iter%> and services: <%=services%>)
			<% } else { %>
				<select name="serviceNameId">
				<%
				while (iter.hasNext()) {
					globaz.jade.ged.message.JadeGedService nextService = (globaz.jade.ged.message.JadeGedService)iter.next();
					if (nextService == null) { %>
						<option value="(-)" label="(-)">(-)</option>
					<% } else { %>
					<option value="<%=nextService.toString()%>" label="<%=nextService.getText()%>"><%=nextService.getText()%></option>
				<%
					}
				}
				%>
				</select>
			<% }
java.util.Enumeration params = request.getParameterNames();
while(params.hasMoreElements()) {
	String nextName = (String)params.nextElement();
	String nextValue = request.getParameter(nextName);
%>
		<input type="hidden" name="<%=nextName%>" value="<%=nextValue%>"/>
<%
}
%>
		<input type="hidden" name="userAction" value="naos.affiliation.affiliation.gedafficherdossier"/>
		</TD>
			<TD width="5">&nbsp;</TD>
		</TR>
		<TR>
			<TD colspan="3" height="13">&nbsp;</TD>
		</TR>
		<TR>
			<TD bgcolor="#FFFFFF" colspan="3" align="right" height="18">
				<INPUT class="btnCtrl" id="btnVal" type="button" value="Valider" onclick="doActionOk()">
				<INPUT class="btnCtrl" id="btnCan" type="button" value="Annuler" onclick="doActionCancel()">
			</TD>
		</TR>
	</TBODY>
</TABLE>
</FORM>
</BODY>
</HTML>