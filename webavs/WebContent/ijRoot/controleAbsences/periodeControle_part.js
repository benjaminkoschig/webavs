function initZoneAjaxPeriodeControleAbsences() {

	var $zoneAjaxSaisieAbsence = $('#zoneAjaxSaisieAbsence');
	var $boutonPeriodeControleAbsences = $('#boutonPeriodeControleAbsences');

	var ajaxObj = defaultTableAjax.init({
		s_container : '#zoneAjaxPeriodeControleAbsences',
		s_actionAjax : globazGlobal.actionAjaxPeriodeControleAbsences,

		getParametresForFind : function() {
			return {
				'searchModel.forIdDossier' : globazGlobal.idDossier
			};
		},

		afterStartEdition : function() {
			var $detailZone = $('#zoneDetailPeriodeControleAbsences');
			$detailZone.dialog('open');
		},

		beforeStartEdition : function() {
			this.mainContainer = $('#zoneDetailPeriodeControleAbsences');
		},

		init : function() {
			this.capage(5, [ 10, 20, 30, 50, 100 ]);
		}
	});

	detailZoneDialog.init({
		$zone : $('#zoneDetailPeriodeControleAbsences'),
		o_ajaxObject : ajaxObj[0],
		s_selecteurDeclencheur : '.showDetailPeriodeControleAbsences',
		f_validate : function() {
			return true;
		},
		
		f_callbackOk: function () {
			this.o_ajaxObject.validateEdition();
		},
		s_titleLabel : globazGlobal.labelTitrePeriodeControle
	});

	$zoneAjaxSaisieAbsence.bind(eventConstant.AJAX_UPDATE_COMPLETE, function() {
		ajaxObj[0].ajaxFind(false);
	});

	$boutonPeriodeControleAbsences
			.click(function() {
				var options = {
					serviceClassName : 'ch.globaz.ij.business.services.IJPeriodeControleAbsencesService',
					serviceMethodName : 'create',
					parametres : globazGlobal.idDossier,
					wantInitThreadContext : true,
					callBack : function(data) {
						// Reloading the list after adding a new dossier de contrôle
						ajaxObj[0].ajaxFind(false);
						
					}
				};

				globazNotation.readwidget.options = options;
				globazNotation.readwidget.read();
			});
}

$(document).ready(function() {
	initZoneAjaxPeriodeControleAbsences();
});
