<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%@page import="globaz.naos.db.affiliation.AFAttestationAffiliationViewBean"%>

<%
AFAttestationAffiliationViewBean viewBean = (AFAttestationAffiliationViewBean) session.getAttribute("viewBean");
idEcran = "CAF3007";
userActionValue = "naos.affiliation.attestationAffiliation.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Attestation d'affiliation<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<% if (viewBean.getAffiliationId() != null && viewBean.getAffiliationId().length() != 0) { %>
						<%@page import="globaz.naos.translation.CodeSystem"%>
<naos:AFInfoAffiliation name="affiliationId" affiliation="<%=viewBean.getAffiliation()%>"/>
						<% } %>
						<TR>
							<TD>E-Mail:</TD>
							<TD><INPUT type="text" name="email" value="<%=viewBean.getEmail()%>"></TD>
						</TR>
						<TR>
							<TD width="150">Branche économique:</TD>
							<TD><INPUT type="text" name="brancheEconomique" value="<%=CodeSystem.getLibelleIso(viewBean.getSession(),viewBean.getAffiliation().getBrancheEconomique(), viewBean.getAffiliation().getTiers().getLangueIso().toUpperCase())%>" size="60"></TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>