<SCRIPT	language="javascript">

//  ---------------- Function pour la ventilation -----------------

shortKeys[107] = "plus";
shortKeys[49] = "plus";
shortKeys[48] = "flv";
shortKeys[96] = "flv";

var nextRowToShow = <%=viewBean.getShowRows()%>;
var maxRows = <%=viewBean.getMaxRows()%>;

var indexCompteCreance = 0;
var indexCompteCharge = 1;
var indexCompteTVA = 2;

function hideRows() {
	for (i=nextRowToShow; i<maxRows; i++) {
		document.getElementById("ligne" + i).style.display = "none";
	}
}

function showNextRow() {
	if (nextRowToShow < maxRows) {
		document.getElementById("ligne" + nextRowToShow).style.display = "block";
		nextRowToShow ++;
	}
}

function focusOnNextCompte() {
	for (i=0; i<maxRows; i++) {
		if (document.getElementById("idext" + i).value == "") {
			self.focus();
   			document.getElementById("idext" + i).focus();

   			return;
		}
	}
}

function showMontantEtranger() {
	for (i=0; i<maxRows; i++) {
		if (document.getElementById("me" + i).value != null && document.getElementById("me" + i).value != "0.00") {
			document.getElementById("me" + i).className = "montantShort";
			document.getElementById("me" + i).disabled = false;
			document.getElementById("me" + i).readOnly = false;
		}

		if (document.getElementById("c" + i).value != null && document.getElementById("c" + i).value != "0.00000") {
			document.getElementById("c" + i).className = "montantShort";
			document.getElementById("c" + i).disabled = false;
			document.getElementById("c" + i).readOnly = false;
		}
	}
}

function disableCentreCharge() {
	for (i=0; i<maxRows; i++) {
		if (document.getElementById("idcc" + i).value == 0) {
			document.getElementById("idcc" + i).className = "selectDisabled";
			document.getElementById("idcc" + i).disabled = true;
		}
	}
}

/**
 * Appel a la methode de mise a jour du compte
 */
function updateCompte(tag, i) {
	if (tag.select) {
		var element = tag.select.options[tag.select.selectedIndex];
		updateCompteCorps(element, i); // Appel au coeur de la methode pour mise a jour
	}
}

/**
 * Corps de la methode de mise a jour d'un compte
 */
function updateCompteCorps(element, i) {
	document.getElementById("idc" + i).value = element.idCompte;

	if (element.idNature == <%=globaz.helios.db.comptes.CGCompte.CS_CENTRE_CHARGE%>) {
		document.getElementById("idcc" + i).className = "libelle";
		document.getElementById("idcc" + i).disabled = false;
		document.getElementById("idcc" + i).className = "selectEnabled";

		if (element.defaultIdCentreCharge >= 0) {
			document.getElementById("idcc" + i).value = element.defaultIdCentreCharge;
		}
	} else {
		document.getElementById("idcc" + i).className = "selectDisabled";
		document.getElementById("idcc" + i).disabled = true;
		document.getElementById("idcc" + i).value = "0";
	}

	if (element.idNature == <%=globaz.helios.db.comptes.CGCompte.CS_MONNAIE_ETRANGERE%>) {
		document.getElementById("me" + i).className = "montantShort";
		document.getElementById("me" + i).disabled = false;
		document.getElementById("me" + i).readOnly = false;
		document.getElementById("c" + i).className = "montantShort";
		document.getElementById("c" + i).disabled = false;
		document.getElementById("c" + i).readOnly = false;
	} else {
		document.getElementById("me" + i).className = "montantShortDisabled";
		document.getElementById("me" + i).disabled = true;
		document.getElementById("me" + i).readOnly = true;
		document.getElementById("me" + i).value = "0.00";
		document.getElementById("c" + i).className = "montantShortDisabled";
		document.getElementById("c" + i).disabled = true;
		document.getElementById("c" + i).readOnly = true;
		document.getElementById("c" + i).value = "0.00000";
	}
}

function updateSum() {
	var decote=/'/g;

	montantDebit = parseFloat('0.0');
	montantCredit = parseFloat('0.0');
	colPourcentageDeb = parseFloat('0.0');
	colPourcentageCred = parseFloat('0.0');
	montantEtranger = parseFloat('0.0');

	for (i=0; i<maxRows; i++) {
		if (document.getElementById("md" + i).value != null && document.getElementById("md" + i).value != '') {
			tmp = document.getElementById("md" + i).value;
			tmp = tmp.replace(decote,"");
			montantDebit += parseFloat(tmp);
		}

		if (document.getElementById("mc" + i).value != null && document.getElementById("mc" + i).value != '') {
			tmp = document.getElementById("mc" + i).value;
			tmp = tmp.replace(decote,"");
			montantCredit += parseFloat(tmp);
		}

		if (document.getElementById("me" + i).value != null && document.getElementById("me" + i).value != '') {
			tmp = document.getElementById("me" + i).value;
			tmp = tmp.replace(decote,"");
			montantEtranger += parseFloat(tmp);
		}

		if (document.getElementById("colPourcentageDebInput" + i).value != null && document.getElementById("colPourcentageDebInput" + i).value != '') {
			tmp = document.getElementById("colPourcentageDebInput" + i).value;
			tmp = tmp.replace(decote,"");
			colPourcentageDeb += parseFloat(tmp);
		}
		
		if (document.getElementById("colPourcentageCredInput" + i).value != null && document.getElementById("colPourcentageCredInput" + i).value != '') {
			tmp = document.getElementById("colPourcentageCredInput" + i).value;
			tmp = tmp.replace(decote,"");
			colPourcentageCred += parseFloat(tmp);
		}
	}
	

	document.getElementById("sd").value = '';
	document.getElementById("sd").value = montantDebit+'';
	validateFloatNumber(document.getElementById("sd"));

	document.getElementById("sc").value = '';
	document.getElementById("sc").value = montantCredit+'';
	validateFloatNumber(document.getElementById("sc"));

	document.getElementById("se").value = '';
	document.getElementById("se").value = montantEtranger+'';
	validateFloatNumber(document.getElementById("se"));

	document.getElementById("spd").value = '';
	document.getElementById("spd").value = colPourcentageDeb+'';
	validateFloatNumber(document.getElementById("spd"));

	document.getElementById("spc").value = '';
	document.getElementById("spc").value = colPourcentageCred+'';
	validateFloatNumber(document.getElementById("spc"));

	document.getElementById("btnVal").disabled = true;

	if ( montantDebit.toFixed(2) == montantCredit.toFixed(2) && montantDebit.toFixed(2) != 0.0) {
		document.getElementById("btnVal").disabled = false;		
	} else if (colPourcentageDeb.toFixed(2) == colPourcentageCred.toFixed(2) && colPourcentageDeb.toFixed(2) != 0.0) {
		if(colPourcentageDeb.toFixed(2) == 100.0 && colPourcentageCred.toFixed(2) == 100.0) {
			document.getElementById("btnVal").disabled = false;
		}
	}
	
	updateBalance(montantDebit, montantCredit);
}

function updateBalance(montantDebit, montantCredit) {
	document.getElementById("bd").value = '';
	document.getElementById("bc").value = '';

	if (montantDebit > montantCredit) {
		tmp = montantDebit - montantCredit;
		document.getElementById("bc").value = tmp+'';
		validateFloatNumber(document.getElementById("bc"));
	} else if (montantDebit < montantCredit) {
		tmp = montantCredit - montantDebit;
		document.getElementById("bd").value = tmp+'';
		validateFloatNumber(document.getElementById("bd"));
	}
}

function clearDebitCredit(debitOrCredit, i) {
	document.getElementById(debitOrCredit + i).value = "";
}

function equilibrate() {
	var decote=/'/g;

	montantDebit = parseFloat('0.0');
	montantCredit = parseFloat('0.0');

	count = 0;

	for (i=0; i<maxRows; i++) {
		if (document.getElementById("md" + i).value != null && document.getElementById("md" + i).value != '' && document.getElementById("md" + i).value != '0.00') {
			tmp = document.getElementById("md" + i).value;
			tmp = tmp.replace(decote,"");
			montantDebit += parseFloat(tmp);

			if (montantDebit != 0.00) {
				count++;
			}
		}

		if (document.getElementById("mc" + i).value != null && document.getElementById("mc" + i).value != '' && document.getElementById("mc" + i).value != '0.00') {
			tmp = document.getElementById("mc" + i).value;
			tmp = tmp.replace(decote,"");
			montantCredit += parseFloat(tmp);

			if (montantCredit != 0.00) {
				count++;
			}
		}
	}

	if (montantDebit != montantCredit && count != nextRowToShow) {
		index = nextRowToShow - 1;
		if (montantDebit > montantCredit) {
			tmp = montantDebit - montantCredit;
			document.getElementById("mc" + index).value = tmp;
			validateFloatNumber(document.getElementById("mc" + index));
			clearDebitCredit('md', index);
		} else {
			tmp = montantCredit - montantDebit;
			document.getElementById("md" + index).value = tmp;
			validateFloatNumber(document.getElementById("md" + index));
			clearDebitCredit('mc', index);
		}
	}

	updateSum();
}

function updateMontantChf(i) {
	var decote=/'/g;

	if (document.getElementById("me" + i).value != null && document.getElementById("me" + i).value != '' && document.getElementById("me" + i).value != '0.00' 
		&& 
		document.getElementById("c" + i).value != null && document.getElementById("c" + i).value != '' && document.getElementById("c" + i).value != '0.00000') {
		tmpEtr = document.getElementById("me" + i).value;
		tmpEtr = tmpEtr.replace(decote,"");
		
		cours = document.getElementById("c" + i).value;
		
		tmp = Math.round((parseFloat(tmpEtr) * parseFloat(cours))*100)/100;
		
		if (document.getElementById("md" + i).value == '0.00') {
			document.getElementById("md" + i).value = tmp+'';
			validateFloatNumber(document.getElementById("md" + i));
		} else if (document.getElementById("mc" + i).value == '0.00') {
			document.getElementById("mc" + i).value = tmp+'';
			validateFloatNumber(document.getElementById("mc" + i));
		}
	}
}

function onCompteFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert(" Le compte n'existe pas.");
	}
}

function onLibelleFailure(event) {
	//si touche different de [DEL] ou [BACKSPACE]
	if(event.keyCode != 8 && event.keyCode != 46) {
		alert("Le libellé n'existe pas.");
	}
}

//Permet de remettre a l'etat initial (vide) toute la ventilation
function resetVentilation() {

	//On ne réaffiche que les lignes par défaut
	nextRowToShow = <%=viewBean.getShowRows()%>;
	hideRows();

	//On efface tous les champs
	for (i=0; i<maxRows; i++) {
		document.getElementById("idext" + i).value = "";
		document.getElementById("idc" + i).value = "";
		document.getElementById("idcc" + i).value = "0";
		document.getElementById("l" + i).value = "";
		if(i > 0) { 
			document.getElementById("md" + i).value = "0.00";
			document.getElementById("mc" + i).value = "";
		} else {
			document.getElementById("md" + i).value = "";
			document.getElementById("mc" + i).value = "0.00";
		}
		
		document.getElementById("me" + i).value = "0.00";
		document.getElementById("c" + i).value = "0.00";
		document.getElementById("sd").value = "0.00";
		document.getElementById("sc").value = "0.00";
		document.getElementById("se").value = "";
	}
}


</SCRIPT>