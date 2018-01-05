
var setConjointInfosState = function (displayState) {
	
	if(displayState){
		
		var maxHe = $('.adresse').getObjectByValue('max','height').height();
		
		$('.adresse').each(function () {
			
			$(this).height(maxHe);
		});
		$('#zoneAdressePaiementReq').addClass('adresseConjoint');
	}else{
		$('#conjointInfos').hide();
		$('#zoneAdressePaiementCon').hide();
	}
}

var showConfirmDialogForBlocage = function () {
	$( "#dialog-warningRFM-confirm" ).dialog({
        resizable: false,
        height:300,
        width:500,
        modal: true,
        
        buttons: {
        	"OK": function() {
                $( this ).dialog( "close" );
                //userAction.value=ACTION_DECISION_DEVALIDE;
        		action(COMMIT);
            },
            "Annuler": function() {
                $( this ).dialog( "close" );
            }
        }
    });
};