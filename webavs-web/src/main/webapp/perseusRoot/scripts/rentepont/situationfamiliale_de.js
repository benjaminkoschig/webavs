
	function add() {}
	
	function upd() {
		$('#widgetTiers').show();
		$('#linkSupprimerConjoint').show();
	}
	
	function del() {}
	
	function validate() {
	    state = validateFields();
	    document.forms[0].elements('userAction').value="perseus.rentepont.situationfamiliale.modifier";
	    
	    return state;	
	}
	
	function cancel() {
		if (document.forms[0].elements('_method').value == "upd")
			document.forms[0].elements('userAction').value="back";
	}
	
	function init() {}
	
	function postInit() {
		$("#csNationaliteAffiche").attr("disabled","true");
		$("#partiallikeNSS").attr("disabled","true");
	}

	function supprimerConjoint() {
		$('#nss').val("");
		$('#idTiers').val("");
		$('#resultAutocompete').children().remove();
	}