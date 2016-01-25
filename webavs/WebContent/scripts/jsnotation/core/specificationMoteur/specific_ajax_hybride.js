/**
 * DMA
 */

NotationManager.prototype.specific = {

	getContainer: function ($elementToPutObject) {
		return $elementToPutObject.closest('.mainContainerAjax');
	},

	callObjectToUseOnJS: function (s_notation, o_options, $elementToPutObject) {
		var $container = $elementToPutObject.closest('.mainContainerAjax');
		var o_objNoation = this.callObjectByJs(s_notation, o_options, $elementToPutObject, $container);
		return o_objNoation;
	},

	callObjToUse: function (o_attribut, $elementToPutObject) {
		var $container = $elementToPutObject.closest('.mainContainerAjax');
		var o_objNoation = this.callOject(o_attribut, $elementToPutObject, $container);
		return o_objNoation;
	}, 

	bindEventButton: function ($elementToPutObject, o_obj, $container) {
		var s_event = 'click';
		/* bind pour les bouttons ajax */
		this.bindEventAndSearchElementToBind($elementToPutObject, o_obj, '.btnAjaxCancel', 'btnCancel', s_event, $container);
		this.bindEventAndSearchElementToBind($elementToPutObject, o_obj, '.btnAjaxAdd', 'btnAdd', s_event, $container);
		this.bindEventAndSearchElementToBind($elementToPutObject, o_obj, '.btnAjaxValidate', 'btnValidate', s_event, $container);
		this.bindEventAndSearchElementToBind($elementToPutObject, o_obj, '.btnAjaxUpdate', 'btnUpdate', s_event, $container);
		this.bindEventAndSearchElementToBind($elementToPutObject, o_obj, '.btnAjaxDelete', 'btnDelete', s_event, $container);
	},

	/**
	 * Lie les évenments de type AJAX à la notations passé en paramétre
	 * 
	 * @param {Object}
	 *            $elementToPutObject: element ou se trouve la noation
	 * @param {Object}
	 *            o_obj: objet notation
	 */
	bindSpecialEvent: function ($elementToPutObject, o_obj, $container) {
		if (this.isAjaxPage()) {
			var $elemtToBind = $container; // $elementToPutObject.closest('.mainContainerAjax');
			this.bindEventWithObject(o_obj, $elemtToBind, 'ajaxDisableEnableInput', eventConstant.AJAX_DISABLE_ENABLED_INPUT);
			this.bindEventWithObject(o_obj, $elemtToBind, 'ajaxShowDetailRefresh', eventConstant.AJAX_DETAIIL_REFRESH);
			this.bindEventWithObject(o_obj, $elemtToBind, 'ajaxLoadData', eventConstant.AJAX_LOAD_DATA);
			this.bindEventWithObject(o_obj, $elemtToBind, 'ajaxShowDetail', eventConstant.AJAX_STOP_SHOW_DETAIL);
			this.bindEventWithObject(o_obj, $elemtToBind, 'ajaxStopEdition', eventConstant.AJAX_STOP_EDITION);
			this.bindEventWithObject(o_obj, $elemtToBind, 'ajaxValidateEditon', eventConstant.AJAX_VALIDATE_EDITION);
			this.bindEventWithObject(o_obj, $elemtToBind, 'ajaxUpdateComplete', eventConstant.AJAX_UPDATE_COMPLETE);
		}
		// this.bindEventWithObject(o_obj, $elemtToBind, 'ajaxShowDetail', eventConstant.AJAX_SELECT_SUGGESTION);
	}
};

/*
 * NotationManager.prototype = { // prototype: NotationManager.prototype, startByEvent: function () { alert(2) var that = this; var start = (new Date()).getTime();
 * $('html').bind(eventConstant.AJAX_INIT_DONE, function () { that.o_infosMoteur.t_timeJsAutre = (new Date()).getTime() - start; that.startNotation(); that.b_started = true; }); } }
 * 
 * 
 * NotationManager.prototype.startByEvent = function () { var that = this; var start = (new Date()).getTime(); $('html').bind(eventConstant.AJAX_INIT_DONE, function () {
 * that.o_infosMoteur.t_timeJsAutre = (new Date()).getTime() - start; that.startNotation(); that.b_started = true; }); }
 */
