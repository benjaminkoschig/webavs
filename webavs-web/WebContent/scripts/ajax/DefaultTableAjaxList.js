function DefaultObjectTableAjax(m_options) {
	var that = this;
	this.ACTION_AJAX = m_options.s_actionAjax;
	this.mainContainer = m_options.$mainContainer;
	this.table = m_options.$table;
	this.detail = m_options.$detail;
	this.t_element = [];
	this.s_selector = m_options.s_selector;
	this.b_wantReloadEntityAfterUpdate = m_options.b_wantReloadEntityAfterUpdate && true;
	this.userActionDetail = m_options.userActionDetail;
	
	this.getParametresForFind = function () {
		var m_map = {};
		if (typeof m_options.getParametresForFind === "function") {
			m_map = m_options.getParametresForFind.call(that);
		} else { 
			m_map = ajaxUtils.createMapForSendData(m_options.$search, m_options.s_selector);
		}
		m_map['changeStack'] = m_options.b_changeStack;
		return m_map;
	};
	
	this.addTableEventOnElements = function ($elements) {
		var that = this, $trs = null;
		if($elements.length == 0){
			$trs = this.table;
		} else {
			$trs = $elements;
		}
		$trs.delegate('td', 'click', function (event) {
			var $this = $(this), $parent = $this.parent();
			if (that.isEntitySelectable() &&  event.target == this && !$parent.is(".notSortable") && $parent.attr('idEntity')) {
				window.top.fr_main.window.location=m_options.userActionDetail+$parent.attr('idEntity');
			}
		});	
	};		
		
	this.getParentViewBean = function () {};
	
	this.setParentViewBean = function (newViewBean) {};
	
	this.formatTableTd = function ($elementTable) {};
	// initialization
	this.init(m_options.init);
}


/*
 * L'apel a la fonction init permet d'appliquer tout les comportement par défaut pour les zones ajax
 * La fonction init retourne un tableau contenant les zones Ajax
 */

var defaultTableAjaxList = {
	options: {
		s_actionAjax: globazGlobal.ACTION_AJAX,
		s_container: ".area",		//Spécifie la zone ajax
		s_detail: ".areaDetail",	//Spécifie la zone on se trouve le détail à afficher
		s_table: ".areaTable",		//Spécifie la zone on se trouve le tableau à peupler. Le sélecteur doit être définit sur l’élément <table>
		s_selector: "#",			//Spécifie quel sélecteur il faut utiliser pour la réception et l’envoi des données  
		s_search: ".areaSearch",	//Indique la zone de recherche 
		findParameters: null,		//Permet de redéfinir la fonction findParameters Utilisé pour exécuter la recherche de la liste doit retourner une map de critères de recherche
		getParametres: null,		//Permet de redéfinir la fonction getParametres. Utilisé pour exécuter trouvée les valeurs à modifier ou créer dans la zone ajax. Doit retourner une map de valeurs
		addParametersForRead: null,	//Permet d'envoyer des paramétres suplémentaire pour le read
		afterRetrieve: null,		//Permet de redéfinir la fonction afterRetrieve. Utilisé pour définir l’action que l’on veut exécuter après la lecture d’un détail
		clearFields: null,			//Permet de redéfinir la fonction clearFields. Permet de définir quel champs il faut vidé pour lors de l’affichage d’un détail ou de l’annulation
		b_validateNotation: false, 	//Inidque si il faut faire une validation du formulaire par js
		b_hasButtonNew: true,		//Permet d'indiquer si l'on veut afficher le boutton "Nouveau"
		b_changeStack: false,		//Permet d'indiquer si l'on souhaite que la stack (précédente URL) soit mise à jour avec les données de l'action
		afterStartEdition: null,    //Permet d'éxecuter une fonction après un startEdition
		userActionDetail : null,
		init: function () {			//Permet de définir le comportement de la zone ajax
			this.list(10, [20, 30, 50, 100]);	// Définit le comportement d'une liste 
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
	}
	
};

DefaultObjectTableAjax.prototype = AbstractScalableAJAXTableZone;
