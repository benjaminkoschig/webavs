$(function () {
	runAjax.init({
		s_actionAjax: "aries.decisioncgas.decisionCgasAjax",
		s_spy: "decisionCGASBean.decisionCGAS",	
		s_entityIdPath: "decisionCGASBean.decisionCGAS.idDecision",	
		s_actionCancelback: 'aries?userAction=aries.decisioncgas.decisionCgasSearch.afficherCgasSearch',
		b_validateNotation: true,
		b_hasButtonNew: false,
		n_idEntity:  globazGlobal.selectedID,
		onError: function(data){
			$('#printDoc').hide();
		}
	});
	
	//mise à jour des dates de début et fin en fonction de l'année saisie. 
	$('#decisionCGASBean\\.decisionCGAS\\.annee').blur(function(){
		var annee = $(this).val();
		if(annee!=null && annee.length==4){
			var $_dateDebut = $('#decisionCGASBean\\.decisionCGAS\\.dateDebut');
			if($_dateDebut.val()==null || $_dateDebut.val().length==0){
				$_dateDebut.val("01.01."+annee);
			}
			
			var $_dateFin = $('#decisionCGASBean\\.decisionCGAS\\.dateFin');
			if($_dateFin.val()==null || $_dateFin.val().length==0){
				$_dateFin.val("31.12."+annee);
			}
		}
	});
	
	var idDecision = $('#decisionCGASBean\\.decisionCGAS\\.idDecision').val();
	if(idDecision==null || idDecision.length==0){
		$('#printDoc').hide();
	}
	
	$('.btnAjaxUpdate').click(function(){
		$('#printDoc').hide();
	});
	
	$('.btnAjaxCancel').click(function(){
		$('#printDoc').show();
	});
	
	$('.btnAjaxValidate').click(function(){
		$('#printDoc').show();
	});
});

function getParamDynamique() {
	return $('#decisionCGASBean\\.decisionCGAS\\.idDecision').val();
}













