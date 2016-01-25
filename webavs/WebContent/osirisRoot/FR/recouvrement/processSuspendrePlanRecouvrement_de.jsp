<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCA60010"; %>
<%
globaz.osiris.db.recouvrement.CAProcessSuspendrePlanRecouvrementViewBean viewBean = (globaz.osiris.db.recouvrement.CAProcessSuspendrePlanRecouvrementViewBean) session.getAttribute("viewBean");
userActionValue = "osiris.recouvrement.processSuspendrePlanRecouvrement.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
top.document.title = "Process - Suspendre plan recouvrement - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Annuler sursis au paiement<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<TR>
	<TD align="left" width="180" height="21" valign="middle">E-mail</TD>
	<TD align="left">
		<INPUT type="text" name="email" class="libelleLong" value="<%=viewBean.getEmail()%>"/>
		<INPUT type="hidden" name="idPlanRecouvrement" value="<%=viewBean.getIdPlanRecouvrement()%>"/>
</TD>
</TR>
<TR>
	<TD align="left" width="180" height="21" valign="middle">Date d'annulation</TD>
	<TD align="left">
		<INPUT type="text" name="dateSuspension" value="<%=viewBean.getDateSuspension()%>" class="date disabled" readonly>
	</TD>
</TR>
<TR>
	<TD align="left" width="180" height="21" valign="middle">Exécuter sommation</TD>
	<TD align="left">
		<input type="checkbox" name="sendSommation" id="sendSommation" <%=(viewBean.getSendSommation().booleanValue())? "checked" : "unchecked"%> ><label for="sendSommation"></label>
	</TD>
</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>