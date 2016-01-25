<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@page import="globaz.perseus.vb.lot.PFAbstractPrestationViewBean"%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeLot"%>
<%@page import="ch.globaz.perseus.business.services.PerseusServiceLocator"%>
<%@page import="globaz.jade.client.util.JadeNumericUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.perseus.vb.lot.PFPrestationViewBean"%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@page import="java.util.List"%>
<%@page import="globaz.perseus.vb.lot.PFPrestationListViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="ch.globaz.perseus.business.models.lot.Prestation"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>

<%-- tpl:put name="zoneScripts" --%>
<%
	// Les labels de cette page commence par le préfix "JSP_PF_PRE_L"

	PFPrestationListViewBean viewBean = (PFPrestationListViewBean) request.getAttribute("viewBean");
	size=viewBean.getSize();

	detailLink = "perseus?userAction=perseus.lot.prestation.afficher&selectedId=";

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>

			<TH>&nbsp;</TH>
			<TH><ct:FWLabel key="JSP_PF_PRE_L_PRESTATAIRE"/></TH>
			<TH><ct:FWLabel key="JSP_PF_PRE_L_PERIODE"/></TH>
			<TH><ct:FWLabel key="JSP_PF_PRE_L_MONTANT"/></TH>
		    <TH><ct:FWLabel key="JSP_PF_PRE_L_ETAT"/></TH>
		    <TH><ct:FWLabel key="JSP_PF_PRE_L_NO"/></TH>
	    
  	    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>

		<%-- tpl:insert attribute="zoneList" --%>
		<%
		
			PFAbstractPrestationViewBean line = (PFAbstractPrestationViewBean) viewBean.getEntity(i);
			String detailUrl = "parent.location.href='" + detailLink + line.getId()+"'";
		%>
		
    <TD class="mtd" width="">
	   	<ct:menuPopup menu="perseus-optionsprestation" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getId()%>">
			<ct:menuParam key="selectedId" value="<%=line.getId()%>"/>
			<ct:menuParam key="idPrestation" value="<%=line.getId()%>"/>
			<ct:menuParam key="montantPrestation" value="<%=line.getMontant()%>"/>
			<ct:menuParam key="idTierRequerant" value="<%=line.getIdTiersRequerant()%>"/>
			<ct:menuParam key="idFacture" value="<%=line.getIdFacture()%>"/>
			<ct:menuParam key="idDecision" value="<%=line.getIdDecision()%>"/>
			
			<!-- Ajout d'une vérification pour afficher dans l'option, le lien permettant de voir le detail d'une facture -->
			<%if (!CSTypeLot.LOT_DECISION.getCodeSystem().equals(line.getTypeLot())) {%>
					<ct:menuExcludeNode nodeId="DECISION" />
			<%} %>
			<%if (!CSTypeLot.LOT_FACTURES.getCodeSystem().equals(line.getTypeLot())) {%>
					<ct:menuExcludeNode nodeId="FACTURE" />
			<%} %>
		
		</ct:menuPopup>
    </TD>
    <TD class="mtd" nowrap="nowrap" onClick="<%=detailUrl%>"><%=line.getDetailAssure()%>&nbsp;</TD>
    <TD class="mtd" nowrap="nowrap" onClick="<%=detailUrl%>"><%=line.getPeriode()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=detailUrl%>" align="right"><%=new FWCurrency(line.getMontant()).toStringFormat()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=detailUrl%>"><%=line.getEtat()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=detailUrl%>"><%=line.getId()%>&nbsp;</TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>