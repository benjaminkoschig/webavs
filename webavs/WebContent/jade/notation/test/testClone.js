/**
 * @author PBA
 */
var testClone = {

	notations: [],
	$fieldsets: null,

	init: function () {
		this.$fieldsets = $('fieldset').hide();
		
		this.initEventButtons();

		this.loadNotations();
		this.buildAccordion();
	},

	buildAccordion: function (){
		var $divAccordion = $('<div/>',{id:'clones'});
		this.$fieldsets.each(function(){
			var $unFieldset = $(this);
			var $legend = $unFieldset.find('legend');
			var s_title = '';
			var $notations = null;

			if($legend.length == 1){
				s_title = $legend.eq(0).text();

				var s_notationFilter = notationManager.createNameObjForFilter();
				$notations = $($unFieldset.contents().not(':first'));

				var $title = $('<h3/>');
				var $link = $('<a/>', {'href':'#'});
				$link.text(s_title);
				$title.append($link);

				var $notationDiv = $('<div/>');
				var $tableNotations = $('<table/>').css('width','100%');
				var $tbodyNotations = $('<tbody/>');
				$tableNotations.append($tbodyNotations);

				var $tr = $('<tr/>',{'data-g-clone':'waitForManagerToSetClone:false'}).css('border','1px solid #AAAAAA');
				var $td = $('<td/>', {'width':'80%'});
				$td.append($notations).append('<hr/>');
				$tr.append($td);
				$tbodyNotations.append($tr);
				notationManager.addObjToElement($tr.get(0));

				$notationDiv.append($tableNotations);

				$divAccordion.append($title).append($notationDiv);
			}
		});
		$divAccordion.appendTo('#selectCloneDiv');
		$divAccordion.accordion({active:false,collapsible:true});
	},

	loadNotations: function () {
		for (var fonction in globazNotation) {
			this.notations.push(fonction);
		}
		this.notations.sort();
	},
	
	initEventButtons: function () {
		var $select = $('input,select');

		$("#locker").button().click( function() {
			$select.attr('disabled',true);
			$select.change();
		    $("html").trigger(eventConstant.AJAX_DISABLE_ENABLED_INPUT);
		});

		$("#deLocker").button().click( function() {
			$select.attr('disabled',false);
			$select.change();
		    $("html").trigger(eventConstant.AJAX_DISABLE_ENABLED_INPUT);
		});

		$('#btnCan, #btnNew ,#btnVal ,#btnUpd ,#btnDel').button();

		$("#ajaxLoadData").button().click( function() {
			$('.mainContainerAjax').triggerHandler(eventConstant.AJAX_LOAD_DATA);
		});

		$("#ajaxShowDetailRefresh").button().click( function() {
			$('.mainContainerAjax').triggerHandler(eventConstant.AJAX_DETAIIL_REFRESH);
		});

		$("#ajaxShowDetail").button().click( function() {
			$('.mainContainerAjax').triggerHandler(eventConstant.AJAX_STOP_SHOW_DETAIL);
		});

		$("#ajaxStopEdition").button().click( function() {
			$('.mainContainerAjax').triggerHandler(eventConstant.AJAX_STOP_EDITION);
		});

		$("#ajaxValidateEditon").button().click( function() {
			$('.mainContainerAjax').triggerHandler(eventConstant.AJAX_VALIDATE_EDITION);
		});

		$("#ajaxUpdateComplete").button().click( function() { 
			$('.mainContainerAjax').triggerHandler(eventConstant.AJAX_UPDATE_COMPLETE); 
		});

		$('#validateAndDisplayError').button().click(function(){
			notationManager.validateAndDisplayError();
		});
	}
};

jsManager.addBefore(function () {
	testClone.init();
}, 'initialisation spéciale pour le test du clonage');


$(document).ready(function(){
	$('html').triggerHandler(eventConstant.AJAX_INIT_DONE);
});