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
 * L'apel a la fonction init permet d'appliquer tout les comportement par d�faut pour les zones ajax
 * La fonction init retourne un tableau contenant les zones Ajax
 */

var defaultTableAjaxList = {
	options: {
		s_actionAjax: globazGlobal.ACTION_AJAX,
		s_container: ".area",		//Sp�cifie la zone ajax
		s_detail: ".areaDetail",	//Sp�cifie la zone on se trouve le d�tail � afficher
		s_table: ".areaTable",		//Sp�cifie la zone on se trouve le tableau � peupler. Le s�lecteur doit �tre d�finit sur l��l�ment <table>
		s_selector: "#",			//Sp�cifie quel s�lecteur il faut utiliser pour la r�ception et l�envoi des donn�es  
		s_search: ".areaSearch",	//Indique la zone de recherche 
		findParameters: null,		//Permet de red�finir la fonction findParameters Utilis� pour ex�cuter la recherche de la liste doit retourner une map de crit�res de recherche
		getParametres: null,		//Permet de red�finir la fonction getParametres. Utilis� pour ex�cuter trouv�e les valeurs � modifier ou cr�er dans la zone ajax. Doit retourner une map de valeurs
		addParametersForRead: null,	//Permet d'envoyer des param�tres supl�mentaire pour le read
		afterRetrieve: null,		//Permet de red�finir la fonction afterRetrieve. Utilis� pour d�finir l�action que l�on veut ex�cuter apr�s la lecture d�un d�tail
		clearFields: null,			//Permet de red�finir la fonction clearFields. Permet de d�finir quel champs il faut vid� pour lors de l�affichage d�un d�tail ou de l�annulation
		b_validateNotation: false, 	//Inidque si il faut faire une validation du formulaire par js
		b_hasButtonNew: true,		//Permet d'indiquer si l'on veut afficher le boutton "Nouveau"
		b_changeStack: false,		//Permet d'indiquer si l'on souhaite que la stack (pr�c�dente URL) soit mise � jour avec les donn�es de l'action
		afterStartEdition: null,    //Permet d'�xecuter une fonction apr�s un startEdition
		userActionDetail : null,
		init: function () {			//Permet de d�finir le comportement de la zone ajax
			this.list(10, [20, 30, 50, 100]);	// D�finit le comportement d'une liste 
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
