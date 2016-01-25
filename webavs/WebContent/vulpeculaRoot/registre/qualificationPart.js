var qualificationSearch;
var parametreTable;

$(function () {
	var changedForced = false;
	$(".areaSearch :input").change(function() {
		if(!changedForced) {
			qualificationSearch.ajaxFind();
		}
		else {
			changedForced = false;
		}
	});
	$(".areaSearch :input").keydown(function(e) {
		changedForced = false;
		if(e.which == 13) {
			changedForced = true;
			qualificationSearch.ajaxFind();
		}
	});
	
	//Initialisation des tabs
	$("#tabLiaisons").tabs();

	defaultTableAjax.init({
		s_actionAjax: globazGlobal.ACTION_AJAX,
		s_spy: "currentEntity.admin",

		init: function () {
			qualificationSearch = this;
			this.capage(5, [10, 20, 30, 50, 100]);
			this.addSearch();
			
			parametreTable = vulpeculaTable.create({
				s_message_erreur: globazGlobal.ERREUR,
				getExtrasParameters : function() {
					var idTiersAdministration = $("#currentEntity\\.admin\\.idTiersAdministration").text();
					var extras = {
							idTiersAdministration : idTiersAdministration
					};
					return extras;
				}
			});
		},
		
		getParametresForFind: function() {
			var map = ajaxUtils.createMapForSendData(defaultTableAjax.optionsDefinit.$search, this.s_selector);
			map['searchModel.forDesignation1Like'] = vulpeculaUtils.toUpper(map['searchModel.forDesignation1Like']);
			return map;
		},

		clearFields: function () {
			parametreTable.clear();
			this.defaultClearFields();
			//On vide l'adresse et le spy
			$('.spyFormatted').html('');
			//On vide le clone, mais il faut quand même créér une ligne vide
			var $detail = $($("#template").html());
			$("#tableParametres").empty().append($detail);
			$detail.find(":input").prop("disabled", true);
			notationManager.addNotationOnFragment($detail);
		},
		
		getParametres: function () {
			var parameters = parametreTable.save();
			return parameters;
		},
		afterRetrieve: function (data) {
			parametreTable.load(data.qualificationsGSON);
			
			this.detail.show();
		
			// provient du afterRetrieve parent
			this.t_element = this.defaultLoadData(data, this.s_selector);
		
			this.$formElement = this.detail.find('input,select,textarea').not(this.$inputsButton);
			this.disabeldEnableForm(true);
		},
		afterStartEdition: function () {
			parametreTable.editRow();
		},
		afterUpdate: function (viewbean){
			this.clearFields();
			this.t_element = this.defaultLoadData(viewbean, this.s_selector);
			this.$formElement = this.detail.find('input,select,textarea').not(this.$inputsButton);
			this.disabeldEnableForm(true);
			this.stopEdition();
		}
	});
});


