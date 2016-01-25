<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
	<%	
		CIDetectionDoublEcritureProcessViewBean viewBean = (CIDetectionDoublEcritureProcessViewBean)session.getAttribute("viewBean");
		userActionValue = "pavo.process.detectionDoublEcritureProcess.executer";
		idEcran ="CCI3006";
		String emailAdresse = !globaz.jade.client.util.JadeStringUtil.isBlank(viewBean.getEmailAddress())?viewBean.getEmailAddress():"";
	%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détection des écritures doubles<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
	
							<%@page import="globaz.pavo.db.process.CIDetectionDoublEcritureProcessViewBean"%>

		<tr>
			<td>Année</td><td><input type="text" name="forAnnee" value="<%=viewBean.getForAnnee()%>"></td>
		</tr>
		<tr>						
			<td>E-mail</td><td><input type="text" name="eMailAddress" maxlength="40" size="40" style="width:8cm;" value="<%=emailAdresse%>">&nbsp;</td>
		</tr>

		<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>