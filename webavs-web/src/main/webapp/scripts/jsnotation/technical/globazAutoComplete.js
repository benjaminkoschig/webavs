globazNotation.autocomplete = {

	author: 'DMA,PBA',

	forTagHtml: 'input',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,
	
	degreePrioritee: 99,

	description: "Cette objet permet d'ajouter le comportement d'autocompletion aux champs <code>&lt;input/&gt;</code><br />" +
	"Il est obligatoire de renseigner le service ou le manager � utiliser pour la recherche",

	descriptionOptions: {
		mandatory: {
			desc: 'D�fini si le champ d\'auto-compl�tion est obligatoire au niveau m�tier ou non',
			param: '<code>false</code>(par d�faut), ou <code>true</code>'
		},
		service: {
			desc: 'Le service qui sera appel� pour la recherche (chemin complet)<br/>' +
			'Il est n�cessaire de d�finir soit un service soit un manager pour la recherche',
			param: 'le package et le nom de la d�claration (interface) du service � utiliser'
		},
		method: {
			desc: 'le nom de la m�thode (du service) qui sera utilis�e pour lancer la recherche<br/>' + 
			'Il n\'est pas n�cessaire de d�finir cette option si on utilise un manager (vu que la m�thode sera toujours <code>find()</code>)',
			param: 'le nom de la m�thode sans les parenth�ses et sans les param�tres'
		},
		manager: {
			desc: 'Le manager qui sera appel� pour la recherche (chemin complet)<br/>' + 
			'Il est n�cessaire de d�finir soit un service soit un manager pour la recherche',
			param: 'le package et le nom de la classe du manager � utiliser'
		},
		criterias: {
			desc: 'Une map (Json) contenant comme cl�s les nom des param�tre de recherche (en Java) et comme valeurs le texte � afficher � l\'utilistaeur pour ce crit�re de recherche<br/>' +
			'Attention � ne pas oublier de mettre le caract�re � de chaque c�t� de la map dans la d�claration de la notation',
			param: 'Une map de type : <code>�{<br/>' +
			'&nbsp;&nbsp;&nbsp;&nbsp;\'likeNomTiers\': \'Nom\',<br/>' +
			'&nbsp;&nbsp;&nbsp;&nbsp;\'likePrenomTiers\': \'Pr�nom\'<br/>' +
			'}�</code>'
		},
		constCriterias: {
			desc: 'Une cha�ne de caract�re d�finissant les constantes pour la recherche.<br />Si par exemple je ne veux rechercher que les personnes qui sont n�es le 01.01.1900, les param�tres constants seront : <code>constCriterias:�forDateNaissance="01.01.2000"�</code>',
			param: 'Les valeurs constantes pour la recherche'
		},
		lineFormatter: {
			desc: 'Le format qu\'auront les lignes retourn�es par la recherche.<br/>' + 
			'La syntaxe est la suivante : Si dans l\'objet Java que retourne le service ou le manager il y a, par exemple, une m�thode <code>getSimplePersonne()</code> qui retourne un container qui lui m�me � une m�thode <code>getPrenom()</code>, il est possible d\'afficher cette valeur en �crivant <code>#{simplePersonne.prenom}</code>',
			param: 'une cha�ne avec la syntaxe vu dans la description<br/>' + 
			'Exemple : <code>#{simplePersonne.nom} #{simplePersonne.prenom}, #{simplePersonne.dateNaissance}</code> affichera "NomBidon Pr�nomBidon, DateBidon"'
		},
		modelReturnVariables: {
			desc: 'D�fini les donn�es qui seront pr�sentes dans le param�tre "element" de la fonction de callback.<br/>' + 
			'Si par exemple la recherche retourne des objets ayant une m�thode <code>getSimplePersonne()</code> retournant un container pour les donn�es d\'une personne et que l\'on veut avoir des donn�es du tiers comme l\'ID tiers et l\'ID du pays du tiers pour remplir des champs cach�s.<br/>' + 
			'Il faut que le param�tre <code>modelReturnVariables</code> contienne <code>simplePersonne.idTiers,simplePersonne.idPays</code> (les donn�es sont s�par�es par des virgules et d�finies par convention de nommage -> <code>simplePersonne.idTiers</code> sera int�rp�t� comme <code><strong>this</strong>.getSimplePersonne().getIdTiers()</code>)',
			param: ''
		},
		nbReturn: {
			desc: 'D�fini le nombre d\'�lement affich�s par "page" du widget (possiblit� de passer d\'une page � l\'autre par la touche "+" et "-")<br/>' +
			'La valeur 0 (<code>BManager.SIZE_NOLIMIT</code> ou <code>JadeAbstractSearchModel.SIZE_NOLIMIT</code>)d�finie que tout le r�sultat de la requ�te sera retourn� dans l\'affichage',
			param: 'par d�faut : 20, ou un autre entier positif (ou z�ro pour aucune limite)'
		},
		functionReturn: {
			desc: 'D�fini la fonction de callback (anonymement), ou le nom de la fonction de callback (d�fini dans le javascript de la page), qui sera appel�e apr�s la s�l�ction d\'un �lement dans le widget d\'autocompl�tion<br />' + 
			'Les �lements accessibles (dans le Json element pass� en param�tre) dans cette fonction de callback sont ceux d�finis dans <code>modelReturnVariables</code>.',
			param: 'Un nom de fonction, ou une fonction anonyme qui sera appel� apr�s la s�clection d\'un �lement dans l\'auto-compl�tion<br />' +
			'Devra �tre wrapp�e dans les caract�res sp�ciaux � si d�clar�e anonymement<br />' +
			'Exemple de fonction anonyme :<br /><code>' + 
			'functionReturn:&nbsp;�function&nbsp;(element)&nbsp;{<br />' + 
			'&nbsp;&nbsp;&nbsp;&nbsp;var&nbsp;$element&nbsp;=&nbsp;$(element);<br />' +
			'&nbsp;&nbsp;&nbsp;&nbsp;$(\'#idTiers\').val($element.attr(\'idTiers\'));<br />' +
			'};�</code>'
		},
		nbOfCharBeforeLaunch: {
			desc: 'D�fini le nombre de caract�res, dans le champ <code>&lt;input&gt;</code> n�cessaires � lancer la requ�te.',
			param: 'Un entier, ou z�ro pour d�finir que la requ�te doit �tre lanc�e d�s que le champ est s�l�ctionn� (� vos risques et p�rils!).'
		},
		dynamicCriterias: {
			desc: 'Un nom de fonction, ou une fonction anonyme, qui d�fini les param�tres dynamiques de la recherche.<br />' + 
			'Par exemeple, la recheche d�pend du type de tiers qui est d�fini dans un champ <code>&lt;select id="typeTiers"&gt;</code> de la page.<br />' + 
			'La fonction anonyme correspondante sera : <br />' + 
			'<code>dynamicCriterias&nbsp;:&nbsp;�function () {<br />' + 
			'&nbsp;&nbsp;&nbsp;&nbsp;return&nbsp;"forCsTypeTiers="&nbsp;+&nbsp;$(\'#typeTiers\').val();<br />' + 
			'}�</code>',
			param: 'un nom de fonction javascipt d�fini dans la page, ou une fonction anonyme d�finissant les pram�tres dynamiques (d�finis � chaque recherche par la m�thode) de la recherche'
		},
		wantValueCache : {
			desc: 'D�fini si les valeurs pr�c�dantes du widget doivent �tre mises en m�moire, et retourn�es si aucune nouvelle s�lection n\'a �t� faite.',
			param: '<code>false</code>(par d�faut), ou <code>true</code>'
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
	 *   <li>AJAX: tous ces fonctions se lancent � la fin de la fonction dans AJAX</li>
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
