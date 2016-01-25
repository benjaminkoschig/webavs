<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.revenusdepenses.PCRevenuActiviteLucrativeDependanteAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.SimpleRevenuActiviteLucrativeDependante"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>

<%
PCRevenuActiviteLucrativeDependanteAjaxViewBean viewBean = (PCRevenuActiviteLucrativeDependanteAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleRevenuActiviteLucrativeDependante entity=viewBean.getRevenuActiviteLucrativeDependante().getSimpleRevenuActiviteLucrativeDependante();

SimpleTypeFraisObtentionRevenu simpleTypeFraisObtentionRevenu = new SimpleTypeFraisObtentionRevenu();

FWCurrency montantActiviteLucrative = new FWCurrency(entity.getMontantActiviteLucrative());
FWCurrency deductionsSociales = new FWCurrency(entity.getDeductionsSociales());
FWCurrency montantFrais = new FWCurrency(entity.getMontantFrais());	
FWCurrency deductionsLpp = new FWCurrency(entity.getDeductionsLpp());	
%>

<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.SimpleTypeFraisObtentionRevenu"%><message>
	<contenu>
		<csGenreRevenu><%=JadeStringUtil.isBlankOrZero(entity.getCsGenreRevenu())?"":entity.getCsGenreRevenu()%></csGenreRevenu>
		<typeRevenuNature><%=entity.getTypeRevenu()%></typeRevenuNature>
		<idEmployeur><%=entity.getIdEmployeur()%></idEmployeur>
		<nomEmployeur><%=viewBean.getNomEmployeur()%></nomEmployeur>
		<noAffilie><%=viewBean.getNoAffilie()%></noAffilie>
		<montant><%=montantActiviteLucrative.toStringFormat() %></montant>
		<deductionSociale><%=deductionsSociales.toStringFormat()%></deductionSociale>
		<deductionsLpp><%=deductionsLpp.toStringFormat()%></deductionsLpp>				
		<autreFraisObtentionRevenu><%=entity.getAutreFraisObtentionRevenu()%></autreFraisObtentionRevenu>
		<!-- les éléments frais obtention revenu -->
		<fraisObtention>
			<% int n = viewBean.getSimpleTypeFraisObtentionRevenuSearch().getSearchResults().length;				
				for(int i = 0; i< n ;i++){
					simpleTypeFraisObtentionRevenu = (SimpleTypeFraisObtentionRevenu)viewBean.getSimpleTypeFraisObtentionRevenuSearch().getSearchResults()[i];
			%>
				<frais><%=simpleTypeFraisObtentionRevenu.getCsFraisObtentionRevenu() %></frais>
			<% } %>
		</fraisObtention>
		
		<montantFrais><%=montantFrais.toStringFormat()%></montantFrais>				
		<DR><%=viewBean.getRevenuActiviteLucrativeDependante().getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu()%></DR>		
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
