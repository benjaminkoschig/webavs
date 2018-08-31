var GLO = GLO || {};

$(function() {
	GLO.nouveauTravailleur.init();
	initialiseEvent();

});

function add () {
}

function upd() {
}

function cancel() {
	document.forms[0].elements('userAction').value="vulpecula.ebusiness.nouveauTravailleur.afficher";
}

function del() {
}

function init() {
	action(UPDATE);
}

function validate() {
	GLO.nouveauTravailleur.submitNouveauTravailleur();
}

function RemoveAccents(strAccents) {
//	var strAccents = strAccents.split('');
//	var strAccentsOut = new Array();
//	var strAccentsLen = strAccents.length;
//	var accents = '¿¡¬√ƒ≈‡·‚„‰Â“”‘’’÷ÿÚÛÙıˆ¯»… ÀËÈÍÎ«Á–ÃÕŒœÏÌÓÔŸ⁄€‹˘˙˚¸—Òäöüˇ˝éû';
//	var accentsOut = "AAAAAAaaaaaaOOOOOOOooooooEEEEeeeeeCcDIIIIiiiiUUUUuuuuNnSsYyyZz";
//	for (var y = 0; y < strAccentsLen; y++) {
//		if (accents.indexOf(strAccents[y]) != -1) {
//			strAccentsOut[y] = accentsOut.substr(accents.indexOf(strAccents[y]), 1);
//		} else
//			strAccentsOut[y] = strAccents[y];
//	}
//	strAccentsOut = strAccentsOut.join('');
//	return strAccentsOut;
	
	return strAccents;
}

function checkTiers(){
	var isDifferent = false;

	var nomExistant = globazGlobal.travailleurExistant.designation1;
	var prenomExistant = globazGlobal.travailleurExistant.designation2;
	var dateNaissanceExistant = globazGlobal.travailleurExistant.dateNaissance;
	var sexeExistant = globazGlobal.travailleurExistant.sexe;
	var etatCivilExistant = globazGlobal.travailleurExistant.etatCivil;
	var idPaysExistant = globazGlobal.paysExistant;
	var telephoneExistant = globazGlobal.telephoneExistantFormatted;
	var telephone = globazGlobal.telephoneFomatted;
	
	var nssExistant = globazGlobal.travailleurExistant.numAvsActuel;	
	var nssEbu = $('input[name="nssNssPrefixe"]').val() + $('input[name="partialnss"]').val();
	
	if(nssEbu == "756."){
		nssEbu = "";
	}
	
	if(nssEbu != nssExistant){
		$('input[name="partialnss"]').prop("disabled", false);
		isDifferent = true;
	}else{
		$('input[name="partialnss"]').prop("disabled", true);
	}
	
	if(globazGlobal.tiersTraite == "true"){
		
		$("#partialnss").prop("disabled", true);
		$("#nom").prop("disabled", true);
		$("#prenom").prop("disabled", true);
		$("#dateNaissance").prop("disabled", true);
		$("#sexe").prop("disabled", true);
		$("#etatCivil").prop("disabled", true);
		$("#nationalite").prop("disabled", true);
		$("#rue").prop("disabled", true);
		$("#rueNumero").prop("disabled", true);
		$("#localite").prop("disabled", true);
		$("#casePostale").prop("disabled", true);
		$("#pays").prop("disabled", true);
		$("#npa").prop("disabled", true);
		$("#telephone").prop("disabled", true);
		
		$("#refuseTiersEtAdresse").prop("disabled", true);
		$("#submitTiersEtAdresse").prop("disabled", true);
		
	}else{
		if($("#partialnss").val() != globazGlobal.nssExistant){
			$("#partialnss").prop("disabled", false);
			isDifferent = true;
		}else{
			$("#partialnss").prop("disabled", true);
		}
		if($("#nom").val() != nomExistant){
			$("#nom").prop("disabled", false);
			isDifferent = true;
		}else{
			$("#nom").prop("disabled", true);
		}
		
		if($("#prenom").val() != prenomExistant){
			$("#prenom").prop("disabled", false);
			isDifferent = true;
		}else{
			$("#prenom").prop("disabled", true);
		}
		
		if($("#dateNaissance").val() != dateNaissanceExistant){
			$("#dateNaissance").prop("disabled", false);
			isDifferent = true;
		}else{
			$("#dateNaissance").prop("disabled", true);
		}
		
		if($("#sexe").val() != sexeExistant){
			$("#sexe").prop("disabled", false);
			isDifferent = true;
		}else{
			$("#sexe").prop("disabled", true);
		}
		
		if($("#etatCivil").val() != etatCivilExistant){
			$("#etatCivil").prop("disabled", false);
			isDifferent = true;
		}else{
			$("#etatCivil").prop("disabled", true);
		}
		
		if(globazGlobal.nationalite != idPaysExistant){
			$("#nationalite").prop("disabled", false);
			isDifferent = true;
		}else{
			$("#nationalite").prop("disabled", true);
		}
		
		if(telephone != telephoneExistant){
			$("#telephone").prop("disabled", false);
			isDifferent = true;
		}else{
			$("#telephone").prop("disabled", true);
		}
		
		// checkTiers partage le mÍme isDifferent que checkAdresse
		checkAdresse(isDifferent);
	}
	
}

function checkAdresse(isDifferent){
	
	// Gestion des cas de modifications qui n'ont aucune adresse
	if(globazGlobal.isAdresseNull != "true"){
		var rueExistant = globazGlobal.travailleurExistant.adressePrincipale.rue;
		var rueNumeroExistant = globazGlobal.travailleurExistant.adressePrincipale.rueNumero;
		var npaExistant = globazGlobal.travailleurExistant.adressePrincipale.npa;
		var casePostaleExistant = $.trim(globazGlobal.travailleurExistant.adressePrincipale.casePostale);
		var paysExistant = globazGlobal.travailleurExistant.adressePrincipale.isoPays;
	}else{
		var rueExistant = "";
		var rueNumeroExistant = "";
		var npaExistant = "";
		var casePostaleExistant = "";
		var paysExistant = "";
	}
	
	$("#localite").prop("disabled", true);
		
	if($("#rue").val() != rueExistant){
		$("#rue").prop("disabled", false);
		isDifferent = true;
	}else{
		$("#rue").prop("disabled", true);
	}
	if($("#rueNumero").val() != rueNumeroExistant){
		$("#rueNumero").prop("disabled", false);
		isDifferent = true;
	}else{
		$("#rueNumero").prop("disabled", true);
	}
	var npaPortail = $("#npa").val();
	if(npaPortail.length > 4){
		npaPortail = npaPortail.substring(0, 4);
	}
	if(npaPortail != npaExistant){
		$("#npa").prop("disabled", false);
		isDifferent = true;
	}else{
		$("#npa").prop("disabled", true);
	}
	if($("#casePostale").val() != casePostaleExistant){
		$("#casePostale").prop("disabled", false);
		isDifferent = true;
	}else{
		$("#casePostale").prop("disabled", true);
	}
	if($("#pays").val() != paysExistant){
		$("#pays").prop("disabled", false);
		isDifferent = true;
	}else{
		$("#pays").prop("disabled", true);
	}

	if(isDifferent){
		$("#refuseTiersEtAdresse").prop("disabled", false);
		$("#submitTiersEtAdresse").prop("disabled", false);
	}else{
		$("#refuseTiersEtAdresse").prop("disabled", true);
		$("#submitTiersEtAdresse").prop("disabled", true);
	}
}

function checkPosteTravail(){
	
	var isDifferent = false;
	
	var qualificationExistant = globazGlobal.qualifValue;
	var dateDebutExistant;
	var dateFinExistant;
	var typeSalaireExistant = globazGlobal.posteTravail.typeSalaire;
	if(typeSalaireExistant !=null){
		typeSalaireExistant = typeSalaireExistant.toLowerCase();
	}
	var tauxExistant = globazGlobal.tauxExistant;
	var dateTauxExistant = globazGlobal.dateTauxExistant;
	
	var dateValiditeQualificationExistant;
	if(typeof globazGlobal.dateValiditeQualificationSwiss != "undefined"){
		dateValiditeQualificationExistant = globazGlobal.dateValiditeQualificationSwiss
	} else {
		dateValiditeQualificationExistant = "";
	}
	
	var dateValiditeTypeSalaireExistant;
	if(typeof globazGlobal.dateValiditeTypeSalaireSwiss != "undefined"){
		dateValiditeTypeSalaireExistant = globazGlobal.dateValiditeTypeSalaireSwiss
	} else {
		dateValiditeTypeSalaireExistant = "";
	}
	
	if(typeof globazGlobal.posteTravail.periodeActivite != "undefined"){
		dateDebutExistant = globazGlobal.dateDebutSwiss;
		if(typeof globazGlobal.dateDebutSwiss != "undefined" ){
			dateFinExistant = globazGlobal.dateFinSwiss;
		}else{
			dateFinExistant = "";
		}
	}else{
		dateDebutExistant = "";
	}
	
	if(globazGlobal.posteTraite == "true"){
		$("#qualification").prop("disabled", true);
		$("#dateDebut").prop("disabled", true);
		$("#dateFin").prop("disabled", true);
		$("#typeSalaire").prop("disabled", true);
		$("#tauxActivite").prop("disabled", true);
		$("#dateTauxActivite").prop("disabled", true);
		$("#dateValiditeQualification").prop("disabled", true);
		$("#dateValiditeTypeSalaire").prop("disabled", true);
		
	}else{
		
		if($("#qualification").val().toUpperCase() != qualificationExistant){
			$("#qualification").prop("disabled", false);
			isDifferent = true;
		}else{
			$("#qualification").prop("disabled", true);
		}
		
		if(typeof globazGlobal.dateDebutSwiss != "undefined" ){
			dateFinExistant = globazGlobal.dateFinSwiss;
		}else{
			dateFinExistant = "";
		}
		
		if($("#dateValiditeQualification").val() != dateValiditeQualificationExistant){
			$("#dateValiditeQualification").prop("disabled", false);
			isDifferent = true;
		}else{
			$("#dateValiditeQualification").prop("disabled", true);
		}
		
		if(globazGlobal.posteTravail.periodeActivite != null || globazGlobal.posteTravail.periodeActivite != "undefined" ){
			if($("#dateDebut").val() != dateDebutExistant){
				$("#dateDebut").prop("disabled", false);
				isDifferent = true;
			}else{
				$("#dateDebut").prop("disabled", true);
			}
			if($("#dateFin").val() != dateFinExistant){
				$("#dateFin").prop("disabled", false);
				isDifferent = true;
			}else{
				$("#dateFin").prop("disabled", true);
			}
		}
		
		if($("#typeSalaire option:selected").text() != typeSalaireExistant){
			$("#typeSalaire").prop("disabled", false);
			isDifferent = true;
		}else{
			$("#typeSalaire").prop("disabled", true);
		}
		
		if($("#dateValiditeTypeSalaire").val() != dateValiditeTypeSalaireExistant){
			$("#dateValiditeTypeSalaire").prop("disabled", false);
			isDifferent = true;
		}else{
			$("#dateValiditeTypeSalaire").prop("disabled", true);
		}
			
		if($("#tauxActivite").val() !== tauxExistant){
			$("#tauxActivite").prop("disabled", false);
			isDifferent = true;
		}else{
			$("#tauxActivite").prop("disabled", true);
		}
		
		if($("#dateTauxActivite").val() != dateTauxExistant){
			$("#dateTauxActivite").prop("disabled", false);
			isDifferent = true;
		}else{
			$("#dateTauxActivite").prop("disabled", true);
		}
		
	}
	
	if(isDifferent){
		$("#submitPosteTravail").prop("disabled", false);
		$("#refusePosteTravail").prop("disabled", false);
		$("#updatePosteTravail").prop("disabled", false);
	}else{
		$("#submitPosteTravail").prop("disabled", true);
		$("#refusePosteTravail").prop("disabled", true);
		$("#updatePosteTravail").prop("disabled", true);
	}
	
	
}

function checkTravailleur(){
	var isDifferent = false;
	
	var permisTravailExistant;
	if( globazGlobal.permisTravailExistant != null){
		permisTravailExistant = globazGlobal.permisTravailExistant;
	}else{
		permisTravailExistant = "";
	}
	
	if(globazGlobal.travailleurTraite == "true"){

		$("#permisTravail").prop("disabled", true);
		$("#referencePermis").prop("disabled", true);
			
	}else{
		
		if(globazGlobal.travailleurAModifier.permisSejour.categoriePermis != permisTravailExistant){
			$("#permisTravail").prop("disabled", false);
			isDifferent = true;
		}else{
			$("#permisTravail").prop("disabled", true);
		}

		$("#tdReferencePermis").hide();
		
	}
		
	if(isDifferent){
		$("#submitTravailleur").prop("disabled", false);
		$("#refuseTravailleur").prop("disabled", false);
	}else{
		$("#submitTravailleur").prop("disabled", true);
		$("#refuseTravailleur").prop("disabled", true);
	}
	
}

function checkAdresseBanque(){
	var isDifferent = false;
	
	var ibanExistant = globazGlobal.iban;
	var ibanEbu = $("#iban").val();
	
	if(ibanEbu == ""){
		ibanEbu = "non trouvÈ";
	}
	if(globazGlobal.banqueTraite == "true"){
		
		$("#iban").prop("disabled", true);
		
	}else{
		
		if(ibanEbu != ibanExistant){
			$("#iban").prop("disabled", false);
			isDifferent = true;
		}else{
			$("#iban").prop("disabled", true);
		}
		
	}
	
	if(isDifferent){
		$("#submitBanque").prop("disabled", false);
		$("#refuseBanque").prop("disabled", false);	
	}else{
		$("#submitBanque").prop("disabled", true);
		$("#refuseBanque").prop("disabled", true);
	}
	
}

function checkFieldsToDisable(){
	checkTiers();
	if(globazGlobal.posteTravail != null){
		checkPosteTravail();
	}
	if(globazGlobal.posteTraite == "true"){
		$("#qualification").prop("disabled", true);
		$("#dateDebut").prop("disabled", true);
		$("#dateFin").prop("disabled", true);
		$("#typeSalaire").prop("disabled", true);
		$("#tauxActivite").prop("disabled", true);
		$("#dateTauxActivite").prop("disabled", true);
		$("#dateValiditeQualification").prop("disabled", true);
		$("#dateValiditeTypeSalaire").prop("disabled", true);		
	}
	checkTravailleur();
	checkAdresseBanque();	
}

function sendToService(objet, methode){
	
	var objectGSON = JSON.stringify(objet);
	
	var options = {
			serviceClassName:globazGlobal.nouveauTravailleurService,
			serviceMethodName:methode,
			parametres:objectGSON,
			callBack:function (data) {
				if (data != undefined) {
					if (data.idPosteTravail) {
						window.location.href = "vulpecula?userAction=vulpecula.postetravail.posteTravail.afficher&_method=upd&selectedId="+data.idPosteTravail;
					}else{
						window.location.href = "vulpecula?userAction=vulpecula.ebusiness.modificationTravailleurDetail.afficher&selectedId="+globazGlobal.idPortail;
					}
				} 
			}
	};
	
	vulpeculaUtils.lancementService(options);
};

function onChangeDateDebutActivite(){
	majPosteTravail($('#dateDebut').val(), globazGlobal.dateFinSwiss);
}

function majPosteTravail(dateDebut, dateFin){
		var options = {
				serviceClassName:globazGlobal.posteTravailAjaxService,
				serviceMethodName:'getIsPosteReactivable',
				parametres:dateDebut+","+dateFin,
				callBack:function (data) {
					if(data != undefined){
						alert(data);
						globazGlobal.isPosteReactivable = data;
					}
				}
		};
		
		vulpeculaUtils.lancementService(options);
};

function sendToUpdateService(objet, methode){
	
	var objectGSON = JSON.stringify(objet);
	
	var options = {
			serviceClassName:globazGlobal.modificationTravailleurService,
			serviceMethodName:methode,
			parametres:objectGSON,
			callBack:function (data) {
				alert('Mise ‡ jour effectuÈe !');
				if (data != undefined) {
					if (data.idPosteTravail) {
						window.location.href = "vulpecula?userAction=vulpecula.postetravail.posteTravail.afficher&_method=upd&selectedId="+data.idPosteTravail;
					}else{
						window.location.href = "vulpecula?userAction=vulpecula.ebusiness.modificationTravailleurDetail.afficher&selectedId="+globazGlobal.idPortail;
					}
				} 
			}
	};
	
	vulpeculaUtils.lancementService(options);
};

function sendRefuseToService(idTravailleur, methode, field){
	var options = {
		serviceClassName:globazGlobal.nouveauTravailleurService,
			serviceMethodName:methode,
			parametres:idTravailleur + ',' + field,
			callBack:function (data) {
				window.location.href = "vulpecula?userAction=vulpecula.ebusiness.modificationTravailleurDetail.afficher&selectedId="+globazGlobal.idPortail;
			}
	};
	vulpeculaUtils.lancementService(options);
}

function submitTiersEtAdresse(){
	var tiersInfo = {
			idTiersExistant: globazGlobal.idTiersExistant,
			idPortail: globazGlobal.idPortail,
			nss: $('input[name="nssNssPrefixe"]').val() + $('input[name="partialnss"]').val(),
			nom: RemoveAccents($('#nom').val()),
			prenom: RemoveAccents($('#prenom').val()),
			dateNaissance: $('#dateNaissance').val(),
			sexe: $('#sexe').val(),
			etatCivil: $('#etatCivil').val(),
			nationalite: $('#nationalite').val(),
			telephone: $('#telephone').val()
	};
	
	var adresseInfo = {
			rue: RemoveAccents($('#rue').val()),
			rueNumero: $('#rueNumero').val(),
			npa: $('#npa').val(),
			localite: RemoveAccents($('#localite').val()),
			casePostale: $('#casePostale').val(),
			pays: $('#pays').val()
	};
	tiersInfo.adresseInfo = adresseInfo;
	
	sendToUpdateService(tiersInfo, "updateTiers");
}

function getPosteTravailField(){
	var posteTravailInfo = {
			idPosteTravailExistant: globazGlobal.idPosteTravailExistant,
			idPortail: globazGlobal.idPortail,
			idAffiliation: globazGlobal.idAffiliation,
			idTravailleur: globazGlobal.idTravailleurExistant,
			profession: $('#profession').text(),
			qualification: $('#qualification').val(),
			dateDebut: $('#dateDebut').val(),
			dateFin: $('#dateFin').val(),
			typeSalaire: $('#typeSalaire').val(),
			tauxActivite: $('#tauxActivite').val(),
			salaireBase: $('#salaireBase').text(),
			dateTauxActivite: $("#dateTauxActivite").val(),
			correlationId : globazGlobal.idCorrelation,
			posteCorrelationId : globazGlobal.posteCorrelationId,
			assurance: globazGlobal.assurance,
			dateValiditeQualification: globazGlobal.dateValiditeQualificationSwiss,
			dateValiditeTypeSalaire: globazGlobal.dateValiditeTypeSalaireSwiss
	}
	return posteTravailInfo;
}

function initialiseEvent(){	
	
	$('#submitTiersEtAdresse').click(function() {
		var $this = $(this);
		$this.attr('disabled','disabled');
		submitTiersEtAdresse();		
	});
	
	$('#refuseTiersEtAdresse').click(function() {
		var $this = $(this);
		$this.attr('disabled','disabled');
		sendRefuseToService(globazGlobal.idTravailleur, "refuse", "refuseTiers" );
		
	});
	
	$('#submitTravailleur').click(function() {
		var $this = $(this);
		$this.attr('disabled','disabled');
		var travailleurInfo = {
				idPortail: globazGlobal.idPortail,
				correlationId: globazGlobal.idCorrelation,
				idTravailleurExistant: globazGlobal.idTravailleurExistant,	
				idTiersExistant: globazGlobal.idTiersExistant,
				permisTravail: $('#permisTravail').val(),
				referencePermis: $('#referencePermis').val()			
		};
		sendToUpdateService(travailleurInfo, "updateTravailleur");		
	});
	
	$('#refuseTravailleur').click(function() {
		var $this = $(this);
		$this.attr('disabled','disabled');
		sendRefuseToService(globazGlobal.idTravailleur, "refuse", "refuseTravailleur" );		
	});
	
	$('#submitPosteTravail').click(function() {
		var $this = $(this);
		$this.attr('disabled','disabled');
		var posteTravailInfo = getPosteTravailField();
		if(globazGlobal.existPostePourQualif == 'false'){
			sendToService(posteTravailInfo, "insertPosteTravail");
		}else{
			alert("Nouveau impossible, Un poste avec la mÍme qualification est actif ou a ÈtÈ actif dans les 9 derniers mois.");
			$this.prop("disabled", false);
		}		
	});
	
	$('#updatePosteTravail').click(function() {
		var $this = $(this);
		$this.attr('disabled','disabled');
		var posteTravailInfo = getPosteTravailField();
		if(globazGlobal.isPosteReactivable == 'true'){
			sendToUpdateService(posteTravailInfo, "updatePosteTravail");
		}else{
			alert("RÈactivation impossible, la date de fin du poste existant est de plus de neuf mois.");
			$this.prop("disabled", false);
		}
		
	});
	
	$('#refusePosteTravail').click(function() {
		var $this = $(this);
		$this.attr('disabled','disabled');
		sendRefuseToService(globazGlobal.idTravailleur, "refuse", "refusePosteTravail" );		
	});
	
	$('#submitBanque').click(function() {
		var $this = $(this);
		$this.attr('disabled','disabled');
		var adresseBanqueInfo = {
				idPortail: globazGlobal.idPortail,
				idTiersExistant: globazGlobal.idTiersExistant,
				iban: $('#iban').val(),
				idLocalite : globazGlobal.idLocaliteBanque
		}
		sendToUpdateService(adresseBanqueInfo, "updateBanque");		
	});
	
	$('#refuseBanque').click(function() {
		var $this = $(this);
		$this.attr('disabled','disabled');
		sendRefuseToService(globazGlobal.idTravailleur, "refuse", "refuseBanque" );	
	});
	
	var permisDeTravail = {
			$nationalite: null,	
			init: function () {
				this.$nationalite = $("#nationalite");
				var that = this;
				this.$nationalite.change(function(){
					that.disabledOrEnabledPermis();
				});
				that.disabledOrEnabledPermis();
			},
			
			disabledOrEnabledPermis: function (){
				var isSuisse = this.$nationalite.val() === "CH";
				if(isSuisse){
					$("[name='permisTravail']").val("");
				}
				$("[name='permisTravail']").prop("disabled", isSuisse);
				$('#referencePermis').prop("disabled", isSuisse);
			}
	}
	
	
	jsManager.addAfter(function (){
		permisDeTravail.init();
		checkFieldsToDisable();
		gererStatus();
	}, "Initialisation des evenements")
}

function gererStatus(){
	if(globazGlobal.tiersTraite == 'true'){
		$("#submitTiersEtAdresse").prop("disabled", true);
		$("#refuseTiersEtAdresse").prop("disabled", true);
	}
	if(globazGlobal.posteTraite == 'true'){
		$("#submitPosteTravail").prop("disabled", true);
		$("#refusePosteTravail").prop("disabled", true);
		$("#updatePosteTravail").prop("disabled", true);		
	}
	if(globazGlobal.travailleurTraite == 'true'){
		$("#submitTravailleur").prop("disabled", true);
		$("#refuseTravailleur").prop("disabled", true);
	}
	if(globazGlobal.banqueTraite == 'true'){
		$("#submitBanque").prop("disabled", true);
		$("#refuseBanque").prop("disabled", true);
	}
}


GLO.nouveauTravailleur = {
	$quittancer : null,
	idTravailleurAModifier : null,
	
	errors : {
	},
	
	init : function() {
		var that = this;
		that.$quittancer = $('#quittancer');
		that.idTravailleurAModifier = $('#idTravailleur').html();
	},
	
	submitNouveauTravailleur : function () {
		var that = this;
		var options = {
				serviceClassName:globazGlobal.nouveauTravailleurService,
				serviceMethodName:'quittancer',
				parametres:globazGlobal.idNouveauTravailleur,
				callBack:function (data) {
					window.location.href = "vulpecula?userAction=back";
				}
		};
		vulpeculaUtils.lancementService(options);	
	}	
};




