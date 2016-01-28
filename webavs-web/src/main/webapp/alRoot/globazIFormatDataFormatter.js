/*
 * Cet objet globazNotation sera � d�placer avec les autres.
 * une fois valid� / adapt� par DMA
 */
globazNotation.numaffilieformatter={
	
	forTagHtml : 'input',
	
	description : "Cet objet formatte la valeur saisie dans le champ input.<br />",
			
    descriptionOptions: {
		formatterClass: {
    		desc: "La classe de formattage � utiliser (doit impl�menter IFormatData)",
    		param: "aucune(default)|classe impl�mentant IFormatData"
		} 
	},
	
	/**
	 * Parm�tre de le l'objet qui vont �tre pour son initilisation
	 * Cet �l�ment est obligatoire, Si pas d'option le cr��er mais vide (options:{})
	 * TODO:ajouter une option minChars pour le d�clenchement du formattage
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
	 * Fonction de gestion des �v�nements
	 */
	initEvents:function($elementToPutObject){
		var that = this;
	
		//gestion regexp des caract�res autoris�s
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
			//TODO:faire une fonction retourne la valeur formatt�e
			$.ajax({
				  url: urlBase+'/alRoot/IFormatData_formatter.jsp?formatter='+that.options.formatterClass+'&value='+$elementToPutObject.val(),
				  success: function(data) {
			
					response = eval("(" + data + ")");
					$elementToPutObject.val(response.result);
					if(response.status=='-1'){
						//TODO: g�rer l'erreur mettre en rouge le champ par exemple
					}
						
				 }
				});	
		});
		
	}
	
	
} 