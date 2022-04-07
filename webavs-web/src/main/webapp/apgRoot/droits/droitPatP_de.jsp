<%@page import="globaz.apg.enums.APModeEditionDroit" %>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*"
         contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.apg.api.droits.IAPDroitAPG" %>
<%@ page import="globaz.apg.servlet.APAbstractDroitDTOAction" %>
<%@ page import="globaz.apg.servlet.IAPActions" %>
<%@ page import="globaz.globall.util.JANumberFormatter" %>
<%@ page import="globaz.jade.client.util.JadeStringUtil" %>
<%@ page import="globaz.pyxis.db.adressecourrier.TIPays" %>
<%@ page import="globaz.prestation.interfaces.util.nss.PRUtil" %>
<%@ page import="globaz.apg.vb.droits.APDroitPatPViewBean" %>
<%@ page import="globaz.prestation.beans.PRPeriode" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="globaz.globall.db.BSessionUtil" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="globaz.globall.db.FWFindParameter" %>
<%@ page import="globaz.apg.properties.APParameter" %>
<%@ page import="globaz.globall.db.FWFindParameterManager" %>
<%@ page import="globaz.apg.helpers.droits.APAbstractDroitPHelper" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>

<%@ include file="/theme/detail/header.jspf" %>
<%!
    %><%
    idEcran = "PAP0012";

    APDroitPatPViewBean viewBean = (APDroitPatPViewBean) session.getAttribute("viewBean");
    selectedIdValue = viewBean.getIdDroit();
    viewBean.setAControler(false);
    viewBean.setCheckWarn(false);
    bButtonUpdate = viewBean.isModifiable() && bButtonUpdate;
    bButtonValidate = false;
    bButtonCancel = false;
    bButtonDelete = false;
    String dateDebutValidite = viewBean.getDateValidite();

%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/apgRoot/droits/droitPeriodeUtils.js"></script>
<script type="text/javascript" src="<%=servletContext%>/apgRoot/droits/enfantUtils.js"></script>
<script type="text/javascript" src="<%=servletContext%>/apgRoot/scripts/apgUtils.js"></script>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalapat" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ap-optionsempty"/>

<script type="text/javascript">
    var JOUR_SUPPLEMENTAIRE_PAT = true;
    var JOUR_SUPPLEMENTAIRE_PAI = false;
    var SOUMIS_COTISATION_PERIODE = true;
    var EDITION_MODE = false;
    <%if(viewBean.getModeEditionDroit().equals(APModeEditionDroit.CREATION) || viewBean.getModeEditionDroit().equals(APModeEditionDroit.EDITION) ){%>
    EDITION_MODE = true;
    <%}%>

    var ACTION_DROIT = "apg.droits.droitPatP",
        jsonAnnonce;

    /* Contrôle que la date de naissance n'est pas avant la dateMin du "01.01.2021" */
    function checkDateDebutAPG(date) {
        var dateRaw = date.split('.');
        var dateDebut = new Date(dateRaw[0] + '/' + dateRaw[1] + '/' + dateRaw[2]);
        var user = '<%=viewBean.getSession().getUserName()%>';
        var isError = false;
        var dateMin = new Date('<%=dateDebutValidite%>');
        if (dateDebut < dateMin) {
            var text = '<%=viewBean.getSession().getLabel("ERREUR_MIN_DATE_NAI")%>';
            var day = dateMin.getDate();
            if (day < 10) {
                day = '0' + day;
            }
            var month = dateMin.getMonth() + 1;
            if (month < 10) {
                month = '0' + month;
            }
            text = text.replace("{0}", day + '.' + month + '.' + dateMin.getFullYear());
            showErrorMessage(text);
            isError = true;
        } else {
            isError = false;
        }

        return isError;
    }
    function checkDateDebutAPGNaissance(date) {
        var isError = checkDateDebutAPG(date);
        if(isError){
            document.getElementById("dateDebutDroit").value = "";
        }
    }
    function add() {
        nssUpdateHiddenFields();
        document.forms[0].elements('userAction').value = "apg.droits.droitPatP.ajouter"
    }

    function upd() {
        document.getElementById("nomAffiche").disabled = true;
        document.getElementById("prenomAffiche").disabled = true;
        document.getElementById("dateNaissanceAffiche").disabled = true;
        document.getElementById("dateDecesAffiche").disabled = true;
        document.getElementById("csEtatCivilAffiche").disabled = true;
        document.getElementById("csSexeAffiche").disabled = true;
        $('#nbJourSolde').prop("disabled", true);
        $('#jourSupplementaire').prop("disabled", true);
        $('#dateFinCalculee').prop("disabled", true);
        $('#isSoumisCotisation').prop("disabled", true);
        $('#tauxImpotSource').prop("disabled", true);
    }

    function validate() {
        var isModifiable = <%=viewBean.isModifiable()%>;
        var hasError = false;
        if (document.forms[0].elements('_method').value === "read" && !isModifiable) {
            document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_ENFANT_PAT%>.chercher";
        } else {
            $('#aControler').prop("checked", true);
            $('#checkWarn').prop("checked", true);
            hasError = nextStepValidate();
        }
        if (!hasError) {
            action(COMMIT);
        }
    }

    function validateSansCheck() {
        var isModifiable = <%=viewBean.isModifiable()%>;
        var hasError = false;
        if (document.forms[0].elements('_method').value === "read" && !isModifiable) {
            document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_ENFANT_PAT%>.chercher";
        } else {
            $('#aControler').prop("checked", false);
            $('#checkWarn').prop("checked", false);
            hasError = nextStepValidate();
        }
        if (!hasError) {
            action(COMMIT);
        }
    }

    function nextStepValidateAfterPopupSeodor() {
        nssUpdateHiddenFields();
        //Récupération des dates
        // si l'utilisateur à saisit des valeurs pour les priodes sans cliquer le btn ajouter, on lui fit à sa place
        var dateDebut = $('#dateDebutPeriode').val();
        var dateFin = $('#dateFinPeriode').val();
        var nbrJours = $('#nbrJour').val();
        var tauxImposition = "";
        var cantonImposition = "";
        if (document.getElementById("isSoumisCotisation").checked) {
            tauxImposition = $('#tauxImpotSource').val();
            cantonImposition = $('#csCantonDomicileAffiche').val();
        }

        // Si au moins un des 3 champs n'est pas vide
        if (dateDebut || dateFin) {
            addPeriode();
        }
        // Si aucune période n'est renseigné -> message d'erreurs
        if (periodes.lenght == 0) {
            showErrorMessage("Aucune période n'est renseignée");
            return;
        }

        document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_ENFANT_PAT%>.chercher";

        var tmp = "";
        for (var i = 0; i < periodes.length; i++) {
            tmp += periodes[i].toString();
            if (i + 1 < periodes.length) {
                tmp += ";";
            }
        }
        $('#periodesAsString').val(tmp);
        action(COMMIT);
    }

    function nextStepValidate() {
        nssUpdateHiddenFields();
        //Récupération des dates
        // si l'utilisateur à saisit des valeurs pour les priodes sans cliquer le btn ajouter, on lui fit à sa place
        var dateDebut = $('#dateDebutPeriode').val();
        var dateFin = $('#dateFinPeriode').val();
        var nbrJours = $('#nbrJour').val();
        var tauxImposition = "";
        var cantonImposition = "";

        if (document.getElementById("isSoumisCotisation").checked) {
            tauxImposition = $('#tauxImpotSource').val();
            cantonImposition = $('#csCantonDomicileAffiche').val();
        }

        // Si au moins un des 3 champs n'est pas vide
        if (dateDebut || dateFin) {
            addPeriodePatP();
        }
        // Si aucune période n'est renseigné -> message d'erreurs
        if (periodes.lenght == 0) {
            showErrorMessage("Aucune période n'est renseignée");
            return true;
        }

        <%if(viewBean.getModeEditionDroit().equals(APModeEditionDroit.CREATION)){%>
        document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_SAISIE_CARTE_APAT%>.ajouter";
        <%} else if(viewBean.getModeEditionDroit().equals(APModeEditionDroit.EDITION)){%>
        document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_SAISIE_CARTE_APAT%>.modifier";
        <%} else if(viewBean.getModeEditionDroit().equals(APModeEditionDroit.LECTURE)){%>
        if (EDITION_MODE == true) {
            document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_SAISIE_CARTE_APAT%>.modifier";
        } else {
            document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_ENFANT_PAT%>.chercher";

        }
        <%}%>

        var tmp = "";
        for (var i = 0; i < periodes.length; i++) {
            tmp += periodes[i].toString();
            if (i + 1 < periodes.length) {
                tmp += ";";
            }
        }
        $('#periodesAsString').val(tmp);

    }

    // On définit dateFinCalculee en comparant les jours disponibles entre les périodes saisies et le nombres de jours soldes total saisi ou calculé
    function resolveDateFinCalculee(nbJourSoldeTot) {

        // si nbJourSoldeTot n'est pas initialisé on reprends les valeurs du tableau dans la ligne nbJourSuppSummary
        if (!nbJourSoldeTot) {
            nbJourSoldeTot = Number($(".nbJourSuppSummary #nbJourSoldesTot").text()) + Number($(".nbJourSuppSummary #nbJourSuppTot").text());
        }

        var dateFinSaisiePeriode = Date.toDate($('#dateFinPeriode').val()); // init avec la date saisie dans le champs "période au"

        // recherche dans tous le tableau si une des dateFin du tableau est plus grande que la dernière date saisie
        $("#periodes .dateFin").each(function(index, element) {
            var dateFinTableau = Date.toDate($(element).text());
            if (dateFinTableau) {
                if (isNaN(dateFinSaisiePeriode.getTime())) { // si la date saisie dans le champs "période au" est vide on prends la dateFinTableau à la place
                    dateFinSaisiePeriode = dateFinTableau;
                } else if (dateFinSaisiePeriode < dateFinTableau) { // si dateFinTableau est plus grande on la prends comme dateFinDernierePeriode
                    dateFinSaisiePeriode = dateFinTableau;
                }
            }
        });

        var dateDebutSaisiePeriode = Date.toDate($('#dateDebutPeriode').val()); // init avec la date saisie dans le champs "période du"

        // recherche dans tous le tableau si une des dateDebut du tableau est plus petite que la dernière date saisie
        $("#periodes .dateDebut").each(function(index, element) {
            var dateDebutTableau = Date.toDate($(element).text());
            if (dateDebutTableau) {
                if (isNaN(dateDebutSaisiePeriode.getTime())) { // si la date saisie dans le champs "période du" est vide on prends dateDebutTableau à la place
                    dateDebutSaisiePeriode = dateDebutTableau;
                } else if (dateDebutSaisiePeriode > dateDebutTableau) { // si dateDebutTableau est plus petite on la prends comme dateDebutDernierePeriode
                    dateDebutSaisiePeriode = dateDebutTableau;
                }
            }
        });

        // nb de jours disponible = jour entre la date de début la plus petite et la date de fin la plus grande
        var nbJoursDisponible = dateDebutSaisiePeriode.daysBetween(dateFinSaisiePeriode);

        // nb de jours de difference = difference entre nb jours soldées total (jours soldées + indemnités supplémentaires) et nb de jours disponible
        var nbJoursDifference = Math.abs(nbJoursDisponible - nbJourSoldeTot);

        var dateFinCalculee;

        // si nb jours soldées total (jours soldées + indemnités supplémentaires) > nb jours disponibles dans la période de la prestation
        if (nbJourSoldeTot > nbJoursDisponible) {
            dateFinCalculee = Date.toDate(dateFinSaisiePeriode);
            dateFinCalculee.setDate(dateFinCalculee.getDate() + nbJoursDifference);
        } else {
            dateFinCalculee = Date.toDate(dateFinSaisiePeriode);
        }

        if (isNaN(dateFinCalculee.getTime())) {
            return "";
        } else {
            return globazNotation.utilsDate.convertJSDateToGlobazStringDateFormat(dateFinCalculee);
        }
    }

    /* Contrôle que la date fin période n'est pas éloigné de plus de 6 mois (délai cadre) de la date de naissance */
    function isDelaiCadreDepasse(dateFin) {
        var dateNaissance = Date.toDate($('#dateDebutDroit').val());
        var paterniteMoisMaxDelaiCadre = parseInt("<%=APAbstractDroitPHelper.PATERNITE_MOIS_MAX_DELAI_CADRE%>");
        var dateDeFinDroitMax = Date.toDate(dateNaissance.setMonth(dateNaissance.getMonth()+paterniteMoisMaxDelaiCadre));

        if (dateFin > dateDeFinDroitMax) {
            return true;
        } else {
            return false;
        }
    }

    /* Contrôle que les champs jours supplémentaires et jours de congées sont dans les limites autorisées */
    function isChampsHorsLimites(nbJourSuppChamp, nbJourSoldeChamp) {
        if (nbJourSuppChamp > 4 || nbJourSoldeChamp > 10) {
            return true;
        } else {
            return false;
        }
    }

    /* Contrôle que le nombre total de jours de congées + le nombre de jours supplémentaires ne dépasse pas la maximum autorisé */
    function isNbJourPlusGrandQueJourMax(nbJourTot, nbJourMax) {
        if (nbJourTot > nbJourMax) {
            return true;
        } else {
            return false;
        }
    }

    function cancel() {
        if (document.forms[0].elements('_method').value == "add") {
            document.forms[0].elements('userAction').value = "back";
        } else {
            document.forms[0].elements('userAction').value = "apg.droits.droitPatP.afficher";
        }
    }

    function del() {
        if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")) {
            document.forms[0].elements('userAction').value = "apg.droits.droitPatP.supprimer";
            document.forms[0].submit();
        }
    }

    function init() {
        if (<%=viewBean.getIsSoumisCotisation()%>) {
            document.getElementById("isSoumisCotisation").checked = true;
        }

        // On définit dateFinCalculee avec la date la plus éloignée du tableau ou le champs "période au"
        $('#dateFinCalculee').val(resolveDateFinCalculee());

        showCantonImpotSource();
        checkParametersWebService();
        checkMsgWarn();
    }

    function checkParametersWebService() {
        $("#dialog_apg_webservice").dialog({
            resizable: false,
            height: 500,
            width: 500,
            modal: true,
            buttons: [{
                id: "Correct",
                text: "<ct:FWLabel key='JSP_CONTINUER'/>",
                click: function () {
                    $('#aControler').prop("checked", false);
                    EDITION_MODE = false;
                    $('#modeEditionDroit').val('<%=APModeEditionDroit.LECTURE%>');
                    nextStepValidateAfterPopupSeodor();
                    $(this).dialog("close");
                }
            }, {
                id: "Ok",
                text: "<ct:FWLabel key='JSP_CORRIGER'/>",
                click: function () {
                    action(UPDATE);
                    upd();
                    EDITION_MODE = true;
                    repaintTablePeriodes();
                    $(this).dialog("close");
                }
            }],
            open: function () {
                $(".ui-dialog-titlebar-close", ".ui-dialog-titlebar").hide();
                $("#Ok").focus();
                <% if(viewBean.getMessagesErrorList().getApgSeodorErreurEntityList().size()!=0) { %>
                $('#dialog_apg_webservice').append('<%=viewBean.getMessagesErrorList().getListErreursTableHTML()%>');
                <% } %>
            }
        });
    }

    function checkMsgWarn() {
        $("#dialog_apg_warn").dialog({
            resizable: false,
            height: 300,
            width: 500,
            modal: true,
            buttons: [{
                id: "oui",
                text: "<ct:FWLabel key='JSP_OUI'/>",
                click: function () {
                    $('#checkWarn').prop("checked", false);
                    if(<%=viewBean.getModeEditionDroit().equals(APModeEditionDroit.CREATION)%>){
                        $('#modeEditionDroit').val('<%=APModeEditionDroit.CREATION%>');
                        document.forms[0].elements('_method').value = "add";
                    }
                    if(<%=viewBean.getModeEditionDroit().equals(APModeEditionDroit.EDITION)%>){
                        $('#modeEditionDroit').val('<%=APModeEditionDroit.EDITION%>');
                        document.forms[0].elements('_method').value = "upd";
                    }
                    validateSansCheck();
                    $(this).dialog("close");
                }
            }, {
                id: "non",
                text: "<ct:FWLabel key='JSP_NON'/>",
                click: function () {
                    action(UPDATE);
                    upd();
                    EDITION_MODE = true;
                    repaintTablePeriodes();
                    if(<%=viewBean.getModeEditionDroit().equals(APModeEditionDroit.CREATION)%>){
                        $('#modeEditionDroit').val('<%=APModeEditionDroit.CREATION%>');
                        document.forms[0].elements('_method').value = "add";
                    }
                    $(this).dialog("close");
                }
            }],
            open: function () {
                $(".ui-dialog-titlebar-close", ".ui-dialog-titlebar").hide();
                <% if(viewBean.hasMessageWarn()) { %>
                $('#dialog_apg_warn').append("<%=viewBean.getMessagesWarn()%>");$('#dialog_apg_warn').prop("vertical-align", "middle");
                <% } %>
            }
        });
    }

    function arret() {
        nssUpdateHiddenFields();
        document.forms[0].elements('userAction').value = "apg.droits.droitPatP.arreterEtape1";
        document.forms[0].elements('arreter').value = "on";
        document.forms[0].submit();
    }

    function limiteur() {
        // limite la saisie de la remarque à 8000 caractères
        maximum = 7990;
        if (document.forms[0].elements('remarque').value.length > maximum) {
            document.forms[0].elements('remarque').value = document.forms[0].elements('remarque').value.substring(0, maximum);
        }
    }

    function postInit() {
        if (<%=viewBean.isTrouveDansTiers()%>) {
            document.getElementById("nomAffiche").disabled = true;
            document.getElementById("prenomAffiche").disabled = true;
            document.getElementById("dateNaissanceAffiche").disabled = true;
            document.getElementById("csEtatCivilAffiche").disabled = true;
            document.getElementById("csSexeAffiche").disabled = true;
        }
        if (<%=viewBean.getIdDroit()!=""%>) {
            document.getElementById("linkTiers").style.visibility = "visible";
        } else {
            document.getElementById("linkTiers").style.visibility = "hidden";
        }
        $('#nbJourSolde').prop("disabled", true);
        $('#jourSupplementaire').prop("disabled", true);
        $('#dateFinCalculee').prop("disabled", true);
        $('#isSoumisCotisation').prop("disabled", true);
        $('#tauxImpotSource').prop("disabled", true);
    }

    function periodeChange() {
        var dateDebut = $('#dateDebutPeriode').val();
        var dateFin = $('#dateFinPeriode').val();
        var impot = $('#isSoumisCotisation');
        var taux = $('#tauxImpotSource');
        $('#nbJourSolde').prop("disabled", false);
        $('#jourSupplementaire').prop("disabled", false);

        if(impot.is(':disabled')
            && dateDebut != ''
            && dateFin != '') {
            impot.prop("disabled", false);
            taux.prop("disabled", false);
        } else if(!impot.is(':disabled')
            && (dateDebut == '' || dateFin == '')) {
            impot.prop("disabled", true);
            taux.prop("disabled", true);
        }
    }

    function nssFailure() {
        var param_nss = "756." + document.getElementById("partiallikeNSS").value;
        document.getElementById("idAssure").value = null;
        document.getElementById("nss").value = null;
        document.getElementById("provenance").value = null;
        document.getElementById("nomAffiche").disabled = false;
        document.getElementById("prenomAffiche").disabled = false;
        document.getElementById("nomPrenom").value = "";
        document.getElementById("dateNaissanceAffiche").disabled = false;
        document.getElementById("dateDecesAffiche").disabled = false;
        document.getElementById("csEtatCivilAffiche").disabled = false;
        document.getElementById("csSexeAffiche").disabled = false;
        checkNss(param_nss);
    }

    function nssUpdateHiddenFields() {
        document.getElementById("nom").value = document.getElementById("nomAffiche").value;
        document.getElementById("prenom").value = document.getElementById("prenomAffiche").value;
        document.getElementById("dateNaissance").value = document.getElementById("dateNaissanceAffiche").value;
        document.getElementById("dateDeces").value = document.getElementById("dateDecesAffiche").value;
        document.getElementById("csEtatCivil").value = document.getElementById("csEtatCivilAffiche").value;
        document.getElementById("nss").value = document.getElementById("likeNSS").value;
        document.getElementById("csSexe").value = document.getElementById("csSexeAffiche").value;
        document.getElementById("aControler").value = $('#aControler').is(':checked');
        document.getElementById("checkWarn").value = $('#checkWarn').is(':checked');
        if (!document.getElementById("isSoumisCotisation").checked) {
            document.getElementById("csCantonDomicile").value = '';
        } else {
            document.getElementById("csCantonDomicile").value = document.getElementById("csCantonDomicileAffiche").value;
        }
    }

    function nssChange(tag) {
        var param_nss = "756." + document.getElementById("partiallikeNSS").value;
        if (tag.select !== null) {
            var element = tag.select.options[tag.select.selectedIndex];
            if (element.nss !== null) {
                document.getElementById("nss").value = element.nss;
            }

            if (element.nom !== null) {
                document.getElementById("nom").value = element.nom;
                document.getElementById("nomAffiche").value = element.nom;
            }

            if (element.prenom !== null) {
                document.getElementById("prenom").value = element.prenom;
                document.getElementById("prenomAffiche").value = element.prenom;
            }

            if (element.nom !== null && element.prenom !== null) {
                document.getElementById("nomPrenom").value = element.nom + " " + element.prenom;
            }

            if (element.codeSexe !== null) {
                for (var i = 0; i < document.getElementById("csSexeAffiche").length; i++) {
                    if (element.codeSexe === document.getElementById("csSexeAffiche").options[i].value) {
                        document.getElementById("csSexeAffiche").options[i].selected = true;
                    }
                }
                document.getElementById("csSexe").value = element.codeSexe;
            }

            if (element.provenance !== null) {
                document.getElementById("provenance").value = element.provenance;
            }

            if (element.id !== null) {
                document.getElementById("idAssure").value = element.idAssure;
            }

            if (element.dateNaissance !== null) {
                document.getElementById("dateNaissance").value = element.dateNaissance;
                document.getElementById("dateNaissanceAffiche").value = element.dateNaissance;
            }

            if (element.dateDeces !== null) {
                document.getElementById("dateDeces").value = element.dateDeces;
                document.getElementById("dateDecesAffiche").value = element.dateDeces;
            }

            if (element.codeEtatCivil !== null) {
                for (var i = 0; i < document.getElementById("csEtatCivilAffiche").length; i++) {
                    if (element.codeEtatCivil === document.getElementById("csEtatCivilAffiche").options[i].value) {
                        document.getElementById("csEtatCivilAffiche").options[i].selected = true;
                    }
                }
                document.getElementById("csEtatCivil").value = element.codeEtatCivil;
            }

            if ('<%=PRUtil.PROVENANCE_TIERS%>' === element.provenance) {
                document.getElementById("nomAffiche").disabled = true;
                document.getElementById("prenomAffiche").disabled = true;
                document.getElementById("dateNaissanceAffiche").disabled = true;
                document.getElementById("dateDecesAffiche").disabled = true;
                document.getElementById("csEtatCivilAffiche").disabled = true;
                document.getElementById("csSexeAffiche").disabled = true;
            }
        }
        checkNss(param_nss);
    }

    function showCantonImpotSource() {
        var trCantonImposition = document.getElementById("availableIfSoumisCotisation");
        var cbxImpositionSouce = document.getElementById("isSoumisCotisation");
        if (cbxImpositionSouce.checked) {
            trCantonImposition.style.visibility = "visible";
        } else {
            trCantonImposition.style.visibility = "hidden";
        }
    }
    function isDayPositiveNumber(){
        var nbJourSoldeInput = $('#nbJourSolde').val();
        var jourSupplementaireInput = $('#jourSupplementaire').val();

        if(nbJourSoldeInput <0|| jourSupplementaireInput<0 ){
            return false;
        }else{
            return true;
        }
    }

    function addPeriodePatP() {
        var nbJourSoldeTableau = 0
        var periodeConsecutive = 0
        var nbJourSuppTableau = 0
        $("#periodes .nbJourPourUnePeriode").each(function (index, element) {
            if ($(element).text()) {
                nbJourSoldeTableau = nbJourSoldeTableau + ($(element).text() * 1);
                if (($(element).text() * 1) >= 5) {
                    periodeConsecutive += 1;
                }
            }
        });

        $("#periodes .ndJourSup").each(function(index, element) {
            if ($(element).text()) {
                nbJourSuppTableau = nbJourSuppTableau + ($(element).text() * 1);
            }
        });

        // initialise les valeures selon les périodes du calendrier
        var dateDebut = Date.toDate($('#dateDebutPeriode').val());
        var dateFin = Date.toDate($('#dateFinPeriode').val());
        var nbJourSoldePeriode = dateDebut.daysBetween(dateFin);

        var nbJourSoldesActuel;
        var nbJourSoldeChamp =  $('#nbJourSolde').val();
        if (nbJourSoldeChamp) {
            nbJourSoldesActuel = Number(nbJourSoldeChamp);
        } else{
            nbJourSoldesActuel = Number(nbJourSoldePeriode);
        }

        var nbJourSoldeTot = Number(nbJourSoldeTableau) + Number(nbJourSoldesActuel);

        // On calcul le nombre de jours supplémentaires
        var nbJourSuppChamp = $('#jourSupplementaire').val();
        var nbJourSuppActuel = '';
        if (nbJourSuppChamp) {
            nbJourSuppActuel = Number(nbJourSuppChamp);
        } else {
            // Si nombre de jours actuel est +5 et qu'il y a -4 jours supp
            if (nbJourSoldesActuel >= 5 && nbJourSuppTableau < 4) {
                nbJourSuppActuel = 2;
            }
            // Si nombre de jours tot est +5 jours et qu'il y a -2 jours supp
            if (nbJourSoldeTot >= 5 && nbJourSuppTableau < 2) {
                nbJourSuppActuel = 2;
            }
            // Si nombre de jours tot est +10 jours et qu'il y a -4 jours supp
            if (nbJourSoldeTot >= 10 && nbJourSuppTableau < 4) {
                nbJourSuppActuel = 2;
            }
            // Si nombre de jours tot est +10 jours et qu'il y a -2 jours supp
            if (nbJourSoldeTot >= 10 && nbJourSuppTableau < 2) {
                nbJourSuppActuel = 4;
            }
            // Si nombre de jours actuel est +10 et qu'il y a -2 jours supp
            if (nbJourSoldesActuel >= 10 && nbJourSuppTableau < 2) {
                nbJourSuppActuel = 4;
            }
        }

        // On met à jour le champ jourSupplementaire avec la valeur calculée
        $('#jourSupplementaire').val(nbJourSuppActuel);

        // On ajoute les jours supplémentaires au jours soldes pour trouver le nb jour soldes total
        if (nbJourSuppActuel) {
            if (nbJourSoldeTot === 7 || nbJourSoldeTot === 14 || nbJourSoldesActuel === 7) {
                nbJourSoldeTot -= nbJourSuppActuel;
            } else {
                nbJourSoldeTot += nbJourSuppActuel;
            }
        }
        nbJourSoldeTot += nbJourSuppTableau;

        // On définit dateFinCalculee avec la date la plus éloignée du tableau ou le champs "période au"
        $('#dateFinCalculee').val(resolveDateFinCalculee(nbJourSoldeTot));

        /* Contrôle que la date fin période n'est pas éloigné de plus de 6 mois (délai cadre) de la date de naissance */
        if (isDelaiCadreDepasse(dateFin)) {
            var text = "<%=viewBean.getSession().getLabel("ERREUR_DELAI_CADRE_APRES_DATE_NAI")%>";
            showErrorMessage(text);
            return;
        }

        /* Contrôle que les champs jours supplémentaires et jours de congées sont dans les limites autorisées */
        if (isChampsHorsLimites(nbJourSuppChamp, nbJourSoldeChamp)) {
            var text = "<%=viewBean.getSession().getLabel("ERREUR_SAISIES_DANS_LES_LIMITES")%>";
            showErrorMessage(text);
            return;
        }

        /* Contrôle que le nombre total de jours de congées + le nombre de jours supplémentaires ne dépasse pas la maximum autorisé */
        if (isNbJourPlusGrandQueJourMax(nbJourSoldeTot, 14)) {
            var text = "<%=viewBean.getSession().getLabel("ERREUR_NB_JOURS_PLUS_GRAND_QUE_JOUR_MAX")%>";
            showErrorMessage(text);
            return;
        }
        // Contrôle que le nombre de jours de congé et l'indemnité supplémentaire sont négatifs.
        if(!isDayPositiveNumber()){
            var text = "<%=viewBean.getSession().getLabel("ERROR_PATERNITE_JOUR_NEGATIF")%>";
            showErrorMessage(text);
            return;
        }

        // Si on arrive jusqu'ici tous les contrôles sont passés et on peut ajouter la période
        addPeriode();
    }


    $(document).ready(function () {

        $('#btnUpd').click(function () {
            EDITION_MODE = true;
            $('#modeEditionDroit').val('<%=APModeEditionDroit.EDITION%>');
            repaintTablePeriodes();
            $('#nbJourSolde').prop("disabled", false);
            $('#jourSupplementaire').prop("disabled", false);
            $('#dateFinCalculee').prop("disabled", true);
            $('#isSoumisCotisation').prop("disabled", true);
            $('#tauxImpotSource').prop("disabled", true);
        });

        <%
        //Ajout des périodes en cas de relecture de données
        for(PRPeriode periode : viewBean.getPeriodes()){
            %>
        var ddd = '<%=periode.getDateDeDebut()%>';
        var ddf = '<%=periode.getDateDeFin() %>';
        var ndj = '<%=periode.getNbJour() %>';
        var tis = '<%=periode.getTauxImposition() %>';
        var cis = '<%=periode.getCantonImposition() %>';
        var cisLibelle = '<%=objSession.getCodeLibelle(periode.getCantonImposition()) %>';
        var nbJourSup = '<%=periode.getNbJoursupplementaire() %>';
        addPeriodeToTable(ddd, ddf, ndj, tis, cis, cisLibelle, nbJourSup);
        <%
    }%>
    });

</script>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<ct:FWLabel key="JSP_TITRE_SAISIE_PAT_1"/>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<tr>
    <td>
        <label for="idGestionnaire">
            <ct:FWLabel key="JSP_GESTIONNAIRE"/>
        </label>
    </td>
    <td colspan="5">
        <ct:FWListSelectTag name="idGestionnaire"
                            data="<%=viewBean.getResponsableData()%>"
                            defaut="<%=viewBean.getIdGestionnaire()%>"/>
    </td>
</tr>
<tr>
    <td colspan="6">
        <hr/>
    </td>
</tr>
<tr>
    <td colspan="6">
        <h6>
            <ct:FWLabel key="JSP_TIERS"/>
        </h6>
    </td>
</tr>
<!--Gestion du NSS -->
<tr>
    <td>
        <label for="partiallikeNSS">
            <ct:FWLabel key="JSP_NSS_ABREGE"/>
        </label>
    </td>
    <td colspan="4">
        <%
            String params = "&provenance1=TIERS&provenance2=CI";
            String jspLocation = servletContext + "/ijRoot/numeroSecuriteSocialeSF_select.jsp";
        %> <ct1:nssPopup name="likeNSS"
                         onFailure="nssFailure();"
                         onChange="nssChange(tag);"
                         params="<%=params%>"
                         value="<%=viewBean.getNumeroAvsFormateSansPrefixe()%>"
                         newnss="<%=viewBean.isNNSS()%>"
                         jspName="<%=jspLocation%>"
                         avsMinNbrDigit="3"
                         nssMinNbrDigit="3"
                         avsAutoNbrDigit="11"
                         nssAutoNbrDigit="10"/>
        <input type="text"
               name="nomPrenom"
               value="<%=viewBean.getNom()%> <%=viewBean.getPrenom()%>"
               class="libelleLongDisabled"/>
        <span id="linkTiers">/&nbsp;<A
                href="<%=request.getContextPath()%>\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=<%=viewBean.getIdAssure()%>"><ct:FWLabel
                key="JSP_TIERS"/></A></span>
        <input type="hidden"
               name="nss"
               id="nss"
               value="<%=viewBean.getNss()%>"/>
        <input type="hidden"
               name="idAssure"
               value="<%=viewBean.getIdAssure()%>"/>
        <input type="hidden"
               name="provenance"
               value="<%=viewBean.getProvenance()%>"/>
        <input type="hidden"
               id="idDroit"
               name="idDroit"
               value="<%=viewBean.getIdDroit()%>"/>
        <input type="hidden"
               name="<%=APAbstractDroitDTOAction.PARAM_ID_DROIT%>"
               value="<%=viewBean.getIdDroit()%>"/>
        <input type="hidden"
               name="periodesAsString"
               id="periodesAsString"
               value="<%=viewBean.getIdDroit()%>"/>
        <input type="hidden"
               name="modeEditionDroit"
               id="modeEditionDroit"
               value="<%=viewBean.getModeEditionDroit()%>"/>
        <input type="hidden"
               name="aControler"
               id="aControler"
               value="<%=viewBean.getAControler()%>"/>
        <input type="hidden"
               name="checkWarn"
               id="checkWarn"
               value="<%=viewBean.getCheckWarn()%>"/>
    </td>
    <td></td>
</tr>
<tr>
    <td>
        <label for="nomAffiche">
            <ct:FWLabel key="JSP_NOM"/>
        </label>
    </td>
    <td>
        <input type="hidden"
               name="nom"
               value="<%=viewBean.getNom()%>"/>
        <input type="text"
               id="nomAffiche"
               name="nomAffiche"
               value="<%=viewBean.getNom()%>"/>
    </td>
    <td>
        <label for="prenomAffiche">
            <ct:FWLabel key="JSP_PRENOM"/>
        </label>
    </td>
    <td colspan="2">
        <input type="hidden"
               name="prenom"
               value="<%=viewBean.getPrenom()%>"/>
        <input type="text"
               id="prenomAffiche"
               name="prenomAffiche"
               value="<%=viewBean.getPrenom()%>"/>
    </td>
    <td></td>
</tr>
<tr>
    <td>
        <label for="csSexeAffiche">
            <ct:FWLabel key="JSP_SEXE"/>
        </label>
    </td>
    <td colspan="4">
        <ct:FWCodeSelectTag name="csSexeAffiche"
                            wantBlank="<%=true%>"
                            codeType="PYSEXE"
                            defaut="<%=viewBean.getCsSexe()%>"/>
        <input type="hidden"
               name="csSexe"
               value="<%=viewBean.getCsSexe()%>"/>
    </td>
    <td></td>
</tr>
<tr>
    <td>
        <label for="csEtatCivilAffiche">
            <ct:FWLabel key="JSP_ETAT_CIVIL"/>
        </label>
    </td>
    <td>
        <ct:FWCodeSelectTag name="csEtatCivilAffiche"
                            wantBlank="<%=true%>"
                            codeType="PYETATCIVI"
                            defaut="<%=viewBean.getCsEtatCivil()%>"/>
        <input type="hidden"
               name="csEtatCivil"
               id="csEtatCivil"
               value="<%=viewBean.getCsEtatCivil()%>"/>
    </td>
    <td>
        <label for="dateNaissanceAffiche">
            <ct:FWLabel key="JSP_DATE_NAISSANCE"/>
        </label>
    </td>
    <td colspan="2">
        <input type="hidden"
               name="dateNaissance"
               value="<%=viewBean.getDateNaissance()%>"/>
        <input type="text"
               id="dateNaissanceAffiche"
               name="dateNaissanceAffiche"
               data-g-calendar=" "
               value="<%=viewBean.getDateNaissance()%>"/>
    </td>
    <td></td>
</tr>
<tr>
    <td>
        <label for="npa">
            <ct:FWLabel key="JSP_NPA"/>
        </label>
    </td>
    <td>
        <input type="text"
               name="npa"
               id="npa"
               value="<%=viewBean.getNpa()%>"
               class="numero"/>
    </td>
    <td>
        <label for="pays">
            <ct:FWLabel key="JSP_PAYS_DOMICILE"/>
        </label>
    </td>
    <td colspan="2">
        <ct:FWListSelectTag name="pays"
                            data="<%=viewBean.getTiPays()%>"
                            defaut="<%=JadeStringUtil.isIntegerEmpty(viewBean.getPays()) ? TIPays.CS_SUISSE : viewBean.getPays()%>"/>
        <script type="text/javascript">
            document.getElementById("likeNSS").onkeypress = new Function("", "return filterCharForPositivFloat(window.event);");
        </script>
    </td>
    <td></td>
</tr>
<tr>
    <td colspan="6">
        <br>
    </td>
</tr>
<tr>
    <td colspan="6">
        <h6>
            <ct:FWLabel key="JSP_PERIODES"/>
        </h6>
    </td>
</tr>
<tr>
    <td colspan="6">
        <table width="100%">
            <tr>

                <td>
                    <label for="dateDebutPeriode">
                        <ct:FWLabel key="JSP_PERIODE_DU"/>
                    </label>
                </td>
                <td colspan="3">
                    <input type="text"
                           id="dateDebutPeriode"
                           name="dateDebutPeriode"
                           data-g-calendar=" "
                           onChange="periodeChange();"
                           value=""/>
                    <label for="dateFinPeriode">
                        <ct:FWLabel key="JSP_AU"/>
                    </label>
                    <input type="text"
                           id="dateFinPeriode"
                           name="dateFinPeriode"
                           data-g-calendar=" "
                           onChange="periodeChange();"
                           value=""/>
                    <input type="hidden"
                           name="nbJour"
                           id="nbJour"
                           value=""
                           class="numero"/>
                    <input type="button"
                           name=""
                           value="<ct:FWLabel key="JSP_AJOUTER" />"
                           onclick="addPeriodePatP()"/>
                </td>
                <td colspan="2" rowspan="4" width="50%">
                    <table class="areaTable" width="100%">
                        <thead>
                        <tr>
                            <th width="30%">
                                <ct:FWLabel key="DATE_DE_DEBUT"/>
                            </th>
                            <th width="30%">
                                <ct:FWLabel key="DATE_DE_FIN"/>
                            </th>
                            <th width="10%">
                                <ct:FWLabel key="JSP_NB_JOURS_CONGES"/>
                            </th>
                            <th width="10%">
                                <ct:FWLabel key="JSP_INDEMNITE_SUPPLEMENTAIRES"/>
                            </th>
                            <th width="10%">
                                <ct:FWLabel key="MENU_OPTION_TAUX_IMPOSITIONS_RACC"/>
                            </th>
                            <th width="10%">
                                <ct:FWLabel key="JSP_CANTON_IMPOT_SOURCE_RACC"/>
                            </th>
                            <th width="10%"></th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                    <div style="height:120px; overflow-y:scroll; width:100%; background-color:#FFF; margin-left: 3px;">
                        <table id="periodes" name=periode" class="areaTable" width="100%">
                            <thead>
                            <tr style="height: 0px;">
                                <th width="25%"></th>
                                <th width="25%"></th>
                                <th width="10%"></th>
                                <th width="10%"></th>
                                <th width="10%"></th>
                                <th width="10%"></th>
                                <th width="10%"></th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <label for="nbJourSolde">
                        <ct:FWLabel key="JSP_NB_JOURS_CONGES"/>
                    </label>
                </td>
                <td colspan="3">
                    <input type="text"
                           data-g-integer="sizeMax:2"
                           size="5"
                           id="nbJourSolde"
                           name="nbJourSolde"
                    />

                    <label style="margin-left:40px " for="jourSupplementaire">
                        <ct:FWLabel key="JSP_INDEMNITE_SUPPLEMENTAIRES"/>
                    </label>
                    <input type="text"
                           data-g-integer="sizeMax:2"
                           size="5"
                           id="jourSupplementaire"
                           name="jourSupplementaire"
                    />
                </td>
            </tr>
            <tr>
                <td>
                    <label for="dateFinCalculee">
                        <ct:FWLabel key="JSP_DATE_FIN_CALCULEE"/>
                    </label>

                </td>
                <td colspan="3">
                    <input type="text"
                            data-g-calendar=" "
                            value="<%=viewBean.getDateFinCalculee()%>"
                            id="dateFinCalculee"
                            name="dateFinCalculee"
                    />
                </td>
            </tr>
            <tr>
                <td>
                    <label for=isSoumisCotisation">
                        <ct:FWLabel key="JSP_SOUMIS_IMPOT_SOURCE"/>
                    </label>
                </td>
                <td colspan="3">
                    <input type="checkbox"
                           id="isSoumisCotisation"
                           name="isSoumisCotisation"
                            <%=viewBean.getIsSoumisCotisation().booleanValue() ? "checked" : ""%>
                           onclick="showCantonImpotSource();"
                           onload="showCantonImpotSource()"
                    periode="true"/>
                    <input type="hidden"
                           id="isSoumisCotisationPeriode"
                           value="true"/>
                </td>
            </tr>
            <tr>
                <td>
                    <label for=tauxImpotSource">
                        <ct:FWLabel key="JSP_TAUX_IMPOT_SOURCE_CARTE"/>
                    </label>
                </td>
                <td colspan="3">
                    <input type="text"
                           id="tauxImpotSource"
                           name="tauxImpotSource"
                           value="<%=viewBean.getTauxImpotSource()%>"
                           class="numero"
                           onchange="validateFloatNumber(this);"
                           onkeypress="return filterCharForFloat(window.event);"
                           style="text-align: right"/>
                </td>
            </tr>
            <tr id="availableIfSoumisCotisation">
                <TD>
                </TD>
                <TD><LABEL for="csCantonImpoAffiche"><ct:FWLabel key="JSP_CANTON_IMPOT_SOURCE"/></LABEL></TD>
                <TD>
                    <ct:FWCodeSelectTag name="csCantonDomicileAffiche"
                                        wantBlank="<%=false%>"
                                        codeType="PYCANTON"
                                        defaut="<%=viewBean.getCsCantonDomicile()%>"/>
                    <INPUT type="hidden" name="csCantonDomicile" value="<%=viewBean.getCsCantonDomicile()%>"/>
                </TD>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td colspan="6">
        <ht/>
    </td>
</tr>
<tr>
    <td colspan="6">
        <h6>
            <ct:FWLabel key="JSP_DROIT"/>
        </h6>
    </td>
</tr>
<tr>
<tr>
    <td>
        <label for="dateDebutDroit">
            <ct:FWLabel key="JSP_DATE_DEBUT_PAT_NAISSANCE"/>
        </label>
    </td>
    <td>
        <input type="text"
               id="dateDebutDroit"
               name="dateDebutDroit"
               onChange="checkDateDebutAPGNaissance(dateDebutDroit.value);"
               data-g-calendar=" "
               value="<%=viewBean.getDateDebutDroit()%>"/>
    </td>
    <td>
        <label for="dateDecesAffiche">
            <ct:FWLabel key="JSP_DATE_DECES"/>
        </label>
    </td>
    <td colspan="2">
        <input type="hidden"
               name="dateDeces"
               value="<%=viewBean.getDateDeces()%>"/>
        <input type="text"
               id="dateDecesAffiche"
               name="dateDecesAffiche"
               data-g-calendar=" "
               value="<%=viewBean.getDateDeces()%>"/>
    </td>
    <td></td>
</tr>
<tr>
    <td>
        <label for="dateDepot">
            <ct:FWLabel key="JSP_DATE_DEPOT"/>
        </label>
    </td>
    <td>
        <input type="text"
               id="dateDepot"
               name="dateDepot"
               data-g-calendar=" "
               value="<%=viewBean.getDateDepot()%>"/>
    </td>
    <td>
        <label for="dateReception">
            <ct:FWLabel key="JSP_DATE_RECEPTION"/>
        </label>
    </td>
    <td colspan="2">
        <input type="text"
               id="dateReception"
               name="dateReception"
               data-g-calendar=" "
               value="<%=viewBean.getDateReception()%>"/>
    </td>
    <td></td>
</tr>
<tr>
    <td colspan="6">
        &nbsp;
    </td>
</tr>
<tr>
    <td colspan="6">
        &nbsp;
    </td>
</tr>
<tr>
    <td>
        <label for="droitAcquis">
            <ct:FWLabel key="JSP_DROIT_ACQUIS"/>
        </label>
    </td>
    <td>
        <input type="text"
               id="droitAcquis"
               name="droitAcquis"
               class="libelle"
               onchange="validateFloatNumber(this);"
               onkeypress="return filterCharForFloat(window.event);"
               style="text-align: right"
               value="<%=JANumberFormatter.fmt(viewBean.getDroitAcquis(), true, true, false, 2)%>"/>
    </td>
    <td>
        <label for="csProvenanceDroitAcquis">
            <ct:FWLabel key="JSP_PROVENANCE_DROIT"/>
        </label>
    </td>
    <td colspan="2">
        <ct:select name="csProvenanceDroitAcquis"
                   wantBlank="true"
                   defaultValue="<%=viewBean.getCsProvenanceDroitAcquis()%>">
            <ct:optionsCodesSystems csFamille="<%=IAPDroitAPG.GROUPE_CS_PROVENANCE_DROIT_ACQUIS%>">
            </ct:optionsCodesSystems>
        </ct:select>
    </td>
    <td></td>
</tr>
<tr>
    <td colspan="6">
    </td>
</tr>
<tr>
    <td>
        <label for="reference">
            <ct:FWLabel key="JSP_REFERENCE"/>
        </label>
    </td>
    <td colspan="4">
        <input type="text"
               id="reference"
               name="reference"
               value="<%=viewBean.getReference()%>"
               class="libelleLong"/>
        <!-- champs relatifs à la demande -->
        <input type="hidden"
               name="idDemande"
               value="<%=viewBean.getIdDemande()%>"/>
        <input type="hidden"
               name="arreter"
               value=""/>
    </td>
    <td></td>
</tr>
<tr>
    <td>
        <label for="remarque">
            <ct:FWLabel key="JSP_REMARQUE"/>
        </label>
    </td>
    <td colspan="4"><textarea id="remarque" name="remarque" cols="85" rows="3"
                              onkeydown="limiteur();"><%=viewBean.getRemarque()%></textarea>
        <br/>
        <ct:FWLabel key="JSP_REMARQUE_COMMENT_8000"/>
    </td>
    <td></td>
</tr>
<% if (viewBean.hasMessagePropError()) { %>
<div style="display:none" align="center" id="dialog_apg_webservice"
     title="<ct:FWLabel key='JSP_CONTROLE_SERVICE'/>">
    <% if (StringUtils.isNotEmpty(viewBean.getMessagesError())) { %>
    <h3><%=viewBean.getMessagesError()%>
    </h3>
    <% } %>
</div>
<% } %>

<% if (viewBean.hasMessageWarn() && viewBean.getMessage().isEmpty()) { %>
<div style="display:none;vertical-align:middle" id="dialog_apg_warn"
     title="<ct:FWLabel key='JSP_WARN'/>">
</div>
<% } %>

<%@ include file="plausibilites.jsp" %>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<input type="button"
       value="<ct:FWLabel key="JSP_ARRET" /> (alt+<ct:FWLabel key="AK_MATERNITE_ARRET" />)"
       onclick="arret()"
       accesskey="<ct:FWLabel key="AK_MATERNITE_ARRET" />"/>
<input type="button"
       value="<ct:FWLabel key="JSP_SUIVANT" /> (alt+<ct:FWLabel key="AK_MATERNITE_SUIVANT" />)"
       onclick="validate()"
       accesskey="<ct:FWLabel key="AK_MATERNITE_SUIVANT" />"/>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
