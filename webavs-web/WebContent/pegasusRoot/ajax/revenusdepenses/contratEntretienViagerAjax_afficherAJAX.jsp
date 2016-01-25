<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.revenusdepenses.PCContratEntretienViagerAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.SimpleContratEntretienViager"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>

<%
PCContratEntretienViagerAjaxViewBean viewBean = (PCContratEntretienViagerAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleContratEntretienViager entity=viewBean.getContratEntretienViager().getSimpleContratEntretienViager();

SimpleLibelleContratEntretienViager simpleLibelleContratEntretienViager = new SimpleLibelleContratEntretienViager();

FWCurrency montantContrat = new FWCurrency(entity.getMontantContrat());
%>

<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.SimpleLibelleContratEntretienViager"%>
<message>
	<contenu>
		<montantContrat><%=montantContrat.toStringFormat()%></montantContrat>
		<!-- les éléments du libelle -->				
		<csLibelle>
			<% int n = viewBean.getSimpleLibelleContratEntretienViagerSearch().getSearchResults().length;				
				for(int i = 0; i< n ;i++){
					simpleLibelleContratEntretienViager = (SimpleLibelleContratEntretienViager)viewBean.getSimpleLibelleContratEntretienViagerSearch().getSearchResults()[i];
			%>
				<libelle><%=simpleLibelleContratEntretienViager.getCsLibelleContratEntretienViager() %></libelle>
			<% } %>
		</csLibelle>
		<DR><%=viewBean.getContratEntretienViager().getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu()%></DR>		
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
