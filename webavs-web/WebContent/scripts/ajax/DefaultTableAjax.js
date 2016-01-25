function DefaultObjectTableAjax(m_options) {
	var that = this;
	this.ACTION_AJAX = m_options.s_actionAjax;
	this.mainContainer = m_options.$mainContainer;
	this.table = m_options.$table;
	this.detail = m_options.$detail;
	this.t_element = [];
	this.s_spy = m_options.s_spy;
	this.s_selector = m_options.s_selector;
	this.b_wantReloadEntityAfterUpdate = m_options.b_wantReloadEntityAfterUpdate && true;
	//this.m_options = m_options;

	this.afterRetrieve = function (data) {
		if (typeof m_options.afterRetrieve === "function") {
			m_options.afterRetrieve.call(that, data);
		} else { 
			this.t_element = this.defaultLoadData(data, m_options.s_selector); 
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
	
	this.afterUpdate = function (data){
		if(typeof m_options.afterUpdate === "function"){
			 m_options.afterUpdate.call(this, data);
		} 
	};
	
	this.afterStartEdition = function () {
		if(typeof m_options.afterStartEdition === "function"){
			 m_options.afterStartEdition();
		} else {
			this.superAfterStartEdition();
		}
	};
	
	this.prepareParamters = function (s_selector) {
		var selector = m_options.s_selector;
		if(typeof s_selector !== "undefined"){
			selector = s_selector
		} 
		var o_map = this.createMapForSendData(m_options.s_selector);
		return $.extend(o_map, this.getParametresForFind());
	};
	
	this.getParametres = function () {
		var o_map = this.prepareParamters();
		if (typeof m_options.getParametres === "function") {
			o_map = $.extend(o_map, m_options.getParametres());
		}
		return o_map;
	};
	
	this.onDeleteAjax = function () {
		var that = this;
		if (typeof m_options.onDeleteAjax === "function") {
			 m_options.onDeleteAjax.call(that);
		} else { 
			setTimeout(function () {
				that.stopEdition();
			}, 100);
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


/*
 * L'apel a la fonction init permet d'appliquer tout les comportement par d�faut pour les zones ajax
 * La fonction init retourne un tableau contenant les zones Ajax
 */

var defaultTableAjax = {
	options: {
		s_actionAjax: globazGlobal.ACTION_AJAX,
		s_container: ".area",		//Sp�cifie la zone ajax
		s_detail: ".areaDetail",	//Sp�cifie la zone on se trouve le d�tail � afficher
		s_table: ".areaTable",		//Sp�cifie la zone on se trouve le tableau � peupler. Le s�lecteur doit �tre d�finit sur l��l�ment <table>
		s_selector: "#",			//Sp�cifie quel s�lecteur il faut utiliser pour la r�ception et l�envoi des donn�es  
		s_search: ".areaSearch",	//Indique la zone de recherche 
		s_spy: null,				//Indique le chemin de l'objet qui contient le spy
		findParameters: null,		//Permet de red�finir la fonction findParameters Utilis� pour ex�cuter la recherche de la liste doit retourner une map de crit�res de recherche
		getParametres: null,		//Permet de red�finir la fonction getParametres. Utilis� pour ex�cuter trouv�e les valeurs � modifier ou cr�er dans la zone ajax. Doit retourner une map de valeurs
		addParametersForRead: null,	//Permet d'envoyer des param�tres supl�mentaire pour le read
		afterRetrieve: null,		//Permet de red�finir la fonction afterRetrieve. Utilis� pour d�finir l�action que l�on veut ex�cuter apr�s la lecture d�un d�tail
		clearFields: null,			//Permet de red�finir la fonction clearFields. Permet de d�finir quel champs il faut vid� pour lors de l�affichage d�un d�tail ou de l�annulation
		b_validateNotation: false, 	//Inidque si il faut faire une validation du formulaire par js
		b_hasButtonNew: true,		//Permet d'indiquer si l'on veut afficher le boutton "Nouveau"
		afterStartEdition: null,    //Permet d'�xecuter une fonction apr�s un startEdition
		init: function () {			//Permet de d�finir le comportement de la zone ajax
			this.capage(5, [10, 20, 30, 50, 100]);	// D�finit le comportement d'une capage
			//this.list(5, [10, 20, 30, 50, 100])	// D�finit le comportement d'une liste
			//this.addSearch();					 	// Permet d'ajoute le comportement de recherche dans la zone de recherche 
		} 
	},
	optionsDefinit: null,
	m_options:{},
	
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
			var zone = new DefaultObjectTableAjax(that.optionsDefinit);
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
				zone.executeAddEvent();
			});
		} else {
			$that.find('.btnAjaxAdd').remove();
		}
	}
};

DefaultObjectTableAjax.prototype = AbstractScalableAJAXTableZone;
