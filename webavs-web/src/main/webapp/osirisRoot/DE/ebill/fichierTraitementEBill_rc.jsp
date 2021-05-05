<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
    idEcran = "GCA3033";
    rememberSearchCriterias = true;
%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.osiris.db.ebill.CAFichierTraitementEBillViewBean" %>
<%@ page import="globaz.osiris.db.ebill.enums.CAFichierTraitementStatutEBillEnum" %>
<%
    bButtonNew = false;
    CAFichierTraitementEBillViewBean viewBean = (CAFichierTraitementEBillViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-OnlyDetail" showTab="menu"/>

<SCRIPT>
    usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.ebill.fichierTraitementEBill.lister";
    bFind = false;

    function afficheMsg() {
        var contenu = document.getElementById('numNomSearch').value;
        document.getElementById('msgPonctuation').style.display = 'none';
        if (contenu.length > 0) {
            if (contenu.charAt(0) >= 0 && contenu.charAt(0) <= 9) {
                document.getElementById('msgPonctuation').style.display = '';
            }
        }
    }
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Suivi des traitements eBill<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<TR>
    <TD>Gelesenes Datum</TD>
    <TD nowrap width="400" colspan="2">
        <ct:FWCalendarTag name="forDateLecture" doClientValidation="CALENDAR" value=""/>
    </TD>
</TR>
<TR>
    <TD>Datei-Status</TD>
    <TD nowrap>
    <select name="forStatutFichier" tabindex="2">
        <option value=""></option>
        <option value="1"><ct:FWLabel key="EBILL_ENUM_TRAITE"/></option>
        <option value="2"><ct:FWLabel key="EBILL_ENUM_A_TRAITER"/></option>
    </select>
</TD>
</TR>
</TR>
<TR>
    <TD></TD>
    <TD colspan="2"></TD>
    <TD></TD>
    <TD></TD>
    <TD></TD>
</TR>
<TR>
    <TD></TD>
    <TD colspan="2"></TD>
    <TD></TD>
    <TD></TD>
    <TD></TD>
</TR>
<TR>
    <TD nowrap width="128"></TD>
    <TD nowrap colspan="2"></TD>
</TR>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>