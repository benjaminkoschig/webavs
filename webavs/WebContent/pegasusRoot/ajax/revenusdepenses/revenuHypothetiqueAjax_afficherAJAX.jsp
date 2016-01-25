<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.revenusdepenses.PCRevenuHypothetiqueAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuHypothetique"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%
PCRevenuHypothetiqueAjaxViewBean viewBean = (PCRevenuHypothetiqueAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleRevenuHypothetique entity=viewBean.getRevenuHypothetique().getSimpleRevenuHypothetique();

FWCurrency revenuNet = new FWCurrency(entity.getMontantRevenuHypothetiqueNet());
FWCurrency revenuBrut = new FWCurrency(entity.getMontantRevenuHypothetiqueBrut());
FWCurrency deductionsSociales = new FWCurrency(entity.getDeductionsSociales());
FWCurrency deductionLPP = new FWCurrency(entity.getDeductionLPP());
FWCurrency fraisDeGarde = new FWCurrency(entity.getFraisDeGarde());


%>
<message>
	<contenu>
		<revenuNet><%=revenuNet.toStringFormat()%></revenuNet>
		<revenuBrut><%=revenuBrut.toStringFormat() %></revenuBrut>
		<deductionSociales><%=deductionsSociales.toStringFormat()%></deductionSociales>
		<deductionLPP><%=deductionLPP.toStringFormat()%></deductionLPP>
		<fraisDeGarde><%=fraisDeGarde.toStringFormat()%></fraisDeGarde>
		<csMotif><%=JadeStringUtil.isBlankOrZero(entity.getCsMotif())?"":entity.getCsMotif()%></csMotif>
		<autres><%=entity.getAutreMotif()%></autres>
		<autreBrutOuNet><%=viewBean.getAutreBrutOuNet()%></autreBrutOuNet>				
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
