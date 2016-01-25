<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
	<%	
		CIExportPrevhorViewBean viewBean = (CIExportPrevhorViewBean)session.getAttribute("viewBean");
		userActionValue = "pavo.compte.exportPrevhor.executer";
		String jspLocation = servletContext + mainServletPath + "Root/ti_select.jsp";
		int autoDigitAff = CIUtil.getAutoDigitAff(session);
		idEcran ="CCI2016";
	%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.pavo.db.compte.CIExportPrevhorViewBean"%><SCRIPT language="JavaScript">
	
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Export des assurés PREVHOR<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
<%@page import="globaz.pavo.util.CIUtil"%>
	<tr>
	<td>Année</td>
		<td><input type="text" name="annee" maxlength="4" onkeypress="return filterCharForPositivInteger(window.event); " value = "<%=viewBean.getAnnee()%>" size="4"/> </td>
	</tr>
	<tr>
		<td><input type="hidden" name="montantSeuilAnnuel" value="0"/> </td>
	</tr>
	<tr>
		<td>E-mail</td>
		<td><input type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getEmailAddress()%>">&nbsp;</td>
	</tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>