<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/find_ajax_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° écran et titre --%>
<c:set var="idEcran" value="PPT1011" />
<c:set var="labelTitreEcran" value="JSP_PARAMETRES_COTISATIONS_ASSOCIATIONS" />

<%@ include file="/theme/find_ajax_el/javascripts.jspf" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjax.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaTable.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/registre/parametresCotisationsAssociations_de.js"></script>

<script type="text/javascript">
	globazGlobal.ACTION_AJAX = '${userActionListerAjax}';
</script>

<%@ include file="/theme/find_ajax_el/bodyStart.jspf" %>
	<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/find_ajax_el/bodyStart2.jspf" %>
<tr>
	<td>
	<div class="area">
		<div class="areaSearch">
			<table width="100%">
				<tr><td width ="20%"><ct:FWLabel key='JSP_NUMERO'/><td><input type="text" id="searchModel.forId"></input> </td></tr>
				<tr><td width ="20%"><ct:FWLabel key='JSP_DESIGNATION'/><td><input type="text" id="searchModel.forLibelleLike" value="${viewBean.forLibelle}"></input> </td></tr>
				 <tr>
							<td>
								<ct:FWLabel key="JSP_GENRE"/>
							</td>
							<td>
								<ct:select id="searchModel.forGenre" name="searchModel.forGenre" wantBlank="true" notation="data-g-select=' '">
									<ct:optionsCodesSystems csFamille="PTGENCOTAP"/>
								</ct:select>
							</td>
						</tr>		
			</table>
		</div>		
		<div align="right"  style="padding-right:80px;padding-top:10px;padding-bottom:10px;">
			<input type="button" name="" value="Rechercher">
		</div>
		<div>
			<table class="areaTable" width="100%">
				<thead>
					<tr>
						<th><ct:FWLabel key="JSP_NO"/></th>
						<th><ct:FWLabel key="JSP_COTISATION"/></th>
						<th><ct:FWLabel key="JSP_ASSOCIATION_PROFESSIONNELLE"/></th>
						<th><ct:FWLabel key="JSP_GENRE"/></th>
						<th><ct:FWLabel key="JSP_TAUX"/></th>
						<th><ct:FWLabel key="JSP_FOURCHETTE_DEBUT"/></th>
						<th><ct:FWLabel key="JSP_FOURCHETTE_FIN"/></th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
		<div class="areaDetail">
			<div class="detail">
					<table>
						<tr>					
							<td>
								<ct:FWLabel key="JSP_NO"/>
							</td>
							<td>
								<span id="currentEntity.id" name="currentEntity.id"> </span>
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_COTISATION"/>
							</td>
							<td>
                                  <input 
                                  id="nomCotisation"
                                  style="width:860px;"
                                  class="jadeAutocompleteAjax"
                                  value=""
                                  type="text"
                                  data-g-autocomplete="service:¦ch.globaz.vulpecula.business.services.association.CotisationAssociationProfessionnelleCRUDService¦,
                                                                   method:¦search¦,
                                                                   criterias:¦{'forLibelleUpperLike':'Cotisation','forAssociationUpperLike':'Association'}¦,
                                                                   constCriterias:¦forGenreNot=68020003¦
                                                                   lineFormatter:¦<b>#{cotisationAssociationProfessionnelleSimpleModel.libelle}</b><br />#{administrationComplexModel.tiers.designation1} #{administrationComplexModel.tiers.designation2}¦,
                                                                   modelReturnVariables:¦cotisationAssociationProfessionnelleSimpleModel.id,cotisationAssociationProfessionnelleSimpleModel.libelle,administrationComplexModel.tiers.designation1,administrationComplexModel.tiers.designation2¦,nbReturn:¦20¦,
                                                                   functionReturn:¦
                                                                          function(element){
                                                                          		var libelleCotisation = $(element).attr('cotisationAssociationProfessionnelleSimpleModel.libelle');
                                                                          		var libelleAssociation = $(element).attr('administrationComplexModel.tiers.designation1') + ' ' + $(element).attr('administrationComplexModel.tiers.designation2'); 
                                                                                this.value=libelleCotisation + ' | ' + libelleAssociation;
                                                                                $('#currentEntity\\.parametreCotisationAssociationSimpleModel\\.idCotisationAssociationProfessionnelle').val($(element).attr('cotisationAssociationProfessionnelleSimpleModel.id'));
                                                                         }¦
                                                                   ,nbOfCharBeforeLaunch:¦3¦"
                                  />
                                  <input id="currentEntity.parametreCotisationAssociationSimpleModel.idCotisationAssociationProfessionnelle" type="hidden" />
                             </td>
                        </tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_TAUX"/>
							</td>
							<td>
								 <input id="currentEntity.parametreCotisationAssociationSimpleModel.taux" name="currentEntity.cotisationCaisseMetierSimpleModel.taux" data-g-rate="nbMaxDecimal:3" />  
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_FOURCHETTE"/>
							</td>
							<td>
								<input id="currentEntity.parametreCotisationAssociationSimpleModel.fourchetteDebut" name="currentEntity.cotisationCaisseMetierSimpleModel.fourchetteDebut" data-g-amount="" />
								<input id="currentEntity.parametreCotisationAssociationSimpleModel.fourchetteFin" name="currentEntity.cotisationCaisseMetierSimpleModel.fourchetteFin" data-g-amount="" />
							</td>
						</tr>
					</table>
			</div>
			
			<div class="buttons">
					<%@ include file="/theme/detail_ajax_el/capageButtons.jspf"%>
			</div>
		</div> <!-- /areaDetail -->
	</div>
	</td>
</tr>

<%@ include file="/theme/find_ajax_el/bodyButtons.jspf" %>

<%@ include file="/theme/find_ajax_el/bodyErrors.jspf" %>

<%@ include file="/theme/find_ajax_el/footer.jspf" %>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />