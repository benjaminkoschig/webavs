<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/find_ajax_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° écran et titre --%>
<c:set var="idEcran" value="PPT1003"/>
<c:set var="labelTitreEcran" value="JSP_ASSUREUR_MALADIE"/>

<%@ include file="/theme/find_ajax_el/javascripts.jspf" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/widget.css"/>
<link rel="stylesheet" type="text/css" href="${rootPath}/css/vulpecula.css"/>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjax.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/registre/administrationPart.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaTable.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/widget/globazwidget.js"></script>

<script type="text/javascript">
	globazGlobal.ACTION_AJAX = '${userActionListerAjax}';
	globazGlobal.ERREUR = 'Erreur : une ligne comprend des champs obligatoire vide !';
</script>
<!--  data-g-calendar="mandatory:true" -->
<script type="text/jadeTemplate" id="lineTemplate">
	<tr statut=''>
		<td><ct:FWCodeSelectTag name="csParametre" codeType="PTPARASMA" defaut="" notation="class='csParametre'" /></td>
		<td class='numeriqueParametre'><input type="text" class="dateValiditeFormatter" data-g-calendar="mandatory:true" name="dateValiditeFormatter" value=""></td>
		<td class='numeriqueParametre'><input type="text" class="valeur" 				name="valeur"      			 value="" data-g-rate="nbMaxDecimal:2" /></td>
		<td class='numeriqueParametre'><input type="text" class="plageValeurDebut" 		name="plageValeurDebut"		 value="" data-g-rate="nbMaxDecimal:2" /></td>
		<td class='numeriqueParametre'><input type="text" class="plageValeurFin"		name="plageValeurFin"   	 value="" data-g-rate="nbMaxDecimal:2" /></td>
	</tr>
</script>

<%@ include file="/theme/find_ajax_el/bodyStart.jspf" %>
	<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/find_ajax_el/bodyStart2.jspf" %>
<tr>
	<td>
		<div class="area">
			<div class="areaSearch">
				<table style="width: 100%;">
					<tr>
						<td width="20%"><ct:FWLabel key='JSP_CODE_ADMINISTRATION' /></td>
						<td><input type="text"
							id="searchModel.forCodeAdministrationLike"></input></td>
					</tr>
					<tr>
						<td width="20%"><ct:FWLabel key='JSP_DESIGNATION' /></td>
						<td><input type="text" id="searchModel.forDesignation1Like"></input></td>
					</tr>
				</table>
			</div>
			<div align="right"
				style="padding-right: 80px; padding-top: 10px; padding-bottom: 10px;">
				<input type="button" name="" value="Rechercher">
			</div>
			<div>
				<table class="areaTable" width="100%">
					<thead>
						<tr>
							<th data-orderKey="codeAdministration"><ct:FWLabel
									key='JSP_CODE_ADMINISTRATION' /></th>
							<th data-orderKey="designation1"><ct:FWLabel
									key='JSP_DESIGNATION' /></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			<div class="areaDetail">
				<div class="detail">
					<span id="currentEntity.admin.idTiersAdministration" class="hidden"></span>
					<p class="line">
						<a href="#" id="administrationLink" class="external_link">
						<span id="currentEntity.admin.codeAdministration"></span>
						 - <span id="currentEntity.tiers.designation1"></span>&nbsp;<span
							id="currentEntity.tiers.designation2"></span>&nbsp;<span
							id="currentEntity.tiers.designation3"></span></a>
					</p>
					<p class="line">
					<pre>
<span id="adresse"></span>
					</pre>
					</p>
					<div id="tabLiaisons">
						<ul>
							<li><a href="#areaLiaisonParametres"><ct:FWLabel
										key='JSP_PARAMETRES' /></a></li>
						</ul>
						<div id="areaLiaisonParametre">							
							<table id="tblParametre" class="parametre">
								<thead>
									<tr>
										<th><ct:FWLabel
												key='JSP_ASSUREURMALADIE_LIEN_PARAMETRE_PARAMETRE' /></th>
										<th><ct:FWLabel
												key='JSP_ASSUREURMALADIE_LIEN_PARAMETRE_DATEVALEUR' /></th>
										<th><ct:FWLabel
												key='JSP_ASSUREURMALADIE_LIEN_PARAMETRE_VALEUR' /></th>
										<th><ct:FWLabel
												key='JSP_ASSUREURMALADIE_LIEN_PARAMETRE_PLAGEVALEURDEBUT' /></th>
										<th><ct:FWLabel
												key='JSP_ASSUREURMALADIE_LIEN_PARAMETRE_PLAGEVALEURFIN' /></th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>

				<div class="buttons">
					<div class="btnAjax">
						<ct:ifhasright element="${partialUserAction}" crud="u">
							<input class="btnAjaxUpdate" type="button" value="${btnUpdLabel}">
						</ct:ifhasright>
						<input class="btnAjaxValidate" type="button" value="${btnValLabel}">
						<input class="btnAjaxCancel" type="button" value="${btnCanLabel}">
					</div>
				</div>

			</div>
			<!-- areaDetail -->
		</div> <!-- area -->
	</td>
</tr>

<%@ include file="/theme/find_ajax_el/bodyButtons.jspf" %>

<%@ include file="/theme/find_ajax_el/bodyErrors.jspf" %>

<%@ include file="/theme/find_ajax_el/footer.jspf" %>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />