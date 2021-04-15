<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
    idEcran = "GCA3031";
    rememberSearchCriterias = false;
%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.osiris.db.ebill.*" %>
<%
    bButtonNew = false;
    CAInscriptionEBillViewBean viewBean = (CAInscriptionEBillViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
    viewBean.setIdFichier(request.getParameter("idFichier"));
    String nom = request.getParameter("nom");
    String date = request.getParameter("date");
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
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-inscription-eBill" showTab="menu"/>

<SCRIPT>
    usrAction = "<%=globaz.osiris.application.CAApplication.DEFAULT_OSIRIS_NAME %>.ebill.inscriptionEBill.lister";
    bFind = true;

    function afficheMsg() {
        var contenu = document.getElementById('numNomSearch').value;
        document.getElementById('msgPonctuation').style.display = 'none';
        if (contenu.length > 0) {
            if (contenu.charAt(0) >= 0 && contenu.charAt(0) <= 9) {
                document.getElementById('msgPonctuation').style.display = '';
            }
        }
    }

   function postInit() {
       document.getElementById('forStatutFichier').value = <%=statutFichier%>;
       document.getElementById('date').disabled=true;
       document.getElementById("anchor_date").disabled=true;
   }

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>D&eacute;tail inscriptions et r&eacute;siliations eBill<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<TR>

    <TD width="90">Libell&eacute;</TD>
    <TD width="20">&nbsp;</TD>
    <TD width="400">
        <INPUT type="hidden" name="forIdFichier" value="<%=viewBean.getIdFichier()%>">
        <input disabled name="name" value="<%=nom%>" style="width : 9cm">
    </TD>
    <TD width="90">Date lecture</TD>
    <TD width="20">&nbsp;</TD>
    <TD nowrap width="300" colspan="2">
        <ct:FWCalendarTag name="date" doClientValidation="CALENDAR" value="<%=date%>"/>
    </TD>
    <TD width="90">Statut fichier</TD>
    <TD width="20">&nbsp;</TD>
    <TD nowrap>
        <select disabled name="forStatutFichier" tabindex="2">
            <option value=""></option>
            <option value="1"><ct:FWLabel key="EBILL_ENUM_TRAITE_SANS_ERREUR"/>
            </option>
            <option value="2"><ct:FWLabel key="EBILL_ENUM_TRAITE_AVEC_ERREUR"/>
            </option>
            <option value="3"><ct:FWLabel key="EBILL_ENUM_NON_TRAITE"/>
            </option>
        </select>
    </TD>
</TR>
<TR>
    <TD width="90">eBillAccountID</TD>
    <TD width="20">&nbsp;</TD>
    <TD nowrap width="200">
        <input type="text" name="forEBillAccountID" tabindex="-1" class="libelleLong">
    </TD>
    <TD width="90">Type</TD>
    <TD width="20">&nbsp;</TD>
    <TD nowrap>
        <select name="forType" tabindex="2">
            <option value=""></option>
            <option value="1"><ct:FWLabel key="EBILL_ENUM_INSCRIPTION"/>
            </option>
            <option value="2"><ct:FWLabel key="EBILL_ENUM_INSCRIPTION_DIRECTE"/>
            </option>
            <option value="3"><ct:FWLabel key="EBILL_ENUM_RESILIATION"/>
            </option>
        </select>
    </TD>
</TR>
<TR>
    <TD width="90">No d'affili&eacute;</TD>
    <TD width="20">&nbsp;</TD>
    <TD nowrap width="200">
        <input type="text" name="forNumAffilie" tabindex="-1" class="libelleLong">
    </TD>
    <TD width="90">Statut interne</TD>
    <TD width="20">&nbsp;</TD>
    <TD nowrap>
        <select name="forStatutInterne" tabindex="2">
            <option value=""></option>
            <option value="1"><ct:FWLabel key="EBILL_ENUM_TRAITE_AUTOMATIQUEMENT"/>
            </option>
            <option value="2"><ct:FWLabel key="EBILL_ENUM_TRAITE_MANUELLEMENT"/>
            </option>
            <option value="3"><ct:FWLabel key="EBILL_ENUM_A_TRAITER"/>
            </option>
        </select>
    </TD>
</TR>
<TR>
    <TD></TD>
    <TD colspan="2"></TD>
    <TD></TD>
    <TD colspan="2"></TD>
    <TD></TD>
    <TD colspan="2"></TD>
</TR>
<TR>
    <TD></TD>
    <TD colspan="2"></TD>
    <TD></TD>
    <TD colspan="2"></TD>
    <TD></TD>
    <TD colspan="2"></TD>
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