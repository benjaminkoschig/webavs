<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.fortuneusuelle.PCBienImmobilierNonHabitableAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.SimpleBienImmobilierNonHabitable"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
PCBienImmobilierNonHabitableAjaxViewBean viewBean = (PCBienImmobilierNonHabitableAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleBienImmobilierNonHabitable entity=viewBean.getBienImmobilierNonHabitable().getSimpleBienImmobilierNonHabitable();	
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>

<message>
	<contenu>
		<csTypePropriete><%=JadeStringUtil.isBlankOrZero(entity.getCsTypePropriete())?"":entity.getCsTypePropriete()%></csTypePropriete>
		<part><%=entity.getPartProprieteNumerateur()%> / <%=entity.getPartProprieteDenominateur()%></part>
		<csTypeBien><%=JadeStringUtil.isBlankOrZero(entity.getCsTypeBien())?"":entity.getCsTypeBien()%></csTypeBien>
		<autres><%=entity.getAutresTypeBien()%></autres>
		<valeurVenale><%=entity.getValeurVenale()%></valeurVenale>
		<commune><c:out value="<%=entity.getIdCommuneDuBien()%>"></c:out></commune>
		<idPays><%=entity.getIdPays()%></idPays>
		<pays><%=viewBean.getPays(objSession)%></pays>
		<nomCommune><%=viewBean.getNomCommune()%></nomCommune>
		<numeroFeuillet><%=entity.getNoFeuillet() %></numeroFeuillet>
		<detteHypothecaire><%=entity.getMontantDetteHypothecaire()%></detteHypothecaire>
		<interetHypothecaire><%=entity.getMontantInteretHypothecaire()%></interetHypothecaire>
		<numeroHypothecaire><%=entity.getNoHypotheque()%></numeroHypothecaire>
		<compagnie><c:out value="<%=entity.getNomCompagnie()%>"></c:out></compagnie>
		<nomCompagnie><c:out value="<%=viewBean.getNomNomCompagnie()%>"></c:out></nomCompagnie>
		<rendements><%=entity.getMontantRendement()%></rendements>						
		<DF><%=viewBean.getBienImmobilierNonHabitable().getSimpleDonneeFinanciereHeader().getIsDessaisissementFortune()%></DF>
		<DR><%=viewBean.getBienImmobilierNonHabitable().getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu()%></DR>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>

	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
