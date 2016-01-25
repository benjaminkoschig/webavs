<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.revenusdepenses.PCAllocationsFamilialesAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.SimpleAllocationsFamiliales"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%
PCAllocationsFamilialesAjaxViewBean viewBean = (PCAllocationsFamilialesAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleAllocationsFamiliales entity=viewBean.getAllocationsFamiliales().getSimpleAllocationsFamiliales();

FWCurrency montantMensuel = new FWCurrency(entity.getMontantMensuel());
	
%>

<message>
	<contenu>
		<montantMensuel><%=montantMensuel.toStringFormat() %></montantMensuel>	
		<idCaisseAF><%=entity.getIdCaisseAf() %></idCaisseAF>
		<nomCaisse><%=viewBean.getNomCaisse()%></nomCaisse>	
		<dateEcheance><%=entity.getDateEcheance() %></dateEcheance>
		<DR><%=viewBean.getAllocationsFamiliales().getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu()%></DR>		
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>

	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
