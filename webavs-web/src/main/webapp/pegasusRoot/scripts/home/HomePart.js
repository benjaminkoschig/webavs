

function HomePart(container) {
	// variables
	this.ACTION_AJAX = ACTION_AJAX;
	this.mainContainer = container;
	this.modifiedZoneClass = "areaPeriodesModified";
	//this.currentViewBean=homeAjaxViewBean; 
	this.selectedEntityId = PAGE_ID_HOME;
	this.areaAllPeriodes = $("#areaAllPeriodes");
	// functions
	
	this.onRetrieve = function ($data) {
		$(this.mainContainer)
				.find('#home\\.simpleHome\\.idTiersHome').val($data.find('idTiers').text()).end()
				.find('.detailAdresseTiers').html($data.find('adresseTiers').text().replaceAll('\n', '<br>')).end()
				.find('#tiersWidget').val('').end()
				.find('.external_link').attr('href', urlTiers + $data.find('idTierss').text()).end()
				.find('#home\\.simpleHome\\.nomBatiment').val($data.find('nomBatiment').text()).end()
				.find('#home\\.simpleHome\\.isHorsCanton').val($data.find('isHorCanton').text()).end()
				.find('#home\\.simpleHome\\.numeroIdentification').val($data.find('noIdentification').text()).end();

	};

	this.getParametres = function () {
		return this.createMapForSendData("#");;
	};

	this.onUpdate = function (data) {
		var $data =  $(data);
		PAGE_ID_HOME = $data.find('idHome').text(); 
		notationManager.addNotationOnFragment(this.areaAllPeriodes);
		if($data.find('isHorCanton').text() === 'true') {
			$("#isHorsCanton").removeClass("ui-icon-closethick");
			$("#isHorsCanton").addClass("ui-icon-check");
		} else {
			$("#isHorsCanton").removeClass("ui-icon-check");
			$("#isHorsCanton").addClass("ui-icon-closethick");
		}
	
	};
	
	this.getParentViewBean = function () {
		return home;
	};
	
	this.setParentViewBean = function (newViewBean) {
		home = newViewBean;
	};

	this.init(function () {
		if (globazGlobal.isNew) {
			this.startEdition();
		} else {  
			this.stopEdition(); 
		}
	});
}

HomePart.prototype = AbstractSimpleAJAXDetailZone;


//fonction d'initialisation de la page lorsque JQuery est prêt
$(function () {
	
	var homePart = new HomePart($('#homeDetail'));
	if (globazGlobal.isNew) {
		homePart.areaAllPeriodes.hide();
	}

	$('#homeDetail')
	.find('.btnAjaxUpdate').click(function () {
		homePart.ajaxLoadEntity();
		homePart.startEdition();
		$('.external_link').hide();
	}).end()
	.find('.btnAjaxCancel').click(function () {
		homePart.ajaxLoadEntity();
		homePart.stopEdition();
		$('.external_link').show();
	}).end()
	.find('.btnAjaxValidate').click(function () {
		homePart.validateEdition();
		homePart.mainContainer.bind(eventConstant.AJAX_DETAIIL_REFRESH, function () {
			if (globazGlobal.isNew) {
				homePart.areaAllPeriodes.show();
			}
		});
		$('.external_link').show();
	});

});