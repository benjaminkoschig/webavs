<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PT1200"/>
<c:set var="labelTitreEcran" value="JSP_ABSENCES_JUSTIFIEES"/>

<c:set var="absenceJustifiee" value="${viewBean.absenceJustifiee}" />
<c:set var="travailleur" value="${viewBean.travailleur}" />

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="${viewBean.modifiable}" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="${viewBean.modifiable}" scope="page"/>
<c:set var="bButtonUpdate" value="false" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<script type="text/javascript" src="${rootPath}/scripts/utils/jquery.noty.packaged.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/jsnotation/utilsFormatter.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/amount.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/ajax/ajaxUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/json2.js"></script>
<script type="text/javascript" src="${rootPath}/absencesjustifiees/absencesjustifiees_de.js"></script>

<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">
globazGlobal.decompteSalaireViewService = '${viewBean.decompteSalaireViewService}';
globazGlobal.prestationsViewService = '${viewBean.prestationsViewService}';
globazGlobal.isNouveau = ${viewBean.nouveau};
<c:if test="${not empty absenceJustifiee.idPosteTravail}">
	globazGlobal.idPosteTravail = ${absenceJustifiee.idPosteTravail};
</c:if>
globazGlobal.csDeuil = ${viewBean.csDeuil};
globazGlobal.messagePeriodeNonVide = '<c:out value="${viewBean.messagePeriodeNonVide}" />';
globazGlobal.messagePeriodeFinNonSaisie = '<c:out value="${viewBean.messagePeriodeFinNonSaisie}" />';
globazGlobal.messagePeriodeDebutPlusGrandePeriodeFin = '<c:out value="${viewBean.messagePeriodeDebutPlusGrandePeriodeFin}" />';
</script>


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
	<%@ include file="absencesjustifiees.jspf" %>
</div>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>