//------------------------------------------------------------
// Appel Ajax pour le lancement de word avec 
// id du controleur à afficher
//------------------------------------------------------------
function generateWordLauncher(_currentIdEnvoiStatus) {
	// Show Progress Windows
	showProgress('#inProgressDialog');
	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.models.controleurEnvoi.ControleurEnvoiService',
		serviceMethodName:'getInteractivDocumentFullFileName',
		parametres:_currentIdEnvoiStatus,
		callBack: launchWord
	}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}

//------------------------------------------------------------
// Lancement de word si data est correctement renseigné 
//------------------------------------------------------------
function launchWord(data){
	// Hide Progress Windows
	hideProgress('#inProgressDialog');
	// si OK, ouverture du fichier et raffraichissement de la page
	// -----------------------------------------------------------
	try{
		var s_filepath=""+data;
		if(s_filepath.length<=0){
			alert("Error, file not found !");
		}else{
			var word=null;
			try {
		  		if(word==null){
		  			word = new ActiveXObject('Word.Application');
		  		}
			    word.application.visible="true";
		  	} catch(e){
			   	word = new ActiveXObject('Word.Application');
			    word.application.visible="true";
		  	}
		    var currentDocument = word.documents.open(s_filepath);
		}
	} catch (err){
		errorMessage+="\r\nError Description : "+err.description;
		alert(errorMessage);
	}

}
