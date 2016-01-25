<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran = "CAF3015";
globaz.naos.db.affiliation.AFEpuCasNonSoumisViewBean viewBean = (globaz.naos.db.affiliation.AFEpuCasNonSoumisViewBean) session.getAttribute("viewBean");
userActionValue = "naos.affiliation.epuCasNonSoumis.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Epuration des cas non-soumis<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>E-mail:</TD>
							<TD><INPUT type="text" name="email" value="<%=viewBean.getEmail()%>"></TD>
							<TD width='500'>Processus qui met en "inactif" le tiers dont l'affiliation est de genre "Non soumis" et dont la date de début d'affiliation est inférieure ou égale à la date saisie dans l'écran de lancement.</TD>
						</TR>
						<TR>
							<TD>Jusqu'au:</TD>
							<TD><ct:FWCalendarTag name="fromDate" value="<%=viewBean.getFromDate()%>"/></TD>
						</TR>
						<TR>
							<TD>Simulation : <input type="checkbox" name="simulation" value="coche" checked></TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFMenuVide" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>