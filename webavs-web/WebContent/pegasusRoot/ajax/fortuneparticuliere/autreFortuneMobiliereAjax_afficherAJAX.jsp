<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.fortuneparticuliere.PCPretEnversTiersAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.SimplePretEnversTiers"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.fortuneparticuliere.PCAutreFortuneMobiliereAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleAutreFortuneMobiliere"%>
<%
PCAutreFortuneMobiliereAjaxViewBean viewBean = (PCAutreFortuneMobiliereAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleAutreFortuneMobiliere entity=viewBean.getAutreFortuneMobiliere().getSimpleAutreFortuneMobiliere();
%>

<message>
	<contenu>
		<csTypePropriete><%=JadeStringUtil.isBlankOrZero(entity.getCsTypePropriete())?"":entity.getCsTypePropriete()%></csTypePropriete>
		<part><%=entity.getPartProprieteNumerateur()%> / <%=entity.getPartProprieteDenominateur()%></part>
		<montant><%=entity.getMontant()%></montant>
		<csTypeFortune><%=JadeStringUtil.isBlankOrZero(entity.getCsTypeFortune())?"":entity.getCsTypeFortune()%></csTypeFortune>
		<autres><%=entity.getAutre()%></autres>
		<DF><%=viewBean.getAutreFortuneMobiliere().getSimpleDonneeFinanciereHeader().getIsDessaisissementFortune()%></DF>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>

	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
