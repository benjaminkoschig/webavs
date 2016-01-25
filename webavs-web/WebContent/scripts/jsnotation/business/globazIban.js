/**
 * @author BSC
 */
globazNotation.globalVariables.iban = {};
globazNotation.iban = {

	author: 'BSC',
	forTagHtml: 'input',
	type: globazNotation.typesNotation.BUSINESS_NOTATION,

	description: "Cette objet permet de générer un champ input de type IBAN. "+"<br /><br />" + 
				 "A la sortie du champ input, le contenu du champ est analysé de la manière suivante:"+"<br /><br />" +
				 "  1)Si la chaine de caractère commence par \"CH\" l'Iban sera validé, sinon aucune validation n’est effectuée."+"<br /><br />" +
				 "  2)Si l'Iban est valide, l'icône Iban valide est affiché et la banque liée à ce compte est recherchée à l'aide du n° de clearing."+"<br /><br />" +
				 "    Sinon l'icône Iban non valide est affichée.",

	descriptionOptions: {
		mandatory: {
			desc: "Test si élément est obligatoire",
			param: "true, false(default)"
		},
		addSymboleMandatory: {
			desc: "Ajoute le symoble *. Si le paramétre mandatory est à true ce paramétre n'est pas utilisé",
			param: "true(default)|false"
		},
		checkOnLoad: {
			desc:"Définit si une validation doit être effectuée après l'initialisation de l'objet.",
			param: "true(default)|false"
		}
	},

	/**
	 * Ce paramètre est facultatif.<br/> Il permet des lancer des fonctions sur différent types d'évenements.<br/> Liste des événements :<br/>
	 * <ul>
	 * <li>boutons standard de l'application. Les événements se lancent sur le clique du bouton</li>
	 * <ul>
	 * <li>btnCancel</li>
	 * <li>btnAdd</li>
	 * <li>btnValidate</li>
	 * <li>btnUpdate</li>
	 * <li>btnDelete</li>
	 * </ul>
	 * <li>AJAX: toutes ces fonctions se lancent à la fin de la fonction dans AJAX</li>
	 * <ul>
	 * <li>ajaxShowDetailRefresh</li>
	 * <li>ajaxLoadData</li>
	 * <li>ajaxShowDetail</li>
	 * <li>ajaxStopEdition</li>
	 * <li>ajaxValidateEditon</li>
	 * <li>ajaxUpdateComplete</li>
	 * </ul>
	 * </ul>
	 */
	bindEvent: {
		ajaxLoadData: function(){ 
			if (this.options.checkOnLoad === true) {
				this.$elementToPutObject.focusout();
			}
		},
		ajaxStopEdition : function() {
			this.reset();
		}
	},
	
	vars: {
		
	},
	
	

	/**
	 * Paramètre de le l'objet qui vont être utilisés pour son initialisation Cet élément est obligatoire. 
	 * Si aucune option, le créer vide (options:{})
	 */
	options: {
		mandatory: false,
		addSymboleMandatory: true,
		checkOnLoad: true,
		callBack: function () {
		}
	},

	/**
	 * Cette fonction est obligatoire car c'est elle qui va initialiser l'objet
	 */
	init: function () {
		var that = this;
		

		this.createHtml();
		this.utils.input.addAllPropertyFromUtil(this.options, this.$elementToPutObject);

		var options = {
				serviceClassName: 'ch.globaz.pegasus.business.services.models.fortuneusuelle.CompteBancaireCCPService' ,
				serviceMethodName: 'checkChIban',
				parametres: that.value,
				criterias: '',
				cstCriterias: '',
				callBack: function (data) {
					that.callBack(data);
				},
				errorCallBack: function (data){
					that.callBackError(data);
				}
			}; 
			
		
		// Ajout du check a la sortie de l'input
		this.$elementToPutObject.focusout(function() {
			
			if($.trim(that.$elementToPutObject.val()).length>0){
				options.parametres = that.$elementToPutObject.val();
				globazNotation.readwidget.options = options;
				
				// Avant l'execution masque les images du check
				that.hideCheckImageKo();
				that.hideCheckImageOk();
				
				// on affiche l'image de checking
				that.showCheckingImage();
				
				// appel au service de validation
				globazNotation.readwidget.read();
			}else{
				that.hideBankName();				
				that.hideCheckImageKo();
				that.hideCheckImageOk();
				that.hideNotCheckableImage();
				that.hideError();
			}
		});

	},
	
	createHtml: function () {
		// Construction des elements de la notation
		var $span = $("<div class='ibanSpan ui-widget'></div>");
		this.$elementToPutObject.wrap($span);

		// Ajout de la zone erreur durant validation		
		var $spanErreur = $("<div>",{
							text: this.i18n('erreur'),
							'class' : 'spanErreur'
							});
		this.$elementToPutObject.parent().append($spanErreur);
		$spanErreur.notationBoxmessage({
				type: 'ERROR'				
				});
		this.hideError();
		
		// Ajout de la zone bank name
		var $spanBankName = $("<span class='spanBankName ui-corner-all'></span>");
		this.$elementToPutObject.after($spanBankName);
		
		// Ajout de la zone check logo
		var $spanCheckLogo = $("<span class='spanCheckLogo ui-corner-all' ></span>");
		this.$elementToPutObject.after($spanCheckLogo);
		
		// Ajout image check OK
		var $imgCheckOk = $("<img>", {
			id: "imgCheckOk",
			"class": "imgCheckOk",
			src: this.getImage("symbol_check24.png"),
			title: this.i18n('imageIbanOk')
		});
		$spanCheckLogo.append($imgCheckOk);
		this.hideCheckImageOk();
		
		// Ajout image check KO
		var $imgCheckKo = $("<img>", {
			id: "imgCheckKo",
			"class": "imgCheckKo",
			src: this.getImage("symbol_check_fail24.png"),
			title: this.i18n('imageIbanKo')
		});
		$spanCheckLogo.append($imgCheckKo);
		this.hideCheckImageKo();
		
		// Ajout image not checkable
		var $imgNotCheckable = $("<img>", {
			id: "imgNotCheckable",
			"class": "imgNotCheckable",
			src: this.s_contextUrl + "/scripts/jsnotation/imgs/symbol_warning24.png",
			title: this.i18n('imageNotCheckable')
		});
		$spanCheckLogo.append($imgNotCheckable);
		this.hideNotCheckableImage();

		// Ajout image checking
		var $imgChecking = $("<img>", {
			id: "imageChecking",
			"class": "imageChecking",
			src: this.s_contextUrl + "/scripts/jsnotation/imgs/checking.gif"
		});
		$spanCheckLogo.append($imgChecking);
		this.hideCheckingImage();
		
	},
	
	// positionnement des images dans l'input
	initPositionCheckingImage: function(s_image) {
		var $element = this.$elementToPutObject;
		var mandatorySymbolWidth = 0;
		
		if(this.options.mandatory===true){
			mandatorySymbolWidth = this.getElement(".simboleMandatory").outerWidth();
		}
		this.getElement(s_image).css( 
				{'position': 'relative',
				 'left': (- ($element.innerHeight()+ mandatorySymbolWidth + 2)) + 'px',
				 'top' : '3px',
				 'width': ($element.innerHeight())+'px',
				 'height' :($element.innerHeight())+'px'
				 });
	},
	
	showCheckingImage: function(){
		this.initPositionCheckingImage(".imageChecking");
		this.getElement(".imageChecking").show();
	},
	
	hideCheckingImage: function() {
		this.getElement(".imageChecking").hide();
	},
	
	hideCheckImageOk: function() {
		this.getElement(".imgCheckOk").hide();
	},
	
	showCheckImageOk: function(){
		this.initPositionCheckingImage(".imgCheckOk");
		this.getElement(".imgCheckOk").show();
	},
	
	hideCheckImageKo: function() {
		this.getElement(".imgCheckKo").hide();
	},
	
	showCheckImageKo: function(){
		this.initPositionCheckingImage(".imgCheckKo");
		this.getElement(".imgCheckKo").show();
	},
	
	hideError: function() {
		this.getElement(".spanErreur").hide();
	},
	
	showError: function() {
		this.getElement(".spanErreur").show();
		
		// on cahe tous les autres elements
		this.hideCheckingImage();
		this.hideCheckImageOk();
		this.hideCheckImageKo();
		this.hideNotCheckableImage();
		this.hideBankName();
	},
	
	hideNotCheckableImage: function() {
		this.getElement(".imgNotCheckable").hide();
	},
	
	showNotCheckableImage: function() {
		this.initPositionCheckingImage(".imgNotCheckable");
		this.getElement(".imgNotCheckable").show();
	},
	
	hideBankName: function() {
		this.getElement(".spanBankName").text('');
		this.getElement(".spanBankName").hide();
	},
	
	showBankName: function(s_bankName) {
		this.getElement(".spanBankName").text(s_bankName);
		this.getElement(".spanBankName").show();
	},
	
	setIban: function(s_iban) {
		this.$elementToPutObject.attr("value", s_iban);
	},
	
	reset: function() {
		// on cahe tous les elements
		this.hideCheckingImage();
		this.hideCheckImageOk();
		this.hideCheckImageKo();
		this.hideNotCheckableImage();
		this.hideBankName();
		this.hideError();
	},
	
	callBackError: function (data) {		
		// message d'erreur
		this.showError();
	},
	
	callBack: function(data) {
		this.hideCheckingImage();
		// cacher les anciennes valeurs affichees
		this.hideError();
		this.hideBankName();
		
		if(data.isCheckable===true){
			// on remplace l'iban donne par l'iban formate
			this.setIban(data.formattedIban);
			
			this.hideNotCheckableImage();
			
			if(data.isValidChIban===true){
				// la description de la banque
				this.showBankName(data.bankDescription);
				
				this.hideCheckImageKo();
				this.showCheckImageOk();
			}else{
				this.hideCheckImageOk();
				this.showCheckImageKo();
			}
		}else{
			this.showNotCheckableImage();
			
			this.hideCheckImageOk();
			this.hideCheckImageKo();
		}
	},
	
	getElement: function (s_selector) {
		return this.$elementToPutObject.parent().find(s_selector);
	}

};
