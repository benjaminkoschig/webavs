<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
    targetLocation = "location.href";
    globaz.osiris.db.ebill.CAFichierInscriptionEBillListViewBean viewBean = (globaz.osiris.db.ebill.CAFichierInscriptionEBillListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
    size = viewBean.getSize();
    wantPagination=false;
    String directLink = "osiris?userAction=osiris.ebill.inscriptionEBill.chercher&idFichier=";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
<script type="text/javascript">
    notationManager.b_stop = true;
</script>
<TH colspan="2">Text</TH>
<TH align="left">Date lecture</TH>
<TH nowrap align="left">Anzahl der Positionen</TH>
<TH align="left">Statut fichier</TH>
<%	globaz.osiris.db.ebill.CAFichierInscriptionEBill _fichierInscriptionEBill = null ; %>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>
<%_fichierInscriptionEBill = (globaz.osiris.db.ebill.CAFichierInscriptionEBill) viewBean.getEntity(i);
    actionDetail = "parent.location.href='" + directLink + _fichierInscriptionEBill.getIdFichier() + "&nom=" + _fichierInscriptionEBill.getNomFichier() + "&date=" + _fichierInscriptionEBill.getDateLecture() + "&statutFichier=" + String.valueOf(_fichierInscriptionEBill.getStatutFichier().getIndex()) + "'";
%>

<TD class="mtd" width="16" >
    <ct:menuPopup menu="CA-fichier-inscription-eBill" label="<%=optionsPopupLabel%>" target="top.fr_main">
        <ct:menuParam key="idFichier" value="<%=_fichierInscriptionEBill.getId()%>"/>
        <ct:menuParam key="nom" value="<%=_fichierInscriptionEBill.getNomFichier()%>"/>
        <ct:menuParam key="date" value="<%=_fichierInscriptionEBill.getDateLecture()%>"/>
        <ct:menuParam key="statutFichier" value="<%=String.valueOf(_fichierInscriptionEBill.getStatutFichier().getIndex())%>"/>
    </ct:menuPopup>
</TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_fichierInscriptionEBill.getNomFichier()%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_fichierInscriptionEBill.getDateLecture()%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_fichierInscriptionEBill.getNbElements()%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_fichierInscriptionEBill.getLibelleStatutFichier()%></TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>