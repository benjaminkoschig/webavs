<%@ page import="globaz.jade.client.util.JadeDateUtil" %>
<%@ page import="globaz.jade.i18n.JadeI18n" %>
<script type="text/javascript">

//Check des données obligatoires
function checkMandatory(){
	
	if($("#dateReception").val().length <= 0){
		isValid = false;
	}
	
	if($("#dateFacture").val().length <= 0){
		isValid = false;
	}
	
	if($("#typeSoin").val().length <= 0){
		isValid = false;
	}
	
	if($("#montant").val().length <= 0){
		isValid = false;
	}
	
	if($("#montantRembourse").val().length <= 0){
		isValid = false;
	}
	
	if(!isValid){
		messageError = messageError + <%=viewBean.getLabel("JSP_PF_MANDATORY_MISSING_INFORMATIONS")%> + "\n"
	}
}

//Check du format des dates 
function checkFormatDates(){
	
	if($("#dateReception").val().length > 0 && !globazNotation.utilsDate._isValidGlobazDate($("#dateReception").val())){
		messageError = messageError + <%=viewBean.getLabel("JSP_PF_FORMAT_DATE_RECEPTION_FACTURE_RENTE_PONT")%> + "\n"
		isValid = false;
		isFormatDatesValid = false;
	}

	if($("#dateFacture").val().length > 0 && !globazNotation.utilsDate._isValidGlobazDate($("#dateFacture").val())){
		messageError = messageError + <%=viewBean.getLabel("JSP_PF_FORMAT_DATE_FACTURE_RENTE_PONT")%> + "\n"
		isValid = false;
		isFormatDatesValid = false;
	} 

	if($("#datePriseEnCharge").val().length > 0 && !globazNotation.utilsDate._isValidGlobazDate($("#datePriseEnCharge").val())){
		messageError = messageError + <%=viewBean.getLabel("JSP_PF_FORMAT_DATE_PRISE_EN_CHARGE_FACTURE_RENTE_PONT")%> + "\n"
		isValid = false;
		isFormatDatesValid = false;
	}

	if($("#dateDebutTraitement").val().length > 0 && !globazNotation.utilsDate._isValidGlobazDate($("#dateDebutTraitement").val())){
		messageError = messageError + <%=viewBean.getLabel("JSP_PF_FORMAT_DATE_DEBUT_TRAITEMENT_FACTURE_RENTE_PONT")%> + "\n"
		isValid = false;
		isFormatDatesValid = false;
	}

	if($("#dateFinTraitement").val().length > 0 && !globazNotation.utilsDate._isValidGlobazDate($("#dateFinTraitement").val())){
		messageError = messageError + <%=viewBean.getLabel("JSP_PF_FORMAT_DATE_FIN_TRAITEMENT_FACTURE_RENTE_PONT")%> + "\n"
		isValid = false;
		isFormatDatesValid = false;
	}

	if($("#dateInfoFacture").val().length > 0 && !globazNotation.utilsDate._isValidGlobazDate($("#dateInfoFacture").val())){
		messageError = messageError + <%=viewBean.getLabel("JSP_PF_FORMAT_DATE_SAISIE_INFO_FACTURE_RENTE_PONT")%> + "\n"
		isValid = false;
		isFormatDatesValid = false;
	}

}

//Check délai de 15 mois entre la date de facture et la date de réception
function checkDelai() {

	var dateFacture = $('#dateFacture').val();
	var dateReception = $('#dateReception').val();
	
	var nbMois = globazNotation.utilsDate.countMonth(dateReception, dateFacture);
	
	if(nbMois > 15){
		messageError = messageError + "<%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), "perseus.rentePont.factureRentePont.delais.15mois")%>" + "\n";
		isValid = false;
	}
	
}

//Check si une demande de rente-pont valide existe pour la date de facture
function checkDemandeExiste(){
	
	var valDateReception = $('#dateReception').val();
	var valDateFacture = $('#dateFacture').val();
	var valDatePriseEnCharge = $("#datePriseEnCharge").val();
	var valIdDossier = <%=viewBean.getDossier().getId()%>;
	var valAnneeQD = <%=viewBean.getFactureRentePont().getQdRentePont().getSimpleQDRentePont().getAnnee()%>;
	
	//Impossible de passer un paramètre vide ou null
	//Un test est effectué sur cette valeur dans la méthode checkDemandeExiste côté serveur
	if(!valDatePriseEnCharge.length > 0){
		valDatePriseEnCharge = "dateNull";
	};

		var options = {
			serviceClassName : 'ch.globaz.perseus.business.services.models.rentepont.FactureRentePontDoublonService',
			serviceMethodName : 'checkDemandeExiste',
			parametres: valDateReception + ',' + valDateFacture + ',' + valDatePriseEnCharge + ',' + valIdDossier + ',' + valAnneeQD,
			criterias : '',
			cstCriterias : '',
			errorCallBack : function()
			{
				// Si l'appel ajax échoue, remettre les boutons à enabled de toutes façons
				disableButtons(false);
				isValid = false;
			},
			callBack : function(message) {
				// S'il y existe une facture rente-pont similaire ...
				if (message.length > 0) {
					isValid = false;
					messageError = messageError + message + "\n";
				};
			},
			async : false
		};
		
		globazNotation.readwidget.options=options;
		globazNotation.readwidget.read();
		
}

//Check si une demande similaire existe déjà et averti l'utilisateur
function checkDemandeSimilaire() {
	
	var valIdDossier = <%=viewBean.getDossier().getId()%>;
	var valMontant = $("#montant").prop("value");
	var valDateFacture = $("#dateFacture").prop("value");
	var valTypeSoin = $("#sousTypeSoin option:selected").val();
	var valIdFacture = <%=viewBean.getFactureRentePont().getId()%>;
	
	if (valIdDossier > 0 && valMontant.length > 0 && valDateFacture.length > 0 && typeof valTypeSoin !== "undefined") {
		disableButtons(true);
		

		var options = {
			serviceClassName : 'ch.globaz.perseus.business.services.models.rentepont.FactureRentePontDoublonService',
			serviceMethodName : 'factureSimilaireExiste',
			parametres: valIdFacture + ',' + valIdDossier + ',' + valDateFacture + ',' + valMontant + ',' + valTypeSoin,
			criterias : '',
			cstCriterias : '',
			errorCallBack : function()
			{
				// Si l'appel ajax échoue, remettre les boutons à enabled de toutes façons
				disableButtons(false);
				isValid = false;
			},
			callBack : function(existe) {
				
				// S'il y existe une facture rente-pont similaire ...
				if (existe) {
					// Déclaration de la méthode effectuant la construction du message d'avertissement
					if (!String.prototype.format) {
						String.prototype.format = function() {
							var args = arguments;
							return this.replace(/{(\d+)}/g, function(match, number) { 
								return typeof args[number] != 'undefined' ? args[number] : match ;
							});
						};
					}
					
					// Génération du message d'avertissement
					var valSousType = $("#sousTypeSoin option:selected").text();
					var message = globazGlobal.label.JSP_PF_FACRP_AVERTISSEMENT_FACTURE_SIMILAIRE.format(valDateFacture, valSousType, valMontant);
					
				
					if(!isModifier && actionMethod != "add"){
					} else {
						// Si l'utilisateur ne confirme pas son intention, le COMMIT n'est pas effectué
						if (confirm(message) == false) {
							disableButtons(false);
							isValid = false;							
						}
					}
				};
			},
			async: false
		};
		
		globazNotation.readwidget.options=options;
		globazNotation.readwidget.read();
	}
}

//Check si l'état de la QD permet l'imputation de cette facture
function checkQD(){
	
	var excedantRevenuCompenseQD = globazNotation.utilsFormatter.amountTofloat($('#excedantRevenuCompense').text());
    var excedantRevenuCompenseFacture = globazNotation.utilsFormatter.amountTofloat($('#excedantRevenuPrisEnCompte').val());
    var montantUtiliseQD = globazNotation.utilsFormatter.amountTofloat($('#qdUtilise').text());
    var montantRembourseFacture = globazNotation.utilsFormatter.amountTofloat($('#montantRembourse').val());
    var excedantRevenuQD = globazNotation.utilsFormatter.amountTofloat($('#excedantRevenu').text());
    var montantLimiteQD = globazNotation.utilsFormatter.amountTofloat($('#qdLimite').text());
    var montantFacture = globazNotation.utilsFormatter.amountTofloat($('#montant').val());

    if ((montantRembourseFacture < 0) || (excedantRevenuCompenseFacture < 0)) {
    	messageError = messageError + "<%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), "perseus.rentePont.factureRentePont.montants.negatifs")%>" + "\n";
		isValid = false;
    };

    if (montantRembourseFacture > globazNotation.utilsFormatter.amountTofloat($('#montant').val())) {
    	messageError = messageError + "<%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), "perseus.rentePont.factureRentePont.montants.incoherents")%>" + "\n";
        isValid = false;
    }

    if (!$("#acceptationForceeCB").prop("checked")) { 
        if (montantRembourseFacture > (montantLimiteQD - montantUtiliseQD)) {
        	messageError = messageError + "<%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), "perseus.rentePont.factureRentePont.qd.montanttropfaible")%>" + "\n";
    		isValid = false;
        }
        if (((excedantRevenuQD - excedantRevenuCompenseQD) > 0)
                && (excedantRevenuCompenseFacture > (excedantRevenuQD - excedantRevenuCompenseQD))) {
        	messageError = messageError + "<%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), "perseus.rentePont.factureRentePont.qd.excedantRevenuTropGrand")%>" + "\n";
            isValid = false;
        }
    }
    
    if ((montantRembourseFacture + excedantRevenuCompenseFacture) > montantFacture) {
    	messageError = messageError + "<%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), "perseus.facture.montantcompense.plus.montantrembourse.plusgrand.montantfacture")%>" + "\n";
    	isValid = false;
    }
	
}

//Check si une adresse de paiement est saisie
function checkAdressePaiement(){
	
	if(!$('#idAdressePaiement').val().length > 0 || !$('#idDomaineApplicatif').val() > 0){
		messageError = messageError + "<%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), "perseus.facture.adressepaiement.mandatory")%>" + "\n";
		isValid = false;
	}
	
}

//Check si le numéro de référence de la facture est valide
function checkNumRefFacture(){
	
 	var bvrOk = true;
    var ccpOk = true;
    
    var numBVR = $("#numRefFacture").val();
    
    var optionsBVR = {
			serviceClassName : 'ch.globaz.perseus.business.services.bvr.BvrService',
			serviceMethodName : 'validationNumeroBVR',
			parametres: numBVR,
			criterias : '',
			cstCriterias : '',
			errorCallBack : function()
			{
				// Si l'appel ajax échoue, remettre les boutons à enabled de toutes façons
				disableButtons(false);
				return false;
			},
			callBack : function(bvrReturn) {
				if (bvrReturn == "") {
					bvrOk = false;
				} 
			},
			async: false
	};
		
	globazNotation.readwidget.options=optionsBVR;		
	globazNotation.readwidget.read();
	
	
	var idAdressePaiement = $('#idAdressePaiement').val();
    var idDomaineApp = $('#idDomaineApplicatif').val();
    
    if (idAdressePaiement == 0 || idDomaineApp == 0) {
    	ccpOk = false;
    } else {
    	 
        
         
   	 	var optionsCCP = {
 			serviceClassName : 'ch.globaz.perseus.business.services.bvr.BvrService',
 			serviceMethodName : 'validationCCP',
 			parametres: idAdressePaiement + ',' + idDomaineApp,
 			criterias : '',
 			cstCriterias : '',
 			errorCallBack : function()
 			{
 				// Si l'appel ajax échoue, remettre les boutons à enabled de toutes façons
 				disableButtons(false);
 				isValid = false;
 			},
 			callBack : function(valide) {
 				
 				// S'il y existe une facture rente-pont similaire ...
 				if (!valide) {
 					ccpOk = false;
 				} 
 			},
 			async : false
   	 	};
   	 	
 		globazNotation.readwidget.options=optionsCCP;		
 		globazNotation.readwidget.read();
    }

    if (!bvrOk && !ccpOk) {
    	messageError = messageError + "<%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), "perseus.facture.numero.reference.et.ccp.incorrect")%>" + "\n";
        isValid = false;
    } else if (!bvrOk) {
    	messageError = messageError + "<%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), "perseus.facture.numero.reference.incorrect")%>" + "\n";
        isValid = false;
    } else if (!ccpOk) {
    	messageError = messageError + "<%=JadeI18n.getInstance().getMessage(objSession.getIdLangue(), "perseus.facture.ccp.incorrect")%>" + "\n";
        isValid = false;
    }
   	 	
}

//Appel des différents checks
//Retourne isValid = false si une ou plusieurs erreurs sont survenues durant les checks
//Affiche une alert contenant les différents messages d'erreurs 
function checkForCreate(){
	isValid = true;
	isFormatDatesValid = true;
	messageError = "";
	
	checkMandatory();
	if(isValid){
		checkFormatDates();
		if(isFormatDatesValid){
			checkDelai();	
		}
		checkDemandeExiste();
		checkDemandeSimilaire();
		checkQD();
		checkAdressePaiement();
		if($('#numRefFacture').val().length > 0){
			checkNumRefFacture();
		}
	}
	
	
	if(messageError != ""){
		alert(messageError);
	}
	
	return isValid;
}
</script>