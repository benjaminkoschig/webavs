<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.fortuneparticuliere.PCAssuranceRenteViagereAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneparticuliere.SimpleAssuranceRenteViagere"%>
<%
PCAssuranceRenteViagereAjaxViewBean viewBean = (PCAssuranceRenteViagereAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleAssuranceRenteViagere entity=viewBean.getAssuranceRenteViagere().getSimpleAssuranceRenteViagere();
%>

<message>
	<contenu>
		<montantRachat><%=entity.getMontantValeurRachat()%></montantRachat>
		<noPolice><%=entity.getNumeroPolice()%></noPolice>
		<idCompagnie><%=entity.getIdCompagnie()%></idCompagnie>
		<nomCompagnie><%=viewBean.getNomCompagnie()%></nomCompagnie>
		<renteMontant><%=entity.getMontantRenteViagere()%></renteMontant>
		<renteExcedent><%=entity.getExcedentRenteViagere()%></renteExcedent>
		<DF><%=viewBean.getAssuranceRenteViagere().getSimpleDonneeFinanciereHeader().getIsDessaisissementFortune()%></DF>
		<DR><%=viewBean.getAssuranceRenteViagere().getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu()%></DR>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>

	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
