<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.apg.api.droits.IAPDroitLAPG"
         contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.apg.enums.APModeEditionDroit" %>
<%@ page import="globaz.apg.servlet.IAPActions" %>
<%@ page import="globaz.apg.vb.droits.APDroitPanSituationViewBean" %>
<%@ page import="globaz.pyxis.db.adressecourrier.TIPays" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="globaz.apg.servlet.APAbstractDroitDTOAction" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="ct1" %>
<%@ include file="/theme/detail/header.jspf" %>


<%
    idEcran = "PAP0010";

    APDroitPanSituationViewBean viewBean = (APDroitPanSituationViewBean) session.getAttribute("viewBean");

    selectedIdValue = viewBean.getIdDroit();

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

<script type="text/javascript">

    var EDITION_MODE = false;
    <%if(viewBean.getModeEditionDroit().equals(APModeEditionDroit.CREATION) || viewBean.getModeEditionDroit().equals(APModeEditionDroit.EDITION)){%>
    EDITION_MODE = true;
    <%}%>

    function init() {
    }

    function add() {
    }

    function upd() {
        for (i = 0; i < document.forms[0].length; i++) {
            document.forms[0].elements[i].disabled = false;
        }
        document.getElementById("likeNSSNssPrefixe").disabled = true;
        document.getElementById("gestionnaire").disabled = true;
        document.getElementById("partiallikeNSS").disabled = true;
        document.getElementById("nomPrenom").disabled = true;
        document.getElementById("nom").disabled = true;
        document.getElementById("prenom").disabled = true;
        document.getElementById("dateNaissance").disabled = true;
        document.getElementById("csEtatCivil").disabled = true;
        document.getElementById("csSexe").disabled = true;
        document.getElementById("npa").disabled = true;
        document.getElementById("pays").disabled = true;
        document.getElementById("noCompte").disabled = true;
        document.getElementById("noControlePers").disabled = true;
        document.getElementById("cuGenreService").disabled = true;
        document.getElementById("genreService").disabled = true;
        document.getElementById("email").disabled = true;

        <%if (StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_GARDE_PARENTALE) || StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_GARDE_PARENTALE_17_09_20)) {%>
        document.getElementById("categorieEntreprise").disabled = true;
        document.getElementById("categorieEntrepriseLibelle").disabled = true;
        document.getElementById("quarantaineOrdonnee").disabled = true;
        document.getElementById("quarantaineOrdonneePar").disabled = true;
        document.getElementById("dateDebutFermeture").disabled = true;
        document.getElementById("dateFinFermeture").disabled = true;
        document.getElementById("dateDebutManifAnnulee").disabled = true;
        document.getElementById("dateFinManifAnnulee").disabled = true;
        document.getElementById("motifGardeHandicap").disabled = true;
        document.getElementById("dateDebutPerteGains").disabled = true;
        document.getElementById("dateFinPerteGains").disabled = true;
        document.getElementById("dateDebutActiviteLimitee").disabled = true;
        document.getElementById("dateFinActiviteLimitee").disabled = true;
        <%} else if (StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP) || StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP_17_09_20)) { %>
        document.getElementById("categorieEntreprise").disabled = true;
        document.getElementById("categorieEntrepriseLibelle").disabled = true;
        document.getElementById("quarantaineOrdonnee").disabled = true;
        document.getElementById("quarantaineOrdonneePar").disabled = true;
        document.getElementById("dateDebutFermeture").disabled = true;
        document.getElementById("dateFinFermeture").disabled = true;
        document.getElementById("dateDebutManifAnnulee").disabled = true;
        document.getElementById("dateFinManifAnnulee").disabled = true;
        document.getElementById("dateDebutPerteGains").disabled = true;
        document.getElementById("dateFinPerteGains").disabled = true;
        document.getElementById("motifGarde").disabled = true;
        document.getElementById("dateDebutActiviteLimitee").disabled = true;
        document.getElementById("dateFinActiviteLimitee").disabled = true;
        <%} else if (StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_QUARANTAINE) || StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_QUARANTAINE_17_09_20)) { %>
        document.getElementById("categorieEntreprise").disabled = true;
        document.getElementById("categorieEntrepriseLibelle").disabled = true;
        document.getElementById("motifGarde").disabled = true;
        document.getElementById("motifGardeHandicap").disabled = true;
        document.getElementById("dateDebutFermeture").disabled = true;
        document.getElementById("dateFinFermeture").disabled = true;
        document.getElementById("dateDebutManifAnnulee").disabled = true;
        document.getElementById("dateFinManifAnnulee").disabled = true;
        document.getElementById("dateDebutPerteGains").disabled = true;
        document.getElementById("dateFinPerteGains").disabled = true;
        document.getElementById("dateDebutActiviteLimitee").disabled = true;
        document.getElementById("dateFinActiviteLimitee").disabled = true;
        <%} else if (StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_INDEPENDANT_PANDEMIE)) { %>
        document.getElementById("motifGarde").disabled = true;
        document.getElementById("motifGardeHandicap").disabled = true;
        document.getElementById("quarantaineOrdonnee").disabled = true;
        document.getElementById("quarantaineOrdonneePar").disabled = true;
        document.getElementById("dateDebutPerteGains").disabled = true;
        document.getElementById("dateFinPerteGains").disabled = true;
        document.getElementById("dateDebutActiviteLimitee").disabled = true;
        document.getElementById("dateFinActiviteLimitee").disabled = true;
        <%} else if (StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_INDEPENDANT_MANIF_ANNULEE) || StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_INDEPENDANT_MANIFESTATION_ANNULEE) || StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_DIRIGEANT_SALARIE_MANIFESTATION_ANNULEE)) { %>
        document.getElementById("motifGarde").disabled = true;
        document.getElementById("motifGardeHandicap").disabled = true;
        document.getElementById("quarantaineOrdonnee").disabled = true;
        document.getElementById("quarantaineOrdonneePar").disabled = true;
        document.getElementById("dateDebutPerteGains").disabled = true;
        document.getElementById("dateFinPerteGains").disabled = true;
        document.getElementById("dateDebutFermeture").disabled = true;
        document.getElementById("dateFinFermeture").disabled = true;
        document.getElementById("dateDebutActiviteLimitee").disabled = true;
        document.getElementById("dateFinActiviteLimitee").disabled = true;
        <%} else if (StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_INDEPENDANT_PERTE_GAINS)) { %>
        document.getElementById("motifGarde").disabled = true;
        document.getElementById("motifGardeHandicap").disabled = true;
        document.getElementById("quarantaineOrdonnee").disabled = true;
        document.getElementById("quarantaineOrdonneePar").disabled = true;
        document.getElementById("dateDebutManifAnnulee").disabled = true;
        document.getElementById("dateFinManifAnnulee").disabled = true;
        document.getElementById("dateDebutFermeture").disabled = true;
        document.getElementById("dateFinFermeture").disabled = true;
        document.getElementById("dateDebutActiviteLimitee").disabled = true;
        document.getElementById("dateFinActiviteLimitee").disabled = true;
        <%} else if (StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_INDEPENDANT_FERMETURE) || StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_DIRIGEANT_SALARIE_FERMETURE)) { %>
        document.getElementById("motifGarde").disabled = true;
        document.getElementById("motifGardeHandicap").disabled = true;
        document.getElementById("quarantaineOrdonnee").disabled = true;
        document.getElementById("quarantaineOrdonneePar").disabled = true;
        document.getElementById("dateDebutPerteGains").disabled = true;
        document.getElementById("dateFinPerteGains").disabled = true;
        document.getElementById("dateDebutManifAnnulee").disabled = true;
        document.getElementById("dateFinManifAnnulee").disabled = true;
        document.getElementById("dateDebutActiviteLimitee").disabled = true;
        document.getElementById("dateFinActiviteLimitee").disabled = true;
        <%} else if (StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_INDEPENDANT_LIMITATION_ACTIVITE) || StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_DIRIGEANT_SALARIE_LIMITATION_ACTIVITE)) { %>
        document.getElementById("motifGarde").disabled = true;
        document.getElementById("motifGardeHandicap").disabled = true;
        document.getElementById("quarantaineOrdonnee").disabled = true;
        document.getElementById("quarantaineOrdonneePar").disabled = true;
        document.getElementById("dateDebutManifAnnulee").disabled = true;
        document.getElementById("dateFinManifAnnulee").disabled = true;
        document.getElementById("dateDebutFermeture").disabled = true;
        document.getElementById("dateFinFermeture").disabled = true;
        document.getElementById("dateDebutPerteGains").disabled = true;
        document.getElementById("dateFinPerteGains").disabled = true;
        <%} else if (StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_SALARIE_EVENEMENTIEL)) { %>
        for (i = 0; i < document.forms[0].length; i++) {
            document.forms[0].elements[i].disabled = true;
        }
        <%}%>
    }

    function validate() {
        if (document.forms[0].elements('_method').value === "read") {
            document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_SITUATION_PROFESSIONNELLE%>.chercher";
            action(COMMIT);
        } else {
            <%if(viewBean.getModeEditionDroit().equals(APModeEditionDroit.CREATION)){%>
            document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_SAISIE_CARTE_PAN_SITUATION%>.ajouter";
            <%} else if(viewBean.getModeEditionDroit().equals(APModeEditionDroit.EDITION)){%>
            document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_SAISIE_CARTE_PAN_SITUATION%>.modifier";
            <%} else if(viewBean.getModeEditionDroit().equals(APModeEditionDroit.LECTURE)){%>
            if (EDITION_MODE) {
                document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_SAISIE_CARTE_PAN_SITUATION%>.modifier";
            } else {
                document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_SITUATION_PROFESSIONNELLE%>.chercher";
            }
            <%}%>
            action(COMMIT);
        }
    }

    function cancel() {
    }

    function del() {
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
        <%if(viewBean.getModeEditionDroit().equals(APModeEditionDroit.CREATION) || viewBean.getModeEditionDroit().equals(APModeEditionDroit.EDITION)){%>
        action(ADD);
        document.getElementById("likeNSSNssPrefixe").disabled = true;
        document.getElementById("gestionnaire").disabled = true;
        document.getElementById("partiallikeNSS").disabled = true;
        document.getElementById("nomPrenom").disabled = true;
        document.getElementById("nom").disabled = true;
        document.getElementById("prenom").disabled = true;
        document.getElementById("dateNaissance").disabled = true;
        document.getElementById("csEtatCivil").disabled = true;
        document.getElementById("csSexe").disabled = true;
        document.getElementById("npa").disabled = true;
        document.getElementById("pays").disabled = true;
        document.getElementById("noCompte").disabled = true;
        document.getElementById("noControlePers").disabled = true;
        document.getElementById("cuGenreService").disabled = true;
        document.getElementById("genreService").disabled = true;
        document.getElementById("email").disabled = true;

        <%if (StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_GARDE_PARENTALE) || StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_GARDE_PARENTALE_17_09_20)) {%>
        document.getElementById("categorieEntreprise").disabled = true;
        document.getElementById("categorieEntrepriseLibelle").disabled = true;
        document.getElementById("quarantaineOrdonnee").disabled = true;
        document.getElementById("quarantaineOrdonneePar").disabled = true;
        document.getElementById("dateDebutFermeture").disabled = true;
        document.getElementById("dateFinFermeture").disabled = true;
        document.getElementById("dateDebutManifAnnulee").disabled = true;
        document.getElementById("dateFinManifAnnulee").disabled = true;
        document.getElementById("motifGardeHandicap").disabled = true;
        document.getElementById("dateDebutPerteGains").disabled = true;
        document.getElementById("dateFinPerteGains").disabled = true;
        document.getElementById("dateDebutActiviteLimitee").disabled = true;
        document.getElementById("dateFinActiviteLimitee").disabled = true;
        <%} else if (StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP) || StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_GARDE_PARENTALE_HANDICAP_17_09_20)) { %>
        document.getElementById("categorieEntreprise").disabled = true;
        document.getElementById("categorieEntrepriseLibelle").disabled = true;
        document.getElementById("quarantaineOrdonnee").disabled = true;
        document.getElementById("quarantaineOrdonneePar").disabled = true;
        document.getElementById("dateDebutFermeture").disabled = true;
        document.getElementById("dateFinFermeture").disabled = true;
        document.getElementById("dateDebutManifAnnulee").disabled = true;
        document.getElementById("dateFinManifAnnulee").disabled = true;
        document.getElementById("dateDebutPerteGains").disabled = true;
        document.getElementById("dateFinPerteGains").disabled = true;
        document.getElementById("motifGarde").disabled = true;
        document.getElementById("dateDebutActiviteLimitee").disabled = true;
        document.getElementById("dateFinActiviteLimitee").disabled = true;
        <%} else if (StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_QUARANTAINE) || StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_QUARANTAINE_17_09_20)) { %>
        document.getElementById("categorieEntreprise").disabled = true;
        document.getElementById("categorieEntrepriseLibelle").disabled = true;
        document.getElementById("motifGarde").disabled = true;
        document.getElementById("motifGardeHandicap").disabled = true;
        document.getElementById("dateDebutFermeture").disabled = true;
        document.getElementById("dateFinFermeture").disabled = true;
        document.getElementById("dateDebutManifAnnulee").disabled = true;
        document.getElementById("dateFinManifAnnulee").disabled = true;
        document.getElementById("dateDebutPerteGains").disabled = true;
        document.getElementById("dateFinPerteGains").disabled = true;
        document.getElementById("dateDebutActiviteLimitee").disabled = true;
        document.getElementById("dateFinActiviteLimitee").disabled = true;
        <%} else if (StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_INDEPENDANT_PANDEMIE)) { %>
        document.getElementById("motifGarde").disabled = true;
        document.getElementById("motifGardeHandicap").disabled = true;
        document.getElementById("quarantaineOrdonnee").disabled = true;
        document.getElementById("quarantaineOrdonneePar").disabled = true;
        document.getElementById("dateDebutPerteGains").disabled = true;
        document.getElementById("dateFinPerteGains").disabled = true;
        document.getElementById("dateDebutActiviteLimitee").disabled = true;
        document.getElementById("dateFinActiviteLimitee").disabled = true;
        <%} else if (StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_INDEPENDANT_MANIF_ANNULEE) || StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_INDEPENDANT_MANIFESTATION_ANNULEE)  || StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_DIRIGEANT_SALARIE_MANIFESTATION_ANNULEE)) { %>
        document.getElementById("motifGarde").disabled = true;
        document.getElementById("motifGardeHandicap").disabled = true;
        document.getElementById("quarantaineOrdonnee").disabled = true;
        document.getElementById("quarantaineOrdonneePar").disabled = true;
        document.getElementById("dateDebutPerteGains").disabled = true;
        document.getElementById("dateFinPerteGains").disabled = true;
        document.getElementById("dateDebutFermeture").disabled = true;
        document.getElementById("dateFinFermeture").disabled = true;
        document.getElementById("dateDebutActiviteLimitee").disabled = true;
        document.getElementById("dateFinActiviteLimitee").disabled = true;
        <%} else if (StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_INDEPENDANT_PERTE_GAINS)) { %>
        document.getElementById("motifGarde").disabled = true;
        document.getElementById("motifGardeHandicap").disabled = true;
        document.getElementById("quarantaineOrdonnee").disabled = true;
        document.getElementById("quarantaineOrdonneePar").disabled = true;
        document.getElementById("dateDebutManifAnnulee").disabled = true;
        document.getElementById("dateFinManifAnnulee").disabled = true;
        document.getElementById("dateDebutFermeture").disabled = true;
        document.getElementById("dateFinFermeture").disabled = true;
        document.getElementById("dateDebutActiviteLimitee").disabled = true;
        document.getElementById("dateFinActiviteLimitee").disabled = true;
        <%} else if (StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_INDEPENDANT_FERMETURE) || StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_DIRIGEANT_SALARIE_FERMETURE)) { %>
        document.getElementById("motifGarde").disabled = true;
        document.getElementById("motifGardeHandicap").disabled = true;
        document.getElementById("quarantaineOrdonnee").disabled = true;
        document.getElementById("quarantaineOrdonneePar").disabled = true;
        document.getElementById("dateDebutPerteGains").disabled = true;
        document.getElementById("dateFinPerteGains").disabled = true;
        document.getElementById("dateDebutManifAnnulee").disabled = true;
        document.getElementById("dateFinManifAnnulee").disabled = true;
        document.getElementById("dateDebutActiviteLimitee").disabled = true;
        document.getElementById("dateFinActiviteLimitee").disabled = true;
        <%} else if (StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_INDEPENDANT_LIMITATION_ACTIVITE) || StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_DIRIGEANT_SALARIE_LIMITATION_ACTIVITE)) { %>
        document.getElementById("motifGarde").disabled = true;
        document.getElementById("motifGardeHandicap").disabled = true;
        document.getElementById("quarantaineOrdonnee").disabled = true;
        document.getElementById("quarantaineOrdonneePar").disabled = true;
        document.getElementById("dateDebutManifAnnulee").disabled = true;
        document.getElementById("dateFinManifAnnulee").disabled = true;
        document.getElementById("dateDebutFermeture").disabled = true;
        document.getElementById("dateFinFermeture").disabled = true;
        document.getElementById("dateDebutPerteGains").disabled = true;
        document.getElementById("dateFinPerteGains").disabled = true;
        <%} else if (StringUtils.equals(viewBean.getGenreService(),IAPDroitLAPG.CS_SALARIE_EVENEMENTIEL)) { %>
        for (i = 0; i < document.forms[0].length; i++) {
            document.forms[0].elements[i].disabled = true;
        }
        <%} %>
        <%} else {%>
        for (i = 0; i < document.forms[0].length; i++) {
            document.forms[0].elements[i].disabled = true;
        }
        <%}%>
    }

    function arret() {
        document.forms[0].elements('userAction').value = "<%=IAPActions.ACTION_DROIT_LAPG%>.chercher";
        action(COMMIT);
    }

    function readOnly(flag) {
        for (i = 0; i < document.forms[0].length; i++) {
            if (!document.forms[0].elements[i].readOnly) {
                document.forms[0].elements[i].disabled = flag;
            }
        }
    }

    function limiteurRefus() {
        // limite la saisie de la remarque à 100 caractères
        maximum = 995;
        if (document.forms[0].elements('remarqueRefus').value.length > maximum) {
            document.forms[0].elements('remarqueRefus').value = document.forms[0].elements('remarque').value.substring(0, maximum);
        }
    }

    function limiteur() {
        // limite la saisie de la remarque à 255 caractères
        maximum = 252;
        if (document.forms[0].elements('categorieAutre').value.length > maximum) {
            document.forms[0].elements('categorieAutre').value = document.forms[0].elements('categorieAutre').value.substring(0, maximum);
        }
        if (document.forms[0].elements('quarantaineOrdoPar').value.length > maximum) {
            document.forms[0].elements('quarantaineOrdoPar').value = document.forms[0].elements('quarantaineOrdoPar').value.substring(0, maximum);
        }
    }

    $(document).ready(function () {
        if (EDITION_MODE) {
            $('#btnUpd').hide();
        }

        $('#btnUpd').click(function () {
            EDITION_MODE = true;
            $('#modeEditionDroit').val('<%=APModeEditionDroit.EDITION%>');
            upd();
        });

    });

</script>


<%@ include file="/theme/detail/bodyStart.jspf" %>
<ct:FWLabel key="TITRE_CARTE_PANDEMIE_2"/>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<tr>
    <td>
        <label for="gestionnaire">
            <ct:FWLabel key="JSP_GESTIONNAIRE"/>
        </label>
    </td>
    <td colspan="2">
        <ct:FWListSelectTag name="gestionnaire"
                            data="<%=viewBean.getResponsableData()%>"
                            defaut="<%=viewBean.getGestionnaire()%>"/>
    </td>
    <td colspan="3">
        <label>
            <ct:FWLabel key="JSP_GENRE_SERVICE"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        </label>
        <input type="text"
               id="cuGenreService"
               name="cuGenreService"
               value="<%=viewBean.getCuGenreService()%>"/>
        <ct:FWCodeSelectTag name="genreService"
                            wantBlank="<%=true%>"
                            codeType="APGENSERVI"
                            defaut="<%=viewBean.getGenreService()%>"/>
    </td>
</tr>
<tr>
    <td colspan="6">
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
    <td colspan="2">
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
               id="nomPrenom"
               name="nomPrenom"
               value="<%=viewBean.getNomPrenom()%>"
               class="libelleLongDisabled"/>
        <input type="hidden"
               id="idDroit"
               name="idDroit"
               value="<%=viewBean.getIdDroit()%>"/>
        <input type="hidden"
               name="nss"
               id="nss"
               value="<%=viewBean.getNss()%>"/>
        <input type="hidden"
               name="idTiers"
               id="idTiers"
               value="<%=viewBean.getIdTiers()%>"/>
        <input type="hidden"
               name="<%=APAbstractDroitDTOAction.PARAM_ID_DROIT%>"
               value="<%=viewBean.getIdDroit()%>"/>
        <input type="hidden"
               name="dateDebutDroit"
               id="dateDebutDroit"
               value="<%=viewBean.getDateDebutDroit()%>"/>
        <input type="hidden"
               id="idDroitPanSituation"
               name="idDroitPanSituation"
               value="<%=viewBean.getIdDroitPanSituation()%>"/>
        <input type="hidden"
               name="modeEditionDroit"
               id="modeEditionDroit"
               value="<%=viewBean.getModeEditionDroit()%>"/>
        <input type="hidden"
               name="modifiable"
               id="modifiable"
               value="<%=viewBean.isModifiable()%>"/>
    </td>
    <td>
        <input type="email"
               size="50"
               id="email"
               name="email"
               value="<%=viewBean.getEmail()%>"/>
    </td>
</tr>
<tr>
    <td>
        <ct:FWLabel key="JSP_NOM"/>
    </td>
    <td>
        <input type="text"
               id="nom"
               name="nom"
               value="<%=viewBean.getNom()%>"/>
    </td>
    <td>
        <ct:FWLabel key="JSP_PRENOM"/>
    </td>
    <td>
        <input type="text"
               id="prenom"
               name="prenom"
               value="<%=viewBean.getPrenom()%>"/>
    </td>
</tr>
<tr>
    <td>
        <ct:FWLabel key="JSP_SEXE"/>
    </td>
    <td>
        <ct:FWCodeSelectTag name="csSexe"
                            wantBlank="<%=true%>"
                            codeType="PYSEXE"
                            defaut="<%=viewBean.getCsSexe()%>"/>
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
        <ct:FWCodeSelectTag name="csEtatCivil"
                            wantBlank="<%=true%>"
                            codeType="PYETATCIVI"
                            defaut="<%=viewBean.getCsEtatCivil()%>"/>
    </td>
    <td>
        <ct:FWLabel key="JSP_DATE_NAISSANCE"/>
    </td>
    <td>
        <input type="text"
               id="dateNaissance"
               name="dateNaissance"
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
    <td colspan="6">
        <hr/>
    </td>
</tr>
<tr>
    <td colspan="4">
        <h6>
            <ct:FWLabel key="JSP_SITUATION"/>
        </h6>
    </td>
</tr>
<tr>
    <td>
        <label>
            <ct:FWLabel key="JSP_ACTIVITE_SALARIEE"/>
        </label>
    </td>
    <td>
        <input type="checkbox"
               name="activiteSalarie"
                <%=viewBean.isActiviteSalarie() ? "checked" : ""%> />
    </td>
    <td>
        <label>
            <ct:FWLabel key="JSP_CATEGORIE_ENTREPRISE"/>
        </label>
    </td>
    <td>
        <ct:FWCodeSelectTag name="categorieEntreprise"
                            wantBlank="<%=true%>"
                            codeType="APPANCATEN"
                            defaut="<%=viewBean.getCategorieEntreprise()%>"/>
    </td>
    <td>
        <label>
            <ct:FWLabel key="JSP_QUARANTAINE_ORDONNEE"/>
        </label>
    </td>
    <td>
        <input type="checkbox"
               name="quarantaineOrdonnee"
                <%=viewBean.isQuarantaineOrdonnee() ? "checked" : ""%> />
    </td>
</tr>
<tr>
    <td>
        <label>
            <ct:FWLabel key="JSP_PAIEMENT_EMPLOYEUR"/>
        </label>
    </td>
    <td>
        <input type="checkbox"
               name="paiementEmployeur"
                <%=viewBean.isPaiementEmployeur() ? "checked" : ""%> />
    </td>
    <td>
        <label>
            <ct:FWLabel key="JSP_CATEGORIE_AUTRE"/>
        </label>
    </td>
    <td colspan="1">
    <textarea type="hidden"
              name="categorieEntrepriseLibelle"
              cols="50"
              rows="3"
              onKeyDown="limiteur();"><%=viewBean.getCategorieEntrepriseLibelle()%></textarea>
    </td>
    <td>
        <label>
            <ct:FWLabel key="JSP_QUARANTAINE_ORDONNEE_PAR"/>
        </label>
    </td>
    <td colspan="1">
    <textarea type="text"
              name="quarantaineOrdonneePar"
              cols="50"
              rows="3"
              onKeyDown="limiteur();"><%=viewBean.getQuarantaineOrdonneePar()%></textarea>
    </td>
</tr>
<tr>
    <td>
        <label>
            <ct:FWLabel key="JSP_COPIE_DECOMPTE_EMPLOYEUR"/>
        </label>
    </td>
    <td>
        <input type="checkbox"
               name="copieDecompteEmployeur"
                <%=viewBean.isCopieDecompteEmployeur() ? "checked" : ""%> />
    </td>
    <td>
        <label>
            <ct:FWLabel key="JSP_MOTIF_GARDE"/>
        </label>
    </td>
    <td>
        <ct:FWCodeSelectTag name="motifGarde"
                            wantBlank="<%=true%>"
                            codeType="APPANMOTGA"
                            defaut="<%=viewBean.getMotifGarde()%>"/>
    </td>
    <td>
        <label>
            <ct:FWLabel key="JSP_MOTIF_GARDE_HANDICAP"/>
        </label>
    </td>
    <td>
        <ct:FWCodeSelectTag name="motifGardeHandicap"
                            wantBlank="<%=true%>"
                            codeType="APPANMOTHA"
                            defaut="<%=viewBean.getMotifGardeHandicap()%>"/>
    </td>
</tr>
<tr>
    <td colspan="6">
        <h6>
            &nbsp;
        </h6>

    </td>
</tr>
<tr>
    <td>
        <label>
            <ct:FWLabel key="JSP_FERMETUREETABLISSEMENT_DU"/>
        </label>
    </td>
    <td colspan="2">
        <input type="text"
               id="dateDebutFermeture"
               name="dateDebutFermeture"
               data-g-calendar=" "
               value="<%=viewBean.getDateDebutFermeture()%>"/>
        <label>
            <ct:FWLabel key="JSP_AU"/>
        </label>
        <input type="text"
               id="dateFinFermeture"
               name="dateFinFermeture"
               data-g-calendar=" "
               value="<%=viewBean.getDateFinFermeture()%>"/>
    </td>
    <td>
        <label>
            <ct:FWLabel key="JSP_PERTE_GAINS_DU"/>
        </label>
        <input type="text"
               id="dateDebutPerteGains"
               name="dateDebutPerteGains"
               data-g-calendar=" "
               value="<%=viewBean.getDateDebutPerteGains()%>"/>
        <label>
            <ct:FWLabel key="JSP_AU"/>
        </label>
        <input type="text"
               id="dateFinPerteGains"
               name="dateFinPerteGains"
               data-g-calendar=" "
               value="<%=viewBean.getDateFinPerteGains()%>"/>
    </td>
    <td>
        <label>
            <ct:FWLabel key="JSP_MANIFESTATION_ANNULEE"/>
        </label>
    </td>
    <td>
        <input type="text"
               id="dateDebutManifAnnulee"
               name="dateDebutManifAnnulee"
               data-g-calendar=" "
               value="<%=viewBean.getDateDebutManifAnnulee()%>"/>
        <label>
            <ct:FWLabel key="JSP_AU"/>
        </label>
        <input type="text"
               id="dateFinManifAnnulee"
               name="dateFinManifAnnulee"
               data-g-calendar=" "
               value="<%=viewBean.getDateFinManifAnnulee()%>"/>
    </td>
</tr>
<tr>
    <td>
        <label>
            <ct:FWLabel key="JSP_ACTIVITE_LIMITEE_DU"/>
        </label>
    </td>
    <td colspan="2">
        <input type="text"
               id="dateDebutActiviteLimitee"
               name="dateDebutActiviteLimitee"
               data-g-calendar=" "
               value="<%=viewBean.getDateDebutActiviteLimitee()%>"/>
        <label>
            <ct:FWLabel key="JSP_AU"/>
        </label>
        <input type="text"
               id="dateFinActiviteLimitee"
               name="dateFinActiviteLimitee"
               data-g-calendar=" "
               value="<%=viewBean.getDateFinActiviteLimitee()%>"/>
    </td>
</tr>



<tr>
    <td colspan="6">
        <hr/>
    </td>
</tr>
<tr>
    <td colspan="1"/>
    <td colspan="5">
        <h3>
            <ct:FWLabel key="JSP_REMARQUE_REFUS"/>
        </h3>
    </td>
</tr>
<tr>
    <td colspan="1"/>
    <td colspan="5">
							<textarea type="text"
                                      name="remarqueRefus"
                                      cols="150"
                                      rows="5"
                                      onKeyDown="limiteurRefus();"><%=
                            viewBean.getRemarqueRefus()
                            %></textarea>
        <br/>
        <ct:FWLabel key="JSP_REMARQUE_REFUS_COMMENT"/>
    </td>
</tr>
<ct:menuChange displayId="options" menuId="ap-optionsempty" showTab="options"/>
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
