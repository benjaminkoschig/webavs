/**
 * @author PBA
 */
globazNotation.email = {

	author: 'PBA',
	forTagHtml: 'input,textarea',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Cette objet permet de gérer les champs inputs qui contiendrons une adresse e-mail<br />",

	descriptionOptions: {
		mandatory: {
			desc: "Défini si l'élément est obligatoire",
			param: "true|false(par défault)"
		},
		addSymboleMandatory: {
			desc: "Ajoute le symoble * si l'élement est obligatoire (attribut mandatory à true).",
			param: "true(par défault)|false"
		}
	},

	// vu sur http://www.regular-expressions.info/email.html le 30.08.2011
	REGEXP_EMAIL: /(?:[a-z0-9!#$%&'*+\/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+\/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/,

	bindEvent: {
		ajaxShowDetailRefresh: function (event) {
			this.utils.input.testColor(this.options, this.$elementToPutObject);
		}
	},

	options: {
		mandatory: false,
		addSymboleMandatory: true
	},

	init: function ($elementToPutObject) {
		this.utils.input.addAllPropertyFromUtil(this.options, $elementToPutObject);
		this.bindEvents();
	},

	bindEvents: function () {
		var that = this;

		that.$elementToPutObject.change(function () {
			that.validate();
		});
	},

	validate: function () {
		var s_email = this.$elementToPutObject.val();

		var n_atpos = s_email.indexOf("@");
		var n_dotpos = s_email.lastIndexOf(".");
		if (n_atpos < 1 || n_dotpos < n_atpos + 2 || n_dotpos + 2 >= s_email.length || s_email.match(this.REGEXP_EMAIL) == null) {
			this.processInvalidEmail();
			return false;
		}
		this.processValidEmail();
		return true;
	},

	processInvalidEmail: function () {
		this.$elementToPutObject.addClass('errorBgColor');
		globazNotation.utils.logging.error('email invalide', this.$elementToPutObject);
	},

	processValidEmail: function () {
		this.$elementToPutObject.removeClass('errorBgColor');
		globazNotation.utils.logging.info('email valide', this.$elementToPutObject);
	}
};
