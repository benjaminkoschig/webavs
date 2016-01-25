globazNotation.actionimage = {

	author: 'SCE',
	forTagHtml: 'img',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "<b>Cet objet est en développement!</b> Une spécification claire et précises des besoins sera nécessaire! Il est prévu, dans cet objet, de gérer une image avec un action liée. Cela afin de gérer, entre autres, le comportement des formulaires (grisé ou pas), et également les actions liées à cette image (click, hover, etc..), à suivre, donc! <br/>Objet à appliquer à une image servant d'action. Exemple: une image de suppression, d'ajout, etc...",

	descriptionOptions: {
		imgFile: {
			desc: "Fichier image qui sera utilisé. Doit être dans le répertoire <b>jsnotation/imgs</b>",
			param: "chaine de caractere, vide par defaut, valeur obligatoire!",
			mandatory: true

		}
	},

	/**
	 * Parmétre de le l'objet qui vont être pour son initilisation
	 */
	options: {
		imgFile: '',
		imgFileLock: 'add_lock.png'
	},

	$imgTag: null,// balise img
	s_imgFolderName: '/scripts/jsnotation/imgs/',// Dossier image par defaut
	// jsnotation

	bindEvent: {
		// sur clic update
		btnUpdate: function () {
			this.lockMode();
		}
	},
	/**
	 * Cette fonction est obligatoire car c'est elle qui va initiliser l'objet
	 */
	init: function ($elementToPutObject) {
		// check si fichier image spécifié
		// if(this.utils.isEmpty(this.options.imgFile)){
		// this.putFormattedError('Erreur js dans cette action ','init',', le
		// nom du fichier image n a pas été spécifié');
		// }
		this.$imgTag = $elementToPutObject;
		this.buildImgAction();
	},
	buildImgAction: function () {
		alert(this.options.imgFile);
		this.$imgTag.attr('src', this.s_contextUrl + this.s_imgFolderName + this.options.imgFile);
		alert(this.$imgTag.src);
	},
	lockMode: function () {
		this.$imgTag.attr('src', this.s_contextUrl + this.s_imgFolderName + this.options.imgFileLock);
		this.$imgTag.css('filter', 'alpha(opacity=50)');
	}

}