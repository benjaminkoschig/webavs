/**
 * @author SCE
 */
//Gestion evenement onclick sur bouton widget adresse
$('#buttonAdresse').click(function(){
	$('#homeWidget1').css('margin-left','10px');
	$('#homeWidget1').show();
	$('#buttonAdresse').hide();
});

//valeur init zone billag
$('#phraseCaseCoche').show();
$('#phraseCaseNonCoche').hide();
$('#billagChk').attr('checked', true);

//valeur init adresse widget
$('#homeWidget1').hide();
$('#buttonAdresse').css('margin-left','70px');

function hideInputC(){
	$('#homeWidget1').hide();
	$('#buttonAdresse').show()
	$('#homeWidget1').val('');
}

function buildStringC(element){
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
	$().find('.detailAdresseTiersP').html(chaine);
}

function setPreValidBouton(state,name){

	if(state){
		$('<input/>',{
			type: 'button',
			value:name,
			id:'btnPreValid',
			click:function(){
	
				
				if(preValidateDecision()){
					$(this).hide();
					action(COMMIT);
				} 
			}
		}).prependTo('#btnCtrlJade');
	}
	else{
		$('#btnPreValid').hide();
	}
	
}

/**
 * Gestion des copies ajoutés automatiquement, donc sans id multistring
 */
function fillCopiesAndAnnexesHiddenFields(){
	
	var champHiddenCopies =$('#listeCopies');
	var champHiddenAnnexes = $('#listeAnnexes');
	
	var copies = [];
	var annexes = [];
	
	//iteration sur les options sans id
	$('#copieSelect option').each(function () {
		copies.push(this.value);
	});
	
	$('#annexeSelect option').each(function () {
		annexes.push(this.value);
	});
	
	champHiddenCopies.val(copies.join('¦'));
	champHiddenAnnexes.val(annexes.join('¦'));
}

/**
 * Affichage de la boite de dialogue de confirmation de dévalidation
 * @returns
 */
var showConfirmDialogForDevalidateDecision = function () {
	$( "#dialog-devalid-confim" ).dialog({
        resizable: false,
        height:250,
        width:500,
        modal: true,
        
        buttons: {
        	"OK": function() {
                $( this ).dialog( "close" );
                userAction.value=ACTION_DECISION_DEVALIDE;
        		action(COMMIT);
            },
            "Annuler": function() {
                $( this ).dialog( "close" );
            }
        }
    });
};

var showConfirmDialogForCreateLot = function () {
	$( "#dialog-confirm-creation-lot" ).dialog({
        resizable: false,
        height:250,
        width:500,
        modal: true,
        
        buttons: {
            "Oui": function() {
                $( this ).dialog( "close" );
                $('#isComptabilisationAuto').val("true");
        		action(COMMIT);
            },
            "Non": function() {
                $( this ).dialog( "close" );
                action(COMMIT);
            }
        }
    });
};

function setDeValidBouton(state,name){
	$('<input/>',{
		type: 'button',
		value:name,
		id:'btnDeValid',
		click:function(){
			$(this).hide();
			showConfirmDialogForDevalidateDecision();
		}
	}).prependTo('#btnCtrlJade');
}

function setPrintBouton(state,name){

	if(state){
		$('<input/>',{
			type: 'button',
			value:name,
			id:'btnPrint',
			click:function(){
				if(printDecision()){
					action(COMMIT);
				} 
			}
		}).prependTo('#btnCtrlJade');
	}
	else{
		$('#btnPrint').hide();
	}	
}

function setValidBouton(name){
	//Ajout bouton valider
	$('<input/>',{
		type: 'button',
		value:name,
		id:'btnValid',
		click:function(){
		
			if(validateDecision()){
				$(this).hide();
				
				if(globaz.amountRestitution){
					showConfirmDialogForCreateLot();
				}else{
					action(COMMIT);
				}
											
			} 
		}
	}).prependTo('#btnCtrlJade');

}