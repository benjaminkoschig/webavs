<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT5001"/>
<c:set var="labelTitreEcran" value="JSP_SAISIE_RAPIDE_PRESTATIONS"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="${rootPath}/css/vulpecula.css"/>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/utils/jquery.noty.packaged.min.js"></script>
<script type="text/javascript" src="${rootPath}/prestations/prestationsServices.js"></script>
<script type="text/javascript" src="${rootPath}/prestations/saisierapidePart.js"></script>
<%--  *************************************************************** Script propre à la page **************************************************************** --%>

<script type="text/javascript">
globazGlobal.prestationsViewService='${viewBean.prestationsViewService}';

globazGlobal.usersService='${viewBean.usersService}';

globazGlobal.genrePrestationAJ = '${viewBean.genrePrestationAJ}';
globazGlobal.genrePrestationCP = '${viewBean.genrePrestationCP}';
globazGlobal.genrePrestationSM = '${viewBean.genrePrestationSM}';

globazGlobal.ajouterSuccesLibelle = '${viewBean.ajouterSuccesLibelle}';

function getGenrePrestation(){
	return "forGenrePrestation="+$('#genrePrestation').val();
}
</script>

<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<style>
body {
width: 100%;
}
#innerWrapper {
	margin: 0px;
}
</style>

<div id="showSucces" style="float:right;width:30%;"></div>

<div>
	<table>
		<tr>
			<td>
				<ct:FWLabel key='JSP_GENRE_PRESTATION'/>
			</td>
			<td>
				<ct:FWCodeSelectTag name="genrePrestation" codeType="PTPRESTA" defaut="${viewBean.typePrestation}"/>
			</td>
		</tr>
		<tr>
			<td><br/></td>
		</tr>
		<tr>
		<td>
			<ct:FWLabel key='JSP_TRAVAILLEUR'/>
		</td>
		<td>
				<input id="idTravailleur" name="idTravailleur" type="hidden" value="${viewBean.idTravailleur}"/>
				<input id="descriptionTravailleur"
				class="jadeAutocompleteAjax"
				name="tiersWidget"
				value="${viewBean.nomPrenomTravailleur}"
				type="text"
				data-g-autocomplete="service:¦ch.globaz.vulpecula.business.services.travailleur.TravailleurServiceCRUD¦,
									 method:¦search¦,
									 criterias:¦{'likeNom':'Nom','likePrenom':'Prenom','forNumAvs':'NSS	','forDateNaissance':'Date de naissance'}¦,
									 lineFormatter:¦#{personneEtendueComplexModel.tiers.designation1} #{personneEtendueComplexModel.tiers.designation2} #{personneEtendueComplexModel.personneEtendue.numAvsActuel} #{personneEtendueComplexModel.personne.dateNaissance}¦,
									 modelReturnVariables:¦travailleurSimpleModel.id,personneEtendueComplexModel.tiers.designation1,personneEtendueComplexModel.tiers.designation2¦,nbReturn:¦20¦,
									 functionReturn:¦
									 	function(element){
									 		var idTravailleur = $(element).attr('travailleurSimpleModel.id');
											if(!hasRights(idTravailleur)){
												alert('${viewBean.pasDroitsLibelle}');
											}else{
												$('#idTravailleur').val(idTravailleur);
												$('#idTravailleur').trigger('change');
												this.value=$(element).attr('personneEtendueComplexModel.tiers.designation1')+' '+$(element).attr('personneEtendueComplexModel.tiers.designation2');
											}
									 	}¦
									 ,nbOfCharBeforeLaunch:¦3¦
									 ,mandatory:true"
				/>
				<span id="adressePaiementTravailleur"/>
		</td>
		</tr>
	</table>
</div>

	<iframe id="frame" style="display:block; border:none; width: 100%; height: 600px;" src="">
	</iframe>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>