<%@ page import="globaz.eform.vb.envoi.GFEnvoiViewBean" %>

<%@ page errorPage="/errorPage.jsp" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>


<%@ include file="/theme/detail_ajax/header.jspf" %>

<%
    idEcran = " GFE0101";
    bButtonNew = false;
    GFEnvoiViewBean viewBean = (GFEnvoiViewBean) session.getAttribute("viewBean");
%>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Content-Style-Type" content="text/css" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta name="User-Lang" content="<%=languePage%>"/>
<meta name="Context_URL" content="<%=servletContext%>"/>
<meta name="formAction" content="<%=formAction%>"/>

<link rel="stylesheet" type="text/css" href="<%=servletContext%>/common/css/bootstrap-2.0.4.css"/>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.css"/>
<%--<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/eform/envoi/envoi_rc.css"/>--%>

<script type="text/javascript" src="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/menu.js"></script>

<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ include file="/jade/notation/notationLibJs.jspf" %>

<script>
    var bFind = true;
    var detailLink = "<%=actionNew%>";
</script>


<TITLE><%=idEcran%></TITLE>
</HEAD>

<body style="background-color: #B3C4DB">
<div class="title thDetail text-center">
    <ct:FWLabel key="ENVOI_TITRE"/>
    <span class="idEcran"><%=(null==idEcran)?"":idEcran%></span>
</div>

<div class="container-sm">
    <h5><ct:FWLabel key="JSP_GESTIONNAIRE"/></h5>
    <form class="form-inline">
        <label class="control-label" for="nomGestionnaire"><ct:FWLabel key="NOM_GESTIONNAIRE"/></label>
        <input type="text" id="nomGestionnaire" class="input-large">
        <label class="control-label" for="departement"><ct:FWLabel key="DEPARTEMENT_GESTIONNAIRE"/></label>
        <input type="text" id="departement" class="input-large">
        <label class="control-label" for="telephone"><ct:FWLabel key="GESTIONNAIRE_TELEPHONE"/></label>
        <input type="text" id="telephone" class="input-large">
        <label class="control-label" for="email"><ct:FWLabel key="GESTIONNAIRE_EMAIL"/></label>
        <input type="text" id="email" class="input-large">
    </form>
    <h5><ct:FWLabel key="ASSURE"/></h5>
    <form class="form-inline">
        <label class="control-label" ><ct:FWLabel key="NSS"></ct:FWLabel><nss:nssPopup avsMinNbrDigit="2" nssMinNbrDigit="2" name="likeNss" newnss="true" tabindex="3"/></label>
<%--        <input type="text" id="NSS" class="input-large">--%>
    </form>
    <form class="form-inline">
        <label class="control-label" for="lastName"><ct:FWLabel key="LASTNAME"/></label>
        <input type="text" id="lastName" class="input-large" readonly>
        <label class="control-label" for="firstName"><ct:FWLabel key="FIRSTNAME"/></label>
        <input type="text" id="firstName" class="input-large" readonly>
        <label class="control-label" for="dateNaissance"><ct:FWLabel key="DATE_DE_NAISSANCE"/></label>
        <input type="text" id="dateNaissance" class="input-large" readonly>
    </form>
    <form class="form-inline">
        <label class="control-label"><ct:FWLabel key="ADRESSE_DOMICILE"/></label>
        <textarea rows="5" readonly></textarea>
    </form>
    <h5><ct:FWLabel key="CAISSE_DESTINATRICE"/></h5>
    <div class="form-group form-inline">
        <label><ct:FWLabel key="CAISSE"/></label>
        <select class="form-control" id="caisse">
            <option value="1">Value 1</option>
            <option value="2">Value 2</option>
            <option value="3">Value 3</option>
        </select>
    </div>
    <h5><ct:FWLabel key="DOCUMENT_A_ENVOYER"/></h5>
    <div class="form-group form-inline">
        <label><ct:FWLabel key="TYPE_DE_FICHIER"/></label>
        <select class="form-control" id="fileType">
            <option value="1">Value 1</option>
            <option value="2">Value 2</option>
            <option value="3">Value 3</option>
        </select>
    </div>

    <div class="form-group form-inline">
        <label><ct:FWLabel key="REPERTOIRE_SOURCE"/></label>
    </div>
    <div class="form-group form-inline">
        <label><ct:FWLabel key="SELECTION_FICHIER"/></label>
        <textarea rows="5"></textarea>
    </div>
</div>
</body>
</html>