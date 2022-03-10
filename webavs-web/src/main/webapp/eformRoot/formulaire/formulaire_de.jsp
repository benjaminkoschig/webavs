<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*"
         contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<ct:menuChange displayId="menu" menuId="eform-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="eform-optionsempty" showTab="options"/>

<%
    /*
    selectedIdValue = "";

    idEcran = "GF0005";
    userActionValue = "eform.formulaire.detail";

    btnUpdLabel = objSession.getLabel("MODIFIER");
    btnDelLabel = objSession.getLabel("SUPPRIMER");
    btnValLabel = objSession.getLabel("VALIDER");
    btnCanLabel = objSession.getLabel("ANNULER");
    btnNewLabel = objSession.getLabel("NOUVEAU");

    //désactive les boutons Update,Delete, Cancel, et New depuis cet écran
    bButtonUpdate = false;
    bButtonDelete = false;
    bButtonCancel = false;
    bButtonNew = false;
     */
%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<script type="text/javascript">
    function add() {
    }

    function upd() {
    }

    function validate() {
    }

    function cancel() {
    }

    function del() {
    }

    function init() {
    }

    function postInit() {
    }


</script>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%>

<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<tr>
    <td>

    </td>
</tr>
<%-- tpl:insert attribute="zoneButtons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>

<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
