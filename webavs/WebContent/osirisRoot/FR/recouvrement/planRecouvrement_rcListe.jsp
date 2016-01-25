<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.osiris.db.recouvrement.CAPlanRecouvrementListViewBean viewBean = (globaz.osiris.db.recouvrement.CAPlanRecouvrementListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink ="osiris?userAction=osiris.recouvrement.planRecouvrement.afficher&selectedId=";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    <TH>&nbsp;</TH>
    <TH>Compte annexe</TH>    
    <TH>N°</TH>
    <TH>Libellé</TH>
    <TH>Date</TH>
    <TH>Mode</TH>
    <TH>Collaborateur</TH>
    <TH>Etat</TH>            
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
		globaz.osiris.db.recouvrement.CAPlanRecouvrementViewBean line = (globaz.osiris.db.recouvrement.CAPlanRecouvrementViewBean) viewBean.getEntity(i);
    
    	detailLink ="osiris?userAction=osiris.recouvrement.planRecouvrement.afficher&" + ch.globaz.utils.VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + line.getCompteAnnexe().getIdTiers() + "&selectedId=";
		actionDetail = targetLocation  + "='" + detailLink + line.getIdPlanRecouvrement()+"'";
	%>
    <TD class="mtd">
    <ct:menuPopup menu="CA-PlanRecouvrement" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink + line.getIdPlanRecouvrement())%>">
		<ct:menuParam key="id" value="<%=line.getIdPlanRecouvrement()%>"/>
		<ct:menuParam key="selectedId" value="<%=line.getIdPlanRecouvrement()%>"/> 
		<ct:menuParam key="idTiersVueGlobale" value="<%=line.getCompteAnnexe().getIdTiers()%>"/> 
		<% if (!globaz.osiris.db.access.recouvrement.CAPlanRecouvrement.CS_ACTIF.equals(line.getIdEtat())) { %>
		<ct:menuExcludeNode nodeId="SUSPENDRE_PLAN"/>
		<% } %>
	</ct:menuPopup>
	</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getCompteAnnexe().getIdExterneRole()+" - "+ line.getCompteAnnexe().getDescription() %>&nbsp;</TD>	
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getIdPlanRecouvrement()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getLibelle()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getDate()%>&nbsp;</TD>	
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getSession().getCodeLibelle(line.getIdModeRecouvrement())%>&nbsp;</TD>		
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getCollaborateur()%>&nbsp;</TD>		
	<TD class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%= line.getSession().getCodeLibelle(line.getIdEtat())%>&nbsp;</TD>		
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>