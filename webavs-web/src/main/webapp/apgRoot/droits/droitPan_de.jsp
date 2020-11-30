<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.apg.api.droits.IAPDroitAPG"
         contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.apg.api.droits.IAPDroitLAPG" %>
<%@ page import="globaz.apg.api.droits.IAPDroitMaternite" %>
<%@ page import="globaz.apg.enums.APModeEditionDroit" %>
<%@ page import="globaz.apg.servlet.APAbstractDroitDTOAction" %>
<%@ page import="globaz.apg.servlet.IAPActions" %>
<%@ page import="globaz.apg.vb.droits.APDroitPanViewBean" %>
<%@ page import="globaz.globall.util.JANumberFormatter" %>
<%@ page import="globaz.prestation.beans.PRPeriode" %>
<%@ page import="globaz.prestation.interfaces.util.nss.PRUtil" %>
<%@ page import="globaz.prestation.tools.PRCodeSystem" %>
<%@ page import="globaz.pyxis.db.adressecourrier.TIPays" %>
<%@ page import="java.util.HashSet" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ include file="/theme/detail/header.jspf" %>

<%
    idEcran = "PAP0004";

    APDroitPanViewBean viewBean = (APDroitPanViewBean) session.getAttribute("viewBean");
    HashSet exceptGenreService = new HashSet();
    exceptGenreService.add(IAPDroitLAPG.CS_ALLOCATION_DE_MATERNITE);

    HashSet exceptRevision = new HashSet();
    exceptRevision.add(IAPDroitMaternite.CS_REVISION_MATERNITE_2005);

    selectedIdValue = viewBean.getIdDroit();

    java.util.List cucsGenreService = PRCodeSystem.getCUCS(viewBean.getSession(), IAPDroitLAPG.CS_GROUPE_GENRE_SERVICE_APG);

    bButtonUpdate = viewBean.isModifiable() && bButtonUpdate;
    bButtonValidate = false;
    bButtonCancel = false;
    bButtonDelete = false;

%>


<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"/>
<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/ajaxUtils.js"/>
<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/JSON.js"/>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalpan" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ap-optionsempty"/>


<script type="text/javascript">

    var EDITION_MODE = false;
    <%if(viewBean.getModeEditionDroit().equals(APModeEditionDroit.CREATION) || viewBean.getModeEditionDroit().equals(APModeEditionDroit.EDITION) ){%>
    EDITION_MODE = true;
    <%}%>


    function add() {
        nssUpdateHiddenFields();
    }

    function upd() {
        document.getElementById("nomAffiche").disabled = true;
        document.getElementById("prenomAffiche").disabled = true;
        document.getElementById("dateNaissanceAffiche").disabled = true;
        document.getElementById("csEtatCivilAffiche").disabled = true;
        document.getElementById("csSexeAffiche").disabled = true;
    }

    function validate() {
        if (document.forms[0].elements('_method').value === "read") {
            if (document.forms[0].elements('genreService').value === "<%=IAPDroitLAPG.CS_GARDE_PARENTALE%>"
                || document.forms[0].elements('genreService').value === "<%=IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP%>"
                || document.forms[0].elements('genreService').value === "<%=IAPDroitLAPG.CS_GARDE_PARENTALE_17_09_20%>"
                || document.forms[0].elements('genreService').value === "<%=IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP_17_09_20%>") {
                document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_ENFANT_PAN%>.chercher";
            } else {
                document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_SAISIE_CARTE_PAN_SITUATION%>.afficher";
            }
            action(COMMIT);
        } else {
            nssUpdateHiddenFields();
            //Récupération des dates
            // si l'utilisateur à saisit des valeurs pour les priodes sans cliquer le btn ajouter, on lui fit à sa place
            var dateDebut = $('#dateDebutPeriode').val();
            var dateFin = $('#dateFinPeriode').val();
            // Si au moins un des 3 champs n'est pas vide
            if (dateDebut || dateFin) {
                addPeriode();
            }
            // Si aucune période n'est renseigné -> message d'erreurs
            if (periodes.lenght == 0) {
                showErrorMessage("Aucune période n'est renseignée");
                return;
            }

            <%if(viewBean.getModeEditionDroit().equals(APModeEditionDroit.CREATION)){%>
            document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_SAISIE_CARTE_PAN%>.ajouter";
            <%} else if(viewBean.getModeEditionDroit().equals(APModeEditionDroit.EDITION)){%>
            document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_SAISIE_CARTE_PAN%>.modifier";
            <%} else if(viewBean.getModeEditionDroit().equals(APModeEditionDroit.LECTURE)){%>
            if (EDITION_MODE) {
                document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_SAISIE_CARTE_PAN%>.modifier";
            } else {
                if (document.forms[0].elements('genreService').value === "<%=IAPDroitLAPG.CS_GARDE_PARENTALE%>"
                    || document.forms[0].elements('genreService').value === "<%=IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP%>"
                    || document.forms[0].elements('genreService').value === "<%=IAPDroitLAPG.CS_GARDE_PARENTALE_17_09_20%>"
                    || document.forms[0].elements('genreService').value === "<%=IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP_17_09_20%>") {
                    document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_ENFANT_PAN%>.chercher";
                } else {
                    document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_SAISIE_CARTE_PAN_SITUATION%>.afficher";
                }
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
            action(COMMIT);
        }
    }

    function cancel() {
        if (document.forms[0].elements('_method').value === "add") {
            document.forms[0].elements('userAction').value = "back";
        } else {
            document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_SAISIE_CARTE_PAN%>.afficher";
        }
    }

    function del() {
        if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")) {
            document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_SAISIE_CARTE_PAN%>.supprimer";
            document.forms[0].submit();
        }
    }

    periodes = [];
    Periode = function (dateDeDebut, dateDeFin, nbJour) {
        this.dateDeDebut = dateDeDebut;
        this.dateDeFin = dateDeFin;
        this.nbJour = nbJour;
        this.getDateDeDebut = function () {
            return this.dateDeDebut;
        };
        this.getDateDeFin = function () {
            return this.dateDeFin;
        };
        this.getNbJour = function () {
            return this.nbJour;
        };
        this.toString = function () {
            return this.getDateDeDebut() + "-" + this.getDateDeFin() + "-" + this.getNbJour();
        }
        this.toJson = function () {
            return {dateDeDebut: this.dateDeDebut, dateDeFin: this.dateDeFin, nbJour: this.nbJour};
        };
    }

    function addPeriode() {
        var dateDebut = $('#dateDebutPeriode').val();
        var dateFin = $('#dateFinPeriode').val();
        var nbJour = $('#nbJour').val();
        if (isAjoutdePeriodeAuthorise(dateDebut, dateFin, nbJour, true)) {
            addPeriodeToTable(dateDebut, dateFin, nbJour);
            $('#dateDebutPeriode').val("");
            $('#dateFinPeriode').val("");
            $('#nbJour').val("");
        }
    }

    function deletePeriode(index) {
        periodes.splice(index, 1);
        repaintTablePeriodes();
    }

    function editPeriode(index) {
        var periode = periodes[index];
        periodes.splice(index, 1);
        repaintTablePeriodes();
        $('#dateDebutPeriode').val(periode.getDateDeDebut());
        $('#dateFinPeriode').val(periode.getDateDeFin());
        $('#nbJour').val(periode.getNbJour());

    }


    function addPeriodeToTable(dateDebut, dateFin, nbJour) {
        if (isAjoutdePeriodeAuthorise(dateDebut, dateFin, nbJour, false)) {
            periodes.push(new Periode(dateDebut, dateFin, nbJour));
        }
        repaintTablePeriodes();
    }

    function repaintTablePeriodes() {
        $('#periodes tbody > tr').remove();
        for (var ctr = 0; ctr < periodes.length; ctr++) {
            var periode = periodes[ctr];
            var ddd = '<td>' + periode.getDateDeDebut() + '</td>';
            var ddf = '<td>' + periode.getDateDeFin() + '</td>';
            var njg = '<td>' + periode.getNbJour() + '</td>';
            var height = '12px';
            var spacer = '<span style="width:6px; heigth:10px"></span>'
            if (EDITION_MODE) {
                var deleteBtn = '<td><a onclick="deletePeriode(' + ctr + ')"><img src="images/small_error.png" height="' + height + '" width="12px" alt="delete" /></a>';
                var editBtn = '<a onclick="editPeriode(' + ctr + ')"><img src="images/edit_pen.png" height="' + height + '" width="12px" alt="edition" /></a></td>';
            } else {
                var deleteBtn = '<td>';
                var editBtn = '</td>';
            }
            var html = '<tr>' + ddd + ddf + njg + deleteBtn + spacer + editBtn + '</tr>';
            $('#periodes tbody').append(html);
        }
    }

    function isAjoutdePeriodeAuthorise(dateDebut, dateFin, nbJour, displayMessage) {
        var result = true;
        if (!dateDebut) {
            if (displayMessage) {
                showErrorMessage("La date de début doit être renseignée");
            }
            result = false;
        }
        if (!dateFin && nbJour) {
            if (displayMessage) {
                showErrorMessage("La date de fin doit être renseignée pour saisir le nombre de jours");
            }
            result = false;
        }
        return result;
    }

    function showErrorMessage(message) {
        var html = '<div>';
        html += message;
        html += '</div>';

        $html = $(html);
        $html.dialog({
            position: 'center',
            title: "Erreur",
            width: 400,
            height: 50,
            show: "blind",
            hide: "blind",
            closeOnEscape: true,
            buttons: {'Close': popupClose}
        });
    }

    function popupClose() {
        $html.dialog("close");
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
    }

    function arret() {
        nssUpdateHiddenFields();
        document.forms[0].submit();
        document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_DROIT_LAPG%>.chercher";
    }

    function metAJourGenreService() {
        <%java.util.Iterator iterator = cucsGenreService.iterator();

            while (iterator.hasNext()) {
                String cu = (String)(iterator.next());
                String cs = (String)(iterator.next());%>
        if (document.forms[0].elements('CUGenreService').value === "<%=cu%>") {
            document.forms[0].elements('genreServiceListe').value = "<%=cs%>";
            document.forms[0].elements('genreService').value = "<%=cs%>";
        } else
            <%}
                if (cucsGenreService.size() != 0) {%>        {
            document.forms[0].elements('genreServiceListe').value = "";
            document.forms[0].elements('genreService').value = "";
        }
        <%} else {%>
        document.forms[0].elements('genreServiceListe').value = "";
        document.forms[0].elements('genreService').value = "";
        <%}%>
    }

    function readOnly(flag) {
        // empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
        for (i = 0; i < document.forms[0].length; i++) {
            if (!document.forms[0].elements[i].readOnly
                && document.forms[0].elements[i].className !== 'forceDisable'
                && document.forms[0].elements[i].type !== 'hidden') {
                document.forms[0].elements[i].disabled = flag;
            }
        }
    }

    function limiteur() {
        // limite la saisie de la remarque à 255 caractères
        maximum = 252;
        if (document.forms[0].elements('remarque').value.length > maximum) {
            document.forms[0].elements('remarque').value = document.forms[0].elements('remarque').value.substring(0, maximum);
        }
    }

    function init() {
        // sert à rien mais doit être déclarée...
        if (<%=viewBean.getIsSoumisCotisation()%>) {
            document.getElementById("isSoumisCotisation").checked = true;
        }
        showCantonImpotSource();
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
        if (!document.getElementById("isSoumisCotisation").checked) {
            document.getElementById("csCantonDomicile").value = '';
        } else {
            document.getElementById("csCantonDomicile").value = document.getElementById("csCantonDomicileAffiche").value;
        }
    }

    function nssChange(tag) {
        var param_nss = "756." + document.getElementById("partiallikeNSS").value;
        if (tag.select !== null) {
            _nss = removeDots(tag.select.options[tag.select.selectedIndex].nss);

            if (_nss.length == 13) {
                //formatte tag avec nouveau nss
                nssCheckChar(43, 'likeNSS');
                nssAction('likeNSS');
                concatPrefixAndPartial('likeNSS');
            } else {
                //formatte tag avec ancien nss
                nssCheckChar(45, 'likeNSS');
                nssAction('likeNSS');
                concatPrefixAndPartial('likeNSS');
            }

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

            if (element.codeSexe !== null) {
                for (var i = 0; i < document.getElementById("csSexeAffiche").length; i++) {
                    if (element.codeSexe === document.getElementById("csSexeAffiche").options[i].value) {
                        document.getElementById("csSexeAffiche").options[i].selected = true;
                    }
                }
                document.getElementById("csSexe").value = element.codeSexe;
            }

            if (element.nom !== null && element.prenom !== null) {
                document.getElementById("nomPrenom").value = element.nom + " " + element.prenom;
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
        $('#nomAffiche').change();
        $('#prenomAffiche').change();
        $('#dateNaissanceAffiche').change();
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

    $(document).ready(function () {

        $('#btnUpd').click(function () {
            EDITION_MODE = true;
            $('#modeEditionDroit').val('<%=APModeEditionDroit.EDITION%>');
            repaintTablePeriodes();
        });

        <%
        //Ajout des périodes en cas de relecture de données
        for(PRPeriode periode : viewBean.getPeriodes()){
            %>
        var ddd = '<%=periode.getDateDeDebut()%>';
        var ddf = '<%=periode.getDateDeFin() %>';
        var ndj = '<%=periode.getNbJour() %>';
        addPeriodeToTable(ddd, ddf,ndj);
        <%
    }%>
    });

</script>


<%@ include file="/theme/detail/bodyStart.jspf" %>
<ct:FWLabel key="TITRE_CARTE_PANDEMIE_1"/>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<tr>
    <td>
        <label for="idGestionnaire">
            <ct:FWLabel key="JSP_GESTIONNAIRE"/>
        </label>
    </td>
    <td colspan="3">
        <ct:FWListSelectTag name="idGestionnaire"
                            data="<%=viewBean.getResponsableData()%>"
                            defaut="<%=viewBean.getCurrentUserId()%>"/>
    </td>
</tr>
<tr>
    <td colspan="4">
        <hr/>
    </td>
</tr>
<tr>
    <td colspan="4">
        <h6>
            <ct:FWLabel key="JSP_TIERS"/>
        </h6>
    </td>
</tr>
<!--Gestion du NSS -->
<tr>
    <td>
        <ct:FWLabel key="JSP_NSS_ABREGE"/>
    </td>
    <td colspan="3">
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
               name="alreadyWarn"
               value="<%=request.getParameter("alreadyWarn") == null ? "false" : request.getParameter("alreadyWarn")%>"/>
        <input type="hidden"
               name="alreadyWarnArgus"
               value="<%=request.getParameter("alreadyWarnArgus") == null ? "false" : request.getParameter("alreadyWarnArgus")%>"/>
        <input type="hidden"
               name="validerAgeAvs"
               value="<%=viewBean.getDroit().isValiderAgeAvs()%>"/>
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
    </td>
</tr>
<tr>
    <td>
        <ct:FWLabel key="JSP_NOM"/>
    </td>
    <td>
        <input type="hidden"
               name="nom"
               value="<%=viewBean.getNom()%>"/>
        <input type="text"
               id="nomAffiche"
               name="nomAffiche"
               data-g-string="mandatory:true"
               value="<%=viewBean.getNom()%>"/>
    </td>
    <td>
        <ct:FWLabel key="JSP_PRENOM"/>
    </td>
    <td>
        <input type="hidden"
               name="prenom"
               value="<%=viewBean.getPrenom()%>"/>
        <input type="text"
               id="prenomAffiche"
               name="prenomAffiche"
               data-g-string="mandatory:true"
               value="<%=viewBean.getPrenom()%>"/>
    </td>
</tr>
<tr>
    <td>
        <ct:FWLabel key="JSP_SEXE"/>
    </td>
    <td>
        <ct:FWCodeSelectTag name="csSexeAffiche"
                            wantBlank="<%=true%>"
                            codeType="PYSEXE"
                            defaut="<%=viewBean.getCsSexe()%>"/>
        <input type="hidden"
               name="csSexe"
               data-g-string="mandatory:true"
               value="<%=viewBean.getCsSexe()%>"/>
    </td>
    <td colspan="2">
        &nbsp;
    </td>
</tr>
<tr>
    <td>
        <ct:FWLabel key="JSP_ETAT_CIVIL"/>
    </td>
    <td>
        <ct:FWCodeSelectTag name="csEtatCivilAffiche"
                            wantBlank="<%=true%>"
                            codeType="PYETATCIVI"
                            defaut="<%=viewBean.getCsEtatCivil()%>"/>
        <input type="hidden"
               name="csEtatCivil"
               id="csEtatCivil"
               data-g-string="mandatory:true"
               value="<%=viewBean.getCsEtatCivil()%>"/>
    </td>
    <td>
        <ct:FWLabel key="JSP_DATE_NAISSANCE"/>
    </td>
    <td>
        <input type="hidden"
               name="dateNaissance"
               value="<%=viewBean.getDateNaissance()%>"/>
        <input type="text"
               id="dateNaissanceAffiche"
               name="dateNaissanceAffiche"
               data-g-calendar="mandatory:true"
               value="<%=viewBean.getDateNaissance()%>"/>
    </td>
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
               data-g-integer=""
               value="<%=viewBean.getNpa()%>"
               class="numero"/>
    </td>
    <td>
        <label for="pays">
            <ct:FWLabel key="JSP_PAYS_DOMICILE"/>
        </label>
    </td>
    <td>
        <ct:FWListSelectTag name="pays"
                            data="<%=viewBean.getTiPays()%>"
                            defaut="<%=JadeStringUtil.isIntegerEmpty(viewBean.getPays()) ? TIPays.CS_SUISSE : viewBean.getPays()%>"/>
    </td>
</tr>
<tr>
    <td>
        <label for="noCompte">
            <ct:FWLabel key="JSP_NO_COMPTE"/>
        </label>
    </td>
    <td>
        <input type="text"
               name="noCompte"
               id="noCompte"
               onkeypress="return filterCharForInteger(window.event);"
               value="<%=viewBean.getNoCompte()%>"
               class="numero"
               maxlength="7"
        />

    </td>
    <td>
        <label for="noControlePers">
            <ct:FWLabel key="JSP_NO_CONTROLE"/>
        </label>
    </td>
    <td>
        <input type="text"
               name="noControlePers"
               id="noControlePers"
               onkeypress="return filterCharForInteger(window.event);"
               value="<%=viewBean.getNoControlePers()%>"
               class="numero"
               maxlength="3"/>
        <script type="text/javascript">
            document.getElementById("likeNSS").onkeypress = new Function("", "return filterCharForPositivFloat(window.event);");
        </script>
    </td>
</tr>
<tr>
    <td colspan="4">
        <hr/>
    </td>
</tr>
<tr>
    <td colspan="4">
        <h6>
            <ct:FWLabel key="JSP_PERIODES"/>
        </h6>
    </td>
</tr>
<tr>
    <td colspan="4">
        <table width="100%">
            <tr>
                <td valign="center">
                    <label for="dateDebutPeriode">
                        <ct:FWLabel key="JSP_PERIODE_DU"/>
                    </label>
                </td>
                <td valign="center">
                    <input type="text"
                           id="dateDebutPeriode"
                           name="dateDebutPeriode"
                           data-g-calendar=" "
                           value=""/>
                    <label for="dateFinPeriode">
                        <ct:FWLabel key="JSP_AU"/>
                    </label>
                    <input type="text"
                           id="dateFinPeriode"
                           name="dateFinPeriode"
                           data-g-calendar=" "
                           value=""/>
                    <input type="text"
                           name="nbJour"
                           id="nbJour"
                           value=""
                           class="numero"/>
                    <input type="button"
                           name=""
                           value="<ct:FWLabel key="JSP_AJOUTER" />"
                           onclick="addPeriode()"/>
                </td>
                <td rowspan="2" width="50%">
                    <table class="areaTable">
                        <thead>
                        <tr>
                            <th width="150px">
                                <ct:FWLabel key="DATE_DE_DEBUT"/>
                            </th>
                            <th width="150px">
                                <ct:FWLabel key="DATE_DE_FIN"/>
                            </th>
                            <th width="40px">
                                <ct:FWLabel key="NB_JOUR_GARDE"/>
                            </th>
                            <th width="20px"></th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                    <div style="height:120px; overflow-y:scroll; width:475px; background-color:#FFF; margin-left: 3px;">
                        <table id="periodes" name=periode" class="areaTable">
                            <thead>
                            <tr style="height: 0px;">
                                <th width="150px"></th>
                                <th width="150px"></th>
                                <th width="40px"></th>
                                <th width="20px"></th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td colspan="4">
        <ht/>
    </td>
</tr>
<tr>
    <td colspan="4">
        <h6>
            <ct:FWLabel key="JSP_DROIT"/>
        </h6>
    </td>
</tr>
<tr>
    <td>
        <label for="genreService">
            <ct:FWLabel key="JSP_GENRE_SERVICE"/>
        </label>
    </td>
    <td>
        <input type="text"
               name="CUGenreService"
               id="CUGenreService"
               value="<%=viewBean.getCuGenreService()%>"
               data-g-integer="mandatory:true"
               onkeyup="metAJourGenreService()"/>
        <input type="hidden"
               name="genreService"
               id="genreService"
               value="<%=viewBean.getGenreService()%>"/>
    </td>
    <td colspan="2">
        <ct:select name="genreServiceListe"
                   defaultValue="<%=viewBean.getGenreService()%>"
                   wantBlank="true"
                   disabled="true"
                   styleClass="forceDisable">
            <ct:optionsCodesSystems csFamille="<%=IAPDroitLAPG.CS_GROUPE_GENRE_SERVICE_APG%>">
                <ct:forEach items="<%=viewBean.getExcluCodes()%>" var="code">
                    <ct:excludeCode id="code"/>
                </ct:forEach>
            </ct:optionsCodesSystems>
        </ct:select>
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
    <td>
        <input type="text"
               id="dateReception"
               name="dateReception"
               data-g-calendar=" "
               value="<%=viewBean.getDateReception()%>"/>
    </td>
</tr>
<tr>
    <td>
        <label for="duplicata">
            <ct:FWLabel key="JSP_DUPLICATA"/>
        </label>
    </td>
    <td>
        <input type="checkbox"
               name="duplicata"
                <%=viewBean.getDuplicata().booleanValue() ? "checked" : ""%> />
    </td>
    <td>
        <label for="noRevision">
            <ct:FWLabel key="JSP_NO_REVISION"/>
        </label>
    </td>
    <td>
        <ct:FWCodeSelectTag codeType="<%=IAPDroitAPG.CS_GROUPE_REVISION_APG%>"
                            defaut="<%=viewBean.getNoRevision()%>"
                            name="noRevision"
                            except="<%=exceptRevision%>"/>
    </td>
</tr>
<tr>
    <td>
        <label for=isSoumisCotisation">
            <ct:FWLabel key="JSP_SOUMIS_IMPOT_SOURCE"/>
        </label>
    </td>
    <td>
        <input type="checkbox"
               id="isSoumisCotisation"
               name="isSoumisCotisation"
                <%=viewBean.getIsSoumisCotisation().booleanValue() ? "checked" : ""%>
               onclick="showCantonImpotSource()"
               onload="showCantonImpotSource()"/>
    </td>
    <td>
        <label for=tauxImpotSource">
            <ct:FWLabel key="JSP_TAUX_IMPOT_SOURCE_CARTE"/>
        </label>
    </td>
    <td>
        <input type="text"
               name="tauxImpotSource"
               value="<%=viewBean.getTauxImpotSource()%>"
               class="numero"
               onchange="validateFloatNumber(this);"
               onkeypress="return filterCharForFloat(window.event);"
               style="text-align: right"/>
        <ct:FWLabel key="JSP_TAUX_CANTON"/>
    </td>
</tr>
<tr id="availableIfSoumisCotisation">
    <TD colspan="2">
        &nbsp;
    </TD>
    <TD><LABEL for="csCantonImpoAffiche"><ct:FWLabel key="JSP_CANTON_IMPOT_SOURCE"/></LABEL></TD>
    <TD>
        <ct:FWCodeSelectTag name="csCantonDomicileAffiche"
                            wantBlank="<%=false%>"
                            codeType="PYCANTON"
                            defaut="<%=viewBean.getCsCantonDomicile()%>"/>
        <INPUT type="hidden" name="csCantonDomicile" value="<%=viewBean.getCsCantonDomicile()%>"/>
    </TD>
    <TD colspan="2">
        &nbsp;
    </TD>
</tr>
<tr>
    <td colspan="4">
        &nbsp;
    </td>
</tr>
<tr>
    <td>
        <ct:FWLabel key="JSP_DROIT_ACQUIS"/>
    </td>
    <td>
        <input onchange="validateFloatNumber(this);"
               onkeypress="return filterCharForFloat(window.event);"
               type="text"
               class="libelle"
               style="text-align: right"
               name="droitAcquis"
               value="<%=JANumberFormatter.fmt(viewBean.getDroitAcquis(), true, true, false, 2)%>"/>
    </td>
    <td>
        <label for="csProvenanceDroitAcquis">
            <ct:FWLabel key="JSP_PROVENANCE_DROIT"/>
        </label>
    </td>
    <td>
        <ct:select name="csProvenanceDroitAcquis"
                   wantBlank="true"
                   defaultValue="<%=viewBean.getCsProvenanceDroitAcquis()%>">
            <ct:optionsCodesSystems csFamille="<%=IAPDroitAPG.GROUPE_CS_PROVENANCE_DROIT_ACQUIS%>">
            </ct:optionsCodesSystems>
        </ct:select>
    </td>
</tr>
<tr>
    <td colspan="4">
        &nbsp;
    </td>
</tr>
<tr>
    <td valign="top">
        <label for="remarque">
            <ct:FWLabel key="JSP_REMARQUE"/>
        </label>
    </td>
    <td colspan="3">
							<textarea name="remarque"
                                      cols="85"
                                      rows="3"
                                      onKeyDown="limiteur();"><%=
                            viewBean.getRemarque()
                            %></textarea>
        <br/>
        <ct:FWLabel key="JSP_REMARQUE_COMMENT"/>
    </td>
</tr>
<ct:menuChange displayId="options" menuId="ap-optionmenupan" showTab="options" >
    <ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdDroit()%>" />
    <ct:menuSetAllParams key="noAVS" value="<%=viewBean.getNumeroAvsEntier()%>" />
    <ct:menuSetAllParams key="forIdDroit" value="<%=viewBean.getIdDroit()%>" />
    <ct:menuSetAllParams key="genreService" value="<%=viewBean.getGenreService()%>" />
</ct:menuChange>
<%@ include file="plausibilites.jsp" %>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<input type="button"
       value="<ct:FWLabel key="JSP_ARRET" /> (alt+<ct:FWLabel key="AK_APG_ARRET" />)"
       onclick="arret()"
       accesskey="<ct:FWLabel key="AK_APG_ARRET" />"/>
<input type="button"
       value="<ct:FWLabel key="JSP_SUIVANT" /> (alt+<ct:FWLabel key="AK_APG_SUIVANT" />)"
       onclick="validate()"
       accesskey="<ct:FWLabel key="AK_APG_SUIVANT" />"/>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%@ include file="/theme/detail/footer.jspf" %>
