$(function (){
	//Initialisation des variables jquery
	$_headerChk = $('#headerCheckBox');
	$_submitButton = '';//$('');
	$_form = $("[name=mainForm]");
	$_fratrieTable = $('#fratrieTable');
});

//ajout des événements
var addEvents = function () {
	//Clic checkbox header
	$_headerChk.click(function () {
		//alert('click' +$('#thChkBox').is(':checked'));
		setAllChildState($_headerChk.is(':checked'));
	});
	
	//Click sur les boutons radios en
	$_fratrieTable.find('.tdChkChild').each(function() { 
		$(this).click(function(){
			
			//Si unchek, desactiver radio hedaer
			if(!($(this).is(':checked'))){
				$_headerChk.attr('checked',false);
			}
		});
	}); 
};

//Set l'état des boutons radios
var setAllChildState = function (state) {
	
	$_fratrieTable.find('.chkChild').each(function() { 
		$(this).attr('checked',state);
	}); 
};

var isAllBoutonsUnchecked = function (){
	var allUnchecked = true;
	$_fratrieTable.find('.tdChkChild').each(function() { 
		
		//Si la case est cocher on set l'id dans le champ caché
		if($(this).is(':checked')){
			allUnchecked = false;
		}
	}); 
	return allUnchecked;
};