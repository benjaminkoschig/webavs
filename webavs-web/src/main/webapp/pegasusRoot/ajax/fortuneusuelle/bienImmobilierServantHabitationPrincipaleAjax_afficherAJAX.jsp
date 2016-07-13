<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.fortuneusuelle.PCBienImmobilierServantHabitationPrincipaleAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.SimpleBienImmobilierServantHabitationPrincipale"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%
PCBienImmobilierServantHabitationPrincipaleAjaxViewBean viewBean = (PCBienImmobilierServantHabitationPrincipaleAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleBienImmobilierServantHabitationPrincipale entity=viewBean.getBienImmobilierServantHabitationPrincipale().getSimpleBienImmobilierServantHabitationPrincipale();	
%>

<message>
	<contenu>
		<csTypePropriete><%=JadeStringUtil.isBlankOrZero(entity.getCsTypePropriete())?"":entity.getCsTypePropriete()%></csTypePropriete>
		<part><%=entity.getPartProprieteNumerateur()%> / <%=entity.getPartProprieteDenominateur()%></part>
		<csTypeBien><%=JadeStringUtil.isBlankOrZero(entity.getCsTypeBien())?"":entity.getCsTypeBien()%></csTypeBien>
		<isConstructionMoinsDixAns><%=entity.getIsConstructionMoinsDixAns()%></isConstructionMoinsDixAns>
		<autres><%=entity.getAutresTypeBien()%></autres>
		<commune><%=entity.getIdCommuneDuBien()%></commune>
		<nomCommune><%=viewBean.getNomCommune()%></nomCommune>
		<numeroFeuillet><%=entity.getNoFeuillet() %></numeroFeuillet>
		<nombrePersonne><%=entity.getNombrePersonnes() %></nombrePersonne>
		<valeurLocative><%=entity.getMontantValeurLocative() %></valeurLocative>
		<valeurFiscale><%=entity.getMontantValeurFiscale()%></valeurFiscale>
		<detteHypothecaire><%=entity.getMontantDetteHypothecaire()%></detteHypothecaire>
		<interetHypothecaire><%=entity.getMontantInteretHypothecaire()%></interetHypothecaire>
		<numeroHypothecaire><%=entity.getNoHypotheque()%></numeroHypothecaire>
		<compagnie><%=entity.getNomCompagnie()%></compagnie>
		<nomCompagnie><%=viewBean.getNomNomCompagnie()%></nomCompagnie>
		<loyersEncaisses><%=entity.getMontantLoyesEncaisses()%></loyersEncaisses>
		<sousLocation><%=entity.getMontantSousLocation()%></sousLocation>				
		<DF><%=viewBean.getBienImmobilierServantHabitationPrincipale().getSimpleDonneeFinanciereHeader().getIsDessaisissementFortune()%></DF>
		<DR><%=viewBean.getBienImmobilierServantHabitationPrincipale().getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu()%></DR>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>

	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
