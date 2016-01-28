var REMARQUE_DECOMPTE;


function callback_function(data){
	if(data=="-1")
		$("#remarqueButtonSpan").css("background-image", "url(theme/jquery/images/ui-icons_f9bd01_256x240.png)");
	if(data=="0")
		alert(JSP_REMARQUE_SIZE_LIMIT_MESSAGE_INFO);
		$("#remarqueButtonSpan").css("background-image", "url(theme/jquery/images/ui-icons_f9bd01_256x240.png)");
	if(data=="1")
		$("#remarqueButtonSpan").css("background-image", "url(theme/jquery/images/ui-icons_green_256x240.png)");
}

function initButtonDisplay(){
	var element = document.getElementById("areaAssureIdCsEtatDroit"); 
    var sDataValue = element.getAttribute("areaAssureIdCsEtatDroit");  
    //64003001 Enregistré
    //64003002 Au calcul
    //64003003 Calculé
    //64003004 Validé
    //64003005 Historisé
    //64003006 Courant validé
    if(sDataValue > 64003003){
    	$("#remarqueButton").prop('disabled', true);
    	$("#remarqueDecompte").prop('disabled', true);
    }
}

function bindEventButton(){
	$("#remarqueButton").click(function(){
			valideModification();	
		});
}

function imposeMaxLength(obj){
	$("#remarqueButtonSpan").css("background-image", "url(theme/jquery/images/ui-icons_f9bd01_256x240.png)");
	var mlength=1023;//1023 is 1024th position (begin 0)
	if (obj.getAttribute && obj.value.length>mlength){
		$("#remarqueSizeLimit").css("display","block");
	  //obj.value=obj.value.substring(0,mlength);
	}else{
		$("#remarqueSizeLimit").css("display","none");
	}
}

function valideModification(){
	REMARQUE_DECOMPTE = $('#remarqueDecompte').val().replace(/,/g , "&#44;");
	if(REMARQUE_DECOMPTE.length=0){
		REMARQUE_DECOMPTE=" ";
	}
	var affichage_decompte_service_call_options = {
			
			serviceClassName: 'ch.globaz.pegasus.business.services.decompte.DecompteService',
			serviceMethodName: 'saveRemarqueSurDecompte',
			callBack: callback_function,		
			parametres: ID_VERSIONDROIT + ", " + REMARQUE_DECOMPTE
	}
	globazNotation.readwidget.options = affichage_decompte_service_call_options;
	globazNotation.readwidget.read();
	
}

$(function () {
	ajaxUtils.triggerStartNotation();
	
		initButtonDisplay();
		bindEventButton();	
	
});
