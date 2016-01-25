<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
LXPaiementListViewBean viewBean = (LXPaiementListViewBean) session.getAttribute("listViewBean");
size = viewBean.size();

LXPaiementViewBean paiement = null;

detailLink ="lynx?userAction=lynx.paiement.paiement.afficher&idOperation=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
	
<%@page import="globaz.lynx.db.paiement.LXPaiementViewBean"%>
<%@page import="globaz.lynx.db.paiement.LXPaiementListViewBean"%>
<%@page import="globaz.lynx.db.operation.LXOperation"%>
	
	<TH width="30">&nbsp;</TH>
	<TH width="100">Datum</TH>
	<TH width="400">Bezeichnung</TH>
	<TH width="200">Lieferant</TH>
	<TH width="200">Interne-Nr.</TH>
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
    paiement = (LXPaiementViewBean) viewBean.getEntity(i);
    actionDetail = "parent.location.href='" + detailLink+paiement.getIdOperation() + 
    		"&" + ch.globaz.utils.VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + paiement.getIdTiersFournisseur() + "'";
    
    String style = "";
    if(LXOperation.CS_ETAT_OUVERT.equals(paiement.getCsEtat())) {
    	style = "color:blue";
    } else if(LXOperation.CS_ETAT_ANNULE.equals(paiement.getCsEtat())) {
    	style = "color:gray";
    }
    
    String bloque = "";
    if(paiement.getEstBloque().booleanValue()) {
    	bloque = "<img title=\"Bloqué\" src=\"" + request.getContextPath()+"/images/cadenas.gif\"";
    }
    %>

    <td class="mtd" width="16">
    	<ct:menuPopup menu="LX-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=detailLink + paiement.getIdOperation() + 
    		\"&\" + ch.globaz.utils.VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + \"=\" + paiement.getIdTiersFournisseur()%>"/>
    </td>
    <td class="mtd" onClick="<%=actionDetail%>" align="center" style="<%=style%>"><%=paiement.getDateFacture()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" style="<%=style%>"><%=paiement.getLibelle()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" style="<%=style%>"><%=paiement.getIdExterneFournisseur() + " - " + paiement.getNomFournisseur()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" style="<%=style%>"><%=paiement.getIdExterne()%>&nbsp;</td>
	<td class="mtd" onClick="<%=actionDetail%>" style="<%=style%>"><%=paiement.getReferenceExterne()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" align="right" style="<%=style%>"><%=paiement.getMontantFormattedPositif()%>&nbsp;</td>
	<td class="mtd" onClick="<%=actionDetail%>" align="center" style="<%=style%>"><%=bloque%>&nbsp;</td>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>