globazNotation.autocomplete = {

	author: 'DMA,PBA',

	forTagHtml: 'input',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,
	
	degreePrioritee: 99,

	description: "Cette objet permet d'ajouter le comportement d'autocompletion aux champs <code>&lt;input/&gt;</code><br />" +
	"Il est obligatoire de renseigner le service ou le manager à utiliser pour la recherche",

	descriptionOptions: {
		mandatory: {
			desc: 'Défini si le champ d\'auto-complétion est obligatoire au niveau métier ou non',
			param: '<code>false</code>(par défaut), ou <code>true</code>'
		},
		service: {
			desc: 'Le service qui sera appelé pour la recherche (chemin complet)<br/>' +
			'Il est nécessaire de définir soit un service soit un manager pour la recherche',
			param: 'le package et le nom de la déclaration (interface) du service à utiliser'
		},
		method: {
			desc: 'le nom de la méthode (du service) qui sera utilisée pour lancer la recherche<br/>' + 
			'Il n\'est pas nécessaire de définir cette option si on utilise un manager (vu que la méthode sera toujours <code>find()</code>)',
			param: 'le nom de la méthode sans les parenthèses et sans les paramètres'
		},
		manager: {
			desc: 'Le manager qui sera appelé pour la recherche (chemin complet)<br/>' + 
			'Il est nécessaire de définir soit un service soit un manager pour la recherche',
			param: 'le package et le nom de la classe du manager à utiliser'
		},
		criterias: {
			desc: 'Une map (Json) contenant comme clés les nom des paramètre de recherche (en Java) et comme valeurs le texte à afficher à l\'utilistaeur pour ce critère de recherche<br/>' +
			'Attention à ne pas oublier de mettre le caractère ¦ de chaque côté de la map dans la déclaration de la notation',
			param: 'Une map de type : <code>¦{<br/>' +
			'&nbsp;&nbsp;&nbsp;&nbsp;\'likeNomTiers\': \'Nom\',<br/>' +
			'&nbsp;&nbsp;&nbsp;&nbsp;\'likePrenomTiers\': \'Prénom\'<br/>' +
			'}¦</code>'
		},
		constCriterias: {
			desc: 'Une chaîne de caractère définissant les constantes pour la recherche.<br />Si par exemple je ne veux rechercher que les personnes qui sont nées le 01.01.1900, les paramètres constants seront : <code>constCriterias:¦forDateNaissance="01.01.2000"¦</code>',
			param: 'Les valeurs constantes pour la recherche'
		},
		lineFormatter: {
			desc: 'Le format qu\'auront les lignes retournées par la recherche.<br/>' + 
			'La syntaxe est la suivante : Si dans l\'objet Java que retourne le service ou le manager il y a, par exemple, une méthode <code>getSimplePersonne()</code> qui retourne un container qui lui même à une méthode <code>getPrenom()</code>, il est possible d\'afficher cette valeur en écrivant <code>#{simplePersonne.prenom}</code>',
			param: 'une chaîne avec la syntaxe vu dans la description<br/>' + 
			'Exemple : <code>#{simplePersonne.nom} #{simplePersonne.prenom}, #{simplePersonne.dateNaissance}</code> affichera "NomBidon PrénomBidon, DateBidon"'
		},
		modelReturnVariables: {
			desc: 'Défini les données qui seront présentes dans le paramètre "element" de la fonction de callback.<br/>' + 
			'Si par exemple la recherche retourne des objets ayant une méthode <code>getSimplePersonne()</code> retournant un container pour les données d\'une personne et que l\'on veut avoir des données du tiers comme l\'ID tiers et l\'ID du pays du tiers pour remplir des champs cachés.<br/>' + 
			'Il faut que le paramètre <code>modelReturnVariables</code> contienne <code>simplePersonne.idTiers,simplePersonne.idPays</code> (les données sont séparées par des virgules et définies par convention de nommage -> <code>simplePersonne.idTiers</code> sera intérpété comme <code><strong>this</strong>.getSimplePersonne().getIdTiers()</code>)',
			param: ''
		},
		nbReturn: {
			desc: 'Défini le nombre d\'élement affichés par "page" du widget (possiblité de passer d\'une page à l\'autre par la touche "+" et "-")<br/>' +
			'La valeur 0 (<code>BManager.SIZE_NOLIMIT</code> ou <code>JadeAbstractSearchModel.SIZE_NOLIMIT</code>)définie que tout le résultat de la requête sera retourné dans l\'affichage',
			param: 'par défaut : 20, ou un autre entier positif (ou zéro pour aucune limite)'
		},
		functionReturn: {
			desc: 'Défini la fonction de callback (anonymement), ou le nom de la fonction de callback (défini dans le javascript de la page), qui sera appelée après la séléction d\'un élement dans le widget d\'autocomplétion<br />' + 
			'Les élements accessibles (dans le Json element passé en paramètre) dans cette fonction de callback sont ceux définis dans <code>modelReturnVariables</code>.',
			param: 'Un nom de fonction, ou une fonction anonyme qui sera appelé après la séclection d\'un élement dans l\'auto-complétion<br />' +
			'Devra être wrappée dans les caractères spéciaux ¦ si déclarée anonymement<br />' +
			'Exemple de fonction anonyme :<br /><code>' + 
			'functionReturn:&nbsp;¦function&nbsp;(element)&nbsp;{<br />' + 
			'&nbsp;&nbsp;&nbsp;&nbsp;var&nbsp;$element&nbsp;=&nbsp;$(element);<br />' +
			'&nbsp;&nbsp;&nbsp;&nbsp;$(\'#idTiers\').val($element.attr(\'idTiers\'));<br />' +
			'};¦</code>'
		},
		nbOfCharBeforeLaunch: {
			desc: 'Défini le nombre de caractères, dans le champ <code>&lt;input&gt;</code> nécessaires à lancer la requête.',
			param: 'Un entier, ou zéro pour définir que la requête doit être lancée dès que le champ est séléctionné (à vos risques et périls!).'
		},
		dynamicCriterias: {
			desc: 'Un nom de fonction, ou une fonction anonyme, qui défini les paramètres dynamiques de la recherche.<br />' + 
			'Par exemeple, la recheche dépend du type de tiers qui est défini dans un champ <code>&lt;select id="typeTiers"&gt;</code> de la page.<br />' + 
			'La fonction anonyme correspondante sera : <br />' + 
			'<code>dynamicCriterias&nbsp;:&nbsp;¦function () {<br />' + 
			'&nbsp;&nbsp;&nbsp;&nbsp;return&nbsp;"forCsTypeTiers="&nbsp;+&nbsp;$(\'#typeTiers\').val();<br />' + 
			'}¦</code>',
			param: 'un nom de fonction javascipt défini dans la page, ou une fonction anonyme définissant les pramètres dynamiques (définis à chaque recherche par la méthode) de la recherche'
		},
		wantValueCache : {
			desc: 'Défini si les valeurs précédantes du widget doivent être mises en mémoire, et retournées si aucune nouvelle sélection n\'a été faite.',
			param: '<code>false</code>(par défaut), ou <code>true</code>'
		}
	},

	options: {
		mandatory: false,
		service: '',
		manager: '',
		method: '',
		criterias: {},
		constCriterias: '',
		lineFormatter: '',
		modelReturnVariables: '',
		nbReturn: 20,
		functionReturn: function () {},
		nbOfCharBeforeLaunch: 3,
		dynamicCriterias: function () {},
		wantInitThreadContext: false,
		wantValueCache: true
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

		btnCancel: function () {
			this.enableDisableInput(true);
		},

		ajaxStopEdition: function () {
			this.enableDisableInput(true);	
		}
	},

	init: function () {
		var script = null,
		    style = null;
		
		if(!jQuery.fn.globazWidget){
			script = document.createElement('script');
			script.type = 'text/javascript';
			script.src = this.s_contextUrl + "/scripts/widget/globazwidget.js";
			document.getElementsByTagName('head')[0].appendChild(script);
	
			style = document.createElement('style');
			style.type = 'text/css';
			style.src = this.s_contextUrl + "/theme/widget.css";
			document.getElementsByTagName('head')[0].appendChild(style);		
		}
		
		this.$elementToPutObject.addClass('jadeAutocompleteAjax');

		var labels = [];
		var criteriasToString = '';
		if (this.options.criterias) {
			for (var aCriteria in this.options.criterias) {
				if (this.options.criterias.hasOwnProperty(aCriteria)) {
					criteriasToString += aCriteria + ',';
					labels.push(this.options.criterias[aCriteria]);
				}
			}
			criteriasToString = criteriasToString.substr(0, criteriasToString.lastIndexOf(','));
		}

		this.utils.input.addAllPropertyFromUtil(this.options, this.$elementToPutObject);		
		this.startAsync(criteriasToString, labels);
		
	},
	
	startAsync: function (criteriasToString, labels) {
		var  i = 0, that = this;
		if (!jQuery.fn.globazWidget) {
			setTimeout(function () {
				that.startAsync(criteriasToString, labels);
			}, 5);
		} else {
			that.initWidget(criteriasToString, labels);
		}
	},
	
	initWidget: function (criteriasToString, labels) {
		if (this.options.manager && this.options.manager !== '') {
			this.$elementToPutObject.globazWidget(
					'', 
					'', 
					this.options.manager, 
					criteriasToString, 
					this.options.constCriterias, 
					this.options.lineFormatter, 
					this.options.modelReturnVariables, 
					labels, 
					this.options.nbReturn, 
					this.options.functionReturn, 
					this.options.nbOfCharBeforeLaunch, 
					this.options.dynamicCriterias, 
					this.options.wantValueCache,
					this.options.wantInitThreadContext
			);
		} else {
			this.$elementToPutObject.globazWidget(
					this.options.service, 
					this.options.method, 
					'', 
					criteriasToString, 
					this.options.constCriterias, 
					this.options.lineFormatter, 
					this.options.modelReturnVariables, 
					labels, 
					this.options.nbReturn, 
					this.options.functionReturn, 
					this.options.nbOfCharBeforeLaunch, 
					this.options.dynamicCriterias, 
					this.options.wantValueCache,
					this.options.wantInitThreadContext
			);
		}
	},

	enableDisableInput: function (b_forceState) {
		var b_isDisabled;
		if (typeof b_forceState !== 'undefined') {
			b_isDisabled = !!b_forceState;
		} else {
			b_isDisabled = this.utils.input.isDisabled(this.$elementToPutObject);
		}

		if (this.options.mandatory) {
			if (b_isDisabled && this.$elementToPutObject.hasClass('errorColor')) {
				this.$elementToPutObject.removeClass('errorColor');
				this.$elementToPutObject.addClass('errorColorDisable');
			} else if (this.$elementToPutObject.hasClass('errorColorDisable')) {
				this.$elementToPutObject.removeClass('errorColorDisable');
				this.$elementToPutObject.addClass('errorColor');
			}
		} else {
			if (b_isDisabled) {
				this.$elementToPutObject.removeClass('jadeAutoCompleteAjaxColor');
				this.$elementToPutObject.addClass('jadeAutoCompleteAjaxColorDisabled');
			} else {
				this.$elementToPutObject.removeClass('jadeAutoCompleteAjaxColorDisabled');
				this.$elementToPutObject.addClass('jadeAutoCompleteAjaxColor');
			}
		}
	}
};
