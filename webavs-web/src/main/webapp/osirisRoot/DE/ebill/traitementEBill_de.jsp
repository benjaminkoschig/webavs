<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
    idEcran = "GCA3035";
%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.osiris.db.ebill.*" %>
<%@ page import="globaz.osiris.db.ebill.enums.CATraitementCodeErreurEBillEnum" %>
<%@ page import="globaz.osiris.db.ebill.enums.CAStatutEBillEnum" %>
<%@ page import="globaz.osiris.db.ebill.enums.CATraitementEtatEBillEnum" %>
<%
    CATraitementEBillViewBean viewBean = (CATraitementEBillViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
    selectedIdValue = viewBean.getIdTraitement();
    bButtonUpdate = false;
    bButtonDelete = false;

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

    String directLink = "osiris?userAction=osiris.ebill.traitementEBill.afficher&selectedId=";
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">

    function avalider() {
        document.forms[0].elements('userAction').value = "osiris.ebill.traitementEBill.avalider";
        document.forms[0].submit();
    }

    function atraiter() {
        document.forms[0].elements('userAction').value = "osiris.ebill.traitementEBill.atraiter";
        document.forms[0].submit();
    }

    function annuler() {
        document.forms[0].elements('userAction').value = "back";
        document.forms[0].submit();
    }

    function init(){
        document.getElementById('statutInterne').value = <%=viewBean.getStatut().getNumeroStatut()%>;
        document.getElementById('etat').value = <%=viewBean.getEtat().getNumeroEtat()%>;
        <% if (viewBean.getCodeErreurDescription().isEmpty()) { %>
        document.getElementById('codeErreur').value = '';
        <% } else {%>
        document.getElementById('codeErreur').value = <%=viewBean.getCodeErreurDescription()%>;
        <% } %>
    }

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>D&eacute;tail traitement eBill<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<TR>
    <TD width="110">eBillAccountID</TD>
    <TD nowrap width="200">
        <INPUT type="hidden" name="idFichier" value="<%=viewBean.getIdFichier()%>">
        <input type="text" name="eBillAccountID" tabindex="1" class="libelleLong" value="<%=viewBean.geteBillAccountID()%>">
    </TD>
    <TD width="110">TransactionID</TD>
    <TD nowrap width="200">
        <input type="text" name="transactionID" tabindex="1" class="libelleLong" value="<%=viewBean.getTransactionID()%>">
    </TD>
    <TD width="110">Teilnehmernummer</TD>
    <TD nowrap width="200">
        <input type="text" name="numeroAffilie" tabindex="1" class="libelleLong" value="<%=viewBean.getNumeroAffilie()%>">
    </TD>
</TR>
<TR>
    <TD width="110">Name</TD>
    <TD nowrap width="200">
        <input type="text" name="nom" tabindex="1" class="libelleLong" value="<%=viewBean.getNom()%>">
    </TD>
    <TD width="110">Unternehmen</TD>
    <TD nowrap width="200">
        <input type="text" name="entreprise" tabindex="1" class="libelleLong" value="<%=viewBean.getEntreprise()%>">
    </TD>
    <TD width="110">eBill Stand</TD>
    <TD nowrap  width="200">
        <select name="etat" tabindex="2">
            <option value=""></option>
            <option value="1"><ct:FWLabel key="EBILL_ENUM_TRAITE"/>
            </option>
            <option value="2"><ct:FWLabel key="EBILL_ENUM_ERREUR"/>
            </option>
            <option value="3"><ct:FWLabel key="EBILL_ENUM_REJETEE"/>
            </option>
        </select>
    </TD>
</TR>
<TR>
    <TD width="110">Vorname</TD>
    <TD nowrap width="200">
        <input type="text" name="prenom" tabindex="1" class="libelleLong" value="<%=viewBean.getPrenom()%>">
    </TD>
    <TD width="110">Interner Status</TD>
    <TD nowrap width="200">
        <select name="statutInterne" tabindex="2">
            <option value=""></option>
            <option value="1"><ct:FWLabel key="EBILL_ENUM_TRAITE_AUTOMATIQUEMENT"/>
            </option>
            <option value="2"><ct:FWLabel key="EBILL_ENUM_TRAITE_MANUELLEMENT"/>
            </option>
            <option value="3"><ct:FWLabel key="EBILL_ENUM_A_TRAITER"/>
            </option>
        </select>
    </TD>
    <TD width="110">Fehler Code</TD>
    <TD nowrap  width="200">
        <select name="codeErreur" tabindex="2">
            <option value=""></option>
            <option value="03"><%=CATraitementCodeErreurEBillEnum.CODE_ERREUR_03.getDescription()%>
            </option>
            <option value="08"><%=CATraitementCodeErreurEBillEnum.CODE_ERREUR_08.getDescription()%>
            </option>
            <option value="25"><%=CATraitementCodeErreurEBillEnum.CODE_ERREUR_25.getDescription()%>
            </option>
        </select>
    </TD>
    </TR>
<TR>
    <TD width="110">eBill Fehler</TD>
    <TD nowrap width="200">
        <TEXTAREA cols="40" rows="3" name="texteErreur" class="libelleLong"><%=viewBean.getTexteErreur()%></TEXTAREA>
    </TD>
    <TD width="110">Interner Fehler</TD>
    <TD nowrap width="200">
        <TEXTAREA cols="40" rows="3" name="texteErreurInterne" class="libelleLong"><%=viewBean.getTexteErreurInterne()%></TEXTAREA>
    </TD>
</TR>
<TR>
    <TD nowrap width="110">ESR-Mitgliedsnummer</TD>
    <TD nowrap width="200">
        <input type="text" name="numAdherentBVR" tabindex="1" class="libelleLong" value="<%=viewBean.getNumAdherentBVR()%>">
    </TD>
    <TD nowrap width="110">ESR-Referenznummer</TD>
    <TD nowrap width="200">
        <input type="text" name="numRefBVR" tabindex="1" class="libelleLong" value="<%=viewBean.getNumRefBVR()%>">
    </TD>
</TR>
<TR>
    <TD nowrap width="110">Gesamtbetrag</TD>
    <TD nowrap width="200">
        <input type="text" name="montantTotal" tabindex="1" class="libelleLong" value="<%=viewBean.getMontantTotal()%>">
    </TD>
    <TD nowrap width="110">Verarbeitungsdatum</TD>
    <TD nowrap width="200">
        <ct:FWCalendarTag name="date" doClientValidation="CALENDAR" value="<%=viewBean.getDateTraitement()%>"/>
    </TD>
</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<% if (CAStatutEBillEnum.A_TRAITER.equals(viewBean.getStatut())) { %>
<INPUT id="atraitemanuellement" type="button" value="<ct:FWLabel key="EBILL_ENUM_TRAITE_MANUELLEMENT"/>" onclick="avalider()">
<% } %>
<% if (CAStatutEBillEnum.TRAITE_MANUELLEMENT.equals(viewBean.getStatut())) { %>
<INPUT id="atraiter" type="button" value="<ct:FWLabel key="EBILL_ENUM_A_TRAITER"/>" onclick="atraiter()">
<% } %>
<INPUT id="annuler" type="button" value="Annuler" onclick="annuler()">
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<%	} %>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>