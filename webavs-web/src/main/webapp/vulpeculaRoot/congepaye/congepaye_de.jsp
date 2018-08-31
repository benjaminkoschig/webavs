<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PT1300"/>
<c:set var="labelTitreEcran" value="JSP_CONGES_PAYES"/>
<c:set var="userActionCompteur" value="vulpecula.congepaye.compteurs.afficher" />
<c:set var="congePaye" value="${viewBean.congePaye}" />
<c:set var="travailleur" value="${viewBean.travailleur}" />

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="${viewBean.modifiable}" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="${viewBean.modifiable}" scope="page"/>
<c:set var="bButtonUpdate" value="false" />

<script type="text/javascript">
	var globazGlobal = globazGlobal || {};
	globazGlobal.compteurService = '${viewBean.compteurService}';
	globazGlobal.prestationsViewService = '${viewBean.prestationsViewService}';
	globazGlobal.isNouveau = ${viewBean.nouveau};
	globazGlobal.idCongepaye = '${viewBean.id}';
	globazGlobal.wantCotisations = ${viewBean.cotisations};
	globazGlobal.messageAnneeDebutNonVide = '<c:out value="${viewBean.messageAnneeDebutNonVide}" />';
	globazGlobal.messageAnneeFinNonVide = '<c:out value="${viewBean.messageAnneeFinNonVide}" />';
	globazGlobal.messageAnneeFinPlusGrandAnneeDebut = '<c:out value="${viewBean.messageAnneeFinPlusGrandAnneeDebut}" />';
	globazGlobal.messageDateRequise =  '<c:out value="${viewBean.messageDateRequise}" />';
	globazGlobal.messageAucunCompteur = '<c:out value="${viewBean.messageAucunCompteur}" />'
	globazGlobal.messageAucuneCaisseMetier = '<c:out value="${viewBean.messageAucuneCaisseMetier}" />'
	globazGlobal.messageLignePeriodeSaisie = '<ct:FWLabel key="JSP_LIGNE_PERIODE_SAISIE"/>'
	globazGlobal.mustSubstractCotisations = '${viewBean.mustSubstractCotisations}';
	globazGlobal.numAvsTravailleur = '${travailleur.numAvsActuel}';
	globazGlobal.messageEmptyNSS = '${viewBean.messageEmptyNSS}';
</script> 

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<script type="text/javascript" src="${rootPath}/scripts/utils/jquery.noty.packaged.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/utilsFormatter.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/amount.js"></script>
<script type="text/javascript" src="${rootPath}/prestations/prestationsServices.js"></script>
<script type="text/javascript" src="${rootPath}/congepaye/congepaye_de.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/json2.js"></script>

<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<div id="informations" style="float:right; width: 30%">
</div>

<div class="content">
	<div class="blocLeft">
		<div class="bloc blocMedium">
			<%@ include file="/vulpeculaRoot/blocs/travailleur.jspf" %>
		</div>
	</div>
</div>

<div style="margin-top: 30px;">
<div id="mainContainer" class="blocLeft">
	<%@ include file="congepaye.jspf" %>
</div>

<%@ include file="congepayecotisations.jspf" %>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>