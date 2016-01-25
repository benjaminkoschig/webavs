globazNotation.deallaterperiod = {

	author: 'SCE',
	forTagHtml: 'tr,th',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Objet � appliquer aux entete de tableau pour les p�riodes. Affichant une icone si la periode de la donn�e n'est pas la periode courante. Cet objet ne fonctionne que sur un �cran Javascript avec plusieurs donn�es financi�res. Il recherche l'attribut [header] de la ligne. Qui correspond � la derni�re p�riode de la donn�es financi�re.",

	descriptionOptions: {
		dateToCompar: {
			desc:"Permet de d�finir la date � utiliser pour la comparaison. Si aucune date n'est d�finit ou utilise la date du jour",
			param:"Date au format dd.mm.yyyy ou mm.yyyy",
			mandatory: false
		}
	},

	options: {
		dateToCompar:null
	},
	
	imgSrc: '',
	lang: 'fr',// langue,
	imgName: 'fortune_period.png',
	imgFolderName: '/images/',
	$elementToPutObject: '',// objet jquery td
	$table: '',// objet jquery table
	tdIndex: '',// index de la cellule dans le tableau
	dateToCompar:null,
	// gestion evenement
	bindEvent: {
		ajaxUpdateComplete: function () {
			this.dealLine();
		}
	},
	/**
	 * Cette fonction est obligatoire car c'est elle qui va initiliser l'objet
	 */
	init: function ($elementToPutObject) {
		// recup objet td
		this.$elementToPutObject = $elementToPutObject;
		// Set chemin image
		this.imgSrc = this.s_contextUrl + this.imgFolderName + this.imgName;
		// Recup tableau
		this.$table = this.$elementToPutObject.closest('table', this.$elementToPutObject);
		// Recup index cellule
		this.tdIndex = this.$elementToPutObject.parent("tr").children().index(this.$elementToPutObject);
			
		if(this.options.dateToCompar === null) {
			this.dateToCompar = this.utils.date.convertJSDateToGlobazStringDateFormat(new Date());
		} else {
			this.dateToCompar = this.utils.date.normalizeFullGlobazDate(this.options.dateToCompar);
		}	
		// lancement travaille sur les lignes et cellules
		this.dealLine();		
	},
	/**
	 * Fonction qui itere sur les header des donn�es et parcours ensuite chaque cellule p�riode �pour lui afficher l'icone si la date n'est pas courante
	 */
	dealLine: function () {
		var that = this;
		// chaque ligne, sauf la premiere
		this.$table.find('tr:gt(0)').filter('[header=true]').each(function () {
			// chaque colone td
			$(this).find('td:eq(' + that.tdIndex + ')').each(function () {
				// Rceup valeur dans td et date globaz
				var period = $(this).text().split('-');
				// Suppression espace blanc avant tiret
				var periodToTest = period[0].slice(0, 10);
				// Si date de la donnee apres now
				if (that.utils.date.isDateAfter(periodToTest,that.dateToCompar)) {
					$(this).prepend('<img style="vertical-align:middle;margin-right:5px;" src="' + that.imgSrc + '" title="' + that.i18n('periodeDansLeFutur') + '"></img>');
				}
			});
		});
	}
};