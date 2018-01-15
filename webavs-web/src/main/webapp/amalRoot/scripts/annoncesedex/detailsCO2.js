//------------------------------------------------------------
// Clean and load the modal dialog
//------------------------------------------------------------
function showdetailCO2(_annonceIdx){
	// Clean up previous information of DIV container
	var tblContainer = document.getElementById("dlgAnnonceDetailCO2");
	var oldTable = document.getElementById("tblAnnonceContainerCO2");
	tblContainer.removeChild(oldTable);

	if(annonceDetail != undefined){
		var titreAnnonce = annonceDetail[_annonceIdx].title;
		var subtitreAnnonce = annonceDetail[_annonceIdx].subtitle;
		var avsDebiteur = annonceDetail[_annonceIdx].debiteur.avs;
		var nomPrenomDebiteur = annonceDetail[_annonceIdx].debiteur.nomPrenom;
		var interets = annonceDetail[_annonceIdx].interets;
		var frais = annonceDetail[_annonceIdx].frais;
		var total = annonceDetail[_annonceIdx].total;

		$('#dlgAnnonceDetailCO2').dialog('option', 'title', titreAnnonce); // Set dialog title
		var textToInsert = '<table id="tblAnnonceContainerCO2" width="100%">';
		textToInsert += '<col width="10"></col>'; // empty col only to format view
		textToInsert += '<col align="left" width="100"></col>'; // Fix text Débiteur, Assuré, Intérêts, Frais et Total
		textToInsert += '<col align="left" width="150"></col>'; // Data
		textToInsert += '<col align="left"></col>'; // Data
		textToInsert += '<col align="right" width="100"></col>';// Data financial
		textToInsert += '<col align="left" width="50"></col>';// empty col only to format view

		// Body title section
		textToInsert += '<tr><td></td><td colspan=6><h2>' + subtitreAnnonce + '</h2></td></tr>';
		
		// Débiteur section
		textToInsert += '<tr><td></td><td>D&eacute;biteur</td>';
		textToInsert += '<td>' + avsDebiteur + '</td>';
		textToInsert += '<td>' + nomPrenomDebiteur +'</td></tr>';
		textToInsert += emptyRow + emptyRow + emptyRow;

		// Assure collection section
		for(var i=0; i<annonceDetail[_annonceIdx].assure.length;i++){
			var currentAssure = annonceDetail[_annonceIdx].assure[i];
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
		
		$('#dlgAnnonceDetailCO2').append(textToInsert);
		$('#dlgAnnonceDetailCO2').dialog({height:'400px', minHeight :'300px', width:'700'});
		$('#dlgAnnonceDetailCO2').dialog('open');
	}			
}