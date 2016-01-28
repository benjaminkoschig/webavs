var decompteSearch;

$(function() {
	var printed = [];

	$(".areaSearch :input").change(function() {
		$element = $(this);
		if($element.attr('id')=='noAffilie') {
			vulpeculaUtils.formatNoAffilie($element);
		}
		
		var result = false;
		$(".areaSearch :input").each( function(n,element){if ($(element).val()!='' &&  $(element).val()!='68012007') {result = true;} } );
		if (result) {
			$element = $(this);
			if($element.attr('id')=='searchModel.likeNoAffilie') {
				vulpeculaUtils.formatNoAffilie($element);
			}
			decompteSearch.ajaxFind();
		} else {
			alert("Veuillez saisir des critères de recherche.");
		}
	});
	
	$('.areaTable').on('click','.toPrintStandard',function(ev) {
		$(this).attr('disabled','disabled');
		var $tr = $(this).closest('tr'); 
		handlePrint($tr);
		var id = $tr.find('.id').text();
		var options = {
				serviceClassName:globazGlobal.decompteViewService,
				serviceMethodName:'imprimerDecompte',
				parametres:id,
				callBack:function (data) {
			}
		};
		vulpeculaUtils.lancementService(options);
	});
	
	$('.areaTable').on('click','.toPrintSpecial',function(ev) {
		$(this).attr('disabled','disabled');
		var $tr = $(this).closest('tr'); 
		handlePrint($tr);
		var id = $tr.find('.id').text();
		var options = {
				serviceClassName:globazGlobal.decompteViewService,
				serviceMethodName:'imprimerDecompteSpecial',
				parametres:id,
				callBack:function (data) {
				}
		};
		vulpeculaUtils.lancementService(options);
	});
	
	defaultTableAjaxList.init({
		s_actionAjax : globazGlobal.ACTION_AJAX,
		s_search : '.areaSearch',
		s_selector: "#",
		init : function() {
			decompteSearch = this;
			this.capage(20, [ 10, 20, 30, 50, 100 ]);
			this.addSearch();
		}
	});
	
	$('.area').on(eventConstant.AJAX_FIND_COMPLETE, function() {
		refreshView();
	});
	
	function handlePrint($tr) {
		var id = $tr.find('.id').text();
		printed.push(id);
	}
	
	function refreshView() {
		$elements = $('.toPrint');
		for(var i=0;i<$elements.length;i++) {
			var $element = $($elements[i]);
			var id = $element.closest('tr').find('.id').text();
			if($.inArray(id,printed)!==-1) {
				$element.attr('disabled','disabled');
			} else {
				$element.removeAttr('disabled');
			}
		}		
	}
});
