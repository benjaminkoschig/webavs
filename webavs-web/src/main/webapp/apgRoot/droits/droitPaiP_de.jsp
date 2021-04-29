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
<%@ page import="globaz.prestation.beans.PRPeriode" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="globaz.apg.vb.droits.APDroitPaiPViewBean" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>

<%@ include file="/theme/detail/header.jspf" %>
<%
    idEcran = "PAP0013";

    APDroitPaiPViewBean viewBean = (APDroitPaiPViewBean) session.getAttribute("viewBean");
    selectedIdValue = viewBean.getIdDroit();
    viewBean.setAControler(false);
    viewBean.setCheckWarn(false);
    bButtonUpdate = viewBean.isModifiable() && bButtonUpdate;
    bButtonValidate = false;
    bButtonCancel = false;
    bButtonDelete = false;
%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/apgRoot/droits/droitPeriodeUtils.js"></script>
<script type="text/javascript" src="<%=servletContext%>/apgRoot/droits/enfantUtils.js"></script>
<script type="text/javascript" src="<%=servletContext%>/apgRoot/scripts/apgUtils.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/dates.js"></script>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalprai" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ap-optionsempty"/>

<script type="text/javascript">
    var JOUR_SUPPLEMENTAIRE = true;
    var EDITION_MODE = false;
    <%if(viewBean.getModeEditionDroit().equals(APModeEditionDroit.CREATION) || viewBean.getModeEditionDroit().equals(APModeEditionDroit.EDITION) ){%>
    EDITION_MODE = true;
    <%}%>

    var ACTION_DROIT = "<%=IAPActions.ACTION_SAISIE_CARTE_PAI%>";
    var jsonAnnonce;

    function add() {
        nssUpdateHiddenFields();
        document.forms[0].elements('userAction').value = ACTION_DROIT + ".ajouter"
    }

    function upd() {
        document.getElementById("nomAffiche").disabled = true;
        document.getElementById("prenomAffiche").disabled = true;
        document.getElementById("dateNaissanceAffiche").disabled = true;
        document.getElementById("csEtatCivilAffiche").disabled = true;
        document.getElementById("csSexeAffiche").disabled = true;
        $('#isSoumisCotisation').prop("disabled", true);
        $('#tauxImpotSource').prop("disabled", true);
    }

    function validate() {
        var isModifiable = <%=viewBean.isModifiable()%>;
        $('#button_suivant').prop("disabled", true);
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
        } else {
            $('#button_suivant').prop("disabled", false);
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

    function checkDateDebutAPG(){

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
            addPeriode();
        }
        // Si aucune période n'est renseigné -> message d'erreurs
        if (periodes.lenght == 0) {
            showErrorMessage("Aucune période n'est renseignée");
            return true;
        }
        <%if(viewBean.getModeEditionDroit().equals(APModeEditionDroit.CREATION)){%>
        document.forms[0].elements('userAction').value = ACTION_DROIT + ".ajouter";
        <%} else if(viewBean.getModeEditionDroit().equals(APModeEditionDroit.EDITION)){%>
        document.forms[0].elements('userAction').value = ACTION_DROIT + ".modifier";
        <%} else if(viewBean.getModeEditionDroit().equals(APModeEditionDroit.LECTURE)){%>
        if (EDITION_MODE == true) {
            document.forms[0].elements('userAction').value = ACTION_DROIT + ".modifier";
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

    function cancel() {
        if (document.forms[0].elements('_method').value == "add") {
            document.forms[0].elements('userAction').value = "back";
        } else {
            document.forms[0].elements('userAction').value = ACTION_DROIT + ".afficher";
        }
    }

    function del() {
        if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")) {
            document.forms[0].elements('userAction').value = ACTION_DROIT + ".supprimer";
            document.forms[0].submit();
        }
    }

    function init() {
        if (<%=viewBean.getIsSoumisCotisation()%>) {
            document.getElementById("isSoumisCotisation").checked = true;
        }
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
                    if (<%=viewBean.getModeEditionDroit().equals(APModeEditionDroit.CREATION)%>) {
                        $('#modeEditionDroit').val('<%=APModeEditionDroit.CREATION%>');
                        document.forms[0].elements('_method').value = "add";
                    }
                    if (<%=viewBean.getModeEditionDroit().equals(APModeEditionDroit.EDITION)%>) {
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
                    if (<%=viewBean.getModeEditionDroit().equals(APModeEditionDroit.CREATION)%>) {
                        $('#modeEditionDroit').val('<%=APModeEditionDroit.CREATION%>');
                        document.forms[0].elements('_method').value = "add";
                    }
                    $(this).dialog("close");
                }
            }],
            open: function () {
                $(".ui-dialog-titlebar-close", ".ui-dialog-titlebar").hide();
                <% if(viewBean.hasMessageWarn()) { %>
                $('#dialog_apg_warn').append("<%=viewBean.getMessagesWarn()%>");
                $('#dialog_apg_warn').prop("vertical-align", "middle");
                <% } %>
            }
        });
    }

    function arret() {
        nssUpdateHiddenFields();
        document.forms[0].elements('userAction').value = ACTION_DROIT + ".arreterEtape1";
        document.forms[0].elements('arreter').value = "on";
        document.forms[0].submit();
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
        $('#isSoumisCotisation').prop("disabled", true);
        $('#tauxImpotSource').prop("disabled", true);
    }

    function periodeChange() {
        var dateDebut = $('#dateDebutPeriode').val();
        var dateFin = $('#dateFinPeriode').val();
        var impot = $('#isSoumisCotisation');
        var taux = $('#tauxImpotSource');

        if (impot.is(':disabled')
            && dateDebut != ''
            && dateFin != '') {
            impot.prop("disabled", false);
            taux.prop("disabled", false);
        } else if (!impot.is(':disabled')
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
        document.getElementById("csEtatCivilAffiche").disabled = false;
        document.getElementById("csSexeAffiche").disabled = false;
        checkNss(param_nss);
    }

    function nssUpdateHiddenFields() {
        document.getElementById("nom").value = document.getElementById("nomAffiche").value;
        document.getElementById("prenom").value = document.getElementById("prenomAffiche").value;
        document.getElementById("dateNaissance").value = document.getElementById("dateNaissanceAffiche").value;
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

    function addPeriodePai(){
        var sum = 0
        $("#periodes .nbJourPourUnePeriode").each(function(index, element){
            if($(element).text()) {
                sum = sum + ($(element).text() * 1)
            }
        });

        var nbJourSup = $('#jourSupplementaire').val()
        if(nbJourSup){
            sum = sum+nbJourSup*1;
        }

        var dateDebut = Date.toDate($('#dateDebutPeriode').val());
        var dateFin = Date.toDate($('#dateFinPeriode').val());
        var nbJour = dateDebut.daysBetween(dateFin);

        if(sum+nbJour > dateDebut.daysInMonth()) {
            globazNotation.utils.consoleError("<ct:FWLabel key="JSP_NBJOUR_SUP_MOIS"/>", "<ct:FWLabel key="JSP_NBJOUR_SUP_MOIS_TITRE"/>");
        } else {
            addPeriode()
        }
    }

    $(document).ready(function () {
        $('#btnUpd').click(function () {
            EDITION_MODE = true;
            $('#modeEditionDroit').val('<%=APModeEditionDroit.EDITION%>');
            repaintTablePeriodes();
            $('#isSoumisCotisation').prop("disabled", true);
            $('#tauxImpotSource').prop("disabled", true);
        });

        <%
        //Ajout des périodes en cas de relecture de données
        for(PRPeriode periode : viewBean.getPeriodes()){
            %>
        var ddd = '<%=periode.getDateDeDebut()%>';
        var ddf = '<%=periode.getDateDeFin() %>';
        var ndj = '<%=periode.getNbJour()%>';
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
<ct:FWLabel key="JSP_TITRE_SAISIE_PAI_1"/>
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
               data-g-calendar="mandatory:false"
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
                           data-g-calendar="mandatory:false"
                           onChange="periodeChange();"
                           value=""/>
                    <label for="dateFinPeriode">
                        <ct:FWLabel key="JSP_AU"/>
                    </label>
                    <input type="text"
                           id="dateFinPeriode"
                           name="dateFinPeriode"
                           data-g-calendar="mandatory:false"
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
                           onclick="addPeriodePai()"/>
                </td>
                <td colspan="2" rowspan="4" width="50%">
                    <table class="areaTable" width="100%">
                        <thead>
                        <tr>
                            <th width="25%">
                                <ct:FWLabel key="DATE_DE_DEBUT"/>
                            </th>
                            <th width="25%">
                                <ct:FWLabel key="DATE_DE_FIN"/>
                            </th>
                            <th width="10%">
                                <ct:FWLabel key="JSP_NB_JOURS_SOLDES"/>
                            </th>
                            <th width="10%">
                                <ct:FWLabel key="JSP_JOURS_SUPPL"/>
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
                    <label for="jourSupplementaire">
                        <ct:FWLabel key="JSP_JOURS_SUPPLEMENTAIRES"/>
                    </label>
                </td>
                <td colspan="3">
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
                           onload="showCantonImpotSource()"/>
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
            <ct:FWLabel key="JSP_DATE_DEBUT"/>
        </label>
    </td>
    <td>
        <input type="text"
               id="dateDebutDroit"
               name="dateDebutDroit"
               data-g-calendar="mandatory:true"
               value="<%=viewBean.getDateDebutDroit()%>"/>
    </td>
    <td>
        <label for="dateMax">
            <ct:FWLabel key="JSP_DELAI_CADRE"/>
        </label>
    </td>
    <td>
        <input type="text"
               size="11"
               disabled="disabled"
               readonly="readonly"
               id="dateMax"
               name="dateMax"
               value="<%=viewBean.getDelaiCadre()%>"/>
    </td>
    <td>
        <label for="dateFinDroit">
            <ct:FWLabel key="JSP_DATE_FIN_DROIT"/>
        </label>
    </td>
    <td>
        <input type="text"
               id="dateFinDroit"
               name="dateFinDroit"
               data-g-calendar=" "
               value="<%=viewBean.getDateFinDroit()%>"/>
    </td>
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
    <td>
        <label for="dateReception">
            <ct:FWLabel key="JSP_NOMBRE_JOURS_COMPTABILISE"/>
        </label>
    </td>
    <td>
        <input type="text"
               size="5"
               disabled="disabled"
               readonly="readonly"
               id="nbTotalJourPayes"
               name="nbTotalJourPayes"
               value="<%=viewBean.calculerNbjourTotalIndemnise()%>"/>
    </td>
    <td>
        <label for="nbJourDisponible">
            <ct:FWLabel key="JSP_NB_JOUR_DISPONIBLE"/>
        </label>

    </td>
    <td>
        <input type="text"
               size="11"
               disabled="disabled"
               readonly="readonly"
               id="nbJourDisponible"
               name="nbJourDisponible"
               value="<%=viewBean.calculerNbJourDisponible()%>"/>
    </td>
    <td colspan="" ></td>
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
    <td colspan="4">
        <textarea data-g-string="sizeMax:100,isPasteOverload:true" id="remarque" name="remarque" cols="85" rows="3">
            <%=viewBean.getRemarque()%>
        </textarea>
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
       id="button_suivant"
       value="<ct:FWLabel key="JSP_SUIVANT" /> (alt+<ct:FWLabel key="AK_MATERNITE_SUIVANT" />)"
       onclick="validate()"
       accesskey="<ct:FWLabel key="AK_MATERNITE_SUIVANT" />"/>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>
