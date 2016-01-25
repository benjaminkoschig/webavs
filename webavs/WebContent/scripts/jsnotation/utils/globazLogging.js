globazNotation.utils.logging = {

	author: 'PBA',
	forTagHtml: 'Utils',
	description: '',
	type: globazNotation.typesNotation.UTILITIES,

	LEVEL_INFO: 1,
	LEVEL_WARNING: 2,
	LEVEL_ERROR: 3,

	object: {
		n_level: 0,
		s_message: '',
		b_hasError: false,
		$element: null
	},

	o_cache: [],

	init: function () {

	},

	clearCache: function () {
		this.o_cache = [];
	},

	info: function (s_message, $element) {
		var o_validateObject = this._createStandardObject(s_message, $element);
		o_validateObject.n_level = this.LEVEL_INFO;
		this.triggerEvent(o_validateObject, $element);
		return o_validateObject;
	},

	warning: function (s_message, $element) {
		var o_validateObject = this._createStandardObject(s_message, $element);
		o_validateObject.n_level = this.LEVEL_WARNING;
		this.triggerEvent(o_validateObject, $element);
		return o_validateObject;
	},

	error: function (s_message, $element) {
		var o_validateObject = this._createStandardObject(s_message, $element);
		o_validateObject.n_level = this.LEVEL_ERROR;
		if (o_validateObject.s_message.length != 0) {
			o_validateObject.b_hasError = true;
		}
		this.triggerEvent(o_validateObject, $element);
		return o_validateObject;
	},

	triggerEvent: function (o_validateObject, $element) {
		if($element){
			$element.trigger(eventConstant.NEW_LOG, o_validateObject);
		}
	},

	_createStandardObject: function (s_message, $element) {
		var o_validateObject = Object.create(this.object);
		if (typeof s_message == 'string' && s_message.length != 0) {
			o_validateObject.s_message = s_message;
		}
		if (globazNotation.utils.isJQueryObject($element)) {
			o_validateObject.$element = $element;
		}
		return o_validateObject;
	},

	log: function (o_logObject, n_wantedLogLevel) {
		if ((o_logObject.n_level >= n_wantedLogLevel) && (typeof console !== 'undefined')) {
			switch (o_logObject.n_level) {
			case globazNotation.utils.logging.LEVEL_INFO:
				console.info(o_logObject.s_message, o_logObject.$element);
				break;
			case globazNotation.utils.logging.LEVEL_WARNING:
				console.warn(o_logObject.s_message, o_logObject.$element);
				break;
			case globazNotation.utils.logging.LEVEL_ERROR:
				console.error(o_logObject.s_message, o_logObject.$element);
				break;
			}
		}
		this.o_cache.push(o_logObject);
	}
};
