<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.jade.client.util.JadeNumericUtil"%>
<%@page import="ch.globaz.pegasus.business.models.renteijapi.SimpleIjApg"%>
<%@page import="globaz.pegasus.vb.renteijapi.PCIjApgAjaxViewBean"%>
<%
	PCIjApgAjaxViewBean viewBean = (PCIjApgAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
	SimpleIjApg entity = viewBean.getIjApg().getSimpleIjApg();
%>

<message>
	<contenu>
		<csGenrePrestation><%= entity.getCsGenrePrestation()%></csGenrePrestation>
		<montant><%= (JadeNumericUtil.isEmptyOrZero(entity.getMontant()))?"":entity.getMontant()%></montant>
		<montantBrutAC><%= (JadeNumericUtil.isEmptyOrZero(entity.getMontantBrutAC()))?"":entity.getMontantBrutAC()%></montantBrutAC>
		<nbJours><%= (JadeNumericUtil.isEmptyOrZero(entity.getNbJours()))?"":entity.getNbJours()%></nbJours>
		<tauxAVS><%= (JadeNumericUtil.isEmptyOrZero(entity.getTauxAVS()))?"":entity.getTauxAVS()%></tauxAVS>
		<tauxAA><%= (JadeNumericUtil.isEmptyOrZero(entity.getTauxAA()))?"":entity.getTauxAA()%></tauxAA>
		<cotisationLPPMens><%= (JadeNumericUtil.isEmptyOrZero(entity.getCotisationLPPMens()))?"":entity.getCotisationLPPMens()%></cotisationLPPMens>
		<gainIntAnnuel><%= (JadeNumericUtil.isEmptyOrZero(entity.getGainIntAnnuel()))?"":entity.getGainIntAnnuel()%></gainIntAnnuel>
		<autreGenrePresation><%= entity.getAutreGenrePresation()%></autreGenrePresation>
		<fournisseurPrestation><%= entity.getIdFournisseurPrestation()%></fournisseurPrestation>
		<nomFournisseurPrestation><%= viewBean.getNomFournisseurPrestation()%></nomFournisseurPrestation>
		<DR><%=viewBean.getIjApg().getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu()%></DR>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>

	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
