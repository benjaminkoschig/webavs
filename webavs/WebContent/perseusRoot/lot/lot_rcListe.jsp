<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@page import="ch.globaz.perseus.business.services.PerseusServiceLocator"%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeLot"%>
<%@page import="ch.globaz.perseus.business.constantes.CSEtatLot"%>
<%@page import="ch.globaz.perseus.business.services.PerseusServiceLocator"%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	// Les labels de cette page commence par le préfix "JSP_PF_LOT_L"	

	PFLotListViewBean viewBean = (PFLotListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "perseus?userAction=perseus.lot.lot.afficher&selectedId=";
%>

<style type="text/css">

	.tdItalique {
		font-style:italic;
		opacity : 0.5;
		filter : alpha(opacity=50); 
	}

</style>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>

	<%-- tpl:put name="zoneHeaders" --%>
<%@page import="globaz.perseus.vb.lot.PFLotViewBean"%>
<%@page import="globaz.perseus.vb.lot.PFLotListViewBean"%>
	<TH>&nbsp;</TH>
	<TH><ct:FWLabel key="JSP_PF_LOT_DESCRIPTION"/></TH>
	<TH><ct:FWLabel key="JSP_PF_LOT_DATE_CREATION"/></TH>
	<TH><ct:FWLabel key="JSP_PF_LOT_DATE_COMPTABILISATION"/></TH>
    <TH><ct:FWLabel key="JSP_PF_LOT_ETAT"/></TH>
    <TH><ct:FWLabel key="JSP_PF_LOT_TYPE"/></TH>
    <TH><ct:FWLabel key="JSP_PF_LOT_NO_LOT"/></TH> 
    
    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>

	<%-- tpl:put name="lotList" --%>
    <%
		PFLotViewBean line = (PFLotViewBean) viewBean.getEntity(i);
    	actionDetail = targetLocation  + "='" + detailLink + line.getLot().getSimpleLot().getIdLot()+"'";
    	
    	String cssItal = "";
    	if (CSEtatLot.LOT_VALIDE.getCodeSystem().equals(line.getLot().getSimpleLot().getEtatCs())) {
    		cssItal = "tdItalique";
    	}
	%>
	<TD class="mtd" width="">		
	   	<ct:menuPopup menu="perseus-optionsLot" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getLot().getId()%>">
	   		<ct:menuParam key="selectedId" value="<%=line.getLot().getSimpleLot().getIdLot()%>"/>
			<ct:menuParam key="idLot" value="<%=line.getLot().getId()%>"/>
			<ct:menuParam key="csEtatLot" value="<%=line.getLot().getSimpleLot().getEtatCs()%>"/>
			<ct:menuParam key="csTypeLot" value="<%=line.getLot().getSimpleLot().getTypeLot()%>"/>
			<ct:menuParam key="descriptionLot" value="<%=line.getLot().getSimpleLot().getDescription()%>"/>
			<% if (CSEtatLot.LOT_VALIDE.getCodeSystem().equals(line.getLot().getSimpleLot().getEtatCs())||CSTypeLot.LOT_MENSUEL.getCodeSystem().equals(line.getLot().getSimpleLot().getTypeLot())|| CSTypeLot.LOT_MENSUEL_RP.getCodeSystem().equals(line.getLot().getSimpleLot().getTypeLot())|| PerseusServiceLocator.getPmtMensuelService().isValidationDecisionAuthorise()) { %>
				<ct:menuExcludeNode nodeId="COMPTABILISE"/>
			<% } %>
			
			<!-- Ajout d'une vérification pour afficher dans l'option, le lien permettant d'imprimer les lots de type facture -->
			<%if (!CSTypeLot.LOT_FACTURES.getCodeSystem().equals(line.getLot().getSimpleLot().getTypeLot())) {%>
				<ct:menuExcludeNode nodeId="FACTURE" />
			<%} %>
			
			<!-- Ajout d'une vérification pour afficher dans l'option, le lien permettant d'imprimer les lots de type facture RP -->
			<%if (!CSTypeLot.LOT_FACTURES_RP.getCodeSystem().equals(line.getLot().getSimpleLot().getTypeLot())) {%>
				<ct:menuExcludeNode nodeId="FACTURE_RP" />
			<%} %>
				
		</ct:menuPopup>
    </TD>
	<TD class="mtd <%=cssItal %>" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getLot().getSimpleLot().getDescription()%>&nbsp;</TD>
	<TD class="mtd <%=cssItal %>" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getLot().getSimpleLot().getDateCreation()%>&nbsp;</TD>
	<TD class="mtd <%=cssItal %>" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getLot().getSimpleLot().getDateEnvoi()%>&nbsp;</TD>
	<TD class="mtd <%=cssItal %>" nowrap="nowrap" onClick="<%=actionDetail%>"><%=objSession.getCodeLibelle(line.getLot().getSimpleLot().getEtatCs())%>&nbsp;</TD>
	<TD class="mtd <%=cssItal %>" nowrap="nowrap" onClick="<%=actionDetail%>"><%=objSession.getCodeLibelle(line.getLot().getSimpleLot().getTypeLot())%>&nbsp;</TD>
	<TD class="mtd <%=cssItal %>" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getLot().getSimpleLot().getIdLot()%>&nbsp;</TD>
   
   
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>