<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.fortuneparticuliere.PCMarchandisesStockAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleMarchandisesStock"%>
<%
PCMarchandisesStockAjaxViewBean viewBean = (PCMarchandisesStockAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleMarchandisesStock entity=viewBean.getMarchandisesStock().getSimpleMarchandisesStock();

%>

<message>
	<contenu>
		<csTypePropriete><%=JadeStringUtil.isBlankOrZero(entity.getCsTypePropriete())?"":entity.getCsTypePropriete()%></csTypePropriete>
		<part><%=entity.getPartProprieteNumerateur()%> / <%=entity.getPartProprieteDenominateur()%></part>
		<montant><%=entity.getMontantStock()%></montant>
		<DF><%=viewBean.getMarchandisesStock().getSimpleDonneeFinanciereHeader().getIsDessaisissementFortune()%></DF>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
