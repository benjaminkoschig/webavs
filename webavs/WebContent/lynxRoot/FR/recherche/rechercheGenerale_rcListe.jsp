<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
LXRechercheGeneraleListViewBean viewBean = (LXRechercheGeneraleListViewBean) session.getAttribute("listViewBean");
size = viewBean.size();

LXRechercheGeneraleViewBean factures = null;
detailLink ="lynx?userAction=lynx.recherche.rechercheDetail.chercherDetail&forEtat="+ viewBean.getForEtat() +"&forIdSection=";

%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>

<%@page import="globaz.lynx.db.recherche.LXRechercheGeneraleListViewBean"%>
<%@page import="globaz.lynx.db.recherche.LXRechercheGeneraleViewBean"%>
<%@page import="globaz.lynx.db.section.LXSection"%>

	<TH width="30">&nbsp;</TH>
	<TH width="100">Date</TH>
	<TH width="400">Soci&eacute;té</TH>
	<TH width="400">Fournisseur</TH>
	<TH width="150">No. Interne</TH>
	<TH width="150">No. Fact. Four.</TH>
	<TH width="150">Base</TH>
	<TH width="150">Mouvements</TH>
	<TH width="150">Solde</TH>
	<TH>Type</TH>

<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>

    <%
    factures = (LXRechercheGeneraleViewBean) viewBean.getEntity(i);
    detailLink ="lynx?userAction=lynx.recherche.rechercheDetail.chercherDetail&" + ch.globaz.utils.VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + factures.getIdTiersFournisseur() + "&forEtat="+ viewBean.getForEtat() +"&forIdSection=";
    actionDetail = "parent.location.href='" + detailLink + factures.getIdSectionBase() + "'";
    %>

    <td class="mtd" width="16">
    	<ct:menuPopup menu="LX-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=detailLink + factures.getIdSection()%>"/>
    </td>
    <td class="mtd" onClick="<%=actionDetail%>" align="center"><%=factures.getDateSection()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=factures.getNomSociete()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=factures.getNomCompletFournisseur()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" align="center"><%=factures.getIdExterne()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>" align="center"><%=factures.getReferenceExterne()%>&nbsp;</td>
	<td class="mtd" onClick="<%=actionDetail%>" align="right"><%=factures.getBaseFormatted()%>&nbsp;</td>
	<td class="mtd" onClick="<%=actionDetail%>" align="right"><%=factures.getMouvementFormatted()%>&nbsp;</td>
	<td class="mtd" onClick="<%=actionDetail%>" align="right"><%=factures.getSoldeFormatted()%>&nbsp;</td>
	<%
		String image = "";
		if (LXSection.CS_TYPE_FACTURE.equals(factures.getCsTypeSection())) {
			image = "<img width=\"20\" title=\"Facture\" src=\"" + request.getContextPath()+"/images/lynx/facture.png\"\\>";

			if(factures.getEstBloque() || factures.getEstBloqueoperation()) {
				image += "<img src=\"" + request.getContextPath()+"/images/cadenas.gif\" alt='Facture bloquée'\\>";
			} else {
				image += "<img src=\"" + request.getContextPath()+"/images/blankCadenas.gif\" alt=''\\>";
			}
		} else {
			image = "<img width=\"20\" title=\"Node de crédit\" src=\"" + request.getContextPath()+"/images/lynx/ndc.png\"\\>";
			image += "<img src=\"" + request.getContextPath()+"/images/blankCadenas.gif\" alt=''\\>";
		}
	%>
	<td class="mtd" onClick="<%=actionDetail%>" align="center"><%=image%>&nbsp;</td>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>