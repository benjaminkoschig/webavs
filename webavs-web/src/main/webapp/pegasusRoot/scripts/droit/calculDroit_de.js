/**************************************************************
 * Script des comportements de l'écran du calcul
 * 
 * SCE
 */


function add() {}
function upd() {}	   
function cancel() {}
function init(){}

  

/**
 * Affichage de la boite de dialogue de confirmation du clacul avec modifciation des données financières
 * @returns
 */
var showConfirmDialogForModifDFs = function () {
	$( "#dialog-modifiydf-confim" ).dialog({
        resizable: false,
        height:250,
        width:500,
        modal: true,
        
        buttons: {
            "Confimer": function() {
                $( this ).dialog( "close" );
                if(validate()) {
        			action(COMMIT);
        			$('#calculer_btn').hide();
        		}
            },
            "Annuler": function() {
                $( this ).dialog( "close" );
            }
        }
    });
};

var displayCalculStandardBloquedErrorMsg = function (isNonDfreason) {
	
	if(isNonDfreason){
		$("<div class=\"msgStandardCalBloque\" data-g-boxmessage=\"type:WARN\">"+globazGlobal.label.calculStandardInterditAucuneDonneeFinanciere+"</div>").insertAfter('.typeCalculSansRetro');
	}else{
		$("<div class=\"msgStandardCalBloque\" data-g-boxmessage=\"type:WARN\">"+globazGlobal.label.calculStandardInterditPasDansLaPlageDeCalcule+dateDebutPlageCalcul+")</div>").insertAfter('.typeCalculSansRetro');
	}
};

$(function(){
	
	//case a cocher forcer calcul
	var $checkBoxForcerCalcul = $('#forcerdatefin');
	//Zone de saisie date de fin
	var $zoneSaisieDateFin = $('.zoneSaisieDateFin');
	$zoneSaisieDateFin.hide();
	// date de fin du calcul
	var $dateFinInput = $('#dateFinCalcul');
	// ligne de gestion pour forcer la date de fin du calcul
	var $ligneForcerCalcul = $('.ligneForcerCalcul');
	//design bouton et gestion
	var $submitCalculButton = $('#calculer_btn');
	//ligne df
	var $ligneDF = $('.row-type-df');
	
	
	//etat blocqge calcul standard
	var isCalculStandardBloqued = (isStandardCalBloquedNonDfFoVersion || isStandardCalBloquedNoPeriod || isStandardCalBloquedClorePeriod);
	
	var  isStandardCalBloquedClorePeriod = false;
	
	//blocage du calcul pour droit motif adaptation
	var calculInterditMotifDroitAdaptation = (csMotifDroit === motifAdaptation);
	
	//****************************** GUI INIT *********************************
	$('#typeDeCalcule_NORMAL').attr('checked','checked');
	
	//gestion messages periodes multiples pour df, caché par css
	$('.period_error').append("<div class='msgPeriodBox' data-g-boxmessage='type:WARN'><strong>"+titreCMSBlocked+"</strong><p style=\"font-weight:normal\">"+contentCMSBlocked+"</p></div>");
	
	//gestion messages periodes retro, caché par css
	$('.retro_warning').append("<div class='msgRetroBox' data-g-boxmessage='type:WARN'><strong>"+titreRetroWarn+"</strong><p style=\"font-weight:normal\">"+contentRetroWarn+"</p></div>");
	
	//Si motif adaptation calcul interdit
	if(calculInterditMotifDroitAdaptation){
		$('.typeDeCalculStandard').hide();	
		$('.typeCalculSansRetro').hide();
		$("#zone_bouton_calcul").hide();
		$("<div class='msgStandardCalBloque' data-g-boxmessage='type:WARN'><strong>"+globazGlobal.label.calculInterditMotifAdaptation+"</strong></div>").insertAfter('.typeCalculSansRetro');
		
		$('#contenu_df_modif').hide();
		$('.ui-widget-header').hide();
		
	}else{
		if(isCalculInterdit) {
			$('.typeCalculSansRetro').hide();
			$('.typeDeCalculStandard').hide();	
			$(".ligneForcerCalcul").hide();
			$("<div class='msgStandardCalBloque' data-g-boxmessage='type:WARN'>"+globazGlobal.label.calculInterdit+"</div>").insertAfter('.typeCalculSansRetro');
			$("#zone_bouton_calcul").hide();
		} else if(isCalculeBloqueDuAujourDappoint){
			$('.typeCalculSansRetro').hide();
			$('.typeDeCalculStandard').hide();	
			$(".ligneForcerCalcul").hide();
			$("<div class='msgStandardCalBloque' data-g-boxmessage='type:WARN'>"+globazGlobal.label.calculInterditJourAppoint+"</div>").insertAfter('.typeCalculSansRetro');
			$("#zone_bouton_calcul").hide();
		}else if(isStandardCalBloquedClorePeriod){
			$('.typeDeCalculStandard').hide();	
			$('#typeDeCalcule_SANS_RETRO').attr('checked','checked');
			$("<div class='msgStandardCalBloque' data-g-boxmessage='type:WARN'>"+globazGlobal.label.calculStrandardInterditClorePeriode+"</div>").insertAfter('.typeCalculSansRetro');
		} else if(isCalculStandardBloqued){
			//on enleve l'option
			$('.typeDeCalculStandard').hide();
			//on force le choiy dur calcul mois suivants
			$('#typeDeCalcule_SANS_RETRO').attr('checked','checked');
			//on enleve l'option de forcer la date de fin du calcul
			$ligneForcerCalcul.hide();
			//application du message d'ereur
			displayCalculStandardBloquedErrorMsg(isStandardCalBloquedNonDfFoVersion);
		}else if(isCMSBloquedDemandeClose){
			//on enleve l'option
			$('.typeCalculSansRetro').hide();
			//on force le choiy dur calcul mois suivants
			$('#typeDeCalcule_STANDARD').attr('checked','checked');
			//Affichage message avertissemetn blocage cms 
			//gestion messages demande close cms bloqué
			$("<div class='msgDemandeCloseBox' data-g-boxmessage='type:WARN'><strong>"+titreCMSDemandeCloseWarn+"</strong><br>"+contentCMSDemandeCloseWarn+"</div>").insertAfter('.typeCalculSansRetro');
		}
	}
	
	if($('.typeCalculSansRetro').is(":visible") && isEffetMoisSuivantBloque){
			$('.typeCalculSansRetro').hide();
			$(".ligneForcerCalcul").hide();
			$("<div class='msgDemandeCloseBox' data-g-boxmessage='type:WARN'><strong>"+titreMoisSuivantBlocked+"</strong><br>"+contentMoisSuivantBlocked+"</div>").insertAfter('.typeCalculSansRetro');
			if(!$('.typeDeCalculStandard').is(":visible")){
				$("#zone_bouton_calcul").hide();
			}
	}
	
	
	


	//******************************* Evenements *******************************
	//bouton calculer
	$submitCalculButton.click(function () {
		var that = $(this);
		
		//version de droit > 1 et calcul mois suivant
		var checked = $('#typeDeCalcule_SANS_RETRO').is(':checked');
		
		if($('#typeDeCalcule_SANS_RETRO').is(':checked') && noVersion!==1 && isDFCreeOrModifForVersion && !isCalculStandardBloqued){
			if(!isStandardCalBloquedClorePeriod){
				showConfirmDialogForModifDFs();							
			}
		}else{
			if(validate()){
				action(COMMIT);
			}
		}

	});

	//******************** clic sur df lien df ********************
	//$ligneDF.click(function () {
		
	//	var url = $(this).attr('data-url');
	//	location.href=url;
	//});
	//radio effet mois suivant, gestion affichage zone forcer date de fin
	$('#typeDeCalcule_SANS_RETRO,#typeDeCalcule_NORMAL').bind('click',function () {
		
		//effet moi suivants
		if($(this).attr('value')===csCalculSansRetro){
			$ligneForcerCalcul.hide();
			//afiichage warn
			$('.msgRetroBox').show();
			//Si blocage cms du a erreurs periode et que version > 1, calcul impossible et affichage des erreurs de périodes
			if(isCMSBlockedDueManyPeriodModfis && noVersion > 1){
				$submitCalculButton.hide();
				$('.msgPeriodBox').show();
			}
		}
		//Standards
		else{
			$ligneForcerCalcul.show();
			$('.msgRetroBox').hide();
			$('.msgPeriodBox').hide();
			$submitCalculButton.show();
		}
	});
	
	//Deafut framework
	actionMethod=$('[name=_method]',document.forms[0]).val();
	userAction=$('[name=userAction]',document.forms[0])[0];

});