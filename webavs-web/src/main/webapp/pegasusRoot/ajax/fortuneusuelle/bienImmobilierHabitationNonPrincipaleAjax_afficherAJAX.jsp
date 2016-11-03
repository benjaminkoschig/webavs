<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.fortuneusuelle.PCBienImmobilierHabitationNonPrincipaleAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.SimpleBienImmobilierHabitationNonPrincipale"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.framework.controller.FWController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored ="false" %>
<%
PCBienImmobilierHabitationNonPrincipaleAjaxViewBean viewBean = (PCBienImmobilierHabitationNonPrincipaleAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleBienImmobilierHabitationNonPrincipale entity=viewBean.getBienImmobilierHabitationNonPrincipale().getSimpleBienImmobilierHabitationNonPrincipale();
FWController controller = (FWController) session.getAttribute("objController");
BSession objSession = (BSession)controller.getSession();
%>

<message>
	<contenu>
		<csTypePropriete><%=JadeStringUtil.isBlankOrZero(entity.getCsTypePropriete())?"":entity.getCsTypePropriete()%></csTypePropriete>
		<part><%=entity.getPartProprieteNumerateur()%> / <%=entity.getPartProprieteDenominateur()%></part>
		<isConstructionMoinsDixAns><%=entity.getIsConstructionMoinsDixAns()%></isConstructionMoinsDixAns>
		<csTypeBien><%=JadeStringUtil.isBlankOrZero(entity.getCsTypeBien())?"":entity.getCsTypeBien()%></csTypeBien>
		<autres><%=entity.getAutresTypeBien()%></autres>
		<valeurLocative><%=entity.getMontantValeurLocative() %></valeurLocative>
		<commune><%=entity.getIdCommuneDuBien()%></commune>
		<nomCommune><%=viewBean.getNomCommune()%></nomCommune>
		<pays><%=viewBean.getPays(objSession)%></pays>
		<idPays><%=entity.getIdPays()%></idPays>		
		<numeroFeuillet><%=entity.getNoFeuillet() %></numeroFeuillet>
		<detteHypothecaire><%=entity.getMontantDetteHypothecaire()%></detteHypothecaire>
		<interetHypothecaire><%=entity.getMontantInteretHypothecaire()%></interetHypothecaire>
		<numeroHypothecaire><%=entity.getNoHypotheque()%></numeroHypothecaire>
		<compagnie><c:out value="${entity.nomCompagnie}"></c:out></compagnie>
		<nomCompagnie><c:out value="${viewBean.nomNomCompagnie}"></c:out></nomCompagnie>
		<valeurVenale><%=entity.getValeurVenale()%></valeurVenale>
		<loyersEncaisses><%=entity.getMontantLoyesEncaisses()%></loyersEncaisses>
		<sousLocation><%=entity.getMontantSousLocation()%></sousLocation>		
		<DF><%=viewBean.getBienImmobilierHabitationNonPrincipale().getSimpleDonneeFinanciereHeader().getIsDessaisissementFortune()%></DF>
		<DR><%=viewBean.getBienImmobilierHabitationNonPrincipale().getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu()%></DR>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
