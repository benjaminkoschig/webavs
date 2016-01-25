var url = null;

if (typeof MAIN_URL == 'undefined') {
		url = $('[name=formAction]').attr('content');
}else{
		url = MAIN_URL;
}
			
var response = null;
var req1 = null;
var hostScreen = null;
// nom du champ qui contiendra la valeur de la propriété id de la réponse ajax
// ce sera toujours l'id tiers pour les requêtes sur les personnes
var targetIdName = "";
var searchAjaxInputId = "searchNssCriteria";
var autoSuggestContainerId = "autoSuggestContainer";
var prefixModel = "";
var statutSynchroId = "statutSynchroTiers";
// pour savoir si on vient de la sélection de tiers via la page de recherche
// [...]
var isFromSelectionTiers = "";
var searchAjax = false;

// Les querys de base faites lors de la saisie du nss sur le composant
// initialisés par initNssInputComponent
var nssAjaxQueryScreens = new Array();
nssAjaxQueryScreens["AL0004"] = 'prefixModel=' + prefixModel + '&userAction=al.ajax.allocataire.lister&allocataireComplexSearchModel.likeNssAllocataire=756.';
nssAjaxQueryScreens["AL0003"] = 'prefixModel=' + prefixModel + '&userAction=al.ajax.tiersAjax.lister&personneEtendueSearchComplexModel.forNumeroAvsActuel=756.';
nssAjaxQueryScreens["AL0006"] = 'prefixModel=' + prefixModel + '&userAction=al.ajax.tiersAjax.lister&personneEtendueSearchComplexModel.forNumeroAvsActuel=756.';

function initNssInputComponent(screen, nameComponent) {

	hostScreen = screen;
	// si param forNumAvsActuel défini Recherche Ajax au chargement de la page ?
	var params = extractUrlParams();

	if (document.getElementById('partial' + nameComponent)) {
		//lancement de la recherche si critère défini, écrase le numAvsActuel
		if (params["forNumAvsActuel"] != undefined) {
			document.getElementById('partial' + nameComponent).value = params["forNumAvsActuel"];
		}

		var nssTiersSelected = document.forms[0]
				.elements(prefixModel + '.personneEtendueComplexModel.personneEtendue.numAvsActuel').value;
		// On remplit le champ NSS du composant NSSFormat avec la véritable
		// valeur du NSS du tiers en cours d'utilisation
		// car c'est cette valeur qui sera utilisé si synchro à faire ou pour
		// afficher en fonction du tiers synchronisé (lors de réaffichage pour
		// prob avec création alloc)
		if (nssTiersSelected.length > 12
				&& nssTiersSelected.substr(0, 4) == '756.')
			document.getElementById('partial' + nameComponent).value = nssTiersSelected
					.substr(4);
		else if (nssTiersSelected != '')
			document.getElementById('partial' + nameComponent).value = nssTiersSelected;

		// -------------- NSS MANAGEMENT EVENTS---------------
		// hack du composant nssPopup car pas tout compatible avec new fw
		// persistance
		// on garde que les actions qui gère le format du nss, l'autosuggestion
		// et recherche => manufacturé ^^
		document.forms[0].elements('partial' + nameComponent).onblur = function() {
			if (this.value.length == 12 && screen != 'AL0004')
				synchroTiersAjax();
			if (this.value.length == 12 && screen == 'AL0004')
				synchroAllocAjax();
		};
		document.forms[0].elements('partial' + nameComponent).onkeydown = function() {
		};
		document.forms[0].elements('partial' + nameComponent).onchange = function() {
		};
		// commenter cette ligne pour gérer aussi le n°avs
		document.forms[0].elements('partial' + nameComponent).onkeypress = function() {
		};
		document.forms[0].elements('partial' + nameComponent).onkeyup = function() {

			if (event.keyCode == 13 && this.value.length == 12) {
				if (screen != 'AL0004')
					synchroTiersAjax();
				if (screen == 'AL0004')
					synchroAllocAjax();
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

//Fonction initialisant un composant input n°affilie sur lequel on veut une suggestList ajax
// TODO: tenir compte du format du n° affilié selon la caisse, hardcodé pour le
// moment 000.0000
function initNumAffInputComponent(component, typeActiviteComponent, dateComponent) {

	if (component) {
		component.onblur = function() {
			if (this.value.length == 8) {
				mlog('before synchroAffilieAjax', 'red');
				synchroAffilieAjax(this.value,typeActiviteComponent.value,dateComponent.value);
			}

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

			if ( (event.keyCode > 95 && event.keyCode < 106) || (event.keyCode > 47 && event.keyCode < 58)
					|| (event.keyCode == 8)) {

				var ajaxQuery = new AjaxQuery(
						url+'?prefixModel=affiliation&userAction=al.ajax.affiliation.lister&likeNumeroAffilie='
								+ this.value
								+ '&typeActivite='
								+ typeActiviteComponent.value
								+'&dateCalcul='
								+ dateComponent.value);

				ajaxQuery.noResultCallback = suggestSelectList;
				ajaxQuery.oneResultCallback = suggestSelectList;
				ajaxQuery.listResultsCallback = suggestSelectList;

				ajaxQuery.noResultCallbackParams = new Array(this, document
						.getElementById('autoSuggestNumAffContainer'), 8);
				ajaxQuery.oneResultCallbackParams = new Array(this, document
						.getElementById('autoSuggestNumAffContainer'), 8);
				ajaxQuery.listResultsCallbackParams = new Array(this, document
						.getElementById('autoSuggestNumAffContainer'), 8);

				ajaxQuery.launch();
			}
		};

	}
}

//Fonction initialisant un composant input de base sur lequel on veut une suggestList ajax
// Lorsque le champ est considéré comme complet => nb chars = maxChars
// La même query est exécuté en remplacant le paramètre likeXXX par forXXX
// Ne peut être appelé qu'une fois dans une page (id du div contenant le select
// de suggestion...)
function initCodeCaisseInputComponent(nameComponent) {

	if (document.getElementById(nameComponent)) {
		document.getElementById(nameComponent).onblur = function() {
			//pas besoin de synchro / remplir le form, car que le code dispo et nécessaire
			if (this.value.length == 3) {
				//synchroCaisseAjax => fillForm
			}

		};
		document.getElementById(nameComponent).onkeydown = function() {
		};
		document.getElementById(nameComponent).onchange = function() {
		};
		document.getElementById(nameComponent).onkeypress = function() {
		};
		document.getElementById(nameComponent).onkeyup = function() {

			if (this.value.length == 3) {
				document.getElementById('autoSuggestCodeCaisseContainer').style.display = 'none';
			}

			if (event.keyCode != 9) {
				var ajaxQuery = new AjaxQuery(
						url+'?userAction=al.ajax.administration.lister&prefixModel=dossierComplexModel.caisseAFComplexModel&searchModel.forCodeAdministrationLike=' + this.value);

				ajaxQuery.noResultCallback = suggestSelectList;
				ajaxQuery.oneResultCallback = suggestSelectList;
				ajaxQuery.listResultsCallback = suggestSelectList;

				ajaxQuery.noResultCallbackParams = new Array(this, document
						.getElementById('autoSuggestCodeCaisseContainer'), 3);
				ajaxQuery.oneResultCallbackParams = new Array(this, document
						.getElementById('autoSuggestCodeCaisseContainer'), 3);
				ajaxQuery.listResultsCallbackParams = new Array(this, document
						.getElementById('autoSuggestCodeCaisseContainer'), 3);

				ajaxQuery.launch();
			}

		};

	}

}

//execute une requête ajax
// - query : url de la requête
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

// cette méthode prend les noms de toutes les propriétés de la réponse ajax
// et remplir les champs de la page html qui ont le même noms avec la valeur de
// la propriété
// pour la propriété id de la réponse, on met sa valeur dans le champ défini par
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

//			if (currentPropertyName == 'id'
//					&& document.getElementsByName(targetIdName)[0] != undefined) {
//				document.getElementsByName(targetIdName)[0].value = result.properties[j].value;
//			}

			var currentElmt = document.getElementsByName(currentPropertyName)[0];
			if (currentElmt) {
				mlog('[FORMHTML] '+currentPropertyName+' with :'+result.properties[j].value);
				if (currentElmt.nodeName == 'SPAN'
						|| currentElmt.nodeName == 'DIV')
					currentElmt.innerHTML = result.properties[j].value;
				else
					currentElmt.value = result.properties[j].value;

				// on regarde si il y a un champ nommé seulement comme la
				// dernière partie
				// surtout utile pour les dates (cf dateNaissance)
				var endSuffix = currentPropertyName.substring(
						currentPropertyName.lastIndexOf(".") + 1,
						currentPropertyName.length);
				if (document.getElementsByName(endSuffix)[0] != undefined)
					document.getElementsByName(endSuffix)[0].value = result.properties[j].value;

			}
			//si on pas trouvé la propriété ayant le nom correspondant, on cherche par l'id (pour les <span> par ex)
			else if (document.getElementById(currentPropertyName)) {
				mlog('[FORMHTML] '+currentPropertyName+' with :'+result.properties[j].value);
				currentElmt = document.getElementById(currentPropertyName);
				// dans le cas d'un span ou div on mettre le contenu de la
				// propriété en innerHTML
				if (currentElmt.nodeName == 'SPAN'
						|| currentElmt.nodeName == 'DIV')
					currentElmt.innerHTML = result.properties[j].value;
				else
					currentElmt.value = result.properties[j].value;
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

//contrôle si lallocataire/enfant existe et si oui, le lie à nouveau dossier/droit
// sur l'écran al0004 (on peut changer l'alloc quand dans le cas de création
// dossier)
function checkIfPersonneAFExists() {

	var responseTiers = response;
	var idTiersPropertyName = prefixModel + '.personneEtendueComplexModel.tiers.idTiers';
	var searchParams = '';
	var userAction = '';

	for ( var i = 0; i < responseTiers.search.results[0].properties.length; i++) {

		if (idTiersPropertyName == responseTiers.search.results[0].properties[i].name) {
			if (hostScreen && hostScreen == 'AL0003') {

				userAction = '&userAction=al.ajax.enfant.lister';
				searchParams = '&enfantComplexSearchModel.forIdTiers=' + responseTiers.search.results[0].properties[i].value;
			}
			if (hostScreen && hostScreen == 'AL0006') {
				userAction = '&userAction=al.ajax.allocataire.lister';
				searchParams = '&allocataireComplexSearchModel.forIdTiers=' + responseTiers.search.results[0].properties[i].value;
			}
		}
	}
	mlog('before ajaxQuery in checkifPersonneAFExists');
	var ajaxQuery = new AjaxQuery(url+'?prefixModel=' + prefixModel
			+ userAction + searchParams);

	ajaxQuery.noResultCallback = function() {
		response = responseTiers;
		// on remplit le formulaire avec les données tiers trouvés
		fillInputWithAjaxResponse(0, true);
	};
	// si 1 alloc on remplit le formulaire avec l'alloc trouvé (données tiers +
	// alloc)
	ajaxQuery.oneResultCallback = fillInputWithAjaxResponse;
	ajaxQuery.listResultsCallback = function() {
		alert('plusieurs personnes correspondent à ce NSS, veuillez sélectionner le tiers voulu via [...]')
	};

	ajaxQuery.noResultCallbackParams = new Array();
	ajaxQuery.oneResultCallbackParams = new Array(0, true);
	ajaxQuery.listResultsCallbackParams = new Array();

	if (searchParams != '')
		ajaxQuery.launch();

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
			strHtml += '<select name="suggestChoice" size="4" style="position:absolute;top:0px;left:0px;">';
		}

		for ( var j = 0; j < response.search.results[i].properties.length; j++) {

			if (response.search.results[i].properties[j].name == "suggestOptionSelect") {
				strHtml += response.search.results[i].properties[j].value;
			}
		}

		if (i == response.search.results.length - 1)
			strHtml += '</select>';
	}

	//on affiche le select que si des résultats sont trouvés...
	if (response.search.results.length > 0) {
		suggestContainer.innerHTML = strHtml;

		suggestContainer.getElementsByTagName('select')[0].onclick = function() {
			if (this.value.length > 12 && this.value.substr(0, 4) == '756.')
				finalContainer.value = this.value.substr(4);
			else
				finalContainer.value = this.value;
			//this.style.display = "none";
			suggestContainer.style.display="none";
			// appel du onblur si défini
			try {
				finalContainer.onblur();
			} catch (e) {
			}

		};
	}
	//si on a tout remplit, on le cache
	if (finalContainer.value.length == finalContainerMaxChars) {
		suggestContainer.style.display = "none";
	}
}

//récupère toutes les informations du tiers et les traite en conséquence ( remplit le formulaire)
function synchroTiersAjax() {
	
	//on virer le statut synchro car on recommence une nouvelle
	if (document.getElementById(statutSynchroId))
		document.getElementById(statutSynchroId).innerHTML = "";

	var partialNssValue = document.getElementById('partialforNumAvs').value;
	// On met toujours la même valeur dans le champ réellement utilisé pour le
	// modèle et le champ nss visible
	if (partialNssValue != '')
		document
				.getElementsByName(prefixModel + '.personneEtendueComplexModel.personneEtendue.numAvsActuel')[0].value = '756.' + partialNssValue;

	var idTiers = document
			.getElementById(prefixModel + '.personneEtendueComplexModel.tiers.idTiers').value;
	try {

		document
				.getElementsByName(prefixModel + '.personneEtendueComplexModel.tiers.idTiers')[0].value = '';
		document
				.getElementsByName(prefixModel + '.personneEtendueComplexModel.personne.idTiers')[0].value = '';
		document
				.getElementsByName(prefixModel + '.personneEtendueComplexModel.personneEtendue.idTiers')[0].value = '';
		document
				.getElementsByName(prefixModel + '.personneEtendueComplexModel.tiers.spy')[0].value = '';
		document
				.getElementsByName(prefixModel + '.personneEtendueComplexModel.personne.spy')[0].value = '';
		document
				.getElementsByName(prefixModel + '.personneEtendueComplexModel.personneEtendue.spy')[0].value = '';
		
		
		if(document.getElementsByName('droitComplexModel.enfantComplexModel.enfantModel.idEnfant')[0])
			document.getElementsByName('droitComplexModel.enfantComplexModel.enfantModel.idEnfant')[0].value='';
		else
			mlog('[WARN] (synchroTiersAjax): cannot reset droitComplexModel.enfantComplexModel.enfantModel.idEnfant field','orange');
		if(document.getElementsByName('droitComplexModel.enfantComplexModel.enfantModel.spy')[0])
			document.getElementsByName('droitComplexModel.enfantComplexModel.enfantModel.spy')[0].value='';
		else
			mlog('[WARN] (synchroTiersAjax): cannot reset droitComplexModel.enfantComplexModel.enfantModel.spy field','orange');
		if(document.getElementsByName('droitComplexModel.enfantComplexModel.enfantModel.idTiersEnfant')[0])
			document.getElementsByName('droitComplexModel.enfantComplexModel.enfantModel.idTiersEnfant')[0].value='';
		else
			mlog('[WARN] (synchroTiersAjax): cannot reset droitComplexModel.enfantComplexModel.enfantModel.idTiersEnfant field','orange');
		
		if(document.getElementsByName('allocataireComplexModel.allocataireModel.idTiersAllocataire')[0])
			document.getElementsByName('allocataireComplexModel.allocataireModel.idTiersAllocataire')[0].value='';
		else
			mlog('[WARN] (synchroTiersAjax): cannot reset allocataireComplexModel.allocataireModel.idTiersAllocataire field','orange');
		
		document.getElementsByName(targetIdName)[0].value = '';
		document.getElementsByName('dateNaissance')[0].value = '';

	} catch (e) {
		//TODO: ajouter une console de notification js, qui sera cachée et visible que par nous
		// pour détecter les erreurs
		// alert('Problème lors de la réinitialisation des champs
		// tiers:'+e.description);
	}
	var searchTiersParams = '';
	if (partialNssValue == '' && idTiers != '')
		searchTiersParams = 'personneEtendueSearchComplexModel.forIdTiers=' + idTiers;
	if (partialNssValue != '' && idTiers == '')
		searchTiersParams = 'personneEtendueSearchComplexModel.forNumeroAvsActuel=756.' + partialNssValue;
	// priorité à la recherche par nss, car affiché à l'écran et pas idTiers
	if (partialNssValue != '' && idTiers != '')
		searchTiersParams = 'personneEtendueSearchComplexModel.forNumeroAvsActuel=756.' + partialNssValue;

	var ajaxQuery = new AjaxQuery(url+'?prefixModel=' + prefixModel
			+ '&userAction=al.ajax.tiersAjax.lister&' + searchTiersParams);
	// pour l'enfant, pas de recherche si le CI si pas de tiers trouvé, on
	// appelle la méthode qui met à jour le form (vide si pas de résultat)
	if (hostScreen && hostScreen == 'AL0003') {
		ajaxQuery.noResultCallback = fillInputWithAjaxResponse;
		ajaxQuery.noResultCallbackParams = new Array(0, true);
	} else {
		ajaxQuery.noResultCallback = synchroCIAjax;
		ajaxQuery.noResultCallbackParams = new Array();
	}
	ajaxQuery.oneResultCallback = checkIfPersonneAFExists;
	ajaxQuery.listResultsCallback = function() {
		alert('plusieurs personnes correspondent à ce NSS, veuillez sélectionner le tiers voulu via [...]');
	};

	ajaxQuery.oneResultCallbackParams = new Array(0, true);
	ajaxQuery.listResultsCallbackParams = new Array();

	if (searchTiersParams != '')
		ajaxQuery.launch();

}

//récupère toutes les informations du tiers et les traite en conséquence ( remplit le formulaire)
function synchroCIAjax() {

	var partialNssValue = document.getElementById('partialforNumAvs').value;

	try {
		document.getElementsByName(targetIdName)[0].value = '';
		document
				.getElementsByName(prefixModel + '.personneEtendueComplexModel.tiers.idTiers')[0].value = '';
		document
				.getElementsByName(prefixModel + '.personneEtendueComplexModel.personne.idTiers')[0].value = '';
		document
				.getElementsByName(prefixModel + '.personneEtendueComplexModel.personneEtendue.idTiers')[0].value = '';
		document
				.getElementsByName(prefixModel + '.personneEtendueComplexModel.tiers.spy')[0].value = '';
		document
				.getElementsByName(prefixModel + '.personneEtendueComplexModel.personne.spy')[0].value = '';
		document
				.getElementsByName(prefixModel + '.personneEtendueComplexModel.personneEtendue.spy')[0].value = '';
		
		document.getElementsByName('dateNaissance')[0].value = '';

	} catch (e) {
		//TODO: ajouter une console de notification js, qui sera cachée et visible que par nous
		// pour détecter les erreurs
		// alert('Problème lors de la réinitialisation des champs
		// tiers:'+e.description);
	}
	var searchCIParams = '';

	if (partialNssValue != '')
		searchCIParams = 'ciSearchModel.forNumAvsActuel=756.' + partialNssValue;
	mlog('synchroCIAjax - prefixModel=' + prefixModel
			+ '&userAction=al.ajax.ci.lister&' + searchCIParams, 'blue');

	// ajaxQuery.noResultCallback = function recherche CI
	// ajaxQuery.noResultCallbackParams = params pour recherche CI
	// ajaxQuery.oneResultCallback = function checkifPersonneAF
	// ajaxQuery.oneResultCallbackParams = params checkifPersonneAF
	// ajaxQuery.listResult = function warning, plusieurs personne avec ce NSS

	var ajaxQuery = new AjaxQuery(url+'?prefixModel=' + prefixModel
			+ '&userAction=al.ajax.ci.lister&' + searchCIParams);

	ajaxQuery.noResultCallback = fillInputWithAjaxResponse;
	ajaxQuery.oneResultCallback = fillInputWithAjaxResponse;
	ajaxQuery.listResultsCallback = function() {
		alert('plusieurs CI correspondent à ce NSS')
	};

	ajaxQuery.noResultCallbackParams = new Array(0, true);
	ajaxQuery.oneResultCallbackParams = new Array(0, true);
	ajaxQuery.listResultsCallbackParams = new Array();
	// on lance que si critères, car sinon...tous
	// if(searchCIParams!='')
	// ajaxQuery.launch();

	if (searchCIParams != '')
		ajaxQuery.launch();

}

//récupère les infos de l'allocataire (utilisé que depuis l'icone synch de l'écran AL0004)
function synchroAllocAjax(displayMode) {
	
	try {
	
		document
				.getElementsByName("dossierComplexModel.dossierModel.idAllocataire")[0].value = '';
		document.getElementsByName("allocataireDesignation")[0].value = '';
		document.getElementsByName("allocataireData")[0].value = '';
		document.getElementsByName("allocataireResidence")[0].value = '';

	} catch (e) {
		//TODO: ajouter une console de notification js, qui sera cachée et visible que par nous
		// pour détecter les erreurs
		mlog('Problème lors de la réinitialisation des champs tiers:' + e.description);
	}

	var nssValue = document.getElementById('partialforNumAvs').value;
	mlog(
			'synchroAllocAjax - userAction=al.ajax.allocataire.lister&prefixModel='
					+ prefixModel
					+ '&allocataireComplexSearchModel.likeNssAllocataire=756.'
					+ nssValue, 'blue');

	var ajaxQuery = new AjaxQuery(
			url+'?userAction=al.ajax.allocataire.lister&prefixModel='
					+ prefixModel
					+ '&allocataireComplexSearchModel.likeNssAllocataire=756.'
					+ nssValue);

	ajaxQuery.noResultCallback = fillInputWithAjaxResponse;
	ajaxQuery.oneResultCallback = fillInputWithAjaxResponse;
	ajaxQuery.listResultsCallback = function() {
		alert('plusieurs allocataires correspondent à ce NSS')
	};

	ajaxQuery.noResultCallbackParams = new Array(0, true);
	ajaxQuery.oneResultCallbackParams = new Array(0, true);
	ajaxQuery.listResultsCallbackParams = new Array();

	ajaxQuery.launch();

}

function synchroAffilieAjax(numAffilie,typeActivite,date) {
	mlog('[METHOD]synchroAffilieAjax', 'blue');
	// resetZonePartForm('affilieZone')
	var ajaxQuery = new AjaxQuery(
			url+'?prefixModel=affiliation&userAction=al.ajax.affiliation.lister&forNumeroAffilie=' + numAffilie+'&typeActivite='+typeActivite+'&dateCalcul='+date);

	ajaxQuery.noResultCallback = function() {
		alert('Pas d\'affilié correspondant à ce n°')
	};
	ajaxQuery.noResultCallbackParams = new Array();

	ajaxQuery.oneResultCallback = fillInputWithAjaxResponse;
	ajaxQuery.listResultsCallback = function() {
		alert('plusieurs affiliés correspondent à ce n°')
	};

	ajaxQuery.oneResultCallbackParams = new Array(0, true);
	ajaxQuery.listResultsCallbackParams = new Array();

	ajaxQuery.launch();

}

function accordStatutLogoWithResponse(forceToValid) {
	if (forceToValid == undefined)
		forceToValid = false;
	var strHtmlError1 = '<img src="images/edit-delete.png" alt="Synchronisation échouée" width="16" height="16"/>';
	var strHtmlError2 = '<img src="images/edit-delete.png" alt="Synchronisation échouée - aucun tiers ne correspond aux critères" width="16" height="16"/>';
	var strHtmlSuccess = '<img src="images/dialog-ok-apply.png" alt="Synchronisation réussie" width="16" height="16"/>';

	// si on a pas les éléments à mettre à jour, on quitte la fonction

	if (document.getElementById(statutSynchroId) == null
			|| document.getElementById("idTiers") == null) {
		return;
	}

	document.getElementById(statutSynchroId).innerHTML = "";

	if (forceToValid)
		document.getElementById(statutSynchroId).innerHTML = strHtmlSuccess;
	else {
		if (document.getElementById("idTiers").value != '') {
			document.getElementById(statutSynchroId).innerHTML = strHtmlError1;
		}
		/*
		if(response.search.results.length==0){
			document.getElementById(statutSynchroId).innerHTML=strHtmlError2;
		}	
		if(response.search.results.length==1 || forceToValid){
			document.getElementById(statutSynchroId).innerHTML=strHtmlSuccess;
		}*/
	}

}