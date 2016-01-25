<SCRIPT language="javascript">

function updateSociete(tag) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("idSociete").value = element.idSociete;
		document.getElementById("libelleSociete").value = element.adresseSociete;

		//Mise a jour de l'autocomplete pour le numero de facture interne
		updateIdExternePopupTag();

		//Remise a zero de la comboBox pour les organes d'execution
		for (i = 0; i < Number(document.getElementById("idOrganeExecution").options.length); i++){ 			
				document.getElementById("idOrganeExecution").options[ Number(i) ] = null; 
		}
		
		//Mise a jour de la combo box pour les organes d'execution
		document.getElementById("idOrganeExecution").options[ Number(0) ]= new Option('', '0');
		document.getElementById("idOrganeExecution").options.selectedIndex = Number(0);
		for(i = 0; i < Number(element.nbOrganeExecution); i++){ 
			document.getElementById("idOrganeExecution").options[ Number(i+1) ]= new Option(eval("element.nomOrganeExecution"+Number(i)), eval("element.idOrganeExecution"+Number(i)));
		}

		//reset de l'idExterne
	}
}

function onSocieteFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" La société débitrice n'existe pas.");
	}
}

function onFounisseurFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" Le founisseur n'existe pas.");
	}
}

function onCodeBVRFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" Référence Incorrect. Impossible de trouver le CCP");
	}
}

function updateFournisseur(tag) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("idFournisseur").value = element.idFournisseur;
		document.getElementById("adressePaiementFournisseur").value = element.adressePaiementFournisseur;
		document.getElementById("ccpFournisseur").value = element.ccpFournisseur;

		//Positionnement de la devise suivant les info comptable
		var selectBox = document.getElementById("csCodeIsoMonnaie");
		for (var i=0; i < selectBox.options.length; i++) {
			if(selectBox.options[i].value == element.codeIsoMonnaieDefaut) {
				selectBox.options[i].selected = "selected";
				break;
			}
		}
		
		//Mise a jour de l'écheance
		echeance = element.echeance;
		updateDateEcheance("false");

		//Mise a jour du libellé facture
		updateLibelleFacture(element.libelleFacture);

		//Mise a jour de l'autocomplete pour le numero de facture interne
		updateIdExternePopupTag();
		
		<% if(globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_BVR_ORANGE.equals(tmpTypeOp)) { %>
			document.getElementById("referenceBVR").focus();
		<% } else if(globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_BVR_ROUGE.equals(tmpTypeOp)) {%>
			document.getElementById("montant").focus();
		<% } else if(globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_FACTURE_VIREMENT.equals(tmpTypeOp)) {%>
			document.getElementById("clearingBanque").value = element.clearingFournisseur;
			document.getElementById("adresseBanque").value = element.adresseBanque;
			document.getElementById("compte").value = element.compteFournisseur;
		<% } %>
		
		updateCodeTVA(element);

		//Mise a jour de la ventilation
		updateVentilation(element);
	}
}

function updateIdExternePopupTag() {
	var idSociete = document.getElementById("idSociete").value;
	var idFournisseur = document.getElementById("idFournisseur").value;
	idExternePopupTag.updateJspName('<%=jspSectionLocation%>?forIdSociete=' + idSociete + '&forCsTypeSection=<%=typeSection%>&forIdFournisseur=' + idFournisseur + '&like=');
}

function updateCodeTVA(element) {
	//Mise en place de la TVA par default
	for (i = 0; i < Number(document.getElementById("csCodeTva").options.length-1); i++){ 			
		if(document.getElementById("csCodeTva").options[ Number(i+1) ].value == element.defaultCsCodeTVA) {	
			document.getElementById("csCodeTva").options[ Number(i+1) ].selected = "selected"; 
			document.getElementById("csCodeTva").options.selectedIndex = Number(i+1);
			break;
		}
	}
}

function updateBVR(tag) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		document.getElementById("referenceBVR").value = element.numeroReference;
		if(element.montant == 'null') {
			document.getElementById("montant").value = '';
		} else {
			document.getElementById("montant").value = element.montant;
			//Formatage du montant
			validateFloatNumber(document.getElementById("montant"));
		}	
		document.getElementById("ccpFournisseur").value = element.ccpFournisseur;
		document.getElementById("adressePaiementFournisseur").value = element.adressePaiementFournisseur;
		document.getElementById("idFournisseur").value = element.idFournisseur;
		document.getElementById("idExterneFournisseur").value = element.idExterneFournisseur;

		//Positionnement de la devise suivant les info comptable
		var selectBox = document.getElementById("csCodeIsoMonnaie");
		for (var i=0; i < selectBox.options.length; i++) {
			if(selectBox.options[i].value == element.codeIsoMonnaieDefaut) {
				selectBox.options[i].selected = "selected";
				break;
			}
		}

		//Mise a jour du motif
		if(!element.motif == 'null') {
			document.getElementById("motif").value = element.motif;
		} else {
			document.getElementById("motif").value = '';
		}
		
		//Mise a jour de l'écheance
		echeance = element.echeance;

		//Mise a jour du libellé facture
		updateLibelleFacture(element.libelleFacture);	
		document.getElementById("libelle").focus();

		//Mise a jour de la ventilation
		updateVentilation(element);
		
		updateCodeTVA(element);
	}
}

function updateVentilation(element) {

	resetVentilation();
	updateMontant();
	
	var tagUpdate;

	if(element.compteCreance != null && element.compteCreance != "") {
		//Compte de  créance
		document.getElementById("idext"+indexCompteCreance).value = element.compteCreance;
		document.getElementById("idc"+indexCompteCreance).value = element.idCompteCreance;
	
		tagUpdate = new obj(element.idCompteCreance, element.idNatureCompteCreance, element.defaultIdCentreChargeCompteCreance);
		updateCompteCorps(tagUpdate,indexCompteCreance);
		
	}
	
	// Compte de charge
	if(element.compteCharge != null && element.compteCharge != "") {
		document.getElementById("idext"+indexCompteCharge).value = element.compteCharge;
		document.getElementById("idc"+indexCompteCharge).value = element.idCompteCharge;

		tagUpdate = new obj(element.idCompteCharge, element.idNatureCompteCharge, element.defaultIdCentreChargeCompteCharge);
		updateCompteCorps(tagUpdate,indexCompteCharge);
	}

	// Compte TVA
	if(element.compteTVA != null && element.compteTVA != "") {
		document.getElementById("idext"+indexCompteTVA).value = element.compteTVA;
		document.getElementById("idc"+indexCompteTVA).value = element.idCompteTVA;

		tagUpdate = new obj(element.idCompteTVA, element.idNatureCompteTVA, element.defaultIdCentreChargeCompteTVA);
		updateCompteCorps(tagUpdate,indexCompteTVA);
	}	
}

function obj( idCompte,idNature,defaultIdCentreCharge)
{       this.idCompte = idCompte;
        this.idNature = idNature;
        this.defaultIdCentreCharge = defaultIdCentreCharge;
}

/**
 * Mise a jout du montant du BVR.
 * Lance la mise a jour de la ventilation suivant
 * - Le montant
 * - La TVA
 */
function updateMontant() {
	/*
	* Interdire le caractere '-'
	*/
	var montantSansCarMoins = document.getElementById("montant").value;
	montantSansCarMoins = montantSansCarMoins.replace(/-/g, "");
	document.getElementById("montant").value = montantSansCarMoins;
	
	   <% if(globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_PAIEMENT_BVR_ORANGE.equals(tmpTypeOp)
			  || globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_PAIEMENT_BVR_ROUGE.equals(tmpTypeOp)
			  || globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_PAIEMENT_VIREMENT.equals(tmpTypeOp)
			  || globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_PAIEMENT_LSV.equals(tmpTypeOp)
			  || globaz.lynx.db.operation.LXOperationViewBean.CS_TYPE_PAIEMENT_CAISSE.equals(tmpTypeOp)) {  %>
			var elementCreance = "md";
			var element = "mc";
	   <% } else { %>
			var elementCreance = "mc";
			var element = "md";
	   <% } %>
	   
		var montant = deFormatMontant(document.getElementById('montant').value);
		var montantTVA = 0 ;
		var tva = 0;
		
		if(montant != null && montant != '') {

			if(document.getElementById('csCodeIsoMonnaie').value != '<%=LXConstants.CODE_ISO_CHF%>') {
				// ------- Montant Créanc	
				document.getElementById("me"+indexCompteCreance).value = montant;
				validateFloatNumber(document.getElementById("me"+indexCompteCreance));

				document.getElementById(elementCreance+indexCompteCreance).value = "0.00";
				document.getElementById("me" + indexCompteCreance).className = "montantShort";
				document.getElementById("me" + indexCompteCreance).readOnly = "";
				document.getElementById("me" + indexCompteCreance).disabled = "";

				document.getElementById("c" + indexCompteCreance).className = "montantShort";
				document.getElementById("c" + indexCompteCreance).readOnly = "";
				document.getElementById("c" + indexCompteCreance).disabled = "";
				
				updateMontantChf(0);
			}else {
				// ------- Montant Créance
				document.getElementById(elementCreance+indexCompteCreance).value = montant;
				validateFloatNumber(document.getElementById(elementCreance+indexCompteCreance));

				document.getElementById("me" + indexCompteCreance).value = "0.00";
				document.getElementById("me" + indexCompteCreance).className = "montantShortDisabled";
				document.getElementById("me" + indexCompteCreance).readOnly = "readonly";
				document.getElementById("me" + indexCompteCreance).disabled = "disabled";
				document.getElementById("c" + indexCompteCreance).value = "0.00";
				document.getElementById("c" + indexCompteCreance).className = "montantShortDisabled";
				document.getElementById("c" + indexCompteCreance).readOnly = "readonly";
				document.getElementById("c" + indexCompteCreance).disabled = "disabled";
			}
			
			// ------- Montant TVA
			var elementTva = document.getElementById("csCodeTva");
			if(elementTva != null) {
				var tvaSelected = elementTva.options[elementTva.selectedIndex];
				if(tvaSelected.taux != null && tvaSelected.taux != "") {
					montantTVA = montant - (montant * 100 / (100 + parseFloat(tvaSelected.taux)));
				}
			}

			var compteTva = document.getElementById(element+indexCompteTVA);
			if(compteTva != null) {
				compteTva.value = montantTVA;
				validateFloatNumber(compteTva);
			}
			// ------- Montant Charge
			var montantCharge = montant - montantTVA;
			document.getElementById(element+indexCompteCharge).value = montantCharge.toFixed(2);
			validateFloatNumber(document.getElementById(element+indexCompteCharge));
		}
}

/**
 * Permet de supprimer le formatage du montant
 * exemple : 2'758.00   =>   2758.00
 */
function deFormatMontant(tmp) {

	var decote=/'/g;
	
	tmp = tmp.replace(decote,"");
	return parseFloat(tmp);
}

function cheminTabulationBVR() {
	document.getElementById("csCodeTVA").tabIndex = '0';

 	//Permet de créer une le chemin à suivre en faisant des tabultations

	document.getElementById("idExterneFournisseur").tabIndex = '1';
	document.getElementById("idExterneFournisseur").focus();
	document.getElementById("referenceBVR").tabIndex = '1';
	document.getElementById("montant").tabIndex = '1';
	
	document.getElementById("idOrganeExecution").tabIndex = '1';
	document.getElementById("libelle").tabIndex = '1';
	document.getElementById("idExterne").tabIndex = '1';
	document.getElementById("btnVal").tabIndex = '1';
	document.getElementById("btnCan").tabIndex = '1';
}

function cheminTabulationVirementCaisseLSV() {
	document.getElementById("csCodeTVA").tabIndex = '0';
		
	document.getElementById("idExterneFournisseur").tabIndex = '1';
	document.getElementById("montant").tabIndex = '1';
	document.getElementById("idOrganeExecution").tabIndex = '1';
	document.getElementById("libelle").tabIndex = '1';
	document.getElementById("idExterne").tabIndex = '1';
	document.getElementById("btnVal").tabIndex = '1';
	document.getElementById("btnCan").tabIndex = '1';
	document.getElementById("idExterneFournisseur").focus();
}
</SCRIPT>