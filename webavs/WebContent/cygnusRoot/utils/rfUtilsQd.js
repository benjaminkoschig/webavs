
$(function() {
	var listMontants = $("[class=montant]");
	
	listMontants.change(function(){
		$(this).val(formatMnt($(this).val()));
		
		mntResiduelElems = ["limiteAnnuelle","soldeCharge","augmentationQd"];
		
		if ($.inArray($(this).attr("name"), mntResiduelElems) != -1){
			updateMntResiduel();
		}
	});
	
	listMontants.change();
});

function formatMnt(valueStr) {
	
	var decote=/'/g;
	valueStr = valueStr.replace(decote,"");
	if (isNaN(valueStr))
		valueStr="";
	else
		valueStr = (new Number(valueStr)).toFixed(2);
	
	var i = valueStr.indexOf(".");
	var j = 0;
	while (i>0) {
		if (j%3==0 && j!=0 && valueStr.charAt(i-1)!='-') {
			valueStr = valueStr.substring(0,i) +"'"+ valueStr.substring(i);
			j++;
			i--;
		}
		i--;
		j++;
	}
	
	return valueStr;
}

function updateMntResiduel(){

	var decote=/'/g;
	
	if (document.getElementsByName("limiteAnnuelle")[0].value == ''){
		document.getElementsByName("limiteAnnuelle")[0].value=parseFloat(0).toFixed(2);
	}
	
	if (document.getElementsByName("soldeCharge")[0].value == ''){
		document.getElementsByName("soldeCharge")[0].value=parseFloat(0).toFixed(2);
	}
	
	if (document.getElementsByName("augmentationQd")[0].value == ''){
		document.getElementsByName("augmentationQd")[0].value=parseFloat(0).toFixed(2);
	}
	
	if (document.getElementsByName("mntCharge")[0].value == ''){
		document.getElementsByName("mntCharge")[0].value=parseFloat(0).toFixed(2);
	}
	
	var limiteAnnuelle 	= document.getElementsByName("limiteAnnuelle")[0].value;
	var soldeCharge 	= document.getElementsByName("soldeCharge")[0].value;
	var augmentationQd 	= document.getElementsByName("augmentationQd")[0].value;
	var mntCharge 		= document.getElementsByName("mntCharge")[0].value;
	
	limiteAnnuelle 	= limiteAnnuelle.replace(decote,"");
	soldeCharge 	= soldeCharge.replace(decote,"");
	augmentationQd 	= augmentationQd.replace(decote,"");
	mntCharge 		= mntCharge.replace(decote,"");

	var total = parseFloat(limiteAnnuelle) - 
				parseFloat(soldeCharge)    + 
				parseFloat(augmentationQd) -
				parseFloat(mntCharge);

	var mntResiduelElem = document.getElementById("idMntResiduelAffiche");
	mntResiduelElem.innerHTML='<b><i>'+formatMnt(parseFloat(total).toFixed(2))+'</i></b>';
}

