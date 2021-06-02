<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
    idEcran = "GCA3032";
%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.osiris.db.ebill.CAInscriptionEBillViewBean" %>
<%@ page import="globaz.osiris.db.ebill.enums.CAInscriptionTypeEBillEnum" %>
<%@ page import="globaz.osiris.db.ebill.enums.CAStatutEBillEnum" %>
<%
    CAInscriptionEBillViewBean viewBean = (CAInscriptionEBillViewBean) session.getAttribute(globaz.osiris.servlet.action.CADefaultServletAction.VB_ELEMENT);
    selectedIdValue = viewBean.getIdInscription();
    bButtonUpdate = false;
    bButtonDelete = false;

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

    String directLink = "osiris?userAction=osiris.ebill.inscriptionEBill.afficher&selectedId=";
%>

<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">

    function avalider() {
        document.forms[0].elements('userAction').value = "osiris.ebill.inscriptionEBill.avalider";
        document.forms[0].submit();
    }

    function atraiter() {
        document.forms[0].elements('userAction').value = "osiris.ebill.inscriptionEBill.atraiter";
        document.forms[0].submit();
    }

    function annuler() {
        document.forms[0].elements('userAction').value = "back";
        document.forms[0].submit();
    }

    function init(){
        document.getElementById('type').value = <%=viewBean.getType().getNumeroType()%>;
        document.getElementById('statutInterne').value = <%=viewBean.getStatut().getNumeroStatut()%>;
    }

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>D&eacute;tail inscription/r&eacute;siliation eBill<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<TR>
    <TD width="110">eBillAccountID</TD>
    <TD nowrap width="200">
        <INPUT type="hidden" name="idFichier" value="<%=viewBean.getIdFichier()%>">
        <input type="text" name="eBillAccountID" tabindex="1" class="libelleLong" value="<%=viewBean.geteBillAccountID()%>">
    </TD>
    <TD width="110">Type</TD>
    <TD nowrap  width="200">
        <select name="type" tabindex="2">
            <option value=""></option>
            <option value="1"><ct:FWLabel key="EBILL_ENUM_INSCRIPTION"/>
            </option>
            <option value="2"><ct:FWLabel key="EBILL_ENUM_INSCRIPTION_DIRECTE"/>
            </option>
            <option value="3"><ct:FWLabel key="EBILL_ENUM_RESILIATION"/>
            </option>
        </select>
    </TD>
    <TD width="110">No d'affili&eacute;</TD>
    <TD nowrap width="200">
        <input type="text" name="numeroAffilie" tabindex="1" class="libelleStandard" value="<%=viewBean.getNumeroAffilie()%>">
    </TD>
</TR>
<TR>
    <TD width="110">Nom</TD>
    <TD nowrap width="200">
        <input type="text" name="nom" tabindex="1" class="libelleStandard" value="<%=viewBean.getNom()%>">
    </TD>
    <TD width="110">Entreprise</TD>
    <TD nowrap width="200">
        <input type="text" name="entreprise" tabindex="1" class="libelleStandard" value="<%=viewBean.getEntreprise()%>">
    </TD>
    <TD width="110">R&ocirc;le paritaire</TD>
    <TD nowrap width="200">
        <input type="checkbox" name="roleParitaire" tabindex="1" class="libelleStandard" value="<%=viewBean.getRoleParitaire()%>">
    </TD>
</TR>
<TR>
    <TD width="110">Pr&eacute;nom</TD>
    <TD nowrap width="200">
        <input type="text" name="prenom" tabindex="1" class="libelleStandard" value="<%=viewBean.getPrenom()%>">
    </TD>
    <TD width="110">Contact</TD>
    <TD nowrap width="200">
        <input type="text" name="contact" tabindex="1" class="libelleStandard" value="<%=viewBean.getContact()%>">
    </TD>
    <TD nowrap width="110">R&ocirc;le personnel</TD>
    <TD nowrap width="200">
        <input type="checkbox" name="rolePersonnel" tabindex="1" class="libelleStandard" value="<%=viewBean.getRolePersonnel()%>">
    </TD>
</TR>
<TR>
    <TD width="110">Adresse 1</TD>
    <TD nowrap width="200">
        <input type="text" name="adresse1" tabindex="1" class="libelleStandard" value="<%=viewBean.getAdresse1()%>">
    </TD>
    <TD width="110">Adresse 2</TD>
    <TD nowrap width="200">
        <input type="text" name="adresse2" tabindex="1" class="libelleStandard" value="<%=viewBean.getAdresse2()%>">
    </TD>
    <TD width="110">Statut interne</TD>
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
</TR>
<TR>
    <TD width="110">NPA</TD>
    <TD nowrap width="200">
        <input type="text" name="npa" tabindex="1" class="libelleStandard" value="<%=viewBean.getNpa()%>">
    </TD>
    <TD width="110">Localit&eacute;</TD>
    <TD nowrap width="200">
        <input type="text" name="localite" tabindex="1" class="libelleStandard" value="<%=viewBean.getLocalite()%>">
    </TD>
    <TD width="110">Erreur Interne</TD>
    <TD nowrap width="200">
        <TEXTAREA cols="40" rows="3" name="texteErreurInterne" class="libelleStandard"><%=viewBean.getTexteErreurInterne()%></TEXTAREA>
    </TD>
</TR>
<TR>
    <TD width="110">e-mail</TD>
    <TD nowrap width="200">
        <input type="text" name="email" tabindex="1" class="libelleStandard" value="<%=viewBean.getEmail()%>">
    </TD>
    <TD width="110">No t&eacute;l&eacute;phone</TD>
    <TD nowrap width="200">
        <input type="text" name="numTel" tabindex="1" class="libelleStandard" value="<%=viewBean.getNumTel()%>">
    </TD>
</TR>
<TR>
    <TD nowrap width="110">No adh&eacute;rent BVR</TD>
    <TD nowrap width="200">
        <input type="text" name="numAdherentBVR" tabindex="1" class="libelleLong" value="<%=viewBean.getNumAdherentBVR()%>">
    </TD>
    <TD nowrap width="110">No r&eacute;f&eacute;rence BVR</TD>
    <TD nowrap width="200">
        <input type="text" name="numRefBVR" tabindex="1" class="libelleLong" value="<%=viewBean.getNumRefBVR()%>">
    </TD>
</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<% if (CAStatutEBillEnum.A_TRAITER.equals(viewBean.getStatut())) { %>
<INPUT id="valider" type="button" value="<ct:FWLabel key="EBILL_ENUM_TRAITE_MANUELLEMENT"/>" onclick="avalider()">
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