<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.fortuneusuelle.PCTitreAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.SimpleTitre"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.fortuneusuelle.PCTitreAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.SimpleTitre"%>
<%
PCTitreAjaxViewBean viewBean = (PCTitreAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleTitre entity=viewBean.getTitre().getSimpleTitre();
%>

<message>
	<contenu>
		<csTypePropriete><%=JadeStringUtil.isBlankOrZero(entity.getCsTypePropriete())?"":entity.getCsTypePropriete()%></csTypePropriete>
		<part><%=entity.getPartProprieteNumerateur()%> / <%=entity.getPartProprieteDenominateur()%></part>
		<csGenre><%=JadeStringUtil.isBlankOrZero(entity.getCsGenreTitre())?"":entity.getCsGenreTitre()%></csGenre>
		<autres><%=entity.getAutreGenreTitre()%></autres>
		<designation><%=entity.getDesignationTitre()%></designation>
		<valeur><%=entity.getNumeroValeur()%></valeur>
		<montant><%=entity.getMontantTitre()%></montant>
		<sansRendement><%=entity.getIsSansRendement()%></sansRendement>
		<rendement><%=entity.getRendementTitre()%></rendement>
		<droitGarde><%=entity.getDroitDeGarde()%></droitGarde>
		<DF><%=viewBean.getTitre().getSimpleDonneeFinanciereHeader().getIsDessaisissementFortune()%></DF>
		<DR><%=viewBean.getTitre().getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu()%></DR>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
