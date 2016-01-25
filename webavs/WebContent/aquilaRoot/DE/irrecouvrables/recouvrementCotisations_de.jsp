<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page isELIgnored ="false" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.aquila.db.irrecouvrables.CORecouvrementCotisationsViewBean"%>
<%
CORecouvrementCotisationsViewBean viewBean = (CORecouvrementCotisationsViewBean) session.getAttribute("viewBean");
userActionValue = "aquila.irrecouvrables.comptabilisationRecouvrement.executer";
idEcran = "GCO0044";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<ct:menuChange displayId="menu" menuId="CO-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CO-OptionsDefaut" showTab="menu"/>

<c:set var="postes_cumulCotisationAmortieCorrigee_prefix" value="postes_cumulCotisationAmortieCorrigee_" />
<c:set var="postes_cumulRecouvrementCotisationAmortieCorrigee_prefix" value="postes_cumulRecouvrementCotisationAmortieCorrigee_" />
<c:set var="postes_recouvrement_prefix" value="postes_recouvrement_" />
<c:set var="postes_solde_prefix" value="postes_solde_"/>
<c:set var="postes_cotisationAvs_prefix" value="postes_cotisationAvs_"/>
<c:set var="postes_revenuCi_prefix" value="postes_revenuCi_"/>
<c:set var="postes_Annee_prefix" value="postes_Annee_"/>
<c:set var="postes_montantNoteDeCredit_prefix" value="postes_montantNoteDeCredit_"/>
<c:set var="postes_type_prefix" value="postes_type_"/>

<c:set var="rectificationCI_montantEtatCi_prefix" value="rectificationCI_montantEtatCi_"/>
<c:set var="rectificationCI_montantRecouvrement_prefix" value="rectificationCI_montantRecouvrement_"/>
<c:set var="rectificationCI_soldeNet_prefix" value="rectificationCI_soldeNet_"/>

<style type="text/css">
.erreur {
	color:#f00;
	font-weight:bold;
}

.hiddenRow {
	display: none;
}

.visibleRow {
	display: table-row;
}

.activeRow {
	background-color: #2E87CC;
	color: #fff;
}

.colLibelle {
	width:460px;
}

</style>
<style type="text/css" media="print">

#btnOk, #btn_modifier_bases, .notDisplayForPrint {
	display: none;
}

#postes input {
	border:0;
	font-size:10pt;
	font-weight:normal;
}

#postes {
	border-collapse: collapse;
}
#postes, #postes th, #postes td {
	border: 1px solid black;
}

#postes th {
	background-color: #ccc;
	font-weight:bold;
}

</style>

<script type="text/javascript">
var montantARecouvrir = ${viewBean.montantARecouvrir};
var formHasError = false;
var formEditBaseHasError = false;
var formCIHasError = false;
var editModeEnabled = false;
var annee = <c:out value="${empty viewBean.annee ? 0: viewBean.annee}"/>;
var cumulCotisationAmortieTotalInitial = 0.0;

/**
 * Retourne le numéro de ligne correspondant au champ passé en paramètre
 */
function getNumLine(input) {
	var id = input.attr("id");
	var separatorPos = id.lastIndexOf('_');
	var numLine = id.substring(separatorPos + 1);
	
	return numLine;
}

/**
 * Met à jour le champ solde correspondant au champ passé en paramètre
 */
function updateSolde(input) {
	
	var numLine = getNumLine(input);
	var cumulCotisationAmortieCorrigee = parseInputMontant($('#${postes_cumulCotisationAmortieCorrigee_prefix}' + numLine));
	var cumulRecouvrementCotisationAmortieCorrigee = parseInputMontant($('#${postes_cumulRecouvrementCotisationAmortieCorrigee_prefix}' + numLine));
	var recouvrement = parseInputMontant($('#${postes_recouvrement_prefix}' + numLine));
	var newSolde = cumulCotisationAmortieCorrigee - cumulRecouvrementCotisationAmortieCorrigee - recouvrement;
	$('#${postes_solde_prefix}' + numLine).val(newSolde.toFixed(2));
}

/**
 * Mets à jour le montant de rectification CI en fonction du contenu recouvrement corrspondant à la ligne du champ passé en paramètre
 */
function updateRectificationCI(input) {
	var numLine = getNumLine(input);
	
	var annee = $("#${postes_Annee_prefix}"+numLine).val();
	var recouvrement = parseInputMontant($("#${postes_recouvrement_prefix}"+numLine));
	var revenuCi = parseInputMontant($("#${postes_revenuCi_prefix}"+numLine));
	var cotisationAvs = parseInputMontant($("#${postes_cotisationAvs_prefix}"+numLine));
	
	if(revenuCi > 0 && cotisationAvs > 0) {
		var rectificationCI = Math.floor(revenuCi * recouvrement / cotisationAvs);
		$("[data-montantrecouvrementci-annee="+annee+"]").val(rectificationCI);
		$("[data-montantrecouvrementci-annee="+annee+"]").change();
	}
}

/**
 * Met à jour le champ solde CI correspondant au champ passé en paramètre
 */
function updateSoldeCI(input) {
	
	var numLine = getNumLine(input);
	var montantEtatCi = parseInputMontant($('#${rectificationCI_montantEtatCi_prefix}' + numLine));
	var montantRecouvrement = parseInputMontant($('#${rectificationCI_montantRecouvrement_prefix}' + numLine));
	
	var newSolde = montantEtatCi + montantRecouvrement;
	
	$('#${rectificationCI_soldeNet_prefix}' + numLine).val(newSolde.toFixed(2));
	$('#${rectificationCI_soldeNet_prefix}' + numLine).change();
}



/**
 * Met à jour les lignes de totaux et de montant disponibles affectés
 */
function updateTotaux() {
	
	var totalcumulCotisationAmortie = 0.0;
	var totalcumulRecouvrementCotisationAmortie = 0.0;
	var totalRecouvrement = 0.0;
	var totalSolde = 0.0;
	
	$('#postes > tbody > tr').each(function() {
		var numLine = getNumLine($(this));
		
		totalcumulCotisationAmortie += parseInputMontant($('#${postes_cumulCotisationAmortieCorrigee_prefix}' + numLine));
		totalcumulRecouvrementCotisationAmortie += parseInputMontant($('#${postes_cumulRecouvrementCotisationAmortieCorrigee_prefix}' + numLine));
		totalRecouvrement += parseInputMontant($('#${postes_recouvrement_prefix}' + numLine));
		totalSolde = totalcumulCotisationAmortie - totalcumulRecouvrementCotisationAmortie - totalRecouvrement;
	});
	
	$('#total_cumulCotisationAmortie').val(totalcumulCotisationAmortie.toFixed(2));
	$('#total_cumulCotisationAmortie').change();
	$('#total_deja_recouvert').val(totalcumulRecouvrementCotisationAmortie.toFixed(2));
	$('#total_deja_recouvert').change();
	$('#total_recouvrement').val(totalRecouvrement.toFixed(2));
	$('#total_recouvrement').change();
	$('#total_solde').val(totalSolde.toFixed(2));
	$('#total_solde').change();
	
	
	var montantARecouvrir = parseInputMontant($('#montantARecouvrir'));
	var soldeAAffecter = montantARecouvrir - totalRecouvrement;
	
	$('#total_recouvrement_2').val(totalRecouvrement.toFixed(2));
	$('#total_recouvrement_2').change();
	$('#solde_a_affecter').val(soldeAAffecter.toFixed(2));
	$('#solde_a_affecter').change();
}

/**
 * Parse le montant se trouvant dans le champ passé en paramètre. Les espace, séparateur de miliers sont supprimés.
 */
function parseInputMontant(input) {
	return parseFloat(input.val().replace("'", "").replace(" ", ""));
}

/**
 * Vérifie les totaux. Si un montant n'est pas valide, la classe CSS "erreur" est ajoutée au champ correspondant
 */
function validateTotal() {
	
	var total_recouvrementVal = parseInputMontant($('#total_recouvrement'));
	var total_soldeVal = parseInputMontant($('#total_solde'));
	
	$('#total_solde').removeClass('erreur');
	$('#total_recouvrement').removeClass('erreur');
	
	if(total_recouvrementVal != montantARecouvrir) {
		$('#total_recouvrement').addClass('erreur');
 		formHasError = true;
 	}
	
	if(total_soldeVal < 0) {
		$('#total_solde').addClass('erreur');
 		formHasError = true;
 	}
}

/**
 * Vérifie les montants saisis par l'utilisateur et mets les éventuelles erreurs en évidence.
 */
function validateLine(numLine) {
	
	var cumulCotisationAmortieCorrigeeInput = $('#${postes_cumulCotisationAmortieCorrigee_prefix}' + numLine);
	var cumulRecouvrementCotisationAmortieCorrigeeInput = $('#${postes_cumulRecouvrementCotisationAmortieCorrigee_prefix}' + numLine);
	var recouvrementInput = $('#${postes_recouvrement_prefix}' + numLine);
	var soldeInput = $('#${postes_solde_prefix}' + numLine);
	
		
	var cumulCotisationAmortieCorrigeeVal = parseInputMontant(cumulCotisationAmortieCorrigeeInput);
	var dejaRevouvertVal = parseInputMontant(cumulRecouvrementCotisationAmortieCorrigeeInput);
	var recouvrementVal = parseInputMontant(recouvrementInput);
	var soldeVal = parseInputMontant(soldeInput);
	
	
	cumulCotisationAmortieCorrigeeInput.removeClass('erreur');
	cumulRecouvrementCotisationAmortieCorrigeeInput.removeClass('erreur');
	recouvrementInput.removeClass('erreur');
	soldeInput.removeClass('erreur');
	
	if(cumulCotisationAmortieCorrigeeVal < 0) {
		cumulCotisationAmortieCorrigeeInput.addClass('erreur');
		formEditBaseHasError = true;
		formHasError = true;
	}
	
	if(dejaRevouvertVal < 0) {
		cumulRecouvrementCotisationAmortieCorrigeeInput.addClass('erreur');
		formEditBaseHasError = true;
		formHasError = true;
	}
	
	if(recouvrementVal < 0) {
		recouvrementInput.addClass('erreur');
		formHasError = true;
	}
	
	if(soldeVal < 0) {
		soldeInput.addClass('erreur');
		formHasError = true;
	}
	
	if(recouvrementVal > cumulCotisationAmortieCorrigeeVal) {
		recouvrementInput.addClass('erreur');
		formHasError = true;
	}
	
 	if(soldeVal > cumulCotisationAmortieCorrigeeVal) {
 		soldeInput.addClass('erreur');
 		formHasError = true;
 	}
}

/**
 * Vérifie les montants saisis par l'utilisateur dans le tableau de rectification des CI
 * et mets les éventuelles erreurs en évidence.
 */
function validateLineCI(numLine) {
	var montantRecouvrementInput = $('#${rectificationCI_montantRecouvrement_prefix}' + numLine);
	var soldeNetInputInput = $('#${rectificationCI_soldeNet_prefix}' + numLine);

	var montantRecouvrement = parseInputMontant(montantRecouvrementInput);
	var soldeNet = parseInputMontant(soldeNetInputInput);
	
	montantRecouvrementInput.removeClass('erreur');
	soldeNetInputInput.removeClass('erreur');
	
	if(montantRecouvrement < 0) {
		montantRecouvrementInput.addClass('erreur');
		formCIHasError = true;
	}
	
	if(soldeNet < 0) {
		soldeNetInputInput.addClass('erreur');
		formCIHasError = true;
	}
}

/**
 * Vérifie le tableau complet en faisant appel à la fonction validateLine pour chacune des lignes
 */
function validateTable() {
	formHasError = false;
	formEditBaseHasError = false;
	
	$('#postes > tbody > tr').each(function() {
		var numLine = getNumLine($(this));
		validateLine(numLine);
	});
	
	validateTotal();
}

/**
 * Vérifie le tableau des CI en faisant appel à la fonction validateLineCI pour chacune des lignes
 */
function validateTableCI() {
	formCIHasError = false;
	
	$('#tableCI > tbody > tr').each(function() {
		var numLine = getNumLine($(this));
		validateLineCI(numLine);
	});
}

/**
 * Mets à jour l'état du bouton d'exécution en fonction des erreurs présentes dans le formulaire
 */
function updateEtatBtnOk() {
	if(formHasError || formCIHasError || editModeEnabled) {
		$('#btnOk').attr('disabled', 'disabled');	
	} else {
		$('#btnOk').removeAttr('disabled');
	}
}

/**
 * Mets à jour l'état du bouton d'actualisation en fonction des erreurs présente dans le formulaire ou du mode d'édition actuel
 */
function updateEtatBtnActualiserRecouvrement() {
	
	if(formEditBaseHasError) {
		$('#btn_modifier_bases').attr('disabled', 'disabled');
	} else {
		$('#btn_modifier_bases').removeAttr('disabled');
	}
}

/**
 * Appel l'action d'actualisation du recouvement
 */
function submitActualiserRecouvrement() {
	
	if(!editModeEnabled) {
		$('[name=userAction]').val('aquila.irrecouvrables.recouvrementCotisations.afficher');
		$('#postes > tbody > tr > td > input').removeAttr("disabled");
		$('#btnOk').attr('disabled', 'disabled');
		$('#btn_modifier_bases').attr('disabled', 'disabled');	
		document.forms[0].submit();
	}
}

/**
 * Change l'état des champs éditables du formulaire.
 */
function toggleEditModeBases() {
	
	$('.cumulCotisationAmortieCorrigee').each(function(){
		if(editModeEnabled) {
			$(this).attr('readonly', 'readonly');
			$(this).addClass('montantDisabled');
			$(this).removeClass('montant');
		} else {
			$(this).removeAttr('readonly');
			$(this).removeClass('montantDisabled');
			$(this).addClass('montant');
		}
	});
	
	$('.deja_couvert').each(function(){
		if(editModeEnabled) {
			$(this).attr('readonly', 'readonly');
			$(this).addClass('montantDisabled');
			$(this).removeClass('montant');
		} else {
			$(this).removeAttr('readonly');
			$(this).removeClass('montantDisabled');
			$(this).addClass('montant');
		}
	});
	
	$('.inputRecouvrement').each(function(){
		var $this = $(this);
		if(editModeEnabled) {
			$this.removeAttr('readonly');
			$this.removeClass('montantDisabled');
			$this.addClass('montant');
		} else {
			$this.attr('readonly', 'readonly');
			$this.addClass('montantDisabled');
			$this.removeClass('montant');
		}
	});
	
	if(editModeEnabled) {
		$('#btn_modifier_bases').html('<ct:FWLabel key="GCO0044_BTN_MODIFIER_BASES"/>');
	} else {
		$('#btn_modifier_bases').html('<ct:FWLabel key="GCO0044_BTN_ACTUALISER_RECOUVREMENT"/>');
	}
	
	editModeEnabled = !editModeEnabled;
}

function toggleLinesVisibility() {
	
	$('#postes > tbody > tr').each(function() {
		var numLine = getNumLine($(this));
		var cumulCotisationAmortieCorrigee = parseInputMontant($('#${postes_cumulCotisationAmortieCorrigee_prefix}' + numLine));
		var cumulRecouvrementCotisationAmortieCorrigee = parseInputMontant($('#${postes_cumulRecouvrementCotisationAmortieCorrigee_prefix}' + numLine));
		var recouvrement = parseInputMontant($('#${postes_recouvrement_prefix}' + numLine));
		
		if(editModeEnabled || cumulCotisationAmortieCorrigee > 0 || cumulRecouvrementCotisationAmortieCorrigee > 0 || recouvrement > 0) {
			$(this).removeClass('hiddenRow');
			$(this).addClass('visibleRow');
		} else {
			$(this).removeClass('visibleRow');
			$(this).addClass('hiddenRow');
		}
		
	});
	
	$('#postes > tbody > tr.hiddenRow > td > input').attr("disabled","disabled");
	$('#postes > tbody > tr.visibleRow > td > input').removeAttr("disabled");
}

function initTable() {
	
	if(cumulCotisationAmortieTotalInitial == 0) {
		toggleEditModeBases();
	}
	
	validateTable();
	toggleLinesVisibility();
	
	$('#postes > tbody > tr.hiddenRow > td > input').attr("disabled","disabled");
	$('#postes > tbody > tr.visibleRow > td > input').removeAttr("disabled");
	
	updateEtatBtnOk();
	updateEtatBtnActualiserRecouvrement();
}

function showActiveRow(field) {
	$('tr').removeClass('activeRow');
	field.parent().parent().addClass('activeRow');
}


$(document).ready(function() {
	
	$("#btnOk").val('<ct:FWLabel key="GCO0044_BTN_COMPTABILISER"/>');
	
	//désactivation du bouton OK au chargement
	$('#btnOk').attr('disabled', 'disabled');
	
	//désactivation du bouton OK après exécution
	$("#btnOk").click(function(){
		$(this).attr("disabled","disabled");
	});
	
	//activation/désactivation des champs "cumulCotisationAmortieCorrigee" au clic sur le bouton "Modifier les bases" 
	$('#btn_modifier_bases').click(function() {
		toggleEditModeBases();
		validateTable();
		toggleLinesVisibility();
		updateEtatBtnOk();
		updateEtatBtnActualiserRecouvrement();
		submitActualiserRecouvrement();
	});
	
	//mise à jour des montants
	$('.montantPoste').blur(function() {
		updateSolde($(this));
		updateRectificationCI($(this));
		updateTotaux();
		validateTable();
		updateEtatBtnOk();
		updateEtatBtnActualiserRecouvrement();
	});
	
	$( ".montantPoste" ).focus(function() {
		showActiveRow($(this));
	});
	
	$( ".montantCI" ).focus(function() {
		showActiveRow($(this));
	});
	
	//mise à jour des montants CI
	$('.montantCI').blur(function() {
		updateSoldeCI($(this));
		validateTableCI();
		updateEtatBtnOk();
	});
	
	initTable();
});

</script>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			<ct:FWLabel key="GCO0044_TITRE"/>
			<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
<tr><td colspan="3">
<!-- Affilié / Email -->
<table>
	<tr>
		<td><label for="affilie"><ct:FWLabel key="GCO0044_AFFILIE"/></label></td>
		<td><input type="text" style="width:450px" readonly="readonly" class="libelleLong disabled" value="${viewBean.numeroAffilie} ${viewBean.nomAffilie}"/></td>
	</tr>
	<tr class="notDisplayForPrint">
		<td><label for="email"><ct:FWLabel key="GCO0044_EMAIL"/></label></td>
		<td><input type="text" style="width:450px" name="email" id="email" class="libelleLong" value="${viewBean.email}"/></td>
	</tr>
</table>

<input type="hidden" name="montantNoteDeCreditCumul" id="montantNoteDeCreditCumul" value="${viewBean.ventilateur.montantNoteCredit}">
<input type="hidden" name="montantDisponible" id="montantDisponible" value="${viewBean.montantDisponible}">
<input type="hidden" name="montantARecouvrir" id="montantARecouvrir" value="${viewBean.montantARecouvrir}">
<input type="hidden" name="annee" id="annee" value="${viewBean.annee}"><br />
<input type="hidden" name="isInitialized" id="isInitialized" value="1">
<input type="hidden" name="idCompteAnnexe" id="idCompteAnnexe" value="${viewBean.idCompteAnnexe}">

<c:forEach var="idSection" items="${viewBean.idSectionsList}">
	<input type="hidden" name="idSectionsList" value="${idSection}">
</c:forEach>

<c:forEach var="base" items="${viewBean.selectedBasesAmortissement}">
	<input type="hidden" name="SelectedBasesAmortissement" value="${base}">
</c:forEach>



<input type="hidden" name="idCompteIndividuelAffilie" value="${viewBean.ventilateur.compteIndividuelAffilie.compteIndividuelId}">

<c:if test="${not empty viewBean.ventilateur.messageErreurList}">
	<div style="border:4px solid red">
		<ul>
			<c:forEach var="erreur" items="${viewBean.ventilateur.messageErreurList}">
				<li class="erreur">${erreur}</li>
			</c:forEach>
		</ul>
	</div>
</c:if>


<div style="text-align:center; margin:10px">
	<button id="btn_modifier_bases"><ct:FWLabel key="GCO0044_BTN_MODIFIER_BASES"/></button>
</div>
<table id="postes">
	<thead>
		<tr>
			<th class="colLibelle"><ct:FWLabel key="GCO0044_HEADER_RUBRIQUE_AMORTISSEMENT_ANNEE"/></th>
			<th><ct:FWLabel key="GCO0044_HEADER_AMORTISSEMENT"/></th>
			<th><ct:FWLabel key="GCO0044_HEADER_DEJA_RECOUVERT"/></th>
			<th><ct:FWLabel key="GCO0044_HEADER_RECOUVREMENT"/></th>
			<th><ct:FWLabel key="GCO0044_HEADER_SOLDE"/></th>
		</tr>
		</thead>
	<tbody>
		<c:set var="cumulCotisationAmortieTotal" value="${0}"/> 
		<c:set var="cumulRecouvrementCotisationAmortieTotal" value="${0}"/> 
		<c:set var="recouvrementTotal" value="${0}"/> 
		<c:set var="posteIncrement" value="${0}"/>
		<c:forEach var="poste" items="${viewBean.ventilateur.recouvrementPostesTries}">
			<c:choose>
			    <c:when test="${poste.value.cumulCotisationAmortie.unscaledValue() > 0}">
			    	<c:set var="rowVisibility" value="visibleRow"/>
			    </c:when>
			    <c:otherwise>
			        <c:set var="rowVisibility" value="hiddenRow"/>
			    </c:otherwise>
			</c:choose>
			
			<tr id="poste_line_${posteIncrement}" class="${rowVisibility}">
				<td>
					${poste.value.numeroRubriqueRecouvrement} ${poste.value.libelleRubriqueRecouvrement} ${poste.value.annee}<br />
					<input type="hidden" name="postes[${posteIncrement}][idRubriqueIrrecouvrable]" id="postes_idRubriqueIrrecouvrable_${posteIncrement}" value="${poste.value.idRubriqueIrrecouvrable}"/>
					<input type="hidden" name="postes[${posteIncrement}][idRubriqueRecouvrement]" id="postes_idRubriqueRecouvrement_${posteIncrement}" value="${poste.value.idRubriqueRecouvrement}"/>
					<input type="hidden" name="postes[${posteIncrement}][idRubrique]" id="postes_idRubrique_${posteIncrement}" value="${poste.value.idRubrique}"/>
					<input type="hidden" name="postes[${posteIncrement}][libelleRubriqueRecouvrement]" id="postes_libelleRubriqueRecouvrement_${posteIncrement}" value="${poste.value.libelleRubriqueRecouvrement}"/>
					<input type="hidden" name="postes[${posteIncrement}][numeroRubriqueIrrecouvrable]" id="postes_numeroRubriqueIrrecouvrable_${posteIncrement}" value="${poste.value.numeroRubriqueIrrecouvrable}"/>
					<input type="hidden" name="postes[${posteIncrement}][numeroRubriqueRecouvrement]" id="postes_numeroRubriqueRecouvrement_${posteIncrement}" value="${poste.value.numeroRubriqueRecouvrement}"/>
					<input type="hidden" name="postes[${posteIncrement}][ordrePriorite]" id="postes_ordrePriorite_${posteIncrement}" value="${poste.value.ordrePriorite}"/>
					<input type="hidden" name="postes[${posteIncrement}][annee]" id="${postes_Annee_prefix}${posteIncrement}" value="${poste.value.annee}"/>
					<input type="hidden" name="postes[${posteIncrement}][cumulCotisationAmortie]" id="postes_cumulCotisationAmortie_${posteIncrement}" value="${poste.value.cumulCotisationAmortie}"/>
					<input type="hidden" name="postes[${posteIncrement}][cumulRecouvrementCotisationAmortie]" id="postes_cumulRecouvrementCotisationAmortie_${posteIncrement}" value="${poste.value.cumulRecouvrementCotisationAmortie}"/>
					<input type="hidden" name="postes[${posteIncrement}][valeurInitialeCotAmortie]" id="postes_valeurInitialeCotAmortie_${posteIncrement}" value="${poste.value.valeurInitialeCotAmortie}"/>
     				<input type="hidden" name="postes[${posteIncrement}][valeurInitialeCotRecouvrement]" id="postes_valeurInitialeCotRecouvrement_${posteIncrement}" value="${poste.value.valeurInitialeCotRecouvrement}"/>
					<input type="hidden" name="postes[${posteIncrement}][cotisationAvs]" id="${postes_cotisationAvs_prefix}${posteIncrement}" value="${poste.value.cotisationAvs}"/>
					<input type="hidden" name="postes[${posteIncrement}][revenuCi]" id="${postes_revenuCi_prefix}${posteIncrement}" value="${poste.value.revenuCi}"/>
					<input type="hidden" name="postes[${posteIncrement}][montantNoteDeCredit]" id="${postes_montantNoteDeCredit_prefix}${posteIncrement}" value="${poste.value.montantNoteDeCredit}"/>
					<input type="hidden" name="postes[${posteIncrement}][type]" id="${postes_type_prefix}${posteIncrement}" value="${poste.value.type.codeSystem}"/>
				</td>
				<td><input type="text" name="postes[${posteIncrement}][cumulCotisationAmortieCorrigee]" id="${postes_cumulCotisationAmortieCorrigee_prefix}${posteIncrement}"  value="${poste.value.cumulCotisationAmortie}" data-g-amount readonly="readonly" class="montantDisabled cumulCotisationAmortieCorrigee montantPoste"/></td>
				<td><input type="text" name="postes[${posteIncrement}][cumulRecouvrementCotisationAmortieCorrigee]" id="${postes_cumulRecouvrementCotisationAmortieCorrigee_prefix}${posteIncrement}" value="${poste.value.cumulRecouvrementCotisationAmortie}" data-g-amount readonly="readonly" class="montantDisabled deja_couvert montantPoste"/></td>
				<td><input type="text" name="postes[${posteIncrement}][recouvrement]" id="${postes_recouvrement_prefix}${posteIncrement}" value="${poste.value.recouvrement}" data-g-amount class="inputRecouvrement montant montantPoste"/></td>
				<td><input type="text" name="postes[${posteIncrement}][solde]" id="${postes_solde_prefix}${posteIncrement}" value="${poste.value.cumulCotisationAmortie - poste.value.cumulRecouvrementCotisationAmortie - poste.value.recouvrement}" data-g-amount readonly="readonly" class="montantDisabled montantPoste"/></td>
				
				<c:set var="cumulCotisationAmortieTotal" value="${cumulCotisationAmortieTotal + poste.value.cumulCotisationAmortie}"/> 
				<c:set var="cumulRecouvrementCotisationAmortieTotal" value="${cumulRecouvrementCotisationAmortieTotal + poste.value.cumulRecouvrementCotisationAmortie}"/> 
				<c:set var="recouvrementTotal" value="${recouvrementTotal + poste.value.recouvrement}"/>
				<c:set var="posteIncrement" value="${posteIncrement +1}"/>
			</tr>
			
		</c:forEach>
	</tbody>
	<tfoot>
		<tr>
			<th class="colLibelle"><ct:FWLabel key="GCO0044_HEADER_TOTAUX"/></th>
			<td><input type="text" name="total_cumulCotisationAmortie" id="total_cumulCotisationAmortie" value="${cumulCotisationAmortieTotal}" data-g-amount readonly="readonly" class="montantDisabled montantPoste"/></td>
			<td><input type="text" name="total_deja_recouvert" id="total_deja_recouvert" value="${cumulRecouvrementCotisationAmortieTotal}" data-g-amount readonly="readonly" class="montantDisabled montantPoste"/></td>
			<td><input type="text" name="total_recouvrement" id="total_recouvrement" value="${recouvrementTotal}" data-g-amount readonly="readonly" class="montantDisabled montantPoste"/></td>
			<td><input type="text" name="total_solde" id="total_solde" value="${cumulCotisationAmortieTotal - cumulRecouvrementCotisationAmortieTotal - recouvrementTotal}" data-g-amount readonly="readonly" class="montantDisabled montantPoste"/></td>
		</tr>
		<tr>
			<th><ct:FWLabel key="GCO0044_HEADER_MONTANT_DISPONIBLE_AFFECTE"/></th>
			<td>&nbsp;</td>
			<td><input type="text" name="montantARecouvrir" id="montantARecouvrir" value="${viewBean.montantARecouvrir}" data-g-amount readonly="readonly" class="montantDisabled"/></td>
			<td><input type="text" name="total_recouvrement_2" id="total_recouvrement_2" value="${recouvrementTotal}" data-g-amount readonly="readonly" class="montantDisabled"/></td>
			<td><input type="text" name="solde_a_affecter" id="solde_a_affecter" value="${viewBean.montantARecouvrir - recouvrementTotal}" data-g-amount readonly="readonly" class="montantDisabled"/></td>
		</tr>
	</tfoot>
</table>
<script type="text/javascript">
cumulCotisationAmortieTotalInitial = ${cumulCotisationAmortieTotal};
</script>
<hr />
<c:if test="${fn:length(viewBean.ventilateur.recouvrementCiTries) gt 0}">
	<div>
		<c:set var="hasRecouvrementPosteOnError" value="${viewBean.hasRecouvrementPosteOnError}"/>
		<c:choose>
			<c:when test="${hasRecouvrementPosteOnError}">
				<p class="erreur"><ct:FWLabel key="GCO0044_WARNING_RECTIFICATION_CI"/></p>
		    	<input type="hidden" name="effectuerRectificationCI" id="effectuerRectificationCI" value="0" />
			</c:when>
			<c:otherwise>
				<label for="effectuerRectificationCI">
					<input type="checkbox" name="effectuerRectificationCI" id="effectuerRectificationCI" checked="checked" />
					<ct:FWLabel key="GCO0044_EFFECTUER_RECTIFICATION"/>
				</label>
				<span>
					<ct:ifhasright element="pavo.compte.ecriture.chercherEcriture" crud="r">	
						(<a href="<%=request.getContextPath()%>/pavo?userAction=pavo.compte.ecriture.chercherEcriture&compteIndividuelId=${viewBean.ventilateur.compteIndividuelAffilie.compteIndividuelId}"><ct:FWLabel key="GCO0044_VOIR_EXTRAIT_CI"/></a>)
					</ct:ifhasright>
				</span>
			</c:otherwise>
		</c:choose>
	</div>
	<table id="tableCI">
		<thead>
			<tr>
				<th><ct:FWLabel key="GCO0044_HEADER_RECTIFICATION_CI"/></th>
				<th><ct:FWLabel key="GCO0044_HEADER_ETAT_CI"/></th>
				<th><ct:FWLabel key="GCO0044_HEADER_RECTIFICATION"/></th>
				<th><ct:FWLabel key="GCO0044_HEADER_SOLDE_NET"/></th>
			</tr>
		</thead>
		<c:set var="ciIncrement" value="${0}"/>
		<tbody>
			<c:forEach var="ci" items="${viewBean.ventilateur.recouvrementCiTries}">
				<tr id="ci_line_${posteIncrement}" >
					<td style="width:650px">
						<ct:FWLabel key="GCO0044_RECTIFICATION"/> ${ci.value.annee}
						<input type="hidden" name="rectificationCI[${ciIncrement}][annee]" id="${rectificationCI_annee_prefix}${ciIncrement}" value="${ci.value.annee}"/>
						<input type="hidden" name="rectificationCI[${ciIncrement}][genreDecision]" value="${ci.value.genreDecision}"/>	
					</td>
					<td><input name="rectificationCI[${ciIncrement}][montantEtatCi]" id="${rectificationCI_montantEtatCi_prefix}${posteIncrement}" value="${ci.value.montantEtatCi}" data-g-amount readonly="readonly" class="montantDisabled montantCI" /></td>
					<td><input data-montantrecouvrementci-annee="${ci.value.annee}" name="rectificationCI[${ciIncrement}][montantRecouvrement]" id="${rectificationCI_montantRecouvrement_prefix}${posteIncrement}" value="${ci.value.montantRecouvrement}" data-g-amount class="montant montantCI"/></td>
					<td><input name="rectificationCI[${ciIncrement}][soldeNet]" id="${rectificationCI_soldeNet_prefix}${posteIncrement}" value="${ci.value.montantEtatCi + ci.value.montantRecouvrement}" data-g-amount readonly="readonly" class="montantDisabled montantCI" /></td>
				</tr>
				
				<c:set var="ciIncrement" value="${ciIncrement +1}"/>
			</c:forEach>
		</tbody>
	</table>
</c:if>
</td></tr>
<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>