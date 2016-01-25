function initZoneAjaxSaisieAbsence() {
	
	var $caseACocherBaseIndemnisation = $('#showOnlyBIAbsences');
	$caseACocherBaseIndemnisation.attr('checked', true);
	
	// On initialise cette zone après l'initialisation de la zone de la base d'indemnisation
	// car il faut que l'id de la base d'indemn soit managé !!
	$('#zoneAjaxDetailBaseIndeminsation').one(eventConstant.AJAX_LOAD_DATA, function(){
		var ajaxObj = defaultTableAjax.init({
			s_container: '#zoneAjaxSaisieAbsence',
			s_actionAjax: globazGlobal.actionAjaxSaisieAbsence,
			b_wantReloadEntityAfterUpdate: false,
			
			getParametresForFind: function () {
				var o_param = {
					'searchModel.forIdDossier': globazGlobal.idDossier
				};
	
				if ($caseACocherBaseIndemnisation.is(':checked')) {
					// It's a big Ack... snif :
					// Si des click sont effectués sur la case à cocher et qu'aucune BI n'existe,
					// met l'id 0 pour que le searchModel prenne en considération l'id de la base d'indemnisation
					if(isNaN(globazGlobal.idBaseIndemnisation) == true || globazGlobal.idBaseIndemnisation == ""){
						o_param['searchModel.forIdBaseIndemnisation'] = 0;
					}
					else {
						o_param['searchModel.forIdBaseIndemnisation'] = globazGlobal.idBaseIndemnisation;
					}				
				}
				return o_param;
			},
	
			getParametres: function () {
				return {
					'absence.idDossierControleAbsences': globazGlobal.idDossier,
					'absence.idBaseIndemnisation': globazGlobal.idBaseIndemnisation
				};
			},
	
			afterStartEdition: function () {
				var $detailZone = $('#zoneAjaxDetailSaisieAbsences');
				$detailZone.dialog('open');
			},
	
			beforeStartEdition: function () {
				this.mainContainer = $('#zoneAjaxDetailSaisieAbsences');
			},
			
			init: function () {	
				// It's a big Ack... snif :
				// Copie du code de la méthode capage() pour pouvoir inhiber l'appel
				// au service si il n'y a pas de base d'indemnisation
				var n_definedSearchSize = 5;
				var t_sizeArray = [10,20];
				this.addTableEvent();
				this.colorTableRows();
				//Pour afficher par défaut la zone détail
				this.detail.show();
				this.getBtnContainer().show();
				this.stopEdition();
				//excute la recheche directement
				if (typeof n_definedSearchSize !== "undefined") {
					this.createPagination(n_definedSearchSize, t_sizeArray);
				}
	
				if( (!isNaN(globazGlobal.idBaseIndemnisation) == true) && (globazGlobal.idBaseIndemnisation != "") ){
					this.ajaxFind();
					this.sortTable();
				}

			}
			
		});
		
		detailZoneDialog.init({
			$zone: $('#zoneAjaxDetailSaisieAbsences'),
			o_ajaxObject: ajaxObj[0],
			s_selecteurDeclencheur: '.showDetailSaisieAbsences',
			f_validate: function () {
				return true;
			},
			f_callbackOk: function () {
				this.o_ajaxObject.validateEdition();
			},
			s_width: '650px',
			s_titleLabel: globazGlobal.labelTitreSaisieAbsences
		});
		
		$('#showOnlyBIAbsences').click(function () {
			ajaxObj[0].ajaxFind(false);
		});
		
		$('#boutonAjouterAbsence').click(function () {
			ajaxObj[0].startEdition();
		});
		
		ajaxObj[0].mainContainer.bind(eventConstant.AJAX_UPDATE_COMPLETE, function () {
			ajaxObj[0].stopEdition();
		});
		
		var zoneAjaxDetailBaseIndeminsation = $('#zoneAjaxDetailBaseIndeminsation');
		zoneAjaxDetailBaseIndeminsation.bind(eventConstant.AJAX_UPDATE_COMPLETE, function() {
			ajaxObj[0].ajaxFind(false);
		});
		

		//Vérification du solde avant la création ou la mise à jour d'un absence
		ajaxObj[0].validateEditionSuper = ajaxObj[0].validateEdition;
		ajaxObj[0].validateEdition =  function () {
			var codeAbsence = $('#absence\\.absence\\.codeAbsence').val();
			var dateDeDebut = $('#absence\\.absence\\.dateDeDebut').val();
			var dateDeFin = $('#absence\\.absence\\.dateDeFin').val();
			var joursSaisis = $('#absence\\.absence\\.joursSaisis').val();
			var joursNonPayeSaisis = $('#absence\\.absence\\.joursNonPayeSaisis').val();
			var idAbsence = $('#absence\\.absence\\.idAbsence').val();
			
			if(globazGlobal.idDossier.length==0){
				globazGlobal.idDossier=' ';
			}
			
			if(globazGlobal.idBaseIndemnisation.length==0){
				globazGlobal.idBaseIndemnisation=' ';
			}
			
			if(idAbsence.length==0){
				idAbsence=' ';
			}
			
			if(codeAbsence.length==0){
				codeAbsence=' ';
			}
			if(dateDeDebut.length==0){
				dateDeDebut=' ';
			}
			if(dateDeFin.length==0){
				dateDeFin=' ';
			}
			if(joursSaisis.length==0){
				joursSaisis=' ';
			}
			if(joursNonPayeSaisis.length==0){
				joursNonPayeSaisis=' ';
			}
			
			
			var mode;
			if (this.selectedEntityId) {
				mode = 'update';
			} else {
				mode = 'create';
			}
			$('#zoneAjaxDetailSaisieAbsences').dialog('close');

			var options = {
					serviceClassName : 'ch.globaz.ij.business.services.IJAbsenceService',
					serviceMethodName : 'verifierSiSoldeNegatifApresSaisieAbsence',
					parametres : globazGlobal.idDossier+","+globazGlobal.idBaseIndemnisation+","+idAbsence+","+codeAbsence+","+dateDeDebut+","+dateDeFin+","+joursNonPayeSaisis+","+joursSaisis+","+mode,
					wantInitThreadContext : true,
					callBack : function(data) {
						if(data!=null){
							$("<div>"+data+"</div>").dialog({
									autoOpen : true,
									modal : true,
									width : 450,
									title : "",
									buttons : [ {											
													text : globazGlobal.labelNon,
													click : function() {
														$(this).dialog('close');
														$('#zoneAjaxDetailSaisieAbsences').dialog('open');
												}	
												},
											    {
													text : globazGlobal.labelOui,
													click : function() {
														$(this).dialog('close');
														ajaxObj[0].validateEditionSuper.call(ajaxObj[0]);
												}
									} ]
								});
						} else {
							ajaxObj[0].validateEditionSuper.call(ajaxObj[0]);
						}
					}
				};

			var ajax = Object.create($.extend(true, {}, globazNotation.readwidget));
			ajax.options = options;
			ajax.read();
		};
		
	});
}


$(document).ready(function () {
	initZoneAjaxSaisieAbsence();
});
