<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.fortuneparticuliere.PCPretEnversTiersAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.SimplePretEnversTiers"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.pegasus.utils.PCCommonHandler"%>
<%@page import="globaz.pegasus.vb.habitat.PCTaxeJournaliereHomeAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.habitat.SimpleTaxeJournaliereHome"%>
<%@page import="globaz.jade.client.util.JadeNumericUtil"%>

<%@page import="ch.globaz.pegasus.business.models.home.TypeChambreSearch"%>
<%
	PCTaxeJournaliereHomeAjaxViewBean viewBean = (PCTaxeJournaliereHomeAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
    viewBean.setAfficheTypeChambre(Boolean.parseBoolean(request.getParameter("afficheTypeChambre")));
	TypeChambreSearch typeChambreSearch = new TypeChambreSearch();
	SimpleTaxeJournaliereHome entity= viewBean.getTaxeJournaliereHome().getSimpleTaxeJournaliereHome();
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>


<%@page import="ch.globaz.pegasus.business.services.PegasusServiceLocator"%>
<%@page import="ch.globaz.pegasus.business.models.home.TypeChambre"%>
<%@page import="globaz.pegasus.vb.home.PCTypeChambreListViewBean"%>
<%@page import="globaz.pegasus.utils.PCTaxeJournaliereHomeHandler"%><message>
	<contenu>
		<idTypeChambre><%=PCCommonHandler.getStringDefault(entity.getIdTypeChambre())%></idTypeChambre>
		<dateEcheance><%=entity.getDateEcheance()%></dateEcheance>
		<idAssureurMaladie><%=PCCommonHandler.getStringDefault(entity.getIdAssureurMaladie())%></idAssureurMaladie>
		<libelleAssureurMaladie><%=PCTaxeJournaliereHomeHandler.getLibelleAssurenceMaladie(viewBean.getTaxeJournaliereHome().getTiersAssurenceMaladie(),objSession)%></libelleAssureurMaladie>
		<idDonneeFinanciereHeader><%=entity.getIdDonneeFinanciereHeader()%></idDonneeFinanciereHeader>
		<idHome><%=entity.getIdHome()%></idHome>
		<libelleHome><%=PCTaxeJournaliereHomeHandler.getSimpleLibelleHome(viewBean.getTaxeJournaliereHome().getTypeChambre())%></libelleHome>
		<isParticipationLCA><%=entity.getIsParticipationLCA()%></isParticipationLCA>
		<montantJournalierLCA><%=PCCommonHandler.getCurrencyFormtedDefault(entity.getMontantJournalierLCA())%></montantJournalierLCA>
		<primeAPayer><%= PCCommonHandler.getCurrencyFormtedDefault(entity.getPrimeAPayer())%></primeAPayer> 
		<DR><%=viewBean.getTaxeJournaliereHome().getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu()%></DR>
		<dateEntreeHome><%=viewBean.getTaxeJournaliereHome().getSimpleTaxeJournaliereHome().getDateEntreeHome()%></dateEntreeHome>
		<test><%=viewBean.getAfficheTypeChambre() +" s "+  typeChambreSearch.getSize()%> </test>
		<csDestinationSortie><%=viewBean.getTaxeJournaliereHome().getSimpleTaxeJournaliereHome().getCsDestinationSortie()%></csDestinationSortie>
		<isDeplafonner><%=viewBean.getTaxeJournaliereHome().getSimpleTaxeJournaliereHome().getIsDeplafonner()%></isDeplafonner>
		<isVersementDirect><%=viewBean.getTaxeJournaliereHome().getSimpleTaxeJournaliereHome().getIsVersementDirect()%></isVersementDirect>
		<montantFraisLongueDuree><%=viewBean.getTaxeJournaliereHome().getSimpleTaxeJournaliereHome().getMontantFraisLongueDuree()%></montantFraisLongueDuree>
		
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>
		
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
