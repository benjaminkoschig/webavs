function initZoneAjaxBaseIndemnisation() {

	var ajaxObj = defaultDetailAjax
			.init({
				s_detail : '#zoneAjaxDetailBaseIndeminsation',
				s_actionAjax : globazGlobal.actionAjaxBaseIndemnisation,
				n_idEntity : globazGlobal.idBaseIndemnisation,
				b_hasButtonNew : false,
				selectedEntityId : globazGlobal.idBaseIndemnisation,
				s_entityIdPath: 'baseIndemnisation.idBaseIndemnisation',

				afterRetrieve : function() {
					manageBaseIndemnisartionId();
					manageButtons();
				},

				init : function() {
					this.ajaxLoadEntity();
				},

				getParametres : function() {
					return {
						'absence.idBaseIndemnisation' : globazGlobal.idBaseIndemnisation,
						'idPrononce' : globazGlobal.idPrononce
					};
				},
				
				getParametersForLoad : function(){
					return {'baseIndemnisation.idPrononce' : globazGlobal.idPrononce};
				}

			});

	$('#boutonSupprimerBaseIndemnisation').click(function() {
		ajaxObj[0].ajaxDeleteEntity(globazGlobal.idBaseIndemnisation);
		globazGlobal.idBaseIndemnisation = undefined;
		ajaxObj[0].selectedEntityId = globazGlobal.idBaseIndemnisation;
		manageButtons();
	});

	 var $zoneAjaxSaisieAbsence = $('#zoneAjaxSaisieAbsence');
	 $zoneAjaxSaisieAbsence.bind(eventConstant.AJAX_UPDATE_COMPLETE,
	 function() {
	 		ajaxObj[0].ajaxLoadEntity();
	 });
	 
	
	$('#zoneAjaxDetailBaseIndeminsation').bind(
			eventConstant.AJAX_UPDATE_COMPLETE, function() {
				ajaxObj[0].ajaxLoadEntity();
				manageBaseIndemnisartionId();
				manageButtons();
			});
	
	function desactiverSousMenu(){
		desactiveUnSousMenu('#calculergp');
		desactiveUnSousMenu('#showPrestBase');
		desactiveUnSousMenu('#corrigerbi');
		desactiveUnSousMenu('#showFormIndemn');

	}
	
	function desactiveUnSousMenu(selecteur){
		var $frame = $(top.frames["fr_menu"].document);
		if($frame != undefined && $frame != null){
			var $element = $frame.find(selecteur);
			if($element != undefined && $element != null){
				$element.removeAttr('onclick');
				$element.addClass('disabled');
			}
		}
	}
	
	function activeUnSousMenu(selecteur, onClickAction){
		var $frame = $(top.frames["fr_menu"].document);
		if($frame != undefined && $frame != null){
			var $element = $frame.find(selecteur);
			if($element != undefined && $element != null){
				$element.attr('onclick', onClickAction);
				$element.removeClass('disabled');
			}
		}
	}
	
	function activerSousMenu(){
		var calculergpAction = "doAction('" + globazGlobal.contextPath + "/ij?userAction=ij.acor.calculACORDecompte.afficher&selectedId="+globazGlobal.idBaseIndemnisation+"&forNoBaseIndemnisation="+globazGlobal.idBaseIndemnisation+"&csTypeIJ=&_method=upd', 'fr_main');";
		var showPrestBaseAction = "doAction('" + globazGlobal.contextPath + "/ij?userAction=ij.prestations.prestationJointLotPrononce.chercher&selectedId="+globazGlobal.idBaseIndemnisation+"&forNoBaseIndemnisation="+globazGlobal.idBaseIndemnisation+"', 'fr_main');";
		var corrigerbiAction = "doAction('" + globazGlobal.contextPath + "/ij?userAction=ij.basesindemnisation.baseIndemnisation.creerCorrection&selectedId="+globazGlobal.idBaseIndemnisation+"&forNoBaseIndemnisation="+globazGlobal.idBaseIndemnisation+"', 'fr_main');";
		var showFormIndemnAction = "doAction('" + globazGlobal.contextPath + "/ij?userAction=ij.basesindemnisation.formulaireIndemnisation.chercher&selectedId="+globazGlobal.idBaseIndemnisation+"&forNoBaseIndemnisation="+globazGlobal.idBaseIndemnisation+"', 'fr_main');";
		activeUnSousMenu('#calculergp', calculergpAction);
		activeUnSousMenu('#showPrestBase', showPrestBaseAction);
		activeUnSousMenu('#corrigerbi', corrigerbiAction);
		activeUnSousMenu('#showFormIndemn', showFormIndemnAction);
	}
	
	
	$('#zoneAjaxDetailBaseIndeminsation').bind(eventConstant.AJAX_CHANGE,
			function() {
				manageBaseIndemnisartionId();
				manageButtons();
			});

	$('#zoneAjaxDetailBaseIndeminsation').bind(
			eventConstant.AJAX_FIND_COMPLETE, function() {
				manageBaseIndemnisartionId();
				manageButtons();
			});

	manageBaseIndemnisartionId();
	manageButtons();

	function manageBaseIndemnisartionId() {
		var id = document
				.getElementById("baseIndemnisation.idBaseIndemnisation").value;
		if (isNaN(id) == true) {
			globazGlobal.idBaseIndemnisation = 0;
			ajaxObj[0].selectedEntityId = undefined;
		} else {
			globazGlobal.idBaseIndemnisation = id;
			ajaxObj[0].selectedEntityId = id;
		}
	}

	function baseIndemnisationExist() {
		var id = globazGlobal.idBaseIndemnisation;
		if (isNaN(id) || id == undefined || id == 0 || id == "0") {
			return false;
		} else {
			return true;
		}
	}

	function manageButtons() {
		// 52412001 - OUVERT
		// 52412002 - VALIDE
		// 52412003 - COMMUNIQUE
		// 52412004 - ANNULE
		
		var boutonValider = $('.btnAjaxValidate');
		var boutonModifier = $('#btnAjaxUpdate');
		var boutonSupprimer = $('#boutonSupprimerBaseIndemnisation');
		var etat = document.getElementById("baseIndemnisation.etatCS").value;

		if (baseIndemnisationExist()) {
			if ((etat == 52412003) || (etat == 52412004)) {
				boutonModifier.hide();
				boutonSupprimer.hide();
				
			} else {
				boutonModifier.show();
				boutonSupprimer.show();
				
			}
			activerSousMenu();
			boutonModifier.attr('value', globazGlobal.actionAjaxLabelBaseIndemnisationBoutonModifier);
		} else {
			boutonSupprimer.hide();
			boutonValider.hide();
			desactiverSousMenu();
			boutonModifier.attr('value', globazGlobal.actionAjaxLabelBaseIndemnisationBoutonCreer);
		}
	}
}

$(document).ready(function() {
	initZoneAjaxBaseIndemnisation();
});
