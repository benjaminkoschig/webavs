<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.hera.enums.TypeDeDetenteur" %>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*"
         contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.hera.helpers.famille.*" %>
<%
    globaz.hera.vb.famille.SFPeriodeViewBean viewBean = (globaz.hera.vb.famille.SFPeriodeViewBean) session.getAttribute("viewBean");
    String idMembreFamille = request.getParameter("idMembreFamille");
    idEcran = "GSF0009";

    String warningMsg = "";
    if (FWViewBeanInterface.WARNING.equals(viewBean.getMsgType())) {
        warningMsg = viewBean.getMessage();
        viewBean.setMessage("");
        viewBean.setMsgType(FWViewBeanInterface.OK);
    }
%>
<%-- /tpl:put --%>

<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/javascripts.jspf" %>

<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.framework.bean.FWViewBeanInterface" %>
<%@page import="globaz.jade.client.util.JadeStringUtil" %>
<%@ page import="globaz.hera.api.ISFSituationFamiliale" %>

<script>
    function init() {
    }

    function add() {
    }

    function validate() {
        state = true;
        if (document.getElementsByName('_method')[0].value == "add") {
            document.getElementsByName('userAction')[0].value = "hera.famille.periode.ajouter";
        } else {
            document.getElementsByName('userAction')[0].value = "hera.famille.periode.modifier";
        }
        return state;
    }

    function del() {
        if (window.confirm("<ct:FWLabel key='ERROR_DELETE_PERIODE_CONFIRMATION'/>")) {
            document.getElementsByName('userAction')[0].value = "hera.famille.periode.supprimer";
            document.getElementsByName('mainForm')[0].submit();
        }
    }

    function cancel() {
        if (document.getElementsByName('_method')[0].value == "add") {
            document.getElementsByName('userAction')[0].value = "hera.famille.periode.chercher";
            document.getElementsByName('mainForm')[0].submit();
        } else {
            document.getElementsByName('userAction')[0].value = "hera.famille.periode.afficher";
        }
    }

    function upd() {
        $selectTypeDeDetenteur.change();
    }

    function changeTypePeriode() {
        // Met le champ pays inactif sauf si la p?riode est de type assurance ?trang?re
        if ($selectTypePeriode.val() == "<%=globaz.hera.api.ISFSituationFamiliale.CS_TYPE_PERIODE_ASSURANCE_ETRANGERE%>") {
            show($tdPays);
            show($tdPaysTitle);
            show($tdDateDebut);
            show($tdDateDebutTitle);
            hide($tdIdDetenteurBTE);
            hide($tdIdDetenteurBTETitle);
            hide($tdCsTypeDeDetenteur);
            hide($tdCsTypeDeDetenteurTitle);
            hide($tdRecueillantTiersTitle);
            hide($tdRecueillantTitle);
            hide($tdRecueillant);

            clear($selectDetenteur);
            clear($selectTypeDeDetenteur);
        }

        // Met le champ d?tenteur inactif sauf si la p?riode est de garde BTE
        else if ($selectTypePeriode.val() == "<%=globaz.hera.api.ISFSituationFamiliale.CS_TYPE_PERIODE_GARDE_BTE%>") {
            show($tdDateDebut);
            show($tdDateDebutTitle);
            show($tdCsTypeDeDetenteur);
            show($tdCsTypeDeDetenteurTitle);
            hide($tdPays);
            hide($tdPaysTitle);
            hide($tdIdDetenteurBTE);
            hide($tdIdDetenteurBTETitle);
            hide($tdRecueillantTiersTitle);
            hide($tdRecueillantTitle);
            hide($tdRecueillant);

            clear($selectPays);
            clear($selectDetenteur);
            initTypeDeDetenteurForBTE();
        }

        // Enfant recueilli
        else if ($selectTypePeriode.val() == "<%=globaz.hera.api.ISFSituationFamiliale.CS_TYPE_PERIODE_ENFANT%>") {
            show($tdDateDebut);
            show($tdDateDebutTitle);
            show($tdCsTypeDeDetenteur);
            show($tdCsTypeDeDetenteurTitle);
            hide($tdPays);
            hide($tdPaysTitle);
            hide($tdIdDetenteurBTE);
            hide($tdIdDetenteurBTETitle);
            show($tdRecueillantTiersTitle);
            hide($tdRecueillantTitle);
            show($tdRecueillant);
            initTypeDeDetenteurForEnfant();

            clear($selectPays);
            clear($selectDetenteur);
        }

        // Enfant recueilli conjoint
        else if ($selectTypePeriode.val() == "<%=ISFSituationFamiliale.CS_TYPE_PERIODE_ENFANT_CONJOINT%>") {
            show($tdDateDebut);
            show($tdDateDebutTitle);
            hide($tdPays);
            hide($tdPaysTitle);
            hide($tdCsTypeDeDetenteur);
            hide($tdCsTypeDeDetenteurTitle);
            hide($tdIdDetenteurBTE);
            hide($tdIdDetenteurBTETitle);
            hide($tdRecueillantTiersTitle);
            show($tdRecueillantTitle);
            show($tdRecueillant);

            clear($selectPays);
            clear($selectTypeDeDetenteur);
            clear($selectDetenteur);
        }

        // si le type est "Certificat de vie" seule la date de fin est utilisable
        else if ($selectTypePeriode.val() == "<%=globaz.hera.api.ISFSituationFamiliale.CS_TYPE_PERIODE_CERTIFICAT_VIE%>") {
            hide($tdPays);
            hide($tdPaysTitle);
            hide($tdCsTypeDeDetenteur);
            hide($tdCsTypeDeDetenteurTitle);
            hide($tdIdDetenteurBTE);
            hide($tdIdDetenteurBTETitle);
            hide($tdDateDebut);
            hide($tdDateDebutTitle);
            hide($tdRecueillantTiersTitle);
            hide($tdRecueillantTitle);
            hide($tdRecueillant);

            clear($selectPays);
            clear($selectTypeDeDetenteur);
            clear($selectDetenteur);
            clear($dateDebut);
        }
        // si le type est "Certificat de vie" seule la date de fin est utilisable
        else if ($selectTypePeriode.val() == "<%=globaz.hera.api.ISFSituationFamiliale.CS_TYPE_PERIODE_INCARCERATION%>") {
            show($tdDateDebut);
            show($tdDateDebutTitle);
            show($tdDateFin);
            show($tdDateFinTitle);
            hide($tdPays);
            hide($tdPaysTitle);
            hide($tdCsTypeDeDetenteur);
            hide($tdCsTypeDeDetenteurTitle);
            hide($tdIdDetenteurBTE);
            hide($tdIdDetenteurBTETitle);
            hide($tdRecueillantTiersTitle);
            hide($tdRecueillantTitle);
            hide($tdRecueillant);

            clear($selectPays);
            clear($selectTypeDeDetenteur);
            clear($selectDetenteur);
        } else {
            hide($tdCsTypeDeDetenteur);
            hide($tdCsTypeDeDetenteurTitle);
            hide($tdIdDetenteurBTE);
            hide($tdIdDetenteurBTETitle);
            hide($tdRecueillantTiersTitle);
            hide($tdRecueillantTitle);
            hide($tdRecueillant);

            clear($selectTypeDeDetenteur);
            clear($selectDetenteur);
        }
    }

    function initTypeDeDetenteurForBTE() {
        clearTypeDeDetenteur();

        var csTiers = "<%=TypeDeDetenteur.TIERS.getCodeSystem()%>";
        var valueTiers = '<%=objSession.getLabel(TypeDeDetenteur.TIERS.getLabelKey())%>';
        $selectTypeDeDetenteur.find('option').end().append('<option value="' + csTiers + '">' + valueTiers + '</option>');

        var csFamille = "<%=TypeDeDetenteur.FAMILLE.getCodeSystem()%>";
        var valueFamille = '<%=objSession.getLabel(TypeDeDetenteur.FAMILLE.getLabelKey())%>';
        $selectTypeDeDetenteur.find('option').end().append('<option value="' + csFamille + '">' + valueFamille + '</option>');

        $selectTypeDeDetenteur.val('');
    }

    function initTypeDeDetenteurForEnfant() {
        clearTypeDeDetenteur();

        var csTuteur = "<%=TypeDeDetenteur.TUTEUR_LEGAL.getCodeSystem()%>";
        var valueTuteur = '<%=objSession.getLabel(TypeDeDetenteur.TUTEUR_LEGAL.getLabelKey())%>';
        $selectTypeDeDetenteur.find('option').end().append('<option value="' + csTuteur + '">' + valueTuteur + '</option>');

        $selectTypeDeDetenteur.val('');
    }

    function changeTypeDeDetenteur() {
        if ($selectTypeDeDetenteur.val() == "<%=TypeDeDetenteur.FAMILLE.getCodeSystem()%>") {
            show($tdIdDetenteurBTE);
            show($tdIdDetenteurBTETitle);
        } else {
            hide($tdIdDetenteurBTE);
            hide($tdIdDetenteurBTETitle);
            clear($selectDetenteur);
        }
    }

    function show($input) {
        $input.show();
        $input.change();
    }

    function hide($input) {
        $input.hide();
        $input.change();
    }

    function clear($input) {
        $input.val("");
        $input.change();
    }

    function clearTypeDeDetenteur() {
        $selectTypeDeDetenteur
            .find('option')
            .remove()
            .end().append('<option value=""></option>')
            .val('');
    }

    function initSelectValues() {
        <%
        // En fonction du type de p?riode (read) on va charger les bonnes donn?es dans les combos csTypeDeDetenteur et idDetenteurBTE
        if(!JadeStringUtil.isBlankOrZero(viewBean.getType())){
            // Type de pP?riode = BTE
            if(globaz.hera.api.ISFSituationFamiliale.CS_TYPE_PERIODE_GARDE_BTE.equals(viewBean.getType())){
                %>
        $selectTypeDeDetenteur.val(<%=viewBean.getCsTypeDeDetenteur()%>);
        $selectDetenteur.val(<%=viewBean.getIdDetenteurBTE()%>);
        <%
    }
    // Type de p?riode = ENFANT
    else if(globaz.hera.api.ISFSituationFamiliale.CS_TYPE_PERIODE_ENFANT.equals(viewBean.getType())){
        %>
        $selectTypeDeDetenteur.val(<%=viewBean.getCsTypeDeDetenteur()%>);
        <%
    }
}

%>
    }

    $(function () {
        $tdDateDebut = $("#tdDateDebut");
        $tdDateDebutTitle = $("#tdDateDebutTitle");
        $tdDateFin = $("#tdDateFin");
        $tdDateFinTitle = $("#tdDateFinTitle");
        $tdPays = $("#tdPays");
        $tdPaysTitle = $("#tdPaysTitle");
        $tdIdDetenteurBTE = $("#tdIdDetenteurBTE");
        $tdIdDetenteurBTETitle = $("#tdIdDetenteurBTETitle");
        $tdCsTypeDeDetenteur = $("#tdCsTypeDeDetenteur");
        $tdCsTypeDeDetenteurTitle = $("#tdCsTypeDeDetenteurTitle");
        $tdType = $("#tdType");
        $tdTypeTitle = $("#tdTypeTitle");
        $tdRecueillantTiersTitle = $("#tdRecueillantTiersTitle");
        $tdRecueillantTitle = $("#tdRecueillantTitle");
        $tdRecueillant = $("#tdRecueillant");

        $dateDebut = $("#dateDebut");
        $dateFin = $("#dateFin");
        $selectPays = $("#pays");
        $selectDetenteur = $("#idDetenteurBTE");
        $selectTypeDeDetenteur = $("#csTypeDeDetenteur");
        $selectTypePeriode = $("#type");
        changeTypePeriode();
        initSelectValues();
    });

</script>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>
<ct:FWLabel key="JSP_PERIODE_TITLE"/>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
<input type="hidden" name="idMembreFamille" value="<%=idMembreFamille%>">
<tr>
    <td id="tdTypeTitle" width="10%">
        <ct:FWLabel key="JSP_PERIODE_TYPE"/>
    </td>
    <td id="tdType" width="40%">
        <ct:FWCodeSelectTag name="type"
                            wantBlank="<%=false%>"
                            codeType="SFTYPPER"
                            defaut="<%=viewBean.getType()%>"
                            doClientValidation="' onchange='changeTypePeriode()"/>
    </td>
    <td width="10%">
    </td>
    <td width="40%">
    </td>
</tr>
<tr>
    <td id="tdDateDebutTitle">
        <ct:FWLabel key="JSP_PERIODE_DATED"/>
    </td>
    <td id="tdDateDebut">
        <input type="text"
               id="dateDebut"
               name="dateDebut"
               value="<%=viewBean.getDateDebut()%>"
               data-g-calendar="mandatory:false"/>
    </td>
    <td id="tdDateFinTitle">
        <ct:FWLabel key="JSP_PERIODE_DATEF"/>
    </td>
    <td id="tdDateFin">
        <input type="text"
               id="dateFin"
               name="dateFin"
               value="<%=viewBean.getDateFin()%>"
               data-g-calendar="mandatory:false"/>
    </td>
</tr>

<tr>

    <td id="tdPaysTitle">
        <ct:FWLabel key="JSP_PERIODE_ETAT"/>
    </td>
    <td id="tdPays">
        <ct:FWCodeSelectTag name="pays"
                            wantBlank="<%=true%>"
                            codeType="CIPAYORI"
                            defaut="<%=viewBean.getPays()%>"/>
    </td>


    <td id="tdCsTypeDeDetenteurTitle">
        <ct:FWLabel key="JSP_PERIODE_TYPE_DETENTEUR"/>
    </td>
    <td id="tdCsTypeDeDetenteur">
        <select name="csTypeDeDetenteur" id="csTypeDeDetenteur"
                defaut="<%=viewBean.getCsTypeDeDetenteur()%>"
                onchange="changeTypeDeDetenteur()"/>
    </td>
</tr>


<tr>
    <td id="tdIdDetenteurBTETitle">
        <ct:FWLabel key="JSP_PERIODE_DETENTEUR"/>
    </td>
    <%
        globaz.hera.helpers.famille.SFRequerantHelper rh = new globaz.hera.helpers.famille.SFRequerantHelper();
    %>
    <td id="tdIdDetenteurBTE">
        <ct:FWListSelectTag name="idDetenteurBTE"
                            data="<%=rh.getDetenteur(idMembreFamille, viewBean.getIdDetenteurBTE(), objSession)%>"
                            defaut="<%=viewBean.getIdDetenteurBTE()%>"/>
    </td>
</tr>

<tr>

    <td id="tdRecueillantTitle">
        <ct:FWLabel key="JSP_PARENT_NON_BIOLOGIQUE"/>
    </td>
    <td id="tdRecueillantTiersTitle">
        <ct:FWLabel key="TYPE_DETENTEUR_TIERS"/>
    </td>
    <td id="tdRecueillant">
        <%
            String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/famille/periode_de.jsp";

            Object[] recueillantMethods= new Object[]{
                    new String[]{"setIdRecueillant","getIdTiers"}
            };

        %>
        <INPUT type="text" id="recueillantName" size="80" maxlength="80" value="<%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getRecueillant(),"\"","&quot;")%>" tabindex="-1" readOnly class="disabled">

        <ct:FWSelectorTag
                name="recueillantSelector"
                methods="<%=recueillantMethods%>"
                providerApplication ="pyxis"
                providerPrefix="TI"
                providerAction ="pyxis.tiers.tiers.chercher"
                redirectUrl="<%=redirectUrl%>"/>
    </td>
</tr>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>