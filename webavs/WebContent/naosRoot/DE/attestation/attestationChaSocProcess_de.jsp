<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%@page import="globaz.naos.vb.attestation.AFAttestationChaSocProcessViewBean"%>

<%
AFAttestationChaSocProcessViewBean viewBean = (AFAttestationChaSocProcessViewBean) session.getAttribute("viewBean");
idEcran = "CAF3019";
userActionValue = "naos.attestation.attestationChaSocProcess.executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Zahlungsbest�tigung der Sozialabgaben<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
		<% if (viewBean.getAffiliation()!=null) { %>
			<%@page import="globaz.naos.translation.CodeSystem"%>
			<naos:AFInfoAffiliation name="affiliation" affiliation="<%=viewBean.getAffiliation()%>"/>
		<% } %>
		<TR>
			<TD>Anrede</TD>
			<TD><INPUT type="text" name="titre" value="<%=viewBean.getTitre()%>"></TD>
		</TR>
		<TR>
			<TD width="250px">Datum auf der Best�tigung</TD>
			<TD><ct:FWCalendarTag name="dateAttestation" value="<%=viewBean.getDateAttestation()%>" /></TD>
		</TR>
		<TR>
			<TD>G�ltigkeitsdatum der Best�tigung</TD>
			<TD><ct:FWCalendarTag name="dateValidite" value="<%=viewBean.getDateValidite()%>" /></TD>
		</TR>
		<TR>
			<TD>Regelm�ssigezahlungen der Beitr�ge</TD>
			<TD><input name="paiementRegulier" size="20" type="checkbox" checked style="text-align : right;"></TD>
		</TR>
		<TR>
			<TD>St�ckzahl</TD>
			<TD><INPUT type="text" name="nombreExemplaire" maxlength="2" size="1" value="<%=viewBean.getNombreExemplaire()%>"></TD>
		</TR>
		<TR>
		<TR>
			<TD>E-Mail</TD>
			<TD><INPUT type="text" name="email" value="<%=viewBean.getEmail()%>"></TD>
		</TR>
			<TD>&nbsp;</TD>
			<TD>&nbsp;</TD>
		</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>