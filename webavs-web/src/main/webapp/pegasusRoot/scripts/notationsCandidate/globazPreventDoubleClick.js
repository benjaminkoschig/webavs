globazNotation.preventdoubleclick = {

	$element:'',
	IMG_LOADER_PATH:'./images/loader_horizontal.gif',
	author: 'SCE',
	forTagHtml: 'button,input',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: '<b>Cet objet est en développement!</b> Une spécification claire et précises des besoins sera nécessaire!' +
	'Il est prévu, dans cet objet, de gérer le one click, avec un indicateur visuel, à savoir: <br/>' +
	'1. on va cacher le bouton <br/>' +
	'2. une image, définie par défaut, va indiquer à l\'utilisateur que le traitement est en cours <br/>' +
	'3. un label sera passé <br/>' +
	'En outre, la fonction one sera utilisé pour s\'assurer que le click ne sera pris en compte qu\'une fois',

	
	descriptionOptions: {
		imgFile: {
			desc: 'Fichier image qui sera utilisé. Doit être dans le répertoire <b>jsnotation/imgs</b>',
			param: 'chaine de caractere, par défaut image standard',
			mandatory: false
		},
		
		label: {
			desc: 'Texte qui sera affiché au dessus de l\'image',
			param : 'chaine de caractère vide par défaut',
			mandatory: false
		},
		labelCssClass:{
			desc: 'classe css du label accompgnant l\'image',
			param:'Chaine de carctère, vide par défaut',
			mandatory:false
		}
	},

	/**
	 * Parmétre de le l'objet qui vont être pour son initilisation
	 */
	options: {
		imgFile: '',
		label: '',
		labelCssClass:''
		
	},


	/**
	 * Initialisation du composant
	 */
	init: function ($elementToPutObject) {
		var that = this;
		this.$element = $elementToPutObject;
		
		$elementToPutObject.one('click',function () {
			
			that.$element.after(that.displayWaitingMsg()).after('<br/>')
			.after(that.displayWaitingGif());
			
			that.$element.hide();
		});
	},
	
	displayWaitingGif : function () {
		var $img = $('<img/>',{
			src : this.IMG_LOADER_PATH
		});
		
		return $img;
	},
	
	displayWaitingMsg : function () {
		
		var $label = $('<label/>',{
			html:options.label,
			id:options.labelCssClass
		}); 
		return $label;
	}
	

}