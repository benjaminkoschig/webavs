/*
 * gmo, 23.05.2012 pas utilisé pour le moment
 * javascript pour gérer la partie rafam du droit
 */
	
//------------------------------------------------------------
//Appel Ajax pour générer les annonces en état enregistrée
//------------------------------------------------------------


function AnnonceAjax() {
	//marche pas ??
	this.etatTmp = "";
	this.idDroit = "";

	this.init = function () {
		
	
		var that = this;
		alert('init1');
//		$("input[@name='radioRecordNumber']").change(function (){
//			alert('test');
//			
//			
//		});
		//alert('radio click value '+$('input[type=radio][name=radioRecordNumber]:checked').attr('value'));

		//TODO: initialiser la détection de radioBox
		//et faire le job pour mettre à jour le champ recordNumber de l'annonce temporaire
		
		
//			this.initTableAjax();
//			this.initButton();
//			$(".imgLoading").remove();
	};

	this.generateAndLoad = function(){
		alert('generateAndLoad');
		var a_params = new Array(
				"dateDebut:"+$("#debutDroit").val(),
				"dateFin:"+$("#finDroitForcee").val(),
				"dateNaissance:"+$("#dateNaissance").val(),
				"idDroit:"+this.idDroit
				
		);
		
		for(i=0;i<a_params.length;i++) {
			a_params[i] = a_params[i].replace(/\'/g,"");
		}				
		
		
		var o_options= {
				serviceClassName: 'ch.globaz.al.business.services.models.droit.DroitBusinessService',
				serviceMethodName:'readWidget_apercuAnnoncesRafam',
				parametres:'['+a_params+']',
				callBack:this.displayAnnonces
		
		};
		globazNotation.readwidget.options=o_options;		
		globazNotation.readwidget.read();	
	};
	
	this.displayAnnonces = function(data){
		alert('displayAnnonces');
		var annoncesTemporaires = [];
		var annoncesExistantes = [];
		var s_html="";
		for (var i = 0; i < data.length; i++) {					
			if(data[i].etat == '61370001'){	
				annoncesTemporaires.push(data[i]);
			}
			else{
				annoncesExistantes.push(data[i]);
			}
		}	
		
		//on parcours toutes les annonces proposées ( 1 par type de prestation) (Ex: 1 enf et 1 nais)
		for(var i = 0;i< annoncesTemporaires.length;i++){
			var annoncesExistantesMemePrestation = [];
			//on parcours toutes les annonces existantes du même type que la temporaire courante
			for(var j=0;j<annoncesExistantes.length;j++){
				if(annoncesExistantes[j].genrePrestation == annoncesTemporaires[i].genrePrestation){
					annoncesExistantesMemePrestation.push(annoncesExistantes[j]);
				}
			}
			s_html+= displayBlocTypePrestation(i,annoncesTemporaires[i], annoncesExistantesMemePrestation);
	
		}
		
		//marche pas...
		//alert('displayAnnonces: etat tmp:'+that.etat_tmp);
		
		$('#dialogRafamTemp').append(s_html);
		
	
		$( "#selectable_0" ).selectable({
			selected : function(event,ui){
				if($(ui.selected).find('div.recordNumber').text()!='0'){
					$(this).closest('div').siblings('.rafamProposition').find('.recordNumber').text($(ui.selected).find('div.recordNumber').text());
				}
				
			}
			
		});
		if($( "#selectable_1" )){
				$( "#selectable_1" ).selectable();
		}
		if($( "#selectable_2" )){
				$( "#selectable_2" ).selectable();
		}
		
		
		

		$('#dialogRafamTemp').dialog('open');
		
		
	};
	
	
	/**
	 * convertir l'annonce proposée et les annonces existantes pour ce droit / type de prestation en html
	 * via des templates
	 */
	var displayBlocTypePrestation = function (index,newAnnonce,annoncesExistantes){

		
		var templateTemporaire = '<table class="al_listrafam">'+
		'<tr>'+
        '<th scope="col" >Annonce proposée</th>'+
        
   		'</tr>' +
   		'<tr >'+
   		'<td>{{debutDroit}} - {{echeanceDroit}}<div class="hide cle">{{idAnnonce}}</div><div class="hide recordNumber">{{idAnnonce}}</div></td>'+
   		'</tr>'+
   		'</table>';
   			
		var templateListeExistantes = '<table class="al_listrafam">'+
		'<tr>'+
        '<th scope="col" >Annonces générées</th>'+
   		'</tr>' +
   		'<tr>'+
   		'<td >'+
   		'<ol id="selectable_{{css_index}}">'+
   		'{{existantes}}'+
   		'<li style="border-top:1px solid black;"><div class="recordNumber hide">0</div>Nouvelle annonce</li>'+
   		'</ol>'+
   		'</td>'+ 		
   		'</tr>'+
   		'</table>';
   			
		var templateOneExistante = '<li>'+
		//'<td><input type="radio" name="radioRecordNumber_{{genrePrestation}}" value="{{recordNumber}}">{{recordNumber}}</input></td>'+
		'<div class="recordNumber hide">{{recordNumber}}</div>'+
          '{{debutDroit}} - {{echeanceDroit}}'+
          '{{genrePrestation}}'+
   		'</li>'; 

		var o_lignes = {
				css_index:index,
				genrePrestation:newAnnonce.genrePrestation,
				existantes : ""
		};
		
		
		
		//transformation des annonces existantes via le template
		for (var i = 0; i < annoncesExistantes.length; i++) {
			o_lignes.existantes += globazNotation.template.compile(annoncesExistantes[i], templateOneExistante);
		}

		var o_bloc = {
				type_prest:newAnnonce.genrePrestation,
				bloc_existantes :  "",
				bloc_temporaire : ""
		};

		o_bloc.bloc_existantes = globazNotation.template.compile(o_lignes, templateListeExistantes);
		
		o_bloc.bloc_temporaire = globazNotation.template.compile(newAnnonce, templateTemporaire);
		

		
		var templateListeParTypePrestation = '<div class="rafamBlocType">'+
		'<div class="subtitle ">{{type_prest}}</div>'+
		'<div class="rafamProposition">{{bloc_temporaire}}</div>'+
		'<div class="rafamExistant">{{bloc_existantes}}</div>'+
		'</div> ';
		var htmlBloc = globazNotation.template.compile(o_bloc, templateListeParTypePrestation);
		alert('displayBlocTypePrestation4'+htmlBloc);
		
		return htmlBloc;
		
	};
	
	/**
	 * Méthode qui met à jour les annonces enregistrées avec recordNumber choisi et enregistré => à_transmettre
	 */
	this.valide = function(){
	
		$('.rafamProposition').each(function(index){
			var idAnnonce= $(this).find('.cle').text();
			var recordNumber= $(this).find('.recordNumber').text();
			
			if(idAnnonce==recordNumber){
				alert('68a');
			}
			else{
				alert('68b');
			}
			
			
			var a_params = new Array(
					"idAnnonce:"+idAnnonce,
					"recordNumber:"+recordNumber			
			);
			
			for(i=0;i<a_params.length;i++) {
				a_params[i] = a_params[i].replace(/\'/g,"");
			}				
			
			
			var o_options= {
					serviceClassName: 'ch.globaz.al.business.services.rafam.AnnonceRafamBusinessService',
					serviceMethodName:'readWidget_changeRecordNumber',
					parametres:'['+a_params+']'
			
			};
			globazNotation.readwidget.options=o_options;		
			globazNotation.readwidget.read();	
			
		});
		
		
		
		//OLD 
		//pour chaque radio recordNumber sélectionné, on met à jour avec l'annonce temporaire		
		$('input:radio[name^="radioRecordNumber_"]:checked').each(function(index){
			
			var idAnnonce = $(this).closest('div').siblings('.rafamProposition').find('.cle').text();
			alert('validation3:'+idAnnonce);
			var newRecordNumber = '';
			if($(this).val()=='0'){
				
				newRecordNumber = idAnnonce,
				alert('68a, donc recordNumber : '+newRecordNumber);
			}
			else{
				newRecordNumber=$(this).val();
				alert('68b, donc recordNumber : '+newRecordNumber);
			}
		
			var a_params = new Array(
					"idAnnonce:"+idAnnonce,
					"recordNumber:"+newRecordNumber			
			);
			
			for(i=0;i<a_params.length;i++) {
				a_params[i] = a_params[i].replace(/\'/g,"");
			}				
			
			
			var o_options= {
					serviceClassName: 'ch.globaz.al.business.services.rafam.AnnonceRafamBusinessService',
					serviceMethodName:'readWidget_changeRecordNumber',
					parametres:'['+a_params+']'
			
			};
			globazNotation.readwidget.options=o_options;		
			globazNotation.readwidget.read();	
			
			
			//alert($(this).closest('div').siblings('.rafamProposition').find('.recordNumber').val());
		});
		
		
		
	};
	
	/** 
	 * annule les annonces temporaires créées
	 */

	this.cancel = function(){
		var a_params = new Array(
				"idDroit:"+this.idDroit
				
		);
		
		for(i=0;i<a_params.length;i++) {
			a_params[i] = a_params[i].replace(/\'/g,"");
		}				
		
		
		var o_options= {
				serviceClassName: 'ch.globaz.al.business.services.models.droit.DroitBusinessService',
				serviceMethodName:'readWidget_deleteAnnoncesTemporaires',
				parametres:'['+a_params+']'
			
		};
		globazNotation.readwidget.options=o_options;		
		globazNotation.readwidget.read();	
		
	};
}



