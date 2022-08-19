<%@ page import="globaz.eform.vb.envoi.GFEnvoiViewBean" %>
<%@ page import="globaz.prestation.interfaces.util.nss.PRUtil" %>
<%@ page import="javax.swing.*" %>

<%@ page errorPage="/errorPage.jsp" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>


<%@ include file="/theme/detail_ajax/header.jspf" %>

<%
    idEcran = " GFE0101";
//    bButtonNew = false;
    GFEnvoiViewBean viewBean = (GFEnvoiViewBean) session.getAttribute("viewBean");

    String params = "&provenance1=TIERS&provenance2=CI";
    String jspLocation = servletContext + "/ijRoot/numeroSecuriteSocialeSF_select.jsp";

%>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<meta http-equiv="Content-Style-Type" content="text/css"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta name="User-Lang" content="<%=languePage%>"/>
<meta name="Context_URL" content="<%=servletContext%>"/>
<meta name="formAction" content="<%=formAction%>"/>

<link rel="stylesheet" type="text/css" href="<%=servletContext%>/common/css/bootstrap-2.0.4.css"/>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.css"/>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/eform/envoi/envoi_de.css"/>

<script type="text/javascript" src="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/menu.js"></script>

<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ include file="/jade/notation/notationLibJs.jspf" %>

<style>

</style>

<script>
    var bFind = true;
    var detailLink = "<%=actionNew%>";

    <%--function nssChange(tag) {--%>
    <%--    var param_nss = "756." + document.getElementById("partiallikeNSS").value;--%>
    <%--    if (tag.select !== null) {--%>
    <%--        var element = tag.select.options[tag.select.selectedIndex];--%>
    <%--        if (element.nss !== null) {--%>
    <%--            document.getElementById("nss").value = element.nss;--%>
    <%--        }--%>

    <%--        if (element.nom !== null) {--%>
    <%--            document.getElementById("nom").value = element.nom;--%>
    <%--            document.getElementById("nomAffiche").value = element.nom;--%>
    <%--        }--%>

    <%--        if (element.prenom !== null) {--%>
    <%--            document.getElementById("prenom").value = element.prenom;--%>
    <%--            document.getElementById("prenomAffiche").value = element.prenom;--%>
    <%--        }--%>

    <%--        if (element.nom !== null && element.prenom !== null) {--%>
    <%--            document.getElementById("nomPrenom").value = element.nom + " " + element.prenom;--%>
    <%--        }--%>

    <%--        if (element.codeSexe !== null) {--%>
    <%--            for (var i = 0; i < document.getElementById("csSexeAffiche").length; i++) {--%>
    <%--                if (element.codeSexe === document.getElementById("csSexeAffiche").options[i].value) {--%>
    <%--                    document.getElementById("csSexeAffiche").options[i].selected = true;--%>
    <%--                }--%>
    <%--            }--%>
    <%--            document.getElementById("csSexe").value = element.codeSexe;--%>
    <%--        }--%>

    <%--        if (element.provenance !== null) {--%>
    <%--            document.getElementById("provenance").value = element.provenance;--%>
    <%--        }--%>

    <%--        if (element.id !== null) {--%>
    <%--            document.getElementById("idAssure").value = element.idAssure;--%>
    <%--        }--%>

    <%--        if (element.dateNaissance !== null) {--%>
    <%--            document.getElementById("dateNaissance").value = element.dateNaissance;--%>
    <%--            document.getElementById("dateNaissanceAffiche").value = element.dateNaissance;--%>
    <%--        }--%>

    <%--        if (element.dateDeces !== null) {--%>
    <%--            document.getElementById("dateDeces").value = element.dateDeces;--%>
    <%--            document.getElementById("dateDecesAffiche").value = element.dateDeces;--%>
    <%--        }--%>

    <%--        if (element.codeEtatCivil !== null) {--%>
    <%--            for (var i = 0; i < document.getElementById("csEtatCivilAffiche").length; i++) {--%>
    <%--                if (element.codeEtatCivil === document.getElementById("csEtatCivilAffiche").options[i].value) {--%>
    <%--                    document.getElementById("csEtatCivilAffiche").options[i].selected = true;--%>
    <%--                }--%>
    <%--            }--%>
    <%--            document.getElementById("csEtatCivil").value = element.codeEtatCivil;--%>
    <%--        }--%>

    <%--        if ('<%=PRUtil.PROVENANCE_TIERS%>' === element.provenance) {--%>
    <%--            document.getElementById("nomAffiche").disabled = true;--%>
    <%--            document.getElementById("prenomAffiche").disabled = true;--%>
    <%--            document.getElementById("dateNaissanceAffiche").disabled = true;--%>
    <%--            document.getElementById("dateDecesAffiche").disabled = true;--%>
    <%--            document.getElementById("csEtatCivilAffiche").disabled = true;--%>
    <%--            document.getElementById("csSexeAffiche").disabled = true;--%>
    <%--        }--%>
    <%--    }--%>
    <%--    checkNss(param_nss);--%>
    <%--}--%>


</script>


<TITLE><%=idEcran%>
</TITLE>
</HEAD>

<body style="background-color: #B3C4DB">
<div class="title thDetail text-center">
    <ct:FWLabel key="ENVOI_TITRE"/>
    <span class="idEcran"><%=(null == idEcran) ? "" : idEcran%></span>
</div>

<%--Partie gestionnaire--%>
<form name="mainForm" action="" method="post" enctype="multipart/form-data">
    <ct:inputHidden name="likeNss"/>
    <div class="container-fluid" style="padding: 0px">
        <div class="row-fluid" style="font-weight: bold">
            <ct:FWLabel key="JSP_GESTIONNAIRE"/>
        </div>
        <div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
            <div style="display: table-cell;width: 140px;"><ct:FWLabel key="NOM_GESTIONNAIRE"/></div>
            <div style="display: table-cell;width: 310px;"><ct:inputText name="nomGestionnaire"
                                                                         id="nomGestionnaire"/></div>
        </div>
        <div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
            <div style="display: table-cell;width: 140px;"><ct:FWLabel key="DEPARTEMENT_GESTIONNAIRE"/></div>
            <div style="display: table-cell;width: 310px;"><ct:inputText name="departementGestionnaire"
                                                                         id="departementGestionnaire"/></div>
            <div style="display: table-cell;width: 140px;"><ct:FWLabel key="GESTIONNAIRE_TELEPHONE"/></div>
            <div style="display: table-cell;width: 310px;"><ct:inputText name="gestionnaireTelephone"
                                                                         id="gestionnaireTelephone"/></div>
            <div style="display: table-cell;width: 140px;"><ct:FWLabel key="GESTIONNAIRE_EMAIL"/></div>
            <div style="display: table-cell;width: 310px;"><ct:inputText name="birthday" id="birthday"/></div>
        </div>

        <%--Partie assuré--%>
        <div class="row-fluid" style="font-weight: bold">
            <ct:FWLabel key="ASSURE"/>
        </div>
        <div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
            <div style="display: table-cell;width: 140px;"><ct:FWLabel key="NSS"/></div>
            <div style="display: table-cell;width: 310px;"><nss:nssPopup avsMinNbrDigit="2" nssMinNbrDigit="2" name="Nss" newnss="true" tabindex="3"/></div>


<%--            ct1:nssPopup name="likeNSS" onFailure="nssFailure();"--%>
<%--            onChange="nssChange(tag);" params="<%=params%>"--%>
<%--            value="<%=viewBean.getNumeroAvsFormateSansPrefixe()%>"--%>
<%--            newnss="<%=viewBean.isNNSS()%>"--%>
<%--            jspName="<%=jspLocation%>" avsMinNbrDigit="3"--%>
<%--            nssMinNbrDigit="3" avsAutoNbrDigit="11"--%>
<%--            nssAutoNbrDigit="10"--%>
        </div>
        <div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
            <div style="display: table-cell;width: 140px;"><ct:FWLabel key="LASTNAME"/></div>
            <div style="display: table-cell;width: 310px;"><ct:inputText name="lastName" id="lastName"
                                                                         readonly="true"/></div>
            <div style="display: table-cell;width: 140px;"><ct:FWLabel key="FIRSTNAME"/></div>
            <div style="display: table-cell;width: 310px;"><ct:inputText name="firstName" id="firstName"
                                                                         readonly="true"/></div>
            <div style="display: table-cell;width: 140px;"><ct:FWLabel key="BIRTHDAY"/></div>
            <div style="display: table-cell;width: 310px;"><ct:inputText name="birthday" id="birthday"
                                                                         readonly="true"/></div>
        </div>
        <div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
            <div style="display: table-cell;width: 140px;"><ct:FWLabel key="ADRESSE_DOMICILE"/></div>
            <div style="display: table-cell;width: 310px;"><textarea id="remarque" name="remarque" cols="100"
                                                                     rows="5"></textarea></div>
        </div>

        <div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
            <div style="display: table-cell;width: 1350px; border-bottom: 2px solid black"></div>
        </div>


        <%--        Partie sur la caisse--%>
        <div class="row-fluid" style="font-weight: bold; margin-top: 15px;">
            <ct:FWLabel key="CAISSE_DESTINATRICE"/>
        </div>
        <div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
            <div style="display: table-cell;width: 140px;"><ct:FWLabel key="CAISSE"/></div>
            <div style="display: table-cell;width: 310px; padding-bottom: 40px"><ct:select name="attributAvisEcheance"
                                                                      styleClass="longSelect" tabindex="3">
                <ct:optionsCodesSystems csFamille="test selection caisse">
                </ct:optionsCodesSystems>
            </ct:select>
            </div>
        </div>
        <div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
            <div style="display: table-cell;width: 1350px; border-bottom: 2px solid black"></div>
        </div>

        <%--        Partie sur les fichiers--%>
        <div class="row-fluid" style="font-weight: bold; padding-top: 20px">
            <ct:FWLabel key="DOCUMENT_A_ENVOYER"/>
        </div>
        <div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
            <div style="display: table-cell;width: 140px;"><ct:FWLabel key="TYPE_DE_FICHIER"/></div>
            <div style="display: table-cell;width: 310px;"><ct:select name="attributAvisEcheance" styleClass="longSelect" tabindex="3">
                <ct:option value="" label=""></ct:option>
                <ct:option value="Rente AVS" label="Rente AVS"></ct:option>
                <ct:option value="Rente AI" label="Rente AI"></ct:option>
                <ct:option value="API AVS" label="API AVS"></ct:option>
                <ct:option value="API AI" label="API AI"></ct:option>
            </ct:select></div>
        </div>

        <div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
            <div style="display: table-cell;width: 140px;"><ct:FWLabel key="REPERTOIRE_SOURCE"/></div>
            <div style="display: table-cell;width: 310px;"><input id='upload' name="upload" type="file"></div>
        </div>

        <div style="display: table; margin-top: 15px;" class="panel-body std-body-height">
            <div style="display: table-cell;width: 140px;"><ct:FWLabel key="SELECTION_FICHIER"/></div>
            <div style="width: 310px;background-color:#FFF">
                <table id="periodes" name=periode" class="areaTable" style="width:100%">
                    <tbody>
                    <tr name="test"> fichier 1</tr>
                    <tr> fichier 2</tr>
                    <tr> fichier 3</tr>
                    </tbody>
                </table>
            </div>
        </div>
        <%--        <input type="hidden"--%>
        <%--               name="nss"--%>
        <%--               id="nss"--%>
        <%--               value="<%=viewBean.getNss()%>"/>--%>
    </div>
</body>
</html>

