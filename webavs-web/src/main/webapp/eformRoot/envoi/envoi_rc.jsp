<%@ page import="globaz.eform.helpers.formulaire.GFFormulaireHelper" %>

<%@ page errorPage="/errorPage.jsp" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>


<%@ include file="/theme/find/header.jspf" %>
<%
    idEcran = " GFE0101";
    bButtonNew = false;
%>

<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.css"/>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/eform/envoi/envoi_rc.css"/>

<script type="text/javascript" src="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/menu.js"></script>


<%-- tpl:insert attribute="zoneScripts" --%>
<script>
    var bFind = true;
    var detailLink = "<%=actionNew%>";
    var usrAction = "eform.formulaire.formulaire.lister";
</script>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyStart.jspf" %>

<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="ENVOI_TITRE"/>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyStart2.jspf" %>

<%-- tpl:insert attribute="zoneMain" --%>
<%--partie gestionnaire--%>
<tr>
    <td colspan="10">
        <h6>
            <ct:FWLabel key="JSP_GESTIONNAIRE" />
        </h6>
    </td>
</tr>
<tr>
    <td style="width:10px"></td>
    <td style="width:120px">
        <LABEL for="byNameGestionnaire">
            <ct:FWLabel key="NOM_GESTIONNAIRE"/>
        </LABEL>
    </td>
    <td>
        <ct:inputText name="byNameGestionnaire" id="byNameGestionnaire"/>
    </td>

    <td style="width:20px"></td>
    <td style="width:120px">
        <LABEL for="byDepartementGestionnaire">
            <ct:FWLabel key="DEPARTEMENT_GESTIONNAIRE"/>
        </LABEL>
    </td>
    <td>
        <ct:inputText name="byDepartementGestionnaire" id="byDepartementGestionnaire"/>
    </td>

    <td style="width:20px"></td>
    <td style="width:120px">
        <LABEL for="byTelephoneGestionnaire">
            <ct:FWLabel key="GESTIONNAIRE_TELEPHONE"/>
        </LABEL>
    </td>
    <td>
        <ct:inputText name="byTelephoneGestionnaire" id="byTelephoneGestionnaire"/>
    </td>

    <td style="width:20px"></td>
    <td style="width:80px">
        <LABEL for="byEmailGestionnaire">
            <ct:FWLabel key="GESTIONNAIRE_EMAIL"/>
        </LABEL>
    </td>
    <td>
        <ct:inputText name="byEmailGestionnaire" id="byEmailGestionnaire"/>
    </td>
</tr>

<tr>
    <td colspan="10" style="height: 20px"></td>
</tr>

<tr>
    <td colspan="10">
        <h6>
            <ct:FWLabel key="CAISSE_DESTINATRICE" />
        </h6>
    </td>
</tr>



<tr>
    <td colspan="10" style="height: 10px"></td>
</tr>
<tr>
    <td>
    </td>
    <td>
        <LABEL for="likeNss">
            <ct:FWLabel key="NSS"/>
        </LABEL>
    </td>
    <td>
        <nss:nssPopup avsMinNbrDigit="2" nssMinNbrDigit="2" name="likeNss" newnss="true" tabindex="3"/>

        <ct:inputHidden name="likeNss"/>
    </td>
</tr>
<tr>
    <td colspan="10" style="height: 10px"></td>
</tr>
<tr>
    <%-- affichage nom prenom et date de naissance --%>
    <td></td>
    <td>
        <LABEL for="byLastName">
            <ct:FWLabel key="LASTNAME"/>
        </LABEL>
    </td>
    <td>
        <ct:inputText name="byLastName" id="byLastName"/>
    </td>
        <td></td>
    <td>
        <LABEL for="byFirstName">
            <ct:FWLabel key="FIRSTNAME"/>
        </LABEL>
    </td>
    <td>
        <ct:inputText name="byFirstName" id="byFirstName"/>
    </td>
        <td></td>
    <td>
        <LABEL for="byDateNaissance">
            <ct:FWLabel key="DATE_DE_NAISSANCE"/>
        </LABEL>
    </td>
    <td>
        <ct:inputText name="byDateNaissance" id="byDateNaissance"/>
    </td>
</tr>
<tr>
    <td colspan="10" style="height: 10px"></td>
</tr>
<tr>
    <td></td>
    <td>
        <LABEL for="byAdresseDomicile">
            <ct:FWLabel key="ADRESSE_DOMICILE"/>
        </LABEL>
    </td>
    <td>
        <ct:inputText name="byAdresseDomicile" id="byAdresseDomicile"/>
    </td>
</tr>
<tr>
    <td colspan="10" style="height: 10px"></td>
</tr>
<tr>
    <td></td>
    <td>
        <LABEL for="byCaisse">
            <ct:FWLabel key="CAISSE"/>
        </LABEL>
    </td>
    <td>
        <ct:inputText name="byCaisse" id="byCaisse"/>
    </td>
</tr>
<%--Partie sur les documents--%>

<tr>
    <td colspan="10" style="height: 30px"></td>
</tr>










