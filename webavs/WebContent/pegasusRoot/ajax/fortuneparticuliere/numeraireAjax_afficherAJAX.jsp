<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.fortuneparticuliere.PCPretEnversTiersAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.SimplePretEnversTiers"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.fortuneparticuliere.PCNumeraireAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleNumeraire"%>
<%
PCNumeraireAjaxViewBean viewBean = (PCNumeraireAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleNumeraire entity=viewBean.getNumeraire().getSimpleNumeraire();
%>

<message>
	<contenu>
		<csTypePropriete><%=JadeStringUtil.isBlankOrZero(entity.getCsTypePropriete())?"":entity.getCsTypePropriete()%></csTypePropriete>
		<part><%=entity.getPartProprieteNumerateur()%> / <%=entity.getPartProprieteDenominateur()%></part>
		<montant><%=entity.getMontant()%></montant>
		<DF><%=viewBean.getNumeraire().getSimpleDonneeFinanciereHeader().getIsDessaisissementFortune()%></DF>
		<DR><%=viewBean.getNumeraire().getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu()%></DR>
		<interet><%=entity.getIsSansInteret()%></interet>		
		<montantInteret><%=entity.getMontantInteret()%></montantInteret>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>

	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
