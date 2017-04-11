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
	
 	
	// Container Modal dialog used to display annonce details
    $('#dlgAnnonceDetail').dialog(
    		{
    			autoOpen:false,
    			modal:true,
    			closeOnEscape:true,
    			draggable : true,
    			resizable : true,
    			width : 700,
    			height: "auto",
    			minHeight:400,
    			minWidth:600
		});
});

//------------------------------------------------------------
// Clean and load the modal dialog
//------------------------------------------------------------
function showdetail(_sedexId){
	// Clean up previous information of DIV container
	var tblContainer = document.getElementById("dlgAnnonceDetail");
	var oldTable = document.getElementById("tblAnnonceContainer");
	tblContainer.removeChild(oldTable);
	
	var c1 = annonceDetail.title;
	var c2 = annonceDetail.subtitle;
	var c3 = annonceDetail.debiteur.avs;
	var c4 = annonceDetail.debiteur.prenom;
	var c5 = annonceDetail.debiteur.nom;
	var c6 = annonceDetail.interets;
	var c7 = annonceDetail.frais;
	var c8 = annonceDetail.total;

	$('#dlgAnnonceDetail').dialog('option', 'title', c1); // Set dialog title
	var emptyRow = '<tr height="20"/>';
	var textToInsert = '<table id="tblAnnonceContainer" width="100%">';
	textToInsert += '<col width="10"></col>'; // empty col only to format view
	textToInsert += '<col align="left" width="100"></col>'; // Fix text Débiteur, Assuré, Intérêts, Frais et Total
	textToInsert += '<col align="left" width="150"></col>'; // Data
	textToInsert += '<col align="left"></col>'; // Data
	textToInsert += '<col align="right" width="100"></col>';// Data financial
	textToInsert += '<col align="left" width="50"></col>';// empty col only to format view

	// Body title section
	textToInsert += '<tr><td></td><td colspan=6><h2>' + c2 + '</h2></td></tr>';
	
	// Débiteur section
	textToInsert += '<tr><td></td><td>D&eacute;biteur</td>';
	textToInsert += '<td>' + c3 + '</td>';
	textToInsert += '<td>' + c4 + c5 +'</td></tr>';
	textToInsert += emptyRow + emptyRow + emptyRow;

	// Assuré section loop on elements 
	
	
	// Intérêts section
	textToInsert += '<tr><td></td><td colspan="3">Int&eacute;r&ecirc;ts</td>';
	textToInsert += '<td>' + c6 +'</td></tr>';
	
	// Frais section
	textToInsert += '<tr><td></td><td colspan="3">Frais</td>';
	textToInsert += '<td>' + c7 +'</td></tr>';
	textToInsert += emptyRow + emptyRow + emptyRow;
	
	// Total section
	textToInsert += '<tr><td></td><td colspan="3"><b>Total</b></td>';
	textToInsert += '<td><b>' + c8 +'</b></td></tr>';
	textToInsert += emptyRow;
	textToInsert += emptyRow;
	
	textToInsert += '</table>';
	
	$('#dlgAnnonceDetail').append(textToInsert);
	$('#dlgAnnonceDetail').dialog('open');
}

////------------------------------------------------------------
////Appel Ajax pour générer l'annonce 'Demande de rapport d'assurance'
////------------------------------------------------------------
//function generateInsuranceQuery(_currentIdDetailFamille, s_arrayCaisses, anneeHistorique) {
//	var o_options= {
//			serviceClassName: 'ch.globaz.amal.business.services.sedexRP.AnnoncesRPService',
//			serviceMethodName:'initAnnonceDemandeRapport',
//			parametres:_currentIdDetailFamille+","+s_arrayCaisses+","+anneeHistorique,
//			callBack: callBackSendAnnonceInsuranceQuery
//	}
//	globazNotation.readwidget.options=o_options;		
//	globazNotation.readwidget.read();	
//}