var cat10;
var cat20;

$(function () {
	var idDecision = $('#decisionCap\\.idDecision').val();
	
	//initialisation ajax
	runAjax.init({
		s_actionAjax: "auriga.decisioncap.decisionCapAjax",
		s_entityIdPath: "decisionCap.idDecision",	
		s_actionCancelback: 'auriga?userAction=auriga.decisioncap.decisionCapSearch.afficherCapSearch',
		b_validateNotation: true,
		b_hasButtonNew: false,
		n_idEntity:  globazGlobal.selectedID,
		f_afterUpdate: function (data) {
			var idDecision = $('#decisionCap\\.idDecision').val();
			var idAffilie = $('#decisionCap\\.idAffiliation').val();
			var idDecisionCapRectifiee = $('#decisionCap\\.idDecisionRectifiee').val();;
			window.top.fr_main.window.location="auriga?userAction=auriga.decisioncap.decisionCap.afficher&amp;idAffilie=" + idAffilie +"&amp;id=" + idDecision+"&amp;idDecisionCapRectifiee="+idDecisionCapRectifiee +"";
		}
	});
		
	//affichage des champs en fonction de la catégorie. Si mode création, les champs spéciaux sont cachés
	updateDisplayFieldsForCategorie();
	
	if(idDecision!=null && idDecision.length!=0){
		$('.deleteImg').hide();
		$('.widgetLine').hide();
		
		$('.btnAjaxUpdate').click(function(){
			$('.deleteImg').show();
			$('.widgetLine').show();
			$('.widgetTiers').removeAttr("disabled");
			$('#printDoc').hide();
		});
		
		$('.btnAjaxCancel').click(function(){
			$('.deleteImg').hide();
			$('.widgetLine').hide();
			$('#printDoc').show();
			location.reload();
		});
	}else{
		$('#printDoc').hide();
	}
	
	$('#enfantsTable').on('click','.deleteImg',function(){
		var idTiersToDelete = $(this).closest('tr').attr('id').split('_')[1];
		var listIdEnfants = $('#listIdEnfants').val();
		var tabIdEnfants = listIdEnfants.split(',');
		var key;
		var newListIdEnfants="";
		
		for(var i=0;i<tabIdEnfants.length;i++){
			if(tabIdEnfants[i]==idTiersToDelete){
				key=i;
			}
		}
		
		deleteElement(tabIdEnfants,key);
		
		for(var i=0;i<tabIdEnfants.length;i++){
			if(i==0){
				newListIdEnfants = tabIdEnfants[i];
			}else{
				newListIdEnfants = newListIdEnfants+","+tabIdEnfants[i];
			}
		}
		
		$('#listIdEnfants').val(newListIdEnfants);
		$('#enfantsTable tbody #idTiersDecision_'+idTiersToDelete).remove();
	});
	
	function deleteElement(Tab,a){
		return (a>Tab.length)?false:(Tab.splice(a,1));
	}
});

function getParamDynamique() {
	return $('#decisionCap\\.idDecision').val();
}

function hideAllSpecialFields(){
	$('.revIFD').hide();
	$('.revFRV').hide();
	$('.tauxAssurance').hide();
	$('.cotBrute').hide();
	$('.forfait').hide();
	$('.allocationFamiliale').hide();
}

function updateDisplayFieldsForCategorie(){
	hideAllSpecialFields();
	var categorie = $('#decisionCap\\.categorie').val();
	if(categorie!=null && categorie.length!=0){
		// si catégorie 10
		if(categorie==cat10){
			$('.revIFD').show();
			$('.revFRV').show();
			$('.tauxAssurance').show();
			$('.cotBrute').show();
			$('.allocationFamiliale').show();
		}
		// si catégorie 20
		else if(categorie==cat20){
			$('.forfait').show();
			$('.allocationFamiliale').show();
		}
		//si autre catégorie 
		else{
			$('.forfait').show();
		}
	}else{
		hideAllSpecialFields();
	}
}

function updateDateDebutAndDateFin(dateDebutCoti, dateFinCoti){
	var annee = $('#decisionCap\\.annee').val();
	if(annee!=null && annee.length==4){
		var $_dateDebut = $('#decisionCap\\.dateDebut');
		var dateDebut = "01.01."+annee;
		if(compareDates(dateDebut,"dd.MM.yyyy",dateDebutCoti,"dd.MM.yyyy")==0){
			dateDebut=dateDebutCoti;
		}
		$_dateDebut.val(dateDebut);
		
		var $_dateFin = $('#decisionCap\\.dateFin');
		var dateFin = "31.12."+annee;
		if(compareDates(dateFin,"dd.MM.yyyy",dateFinCoti,"dd.MM.yyyy")==1){
			dateFin=dateFinCoti;
		}
		$_dateFin.val(dateFin);
	}
}
