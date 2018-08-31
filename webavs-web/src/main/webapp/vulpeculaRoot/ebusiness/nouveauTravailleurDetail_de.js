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
	if(globazGlobal.countForTiers == 0){
		GLO.nouveauTravailleur.submitNouveauTravailleur();
	}else{
		if (confirm('Il existe un tiers existant semblable, désirez vous continuez cet enregistrement quand même ?')) {
			GLO.nouveauTravailleur.submitNouveauTravailleur();
		} else {
		    // Do nothing!
		}
	}
}

function RemoveAccents(strAccents) {
//	var strAccents = strAccents.split('');
//	var strAccentsOut = new Array();
//	var strAccentsLen = strAccents.length;
//	var accents = 'ÀÁÂÃÄÅàáâãäåÒÓÔÕÕÖØòóôõöøÈÉÊËèéêëğÇçĞÌÍÎÏìíîïÙÚÛÜùúûüÑñŠšŸÿı';
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

function submitTiers() {
	
	var tiersInfo = {
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
			casePostale: $('#casePostale').val()
	};
	tiersInfo.adresseInfo = adresseInfo;
	
	var travailleurInfo = {
			correlationId: globazGlobal.idCorrelation,
			permisTravail: $('#permisTravail').val(),
			referencePermis: $('#referencePermis').val()
	};
	tiersInfo.travailleurInfo = travailleurInfo;
	
	var posteTravailInfo = {
			idAffiliation: $("#idAffiliation").val(),
			profession: $('#profession').val(),
			qualification: $('#qualification').val(),
			dateValiditeQualification: $('#dateValiditeQualification').val(),
			dateDebut: $('#dateDebut').val(),
			dateFin: $('#dateFin').val(),
			typeSalaire: $('#typeSalaire').val(),
			dateValiditeTypeSalaire: $('#dateValiditeTypeSalaire').val(),
			tauxActivite: $('#tauxActivite').val(),
			salaireBase: $('#salaireBase').val(),
			dateTauxActivite: $("#dateTauxActivite").val(),
			posteCorrelationId: globazGlobal.posteCorrelationId,
			assurance: globazGlobal.assurance
	};
	tiersInfo.posteTravailInfo = posteTravailInfo;
	
	var adresseBanqueInfo = {
			iban: $('#iban').val(),
			idLocalite : globazGlobal.idLocaliteBanque
	};
	tiersInfo.adresseBanqueInfo = adresseBanqueInfo;
	
	sendToService(tiersInfo, "insertTiers");
}

function validateAllFields(){
	submitTiers();
}

function sendRefuseToService(idTravailleur, methode, field){
	var options = {
		serviceClassName:globazGlobal.nouveauTravailleurService,
			serviceMethodName:methode,
			parametres:idTravailleur + ',' + field,
			callBack:function (data) {
				window.location.href = "vulpecula?userAction=vulpecula.ebusiness.nouveauTravailleurDetail.afficher&selectedId="+globazGlobal.idPortail;
			}
	};
	vulpeculaUtils.lancementService(options);
}

function disableAllFields(){
	//Disable Tiers Section
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
	
	//Disable Poste Travail Section
	$("#qualification").prop("disabled", true);
	$("#dateValiditeQualification").prop("disabled", true);
	$("#dateDebut").prop("disabled", true);
	$("#dateFin").prop("disabled", true);
	$("#typeSalaire").prop("disabled", true);
	$("#dateValiditeTypeSalaire").prop("disabled", true);
	$("#dateTauxActivite").prop("disabled", true);
	$("#tauxActivite").prop("disabled", true);
	
	//Disable Travailleur Section
	$("#permisTravail").prop("disabled", true);
	$("#referencePermis").prop("disabled", true);
	
	//Disable banque Section
	$("#iban").prop("disabled", true);
}

function sendToService(objet, methode){
	var objectGSON = JSON.stringify(objet);
	var options = {
			serviceClassName:globazGlobal.nouveauTravailleurService,
			serviceMethodName:methode,
			parametres:objectGSON,
			callBack:function (data) {
				if (data != undefined) {
					var obj = JSON.parse(data);
					if (obj.idPosteTravail != undefined) {
						window.location.href = "vulpecula?userAction=vulpecula.postetravail.posteTravail.afficher&_method=upd&selectedId="+obj.idPosteTravail;
					}
				} 
			}
	};
	
	vulpeculaUtils.lancementService(options);
};


function initialiseEvent(){
	
	$('#submitTiers').click(function() {
		var $this = $(this);
		$this.attr('disabled','disabled');
		
		submitTiers();
	});
	
	$('#submitAdresse').click(function() {
		var $this = $(this);
		$this.attr('disabled','disabled');	

		submitAdresseDomicile();
	});
	
	$('#submitTravailleur').click(function() {
		var $this = $(this);
		$this.attr('disabled','disabled');	

		submitTravailleur();	
	});
	
	$('#submitPosteTravail').click(function() {
		var $this = $(this);
		$this.attr('disabled','disabled');

		submitPosteTravail();
	});
	
	$('#submitBanque').click(function() {
		var $this = $(this);
		$this.attr('disabled','disabled');

		submitAdresseBanque();		
	});
	
	$('#submitAllFields').click(function() {
		var $this = $(this);
		$this.attr('disabled','disabled');
	    
		validateAllFields();
	});
	
	$('#refuseAllFields').click(function() {
		var $this = $(this);
		$this.attr('disabled','disabled');
	    
		sendRefuseToService(globazGlobal.idNouveauTravailleur, "refuse", "all");
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
		$("#localite").prop("disabled", true);
		$("#tdReferencePermis").hide();
		
		if(globazGlobal.isTraite === "true"){
			$('#submitAllFields').prop("disabled", true);
			$('#refuseAllFields').prop("disabled", true);
			disableAllFields();
		}
	}, "Initialisation des evenements")
}


GLO.nouveauTravailleur = {
	$quittancer : null,
	idNouveauTravailleur : null,
	
	errors : {
	},
	
	init : function() {
		var that = this;
		that.$quittancer = $('#quittancer');
		that.idNouveauTravailleur = $('#idDecompte').val();
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




