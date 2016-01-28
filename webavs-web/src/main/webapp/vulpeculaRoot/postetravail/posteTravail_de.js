var editMode = false;

function add () {
	document.forms[0].elements('userAction').value="vulpecula.postetravail.posteTravail.afficher&_method=_add";
}

function upd() {
	if(!globazGlobal.isNouveau) {
		$('#caisseMaladie').attr('disabled','disabled');
	}
	editMode = true;
	//Impossible d'éditer le travailleur ou l'employeur
	$('#descriptionTravailleur').prop('disabled',true);
	$('#descriptionEmployeur').prop('disabled',true);
}

function postInit() {
	if(globazGlobal.affiliationCaisseMaladie != "-1") {
		$("#caisseMaladie").val(globazGlobal.affiliationCaisseMaladie);
		//$("#caisseMaladie").attr('disabled','disabled');
	}
}

function validate() {
 	var o_occupations = GLO.tauxOccupation.retrieve();
 	var o_cotisations = GLO.cotisations.retrieve();
 	
 	
 	var o_posteTravail = {	
 		id : $("#idPosteTravail").text(),
 		occupations : o_occupations,
 		cotisations : o_cotisations,
 		idEmployeur : $("#idAffiliation").val(),
 		idTravailleur : $("#idTravailleur").val(),
 		spy : $("#spy").val(),
 		qualification : $("#qualification").val(),
 		genreSalaire : $("#genreSalaire").val(),
 		posteFranchiseAVS : $("#posteFranchiseAvs").is(':checked'),
 	    periodeDebut : $("#debutActivite").val(),
 	    periodeFin : $("#finActivite").val(),
 	    idTiersCM : $("#caisseMaladie").val()
 	};
 	var posteTravail = JSON.stringify(o_posteTravail);
 	
 	if (document.forms[0].elements('_method').value == "add"){
		var options = {
				serviceClassName:globazGlobal.posteTravailViewService,
				serviceMethodName:'create',
				parametres:posteTravail,
				callBack:function (data) {
					window.location.href="vulpecula?userAction=back";
				}
		};
		vulpeculaUtils.lancementServicePost(options);
 	}else{
 		var options = {
				serviceClassName:globazGlobal.posteTravailViewService,
				serviceMethodName:'update',
				parametres:posteTravail,
				callBack:function (data) {
					document.forms[0].elements('userAction').value="back";
					action(COMMIT);
				}
 		};
 		vulpeculaUtils.lancementServicePost(options);
 	}			
 	
	state = false;
	 if (document.forms[0].elements('_method').value == "add"){
	     document.forms[0].elements('userAction').value="vulpecula.postetravail.posteTravail.ajouter";
	 }
	 else 
	     document.forms[0].elements('userAction').value="vulpecula.postetravail.posteTravail.modifier";
	 return state;
}

function cancel() {
    if (document.forms[0].elements('_method').value == "add") {
    	window.location.href="vulpecula?userAction=back";
    }
    else {
        document.forms[0].elements('userAction').value="vulpecula.postetravail.posteTravail.afficher";
        document.forms[0].elements('selectedId').value=$('#idPosteTravail').text();
        action(ROLLBACK);
    }
}

function del() {
	if (window.confirm(globazGlobal.messageSuppression)){
	    document.forms[0].elements('userAction').value="vulpecula.postetravail.posteTravail.supprimer";
	    document.forms[0].submit();
	}
}

function init(){

}

//chargement du dom jquery
jsManager.executeAfter = function () {
	var $zoneCotisation = $("#zoneCotisations");
	
	$('html').on(eventConstant.JADE_FW_ACTION_DONE,function() {
		if(globazGlobal.addWithTravailleur) {
			$('#descriptionTravailleur').prop('disabled',true);
		}
		else if(globazGlobal.addWithEmployeur) {
		$('#descriptionEmployeur').prop('disabled',true);
		}
	});
	
	$zoneCotisation.accordion({
		animated : false,
		autoHeight : false,
		collapsible: true
	});
	
	$zoneCotisation.on("click",".idCotisation", function() {
		$checkbox = $(this);
		doCotisationCheck($checkbox);
	});
	
	$zoneCotisation.on("click","label", function() {
		var $checkbox = $(this).closest('tr').find('.idCotisation');
		if($checkbox.prop('disabled')) return;
		$checkbox.prop('checked',!$checkbox.prop('checked'));
		doCotisationCheck($checkbox);
	});
	
	function doCotisationCheck($checkbox) {
		idCotisation = $checkbox.val();
		
		var $tr = $checkbox.closest('tr');
		var $dateDebut = $tr.find('.dateDebut');
		var $dateFin = $tr.find('.dateFin');
		
		if ($checkbox.is(':checked')) {
			GLO.cotisations.callFindCotisationAndFillDate($dateDebut, $dateFin, idCotisation);
		} else {
			$dateDebut.val("");
			$dateFin.val("");
		}
		GLO.cotisations.coloriageTitre($checkbox);		
	}

	$("#wizard").tabs();	
	
	$("#idConventionForm").change(function(){
			var that = this;
			callServiceQualificationsPourConvention($(that).val());
		}
	);
	
	//Lorsque la date de début d'activité change, on modifie les données d'affichage.
	$('#debutActivite').change(function() {
		GLO.cotisations.reloadIfPossible(globazGlobal.getPosteTravail());
	});
	$('#finActivite').change(function() {
		GLO.cotisations.reloadIfPossible(globazGlobal.getPosteTravail());
	});
	
	var idPosteTravail = $("#idPosteTravail").text();
	
	function createOptionsQualifications(data) {
		var $select = $("#qualification");
		
		$select.empty();
		$select.append($("<option>"));
		
		for(var i=0;i<data.length;i++) {
			$select.append($("<option>").attr("value", data[i].id).text(data[i].libelle));
		}
	}
	
	function callServiceQualificationsPourConvention(idConvention){
		if(idConvention.length > 0){
			var options = {
					serviceClassName:'ch.globaz.vulpecula.web.views.postetravail.PosteTravailViewService',
					serviceMethodName:'getQualificationsParConvention',
					parametres:idConvention,
					callBack:function (data) {createOptionsQualifications(data);}
			};
			vulpeculaUtils.lancementService(options);			
		}
	}
	
	if(!globazGlobal.isNouveau) {
		$("#caisseMaladie").attr('disabled','disabled');
		GLO.cotisations.reloadIfPossible(globazGlobal.getPosteTravail());
	}
	
	GLO.tauxOccupation.init(idPosteTravail);

};

globazGlobal.getPosteTravail = function() {
	return $("#idPosteTravail").text();
};

var GLO = GLO || {};

//Module se chargeant de gérer les cotisations sur l'écran
GLO.cotisations = {
		previousDateDebut : null,
		previousDateFin : null,

	reloadIfPossible : function(idPosteTravail) {
		var idAffiliation = $('#idAffiliation').val();
		var dateDebut = $('#debutActivite').val();
		var dateFin = $('#finActivite').val();

		if(idAffiliation.length > 0 && dateDebut.length > 0) {
			if(dateDebut!=this.previousDateDebut || dateFin!=this.previousDateFin) {
				this.reload(idAffiliation, dateDebut, dateFin, idPosteTravail);
			}
		}
		this.previousDateDebut = dateDebut;
		this.previousDateFin = dateFin;
	},
	
	setDateFinAuxCotisations : function(dateFinActivite) {
		if(dateFinActivite.length>0) {
			d_dateFinActivite = getDateFromFormat(dateFinActivite,'dd.MM.yyyy');
			$(".cotisation input:checked").each(function(i, element) {
				var $dateFin = $(element).closest('tr').find('.dateFin');
				var s_dateFin = $dateFin.val();
				var d_dateFin = getDateFromFormat(s_dateFin,'dd.MM.yyyy');
				if(s_dateFin.length==0 || d_dateFin > d_dateFinActivite) {
					$dateFin.val(dateFinActivite);
				}
			});
		}
	},
	
	reload : function(idAffilie, dateDebut, dateFin, idPosteTravail) {
		var that = this;
		$.ajax({
			data: {idEmployeur:idAffilie,
				   dateDebut : dateDebut,
				   dateFin : dateFin,
				   idPosteTravail : idPosteTravail,
				   dateNaissanceTravailleur : $("#dateNaissanceTravailleur").val(),
				   sexeTravailleur : $("#sexeTravailleur").val(),
				  "userAction":"vulpecula.postetravail.cotisationsAjax.afficherAJAX"
				},
			success: function (data) {
				var s_divCotisations = '#zoneCotisations';
				var $zoneCotisations = $(s_divCotisations);

				$zoneCotisations.empty().html(data);
				notationManager.addNotationOnFragmentWithoutEvents($zoneCotisations);
				$('#zoneCotisations').accordion('destroy');
				$('#zoneCotisations').accordion({
					animated : false,
					autoHeight : false,
					active: false,
					collapsible: true
				});
				
				if(!globazGlobal.isNouveau) {
					that.setDateFinAuxCotisations(dateFin);
					var inputs = $('#zoneCotisations input');
					if(!editMode) {
						inputs.attr('disabled','disabled');
					}
				}
				
				var debutActivite = $("#debutActivite").val();
				var finActivite = $("#finActivite").val();
				
				if(debutActivite.length == 0){
					debutActivite = "";
				}
				
				if(finActivite.length == 0){
					finActivite = " ";
				}

				if(globazGlobal.isNouveau) {
					$("#zoneCotisations").find(".cotisations").each(function() {
						$(this).find(".cotisation").each(function(){
							var $cotisation = $(this);
							$idCotisation = $cotisation.find(".idCotisation");
							if($("#cotisationDateDebut" + $idCotisation.val()).val()){
								$idCotisation.attr('checked', 'checked');
								$cotisation.find("label").css('font-style', 'italic');
								GLO.cotisations.coloriageTitre($idCotisation);
							}
						});
					});
				}
			}
		});
	},
	
	getZoneCotisations : function() {
		return $("#zoneCotisations");
	},
	
	retrieve : function() {
		var i_cotisationId;
		var s_dateDebut;
		var s_dateFin;
		var o_cotisation;
		
		var cotisations = [];
		
		var $cotisations = this.getZoneCotisations().find("input:checked");
		$.each($cotisations , function(index, value) {
			var cotisation = $(value).closest('tr');
			i_id = $(cotisation).attr('data-idAdhesionCotsation');
			i_cotisationId = $(cotisation).find(".idCotisation").val();
			s_dateDebut = $(cotisation).find(".dateDebut").val();
			s_dateFin = $(cotisation).find(".dateFin").val();
			s_spy = $(cotisation).attr('data-spy');
			
			o_cotisation = {
				id : i_id,
				idCotisation : i_cotisationId,
				dateEntree : s_dateDebut,
				dateSortie : s_dateFin,
				spy : s_spy
			};
			cotisations.push(o_cotisation);
		});
		return cotisations;
	},
	
	coloriageTitre : function($checkbox) {
		//On recherche la caisse et on la met en noir
		var $tr = $($checkbox.closest("tr"));
		var $zoneCaisse = $($tr.closest(".cotisations"));
		var $a = $zoneCaisse.prev("h3").find("a");
		$a.css("color","black");
		//On parcourt les différentes cotisations et si une est cochée, on affiche le texte de la caisse en vert
		$tr.siblings().andSelf().each(function(){
			if($(this).find(".idCotisation").is(':checked')){
				$a.css("color","#339933");
			}
		});
	}, 
	
	callFindCotisationAndFillDate : function($dateDebut, $dateFin, idCotisation) {
		var options = {
				serviceClassName:globazGlobal.cotisationService,
				serviceMethodName:'findByIdCotisation',
				parametres:idCotisation,
				callBack:function (cotisation) {
					var $debutActivite = $("#debutActivite");
					var dateDebutActivite = $debutActivite.val().split(".");
					var dateDebutCoti = cotisation.dateDebut.split(".");
					if (new Date(dateDebutActivite[2], dateDebutActivite[1]-1, dateDebutActivite[0]).getTime() > new Date(dateDebutCoti[2], dateDebutCoti[1]-1, dateDebutCoti[0]).getTime()) {
						$dateDebut.val($debutActivite.val());
					} else {
						$dateDebut.val(cotisation.dateDebut);
					}
					
					var $finActivite = $("#finActivite");
					var dateFinActivite = $finActivite.val().split(".");
					var dateFinCoti = cotisation.dateFin.split(".");
					
					if (dateFinActivite[0] != "") {
						if (dateFinCoti[0] == "") {
							$dateFin.val($finActivite.val());
						} else {
							if (new Date(dateFinActivite[2], dateFinActivite[1]-1, dateFinActivite[0]).getTime() < new Date(dateFinCoti[2], dateFinCoti[1]-1, dateFinCoti[0]).getTime()) {
								$dateFin.val($finActivite.val());
							} else {
								$dateFin.val(cotisation.dateFin);
							}
						}
					} else {
						$dateFin.val(cotisation.dateFin);
					}
				}
		};
		vulpeculaUtils.lancementService(options);
	}
};

GLO.tauxOccupation = {
	init : function (s_idPoste) {
		//Si le poste de travail existe, on affiche ses taux
		if(s_idPoste.length > 0){
			this.callServiceTauxPourPoste(s_idPoste);
		} else {
			this.appendEmptyTauxIfNoElements();
		}
	},
	
	callServiceTauxPourPoste: function(idPosteTravail){
		var that = this;
		var options = {
				serviceClassName:globazGlobal.posteTravailService,
				serviceMethodName:'getTauxListParPoste',
				parametres:idPosteTravail,
				callBack:function (data) {that.remplirTaux(data);}
		};
		vulpeculaUtils.lancementService(options);		
	},
	
	appendEmptyTauxIfNoElements : function() {
		if(this.getNombreElements()===0) {
			this.appendEmptyTaux();
		}
	},
	
	getNombreElements : function() {
		return this.getTableTaux().children().length;
	},
	
	appendEmptyTaux : function() {
		var taux = this.getTauxClone();
		this.getTableTaux().append(taux);
		notationManager.addNotationOnFragmentWithoutEvents(taux);
	},
		
	getTableTaux : function() {
		return $("#tableTauxOccupation");
	},
	
	getTauxClone : function() {
		return $($("#templateTauxOccupation").html());
	},
	
	remplirTaux : function(data) {
		var $body = $('body');
		var $detailTemplateTaux = this.getTauxClone();
		var $tableTaux = GLO.tauxOccupation.getTableTaux();
		// pour chaque parametre on crée un template
		var $templateTaux = $detailTemplateTaux.clone(false, false);
		//On vide le clone
		$tableTaux.empty();
		if(data.length > 0){
			// on attache le template au DOM
			$body.append($templateTaux);
			for (var i = 0; i < data.length; i++){
				var line = data[i];
				//Pour chaque paramètre on créé le detai
				var $detailT = $templateTaux.clone(false,false);
				$detailT.attr('data-g-clone');
				$tableTaux.append($detailT);
				// Traite les notations contenues dans $detail
				notationManager.addNotationOnFragmentWithoutEvents($detailT);
				//Il faut encore setter les valeur au clone + disable des champs
				$detailT.find($(".tauxOccupation")).val(line[0]);
				$detailT.find($(".tauxOccupation")).prop('disabled', true);
				$detailT.find($(".dateValidite")).val(line[1]);
				$detailT.find($(".dateValidite")).prop('disabled', true);
			}
			$templateTaux.hide();
		}else{
			//Si il n'y a pas de liens sur les conventions, il faut quand même afficher le clone vide
			var $detail = $detailTemplateTaux.clone(false,false);
			$tableTaux.append($detail);
			notationManager.addNotationOnFragmentWithoutEvents($detail);
			$detail.find(":input").prop("disabled", true);
		}
	},
	
	retrieve : function() {
		var i_tauxOccupation;
		var s_dateValidite;
		var $taux = this.getTableTaux().find(".taux");
		var o_taux;
		var tauxList = [];
		$.each($taux, function(index, value) {
			i_tauxOccupation = $(value).find(".tauxOccupation:first").val();
			s_dateValidite = $(value).find(".dateValidite:first").val();
			
			if(i_tauxOccupation===undefined || i_tauxOccupation==='') {
				return tauxList;
			}
			
			o_taux = {
				taux : i_tauxOccupation,
				dateValidite : s_dateValidite
			};
			
			tauxList.push(o_taux);
		});
		return tauxList;
	}
};
