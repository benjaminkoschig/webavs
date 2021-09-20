<%@page import="globaz.al.vb.dossier.ALDossierViewBean" %>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*"
         contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
    ALDossierViewBean viewBean = (ALDossierViewBean) session.getAttribute("viewBean");
    selectedIdValue = viewBean.getId();
    btnUpdLabel = objSession.getLabel("MODIFIER");
    btnDelLabel = objSession.getLabel("SUPPRIMER");
    btnValLabel = objSession.getLabel("VALIDER");
    btnCanLabel = objSession.getLabel("ANNULER");
    btnNewLabel = objSession.getLabel("NOUVEAU");
    //désactive le bouton new depuis cet écran
    bButtonNew = false;
    bButtonDelete = false;

    idEcran = "AL0005";
    userActionValue = "al.dossier.dossier.modifier";

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<%@page import="ch.globaz.al.business.constantes.ALCSDossier" %>
<%@page import="globaz.jade.client.util.JadeStringUtil" %>
<%@page import="globaz.globall.util.JAUtil" %>
<%@page import="globaz.fweb.util.JavascriptEncoder" %>
<%@page import="globaz.jade.client.util.JadeNumericUtil" %>
<%@ page import="ch.globaz.al.web.application.ALApplication" %>
<%@ page import="globaz.globall.api.GlobazSystem" %>
<script type="text/javascript" src="<%=servletContext%>/alRoot/util_webaf.js"></script>
<script type="text/javascript">
    function add() {
        document.forms[0].elements('userAction').value = "al.dossier.dossier.ajouter";
    }

    function upd() {
        //on affiche les liens retirer qui sont cachés si on est pas en mode modifier
        for (key in document.getElementsByTagName('a')) {
            if (document.getElementsByTagName('a')[key].className == 'removeLink')
                document.getElementsByTagName('a')[key].style.display = 'inline';
        }
        document.forms[0].elements('userAction').value = "al.dossier.dossier.modifier";
    }

    function validate() {
        state = validateFields();

        if (document.forms[0].elements('_method').value == "add")
            document.forms[0].elements('userAction').value = "al.dossier.dossier.ajouter";
        else
            document.forms[0].elements('userAction').value = "al.dossier.dossier.modifier";
        return state;
    }

    function showErrorMessage(message){
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
            buttons: {'Close':popupClose}
        });
    }

    function popupClose(){
        $html.dialog( "close" );
    }

    function cancel() {
        var methodElement = document.forms[0].elements('_method');
        action(methodElement.value);
        if (methodElement.value == ADD) {
            document.forms[0].elements('userAction').value = "al.dossier.dossierMain.afficher";
        } else {
            document.forms[0].elements('userAction').value = "al.dossier.dossier.afficher";
        }
    }

    function del() {
        var msgDelete = '<%= JavascriptEncoder.getInstance().encode(objSession.getLabel("MESSAGE_SUPPRESSION"))%>';
        if (window.confirm(msgDelete)) {
            document.forms[0].elements('userAction').value = "al.dossier.dossier.supprimer";
            document.forms[0].submit();
        }
    }

    function init() {
    }

    function postInit() {
    }


</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%>
<%=(viewBean.getDossierComplexModel().isNew()) ? objSession.getLabel("AL0005_TITRE_NEW") : objSession.getLabel("AL0005_TITRE") + viewBean.getDossierComplexModel().getId()%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<tr>
    <td>
        <%-- tpl:insert attribute="zoneMain" --%>
        <table id="AL0005zone1" class="zone">
            <tr>
                <td class="label"><ct:FWLabel key="AL0004_DOSSIER_VERSEMENT"/></td>
                <td>
                    <%if (viewBean.getDossierComplexModel().getTiersBeneficiaireModel() != null && !JadeNumericUtil.isEmptyOrZero(viewBean.getDossierComplexModel().getTiersBeneficiaireModel().getIdTiers())) { %>
                    <a href="<%=servletContext+"/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId="+viewBean.getDossierComplexModel().getTiersBeneficiaireModel().getIdTiers()%>"
                       title="Détail bénéficaire"><%=viewBean.getDossierComplexModel().getTiersBeneficiaireModel().getDesignation1()%>&nbsp;<%=viewBean.getDossierComplexModel().getTiersBeneficiaireModel().getDesignation2()%>
                    </a>
                    <%} else { %>
                    <ct:FWLabel key="AL0000_VERSEMENT_A_EMPLOYEUR"/>
                    <%} %>

                </td>

                <ct:inputHidden name="dossierComplexModel.dossierModel.activiteAllocataire"/>

            </tr>
        </table>

        <table id="AL0005zone2" class="zone">
            <tr>
                <td class="label"><ct:FWLabel key="AL0004_DEBUT_ACTIVITE"/></td>
                <td><ct:FWCalendarTag name="debutActivite" tabindex="1"
                                      value="<%=viewBean.getDossierComplexModel().getDossierModel().getDebutActivite()%>"
                                      doClientValidation="CALENDAR"/>
                    <ct:inputHidden name="dossierComplexModel.dossierModel.debutActivite" id="debutActiviteValue"/>
                    <script language="JavaScript">
                        document.getElementsByName('debutActivite')[0].onblur = function () {
                            fieldFormat(document.getElementsByName('debutActivite')[0], 'CALENDAR');
                            document.getElementById('debutActiviteValue').value = this.value;
                        };

                        function theTmpReturnFunctiondebutActivite(y, m, d) {
                            if (window.CalendarPopup_targetInput != null) {
                                var d = new Date(y, m - 1, d, 0, 0, 0);
                                window.CalendarPopup_targetInput.value = formatDate(d, window.CalendarPopup_dateFormat);
                                document.getElementById('debutActiviteValue').value = document.getElementsByName('debutActivite')[0].value;
                                alert(document.getElementById('debutActiviteValue').value);
                            } else {
                                alert('Use setReturnFunction() to define which function will get the clicked results!');
                            }
                        }

                        cal_debutActivite.setReturnFunction('theTmpReturnFunctiondebutActivite');
                    </script>
                </td>
                <td class="label"><ct:FWLabel key="AL0004_FIN_ACTIVITE"/></td>
                <td>
                    <ct:FWCalendarTag name="finActivite" tabindex="6"
                                      value="<%=viewBean.getDossierComplexModel().getDossierModel().getFinActivite()%>"
                                      doClientValidation="CALENDAR"/>
                    <ct:inputHidden name="dossierComplexModel.dossierModel.finActivite" id="finActiviteValue"/>
                    <script language="JavaScript">
                        document.getElementsByName('finActivite')[0].onblur = function () {
                            fieldFormat(document.getElementsByName('finActivite')[0], 'CALENDAR');
                            document.getElementById('finActiviteValue').value = this.value;
                        };

                        function theTmpReturnFunctionfinActivite(y, m, d) {
                            if (window.CalendarPopup_targetInput != null) {
                                var d = new Date(y, m - 1, d, 0, 0, 0);
                                window.CalendarPopup_targetInput.value = formatDate(d, window.CalendarPopup_dateFormat);
                                document.getElementById('finActiviteValue').value = document.getElementsByName('finActivite')[0].value;
                            } else {
                                alert('Use setReturnFunction() to define which function will get the clicked results!');
                            }
                        }

                        cal_finActivite.setReturnFunction('theTmpReturnFunctionfinActivite');
                    </script>
                </td>
            </tr>
            <tr>
                <td class="label"><ct:FWLabel key="AL0005_DOSSIER_DEBUT"/></td>
                <td>
                    <ct:FWCalendarTag name="debutValidite" tabindex="2"
                                      value="<%=viewBean.getDossierComplexModel().getDossierModel().getDebutValidite()%>"
                                      doClientValidation="CALENDAR"/>
                    <ct:inputHidden name="dossierComplexModel.dossierModel.debutValidite" id="debutValiditeValue"/>
                    <script language="JavaScript">
                        document.getElementsByName('debutValidite')[0].onblur = function () {
                            fieldFormat(document.getElementsByName('debutValidite')[0], 'CALENDAR');
                            document.getElementById('debutValiditeValue').value = this.value;
                        };

                        function theTmpReturnFunctiondebutValidite(y, m, d) {
                            if (window.CalendarPopup_targetInput != null) {
                                var d = new Date(y, m - 1, d, 0, 0, 0);
                                window.CalendarPopup_targetInput.value = formatDate(d, window.CalendarPopup_dateFormat);
                                document.getElementById('debutValiditeValue').value = document.getElementsByName('debutValidite')[0].value;
                            } else {
                                alert('Use setReturnFunction() to define which function will get the clicked results!');
                            }
                        }

                        cal_debutValidite.setReturnFunction('theTmpReturnFunctiondebutValidite');
                    </script>
                </td>
                <td class="label"><ct:FWLabel key="AL0005_DOSSIER_FIN"/></td>
                <td>
                    <ct:FWCalendarTag name="finValidite" tabindex="7"
                                      value="<%=viewBean.getDossierComplexModel().getDossierModel().getFinValidite()%>"
                                      doClientValidation="CALENDAR"/>
                    <ct:inputHidden name="dossierComplexModel.dossierModel.finValidite" id="finValiditeValue"/>
                    <script language="JavaScript">
                        document.getElementsByName('finValidite')[0].onblur = function () {
                            fieldFormat(document.getElementsByName('finValidite')[0], 'CALENDAR');
                            document.getElementById('finValiditeValue').value = this.value;
                        };

                        function theTmpReturnFunctionfinValidite(y, m, d) {
                            if (window.CalendarPopup_targetInput != null) {
                                var d = new Date(y, m - 1, d, 0, 0, 0);
                                window.CalendarPopup_targetInput.value = formatDate(d, window.CalendarPopup_dateFormat);
                                document.getElementById('finValiditeValue').value = document.getElementsByName('finValidite')[0].value;
                            } else {
                                alert('Use setReturnFunction() to define which function will get the clicked results!');
                            }
                        }

                        cal_finValidite.setReturnFunction('theTmpReturnFunctionfinValidite');
                    </script>

                </td>

            </tr>
            <tr>
                <td class="label"><ct:FWLabel key="AL0004_DOSSIER_JOURS"/></td>
                <td><ct:inputText tabindex="3" name="dossierComplexModel.dossierModel.nbJoursDebut"
                                  styleClass="percent"/></td>


                <td class="label"><ct:FWLabel key="AL0004_DOSSIER_JOURS"/></td>
                <td><ct:inputText tabindex="8" name="dossierComplexModel.dossierModel.nbJoursFin"
                                  styleClass="percent"/></td>
            </tr>
            <tr>
                <td class="label"><ct:FWLabel key="AL0004_DOSSIER_ETAT"/></td>
                <td>
                    <ct:select name="dossierComplexModel.dossierModel.etatDossier" tabindex="4" wantBlank="false">
                        <ct:optionsCodesSystems csFamille="ALDOSETAT">
                            <%if (!ALCSDossier.ETAT_RADIE.equals(viewBean.getDossierComplexModel().getDossierModel().getEtatDossier())) { %>
                            <ct:excludeCode code="<%=ALCSDossier.ETAT_RADIE%>"/>
                            <%} %>
                        </ct:optionsCodesSystems>
                    </ct:select>
                </td>
                <td class="label"><ct:FWLabel key="AL0004_DOSSIER_CAF_AUTREPARENT"/></td>
                <td>

                    <ct:inputHidden name="dossierComplexModel.dossierModel.idTiersCaisseConjoint"/>
                    <%
                        String caisseDescription = "";
                        if (!JadeStringUtil.isEmpty(viewBean.getDossierComplexModel().getCaisseAFComplexModel().getTiers().getDesignation1()))
                            caisseDescription = viewBean.getDossierComplexModel().getCaisseAFComplexModel().getTiers().getDesignation1();
                        if (!JadeStringUtil.isEmpty(viewBean.getDossierComplexModel().getCaisseAFComplexModel().getTiers().getDesignation2()))
                            caisseDescription = caisseDescription.concat(viewBean.getDossierComplexModel().getCaisseAFComplexModel().getTiers().getDesignation2());
                        if (!JadeStringUtil.isEmpty(viewBean.getDossierComplexModel().getCaisseAFComplexModel().getTiers().getDesignation3()))
                            caisseDescription = caisseDescription.concat(viewBean.getDossierComplexModel().getCaisseAFComplexModel().getTiers().getDesignation3());
                        if (!JadeStringUtil.isEmpty(viewBean.getDossierComplexModel().getCaisseAFComplexModel().getTiers().getDesignation4()))
                            caisseDescription = caisseDescription.concat(viewBean.getDossierComplexModel().getCaisseAFComplexModel().getTiers().getDesignation4());

                    %>
                    <ct:inputText tabindex="9" name="dossierComplexModel.caisseAFComplexModel.admin.codeAdministration"
                                  styleClass="small" title="<%=caisseDescription%>"/>

                    <%
                        Object[] caisseMethodsName = new Object[]{
                                new String[]{"dossierComplexModel.dossierModel.idTiersCaisseConjoint", "getIdTiers"},
                                //pour afficher code dès retour sur le dossier ,sans avoir sauvegarder...
                                new String[]{"dossierComplexModel.caisseAFComplexModel.admin.codeAdministration", "getCodeAdministration"}
                        };
                    %>
                    <ct:FWSelectorTag name="caisseSelector"
                                      methods="<%=caisseMethodsName%>"
                                      providerApplication="pyxis"
                                      providerPrefix="TI"
                                      providerAction="pyxis.tiers.administration.chercher"
                    />

                </td>

            </tr>
            <tr>
                <td class="label"><ct:FWLabel key="AL0004_DOSSIER_STATUT"/></td>
                <td>
                    <%=viewBean.renderHTMLSelectStatut() %>
                </td>
                <td class="label"><ct:FWLabel key="AL0004_DOSSIER_LOI_AUTREPARENT"/></td>
                <td>
                    <ct:select tabindex="10" name="dossierComplexModel.dossierModel.loiConjoint" wantBlank="true"
                               defaultValue="<%=viewBean.getDossierComplexModel().getDossierModel().getLoiConjoint() %>">
                        <ct:optionsCodesSystems csFamille="ALTARCAT"></ct:optionsCodesSystems>
                    </ct:select>

                </td>
            </tr>
        </table>

        <table id="AL0005zone3" class="zone">
            <tr>
                <td class="label"><ct:FWLabel key="AL0005_DOSSIER_TAUX"/></td>
                <td>
                    <ct:inputText tabindex="11" name="dossierComplexModel.dossierModel.tauxVersement"
                                  styleClass="small"/>%
                </td>
                <td class="label"><ct:FWLabel key="AL0005_DOSSIER_UNITE"/></td>
                <td>
                    <ct:select tabindex="15" name="dossierComplexModel.dossierModel.uniteCalcul"
                               defaultValue="<%=ALCSDossier.UNITE_CALCUL_MOIS %>">
                        <ct:optionsCodesSystems csFamille="ALDOSUNITE">
                            <ct:excludeCode code="<%=ALCSDossier.UNITE_CALCUL_SPECIAL %>"/>
                        </ct:optionsCodesSystems>
                    </ct:select>
                </td>

            </tr>
            <tr>
                <td class="label"><ct:FWLabel key="AL0005_DOSSIER_MOTIF_REDUC"/></td>
                <td>
                    <ct:select tabindex="12" name="dossierComplexModel.dossierModel.motifReduction" wantBlank="true">
                        <ct:optionsCodesSystems csFamille="ALDOSMORED"></ct:optionsCodesSystems>
                    </ct:select>
                </td>

                <td class="label"><ct:FWLabel key="AL0005_ALLOC_NUMSALARIE"/></td>
                <td><ct:inputText tabindex="16" name="dossierComplexModel.dossierModel.numSalarieExterne"/></td>
            </tr>
            <tr>
                <td class="label"><ct:FWLabel key="AL0005_DOSSIER_TARIF_FORCE"/></td>
                <td>
                    <ct:select tabindex="13" id ="tarifForce" name="dossierComplexModel.dossierModel.tarifForce" wantBlank="true">
                        <ct:optionsCodesSystems csFamille="ALTARCAT"></ct:optionsCodesSystems>
                    </ct:select>
                </td>
                <td class="label"><ct:FWLabel key="AL0005_DOSSIER_IMPOT"/></td>
                <td>
                    <ct:inputHidden name="dossierComplexModel.dossierModel.retenueImpot"/>
                    <% if (viewBean.getDossierComplexModel().getDossierModel().getRetenueImpot().booleanValue()) { %>
                    <input type="checkbox" tabindex="17" checked="checked" name="retenueImpot"
                           onclick="reportCheckboxInModel('dossierComplexModel.dossierModel');"/>
                    <%} else {%>
                    <input type="checkbox" name="retenueImpot" tabindex="17"
                           onclick="reportCheckboxInModel('dossierComplexModel.dossierModel');"/>
                    <%}%>
                </td>
            </tr>
            <tr>
                <td class="label"><ct:FWLabel key="AL0005_DOSSIER_MONTANT_FORCE"/></td>
                <td>
                    <ct:inputText tabindex="14" name="dossierComplexModel.dossierModel.montantForce"
                                  styleClass="montant"/><ct:FWLabel key="AL0005_DOSSIER_MONTANT_FORCE_DATE"/>
                    <ct:inputText name="dossierComplexModel.dossierModel.finValidite" readonly="readonly"
                                  styleClass="readOnly" style="width:90px;"/>
                </td>

                <% if (((ALApplication) GlobazSystem
                        .getApplication(ALApplication.DEFAULT_APPLICATION_WEBAF)).hasImpotSourceActive()) { %>
                <td class="label"><ct:FWLabel key="AL0005_DOSSIER_CANTON_IMPOSITION"/></td>
                <td>
                    <ct:select tabindex="13" id ="cantonImposition" name="dossierComplexModel.dossierModel.cantonImposition" wantBlank="true">
                        <ct:optionsCodesSystems csFamille="ALCANTON"></ct:optionsCodesSystems>
                    </ct:select>
                </td>
                <%}%>
            </tr>

        </table>

        <%-- /tpl:insert --%>
    </td>
</tr>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:insert attribute="zoneButtons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="menuWEBAF" showTab="menu"/>
<ct:menuChange displayId="options" menuId="dossier-detail" showTab="options">
    <ct:menuSetAllParams key="id" value="<%=viewBean.getDossierComplexModel().getId()%>"/>
    <ct:menuSetAllParams key="selectedId" value="<%=viewBean.getDossierComplexModel().getId()%>"/>
</ct:menuChange>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
