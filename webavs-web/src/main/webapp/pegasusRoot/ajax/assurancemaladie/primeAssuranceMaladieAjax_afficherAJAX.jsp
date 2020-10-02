<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@ page import="globaz.pegasus.vb.assurancemaladie.PCPrimeAssuranceMaladieAjaxViewBean" %>
<%@ page import="ch.globaz.pegasus.business.models.assurancemaladie.SimplePrimeAssuranceMaladie" %>
<%@ page import="globaz.framework.util.FWCurrency" %>
<%
	PCPrimeAssuranceMaladieAjaxViewBean viewBean = (PCPrimeAssuranceMaladieAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
	SimplePrimeAssuranceMaladie entity=viewBean.getPrimeAssuranceMaladie().getSimplePrimeAssuranceMaladie();

	FWCurrency montant = new FWCurrency(entity.getMontant());
%>

<message>
	<contenu>
		<montant><%=montant.toStringFormat()%></montant>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
			<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>
</message>
