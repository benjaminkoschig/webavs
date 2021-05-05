<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
    targetLocation = "location.href";
    globaz.osiris.db.ebill.CAFichierTraitementEBillListViewBean viewBean = (globaz.osiris.db.ebill.CAFichierTraitementEBillListViewBean)session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VBL_ELEMENT);
    size = viewBean.getSize();
    wantPagination=false;
    String directLink = "osiris?userAction=osiris.ebill.traitementEBill.chercher&idFichier=";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
<script type="text/javascript">
    notationManager.b_stop = true;
</script>
<TH colspan="2">Text</TH>
<TH align="left">Gelesenes Datum</TH>
<TH nowrap align="left">Anzahl der Positionen</TH>
<TH nowrap align="left">Anzahl Verarbeitungen</TH>
<TH nowrap align="left">Anzahl Verarbeitungen</TH>
<TH nowrap align="left">Anzahl abgelehnt</TH>
<TH align="left">Datei-Status</TH>
<%	globaz.osiris.db.ebill.CAFichierTraitementEBill _fichierTraitementEBill = null ; %>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>
<%_fichierTraitementEBill = (globaz.osiris.db.ebill.CAFichierTraitementEBill) viewBean.getEntity(i);
    actionDetail = "parent.location.href='" + directLink + _fichierTraitementEBill.getIdFichier() + "&nom=" + _fichierTraitementEBill.getNomFichier() + "&date=" + _fichierTraitementEBill.getDateLecture() + "&statutFichier=" + String.valueOf(_fichierTraitementEBill.getStatutFichier().getIndex()) + "'";
%>

<TD class="mtd" width="16" >
    <ct:menuPopup menu="CA-fichier-traitement-eBill" label="<%=optionsPopupLabel%>" target="top.fr_main">
        <ct:menuParam key="idFichier" value="<%=_fichierTraitementEBill.getId()%>"/>
        <ct:menuParam key="nom" value="<%=_fichierTraitementEBill.getNomFichier()%>"/>
        <ct:menuParam key="date" value="<%=_fichierTraitementEBill.getDateLecture()%>"/>
        <ct:menuParam key="statutFichier" value="<%=String.valueOf(_fichierTraitementEBill.getStatutFichier().getIndex())%>"/>
    </ct:menuPopup>
</TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_fichierTraitementEBill.getNomFichier()%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_fichierTraitementEBill.getDateLecture()%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_fichierTraitementEBill.getNbElements()%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_fichierTraitementEBill.getNbElementsTraites()%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_fichierTraitementEBill.getNbElementsEnErreurs()%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_fichierTraitementEBill.getNbElementsRejetes()%></TD>
<TD class="mtd" onClick="<%=actionDetail%>"><%=_fichierTraitementEBill.getLibelleStatutFichier()%></TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>