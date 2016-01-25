globazNotation.processbutton = {
	
	author: 'SCE',
	forTagHtml: 'img',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Notation permattant d'appliquer un comportement � un bouton."+
	             "Typiquement un bouton servant de lancement de process. Un gi anim� (tyoe chargement) remplacera le bouton une fois l'action lanc�",

	descriptionOptions: {
	},


	options: {
		imgFile: '',
		imgFileLock: 'add_lock.png'
	},
	
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

}