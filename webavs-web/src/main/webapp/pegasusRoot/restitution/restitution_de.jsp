<%@page import="ch.globaz.pegasus.business.constantes.IPCActions" %>
<%@ page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel" %>
<%@ page import="globaz.jade.client.util.JadeStringUtil" %>
<%@ page import="globaz.pegasus.utils.PCUserHelper" %>
<%@ page import="globaz.pegasus.vb.restitution.PCRestitutionViewBean" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page language="java" errorPage="/errorPage.jsp"
         contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/detail/header.jspf" %>

<%-- tpl:insert attribute="zoneInit" --%>
<%
    PCRestitutionViewBean viewBean = (PCRestitutionViewBean) session.getAttribute("viewBean");
    PersonneEtendueComplexModel personne = viewBean.getPersonne();
    String affichePersonnne= PCUserHelper.getDetailAssure(objSession,personne);
    idEcran = "PPC0XXX";
    autoShowErrorPopup = true;
    bButtonDelete = false;

%>
<%-- /tpl:insert --%>
<%-- tpl:insert attribute="zoneBusiness" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ include file="/pegasusRoot/ajax/javascriptsAndCSS.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsdossiers">
    <ct:menuSetAllParams key="idDossier" value="<%=viewBean.getIdDossier()%>"/>
</ct:menuChange>

<script type="text/javascript">
    var ACTION_RESTITUTION = "<%=IPCActions.ACTION_RESTITUTION_PC%>";

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

    function cancel() {
        document.forms[0].elements('userAction').value = ACTION_RESTITUTION + ".afficher";
    }

    function validate() {
        if (<%=StringUtils.isEmpty(viewBean.getId())%>) {
            document.forms[0].elements('userAction').value = ACTION_RESTITUTION + ".ajouter";
        } else {
            document.forms[0].elements('userAction').value = ACTION_RESTITUTION + ".modifier";
        }
        return true;
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
<span class="postItIcon" data-g-note="idExterne:<%=viewBean.getId()%>, tableSource:PC_RESTIT"></span>
<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="JSP_PC_RESTIT_D_TITRE"/>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:insert attribute="zoneMain" --%>

<TR>
    <TD class="standardLabel"><ct:FWLabel key="JSP_PC_DOS_D_ASSURE"/></TD>
    <TD width="500px">
        <input type="hidden" id="nss" name="personne.personneEtendue.numAvsActuel"
               value="<%=JadeStringUtil.toNotNullString(personne.getPersonneEtendue().getNumAvsActuel())%>"/>
        <input type="hidden" id="idTiers" name="personne.tiers.idTiers"
               value="<%=JadeStringUtil.toNotNullString(personne.getTiers().getId())%>"/>
        <span id="resultAutocompete"><%=affichePersonnne%></span>
    </TD>
</TR>
<TR>
    <TD colspan="6">&nbsp;<HR class="separator"/>
    </TD>
</TR>
<input type="hidden"
       name="idDossier"
       value="<%=viewBean.getIdDossier()%>"/>
<tr>
    <td width="180px">
        <label>
            <ct:FWLabel key="JSP_PC_RESTIT_PC_AVS_FED"/>
        </label>
    </td>
    <td width="250px">
        <ct:inputText
                name="simpleRestitution.montantRestitutionPCAvsFederal"
                defaultValue="<%=viewBean.getSimpleRestitution().getMontantRestitutionPCAvsFederal()%>"
                id="montantRestitutionPCAvsFederal"
                notation="data-g-amount='mandatory:false'"/>
    </td>
    <td width="180px">
        <label>
            <ct:FWLabel key="JSP_PC_RESTIT_PC_AI_FED"/>
        </label>
    </td>
    <td>
        <ct:inputText
                name="simpleRestitution.montantRestitutionPCAIFederal"
                defaultValue="<%=viewBean.getSimpleRestitution().getMontantRestitutionPCAIFederal()%>"
                id="montantRestitutionPCAIFederal"
                notation="data-g-amount='mandatory:false'"/>
    </td>
</tr>
<tr>
    <td width="180px">
        <label>
            <ct:FWLabel key="JSP_PC_RESTIT_PC_AVS_SUB"/>
        </label>
    </td>
    <td width="250px">
        <ct:inputText
                name="simpleRestitution.montantRestitutionPCAvsSubside"
                defaultValue="<%=viewBean.getSimpleRestitution().getMontantRestitutionPCAvsSubside()%>"
                id="montantRestitutionPCAvsSubside"
                notation="data-g-amount='mandatory:false'"/>
    </td>
    <td width="180px">
        <label>
            <ct:FWLabel key="JSP_PC_RESTIT_PC_AI_SUB"/>
        </label>
    </td>
    <td>
        <ct:inputText
                name="simpleRestitution.montantRestitutionPCAISubside"
                defaultValue="<%=viewBean.getSimpleRestitution().getMontantRestitutionPCAISubside()%>"
                id="montantRestitutionPCAISubside"
                notation="data-g-amount='mandatory:false'"/>
    </td>
</tr>
<tr>
    <td width="180px">
        <label>
            <ct:FWLabel key="JSP_PC_RESTIT_PC_AVS_CANT"/>
        </label>
    </td>
    <td width="250px">
        <ct:inputText
                name="simpleRestitution.montantRestitutionPCAvsCantonal"
                defaultValue="<%=viewBean.getSimpleRestitution().getMontantRestitutionPCAvsCantonal()%>"
                id="montantRestitutionPCAvsCantonal"
                notation="data-g-amount='mandatory:false'"/>
    </td>
    <td width="180px">
        <label>
            <ct:FWLabel key="JSP_PC_RESTIT_PC_AI_CANT"/>
        </label>
    </td>
    <td>
        <ct:inputText
                name="simpleRestitution.montantRestitutionPCAICantonal"
                defaultValue="<%=viewBean.getSimpleRestitution().getMontantRestitutionPCAICantonal()%>"
                id="montantRestitutionPCAICantonal"
                notation="data-g-amount='mandatory:false'"/>
    </td>
</tr>
<tr>
    <td width="180px">
        <label>
            <ct:FWLabel key="JSP_PC_RESTIT_PC_RFM_AVS"/>
        </label>
    </td>
    <td width="250px">
        <ct:inputText
                name="simpleRestitution.montantRestitutionPCRfmAvs"
                defaultValue="<%=viewBean.getSimpleRestitution().getMontantRestitutionPCRfmAvs()%>"
                id="montantRestitutionPCRfmAvs"
                notation="data-g-amount='mandatory:false'"/>
    </td>
    <td width="180px">
        <label>
            <ct:FWLabel key="JSP_PC_RESTIT_PC_RFM_AI"/>
        </label>
    </td>
    <td>
        <ct:inputText
                name="simpleRestitution.montantRestitutionPCRfmAI"
                defaultValue="<%=viewBean.getSimpleRestitution().getMontantRestitutionPCRfmAI()%>"
                id="montantRestitutionPCRfmAI"
                notation="data-g-amount='mandatory:false'"/>
    </td>
</tr>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%-- tpl:insert attribute="zoneButtons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:insert attribute="zoneEndPage" --%><%-- /tpl:insert --%>
<%@ include file="/theme/detail/footer.jspf" %>
