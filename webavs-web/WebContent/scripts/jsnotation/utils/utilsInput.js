/**
 * @author DMA
 */
globazNotation.utilsInput = {

	author: 'DMA',
	forTagHtml: 'Utils',
	description: "Regroupement de fonction utilitaire pour les inputs",
	type: globazNotation.typesNotation.UTILITIES,

	addEvent: true,
	mandatoryToCheck: null,

	descriptionOptions: {
		mandatory: {
			desc: "Test si élément est obligatoir",
			param: "true|false(default)"
		},
		addSymboleMandatory: {
			desc: "Ajoute le symoble *. Si le paramétre mandatory est à true ce paramétre n'est pas utilisé",
			param: "true|false(default)"
		}
	},

	objHasMandatory: function ($obj) {
		return $obj.hasClass('mandatory');
	},

	addMandatory: function ($obj) {
		if (!this.objHasMandatory($obj)) {
			$obj.addClass('mandatory');
			this.addSymboleMandatory($obj);
			this.checkMandatory($obj);
		}
	},
	
	addMandatoryWithOutSymbole: function ($obj) {
		if (!this.objHasMandatory($obj)) {
			$obj.addClass('mandatory');
			this.checkMandatory($obj);
		}
	},

	isDisabled: function ($obj) {
		return $obj.is(':disabled');
	},

	removeMandatory: function ($obj) {
		this.removeSymboleMandatory($obj);
		this.removeCheckMandatory($obj);
		$obj.removeClass('mandatory');
	},

	removeSymboleMandatory: function ($obj) {
		$obj.nextAll().remove('.simboleMandatory');
	},

	addSymboleMandatory: function ($obj) {
		if (!$obj.next().is('.simboleMandatory')) {
			$obj.after('<span class="simboleMandatory">*</span>');
		}
	},

	addEventIsRate: function ($obj) {
		$obj.keypress(function (event) {
			event.keyCode = event.which ? event.which : event.keyCode;
			return filterCharForFloat(event);
		});
	},

	addEventIsAmount: function ($obj) {
		$obj.keypress(function (event) {
			event.keyCode = event.which ? event.which : event.keyCode;
			return filterCharForFloat(event);
		});
	},

	addEventIsInteger: function ($obj) {
		$obj.keypress(function (event) {
			event.keyCode = event.which ? event.which : event.keyCode;
			return filterCharForInteger(event);
		});
	},

	addEventSizeMax: function ($obj, sizeMax) {
		var that = this;
		$obj.keypress(function (event) {
			return !that.isSizeMax($obj[0], sizeMax);
		});
	},

	changeColorGolbal: function (b_ok, $obj, b_disable) {
		var s_disable = 'Disable';
		$obj.removeClass('isValueKo');
		$obj.removeClass('errorColor');
		if (!this.isAutocomplete($obj)) {
			if (b_ok) {
				$obj.removeClass('isValueKo' + s_disable);
			} else {
				$obj.addClass('isValueKo' + s_disable);
			}
		} else {
			if (b_ok) {
				$obj.removeClass('errorColor' + s_disable).addClass('jadeAutocompleteAjax' + s_disable);
			} else {
				$obj.addClass('errorColor' + s_disable);
			}
		}
	},

	changeColorDisabled: function ($obj, b_flag) {
		this.changeColor(!($obj.hasClass("isValueKo") || $obj.hasClass("isValueKoDisable")), $obj);
	},

	testColor: function (options, $obj) {
		if (options.mandatory) {
			this.changeColor($.trim($obj.val()) !== '', $obj);
		}
	},

	changeColor: function (ok, $obj) {
		if (this.isDisabled($obj)) {
			$obj.removeClass('isValueKo');
			$obj.removeClass('errorColor');
			if (!this.isAutocomplete($obj)) {
				if (ok) {
					$obj.removeClass('isValueKoDisable');
				} else {
					$obj.addClass('isValueKoDisable');
				}
			} else {
				if (ok) {
					$obj.removeClass('errorColorDisable').addClass('jadeAutocompleteAjaxDisable');
				} else {
					$obj.addClass('errorColorDisable');
				}
			}
		} else {
			$obj.removeClass('isValueKoDisable');
			$obj.removeClass('errorColorDisable');
			$obj.removeClass('jadeAutocompleteAjaxDisable');
			if (!this.isAutocomplete($obj)) {
				if (ok) {
					$obj.removeClass('isValueKo');
				} else {
					$obj.addClass('isValueKo');
				}
			} else if (this.objHasMandatory($obj)) {
				if (ok) {
					$obj.removeClass('errorColor');
				} else {
					$obj.addClass('errorColor');
				}
			}
		}
	},

	addCheckUnsigned: function ($elementToPutObject) {
		$elementToPutObject.keypress(function (event) {
			return !/-/.test(String.fromCharCode(event.which));
		});
	},

	addAllPropertyFromUtil: function (options, $elementToPutObject) {
	
		if (options.mandatory) {
			this.addMandatoryWithOutSymbole($elementToPutObject);
		}
		if (options.addSymboleMandatory && options.mandatory) {
			this.addSymboleMandatory($elementToPutObject);
		}
		if (options.hasOwnProperty('sizeMax')) {
			if (options.sizeMax !== null) {
				this.addEventSizeMax($elementToPutObject, options.sizeMax);
			}
		}
		if (options.hasOwnProperty('autoFit')) {
			if (options.autoFit) {
				$elementToPutObject.attr('style', 'width:' + options.sizeMax * 9 + 'px');
			}
		}
		if ($elementToPutObject[0].type !== 'select-multiple' && $elementToPutObject[0].type !== 'select-one') {
			$elementToPutObject.attr('autocomplete', 'off');
		}
		if (options.hasOwnProperty('unsigned')) {
			if (options.unsigned === true) {
				this.addCheckUnsigned($elementToPutObject);
			}
		}
	},

	removeCheckMandatory: function ($obj) {
		$obj.unbind('.mandatory');
		this.changeColor(true, $obj);
	},

	checkMandatory: function ($objToChek) {
		var that = this;
		that.changeColor($.trim($objToChek.val()) !== '', $objToChek);
		if (this.addEvent) {
			$objToChek.bind('change.mandatory', function () {
				var ok = false;
				if (!that.isAutocomplete($objToChek)) {
					ok = $.trim($objToChek.val()) !== '';
					that.changeColor(ok, $objToChek);
				}
			});
			$objToChek.bind('keyup.mandatory', function (event) {
				var ok = false;
				if (!that.isAutocomplete($objToChek)) {
					ok = $.trim($objToChek.val()) !== '';
					that.changeColor(ok, $objToChek);
				}
			});
			$objToChek.bind(eventConstant.AJAX_SELECT_SUGGESTION, function (event) {
				that.changeColor(true, $objToChek);
			});
		}
	},

	getSelected: function () {
		var txt = '';
		if (window.getSelection) {
			txt = window.getSelection();
		} else if (document.getSelection) {
			txt = document.getSelection();
		} else if (document.selection) {
			txt = document.selection.createRange().text;
		}
		return txt;
	},

	isAutocomplete: function ($obj) {
		return $obj.hasClass('jadeAutocompleteAjax');
	},

	isTextSelctionned: function (element) {
		if (this.getSelected().length > 0) {
			return this.getSelected() === element.value && element.value !== "";
		}
		return false;
	},

	isSizeMax: function (element, size) {
		if ((!this.isTextSelctionned(element)) && element.value.length >= size) {
			return true;
		} else {
			return false;
		}
	},

	/**
	 * Lie l'événment change dans un select lorsque que l'on utilise les touches (flèche) du clavier
	 */
	applyChangeOnSelect: function ($container) {
		var $select = {};
		if ($container !== undefined && $container.length) {
			$select = $container.find('select');
		} else {
			$select = $('select');
		}
		$select.keyup(function (e) {
			if (/^(37|38|39|40)$/.test(e.which)) {
				$(this).change();
			}
		});
	},

	validate: function (options, $elementToPutObject) {
		var s_message = '';
		if (typeof options.mandatory !== 'undefined' && options.mandatory) {
			if ($.trim($elementToPutObject.val()) === '' && $elementToPutObject.is(':visible')) {
				s_message = jQuery.i18n.prop('notation.error.mandatory');
				globazNotation.utils.logging.error(s_message, $elementToPutObject);
			}
		}
		return s_message;
	},

	disableInputs: function ($inputs, b_wantTriggerChange) {
		$inputs.prop('disabled', 'true');
		if (typeof b_wantTriggerChange === 'undefined' || b_wantTriggerChange) {
			$inputs.change();
		}
		$('html').triggerHandler(eventConstant.AJAX_DISABLE_ENABLED_INPUT);
	},

	/**
	 * émule les comportements d'un bouton jQuery (click et hover)
	 * @param $button
	 */
	emulateJqueryButton: function ($button) {
		$button.hover(function () {
			$button.addClass('ui-state-hover');
		}, function () {
			$button.removeClass('ui-state-hover');
		});
		$button.mousedown(function () {
			$button.addClass('ui-state-active');
		});
		$button.mouseup(function () {
			$button.removeClass('ui-state-active');
		});
	}
};
