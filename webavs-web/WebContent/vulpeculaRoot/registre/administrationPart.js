var administrationSearch;
var parametreTable;

$(function () {
	var changedForced = false;
	var $inputs = $(".areaSearch :input");
	var key_enter = 13;
	$inputs.change(function() {
		if(!changedForced) {
			administrationSearch.ajaxFind();
		}
		else {
			changedForced = false;
		}
	});
	$inputs.keydown(function(e) {
		changedForced = false;
		if(e.which == key_enter) {
			changedForced = true;
			administrationSearch.ajaxFind();
		}
	});

	$("#tabLiaisons").tabs();

	defaultTableAjax.init({
		s_actionAjax: globazGlobal.ACTION_AJAX,
		s_spy: "currentEntity.admin",

		init: function () {
			administrationSearch = this;
			this.capage(10, [10, 20, 30, 50, 100]);
			this.addSearch();

			parametreTable = vulpeculaTable.create({
				s_message_erreur: globazGlobal.ERREUR,
				getExtrasParameters : function() {
					var idTiersAdministration = $("#currentEntity\\.admin\\.idTiersAdministration").text();
					var extras = {
							idAdministration : idTiersAdministration
					};
					return extras;
				}
			});
		},
		
		afterStartEdition: function () {
			parametreTable.editRow();
		},

		clearFields: function () {
			this.detail.hide();
			this.defaultClearFields();
			$('#currentEntity\\.admin\\.codeAdministration').val('');
			$('.spyFormatted').html('');
			// Vide l'adresse
			$('.adresse').html('');

			parametreTable.clear();
		},

		afterUpdate: function (viewbean){
			this.clearFields();
			this.t_element = this.defaultLoadData(viewbean, this.s_selector);
			this.$formElement = this.detail.find('input,select,textarea').not(this.$inputsButton);
			this.disabeldEnableForm(true);
			this.stopEdition();
		},
		
		getParametresForFind: function() {
			var map = ajaxUtils.createMapForSendData(defaultTableAjax.optionsDefinit.$search, this.s_selector);
			map['searchModel.forDesignation1Like'] = vulpeculaUtils.toUpper(map['searchModel.forDesignation1Like']);
			return map;
		},

		afterRetrieve: function (viewbean) {
			parametreTable.load(viewbean.listParametres);
			
			this.detail.show();
		
			// provient du afterRetrieve parent
			this.t_element = this.defaultLoadData(viewbean, this.s_selector);
		
			this.$formElement = this.detail.find('input,select,textarea').not(this.$inputsButton);
			this.disabeldEnableForm(true);
			// Ajoute un lien sur l'administrateur de Pyxis
			$("#administrationLink").attr("href", "pyxis?userAction=pyxis.tiers.administration.afficher&selectedId=" + viewbean.currentEntity.tiers.idTiers);
		},
		
		/**
		 * Se lance lors de la validation
		 */
		getParametres: function () {
			var parametres = parametreTable.save();
			return parametres; 
		}
	});
});
