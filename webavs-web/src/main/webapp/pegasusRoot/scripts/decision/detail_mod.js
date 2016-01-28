//**************************Variables globales***********************************
var toggleButtonState = 0;//etat pour bouton toggle
var annexesChanged = 0;//etat zone anexxe
var copiesChanged = 1;//etat zone copies




//*******************************************************************************

//***********************Initialisation zone annexe ****************************
function initAnnexeZone(){
	
	//init bouton ajouter
	$('#btnAjouter').click(function(){
		//Si chaine vide, alert
		if($('#chmAjouter').val()==''){
			alert(errorMsgAnnexe);
		}
		else{
			var chaine = '<option>' + $('#chmAjouter').val() +'</option>';
			$(chaine).appendTo('#listeAnnexes');
			$('#chmAjouter').val('');
			annexesChanged = 1;
			$('#annexesIsChanged').attr('value','1');
		}
	});
	
	//init bouton supprimer
	$('#btnSupprimer').click(function(){
		$('#listeAnnexes :selected').remove();
		annexesChanged = 1;
		$('#annexesIsChanged').attr('value','1');
	});
	
	
}
//*************************** Init validate **************************************
function initValidate(){
//Si annexes changés, set valeur hiden
	if(annexesChanged==1){
		getSelectAsString();		
	}
	//Si copies changé...
	if(copiesChanged==1){
		getCopiesAsString();
	}

	//bouton radio diminition pc
	if($('#diminutionPc').is(':checked')){
		$('#hidChkDiminutionPc').attr('value','true');
		
	}
	else{
		$('#hidChkDiminutionPc').attr('value','false');
		
	}

	//bouton radio allocNonactif
	if($('#allocNonActif').is(':checked')){
		$('#hidChkAllocNonActif').attr('value','true');
		
	}
	else{
		$('#hidChkAllocNonActif').attr('value','false');
		
	}

	//bouton radio ajoute et annule
	if($('#ajouteEtremplace').is(':checked')){
		$('#hidChkannuleEtRemplacePrec').attr('value','true');
		
	}
	else{
		$('#hidChkannuleEtRemplacePrec').attr('value','false');
		
	}
}

//******************** Traitement zone annexe submit ***************************
function getSelectAsString(){
	
	var chaineToSend = "";
	var separator = "\n";

	$('#listeAnnexes').find('option').each(function() { 
		chaineToSend += $(this).text();
		chaineToSend += separator;
	}); 

	//set champ hidden
	$('#listeAnnexesString').attr('value',chaineToSend);
}
//*******************************************************************************

//*************************** Traitement zone copies submit **********************
function getCopiesAsString(){
	var separator = "\n";
	//iteration sur les ligne du tableau des copies, exclu th
	$('#tableCopies tr:gt(0)').each(function(){
		var chaine = '';
		//premiere case idTiers
		chaine += $('td:eq(0)',this).text();
		chaine += separator;

		//Chaque td checkbox, boolean value
		$('td:eq(1),td:eq(2),td:eq(3),td:eq(4),td:eq(5),td:eq(6),td:eq(7),td:eq(8),td:eq(9)',this).each(function(){
			if($('input:checkbox',this).is(':checked')){
				chaine += "1";
			}
			else{
				chaine += "0";
			}
		});
		//alert(chaine);
		//Set le champ caché
		$('[name=copies]',this).attr('value',chaine);
		//alert($('[name=copies]',this).attr('value'));
	});
}
//***********************************************************************************
//*************** initialisation zone texte Billag SA ***************************
function initBillagZone(versionDroitInitial){
	
	
	
	//Si la version du droit est la premiere
	if(versionDroitInitial){
		//valeur init texte et case a cocher
		$('#phraseCaseCoche').show();
		$('#phraseCaseNonCoche').hide();
		$('#billagChk').attr('checked', true);
	}
	else{
		//valeur init texte et case a cocher
		$('#phraseCaseCoche').hide();
		$('#phraseCaseNonCoche').show();
		$('#billagChk').attr('checked', false);
	}
	
	
	//Gestion evénement onclick sur case a cocher Billag
	$('#billagChk').click(function(){
		var checked = $('#billagChk').is(':checked');
		//en fonction du check on affiche le texte
		if(checked){
			$('#phraseCaseNonCoche').hide();
			$('#phraseCaseCoche').show();
		}
		else{
			$('#phraseCaseCoche').hide();
			$('#phraseCaseNonCoche').show();
		}
	});
}
//*******************************************************************************

//*************** initialisation zone widget adresse  ***************************
function initZoneWidgetAdresse(){

	//valeur init adresse widget
	$('#homeWidget1').hide();
	$('#buttonAdresse').css('margin-left','70px');
	$('#homeWidget1').attr('autocomplete','off');//autocomplete IE
	
	//Gestion evenement onclick sur bouton widget adresse
	$('#buttonAdresse').click(function(){
		$('#homeWidget1').css('margin-left','10px');
		$('#homeWidget1').show();
		$('#homeWidget1').focus();
		
	});
	//on blur on ferme le widget
	$('#homeWidget1').blur(function(){
		$('#homeWidget1').hide();	
	});
}
//*******************************************************************************

//****************************** masquage du widget adresse *********************
function hideWidgetAdresse(){
	$('#homeWidget1').hide();
	$('#buttonAdresse').show()
	$('#homeWidget1').val('');
}
//*******************************************************************************

//************************* Construction du libelle adresse *********************
function buildLibForAdresse(element){
	var chaine;
	chaine = $(element).attr('simpleHome.numeroIdentification');
	chaine+='<br />';
	chaine+='[Monsieur]';
	chaine+='<br />';
	chaine+='[Valderama Antonio]';
	chaine+='<br />';
	chaine+='[Rue des Prés 18]';
	chaine+='<br />';
	chaine+='[2300 La Chaux-de-Fonds]';
	$('.detailAdresseTiersC').html(chaine);

}
//*******************************************************************************

//*******************************Init zone widget copies ************************
function initZoneWidgetCopies(){
	//Valeur init zone widget Copies
	$('#widgetTiers').show();
	$('#widgetAdmin').hide();
	$('#addBtn').hide();
	
	$('#radioTiers').click(function(){
		$('#widgetAdmin').hide();
		$('#widgetTiers').val('');
		$('#widgetTiers').show();
		$('#widgetTiers').focus();
		
	});
	
	$('#radioAdmin').click(function(){
		$('#widgetTiers').hide();
		$('#widgetAdmin').val('');
		$('#widgetAdmin').show();
		$('#widgetAdmin').focus();
	});
	
	//evenement suppression
	$('.btnDelete').click(function(){
		$(this).parent().parent().remove();	
		copiesChanged = 1;
		$('#copiesIsChanged').attr('value','1');
	});
	
	//Evenement onclick bouton add dest copies
	$('#addBtn').click(function(){
		//Construction de la requete
		var chaine; 
		chaine+="<tr><td>1</td>";
		chaine+='<td><input type="checkbox" checked="checked"/></td>';
		chaine+='<td><input type="checkbox" checked="checked"/></td>';
		chaine+='<td><input type="checkbox" checked="checked"/></td>';
		chaine+='<td><input type="checkbox" checked="checked"/></td>';
		chaine+='<td><input type="checkbox" checked="checked"/></td>';
		chaine+='<td><input type="checkbox" checked="checked"/></td>';
		chaine+='<td><input type="checkbox" checked="checked"/></td>';
		chaine+='<td><input type="checkbox" checked="checked"/></td>';
		chaine+='<td><input type="checkbox" checked="checked"/></td>';
		chaine+='<td><button class="btnDelete"><img src="'+servletContext+'/images/erreur.gif"/></button></td>';
		chaine+='<input type="hidden" name="copies" value=""/></tr>';
		//ajout de la ligne dans le tableau
		$(chaine).appendTo('#tableCopies');
		//on masque les boutons et init valeur widget
		$('#addBtn').hide();
		$('#widgetAdmin').val('');
		$('#widgetTiers').val('');
		//Set le changement dans copies
		copiesChanged = 1;
		$('#copiesIsChanged').attr('value','1');
		
		//evenement suppression
		$('.btnDelete').click(function(){
			$(this).parent().parent().remove();	
			copiesChanged = 1;
			$('#copiesIsChanged').attr('value','1');
		});
	});
}
//******************************************************************************

//*********************************TEST*********************************************/
function initToggle(){
	//init onclick
	$('#adresseZoneBox th').click(function(){
		togglePanel($('#adresseZone'),$('#iconAdrPanel'));
	});
	
	$('#ligneDemandeBox th').click(function(){
		togglePanel($('#ligneInfosDiverses'),$('#iconPanDemande'));
	});
	
	$('#pcBox th').click(function(){
		togglePanel($('#pcInfo'),$('#iconPanPc'));
	});
	
	$('#annexesCopiesBox th').click(function(){
		togglePanel($('#annexesInfo'),$('#iconPanAnnexes'));
	});
	
	$('#billagBox th').click(function(){
		togglePanel($('#zoneBillag'),$('#iconPanBillag'));
	});
	
	$('#copiesBox th').click(function(){
		togglePanel($('#zoneCopies'),$('#iconPanCopies'));
	});
}
function togglePanel(zone, icon){
	//Si la div est ouverte
	if(zone.css('display') == 'block'){
		//on cache
		zone.hide();
		//on change l'icone
		icon.removeClass('ui-icon ui-icon-minusthick');
		icon.addClass('ui-icon ui-icon-plusthick');
	}
	else{
		//on cache
		zone.show();
		//on change l'icone
		icon.removeClass('ui-icon ui-icon-plusthick');
		icon.addClass('ui-icon ui-icon-minusthick');
	}
}

function openAllDivs(){
	$('#adresseZone').show();
	$('#ligneInfosDiverses').show();
	$('#pcInfo').show();
	$('#annexesInfo').show();
	$('#zoneBillag').show();
	$('#zoneCopies').show();
	
	
}
//*******************************************************************************