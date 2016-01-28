$(function() {
	var key_shift_pressed=false;
	
	function setMasterKey(b_actived,event){
			//Touche shift
			if( 16 === event.which){
				key_shift_pressed = b_actived;
				if (b_actived) {
					$("#ee").html('true');
				} else {
					$("#ee").html('false');
				}
			}
		}
		
		$("html").keyup(function(e){	
			setMasterKey(false,e);			
		});
		
		$("html").keydown(function(e){
			setMasterKey(true,e);
			if (key_shift_pressed){
				if(e.which === 49){
					if ($("#paramLink1").length>0) {
						window.location.href=$("#paramLink1").attr('href');
					}
				} else if(e.which === 50){	
					if ($("#paramLink2").length>0) {
						window.location.href=$("#paramLink2").attr('href');
					}
				} else if(e.which === 51){
					if ($("#paramLink3").length>0) {
						window.location.href=$("#paramLink3").attr('href');
					}
				} 
			}
		});
});

var url = null;

if (typeof MAIN_URL == 'undefined') {
		url = $('[name=formAction]').attr('content');
}else{
		url = MAIN_URL;
}
			
var response = null;
var req1 = null;
var hostScreen = null;
// nom du champ qui contiendra la valeur de la propri�t� id de la r�ponse ajax
// ce sera toujours l'id tiers pour les requ�tes sur les personnes
var targetIdName = "";
var searchAjaxInputId = "searchNssCriteria";
var autoSuggestContainerId = "autoSuggestContainer";
var prefixModel = "";
var statutSynchroId = "statutSynchroTiers";
// pour savoir si on vient de la s�lection de tiers via la page de recherche
// [...]
var isFromSelectionTiers = "";
var searchAjax = false;

// Les querys de base faites lors de la saisie du nss sur le composant
// initialis�s par initNssInputComponent
var nssAjaxQueryScreens = new Array();
nssAjaxQueryScreens["AM0002"] = 'prefixModel=' + prefixModel + '&userAction=amal.ajax.tiersAjax.lister&personneEtendueSearchComplexModel.forNumeroAvsActuel=756.';
nssAjaxQueryScreens["AM0003"] = 'prefixModel=' + prefixModel + '&userAction=amal.ajax.tiersAjax.lister&personneEtendueSearchComplexModel.forNumeroAvsActuel=756.';

function initNssInputComponent(screen, nameComponent) {

	hostScreen = screen;
	// si param forNumAvsActuel d�fini Recherche Ajax au chargement de la page ?
	var params = extractUrlParams();

	if (document.getElementById('partial' + nameComponent)) {
		//lancement de la recherche si crit�re d�fini, �crase le numAvsActuel
		if (params["forNumAvsActuel"] != undefined) {
			document.getElementById('partial' + nameComponent).value = params["forNumAvsActuel"];
		}

		var nssTiersSelected = document.forms[0]
				.elements(prefixModel + '.personneEtendue.personneEtendue.numAvsActuel').value;
		// On remplit le champ NSS du composant NSSFormat avec la v�ritable
		// valeur du NSS du tiers en cours d'utilisation
		// car c'est cette valeur qui sera utilis� si synchro � faire 
		if (nssTiersSelected.length > 12
				&& nssTiersSelected.substr(0, 4) == '756.')
			document.getElementById('partial' + nameComponent).value = nssTiersSelected
					.substr(4);
		else if (nssTiersSelected != '')
			document.getElementById('partial' + nameComponent).value = nssTiersSelected;

		// -------------- NSS MANAGEMENT EVENTS---------------
		// hack du composant nssPopup car pas tout compatible avec new fw
		// persistance
		// on garde que les actions qui g�re le format du nss, l'autosuggestion
		// et recherche => manufactur� ^^
		document.forms[0].elements('partial' + nameComponent).onblur = function() {
			if (this.value.length == 12)
				synchroTiersAjax();
		};
		document.forms[0].elements('partial' + nameComponent).onkeydown = function() {
		};
		document.forms[0].elements('partial' + nameComponent).onchange = function() {
		};
		document.forms[0].elements('partial' + nameComponent).onkeypress = function() {
		};
		document.forms[0].elements('partial' + nameComponent).onkeyup = function() {

			if (event.keyCode == 13 && this.value.length == 12) {
					synchroTiersAjax();
			} else if (event.keyCode == 40) {
				document.getElementById('suggestChoice').focus();
			} else {
				if (this.value.length == 12) {
					document.getElementById('autoSuggestContainer').style.display = 'none';
				}
				nssOnKeyUp(nameComponent);
				// pour formater a chaque passage

				nssAction(nameComponent);

				if (this.value.length > 3
						&& ((event.keyCode > 95 && event.keyCode < 106) || (event.keyCode > 47 && event.keyCode < 58) || (event.keyCode == 8))) {

					var ajaxQuery = new AjaxQuery(url+'?'
							+ nssAjaxQueryScreens[screen] + this.value);

					ajaxQuery.noResultCallback = suggestSelectList;
					ajaxQuery.oneResultCallback = suggestSelectList;
					ajaxQuery.listResultsCallback = suggestSelectList;

					ajaxQuery.noResultCallbackParams = new Array(this, document
							.getElementById('autoSuggestContainer'));
					ajaxQuery.oneResultCallbackParams = new Array(this,
							document.getElementById('autoSuggestContainer'));
					ajaxQuery.listResultsCallbackParams = new Array(this,
							document.getElementById('autoSuggestContainer'));

					ajaxQuery.launch();
				}
			}
		};
	}

}

//Fonction initialisant un composant input n�affilie sur lequel on veut une suggestList ajax
function initNumAffInputComponent(component, typeActiviteComponent, dateComponent) {

	if (component) {
		component.onblur = function() {
		};
		component.onkeydown = function() {
		};
		component.onchange = function() {
		};
		component.onkeypress = function() {
		};
		component.onkeyup = function() {

			if (this.value.length == 8) {
				document.getElementById('autoSuggestNumAffContainer').style.display = 'none';
			}
		};

	}
}

//execute une requ�te ajax
// - query : url de la requ�te
var AjaxQuery = function(query) {

	this.url = query;
	this.noResultCallback = null;
	this.oneResultCallback = null;
	this.listResultsCallback = null;

	this.noResultCallbackParams = null;
	this.oneResultCallbackParams = null;
	this.listResultsCallbackParams = null;

	// this.callback = handlerStateFunction;
	this.launch = function() {
		refObj = this;
		if (window.XMLHttpRequest)
			req1 = new XMLHttpRequest();
		else if (window.ActiveXObject)
			req1 = new ActiveXObject("Microsoft.XMLHTTP");
		else
			return; // fall on our sword
		req1.open('GET', this.url, false);
		req1.onreadystatechange = function() {

			if (req1.readyState != 4) {
				return;
			}
			if ((req1.status == 0) || (req1.status == 200)) {

				response = eval("(" + req1.responseText + ")");

				if (response.search.results.length == 0) {
					mlog('noResultCallback with : @' + refObj.url,
							'red');
					refObj.noResultCallback.apply(this,
							refObj.noResultCallbackParams);
					
				}
				else if (response.search.results.length == 1) {
					mlog('oneResultCallback with : @' + refObj.url,
							'red');
					refObj.oneResultCallback.apply(this,
							refObj.oneResultCallbackParams);
				}
				else if (response.search.results.length > 1) {
					mlog('listResultsCallback with : @' + refObj.url,
							'red');
					refObj.listResultsCallback.apply(this,
							refObj.listResultsCallbackParams);
				}
				mlog('------------------------------------------');

			}

		}
		req1.send(null);
	};

};

// cette m�thode prend les noms de toutes les propri�t�s de la r�ponse ajax
// et remplir les champs de la page html qui ont le m�me noms avec la valeur de
// la propri�t�
// pour la propri�t� id de la r�ponse, on met sa valeur dans le champ d�fini par
// targetIdName
function fillInputWithAjaxResponse(selectedId, enableCallback) {	
	if (selectedId == undefined)
		selectedId = 0;
	if (enableCallback == undefined)
		enableCallback = false;
	if (response.search.results.length == 1) {
		var result = response.search.results[selectedId];

		for ( var j = 0; j < result.properties.length; j++) {

			var currentPropertyName = result.properties[j].name;

			var currentElmt = document.getElementsByName(currentPropertyName)[0];
			if (currentElmt) {

				if (currentElmt.nodeName == 'SPAN'
						|| currentElmt.nodeName == 'DIV')
					currentElmt.innerHTML = result.properties[j].value;
				else
					currentElmt.value = result.properties[j].value;
												
				// on regarde si il y a un champ nomm� seulement comme la
				// derni�re partie
				// surtout utile pour les dates (cf dateNaissance)
				var endSuffix = currentPropertyName.substring(
						currentPropertyName.lastIndexOf(".") + 1,
						currentPropertyName.length);
				if (document.getElementsByName(endSuffix)[0] != undefined)
					document.getElementsByName(endSuffix)[0].value = result.properties[j].value;
				
				currentElmt.disabled=true;
				currentElmt.title="Cette valeur peut �tre modifi� uniquement depuis Pyxis.";
			}
			//si on pas trouv� la propri�t� ayant le nom correspondant, on cherche par l'id (pour les <span> par ex)
			else if (document.getElementById(currentPropertyName)) {				
				currentElmt = document.getElementById(currentPropertyName);
				// dans le cas d'un span ou div on mettre le contenu de la
				// propri�t� en innerHTML
				if (currentElmt.nodeName == 'SPAN'
						|| currentElmt.nodeName == 'DIV')
					currentElmt.innerHTML = result.properties[j].value;
				else
					currentElmt.value = result.properties[j].value;
				
				currentElmt.disabled=true;
				currentElmt.title="Cette valeur peut �tre modifi� uniquement depuis Pyxis.";
			}
			else
				mlog('---IMPOSSIBLE DE TROUVER LE CHAMP :'+currentPropertyName+'---');
			
		}
	}
	try {
		if (enableCallback)			
			callbackFillInputAjax();
	} catch (e) {
//		mlog('pas de callback ajax dans cette page mais no prob mec',
//				'pink');
	}

}

function checkIfPersonneExists() {	
	fillInputWithAjaxResponse(0, true);
}

//Afficher une liste de suggestions selon le nss en cours de saisie
function suggestSelectList(finalContainer, suggestContainer,
		finalContainerMaxChars) {
	if (finalContainerMaxChars == undefined)
		finalContainerMaxChars = 12;
	var strHtml = "";

	suggestContainer.style.display = "none";

	for ( var i = 0; i < response.search.results.length; i++) {
		if (i == 0) {
			suggestContainer.style.display = "block";
			strHtml += '<select name="suggestChoice" id="suggestChoice" size="4" style="position:absolute;top:0px;left:0px;">';
		}

		for ( var j = 0; j < response.search.results[i].properties.length; j++) {

			if (response.search.results[i].properties[j].name == "suggestOptionSelect") {
				strHtml += response.search.results[i].properties[j].value;
			}
		}

		if (i == response.search.results.length - 1)
			strHtml += '</select>';
	}

	//on affiche le select que si des r�sultats sont trouv�s...
	if (response.search.results.length > 0) {
		suggestContainer.innerHTML = strHtml;

		suggestContainer.getElementsByTagName('select')[0].onclick = function() {
			if (this.value.length > 12 && this.value.substr(0, 4) == '756.')
				finalContainer.value = this.value.substr(4);
			else
				finalContainer.value = this.value;
			//this.style.display = "none";
			suggestContainer.style.display="none";
			// appel du onblur si d�fini
			try {
				finalContainer.onblur();
			} catch (e) {
			}

		};
		
		suggestContainer.getElementsByTagName('select')[0].onkeydown = function() {
			 if (event.keyCode == '13' || event.keyCode == '9') {
				 suggestContainer.getElementsByTagName('select')[0].click();
				 suggestContainer.getElementById('partialforNumAvs').focus();
			 } //else {
				 //alert(event.keyCode);
			 //}
		};
	}
	//si on a tout remplit, on le cache
	if (finalContainer.value.length == finalContainerMaxChars) {
		suggestContainer.style.display = "none";
	}
}

//r�cup�re toutes les informations du tiers et les traite en cons�quence ( remplit le formulaire)
function synchroTiersAjax() {
	
	//on virer le statut synchro car on recommence une nouvelle
	if (document.getElementById(statutSynchroId))
		document.getElementById(statutSynchroId).innerHTML = "";
	
	var partialNssValue = document.getElementById('partialforNumAvs').value;
	// On met toujours la m�me valeur dans le champ r�ellement utilis� pour le
	// mod�le et le champ nss visible
	if (partialNssValue != '')
		document
				.getElementsByName(prefixModel + '.personneEtendue.personneEtendue.numAvsActuel')[0].value = '756.' + partialNssValue;

	var idTiers = document
			.getElementById(prefixModel + '.personneEtendue.tiers.idTiers').value;
	
	try {

		document
				.getElementsByName(prefixModel + '.personneEtendue.tiers.idTiers')[0].value = '';
		document
				.getElementsByName(prefixModel + '.personneEtendue.personne.idTiers')[0].value = '';
		document
				.getElementsByName(prefixModel + '.personneEtendue.personneEtendue.idTiers')[0].value = '';
		document
				.getElementsByName(prefixModel + '.personneEtendue.tiers.spy')[0].value = '';
		document
				.getElementsByName(prefixModel + '.personneEtendue.personne.spy')[0].value = '';
		document
				.getElementsByName(prefixModel + '.personneEtendue.personneEtendue.spy')[0].value = '';
		
	} catch (e) {
		//TODO: ajouter une console de notification js, qui sera cach�e et visible que par nous
		// pour d�tecter les erreurs
		// alert('Probl�me lors de la r�initialisation des champs
		// tiers:'+e.description);
	}
	var searchTiersParams = '';
	if (partialNssValue == '' && idTiers != '')
		searchTiersParams = 'personneEtendueSearchComplexModel.forIdTiers=' + idTiers;
	if (partialNssValue != '' && idTiers == '')
		searchTiersParams = 'personneEtendueSearchComplexModel.forNumeroAvsActuel=756.' + partialNssValue;
	// priorit� � la recherche par nss, car affich� � l'�cran et pas idTiers
	if (partialNssValue != '' && idTiers != '')
		searchTiersParams = 'personneEtendueSearchComplexModel.forNumeroAvsActuel=756.' + partialNssValue;

	var ajaxQuery = new AjaxQuery(url+'?prefixModel=' + prefixModel
			+ '&userAction=amal.ajax.tiersAjax.lister&' + searchTiersParams);

	ajaxQuery.noResultCallback = fillInputWithAjaxResponse;
	ajaxQuery.noResultCallbackParams = new Array(0, true);
	ajaxQuery.oneResultCallback = checkIfPersonneExists;
	ajaxQuery.listResultsCallback = function() {
		alert('plusieurs personnes correspondent � ce NSS, veuillez s�lectionner le tiers voulu via [...]');
	};

	ajaxQuery.oneResultCallbackParams = new Array(0, true);
	ajaxQuery.listResultsCallbackParams = new Array();

	if (searchTiersParams != '')
		ajaxQuery.launch();

}


function accordStatutLogoWithResponse(forceToValid) {
	if (forceToValid == undefined)
		forceToValid = false;
	var strHtmlError1 = '<img src="images/edit-delete.png" alt="Synchronisation �chou�e" width="16" height="16"/>';
	var strHtmlError2 = '<img src="images/edit-delete.png" alt="Synchronisation �chou�e - aucun tiers ne correspond aux crit�res" width="16" height="16"/>';
	var strHtmlSuccess = '<img src="images/dialog-ok-apply.png" alt="Synchronisation r�ussie" width="16" height="16"/>';

	// si on a pas les �l�ments � mettre � jour, on quitte la fonction

	if (document.getElementById(statutSynchroId) == null
			|| document.getElementById("idTiers") == null) {
		return;
	}

	document.getElementById(statutSynchroId).innerHTML = "";

	if (forceToValid) {
		//(<a href="<%=servletContext + "/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId="+viewBean.getFamilleContribuable().getPersonneEtendue().getId()%>"><%=viewBean.getFamilleContribuable().getPersonneEtendue().getId()%></a>)
		var idTiers = $("#"+prefixModel+"\\.personneEtendue\\.personne\\.idTiers").val();
		var linkTiers = $('<a>', {
			href:  'pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId='+idTiers,
			text:'('+idTiers+')'
		});
		$("#idTiers").html(linkTiers);
		
		document.getElementById(statutSynchroId).innerHTML = strHtmlSuccess;
	} else {
		if (document.getElementById("idTiers").value != '') {
			document.getElementById(statutSynchroId).innerHTML = strHtmlError1;
		}
		$("#idTiers").html("");
		$('#zoneTiers input, #zoneTiers select').not("#forNumAvsNssPrefixe").each(function() {
			$(this).prop('disabled', false);		
		});
//		$("#forNumAvsNssPrefixe").prop('disabled',true);
	}
}

function getContextUrl() {
	if (this._s_context === null) {
		this._s_context = $('[name=Context_URL]').attr('content');
	}
	return this._s_context;
}