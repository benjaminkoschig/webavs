<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%@page import="globaz.cygnus.vb.paiement.RFPrestationListViewBean"%>
<%@page import="globaz.cygnus.vb.paiement.RFPrestationViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.corvus.api.decisions.IREDecision"%>
<%@page import="globaz.corvus.utils.REPmtMensuel"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%-- tpl:put name="zoneScripts" --%>
<%
	// Les labels de cette page commence par le préfix "JSP_RF_PRE_L"
	RFPrestationListViewBean viewBean = (RFPrestationListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	
	detailLink = "cygnus?userAction=cygnus.paiement.prestation.afficher&selectedId=";
	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>


	<TH colspan="2"><ct:FWLabel key="JSP_RF_PRE_L_PRESTATAIRE"/></TH>
	<TH><ct:FWLabel key="JSP_RF_PRE_L_PAIEMENT"/></TH>	
	<TH><ct:FWLabel key="JSP_RF_PRE_L_PERIODE"/></TH>
	<TH><ct:FWLabel key="JSP_RF_PRE_L_MONTANT"/></TH>
    <TH><ct:FWLabel key="JSP_RF_PRE_L_ETAT"/></TH>
    <TH><ct:FWLabel key="JSP_RF_PRE_L_NO_LOT"/></TH>
    <TH><ct:FWLabel key="JSP_RF_PRE_L_NO"/></TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
		RFPrestationViewBean courant = (RFPrestationViewBean) viewBean.get(i);
	
		String urlForMenuPopUp = detailLink + courant.getId()
		+ "&nss=" + courant.getNss() 
		+ "&nom=" + courant.getNom()
		+ "&prenom=" + courant.getPrenom() 
		+ "&dateNaissance="+courant.getDateNaissance()
		+ "&dateDeces="+courant.getDateDeces()
		+ "&csSexe="+courant.getCsSexe()
		+ "&csNationalite="+courant.getCsNationalite()
		+ "&idPrestation="+courant.getIdPrestation()
		+ "&idTiers="+ courant.getIdTiers()
		+ "&idLot="+courant.getIdLot();
	
		String detailUrl = "parent.location.href='" + urlForMenuPopUp + "'";	
	%>    
    <TD class="mtd" nowrap="nowrap" >
	   	<ct:menuPopup menu="cygnus-optionsprestation" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=urlForMenuPopUp%>">
			<ct:menuParam key="selectedId" value="<%=courant.getIdPrestation()%>"/>							
			<ct:menuParam key="idPrestation" value="<%=courant.getIdPrestation()%>"/>
			<ct:menuParam key="idTierRequerant" value="<%=courant.getIdTiers()%>"/>
			<ct:menuParam key="montantPrestation" value="<%=courant.getMontantTotal()%>"/>
			<ct:menuParam key="idDecision" value="<%=courant.getIdDecision()%>"/>
		</ct:menuPopup>
    </TD>
    <TD class="mtd" nowrap="nowrap" onClick="<%=detailUrl%>"><%=courant.getDetailAssure() %></TD>
    <TD class="mtd" nowrap="nowrap" onClick="<%=detailUrl%>"><%=courant.getDetailPaiement() %></TD>    
	<TD class="mtd" nowrap="nowrap" onClick="<%=detailUrl%>"><%=courant.getDateMoisAnnee()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=detailUrl%>" align="right"><%=JadeStringUtil.isBlankOrZero(courant.getMontantTotal())?"0.00":new FWCurrency(courant.getMontantTotal()).toStringFormat()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=detailUrl%>"><%=objSession.getCodeLibelle(courant.getCsEtatPrestation())%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=detailUrl%>"><%=courant.getIdLot()%>&nbsp;</TD>
	<TD class="mtd" nowrap="nowrap" onClick="<%=detailUrl%>"><%=courant.getIdPrestation()%>&nbsp;</TD>
	
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>