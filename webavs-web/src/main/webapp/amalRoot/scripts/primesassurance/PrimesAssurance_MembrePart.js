/*
 * CBU, 03.2012
 */

$(function () {
	defaultTableAjax.init({			
		s_actionAjax: ACTION_AJAX_PRIMES_ASSURANCE,
		s_container: ".areaMembrePrimes",
		s_detail: ".areaDetail",
		s_table: ".areaDataTable",
		
		init: function () {		
			this.capage(5, [10, 20, 30]);
			this.hideDetail();
			//$(".areaDetail").dialog('autoOpen: false');
		},
		
		getParametresForFind: function () {
			return {
				'listPrimesAssuranceAjaxListViewBean.simplePrimesAssuranceSearch.forIdTiers':$('#idTiersCaisse').val()				
			};
		},
		getParametres : function () {
			return {
				"simplePrimesAssurance.idTiers": $("#idTiersCaisse").val(),
				"simplePrimesAssurance.noCaisseMaladie": $("#noCaisseMaladie").val()
			};
		},
		afterRetrieve: function (data) {	
			this.defaultLoadData(data, "#");
		}
	});
	$(".imgLoading").remove();
	
	$('.areaMembrePrimes').each(function(){
		var $that=$(this);
		
		$that.find('.btnAjaxValidate').click(function(){
			$(".areaDetail").dialog('close');
		}).end()
		.find('.btnAjaxCancel').click(function(){
			$(".areaDetail").dialog('close');
		}).end()
		.find('.btnAjaxDelete').click(function(){
			$(".areaDetail").dialog('close');
		}).end()
		.find('.btnAjaxAdd').click(function(){
			$(".areaDetail").dialog({
				closeOnEscape: true, 
				minHeight: 350, 
				minWidth: 350, 
				title: "Edition / Cr&eacute;ation d'une prime", 
				beforeClose: function(event, ui) {
					$(".ui-icon-refresh").click();
					$(".ajaxOverlay").remove();
				}
			});
			$(".ui-dialog .areaDetail div.btnAjax div.btnAjaxAdd").hide();
			$(".ui-dialog .areaDetail div.btnAjax").show();
		}).end();
	});	
	
	$(".areaMembrePrimes .areaDataTable").delegate('td', 'click', function (event) {		
		$(".areaDetail").dialog({
			closeOnEscape: true, 
			minHeight: 350, 
			minWidth: 350, 
			title: "Edition / Cr&eacute;ation d'une prime",
			open: function(event, ui) {
				$(".ui-dialog .areaDetail div.btnAjax .btnAjaxUpdate").focus();
			},
			beforeClose: function(event, ui) {
				$(".ui-icon-refresh").click();
				$(".ajaxOverlay").remove();
			}			
		});
		$(".ui-dialog .areaDetail div.btnAjax .btnAjaxAdd").hide();
		$(".ui-dialog .areaDetail div.btnAjax .btnAjaxUpdate").show();
	});
});