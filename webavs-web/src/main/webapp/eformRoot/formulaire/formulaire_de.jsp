<%@ page language="java" import="globaz.globall.http.*"%>

<%@ page import="globaz.eform.vb.formulaire.GFFormulaireViewBean" %>
<%@ page import="globaz.commons.nss.NSUtil" %>
<%@ page import="globaz.eform.helpers.formulaire.GFFormulaireHelper" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page isELIgnored ="false" %>

<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>

<%
    idEcran = "GF0002";

    GFFormulaireViewBean viewBean = (GFFormulaireViewBean) session.getAttribute("viewBean");
    viewBean.retrieveWithBlob();
    selectedIdValue = viewBean.getId();

    userActionValue = "eform.formulaire.formulaire.afficher";

    bButtonNew = false;
    bButtonUpdate = true;
    bButtonDelete = false;
    bButtonValidate = true;
    bButtonCancel = true;

    btnUpdLabel = objSession.getLabel("MODIFIER");
    btnValLabel = objSession.getLabel("VALIDER");
    btnCanLabel = objSession.getLabel("ANNULER");

    formAction = servletContext + mainServletPath;
%>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta name="User-Lang" content="<%=languePage%>"/>
<meta name="Context_URL" content="<%=servletContext%>"/>
<meta name="formAction" content="<%=formAction%>"/>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/moduleStyle.css"/>
<link type="text/css" href="<%=servletContext%>/theme/jquery/jquery-ui.css" rel="stylesheet" />


<%@ include file="/theme/detail/javascripts.jspf" %>


<%@ include file="/jade/notation/notationLibJs.jspf" %>
<script type="text/javascript">
    $(document).ready(function(){
        readOnly(true);
    });

    function upd() {
        document.forms[0].elements('userAction').value="eform.formulaire.formulaire.modifier";
    }

    function validate() {
        document.forms[0].elements('userAction').value="eform.formulaire.formulaire.modifier";
        return validateFields();
    }

    function cancel() {
        document.forms[0].elements('userAction').value="eform.formulaire.formulaire.afficher";
        return true;
    }
</script>

<link rel="stylesheet" type="text/css" href="<%=servletContext%>/common/css/bootstrap.css"/>
<style>

    * {
        -webkit-box-sizing: border-box;
        -moz-box-sizing: border-box;
        box-sizing: border-box;
    }

    .centre {
        vertical-align: middle  !important;
        text-align: center !important;
    }

    .panel-primary {
        border-color: #428bca;
    }
    .panel {
        margin-bottom: 20px;
        background-color: #fff;
        border: 1px solid transparent;
        border-radius: 4px;
        -webkit-box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
        box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
    }

    .panelWarning {
        margin-bottom: 20px;
        background-color: #faa732;
        border: 1px solid transparent;
        border-radius: 4px;
        -webkit-box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
        box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
    }

    .panel-heading {
        padding: 10px 15px;
        border-bottom: 1px solid transparent;
        border-top-left-radius: 3px;
        border-top-right-radius: 3px;
        color: #fff;
        background-color: #4878A2;
        border-color: #428bca;
    }

    .panel-heading-infos {
        padding: 10px 15px;
        border-bottom: 1px solid transparent;
        border-top-left-radius: 3px;
        border-top-right-radius: 3px;
        color: #fff;
        background-color: #4878A2;
        border-color: #428bca;
    }


    .std-body-height {
        width: 100%;
        overflow-y: auto;
    }

    .std-body-height-warn {
        min-height:220px;
        width: 100%;
        overflow-y: auto;
        text-align: center;
    }

    .panel-body {
        padding: 15px;
    }

    .dl-horizontal dd {
        margin-left: 170px;
    }

    .dl-horizontal dt {
        width: 160px;
    }

    .demandeurAlign {
        width: 110px;
        text-align: right;
    }

    .nssAlign {
        width: 151px;
        text-align: right;
    }

    .selectAlign {
        width: 250px;
        text-align: right;
    }

    .dl-horizontal-warning-red dd {
        color: red;
    }

    .dl-horizontal-warning-green dd {
        color: green;
    }

    .dl-horizontal {
        vertical-align: middle;
    }

    .ui-dialog{
        height:650px;
    }

    .nbNoteIcone{
        top :2px !important;
    }

    #memo{
        height: 100px !important;
    }

</style>

<TITLE><%=idEcran%></TITLE>
</HEAD>
<body style="background-color: #B3C4DB">
    <div class="title thDetail text-center" style="width: 100%">
        <ct:FWLabel key="DETAIL_FORMULAIRE_TITRE"/>
        <span class="idEcran"><%=(null==idEcran)?"":idEcran%></span>
    </div>

    <form name="mainForm" action="<%=formAction%>" method="post">
        <INPUT type="hidden" name="selectedId" value="<%=selectedIdValue%>">
        <INPUT type="hidden" name="userAction" value="<%=userActionValue%>">
        <INPUT type="hidden" name="_method" value='<%=request.getParameter("_method")%>'>
        <INPUT type="hidden" name="_valid" value='<%=request.getParameter("_valid")%>'>
        <INPUT type="hidden" name="_sl" value="">
        <INPUT type="hidden" name="selectorName" value="">

        <div class="container-fluid" style="margin-top: 20px">
            <div class="row-fluid">
                <div class="span12"></div>
            </div>
            <div class="row-fluid ">
                <div class="span6">
                    <!-- Panel d'information sur le formulaire -->
                    <div class="panel panel-primar">
                        <div class="panel-heading-infos std-body-height">
                            <dl class="dl-horizontal">
                                <dt><strong><ct:FWLabel key="JSP_EFORM_FORMULAIRE_TYPE"/></strong></dt>
                                <dd><%=viewBean.getCompleteSubject(objSession)%></dd>
                                <dt><strong><ct:FWLabel key="JSP_EFORM_FORMULAIRE_DATE"/></strong></dt>
                                <dd>${viewBean.formulaire.date}</dd>
                                <dt><strong><ct:FWLabel key="JSP_EFORM_FORMULAIRE_ID"/></strong></dt>
                                <dd>${viewBean.formulaire.messageId}</dd>
                            </dl>
                        </div>
                    </div>

                    <!-- Panel qui regroupe les informations du demandeur -->
                    <div class="panel panel-primar">
                        <div class="panel-heading">
                            <strong><ct:FWLabel key="JSP_EFORM_DEMANDEUR"/></strong>
                        </div>
                        <div class="panel-body std-body-height">
                            <div class="pull-left">
                                <dl class="dl-horizontal">
                                    <dt><strong><ct:FWLabel key="JSP_EFORM_FORMULAIRE_NOM"/></strong></dt>
                                    <dd class="demandeurAlign">${viewBean.formulaire.beneficiaireNom}</dd>
                                    <dt><strong><ct:FWLabel key="JSP_EFORM_FORMULAIRE_PRENOM"/></strong></dt>
                                    <dd class="demandeurAlign">${viewBean.formulaire.beneficiairePrenom}</dd>
                                </dl>

                                <dl class="dl-horizontal">
                                    <dt><strong><ct:FWLabel key="JSP_EFORM_FORMULAIRE_DATE_NAISSANCE"/></strong></dt>
                                    <dd class="demandeurAlign">${viewBean.formulaire.beneficiaireDateNaissance}</dd>
                                </dl>

                                <dl class="dl-horizontal">
                                    <dt><strong><ct:FWLabel key="JSP_EFORM_FORMULAIRE_NSS"/></strong></dt>
                                    <% if(viewBean.getIdTiers().isEmpty()){ %>
                                        <dd class="demandeurAlign">
                                    <% } else { %>
                                        <dd class="nssAlign">
                                    <% } %>
                                        <%=NSUtil.formatAVSUnknown(viewBean.getFormulaire().getBeneficiaireNss())%>
                                        <% if(!viewBean.getIdTiers().isEmpty()){ %>
                                            &nbsp;/&nbsp;
                                            <a href="<%=servletContext+"/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId="+viewBean.getIdTiers()%>">
                                                <ct:FWLabel key="JSP_EFORM_FORMULAIRE_TIER"/>
                                            </a>
                                        <% } %>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- fin du span6 -->
                <div class="span6">
                    <!-- Panel de téléchargement-->
                    <div class="panel panel-primar">
                        <div class="panel-heading">
                            <strong><ct:FWLabel key="JSP_TELECHARGEMENT_TITRE"/></strong>
                        </div>
                        <div style="display: table;" class="panel-body std-body-height">
                            <div style="display: table-cell;">
                                <strong>${viewBean.formulaire.attachementName}</strong>
                            </div>
                            <div style="display: table-cell;">
                                <strong>${viewBean.tailleAttachement}</strong>
                            </div>
                            <div style="display: table-cell;">
                                <a data-g-download="docType:zip,
                                    parametres:<%=viewBean.getFormulaire().getId() %>,
                                    serviceClassName:ch.globaz.eform.business.services.GFEFormFileService,
                                    serviceMethodName:getZipFormulaire,
                                    displayOnlyImage:true,
                                    docName:<%=viewBean.getFormulaire().getAttachementName().split("\\.")[0] %>">
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="panel panel-primar">
                        <div class="panel-heading">
                            <strong><ct:FWLabel key="JSP_GESTION_TITRE"/></strong>
                        </div>
                        <div class="panel-body std-body-height">
                            <div class="pull-left">
                                <dl class="dl-horizontal">
                                    <dt><strong><LABEL for="byStatus"><ct:FWLabel key="JSP_EFORM_FORMULAIRE_STATUT"/></LABEL></strong></dt>
                                    <dd class="selectAlign">
                                        <ct:FWCodeSelectTag name="byStatus"
                                                            defaut="<%=viewBean.getFormulaire().getStatus()%>"
                                                            wantBlank="false"
                                                            codeType="GFSTATUS"/></dd>
                                </dl>

                                <dl class="dl-horizontal">
                                    <dt><strong><LABEL for="byStatus"><ct:FWLabel key="JSP_EFORM_FORMULAIRE_GESTIONNAIRE"/></LABEL></strong></dt>
                                    <dd class="selectAlign">
                                        <ct:FWListSelectTag name="byGestionnaire"
                                                            data="<%=GFFormulaireHelper.getGestionnairesData(objSession)%>"
                                                            defaut="<%=viewBean.getFormulaire().getUserGestionnaire()%>"/></dd>
                                </dl>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span12">
                <div style="float:right;">
                    <%if (bButtonNew) {%><input class="btnCtrl" type="button" id="btnNew" value="<%=btnNewLabel%>" onclick="onClickNew();btnNew.onclick='';hideAllButtons();document.location.href='<%=actionNew%>'"><%}%>
                    <%if (bButtonUpdate) {%><input class="btnCtrl" id="btnUpd" type="button" value="<%=btnUpdLabel%>" onclick="action(UPDATE);upd();"><%}%>
                    <%if (bButtonDelete) {%><input class="btnCtrl" id="btnDel" type="button" value="<%=btnDelLabel%>" onclick="del();"><%}%>
                    <%if (bButtonValidate) {%><input class="btnCtrl inactive" id="btnVal" type="button" value="<%=btnValLabel%>" onclick="if (validate()) action(COMMIT);"><%}%>
                    <%if (bButtonCancel) {%><input class="btnCtrl inactive" id="btnCan" type="button" value="<%=btnCanLabel%>" onclick="cancel(); action(ROLLBACK);"><%}%>
                </div>
            </div>
        </div>
    </div>

    <SCRIPT>
        if(top.fr_error!=null) {
            top.fr_error.location.replace(top.fr_error.location.href);
        }
    </SCRIPT>

    <ct:menuChange displayId="menu" menuId="eform-menuprincipal" showTab="menu"/>
    <ct:menuChange displayId="options" menuId="eform-optionsempty" showTab="options"/>
</body>
</html>
