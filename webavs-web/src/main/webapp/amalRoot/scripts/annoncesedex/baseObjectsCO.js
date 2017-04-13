$(document).ready(function() {
	$("#selectSedex").on("change", function() {
		var idDetailFamille = $(this).val();
		if (idDetailFamille == 'all') {
			$(".sedexLine").show();	
		} else {
			$(".sedexLine").hide();
			$(".sedexLine."+idDetailFamille).show();
		}
	});
	
    $('#dlgAnnonceDetailCO2').dialog({
			autoOpen:false,
			modal:true,
			closeOnEscape:true,
			draggable : true,
			resizable : true
		});
    
    $('#dlgAnnonceDetailCO4').dialog({
			autoOpen:false,
			modal:true,
			closeOnEscape:true,
			draggable : true,
			resizable : true
		});
});


/* Annonces container*/
var assureArray = new Array;
var emptyRow = '<tr height="20"/>';

function Personne(_noAvs, _nomPrenom){
	this.avs = _noAvs;
	this.nomPrenom = _nomPrenom;
}

function DetailFinance(_description, _startDate, _endDate, _value){
	this.description = _description; // Prime or Participation
	this.startDate = _startDate;
	this.endDate = _endDate;
	this.value = _value;
}

function DetailAssure(_objAssure, _objFinancial){
	this.assure = _objAssure; // Type Personne
	this.financialElement = _objFinancial; // Type DetailFinance
}

function Annonce(_annonceId, _title, _subtitle, _interets, _frais, _total, _objDebiteur, _objAssure) {
	this.annonceId = _annonceId;
	this.title = _title;
	this.subtitle = _subtitle;
	this.interets = _interets;
	this.frais = _frais;
	this.total = _total;
	this.debiteur = _objDebiteur; // Type personne seulement 1 element
	this.assure = _objAssure; // Collection type DetailAssure
}