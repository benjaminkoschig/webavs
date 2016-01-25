<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1"%>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_ajax_el/header.jspf"%>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT1008" />
<c:set var="labelTitreEcran" value="JSP_COTISATIONS_ASSOCIATIONS" />

<%@ include file="/theme/detail_ajax_el/javascripts.jspf"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjax.js"></script>
<script type="text/javascript" src="${rootPath}/registre/cotisationsAP_de.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/widget/globazwidget.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/widget.css" />

<script type="text/javascript">
	globazGlobal.ACTION_AJAX = '${userActionListerAjax}';
</script>

<%@ include file="/theme/detail_ajax_el/bodyStart.jspf"%>
<ct:FWLabel key="${labelTitreEcran}" />
<%@ include file="/theme/detail_ajax_el/bodyStart2.jspf"%>

<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<tr>
	<td>
		<div class="area">
			<div class="areaSearch">
			<table width="100%">
				<tr><td width ="20%"><ct:FWLabel key='JSP_NUMERO'/><td><input type="text" id="searchModel.forId"></input> </td></tr>
				<tr><td width ="20%"><ct:FWLabel key='JSP_LIBELLE'/><td><input type="text" id="searchModel.forLibelleLike"></input></td></tr>
				<tr><td width ="20%"><ct:FWLabel key='JSP_CODE'/><td><input type="text" id="searchModel.forCodeLike"></input></td></tr>
				<tr><td width ="20%"><ct:FWLabel key='JSP_ASSOCIATION_PROFESSIONNELLE'/><td><input type="text" id="searchModel.forAssociationLike"></input></td></tr>
			</table>				
			</div>
			<div>
				<table class="areaTable" width="100%">
					<thead>
						<th><ct:FWLabel key="JSP_ID" /></th>
						<th><ct:FWLabel key="JSP_LIBELLE" /></th>
						<th><ct:FWLabel key="JSP_ASSOCIATION_PROFESSIONNELLE" /></th>
					</thead>
					<tbody>
						<%-- Rempli dynamiquement ! --%>
					</tbody>
				</table>
			</div>
			<div class="areaDetail">
				<div class="detail">
					<table>
						<tr>
							<td><a id="redirectionParam" style="color: blue; cursor:pointer; display:none;"><ct:FWLabel key="JSP_PARAMETRES_COTISATIONS_AP"/></a></td>
						</tr>
						<tr>
							<td><ct:FWLabel key="JSP_ID"/></td>
							<td><span id="currentEntity.cotisationAssociationProfessionnelleSimpleModel.id"></span></td>
						</tr>
						<tr>
							<td><ct:FWLabel key="JSP_LIBELLE"/></td>
							<td><input id="currentEntity.cotisationAssociationProfessionnelleSimpleModel.libelle" type="text"></span></td>
						</tr>
						<tr>
							<td><ct:FWLabel key="JSP_ASSOCIATION_PROFESSIONNELLE"/></td>
							<td>
							<input 
							id="libelleAssociation"
							class="jadeAutocompleteAjax"
							type="text"
							style="width:600px;"
							data-g-autocomplete="service:¦ch.globaz.vulpecula.business.services.administration.AdministrationService¦,
												 method:¦find¦,
												 criterias:¦{'forCodeAdministrationLike':'Code','forDesignationLike':'Désignation'}¦,
												 constCriterias:¦forGenreAdministration=68900004¦,
												 lineFormatter:¦<b>#{admin.codeAdministration}</b><br />#{tiers.designation1} #{tiers.designation2}¦,
												 modelReturnVariables:¦tiers.id,tiers.designation1,tiers.designation2,admin.codeAdministration¦,nbReturn:¦20¦,
												 functionReturn:¦
												 	function(element){
												 		this.value=$(element).attr('admin.codeAdministration') + ' - ' + $(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
												 		$('#currentEntity\\.cotisationAssociationProfessionnelleSimpleModel\\.idAssociationProfessionnelle').val($(element).attr('tiers.id'))
												 	}¦
												 ,nbOfCharBeforeLaunch:¦1¦"
							/></td>
							<input type="hidden"  value="" id="currentEntity.cotisationAssociationProfessionnelleSimpleModel.idAssociationProfessionnelle"/>
						</tr>
						<tr>
							<td><ct:FWLabel key="JSP_GENRE"/></td>
							<td><ct:FWCodeSelectTag notation="data-g-select=''" name="currentEntity.cotisationAssociationProfessionnelleSimpleModel.genre" codeType="PTGENCOTAP" defaut="" wantBlank="false"/></td>
						</tr>
						
						<tr>
							<td><ct:FWLabel key="JSP_MONTANT_DE_BASE"/></td>
							<td><input id="currentEntity.cotisationAssociationProfessionnelleSimpleModel.montantBase" data-g-amount="" type="text" /></td>
						</tr>
						<tr>
							<td><ct:FWLabel key="JSP_MONTANT_MINIMUM"/></td>
							<td><input id="currentEntity.cotisationAssociationProfessionnelleSimpleModel.montantMinimum" data-g-amount="" type="text" /></td>
						</tr>
						<tr>
							<td><ct:FWLabel key="JSP_MONTANT_MAXIMUM"/></td>
							<td><input id="currentEntity.cotisationAssociationProfessionnelleSimpleModel.montantMaximum" data-g-amount="" type="text" /></td>
						</tr>
					</table>
				</div>
				<div class="buttons">
					<%@ include file="/theme/detail_ajax_el/capageButtons.jspf"%>
				</div>
			</div>
		</div>
	</td>
</tr>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_ajax_el/bodyButtons.jspf"%>
<%@ include file="/theme/detail_ajax_el/bodyErrors.jspf"%>
<%@ include file="/theme/detail_ajax_el/footer.jspf"%>