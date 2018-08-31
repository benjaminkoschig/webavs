<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/find_ajax_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° écran et titre --%>
<c:set var="idEcran" value="PPT1011" />
<c:set var="labelTitreEcran" value="JSP_COTISATIONS_ASSOCIATIONS" />
<c:set var="bButtonDelete" value="false" />

<!-- TO BE ABLE TO USE DIFFERENT THEME, UNIMPORT JAVASCRIPT.JSPF FOR /find_ajax_el/ and manually add missing ones -->
<%--  <%@ include file="/theme/find_ajax_el/javascripts.jspf" %>  --%>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/ajaxUtils.js"/></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/AbstractScalableAJAXTableZone.js"/></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/AbstractSimpleAJAXDetailZone.js"/></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/ajax/templateZoneAjax.css"/>

<%@ include file="/theme/detail_el/javascripts.jspf" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjax.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaTable.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/registre/detailParamCotiAP_de.js"></script>

<script type="text/javascript">
	globazGlobal.ACTION_AJAX = '${userActionListerAjax}';
	$(window).resize(function() { 
		$('#mainWrapper').css('width', '100%').css('width', '-=10px'); });
	function add () {
	}

	function upd() {
	}

	function validate() {
		state = true;
		 if (document.forms[0].elements('_method').value == "add") {
		     document.forms[0].elements('userAction').value="vulpecula.registre.detailParamCotiAP.ajouter";
		 } else {
		     document.forms[0].elements('userAction').value="vulpecula.registre.detailParamCotiAP.modifier";
		 }
		 return state;
	}

	function cancel() {
		 if (document.forms[0].elements('_method').value == "add"){
			 document.forms[0].elements('userAction').value="back"; 
		 }else{
			 document.forms[0].elements('userAction').value="vulpecula.registre.detailParamCotiAP.reAfficher";
			 document.forms[0].elements('_method').value="";
		 }
	}

	function init(){
		//reduce 100§ size of div who cause scrollbar
		$('#mainWrapper').css('width', '100%').css('width', '-=10px');
		if (document.forms[0].elements('_method').value == "add") {document.getElementById('paramButtons').style.display='none';};
			
	}
</script>

<!-- BEGINING OF THE DETAIL PART (DETAIL OF A COTISATION_AP) -->
<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<%-- 	<ct:FWLabel key="${labelTitreEcran}"/> --%>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<!-- FORCE CSS STYLE ON SOME OVERWRITED BY /detail_el/bodyStart -->
<style type="text/css">
body {
	background-color: ${appColor};
	font-family: Verdana, Arial, sans-serif;
	font-size: 12px;
}

table {
	font-family: Verdana, Arial, sans-serif;
	font-size: 12px;
}

input {
	font-family: Verdana, Arial, sans-serif;
	font-size: 1em;
	font-weight: bold;
	vertical-align: middle;
}

input[type=text] {
	height: 16px;
}


select {
	font-weight: bold;
	font-size: 1em;
	display:inline-block;
	vertical-align:middle;
	height:24px; 
}
 
.jadeAutocompleteAjax{
	height: 16px;
}

.thDetail, th {
	font-family: Verdana, Arial, sans-serif;
	font-size: 12px;
	padding-bottom: 4px;
}
</style>
<tr>
	<td>
	<table>
		<tr>
			<td><ct:FWLabel key="JSP_ASSOCIATION_PROFESSIONNELLE"/></td>
			<td colspan="3">
			<select class="associationProfessionnelle" id="idAssociationProfessionnelle" name="idAssociationProfessionnelle" tabindex="2">
				<c:forEach var="associationProfessionnelle" items="${viewBean.associationsProfessionnelles}">
					<c:choose>
						<c:when test="${associationProfessionnelle.id==viewBean.idAssociationProfessionnelle}">
							<option selected="selected" value="${associationProfessionnelle.id}"><c:out value="${associationProfessionnelle.codeAdministration} - ${associationProfessionnelle.designation1} ${associationProfessionnelle.designation2}" /></option>
						</c:when>
						<c:otherwise>
							<option value="${associationProfessionnelle.id}"><c:out value="${associationProfessionnelle.codeAdministration} - ${associationProfessionnelle.designation1} ${associationProfessionnelle.designation2}" /></option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select>
			</td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_LIBELLE"/></td>
			<td><input id="libelle" name="libelle" class="libelleLong" type="text" value="${viewBean.cotiAP.libelle}" tabindex="1" ></span></td>
			<td><ct:FWLabel key="JSP_LIBELLE_FR"/></td>
			<td><input id="libelleFR" name="libelleFR" class="libelleLong" type="text" value="${viewBean.cotiAP.libelleFR}" tabindex="5"></span></td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_GENRE"/></td>
			<td><ct:FWCodeSelectTag notation="data-g-select=''" name="genre" codeType="PTGENCOTAP" defaut="${viewBean.genre}" wantBlank="false" tabindex="3"/></td>
			<td><ct:FWLabel key="JSP_LIBELLE_DE"/></td>
			<td><input id="libelleDE" name="libelleDE" class="libelleLong" type="text" value="${viewBean.cotiAP.libelleDE}" tabindex="6"></span></td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_MASSE_SALARIALE_DEFAUT"/></td>
			<td><input id="masseSalarialeDefaut" name="masseSalarialeDefaut" class="taux" data-g-amount="" type="text" value="${viewBean.cotiAP.masseSalarialeDefaut}" tabindex="4"/>%</td>
			<td><ct:FWLabel key="JSP_LIBELLE_IT"/></td>
			<td><input id="libelleIT" name="libelleIT" class="libelleLong" type="text" value="${viewBean.cotiAP.libelleIT}" tabindex="7"></span></td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_A_FACTURER_DEFAUT"/></td>
			<td><ct:FWCodeSelectTag notation="data-g-select=''" name="facturerDefaut" codeType="PTCATCOTAP" defaut="${viewBean.facturerDefaut}" wantBlank="false" tabindex="8"/></td>
			<td><ct:FWLabel key="JSP_PRINT_ORDER"/></td>
			<td><input id="printOrder" name="printOrder" class="numero" type="text" value="${viewBean.cotiAP.printOrder}" tabindex="9"></span></td>
		</tr>
		
		<tr>
			<td><ct:FWLabel key="JSP_RUBRIQUE"/></td>
			<td colspan="2">
			<input 
			id="currentEntity.rubriqueSimpleModel.idExterne"
			class="jadeAutocompleteAjax"
			type="text"
			tabindex="9"
			value="${viewBean.idExterneRubrique}"
			data-g-autocomplete="service:¦ch.globaz.vulpecula.business.services.rubrique.RubriqueService¦,
								 method:¦find¦,
								 criterias:¦{'likeIdExterne':'Id externe'}¦,
								 lineFormatter:¦#{idExterne}¦,
								 modelReturnVariables:¦idRubrique,idExterne¦,nbReturn:¦20¦,
								 functionReturn:¦
								 	function(element){
								 		this.value=$(element).attr('idExterne');
								 		$('#idRubrique').val($(element).attr('idRubrique'));	
								 	}¦
								 ,nbOfCharBeforeLaunch:¦3¦"
			/></td>
			<td>
			<input type="hidden" value="${viewBean.cotiAP.idRubrique}" id="idRubrique" name="idRubrique"/>
			</td>
		</tr>
			
	</table>
	</td>
</tr>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<!-- END OF DETAIL PART -->

<%@ include file="/theme/find_ajax_el/bodyStart.jspf" %>
	<span>${viewBean.titre}</span>
<%@ include file="/theme/find_ajax_el/bodyStart2.jspf" %>
<tr>
	<td>
	<div class="area">
	<!-- detail de la cotisationAP a éditer -->
		<div style="display:none;" class="areaSearch">
				<input style="display:none;" type="text" id="searchModel.forIdCotisationAssociationProfessionnelle" value="${viewBean.idCotisationAP}"></input>
		</div>		
		<div>
			<table class="areaTable" width="100%">
				<thead>
					<tr>
						<th><ct:FWLabel key="JSP_NO"/></th>
						<th><ct:FWLabel key="JSP_COTISATION"/></th>
						<th><ct:FWLabel key="JSP_GENRE"/></th>
						<th><ct:FWLabel key="JSP_TYPE"/></th>
						<th><ct:FWLabel key="JSP_TAUX"/></th>
						<th><ct:FWLabel key="JSP_FACTEUR"/></th>
						<th><ct:FWLabel key="JSP_MONTANT"/></th>
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
								<input style="display:none;" id="currentEntity.parametreCotisationAssociationSimpleModel.idCotisationAssociationProfessionnelle"  />
							</td>
						</tr>
						<tr>
							<td><ct:FWLabel key="JSP_TYPE"/></td>
							<td><ct:FWCodeSelectTag notation="data-g-select=''" name="currentEntity.parametreCotisationAssociationSimpleModel.typeParam" codeType="PTTYPCOTAP" defaut="currentEntity.parametreCotisationAssociationSimpleModel.typeParam" wantBlank="false"/></td>
						</tr>
						<tr id="trTaux">
							<td>
								<ct:FWLabel key="JSP_TAUX"/>
							</td>
							<td>
								 <input id="currentEntity.parametreCotisationAssociationSimpleModel.taux" class="taux" name="currentEntity.cotisationCaisseMetierSimpleModel.taux" data-g-rate="nbMaxDecimal:5" />%  
							</td>
						</tr>
						<tr id="trFacteur">
							<td>
								<ct:FWLabel key="JSP_FACTEUR"/>
							</td>
							<td>
								 <input id="currentEntity.parametreCotisationAssociationSimpleModel.facteur" name="currentEntity.cotisationCaisseMetierSimpleModel.facteur" data-g-amount="" default="0.00" wantBlank="false"/>  
							</td>
						</tr>
						<tr id="trMontant">
							<td>
								<ct:FWLabel key="JSP_MONTANT"/>
							</td>
							<td>
								 <input id="currentEntity.parametreCotisationAssociationSimpleModel.montant" name="currentEntity.cotisationCaisseMetierSimpleModel.montant" data-g-amount="" />  
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
			<div id="paramButtons" class="buttons">
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