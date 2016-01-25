<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.revenusdepenses.PCRevenuActiviteLucrativeIndependanteAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuActiviteLucrativeIndependante"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%
PCRevenuActiviteLucrativeIndependanteAjaxViewBean viewBean = (PCRevenuActiviteLucrativeIndependanteAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleRevenuActiviteLucrativeIndependante entity=viewBean.getRevenuActiviteLucrativeIndependante().getSimpleRevenuActiviteLucrativeIndependante();

FWCurrency montantRevenu = new FWCurrency(entity.getMontantRevenu());
	
%>

<message>
	<contenu>
		<csDeterminationRevenu><%=JadeStringUtil.isBlankOrZero(entity.getCsDeterminationRevenu())?"":entity.getCsDeterminationRevenu()%></csDeterminationRevenu>	
		<csGenreRevenu><%=JadeStringUtil.isBlankOrZero(entity.getCsGenreRevenu())?"":entity.getCsGenreRevenu()%></csGenreRevenu>
		<montant><%=montantRevenu.toStringFormat()%></montant>
		<idTiersAffilie><%=entity.getIdTiersAffilie()%></idTiersAffilie>
		<nomAffilie><%=viewBean.getNomAffilie() %></nomAffilie>		
		<noAffilie><%=viewBean.getNoAffilie()%></noAffilie>
		<idCaisseCompensation><%=entity.getIdCaisseCompensation()%></idCaisseCompensation>
		<nomCaisse><%=viewBean.getNomCaisse()%></nomCaisse>	
		<DR><%=viewBean.getRevenuActiviteLucrativeIndependante().getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu()%></DR>		
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
