<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.fortuneparticuliere.PCPretEnversTiersAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.SimplePretEnversTiers"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.renteijapi.PCIndemniteJournaliereAiAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.renteijapi.SimpleIndemniteJournaliereAi"%>
<%
PCIndemniteJournaliereAiAjaxViewBean viewBean = (PCIndemniteJournaliereAiAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleIndemniteJournaliereAi entity= viewBean.getIndemniteJournaliereAi().getSimpleIndemniteJournaliereAi();
%>

<message>
	<contenu>
		<csTypeIjai><%=JadeStringUtil.isBlankOrZero(entity.getCsTypeIjai())?"":entity.getCsTypeIjai()%></csTypeIjai>
		<nbreJours><%=entity.getNbreJours()%></nbreJours>
		<montant><%=entity.getMontant()%></montant>
		<dateDepot><%=entity.getDateDepot()%></dateDepot>
		<dateDecision><%=entity.getDateDecision()%></dateDecision>
		<dateEcheance><%=entity.getDateEcheance()%></dateEcheance>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>

	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
