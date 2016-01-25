globazNotation.globalVariables.isAlreadyDelegated = false;
globazNotation.globalVariables.globazMultiWidgetLastTarget = null;

globazNotation.multiwidgets = {

	nameNotation: 'multi',
	author: 'PBA',
	forTagHtml : 'div',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Cet objet permet de gérer plusieurs widgets (services ou manager) pour un seul champ.<br/><br/>Il recense les widgets présents dans un div spécifique et crée un bouton radio par widget.<br/><br/><b>Contraintes d'utilisations</b><br/><ul class=\"liWithStyle\"><li>Les widgets doivent être enveloppés dans une balise DIV qui contiendra la notation.</li><li>Les widgets doivent avoir un id débutant par 'widget'.</li><li>Si les widgets doivent être mandatory, ajouter l’option mandatory uniquement au multiwidget pour un meilleure rendu.</li><li>Les libellés des boutons radios seront construit avec toutes les majuscules (dans l’ordre) des libellées passés au widget, sous forme de tableau JS.<br/>Exemple: <code>'Administration,Tiers,Banque'</code> => libellés = <b>A / B / TP</b></li></ul><br/><b>Evénements retournés</b><br/><ul class=\"liWithStyle\"><li>eventConstant.<b>AJAX_LOAD_AXJAX_DATA</b> : Evénement retourné sur le choix d'un détail.</li><li>eventConstant.<b>AJAX_SELECT_SUGGESTION</b> : Evénement retournée après le choix d'une entrée dans la liste de suggestion AJAX.</li></ul>",

	descriptionOptions: {
		mandatory: {
			desc: "Défini si l'élément est obligatoire",
			param: "true, false(default)"
		},
		addSymboleMandatory: {
			desc: "Ajoute le symoble *.<br/>Si le paramétre mandatory est à <code>true</code> ce paramétre est ignoré",
			param: "true(par défault)|false"
		},
		languages: {
			desc: "Chaîne de caractères au fomat <code>[chaine,chaine]</code> permettant d'envoyer au widget les langues de l'infobulle lié aux boutons radios",
			param:	"chaine de caractère <strong>à écrire dans l'ordre des widgets</strong> et séparé par une virgule.<br/>Exemple: <code>'Administration,Tiers,Banque'</code><br/>Si aucune chaîne n'est déclarée, les radios seront numérotés en partant de 1"
		},
		widgetEtendu: {
			desc: "Ajout de fonctionnalités lorsqu'une valeur a été sélectionnée<ul class=\"liWithStyle\"><li>les widgets sont masqués</li><li>la valeur du widget ayant été utilisé est affichée dans un span</li><li>il y a un bouton pour réafficher les widgets</li></ul>Dans ce cas, aucun événement n'est retourné",
			param: "true|false(default)"
		},
		defaultWidget: {
			desc: "Définit le widget activé par défaut<br/>Si un widget possède une valeur au moment de la création de la notation, ce paramètre sera ignoré (et le widget ayant une valeur sera activé par défaut)",
			param: "ID du widget"
		},
		libelleClassName: {
			desc: "Défini une classe CSS qui sera appliquée au texte affichée lorsqu'une valeure aura été choisie.<br/>Permet de modifier l'affichage des valeurs",
			param: "Une classe CSS"
		}
	},

	options: {
		mandatory: false,
		addSymboleMandatory: true,
		languages: '',
		widgetEtendu: false,
		defaultWidget: '',
		libelleClassName: ''
	},

	vars: {
	},

	/**
	 * Ce paramètre est facultatif.<br/>
	 * Il permet des lancer des fonctions sur différent types d'évenements.<br/>
	 * Liste des événements :<br/>
	 *  <ul>
	 *   <li>boutons standard de l'application. Les événements se lancent sur le clique du bouton</li>
	 *   <ul>
	 *    <li>btnCancel</li>
	 *    <li>btnAdd</li>
	 *    <li>btnValidate</li>
	 *    <li>btnUpdate</li>
	 *    <li>btnDelete</li>
	 *   </ul>
	 *   <li>AJAX: tous ces fonctions se lancent à la fin de la fonction dans AJAX</li>
	 *   <ul>
	 *    <li>ajaxShowDetailRefresh</li>
	 *    <li>ajaxLoadData</li>
	 *    <li>ajaxShowDetail</li>
	 *    <li>ajaxStopEdition</li>
	 *    <li>ajaxValidateEditon</li>
	 *    <li>ajaxUpdateComplete</li>
	 *   </ul>
	 *  </ul>
	 */
	bindEvent: {
		ajaxDisableEnableInput: function () {
			this.enableDisableInput();
		},

		btnUpdate: function () {
			this.enableDisableInput();
		},

		btnAdd: function () {
			this.enableDisableInput();
		},

		btnCancel: function () {
			this.enableDisableInput(true);
		},

		ajaxStopEdition: function () {
			this.enableDisableInput(true);	
		}
	},

	init: function () {
		var n_uniqueID = this.utils.generateUniqueNumber();
		
		this.$elementToPutObject.addClass('globazMultiWidgets');
		this.$elementToPutObject.attr('uniqueID', n_uniqueID);

		// recherche des widgets, ils doivent avoir un ID commençant par "widget"
		var $widgets = this.$elementToPutObject.find('[data-g-autocomplete]');
		// ajout d'une classe unique aux widgets afin de les manipuler plus facilement
		$widgets.addClass('multiWidget_' + n_uniqueID).css({
			'z-index': '1',
			'padding': '0',
			'margin': '0'
		});

		// construction du span contenant les boutons radios permettant de passer d'un widget à l'autre
		var $radioSpan = this.buildRadioSpan($widgets, n_uniqueID);

		// les boutons radios doivent être à gauche des widgets et visible
		this.$elementToPutObject.prepend($radioSpan);

		// on cherche le premier radio de la liste et on le coche
		var $radio = $radioSpan.find(':radio');
		$radio.first().attr('checked', true);

		var n_defaultInputHeight = globazNotation.utils.getDefaultInputHeight();
		if (!$.support.boxModel) {
			this.addaptCssForIE7($widgets, $radioSpan, n_defaultInputHeight);
		} else {
			this.addaptCssForStandardNavigator($widgets, $radioSpan, $radio, n_defaultInputHeight);
		}

		// si widget étendu, on crée le bouton qui permettra d'afficher les widgets lorsqu'une valeur a été saisie
		if (this.options.widgetEtendu) {
			this.constructionWidgetEtendu(n_uniqueID);
		}

		if (this.options.mandatory) {
			this.addSymboleMandatory($widgets, $radioSpan, n_uniqueID);
		}

		this.initBehavior($widgets, $radioSpan, n_uniqueID);

		$widgets.hide();
		if (this.options.widgetEtendu) {
			var $widgetWithValue = null;
			$widgets.each(function () {
				if (this.value) {
					$widgetWithValue = $(this);
				}
			});
			
			if ($widgetWithValue) {
				$widgets.hide();
				$widgetWithValue.show();
				$radioSpan.removeProp('checked');
				$radioSpan.filter('#radio_' + $widgetWithValue.attr('id') + '_' + n_uniqueID).prop('checked', true);

				$widgetWithValue.trigger(eventConstant.AJAX_SELECT_SUGGESTION);
			} else {
				$widgets.hide();
				$radioSpan.hide();
			}
		} else {
			$widgets.first().show();
		}
		
		if ($widgets.first().prop('disabled')) {
			this.enableDisableInput(true);
		}
	},

	addSymboleMandatory: function ($widgets, $radioSpan, n_uniqueID) {
		globazNotation.utilsInput.addMandatory($widgets);
		globazNotation.utilsInput.removeSymboleMandatory($widgets);

		$widgets.first().after($('<span/>', {
			'id': 'mandatory_multiwidgets_' + n_uniqueID
		})
		.addClass('simboleMandatory')
		.append('*')
		.css('position', 'relative'));

		$radioSpan.addClass('errorColor');
	},

	constructionWidgetEtendu: function (n_uniqueID) {
		var $button = this.buildExtendWidgetButton(n_uniqueID);

		var $valueSpan = $('<span/>', {
			'id': 'span_value_multiwidgets_' + n_uniqueID,
			'style': 'font-weight:bold;',
			'class': this.options.libelleClassName
		});

		this.$elementToPutObject.prepend($valueSpan);
		this.$elementToPutObject.prepend($button);
		
		$button.button({
			label: '...'
		})
		.css({
			'margin-right': '5px'
		})
		.children('span').css({
			'padding': '1px 4px'
		});
	},

	addaptCssForIE7: function ($widgets, $radioSpan, n_defaultInputHeight) {
		// Pour le mode Quirks d'IE
		// on force la hauteur des widgets, et celle du conteneur des radios afin d'avoir l'impression que les deux ne forment qu'un élément
		$('html').bind(eventConstant.NOTATION_MANAGER_DONE, function () {
			$widgets.css({
				'height': n_defaultInputHeight
			});
			$radioSpan.css({
				'height': n_defaultInputHeight,
				'line-height': n_defaultInputHeight,
				'position': 'relative',
				'top': '1px',
				'left': '-1px'
			});
		});

		// décallage des widgets pour qu'ils collent la zone des radios
		$widgets.css('margin-left', '-4px');
	},

	addaptCssForStandardNavigator: function ($widgets, $radioSpan, $radio, n_defaultInputHeight) {
		// adaptation de la hauteur du conteneur des radio et des widgets
		$widgets.css('height', n_defaultInputHeight + 'px');
		$radioSpan.css({
			'height': n_defaultInputHeight + 'px',
			'line-height': n_defaultInputHeight + 'px'
		});
		$radio.css({
			'margin-top': '2px'
		});
	},

	enableDisableInput: function (b_forceState) {
		var b_isDisabled;
		if (typeof b_forceState !== 'undefined') {
			b_isDisabled = !!b_forceState;
		} else {
			b_isDisabled = this.utils.input.isDisabled(this.$elementToPutObject.children('.jadeAutocompleteAjax'));
		}
		
		var $radiosZone = this.$elementToPutObject.find('.radiosZone');
		var $widgets = this.$elementToPutObject.children('.jadeAutocompleteAjax');
		
		if (this.options.widgetEtendu) {
			var $bouton = this.$elementToPutObject.find('.globazMutliWidget_showMultiWidget');
			$bouton.button('option', 'disabled', b_isDisabled);
		} else {
			var s_idOverlay = 'overlay_multiwidets_' + this.$elementToPutObject.attr('uniqueid');
			if (b_isDisabled) {
				$radiosZone.overlay({
					'id': s_idOverlay,
					b_relatif: true
				});
			} else {
				$('#' + s_idOverlay).remove();
			}
		}

		if (this.options.mandatory) {
			if (b_isDisabled && $radiosZone.hasClass('errorColor')) {
				$radiosZone.removeClass('errorColor');
				$widgets.removeClass('errorColor');

				$radiosZone.addClass('errorColorDisable');
				$widgets.addClass('errorColorDisable');
			} else {
				$radiosZone.removeClass('errorColorDisable');
				$widgets.removeClass('errorColorDisable');

				$radiosZone.addClass('errorColor');
				$widgets.addClass('errorColor');
			}
		} else {
			if (b_isDisabled) {
				$radiosZone.removeClass('jadeAutoCompleteAjaxColor');
				$widgets.removeClass('jadeAutoCompleteAjaxColor');
				
				$radiosZone.addClass('jadeAutoCompleteAjaxColorDisabled');
				$widgets.addClass('jadeAutoCompleteAjaxColorDisabled');
			} else {
				$radiosZone.removeClass('jadeAutoCompleteAjaxColorDisabled');
				$widgets.removeClass('jadeAutoCompleteAjaxColorDisabled');
				
				$radiosZone.addClass('jadeAutoCompleteAjaxColor');
				$widgets.addClass('jadeAutoCompleteAjaxColor');
			}
		}
	},

	positionRadioSpan: function ($widgets, $radioSpan, $symboleMandatory) {
		var $widgetVisible = $widgets.filter(':visible');
		var n_position_top = $widgetVisible.position().top;
		var n_position_left = $widgetVisible.position().left;
		var decalage = 1;
		$radioSpan.css({
			'left': n_position_left + $widgetVisible.outerWidth() - decalage + 'px',
			'top': n_position_top
		});
		if ($symboleMandatory) {
			$symboleMandatory.css('left', $radioSpan.outerWidth() + 'px');
		}
	},

	buildRadioSpan: function ($widgets, n_uniqueID) {
		var $radioSpan = $('<span/>', {
			'id': 'radiosZoneMultiWidget_' + n_uniqueID,
			'class': 'radiosZone radiosZoneGauche'
		});

		if (this.options.mandatory) {
			$radioSpan.addClass('errorColor');
		} else {
			$radioSpan.addClass('jadeAutoCompleteAjaxColor');
		}

		$widgets.css('padding-left', '-5px');

		var t_labels = this.options.languages.split(',');
		var n_indexLabel = 0;

		$widgets.each(function () {
			var $this = $(this);

			$this.addClass('globazMultiWidget_innerWidget_' + n_uniqueID);
			$this.attr('widgetName', $this.attr('id'));

			var s_idRadio = 'radio_' + $this.attr('id') + '_' + n_uniqueID;
			var $radio = $('<input/>', {
				'id': s_idRadio,
				'type': 'radio',
				'tabIndex': '-1',
				'forWidget': $this.attr('id')
			});
			$radio.addClass('multiWidget_' + n_uniqueID);
			$radioSpan.append($radio);

			var $label = $('<label/>', {
				'for': s_idRadio,
				'class': 'multiWidget_' + n_uniqueID
			});
			if (t_labels.length > n_indexLabel) {
				$label.text(t_labels[n_indexLabel].replace(/[^A-Z]*/g, ''));
				$radio.attr('title', t_labels[n_indexLabel]);
			} else {
				$label.text(n_indexLabel);
			}
			$radioSpan.append($label);
			n_indexLabel++;
		});

		return $radioSpan;
	},

	buildExtendWidgetButton: function (n_uniqueID) {
		var $button = $('<button/>', {
			'id': 'showMultiwidget_' + n_uniqueID,
			'class': 'globazMutliWidget_showMultiWidget',
			'type':'button'
		});
		return $button;
	},

	processRadioClick: function ($widgets, $radioSpan, $radio, n_uniqueId) {
		// on vide le cache du widget qui était actif (pour éviter des recherches erronées dans
		// le widget qui sera affiché)
		var data = $widgets.filter(':visible').data('globazWidget');
		if (data) {
			data.clearCache();
		}
		$widgets.val('').focusout().hide();

		// on "dé-check" tous les radio de ce multiwidget
		$radioSpan.find(':radio').attr('checked', false);
		// on active que celui sur lequel il y a eu un clique
		$radio.attr('checked', true);

		// filtrage des widget ayant ce numéro par l'ID unique du multiwidget (utile dans le cas d'un clone)
		var $widgetToShow = $('.globazMultiWidget_innerWidget_' + n_uniqueId + '[widgetName="' + $radio.attr('forWidget') + '"]');
		$widgetToShow.show().focus();

		// dans le cas d'un multiwidget mandatory, il faut replacer le symbole '*'
		var $symboleMandatory = null;
		if (this.options.mandatory) {
			$symboleMandatory = $('#mandatory_multiwidgets_' + n_uniqueId);
			$widgetToShow.after($symboleMandatory).addClass('errorColor');
			$radioSpan.addClass('errorColor');
		}
	},

	initBehavior: function ($widgets, $radioSpan, n_uniqueID) {
		var that = this;
		var $document = $(document);
		var $radio = $radioSpan.find(':radio');

		// Gestion du click sur les radios
		$radio.click(function () {
			that.processRadioClick($widgets, $radioSpan, $(this), n_uniqueID);
		});
		// Gestion du click sur les <label> des radios
		$radioSpan.find('label').click(function () {
			that.processRadioClick($widgets, $radioSpan, $('#' + $(this).attr('for')), n_uniqueID);
		});

		// Gestion manuelle de la couleur si mandatory
		if (this.options.mandatory) {
			$widgets.bind(eventConstant.AJAX_SELECT_SUGGESTION, function () {
				$radioSpan.removeClass('errorColor');
			});
		}

		// Si widget étendu, gestion de la sélection d'une valeur
		if (this.options.widgetEtendu) {
			var $showMultiWidgetButton = $('#showMultiwidget_' + n_uniqueID);
			var $valueSpan = $('#span_value_multiwidgets_' + n_uniqueID);

			$showMultiWidgetButton.click(function () {
				that.showWidgetEtendu($widgets, $radioSpan, $valueSpan, $showMultiWidgetButton);
				// on stoppe la propagation de l'event
				return false;
			});

			$widgets.bind(eventConstant.AJAX_SELECT_SUGGESTION, function () {
				var $this = $(this);

				that.hideWidgetEtendu($widgets, $this, $radioSpan, $valueSpan, $showMultiWidgetButton);

				if (this.options.libelleClassName) {
					$valueSpan.addClass(this.options.libelleClassName);
				}
			});

			$widgets.focusin(function () {
				if (globazNotation.globalVariables.globazMultiWidgetLastTarget === null) {
					globazNotation.globalVariables.globazMultiWidgetLastTarget = that.$elementToPutObject;
				}
			});

			if (!globazNotation.globalVariables.isAlreadyDelegated) {
				globazNotation.globalVariables.isAlreadyDelegated = true;

				$document.click(function (event) {
					if (globazNotation.globalVariables.globazMultiWidgetLastTarget !== null && (globazNotation.globalVariables.globazMultiWidgetLastTarget.find(event.target).length === 0 || globazNotation.globalVariables.globazMultiWidgetLastTarget.get(0) === event.target)) {

						var n_lastTargetUniqueID = globazNotation.globalVariables.globazMultiWidgetLastTarget.attr('uniqueID');

						var $widgetsToHide = globazNotation.globalVariables.globazMultiWidgetLastTarget.children('.globazMultiWidget_innerWidget_' + n_lastTargetUniqueID);
						var $radioSpanFromLastTarget = $('#radiosZoneMultiWidget_' + n_lastTargetUniqueID);
						var $valueSpanFromLastTarget = $('#span_value_multiwidgets_' + n_lastTargetUniqueID);
						var $showMultiWidgetButtonFromLastTarget = $('#showMultiwidget_' + n_lastTargetUniqueID);

						that.hideWidgetEtendu($widgetsToHide, globazNotation.globalVariables.globazMultiWidgetLastTarget, $radioSpanFromLastTarget, $valueSpanFromLastTarget, $showMultiWidgetButtonFromLastTarget);

						globazNotation.globalVariables.globazMultiWidgetLastTarget = null;
					}
				});
			}
		}

		$document.keydown(function (event) {
			if ($(event.target).hasClass('multiWidget_' + n_uniqueID)) {
				var n_keyCode = event.which ? event.which : event.keyCode;
				switch (n_keyCode) {
//					case 9: // TAB
//					case 13: //ENTER
//						that.hideRadiodiv($widgets, $radioSpan, n_uniqueID);
//						return true;
				case 37: // left key
				case 39: // right key
					var $radio = $radioSpan.find(':radio:checked');
					$radio.attr('checked', false);

					var indexRadio = -1;
					var $radios = $radioSpan.find(':radio');

					$radios.each(function (index) {
						if (this === $radio.get(0)) {
							indexRadio = index;
						}
					});

					if (indexRadio === -1) {
						$radioSpan.find(':radio:first').attr('checked', true).click();
					}

					var s_selector = null;
					var n_indexModifier = null;
					var n_maxIndex = null;

					if (n_keyCode === 37) {
						s_selector = ':last';
						n_indexModifier = -1;
						n_maxIndex = 0;
					} else {
						s_selector = ':first';
						n_indexModifier = 1;
						n_maxIndex = $radios.length - 1;
					}

					if (indexRadio === n_maxIndex) {
						$radioSpan.find(':radio' + s_selector).attr('checked', true).click();
					} else {
						$radios.eq(indexRadio + n_indexModifier).attr('checked', true).click();
					}
					return false;
				}
				return true;
			}
		});
	},

	showWidgetEtendu: function ($widgets, $radioSpan, $valueSpan, $showMultiWidgetButton) {
		$valueSpan.hide();
		$showMultiWidgetButton.hide();

		var $lastActiveWidget = $widgets.filter('.lastActiveWidget');
		if ($lastActiveWidget.length === 0) {
			$widgets.first().show().focus();
		} else {
			$lastActiveWidget.show().focus().removeClass('lastActiveWidget');
		}
		$radioSpan.show();
	},

	hideWidgetEtendu: function ($widgets, $lastWidget, $radioSpan, $valueSpan, $showMultiWidgetButton) {
		$lastWidget.addClass('lastActiveWidget');
		$widgets.hide();

		if ($lastWidget.val() !== '') {
			$valueSpan.text($lastWidget.val());
		}
		$valueSpan.show();
		$showMultiWidgetButton.show();

		$radioSpan.hide();
	}
};
