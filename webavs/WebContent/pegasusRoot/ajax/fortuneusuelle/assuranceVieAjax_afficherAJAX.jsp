<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.fortuneusuelle.PCAssuranceVieAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.SimpleAssuranceVie"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%
PCAssuranceVieAjaxViewBean viewBean = (PCAssuranceVieAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleAssuranceVie entity=viewBean.getAssuranceVie().getSimpleAssuranceVie();

FWCurrency montantMensuel = new FWCurrency(entity.getMontantValeurRachat());
	
%>

<message>
	<contenu>
		<valeurRachat><%=montantMensuel.toStringFormat() %></valeurRachat>
		<numeroPolice><%=entity.getNumeroPolice()%></numeroPolice>	
		<nomCompagnie><%=entity.getNomCompagnie() %></nomCompagnie>		
		<nomNomCompagnie><%=viewBean.getNomCompagnie()%></nomNomCompagnie>	
		<dateEcheance><%=entity.getDateEcheance() %></dateEcheance>
		<DF><%=viewBean.getAssuranceVie().getSimpleDonneeFinanciereHeader().getIsDessaisissementFortune()%></DF>		
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
