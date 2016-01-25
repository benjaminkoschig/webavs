
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

function DetailRenteVerseeATort(m_options) {
	var that = this;
	this.ACTION_AJAX = m_options.s_actionAjax;
	this.mainContainer = m_options.$mainContainer;
	this.table = m_options.$table;
	this.detail = m_options.$detail;
	this.t_element = [];
	this.s_spy = m_options.s_spy;
	this.s_selector = m_options.s_selector;
	this.b_wantReloadEntityAfterUpdate = m_options.b_wantReloadEntityAfterUpdate && true;
	this.m_options = m_options;
	
	this.afterRetrieve = function (data) {
		var that = this;

		var $contenu = $(data.find('contenu').toXML());

		notationManager.addNotationOnFragment($contenu);

		$contenu.find('.bouton').button();

		this.detail.empty();
		this.detail.append($contenu);
		this.detail.show();
		this.detail.find('#montant').focus().select();

		var $tiers = $('#idTiers');
		var $selectPrestation = $('#idRenteSelectionnee');
		var $selectTypeRenteVerseeATort = $('#csTypeRenteVerseeATort');
		var $montant = $('#montant');
		var $idPrestationSelectionnee = $('#idPrestationSelectionnee');
		var n_codeSystemeSaisieManuelle = $('#codeSystemeTypeSaisieManuelle').val();
		var $zoneEditionDescription = $('#description');
		var $descriptionPourEnvoiAjax = $('#descriptionPourEnvoiAjax');

		$selectPrestation.change(function () {
			$tiers.val($(this).find('option:selected').attr('idTiers'));
			$montant.focus().select();
		});
		if ($idPrestationSelectionnee) {
			$selectPrestation.find('option[value="' + $idPrestationSelectionnee.val() + '"]').prop('selected', true);
		}

		$zoneEditionDescription.keyup(function () {
			$descriptionPourEnvoiAjax.val($zoneEditionDescription.text());
		});
		
		// on n'affiche la zone d'édition de la description que si c'est une rente versée à tort de type saisie manuelle
		$selectTypeRenteVerseeATort.change(function () {
			that.afficherOuMasquerEditionDescription($selectTypeRenteVerseeATort, n_codeSystemeSaisieManuelle, $zoneEditionDescription, $descriptionPourEnvoiAjax);
		});
		that.afficherOuMasquerEditionDescription($selectTypeRenteVerseeATort, n_codeSystemeSaisieManuelle, $zoneEditionDescription, $descriptionPourEnvoiAjax);

		if ($tiers.val() == '') {
			$tiers.val($selectPrestation.find('option:selected').attr('idTiers'));
		}

		$montant.keydown(function (event) {
			if (event.which == 13 && (!b_supprimerLaDecisionSiModification || window.confirm(JSP_DELETE_MESSAGE_INFO))) {
				event.preventDefault();
				that.validateEdition();
			}
		});
	};

	this.afficherOuMasquerEditionDescription = function ($selectTypeRenteVerseeATort, n_codeSystemeSaisieManuelle, $zoneEditionDescription, $descriptionPourEnvoiAjax) {
		if ((1 * n_codeSystemeSaisieManuelle) == (1 * $selectTypeRenteVerseeATort.val())) {
			$('.necessairePourSaisieManuelle').show();
			$descriptionPourEnvoiAjax.val($zoneEditionDescription.text());
		} else {
			$('.necessairePourSaisieManuelle').hide();
			$descriptionPourEnvoiAjax.val('');
		}
	};

	this.getParametresForFind = function () {
		var m_map = {};
		if (typeof m_options.getParametresForFind === "function") {
			m_map = m_options.getParametresForFind.call(that);
		} else { 
			m_map = ajaxUtils.createMapForSendData(m_options.$search, m_options.s_selector);
		}
		return m_map;
	};

	this.addParamettersForRead = function () {
		var m_map = {};
		if (typeof m_options.addParametersForRead === "function") {
			m_map = m_options.addParametersForRead.call(that);
		} else { 
			m_map = null;
		}
		return m_map;
	};

	this.superAfterStartEdition = this.afterStartEdition;

	this.afterStartEdition = function () {
		if(typeof m_options.afterStartEdition === "function"){
			 m_options.afterStartEdition();
		} else {
			this.superAfterStartEdition();
		}
	};

	this.afterStopEdition = function () {
		$('#detailRenteVerseeATort').empty().hide();;
	};

	this.getParametres = function () {
		var o_map = {
				'idRenteVerseeATort': $('#idRenteVerseeATort').val(),
				'idOrdreVersement': $('#idOrdreVersement').val(),
				'idDemandeRente': $('#idDemandeRente').val(),
				'idTiers': $('#idTiers').val(),
				'csTypeRenteVerseeATort': $('#csTypeRenteVerseeATort').val(),
				'montant': globazNotation.utilsFormatter.amountTofloat($('#montant').val()),
				'idRenteSelectionnee': $('#idRenteSelectionnee').val(),
				'descriptionSaisieManuelle': $('#descriptionPourEnvoiAjax').val(),
				'saisieManuelle': $('#isSaisieManuelle').val()
			};
		return $.extend(o_map, this.getParametresForFind());
	};

	this.onDeleteAjax = function (data) {
		this.refreshIfNoError(data, true);
	};

	this.onUpdateAjaxComplete = function (data, idEntity) {
		if (ajaxUtils.hasError(data)) {
			ajaxUtils.displayError(data);
			ajaxUtils.afterAjaxComplete(this.mainContainer);
			return;
		}

		var b_allerSurEcranPreparationDecision = false;
		if ($.isXMLDoc(data)) {
			var $data = $(data);
			var $contenu = $($data.find('contenu').toXML());
			b_allerSurEcranPreparationDecision = $contenu.find('#suppressionDesDecisionsFaite').val() === 'true';
		}
		this.refreshIfNoError(data, b_allerSurEcranPreparationDecision);
	};

	this.refreshIfNoError = function (data, b_allerSurEcranPreparationDecision) {
		if(data && (data.errorBean || data.error)) {
			ajaxUtils.displayError(data);
			ajaxUtils.removeOverlay(this.mainContainer);
		} else {
			if (b_allerSurEcranPreparationDecision) {
				// si la suppression des décision a été faites, on retourne sur la préparation de décision
				window.location.href = globazGlobal.corvusPath 
										+ '?userAction=corvus.decisions.preparerDecisions.afficherPreparation'
										+ '&noDemandeRente=' + $('#idDemandeRente').val();
			} else {
				window.location.reload();
			}
		}
	};

	this.defaultClearFields = function () {
		if(this.t_element.length){
			for ( var i = 0; i < this.t_element.length; i++) {
				var $element = this.t_element[i]; 
				var name = $element.get(0).nodeName.toUpperCase();
				switch (name) {
				case 'SELECT':
					$element.clearInput();
					break;
				case 'INPUT':
					$element.clearInput();
					break;
				default:
					$element.text('');
				}
			}
		} else {
			this.detail.clearInputForm();
		}
	};
	
	this.clearFields = function () {
		if (typeof m_options.clearFields === "function") {
			m_options.clearFields.call(that);
		} else {
			this.defaultClearFields();
		}
	}; 
	
	this.executeAddEvent = function () {
		this.stopEdition();
		this.startEdition();
	};
	
	this.getParentViewBean = function () {};
	
	this.setParentViewBean = function (newViewBean) {};
	
	this.formatTableTd = function ($elementTable) {};
	// initialization
	this.init(m_options.init);
}



var detailRenteVerseeATort = {

	options: {
		s_actionAjax: globazGlobal.actionAjaxDetail,
		s_container: ".area",		//Spécifie la zone ajax
		s_detail: ".areaDetail",	//Spécifie la zone on se trouve le détail à afficher
		s_table: ".areaTable",		//Spécifie la zone on se trouve le tableau à peupler. Le sélecteur doit être définit sur l’élément <table>
		s_selector: "#",			//Spécifie quel sélecteur il faut utiliser pour la réception et l’envoi des données	
		s_search: ".areaSearch",	//Indique la zone de recherche 
		s_spy: null,				//Indique le chemin de l'objet qui contient le spy
		findParameters: null,		//Permet de redéfinir la fonction findParameters Utilisé pour exécuter la recherche de la liste doit retourner une map de critères de recherche
		getParametres: null,		//Permet de redéfinir la fonction getParametres. Utilisé pour exécuter trouvée les valeurs à modifier ou créer dans la zone ajax. Doit retourner une map de valeurs
		addParametersForRead: null,	//Permet d'envoyer des paramétres suplémentaire pour le read
		afterRetrieve: null,
		clearFields: null,			//Permet de redéfinir la fonction clearFields. Permet de définir quel champs il faut vidé pour lors de l’affichage d’un détail ou de l’annulation
		b_validateNotation: false,	//Inidque si il faut faire une validation du formulaire par js
		b_hasButtonNew: true,		//Permet d'indiquer si l'on veut afficher le boutton "Nouveau"
		afterStartEdition: null,	//Permet d'éxecuter une fonction après un startEdition

		init: function () {
			this.addTableEventOnElements(this.$trInTbody);

			this.getBtnContainer().show();
		}
	},

	optionsDefinit: null,
	m_options: {},

	init: function (m_options) {
		var that = this; 
		t_zone = [];
		if (m_options) {
			this.optionsDefinit = Object.create($.extend({},that.options, m_options));
		}
		$(this.optionsDefinit.s_container).each(function () {
			var $that = $(this);
			that.optionsDefinit.$mainContainer = $that;
			that.optionsDefinit.$table = $that.find(that.optionsDefinit.s_table);
			that.optionsDefinit.$detail = $that.find(that.optionsDefinit.s_detail);
			that.optionsDefinit.$search = $that.find(that.optionsDefinit.s_search);
			var zone = new DetailRenteVerseeATort(that.optionsDefinit);
			t_zone.push(zone);
			that.addEvent($that, zone);
		});
		return t_zone;
	},

	addEvent: function ($that, zone) {
		var that = this;
		$that.on('click', '.btnAjaxUpdate', function () {
			zone.startEdition();
		}).on('click', '.btnAjaxCancel', function () {
			zone.stopEdition();
		}).on('click', '.btnAjaxValidate', function () {
			if (!b_supprimerLaDecisionSiModification || window.confirm(JSP_DELETE_MESSAGE_INFO)) {
				if (that.optionsDefinit.b_validateNotation) {
					if (notationManager.validateAndDisplayError()) {
						zone.validateEdition();
					}
				} else {
					zone.validateEdition();
				}
			}
		}).on('click', '.btnAjaxDelete', function () {
			zone.ajaxDeleteEntity(zone.selectedEntityId);
		});

		if (this.optionsDefinit.b_hasButtonNew) {
			$that.find('.btnAjaxAdd').click(function () {
				if (!b_supprimerLaDecisionSiModification || window.confirm(JSP_DELETE_MESSAGE_INFO)) {
					zone.executeAddEvent();
				}
			});
		} else {
			$that.find('.btnAjaxAdd').remove();
		}

		$('#boutonAjouter').click(function () {
			zone.stopEdition();

			$.ajax({
				data: {
					'userAction': 'corvus.rentesaccordees.renteVerseeATortAjax.creerNouvelleEntiteeAJAX',
					'idDemandeRente': $('#idDemandeRente').val()
				},
				url: globazGlobal.corvusPath,
				success: function (data) {
					if (ajaxUtils.hasError(data)) {
						zone.displayError(data);
					} else {
						zone.afterRetrieve($(data));
					}
				},
				type: 'GET'
			});
		});
	}
};

DetailRenteVerseeATort.prototype = AbstractScalableAJAXTableZone;

var JSP_DELETE_MESSAGE_INFO,
	b_supprimerLaDecisionSiModification = false;

$(document).ready(function () {
	$('.odd').mouseover(function () {
		$(this).addClass('hover').removeClass('odd');
	}).mouseout(function () {
		$(this).removeClass('hover').addClass('odd');
	});
	$('.nonOdd').mouseover(function () {
		$(this).addClass('hover').removeClass('nonOdd');
	}).mouseout(function () {
		$(this).removeClass('hover').addClass('nonOdd');
	});

	detailRenteVerseeATort.init({});
	$('.lastModification').remove();

	$('#boutonAjouter').button();

	b_supprimerLaDecisionSiModification = $('#supprimerLaDecisionSiModification').val() === 'true'; 
	JSP_DELETE_MESSAGE_INFO = $('#messageAvertissementSuppressionDecision').val();
});


ajaxUtils.displayError = function (data) {
	var $data = null,
	$error = null,
	$json = null,
		s_error = null,
		s_errorJson = null;

	if (!$.isPlainObject(data)) {
		$data = $(data);
		
		$error = $data.find('error');

		if ($error.attr('errorPage')) {
			$json = $data.find('json');
			s_errorJson = $json.find('exception').text();
			if (s_errorJson) {
				data = $.parseJSON(s_errorJson);
			}
		} else {
			s_error = $error.text();
		s_errorJson = $data.find('errorJson').text();
		if (s_errorJson) {
			data = $.parseJSON(s_errorJson);
		}
	}
	}

	if ($.isPlainObject(data)) {
		// on n'est dans le cas d'une exception
		if (data.errorBean) {
			s_error = ajaxUtils.renderDialogExceptionError(data.errorBean);
		} else if (data.messages && data.exceptions && data.stack) {
			s_error = ajaxUtils.renderDialogExceptionError(data);
		} else if (data.messages) {
			var logsRender = {};
			logsRender.logs = ajaxUtils.getLogsFromLevel(data.messages, 3);
			logsRender.clazz = 'error';

			logsRender.logs = ajaxUtils.uniqueMessage(logsRender.logs, 'message');

			s_error = ajaxUtils.renderLogs(logsRender);
		}
	}

	if (!s_error || !s_error.length) {
		s_error = data;
		if (data && $.isPlainObject(data)) {
			s_error = data.error;
			if ($.trim(s_error).length === 0 && data.viewBean) {
				s_error = data.viewBean.message;
			}
		}
	}

	if (s_error) {
		globazNotation.utils.consoleError(s_error);
	}
	$('.mainContainerAjax').find('.loading_horizonzal').remove();
};
