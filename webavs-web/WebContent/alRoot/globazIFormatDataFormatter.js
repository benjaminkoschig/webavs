/*
 * Cet objet globazNotation sera à déplacer avec les autres.
 * une fois validé / adapté par DMA
 */
globazNotation.numaffilieformatter={
	
	forTagHtml : 'input',
	
	description : "Cet objet formatte la valeur saisie dans le champ input.<br />",
			
    descriptionOptions: {
		formatterClass: {
    		desc: "La classe de formattage à utiliser (doit implémenter IFormatData)",
    		param: "aucune(default)|classe implémentant IFormatData"
		} 
	},
	
	/**
	 * Parmétre de le l'objet qui vont être pour son initilisation
	 * Cet élément est obligatoire, Si pas d'option le crééer mais vide (options:{})
	 * TODO:ajouter une option minChars pour le déclenchement du formattage
	 */
    options:{
    	formatterClass:''
    	},
	
   	/**
	 * Cette fonction est obligatoire car c'est elle qui va initiliser l'objet
	 */
	  
	init:function($elementToPutObject){
    	this.initEvents($elementToPutObject);
	},
    
   
	/**
	 * Fonction de gestion des événements
	 */
	initEvents:function($elementToPutObject){
		var that = this;
	
		//gestion regexp des caractères autorisés
		$elementToPutObject.blur(function(e){
			
			var reg = /(^[0-9\.\-]*$)/;	//[0123456789\.]*
			
			if (($elementToPutObject.val().search(reg)>=0)) {
  			} 
			else
			{
   			 	var arr = $elementToPutObject.val();
				var newArr = (arr.substr(arr.lenght-2,arr.length-1));
				$elementToPutObject.val(newArr);
  			}
		
			var urlBase = $('[name=Context_URL]').attr('content');
			//TODO:faire une fonction retourne la valeur formattée
			$.ajax({
				  url: urlBase+'/alRoot/IFormatData_formatter.jsp?formatter='+that.options.formatterClass+'&value='+$elementToPutObject.val(),
				  success: function(data) {
			
					response = eval("(" + data + ")");
					$elementToPutObject.val(response.result);
					if(response.status=='-1'){
						//TODO: gérer l'erreur mettre en rouge le champ par exemple
					}
						
				 }
				});	
		});
		
	}
	
	
} 