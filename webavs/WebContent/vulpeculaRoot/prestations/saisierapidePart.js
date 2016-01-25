var GLO = GLO || {};

$(function() {	
	GLO.saisieRapide.init();
});

function add () {
}

function upd() {
}

function cancel() {
}

function del() {
}

function init(){
}

function validate() {
}

function postInit() {
	$('select,input').removeAttr('disabled');
}

function hasRights(idTravailleur){
	var hasRights = false;
	var options = {
			serviceClassName:globazGlobal.usersService,
			serviceMethodName:'hasRightAccesSecurityForTravailleur',
			parametres: idTravailleur + ',',
			callBack:function (data) {
				hasRights = data;
			}
	};
	vulpeculaUtils.lancementServiceSync(options);
	return hasRights;
}

GLO.mustBeShow = true;

GLO.saisieRapide = {
		$frame : null,
		init : function() {
			$frame = $('#frame');
			GLO.saisieRapide.bindEvents();
			$frame.hide();
			this.changeFrame();
		},
		
		bindEvents : function() {
			
			$(document).on("ajoutSucces",function(){
				GLO.mustBeShow = false;
				$frame.hide();
				$('#descriptionTravailleur').focus();
				$('#idTravailleur').val("");
				$('#descriptionTravailleur').val("");
				$('#showSucces').noty({
					type: 'success',
					timeout: 3000,
					maxVisible : 3,
					text: globazGlobal.ajouterSuccesLibelle});
			});
			
			$frame.on('load',function() {
				if(GLO.mustBeShow) {
					$frame.show();
				} else {
					GLO.mustBeShow = true;
				}
			});
			
			$('#genrePrestation').change(function() {
				$frame.hide();
				var $this = $(this);
				globazGlobal.services.loadPrestationsInfo($this.val(), function(data) {
					$('#idPassageFacturation').text(data.passage.id);
					$('#nomPassageFacturation').text(data.passage.libelle);
					var options = "";
					for(var i=0;i<data.beneficiaires.length;i++) {
						var beneficiaire = data.beneficiaires[i];
						options += '<option value="'+beneficiaire.id+'">'+beneficiaire.code+' - '+beneficiaire.libelle+'</option>';
					}
					$('#beneficiaire').empty().append(options);
				});
				
				if($('#idTravailleur').val().length > 0){
					globazGlobal.services.checkAuMoinsUnPostePourPrestationEtTravailleur($('#genrePrestation').val(), $('#idTravailleur').val(), function(data) {
						if(data){
							GLO.saisieRapide.changeFrame();
						}
					});
				}
			});
			
			$('#idTravailleur').change(function() {
				if($('#idTravailleur').val().length > 0){
					var options = {
							serviceClassName:globazGlobal.prestationsViewService,
							serviceMethodName:'getNumeroCompteForTravailleur',
							parametres:$('#idTravailleur').val() + ',',
							callBack:function (data) {
								$('#adressePaiementTravailleur').text(data);
							}
					};
					vulpeculaUtils.lancementService(options);
					
					globazGlobal.services.checkAuMoinsUnPostePourPrestationEtTravailleur($('#genrePrestation').val(), $('#idTravailleur').val(), function(data) {
						if(data){
							GLO.saisieRapide.changeFrame();
						}
					});
				}
			});
			
			$frame.focus(function (event) {
				var $that = $(this);
				var focusSelected = $that.contents().find("select:focus,input:focus");
				if(focusSelected.length===0) {
					var $firstNextElement = $that.contents().find('select,input').filter(':visible:first'); 
					$firstNextElement.focus();
				}
			});
			
			$('#descriptionTravailleur').change(function(){
				if($('#descriptionTravailleur').val().length <= 0){
					$('#idTravailleur').val('');
					$('#frame').hide();
				}
			});
		},
		
		changeFrame : function(){
			var iFrameSrc = "";
			var $genrePrestation = $('#genrePrestation');
			var $frame = $('#frame');
			if(globazGlobal.genrePrestationAJ == $genrePrestation.val()){
				iFrameSrc = "vulpecula?userAction=vulpecula.absencesjustifiees.absencesjustifieesnotitle.afficher&_method=add&idTravailleur=";
			}
			
			if(globazGlobal.genrePrestationCP == $genrePrestation.val()){
				iFrameSrc = "vulpecula?userAction=vulpecula.congepaye.congepayenotitle.afficher&_method=add&idTravailleur=";
			}
			
			if(globazGlobal.genrePrestationSM == $genrePrestation.val()){
				iFrameSrc = "vulpecula?userAction=vulpecula.servicemilitaire.servicemilitairenotitle.afficher&_method=add&idTravailleur=";
			}				
			
			var idTravailleur = $("#idTravailleur").val();
			
			if(idTravailleur.length > 0){
				iFrameSrc += idTravailleur;
				$frame.attr("src", iFrameSrc);
			}
		}
};