var AbstractSimpleAJAXDetailZone = {
	
	isModifyingEntity: false,
	modifiedZoneClass: 'areaAJAXModified',	
	selectedEntityId: null,	
	mainContainer: null,
	currentViewBean: null,
	$inputsButton: null,
	$formElement: null,
	ACTION_AJAX: null,
	XML_DETAIL_TAG: "message",
		
	stopEdition: function () {
		this.disabeldEnableForm(true);
		this.mainContainer.find('input:button').hide().filter('.btnAjaxUpdate, .btnAjaxDelete').show();
		this.mainContainer.removeClass(this.modifiedZoneClass);
		this.isModifyingEntity = false;
		this.mainContainer.triggerHandler(eventConstant.AJAX_STOP_EDITION);
		this.triggerEvent();
	},
	
	triggerEvent: function () {
		this.mainContainer.triggerHandler(eventConstant.AJAX_DETAIIL_REFRESH);
	},
	
	disabeldEnableForm: function (b_disabeEnable) {
		ajaxUtils.disabeldEnableForm(this.mainContainer, this.mainContainer.find(':input').not(":hidden,:button"), b_disabeEnable);
	},
	
	startEdition: function () {
		this.disabeldEnableForm(false);
		this.mainContainer.find('input:button').hide().filter('.btnAjaxValidate,.btnAjaxCancel').show();
		this.isModifyingEntity = true;
		ajaxUtils.addFocusOnFirstElement(this.mainContainer);
		this.mainContainer.addClass(this.modifiedZoneClass);
		this.triggerEvent();
	},
	
	serialize: function ($detail) {
		return ajaxUtils.serialize(this.mainContainer);
	},
	
	getParametersForLoad: function () {
		return {};
	},
	
	ajaxLoadEntity: function () {
		var that = this;
		var o_defaultMap = {"userAction": this.ACTION_AJAX + ".afficherAJAX",
			"idEntity": this.selectedEntityId};
		var o_map = $.extend(true, o_defaultMap, that.getParametersForLoad());
		$.ajax({
			data: o_map,
			success: function (data) {
						that.onLoadAjaxData(data);
					},
			type: "GET"
		});
	},
	
	defaultLoadData: function ($data, s_selector, b_shortId) {
		var $detail = this.mainContainer;
		ajaxUtils.defaultLoadData($data, $detail, s_selector, b_shortId);
	},
	
	createMapForSendData: function (s_selector) {
		return ajaxUtils.createMapForSendData(this.mainContainer, s_selector);
	},
	
	onLoadAjaxData: function (data) {
		var $tree = $(data), isXml = $.isXMLDoc(data);
		if (ajaxUtils.hasError(data)) {
			ajaxUtils.displayError(data);
			ajaxUtils.afterAjaxComplete(this.mainContainer);
			return;
		} else {
			ajaxUtils.displayLogsIfExsite(data);
		}
		if ($tree.find(this.XML_DETAIL_TAG).length > 0 || $.isPlainObject(data)) {
			if (isXml) {
				this.currentViewBean = $tree.find('viewBean').text();
				data = $tree;
			} else {
				this.currentViewBean = data.viewBeanSerialized;
				data = data.viewBean;
			}
			this.onRetrieve(data);
			this.mainContainer.find('span').trigger('change', {from:eventConstant.AJAX_CHANGE});
			this.$formElement.trigger('change', {from:eventConstant.AJAX_CHANGE});
			this.mainContainer.find('span').trigger('ajaxChange.ajax');
			this.$formElement.trigger('ajaxChange.ajax');
			this.mainContainer.triggerHandler(eventConstant.AJAX_LOAD_DATA);
		} else {
			globazNotation.logging.error("Arbre vide ou invalide");
			//window.location.href="/webavs/pyxis?userAction=quit";
		}
	},	
	
	ajaxUpdateEntity: function () {
		var that = this;
		var parametres = this.getParametres();
		ajaxUtils.beforeAjax(this.mainContainer);
		parametres.userAction = this.ACTION_AJAX + ".modifierAJAX";
		parametres.viewBean = this.currentViewBean;
		parametres.parentViewBean = this.getParentViewBean;
		
		$.ajax({
			data: parametres,
			contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
			success: function (data) {
				that.onUpdateAjaxComplete(data,'update');
			},
			type: "POST"
		});					
	},
	
	validateEdition: function () {
		if (this.selectedEntityId && this.selectedEntityId !== "null") {
			this.ajaxUpdateEntity(this.selectedEntityId);
		} else {
			this.ajaxAddEntity();
		}
		this.mainContainer.triggerHandler(eventConstant.AJAX_VALIDATE_EDITION);		
	},
	
	ajaxAddEntity: function () {
		var that = this;
		var parametres = this.getParametres();
		
		ajaxUtils.beforeAjax(this.mainContainer);
		parametres.userAction = this.ACTION_AJAX + ".ajouterAJAX";
		//parametres.viewBean=this.currentViewBean;
		//parametres.parentViewBean=this.getParentViewBean;
		$.ajax({
			data: parametres,
			success: function (data) {
				that.onUpdateAjaxComplete(data,'add');
			},
			type: "POST"
		});
	},
	
	ajaxDeleteEntity: function () {
		var that = this;
		var parametres = this.getParametres();
		
		ajaxUtils.beforeAjax(this.mainContainer);
		parametres.userAction = this.ACTION_AJAX + ".supprimerAJAX";
		parametres.viewBean=this.currentViewBean;
		parametres.parentViewBean=this.getParentViewBean;
		$.ajax({
			data: parametres,
			success: function (data) {
				that.onUpdateAjaxComplete(data,'del');
				that.onDelete(data);
			},
			type: "POST"
		});
	},
	
	onDelete: function (data){
		
	},
	
	
	onUpdateAjaxComplete: function (data,action) {
		var $tree = $(data), isXml = $.isXMLDoc(data);

		if (ajaxUtils.hasError(data)) {
			ajaxUtils.displayError(data);
			ajaxUtils.afterAjaxComplete(this.mainContainer);
			if (typeof this.onError === "function") {
				this.onError(data.viewBean);
			}
			return false;
		} else {
			ajaxUtils.displayLogsIfExsite(data);
		}
		
		ajaxUtils.afterAjaxComplete(this.mainContainer);
		if ($tree.find(this.XML_DETAIL_TAG).length > 0 || $.isPlainObject(data)) {
			
			if (isXml) {
				this.currentViewBean = $tree.find('viewBean').text();
				this.setParentViewBean($tree.find('parentViewBean').text());
				data = $tree;
			} else {
				this.currentViewBean = data.viewBeanSerialized;
				data = data.viewBean;
			}
			
			if (typeof this.onUpdate === "function") {
				this.onUpdate(data,action);
			}
			//this.currentViewBean=$tree.find('viewBean').text();
			
			this.stopEdition();
			this.mainContainer.triggerHandler(eventConstant.AJAX_UPDATE_COMPLETE);
			return true;
		} else {
			ajaxUtils.displayError(data);
			return false;
			/*errorObj.text = data;
			showErrors();
			*/
		}
	},
		
	getParentViewBean: function () {
		throw "getParentViewBean not implemented";
	},
	
	setParentViewBean: function (newViewBean) {
		throw "setParentViewBean  not implemented";
	},		
	 
	init: function (f_function) {
		this.$inputsButton = this.mainContainer.find('.btnAjax');
		this.$formElement = this.mainContainer.find('input,select');
		this.mainContainer.prepend("<div class='imgLoading' />");
		if (jQuery.isFunction(f_function)) {
			f_function.call(this);
		}
		ajaxUtils.afterInit(this.mainContainer);
	}
};