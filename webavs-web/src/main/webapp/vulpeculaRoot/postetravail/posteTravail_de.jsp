<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT1105"/>
<c:set var="labelTitreEcran" value="JSP_POSTE_TRAVAIL"/>

<c:set var="bButtonNew" value="false" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="${rootPath}/css/vulpecula.css"/>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/json2.js"></script>
<script type="text/javascript" src="${rootPath}/postetravail/posteTravail_de.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/utils/jquery.noty.packaged.min.js"></script>


<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/jadeTemplate" id="templateTauxOccupation">
<tr class="taux" data-g-clone="disabled:${!viewBean.nouveau}, deleteAlwaysDisabled:false">
	<td><input type="text" class="dateValidite" value="" data-g-calendar="mandatory:true"></td>
	<td><input type="text" class="tauxOccupation" value="" data-g-rate="nbMaxDecimal:2"></td>	
</tr>
</script>

<script type="text/javascript">
globazGlobal.posteTravailService = '${viewBean.posteTravailAjaxService}';
globazGlobal.posteTravailViewService =  '${viewBean.posteTravailViewService}';
globazGlobal.cotisationService =  '${viewBean.cotisationService}';
globazGlobal.addWithTravailleur = ${viewBean.addWithTravailleur};
globazGlobal.addWithEmployeur = ${viewBean.addWithEmployeur};
globazGlobal.messageSuppression = '${viewBean.messageSuppression}';
globazGlobal.messageDecompteSurPeriode = '${viewBean.messageDecompteSurPeriode}';
globazGlobal.messageCotisationNonComprise = "${viewBean.messageCotisationNonComprise}";
globazGlobal.isNouveau = ${viewBean.nouveau};
globazGlobal.affiliationCaisseMaladie = '${viewBean.affiliationCaisseMaladie}';
globazGlobal.posteCorrelationId = '${viewBean.posteCorrelationId}';
</script>


<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<div id="informations" style="position:absolute;right:0; width: 30%;"></div>

<c:if test="${viewBean.posteCorrelationId != ''}">
	<div id="linkToAnnonce" style="float:right">
		<td colspan="2" style="text-align:right;font-weight:bold"><a href="${pageContext.request.contextPath}\vulpecula?userAction=vulpecula.ebusiness.nouveauTravailleur.afficher">
		<ct:FWLabel key='JSP_RETOUR_ANNONCES'/></a></td>
	</div>
</c:if>




<div id="content">
	<div id="blocLeft">
	</div>
</div>

<input id="occupations" name="occupations" type="hidden" />
<input id="cotisations" name="cotisations" type="hidden" />
<input id="spy" value="<c:out value="${viewBean.spy}" />" type="hidden" />
<c:if test="${viewBean.addWithTravailleur}">
	<input name="addWithTravailleur" type="hidden" value="on" />
</c:if>
<c:if test="${viewBean.addWithEmployeur}">
	<input name="addWithEmployeur" type="hidden" value="on" />
</c:if>

<table width="100%">
	<tr>
		<td>
			<label><ct:FWLabel key='JSP_NUMERO'/></label>
			<span id="idPosteTravail"><c:out value="${viewBean.id}"/></span>
		</td>
		<td>
			<label><ct:FWLabel key='JSP_CONVENTION_ID'/></label>
			<input id="idConventionForm" name="idConvention" type="hidden" />
			<span id="idConvention">
				<c:if test="${not empty viewBean.conventionNo}">
					<c:out value="${viewBean.conventionNo} - ${viewBean.conventionDesignation}"/>
				</c:if>
			</span>
		</td>
	</tr>
	<tr>
		<td>
			<ct:FWLabel key='JSP_TRAVAILLEUR'/></br>
				<input id="dateNaissanceTravailleur" name="dateNaissanceTravailleur" type="hidden" value="${viewBean.posteTravail.travailleur.dateNaissance}"/>
				<input id="sexeTravailleur" name="sexeTravailleur" type="hidden" value="${viewBean.posteTravail.travailleur.sexe}"/>
				<input id="idTravailleur" name="idTravailleur" type="hidden" value="${viewBean.posteTravail.travailleur.id}"/>
				<input id="descriptionTravailleur"
				class="jadeAutocompleteAjax"
				name="tiersWidget"
				value="${viewBean.descriptionTravailleur}"
				type="text"
				data-g-autocomplete="service:¦${viewBean.travailleurServiceCRUD}¦,
									 method:¦search¦,
									 criterias:¦{'likeNom':'Nom','likePrenom':'Prenom','forNumAvs':'NSS	','forDateNaissance':'Date de naissance'}¦,
									 lineFormatter:¦#{personneEtendueComplexModel.tiers.designation1} #{personneEtendueComplexModel.tiers.designation2} #{personneEtendueComplexModel.personneEtendue.numAvsActuel} #{personneEtendueComplexModel.personne.dateNaissance}¦,
									 modelReturnVariables:¦travailleurSimpleModel.id,personneEtendueComplexModel.tiers.designation1,personneEtendueComplexModel.tiers.designation2,personneEtendueComplexModel.personne.dateNaissance,personneEtendueComplexModel.personne.sexe¦,nbReturn:¦20¦,
									 functionReturn:¦
									 	function(element){
											$('#idTravailleur').val($(element).attr('travailleurSimpleModel.id'));
											$('#dateNaissanceTravailleur').val($(element).attr('personneEtendueComplexModel.personne.dateNaissance'));
											$('#sexeTravailleur').val($(element).attr('personneEtendueComplexModel.personne.sexe'));
											this.value=$(element).attr('personneEtendueComplexModel.tiers.designation1')+' '+$(element).attr('personneEtendueComplexModel.tiers.designation2');
									 	}¦
									 ,nbOfCharBeforeLaunch:¦3¦
									 ,mandatory:true"
				/>
		</td>
		<td>
		<ct:FWLabel key='JSP_EMPLOYEUR'/></br>
			<input id="idAffiliation" name="idAffiliation" type="hidden" value="${viewBean.posteTravail.employeur.id}" />
					<input id="descriptionEmployeur" class="jadeAutocompleteAjax" name="employeurNumero" type="text" value="${viewBean.descriptionEmployeur}"
						data-g-autocomplete="
							service:¦${viewBean.employeurService}¦,
							method:¦search¦,
							criterias:¦{'likeNumeroAffilie': 'No affilié', 'likeDesignationTiersUpper': 'designation'}¦,
							lineFormatter:¦#{affiliationTiersComplexModel.affiliation.affilieNumero} #{affiliationTiersComplexModel.affiliation.raisonSociale}¦,
							modelReturnVariables:¦affiliationTiersComplexModel.affiliation.affiliationId,affiliationTiersComplexModel.affiliation.affilieNumero,affiliationTiersComplexModel.affiliation.raisonSociale,affiliationTiersComplexModel.affiliation.idConvention,administrationComplexModel.admin.codeAdministration,administrationComplexModel.tiers.designation1¦,
							functionReturn:¦
								function(element) {
									var idAffilie = $(element).attr('affiliationTiersComplexModel.affiliation.affiliationId');
									var idConvention = $(element).attr('affiliationTiersComplexModel.affiliation.idConvention');
									var code = $(element).attr('administrationComplexModel.admin.codeAdministration')
									var raisonSociale = $(element).attr('affiliationTiersComplexModel.affiliation.raisonSociale');
									var affilieNumero = $(element).attr('affiliationTiersComplexModel.affiliation.affilieNumero');
									var convention = $(element).attr('administrationComplexModel.tiers.designation1');
									
									$('#idAffiliation').val(idAffilie).change();
									$('#idConvention').text(code + ' - ' + convention)
									$('#idConventionForm').val(idConvention).change();
									
									GLO.cotisations.reloadIfPossible();
									
									this.value= affilieNumero+','+raisonSociale;
									
							}
						¦,
						nbOfCharBeforeLaunch:¦3¦,
						mandatory:true"
			/>
		</td>
	</tr>
	<tr>
		<td>
		<ct:FWLabel key='JSP_PERIODE_ACTIVITE'/></br>
		<input type="text" id="debutActivite" name="debutActivite" value="<c:out value="${viewBean.debutActivite}"/>" data-g-calendar="mandatory:true" />-
		<input type="text" id="finActivite" name="finActivite" value="<c:out value="${viewBean.finActivite}"/>" data-g-calendar=" " />
		</td>
		<td>
		<ct:FWLabel key='JSP_QUALIFICATIONS'/></br>
		<select id="qualification" name="qualification">
			<option value=""></option>
			<c:forEach var="codeSystem" items="${viewBean.listQualification}">
				<c:choose>
					<c:when test="${codeSystem.id==viewBean.qualification}">
						<option value="${codeSystem.id}" selected="selected">${codeSystem.libelle}</option>
					</c:when>
					<c:otherwise>
						<option value="${codeSystem.id}">${codeSystem.libelle}</option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select>
		</td>
	</tr>	
	<tr>
		<td>
			<ct:FWLabel key='JSP_GENRE_SALAIRE'/></br>
			<ct:FWCodeSelectTag notation="data-g-select='mandatory:true'" name="genreSalaire" codeType="PTGENRESAL" defaut="${viewBean.genreSalaire}" wantBlank="true"/>
		</td>
		<td class="aCacher">
			<label><ct:FWLabel key='JSP_POSTE_FRANCHISE_AVS'/></label><br />
			<c:choose>
   				<c:when test="${viewBean.posteFranchiseAvs}">
					<input id="posteFranchiseAvs" name="posteFranchiseAvs" type="checkbox" checked="checked" />
				</c:when>
				<c:otherwise>
					<input id="posteFranchiseAvs" name="posteFranchiseAvs" type="checkbox" />
				</c:otherwise>
			</c:choose>
		</td>
		<td>
		</td>
	</tr>
	<tr>
		<td>
			<label><ct:FWLabel key='JSP_CAISSE_MALADIE'/></label><br />
			<select id="caisseMaladie" name="caisseMaladie">
			<c:choose>
				<c:when test="${viewBean.nouveau}">
					<c:forEach var="caisseMaladie" items="${viewBean.listCaisseMaladie}">
							<option value="${caisseMaladie.idTiers}">${caisseMaladie.designation1}</option>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${not empty viewBean.affiliationCourante}">
							<option>${viewBean.affiliationCourante.libelleCaisseMaladie}</option>
						</c:when>
						<c:otherwise>
							<option><ct:FWLabel key="JSP_AUCUNE_CAISSE"/></option>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
			</select>
		</td>
		
		<td>
			<table  id="tmplRowTauxOccupation">
				<thead>
					<tr id="excludeHeader">
						<th><ct:FWLabel key='JSP_DATE_VALIDITE'/></th>
						<th><ct:FWLabel key='JSP_TAUX_OCCUPATION'/></th>
					</tr>
				</thead>
				<tbody id="tableTauxOccupation">
					<c:forEach var="occupation" items="${viewBean.posteTravail.occupations}">
						<tr class="taux">		
							<td><input type="text" class="dateValidite" value="${occupation.dateValiditeAsValue}" data-g-calendar="mandatory:true"></td>
							<td><input type="text" class="tauxOccupation" value="${occupation.tauxAsValue}" data-g-rate="nbMaxDecimal:2"></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</td>
		
	</tr>	
					
</table>
<div id="zoneCotisations" style="width: 80%; margin-top: 30px;"></div>
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>

				<input type="hidden" name="selectedId" value="${selectedIdValue}">
				<input type="hidden" name="userAction" value="${userActionValue}">
				<input type="hidden" name="_method" value="${param['_method']}">
				<input type="hidden" name="_valid" value="${param['_valid']}">
				<input type="hidden" name="_sl" value="">
				<input type="hidden" name="selectorName" value="">
			</form>
	 	</div>
		
			</div>
			<c:if test="${not empty creationSpy or not empty lastModification}">
				<div class="lastModification">
						<c:out value="${creationSpy}" /> Update: <c:out value="${lastModification}" />
				</div>	
			</c:if>
			<c:choose>
				<c:when test="${autoShowErrorPopup || !vBeanHasErrors}">
				</c:when>
				<c:otherwise>
				[ <a id=\"showErrorLink\" href=\"javascript:showErrors();\">visualiser les erreurs</a> ]
				</c:otherwise>
			</c:choose>
			<c:if test="${bButtonNew || bButtonUpdate || bButtonDelete || bButtonValidate || bButtonCancel }">
				<div style="background-color=#FFFFFF;height:18;text-align: right;" id="btnCtrlJade">
				
				
					<c:if test="${bButtonNew}">
						<input class="btnCtrl" type="button" id="btnNew" value="${btnNewLabel}" onclick="onClickNew();btnNew.onclick='';hideAllButtons();document.location.href='${actionNew}'" />
					</c:if>
					
					<c:if test="${bButtonUpdate}">
						<input class="btnCtrl" id="btnUpd" type="button" value="${btnUpdLabel}" onclick="action(UPDATE);upd();"/>
					</c:if>
					
					<c:if test="${bButtonDelete}">
						<input class="btnCtrl" id="btnDel" type="button" value="${btnDelLabel}" onclick="del();"/>
					</c:if>
					
					
					<c:if test="${bButtonValidate}">
						<input class="btnCtrl" id="btnVal" type="button" value="${btnValLabel}" onclick="validate()"/>
					</c:if>
					
					<c:if test="${bButtonCancel}">
						<input class="btnCtrl" id="btnCan" type="button" value="${btnCanLabel}" onclick="cancel();"/>
					</c:if>
				</div>
			</c:if>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />