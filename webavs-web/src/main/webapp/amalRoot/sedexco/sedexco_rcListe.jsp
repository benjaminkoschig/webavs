<%@page import="ch.globaz.common.domaine.Montant"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme.AMStatutAnnonceSedex"%>
<%@page import="ch.globaz.amal.business.constantes.AMMessagesTypesAnnonceSedexCO"%>
<%@page import="ch.globaz.amal.business.constantes.AMMessagesSubTypesAnnonceSedexCO"%>
<%@page import="globaz.amal.vb.sedexco.AMSedexcoViewBean"%>
<%@page import="globaz.amal.vb.sedexco.AMSedexcoListViewBean"%>
<%@page import="ch.horizon.jaspe.util.JACalendarGregorian"%>
<%@page import="globaz.globall.util.JACalendar"%>
<%@page import="globaz.globall.util.JADate"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme"%>
<%@page import="ch.globaz.amal.business.constantes.IAMActions"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Arrays"%>
<%@page import="globaz.globall.parameters.FWParametersCode"%>
<%@page import="java.util.Iterator"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>
	    <%
	    	AMSedexcoListViewBean viewBean=(AMSedexcoListViewBean)request.getAttribute("viewBean");	    	
   	    	size=viewBean.getSize();
	    	    	    	
   	    	menuName = "amal-menuprincipal";	 
	    %>
<%-- /tpl:insert --%>
<script type="text/javascript">
$(document).ready(function() {
});
</script>
<%@ include file="/theme/list/javascripts.jspf" %>	   	    
	    <%-- tpl:insert attribute="zoneHeaders" --%>
	    
	    <TH>&nbsp;</TH>
		<TH>Date</TH>  
		<TH>Assureur</TH> 		
   		<TH>Message</TH>
   		<TH>Status</TH>
   		<TH>Membre</TH>
   		<TH>Période</TH>
   		<TH>Intérêts</TH>
   		<TH>Frais</TH>
   		<TH>Montant</TH>   		
	    <%-- /tpl:insert --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:insert attribute="zoneCondition" --%>
    <%
    	AMSedexcoViewBean line = (AMSedexcoViewBean)viewBean.getEntity(i);
    %>
    <%-- /tpl:insert --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:insert attribute="zoneList" --%>
			<TD class="mtd" nowrap >
				<%
				String msgType = line.getComplexAnnonceSedexCO().getSimpleAnnonceSedexCO().getMessageType();
				if (AMMessagesTypesAnnonceSedexCO.LISTE_PERSONNES_NE_DEVANT_PAS_ETRE_POURSUIVIES.getValue().equals(msgType)) {
				%>
					<img
					width="22px"
					height="22px"
					src="<%=request.getContextPath()%>/images/amal/sedex_outbox.png" title="Envoi (<%=line.getComplexAnnonceSedexCO().getSimpleAnnonceSedexCO().getId()%>)" border="0">
				<% } else { %>
					<img
					width="22px"
					height="22px"
					src="<%=request.getContextPath()%>/images/amal/sedex_inbox.png" title="Réception (<%=line.getComplexAnnonceSedexCO().getSimpleAnnonceSedexCO().getId()%>)" border="0">				
				<% }%>
				<!-- Icone E/R --> 
			</TD>
			<TD class="mtd" nowrap >
				<%=line.getComplexAnnonceSedexCO().getSimpleAnnonceSedexCO().getDateAnnonce() %>&nbsp;
			</TD>
			<TD class="mtd" nowrap >
				<%=line.getComplexAnnonceSedexCO().getCaisseMaladie().getAdmin().getCodeAdministration() %> - <%=line.getComplexAnnonceSedexCO().getCaisseMaladie().getTiers().getDesignation1() %>&nbsp;
			</TD>	
			<TD class="mtd" nowrap >
				<%=AMMessagesSubTypesAnnonceSedexCO.getSubTypeCSLibelle(line.getComplexAnnonceSedexCO().getSimpleAnnonceSedexCO().getMessageSubType())%>&nbsp;
			</TD>
			<TD class="mtd" nowrap align="center">
			<%
			String imgName = "";
			String libelleImg = "";
			String status = line.getComplexAnnonceSedexCO().getSimpleAnnonceSedexCO().getStatus();
			if (AMStatutAnnonceSedex.CREE.getValue().equals(status)) {
				imgName = "sedex_creation_ok.png";
				libelleImg = "Crée";
			} else if (AMStatutAnnonceSedex.ERROR_CREE.getValue().equals(status)) {
				imgName = "sedex_creation_ko.png";
				libelleImg = "Erreur création";
			} else if (AMStatutAnnonceSedex.ENVOYE.getValue().equals(status)) {
				imgName = "sedex_envoi_ok.png";
				libelleImg = "Envoyé";
			} else if (AMStatutAnnonceSedex.ERROR_ENVOYE.getValue().equals(status)) {
				imgName = "sedex_envoi_ko.png";
				libelleImg = "Erreur envoyé";
			} else if (AMStatutAnnonceSedex.RECU.getValue().equals(status)) {
				imgName = "sedex_recu_ok.png";
				libelleImg = "Reçu";
			} else if (AMStatutAnnonceSedex.ERREUR_RECU.getValue().equals(status)) {
				imgName = "sedex_recu_ko.png";
				libelleImg = "Erreur reçu";
			} else {
				imgName = "";
			}
			%>
			<%
			if (!JadeStringUtil.isEmpty(imgName)) { %>
			<img
					width="20px"
					height="20px"
					src="<%=request.getContextPath()%>/images/amal/<%=imgName%>" title="<%=libelleImg%>" border="0">
			<% } else { %>
				&nbsp;
			<% } %>
			</TD>
			<%
			imgName = "";
			libelleImg = "";
			%>				
			<TD class="mtd" nowrap>
<%-- 				<%=line.getComplexAnnonceSedexCO().getSimpleFamille().getNomPrenom() %>&nbsp; --%>
			</TD>
			<TD class="mtd" nowrap align="center">		
				<%if (!line.getComplexAnnonceSedexCO().getSimpleAnnonceSedexCO().getPeriodeDebut().isEmpty()) { %>	
					<%=line.getComplexAnnonceSedexCO().getSimpleAnnonceSedexCO().getPeriodeDebut()%> - <%=line.getComplexAnnonceSedexCO().getSimpleAnnonceSedexCO().getPeriodeFin() %>
				<%} else { %>
					&nbsp;
				<% } %>
			</TD>
			<TD class="mtd" nowrap align="right">
			<% Montant interet = new Montant(line.getComplexAnnonceSedexCO().getSimpleAnnonceSedexCO().getInterets());
			if (!interet.isZero()) {%>	
				<%=interet.toStringFormat() %>
			<%} else { %>
				&nbsp;
			<%} %>
			</TD>
			<TD class="mtd" nowrap align="right">
			<% Montant frais = new Montant(line.getComplexAnnonceSedexCO().getSimpleAnnonceSedexCO().getFrais());
			if (!frais.isZero()) {%>	
				<%=frais.toStringFormat() %>
			<%} else { %>
				&nbsp;
			<%} %>
			</TD>
			<TD class="mtd" nowrap align="right">
			<% Montant totalCreance = new Montant(line.getComplexAnnonceSedexCO().getSimpleAnnonceSedexCO().getTotalCreance());
			if (!totalCreance.isZero()) {%>	
				<%=totalCreance.toStringFormat() %>
			<%} else { %>
				&nbsp;
			<%} %>
			</TD>
		<%-- /tpl:insert --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:insert attribute="zoneTableFooter" --%>
	<%-- /tpl:insert --%>
<%@ include file="/theme/list/tableEnd.jspf" %>