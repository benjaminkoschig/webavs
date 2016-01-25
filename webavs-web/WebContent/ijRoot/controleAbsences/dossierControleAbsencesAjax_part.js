function initZoneAjaxDossierControleAbsences() {
	defaultDetailAjax.init({
		s_detail : '#zoneAjaxDetailDossierControleAbsences',
		s_actionAjax : globazGlobal.actionAjaxDossier,
		n_idEntity : globazGlobal.idDossier,

		init : function() {
			this.ajaxLoadEntity();
		}
	});

	// Gestion de l'historisation du dossier
	var $boutonHistoriser = $('#boutonHistoriser');
	$boutonHistoriser
			.click(function() {
				var options = {
					serviceClassName : 'ch.globaz.ij.business.services.IJDossierControleAbsenceService',
					serviceMethodName : 'historiser',
					parametres : globazGlobal.idDossier,
					wantInitThreadContext : true,
					callBack : function(data) {
						// Reloading the list after adding a new dossier de
						// contrôle
						// ajaxObj[0].ajaxFind(false);
					}
				};

				globazNotation.readwidget.options = options;
				globazNotation.readwidget.read();
			});

}

$(document).ready(function() {
	initZoneAjaxDossierControleAbsences();
});
