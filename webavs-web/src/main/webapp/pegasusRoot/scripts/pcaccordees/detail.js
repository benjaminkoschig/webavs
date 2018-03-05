
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

