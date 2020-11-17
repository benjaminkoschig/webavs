var docMagnifier="";
var docMagnifierPath="";
var nbElemHistoEnvoi = 0;
var nbElemEnvoiModifs = 0;
var nbElemHistoSedex = 0;
var idSedexLineHighlighted = "";

$(document).ready(function() {		
	
	//
    // Select all the a tag with name equal to modal
    //
    // Action pour afficher la sélection des templates
    // -----------------------------------------------
    $('#buttonModal').click(function(e) {
        //Cancel the link behavior
        e.preventDefault();
        //Get the A tag
        //var id = $(this).attr('href');
        var id = $(this).attr('name');
     
        //Get the screen height and width
        var maskHeight = $(document).height();
        var maskWidth = $(window).width();
     
        //Set height and width to mask to fill up the whole screen
        $('#mask').css({'width':maskWidth,'height':maskHeight});
         
        //transition effect     
        $('#mask').fadeIn(0);    
        $('#mask').fadeTo("fast",0.8);  
     
        //Get the window height and width
        var winH = $(window).height();
        var winW = $(window).width();
               
        //Set the popup window to center
        $(id).css('top',  winH/2-$(id).height()/2);
        $(id).css('left', winW/2-$(id).width()/2);
     
        //transition effect
        $(id).fadeIn(0);
        
    	$('#noModele').focus();

    });
     
    //if close button is clicked
    $('.window .close').click(function (e) {
        //Cancel the link behavior
        e.preventDefault();
        $('#mask, .window').hide();
    });     
    //
    // Action sur l'input noCaisseMaladieVisible
    //
    // key-up >> controle de l'affichage de la liste des cm
    // -------------------------------------------------
    $('#noCaisseMaladieVisible').keyup(function(e) {
    	// Afficher la liste si ne contient pas "-"
    	// Adapter la liste avec la nouvelle longeur de recherche
    	var chaineSaisie=$('#noCaisseMaladieVisible').val();
		if(chaineSaisie.indexOf("-")== -1){
			adaptCMList(chaineSaisie);
			adaptSuggestChoice();
		    $('#autoSuggestContainer').show();
		}else{
		    $('#autoSuggestContainer').hide();
		}
    	// Touche bas, met le focus sur la liste
    	if(e.which==40){
    		$('#autoSuggestContainer').focus();
    		$('#cmSuggestChoice').focus();
    		$('#cmSuggestChoice option:first').focus();
    	}
    	// Touche ESC, cacher le champ
    	if(e.which==27){
		    $('#autoSuggestContainer').hide();
    	}
	});
    $('#cmSuggestChoice').keyup(function(e) {
    	// Touche Enter, sélection et retour dans champ input
    	if(e.which==13){
    		// Get and Set Value
    		var myValue = $('#cmSuggestChoice option:selected').val();
    		var myText = $('#cmSuggestChoice option:selected').text();
    		$('#noCaisseMaladieVisible').attr('value',myText);
    		$('#noCaisseMaladie').attr('value',myValue);
    		// Set Focus
    		$('#noCaisseMaladieVisible').focus();
    		// hide the zone
    		$('#autoSuggestContainer').hide();
    	}
    }).dblclick(function(){
		// Get and Set Value
		var myValue = $('#cmSuggestChoice option:selected').val();
		var myText = $('#cmSuggestChoice option:selected').text();
		$('#noCaisseMaladieVisible').attr('value',myText);
		$('#noCaisseMaladie').attr('value',myValue);
		// Set Focus
		$('#noCaisseMaladieVisible').focus();
		// hide the zone
		$('#autoSuggestContainer').hide();
	});
    attachAction();
    
    $("#linkNewAnnonceFromSubside").live("click",function() {
    	attachActionCheckCM();
    });
    
    
    $(".sedexLine td").live("mouseover",function() {
    	var classToHighlight = $(this).parent().attr("class").replace(/\ /g,'.');
    	if ($(this).attr("id") ==  idSedexLineHighlighted && $(this).css("font-weight") == 700) {
    		$("."+classToHighlight).css("font-weight","normal");
    	} else {
    		$(".sedexLine").css("font-weight","normal");    		
    		$("."+classToHighlight).css("font-weight","bold");
    		idSedexLineHighlighted = $(this).attr("id");
    	}
    });
    
    $("#decreeStopCaisse, #createDecreeStop").click(function() {
    	$("#typeNewAnnonce_decreeStop").click();

		var myValue = $('#createDecreeStop option:selected').val();
		if(myValue === "801") {
			$('#membreFamille_div').show();
			$('#decreeStopCaisse_div').hide();
		} else {
			$('#membreFamille_div').hide();
			$('#decreeStopCaisse_div').show();
		}
    });
    
});

function attachActionCheckCM() {
	$( "#dialogNewAnnonceRP" ).dialog({
		minHeight:"200px",
		height:"auto",
		width:"500px",
		autoOpen: true,
        modal: true,
        buttons: {
            Ok: function() {               	
            	var button = $(".ui-dialog-buttonpane button:contains('Ok')");
                $(button).button("disable");
            	var s_tmp = $("#searchModel\\.inIdTiersCaisse").val().join();
            	var s_arrayCaisses = s_tmp.replace(/\,/g,'|');
            	var type = $(".typeNewAnnonce:checked").val();
            	if (type=="decreeStop") {
            		var annonceACreer = $("#createDecreeStop").val();
            		if (annonceACreer == "101") {
            			generateDecreeStop(currentIdDossier,$("#idContribuable").val(), "101", $("#decreeStopCaisse").val());
            		} else if (annonceACreer == "201") {
            			generateDecreeStop(currentIdDossier,$("#idContribuable").val(), "201", $("#decreeStopCaisse").val());
            		} else if (annonceACreer == "801") {
            			var membreFamille = $("#membreFamille_decreeStop").is(":checked");
            			var anneeHistorique = $("#anneeHistorique").val();
            			var idFamille =  $("#idFamille").val();
						generateDecreeStopDemandePT(currentIdDossier,$("#idContribuable").val(),idFamille, anneeHistorique, membreFamille);
					}
            	} else {
                	generateInsuranceQuery(currentIdDossier,s_arrayCaisses,$("#anneeHistorique").val());
            	}
            },
            Annuler: function() {
                $( this ).dialog( "close" );
            }
        }
    });
}


//------------------------------------------------------------
//Appel Ajax pour générer l'annonce 'Demande de rapport d'assurance'
//------------------------------------------------------------
function generateInsuranceQuery(_currentIdDetailFamille, s_arrayCaisses, anneeHistorique) {
	var o_options= {
			serviceClassName: 'ch.globaz.amal.business.services.sedexRP.AnnoncesRPService',
			serviceMethodName:'initAnnonceDemandeRapport',
			parametres:_currentIdDetailFamille+","+s_arrayCaisses+","+anneeHistorique,
			callBack: callBackSendAnnonceInsuranceQuery
	}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}

//------------------------------------------------------------
//Appel Ajax pour générer une annonce Decree ou Stop
//------------------------------------------------------------
function generateDecreeStop(_currentIdDetailFamille, _currentIdContribuable, _type, idTiersCaisse) {
	var o_options= {
			serviceClassName: 'ch.globaz.amal.business.services.sedexRP.AnnoncesRPService',
			serviceMethodName:'initDecreeStopFromJsp',
			parametres:_currentIdDetailFamille+","+_currentIdContribuable+","+_type+","+idTiersCaisse,
			callBack: callBackSendAnnonceInsuranceQuery
	}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}

//------------------------------------------------------------
//Appel Ajax pour générer une annonce demande prime tarofaire
//------------------------------------------------------------
function generateDecreeStopDemandePT(_currentIdDetailFamille, _currentIdContribuable, _currentIdFamille, _currentAnneeHistorique, membreFamille) {
	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.sedexRP.AnnoncesRPService',
		serviceMethodName:'initDecreeStopForDemandePTFromJsp',
		parametres:_currentIdDetailFamille+","+_currentIdContribuable+","+_currentIdFamille+","+_currentAnneeHistorique+","+membreFamille,
		callBack: callBackSendAnnonceInsuranceQuery
	}
	globazNotation.readwidget.options=o_options;
	globazNotation.readwidget.read();
}

function callBackSendAnnonceInsuranceQuery(data) {
	window.location.reload();
}

/**
 * Toggle the visibility of an graphic element
 */
function toggleOpacityElement(idElement, opacityHidden, opacityShown){
	if($('#'+idElement).css('opacity') <= opacityHidden) {
		setOpacityElement(idElement,opacityShown);
		setDisabledElement(idElement, false);
	}else{
		setOpacityElement(idElement, opacityHidden);
		setDisabledElement(idElement, true);
	}		
}

/**
 * set the opacity of a specific element
 */
function setOpacityElement(idElement, myOpacity){
	$('#'+idElement).css({opacity:myOpacity});
}

/**
 * set the disabled attribute of a specific element
 */
function setDisabledElement(idElement, toDisabled){
	if(toDisabled){
		$('#'+idElement).prop('disabled','disabled');
	}else{
		$('#'+idElement).removeProp('disabled');
	}
}

/**
 * Cette méthode rattache les event. Nécessaire car le plugin jquery.contextMenu unbind tout lors de sa destruction
 */
function attachAction() {
    $(".divInfoSubsideHistoEnvoiExpand").live("click",function() {
    	hideAndShow(".divInfoSubsideHistoEnvoiExpand");
    });
    
    $(".divInfoSubsideEnvoiModifExpand").live("click",function() {
    	hideAndShow(".divInfoSubsideEnvoiModifExpand");
    });
    
    $(".divInfoSubsideSedexExpand").live("click",function() {
    	hideAndShow(".divInfoSubsideSedexExpand");
    });
    
//    $(".divInfoSubsideSedexExpand").live("click",function() {
//    	hideAndShow(".divInfoSubsideSedexExpand");
//    });
    
    $("#linkNewAnnonceFromSubside").live("click",function() {
    	attachActionCheckCM();
    });
}

function hideAndShow(elem) {
	//console.log($(elem));
	$(elem+"_Hidable").toggle();	
	if ($(elem+"_Hidable").is(":visible")) {
		$(elem).attr("src",amContextPath+"/images/icon-collapse.gif");
	} else {
		$(elem).attr("src",amContextPath+"/images/icon-expand.gif");
	}
}

//------------------------------------------------------------
// Adapte la liste des caisses maladies à afficher en fonction
// d'une string partielle
//------------------------------------------------------------
function adaptCMList(stringPartielle){
	CMList=new Array();
	var iIncrement=0;
	for(var iCaisse=0;iCaisse<allCM.length;iCaisse++){
		if(allCM[iCaisse].toUpperCase().indexOf(stringPartielle.toUpperCase())!=-1){
			CMList[iIncrement]=allCM[iCaisse];
			iIncrement++;
		}
	}
}
//------------------------------------------------------------
// Adapte l'élément input cmSuggestChoice avec la nouvelle liste
//------------------------------------------------------------
function adaptSuggestChoice(){
	// Remove all element
	$('#cmSuggestChoice option').each(function(){
		$(this).remove();
	});
	// Recreate all element
	for(var iCaisse=0;iCaisse<CMList.length;iCaisse++){
		var myValues = CMList[iCaisse].split(',');
		var toInsert='<option value="';
		toInsert+= myValues[1]+'">';
		toInsert+= myValues[0]+'</option>';
		$('#cmSuggestChoice').append(toInsert);
	}
}
//------------------------------------------------------------
// Action de génération de document (action du bouton Generate)
//------------------------------------------------------------
function onClickGenerateDocument(){
	
	var batchOrNotBatch = $('#listeModele').val().split(",")[3];
	var codeTraitementDossier = $('#listeModele').val().split(",")[4];
	var modeleIdValue = $('#modeleSpecimen').attr("class");
    document.forms[0].elements('modeleId').value = modeleIdValue;
    document.forms[0].elements('modeleType').value = jobManualEdited;

	if(batchOrNotBatch=="42001100"){
		onClickMerge();
	}else if (batchOrNotBatch=="42001101"){
		onClickQueue();
	}else{
		// hide the selection box
		$('#mask, .window').hide();
		alert("Génération du document impossible, catégorie Batch-Direct non spécifiée.");
	}
}

//------------------------------------------------------------
// Génération du document interactif
//------------------------------------------------------------
function onClickMerge(){

	// hide the selection box
	$('#mask, .window').hide();

	// Get the id of the document to merge
	var modeleIdValue = $('#modeleSpecimen').attr("class");
	document.forms[0].elements('action').value = "merge";
    document.forms[0].elements('modeleId').value = modeleIdValue;
    var modeleTypeValue=jobManualEdited;
    document.forms[0].elements('modeleType').value = modeleTypeValue;
	userAction.value = ACTION_DETAILFAMILLE+".createInteractivDocument";
	// submit and merge !
	// document.forms[0].submit();
	
	showProgress('#inProgressDialog');	
	$.post( "/webavs/amal",
			{userAction:userAction.value, action:"merge", modeleId:modeleIdValue,modeleType:modeleTypeValue},
			function(data){
				
				var errorMessage = "Error creating and loading Word Document.";
				errorMessage +="\r\n";
				errorMessage +="Data received from server : " + data;

				// check si data non vide
				// et valeur interne est une URL
				// --------------------------------
				if(data!=null && data.length>4 && data.substr(data.length-4,4)==".doc"){
					// si OK, ouverture du fichier et raffraichissement de la page
					// -----------------------------------------------------------
					try{
						var s_filepath=""+data;
						// check si chemin contient par ./persistence
						if(s_filepath.indexOf('persistence')>=0){
							s_filepath=s_filepath.substr(1);
							var s_httpError = 'Le document a été généré sur le serveur web (http).';
							s_httpError+= '\nLa sauvegarde et la mise en GED automatique ne peut être assurée.';
							s_httpError+= '\nVeuillez vérifier les paramètres de configuration de l\'application';
							alert(s_httpError);
						}
						
						var word=null;
						try {
					  		if(word==null){
					  			word = new ActiveXObject('Word.Application');
					  		}
						    word.application.visible="true";
					  	} catch(e){
						   	word = new ActiveXObject('Word.Application');
						    word.application.visible="true";
					  	}
					  	// 1) open
					    var currentDocument = word.documents.open(s_filepath);
						// 2) save as doc
					    //currentDocument.SaveAs(s_filepath,'.doc');
						//word.application.activedocument.saveas(filename=s_filepath,filetype='wdFormatDocument');
					    
						//mydoc = window.open(data);
						raffraichirPage();
					} catch (err){
						errorMessage+="\r\nError Description : "+err.description;
						alert(errorMessage);
						raffraichirPage();						
					}
				}else{
					// autrement affichage d'une erreur
					// -----------------------------------
					alert(errorMessage);
					raffraichirPage();
				}
			}
	);
}

//------------------------------------------------------------
// Raffraichissement de la page
//------------------------------------------------------------
function raffraichirPage(){
	location.reload();
}
//------------------------------------------------------------
// Ajout du document dans la queue des documents à générés (TOPAZ)
//------------------------------------------------------------
function onClickQueue(){

	// hide the selection box
	$('#mask, .window').hide();

	// Get the id of the document to merge
	// il est mentionné en tant que classe...
	var modeleId = $('#modeleSpecimen').attr("class");
	document.forms[0].elements('action').value = "merge";
    document.forms[0].elements('modeleId').value = modeleId;
    document.forms[0].elements('modeleType').value = jobManualQueued;
	userAction.value = ACTION_DETAILFAMILLE+".createInteractivDocument";
	// submit and merge !
	document.forms[0].submit();
}
//------------------------------------------------------------
// ShowMagnifier for the document
//------------------------------------------------------------
function ShowMagnifier(objectImage){
	var imagePath = docMagnifierPath+docMagnifier+".png";
	TJPzoom(objectImage, imagePath);
}
//------------------------------------------------------------
// trim tous les caractères qui ne sont pas compris
// entre 0 et 128
// ainsi que les espaces
//------------------------------------------------------------
function trimAll(sString) 
{
	var myString = sString;
	while (myString.substring(0,1) == ' ' 
			||
			myString.charCodeAt(0)<0
			||
			myString.charCodeAt(0)>127
			) 
	{ 
		myString = myString.substring(1, myString.length); 
	} 
	while (myString.substring(myString.length-1, myString.length) == ' '
			||
			myString.charCodeAt(myString.length-1)<0
			||
			myString.charCodeAt(myString.length-1)>127
			) 
	{ 
		myString = myString.substring(0,myString.length-1); 
	}
	return myString; 
} 

//------------------------------------------------------------
// Change la vignette et le code utilisateur du champ de saisie
// du formulaire de génération de document
//------------------------------------------------------------
function vignette_request(value) {
	// Change vignette
	var shortCode = trimAll(value.split(",")[0]);
	var id = trimAll(value.split(",")[1]);
	var cu = trimAll(value.split(",")[2]);
	$('#modeleSpecimen').attr("src",amContextPath+"/images/amal/"+shortCode+"preview.png");
	docMagnifier=shortCode;
	$('#modeleSpecimen').attr("class",id);
	// Change code in noModele text input
	$('#noModele').attr("value",cu);

}
//------------------------------------------------------------
// change la valeur de la combobox en fonction du champ de saisie
// du formulaire de génération de document
//------------------------------------------------------------
function combo_request(cu){
	// remove selected attribute
	$('#listeModele option').each(function () {
		$(this).removeAttr("selected");
    });
	// select the new one
	var s_Value="";
	var n_Index=-1;
	$('#listeModele option').each(function () {
		s_Value=$(this).attr("value");
		n_Index=parseInt(trimAll(s_Value.split(",")[2]));
		if(n_Index==cu){
			$(this).attr("selected","selected");
			codeTraitementDossier = trimAll(s_Value.split(",")[4]);
			return false;
		}
    });
	// change of the preview
	var shortCode = trimAll(s_Value.split(",")[0]);
	$('#modeleSpecimen').attr("src",amContextPath+"/images/amal/"+shortCode+"preview.png");
	docMagnifier=shortCode;
	$('#modeleSpecimen').attr("class",trimAll(s_Value.split(",")[1]));
}


//------------------------------------------------------------
// Génération du tableau d'historique
//------------------------------------------------------------
var textToInsert = '';
var currentIdDetailFamille = '';

function generateHistorique(_currentIdDetailFamille){
	currentIdDetailFamille=_currentIdDetailFamille;
	generateHistoriqueAnnonce(currentIdDetailFamille);
}

//------------------------------------------------------------
// Appel Ajax pour générer l'historique des annonces
//------------------------------------------------------------
function generateHistoriqueAnnonce(_currentIdDetailFamille) {
	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.models.detailfamille.DetailFamilleService',
		serviceMethodName:'getHistoriqueAnnonces',
		parametres:_currentIdDetailFamille,
		callBack: createHistoriqueAnnonces
	}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}

//------------------------------------------------------------
//Appel Ajax pour générer l'historique des envois
//------------------------------------------------------------
function generateHistoriqueEnvois(_currentIdDetailFamille) {
	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.models.detailfamille.DetailFamilleService',
		serviceMethodName:'getHistoriqueEnvois',
		parametres:_currentIdDetailFamille,
		callBack: createHistoriqueEnvois
	}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}
//------------------------------------------------------------
// CallBack Ajax pour générer l'historique des annonces
//------------------------------------------------------------
function createHistoriqueAnnonces(data) {
	//nbElemHistoEnvoi = data.length;
	for(iElement=0; iElement< data.length; iElement++){
		nbElemHistoEnvoi++;
		textToInsert += '<tr class="divInfoSubsideHistoEnvoiExpand_Hidable" style="font-weight: bold;">';
		textToInsert += '<td></td>';
		textToInsert += '<td colspan="2" align="left">'+data[iElement].simpleAnnonce.dateAvisRIP+'</td>';
		textToInsert += '<td colspan="8" align="left">Annonce CM : ';
		var myText = '';
		// Find the CM in the list
		for(var iCaisse=0;iCaisse<allCM.length;iCaisse++){
			var myValues = allCM[iCaisse].split(',');
			if(myValues[1]==data[iElement].simpleAnnonce.noCaisseMaladie){
				myText = myValues[0];
				break;
			}
		}
		textToInsert += myText+ ",  ";
		textToInsert += data[iElement].simpleAnnonce.montantContribution;
		textToInsert += ",  ";
		textToInsert += data[iElement].simpleAnnonce.debutDroit;
		if(data[iElement].simpleAnnonce.finDroit!=''){
			textToInsert+= ' - '+data[iElement].simpleAnnonce.finDroit;
		}else{
			textToInsert+= ' - 12.'+data[iElement].simpleAnnonce.anneeHistorique;
		}
		textToInsert += '</tr>';
	}
	if(data.length == 0){
		textToInsert += '<tr class="divInfoSubsideHistoEnvoiExpand_Hidable"><td></td><td colspan="11">Pas d\'annonce caisse-maladie effectué</td></tr>';
	}
	textToInsert += '<tr class="divInfoSubsideHistoEnvoiExpand_Hidable"><td>&nbsp;</td></tr>';
	generateHistoriqueEnvois(currentIdDetailFamille);
}
//------------------------------------------------------------
//CallBack Ajax pour générer l'historique des envois
//------------------------------------------------------------
function createHistoriqueEnvois(data) {
	var documentSent = 0;
	var documentInProgress = 0;
	// Documents déjà envoyé
	for(iElement=0; iElement< data.length; iElement++){
		if(data[iElement].statusEnvoi == '42002403'){//SENT
			nbElemHistoEnvoi++;
			textToInsert += '<tr class="divInfoSubsideHistoEnvoiExpand_Hidable"><td></td>';
			textToInsert += '<td colspan="2" align="left">'+data[iElement].dateEnvoi+'</td>';
			textToInsert += '<td title="'+data[iElement].libelleEnvoi+'" colspan="6" align="left">';
			var libelleDoc = data[iElement].numModele;
			var libelleShort = data[iElement].numModele;
			var cuDoc = data[iElement].numModele;
			if(allCSDocument[data[iElement].numModele]!=undefined){
				libelleDoc = allCSDocument[data[iElement].numModele].libelle;
				libelleShort = allCSDocument[data[iElement].numModele].libelleShort;
				cuDoc = allCSDocument[data[iElement].numModele].cu;
			}
			textToInsert += libelleDoc+'</td>';
			textToInsert += '<td align="right">'+cuDoc+'</td>';
			textToInsert += '<td align="left" width="150px" title="'+libelleDoc+'">- '+libelleShort+'</td>';
			textToInsert += '</tr>';
			documentSent ++;
		}
	}
	if(documentSent==0){
		textToInsert+= '<tr class="divInfoSubsideHistoEnvoiExpand_Hidable"><td></td><td colspan="9">Pas d\'historique disponible</td></tr>';
	}
	
	textToInsert += '<tr class="divInfoSubsideHistoEnvoiExpand_Hidable"><td>&nbsp;</td></tr>';
	textToInsert += '<tr style="font-style:italic" style="font-weight: bold; background-color:#eeeeee">';
	textToInsert += '<td style="border-bottom: 1px solid black" ></td>';
	textToInsert += '<td colspan="11" style="border-bottom: 1px solid black" >';
	textToInsert += '	<img class="divInfoSubsideEnvoiModifExpand" src="'+amContextPath+'/images/icon-collapse.gif" width="12px" height="12px"/>';
	textToInsert += ' 	Envois et annonces modifiables';
	textToInsert += '	<span id="nbElemAnnoncesModifs"></span>';
	textToInsert += '</td>';
	textToInsert += '</tr>';
	textToInsert += '<tr><td>&nbsp;</td></tr>';

	// Document à envoyer
	for(iElement=0; iElement< data.length; iElement++){
		if(data[iElement].statusEnvoi != '42002403'){//SENT
			textToInsert += '<tr class="divInfoSubsideEnvoiModifExpand_Hidable"><td></td>';
			textToInsert += '<td colspan="2" align="left">'+data[iElement].dateEnvoi+'</td>';
			textToInsert += '<td title="'+data[iElement].libelleEnvoi+'" colspan="6" align="left">';
			var libelleDoc = data[iElement].numModele;
			var libelleShort = data[iElement].numModele;
			var cuDoc = data[iElement].numModele;
			if(allCSDocument[data[iElement].numModele]!=undefined){
				libelleDoc = allCSDocument[data[iElement].numModele].libelle;
				libelleShort = allCSDocument[data[iElement].numModele].libelleShort;
				cuDoc = allCSDocument[data[iElement].numModele].cu;
			}
			var typeJob = data[iElement].typeJob;
			var idStatus = data[iElement].idStatus;
			textToInsert += libelleDoc;
			textToInsert += '</td>';
			textToInsert += '<td align="right">'+cuDoc+'</td>';
			textToInsert += '<td align="left" title="'+libelleDoc+'">- ';
			if(typeJob=='42002301'){
				textToInsert += '<a href="javascript:generateWordLauncher('+idStatus+')">';
				textToInsert += libelleShort;
				textToInsert += '</a>';
			}else{
				textToInsert += libelleShort;
			}
			textToInsert +='</td>';
			textToInsert += '</tr>';
			documentInProgress ++;
		}
	}
	nbElemEnvoiModifs = documentInProgress;
	if(documentInProgress == 0){
		textToInsert += '<tr class="divInfoSubsideEnvoiModifExpand_Hidable"><td></td><td colspan="11">Pas d\'élément modifiable actif</td></tr>';
	}
	textToInsert += '<tr class="divInfoSubsideEnvoiModifExpand_Hidable"><td>&nbsp;</td></tr>';
	
	
	generateHistoriqueSEDEXRP(currentIdDetailFamille);
}

//------------------------------------------------------------
//Appel Ajax pour générer l'historique des annonces SEDEX RP
//------------------------------------------------------------
function generateHistoriqueSEDEXRP(_currentIdDetailFamille) {
	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.sedexRP.AnnoncesRPService',
		serviceMethodName:'getListSEDEXAnnonces',
		parametres:_currentIdDetailFamille,
		callBack: createHistoriqueSEDEXRP
	}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}

//------------------------------------------------------------
//CallBack Ajax pour générer l'historique des annonces
//------------------------------------------------------------
function createHistoriqueSEDEXRP(data) {
	
	textToInsert += '<tr style="background-color:#eeeeee">';
	textToInsert += '<td style="border-bottom: 1px solid black" ></td>';
	textToInsert += '<td colspan="11" style="border-bottom: 1px solid black;padding-top:2px;padding-bottom:2px" >';
	textToInsert += '	<table border="0" cellpadding="0" cellspacing="0" width="100%">';
	textToInsert += '		<tr>';
	textToInsert += '			<td style="font-style:italic;font-weight: bold">';
	textToInsert += '				<img class="divInfoSubsideSedexExpand" src="'+amContextPath+'/images/icon-collapse.gif" width="12px" height="12px"/>';
	textToInsert += ' 				Historique SEDEX RP';
	textToInsert += '				<span id="nbElemAnnoncesSedex"></span>';
	textToInsert += '			</td>';
	textToInsert += '			<td align="right">';
	if(hasRightNew) {
		textToInsert += '				<span style="cursor:pointer" id="linkNewAnnonceFromSubside"><img title="Nouvelle annonce" src="'+amContextPath+'/images/amal/check_cm.png"></span>';
	}
	textToInsert += '			</td>';
	textToInsert += '		</tr>';
	textToInsert += '	</table>';
	textToInsert += '</td>';
	textToInsert += '</tr>';
	textToInsert += '<tr><td>&nbsp;</td></tr>';
	nbElemHistoSedex = data.length;
	for(iElement=0; iElement< data.length; iElement++){
		var numDecision = data[iElement].numeroDecision;
		if (numDecision == '') {
			numDecision="0";
		}
		textToInsert += '<tr id="sedexLine_'+data[iElement].idAnnonceSedex+'" class="divInfoSubsideSedexExpand_Hidable sedexLine '+numDecision+'">';
		textToInsert += '<td><input type="hidden" class="subTypeMsg" value="'+data[iElement].messageSubType+'"/></td>';
		textToInsert += '<td colspan="2" align="left">'+data[iElement].dateMessage+'</td>';
		textToInsert += '<td align="left" colspan="5">'+data[iElement].ajax_libelleAnnonce+'</td>';
		if (data[iElement].ajax_nomCM.length>14) {
			textToInsert += '<td align="left" colspan="2" title="'+data[iElement].ajax_nomCM+'">'+data[iElement].ajax_nomCM.substring(0,14)+'...</td>';
		} else {
			textToInsert += '<td align="left" colspan="2">'+data[iElement].ajax_nomCM+'</td>';
		}
		textToInsert += '<td align="left" class="menuSedex" id="statusAnnonce_'+data[iElement].idAnnonceSedex+'">'+data[iElement].ajax_libelleStatus+'</td>';
		textToInsert += '<td align="right" class="menuSedex" id="menuSedex_'+data[iElement].idAnnonceSedex+'" style="cursor:pointer">...';
		textToInsert += '</td>';
		textToInsert += '</tr>';
	}
	
	if(nbElemHistoSedex==0){
		textToInsert+= '<tr class="divInfoSubsideSedexExpand_Hidable"><td></td><td colspan="9">Pas d\'historique disponible</td></tr>';
	}

	textToInsert += '<tr class="divInfoSubsideSedexExpand_Hidable"><td>&nbsp;</td></tr>';
	
	// Fin du traitement, on cache l'icône de chargement
	$('#ajaxLoaderImgLine').hide();
	// On append notre résultat
	$('#tableHisto').append(textToInsert);
	textToInsert='';
	
	attachContextualMenu();
	
	showCounters();
}

function attachContextualMenu() {
	$(".menuSedex").contextMenu({
        menu: 'myMenu'
    },
    function(action, el, pos, attrIdClicked) {
    	var idAnnonceSedex = attrIdClicked.split("_").pop();
    	
    	if (action == 'viewXml') {
			getXmlAnnonce(idAnnonceSedex,'C');
			$('#modelViewXML').dialog('open');
    	} else if (action == 'viewXmlHeader') {
			getXmlAnnonce(idAnnonceSedex,'H');
			$('#modelViewXML').dialog('open');
		} else if (action == 'reSend') {
			if (confirm("Cloner l'annonce pour réémission ?")) {
				reSubmitAnnonce(idAnnonceSedex);
			}
		} else if (action == 'delAnnonce') {
			if (confirm("Supprimer l'annonce (seulement si l'annonce n'a pas été envoyée) ?")) {
				deleteAnnonce(idAnnonceSedex);
			}
		} else if (action == 'simuRep') {
			$("#dialog-simulation-reponse").dialog({
				autoOpen: true,
				modal: true,
				height: 100,
				buttons: {
					Cancel: function() {
						$( this ).dialog( "close" );
				    },
				    "Ok": function() {
				    	  simulationReponse(idAnnonceSedex, $("#subTypeReponse").val());
				          $( this ).dialog( "close" );
				    }
				}
			});
		} else {
			alert('Unknown action !');
		}
    });
}

function deleteAnnonce(_currentIdSedexAnnonce)  {
	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.sedexRP.AnnoncesRPService',
		serviceMethodName:'deleteAnnonce',
		parametres:_currentIdSedexAnnonce,
		callBack: deleteAnnonceCallBack
	}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();
}

function simulationReponse(_currentIdSedexAnnonce, subTypeReponse)  {
	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.sedexRP.AnnoncesRPService',
		serviceMethodName:'simulateAnnonce',
		parametres:_currentIdSedexAnnonce+","+subTypeReponse,
		callBack: simulationAnnonceCallBack
	}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}

function deleteAnnonceCallBack(data) {
	var url = window.location.href;
	window.location.href=url;
}

function simulationAnnonceCallBack(data) {
	window.location.reload();
}

//------------------------------------------------------------
//Appel Ajax pour générer l'historique des annonces SEDEX RP
//------------------------------------------------------------
function reSubmitAnnonce(_currentIdSedexAnnonce) {
	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.sedexRP.AnnoncesRPService',
		serviceMethodName:'cloneAnnonce',
		parametres:_currentIdSedexAnnonce,
		callBack: changeStatus
	}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}


//------------------------------------------------------------
//Appel Ajax pour générer l'historique des annonces SEDEX RP
//------------------------------------------------------------
function getXmlAnnonce(_currentIdSedexAnnonce,_type) {
	$('#modelViewXML').dialog('option', 'title', 'Affichage du code XML de l\'annonce #'+_currentIdSedexAnnonce);
	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.sedexRP.AnnoncesRPService',
		serviceMethodName:'getSedexXMLLines',
		parametres:_currentIdSedexAnnonce+","+_type,
		callBack: viewXml
	}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}


function changeStatus(data) {
	if (data != 0) {
		window.location.reload();
//		$("#statusAnnonce_"+data).html("Crée");
	}
}


function formatXml(xml) {
    var formatted = '';
    var reg = /(>)(<)(\/*)/g;
    xml = xml.replace(reg, '$1\r\n$2$3');
    var pad = 0;
    jQuery.each(xml.split('\r\n'), function(index, node) {
        var indent = 0;
        if (node.match( /.+<\/\w[^>]*>$/ )) {
            indent = 0;
        } else if (node.match( /^<\/\w/ )) {
            if (pad != 0) {
                pad -= 1;
            }
        } else if (node.match( /^<\w[^>]*[^\/]>.*$/ )) {
            indent = 1;
        } else {
            indent = 0;
        }

        var padding = '  ';
        for (var i = 0; i < pad; i++) {
            padding += '  ';
        }

        formatted += padding + node + '\r\n';
        pad += indent;
    });

    return formatted;
}

function viewXml(data) {
	xml_escaped = data.replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/>/g,'&gt;').replace(/ /g, '&nbsp;').replace(/\n/g,'<br />');
	$("#modelViewXML #contentXml").html(xml_escaped);
}

function showCounters() {
	if (nbElemHistoEnvoi==0) {
		$("#nbElemHistoEnvoi").html("(0 élément)");
		$(".divInfoSubsideHistoEnvoiExpand").click();
	} else if (nbElemHistoEnvoi==1) {
		$("#nbElemHistoEnvoi").html("("+nbElemHistoEnvoi+" élément)");
	} else {
		$("#nbElemHistoEnvoi").html("("+nbElemHistoEnvoi+" éléments)");
	}
	
	if (nbElemEnvoiModifs==0) {
		$("#nbElemAnnoncesModifs").html("(0 élément)");
		$(".divInfoSubsideEnvoiModifExpand").click();
	} else if (nbElemEnvoiModifs==1) {
		$("#nbElemAnnoncesModifs").html("("+nbElemEnvoiModifs+" élément)");
	} else {
		$("#nbElemAnnoncesModifs").html("("+nbElemEnvoiModifs+" éléments)");
	}
	

	if (nbElemHistoSedex==0) {
		$("#nbElemAnnoncesSedex").html("(0 élément)");
		$(".divInfoSubsideSedexExpand").click();
	} else if (nbElemHistoSedex==1) {
		$("#nbElemAnnoncesSedex").html("("+nbElemHistoSedex+" élément)");
	} else {
		$("#nbElemAnnoncesSedex").html("("+nbElemHistoSedex+" éléments)");
	}
}

//------------------------------------------------------------
// ESSAI AJAX
//------------------------------------------------------------
function essaiAjax(currentIdDetailFamille) {
	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.models.detailfamille.DetailFamilleService',
		serviceMethodName:'essaiAjax',
		parametres:currentIdDetailFamille,
		callBack: callbackAjax
	}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}

function callbackAjax(data) {
	alert("Data : "+data);
}

