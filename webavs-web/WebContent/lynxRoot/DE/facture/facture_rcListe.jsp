<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
LXFactureListViewBean viewBean = (globaz.lynx.db.facture.LXFactureListViewBean) session.getAttribute("listViewBean");
size = viewBean.size();

LXFactureViewBean facture = null;

detailLink ="lynx?userAction=lynx.facture.facture.afficher&idOperation=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    
<%@page import="globaz.lynx.db.facture.LXFactureViewBean"%>
<%@page import="globaz.lynx.db.facture.LXFactureListViewBean"%>
<%@page import="globaz.globall.util.JANumberFormatter"%>
<%@page import="globaz.lynx.db.operation.LXOperation"%>
	
	<TH width="30">&nbsp;</TH>
	<TH width="100">Datum</TH>
	<TH width="100">Fälligkeit</TH>
	<TH width="400">Bezeichnung</TH>
	<TH width="200">Lieferant</TH>
	<TH width="200">Interne Rechnung-Nr.</TH>
	<TH width="200">Lieferant Rechnung-Nr.</TH>
    <TH width="200">Betrag</TH>
	<TH width="15">Gesperrt</TH>
    
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
    facture = (LXFactureViewBean) viewBean.getEntity(i);
    actionDetail = "parent.location.href='" + detailLink + facture.getIdOperation() + 
    		"&" + ch.globaz.utils.VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + facture.getIdTiersFournisseur() + "'";
    
    String style = "";
    if(LXOperation.CS_ETAT_OUVERT.equals(facture.getCsEtat())) {
    	style = "color:blue";
    } else if(LXOperation.CS_ETAT_ANNULE.equals(facture.getCsEtat())) {
    	style = "color:gray";
    }
    
    String bloque = "";
    if(facture.getEstBloque().booleanValue()) {
    	bloque = "<img title=\"Bloqué\" src=\"" + request.getContextPath()+"/images/cadenas.gif\"";
    }
    %>

    <td class="mtd" width="16">
		<ct:menuPopup menu="LX-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=detailLink + facture.getIdOperation() + 
    		\"&\" + ch.globaz.utils.VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + \"=\" + facture.getIdTiersFournisseur()%>"/>
	</td>
    <td class="mtd" onClick="<%=actionDetail%>" align="center" style="<%=style%>"><%=facture.getDateFacture()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" align="center" style="<%=style%>"><%=facture.getDateEcheance()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" style="<%=style%>"><%=facture.getLibelle()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" style="<%=style%>"><%=facture.getIdExterneFournisseur() + " - " + facture.getNomFournisseur()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" style="<%=style%>"><%=facture.getIdExterne()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" style="<%=style%>"><%=facture.getReferenceExterne()%>&nbsp;</td>
	<td class="mtd" onClick="<%=actionDetail%>" align="right" style="<%=style%>"><%=JANumberFormatter.formatNoRound(facture.getMontant())%>&nbsp;</td>
	<td class="mtd" onClick="<%=actionDetail%>" align="center" style="<%=style%>"><%=bloque%>&nbsp;</td>
    
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>