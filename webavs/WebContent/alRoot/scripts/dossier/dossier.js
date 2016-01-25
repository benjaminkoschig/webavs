var url = null;
var canReload = false;

if (typeof MAIN_URL == 'undefined') {
		url = $('[name=formAction]').attr('content');
}else{
		url = MAIN_URL;
}


var templateTrHtml = '<tr id="${id_lien}">';
templateTrHtml +='<td><a class="deleteLink" href="#" onclick="clickOnDeleteLink(\'${id_lien}\');" title="Supprimer lien"/><input type="hidden" name="idLien" value="${id_lien}"/></td>';
templateTrHtml +='<td class="dossier_id"><a href="'+urlServletPath+'?userAction=al.dossier.dossierMain.afficher&selectedId=${dossier_id}" title="">${dossier_id}</a></td>';
templateTrHtml +='<td>${allocataire_nom} ${allocataire_prenom}</td>';
templateTrHtml +='<td>${allocataire_nss} </td>';
templateTrHtml +='<td>${affilie_numero}</td>';
templateTrHtml +='<td>${dossier_activite}</td>';
templateTrHtml +='<td>${dossier_statut}</td>';
templateTrHtml +='<td>${dossier_etat}</td>';
templateTrHtml +='<td>${droit_debut} ${dossier_radiation}</td>';
templateTrHtml +='<td class="type_lien">${typeLien}</td>';
templateTrHtml += '</tr>';

/**
 *
 * Idem document ready
 * 
 */
$(document).ready(function() {
	initDebugManager();
	
	// Réaction sur le nombre d'enfants, on set le champs à 0 si vide et on recalcule (changement de cellule)
	// ----------------------------------------------------------------------
	$('#debutValidite').change(function (e){	
		$("#debutValidite").css("color", "black");
		if ($('#debutValidite').val().length>0) {
			
			ajaxDateDebutValidite();
		}		
	});
	
	// Réaction sur le nombre d'enfants, on set le champs à 0 si vide et on recalcule (changement de cellule)
	// ----------------------------------------------------------------------
	$('#finValidite').change(function (e){	
		$("#finValidite").css("color", "black");
		if ($('#finValidite').val().length>0) {
			
			ajaxFinValidite();
		}		
	});
	
	
	//Gestion de l'affichage des droits inactifs via + - buttons
	$('tr.child').hide();	
	$(".hideDetail").hide();
	$(".hideAllDetails").hide();
	
	$("#droits a.showAllDetails").click(function() {	 
		
		//on montre tous les enfants 
		$('tr.child').show();	
		//on affiche le lien collapse (-)
	    $(this).siblings(".hideAllDetails").show();
	    //on cache le lien expand (+)(celui clické)
	    $(this).hide();
	    //on cache tous les liens expand(+) par droit parent
		$(".showDetail").hide();
		//on montre tous les liens collapse(-) par droit parent
		$(".hideDetail").show();
	});
	
	$("#droits a.hideAllDetails").click(function() {	
		
		//on cache tous les enfants
		$('tr.child').hide();	
		//on affiche le lien expand (+)
	    $(this).siblings(".showAllDetails").show();
	  //on cache le lien collapse (-)(celui clické)
	    $(this).hide();
	  //on cache tous les liens collapse(-) par droit parent
		$(".hideDetail").hide();
		//on montre tous les liens collapse(+) par droit parent
		$(".showDetail").show();
	});
	
	$("#droits a.showDetail").click(function() {	 
		
		var cssFull = $(this).closest("tr").attr("class");			
		var cssParent =  cssFull.substring(cssFull.indexOf("parent-"));
		var cssChild = "child-".concat(cssParent.substring(7));	

		//on montre les enfants
		$("."+cssChild).show();
		//on affiche le lien collapse (-)
	    $(this).siblings(".hideDetail").show();
	    //on cache le lien expand (+)(celui clické)
	 
	    $(this).hide();
	    
	    //si plus aucun bouton + est visible, on manage en conséquence le bouton + et - relatif à tous les droits
	    if(!$("#droits a.showDetail").is(":visible")){
	    	$(".hideAllDetails").show();
	    	$(".showAllDetails").hide();
	    }
	        	
	});
	
	$("#droits a.hideDetail").click(function() {   
		
		var cssFull = $(this).closest("tr").attr("class");	
		var cssParent =  cssFull.substring(cssFull.indexOf("parent-"));
		var cssChild = "child-".concat(cssParent.substring(7));	
		//on cache les enfants
		$("."+cssChild).hide();
		//on affiche le lien expand (+)
	    $(this).siblings(".showDetail").show();
	   
	    //on cache le lien collapse (-)(celui clické)
	    $(this).hide();
	      
	    //si plus aucun bouton - est visible, on manage en conséquence le bouton + et - relatif à tous les droits
	    if(!$("#droits a.hideDetail").is(":visible")){
	    	$(".showAllDetails").show();
	    	$(".hideAllDetails").hide();
	    }
	    
	});
	
	
	
	$('#dialog-dossiersLies').hide();
	$('#idLinkDossiersLies').click(function(e) {
		
		var idDossier = $('[name=selectedId]').val();
		//on charge les liens du dossier existants
		ajaxLoadDossiersLies(idDossier);
		
		//---------------------------------------------------------------------
		// Initialisation du dialogue de sélection des templates
		// ---------------------------------------------------------------------
		$('#dialog-dossiersLies').dialog(
				{
					autoOpen:false,
					buttons:[	{
									text:"Valider",
									click: function() {
										ajaxSaveDossiersLies(idDossier);
										
									}
								}
							],
					modal:true,
					closeOnEscape:true,
					draggable : false,
					resizable : false,
					width : 1000,
					height:500
					
				}
		);
		
		
		$('#dialog-dossiersLies').dialog('open');
	});
	//détection de la fermeture de la box dossiers liés
	$('#dialog-dossiersLies').bind('dialogclose',function(event){
		initHtmlPopupDossiersLies();
			
	});
	
	//détection de la fermeture de la box dossiers liés
	$('#dialog-copieDroits').bind('dialogclose',function(event){
		
		var that = $(this);
		$('.ui-dialog').each(function(){
			//si le dialogue en cours est la popup dont on a demandé la fermeture
			if($(this).is(that)){
				//do nothing
			}else{
				$(this).dialog('destroy').remove();	
			}
		});
		
			
	});
	
	$('#dialog-copieDroits').hide();
	
	$('.copieLink').click(function(e) {
		var idDroit = $(this).data("iddroit");
		//injecte le titre du droit référence
		$('#droitRef th').html($(this).data("popup-title"));
		//on charge les liens du dossier existants
		ajaxLoadCopieDroits(idDroit);
		
		//---------------------------------------------------------------------
		// Initialisation du dialogue de sélection des templates
		// ---------------------------------------------------------------------
		$('#dialog-copieDroits').dialog(
				{
					autoOpen:false,
					buttons:[	{
									text:"Valider",
									click: function() {
										
										$("#dialog-copieDroits input[name='aCopier']:checked").each(function(){
											
										
											var idDroit = $(this).closest("tr").find(".idDroitValue").val() ;
											
											var newDateDebut = $("#debutDroitCopieId").val();
											if(newDateDebut.length==0){
												newDateDebut="0";
											}
											var newDateFin = $("#finDroitCopieId").val();
											if(newDateFin.length==0){
												newDateFin="0";
											}
											
											var dateAttestationDroit = "0";
											if($(this).closest("tr").find(".attestationDate").length>0 && $(this).closest("tr").find(".attestationDate").val().length>0){
												dateAttestationDroit = $(this).closest("tr").find(".attestationDate").val();
											}
											
											ajaxCopieDroit(idDroit,dateAttestationDroit,newDateDebut,newDateFin);
										
										});
									}
								}
							],
					modal:true,
					closeOnEscape:true,
					draggable : true,
					resizable : true,
					width : 1000,
					height:800
					
				}
		);
			
		$('#dialog-copieDroits').dialog('open');
		
	});
			
});

//function appelée dans le onclick de chaque ligne. Gérée comme ça car  selector.click = function(){} => appelée autant de fois que définie
//dans la boucle à la génération dynamique des lignes de dossiers liés
function clickOnDeleteLink(idLien){
		if(confirm('Voulez-vous vraiment supprimer le lien entre ces 2 dossiers ?')){	
			//si on delete un lien déjà existant
			if(idLien!='new'){
				var trSource = $("#"+idLien);
				ajaxDeleteLien(trSource,idLien);
				
				var trNewLines = [];
	
				//on charge tous les éléments nommés typeLien et on détecte ceux new, pour les mettre de côté	
				$('input[name="idLien"]').each(function(){			
					var currentIdLien = $(this).val();			
					if(currentIdLien=='new'){	
						trNewLines.push($(this).closest("tr"));
					}
				});
			}else{	
				//TODO
				//on charge tous les éléments nommés typeLien et on détecte ceux new, pour voir identifier celui effacé et les autres (qu'on 
				//mettra de côté
			}
			
			var delayDisplay = function() { 		
				ajaxLoadDossiersLies($('input[name="selectedId"]').attr('value')); 			
				//on remet ceux qui étaient en new, sinon ils vont disparaitre...a tout jamais ! :-0
				for(var i=0;i<trNewLines.length;i++){
					$('#dialog-dossiersLies tr:last-child').before(trNewLines[i]);  
				}
				
			};
			setTimeout(delayDisplay, 1000);
		}
}

/*
 * vides les liens existants et laisse entête et newLine 
 */
function initHtmlPopupDossiersLies(){
	//on met de côté l'entête et la ligne nouveau lien, puis on vide, car on recharge les liens à chaque ouverture
	var entete = $('#dialog-dossiersLies tr:first-child').detach();
	var newLienLine = $('#dialog-dossiersLies tr:last-child').detach();
	$('#dialog-dossiersLies table').empty();
	
	//on remet l'entête 
	$('#dialog-dossiersLies table').append(entete);
	//puis on remet enfin la ligne nouveau lien pour qu'elle soit en dernier	
	$('#dialog-dossiersLies table').append(newLienLine);
	$('#widget_newDossierFilsId').val("");
	$('#new_dossierFilsId').val("");
}

function renderRowForLienTable(){

	var trHtml = templateTrHtml;
	trHtml= trHtml.replace(new RegExp("\\${id_lien}","gm"),arguments[0].id_lien);
	trHtml= trHtml.replace(new RegExp("\\${dossier_id}","gm"),arguments[0].dossier_id);
	trHtml= trHtml.replace(new RegExp("\\${allocataire_nom}","gm"),arguments[0].allocataire_nom);	
	trHtml= trHtml.replace(new RegExp("\\${allocataire_prenom}","gm"),arguments[0].allocataire_prenom);	
	trHtml= trHtml.replace(new RegExp("\\${allocataire_nss}","gm"),arguments[0].allocataire_nss);
	trHtml= trHtml.replace(new RegExp("\\${affilie_numero}","gm"),arguments[0].affilie_numero);
	trHtml= trHtml.replace(new RegExp("\\${dossier_activite}","gm"),arguments[0].dossier_activite);
	trHtml= trHtml.replace(new RegExp("\\${dossier_statut}","gm"),arguments[0].dossier_statut);
	trHtml= trHtml.replace(new RegExp("\\${dossier_etat}","gm"),arguments[0].dossier_etat);
	if(arguments[0].droit_debut==''){
		trHtml= trHtml.replace(new RegExp("\\${droit_debut}","gm"),arguments[0].droit_debut+" ");
	}else{
		trHtml= trHtml.replace(new RegExp("\\${droit_debut}","gm"),arguments[0].droit_debut+" -");
	}
	trHtml= trHtml.replace(new RegExp("\\${dossier_radiation}","gm"),arguments[0].dossier_radiation);
	
	var strSelectTypeLien = '<select name="typeLien-'+arguments[0].id_lien+'" size="1">';
	
	for (var i = 0; i < arguments[0].csTypeLien.collection.length; i++) {
		
		if(arguments[0].csTypeLien.value==arguments[0].csTypeLien.collection[i].value){
			strSelectTypeLien +='<option value="'+arguments[0].csTypeLien.collection[i].value+'" selected>'+arguments[0].csTypeLien.collection[i].libelle+'</option>';
		}
		else
		{
			strSelectTypeLien +='<option value="'+arguments[0].csTypeLien.collection[i].value+'">'+arguments[0].csTypeLien.collection[i].libelle+'</option>';	
		}
	}

	strSelectTypeLien +='</select>';
	trHtml= trHtml.replace("${typeLien}",strSelectTypeLien);
	if(arguments[1]=='bottom'){
		$('#dialog-dossiersLies tr:last-child').before(trHtml);  
		if(arguments[2]=='add'){
			 $('#dialog-dossiersLies tr:last-child').prev().slideDown("slow");
		}
		
		  
	}
	
	
}
//Charge les liens existants du dossier père
function ajaxLoadDossiersLies(idDossier){
	initHtmlPopupDossiersLies();
	$.ajax({
		 type:'GET',
	     url:url+'?userAction=al.ajax.dossiersLies.lister&idDossier='+idDossier,
	     async: false,
	     beforeSend: function(x) {
	      if(x && x.overrideMimeType) {
	       x.overrideMimeType("application/j-son;charset=UTF-8");
	      }
	 },
	 dataType: "json",
	 success: function(data){
		 
		for(var i=0;i<data.search.results.length;i++){
			$.cookie("al.dossier.lien-"+data.search.results[i].id_lien, data.search.results[i].csTypeLien.value);		
			renderRowForLienTable(data.search.results[i],'bottom');
			
		}
		
	 }
	});
}

//Charge les liens existants du dossier père
function ajaxLoadCopieDroits(idDroit){
	
	$.ajax({
		 type:'GET',
	     url:url+'?userAction=al.ajax.copieDroits.lister&searchModel.forIdDroit='+idDroit,
	     async: false,
	     dataType: "html",
	     success: function(data){
	    	var dataNew = $('<div>').html( data ); 
	    	notationManager.addNotationOnFragment(dataNew);
	    	 $("#dialog-copieDroits #ajaxContent").html(dataNew);

	    	
	     }
	});
}
/**
 * Prend en compte les modifs de type lien des liens existants et un éventuel nouveau lien
 * (a voir si on valider ici les suppressions ou suppression en live (via function deleteLien(idLien))
 * @param idDossier
 */
function ajaxSaveDossiersLies(idDossier){

	var needReload=false;
	var idDossierSource = $('[name=selectedId]').val();
	
	var trUpdated = [];

	//on charge tous les éléments nommés typeLien-	
	$('input[name="idLien"]').each(function(){
		mlog('id lien:'+$(this).val(),'red','11');
		var currentIdLien = $(this).val();
		var currentIdDossier = $(this).closest('tr').children('.dossier_id').text();
		var currentTypeLien = $(this).closest('tr').children('.type_lien').children('select').val();
		var trSource = $(this).closest('tr');
	
		if(currentIdLien=='new'){
			mlog('-----ADD-----','red','11');
			ajaxAddLien(currentIdDossier,currentTypeLien);
			trUpdated.push(trSource);
		}
		else{
			//si changement de type lien
			var cookieValue = $.cookie("al.dossier.lien-"+currentIdLien);
			
			if(currentTypeLien!=cookieValue){	
				mlog('lien will be updated trsource '+trSource.attr('id'),'red','11');
				ajaxUpdateTypeLien(currentIdLien,currentTypeLien);
				trUpdated.push(trSource);		
			}
		}
		
	});
	
	for(var i=0;i<trUpdated.length;i++){
		trUpdated[i].css({"background":"#00B536"});
	}
	var delayDisplay = function(){
		ajaxLoadDossiersLies(idDossier); 
	};
	setTimeout(delayDisplay, 1000);
}

function traitementErreur(data){
	ajaxUtils.displayError(data);
}

function ajaxCopieDroit(idDroit,dateAttestationDroit,newDateDebut,newDateFin){
	
	var o_options= {
			serviceClassName: 'ch.globaz.al.business.services.models.droit.DroitBusinessService',
			serviceMethodName:'copierDroitAndUpdateAttestationDate',
			parametres:idDroit+","+dateAttestationDroit+","+newDateDebut+","+newDateFin,
			callBack: function(){
				location.reload();
				
			},
			errorCallBack: traitementErreur
				
	};
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();
	

}


function ajaxDeleteLien(trSource,idLien){
	

	if(idLien=='new'){
		//trSource.remove();	
	}else{
		var o_options= {
				serviceClassName: 'ch.globaz.al.business.services.models.dossier.DossierBusinessService',
				serviceMethodName:'retirerLien',
				parametres:idLien,
				callBack: function(){
					//trSource.css({"background":"#00B536"});
				},
				errorCallBack: traitementErreur
			};
		globazNotation.readwidget.options=o_options;		
		globazNotation.readwidget.read();	
	}
	
}

function ajaxAddLien(idDossierFils,typeLien){
	var o_options= {
			serviceClassName: 'ch.globaz.al.business.services.models.dossier.DossierBusinessService',
			serviceMethodName:'ajouterLien',
			parametres:$('[name=selectedId]').val()+","+idDossierFils+","+typeLien,
			callBack: function(){
				
			},
			errorCallBack: traitementErreur
		};
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();

	
}

function ajaxUpdateTypeLien(idLien,newTypeLien){
	
	var o_options= {
			serviceClassName: 'ch.globaz.al.business.services.models.dossier.DossierBusinessService',
			serviceMethodName:'changerTypeLien',
			parametres:idLien+","+newTypeLien,
			callBack: function(){
				//trSource.css({"background":"#00B536"});
				//mlog('callback upd trsource elm:'+trSource.attr('id'));
				//trSource.children('.dossier_id').children('input[name="statusAjax"]').attr('value','1');
			},
			errorCallBack: traitementErreur
				
	};
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();
	

}
/**
 * 
 * Récupère la date de début de validité et appelle le service calculant le nombre de jours de début
 * puis callback pour remplir le champ avec la valeur obtenue 
 * 
 */
function ajaxDateDebutValidite() {
	var a_params = new Array(
			"dateDebutValidite:"+$("#debutValidite").val()
	);
	
	for(i=0;i<a_params.length;i++) {
		a_params[i] = a_params[i].replace(/\'/g,"");
	}		
	

	var o_options= {
		serviceClassName: 'ch.globaz.al.business.services.models.dossier.DossierBusinessService',
		serviceMethodName:'readWidget_nbreJourDebutMoisAF',
		parametres:'['+a_params+']',
		callBack: callback_debutValidite
	}

	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}

/**
 * 
 * Récupère la date de fin de validité et appelle le service calculant le nombre de jours de fin
 * puis callback pour remplir le champ avec la valeur obtenue 
 * 
 */
function ajaxFinValidite() {
	var a_params = new Array(
			"dateFinValidite:"+$("#finValidite").val()
	);
	
	for(i=0;i<a_params.length;i++) {
		a_params[i] = a_params[i].replace(/\'/g,"");
	}		
	

	var o_options= {
		serviceClassName: 'ch.globaz.al.business.services.models.dossier.DossierBusinessService',
		serviceMethodName:'readWidget_nbreJourFinMoisAF',
		parametres:'['+a_params+']',
		callBack: callback_finValidite
	}

	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}


/**
 * Rempli le champ dépendant de la date de début de validité
 * 
 * @param data
 */
function callback_debutValidite(data){
	if(data=="-1")
		$("#debutValidite").css("color", "red");
	else
		$("#nbJoursDebut").val(data);

}

/**
 * Rempli le champ dépendant de la date de début de validité
 * 
 * @param data
 */
function callback_finValidite(data){
	if(data=="-1")
		$("#finValidite").css("color", "red");
	else
		$("#nbJoursFin").val(data);
	
}







	
