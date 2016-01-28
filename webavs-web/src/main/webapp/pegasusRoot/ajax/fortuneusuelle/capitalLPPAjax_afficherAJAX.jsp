<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.fortuneusuelle.PCCapitalLPPAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.SimpleCapitalLPP"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%
PCCapitalLPPAjaxViewBean viewBean = (PCCapitalLPPAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleCapitalLPP entity=viewBean.getCapitalLPP().getSimpleCapitalLPP();
	
%>

<message>
	<contenu>
		<csTypePropriete><%=JadeStringUtil.isBlankOrZero(entity.getCsTypePropriete())?"":entity.getCsTypePropriete()%></csTypePropriete>
		<part><%=entity.getPartProprieteNumerateur()%> / <%=entity.getPartProprieteDenominateur()%></part>
		<capitalLPP><%=entity.getMontantCapitalLPP()%></capitalLPP>
		<numeroPolice><%=entity.getNoPoliceNoCompte()%></numeroPolice>
		<institution><%=entity.getIdInstitutionPrevoyance()%></institution>
		<nomInstitution><%=viewBean.getNomCaisse()%></nomInstitution>
		<dateLiberation><%=entity.getDateLiberation() %></dateLiberation>
		<destinationLiberation><%=entity.getDestinationLiberation() %></destinationLiberation>
		<interet><%=entity.getIsSansInteret()%></interet>
		<montantInteret><%=entity.getMontantInteret()%></montantInteret>
		<montantFrais><%=entity.getMontantFrais()%></montantFrais>
		<DF><%=viewBean.getCapitalLPP().getSimpleDonneeFinanciereHeader().getIsDessaisissementFortune()%></DF>
		<DR><%=viewBean.getCapitalLPP().getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu()%></DR>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
