var year = "";
var type = "";
var idRevenuCalcul = "";
var revenuIsTaxationCalcul = "";
var divScrollerCalculs = null;
var revenuMax = "";
var revenuMax_int = "";

//---------------------------------------------------------------------------------------------------
//Object javascript pour enregistrer les types de demandes
//---------------------------------------------------------------------------------------------------
function CSTypeDemande(_cs,_cu,_libelleShort,_libelle) {
	this.cs = _cs;
	this.cu = _cu;
	this.libelleShort = _libelleShort;
	this.libelle = _libelle;
}
var allCSTypeDemande = new Array;

//---------------------------------------------------------------------------------------------------
//Object javascript pour enregistrer les documents - array clé incrément
//---------------------------------------------------------------------------------------------------
function CSDocument(_cs,_cu,_libelleShort,_libelle) {
	this.cs = _cs;
	this.cu = _cu;
	this.libelleShort = _libelleShort;
	this.libelle = _libelle;
}
var allCSDocuments = new Array;
var allCSDocumentsAssiste = new Array;
var allCSDocumentsContribuable = new Array;
var allCSDocumentsPC = new Array;
var allCSDocumentsSourcier = new Array;

//---------------------------------------------------------------------------------------------------
//Object javascript pour enregistrer les caisses maladies
//---------------------------------------------------------------------------------------------------
function CaisseMaladieCalculs(_idCaisse, _noCaisse, _nomCaisse) {
	this.idCaisse = _idCaisse;
	this.noCaisse = _noCaisse;
	this.nomCaisse = _nomCaisse;
}
var allCMCalculs = new Array;

/**
 * 
 * Initialisation du document, enregistrement des méthodes
 * 
 */
$(document).ready(function() {
	
	// Changement de valeurs des données de base
	// Année du subside, type de demande
	$("#s_type_demande, #s_annee_subside").change(function() {
		$("#generateSubsides").prop('disabled',false);
		launchCalculs();
	});
	
	// Enregistrement de l'événement click d'un radio button
	// >> sélection du calcul à appliquer
	$("#tableCalculWizard2").delegate(':radio','click',function() {
		// Effacement des lignes de subsides	
		$(".trDatasSubsides").remove();
		
		//Désactivation des inputs pour éviter un changement en cours de rechargement
		$("#tableCalculWizard2 :input").prop('disabled',true);
		$("#s_type_demande").prop('disabled',true);
		$("#s_annee_subside").prop('disabled',true);
		
		// set des variables nécessaires à la récupération des subsides
		var idRadio = this.id;
		if(idRadio.indexOf('primeMoyenne')==0){
			if(idRadio.split('_')[1]=='A'){
				type = ID_TYPE_SUBSIDE_ASSISTE;
			}else{
				type = ID_TYPE_SUBSIDE_PC;
			}
		}else if(idRadio.indexOf('taxation')==0){
			idRevenuCalcul=idRadio.split('_')[1];
			revenuIsTaxationCalcul='true';
			//type = ID_TYPE_SUBSIDE_DEMANDE;
			type = $('#s_type_demande').val();
		}else if(idRadio.indexOf('revenu')==0){
			idRevenuCalcul=idRadio.split('_')[1];
			revenuIsTaxationCalcul='false';
			//type = ID_TYPE_SUBSIDE_DEMANDE;
			type = $('#s_type_demande').val();
		}
		// Appel AJAX récupération des subsides
		getFamilyLines();
		$("#divCalculWizard_3").css("display","");
		$("#divCalculWizard_4").css("display","");
	});
	

	// Evénement lors d'un select change sur un subside
	$("#tableCalculWizard3").delegate('select','change',function(event){
		var s_choixMembre = 'choixMembre_';
		var s_typeDemande = 'typeDemande_';
		var s_montantContribution = 'montantContribution_';
		var s_document = 'document_';
		var s_noCaisse = 'noCaisse_';
		var s_currentInputId = this.id;
		var s_noLine = s_currentInputId.split('_')[1];
		if(s_noLine=='0'){
			if(s_currentInputId.indexOf(s_typeDemande)==0){
				onMainFamilyTypeDemandeChange();
			}else if(s_currentInputId.indexOf(s_montantContribution)==0){
				onMainFamilyMontantContributionChange();
			}else if(s_currentInputId.indexOf(s_document)==0){
				onMainFamilyDocumentChange();
			}else if(s_currentInputId.indexOf(s_noCaisse)==0){
				onMainFamilyCaisseChange();
			}			
		}else{
			if(s_currentInputId.indexOf(s_typeDemande)==0){
				onSingleItemFamilyTypeDemandeChange(s_noLine);
			}else if(s_currentInputId.indexOf(s_montantContribution)==0){
				onSingleItemFamilyMontantContributionChange(s_noLine);
			}
		}
	});
	
	// Evénement lors d'un input click sur un subside (checkbox)
	$("#tableCalculWizard3").delegate('input[type=checkbox]','change',function(event){
		var s_currentInputId = this.id;
		var s_noLine = s_currentInputId.split('_')[1];
		if(s_noLine=='0'){
			onMainCheckBoxChange($('#'+s_currentInputId).prop('checked'));
		}
	});
	
	// Evénement lors d'un input key up 
	$("#tableCalculWizard3").delegate('input[type=text]','keyup',function(event){
		var s_currentInputId = this.id;
		var s_noLine = s_currentInputId.split('_')[1];
		if(s_noLine=='0'){
			onMainFamilyDebutDroitChange();
			onMainFamilyFinDroitChange();
			onMainFamilyDateReceptionChange();
			onMainFamilyNoAssureChange();
		}
	});
		
	$('#generateSubsides').click(function(){
		if (!checkMandatoryInputs() && window.confirm('Voulez-vous appliquer le(s) calcul(s) sélectionné(s) ?')){
			showProgress('#inProgressDialog');
			onClickGenerate();
		}
	});
});

/**
 * Méthode qui contrôle que tout les champs obligatoire, pour appliquer le droit, 
 * soient rempli (type, montant, debutDroit, document, caisse)
 *  
 * @returns false si tout les champs obligatoires sont remplis
 */
function checkMandatoryInputs() {
	//Reset la propriété background-color des input pour que les cas corrigés ne restent pas en erreur
	$('#tableCalculWizard3').find(':input').css("background-color","");
	$('#s_annee_subside').css("background-color","");
	var fieldEmpty = false;

	
	// ----------------------------------------------------------------------------
	//Sélection des éléments dans tableCalculWizard3 qui sont de type input (sauf les checkbox), activés, vides et dont le nom ne commence
	//pas par finDroit
	// ----------------------------------------------------------------------------
	var $allObj = $('#tableCalculWizard3')
					.find(':input:not(input[type=checkbox]):input[value=""]')
					.not("input[id^='finDroit_']")
					.not("select[id^='noCaisse_']")
					.not("input[id^='noAssure_']")
					.not('input[id^="receptionDemande_"]')
					;
	//on tourne sur chaque champ vide pour lui mettre un fond rouge
	$allObj.each(function(index) {
		var s_line = this.id.split("_")[1];
		var $checkBoxLine = $('#tableCalculWizard3').find('input[id^="choixMembre_'+s_line+'"]');
		if($checkBoxLine.prop('checked')){
			fieldEmpty = true;
			$(this).css("background-color","red");
		}
	});
	

	// ----------------------------------------------------------------------------
	// Contrôle que toutes les années introduites en date de début / fin
	// Correspondent avec l'année historique sélectionnée
	// ----------------------------------------------------------------------------
	var $allObjDate = $('#tableCalculWizard3')
						.find(':input:not(input[type=checkbox])')
						.not('select[id^="noCaisse_"]')
						.not('input[id^="noAssure_"]')
						.not('select[id^="typeDemande_"]')
						.not('select[id^="montantContribution_"]')
						.not('select[id^="document_"]')
						.not('input[id^="receptionDemande_"]')
	;
	$allObjDate.each(function(index) {
		var s_line = this.id.split("_")[1];
		var $checkBoxLine = $('#tableCalculWizard3').find('input[id^="choixMembre_'+s_line+'"]');
		if($checkBoxLine.prop('checked')){
			var s_dateValue = $(this).val();
			if(s_dateValue.length!=0){
				if(s_dateValue.length==7){
					var s_anneeSubside = $('#s_annee_subside').val();
					if(s_dateValue.substring(3)!=s_anneeSubside){
						fieldEmpty = true;
						$('#s_annee_subside').css("background-color","red");
						$(this).css("background-color","red");
					}
				}else if(s_dateValue.length>0){
					fieldEmpty = true;
					$(this).css("background-color","red");
				}else{
					$(this).css("background-color","green");
				}
			}
		}
	});
	// ----------------------------------------------------------------------------
	// Contrôle que toutes les dates de réception de demande 
	// ----------------------------------------------------------------------------
	var $allObjDateReception = $('#tableCalculWizard3')
							.find(':input:not(input[type=checkbox]):input[id^="receptionDemande_"]')
	;
	$allObjDateReception.each(function(index) {
		var s_line = this.id.split("_")[1];
		var $checkBoxLine = $('#tableCalculWizard3').find('input[id^="choixMembre_'+s_line+'"]');
		if($checkBoxLine.prop('checked')){
			var s_dateValue = $(this).val();
			if(s_dateValue.length!=0){
				if(s_dateValue.length!=10){
					fieldEmpty = true;
					$(this).css("background-color","red");
				}
			}
		}
	});
	
	
	return fieldEmpty;
}

/**
 * Initialisation de l'onglet calcul
 * 
 */
function initListCalculValues(){
	// Initialisation de la combo box Année subsides
	initYearValues();
	
	// Les prochaines étapes d'initialisation sont présentes
	// dans les réceptions AJAX
}
/**
 * Initialisation des valeurs année de la combobox
 */
function initYearValues(){

	// Compte le nombre d'option année
	var i_countYear = 0;
	$('#s_annee_subside option').each(function(i, option){
		i_countYear++;
	});	

	// création uniquement si < 5
	if(i_countYear<5){
	
		// effacer la liste courante
		$('#s_annee_subside option').each(function(i, option){
			$(option).remove(); 
		});	
		// la remplir à nouveau
		var dt_currentDate = new Date();
		var i_currentYear = dt_currentDate.getFullYear();
		for(i_Annee=i_currentYear;i_Annee>i_currentYear-6;i_Annee--){
			if(i_Annee==i_currentYear){
				$('#s_annee_subside').append('<option value="" selected="selected"></option>');
			}
			$('#s_annee_subside').append('<option value="'+i_Annee+'">'+i_Annee+'</option>');
		}
		// ajout d'année + 1
		$('#s_annee_subside').append('<option value="'+(i_currentYear+1)+'">'+(i_currentYear+1)+'</option>');
	}
	
	// appel de la prochaine étape d'initialisation
	initTypeDemandeValues();
}

function traitementErreur(jqXHR, textStatus, errorThrown) {
	ajaxUtils.displayError(jqXHR);
	$("#s_type_demande").prop('disabled',false);
	$("#s_annee_subside").prop('disabled',false);
	$("#generateSubsides").prop('disabled',true);	
}

/**
 * Appel AJAX pour récupérer la liste des types de demande
 */
function initTypeDemandeValues(){
	if(allCSTypeDemande.length == 0){
		var o_options= {
				serviceClassName: 'ch.globaz.amal.business.services.models.detailfamille.DetailFamilleService',
				serviceMethodName:'getListeTypeDemandeCalcul',
				parametres:"empty",
				callBack: createListDemande,
				errorCallBack: traitementErreur
			}
			globazNotation.readwidget.options=o_options;		
			globazNotation.readwidget.read();
	}
}
/**
 * Callback de réception des listes de demande
 * @param data
 */
function createListDemande(data){
	allCSTypeDemande = new Array;
	for(i_Line=0;i_Line<data.length;i_Line++){
		var s_line = data[i_Line];
		var s_codeSysteme = s_line.split('|')[0];
		var s_codeUtilisateur = s_line.split('|')[1];
		var s_codeLibelleShort = s_line.split('|')[2];
		var s_codeLibelle = s_line.split('|')[3];
		allCSTypeDemande[i_Line] = new CSTypeDemande(s_codeSysteme,s_codeUtilisateur,s_codeLibelleShort,s_codeLibelle);
	}
	// Refresh combobox
	// 1) remove
	$('#s_type_demande option').each(function(i, option){
		$(option).remove(); 
	});	
	// la remplir à nouveau
	for(i_Element=0;i_Element<allCSTypeDemande.length;i_Element++){
		if(i_Element==0){
			$('#s_type_demande').append('<option value="" selected="selected"></option>');
		}
		$('#s_type_demande').append('<option value="'+allCSTypeDemande[i_Element].cs+'">'+allCSTypeDemande[i_Element].cu+' - '+allCSTypeDemande[i_Element].libelle+'</option>');
	}
	// Appel de la prochaine étape d'initialisation
	initDocumentListValues();
}
/**
 * Appel AJAX pour récupérer la liste des documents
 */
function initDocumentListValues(){
	if(allCSDocuments.length==0){
		var o_options= {
				serviceClassName: 'ch.globaz.amal.business.services.models.detailfamille.DetailFamilleService',
				serviceMethodName:'getListeDocumentCalcul',
				parametres:"empty",
				callBack: createListDocuments,
				errorCallBack: traitementErreur
			}
			globazNotation.readwidget.options=o_options;		
			globazNotation.readwidget.read();
	}
}
/**
 * Callback de réception des listes de documents
 * @param data
 */
function createListDocuments(data){

	allCSDocuments = new Array;
	allCSDocumentsAssiste = new Array;
	allCSDocumentsContribuable = new Array;
	allCSDocumentsPC = new Array;
	allCSDocumentsSourcier = new Array;
	
	var i_LineAssiste = 0;
	var i_LineContribuable = 0;
	var i_LinePC = 0;
	var i_LineSourcier = 0;
	
	for(i_Line=0;i_Line<data.length;i_Line++){
		var s_line = data[i_Line];
		var s_codeSysteme = s_line.split('|')[0];
		var s_codeUtilisateur = s_line.split('|')[1];
		var s_codeLibelleShort = s_line.split('|')[2];
		var s_codeLibelle = s_line.split('|')[3];
		var s_Assiste = s_line.split('|')[4];
		var s_Contribuable = s_line.split('|')[5];
		var s_PC = s_line.split('|')[6];
		var s_Sourcier = s_line.split('|')[7];
		allCSDocuments[i_Line] = new CSDocument(s_codeSysteme,s_codeUtilisateur,s_codeLibelleShort,s_codeLibelle);
		if(s_Assiste=='A'){
			allCSDocumentsAssiste[i_LineAssiste]=new CSDocument(s_codeSysteme,s_codeUtilisateur,s_codeLibelleShort,s_codeLibelle);
			i_LineAssiste++;
		}
		if(s_Contribuable == 'C'){
			allCSDocumentsContribuable[i_LineContribuable]=new CSDocument(s_codeSysteme,s_codeUtilisateur,s_codeLibelleShort,s_codeLibelle);
			i_LineContribuable++;
		}
		if(s_PC == 'P'){
			allCSDocumentsPC[i_LinePC]=new CSDocument(s_codeSysteme,s_codeUtilisateur,s_codeLibelleShort,s_codeLibelle);
			i_LinePC++;
		}
		if(s_Sourcier == 'S'){
			allCSDocumentsSourcier[i_LineSourcier]=new CSDocument(s_codeSysteme,s_codeUtilisateur,s_codeLibelleShort,s_codeLibelle);
			i_LineSourcier++;
		}
	}

	// Appel de la prochaine étape d'initialisation
	initCMListValues();
	
}

/**
 * Appel AJAX pour récupérer la liste des CM
 */
function initCMListValues(){
	if(allCMCalculs.length==0){
		var o_options= {
				serviceClassName: 'ch.globaz.amal.business.services.models.detailfamille.DetailFamilleService',
				serviceMethodName:'getListeCMCalcul',
				parametres:"empty",
				callBack: createListCMCalculs,
				errorCallBack: traitementErreur
			}
			globazNotation.readwidget.options=o_options;		
			globazNotation.readwidget.read();	
	}
}
/**
 * Callback de réception des listes de caisses maladies
 * @param data
 */
function createListCMCalculs(data){
	allCMCalculs = new Array;
	for(i_Line=0;i_Line<data.length;i_Line++){
		var s_line = data[i_Line];
		var s_nomCaisse = s_line.split('|')[0];
		var s_idCaisse = s_line.split('|')[1];
		var s_noCaisse = s_line.split('|')[2];
		allCMCalculs[i_Line] = new CaisseMaladieCalculs(s_idCaisse,s_noCaisse,s_nomCaisse);
	}

	// Sélection automatique des bonnes valeurs dans les combo box de choix
	// initiales, si year et type renseigné
	if(year!=null && year != undefined && year.length>3){
		$('#s_annee_subside [value="'+year+'"]').prop('selected','selected');
	} 
	if(type!= null && type!= undefined && type.length>7){
		$('#s_type_demande [value="'+type+'"]').prop('selected','selected');
	}
	
	launchCalculs();
}

/**
 * Démarre la procédure de calcul
 */
function launchCalculs(){
	//Désactivation des inputs pour éviter un changement en cours de rechargement
	$("#s_type_demande").prop('disabled',true);
	$("#s_annee_subside").prop('disabled',true);
	
	if ( $("#s_type_demande").val().length > 0 && $("#s_annee_subside").val().length > 0) {
		// sélection manuelle
		var local_year = $("#s_annee_subside").val();
		var local_type = $("#s_type_demande").val();
		//On repars à zéro en supprimant les lignes 
		$(".trDatasRevenus").remove();
		$(".trDatasTaxations").remove();
		$(".trDatasSubsides").remove();
		// Mise à jour de l'année sous assisté-pc (informatif)
		$("#selectedYearAssiste").html(local_year);
		$("#selectedYearPC").html(local_year);
		// Récupération des données fiscales (ajax)
		getDonneesFiscales(idContribuable,local_year, local_type);
		// affichage
		$("#divCalculWizard_2").css("display","");
	} else if(year!=null && type!=null){
		// Global automatique
		$("#s_type_demande").val(type);
		$("#s_annee_subside").val(year);
		//On repars à zéro en supprimant les lignes 
		$(".trDatasRevenus").remove();
		$(".trDatasTaxations").remove();
		$(".trDatasSubsides").remove();
		// Mise à jour de l'année sous assisté-pc (informatif)
		$("#selectedYearAssiste").html(year);
		$("#selectedYearPC").html(year);
		// Récupération des données fiscales (ajax)
		getDonneesFiscales(idContribuable,year, type);
		// affichage
		$("#divCalculWizard_2").css("display","");
	}else {
		$("#divCalculWizard_2").css("display","none");
		$("#divCalculWizard_3").css("display","none");
		$("#divCalculWizard_4").css("display","none");
		$("#s_type_demande").prop('disabled',false);
		$("#s_annee_subside").prop('disabled',false);
	}
}


/**
 * Préparation de réception des données fiscales
 * (revenu et taxations)
 * @param _idContribuable
 * @param _year
 * @param _type
 */
function getDonneesFiscales(_idContribuable, _year, _type) {
	idContribuable = _idContribuable;
	year = _year;
	type = _type;	
	initParamsAnnuels();	
}	

/**
 * Appel ajax des taxations (contribuable, année)
 */
function initParamsAnnuels() {
	var cs_typeParamAnnuel = 42002235;
	var o_options= {
			serviceClassName: 'ch.globaz.amal.business.services.models.parametreannuel.SimpleParametreAnnuelService',
			serviceMethodName:'searchAJAX',
			parametres:year+","+cs_typeParamAnnuel,
			callBack: setRevenuMax,
			errorCallBack: traitementErreur
	}
	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}

function setRevenuMax(data) {	
	revenuMax_int = parseInt(data.valeurParametre);
	revenuMax = formatNumber(data.valeurParametre,true);
	
	getListRevenus();
}

/**
 * Appel ajax des revenus (contribuable, année)
 */
function getListRevenus() {
	var o_options= {
			serviceClassName: 'ch.globaz.amal.business.services.models.detailfamille.DetailFamilleService',
			serviceMethodName:'getListRevenus',
			parametres:idContribuable+","+year,
			callBack: createListRevenus,
			errorCallBack: traitementErreur
		}
		globazNotation.readwidget.options=o_options;		
		globazNotation.readwidget.read();	
}

/**
 * callback ajax revenu >> création des lignes de revenus
 * 
 * @param data
 */
function createListRevenus(data) {
	var textToInsert = '';
	for(iElement=0; iElement< data.length; iElement++){
		var classTr = 'amalRowOdd';
		if (iElement % 2 == 0) {
			classTr = 'amalRow';
		}
		var tdStart = '   <td>';
		if(iElement==data.length-1){
			tdStart = '   <td style="border-bottom: 1px solid #226194">';
		}
		textToInsert += '<tr class="'+classTr+' trDatasRevenus" onMouseOver="jscss(\'swap\', this, \''+classTr+'\', \'amalRowHighligthed\')" onMouseOut="jscss(\'swap\', this, \'amalRowHighligthed\', \''+classTr+'\')">';
		// SI P ou A >> pas de possibilité de sélection du revenu >> info only
		if (type==ID_TYPE_SUBSIDE_PC || type == ID_TYPE_SUBSIDE_ASSISTE) {
			textToInsert += tdStart+'</td>';
		} else {
			textToInsert += tdStart+'<input type="radio" name="donnees_fiscales" id="revenu_'+data[iElement].idRevenuHistorique+'" title="revenu_'+data[iElement].idRevenuHistorique+'"></td>';
		}

		textToInsert += tdStart+data[iElement].anneeHistorique+'</td>';
		
		var revenuDetCalcul_int = parseInt(data[iElement].revenuDeterminantCalcul);		
		if (revenuDetCalcul_int>revenuMax_int) {
			textToInsert += tdStart+'<span style="color:red" title="Revenu trop élevé (Max : '+revenuMax+')">'+formatNumber(data[iElement].revenuDeterminantCalcul,true)+'</span></td>';
		} else {
			textToInsert += tdStart+formatNumber(data[iElement].revenuDeterminantCalcul,true)+'</td>';
		}		
		textToInsert += tdStart+data[iElement].anneeTaxation+'</td>';
		textToInsert += tdStart+data[iElement].typeSourceLibelle+'</td>';
		textToInsert += tdStart+data[iElement].typeTaxationLibelle+'</td>';
		textToInsert += tdStart+data[iElement].dateAvisTaxation+'</td>';
		if(data[iElement].typeRevenu=='42002600'){
			textToInsert += tdStart+formatNumber(data[iElement].revenuImposable,true)+'</td>';
		}else{
			textToInsert += tdStart+formatNumber(data[iElement].revenuImposableSourcier,true)+'</td>';
		}
		textToInsert += tdStart+data[iElement].etatCivilLibelle+'</td>';
		var sommeEnfants = parseFloat(data[iElement].nbEnfants)+(parseFloat(data[iElement].nbEnfantSuspens)/2);
		textToInsert += tdStart+sommeEnfants+'</td>';
//		textToInsert += tdStart+data[iElement].nbEnfants+'</td>';
		textToInsert += '</tr>';		
	}
	if(data.length==0){
		var noResult = 'Pas de revenu enregistré pour l\'année '+year;
		var classTr = 'amalRow';
		var tdStart = '   <td style="border-bottom: 1px solid #226194"';
		textToInsert += '<tr class="'+classTr+' trDatasRevenus" onMouseOver="jscss(\'swap\', this, \''+classTr+'\', \'amalRowHighligthed\')" onMouseOut="jscss(\'swap\', this, \'amalRowHighligthed\', \''+classTr+'\')">';
		textToInsert += tdStart+'>&nbsp;</td>';
		textToInsert += tdStart +'align="left" colspan="9">'+noResult+'</td>';
		textToInsert += '</tr>';		
	}
	
	$("#trRevenusCalculWizard2").after(textToInsert);
	$("#div_loading_Revenus").css("display","none");
	getListTaxations(idContribuable,year);	
}

/**
 * Appel ajax des taxations (contribuable, année)
 */
function getListTaxations() {
	var o_options= {
			serviceClassName: 'ch.globaz.amal.business.services.models.detailfamille.DetailFamilleService',
			serviceMethodName:'getListTaxationsCalcul',
			parametres:idContribuable+","+year,
			callBack: createListTaxations,
			errorCallBack: traitementErreur
		}
		globazNotation.readwidget.options=o_options;		
		globazNotation.readwidget.read();	
}

/**
 * callback ajax des taxations >> création des lignes de taxations
 * 
 * @param data
 */
function createListTaxations(data) {
	var textToInsert = '';
	for(iElement=0; iElement< data.length; iElement++){
		var classTr = 'amalRowOdd'
		if (iElement % 2 == 0) {
			classTr = 'amalRow';
		}		
		var tdStart = '   <td>';
		if(iElement==data.length-1){
			tdStart = '   <td style="border-bottom: 1px solid #226194">';
		}
		textToInsert += '<tr class="'+classTr+' trDatasTaxations" onMouseOver="jscss(\'swap\', this, \''+classTr+'\', \'amalRowHighligthed\')" onMouseOut="jscss(\'swap\', this, \'amalRowHighligthed\', \''+classTr+'\')">';
		// SI P ou A >> pas de possibilité de sélection de la taxation >> info only
		if (type==ID_TYPE_SUBSIDE_PC || type == ID_TYPE_SUBSIDE_ASSISTE) {
			textToInsert += tdStart+'</td>';
		} else {
			textToInsert += tdStart+'<input type="radio" name="donnees_fiscales" id="taxation_'+data[iElement].taxation.simpleRevenu.idRevenu+'" title="taxation_'+data[iElement].taxation.simpleRevenu.idRevenu+'"></td>';
		}
		textToInsert +=tdStart+data[iElement].anneeHistorique+'</td>';		
		var revenuDetCalcul_int = parseInt(data[iElement].revenuDeterminant.revenuDeterminantCalcul);		
		if (revenuDetCalcul_int>revenuMax_int) {
			textToInsert += tdStart+'<span style="color:red" title="Revenu trop élevé (Max : '+revenuMax+')">'+formatNumber(data[iElement].revenuDeterminant.revenuDeterminantCalcul,true)+'</span></td>';
		} else {
			textToInsert += tdStart+formatNumber(data[iElement].revenuDeterminant.revenuDeterminantCalcul,true)+'</td>';
		}
		
		textToInsert += tdStart+data[iElement].taxation.simpleRevenu.anneeTaxation+'</td>';
		textToInsert += tdStart+data[iElement].taxation.simpleRevenu.typeSourceLibelle+'</td>';
		textToInsert += tdStart+data[iElement].taxation.simpleRevenu.typeTaxationLibelle+'</td>';
		textToInsert += tdStart+data[iElement].taxation.simpleRevenu.dateAvisTaxation+'</td>';
		if(data[iElement].taxation.simpleRevenu.typeRevenu=='42002600'){
			textToInsert += tdStart+formatNumber(data[iElement].taxation.simpleRevenuContribuable.revenuImposable,true)+'</td>';
		}else{
			textToInsert += tdStart+formatNumber(data[iElement].taxation.simpleRevenuSourcier.revenuImposable,true)+'</td>';
		}
		textToInsert += tdStart+data[iElement].taxation.simpleRevenu.etatCivilLibelle+'</td>';
		var sommeEnfants = parseFloat(data[iElement].taxation.simpleRevenu.nbEnfants)+(parseFloat(data[iElement].taxation.simpleRevenu.nbEnfantSuspens)/2);
		textToInsert += tdStart+sommeEnfants+'</td>';
//		textToInsert += tdStart+data[iElement].taxation.simpleRevenu.nbEnfants+'</td>';	
		textToInsert += '</tr>';		
	}
	if(data.length==0){
		var noResult = 'Pas de taxation enregistrée pour l\'année '+year;
		var classTr = 'amalRow';
		var tdStart = '   <td style="border-bottom: 1px solid #226194"';
		textToInsert += '<tr class="'+classTr+' trDatasRevenus" onMouseOver="jscss(\'swap\', this, \''+classTr+'\', \'amalRowHighligthed\')" onMouseOut="jscss(\'swap\', this, \'amalRowHighligthed\', \''+classTr+'\')">';
		textToInsert += tdStart+'>&nbsp;</td>';
		textToInsert += tdStart +'align="left" colspan="9">'+noResult+'</td>';
		textToInsert += '</tr>';		
	}
	$("#trTaxationsCalculWizard2").after(textToInsert);
	$("#div_loading_Taxations").css("display","none");		
	
	// Sélection automatique du revenu ou de la prime moyenne à sélectionner
	checkAutoLine();
}

/**
 * Appel ajax des mbre de familles
 */
function getFamilyLines() {
	var a_params = new Array(
			"idContribuable:"+idContribuable,
			"anneeHistorique:"+year,
			"typeDemande:"+type,
			"idRevenu:"+idRevenuCalcul,
			"revenuIsTaxation:"+revenuIsTaxationCalcul
			
	);
	
	// format params
	for(i=0;i<a_params.length;i++) {
		a_params[i] = a_params[i].replace(/\'/g,"");
	}		
	

	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.models.detailfamille.DetailFamilleService',
		serviceMethodName:'getListSubsidesCalculAjax',
		parametres:'['+a_params+']',
		callBack: createFamilyLines,
		errorCallBack: traitementErreur
	}

	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();	
}

/**
 * callback ajax famille >> création des lignes de familles
 * @param data
 */
function createFamilyLines(data) {

	var textToInsert = '';
	
	if(data.subsides.length>0){
		for(iElement=0; iElement< data.subsides.length; iElement++){
			var classTr = 'amalRow';
			if (iElement % 2 == 0) {
				classTr = 'amalRowOdd';
			}
			// 1ère ligne, élément famille !
			if(iElement==0){
				// ensuite, membre de la famille
				textToInsert += '<tr style="height:36px" class="amalRow trDatasSubsides" onMouseOver="jscss(\'swap\', this, \'amalRow\', \'amalRowHighligthed\')" onMouseOut="jscss(\'swap\', this, \'amalRowHighligthed\', \'amalRow\')">';
				textToInsert += '	<td><input type="checkbox" id="choixMembre_'+iElement+'_'+data.famille[iElement].idFamille+'"></td>';
				textToInsert += '   <td align="left" title="Famille">&nbsp;F</td>';
				textToInsert += '   <td align="left">'+data.famille[iElement].nomPrenom+'</td>';
				textToInsert += '   <td><select style="width:40px" class="member_type_demande" id="typeDemande_'+iElement+'_'+data.famille[iElement].idFamille+'">';
					// remplir le type de demande
					for(i_Element=0;i_Element<allCSTypeDemande.length;i_Element++){
						// TYPE A/P, pas de sélection de revenu ou taxation possible
						if (type==ID_TYPE_SUBSIDE_PC || type == ID_TYPE_SUBSIDE_ASSISTE) {
							if(allCSTypeDemande[i_Element].cs!=ID_TYPE_SUBSIDE_PC && allCSTypeDemande[i_Element].cs!=ID_TYPE_SUBSIDE_ASSISTE){
								continue;
							}
						}
						// OK insertion
						textToInsert += '<option value="'+allCSTypeDemande[i_Element].cs+'" ';
						if(allCSTypeDemande[i_Element].cs==type){
							textToInsert += 'selected="selected"';
						}
						textToInsert += 'title="'+allCSTypeDemande[i_Element].libelle+'">'+allCSTypeDemande[i_Element].cu+'</option>';
					}
				textToInsert += '   	</select>';
				textToInsert += '   </td>';
				textToInsert += '   <td>';
				textToInsert += '		<select style="width:80px" class="member_montant" id="montantContribution_'+iElement+'_'+data.famille[iElement].idFamille+'_'+data.subsides[iElement].montantContribSansSuppl+'_'+data.subsides[iElement].montantSupplement+'">';

				if (type!=ID_TYPE_SUBSIDE_PC && type != ID_TYPE_SUBSIDE_ASSISTE) {
					if (data.subsides[iElement].montantSupplement == undefined) {
						textToInsert += '			<option value="montantContribution" selected="selected">'+formatNumber(data.subsides[iElement].montantContribution,true)+'</option>';
					} else {
						var total = parseInt(data.subsides[iElement].montantContribution)+parseInt(data.subsides[iElement].montantSupplement);
						textToInsert += '			<option value="montantContribution" selected="selected" style="color:green">'+formatNumber(total,true)+'</option>';						
					}
				}
				if (data.subsides[iElement].montantContributionAssiste != undefined) {
					var selected = "";
					if (type == ID_TYPE_SUBSIDE_ASSISTE) {
						selected='selected="selected"';
					}
					textToInsert += '			<option value="montantContributionAssiste" '+selected+'>'+formatNumber(data.subsides[iElement].montantContributionAssiste,false)+'</option>';
				}
				if (data.subsides[iElement].montantContributionPC != undefined) {
					var selected = "";
					if (type == ID_TYPE_SUBSIDE_PC) {
						selected='selected="selected"';
					}
					textToInsert += '			<option value="montantContributionPC" '+selected+'>'+formatNumber(data.subsides[iElement].montantContributionPC,false)+'</option>';
				}
				textToInsert += '			<option value="Refus">R</option>';
				textToInsert += '   	</select>';
				textToInsert += '   </td>';
				textToInsert += '   <td><input type="text" id="debutDroit_'+iElement+'_'+data.famille[iElement].idFamille+'" value="'+data.subsides[iElement].debutDroit+'" size="7" /></td>';
				textToInsert += '   <td><input type="text" id="finDroit_'+iElement+'_'+data.famille[iElement].idFamille+'" value="" size="7" /></td>';
				textToInsert += '   <td><input type="text" id="receptionDemande_'+iElement+'_'+data.famille[iElement].idFamille+'" value="" size="10" /></td>';
				textToInsert += '   <td><span><select style="width:140px" class="member_document" id="document_'+iElement+'_'+data.famille[iElement].idFamille+'"></span>';
					// Adaptation de la liste des documents en fonction du type de demande
					if(type==ID_TYPE_SUBSIDE_PC){
						// remplir documents PC
						for(i_Element=0;i_Element<allCSDocumentsPC.length;i_Element++){
							if(i_Element==0){
								textToInsert +='<option value="" selected="selected"></option>';
							}
							textToInsert += '<option value="'+allCSDocumentsPC[i_Element].cs+'" ';
							textToInsert += 'title="'+allCSDocumentsPC[i_Element].libelle+'">'+allCSDocumentsPC[i_Element].cu+' - '+allCSDocumentsPC[i_Element].libelleShort+'</option>';
						}
					}else if(type == ID_TYPE_SUBSIDE_ASSISTE){
						// remplir documents ASSISTE
						for(i_Element=0;i_Element<allCSDocumentsAssiste.length;i_Element++){
							if(i_Element==0){
								textToInsert +='<option value="" selected="selected"></option>';
							}
							textToInsert += '<option value="'+allCSDocumentsAssiste[i_Element].cs+'" ';
							textToInsert += 'title="'+allCSDocumentsAssiste[i_Element].libelle+'">'+allCSDocumentsAssiste[i_Element].cu+' - '+allCSDocumentsAssiste[i_Element].libelleShort+'</option>';
						}
					}else if(type == ID_TYPE_SUBSIDE_SOURCE){
						// remplir documents SOURCIER
						for(i_Element=0;i_Element<allCSDocumentsSourcier.length;i_Element++){
							if(i_Element==0){
								textToInsert +='<option value="" selected="selected"></option>';
							}
							textToInsert += '<option value="'+allCSDocumentsSourcier[i_Element].cs+'" ';
							textToInsert += 'title="'+allCSDocumentsSourcier[i_Element].libelle+'">'+allCSDocumentsSourcier[i_Element].cu+' - '+allCSDocumentsSourcier[i_Element].libelleShort+'</option>';
						}
					}else{
						// remplir documents CONTRIBUABLE
						for(i_Element=0;i_Element<allCSDocumentsContribuable.length;i_Element++){
							if(i_Element==0){
								textToInsert +='<option value="" selected="selected"></option>';
							}
							textToInsert += '<option value="'+allCSDocumentsContribuable[i_Element].cs+'" ';
							textToInsert += 'title="'+allCSDocumentsContribuable[i_Element].libelle+'">'+allCSDocumentsContribuable[i_Element].cu+' - '+allCSDocumentsContribuable[i_Element].libelleShort+'</option>';
						}
					}
				textToInsert += '   	</select>';
				textToInsert += '   </td>';
				
				textToInsert += '   <td><select style="width:140px" class="member_caisse" id="noCaisse_'+iElement+'_'+data.famille[iElement].idFamille+'">';
					// remplir caisses
					for(i_Element=0;i_Element<allCMCalculs.length;i_Element++){
						if(i_Element==0){
							textToInsert +='<option value="" selected="selected"></option>';
						}
						textToInsert += '<option value="'+allCMCalculs[i_Element].idCaisse+'" ';
						textToInsert += 'title="'+allCMCalculs[i_Element].nomCaisse+' - '+allCMCalculs[i_Element].noCaisse+'">'+allCMCalculs[i_Element].nomCaisse+'</option>';
					}
				textToInsert += '   	</select>';
				textToInsert += '   </td>';
				
				//textToInsert += '   <td><input type="text" id="noAssure_'+iElement+'_'+data.famille[iElement].idFamille+'" value="" size="15"/></td>';
				textToInsert += '	<td>&nbsp;</td>';
				textToInsert += '	<td>&nbsp;</td>';
				textToInsert += '</tr>';
			}
						
			
			// ensuite, membre de la famille
			textToInsert += '<tr class="'+classTr+' trDatasSubsides" onMouseOver="jscss(\'swap\', this, \''+classTr+'\', \'amalRowHighligthed\')" onMouseOut="jscss(\'swap\', this, \'amalRowHighligthed\', \''+classTr+'\')">';
			textToInsert += '	<td><input type="checkbox" id="choixMembre_'+(iElement+1)+'_'+data.famille[iElement].idFamille+'"></td>';
			if(data.famille[iElement].pereMereEnfantLibelle.length>1){
				textToInsert += '   <td>&nbsp;'+data.famille[iElement].pereMereEnfantLibelle.substring(0,1)+'&nbsp</td>';
			}else{
				textToInsert += '   <td>'+data.famille[iElement].pereMereEnfantLibelle+'</td>';
			}
			textToInsert += '   <td title="'+data.famille[iElement].dateNaissance+'">'+data.famille[iElement].nomPrenom+'</td>';
			textToInsert += '   <td><select style="width:40px" class="member_type_demande" id="typeDemande_'+(iElement+1)+'_'+data.famille[iElement].idFamille+'">';
				// remplir le type de demande
				for(i_Element=0;i_Element<allCSTypeDemande.length;i_Element++){
					// TYPE A/P, pas de sélection de revenu ou taxation possible
					if (type==ID_TYPE_SUBSIDE_PC || type == ID_TYPE_SUBSIDE_ASSISTE) {
						if(allCSTypeDemande[i_Element].cs!=ID_TYPE_SUBSIDE_PC && allCSTypeDemande[i_Element].cs!=ID_TYPE_SUBSIDE_ASSISTE){
							continue;
						}
					}
					// OK insertion
					textToInsert += '<option value="'+allCSTypeDemande[i_Element].cs+'" ';
					if(allCSTypeDemande[i_Element].cs==type){
						textToInsert += 'selected="selected"';
					}
					textToInsert += 'title="'+allCSTypeDemande[i_Element].libelle+'">'+allCSTypeDemande[i_Element].cu+'</option>';
				}
			textToInsert += '   	</select>';
			textToInsert += '   </td>';
			textToInsert += '   <td>';
			textToInsert += '		<select style="width:80px" class="member_montant" id="montantContribution_'+(iElement+1)+'_'+data.famille[iElement].idFamille+'_'+data.subsides[iElement].montantContribSansSuppl+'_'+data.subsides[iElement].montantSupplement+'">';
			if (type!=ID_TYPE_SUBSIDE_PC && type != ID_TYPE_SUBSIDE_ASSISTE) {
				if (data.subsides[iElement].montantSupplement == undefined) {
					textToInsert += '			<option value="montantContribution" selected="selected">'+formatNumber(data.subsides[iElement].montantContribution,true)+'</option>';
				} else {
					var total = parseInt(data.subsides[iElement].montantContribution)+parseInt(data.subsides[iElement].montantSupplement);
					textToInsert += '			<option value="montantContribution" selected="selected" style="color:green">'+formatNumber(total,true)+'</option>';
				}
			}
			if (data.subsides[iElement].montantContributionAssiste != undefined) {
				var selected = "";
				if (type == ID_TYPE_SUBSIDE_ASSISTE) {
					selected='selected="selected"';
				}
				textToInsert += '			<option value="montantContributionAssiste" '+selected+'>'+formatNumber(data.subsides[iElement].montantContributionAssiste,false)+'</option>';
			}
			if (data.subsides[iElement].montantContributionPC != undefined) {
				var selected = "";
				if (type == ID_TYPE_SUBSIDE_PC) {
					selected='selected="selected"';
				}
				textToInsert += '			<option value="montantContributionPC" '+selected+'>'+formatNumber(data.subsides[iElement].montantContributionPC,false)+'</option>';
			}
			textToInsert += '			<option value="Refus">R</option>';
			textToInsert += '   	</select>';
			textToInsert += '   </td>';
			textToInsert += '   <td><input type="text" id="debutDroit_'+(iElement+1)+'_'+data.famille[iElement].idFamille+'" value="'+data.subsides[iElement].debutDroit+'" size="7" class="member_date_debut"/></td>';
			textToInsert += '   <td><input type="text" id="finDroit_'+(iElement+1)+'_'+data.famille[iElement].idFamille+'" value="" size="7" class="member_date_fin"/></td>';
			textToInsert += '   <td><input type="text" id="receptionDemande_'+(iElement+1)+'_'+data.famille[iElement].idFamille+'" value="';
			if(data.subsides[iElement].dateRecepDemande != null && data.subsides[iElement].dateRecepDemande != undefined && data.subsides[iElement].dateRecepDemande.length>0){
				textToInsert += data.subsides[iElement].dateRecepDemande;
			}
			textToInsert += '" size="10" /></td>';
			textToInsert += '   <td><select style="width:140px" class="member_document" id="document_'+(iElement+1)+'_'+data.famille[iElement].idFamille+'">';
				// remplir documents
				// Adaptation de la liste des documents en fonction du type de demande
				if(type==ID_TYPE_SUBSIDE_PC){
					// remplir documents PC
					for(i_Element=0;i_Element<allCSDocumentsPC.length;i_Element++){
						if(i_Element==0){
							textToInsert +='<option value="" selected="selected"></option>';
						}
						textToInsert += '<option value="'+allCSDocumentsPC[i_Element].cs+'" ';
						textToInsert += 'title="'+allCSDocumentsPC[i_Element].libelle+'">'+allCSDocumentsPC[i_Element].cu+' - '+allCSDocumentsPC[i_Element].libelleShort+'</option>';
					}
				}else if(type == ID_TYPE_SUBSIDE_ASSISTE){
					// remplir documents ASSISTE
					for(i_Element=0;i_Element<allCSDocumentsAssiste.length;i_Element++){
						if(i_Element==0){
							textToInsert +='<option value="" selected="selected"></option>';
						}
						textToInsert += '<option value="'+allCSDocumentsAssiste[i_Element].cs+'" ';
						textToInsert += 'title="'+allCSDocumentsAssiste[i_Element].libelle+'">'+allCSDocumentsAssiste[i_Element].cu+' - '+allCSDocumentsAssiste[i_Element].libelleShort+'</option>';
					}
				}else if(type == ID_TYPE_SUBSIDE_SOURCE){
					// remplir documents SOURCIER
					for(i_Element=0;i_Element<allCSDocumentsSourcier.length;i_Element++){
						if(i_Element==0){
							textToInsert +='<option value="" selected="selected"></option>';
						}
						textToInsert += '<option value="'+allCSDocumentsSourcier[i_Element].cs+'" ';
						textToInsert += 'title="'+allCSDocumentsSourcier[i_Element].libelle+'">'+allCSDocumentsSourcier[i_Element].cu+' - '+allCSDocumentsSourcier[i_Element].libelleShort+'</option>';
					}
				}else{
					// remplir documents CONTRIBUABLE
					for(i_Element=0;i_Element<allCSDocumentsContribuable.length;i_Element++){
						if(i_Element==0){
							textToInsert +='<option value="" selected="selected"></option>';
						}
						textToInsert += '<option value="'+allCSDocumentsContribuable[i_Element].cs+'" ';
						textToInsert += 'title="'+allCSDocumentsContribuable[i_Element].libelle+'">'+allCSDocumentsContribuable[i_Element].cu+' - '+allCSDocumentsContribuable[i_Element].libelleShort+'</option>';
					}
				}
			textToInsert += '   	</select>';
			textToInsert += '   </td>';
			
			textToInsert += '   <td><select style="width:140px" class="member_caisse" id="noCaisse_'+(iElement+1)+'_'+data.famille[iElement].idFamille+'">';
			// remplir caisses
				for(i_Element=0;i_Element<allCMCalculs.length;i_Element++){
					// Si première ligne && pas de no caisse renseignée
					if(data.subsides[iElement].noCaisseMaladie==null ||data.subsides[iElement].noCaisseMaladie==undefined || data.subsides[iElement].noCaisseMaladie.length==0){
						if(i_Element==0){
							textToInsert +='<option value="" selected="selected"></option>';
						}
						textToInsert += '<option value="'+allCMCalculs[i_Element].idCaisse+'" ';
						textToInsert += 'title="'+allCMCalculs[i_Element].nomCaisse+' - '+allCMCalculs[i_Element].noCaisse+'">'+allCMCalculs[i_Element].nomCaisse+'</option>';
					}else if(data.subsides[iElement].noCaisseMaladie!=null && data.subsides[iElement].noCaisseMaladie!=undefined && data.subsides[iElement].noCaisseMaladie.length>0){
						if(i_Element==0){
							textToInsert +='<option value=""></option>';
						}
						if(allCMCalculs[i_Element].idCaisse == data.subsides[iElement].noCaisseMaladie){
							textToInsert += '<option value="'+allCMCalculs[i_Element].idCaisse+'" selected="selected" ';
						}else{
							textToInsert += '<option value="'+allCMCalculs[i_Element].idCaisse+'" ';
						}
						textToInsert += 'title="'+allCMCalculs[i_Element].nomCaisse+' - '+allCMCalculs[i_Element].noCaisse+'">'+allCMCalculs[i_Element].nomCaisse+'</option>';
					}
				}
			textToInsert += '   	</select>';
			textToInsert += '   </td>';
			
			// No assuré
			textToInsert += '   <td><input type="text" id="noAssure_'+(iElement+1)+'_'+data.famille[iElement].idFamille+'" value="';
			if(data.subsides[iElement].noAssure != null && data.subsides[iElement].noAssure != undefined && data.subsides[iElement].noAssure.length>0){
				textToInsert += data.subsides[iElement].noAssure;
			}
			textToInsert += '" size="15"/></td>';
			textToInsert += '	<td>&nbsp;</td>';
			textToInsert += '</tr>';
		}
	}
		
	//notationManager.addNotationOnFragment($(textToInsert));
	//notationManager.addNotationOnFragment(textToInsert);
	var $textToInsert = $(textToInsert);
	//Supprime les données déjà présente pour ne pas afficher 2x tableau l'un sous l'autre
	$(".trDatasSubsides").remove();
	$("#trCalculWizard3").after($textToInsert);
	//Permet de générer les notations sur les input dynamiques.
	//Ralenti considérablement l'affichage et les boutons calendriers ne se désactivent pas correctement
	//notationManager.addNotationOnFragment($textToInsert);
	
	initFamilyLines();
	$("#div_loading_Result").css("display","none");	
}
/**
 * Initialisation des lignes des familles (subsides)
 * 
 * 1) Sélection de la famille (checkbox)
 * 2) Disable de tous les éléments membres familles particuliers (Type, Montant, Début, Fin, Document, Caisse)
 */
function initFamilyLines(){
	
	var s_choixMembre = 'choixMembre_';
	var s_typeDemande = 'typeDemande_';
	var s_montantContribution = 'montantContribution_';
	var s_debutDroit = 'debutDroit_';
	var s_finDroit = 'finDroit_';
	var s_document = 'document_';
	var s_noCaisse = 'noCaisse_';

	// travail sur les inputs (text et checkbox)
	$('#tableCalculWizard3 input').each(function(index,domElement){
		var s_currentInputId = domElement.id;
		var s_noLine = s_currentInputId.split('_')[1];
		if(s_noLine=='0'){
			if(s_currentInputId.indexOf(s_choixMembre)==0){
				// Il s'agit de la checkbox, check by default
				$('#'+s_currentInputId).prop('checked','checked');
			}
			// Enable par défaut
			$('#'+s_currentInputId).removeProp('disabled');
		}else{
			if(s_currentInputId.indexOf(s_choixMembre)==0){
				// Il s'agit de la checkbox, check by default
				$('#'+s_currentInputId).prop('checked','checked');
			}
			// disable par défaut (ligne 1 à x)
			$('#'+s_currentInputId).prop('disabled','disabled');
		}
		$('#'+s_currentInputId).css('background-color','');
	});

	// travail sur les select
	$('#tableCalculWizard3 select').each(function(index,domElement){
		var s_currentInputId = domElement.id;
		var s_noLine = s_currentInputId.split('_')[1];
		if(s_noLine=='0'){
			// Enable par défaut
			$('#'+s_currentInputId).removeProp('disabled');			
		}else{
			// disable par défaut (ligne 1 à x)
			$('#'+s_currentInputId).prop('disabled','disabled');
		}
		$('#'+s_currentInputId).css('background-color','');
	});
	
	// si scrollerCalculs != null, va en bas
	if(divScrollerCalculs!=null){
		divScrollerCalculs.scrollTop = divScrollerCalculs.scrollHeight;
	}
	
	$("#tableCalculWizard2 :input").prop('disabled',false);
	$("#divCalculWizard_1 :input").prop('disabled',false);
}
/**
 * Réaction à l'événement main check box change 
 * de la liste de subsides
 * 
 * @param checked
 */
function onMainCheckBoxChange(newChecked){
	if(newChecked){
		// disable all
		// check all
		initFamilyLines();
		// répercution de l'état de la ligne 1 sur les autres lignes
		onMainFamilyTypeDemandeChange();
		onMainFamilyMontantContributionChange();
		onMainFamilyDebutDroitChange();
		onMainFamilyFinDroitChange();
		onMainFamilyDateReceptionChange();
		onMainFamilyDocumentChange();
		onMainFamilyCaisseChange();
		onMainFamilyNoAssureChange();
	}else{
		// enable all
		// uncheck all
		var s_choixMembre = 'choixMembre_';

		// travail sur les inputs (text et checkbox)
		$('#tableCalculWizard3 input').each(function(index,domElement){
			var s_currentInputId = domElement.id;
			var s_noLine = s_currentInputId.split('_')[1];
			
			if(s_currentInputId.indexOf(s_choixMembre)!=0){
				// pour les éléments différents de check box (text)
				// Enable par défaut
				if(s_noLine!='0'){
					$('#'+s_currentInputId).removeProp('disabled');
				}else{
					$('#'+s_currentInputId).prop('disabled','disabled');
				}
			}else{
				// pour les éléments checkbox 
				$('#'+s_currentInputId).removeProp('disabled');
			}
			$('#'+s_currentInputId).css('background-color','');
		});

		// travail sur les select
		$('#tableCalculWizard3 select').each(function(index,domElement){
			var s_currentInputId = domElement.id;
			var s_noLine = s_currentInputId.split('_')[1];
			if(s_noLine!='0'){
				$('#'+s_currentInputId).removeProp('disabled');
			}else{
				$('#'+s_currentInputId).prop('disabled','disabled');
			}
			$('#'+s_currentInputId).css('background-color','');
		});
	}
}
/**
 * Répercution du changement de la ligne 0
 * sur les autres lignes, TYPE DE DEMANDE
 */
function onMainFamilyTypeDemandeChange(){
	// uniquement valide si la checkbox principale est checké
	if($('#tableCalculWizard3 input[type=checkbox]:first').prop('checked')){

		// variable de travail
		var s_Value = '';
		var s_typeDemande = 'typeDemande_';
		var s_montantContribution = 'montantContribution_';
		var s_optionContribution = 'montantContribution';
		var s_optionContributionAssiste = 'montantContributionAssiste';
		var s_optionContributionPC = 'montantContributionPC';
		var s_optionDocument = 'document';

		// travail sur les select, cible typeDemande
		$('#tableCalculWizard3 select').each(function(index,domElement){
			var s_currentInputId = domElement.id;
			var s_noLine = s_currentInputId.split('_')[1];
			if(s_currentInputId.indexOf(s_typeDemande)==0){
				if(s_noLine=='0'){
					// 1ère ligne, récupération de la valeur
					s_Value = $('#'+s_currentInputId).val();
				}else{
					// ligne ++, application de la sélection
					if(s_Value.length>0){
						$('#'+s_currentInputId+' option[value='+s_Value+']').prop('selected','selected');
					}
				}
				// changement également du montant si P/A ou D/S
				$('#tableCalculWizard3 select').each(function(indexOption,domElementOption){
					var s_currentInputOptionId = domElementOption.id;
					var s_noLineOption = s_currentInputOptionId.split('_')[1];
					if(s_noLineOption == s_noLine){
						if(s_currentInputOptionId.indexOf(s_montantContribution)==0){
							// on est sur les montants
							if(s_Value==ID_TYPE_SUBSIDE_ASSISTE){
								$('#'+s_currentInputOptionId+' option[value='+s_optionContributionAssiste+']').prop('selected','selected');
							} else if(s_Value ==ID_TYPE_SUBSIDE_PC){
								if ($('#'+s_currentInputOptionId+' option[value='+s_optionContributionPC+']').val() != undefined) {
									$('#'+s_currentInputOptionId+' option[value='+s_optionContributionPC+']').prop('selected','selected');
								} else {
									$('#'+s_currentInputOptionId+' option[value='+s_optionContributionAssiste+']').prop('selected','selected');
								}
							} else{
								$('#'+s_currentInputOptionId+' option[value='+s_optionContribution+']').prop('selected','selected');
							}
						}else if(s_currentInputOptionId.indexOf(s_optionDocument)==0){
							adaptDocumentList(s_Value,s_currentInputOptionId);
						}
					}
				});
			}
		});
	}
}
/**
 * Répercution du changement de la ligne x
 * sur sur le montant du subside
 */
function onSingleItemFamilyTypeDemandeChange(s_Line){
	// uniquement valide si la checkbox principale n'est pas checked
	if(!$('#tableCalculWizard3 input[type=checkbox]:first').prop('checked')){

		// variable de travail
		var s_Value = '';
		var s_typeDemande = 'typeDemande_';
		var s_montantContribution = 'montantContribution_';
		var s_optionContribution = 'montantContribution';
		var s_optionContributionAssiste = 'montantContributionAssiste';
		var s_optionContributionPC = 'montantContributionPC';
		var s_optionDocument = 'document';

		// travail sur les select, cible typeDemande
		$('#tableCalculWizard3 select').each(function(index,domElement){
			var s_currentInputId = domElement.id;
			var s_noLine = s_currentInputId.split('_')[1];
			if(s_currentInputId.indexOf(s_typeDemande)==0){
				if(s_noLine==s_Line){
					// 1ère ligne, récupération de la valeur
					s_Value = $('#'+s_currentInputId).val();
					// changement également du montant si P/A ou D/S
					$('#tableCalculWizard3 select').each(function(indexOption,domElementOption){
						var s_currentInputOptionId = domElementOption.id;
						var s_noLineOption = s_currentInputOptionId.split('_')[1];
						if(s_noLineOption == s_noLine){
							if(s_currentInputOptionId.indexOf(s_montantContribution)==0){
								// on est sur les montants
								if(s_Value==ID_TYPE_SUBSIDE_ASSISTE){
									$('#'+s_currentInputOptionId+' option[value='+s_optionContributionAssiste+']').prop('selected','selected');
								} else if(s_Value ==ID_TYPE_SUBSIDE_PC){
									$('#'+s_currentInputOptionId+' option[value='+s_optionContributionPC+']').prop('selected','selected');
								} else{
									$('#'+s_currentInputOptionId+' option[value='+s_optionContribution+']').prop('selected','selected');
								}
							}else if(s_currentInputOptionId.indexOf(s_optionDocument)==0){
								adaptDocumentList(s_Value,s_currentInputOptionId);
							}
						}
					});
				}
			}
		});
	}
}
/*
 * Adapte la liste des documents à afficher en fonction du type de demande
 */
function adaptDocumentList(typeDemande, idDocumentList){
	var textToInsert = '';
	if(typeDemande==ID_TYPE_SUBSIDE_PC){
		// remplir documents PC
		for(i_Element=0;i_Element<allCSDocumentsPC.length;i_Element++){
			if(i_Element==0){
				textToInsert +='<option value="" selected="selected"></option>';
			}
			textToInsert += '<option value="'+allCSDocumentsPC[i_Element].cs+'" ';
			textToInsert += 'title="'+allCSDocumentsPC[i_Element].libelle+'">'+allCSDocumentsPC[i_Element].cu+' - '+allCSDocumentsPC[i_Element].libelleShort+'</option>';
		}
	}else if(typeDemande == ID_TYPE_SUBSIDE_ASSISTE){
		// remplir documents ASSISTE
		for(i_Element=0;i_Element<allCSDocumentsAssiste.length;i_Element++){
			if(i_Element==0){
				textToInsert +='<option value="" selected="selected"></option>';
			}
			textToInsert += '<option value="'+allCSDocumentsAssiste[i_Element].cs+'" ';
			textToInsert += 'title="'+allCSDocumentsAssiste[i_Element].libelle+'">'+allCSDocumentsAssiste[i_Element].cu+' - '+allCSDocumentsAssiste[i_Element].libelleShort+'</option>';
		}
	}else if(typeDemande == ID_TYPE_SUBSIDE_SOURCE){
		// remplir documents SOURCIER
		for(i_Element=0;i_Element<allCSDocumentsSourcier.length;i_Element++){
			if(i_Element==0){
				textToInsert +='<option value="" selected="selected"></option>';
			}
			textToInsert += '<option value="'+allCSDocumentsSourcier[i_Element].cs+'" ';
			textToInsert += 'title="'+allCSDocumentsSourcier[i_Element].libelle+'">'+allCSDocumentsSourcier[i_Element].cu+' - '+allCSDocumentsSourcier[i_Element].libelleShort+'</option>';
		}
	}else{
		// remplir documents CONTRIBUABLE
		for(i_Element=0;i_Element<allCSDocumentsContribuable.length;i_Element++){
			if(i_Element==0){
				textToInsert +='<option value="" selected="selected"></option>';
			}
			textToInsert += '<option value="'+allCSDocumentsContribuable[i_Element].cs+'" ';
			textToInsert += 'title="'+allCSDocumentsContribuable[i_Element].libelle+'">'+allCSDocumentsContribuable[i_Element].cu+' - '+allCSDocumentsContribuable[i_Element].libelleShort+'</option>';
		}
	}
	$('#'+idDocumentList).find('option').remove();
	$('#'+idDocumentList).append(textToInsert);
}

/**
 * Répercution du changement de la ligne 0
 * sur les autres lignes, MONTANT CONTRIBUTION
 */
function onMainFamilyMontantContributionChange(){
	// uniquement valide si la checkbox principale est checké
	if($('#tableCalculWizard3 input[type=checkbox]:first').prop('checked')){

		// variable de travail
		var s_Value = '';
		var s_typeDemande = 'typeDemande_';
		var s_montantContribution = 'montantContribution_';
		var s_optionContribution = 'montantContribution';
		var s_optionContributionAssiste = 'montantContributionAssiste';
		var s_optionContributionPC = 'montantContributionPC';

		// travail sur les select, cible typeDemande
		$('#tableCalculWizard3 select').each(function(index,domElement){
			var s_currentInputId = domElement.id;
			var s_noLine = s_currentInputId.split('_')[1];
			if(s_currentInputId.indexOf(s_montantContribution)==0){
				if(s_noLine=='0'){
					// 1ère ligne, récupération de la valeur
					s_Value = $('#'+s_currentInputId).val();
				}else{
					// ligne ++, application de la sélection
					if(s_Value.length>0){
						$('#'+s_currentInputId+' option[value='+s_Value+']').prop('selected','selected');
					}
				}
				// changement du type de demande si montant Assiste ou normal
				$('#tableCalculWizard3 select').each(function(indexDemande,domElementDemande){
					var s_currentInputDemandeId = domElementDemande.id;
					var s_noLineDemande = s_currentInputDemandeId.split('_')[1];
					if(s_noLineDemande == s_noLine){
						if(s_currentInputDemandeId.indexOf(s_typeDemande)==0){
							// Sélection du type de demande A ou D
							if(s_Value ==s_optionContribution){
								$('#'+s_currentInputDemandeId+' option[value=42002001]').prop('selected','selected');
							}else if (s_Value == s_optionContributionAssiste) {
								$('#'+s_currentInputDemandeId+' option[value=42002000]').prop('selected','selected');
							}else if (s_Value == s_optionContributionPC) {
								$('#'+s_currentInputDemandeId+' option[value=42002003]').prop('selected','selected');
							}
							// pas d'action pour R
						}
					}
				});

			}
		});
	}
}
/**
 * Répercution du changement de la ligne x
 * sur le type de demande
 */
function onSingleItemFamilyMontantContributionChange(s_Line){
	// uniquement valide si la checkbox principale n'est pas checked
	if(!$('#tableCalculWizard3 input[type=checkbox]:first').prop('checked')){

		// variable de travail
		var s_Value = '';
		var s_typeDemande = 'typeDemande_';
		var s_montantContribution = 'montantContribution_';
		var s_optionContribution = 'montantContribution';
		var s_optionContributionAssiste = 'montantContributionAssiste';
		var s_optionContributionPC = 'montantContributionPC';

		// travail sur les select, cible typeDemande
		$('#tableCalculWizard3 select').each(function(index,domElement){
			var s_currentInputId = domElement.id;
			var s_noLine = s_currentInputId.split('_')[1];
			if(s_currentInputId.indexOf(s_montantContribution)==0){
				// Travail sur la ligne transmise en paramètre
				if(s_noLine==s_Line){
					// 1ère ligne, récupération de la valeur
					s_Value = $('#'+s_currentInputId).val();
					// changement du type de demande si montant Assiste ou normal
					$('#tableCalculWizard3 select').each(function(indexDemande,domElementDemande){
						var s_currentInputDemandeId = domElementDemande.id;
						var s_noLineDemande = s_currentInputDemandeId.split('_')[1];
						if(s_noLineDemande == s_noLine){
							if(s_currentInputDemandeId.indexOf(s_typeDemande)==0){
								// Sélection du type de demande A ou D
								if(s_Value ==s_optionContribution){
									$('#'+s_currentInputDemandeId+' option[value=42002001]').prop('selected','selected');
								}else if (s_Value == s_optionContributionAssiste)
								{
									$('#'+s_currentInputDemandeId+' option[value=42002000]').prop('selected','selected');
								}else if (s_Value == s_optionContributionPC) {
									$('#'+s_currentInputDemandeId+' option[value=42002003]').prop('selected','selected');
								}
								// pas d'action pour R
							}
						}
					});
				}
			}
		});
	}
}
/**
 * Répercution du changement de la ligne 0
 * sur les autres lignes, DEBUT DU DROIT
 */
function onMainFamilyDebutDroitChange(){
	// uniquement valide si la checkbox principale est checké
	if($('#tableCalculWizard3 input[type=checkbox]:first').prop('checked')){

		// variable de travail
		var s_Value = '';
		var s_debutDroit = 'debutDroit_';
		// Evénement lors d'un input 
		$('#tableCalculWizard3 input[type=text]').each(function(index,domElement){
			var s_currentInputId = domElement.id;
			var s_noLine = s_currentInputId.split('_')[1];
			if(s_currentInputId.indexOf(s_debutDroit)==0){
				if(s_noLine=='0'){
					// 1ère ligne, récupération de la valeur
					s_Value = $('#'+s_currentInputId).val();
				}else{
					// ligne ++, application de la sélection
					$('#'+s_currentInputId).val(s_Value);
				}
			}
		});
	
	}	
}
/**
 * Répercution du changement de la ligne 0 sur
 * les autres lignes, NO ASSURE
 */
function onMainFamilyNoAssureChange(){
	// désactiver car comportement différent, no assuré unique par tête de pipe
	return;
	
	// uniquement valide si la checkbox principale est checké
	if($('#tableCalculWizard3 input[type=checkbox]:first').prop('checked')){

		// variable de travail
		var s_Value = '';
		var s_noAssure = 'noAssure_';
		// Evénement lors d'un input 
		$('#tableCalculWizard3 input[type=text]').each(function(index,domElement){
			var s_currentInputId = domElement.id;
			var s_noLine = s_currentInputId.split('_')[1];
			if(s_currentInputId.indexOf(s_noAssure)==0){
				if(s_noLine=='0'){
					// 1ère ligne, récupération de la valeur
					s_Value = $('#'+s_currentInputId).val();
				}else{
					// ligne ++, application de la sélection
					$('#'+s_currentInputId).val(s_Value);
				}
			}
		});
	
	}	
}

/**
 * Répercution du changement de la ligne 0
 * sur les autres lignes, FIN DU DROIT
 */
function onMainFamilyFinDroitChange(){
	// uniquement valide si la checkbox principale est checké
	if($('#tableCalculWizard3 input[type=checkbox]:first').prop('checked')){

		// variable de travail
		var s_Value = '';
		var s_finDroit = 'finDroit_';
		// Evénement lors d'un input 
		$('#tableCalculWizard3 input[type=text]').each(function(index,domElement){
			var s_currentInputId = domElement.id;
			var s_noLine = s_currentInputId.split('_')[1];
			if(s_currentInputId.indexOf(s_finDroit)==0){
				if(s_noLine=='0'){
					// 1ère ligne, récupération de la valeur
					s_Value = $('#'+s_currentInputId).val();
				}else{
					// ligne ++, application de la sélection
					$('#'+s_currentInputId).val(s_Value);
				}
			}
		});
	
	}	
}
/**
 * Répercution du changement de la ligne 0
 * sur les autres lignes, DATE RECEPTION DEMANDE
 */
function onMainFamilyDateReceptionChange(){
	// uniquement valide si la checkbox principale est checké
	if($('#tableCalculWizard3 input[type=checkbox]:first').prop('checked')){

		// variable de travail
		var s_Value = '';
		var s_receptionDemande = 'receptionDemande_';
		// Evénement lors d'un input 
		$('#tableCalculWizard3 input[type=text]').each(function(index,domElement){
			var s_currentInputId = domElement.id;
			var s_noLine = s_currentInputId.split('_')[1];
			if(s_currentInputId.indexOf(s_receptionDemande)==0){
				if(s_noLine=='0'){
					// 1ère ligne, récupération de la valeur
					s_Value = $('#'+s_currentInputId).val();
				}else{
					// ligne ++, application de la sélection
					$('#'+s_currentInputId).val(s_Value);
				}
			}
		});
	
	}	
}

/**
 * Répercution du changement de la ligne 0
 * sur les autres lignes, DOCUMENT
 */
function onMainFamilyDocumentChange(){
	// uniquement valide si la checkbox principale est checké
	if($('#tableCalculWizard3 input[type=checkbox]:first').prop('checked')){

		// variable de travail
		var s_Value = '';
		var s_document = 'document_';

		// travail sur les select, cible typeDemande
		$('#tableCalculWizard3 select').each(function(index,domElement){
			var s_currentInputId = domElement.id;
			var s_noLine = s_currentInputId.split('_')[1];
			if(s_currentInputId.indexOf(s_document)==0){
				if(s_noLine=='0'){
					// 1ère ligne, récupération de la valeur
					s_Value = $('#'+s_currentInputId).val();
				}else{
					// ligne ++, application de la sélection
					$('#'+s_currentInputId+' option[value='+s_Value+']').prop('selected','selected');
				}
			}
		});		
	}
}
/**
 * Répercution du changement de la ligne 0
 * sur les autres lignes, NO CAISSE MALADIE
 */
function onMainFamilyCaisseChange(){
	// uniquement valide si la checkbox principale est checké
	if($('#tableCalculWizard3 input[type=checkbox]:first').prop('checked')){

		// variable de travail
		var s_Value = '';
		var s_noCaisse = 'noCaisse_';

		// travail sur les select, cible typeDemande
		$('#tableCalculWizard3 select').each(function(index,domElement){
			var s_currentInputId = domElement.id;
			var s_noLine = s_currentInputId.split('_')[1];
			if(s_currentInputId.indexOf(s_noCaisse)==0){
				if(s_noLine=='0'){
					// 1ère ligne, récupération de la valeur
					s_Value = $('#'+s_currentInputId).val();
				}else{
					// ligne ++, application de la sélection
					$('#'+s_currentInputId+' option[value='+s_Value+']').prop('selected','selected');
				}
			}
		});
	}
}

/**
 * Action par défaut une foix les revenus/taxations récupérées,
 * sélection automatique du bon revenu/taxation ou choix
 * prime moyenne
 */
function checkAutoLine() {
	//Coche le bon bouton radio suivant le type de demande
	if (type==ID_TYPE_SUBSIDE_PC) {
		// génération du click prime moyenne P
		$('#primeMoyenne_P').prop('checked','checked');
		$('#primeMoyenne_P').click();
	} else if (type==ID_TYPE_SUBSIDE_ASSISTE) {
//		 génération du click prime moyenne A
		$('#primeMoyenne_A').prop('checked','checked');
		$('#primeMoyenne_A').click();
	} else{
		// génération du click 1er revenu / taxation de la liste
		// dans tous les cas le premier radio bouton du tableau
		if ($('#tableCalculWizard2 input:radio:first').length > 0){			
			$('#tableCalculWizard2 input:radio:first').prop('checked','checked');
			$('#tableCalculWizard2 input:radio:first').click();
		}
	}
}

//------------------------------------------------------------
//Action de génération des subsides
//------------------------------------------------------------
function onClickGenerate(){

	// Set the value for the main String
	var s_allSubsidesParameters = '';
	var s_choixMembre = 'choixMembre_';
	var s_typeDemande = 'typeDemande_';
	var s_montantContribution = 'montantContribution_';
	var s_debutDroit = 'debutDroit_';
	var s_finDroit = 'finDroit_';
	var s_document = 'document_';
	var s_noCaisse = 'noCaisse_';
	var s_noAssure = 'noAssure_';
	var s_receptionDemande = 'receptionDemande_';
	
	var s_aLines = new Array;
	var i_selectedLine = 0;

	// Enregistrement des lignes checked dans s_aLines
	$('#tableCalculWizard3 input').each(function(index,domElement){
		var s_currentInputId = domElement.id;
		var s_noLine = s_currentInputId.split('_')[1];
		if(s_noLine!=0){
			if(s_currentInputId.indexOf(s_choixMembre)==0){
				if($('#'+s_currentInputId).prop('checked')){
					s_aLines[i_selectedLine]=s_noLine;
					i_selectedLine++;
				};
			}
		}
	});
	
	// Mise dans la ligne principale : uniquement les éléments sélectionnés
	for(iLine = 0; iLine<s_aLines.length;iLine++){
		
		var s_currentLine = s_aLines[iLine];
		
		var s_currentIdFamille= '';
		var s_currentTypeDemande = '';
		var s_currentMontantContribution = '';
		var s_currentMontantContribSansSuppl = '';
		var s_currentMontantSupplement = '';
		var s_currentDebutDroit = '';
		var s_currentFinDroit ='';
		var s_currentDocument = '';
		var s_currentCaisse = '';
		var s_currentNoAssure= '';
		var s_currentReceptionDemande = '';

		// travail sur les inputs (text et checkbox)
		$('#tableCalculWizard3 input').each(function(index,domElement){
			var s_currentInputId = domElement.id;
			var s_noLine = s_currentInputId.split('_')[1];
			if(s_noLine == s_currentLine){
				if(s_currentInputId.indexOf(s_debutDroit)==0){
					s_currentDebutDroit = $('#'+s_currentInputId).val();
				}else if(s_currentInputId.indexOf(s_finDroit)==0){
					s_currentFinDroit = $('#'+s_currentInputId).val();
				}else if(s_currentInputId.indexOf(s_choixMembre)==0){
					s_currentIdFamille = s_currentInputId.split('_')[2];
				}else if(s_currentInputId.indexOf(s_noAssure)==0){
					s_currentNoAssure = $('#'+s_currentInputId).val();
				}else if(s_currentInputId.indexOf(s_receptionDemande)==0){
					s_currentReceptionDemande = $('#'+s_currentInputId).val();
				}
			}
		});
		// travail sur les select
		$('#tableCalculWizard3 select').each(function(index,domElement){
			var s_currentInputId = domElement.id;
			var s_noLine = s_currentInputId.split('_')[1];
			if(s_noLine == s_currentLine){
				if(s_currentInputId.indexOf(s_typeDemande)==0){
					s_currentTypeDemande = $('#'+s_currentInputId).val();
				}else if(s_currentInputId.indexOf(s_montantContribution)==0){
					s_currentMontantContribution = $('#'+s_currentInputId+' option:selected').text();
					s_currentMontantContribSansSuppl = s_currentInputId.split('_')[3];
					s_currentMontantSupplement = s_currentInputId.split('_')[4];
				}else if(s_currentInputId.indexOf(s_document)==0){
					s_currentDocument = $('#'+s_currentInputId).val();
				}else if(s_currentInputId.indexOf(s_noCaisse)==0){
					s_currentCaisse = $('#'+s_currentInputId).val();
				}
			}
		});
		// String finale
		var s_currentSubside = idContribuable+'_;';
		s_currentSubside += s_currentIdFamille +'_;';
		s_currentSubside += year +'_;';
		s_currentSubside += s_currentTypeDemande +'_;';
		if(s_currentMontantContribution == 'R'){
			s_currentSubside += '0.0_;';
			s_currentSubside += '0.0_;';
			s_currentSubside += 'true_;';
		}else{
			if(s_currentMontantContribSansSuppl!='undefined'){
				s_currentSubside += s_currentMontantContribSansSuppl +'_;';
				s_currentSubside += s_currentMontantSupplement +'_;';
			}else{
				s_currentSubside += s_currentMontantContribution +'_;';
				s_currentSubside += '0.0_;';
			}
			s_currentSubside += 'false_;';
		}
		s_currentSubside += s_currentDebutDroit+'_;';
		s_currentSubside += s_currentFinDroit+'_;';
		s_currentSubside += s_currentDocument+'_;';
		s_currentSubside += s_currentCaisse+'_;';
		s_currentSubside += s_currentNoAssure+'_;';
		s_currentSubside += s_currentReceptionDemande+'_';
		
		if(iLine==0){
			s_allSubsidesParameters += s_currentSubside;
		}else{
			s_allSubsidesParameters += '|'+s_currentSubside;
		}
		
	}

	// Set the hidden input fields
	$('#calculs\\.idContribuable').val(idContribuable);
	$('#calculs\\.anneeHistorique').val(year);
	$('#calculs\\.typeDemande').val(type);
	$('#calculs\\.idRevenu').val(idRevenuCalcul);
	$('#calculs\\.revenuIsTaxation').val(revenuIsTaxationCalcul);
	$('#calculs\\.allSubsidesAsString').val(s_allSubsidesParameters);

	// Set the hidden input fields cas détail famille
	$('#detailfamille\\.calculs\\.idContribuable').val(idContribuable);
	$('#detailfamille\\.calculs\\.anneeHistorique').val(year);
	$('#detailfamille\\.calculs\\.typeDemande').val(type);
	$('#detailfamille\\.calculs\\.idRevenu').val(idRevenuCalcul);
	$('#detailfamille\\.calculs\\.revenuIsTaxation').val(revenuIsTaxationCalcul);
	$('#detailfamille\\.calculs\\.allSubsidesAsString').val(s_allSubsidesParameters);

	
	// Set value for simulation subside
	userAction.value = ACTION_DETAILFAMILLE+".generateSubside";

	if (idRevenuCalcul.length == 0) {
		idRevenuCalcul = "0";
	}
	
	if (revenuIsTaxationCalcul.length == 0) {
		revenuIsTaxationCalcul = "false";
	} 
	
	var a_params = 
			idContribuable+","+
			year+","+
			type+","+
			idRevenuCalcul+","+
			revenuIsTaxationCalcul+","+
			s_allSubsidesParameters;
	
	var o_options= {
		serviceClassName: 'ch.globaz.amal.business.services.models.detailfamille.DetailFamilleService',
		serviceMethodName:'generateSubsidesFromAttribution',
		parametres:a_params,
		callBack: callBackGenerateSubside,
		errorCallBack: callBackGenerateSubsideOnError
	}

	globazNotation.readwidget.options=o_options;		
	globazNotation.readwidget.read();		
}

function callBackGenerateSubside(data) {
	window.location.reload();
	hideProgress('#inProgressDialog');
}

function callBackGenerateSubsideOnError(jqXHR, textStatus, errorThrown) {
	hideProgress('#inProgressDialog');
	ajaxUtils.displayError(jqXHR);
}

/**
 * Désactivation d'un champ
 * @param fieldName
 * @param wantDisabled
 */
function disabledField(fieldName, wantDisabled) {
	$("#"+fieldName).prop("disabled",wantDisabled);
	$("#"+fieldName).prop("readonly",wantDisabled);
}

/**
 * Fonction de formatage d'un nombre (xx'xxx.xx)
 * @param nStr
 * @param wantBlankIfDecimalZero
 * @returns
 */
function formatNumber(nStr, wantBlankIfDecimalZero)
{
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + '\'' + '$2');
	}

	if (wantBlankIfDecimalZero) {
		if (x1=='undefined') {
			x1 = '0';
		}
		return x1 + ".00";
	} else {
		return x1 + x2;
	}
}