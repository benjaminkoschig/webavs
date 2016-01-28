var administrationSearch;

var zones;
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
		$caisseMetier: null,
		s_actionAjax: globazGlobal.ACTION_AJAX,
		s_spy: "currentEntity.admin",

		clearFields: function () {
			this.defaultClearFields();
			$('#currentEntity\\.admin\\.codeAdministration').val('');
			$('.adresse').html('');
			$('.spyFormatted').html('');
		},

		init: function () {
			var that = this;
			administrationSearch = this;
			this.capage(30, [5, 10, 20, 30, 50, 100]);
			this.addSearch();
			this.mainContainer.on(eventConstant.AJAX_FIND_COMPLETE, function() {
				if(globazGlobal.idSyndicat.length>0) {
					var element = that.mainContainer.find('tr[idEntity="'+globazGlobal.idSyndicat+'"]').find('td')[0];
					element.click();
				}
			});
			
			if (!$("#currentEntity.tiers.designation1").html()) {
				$(".areaDetail").hide();
			}
			
			$('.areaParametres').on('click','td',function() {
				var idEntity = $(this).parent('tr').attr('identity');
				window.location.href = "vulpecula?userAction=vulpecula.registre.parametresyndicat.afficher&selectedId=" + idEntity;
			});
		},
		
		getParametresForFind: function() {
			var map = ajaxUtils.createMapForSendData($('.areaSearch'), '#');
			map['searchModel.forDesignation1Like'] = "%" + vulpeculaUtils.toUpper(map['searchModel.forDesignation1Like']);
			return map;
		},
		
		afterRetrieve: function(data) {
			var funcParametresForFind = (function(donnees) {
				function getParametresForFind() {
					var $caisseMetier = $('#caisseMetier');
					var idSyndicat = donnees.currentEntity.admin.idTiersAdministration;
					m_map = {'idSyndicat' : idSyndicat};
					var caisseMetier = $caisseMetier.val();
					if(caisseMetier.length>0) {
						m_map['idCaisseMetier'] = caisseMetier;
					}
					return m_map;
				}
				return getParametresForFind;
			})(data);
			
			if(zones) {
				zone = zones[0];
				zone.getParametresForFind = funcParametresForFind;
				zone.ajaxFind();
			} else {
				zones = defaultTableAjax.init({
					s_actionAjax : 'vulpecula.registre.parametresyndicatAjax',
					s_container : ".areaParametres",
					s_table : ".areaTableParametres",
					s_search : ".areaSearchParametres",
					userActionDetail : "vulpecula?userAction=vulpecula.registre.parametressyndicat.afficher&selectedId=",
					init : function() {
						var that = this;
						this.list(20, [5, 10, 20, 30, 50, 100]);
						this.$caisseMetier = $('#caisseMetier');
						this.$caisseMetier.change(function() {
							that.ajaxFind();
						});
						this.mainContainer.on(eventConstant.AJAX_FIND_COMPLETE, function() {
							that.$caisseMetier.removeAttr('disabled');
						});
					},
					getParametresForFind: funcParametresForFind
				});
			}
			this.defaultLoadData(data, "#");
			$('input').attr('disabled',false);
			$("#administrationLink").attr("href", "pyxis?userAction=pyxis.tiers.administration.afficher&selectedId=" + data.currentEntity.tiers.idTiers);
		}
	});
	
	$('#btnNouveau').click(function () {
		var idSyndicat = $('#currentEntity\\.admin\\.idTiersAdministration').val();
		window.location.href='vulpecula?userAction=vulpecula.registre.parametresyndicat.afficher&_method=add&idSyndicat='+idSyndicat;
	});
});


