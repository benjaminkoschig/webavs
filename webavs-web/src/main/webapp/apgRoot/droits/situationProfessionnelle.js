
/**
 * Un object de données tout simple pour le passage des paramètres d'initilisation
 * du ViewController
 * 
 */
ViewControllerData = function(){
	this.className;
	this.methodName;
	this.isModification;
	this.isAcmNeEnable;
	this.isAcmAlphaEnable;
	
	this.validate = function() {
		if(this.isEmpty(this.className)){
			throw new Error("You must define the className : " + this.className);
		}
		if(this.isEmpty(this.methodName)){
			throw new Error("You must define the methodName");
		}
		if(this.isEmpty(this.isModification)){
			throw new Error("You must define the isModification"); 
		}
		if(this.isEmpty(this.isAcmNeEnable)){
			throw new Error("You must define the isAcmNeEnable : " + this.isAcmNeEnable);
		}
		if(this.isEmpty(this.isAcmAlphaEnable)){
			throw new Error("You must define the isAcmAlphaEnable");
		}
	};
	
	this.isEmpty = function(value){
		var result = value == null || value == undefined;
		//console.log("isEmpty ["+value+"] : " + result);
		return result;
	};
};

/**
 * Controlleur de la vue
 * Doit recevoir une instance de type ViewControllerData
 * @param viewControllerData
 */
ViewController = function(viewControllerData){
	if(viewControllerData == undefined || viewControllerData == null){
		throw new Error("viewControllerData instance is null or undefined !!");
	}
	
	this.log = function(message){
		//console.log(message);
	};
	
	this.init = function(isRetourPyxis) {
		this.log("init(isRetourPyxis) : isRetourPyxis = "  + isRetourPyxis);
		viewControllerData.validate();
		this.className = viewControllerData.className;
		this.methodName = viewControllerData.methodName;
		this.isModification = viewControllerData.isModification;
		this.isAcmNeEnable = viewControllerData.isAcmNeEnable;
		this.log("ACM NE enable : " + this.isAcmNeEnable);
		this.isAcmAlphaEnable = viewControllerData.isAcmAlphaEnable;
		this.log("ACM ALFA enable : " + this.isAcmAlphaEnable);
		
		if (isRetourPyxis) {
			this.change();
		} else {
			this.initEcran();
		}	
		this.bindEvent();
	};
	
	this.isVersementEmployeur = function(){
		return document.forms[0].elements('isVersementEmployeur')[0].checked;
	};
	
	this.bindEvent = function() {
		this.log("bindEvent()");
		var that = this;
		$('[name="selecteurEmployeur"]').change(function() {
			that.change();
		});
		
		// Changement sur le radio button Employeur-Assuré
		$('input[name="isVersementEmployeur"]:radio').change(function() {
			if (that.isAcmNeEnable && that.isVersementEmployeur() && !that.isAssociationEmpty()) {
				that.showAcmNe();
			}
			else {
				that.hideAcmNe();
				that.clearTotalAcm();
				that.log("clearing montantJournalierAcmNe");
			}
		});
	};

	
	
	this.clearTotalAcm = function(){
		$('input[name="montantJournalierAcmNe"]').val('');
	};
	
	this.displayInfo = function(data) {
		this.log("displayInfo(data)");
		var typeDePrestation = data.typeDePrestation;
		var csAssociation = data.csAssociation;
		var nomAssociation = data.nomAssociation;
		var nomTypePrestationAcmAlpha = $('#nomTypePrestationAcmAlpha').val();
		var nomTypePrestationAcmNe = $('#nomTypePrestationAcmNe').val();

		// On set les champs selon le résultat du service
		if (typeDePrestation === nomTypePrestationAcmAlpha) {
			$('[name=hasAcmAlphaPrestations]').attr('checked', 'checked');
			$('[name=hasAcm2AlphaPrestations]').attr('checked', 'checked');
			$('[name=montantJournalierAcmNe]').val('');
		}
		else if (typeDePrestation === nomTypePrestationAcmNe  && $('input[name="isVersementEmployeur"]:radio')[0].checked) {
			$('[name=csAssuranceAssociation]').val(csAssociation); 
			$('[name=nomAssociation]').val(nomAssociation); 
		}
		else{
			$('[name=hasAcmAlphaPrestations]').prop('checked',false);
			$('[name=hasAcm2AlphaPrestations]').prop('checked',false);
			$('[name=csAssuranceAssociation]').val('');
			$('[name=montantJournalierAcmNe]').val('');
			this.hideAcmNe();
		}
		
	};
	

	
	this.change = function() {		
		this.log("change()");
		this.hideAllAcm();
		
		var that = this;
		if(this.isAcmNeEnable){
			if(this.isVersementEmployeur() && this.isAcmNeEnable){
				var options = {
					serviceClassName : this.className,
					serviceMethodName : this.methodName,
					parametres : this.getParameters(),
					wantInitThreadContext : true,
					callBack : function(data) {
						that.displayInfo(data);
					}
				};
				this.log("Request to the service with params : ");
				//console.dir(options);
				globazNotation.readwidget.options = options;
				globazNotation.readwidget.read();
				this.showAcmNe();
			}
			this.hideAcmAlpha();
		}
		else if (this.isAcmAlphaEnable){
			this.showAcmAlpha();
			this.hideAcmNe();
		}
		else {
			this.hideAllAcm();
		}
	},

	/**
	 * Récupère les paramètres pour l'envoi de la requête
	 */
	this.getParameters = function() {
		this.log("getParameters()");
		var isPaiementAssure = $('#isVersementEmployeur');
		var isPaiementInDependant = $('[name="isIndependant"]');
		var idDroit = $('[name="idDroitLAPGBackup"]');
		var idAffilieEmployeur = $('[name="idAffilieEmployeur"]');
		var method = $('[name="_method"]');

		var paramsDemande = isPaiementAssure.is(':checked') + ','
				+ isPaiementInDependant.is(':checked') + ',' + idDroit.val()
				+ ',' + idAffilieEmployeur.val() + ',' + method.val();
		return paramsDemande;
	};

	this.isAjout = function(method) {
		return method.val() === 'add';
	};

	this.isModification = function(method, isModification) {
		return (method.val() === 'null' || method.val() === 'upd')
				&& isModification;
	};

	
	this.initEcran = function() {
		this.log("initEcran()");
		this.hideAllAcm();
		if (this.isAcmNeEnable && this.isVersementEmployeur() && !this.isAssociationEmpty()) {
			this.showAcmNe();		
		} 
		if (this.isAcmAlphaEnable) {
			this.showAcmAlpha();	
		}	
	};
	
	this.isAssociationEmpty = function(){
		var associationName = $('[name=nomAssociation]').val();
		var result = this.isEmpty(associationName);
		result = result || associationName == '';
		return result;
	};
	
	this.hideAllAcm = function() {
		this.hideAcmNe();
		this.hideAcmAlpha();
	};

	this.hideAcmNe = function() {
		this.log("Hiding ACM NE bloc");
		$('.prestationAcmNe').hide();
	};

	this.hideAcmAlpha = function() {
		this.log("Hiding ACM ALFA bloc");
		$('.prestationAcmAlpha').hide();
		$('.prestationAcm2Alpha').hide();
	};

	this.showAcmNe = function() {
		this.log("Show ACM NE bloc");
		$('.prestationAcmNe').show();
	};
	
	this.showAcmAlpha = function() {
		this.log("Show ACM ALFA bloc");
		$('.prestationAcmAlpha').show();
		$('.prestationAcm2Alpha').show();
	};
	
	this.isEmpty = function(value){
		return value == null || value == undefined;
	};
};
