/*
 * CBU, 03.2012
 */

annonceAjax = {
		init: function () {
			this.initTableAjax();
			this.initButton();
			$(".imgLoading").remove();
		},
		
		initTableAjax: function () {
			defaultTableAjax.init({			
				s_actionAjax: ACTION_AJAX_ANNONCES_ASSURANCE,
				s_container: ".areaMembreAnnonces",
				s_detail: ".areaDetail",
				s_table: ".areaDataTable",
				
				init: function () {					
					this.list(5, [10, 20, 30, 50, 100]);
				},
				
				getParametresForFind: function () {
					return {
						'listeAnnonceAjaxListViewBean.annoncesCaisseSearch.forNoCaisseMaladie':$('#idTiersCaisse').val()			
					};
				},
				getParametres : function () {					
					return {
						"annoncesCaisse.noCaisseMaladie": $("#idTiersCaisse").val()
					};
				},
				afterRetrieve: function (data) {			
					this.defaultLoadData(data, "#", false);	
				}
			});	
		},
		
		initButton: function () {
			$(".buttonListe").live('click',function() {
				var s_idAnnonce = $(this).attr("id"); 
				var s_date = s_idAnnonce.split('_')[2];	
				alert('Liste au '+s_date);
				return false;
			});
			$(".buttonAnnoncer").live('click',function() {
				var s_idAnnonce = $(this).attr("id"); 
				var s_date = s_idAnnonce.split('_')[2];	
				alert('Annoncer au '+s_date);
				return false;
			});
		}
};

$(function () {
	annonceAjax.init();
});