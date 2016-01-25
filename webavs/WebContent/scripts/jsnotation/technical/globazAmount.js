/**
 * @author DMA
 */
globazNotation.amount = {

	author: 'DMA',
	forTagHtml: 'input, span',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description:	"Cette objet ajoute la class montant au champ input et le formate. <br />"
					+ "Remplace l'ancienne mani�re de faire /<code>onchange='validateFloatNumber(this);' onkeypress='return filterCharForFloat(window.event)'</code>",

	descriptionOptions: {
		mandatory: {
			desc: "D�fini si l'�l�ment est obligatoire",
			param: "true, false(default)"
		},
		addSymboleMandatory: {
			desc: "Ajoute le symbole *. Si le param�tre mandatory est � true ce param�tre n'est pas utilis�",
			param: "true(default)|false"
		},
		blankAsZero: {
			desc: "D�finit si il faut afficher la non valeur par 0.00. Si la valeur est mandatory on ne peut pas d�finir cette option",
			param: "true(default), false(si la valeur est mandatory)"
		},
		unsigned: {
			desc: "Indique si le montant peut �tre n�gatif",
			param: "true, false(default)"
		},
		periodicity: {
			desc: "Permet d'ajouter une indication graphique de periodicit�. Les valeurs possible sont: Y(anne�),M(moi),D(jour) ",
			param: "true, false(default)",
			mandatory: false
		}
	},

	/**
	 * Param�tres de l'objet qui vont �tre n�cessaires � son initialisation
	 * Cet �l�ment est obligatoire. S'il n'y a pas d'option, le cr�er vide (options:{})
	 */
	options: {
		mandatory: false,
		addSymboleMandatory: true,
		blankAsZero: true,
		unsigned: false,
		icon: "",
		periodicity:""
	},

	/**
	 * Ce param�tre est facultatif.<br/>
	 * Il permet des lancer des fonctions sur diff�rent types d'�venements.<br/>
	 * Liste des �v�nements :<br/>
	 * 	<ul>
	 * 		<li>boutons standard de l'application. Les �v�nements se lancent sur le clique du bouton</li>
	 * 		<ul>
	 * 			<li>btnCancel</li>
	 * 			<li>btnAdd</li>
	 * 			<li>btnValidate</li>
	 * 			<li>btnUpdate</li>
	 * 			<li>btnDelete</li>
	 * 		</ul>
	 * 		<li>AJAX: toutes ces fonctions se lancent � la fin de la fonction dans AJAX</li>
	 * 		<ul>
	 * 			<li>ajaxShowDetailRefresh</li>
	 * 			<li>ajaxLoadData</li>
	 * 			<li>ajaxShowDetail</li>
	 * 			<li>ajaxStopEdition</li>
	 * 			<li>ajaxValidateEditon</li>
	 * 			<li>ajaxUpdateComplete</li>
	 * 		</ul>
	 * 	</ul>
	 */
	bindEvent: {
		ajaxShowDetailRefresh: function (event) {
			this.utils.input.testColor(this.options, this.$elementToPutObject);
			this.formatAmount();
		}
	},

	/**
	 * Cette fonction est obligatoire car c'est elle qui va initialiser l'objet
	 */
	init: function ($elementToPutObject) {
		this.utils.input.addAllPropertyFromUtil(this.options, $elementToPutObject);
		this.addEventAndClassForMontant($elementToPutObject);
		$elementToPutObject.trigger('formatNonValid', 'la valeur');
		this.addIcon($elementToPutObject);
		this.addPeriodicite($elementToPutObject);
		this.formatAmount();
	},
	
	translate: function () {
		var message,text; 
		if("Yy".indexOf(this.options.periodicity)>-1){
			message = this.i18n('year');
			text = this.i18n('Y');
		} else if ("Mm".indexOf(this.options.periodicity)>-1) {
			message = this.i18n('month');
			text = this.i18n('M');
		} else  if("Dd".indexOf(this.options.periodicity)>-1) {
			message = this.i18n('day');
			text = this.i18n('D');
		}
		return {message:message, text:text};
	},

	addPeriodicite: function ($elementToPutObject){
		var translate, $periodicity; 
		if (this.options.periodicity !== "") {
			if("YyMmDd".indexOf(this.options.periodicity)>-1) {
				translate = this.translate();
				$periodicity = $("<a>",	{
					'class': 'periodicite',
					"text": translate.text,
					"title": translate.message,
					"alt": translate.message
				}).insertAfter($elementToPutObject);
			} else {
				throw "This value '"+this.options.periodicity+"' is not handled by the paramters of periocidity";
			}
		}
	},
	
	addIcon: function ($elementToPutObject) {
		if (this.options.icon !== "") {
				$("<img/>",	{
					src: this.s_contextUrl + "/scripts/jsnotation/imgs/" + this.options.icon,
					'class': 'icon',
					alt: " ",
					title: ''
				}).insertAfter($elementToPutObject); 
		}		
	},

	formatAmount: function () {
		var n_val, s_amount;
		if(	this.$elementToPutObject.is("span")) {
			n_val = this.$elementToPutObject.text();
		} else {
			n_val = this.$elementToPutObject.val();
		}
		if (this.options.unsigned && (n_val < 0)) {
			n_val *= -1.0;
		}
		
		s_amount = this.utils.formatter.formatStringToAmout(n_val, 2, this.options.blankAsZero);
		
		if(this.$elementToPutObject.is("span")) {
			this.$elementToPutObject.text(s_amount);
		} else {
			this.$elementToPutObject.val(s_amount);
		}
	},

	addEventAndClassForMontant: function ($obj) {
		var that = this;
		if (this.options.mandatory) {
			that.options.blankAsZero = false;
		}
		$obj.change(function () {
			that.formatAmount();
		});
		$obj.addClass('montant');
		this.utils.input.addEventIsAmount($obj);
	},

	validate: function () {
		if (this.utils.input.validate(this.options, this.$elementToPutObject) !== '') {
			return false;
		}
		return true;
	}
};
