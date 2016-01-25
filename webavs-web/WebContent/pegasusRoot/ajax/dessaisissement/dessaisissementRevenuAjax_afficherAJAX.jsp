<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.dessaisissement.PCDessaisissementRevenuAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.dessaisissement.DessaisissementRevenu"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="ch.globaz.pegasus.business.models.dessaisissement.SimpleDessaisissementRevenu"%>
<%
PCDessaisissementRevenuAjaxViewBean viewBean = (PCDessaisissementRevenuAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleDessaisissementRevenu entity=viewBean.getDessaisissementRevenu().getSimpleDessaisissementRevenu();
%>


<message>
	<contenu>
		<libelle><%=entity.getLibelleDessaisissement()%></libelle>
		<montantBrutDessaisi><%=new FWCurrency(entity.getMontantBrut()).toStringFormat()%></montantBrutDessaisi>
		<montantDeductions><%=new FWCurrency(entity.getDeductionMontantDessaisi()).toStringFormat()%></montantDeductions>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
