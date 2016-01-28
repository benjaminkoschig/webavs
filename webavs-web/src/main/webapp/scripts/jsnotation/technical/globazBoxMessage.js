globazNotation.boxmessage = {

	author: 'DMA',
	forTagHtml: 'div',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Permet de créer un conteur de type message",
	
	descriptionOptions: {
		position: {
			desc: "Si ce paramétre et definit cela va positioner la box en abolute par rapport à sont parent.",
			param: "Map content les paramétres de déclage Ex: {top:'20%',left:'30%'}",
			mandatory: false
		},
		
		type: {
			desc: "Définit le type de message box que l'on veut afficher",
			param: "WARN, ERROR",
			mandatory: true
		}
	},
	
	options: {
		position: null,
		type: ""
	},
	
	typeBox: {
		WARN: "WARN",
		ERROR: "ERROR"
	},
	
	vars: {
		templateInfos: '<div class="ui-widget globazBoxMessage {{class}}" id="{{id}}">' +
							'<div class="ui-state-highlight ui-corner-all contentMessage">' +
								'<p>' +
									'<span class="ui-icon ui-icon-info globazBoxMessageIcon"></span>' +
									'{{message}}' +
								'</p>' +
							'</div>' +
						'</div>',
							
		templateError: '<div class="ui-widget globazBoxMessage {{class}}" id={{id}}>' +
							'<div class="ui-state-error ui-corner-all contentMessage">' + 
								'<p>' +
									'<span class="globazBoxMessageIcon ui-icon ui-icon-alert"></span>' +
									'{{message}}' +
								'</p>' +
							'</div>' + 
						'</div>',
							
		$parent: null
	},

	bindEvent: {
		ajaxShowDetail: function () {
			this.postionByTheParent();
		}
	},

	init: function ($elementToPutObject) {
		this.vars.$parent = this.$elementToPutObject.parent();
		var $box = $(this.createHtml());
		this.$elementToPutObject.replaceWith($box);
		this.$elementToPutObject = $box;
		this.postionByTheParent();
	},
	
	postionByTheParent: function () {
		if (this.options.position) {
			var o_postion = this.computePostion();
			o_postion.position = "absolute";
			this.$elementToPutObject.css(o_postion);
		}
	},
	
	createOptionPostionPourcentage: function () {
		var o_position = {};
		for (var key  in this.options.position) {
			if (this.options.position[key].indexOf("%") > 0) {
				o_position[key] = parseFloat($.trim(this.options.position[key].split("%")[0]));
			}
		}
		return o_position;
	},
	
	computePostion: function () {
		var o_postionParent = this.vars.$parent.offset();
		var o_positionPourcentage = this.createOptionPostionPourcentage();
		var o_postion = {};
		
		if (o_positionPourcentage.top) {
			o_postion.top = o_postionParent.top + ((o_positionPourcentage.top / 100) * this.vars.$parent.outerHeight()) + "px";
		} else {
			o_postion.top = o_postionParent.top + this.options.position.top.split("px")[0] + "px";
		}
		
		if (o_positionPourcentage.left) {
			o_postion.left = o_postionParent.left + ((o_positionPourcentage.left / 100) * this.vars.$parent.outerWidth()) + "px";
		} else {
			o_postion.left = o_postionParent.left + this.options.position.left.split("px")[0] + "px";
		}

		return o_postion;
	},

	createHtml: function () {
		var o_data,	template;
		
		o_data = {
			message: this.$elementToPutObject.html(),
			id: (this.$elementToPutObject.attr("id")) ? this.$elementToPutObject.attr("id"): "",
			"class": (this.$elementToPutObject.attr("class")) ? this.$elementToPutObject.attr("class"): ""
		};
			
		if (this.options.type.toUpperCase() === this.typeBox.ERROR) {
			template = this.vars.templateError;
		} else if (this.options.type.toUpperCase() === this.typeBox.WARN) {
			template = this.vars.templateInfos;
		} else {
			throw "Le type de message: '" + this.options.type + "' ne fait pas partie des types existants";
		}
		
		return globazNotation.template.compile(o_data, template);
	}

};