<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT1600"/>
<c:set var="labelTitreEcran" value="JSP_SYNDICAT"/>

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>

<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<c:set var="travailleur" value="${viewBean.travailleur}" />
<script type="text/javascript">
//fonctions de bases à redéfinir
globazGlobal.isNouveau = ${viewBean.nouveau};
globazGlobal.idTravailleur = ${viewBean.idTravailleur};
globazGlobal.decompteSalaireViewService = '${viewBean.decompteSalaireViewSerivce}';

function add() {
	
}

function upd() {
}

function validate() {
	if (document.forms[0].elements('_method').value == "add"){
		document.forms[0].elements('userAction').value="vulpecula.syndicat.syndicat.ajouter";
	}
	else
		document.forms[0].elements('userAction').value="vulpecula.syndicat.syndicat.modifier";
	return (true);
}

function cancel() {
	document.forms[0].elements('userAction').value="vulpecula.postetravailvueglobale.travailleurvueglobale.afficher";
	document.forms[0].elements('selectedId').value="${viewBean.idTravailleur}";
	document.forms[0].elements('tab').value="syndicats";
	document.forms[0].elements('_method').value="";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?"))
	{
		document.forms[0].elements('userAction').value="vulpecula.syndicat.syndicat.supprimer";
		document.forms[0].submit();
	}
}

function init() {
}

globazGlobal.syndicats = (function() {
	var montantVide;
	var $dateDebut;
	var $dateFin;
	var $montantTotal;
	var $listeDetailsDecompteByYear;
	
	function init() {
		montantVide = '0.00';
		$dateDebut = $('#dateDebut');
		$dateFin = $('#dateFin');
		$montantTotal = $('#montantTotal');
		
		if(!globazGlobal.isNouveau) {
			searchCumulSalairesIfPossible();
		}
		//searchDetailsDecompteGroupByYear(globazGlobal.idTravailleur, $dateDebut.val(), $dateFin.val());
		initEvents();
	}
	
	function initEvents() {
		$dateDebut.add($dateFin).change(function () {
			searchCumulSalairesIfPossible();
		});
	}
	
	function searchDetailsDecompteGroupByYear(idTravailleur, dateDebut, dateFin) {
		var dateDebutTime = 0;
		var dateFinTime = 0;
		if(dateDebut.length > 0) {
			dateDebutTime = getDateFromFormat(dateDebut, "dd.MM.yyyy");
		}
		if(dateFin.length > 0) {
			dateFinTime = dateFin;
		}
		var options = {
				serviceClassName:globazGlobal.decompteSalaireViewService,
				serviceMethodName:'getDetailsDecompte',
				parametres:idTravailleur + ',' + dateDebut + ',' + dateFinTime,
				callBack:function (data) {
					$listeDetailsDecompteByYear = data;
					console.log($listeDetailsDecompteByYear);
				}
		};
		vulpeculaUtils.lancementService(options);
	}
	
	function searchCumulSalairesIfPossible() {
		var dateDebut = $dateDebut.val();
		var dateFin = $dateFin.val();
		if(dateDebut.length > 0) {
			var dateDebutTime = getDateFromFormat(dateDebut, "dd.MM.yyyy");
			var dateFinTime = 0;
			if(dateFin.length > 0) {
				dateFinTime = getDateFromFormat(dateFin, "dd.MM.yyyy");
			}
			if(dateFin==0) {
				searchCumulSalairesSansDateFin(globazGlobal.idTravailleur, dateDebut);
			}
			else if(dateFinTime>=dateDebutTime) {
				searchCumulSalaires(globazGlobal.idTravailleur, dateDebut, dateFin);
			} else {
				$montantTotal.val(montantVide);
			}
		} else {
			$montantTotal.val(montantVide);
		}		
	}
	
	function searchCumulSalairesSansDateFin(idTravailleur, dateDebut) {
		var options = {
				serviceClassName:globazGlobal.decompteSalaireViewService,
				serviceMethodName:'cumulSalairesSyndicatWithCotisationsCPRSansDateFin',
				parametres:idTravailleur + ',' + dateDebut,
				callBack:function (data) {
					$montantTotal.val(data);
				}
		};
		vulpeculaUtils.lancementService(options);
	}
	
	function searchCumulSalaires(idTravailleur, dateDebut, dateFin) {
		var options = {
				serviceClassName:globazGlobal.decompteSalaireViewService,
				serviceMethodName:'cumulSalairesSyndicatWithCotisationsCPR',
				parametres:idTravailleur + ',' + dateDebut + ',' + dateFin,
				callBack:function (data) {
					$montantTotal.val(data);
				}
		};
		vulpeculaUtils.lancementService(options);
	}
	return {
		init : init
	}
})();

//chargement du dom jquery
jsManager.executeAfter = function () {
	globazGlobal.syndicats.init();
};
</script>


<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<span class="postItIcon" data-g-note="idExterne:${travailleur.id}, tableSource:PT_TRAVAILLEURS">
</span>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<div class="content">
	<div class="blocLeft">
		<div class="bloc blocMedium">
			<%@ include file="/vulpeculaRoot/blocs/travailleur.jspf" %>
		</div>
	</div>
</div>
<div style="margin-top: 24px;">
	<table>
		<tr>
			<td><ct:FWLabel key="JSP_SYNDICAT"/></td>
			<td>
				<input id="syndicat"
				class="jadeAutocompleteAjax"
				name="tiersWidget"
				value="${viewBean.libelleSyndicat}"
				type="text"
				data-g-autocomplete="service:¦ch.globaz.vulpecula.business.services.administration.AdministrationService¦,
									 method:¦find¦,
									 criterias:¦{'forDesignation1Like':'Désignation'}¦,
									 constCriterias:¦forGenreAdministration=68900006¦,
									 lineFormatter:¦#{tiers.designation1} #{tiers.designation2}¦,
									 modelReturnVariables:¦tiers.id,tiers.designation1,tiers.designation2¦,nbReturn:¦20¦,
									 functionReturn:¦
									 	function(element){
									 		this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
									 		var idTiers = $(element).attr('tiers.id');
									 		$('#idSyndicat').val(idTiers);
									 	}¦
									 ,nbOfCharBeforeLaunch:¦3¦"
				/>
			</td colspan="3">
				<input type="hidden" name="idSyndicat" value="${viewBean.idSyndicat}" id="idSyndicat"/>
			</td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_PERIODE"/></td>
			<td>
				<input id="dateDebut" name="dateDebut" type="text" data-g-calendar="" value="${viewBean.dateDebutAsSwissValue}" />
			</td>
			<td>
				<ct:FWLabel key="JSP_FIN"/>
			</td>
			<td>
				<input id="dateFin" name="dateFin" type="text" data-g-calendar="" value="${viewBean.dateFinAsSwissValue}" />
			</td>
		</tr>
		<tr>
			<td><ct:FWLabel key="JSP_SALAIRE_POUR_LA_PERIODE"/></td>
			<td>
				<input id="montantTotal" data-g-amount="" class="readOnly" readonly="readonly" value="" type="text" />
			</td>
		</tr>
	</table>
	<table style="margin-top:15px;">
		<tr>
			<th><ct:FWLabel key="JSP_SYNDIC_ANNEE" /></th>
			<th><ct:FWLabel key="JSP_SYNDIC_SALAIRE_ANNUEL" /></th>
			<th><ct:FWLabel key="JSP_SYNDIC_TAUX" /></th>
			<th><ct:FWLabel key="JSP_SYNDIC_COTISATION_BRUT" /></th>
			<th><ct:FWLabel key="JSP_SYNDIC_POURCENTAGE" /></th>
			<th><ct:FWLabel key="JSP_SYNDIC_COTISATION_NET" /></th>
			<th><ct:FWLabel key="JSP_SYNDIC_FRAIS" /></th>
		</tr>
  		<c:forEach var="detaisDecompteByYear" items="${viewBean.detailsDecomptesSalairesParAnnee}">
		<tr style="text-align: right;" class="bmsRowEven">
			<td style="padding-left:10px;">${detaisDecompteByYear.annee}</td>
			<td style="padding-left:10px;">${detaisDecompteByYear.salaireAnnuel}</td>
			<td style="padding-left:10px;">${detaisDecompteByYear.taux}</td>
			<td style="padding-left:10px;">${detaisDecompteByYear.cotisationBrute}</td>
			<td style="padding-left:10px;">${detaisDecompteByYear.pourcentage}</td>
			<td style="padding-left:10px;">${detaisDecompteByYear.cotisationNette}</td>
			<td style="padding-left:10px;">${detaisDecompteByYear.frais}</td>
		</tr>
  		</c:forEach>
	</table>
</div>

<input name="tab" type="hidden" value="" />
<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>
<%@ include file="/theme/detail_el/footer.jspf" %>