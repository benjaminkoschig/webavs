//fonctions de bases à redéfinir

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

//chargement du dom jquery
$(function () {
	var postesTableAjax;
	function initPostesTable() {
		defaultTableAjaxList.init({
			s_container: "#postes",
			s_table : '#postesTable',
			s_selector : '#postesContent',
			s_actionAjax : "vulpecula.postetravail.posteTravailForEmployeurAjax",
			userActionDetail : "vulpecula?userAction=vulpecula.postetravail.posteTravail.afficher&selectedId=",
			
			init : function() {
				postesTableAjax = this;
				this.capage(20, [ 10, 20, 30, 50, 100 ]);
			},
			
			getParametresForFind : function () {
				var m_map = {};
				m_map['idEmployeur'] = globazGlobal.idEmployeur;
				m_map['queryParameters'] = JSON.stringify(postesSearch);
				return m_map;
			}
		});		
	}
	
	function initDecomptesTable() {
		defaultTableAjaxList.init({
			s_container: "#decomptes",
			s_table : '#decomptesTable',
			s_selector : '#decomptesContent',
			s_actionAjax : "vulpecula.decompte.decompteForEmployeurAjax",
			userActionDetail : "vulpecula?userAction=vulpecula.decomptedetail.decomptedetail.afficher&selectedId=",
			
			init : function() {
				var that = this;
				this.capage(20, [ 10, 20, 30, 50, 100 ]);
				
				$('#decomptesTable input, #decomptesTable select').change(function() {
					that.ajaxFind();
				});
			},
			
			getParametresForFind : function () {
				var m_map = {};
				m_map['idEmployeur'] = globazGlobal.idEmployeur;
				m_map['idDecompte'] = $('#dIdDecompte').val();
				m_map['numeroDecompte'] = $('#dNumeroDecompte').val();
				m_map['type'] = $('#dType').val();
				return m_map;
			}
		});		
	}
	
	function initPrestationsTable() {
		var $container = $('#prestationsContent');
		
		var options = {
				s_detailPage : 'vulpecula.postetravail.prestationsdetailAjax.afficherAJAX',
				s_detailParam : function() {
					m_map = {};
					m_map.idPassage = this.attr('data-idPassage');
					m_map.idEmployeur = globazGlobal.idEmployeur;
					return m_map;
				},
				s_actionAjax : 'vulpecula.postetravail.prestationsAjax',
				s_actionAjaxParam : 'idEmployeur',
				s_actionAjaxParamValue : globazGlobal.idEmployeur,
				s_container : '#prestations',
				s_table: '#prestationsTable',
				s_detailParamValue : "data-idPassage",
				s_detailClassForClickEvent : '.prestation',
				s_userActionDetail : 'musca?userAction=musca.facturation.enteteFacture.chercher&amp;idPassage='
		};
		
		$container.masterDetail(options);	
	}
	
	function initAssociationsTable() {
		vulpeculaUtils.ajaxWait({
			data : {
				userAction : "vulpecula.association.associationAjax.afficherAJAX",
				idEmployeur : globazGlobal.idEmployeur
			},
			selectorToAppend : '#associationsWait',
			success : function(data) {
				$('#associationsContent').empty();
				$('#associationsContent').append(data);
			}
		});	
	}
	
	var loadingFunctions = [initPostesTable, initDecomptesTable, initPrestationsTable, initAssociationsTable];
	var loadedTabs = [];
	
	function loadTab(index) {
		loadingFunctions[index]();
		loadedTabs[index] = true;		
	}
	
	var $tabs = $("#tabs"); 
	$tabs.tabs({
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
	
	$('#postes, #decomptes, #prestations').on(eventConstant.AJAX_FIND_COMPLETE, function() {
		$(this).find('.imgLoading').hide();
	});
	
	
	//Gestion des champs de recherches
	var postesSearch = {};
	$('.posteSearch:not(#dateNais)').change(function() {
		var $element = $(this);
		var val = $element.val();
		var id = $element.attr('id');
		postesSearch[id] = val;
		postesTableAjax.ajaxFind();
	});
	
	$('#dateNais').change(function() {
		var $element = $(this);
		var id = $element.attr('id');
		var val = $element.val();
		if (isValidDate(val)) {
			postesSearch[id] = unformatDate(val);
		} else {
			postesSearch[id] = '';
		}
		postesTableAjax.ajaxFind();
	});
	
	$('#typeFacturation').change(function() {
		var $typeFacturation = $(this);
		var typeFacturation = $typeFacturation.val();
		changeTypeFacturation(typeFacturation, function(nouveauTypeFacturation) {
			$typeFacturation.val(nouveauTypeFacturation);
		});
		
		$("#typeFacturation option[value=]").remove();
	});
	
	$('#envoiBVRSansDecompte').change(function() {
		var $envoiBVRSansDecompte = $(this);
		var $labelForEnvoiBVRSansDecompte = $('#labelForEnvoiBVRSansDecompte');
		var checked = $envoiBVRSansDecompte.is(':checked');
		changeEnvoiBVRSansDecompte(checked, function(isChecked) {
			if(isChecked) {
				$labelForEnvoiBVRSansDecompte.text(globazGlobal.labelOui);
			} else {
				$labelForEnvoiBVRSansDecompte.text(globazGlobal.labelNon);
			}
			$envoiBVRSansDecompte.prop('checked',isChecked);
		});
	});
	
	$('#editerSansTravailleur').change(function() {
		var $editerSansTravailleur = $(this);
		var checked = $editerSansTravailleur.is(':checked');
		var $labelEditerSansTravailleur = $('#labelEditerSansTravailleur');
		changeEditerSansTravailleur(checked, function(isChecked) {
			if(isChecked) {
				$labelEditerSansTravailleur.text(globazGlobal.labelOui);
			} else {
				$labelEditerSansTravailleur.text(globazGlobal.labelNon);
			}
		});
	});
	
	function changeTypeFacturation(typeFacturation, callback) {
 		var options = {
				serviceClassName:globazGlobal.employeurViewService,
				serviceMethodName:'changeTypeFacturation',
				parametres: globazGlobal.idEmployeur + "," + typeFacturation,
				callBack:callback
 		};
 		vulpeculaUtils.lancementService(options);
	}
	
	function changeEnvoiBVRSansDecompte(checked, callback) {
 		var options = {
				serviceClassName:globazGlobal.employeurViewService,
				serviceMethodName:'changeEnvoiBVRSansDecompte',
				parametres: globazGlobal.idEmployeur + "," + checked,
				callBack:callback
 		};
 		vulpeculaUtils.lancementService(options);		
	}
	
	function changeEditerSansTravailleur(checked, callback) {
 		var options = {
				serviceClassName:globazGlobal.employeurViewService,
				serviceMethodName:'changeEditerSansTravailleur',
				parametres: globazGlobal.idEmployeur + "," + checked,
				callBack:callback
 		};
 		vulpeculaUtils.lancementService(options);	
	}
});