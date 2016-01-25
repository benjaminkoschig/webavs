<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT4004"/>
<c:set var="labelTitreEcran" value="JSP_PRIME_NAISSANCE_AF"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<c:set var="userActionListe" value="vulpecula.listes" />
<ct:checkRight var="hasCreateRightOnListes" element="${userActionListe}" crud="c" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/utils/jquery.noty.packaged.min.js"></script>

<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">
globazGlobal.messageTousChampsRemplis = '${viewBean.messageTousChampsRemplis}';
function add () {
}

function upd() {
}

function validate() {
}

function cancel() {
}

function del() {
}

function init(){
}

function postInit() {
	$('input').removeProp('disabled');
	$('select').removeProp('disabled');
}

$(function () {
	
	$('#launch').click(function () {
		var idTravailleur = $('#idTravailleur').val();
		var dateNaissance = $('#dateNaissance').val();
		var nomEnfant = $('#nomEnfant').val();
		
		if(idTravailleur.length==0 || dateNaissance.length ==0 || nomEnfant.length==0) {
			showErrorDialog(globazGlobal.messageTousChampsRemplis);
			return;
		}
		
		$("#launch").prop('disabled', true);
		document.forms[0].elements('userAction').value="vulpecula.is.primeNaissanceAF.executer";
		document.forms[0].submit();
	});
})
</script>
<style type="text/css">
	#mainWrapper {
		height: auto !important;
	}
</style>


<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<div id="informations" style="position: absolute; right:0px; width: 30%">
</div>
<div style="width: 100%;text-align: center;">
	<div>
		<table>
			<tr>
				<td><label for="email"><ct:FWLabel key="JSP_EMAIL"/></label></td>
				<td><input id="email" type="text" name="email" value="${viewBean.email}" /></td>
			</tr>
			<tr>
				<td><ct:FWLabel key='JSP_TRAVAILLEUR'/></td>
				<td>
				<input id="idTravailleur" name="idTravailleur" type="hidden" value="${viewBean.idTravailleur}"/>
						<input id="descriptionTravailleur"
						class="jadeAutocompleteAjax"
						name="descriptionTravailleur"
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
													this.value=$(element).attr('personneEtendueComplexModel.tiers.designation1')+' '+$(element).attr('personneEtendueComplexModel.tiers.designation2');
													
											 	}¦
											 ,nbOfCharBeforeLaunch:¦3¦
											 ,mandatory:true"
						/>
				</td>
			</tr>
			<tr>
				<td><label for="dateNaissance"><ct:FWLabel key="JSP_DATE_NAISSANCE"/></label></td>
				<td><input data-g-calendar="mandatory:true" id="dateNaissance" type="text" name="dateNaissance" value="${viewBean.dateNaissance}" />
			</tr>
			<tr>
				<td><label for="nomEnfant"><ct:FWLabel key="JSP_NOM_ENFANT" /></label></td>
				<td><input id="nomEnfant" name="nomEnfant" type="text" value="${viewBean.nomEnfant}" />
			</tr>
			<c:if test="${not processLaunched}">	
			<tr>
				<td style="text-align: center;" colspan="2">
					<br />
					<input id="launch" type="button" name="launch" value='<ct:FWLabel key="JSP_LANCER"/>'/>
				</td>
			</tr>
			</c:if>
		</table>
	</div>
</div>
<c:if test="${processLaunched}">
	<div style="margin-top:20px;vertical-align:middle; color: white; font-weight: bold; text-align: center;background-color: green">
		<ct:FWLabel key="FW_PROCESS_STARTED"/>
	</div>
</c:if>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>