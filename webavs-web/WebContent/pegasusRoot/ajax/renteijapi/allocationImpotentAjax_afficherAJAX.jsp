<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.fortuneparticuliere.PCPretEnversTiersAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.SimplePretEnversTiers"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.renteijapi.PCAllocationImpotentAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.renteijapi.SimpleAllocationImpotent"%>
<%
PCAllocationImpotentAjaxViewBean viewBean = (PCAllocationImpotentAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleAllocationImpotent entity= viewBean.getAllocationImpotent().getSimpleAllocationImpotent();
%>

<message>
	<contenu>
		<montant><%=entity.getMontant()%></montant>
		<csTypeRente><%=JadeStringUtil.isBlankOrZero(entity.getCsTypeRente())?"":entity.getCsTypeRente()%></csTypeRente>
		<csGenre><%=JadeStringUtil.isBlankOrZero(entity.getCsGenre())?"":entity.getCsGenre()%></csGenre>
		<csDegre><%=JadeStringUtil.isBlankOrZero(entity.getCsDegre())?"":entity.getCsDegre()%></csDegre>
		<dateDepot><%=entity.getDateDepot()%></dateDepot>
		<dateDecision><%=entity.getDateDecision()%></dateDecision>
		<DR><%=viewBean.getAllocationImpotent().getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu()%></DR>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>