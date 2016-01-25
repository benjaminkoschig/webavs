<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT1102"/>
<c:set var="labelTitreEcran" value="JSP_TRAVAILLEURS_DETAIL"/>

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">
//fonctions de bases à redéfinir

function add () {
	document.forms[0].elements('userAction').value="vulpecula.postetravaildetail.travailleurdetail.ajouter";
}

function upd() {
	$('#descriptionTiers').attr('disabled','disabled');
}

function validate() {
	 state = validateFields();
	 if (document.forms[0].elements('_method').value == "add")
	     document.forms[0].elements('userAction').value="vulpecula.postetravaildetail.travailleurdetail.ajouter";
	 else 
	     document.forms[0].elements('userAction').value="vulpecula.postetravaildetail.travailleurdetail.modifier";
	 return state;
}

function cancel() {
	document.forms[0].elements('userAction').value="vulpecula.postetravail.travailleur.afficher";
}

function del() {
	if (window.confirm('<ct:FWLabel key="JSP_MESSAGE_SUPPRESSION"/>')){
	    document.forms[0].elements('userAction').value="vulpecula.postetravaildetail.travailleurdetail.supprimer";
	    document.forms[0].submit();
	}
}

function init(){
}
//chargement du dom jquery
$(function () {
	
});
</script>


<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<div>
	<table>
		<c:if test="${not empty viewBean.travailleur.id && viewBean.travailleur.id != 'null'}">
			<tr>
				<td><a href="vulpecula?userAction=vulpecula.postetravailvueglobale.travailleurvueglobale.afficher&selectedId=${viewBean.travailleur.id}">Retour à la vue globale</a></td>
			</tr>
		</c:if>
		<tr>
			<td><label><ct:FWLabel key='JSP_NUMERO'/></label></td> 
			<td><c:out value="${viewBean.travailleur.id}"/></td>
			<input type="hidden" name="id" value="<c:out value="${viewBean.id}"/>"/> 
		</tr>	
		<tr>
			<td><ct:FWLabel key='JSP_TRAVAILLEUR'/></td>
			<td><input type="hidden" id="currentEntity.simpleTravailleur.idTiers"/>
				<input id="descriptionTiers"
				class="jadeAutocompleteAjax"
				name="tiersWidget"
				value="${viewBean.nomPrenomTravailleur}"
				type="text"
				data-g-autocomplete="service:¦${viewBean.travailleurServiceCRUD}¦,
									 method:¦searchForNewTravailleur¦,
									 criterias:¦{'forDesignation1Like':'Nom','forDesignation2Like':'Prenom','forNumeroAvsActuel':'NSS	'}¦,
									 lineFormatter:¦#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}¦,
									 modelReturnVariables:¦tiers.designation1,tiers.designation2,personne.dateNaissance,personne.sexe,tiers.id,personneEtendue.numAvsActuel¦,nbReturn:¦20¦,
									 functionReturn:¦
									 	function(element){
									 		this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
									 		$('#idTiers').val($(element).attr('tiers.id'));
									 	}¦
									 ,nbOfCharBeforeLaunch:¦3¦
									 ,mandatory:true""
				/></td>
				<input type="hidden" name="idTiers" value="${viewBean.idTiers}" id="idTiers"/>
		</tr>
	
		<tr>
			<td><label><ct:FWLabel key='JSP_ANNONCE_MEROBA'/></label></td>
	        <td>
		        <input type="checkbox" name="annonceMeroba" <c:out value="${viewBean.isAnnonceMeroba}"/> />
				<c:out value="${viewBean.dateAnnonceMeroba}"/>
			</td>
		</tr>
		<tr>
			<td><label><ct:FWLabel key='JSP_PERMIS_TRAVAIL'/></label> <label><ct:FWLabel key='JSP_REFERENCE_PERMIS_TRAVAIL'/></label></td>
			<td><ct:FWCodeSelectTag name="permisTravail" codeType="PTGENREPER" wantBlank="true" defaut="${viewBean.permisTravail}"/></td>
			<td><input type="text" name="referencePermis" value="<c:out value="${viewBean.referencePermis}"/>" /></td>
		</tr>
	</table>
</div>

<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>