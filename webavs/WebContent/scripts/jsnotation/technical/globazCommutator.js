/**
 * @author DMA
 */

/**
 * le nouvelle objet(json) doit �tre �crit en MINUSCUL. Nous allons ajouter un nouvelle objet "nouvellenotation" � notre espace de nom "globazNotation" Toutes les fonctions est
 * attributs d�crit ci-dessous son obligatoire...
 */
globazNotation.commutator = {
	/** ****************************DOCUMENTATION************************************** */
	author: 'DMA',
	forTagHtml: "input,select,tr,td,div,span",
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Permet de d�clancher(trigger) des actions d�finit sur des �l�ment donn�e(target)",

	descriptionOptions: {
		trigger: {
			desc: "evenement javascript declacheur",
			param: "tous evenement jquery (click,change(default),keypresse,..)"
		},
		master: {
			desc: "D�finit quel sera l'�l�ment d�clancheur(listener). C'est lui qui g�n�rera l'�venement d�finti dans le trigger",
			param: "JS(JQUERY)|Par defaut this"
		},
		condition: {
			desc: "D�finit la condition boolean qui vas �tre utilis� pour r�partir les actions",
			param: "Conditions booelan JS.Ex: ($(this).val==5257 || true==true && 8>7)"
		},
		actionTrue: {
			desc: "Action qui sera d�clacher si la condition est vrais.<br />" + "On peut d�finir plusieur actions mais elles doivent �tre entour� de ��. <br />" + "On peut d�finir la cible de l'action si rien n'est precic� l'action vas s'executer sur l'element(this) ou se trouve la notation" + "Ex: �show('#cible1,#cible2,...'),mandatory('.cible')�",
			param: "Actions d�finit: clear,show,mandatory,hide."
		},
		actionFalse: {
			desc: "Action qui sera d�clacher si la condition du dispatcher est faux.<br />" + "On peut d�finir plusieurs actions, mais elles doivent �tre entour� de ��. <br />",
			param: "Actions d�finit: Idem que actionTrue"
		}
	},

	/** ****************************UTILISATION**************************************** */

	/**
	 * Param�tre de le l'objet qui vont �tre utilis� pour son initialisation 
	 * Cet �l�ment est obligatoire. C'est dans ces options que l'on peut mettre les param�tres par d�fauts.
	 * L'appel de l'objet dans le html se fera de cette mani�re : 
	 * globaz:nouvellenotation="option1:valeur,option2:valeur" ou globaz:nouvellenotation="option2:valeur" S'il n'y pas
	 * d'option pour l'objet il faut quand m�me le cr�er, mais vide (options:{})
	 */
	options: {
		context: '',
		trigger: 'change',
		condition: null,
		master: null,
		actionTrue: 'display',
		actionFalse: 'hide',
		target: null
	},

	/**
	 * Cette fonction est obligatoire car c'est elle qui va initialiser l'objet $elementToPutObject : 
	 * Correspond � un objet Jquery qui contient le noeud ou se trouve notre notation
	 */
	$ele: null,

	init: function ($elementToPutObject) {
		var that = this;
		var $eleTrigger = $elementToPutObject;
		this.$ele = $elementToPutObject;

		if (this.options.context !== '') {
			this.options.context = this.options.context.replace('$(this)', 'that.$ele');
		}

		if (this.options.master !== null) {
			this.options.master = this.createStringToExecut(this.options.master);
			$eleTrigger = eval(this.options.master);
		}
		// var stringConditionnel=this.createCondition();

		/*
		 * if (eval(stringConditionnel)){ this.actionTrue(); }else{ this.actionFalse(); }
		 */
		this.execute();

		$eleTrigger.bind(this.options.trigger + ".comutator", function () {
			that.execute();
		});
	},

	createStringToExecut: function (s_string) {
		var string = s_string;

		if (s_string.indexOf('this') >= 0) {
			string = s_string.replace(/\$\(this\)/g, 'that.$ele');
		}

		if (this.options.context != '') {
			if (s_string.indexOf('context') >= 0) {
				string = string.replace(/context/g, this.options.context);
			}
		}
		return string;
	},

	createCondition: function () {
		var stringConditionnel = "";
		stringConditionnel = this.createStringToExecut(this.options.condition);
		return stringConditionnel;
	},

	execute: function () {
		var that = this;
		var stringConditionnel = this.createCondition();
		var condition; 
		try {
			condition = eval(stringConditionnel);
			if (condition) {
				that.actionTrue();
			} else {
				that.actionFalse();
			}
		} catch (e) {
			var message = "<strong> Erreur js dans codition <i class='ui-state-error-text'>" + this.createCondition + "</i></strong>" + "<p> Condition Execut�: " + condition + "</p> " + e;
			this.putError(message);
		}
	},

	pars: function (s_param) {
		// pars_attribut=/([\w]*\([\s\w.\/'"#%\\=|\{\}&!\[\];\+\-\?<>,]*\)|[\w]*\(\$\([\s\w.\/'"#%\\=|\{\}&!\[\];\+\-\?<>,]*\)\))/g;
		// t_param =s_param.match(pars_attribut);
		var t_param = s_param.split('\),');
		for (var i in t_param) {
			if (i < t_param.length - 1) {
				t_param[i] = t_param[i] + ')';
			}
		}
		// t_param= s_param.splilt(/(?:\)),(?:\w)/g);
		return t_param;

	},

	isActionVailable: function (s_action) {
		var s_name = s_action.match(/(^[\w]*)/)[0];
		s_name = s_name.toLowerCase();
		var b_return = false;
		for (var f in this) {
			if (s_name === f.toLowerCase()) {
				return true;
			}
		}
		return false;
	},

	applyActions: function (s_actions) {
		var that = this;
		var t_action = this.pars(s_actions);
		var action = "";
		var message = '';

		for (var i = 0; i < t_action.length; i++) {
			action = t_action[i];
			action = this.createStringToExecut(action);
			if (this.isActionVailable(action)) {
				try {
					var value = eval('[that.' + (action) + ']')[0];
				} catch (e) {
					message = "<strong> Erreur js dans cette action <i class='ui-state-error-text'>" + action + "</i></strong>" + e;
					this.putError(message);
				}
			} else if ($.isFunction(action)) {
				action = eval('[' + (action) + ']')[0];
				action();
			} else {
				message = "<strong> Cette action  <i class='ui-state-error-text'>" + action + "</i>exite pas</strong>";
				this.putError(message);
			}
		}
	},

	actionTrue: function () {
		this.applyActions(this.options.actionTrue);
	},

	actionFalse: function () {
		this.applyActions(this.options.actionFalse);
	},

	get$Identifiants: function (identifiants) {
		var $identifiants = null;
		if (typeof identifiants === 'string') {
			$identifiants = $(identifiants);
		} else {
			$identifiants = identifiants;
		}
		return $identifiants;
	},

	/* fonction que l'on peut appler dans l'objet */
	show: function (identifiants) {
		if (!this.utils.isEmpty(identifiants)) {
			var $identifiants = this.get$Identifiants(identifiants);
			$identifiants.show();
		} else {
			this.$ele.show();
		}
	},

	hide: function (identifiants) {
		if (!this.utils.isEmpty(identifiants)) {
			var $identifiants = this.get$Identifiants(identifiants);
			$identifiants.hide();
		} else {
			this.$ele.hide();
		}
	},

	notMandatory: function (identifiants) {
		var that = this;
		if (!this.utils.isEmpty(identifiants)) {
			var $identifiants = this.get$Identifiants(identifiants);
			$identifiants.each(function () {
				that.utils.input.removeMandatory($(this));
			});
		} else {
			this.utils.input.removeMandatory(this.$ele);
		}
	},

	mandatory: function (identifiants) {
		var that = this;
		if (!this.utils.isEmpty(identifiants)) {
			var $identifiants = this.get$Identifiants(identifiants);
			$identifiants.each(function () {
				that.utils.input.addMandatory($(this));
			});
		} else {
			that.utils.input.addMandatory(this.$ele);
		}
	},

	clear: function (identifiants) {
		if (!this.utils.isEmpty(identifiants)) {

			var $identifiants = this.get$Identifiants(identifiants);

			if (!$identifiants.is(':input')) {

				$identifiants = $identifiants.find(':input');
			}
			$identifiants.each(function () {
				$(this).clearInput();
			});
		} else {
			this.$ele.clearInput();
		}
	}
};