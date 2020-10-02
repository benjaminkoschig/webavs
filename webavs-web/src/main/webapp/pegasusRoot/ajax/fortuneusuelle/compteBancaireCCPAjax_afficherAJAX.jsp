<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.fortuneusuelle.PCCompteBancaireCCPAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.SimpleCompteBancaireCCP"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.fortuneusuelle.PCCompteBancaireCCPAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.SimpleCompteBancaireCCP"%>
<%
PCCompteBancaireCCPAjaxViewBean viewBean = (PCCompteBancaireCCPAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleCompteBancaireCCP entity=viewBean.getCompteBancaireCCP().getSimpleCompteBancaireCCP();
%>

<message>
	<contenu>
		<csTypePropriete><%=JadeStringUtil.isBlankOrZero(entity.getCsTypePropriete())?"":entity.getCsTypePropriete()%></csTypePropriete>
		<part><%=entity.getPartProprieteNumerateur()%> / <%=entity.getPartProprieteDenominateur()%></part>
		<iban><%=entity.getIban()%></iban>
		<montant><%=entity.getMontant()%></montant>
		<interet><%=entity.getIsSansInteret()%></interet>
		<montantInteret><%=entity.getMontantInteret()%></montantInteret>
		<montantFrais><%=entity.getMontantFraisBancaire()%></montantFrais>
		<DF><%=viewBean.getCompteBancaireCCP().getSimpleDonneeFinanciereHeader().getIsDessaisissementFortune()%></DF>
		<DR><%=viewBean.getCompteBancaireCCP().getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu()%></DR>
		<typeDessaisissementFortune><%=viewBean.getCompteBancaireCCP().getSimpleDonneeFinanciereHeader().getTypeDessaisissementFortune()%></typeDessaisissementFortune>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
