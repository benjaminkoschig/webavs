var decompteSearch;

$(function() {
	var toPrints = [];

	$('.areaSearch :input,select').change(function() {
		var $element = $(this);
		$('#checkAll').prop('checked',false);
		if($element.attr('id')=='noAffilie') {
			vulpeculaUtils.formatNoAffilie($element);
		}
		decompteSearch.ajaxFind();
	});
	
	$('#checkAll').click(function() {
		if($(this).prop('checked')) {
			findTOs();
		} else {
			toPrints = [];
			refreshView();
		}
	});
	
	$('.areaTable').on('click','.toPrint',function(ev) {
		var $tr = $(this).closest('tr');
		handlePrint($tr);
		ev.stopPropagation();
	});
	
	$('.areaTable').on('click','tr',function(ev) {
		$tr = $(this);
		var $toPrint = $tr.find('.toPrint'); 
		var newState = !$toPrint.prop('checked');
		$toPrint.prop('checked',newState);
		handlePrint($tr);
	});
	
	$('#imprimer').click(function() {
		if(!isDocumentSelected()) {
			showErrorDialog(globazGlobal.messagePasDeDocumentSelectionne);
			return;
		}
		$(this).attr('disabled','disabled');
		var joinArray = toPrints.join("|");
		var options = {
				serviceClassName:globazGlobal.decompteViewService,
				serviceMethodName:'imprimerTO',
				parametres:joinArray,
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
	
	function findTOs() {
		var idPassage = $('#idPassage').val();
		var noAffilie = $('#noAffilie').val();
		var etat = $('#etatTaxation').val();
		
		var o_search = {
				idPassage : idPassage,
				noAffilie : noAffilie,
				etat : etat
		};
		
		var options = {
				serviceClassName:globazGlobal.decompteViewService,
				serviceMethodName:'findTOs',
				parametres:JSON.stringify(o_search),
				callBack:function (data) {
					toPrints = data;
					refreshView();
				}
		};
		vulpeculaUtils.lancementServiceSync(options);
	}
	
	function handlePrint($tr) {
		var state = $tr.find('.toPrint').prop('checked');
		var id = $tr.find('.id').text();
		if(state) {
			toPrints.push(id);
		} else {
			var index = $.inArray(id, toPrints);
			delete toPrints[index];
		}
	}
	
	function refreshView() {
		$elements = $('.toPrint');
		for(var i=0;i<$elements.length;i++) {
			var $element = $($elements[i]);
			var id = $element.closest('tr').find('.id').text();
			if($.inArray(id,toPrints)!==-1) {
				$element.prop('checked',true);
			} else {
				$element.prop('checked',false);
			}
		}		
	}
	
	function isDocumentSelected() {
		return $('.toPrint:checked').length > 0;
	}
});
