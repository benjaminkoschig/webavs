var administrationSearch;
$(function () {
	var changedForced = false;
	$(".areaSearch :input").change(function() {
		if(!changedForced) {
			administrationSearch.ajaxFind();
		}
		else {
			changedForced = false;
		}
	});
	$(".areaSearch :input").keydown(function(e) {
		changedForced = false;
		if(e.which == 13) {
			changedForced = true;
			administrationSearch.ajaxFind();
		}
	});
	
	defaultTableAjax.init({

		s_actionAjax: globazGlobal.ACTION_AJAX,
		s_spy: "currentEntity.admin",

		clearFields: function () {
			this.defaultClearFields();
			$('#currentEntity\\.admin\\.codeAdministration').val('');
			$('.adresse').html('');
			$('.spyFormatted').html('');
		},

		init: function () {
			administrationSearch = this;
			this.capage(30, [10, 20, 30, 50, 100]);
			this.addSearch();
			
			if (!$("#currentEntity.tiers.designation1").html()) {
				$(".areaDetail").hide();
			}
		},
		
		getParametresForFind: function() {
			var map = ajaxUtils.createMapForSendData(defaultTableAjax.optionsDefinit.$search, this.s_selector);
			map['searchModel.forDesignation1Like'] = vulpeculaUtils.toUpper(map['searchModel.forDesignation1Like']);
			return map;
		},
		
		afterRetrieve: function(data) {
			this.defaultLoadData(data, "#");
			$("#administrationLink").attr("href", "pyxis?userAction=pyxis.tiers.administration.afficher&selectedId=" + data.currentEntity.tiers.idTiers);
		}
	});
});


