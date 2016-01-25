function DetailAjaxDecision(m_options) {
	
	this.ACTION_AJAX = m_options.s_actionAjax;
	this.mainContainer = $(m_options.s_detail);
	this.selectedEntityId = m_options.n_idEntity;
	this.onError = function(data){
		if (typeof m_options.onError === "function"){
			m_options.onError(data);
		}
	};

	this.clearInputFieldsExceptInputsNotCleared = function(){
		//Peut poser des problèmes de lenteurs !!!
		this.mainContainer.find('*').not('.inputsNotCleared').clearInputForm();
	};
	
	this.onRetrieve = function (data) {
		//Peut poser des problèmes de lenteurs !!!
		this.clearInputFieldsExceptInputsNotCleared();
		this.t_element = this.defaultLoadData(data, m_options.s_selector); 
	};
	
	this.getParametersForLoad = function () {
		return {};
	};

	this.getParametres = function () {
		return this.createMapForSendData(m_options.s_selector);
	};

	this.onUpdate = function (data) {
		var t_entityIdPath = m_options.s_entityIdPath.split(".");
		var tempData = data;
		
		for (var i=0; t_entityIdPath.length > i; i++) {
			tempData = tempData[t_entityIdPath[i]];
		}
		
		//Peut poser des problèmes de lenteurs !!!
		this.clearInputFieldsExceptInputsNotCleared();
		this.t_element = this.defaultLoadData(data, m_options.s_selector); 
		this.selectedEntityId = tempData;
		
		if (typeof m_options.f_afterUpdate == "function" ) {
			m_options.f_afterUpdate.call(this,data);
		}
	};

	this.getParentViewBean = function () {
		if (typeof globalViewBean !== "undefined") {
			return globalViewBean;
		}
	};

	this.setParentViewBean = function (newViewBean) {
		globalViewBean = newViewBean;
	};
	
	this.disabeldEnableForm = function (b_disabeEnable) {
		ajaxUtils.disabeldEnableForm(this.mainContainer, this.mainContainer.find(':input').not(":hidden,:button,.alwaysDisabled"), b_disabeEnable);
	};
	
	this.startEdition = function () {
		this.disabeldEnableForm(false);
		this.mainContainer.find('input:button').hide().filter('.btnAjaxValidate').show();
		
		if(this.selectedEntityId){
			this.mainContainer.find('input:button').filter('.btnAjaxCancel').show();
		}else{
			this.mainContainer.find('input:button').filter('.btnCancelBack').show();
		}
		
		this.isModifyingEntity = true;
		ajaxUtils.addFocusOnFirstElement(this.mainContainer);
		this.mainContainer.addClass(this.modifiedZoneClass);
		this.triggerEvent();
	};
	
	this.ajaxDeleteEntity = function () {
		
		var message ;
		if (typeof JSP_DELETE_MESSAGE_INFO !== "undefined"){
			message = JSP_DELETE_MESSAGE_INFO;
		} else {
			message = jQuery.i18n.prop("ajax.deleteMessage");
		}
		if (window.confirm(message)) {
			var that = this;
			var parametres = this.getParametres();
			
			ajaxUtils.beforeAjax(this.mainContainer);
			parametres.userAction = this.ACTION_AJAX + ".supprimerAJAX";
			parametres.viewBean=this.currentViewBean;
			parametres.parentViewBean=this.getParentViewBean;
			$.ajax({
				data: parametres,
				success: function (data) {
					if(that.onUpdateAjaxComplete(data) || typeof that.onUpdateAjaxComplete(data) == 'undefined'){
						window.top.fr_main.window.location= m_options.s_actionCancelback;
					};
					
				},
				type: "POST"
			});
		}
	};

	// initialization
	this.init(function () {
		if(this.selectedEntityId) {
			this.ajaxLoadEntity();
		}
	});
};


DetailAjaxDecision.prototype =  AbstractSimpleAJAXDetailZone;






/*
 * L'apel a la fonction init permet d'appliquer tout les comportement par défaut pour les zones ajax
 * La fonction init retourne un tableau contenant les zones Ajax
 */
var runAjax = {
	options: {
		s_actionAjax: globazGlobal.ACTION_AJAX,
		s_detail: '.areaDetail',	// Spécifie la zone on se trouve le détail à peupler
		n_idEntity: 0,				// Spécifie l'ID primaire de l'entité à charger
		s_selector: '#',			// Spécifie quel sélecteur il faut utiliser pour la réception et l’envoi des données  
		s_spy: null,				// Indique le chemin de l'objet qui contient le spy
		onRetrieve: null,			// Permet de redéfinir la fonction onRetrieve. Utilisé pour définir l’action que l’on veut exécuter après la lecture d’un détail
		b_validateNotation: false,	// Indique si il faut faire une validation du formulaire par js
		b_hasButtonNew: true,		// Permet d'indiquer si l'on veut afficher le boutton "Nouveau"
		afterStartEdition: null,	// Permet d'éxecuter une fonction après un startEdition
		getParametersForLoad: null, // Indique les parmétre que l'on veut utiliser pour charger une entité.
		s_entityIdPath:"",			// Permet d'indiquer les chemin ou se trouve l'id (Encapsulation de l'objet
		s_actionCancelback: null,	// Indique l'action que l'on veut appliquer sur le boutton annulé lors de la première édition du formulaire.
		f_afterUpdate:null, 		// Permet d'executer une action après l'update et le add
		init: function () {			// Permet de définir le comportement de la zone ajax
			
		},
		onError:null
	},
	
	optionsDefinit: null,
	m_options: {},

	init: function (m_options) {
		var that = this,
			t_zone = [];
		if (m_options) {
			this.optionsDefinit = Object.create($.extend({}, that.options, m_options));
		}
		// pour chaque zone de détail trouvées par le sélecteur s_detail
		$(this.optionsDefinit.s_detail).each(function () {
			
			var $that = $(this);
			that.optionsDefinit.$mainContainer = $that;
			var zone = new DetailAjaxDecision(that.optionsDefinit);
			if(that.optionsDefinit.n_idEntity) { 
				zone.stopEdition();
			} else {
				zone.startEdition();
			}
			t_zone.push(zone);
			that.addEvent($that, zone);
		});

		return t_zone;
	},

	addEvent: function ($that, zone) {
		var that = this;
		$that.find('.btnAjaxUpdate').click(function () {
			zone.startEdition();
			zone.ajaxLoadEntity();
		}).end()
		.find('.btnAjaxCancel').click(function () {
			zone.stopEdition();
			zone.ajaxLoadEntity();
		}).end()
		.find('.btnCancelBack').click(function () {
			window.top.fr_main.window.location= that.optionsDefinit.s_actionCancelback;	
		}).end()
		.find('.btnAjaxValidate').click(function () {
			if (that.optionsDefinit.b_validateNotation) {
				if (notationManager.validateAndDisplayError()) {
					zone.validateEdition();
				}
			} else {
				zone.validateEdition();
			}
		}).end()
		.find('.btnAjaxDelete').click(function () {
			zone.ajaxDeleteEntity(zone.selectedEntityId);
		}).end();
		if (this.optionsDefinit.b_hasButtonNew) {
			$that.find('.btnAjaxAdd').click(function () {
				zone.stopEdition();
				zone.startEdition();
			});
		} else {
			$that.find('.btnAjaxAdd').remove();
		}
	}
};