//------------------------------------------------------------
// Container collection for CO4 Annonces
//------------------------------------------------------------
var annoncesCO4 = new Array;

//------------------------------------------------------------
// Auxiliary class to store extended data for CO4
//------------------------------------------------------------
function AnnonceCO4(_annonce, _annonceId, _caisse, _dateAnnonce) {
	this.annonce = _annonce; // Base annonce defined in baseObjects.js
	this.annonceId = _annonceId;
	this.caisse = _caisse;
	this.dateAnnonce = _dateAnnonce;
}

//------------------------------------------------------------
// Clean and load the modal dialog
//------------------------------------------------------------
function showdetailCO4(_sedexId){
	for(var x=0; x<annoncesCO4.length;x++){
		var annonceCO4 = annoncesCO4[x];
		if(annonceCO4 != undefined && annonceCO4.annonceId == _sedexId){
			// Clean up previous information of DIV container
			var tblContainer = document.getElementById("dlgAnnonceDetailCO4");
			var oldTable = document.getElementById("tblAnnonceContainerCO4");
			tblContainer.removeChild(oldTable);
			
			var annonceDetail = annonceCO4.annonce;
			var titreAnnonce = annonceDetail.title;
			var subtitreAnnonce = annonceDetail.subtitle;
			var avsDebiteur = annonceDetail.debiteur.avs;
			var nomPrenomDebiteur = annonceDetail.debiteur.nomPrenom;
			var interets = annonceDetail.interets;
			var frais = annonceDetail.frais;
			var total = annonceDetail.total;
	
			$('#dlgAnnonceDetailCO4').dialog('option', 'title', titreAnnonce); // Set dialog title
			var textToInsert = '<table id="tblAnnonceContainerCO4" width="100%">';
			textToInsert += '<col width="10"></col>'; // empty col only to format view
			textToInsert += '<col align="left" width="100"></col>'; // Fix text Débiteur, Assuré, Intérêts, Frais et Total
			textToInsert += '<col align="left" width="150"></col>'; // Data
			textToInsert += '<col align="left"></col>'; // Data
			textToInsert += '<col align="right" width="100"></col>';// Data financial
			textToInsert += '<col align="left" width="50"></col>';// empty col only to format view
	
			// Body title section
			textToInsert += '<tr><td></td><td colspan=6><b>' + subtitreAnnonce + '</b></td></tr>';
			
			// Caisse info
			textToInsert += '<tr><td></td><td>Caisse</td>';
			textToInsert += '<td><b>' + annonceCO4.caisse + '</td>';
			textToInsert += '<td colspan="2" align="right"><b>Re&ccedil;u le ' + annonceCO4.dateAnnonce +'</b></td></tr>';
			textToInsert += emptyRow;
			
			// Débiteur section
			textToInsert += '<tr><td></td><td>D&eacute;biteur</td>';
			textToInsert += '<td>' + avsDebiteur + '</td>';
			textToInsert += '<td>' + nomPrenomDebiteur +'</td></tr>';
			textToInsert += emptyRow + emptyRow + emptyRow;
	
			// Assure collection section
			for(var i=0; i<annonceDetail.assure.length;i++){
				var currentAssure = annonceDetail.assure[i];
				if(currentAssure != undefined){
					if(i==0){
						textToInsert += '<tr><td></td><td>Assur&eacute;</td>';	
					}else{
						textToInsert += '<tr><td></td><td></td>';
					}
					
					textToInsert += '<td style="font-style:italic;">' + currentAssure.assure.avs + '</td>';
					textToInsert += '<td style="font-style:italic;">' + currentAssure.assure.nomPrenom +'</td></tr>';
		
					//Financial detail
					for(var j=0; j<currentAssure.financialElement.length;j++){
						var currentDetail = currentAssure.financialElement[j];
						textToInsert += '<tr><td></td><td></td>';
						textToInsert += '<td>' + currentDetail.description + '</td>';
						textToInsert += '<td>' + currentDetail.startDate + " - " + currentDetail.endDate + '</td>';
						textToInsert += '<td>' + currentDetail.value +'</td></tr>';
					}
					textToInsert += emptyRow;
				}
			}
			
			textToInsert += emptyRow + emptyRow + emptyRow;
			
			// Intérêts section
			textToInsert += '<tr><td></td><td colspan="3">Int&eacute;r&ecirc;ts</td>';
			textToInsert += '<td>' + interets +'</td></tr>';
			
			// Frais section
			textToInsert += '<tr><td></td><td colspan="3">Frais</td>';
			textToInsert += '<td>' + frais +'</td></tr>';
			textToInsert += emptyRow + emptyRow + emptyRow;
			
			// Total section
			textToInsert += '<tr><td></td><td colspan="3"><b>Total</b></td>';
			textToInsert += '<td><b>' + total +'</b></td></tr>';
			textToInsert += emptyRow;
			textToInsert += emptyRow;
			
			textToInsert += '</table>';
			
			$('#dlgAnnonceDetailCO4').append(textToInsert);
			$('#dlgAnnonceDetailCO4').dialog({height:'400px', minHeight :'300px', width:'700'});
			$('#dlgAnnonceDetailCO4').dialog('open');
		}
	}
}