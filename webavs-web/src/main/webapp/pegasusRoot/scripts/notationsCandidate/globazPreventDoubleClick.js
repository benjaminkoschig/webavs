globazNotation.preventdoubleclick = {

	$element:'',
	IMG_LOADER_PATH:'./images/loader_horizontal.gif',
	author: 'SCE',
	forTagHtml: 'button,input',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: '<b>Cet objet est en d�veloppement!</b> Une sp�cification claire et pr�cises des besoins sera n�cessaire!' +
	'Il est pr�vu, dans cet objet, de g�rer le one click, avec un indicateur visuel, � savoir: <br/>' +
	'1. on va cacher le bouton <br/>' +
	'2. une image, d�finie par d�faut, va indiquer � l\'utilisateur que le traitement est en cours <br/>' +
	'3. un label sera pass� <br/>' +
	'En outre, la fonction one sera utilis� pour s\'assurer que le click ne sera pris en compte qu\'une fois',

	
	descriptionOptions: {
		imgFile: {
			desc: 'Fichier image qui sera utilis�. Doit �tre dans le r�pertoire <b>jsnotation/imgs</b>',
			param: 'chaine de caractere, par d�faut image standard',
			mandatory: false
		},
		
		label: {
			desc: 'Texte qui sera affich� au dessus de l\'image',
			param : 'chaine de caract�re vide par d�faut',
			mandatory: false
		},
		labelCssClass:{
			desc: 'classe css du label accompgnant l\'image',
			param:'Chaine de carct�re, vide par d�faut',
			mandatory:false
		}
	},

	/**
	 * Parm�tre de le l'objet qui vont �tre pour son initilisation
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