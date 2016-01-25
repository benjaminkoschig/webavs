<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.fortuneparticuliere.PCPretEnversTiersAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.SimplePretEnversTiers"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%
PCPretEnversTiersAjaxViewBean viewBean = (PCPretEnversTiersAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimplePretEnversTiers entity=viewBean.getPretEnversTiers().getSimplePretEnversTiers();

%>
<message>
	<contenu>
		<csTypePropriete><%=JadeStringUtil.isBlankOrZero(entity.getCsTypePropriete())?"":entity.getCsTypePropriete()%></csTypePropriete>
		<part><%=entity.getPartProprieteNumerateur()%> / <%=entity.getPartProprieteDenominateur()%></part>
		<typePret><%=JadeStringUtil.isBlankOrZero(entity.getTypePret())?"":entity.getTypePret()%></typePret>
		<montant><%=entity.getMontantPret()%></montant>
		<beneficiaire><%=entity.getNomPrenomBeneficiaire()%></beneficiaire>
		<dateEcheance><%=entity.getDateEcheance()%></dateEcheance>
		<DF><%=viewBean.getPretEnversTiers().getSimpleDonneeFinanciereHeader().getIsDessaisissementFortune()%></DF>
		<DR><%=viewBean.getPretEnversTiers().getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu()%></DR>
		<interet><%=entity.getIsSansInteret()%></interet>
		<montantInteret><%=entity.getMontantInteret()%></montantInteret>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
