var showConfirmDialogForSuppression = function () {
	$( "#dialog-warningRFM-confirm" ).dialog({
        resizable: false,
        height:300,
        width:500,
        modal: true,
        
        buttons: {
        	"OK": function() {
                $( this ).dialog( "close" );
                state = true;
                $( "#preparer_btn_sup1" ).hide();
                $( "#preparer_btn_sup2" ).show();
                $( "#preparer_btn_sup2" ).click();
        		action(COMMIT);
            },
            "Annuler": function() {
                $( this ).dialog( "close" );
            }
        }
    });
};


