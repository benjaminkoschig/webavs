var globazNotation = {};
globazNotation.globalVariables = {};
globazNotation.typesNotation = {
	UTILITIES: 0,
	TECHNICAL_NOTATION: 1,
	BUSINESS_NOTATION: 2
};

var globazGlobal = globazGlobal || {};

var eventConstant = {
	// moteur Notation
	NOTATION_MANAGER_DONE: 'addNotationIsDone.notationManager',
	NOTATION_FRAGMENT_DONE: 'addFragementIsDone.notationManager',
	NOTATION_MANAGER_START:'startNotationIsDone.notationManager',

	EXECUTE_MOTEUR_ON_HIDDEN_ELEMENTS: 'executeOnHiddenElement.notationManager',

	// logging
	NEW_LOG: 'new.log.event',

	// Console
	UTILS_CONSOLE_CLOSE: 'dialogclose',

	// objets Notation
	MULTIWISGETS_SELECT_SUGGESTION: 'onSelectSuggestion.multiWidgets',
	DOWNLOAD_SUCESS: 'sucess.download',
	WIDGET_FORCE_LOAD: 'forceLoad.ajax',
	UPLOAD_RETURN: 'return.upload', 

	// autocomplétion
	AJAX_SELECT_SUGGESTION : 'onSelectSuggestion.ajax',
	AJAX_SELECT_SUGGESTION_RETURN_VALUE:"selectSuggestionReturnValue.ajax",

	// cloning
	GLOBAZ_CLONE_DONE: 'done.globazClone',

	// validation
	FORM_VALIDATION: 'formValidation.notationManager',

	// AbstractScalableAJAXTableZone
	AJAX_LOAD_DATA: 'loadData.ajax',
	AJAX_DETAIIL_REFRESH: 'detailRefresh.ajax',
	AJAX_STOP_EDITION: 'stopEdition.ajax',
	AJAX_STOP_SHOW_DETAIL: 'showDetail.ajax',
	AJAX_INIT_DONE: 'initDone.ajax',
	AJAX_VALIDATE_EDITION: 'validateEdition.ajax',
	AJAX_UPDATE_COMPLETE: 'updateComplete.ajax',
	AJAX_DETAIL_DISPLAY: 'detailDisplay.ajax',
	AJAX_DISABLE_ENABLED_INPUT: 'disabledEnalbledInputForm.ajax',
	AJAX_CHANGE: 'ajaxChange.ajax', // ajaxChange.ajax'
	AJAX_FIND_COMPLETE: "findComplete.ajax",
	AJAX_HIGHLIGHTED: 'ajaxHighlight.ajax',
	// Action lié au ancien js du framework
	// utile par exemple pour une gestion personnalisée du focus sur une page de
	// l'ancien framework
	JADE_FW_ACTION_DONE: 'actionDone.jadeFW',
		
	//retourne true si l'événement "change" a été levé depuis AJAX
	isChangeFromAjax: function (data){
		return data && data.from == eventConstant.AJAX_CHANGE;
	} 
};
