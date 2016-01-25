/**
 * @author DMA
 */
globazNotation.adresse = {

	author: 'DMA',
	forTagHtml: 'div',
	type: globazNotation.typesNotation.TECHNICAL_NOTATION,

	description: "Cet objet permet de gérer les adresses. Il se base sur le service des adresses pour exécuter l'auto-complétion <br />" + 
				"Cette notation s'utilise sur un élément div et cette élément doit contenir  différents inputs de type hidden<br />" + 
				"Chaque élément hidden doit avoir un attribut class qui indique qu'elle sera le paramètre qui sera setter, il faut utiliser la même notation du widget<br />" + 
				"Pour utiliser cette notation sur un read en ajax, il faut peupler l'attribut defaultvalue sur l'élément qui contient la notation <br />" + 
				" <code>Ex: $('#adress.....').attr('defaultvalue',$data.find('adresse').text())</code> <br/><br/>" + "Classes possibles selon le service/manager : <br/>" + 
				"<div style=\"width:95%;display:block;\">" + 
					"<div style=\"width:45%;float:left;\">" + 
						"<h2>Services</h2>" + 
						"<ul class=\"liWithStyle\">" + 
							"<li>" + "<b>findAdressePaiement</b>" + 
								"<ul>" + 
									"<li>cs(tiersBeneficiaire.tiers.typeTiers)</li>" + 
									"<li>cs(tiersBeneficiaire.tiers.titreTiers)</li>" + 
									"<li>adressePaiement.numCompteBancaire</li>" + 
									"<li>adressePaiement.numCcp</li>" + 
									"<li>avoirPaiement.idExterne</li>" + 
								"<li>avoirPaiement.idTiers</li>" + 
									"<li>avoirPaiement.idApplication</li>" + 
									"<li>tiers.designation1</li>" + 
									"<li>tiers.designation2</li>" + 
									"<li>localite.localite</li>" +
									"<li>localite.numPostal</li>" + 
								"<li>adresse.rue</li>" + 
									"<li>adresse.numeroRue</li>" +
								"</ul>" + 
							"</li>" + "" +
							"<li>" + "<b>findAdresse</b>" + 
								"<ul>" + 
									"<li>cs(tiers.tiers.typeTiers)</li>" + 
									"<li>cs(tiers.tiers.titreTiers)</li>" + 
									"<li>avoirAdresse.idExterne</li>" +
									"<li>avoirAdresse.idTiers</li>" + 
									"<li>avoirAdresse.idApplication</li>" + 
									"<li>avoirAdresse.idAdresseIntUnique</li>" + 
									"<li>avoirAdresse.idAdresseInterne</li>" + 
									"<li>tiers.tiers.designation1</li>" + 
									"<li>tiers.tiers.designation2</li>" + 
									"<li>localite.localite</li>" + 
									"<li>localite.numPostal</li>" +
									"<li>adresse.rue</li>" + 
									"<li>adresse.numeroRue</li>" + 
								"</ul>" + 
							"</li>" + 
						"</ul>" + 
					"</div>" + 
					"<div style=\"width:45%;display:inline-block;\">" + 
						"<h2>Managers</h2>" + 
				"<ul class=\"liWithStyle\">" +
					"<li>" + "<b>findAdressePaiementManager</b>" + 
						"<ul>" + 
							"<li>cs(tiersBeneficiaire.tiers.typeTiers)</li>" + 
							"<li>cs(tiersBeneficiaire.tiers.titreTiers)</li>" + 
							"<li>adressePaiement.numCompteBancaire</li>" +
							"<li>adressePaiement.numCcp</li>" + 
							"<li>avoirPaiement.idExterne</li>" + 
						"<li>avoirPaiement.idTiers</li>" +
							"<li>avoirPaiement.idApplication</li>" +
							"<li>tiers.designation1</li>" + 
							"<li>tiers.designation2</li>" + 
							"<li>localite.localite</li>" +
							"<li>localite.numPostal</li>" +
							"<li>adresse.rue</li>" + 
							"<li>adresse.numeroRue</li>" + 
						"</ul>" + 
					"</li>" + 
					"<li>" + "<b>findAdresseManager</b>" +
						"<ul>" + 
							"<li>cs(tiers.tiers.typeTiers)</li>" + 
							"<li>cs(tiers.tiers.titreTiers)</li>" + 
							"<li>avoirAdresse.idExterne</li>" + 
							"<li>avoirAdresse.idTiers</li>" + 
							"<li>avoirAdresse.idApplication</li>" + 
							"<li>avoirAdresse.idAdresseIntUnique</li>" + 
							"<li>avoirAdresse.idAdresseInterne</li>" + 
							"<li>tiers.tiers.designation1</li>" +
							"<li>tiers.tiers.designation2</li>" + 
							"<li>localite.localite</li>" + 
							"<li>localite.numPostal</li>" +
							"<li>adresse.rue</li>" + 
							"<li>adresse.numeroRue</li>" +
						"</ul>" + 
					"</li>" + 
				"</ul>" + 
			"</div>" + 
		"</div>",

	descriptionOptions: {
		mandatory: {
			desc: "Test si l'élément est obligatoire",
			param: "true, false(default)"
		},
		addSymboleMandatory: {
			desc: "Ajoute le symoble *. Si le paramètre mandatory est à true ce paramètre n'est pas utilisé",
			param: "true|false(default)"
		},
		service: {
			desc: "Indique le service(fonction) qu'il faut appeler. Cette option est obligatoire. Les managers sont là pour une utilisation dans l'ancien framework",
			param: "findAdressePaiement|findAdresse|findAdressePaiementManager|findAdresseManager"
		},
		defaultvalue: {
			desc: "Valeur par défaut de l'adresse. ATTENTION les retours à la lignes sont remplacés par des balises html. <br />" + "Afin de ne pas avoir de problème il est préférable d'utiliser '¦'pour indique que l'on traite une valeur complexe<br />" + "EX: ¦Mon addresse...¦",
			param: "String"
		}
	},
	
	

	/**
	 * Paramètre de le l'objet qui vont être pour son initialisation Cet élément est obligatoire. Si pas d'option, il est créer, mais vide (options:{})
	 */
	options: {
		mandatory: false,
		addSymboleMandatory: false,
		service: '',
		defaultvalue: '',
		initThreadContext: false
	},
	
	
	$zoneAdresse: null,
	$inputAutocomplete: null,
	$divMultiWidgetManager: null,
	t_dataInitAutoComplete: null,
	$bouttonSearchAdresse: null,
	
	bindEvent: {
		ajaxDisableEnableInput: function () {
			var b_disabled = this.$zoneAdresse.find(".bouttonSearchAdresse").prop("disabled");
			this.disableEnable(b_disabled);
		},
		
		btnUpdate: function () {
			if(typeof mainFormIsLocked != "undefined"){
				this.disableEnable(mainFormIsLocked);
			} else {
				this.disableEnable(false);
			}
		}
	},
	
	/**
	 * Cette fonction est obligatoire car c'est elle qui va initialiser l'objet
	 */
	init: function ($elementToPutObject) {

		this.$elementToPutObject.addClass('adresseZone');
		this.$zoneAdresse = $("<div class='adresseAffichee'><div class='adresse'>&nbsp;</div></div>");

		switch (this.options.service) {
		case 'findAdresse':
		case 'findAdressePaiement':
			this.initDataForAutoCompleteService();

			this.$zoneAdresse.appendTo(this.$elementToPutObject);

			this.addButton();
			this.addAutoCompleteService();
			this.initElementToPutObjectAndAddEvent();
			this.addDefaultValue();
			this.bindAdresseEvent();
			break;
		case 'findAdresseManager':
		case 'findAdressePaiementManager':
			this.$divMultiWidgetManager = $('<div/>');
			this.initDataForAutoCompleteManager();

			this.$zoneAdresse.appendTo(this.$elementToPutObject);

			$.globalEval('var langages_adresse = "Nom Prénom et Iban, NSS et Affilié, Code d\'Administration";');

			this.$divMultiWidgetManager.attr('data-g-multiwidgets', 'languages:langages_adresse');
			this.$divMultiWidgetManager.addClass('multiWidgets');
			this.$divMultiWidgetManager.appendTo(this.$zoneAdresse);

			notationManager.addObjToElement(this.$divMultiWidgetManager.get(0));

			this.$divMultiWidgetManager.find('.jadeAutocompleteAjax').css({
				'float': 'none',
				'display': 'inline'
			}).hide().first().show();

			this.addButtonForManager();
			this.initElementToPutObjectAndAddEventForManager();
			break;
		default:
			this.putFormattedError('Service/Manager not founded', this.options.service, "Le service/manager passé en paramètre n'est pas géré par cet objet.<br/>Veuillez vous référer à la documentation");
			break;
		}
		
		if(typeof mainFormIsLocked != "undefined"){
			this.disableEnable(mainFormIsLocked);
		} else {
			this.disableEnable(false);
		}
		
		
	},

	disableEnable: function (isDisabled) {
		var $boutton = this.$zoneAdresse.find(".bouttonSearchAdresse");
		if(!isDisabled) {
			$boutton.prop("disabled",false);
			$boutton.prop("readonly",false);
		} else {
			$boutton.prop("disabled",true);
			$boutton.prop("readonly",true);
		}
	},
	
	initElementToPutObjectAndAddEvent: function () {
		var that = this;
		this.$inputAutocomplete.hide();
		this.$inputAutocomplete.blur(function () {
			that.$inputAutocomplete.hide();
			that.$inputAutocomplete.val('');
		});
	},

	initElementToPutObjectAndAddEventForManager: function () {
		var that = this;
		this.$divMultiWidgetManager.hide();
		this.$divMultiWidgetManager.blur(function () {
			that.$divMultiWidgetManager.hide();
			that.$divMultiWidgetManager.children('input').val('');
		});
		
	},

	addDefaultValue: function () {
		var html = '';
		if (!this.utils.isEmpty(this.options.defaultvalue)) {
			html = this.options.defaultvalue.replace(/[\r\n]/g, '<br />');
			this.$zoneAdresse.find('.adresse').html(html);
		}
	},

	addButton: function () {
		var that = this;
		$('<input/>', {
			click: function () {
				that.$inputAutocomplete.show();
				that.$inputAutocomplete.focus();
			},
			value: '...',
			type: 'button',
			"class": 'bouttonSearchAdresse'
		}).appendTo(this.$zoneAdresse);
		
		
	},

	addButtonForManager: function () {
		var that = this;
		$('<input/>', {
			click: function () {
				that.$divMultiWidgetManager.show();
				that.$divMultiWidgetManager.children('input:visible').focus();
			},
			value: '...',
			type: 'button',
			"class": 'bouttonSearchAdresse'
		}).appendTo(this.$zoneAdresse);
	},

	toDateInStringJadeFormate: function () {
		return this.utils.date.convertJSDateToGlobazStringDateFormat(new Date());
	},

	initDataForAutoComplete: function () {

	},

	initDataForAutoCompleteManager: function () {
		var that = this;
		var data = {
			s_infos: null,
			s_display: null,
			t_labelSearch: null,
			s_cstCriterias: "",
			s_searchWith: null,
			f_affiche: function () {
			}
		};
		switch (this.options.service) {
		case 'findAdresseManager':
			this.putFormattedError('Not yet implemented', this.options.service, "Pas encore implémenté");
			break;
		case 'findAdressePaiementManager':
			// données communes
			data.s_infos = 'idTiers,' + 'idTiersBanque,' + 'idAvoirPaiementUnique,' + 'idApplication,' + 'idAdressePaiement,' + 'dateDebutRelation,' + 'dateFinRelation,' + 'idExterneAvoirPaiement,' + 'idPaysPaiement,' + 'designation1_banque,' + 'designation2_banque,' + 'ligneAdresse1_banque,' + 'ligneAdresse2_banque,' + 'ligneAdresse3_banque,' + 'ligneAdresse4_banque,' + 'clearing,' + 'newClearing,' + 'swift,' + 'ccp_banque,' + 'iban,' + 'rue_banque,' + 'numero_banque,' + 'casePostale_banque,' + 'compte,' + 'code,' + 'localite_banque,' + 'npa_banque,' + 'pays_banque,' + 'paysIso_banque,' + 'ccp,' + 'idAdresseBenef,' + 'cs(titre_tiers),' + 'attention_tiers,' + 'designation1_tiers,' + 'designation2_tiers,' + 'designation3_tiers,' + 'designation4_tiers,' + 'titre_adr,' + 'designation1_adr,' + 'designation2_adr,' + 'designation3_adr,' + 'designation4_adr,' + 'rue,' + 'numero,' + 'casePostale,' + 'idlocalite,' + 'localite,' + 'npa,' + 'pays,' + 'paysIso,' + 'nss,' + 'nomTiers1,' + 'nomTiers2,' + 'idPays,' + 'langue';
			data.s_display = '#{designation1_tiers} #{designation2_tiers} #{localite} #{npa} #{rue} #{numero}';
			data.s_classService = null;
			data.s_methodService = null;
			data.s_classManager = this.options.service;

			// données propres à chaque widget du multiwidget

			// par nom, prénom, IBAN
			data.s_searchWith = 'forDesignation1BeneficiaireLike,' + 'forDesignation2BeneficiaireLike,' + 'forIbanLike';
			data.t_labelSearch = [that.i18n('nom'), that.i18n('prenom'), that.i18n('iban')];
			this.addAutoCompleteManager(data);

			// par NSS, N°Affilié
			data.s_searchWith = 'forNumAvsBeneficiaireLike,' + 'forNumAffilieBeneficiaireLike';
			data.t_labelSearch = [that.i18n('nss'), that.i18n('noaffilie')];
			this.addAutoCompleteManager(data);

			// par code d'administration
			data.s_searchWith = 'forCodeAdministration,';
			data.t_labelSearch = [that.i18n('codeadministration')];
			this.addAutoCompleteManager(data);

			break;
		default:
			this
					.putFormattedError('Manager not founded', this.options.service, "Le manager passé en paramètre n'est pas géré par cet objet.<br/>Veuillez vous référer à la documentation");
			break;
		}
	},

	initDataForAutoCompleteService: function () {
		var that = this;
		var data = {
			s_infos: null,
			s_display: null,
			t_labelSearch: null,
			s_cstCriterias: "",
			s_searchWith: null,
			f_affiche: function () {
			}
		};
		switch (this.options.service) {
		case 'findAdressePaiement':
			data.s_infos = 'cs(tiersBeneficiaire.tiers.typeTiers),' + 'cs(tiersBeneficiaire.tiers.titreTiers),' + 'adressePaiement.numCompteBancaire,' + 'adressePaiement.numCcp,' + 'avoirPaiement.idExterne,' + 'avoirPaiement.idTiers,' + 'avoirPaiement.idApplication,' + 'avoirPaiement.idAdrPmtIntUnique,' + 'tiers.designation1,' + 'tiers.designation2,' + 'localite.localite,' + 'localite.numPostal,' + 'adresse.rue,' + 'adresse.numeroRue';
			data.s_display = '<div style="padding-bottom:5px;">' + '<b> #{tiers.designation1} #{tiers.designation2}</b> #{localite.localite} #{localite.numPostal} #{adresse.rue} #{adresse.numeroRue} <br/>' + '<i> #{adressePaiement.numCompteBancaire} #{adressePaiement.numCcp}</i>' + '</div>';
			data.t_labelSearch = [that.i18n('nom'), that.i18n('prenom'), that.i18n('iban')];
			data.s_cstCriterias = 'forDate=' + that.toDateInStringJadeFormate() + ',whereKey=widgetSearch'; // supresionn de =>,whereKey=widgetSearch
			data.s_searchWith = 'forDesignation1BeneficiaireLike,' + 'forDesignation2BeneficiaireLike,' + 'forNumCompteBancaireLike';
			data.s_classService = 'ch.globaz.pyxis.business.service.AdresseService';
			data.s_methodService = this.options.service;
			break;
		case 'findAdresse':
			data.s_infos = 'cs(tiers.tiers.typeTiers),' + 'cs(tiers.tiers.titreTiers),' + 'avoirAdresse.idExterne,' + 'avoirAdresse.idTiers,' + 'avoirAdresse.idApplication,' + 'avoirAdresse.idAdresseIntUnique,' + 'avoirAdresse.idAdresseInterne,' + 'tiers.tiers.designation1,' + 'tiers.tiers.designation2,' + 'localite.localite,' + 'localite.numPostal,' + 'adresse.rue,' + 'adresse.numeroRue';
			data.s_display = '#{tiers.tiers.designation1} #{tiers.tiers.designation2} #{localite.localite} #{localite.numPostal} #{adresse.rue} #{adresse.numeroRue}';
			data.t_labelSearch = [that.i18n('nom'), that.i18n('prenom'), that.i18n('localite'), that.i18n('npa')];
			data.s_searchWith = 'forDesignation1Like,' + 'forDesignation2Like,' + 'forLocaliteUpperLike,' + 'forNpaLike';
			data.s_classService = 'ch.globaz.pyxis.business.service.AdresseService';
			data.s_methodService = this.options.service;
			break;
		default:
			this
					.putFormattedError('Service not founded', this.options.service, "Le service passé en paramétre n'est pas gérée par cette objet. <br> Veuillez vous référer à la documentation");
			break;
		}

		this.t_dataInitAutoComplete = data;
	},

	addAutoCompleteManager: function (t_data) {
		var that = this;

		var $inputManager = $('<input id="widgetAdresse' + this.utils.generateUniqueNumber() + '" type="text" />');
		this.$divMultiWidgetManager.append($inputManager);
		this.utils.input.addAllPropertyFromUtil(this.options, $inputManager);
		$inputManager.addClass('jadeAutocompleteAjax widgetAdresse' + t_data.t_labelSearch[0]);
		$inputManager
				.globazWidget(t_data.s_classService, t_data.s_methodService, t_data.s_classManager, t_data.s_searchWith, t_data.s_cstCriterias, t_data.s_display, t_data.s_infos, t_data.t_labelSearch, '20', function (element) {
					that.jsOnSelect(element);
				}, 3);
	},

	addAutoCompleteService: function () {
		var that = this;
		var data = this.t_dataInitAutoComplete;
		this.$inputAutocomplete = $('<input type="text"/>');
		this.$zoneAdresse.append(this.$inputAutocomplete);
		this.utils.input.addAllPropertyFromUtil(this.options, this.$inputAutocomplete);
		this.$inputAutocomplete.addClass('jadeAutocompleteAjax');
		this.$inputAutocomplete
				.globazWidget(data.s_classService, data.s_methodService, data.s_classManager, data.s_searchWith, data.s_cstCriterias, data.s_display, data.s_infos, data.t_labelSearch, '20', function (element) {
					that.jsOnSelect(element);
				}, 3, null, null, this.options.initThreadContext);
	},

	createAdresse: function ($element) {
		var chaine = "";
		switch (this.options.service) {
		case 'findAdressePaiement':
			chaine = $element.attr('adressePaiement.numCompteBancaire') + ' ' + $element.attr('adressePaiement.numCcp');
			chaine += '<br />';
			chaine += $element.attr('tiersBeneficiaire.tiers.titreTiers');
			chaine += '<br />';
			chaine += $element.attr('tiers.designation1') + ' ' + $element.attr('tiers.designation2');
			chaine += '<br />';
			chaine += $element.attr('localite.numPostal') + ' ' + $element.attr('localite.localite');
			chaine += '<br />';
			break;
		case 'findAdresse':
			chaine = $element.attr('tiers.tiers.titreTiers');
			chaine += '<br />';
			chaine += $element.attr('tiers.tiers.designation1') + ' ' + $element.attr('tiers.tiers.designation2');
			chaine += '<br />';
			chaine += $element.attr('adresse.rue') + ' ' + $element.attr('adresse.noRue');
			chaine += '<br />';
			chaine += $element.attr('localite.numPostal') + ' ' + $element.attr('localite.localite');
			chaine += '<br />';
			break;
		}
		return chaine;
	},

	bindAdresseEvent: function () {
		var that = this;
		this.$elementToPutObject.bind('change', function () {
			if (that.$elementToPutObject.has('[name=defaultvalue]')) {
				var s_adresse = that.$elementToPutObject.attr('defaultvalue');
				that.options.defaultvalue = s_adresse;
			}
			that.addDefaultValue();
		});
	},

	addValueToInputHidden: function ($element) {
		this.$elementToPutObject.find('input').each(function () {
			var $this = $(this), value = $element.attr(this.className);
			if (value !== undefined) {
				this.value = value;
				$this.change();
			}
		});
	},

	jsOnSelect: function (element) {
		
		var $element = $(element);
		this.addValueToInputHidden($(element));
		this.$zoneAdresse.find('.adresse').html(this.createAdresse($element));
	}
};
