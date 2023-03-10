<%@ page import="globaz.osiris.db.ebill.enums.CAStatutEBillEnum" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
    targetLocation = "location.href";
    globaz.osiris.db.ebill.CATraitementEBillListViewBean  viewBean = (globaz.osiris.db.ebill.CATraitementEBillListViewBean )session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
    size = viewBean.getSize();
    wantPagination=false;

    String nom = request.getParameter("nom");
    String date = request.getParameter("dateLecture");
    String statutFichier = request.getParameter("statutFichier");

    if (globaz.jade.client.util.JadeStringUtil.isNull(nom)) {
        nom = "";
    }

    if (globaz.jade.client.util.JadeStringUtil.isNull(date)) {
        date = "";
    }

    if (globaz.jade.client.util.JadeStringUtil.isNull(statutFichier)) {
        statutFichier = "";
    }

    String directLink = "osiris?userAction=osiris.ebill.traitementEBill.afficher&selectedId=";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
<script type="text/javascript">
    notationManager.b_stop = true;
</script>
<TH colspan="2">eBillAccountID</TH>
<TH align="left">TransactionID</TH>
<TH align="left">Name/Vorname oder Unternehmen</TH>
<TH nowrap align="left">Teilnehmernummer</TH>
<TH align="left">Interner Status</TH>
<TH align="left">Fehler Code</TH>
<TH align="left">eBill Stand</TH>
<%	globaz.osiris.db.ebill.CATraitementEBill _traitementEBill = null ; %>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>
<%_traitementEBill = (globaz.osiris.db.ebill.CATraitementEBill) viewBean.getEntity(i);
    actionDetail = "parent.location.href='" + directLink + _traitementEBill.getIdTraitement() + "'";

    actionDetail = "parent.location.href='" + directLink + _traitementEBill.getIdTraitement() + "&nom=" + nom + "&date=" + date + "&statutFichier=" + statutFichier + "'";
%>

<TD class="mtd" width="16" >
    <ct:menuPopup menu="CA-traitement-eBill" label="<%=optionsPopupLabel%>" target="top.fr_main">
        <ct:menuParam key="selectedId" value="<%=_traitementEBill.getId()%>"/>
        <ct:menuParam key="idFichier" value="<%=viewBean.getForIdFichier()%>"/>
        <ct:menuParam key="nom" value="<%=nom%>"/>
        <ct:menuParam key="date" value="<%=date%>"/>
        <ct:menuParam key="statutFichier" value="<%=statutFichier%>"/>
        <% if (!CAStatutEBillEnum.A_TRAITER.equals(_traitementEBill.getStatut())) { %>
        <ct:menuExcludeNode nodeId="traitementEBill.optionAValider"/>
        <% }
        if (!CAStatutEBillEnum.TRAITE_MANUELLEMENT.equals(_traitementEBill.getStatut())) { %>
        <ct:menuExcludeNode nodeId="traitementEBill.optionATraiter"/>
        <% } %>
    </ct:menuPopup>
</TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_traitementEBill.getEBillAccountID()%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_traitementEBill.getTransactionID()%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_traitementEBill.getNomPrenomOuEntreprise()%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_traitementEBill.getNumeroAffilie()%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_traitementEBill.getLibelleStatut()%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_traitementEBill.getCodeErreurDescription()%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_traitementEBill.getLibelleEtat()%></TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>