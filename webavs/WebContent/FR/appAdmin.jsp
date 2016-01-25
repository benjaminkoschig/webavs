<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=ISO-8859-1"
%>
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<TITLE>Application Administration</TITLE>
</HEAD>
<%
	String servletContext = request.getContextPath();
	String mainServletPath = (String)request.getAttribute("mainServletPath");
	if (mainServletPath == null) {
		mainServletPath = "/pyxis";
	}
	String formAction =  servletContext + mainServletPath;
	

	globaz.globall.db.BSession bsession = null;
	boolean hasAdminCacheResetRight = false;
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	if (controller != null) {
		bsession = (globaz.globall.db.BSession) controller.getSession();
		hasAdminCacheResetRight = bsession.hasRight("framework.admin.cache.reset", globaz.framework.secure.FWSecureConstants.UPDATE);
	}
%>
<BODY>
	<TABLE bgcolor="#B3C4DB" cellspacing="0" cellpadding="0" width="100%">
		<TBODY>
			<COL width="20">
			<COL width="200">
			<COL width="100%">
			<TR class="title">
				<TH colspan="3" class="title">Administration</TH>
			</TR>
			<TR>
				<TD colspan="3">&nbsp;</TD>
			</TR>
			<!--
			<TR>
				<TD>&nbsp;</TD>
				<TD>Mot de passe:</TD>
				<TD><A href="<%=request.getContextPath()%>/changePassword.jsp">Cliquez ici pour changer votre mot de passe</A></TD>
			</TR>
			-->
			<TR>
				<TD colspan="3">&nbsp;</TD>
			</TR>
			<% if (hasAdminCacheResetRight) { %>
			<TR>
				<TD>&nbsp;</TD>
				<TD>Enregistrement en cache:</TD>
				<TD><A href="<%=formAction%>?userAction=framework.admin.cache.reset">Cliquez ici pour vider les enregistrements en cache</A></TD>
			</TR>
			<% } %>
		</TBODY>
	</TABLE>
</BODY>
</HTML>
