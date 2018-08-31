<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1"%>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf"%>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT1110" />
<c:set var="labelTitreEcran" value="JSP_DECOMPTE_NOUVEAU" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf"%>
<script type="text/javascript"
	src="${rootPath}/scripts/monthDateUtil.js"></script>
<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">
	globazGlobal.CS_CONTROLE_EMPLOYEUR = ${viewBean.csControleEmployeur};
	globazGlobal.CS_SPECIAL = ${viewBean.csSpecial};
	globazGlobal.CS_SPECIAL_CAISSE = ${viewBean.csSpecialCaisse};
	globazGlobal.CS_CPP = ${viewBean.csCPP};
	globazGlobal.NUM_CT = "${viewBean.numCT}";
	globazGlobal.validationTousChampsRemplies = "${viewBean.validationTousChampsRemplies}";
	//fonctions de bases à redéfinir

	function add() {
	}

	function upd() {
	}

	function validate() {
		state = true;
		var $mandatories = $('.mandatory:visible');
		for (var i = 0; i < $mandatories.size(); i++) {
			if ($($mandatories[i]).val().length == 0) {
				showErrorDialog(globazGlobal.validationTousChampsRemplies);
				return false;
			}
		}
		document.forms[0].elements('userAction').value = "vulpecula.decomptenouveau.decomptenouveau.ajouter";
		return state;
	}

	function cancel() {
		document.forms[0].elements('userAction').value = "back";
	}

	function del() {
	}

	function init() {
	}

	//chargement du dom jquery
	jsManager.executeAfter = function() {
		$datePeriodeDe = $('#DATE_PERIODE_DE');
		$datePeriodeA = $('#DATE_PERIODE_A');

		monthDateUtil.bind($datePeriodeDe, $datePeriodeA);

		$datePeriodeA.change(function() {
			periodeFin = $(this).val();
			if (periodeFin.length > 0) {
				var annee = periodeFin.split(".")[1];
				var numeroDecompte = annee + globazGlobal.NUM_CT + "000";
				$('#numeroDecompte').val(numeroDecompte);
			}
		});

		$('#typeDecompte').change(function() {
			var $trNumeroDecompte = $('#trNumeroDecompte');
			var $withPostes = $('#withPostes');
			var $toHide = $('.toHide');
			var $email = $('#email');
			var $numeroDecompte = $('#numeroDecompte');

			$trNumeroDecompte.hide();
			$('.hideable').show();
			if (isSPorCT()) {
				$withPostes.prop('checked', false);
				if (isSP()) {
					$('.toHideSP').hide();
				} else if (isCT()) {
					$trNumeroDecompte.show();
					$('.toHideCT').hide();
				}
			}
		});

		$('#withPostes').change(function() {
			if ($(this).is(':checked')) {
				$('#email').show();
			} else {
				$('#email').hide();
			}
		});

		function isSPorCT() {
			return isSP() || isCT();
		}

		function isSP() {
			var typeDecompte = getTypeDecompte();
			return typeDecompte == globazGlobal.CS_SPECIAL || typeDecompte == globazGlobal.CS_CPP || typeDecompte == globazGlobal.CS_SPECIAL_CAISSE;
		}

		function isCT() {
			var typeDecompte = getTypeDecompte();
			return typeDecompte == globazGlobal.CS_CONTROLE_EMPLOYEUR;
		}

		function getTypeDecompte() {
			return $('#typeDecompte').val();
		}
	};
</script>


<%@ include file="/theme/detail_el/bodyStart.jspf"%>
<ct:FWLabel key="${labelTitreEcran}" />
<%@ include file="/theme/detail_el/bodyStart2.jspf"%>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<div>
	<table>
		<tr>
			<td width="240px;"><label><ct:FWLabel
						key='JSP_PROCESS_DECOMPTE_TYPE' /></label></td>
			<td>
				<select id="typeDecompte" name="typeDecompte" /> 
					<c:forEach
						var="codeSystem" items="${viewBean.typesDecomptes}">
						<option value="${codeSystem.id}">${codeSystem.libelle}</option>
					</c:forEach>
				</select>
			</td>
		</tr>
		<tr>
			<td><label><ct:FWLabel key='JSP_PROCESS_DECOMPTE_DATE' /></label></td>
			<td><input name="dateDecompte" data-g-calendar="mandatory:true"
				value="" /></td>
		</tr>
		<tr class="hideable">
			<td><label><ct:FWLabel
						key='JSP_PROCESS_DECOMPTE_EMPLOYEUR_SPECIAL' /></label></td>
			<td><input class="jadeAutocompleteAjax libelleLong" type="text"
				id="employeurNumeroLibelle" value="${viewBean.designationEmployeur}"
				data-g-autocomplete="
					service:¦ch.globaz.naos.business.service.AffiliationService¦,
					method:¦widgetFind¦,
					criterias:¦{'likeNumeroAffilie': 'No affilié', 'likeDesignationUpper': 'designation'}¦,
					lineFormatter:¦#{affiliation.affilieNumero} #{affiliation.raisonSociale}¦,
					modelReturnVariables:¦affiliation.affiliationId,affiliation.affilieNumero,affiliation.raisonSociale¦,
					constCriterias:¦inTypeAffiliationString='804002'¦,
					functionReturn:¦
						function(element) {
							var idAffilie = $(element).attr('affiliation.affiliationId');
							$('#employeurNumero').val(idAffilie);
						this.value=$(element).attr('affiliation.affilieNumero')+','+$(element).attr('affiliation.raisonSociale');
					}
				¦,
				nbOfCharBeforeLaunch:¦3¦,mandatory:true">
				<input type="hidden" id="employeurNumero" name="idEmployeur"
				value="${viewBean.idEmployeur}"></td>
		</tr>
		<tr>
			<td><label><ct:FWLabel
						key='JSP_PROCESS_DECOMPTE_PERIODE' /></label></td>
			<td><input name="periodeDebut" id="DATE_PERIODE_DE"
				data-g-calendar="type:month,mandatory:true" value="" /> &nbsp;<ct:FWLabel
					key='JSP_PROCESS_DECOMPTE_A' /> &nbsp;<input name="periodeFin"
				id="DATE_PERIODE_A" data-g-calendar="type:month,mandatory:true"
				value="" /></td>
		</tr>
		<tr id="trNumeroDecompte" style="display: none;">
			<td><label><ct:FWLabel key='JSP_DECOMPTE_NUM' /></label></td>
			<td><input id="numeroDecompte" name="numeroDecompte"
				data-g-integer="mandatory:true,autoFit:true,sizeMax:10" value="" /></td>
		</tr>
		<tr class="hideable toHideCT toHideSP">
			<td><label><ct:FWLabel
						key='JSP_PROCESS_DECOMPTE_AVEC_POSTES_ET_DOCUMENTS' /></label></td>
			<td><input type="checkbox" name="withPostes" id="withPostes"
				checked="checked" /></td>
		</tr>
		<tr class="hideable toHideCT toHideSP" id="email">
			<td><label for="email"><ct:FWLabel key="JSP_EMAIL" /></label></td>
			<td><input type="text" name="email" value="${viewBean.email}" /></td>
		</tr>
	</table>
</div>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal"
	showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf"%>

<%@ include file="/theme/detail_el/bodyErrors.jspf"%>

<%@ include file="/theme/detail_el/footer.jspf"%>