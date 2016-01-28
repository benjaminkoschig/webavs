var listeMotif;
var listeSousMotifs;

//Fonction de manipulation de liste, initialisation
var listHandler = function () {
	//recuperation des objets listes
	listeMotif = $('#selectMotif');
	listeSousMotifs = $('#selectSousMotif');
	
	var blockSousMotifs = $('.selectSm');
	//on cache la liste des sous-motifs
	blockSousMotifs.hide();
	
	//ajout evenement onchange liste motif
	listeMotif.change(function(){
		blockSousMotifs.hide();
		//recup value codesysteme selected
		var selected = listeMotif.find(':selected').attr('value');
		//pour chaque motif du tableau
		for(var i in tab)
		{
			//si l'entre du motif correspond
			if((tab[i][0])==selected){
				listeSousMotifs.children().remove();//on vide la sous liste
				tableauSM = tab[i][1];
				//si le tableau sm n est pas undefined, pas de sous motifs
				if(tableauSM!=undefined){
					//parcours des valeurs
					for(var j in tableauSM)
					{
						var value = tableauSM[j][0];
						 var libelle = tableauSM[j][1];
						 
						 listeSousMotifs.append("<option value='"+ value +"'>"+libelle+"</option>")
					}
					
					
					 	//ajout focus ligne intro sousmotif
					listeSousMotifs.prepend("<option libelle='intro'></option>");
					listeSousMotifs.find('[libelle=intro]').attr('selected',true);
					blockSousMotifs.show();
					
				}
			}else{
				listeSousMotifs.children().remove();
			}
		}
		
	});
	
	//ajout evenement on change sous liste, suppression intro
	listeSousMotifs.change(function(){
		$(this).find('[libelle=intro]').remove();
	});
	
};

//Affichage du sous motifs au chargement de la page
var displaySousMotifsOnLoad = function (sousMotifs){
	
	var selected = listeMotif.find(':selected').attr('value');
	var smToDisplay;
	// selecetion on enleve la premiere entree
	//listeMotif.find('[libelle=intro]').remove();
	//pour chaque motif du tableau
	for(var i in tab)
	{
		//si l'entre du motif correspond
		if((tab[i][0])==selected){
			listeSousMotifs.children().remove();//on vide la sous liste
			tableauSM = tab[i][1];
			//si le tableau sm n est pas undefined, pas de sous motifs
			if(tableauSM!=undefined){
				//parcours des valeurs
				for(var j in tableauSM)
				{
					var value = tableauSM[j][0];
					var libelle = tableauSM[j][1];
					//si le sousmotifs est egal a celui passé en param
					if(value==sousMotifs){
						smToDisplay = value;
						
					}
				
					
					listeSousMotifs.append("<option value='"+ value +"'>"+libelle+"</option>")
				}
				
				 	//ajout focus ligne intro sousmotif
				listeSousMotifs.prepend("<option libelle='intro'></option>");
				listeSousMotifs.find('[libelle=intro]').attr('selected',true);
				//blockSousMotifs.show();
				
			}
		}
	}
	$("#selectSousMotif option[value='"+smToDisplay+"']").attr("selected", "selected");
	
};