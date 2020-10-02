<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.fortuneparticuliere.PCPretEnversTiersAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.SimplePretEnversTiers"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>

<%@page import="globaz.pegasus.vb.habitat.PCLoyerAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.habitat.SimpleLoyer"%>

<%
PCLoyerAjaxViewBean viewBean = (PCLoyerAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleLoyer entity= viewBean.getLoyer().getSimpleLoyer();
%>



<message>
	<contenu>
		<csMotifChangementLoyer><%=JadeStringUtil.isBlankOrZero(entity.getCsMotifChangementLoyer())?"":entity.getCsMotifChangementLoyer()%></csMotifChangementLoyer>
		<csTypeLoyer><%=entity.getCsTypeLoyer()%></csTypeLoyer>
		<fraisPlacementEnfant><%=entity.getFraisPlacementEnfant()%></fraisPlacementEnfant>
		<idBailleurRegie><%=entity.getIdBailleurRegie()%></idBailleurRegie>
		<nomBailleurRegie><%= viewBean.getNomBailleurRegie() %></nomBailleurRegie>
		<idDonneeFinanciereHeader><%=entity.getIdDonneeFinanciereHeader()%></idDonneeFinanciereHeader>
		<idLoyer><%=entity.getIdLoyer()%></idLoyer>
		<isFauteuilRoulant><%=entity.getIsFauteuilRoulant()%></isFauteuilRoulant>
		<isTenueMenage><%=entity.getIsTenueMenage()%></isTenueMenage>
		<montantCharges><%=entity.getMontantCharges()%></montantCharges>
		<montantLoyerNet><%=entity.getMontantLoyerNet()%></montantLoyerNet>
		<nbPersonnes><%=entity.getNbPersonnes()%></nbPersonnes>
		<pensionNonReconnue><%=entity.getPensionNonReconnue()%></pensionNonReconnue>
		<revenuSousLocation><%=entity.getRevenuSousLocation()%></revenuSousLocation>
		<taxeJournalierePensionNonReconnue><%=entity.getTaxeJournalierePensionNonReconnue()%></taxeJournalierePensionNonReconnue>
		<csDeplafonnementAppartementPartage><%=entity.getCsDeplafonnementAppartementPartage()%></csDeplafonnementAppartementPartage>
		<nomCommune><%=viewBean.getNomCommune(entity.getIdLocalite())%></nomCommune>
		<commune><%=entity.getIdLocalite()%></commune>
		<textLibre><%=entity.getTextLibre()%></textLibre>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>