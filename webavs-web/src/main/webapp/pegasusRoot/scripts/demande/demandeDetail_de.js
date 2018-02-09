  
var showConfirmDialogAnnulation = function () {
		$( "#dialog-confirm-annulation" ).dialog({
	        resizable: false,
	        height:250,
	        width:500,
	        modal: true,
	        
	        buttons: {
	        	"Non": function() {
	                $( this ).dialog( "close" );
	                $('#comptabilisationAuto').val("false");
	                action(COMMIT);
	            },
	        	"Oui": function() {
	                $( this ).dialog( "close" );
	                $('#comptabilisationAuto').val("true");
	        		action(COMMIT);
	            }
	        }
	    });
	};