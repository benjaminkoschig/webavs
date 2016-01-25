globazNotation.clone = {

	author: 'PBA',
	forTagHtml: '*',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "",

	descriptionOptions: {
		waitForManagerToSetClone: {
			desc: "D�fini si la notation attendra que toutes les notations se soient initialis�es avant de d�finir son chablon de clone.",
			param: "true|false(par d�faut)"
		},
		event: {
			desc: "D�fini un �venement (javascript) sur lequel doit �tre d�clench� le cloning.<br/>Cette �venement sera �couter sur l'�lement clonable (le tr par exemple)",
			param: "Le nom de l'�venement"
		},
		disabled: {
			desc: "D�fini si le(s) boutton(s) doivent �tre d�sactiv�s par d�faut (pour par exemple une page de d�tail)",
			param: "true|false(par d�faut)"
		},
		deleteAlwaysDisabled: {
			desc: "D�fini si le bouton delete doit toujours �tre d�sactiv�",
			param: "true (par d�faut)|false"
		}
	},

	degreePrioritee: 100,

	/**
	 * Ce param�tre est facultatif.<br/>
	 * Il permet des lancer des fonctions sur diff�rent types d'�venements.<br/>
	 * Liste des �v�nements :<br/>
	 *  <ul>
	 *   <li>boutons standard de l'application. Les �v�nements se lancent sur le clique du bouton</li>
	 *   <ul>
	 *    <li>btnCancel</li>
	 *    <li>btnAdd</li>
	 *    <li>btnValidate</li>
	 *    <li>btnUpdate</li>
	 *    <li>btnDelete</li>
	 *   </ul>
	 *   <li>AJAX: toutes ces fonctions se lancent � la fin de la fonction dans AJAX</li>
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

	/**
	 * Param�tre de le l'objet qui vont �tre utilis�s pour son initialisation
	 * Cet �l�ment est obligatoire. Si aucune option, le cr�er vide (options:{})
	 */
	options: {
		waitForManagerToSetClone: false,
		event: null,
		disabled: false,
		deleteAlwaysDisabled: true
	},

	vars: {
		$cloneModel: null,
		$addButtonHandler: null,
		$deleteButtonHandler: null,
		b_disableButtons: false,
		n_uniqueId: 0
	},

	/**
	 * Cette fonction est obligatoire car c'est elle qui va initialiser l'objet
	 */
	init: function () {
		var that = this;

		if (!this.options.waitForManagerToSetClone || notationManager.b_started) {
			this.vars.$cloneModel = this.createCloneModel();
		} else {
			$('html').bind(eventConstant.NOTATION_MANAGER_DONE, function () {
				that.vars.$cloneModel = that.createCloneModel();
			});
		}

		this.vars.b_disableButtons = !!this.options.disabled;
		this.vars.n_uniqueId = this.utils.generateUniqueNumber();

		this.buildButtonHandlers();
		this.buildButtons();

		this.$elementToPutObject.addClass('globazClone');
	},

	enableDisableInput: function (b_forceState) {
		var b_isDisabled;
		if (typeof b_forceState !== 'undefined') {
			b_isDisabled = !!b_forceState;
		} else {
			b_isDisabled = this.utils.input.isDisabled(this.$elementToPutObject.children(':input'));
		}

		var $addCloneButton = this.$elementToPutObject.find('.addCloneButton');
		var $deleteCloneButton = this.$elementToPutObject.find('.deleteCloneButton:not(.alwaysDisabled)');
		if (b_isDisabled) {
			$addCloneButton.button("option", "disabled", true);
			$deleteCloneButton.button("option", "disabled", true);
			this.vars.b_disableButtons = true;
		} else {
			$addCloneButton.button("option", "disabled", false);
			$deleteCloneButton.button("option", "disabled", false);
			this.vars.b_disableButtons = false;
		}
	},

	/**
	 * Construit les handlers pour le bouton d'ajout et de suppression de clones.<br/>
	 * Ces handlers ne seront construit qu'une seule fois.
	 */
	buildButtonHandlers: function () {
		var that = this;

		if (this.vars.$addButtonHandler === null) {
			this.vars.$addButtonHandler = function () {
				that.addClone(that);
			};
		}
		if (this.vars.$deleteButtonHandler === null) {
			this.vars.$deleteButtonHandler = function () {
				that.deleteClone(that);
			};
		}
	},

	/**
	 * Construit et ajoute au DOM les boutons d'ajout/suppression de clones et connecte les �v�nements associ�s.<br/>
	 * Il est n�cessaire d'avoir construit les handlers avant l'appel � cette fonction.
	 */
	buildButtons: function () {
		var $buttonContainer = null;

		if (this.$elementToPutObject.is('tr')) {
			$buttonContainer = $('<td/>');
			this.$elementToPutObject.append($buttonContainer);
		} else {
			$buttonContainer = this.$elementToPutObject;
		}

		$buttonContainer.append(this.buildAddButton());
		$buttonContainer.append(this.buildDeleteButton(this.options.deleteAlwaysDisabled));
	},

	/**
	 * Construit, connecte aux event et retourne le bouton pour ajouter un clone
	 * @returns un bouton pour ajouter un clone
	 */
	buildAddButton: function () {
		var $addButton = $('<span/>', {
			'class': 'addCloneButton globazIconButton'
		});
		this.initAddButtonBehavior($addButton);
		return $addButton;
	},

	/**
	 * Construit, connecte aux event et retourne le bouton pour supprimer un clone.<br/>
	 * Le bouton est d�sactiv� (disabled) par d�faut.
	 * @param b_disabled si le bouton doit �tre cr�er "disabled" ou non (true|false)
	 * @returns un bouton pour supprimer un clone
	 */
	buildDeleteButton: function (b_disabled) {
		if ($.type(b_disabled) === 'undefined') {
			b_disabled = true;
		}
		var $deleteButton = $('<span/>', {
			'class': 'deleteCloneButton globazIconButton'
		});
		this.initDeleteButtonBehavior($deleteButton, b_disabled);
		return $deleteButton;
	},

	initAddButtonBehavior: function ($addButton) {
		$addButton.button({
			icons: {
				primary: 'ui-icon-plus'
			},
			text: false,
			disabled: this.vars.b_disableButtons
		});
		$addButton.click(this.vars.$addButtonHandler);

		if (this.options.event) {
			this.$elementToPutObject.bind(this.options.event, function () {
				$addButton.click();
			});
		}
	},

	/**
	 * Initialise le comportement du bouton de suppression de clone.<br/>
	 * Par d�faut (si b_disabled n'est pas d�fini) le bouton est d�sactiv�.
	 * @param $deleteButton le bouton wrapper avec jQuery
	 * @param b_disabled d�fini si le bouton doit �tre d�sactiv� (disabled)
	 */
	initDeleteButtonBehavior: function ($deleteButton, b_disabled) {
		$deleteButton.button({
			icons: {
				primary: 'ui-icon-minus'
			},
			text: false,
			disabled: !!b_disabled || this.vars.b_disableButtons
		});
		if (b_disabled) {
			$deleteButton.addClass('alwaysDisabled');
		}
		$deleteButton.click(this.vars.$deleteButtonHandler);
	},

	/**
	 * Parcours le DOM en descendant jusqu'� la derni�re notation clone et initialise le comportement par d�faut des boutons en remontant.
	 * @param $element l'�lement DOM wrapper par jQuery � parcourir
	 */
	initDefaultButtonBehaviorCascade: function ($element, n_depth) {
		var that = this;
		var $innerClone = $element.find('.globazClone');

		// y a-t-il des clones dans le clone?
		if ($innerClone.length !== 0) {
			// gestion particuli�re pour les boutons des clones internes
			// TODO : � revoir
			$innerClone.each(function () {
				notationManager.addObjToElement(this);
				that.initDefaultButtonBehaviorCascade($(this), n_depth + 1);
			});
		}

		// recherche des boutons dans la notation (et uniquement sur le 1er niveau du DOM pour ne pas interf�rer avec des clones internes)
		var $deleteButton = null;
		var $addButton = null;
		if ($element.is('tr')) {
			var $lastTd = $element.children('td:last');
			$deleteButton = $lastTd.children('.deleteCloneButton');
			$addButton = $lastTd.children('.addCloneButton');
		} else {
			$deleteButton = $element.children('.deleteCloneButton:last');
			$addButton = $element.children('.addCloneButton:last');
		}
		$deleteButton.button('option', 'disabled', false);
		$addButton.button('option', 'disabled', false);
	},

	/**
	 * Construit et retourne le chablon pour les futurs clones.<br/>
	 * Il sera construit par rapport � $elementToPutObject au moment de l'appel de la fonction (qui peut �tre diff�rent selon l'option waitForManagerToSetClone de la notation).
	 * @returns le chablon pour les futurs clones
	 */
	createCloneModel: function () {
		return this.$elementToPutObject.clone(false, false);
	},

	/**
	 * Ajoute un clone � la suite de l'�lement appelant
	 * @param button le bouton ayant d�clench� l'action
	 */
	addClone: function (button) {
		var $clone = this.vars.$cloneModel.clone(false, false);

		// on active tous les champs du clone (si besoin)
		$clone.find(':disabled').andSelf().removeProp('disabled');

		this.reInitClone($clone);
		this.initDefaultButtonBehaviorCascade($clone, 0);

		this.$elementToPutObject.after($clone);
		notationManager.initFocusColorBehavior();
		this.$elementToPutObject.trigger(eventConstant.GLOBAZ_CLONE_DONE);
	},

	reInitClone: function ($clone) {
		notationManager.addNotationOnFragmentWithoutEvents($clone);
	},

	/**
	 * Supprime le clone correspondant au bouton press�
	 * @param button le bouton ayant d�clench� l'action
	 */
	deleteClone: function (button) {
		button.$elementToPutObject.remove();
	}
};
