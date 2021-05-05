<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
    targetLocation = "location.href";
    globaz.osiris.db.ebill.CAInscriptionEBillListViewBean  viewBean = (globaz.osiris.db.ebill.CAInscriptionEBillListViewBean )session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
    size = viewBean.getSize();
    wantPagination=false;

    String nom = request.getParameter("nom");
    String date = request.getParameter("date");
    String etat = request.getParameter("etat");

    if (globaz.jade.client.util.JadeStringUtil.isNull(nom)) {
        nom = "";
    }

    if (globaz.jade.client.util.JadeStringUtil.isNull(date)) {
        date = "";
    }

    if (globaz.jade.client.util.JadeStringUtil.isNull(etat)) {
        etat = "";
    }

    String directLink = "osiris?userAction=osiris.ebill.inscriptionEBill.afficher&selectedId=";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
<script type="text/javascript">
    notationManager.b_stop = true;
</script>
<TH colspan="2">eBillAccountID</TH>
<TH align="left">Name</TH>
<TH nowrap align="left">Teilnehmernummer</TH>
<TH align="left">Typ</TH>
<TH align="left">Internen Status</TH>
<%	globaz.osiris.db.ebill.CAInscriptionEBill _inscriptionEBill = null ; %>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>
<%_inscriptionEBill = (globaz.osiris.db.ebill.CAInscriptionEBill) viewBean.getEntity(i);
    actionDetail = "parent.location.href='" + directLink + _inscriptionEBill.getIdInscription() + "'";

    actionDetail = "parent.location.href='" + directLink + _inscriptionEBill.getIdInscription() + "&nom=" + nom + "&date=" + date + "&etat=" + etat + "'";
%>

<TD class="mtd" width="16" >
    <ct:menuPopup menu="CA-inscription-eBill" label="<%=optionsPopupLabel%>" target="top.fr_main">
        <ct:menuParam key="selectedId" value="<%=_inscriptionEBill.getId()%>"/>
    </ct:menuPopup>
</TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_inscriptionEBill.geteBillAccountID()%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_inscriptionEBill.getNomPrenomOuEntreprise()%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_inscriptionEBill.getChampNumeroAffilie()%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_inscriptionEBill.getLibelleType()%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_inscriptionEBill.getLibelleStatut()%></TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>