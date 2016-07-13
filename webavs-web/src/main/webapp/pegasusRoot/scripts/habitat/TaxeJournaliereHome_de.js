/**************************************************************
 * Script des comportements de l'écran loyer
 * 
 * DMA
 */


function add() {}

function upd() {}	

function validate() {}    

function del() {}

function cancel() {}

function init(){}

function postInit(){
	$('.btnAjax input').attr('disabled',false);	
}


$(function(){
	
	
	$('.detailPrixChambres').live('click',function () {
		
		$that = $(this);
		//Si la dialog n'existe pas on l'ajoute
		if(!$('#listePrixDialog').length){
			
			var idHome = $that.attr('data-id-home');
			var idTypeChambre = $that.attr('data-id-chambre');
			var dateDebut = $that.attr('data-dateDebut');
			var dateFin = $that.attr('data-dateFin');
			
			
			
			if(dateFin === '' || dateFin === undefined){
				dateFin = '0';
			}
		
			
			var $dialog = $('<div/>',{
				 id: 'listePrixDialog',
				 title: dialogTitre,
				 style : 'display:none'
			}).appendTo('.conteneurDF');
			
			$('<table/>',{
				id:		'chmabreDesc',
				html:	'<tr><td><span style="font-weight:bold;">' + dialogHomeLibelle +'</span></td><td><span id="homeDesc">[homeDesc]</span></td></tr>'
					+ '<tr><td><span style="font-weight:bold;">' + dialogTypeChambreLibelle +'</span></td><td><span id="typeChambreDesc">[typeChambreDesc]</span></td></tr>'
			}).appendTo($dialog);
			
			$('<table/>',{
				id:		'listePrix',
				html:	''
	
			}).appendTo($dialog);
			
			
			//on supprime l'élément du dom
			 $dialog.on('dialogclose', function (event, ui) {
				 $dialog.remove();
			});
			
			
			$dialog.dialog({
				modal: false,
				width: 500,
				resizable: false,
				open: function (openevent){
					findMontantTypeChambre(idHome,idTypeChambre, dateDebut, dateFin)
			    	
				} 
			 });
		}
			
	});
	
	var findMontantTypeChambre = function (idHome, idTypeChambre, dateDebut, dateFin) {
		
		
		
		var options = {
			serviceClassName: 'ch.globaz.pegasus.business.services.models.home.HomeService',
			serviceMethodName: 'getListePrixChambres',
			parametres: idHome+","+idTypeChambre+","+dateDebut+","+dateFin,
			callBack: function (data){
				fillPopUp(data); 
			}
		};
		globazNotation.readwidget.options = options;
		globazNotation.readwidget.read();
	}
	
	var fillPopUp = function (data) {
		
		
		var $listePrixTable = $('#listePrix');
		

		var homeDesc = data.libelleHome;
		var typeChambreDesc = data.libelleChambre;
		
		$('#homeDesc').html(homeDesc);
		$('#typeChambreDesc').html(typeChambreDesc);
		
		
		var isWithMontantAdmis = data.withMontantAdmis;
		
		//en tete du tableau
		if(isWithMontantAdmis){
			$listePrixTable.html('<tr>'
					+ '<td><span style="font-weight:bold;">' + dialogPeriodeLibelle + '</span></td>'
					+ '<td><span style="font-weight:bold;">' + dialogPrixJourLibelle + '</span></td>'
					+ '<td><span style="font-weight:bold;">' + dialogMontantLibelle + '</span></td></tr>')
		}else{
			$listePrixTable.html('<tr>'
					+ '<td><span style="font-weight:bold;">' + dialogPeriodeLibelle + '</span></td>'
					+ '<td><span style="font-weight:bold;">' + dialogPrixJourLibelle + '</span></td></tr>');
		}
		
		for(var montantId in data.montantsPeriode){
			var montant = data.montantsPeriode[montantId];
			
			var ligne = '<tr><td width="180px"> ' + montant.dateDebut + ' - ' + montant.dateFin + '</td><td style="text-align:right">' + montant.prixChambre.montant + '</td>';
			
			if(isWithMontantAdmis){
				ligne += '<td style="text-align:right">' + montant.montantPlafond.montant + '</td>';
			}
			
			ligne += '</tr>';
			
			
			$listePrixTable.append(ligne);
		}
	}
});

