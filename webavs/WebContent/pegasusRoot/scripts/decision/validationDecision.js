/**
 * DMA
 */

var checkValiderTout = {
	$form: null,
	
	init: function(){
		this.bindEvent();
		this.$form = $("[name=mainForm]");
		$("#btnCtrlJade").hide();
		
	},
	
	bindEvent: function(){
		var that = this;
		$("#validerTous").click(function(){
			$("[name=userAction]").val(userAction);
			that.$form.append("<input type='hidden' name='valider' value='true'");
			if(parseFloat(montantDecision) < 0) {
				 showConfirmDialogForCreateLot(that);
			} else {
				that.$form.submit();
			}
		}).attr("disabled",false);
	}
};

var showConfirmDialogForCreateLot = function (that) {
	$( "#dialog-confirm-creation-lot" ).dialog({
        resizable: false,
        height:250,
        width:500,
        modal: true,
        
        buttons: {
            "Oui": function() {
                $( this ).dialog( "close" );
                $('#isComptabilisationAuto').val("true");
    			that.$form.submit();
            },
            "Non": function() {
                $( this ).dialog( "close" );
    			that.$form.submit();
            }
        }
    });
};

jsManager.add(function (){checkValiderTout.init();},'Initialisation de checkValiderTout');