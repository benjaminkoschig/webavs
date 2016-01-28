function DefaultObjectDetailAjax(m_options) {
	this.ACTION_AJAX = m_options.s_actionAjax;
	this.mainContainer = $(m_options.s_detail);
	this.selectedEntityId = m_options.n_idEntity;

	this.onRetrieve = function (data) {
		var that = this;
		if (typeof m_options.onRetrieve === 'function') {
			m_options.onRetrieve.call(that, data);
		} else { 
			this.t_element = this.defaultLoadData(data, m_options.s_selector); 
		}
	};
	
	this.getParametersForLoad = function () {
		var that = this;
		if (typeof m_options.getParametersForLoad === 'function') {
			return m_options.getParametersForLoad.call(that);
		} else { 
			return {};
		}
	};

	this.getParametres = function () {
		var o_parametres = this.createMapForSendData(m_options.s_selector);
		if(typeof m_options.getParametres === 'function') {
			o_parametres = $.extend(o_parametres, m_options.getParametres());
		}
		return o_parametres;
	};

	this.onUpdate = function (data, action) {
		var that = this;
		if (typeof m_options.onUpdate === 'function') {
			m_options.onUpdate.call(that, data, action);
		} else  {
			this.superOnUpdate(data, action);
		}
	};
	
	this.onError = function (data) {
		var that = this;
		if (typeof m_options.onError === 'function') {
			m_options.onError.call(that, data);
		}
	};
	
	this.superOnUpdate = function (data) {
		var t_entityIdPath = m_options.s_entityIdPath.split(".");
		var tempData = data;
		for (var i=0; t_entityIdPath.length > i; i++) {
			tempData = tempData[t_entityIdPath[i]];
		}
		this.t_element = this.defaultLoadData(data, m_options.s_selector); 
		this.selectedEntityId = tempData;
	};

	this.getParentViewBean = function () {
		if(typeof globalViewBean != "undefined") {
			return globalViewBean;
		} else {
			return null;
		}
	};

	this.setParentViewBean = function (newViewBean) {
		globalViewBean = newViewBean;
	};

	this.stopEdition();
	// initialization
	this.init(m_options.init);
}

/*
 * L'apel a la fonction init permet d'appliquer tout les comportement par d�faut pour les zones ajax
 * La fonction init retourne un tableau contenant les zones Ajax
 */
var defaultDetailAjax = {
	options: {
		s_actionAjax: globazGlobal.ACTION_AJAX,
		s_detail: '.areaDetail',	// Sp�cifie la zone on se trouve le d�tail � peupler
		n_idEntity: 0,				// Sp�cifie l'ID primaire de l'entit� � charger
		s_selector: '#',			// Sp�cifie quel s�lecteur il faut utiliser pour la r�ception et l�envoi des donn�es  
		s_spy: null,				// Indique le chemin de l'objet qui contient le spy
		onRetrieve: null,			// Permet de red�finir la fonction onRetrieve. Utilis� pour d�finir l�action que l�on veut ex�cuter apr�s la lecture d�un d�tail
		onUpdate: null,				// Permet de red�finir la fonction onUpdate. Utilis� pour d�finir l�action que l�on veut ex�cuter apr�s la mise � jour d'un d�tail
		onError: null,				// Permet d'intervenir en cas d'erreur ajax
		b_validateNotation: false,	// Indique si il faut faire une validation du formulaire par js
		b_hasButtonNew: true,		// Permet d'indiquer si l'on veut afficher le boutton "Nouveau"
		afterStartEdition: null,	// Permet d'�xecuter une fonction apr�s un startEdition
		getParametersForLoad: null,
		s_entityIdPath:"",			// Permet d'indiquer les chemin ou se trouve l'id (Encapsulation de l'objet)
		init: function () {			// Permet de d�finir le comportement de la zone ajax
		}
	},
	optionsDefinit: null,
	m_options: {},

	init: function (m_options) {
		var that = this,
			t_zone = [];
		this.options.s_actionAjax = globazGlobal.ACTION_AJAX;
		if (m_options) {
			this.optionsDefinit = Object.create($.extend({}, that.options, m_options));
		}
		
		//Tester si s_entityIdPath est vide 
		if(!m_options.s_entityIdPath){
			alert("L'option s_entityIdPath n'est pas d�finit.")
		}
		
		// pour chaque zone de d�tail trouv�es par le s�lecteur s_detail
		$(this.optionsDefinit.s_detail).each(function () {
			var $that = $(this);
			that.optionsDefinit.$mainContainer = $that;
			var zone = new DefaultObjectDetailAjax(that.optionsDefinit);
			t_zone.push(zone);
			that.addEvent($that, zone);
		});

		return t_zone;
	},

	addEvent: function ($that, zone) {
		var that = this;
		$that.find('.btnAjaxUpdate').click(function () {
			zone.startEdition();
		}).end()
		.find('.btnAjaxCancel').click(function () {
			zone.stopEdition();
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

DefaultObjectDetailAjax.prototype = AbstractSimpleAJAXDetailZone;
