var historiqueChamps = {
	
	mapValeur : {},
	
	init:function(domElements, histoUserValues){
		$mesElements = $(domElements);
		this.mapValeur = histoUserValues;
		this.bindEvent($mesElements);	
	},	
		
	bindEvent: function ($elements){
		var that = this;
		$elements.keypress(function (event) {
			that.fillCell(this,event);
		});
		
	},
	
	fillCell: function (element,event1) {
		//touche '=' pressée
		if (event1.keyCode==61 && element.value=='') {
			var valeur = this.mapValeur[element.id] ;
			if(valeur != null){
				element.value= valeur ;	
		  	} 
			event.keyCode=null;
		}
	}
};
