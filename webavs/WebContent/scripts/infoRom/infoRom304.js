var idOfDepNumAffInput = "";
var idOfEndNumAffInput = "";

function initIdOfNumAffInput(){
	if($(".idEcran").text() == "CFA0010"){
		idOfDepNumAffInput = "fromIdExterneRole"; 
		idOfEndNumAffInput = "tillIdExterneRole";
	}else if($(".idEcran").text() == "CCP2001"){
		idOfDepNumAffInput = "fromAffilieDebut"; 
		idOfEndNumAffInput = "fromAffilieFin";
	}else if($(".idEcran").text() == "CDS2001"){
		idOfDepNumAffInput = "fromAffilies"; 
		idOfEndNumAffInput = "untilAffilies";
	}
}

function showHidePlageNumAffInput()
{
	
	$("#plageNumAff").show();
	$("#oneNumAff").hide();
	
	if($("#chkImpressionUnSeulAffilie").is(':checked')){
		$("#plageNumAff").hide();
		$("#oneNumAff").show();	
	}	
		
}

function validate(){
	
	$("#erreurNumAffObligatoire").hide();
	
	if($("#chkImpressionUnSeulAffilie").is(':checked') && ($("#" + idOfDepNumAffInput).val() == '' || $("#" + idOfEndNumAffInput).val() == '')){
		$("#erreurNumAffObligatoire").show();
		return false;
	}
	
	saveValueChkImpressionUnSeulAffilie();
	
	if($(".idEcran").text() == "CDS2001"){
		$("#affilieTous").attr("checked",false);
		if($("#" + idOfDepNumAffInput).val() == '' && $("#" + idOfEndNumAffInput).val() == ''){
			$("#affilieTous").attr("checked",true);
		}
	}
	
	return true;
}

function setPlageNumAff()
{
	$("#" + idOfDepNumAffInput).val($("#forIdExterneRole").val());
	$("#" + idOfEndNumAffInput).val($("#forIdExterneRole").val());
}

function clearInputsNumAff(){
	$("#" + idOfDepNumAffInput).val("");
	$("#" + idOfEndNumAffInput).val("");
	$("#forIdExterneRole").val("");
}

function saveValueChkImpressionUnSeulAffilie(){
	
	$("#valueKeeperChkImpressionUnSeulAffilie").val("unchecked");
	
	if($("#chkImpressionUnSeulAffilie").is(':checked')){
		$("#valueKeeperChkImpressionUnSeulAffilie").val("checked");
	}
}

function initValueChkImpressionUnSeulAffilie(){
	
	$("#chkImpressionUnSeulAffilie").attr("checked",true);
	
	<%if(request.getParameter("valueKeeperChkImpressionUnSeulAffilie")!=null && "unchecked".equalsIgnoreCase(request.getParameter("valueKeeperChkImpressionUnSeulAffilie"))){%>		
			$("#chkImpressionUnSeulAffilie").attr("checked",false);
	<%}%>	
}

$(document).ready(function() {
	initIdOfNumAffInput();
	initValueChkImpressionUnSeulAffilie();
	showHidePlageNumAffInput();	
	$("#erreurNumAffObligatoire").hide();
});