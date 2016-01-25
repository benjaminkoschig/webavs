globazNotation.multiselect = {

	author: 'SCE',
	forTagHtml: 'select',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Objet � appliquer � un tag <b>select</b>, permettant de g�rer des listes multiniveaux.",

	descriptionOptions: {
		dataList: {
			desc: "Tableau JSON contenant les objets et leur arborescsnece",
			param: ""
		},
		initValues: {
			desc: "objets json, contenant les valeurs � afficher � l'initialisation de la page",
			param: ""
		},
		subLevelLibelle: {
			desc: "Sp�cifie le label a aplliqu� � la liste de niveau 2. Si vide ou non d�fini, on prendra la valeur <i>childrensLibelle</i> de chaque option de niveau sup�rieur. Si rien n'est d�fini, la valeur est vide",
			param: "chaine de carat�re, vide par defaut"
		},
		subLevelId: {
			desc: "id de la sous liste li�s � la liste maitre. Si pas sp�cifi�, id automatique",
			param: "chaine de caract�re, vide par d�faut"
		}
	},

	/**
	 * Parm�tre de le l'objet qui vont �tre pour son initilisation Cet �l�ment est obligatoire, Si pas d'option le cr��er mais vide (options:{})
	 */
	options: {
		dataList: [],
		subLevelLibelle: '',
		initValues: {},
		subLevelId: ''

	},
	vars: {
		// objez jquery sous liste
		$mainLevelList: null,
		$subLevelList: null,
		$subListLabel: null
	},
	initVars: function () {
		this.vars.$mainLevelList = this.$elementToPutObject;
		this.vars.$subLevelList = this.$elementToPutObject.nextAll('select').eq(0);
		// Gestion sous liste
		var s_subLevelId = this.options.subLevelId;
		// si l'id sous niveau pas pass� en param�tre
		if (globazNotation.utils.isEmpty(s_subLevelId)) {
			var s_subLevelIdInTag = this.$elementToPutObject.nextAll('select').eq(0).attr('id');
			var s_mainLevelId = this.$elementToPutObject.attr('id');
			// recherhe id dans structure de base
			if (globazNotation.utils.isEmpty(s_subLevelIdInTag)) {
				// Si pas id dans la page et pas en param on g�n�re
				s_subLevelId = s_mainLevelId + "_sub";
			} else {
				s_subLevelId = s_subLevelIdInTag;
			}
		} else {
			this.vars.$subLevelList = $('#' + s_subLevelId);
			this.vars.$subListLabel = this.vars.$subLevelList.prevAll('label').eq(0);

			if (this.vars.$subLevelList.length === 0) {
				this.putFormattedError('Erreur js dans cette action ', 'init', "L'identifiant pass� en param�tre pour la sous liste n'existe pas");
			}
		}
		this.vars.$subLevelList.hide();
	},
	/**
	 * Cette fonction est obligatoire car c'est elle qui va initiliser l'objet
	 */
	init: function ($elementToPutObject) {
		var that = this;

		// Si data list vide, null ou non sp�cifi�
		if (globazNotation.utils.isEmpty(this.options.dataList)) {
			this.putFormattedError('Erreur js dans cette action ', 'init', "L'objet json devant contenir les donn�es n'est pas d�fini!");
		}

		// traitement des id des listes
		var s_mainLevelId = $elementToPutObject.attr('id');

		if (globazNotation.utils.isEmpty(s_mainLevelId)) {
			this.putFormattedError('Erreur js dans cette action ', 'init', "La liste maitre doit avoir un id sp�cifi�.");
		}
		this.initVars();

		this.generateFirstLevelList();
	},

	/*
	 * G�n�ration de la liste principale
	 */
	generateFirstLevelList: function () {
		var that = this;
		var options = '';
		// g�n�ration des valeurs liste de premier niveau
		$.each(this.options.dataList, function (i, ligne) {
			if (that.options.initValues.masterValue === ligne.value) {
				options += '<option selected="selected" value="' + ligne.value + '">' + ligne.libelle + '</option>';
				if (!globazNotation.utils.isEmpty(ligne.childrens)) {
					that.generateSubList(1, ligne.childrens, that.returnChildrensLibelle(ligne.value));
				}
			} else {
				options += '<option  value="' + ligne.value + '">' + ligne.libelle + '</option>';
			}

		});
		// ajout des options
		that.vars.$mainLevelList.append(options);
		that.vars.$mainLevelList.after('<br/>');
		// events
		that.addEvents();
	},

	/**
	 * Retourne la liste des enfants, sil il y en a, 0 dans le cas contraire
	 * 
	 * @param childrens
	 * @returns
	 */
	returnChildrens: function (childrenValue) {

		var ret = '';
		// Iteration premier niveau
		$.each(this.options.dataList, function (i, ligne) {

			if (ligne.value === childrenValue) {
				ret = ligne.childrens;
			}
		});

		return ret;
	},

	/*
	 * retourne le libelle des enfants
	 */
	returnChildrensLibelle: function (childrenValue) {
		var retLibelle = '';

		if (!globazNotation.utils.isEmpty(this.options.subLevelLibelle)) {
			retLibelle = this.options.subLevelLibelle;
		} else {

			// Iteration premier niveau
			$.each(this.options.dataList, function (i, ligne) {

				if (ligne.value === childrenValue) {
					retLibelle = ligne.childrensLibelle;
				}
			});
		}

		return retLibelle;
	},

	/**
	 * Efface la sous liste et le label associ�
	 */
	clearSubLevelZone: function () {
		// on supprime la sous liste et son label
		this.vars.$subLevelList.empty().hide();
		this.vars.$subListLabel.hide();
	},

	/**
	 * Ajout des �ve�nements
	 */
	addEvents: function () {
		var that = this, selectedValue = null;
		that.vars.$mainLevelList.change(function () {
			that.clearSubLevelZone();
			// recup id selected
			selectedValue = $(this).find(':selected').attr('value');
			// g�n�ration de la sous liste
			var childrens = that.returnChildrens(selectedValue);
			var childrensLibelle = that.returnChildrensLibelle(selectedValue);

			if (!globazNotation.utils.isEmpty(childrens)) {
				that.generateSubList(0, childrens, childrensLibelle);
			}
		});
	},

	/*
	 * Remplissage et affichage de la liste de niveau 2
	 */
	generateSubList: function (isInit, childrens, libelle) {
		var that = this;
		var subOptions = '';

		$.each(childrens, function (i, ligne) {

			if (that.options.initValues.childValue === ligne.value && isInit) {
				subOptions += '<option selected="selected" value="' + ligne.value + '">' + ligne.libelle + '</option>';
			} else {
				subOptions += '<option value="' + ligne.value + '">' + ligne.libelle + '</option>';
			}
		});

		that.vars.$subLevelList.append(subOptions).show();
		that.vars.$subListLabel.html(libelle).show();

		// $_selectObj.after($_subSelect).after(that.returnSubLevelLibelleAsSpan(libelle)).after('<br/>');
	}
};