function add () {
}

function upd() {
}

function validate() {
}

function cancel() {
}

function del() {
}

function init(){
	
}

function postInit() {
	$('input, select').removeAttr('disabled');
}

var postesSearch = {};
globazGlobal.tabs = (function() {
	var loadingFunctions = [initPostesTable, initDecomptesSalaires, initCongesPayes, initAbsencesJustifiees, initServicesMilitaires,initAF,initSyndicatTable,initCaisseMaladieTable];
	var loadedTabs = [];
	
	function init() {
		var $tabs = $("#tabs").tabs({
			show: function(event, ui) {
				if ( loadedTabs[ui.index] || ui.index >= loadingFunctions.length) {
				      return;
				 }
				else {
					loadTab(ui.index);
				}
			}
	    });
		
		if(globazGlobal.tab!=='') {
			$tabs.tabs("select",globazGlobal.tab);
		}		
		bindEvents();
	}
	
	function initPostesTable() {
		defaultTableAjaxList.init({
			s_container: "#postes",
			s_table : '#postesTable',
			s_selector : '#postesContent',
			s_actionAjax : "vulpecula.postetravail.posteTravailForTravailleurAjax",
			userActionDetail : "vulpecula?userAction=vulpecula.postetravail.posteTravail.afficher&selectedId=",
			
			init : function() {
				postesTableAjax = this;
				this.capage(20, [ 10, 20, 30, 50, 100 ]);
			},
			
			getParametresForFind : function () {
				var m_map = {};
				m_map['idTravailleur'] = globazGlobal.idTravailleur;
				m_map['queryParameters'] = JSON.stringify(postesSearch);
				return m_map;
			}
		});		
	}
	
	function initDecomptesSalaires() {
		defaultTableAjaxList.init({
			s_container: "#decomptessalaires",
			s_table : '#decomptessalairesTable',
			s_selector : '#decomptessalairesContent',
			s_actionAjax : "vulpecula.decomptesalaire.decomptesalaireForTravailleurAjax",
			userActionDetail : "vulpecula?userAction=vulpecula.decomptesalaire.afficher&selectedId=",
			
			init : function() {
				var that = this;
				this.capage(20, [ 10, 20, 30, 50, 100 ]);
				
				$('#decomptessalairesTable input, #decomptessalairesTable select').change(function() {
					that.ajaxFind();
				});
			},
			
			getParametresForFind : function () {
				var m_map = {};
				m_map['idTravailleur'] = globazGlobal.idTravailleur;
				m_map['idDecompte'] = $('#dsIdDecompte').val();
				m_map['raisonSociale'] = $('#dsRaisonSociale').val();
				m_map['numeroDecompte'] = $('#dsNumeroDecompte').val();
				m_map['type'] = $('#dsType').val();
				return m_map;
			}
		});	
	}
	
	var congesPayes = null;
	function initCongesPayes() {
		defaultTableAjaxList.init({
			s_container: "#congespayes",
			s_table : '#congespayesTable',
			s_selector : '#congespayesContent',
			s_actionAjax : "vulpecula.congepaye.congePayeAjax",
			userActionDetail : "vulpecula?userAction=vulpecula.congepaye.afficher&selectedId=",
			
			init : function() {
				congesPayes = this;
				this.capage(20, [ 10, 20, 30, 50, 100 ]);
			},
			
			getParametresForFind : function () {
				var m_map = {};
				m_map['idTravailleur'] = globazGlobal.idTravailleur;
				return m_map;
			}
		});			
	}
	
	var absencesjustifiees = null;
	function initAbsencesJustifiees() {
		defaultTableAjaxList.init({
			s_container: "#absencesjustifiees",
			s_table : '#absencesjustifieesTable',
			s_selector : '#absencesjustifieesContent',
			s_actionAjax : "vulpecula.absencesjustifiees.absencesjustifieesAjax",
			userActionDetail : "vulpecula?userAction=vulpecula.absencesjustifiees.afficher&selectedId=",
			
			init : function() {
				absencesjustifiees = this;
				this.capage(20, [ 10, 20, 30, 50, 100 ]);
			},
			
			getParametresForFind : function () {
				var m_map = {};
				m_map['idTravailleur'] = globazGlobal.idTravailleur;
				return m_map;
			}
		});			
	}
	
	var servicesmilitaires = null;
	function initServicesMilitaires() {
		defaultTableAjaxList.init({
			s_container: "#servicesmilitaires",
			s_table : '#servicesmilitairesTable',
			s_selector : '#servicesmilitairesContent',
			s_actionAjax : "vulpecula.servicemilitaire.servicemilitaireAjax",
			userActionDetail : "vulpecula?userAction=vulpecula.servicemilitaire.afficher&selectedId=",
			
			init : function() {
				servicesmilitaires = this;
				this.capage(20, [ 10, 20, 30, 50, 100 ]);
			},
			
			getParametresForFind : function () {
				var m_map = {};
				m_map['idTravailleur'] = globazGlobal.idTravailleur;
				return m_map;
			}
		});
	}
	
	function initSyndicatTable() {
		defaultTableAjaxList.init({
			s_container: "#syndicats",
			s_table : '#syndicatsTable',
			s_selector : '#syndicatsContent',
			s_actionAjax : "vulpecula.syndicat.syndicatAjax",
			userActionDetail : "vulpecula?userAction=vulpecula.syndicat.syndicat.afficher&selectedId=",
			
			init : function() {
				this.capage(20, [ 10, 20, 30, 50, 100 ]);
			},
			
			getParametresForFind : function () {
				var m_map = {};
				m_map['idTravailleur'] = globazGlobal.idTravailleur;
				return m_map;
			}
		});				
	}
	
	function initAF() {
		$.ajax({
			data: {idTiers:globazGlobal.idTiersTravailleur,
				  userAction:"vulpecula.af.droitsAjax.afficherAJAX"
			},
			success: function (data) {
				$('#afContent').empty().html(data);
			}
		});
	}
	
	function initCaisseMaladieTable() {
		defaultTableAjaxList.init({
			s_container: "#caissesmaladies",
			s_table : '#caissesmaladiesTable',
			s_selector : '#caissesmaladiesContent',
			s_actionAjax : "vulpecula.caissemaladie.caissemaladieAjax",
			userActionDetail : "vulpecula?userAction=vulpecula.caissemaladie.caissemaladie.afficher&selectedId=",
			
			init : function() {
				this.capage(20, [ 10, 20, 30, 50, 100 ]);
			},
			
			getParametresForFind : function () {
				var m_map = {};
				m_map['idTravailleur'] = globazGlobal.idTravailleur;
				return m_map;
			}
		});		
	}
	
	function loadTab(index) {
		var func = loadingFunctions[index];
		if(typeof(func) === 'function') {
			func();
		}
		loadedTabs[index] = true;		
	}
	
	function bindEvents() {
		$('#absencesjustifieesContent').on("click", ".supprimerAJ", function(event) {
			var idAbsenceJustifiee = $(this).attr('data-idAbsenceJustifiee');
			var message = jQuery.i18n.prop("ajax.deleteMessage");
			if (window.confirm(message)) {
				var parametres = {};
				parametres["userAction"] = "vulpecula.absencesjustifiees.absencesjustifieesAjax.supprimerAJAX";
				parametres["idAbsenceJustifiee"] = idAbsenceJustifiee;
				$.ajax({
					data: parametres,
					success: function (data) {
						absencesjustifiees.ajaxFind();				
					}
				});
			}
		});
		$('#congespayesContent').on("click", ".supprimerCP", function(event) {
			var idCongePaye = $(this).attr('data-idCongePaye');
			var message = jQuery.i18n.prop("ajax.deleteMessage");
			if (window.confirm(message)) {
				var parametres = {};
				parametres["userAction"] = "vulpecula.congepaye.congePayeAjax.supprimerAJAX";
				parametres["idCongePaye"] = idCongePaye;
				$.ajax({
					data: parametres,
					success: function (data) {
						congesPayes.ajaxFind();			
					}
				});
			}
		});
		$('#servicesmilitairesContent').on("click",".supprimerSM", function(event) {
			var idServiceMilitaire = $(this).attr('data-idserviceMilitaire');
			var message = jQuery.i18n.prop("ajax.deleteMessage");
			if (window.confirm(message)) {
				var parametres = {};
				parametres["userAction"] = "vulpecula.servicemilitaire.servicemilitaireAjax.supprimerAJAX";
				parametres["idServiceMilitaire"] = idServiceMilitaire;
				$.ajax({
					data: parametres,
					success: function (data) {
						servicesmilitaires.ajaxFind();			
					}
				});
			}
		});	
		
		$('#postes, #decomptessalaires, #congespayes, #absencesjustifiees, #servicesmilitaires, #caissesmaladies, #syndicats, #af').on(eventConstant.AJAX_FIND_COMPLETE, function() {
			$(this).find('.imgLoading').hide();
		});
		
		$('#af').on('click','.droit',function() {
			var idEntity = $(this).attr('idEntity');
			window.location.href = 'al?userAction=al.droit.droit.afficher&selectedId=' + idEntity;
		});
		
		$('#af').on('click','.linkDossier', function(event) {
			var idDossier = $(this).closest('tr').attr('idDossier');
			window.location.href = 'al?userAction=al.dossier.dossierMain.afficher&idDossier=' + idDossier;
			event.stopPropagation();
		});
	}
	
	return {
		init : init
	};
})();

//chargement du dom jquery
$(function () {
	$("[name=blocVolant]").show();
	
	globazGlobal.tabs.init();

	var current = 0;
	
	var nbEmployeurs = $('.employeurEntry').length;
	init();
	$('.boutonPrecedent').click(function() {
		showPrecedent();
	});
	
	$('.boutonSuivant').click(function() {
		showSuivant();
	});
	
	$(document).keydown(function(event) {
		if(event.which===37) {
			showPrecedent();
		}
		else if(event.which===39) {
			showSuivant();
		}
	});
	
	function init() {
		//On cache tout
		hideAll();
		//On affiche le 1er poste
		if(nbEmployeurs > 0){
			var currentEmployeur = $('#employeur'+current);
			currentEmployeur.show();
		}
		//Si il y a plus d'1 poste, il faut afficher le bouton suivant
		if(nbEmployeurs > 1){
			$('.boutonSuivant').show();
		}
		//On affiche le compteur
		majCompteur();
	}
	
	function hideAll() {
		$('.boutonPrecedent').hide();
		$('.boutonSuivant').hide();
		$('.employeurEntry').hide();
	}
	
	function majCompteur(){
		$('.compteur').each(function() {
			$(this).text((current+1)+'/'+(nbEmployeurs));
		});
	}
	
	function showSuivant(){
		if(current < nbEmployeurs-1){
			//On affiche le bouton précédent
			$('.boutonPrecedent').show();
			//On récupère le poste pour le cacher
			var currentEmployeur = $('#employeur'+current);
			current++;
			currentEmployeur.hide(0,function(){
				//On incrémente le compteur pour aller chercher le suivant et l'afficher
				currentEmployeur = $('#employeur'+current);
				currentEmployeur.show( "slide", { direction: "right"});
			});
			
			//On regarde s'il existe encore un suivant
			if(current+1==nbEmployeurs){
				$('.boutonSuivant').hide();
			}
			majCompteur();
		}
	}
	
	function showPrecedent(){
		if(current!=0){
			//On affiche le bouton suivant
			$('.boutonSuivant').show();
			//On récupère le poste pour le cacher
			var currentEmployeur = $('#employeur'+current);
			current--;
			currentEmployeur.hide(0, function() {
				//On décrémente le compteur pour aller chercher le précédent et l'afficher
				currentEmployeur = $('#employeur'+current);
				currentEmployeur.show("slide");
			});
			//On regarde s'il existe encore un précédent
			if(current==0){
				$('.boutonPrecedent').hide();
			}
			majCompteur();
		}
	}

	$('.posteSearch').change(function(event) {
		var $element = $(this);
		var val = $element.val();
		var id = $element.attr('id');
		postesSearch[id] = val;
		postesTableAjax.ajaxFind();
		changed = false;
	});
	
	$('#annoncerEnfants').click(function() {
		var $this = $(this);
		$this.attr('disabled','disabled');
		var options = {
				serviceClassName:globazGlobal.travailleurViewService,
				serviceMethodName:'annoncerEnfants',
				parametres:globazGlobal.idTravailleur,
				callBack:function (data) {
					alert(globazGlobal.messageEnfantsAnnonces);
				}
		};
		vulpeculaUtils.lancementService(options);	
	})
});
	