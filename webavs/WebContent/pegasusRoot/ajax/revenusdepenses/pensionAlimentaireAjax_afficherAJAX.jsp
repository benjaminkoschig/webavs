<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.revenusdepenses.PCPensionAlimentaireAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.revenusdepenses.SimplePensionAlimentaire"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%
PCPensionAlimentaireAjaxViewBean viewBean = (PCPensionAlimentaireAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimplePensionAlimentaire entity=viewBean.getPensionAlimentaire().getSimplePensionAlimentaire();

FWCurrency montantPension = new FWCurrency(entity.getMontantPensionAlimentaire());
	
%>

<message>
	<contenu>
		<pension><%=JadeStringUtil.isBlankOrZero(entity.getCsTypePension())?"":entity.getCsTypePension()%></pension>			
		<montantPension><%=montantPension.toStringFormat()%></montantPension>
		<motif><%=JadeStringUtil.isBlankOrZero(entity.getCsMotif())?"":entity.getCsMotif()%></motif>
		<idTiers><%=entity.getIdTiers()%></idTiers>
		<nomTiers><%=viewBean.getNomTiers() %></nomTiers>		
		<lien><%=JadeStringUtil.isBlankOrZero(entity.getCsLienAvecRequerantPC())?"":entity.getCsLienAvecRequerantPC()%></lien>
		<autres><%=entity.getAutreLienAvecRequerantPC()%></autres>
		<DRE><%=entity.getIsDeductionRenteEnfant()%></DRE>
		<dateEcheance><%=entity.getDateEcheance()%></dateEcheance>		
		<DR><%=viewBean.getPensionAlimentaire().getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu()%></DR>		
		<montantRenteEnfant><%= viewBean.getPensionAlimentaire().getSimplePensionAlimentaire().getMontantRenteEnfant()%></montantRenteEnfant>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
