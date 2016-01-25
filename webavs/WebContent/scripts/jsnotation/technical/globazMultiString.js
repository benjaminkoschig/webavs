globazNotation.multistring = {
	/** ****************************DOCUMENTATION************************************** */
	author: "PBA, ECO",

	forTagHtml: "div",

	description: 
	"<p>" 
		+ "Permet de gérer un champ de saisie à plusieurs valeurs (cas typique : les annexes d'un document d'impression)<br/>" 
		+ "Les valeurs entrées par l'utilisateur seront stockées dans des champs input du même nom, défini par le paramètre <code>tagName</code>, ceci afin que le framework rassemble les donnés dans une <a href=\"http://download.oracle.com/javase/6/docs/api/java/util/Collections.html\"><code>Collection</code></a> lors de l'envoi du formulaire<br/>" 
		+ "Tous les champs du widget seront retournés" 
	+ "</p>" 
	+ "<p>" 
		+ "Le composant a deux modes de fonctionnement: mode texte simple et mode autocompletion.<br/> " 
		+ "En mode texte simple, Les valeurs déjà entrées sont éditable par sélection (on valide la modification avec Enter, ou par perte du focus), sauf s'ils sont protégés en écriture.<br/>" 
		+ "Il est possible d'ajouter une entrée avec le bouton (+) ou en validant la saisie avec Enter<br/>" 
		+ "Il est possible de supprimer une entrée (sélectionnée) avec le bouton (-) ou avec les touches Delete ou Backspace<br/>" 
		+ "La touche Escape permet de tout déselectionner et/ou de remettre le champ de saisie à zéro" 
	+ "</p>" 
	+ "<p>" 
		+ "En mode autocomplétion, l'utilisateur peut faire une recherche dans le widget d'autocomplétion et ajouter le résultat de la recherche, valeur et libellé, dans la liste. Les valeurs déjà entrées ne sont pas modifiables mais elles peuvent être déplacées et supprimées, sauf si elles sont protégés en écriture."
	+ "</p>"
	+ "<p>" 
		+ "TODO: Gérer la désactivation du mutlistring (disabled) pour les formulaires ne pouvant pas être modifiés" 
	+ "</p>",

	descriptionOptions: {
		tagName: {
			desc: "Nom donné au champ select contenant les données entrées. La liste des valeurs s'obtiendra en parcourant la liste des options pour récupérer leur valeur.",
			param: "un nom pour le champ select"
		},
		defaultValues: {
			desc: "Mappage de valeurs par défaut de la liste, dans le format {value:string,libelle:string,readonly:boolean}.",
			param: "Tableau de maps de valeurs."
		},
		languages: {
			desc: "Languages à afficher lors du mode autocomplétion",
			param: ""
		},
		mode: {
			desc: "Mode de fonctionnement du composant. La zone de saisie du composant varie selon le mode, soit en simple zone de texte, soit en autocompletion.",
			param: "Mode de fonctionnement. Valeurs possibles: text, autocompletion. Par défaut text."
		}
	},

	/** ****************************UTILISATION**************************************** */

	vars: {
		$select: null,
		$addBtn: null,
		$subBtn: null,
		$moveUpBtn: null,
		$moveDownBtn: null,		
		$text: null,
		$selectedOption: null,
		lastId: 0,
		textValue: null,
		modes: {
			TEXT: 'text',
			AUTOCOMPLETE: 'autocompletion'
		},
		n_uniqueID: 0
	},

	options: {
		tagName: '',
		defaultValues: [],
		languages: '',
		mode: 'text'
	},

	bindEvent: {
	},

	init: function () {

		this.vars.n_uniqueID = this.utils.generateUniqueNumber();

		this.generateHTML();

		var that = this;
		var $children;

		this.vars.$addBtn.on('click', function () {
			that.addValue();
		});

		this.vars.$subBtn.on('click', function () {
			that.deleteValue(that.getSelected());
		});

		this.vars.$moveUpBtn.on('click', function(){
			var $selected = that.getSelected();
			$selected.insertBefore($selected.prev());
		});

		this.vars.$moveDownBtn.on('click', function(){
			var $selected = that.getSelected();
			$selected.insertAfter($selected.next());
		});

		// si la sélection change, mise à jour du champ de texte pour la modification
		this.vars.$select.on('change', function () {
			$children = that.getSelected();
			if ($children) {
				that.vars.$subBtn.button('option','disabled', false);

				if (that.options.mode === that.vars.modes.TEXT) {
					that.vars.$text.val($children.text());
					that.vars.$selectedOption = $children;
					that.vars.$addBtn.button('option', 'disabled', true);
				}
			} else {
				that.vars.$subBtn.button('option', 'disabled', true);
			}
		});

		if (this.options.mode === this.vars.modes.TEXT) {
		
			this.vars.$text.on('keydown', function (event) {
				// si touche enter ou tab
				if (event.keyCode === jQuery.ui.keyCode.ESCAPE) {
					that.resetTextZone();
					event.stopPropagation();
					event.preventDefault();
				} else if (event.keyCode === jQuery.ui.keyCode.ENTER){
					that.addValue();
					event.stopPropagation();
					return false;
				}
			});

			this.vars.$text.on('focusout', function () {
				// lorsqu'on quitte le champ texte, appliquer les modifications
				if (that.vars.$text.val() !== '') {
					$children = that.vars.$selectedOption;
					if ($children) {
						$children.text(that.vars.$text.val());
					}
				}
			});
		} else if (this.options.mode === this.vars.modes.AUTOCOMPLETE) {
			
			this.$elementToPutObject.bind(eventConstant.AJAX_SELECT_SUGGESTION_RETURN_VALUE, function (event,data) {
				that.getActive$Text().val(data.text);
				that.vars.textValue = data.id;
			});
		}

		this.vars.$select.on('keydown',function (event) {
			if (event.keyCode === jQuery.ui.keyCode.ESCAPE) {
				that.resetTextZone();
				that.vars.$text.focus();
			} else if (event.keyCode === jQuery.ui.keyCode.BACKSPACE || event.keyCode === jQuery.ui.keyCode.DELETE) {
				if (that.vars.$select) {
					that.deleteValue(that.getSelected());
					that.vars.$text.focus();
					event.stopPropagation();
					return false;
				}
			}
		});

		this.resetTextZone();
	},

	getActive$Text: function () {
		if (this.options.mode === this.vars.modes.TEXT) {
			return this.vars.$text;
		} else if (this.options.mode === this.vars.modes.AUTOCOMPLETE) {
			return this.vars.$text.filter(':visible');
		}
	},

	getSelected: function () {
		return this.vars.$select.children('option:selected');
	},

	generateHTML: function () {
		var widgetObject = null;

		if (this.options.mode === this.vars.modes.AUTOCOMPLETE) {
			widgetObject = $('<div />').append(this.$elementToPutObject.find(':input').detach());
		}

		var $table = $('<table />').appendTo(this.$elementToPutObject);
		var $row = $('<tr />').appendTo($table);

		// selectbox
		this.vars.$select = $('<select/>', {
			'size': 5
		}).appendTo($('<td width="100%" />').appendTo($row));

		if (this.options.defaultValues && this.options.defaultValues.length > 0) {
			var values = this.options.defaultValues;
			for (var idx in values) {
				var valueObject = values[idx];
				var value = valueObject.value;
				var isReadOnly = (valueObject.readonly === true);

				var textOption = value;
				if (valueObject.libelle) {
					textOption = valueObject.libelle;
				}
				var valueOption = value;

				$('<option/>', {
					'value': valueOption,
					'text': textOption,
					'disabled': isReadOnly
				}).appendTo(this.vars.$select);
			}
		}

		// boutons
		var $buttonDiv = $('<div/>', {
			'class': 'globazMultistringButtonsDiv'
		}).appendTo($('<td />', {
			'class': 'globazMultistringButtonsDiv'
		}).appendTo($row));

		var createBtn = function(icon) {
			return $('<span />', {
				'class': 'globazMultistringButton'
			}).appendTo($buttonDiv).button({
				icons: {
					primary:icon
				}, 
				text:false
			});
		};

		this.vars.$addBtn = createBtn('ui-icon-plus');
		this.vars.$subBtn = createBtn('ui-icon-minus');
		this.vars.$moveUpBtn = createBtn('ui-icon-arrowthick-1-n');
		this.vars.$moveDownBtn = createBtn('ui-icon-arrowthick-1-s');

		$row = $('<tr />').appendTo($table);

		var $textDiv = $('<td colspan="2" />').appendTo($row);
		if (this.options.mode === this.vars.modes.TEXT) {
			this.vars.$text = $('<input type="text" value=""/>').appendTo($textDiv);
		} else if (this.options.mode === this.vars.modes.AUTOCOMPLETE) {
			this.vars.$text = widgetObject.find('.jadeAutocompleteAjax');
			widgetObject.appendTo($textDiv);
			widgetObject.notationMultiwidgets({
				languages: this.options.languages
			});
		}

		if (this.options.tagName !== '') {
			this.vars.$select.attr('id', this.options.tagName);
		}

		// generate CSS
		this.$elementToPutObject.addClass('ui-widget');
		this.$elementToPutObject.addClass('ui-widget-content');

		this.$elementToPutObject.addClass('globazMultistring');
		this.vars.$select.addClass('globazMultistringSelectDiv');
		$textDiv.addClass('globazMultistringTextDiv');

		if (this.options.mode === this.vars.modes.TEXT) {
			this.vars.$text.addClass('globazMultistringSimpleText');
		} else if (this.options.mode === this.vars.modes.AUTOCOMPLETE) {
			var widthTotal = $textDiv.width();
			var widthRadio = $textDiv.find('.radiosZone').width();
			$textDiv.find('.jadeAutocompleteAjax').css({
				width: (widthTotal - widthRadio - 15) + 'px'
			});
		}
	},

	deleteValue: function ($option) {
		if ($option) {

			$option.remove();

			var s_idOption = $option.attr('id');
			$('#' + s_idOption.substring(0, s_idOption.lastIndexOf('-')) + '-input' + s_idOption.substring(s_idOption.lastIndexOf('input'))).remove();

			this.resetTextZone();
		}
	},

	addValue: function () {
		if (this.getActive$Text().val() !== '') {
			var value = this.getActive$Text().val();
			if (this.options.mode === this.vars.modes.AUTOCOMPLETE) {
				value = this.vars.textValue;
			}

			this.vars.lastId++;

			$('<option />', {
				'id': 'multistring' + this.vars.n_uniqueID + '-option' + this.vars.lastId,
				'text': this.getActive$Text().val(),
				'value': value
			}).appendTo(this.vars.$select);

			$('<input />', {
				'id': 'multistring' + this.vars.n_uniqueID + '-input' + this.vars.lastId,
				'type': 'hidden',
				'name': this.options.tagName,
				'value': value
			}).appendTo(this.$elementToPutObject);

			this.resetTextZone();
		}
	},

	resetTextZone: function () {

		var that = this;

		// hack pour FF, sinon le champ $text n'est pas modifié
		setTimeout(function () {
			if (that.options.mode === that.vars.modes.TEXT) {
				that.vars.$text.val('');
			} else if (that.options.mode === that.vars.modes.AUTOCOMPLETE) {
				that.vars.$text.each(function () {
					$(this).val('');
				});
			}
			that.vars.$selectedOption = null;
			that.vars.$select.val('');
			that.vars.$addBtn.button('option', 'disabled', false);
			that.vars.$subBtn.button('option', 'disabled', true);
			that.vars.textValue = null;
		}, 10);
		// IE 6/7 Hack, sinon les modifications du DOM ne s'affiche pas...
		this.vars.$select.hide().show();
	}
};
