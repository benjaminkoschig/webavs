function tryConsole(msg) { 
   if (typeof console != "undefined") { 
       console.log(msg); 
   } 
} 

function SimplePopup(element){
	 
    this._element = element;
    this._$element = $(element);
	this._title = $(element).data('al-popup').options.title;

	this._iconcss = $(element).data('al-popup').options.iconcss;
	this._height = $(element).data('al-popup').options.height;
	
	this._msg = "";
	for(var i = 0; i < $(element).data('al-popup').options.text.length;i++){
		this._msg = this._msg.concat($(element).data('al-popup').options.text[i]);
		this._msg = this._msg.concat("<br/><br/>");
	}
	

	this._templateHtml = '<div id="contentContainer"><div id="sidebar" class="$cssicon-placeholder$"></div><div id="main">$text-placeholder$</div></div>';
	
	var htmlContent = this._templateHtml.replace('$cssicon-placeholder$',this._iconcss);
	htmlContent = htmlContent.replace('$text-placeholder$',this._msg);
	this._$element.attr('title',this._title);
	
	this._$element.html(htmlContent);
	
    this._$element.hide();
}

SimplePopup.prototype.displayIt = function(){

	this._$element.dialog({
			height:this._height,
			width:400,
			modal: true,
			buttons: {
				Ok: function() {
					$(this).dialog( "close" );
				}
			}
	 });
	
};

function RedirectPopup(element){	
	//call to super constructor
	SimplePopup.call(this, element);
	this._url = $(element).data('al-popup').options.url;
	this._redirectBtnLabel = $(element).data('al-popup').buttons[0] || 'No label defined';
	this._ignoreBtnLabel = $(element).data('al-popup').buttons[1] || 'No label defined';	    
}

RedirectPopup.prototype = Object.create(SimplePopup.prototype);
RedirectPopup.prototype.constructor = RedirectPopup;
RedirectPopup.prototype.displayIt = function() {
	//if you want to call super method :  SimplePopup.prototype.displayIt.call(this) + "!!";
	//do here your specific job
	
	var buttonsOpts = {};
	var that = this;
	buttonsOpts[this._redirectBtnLabel] = function(){
		$( this ).dialog( "close" );
        window.location.href = that._url;
	};
	buttonsOpts[this._ignoreBtnLabel] = function (){
		 $( this ).dialog( "close" );
	};
	

	this._$element.dialog({
			height:this._height,
			width:400,
			modal: true,
			buttons: buttonsOpts
	 });
    
};
tryConsole('initialisation of simple-popup.js --');
//Attache les objets au éléments DOM
$('html').find('[data-al-popup]').each(function (index, element) {
	
	var $element = $(element);
	//capture des events en fonction du type :
	//warn-type1
	//warn-type2
	$element.bind("newWarnMsg", function(){
		var objPopup = null;
		if($element.data('al-popup').type=='redirect'){		
			objPopup = new RedirectPopup(element);
		}
		
		if($element.data('al-popup').type=='simple'){	
			objPopup = new SimplePopup(element);
		}
		objPopup.displayIt();	
		
	});

});




/* USAGE 
 HTML : <div id="popup-container" data-al-popup=''></div>
 JS : 	$('#popup-container').data('al-popup',jsonObject);

Exemple jsonObject =  {"type":"redirect","options":{"title":"Avertissement","text":"L'adresse de paiement n'est pas renseignée pour le tiers","iconcss":"warningIcon","url":"webavs/pyxis?userAction=pyxis.adressepaiement.avoirPaiement.afficher&_method=add&back=_sl&idTiers=222774","height":200},"buttons":["Compléter","Valider"]}
  
*/