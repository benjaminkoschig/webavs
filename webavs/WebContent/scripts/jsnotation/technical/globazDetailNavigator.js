globazNotation.detailnavigator = {

	author: 'SCE',
	forTagHtml: 'div',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Objet à appliquer a un tag div, qui va générer un widget de navigation pour les pages de détail.<br /> Le fond gris à été rajouté à l'exemple, afin de se rendre compte du chamgement d'icònes lors de l'action hover.",

	descriptionOptions: {
		userAction: {
			desc: "Action a ajouter à l'url. Une valeur vide va lever une exception!",
			param: "chaine de caractere, vide par defaut, valeur obligatoire! <br/> ex: var action = 'pegasus?userAction='....''"
		},
		currentId: {
			desc: "Identifiant de la page couurante, doit etre present dans le lot de pagination. Une valeur vide va lever une exception",
			param: "chaine de caractere de l'identifant de l'entité courante, valeur obligatoire<br/> ex: var cId = 1;"
		},
		lotPagination: {
			desc: "Identifiants des pages participant à la navigation. Dans l'ordre de leurs apparitions, doit donc être ordré de la même manière a chaque rechargement de la page. Une valeur vide va lever une exception",
			param: "Tableau de valeur entiere contenant les id,<br/> ex: var tab = [1,2,3]"
		}
	},

	/**
	 * Parmétre de le l'objet qui vont être pour son initilisation Cet élément est obligatoire, Si pas d'option le crééer mais vide (options:{})
	 */
	options: {
		userAction: '',
		currentId: '',
		lotPagination: []
	},
	$mainDiv: '',// div principale ou l'objet a été setter
	$toRightArrow: '',// Image fleche next
	$toLeftArrow: '',// Image fleche previous
	$labelZone: '',// label du texte de la pagination
	s_imgFolderName: '/scripts/jsnotation/imgs/',
	s_imgLeftArrowName: 'toLeft.png',// nom du fichier image, fleche gauche
	s_imgRightArrowName: 'toRight.png',// nom du fichier image, fleche droite
	s_imgRightArrowHover: 'toRightHover.png',// suffixe pour l'image hover
	s_imgLeftArrowHover: 'toLeftHover.png',// suffixe pour l'image disabled
	s_imgLeftArrowDisable: 'toLeftDisable.png',
	s_imgRightArrowDisable: 'toRightDisable.png',
	bindEvent: {
		// sur clic update
		btnUpdate: function () {
			this.disablePaginationEvents();
		}
	},
	/**
	 * Cette fonction est obligatoire car c'est elle qui va initiliser l'objet
	 */
	init: function ($elementToPutObject) {

		// Check si erreur dans les params
		this.checkParams();
		// recup objet div
		this.$mainDiv = $elementToPutObject;
		this.buildDiv();
		this.initEvents();

		// Set label
		this.$labelZone.text(this.determineLabelZoneValue() + "/" + this.options.lotPagination.length);
	},
	/**
	 * Fonction qui construit l'interface visuel de la navigation, à savoir le label et les deux fleches de navigation
	 */
	buildDiv: function () {
		var _leftImgSrc = '';
		var _rightImgSrc = '';
		var leftImgSrc, rightImgSrc;
		// ajout du label
		var $label = $("<label>", {
			"class": "labelZone",
			text: "ok"
		});
		// Si une seule décision, blocage image fleches
		if (this.options.lotPagination.length === 1) {
			leftImgSrc = this.s_contextUrl + this.s_imgFolderName + this.s_imgLeftArrowDisable;
			rightImgSrc = this.s_contextUrl + this.s_imgFolderName + this.s_imgRightArrowDisable;
		} else {
			if (this.determineNext() !== 0) {
				rightImgSrc = this.s_contextUrl + this.s_imgFolderName + this.s_imgRightArrowName;
			} else {
				rightImgSrc = this.s_contextUrl + this.s_imgFolderName + this.s_imgRightArrowDisable;
			}
			if (this.determinePrevious() !== 0) {
				leftImgSrc = this.s_contextUrl + this.s_imgFolderName + this.s_imgLeftArrowName;
			} else {
				leftImgSrc = this.s_contextUrl + this.s_imgFolderName + this.s_imgLeftArrowDisable;
			}
		}

		// ajout des images
		var $toLeftArrow = $("<img>", {
			"class": "toLeftArrow",
			src: leftImgSrc
		});
		var $toRightArrow = $("<img>", {
			"class": "toRightArrow",
			src: rightImgSrc
		});
		this.$mainDiv.addClass('navigator');
		this.$mainDiv.append($toLeftArrow);
		this.$mainDiv.append($label);
		this.$mainDiv.append($toRightArrow);
		this.$toRightArrow = $toRightArrow;
		this.$toLeftArrow = $toLeftArrow;
		this.$labelZone = $label;
	},
	/**
	 * Fonction qui initialise les événements
	 */
	initEvents: function () {
		var that = this;

		if (this.determineNext() !== 0) {
			// evenemnets liés au fleches
			this.$toRightArrow.click(function () {
				var n_nextId = that.determineNext();
				if (n_nextId !== 0) {
					location.href = that.options.userAction + n_nextId + "&lotPagination=" + that.options.lotPagination;
				}
			});
			this.$toRightArrow.hover(function () {
				this.src = that.s_contextUrl + that.s_imgFolderName + that.s_imgRightArrowHover;
			}).mouseout(function () {
				this.src = that.s_contextUrl + that.s_imgFolderName + that.s_imgRightArrowName;
			});
		}

		if (this.determinePrevious() !== 0) {
			this.$toLeftArrow.click(function () {
				var n_previousId = that.determinePrevious();
				if (n_previousId !== 0) {
					location.href = that.options.userAction + n_previousId + "&lotPagination=" + that.options.lotPagination;
				}
			});

			this.$toLeftArrow.hover(function () {
				this.src = that.s_contextUrl + that.s_imgFolderName + that.s_imgLeftArrowHover;
			}).mouseout(function () {
				this.src = that.s_contextUrl + that.s_imgFolderName + that.s_imgLeftArrowName;
			});
		}

	},
	/**
	 * fonctions qui detremine le prochain id de la pagination
	 */
	determineNext: function () {
		var b_currentIdFind = false;
		var t_tab = this.options.lotPagination;
		var n_len = t_tab.length;

		// iteration sur les boucles
		for (var cpt = 0; cpt < n_len; cpt++) {
			// alert("cpt: "+cpt+" ,tab: "+decId[cpt]);
			if (b_currentIdFind) {
				return t_tab[cpt];
			}
			if (Number(t_tab[cpt]) === this.options.currentId) {
				b_currentIdFind = true;
			}
		}
		return 0;
	},
	// Fonction qui determine si il y a une page a afficher avant
	determinePrevious: function () {
		var b_hasPrevious = false;
		var t_tab = this.options.lotPagination;
		var n_len = t_tab.length;

		// iteration sur les boucles
		for (var cpt = 0; cpt < n_len; cpt++) {
			if (Number(t_tab[cpt]) === this.options.currentId && b_hasPrevious) {
				return t_tab[cpt - 1];
			} else {
				b_hasPrevious = true;
			}
		}
		return 0;
	},
	/**
	 * Methode qui determine la valeur du label (compteur)
	 * 
	 * @returns {Number}
	 */
	determineLabelZoneValue: function () {
		var t_tab = this.options.lotPagination;
		// iteration sur les boucles
		var n_len = t_tab.length;
		for (var cpt = 0; cpt < n_len; cpt++) {
			if (Number(t_tab[cpt]) === this.options.currentId) {
				return cpt + 1;
			}
		}
	},
	/**
	 * Methode qui va passer en revue les options obligatoire et va lever une exception si il y a un probleme avec les options passées en paramètres.
	 */
	checkParams: function () {
		// check si le tableau > 0
		if (this.options.lotPagination.length < 1) {
			this.putFormattedError('Erreur js dans cette action ', 'init', ', le tableau de pagination est vide, ou non défini');
		}
		// check si userAction est vide
		if (this.utils.isEmpty(this.options.userAction)) {
			this.putFormattedError('Erreur js dans cette action ', 'init', ', l action a appliquer a l url est vide ou non défini');
		}
		// si currentId diff de 0, ou pas nombre
		if (this.utils.isEmpty(this.options.currentId) || typeof this.options.currentId !== 'number') {
			this.putFormattedError('Erreur js dans cette action ', 'init', ', l identifiant courant est vide ou non défini, ou du mauvais type');
		}
	},
	disablePaginationEvents: function () {

		this.$toRightArrow.unbind();
		this.$toLeftArrow.unbind();

		this.$toLeftArrow.attr('src', this.s_contextUrl + this.s_imgFolderName + this.s_imgLeftArrowDisable);
		this.$toRightArrow.attr('src', this.s_contextUrl + this.s_imgFolderName + this.s_imgRightArrowDisable);
	}
};