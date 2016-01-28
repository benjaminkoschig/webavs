
$(function (){
	defaultTableAjax.init({
		s_actionAjax: globazGlobal.ACTION_AJAX,
		b_hasButtonNew: false,	
		
		init: function () {			//Permet de définir le comportement de la zone ajax
			
			this.list();	// Définit le comportement d'une capage
			this.addSearch();
		},
		getParametresForFind : function () {
			
			return {"idTiers": globazGlobal.idTiers
			};
		}
	});
	
	//lien détail
	$('.avanceEntity').live('click', function () {
		//alert($(this).attr('idEntity'));
		goToDetailAvance($(this).attr('idEntity'));
	});
	
	//nouvelle avance
	$('#newAvance').button();
	
	$('#newAvance').click(function () {
		
	});
});