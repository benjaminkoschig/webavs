<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/find_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="???????"/>
<c:set var="labelTitreEcran" value=""/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" /> <!-- Par défaut : true si l'utilisateur a le droit d'écriture -->
<c:set var="bButtonFind" value="true" scope="page" />
<c:set var="bShowExportButton" value="false" scope="page" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/find_el/javascripts.jspf" %>

<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">

//chargement du dom jquery
$(function () {
	
});
</script>

<%@ include file="/theme/find_el/bodyStart.jspf" %>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/find_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>


<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/find_el/bodyButtons.jspf" %>


<%@ include file="/theme/find_el/bodyEnd.jspf" %>

<%@ include file="/theme/find_el/bodyClose.jspf" %>