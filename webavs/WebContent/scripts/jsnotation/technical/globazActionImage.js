globazNotation.actionimage = {

	author: 'SCE',
	forTagHtml: 'img',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "<b>Cet objet est en d�veloppement!</b> Une sp�cification claire et pr�cises des besoins sera n�cessaire! Il est pr�vu, dans cet objet, de g�rer une image avec un action li�e. Cela afin de g�rer, entre autres, le comportement des formulaires (gris� ou pas), et �galement les actions li�es � cette image (click, hover, etc..), � suivre, donc! <br/>Objet � appliquer � une image servant d'action. Exemple: une image de suppression, d'ajout, etc...",

	descriptionOptions: {
		imgFile: {
			desc: "Fichier image qui sera utilis�. Doit �tre dans le r�pertoire <b>jsnotation/imgs</b>",
			param: "chaine de caractere, vide par defaut, valeur obligatoire!",
			mandatory: true

		}
	},

	/**
	 * Parm�tre de le l'objet qui vont �tre pour son initilisation
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
		// check si fichier image sp�cifi�
		// if(this.utils.isEmpty(this.options.imgFile)){
		// this.putFormattedError('Erreur js dans cette action ','init',', le
		// nom du fichier image n a pas �t� sp�cifi�');
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