
//**************************Variables globales***********************************
var _$copiesIsChanged = "";//champ cacheretat zone copies
var _$tableCopieColoneRecap = "";//selecteurs de lignes tableau copies
var _$chmAjouter = "";//champ texte ajout annexe
var _$annexesIsChanged = "";//champ cacher etat annexe
var _$listeAnnexes = "";


//******************************Init variables globales******************************
var initVariables = function () {
	_$tableCopieColoneRecap = $('#tableCopies tr:gt(0)');
	_$chmAjouter = $('#chmAjouter');
	_$annexesIsChanged = $('#annexesIsChanged');
	_$annexesIsChanged.prop('value','0');
	_$copiesIsChanged = $('#copiesIsChanged');
	_$copiesIsChanged.prop('value','0');
};

//***********************Initialisation zone annexe ****************************
var initAnnexeZone = function (){
	//init bouton ajouter
	$('#btnAjouter').click(function(){
		//Si chaine vide, alert
		if(_$chmAjouter.val()==''){
			alert(errorMsgAnnexe);
		}
		else{
			insertAnnexe(_$chmAjouter.val(),csTexteLibre);
		}
	});
	
	
	
	//init bouton supprimer
	$('#btnSupprimer').click(function(){
		var csCode = $('#listeAnnexes :selected').prop('value');
		deleteAnnexe(Number(csCode));
	});
	
	//Init bouton up et down
	$('#btn_up').click(function () {
		moveUpItem();
	});
	
	$('#btn_down').click(function () {
		moveDownItem();
	});
};

//********************************** INSERT ANNEXE ****************
var insertAnnexe  = function (annexeLabel,type){
	//Champ cachés etat changement des annexes
	//var annexesIsChanged = $('#annexesIsChanged');
	
	var chaine = '<option value="'+type+'">' + annexeLabel +'</option>';
	$(chaine).appendTo('#listeAnnexes');
	_$chmAjouter.val('');
	_$annexesIsChanged.prop('value','1');
};

//********************************** DELETE ANNEXE *********************
var deleteAnnexe = function (type){
	
	if(type===csAutoBillag){
		$('#listeAnnexes option').each(function(){
			//Si annexe billag, on supprime, et gestion champ
			if(Number(this.value)===csAutoBillag){
				$(this).remove();
				setAnnexesChange();
				setBillagState(false);
			}
		});
	}
	else{
		$('#listeAnnexes :selected').remove();
		setAnnexesChange();
	}
};

//*************************** Init validate **************************************
var initValidate = function initValidate() {
	//Si annexes changés, set valeur hiden
	if(_$annexesIsChanged.prop('value')==='1'){
		getSelectAsString();		
	}
	//Si copies changé...
	if(_$copiesIsChanged.prop('value')==='1'){
		getCopiesAsString();
	}

	//bouton radio diminition pc
	$('#hidChkDiminutionPc').prop('value',convertBoolToString($('#diminutionPc').is(':checked')));
	//bouton radio allocNonactif
	$('#hidChkAllocNonActif').prop('value',convertBoolToString($('#allocNonActif').is(':checked')));
	
	//bouton radio ajoute et annule
	$('#hidChkannuleEtRemplacePrec').prop('value',convertBoolToString($('#ajouteEtremplace').is(':checked')));	
	
};

//******************** Traitement zone annexe submit ***************************
var getSelectAsString = function () {
	
	var chaineToSend = "";
	var separator = "\n";
	
	$('#listeAnnexes').find('option').each(function() { 
		chaineToSend += $(this).text();
		
		if(Number($(this).prop('value'))===csAutoBillag){
			chaineToSend += "_"+csAutoBillag;
		}else{
			chaineToSend += "_"+csTexteLibre;
		}
		
		chaineToSend += separator;
	}); 

	//set champ hidden
	$('#listeAnnexesString').prop('value',chaineToSend);

};
//*******************************************************************************
var moveUpItem = function (){
	$('#listeAnnexes option:selected').each(function(){
	   $(this).insertBefore($(this).prev());
	});
}

var moveDownItem = function (){
	$('#listeAnnexes option:selected').each(function(){
	   $(this).insertAfter($(this).next());
	});
} 
//*************************** Traitement zone copies submit **********************
var getCopiesAsString = function (){
	
	var idTiersSeparator = '-';
	//iteration sur les ligne du tableau des copies, exclu th
	_$tableCopieColoneRecap.each(function(){
		var chaine = '';
		
		//Chaque td checkbox, boolean value
		$('td:eq(1),td:eq(2),td:eq(3),td:eq(4),td:eq(5),td:eq(6),td:eq(7),td:eq(8),td:eq(9),td:eq(10)',this).each(function(){
			if($('input:checkbox',this).is(':checked')){
				chaine += "1";
			}
			else{
				chaine += "0";
			}
		});
		chaine+=idTiersSeparator;
		chaine+= $('[name=idTiersCopies]',this).val();
		//Set les champs caché
		$('[name=copies]',this).prop('value',chaine);
	});
};
//***********************************************************************************
//*************** initialisation zone texte Billag SA ***************************
var initBillagZone = function () {
	//var phraseCaseCoche = $('#phraseCaseCoche');
	//var phraseNonCoche = $('#phraseCaseNonCoche');
	
	var billagCheckBox = $('#billagChk');
	//Liste proform-decision
	var typeDecisionList = $('#csTypePreparation');
	
	//recherche si annexe billag présente
	setBillagState(hasBillagAuto);
	
	//Gestion onchange sur la liste
	typeDecisionList.change(function () {
		if(typeDecisionList.val()===BILLAG_PROFORMA_CS){
			//blocage zone billag
			lockAndClearBillagZone();
			//blocage colone recapitulatif et disabled case à cocher
			lockAndClearRecapColone();
			
		}else{
			//on active la zone copies recap (si courante ou plus recente) et case a cocher billag
			if(isMostRecentDecision){
				setColoneRecapState(true,_$tableCopieColoneRecap);
			}
			
			setBillagZone(true);
			$('#billagChk').prop('disabled',false);
			
		}
	});
	
	//Gestion evénement onclick sur case a cocher Billag
	billagCheckBox.click(function(){
		var checked = billagCheckBox.is(':checked');
		//en fonction du check on affiche le texte, et on set l'annexe billag
		setBillagState(checked);
		if(checked){
			//Add annexe billag
			insertAnnexe(billagAutoTexte,csAutoBillag);
		}
		else{
			//remove annexex billag
			deleteAnnexe(csAutoBillag);
		}
	});
};
//*******************************************************************************

//*************** initialisation zone widget adresse  ***************************
var initZoneWidgetAdresse = function () {

	//valeur init adresse widget
	$('#tiersCourrierWidget1').hide();
	$('#buttonAdresse').css('margin-left','70px');
	$('#tiersCourrierWidget1').prop('autocomplete','off');//autocomplete IE
	
	//Gestion evenement onclick sur bouton widget adresse
	$('#buttonAdresse').click(function(){
		$('#tiersCourrierWidget1').show();
		$('#tiersCourrierWidget1').focus();
		
	});
	//on blur on ferme le widget
	$('#tiersCourrierWidget1').blur(function(){
		$('#tiersCourrierWidget1').hide();	
		$('#tiersCourrierWidget1').val('');	
	});
};
//*******************************************************************************

//****************************** masquage du widget adresse *********************
var hideWidgetAdresse = function () {
	$('#tiersCourrierWidget1').hide();
	$('#buttonAdresse').show();
	$('#tiersCourrierWidget1').val('');
};
//*******************************************************************************

//************************* Construction du libelle adresse *********************
var buildLibForAdresse = function (element) {
	var chaine;
	chaine = $(element).prop('tiers.tiers.designation1')+' '+$(element).prop('tiers.tiers.designation2');
	chaine+='<br />';
	chaine+= $(element).prop('adresse.rue')+' '+$(element).prop('adresse.noRue');
	chaine+='<br />';
	chaine+= $(element).prop('localite.numPostal')+' '+$(element).prop('localite.localite');
	chaine+='<br />';
	
	$('.detailAdresseTiersC').html(chaine);
};
//*******************************************************************************

//*******************************Init zone widget copies ************************
var initZoneWidgetCopies = function () {
	
	if($('#csTypePreparation').val()===BILLAG_PROFORMA_CS){
		lockAndClearRecapColone();
		lockAndClearBillagZone();
	}
	
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
	
	$('.checkBoxCopies').click(function(){
		//Set le changement dans copies
		_$copiesIsChanged.prop('value','1');
	});
	
	//choix lettre de base, on désactive les composabts de la lettre de base
	$('.baseLetter').click(function () {
		var $contexte = $(this).closest('tr');
		var $checkForBase = $('.forBase',$contexte);
		
		
		if($(this).is(':checked')){
			$checkForBase.attr('disabled',false);
		}else{
			$checkForBase.attr('disabled',true);
			$checkForBase.prop('checked',false);
		}
		_$copiesIsChanged.prop('value','1');
	});
	//Evenement onclick bouton add dest copies
	$('#addBtn').click(function(){
		manipTable.addRow(isMostRecentDecision);
		_$tableCopieColoneRecap = $('#tableCopies tr:gt(0)');
	});
	
};

var dealCheckBoxOpiesZone = function () {
	
	//inittialisation ddes checkbox, lettre de base
	$('.baseLetter').each(function () {
	
		var $checkBaseLetter = $(this);
		var $contexte = $(this).closest('tr');
		//Si pas checker on désactive les membre du doc principal
		if(!$checkBaseLetter.is(':checked')){
			$('.forBase',$contexte).attr('disabled',true);
		}
	});
};

//objet de manipulation de la table
var manipTable = {
		 designation:'',
		 //ajout d'une ligne au tableau
		 addRow:function(hasDecompte){
			//Construction de la requete
			var chaine = ''; 
			
			//test quel service
			if($('#radioAdmin').is(':checked')){
				//set les champs cachés
				inputValue = $('#widgetAdmin').val();
				idTiersValue = $('.idAdmin1').val();
			
			}
			if($('#radioTiers').is(':checked')){
				inputValue = $('#widgetTiers').val();
				idTiersValue = $('.idTiers1').val()
		
			}
			
			
			chaine+="<tr><td>"+this.designation+"</td>";
			//pageDeGarde
			chaine+='<td><input class="checkBoxCopies" type="checkbox" checked="checked"/></td>';
			//Lettre de base
			chaine+='<td><input class="checkBoxCopies" type="checkbox" checked="checked"/></td>';
			//versement A
			chaine+='<td><input class="checkBoxCopies" type="checkbox" checked="checked"/></td>';
			//remarques
			chaine+='<td><input class="checkBoxCopies" type="checkbox" checked="checked"/></td>';
			//moyens de droit
			chaine+='<td><input class="checkBoxCopies" type="checkbox" checked="checked"/></td>';
			//signature
			chaine+='<td><input class="checkBoxCopies" type="checkbox" checked="checked"/></td>';
			//Recapitulatif decompte
			if(hasDecompte){
				chaine+='<td><input class="checkBoxCopies" type="checkbox" checked="checked"/></td>';
			}else{
				chaine+='<td><input class="checkBoxCopies" type="checkbox" disabled="true"/></td>';
			}
			//Annexes
			chaine+='<td><input class="checkBoxCopies" type="checkbox" checked="checked"/></td>';
			//Copies
			chaine+='<td><input class="checkBoxCopies" type="checkbox" checked="checked"/></td>';
			//Plan de calcul
			chaine+='<td><input class="checkBoxCopies" type="checkbox" checked="checked"/></td>';
			
			chaine+='<td><img class="btnDelete" src="'+servletContext+'/images/moins.png"/></td>';
			chaine +='<input type="hidden" name="idTiersCopies" value="'+idTiersValue+'"/>';
			chaine +='<input type="hidden" name="copies" value=""/></tr>';
			
			//ajout de la ligne dans le tableau
			$(chaine).appendTo('#tableCopies');
			//on masque les boutons et init valeur widget
			$('#addBtn').hide();
			$('#widgetAdmin').val('');
			$('#widgetTiers').val('');
			//Set le changement dans copies
			_$copiesIsChanged.prop('value','1');
			//evenement suppression
			$('.btnDelete').click(function(){
				$(this).parent().parent().remove();
				_$copiesIsChanged.prop('value','1');
				_$tableCopieColoneRecap = $('#tableCopies tr:gt(0)');
			});
			$('.checkBoxCopies').click(function(){
				_$copiesIsChanged.prop('value','1');
			});
		}
		
};
//*******************************************************************************
//*********************************** Gestion bouton preValid *******************
var setPreValidBouton = function (preValid,libelle) {
	if(!preValid){
		$('<input/>',{
			type: 'button',
			value:libelle,
			id:'btnPreValid',
			click:function(){
				//on set le champ hidden valid_Action
				$('#inhValidAction').val('prevalid');
				if(validateDecision()){
					$(this).hide();
					action(COMMIT);
				}
		}
		}).prependTo('#btnCtrlJade');
	}
	else{
		$('#btnPreValid').hide();
	}
};
//*********************************** Gestion bouton deValid *******************
var setDeValidBouton = function (deValid,libelle, labelOk, labelCancel) {
	//alert(deValid);
	if(deValid){
		$('<input/>',{
			type: 'button',
			value:libelle,
			id:'btnDeValid',
			click:function(){
				showConfirmDialogForDevalidateDecision(labelOk, labelCancel);
				$(this).hide();
			}
		}).prependTo('#btnCtrlJade');
	}
	else{
		$('#btnDeValid').hide();
	}
};
/**
 * Affichage de la boite de dialogue de confirmation de dévalidation avec traductions 
 * @returns
 */
var showConfirmDialogForDevalidateDecision = function (lblBtnConfirmer, lblBtnAnnuler) {
	$( "#dialog-devalid-confim" ).dialog({
        resizable: false,
        height:250,
        width:500,
        modal: true,
        buttons: [
                  {
	        		text: lblBtnConfirmer,
	        		click: function() {
	        			$( this ).dialog( "close" );
	        			$('#btnDeValid').hide();
	        			userAction.value=ACTION_DECISION_DEVALIDE;
	        			action(COMMIT);
	        		}
                  },
                  {
                	  text: lblBtnAnnuler,
                	  click:function() {
                          $( this ).dialog( "close" );
                	  }
                  }
                 ]
    });
};

/**
 * Affichage de la liste des plausi en warning ou info
 * @returns
 */
var showPlausiWarningDialog = function (msg) {
	$( "#dialog-plausi-warning" ).dialog({
        resizable: true,
        height:500,
        width:500,
        modal: true,
        
        
        buttons: {
        	"OK": function() {
                $( this ).dialog( "close" );
            }
        }
    });
	$("#dialog-plausi-warning-list").html(msg);
};

//***************************** GEstion bouton modifier **************************
var setUpdateBouton = function (valid){
	if(valid){
		$('#btnUpd').hide();
	}else{
		$('#btnUpd').show();
	}
};

//**************************** Set l'état de la zone billag ***********************
var setBillagState = function (state) {
	var phraseCaseCoche = $('#phraseCaseCoche');
	var phraseNonCoche = $('#phraseCaseNonCoche');
	var billagCheckBox = $('#billagChk');
	
	if(state){
		phraseCaseCoche.show();
		phraseNonCoche.hide();
	}else{
		phraseCaseCoche.hide();
		phraseNonCoche.show();
		
	}
	billagCheckBox.prop('checked', state);
};

//************************* Annexes change ***********************************
var setAnnexesChange = function () {
	var annexesIsChanged = $('#annexesIsChanged');
	//annexesChanged = 1;
	annexesIsChanged.prop('value','1');
};

//***********************  decision proforma liste ***************************
var lockAndClearBillagZone = function () {

	setBillagState(false);
	//On vide les annexes
	$('#listeAnnexes option').each(function(){
			//$(this).remove();
		deleteAnnexe(Number(this.value));
	});
	setBillagZone(false);
};
//*****************************Blocage et raz colone recap**********************
var lockAndClearRecapColone = function () {
	//iteration sur les ligne du tableau des copies, exclu th
	_$tableCopieColoneRecap.each(function(){
		//Chaque td checkbox, boolean value
		setColoneRecapState(false,this);
	});
};

/*
 * Set l'état de la colone recapitulatif/decompte
 */
var setColoneRecapState = function (colState,tab) {
	$('td:eq(6)',tab).prop('disabled',!colState);
	$('td:eq(6) input',tab).prop('checked',false);
};

var setBillagZone = function (zoneBillagState) {
	if(zoneBillagState){
		$('#zoneBillag').show();
	}else{
		$('#zoneBillag').hide();
	}
};

//Converti un booleean en string pour le passage de paramètre (Eviter un warning apache)
var convertBoolToString = function (boolState) {
	if(boolState){
		return 'true';
	}else{		
		return 'false';
	}
}

var setConjointLink = function (idDecisionConjoint,url) {
	//cacher le lien vers la decision liée
	var $decConjoitDiv = $('#zoneLienDecConjoint');
	
	$decConjoitDiv.hide();
	if(idDecisionConjoint!=0){
		$decConjoitDiv.show();
		
		$decConjoitDiv.find('a').attr('href',url+idDecisionConjoint);
	}
}