

function DessaisissementFortunePart(container){
	// variables
	var that=this;
	this.ACTION_AJAX=ACTION_AJAX;
	this.mainContainer=container;
	this.table=this.mainContainer.find(".areaDFDataTable");
	this.tableAuto=this.mainContainer.find(".areaDessaisiAuto");
	this.detail=this.mainContainer.find(".areaDFDetail");
	this.titleContainer=this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass="areaDFModified";
	this.membreId=null;

	// functions
		
	this.afterRetrieve=function($data){
		var contrePrestation=($data.find('contrePrestation').text()=='true');
		this.detail.find('.csMotif').val($data.find('csMotif').text()).change().end()
			.find('.motifAutre').val($data.find('motifAutre').text()).end()
			.find('.montantBrutDessaisi').val($data.find('montantBrutDessaisi').text()).end()
			.find('.montantDeductions').val($data.find('montantDeductions').text()).change().end()
			.find('.charges').val($data.find('charges').text()).change().end()
			.find('.rendementFortune').val($data.find('rendementFortune').text()).change().end()
			.find('.contrePrestation').attr('checked',contrePrestation).end()
			.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
			.find('[name=dateFin]').val($data.find('dateFin').text()).end()
			.find('.resultatCalcul').text($data.find('totalArrondi').text()).end();
		this.ajoutValeurCalcul($data);
	};
	
	this.ajoutValeurCalcul = function($data){
		
		$calculContrePrestation = $data.find('calculContrePrestation');
		var typeValeur = $calculContrePrestation.find('typeValeur').text();
		if ($calculContrePrestation.length) {
			$calcul =$('.calculContrePrestation');
			this.detail.find('.ligneAfficheCalcul').show();
			$calcul.find('.dateDebut').text($data.find('dateDebut').text());
			$calcul.find('.montantBrutDessaisi').text($data.find('montantBrutDessaisi').text());
			$calcul.find('.montantDeductions').text($data.find('montantDeductions').text());
			$calcul.find('.rendementFortune').text($data.find('rendementFortune').text());
			$calcul.find('.charges').text($data.find('charges').text());
			
			var facteurUtilise = $calculContrePrestation.find('[utilise=true]')[0].nodeName;
			var $facteurUtilise = $calcul.find('.' + facteurUtilise);
			
			$calcul.find('.facteur_H').text($calculContrePrestation.find('facteur_H').text()).end().attr;
			$calcul.find('.facteur_F').text($calculContrePrestation.find('facteur_F').text());
			$('#facteurUtilise').remove();
			$('<span id="facteurUtilise" class="ui-icon ui-icon-check" style="float:left"> </span>').prependTo($facteurUtilise.prev());
			
			$calcul.find('#facteur .valeur').not('.' + facteurUtilise).each(function(){
				$('<span id="facteurUtiliseNone" class="icon-none" style="float:left"> </span>').prependTo($(this).prev());
			});
			
			$facteurUtilise.addClass('facteurUtilise');
			$calcul.find('.montantNetDuBien').text($calculContrePrestation.find('montantNetDuBien').text());
			$calcul.find('.rendementNet').text($calculContrePrestation.find('rendementNet').text());
			$calcul.find('.rendementNetAvecFateur').text($calculContrePrestation.find('rendementNetAvecFateur').text());
			$calcul.find('.resultatCalcul').text($calculContrePrestation.find('resultatCalcul').text());
			
			$('#textCalcul').text($calculContrePrestation.find('rendementNet').text() + " x " + $facteurUtilise.text());
			/*if(typeValeur !== CS_TYPE_VALEUR_AFC){
				$('#textCalcul').text($calculContrePrestation.find('rendementNet').text() + " x 1'000.00 / " + $facteurUtilise.text());
			}else{
				$('#textCalcul').text($calculContrePrestation.find('rendementNet').text() + " x " + $facteurUtilise.text());
			}*/
			$calcul.find('.totalArrondi').text($calculContrePrestation.find('totalArrondi').text());
			
		}else{
			this.detail.find('.ligneAfficheCalcul').hide();
		}
		
	};
			 
	this.getParametres=function(){
		return {
			'dessaisissementFortune.simpleDessaisissementFortune.csMotifDessaisissement':this.detail.find('.csMotif').val(),
			'dessaisissementFortune.simpleDessaisissementFortune.autreMotifDessaisissement':this.detail.find('.motifAutre').val(),
			'dessaisissementFortune.simpleDessaisissementFortune.montantBrut':this.detail.find('.montantBrutDessaisi').val(),
			'dessaisissementFortune.simpleDessaisissementFortune.deductionMontantDessaisi':this.detail.find('.montantDeductions').val(),
			'dessaisissementFortune.simpleDessaisissementFortune.isContrePrestation':this.detail.find('.contrePrestation').prop('checked'),
			'dessaisissementFortune.simpleDessaisissementFortune.charges':this.detail.find('.charges').val(),
			'dessaisissementFortune.simpleDessaisissementFortune.rendementFortune':this.detail.find('.rendementFortune').val(),
			'dessaisissementFortune.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'dessaisissementFortune.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'idDroitMembreFamille':this.membreId
		};
	};
		
	this.clearFields=function(){
		/* this.detail.find('.csMotif,.montantBrutDessaisi,.motifAutre,.montantDeductions,[name=dateDebut],[name=dateFin]').val('').end()
					.find('.isContrePrestation').attr('checked',false).end();*/
					
		this.detail.clearInputForm();
		this.detail.find('.resultatCalcul').text('').end().find('.contrePrestation').attr('checked',false);
		this.detail.find('.ligneAfficheCalcul').hide();
		var $calc=$('.calculContrePrestation');
		$calc.find('.dateDebut,.valeur,.resultat,.resultatCalculFinal,.totalArrondi').text('');
	};
	
	this.getParentViewBean=function(){
		return droit;	
	};
	
	this.setParentViewBean=function(newViewBean){
		droit=newViewBean;
	}

	
	
	this.formatTableTd=function($elementTable){
		$elementTable.find('td:eq(1),td:eq(2)').css('text-align','right');
		$elementTable.find('td:eq(3)').css('text-align','center');
		$elementTable.find('td:eq(4)').css('text-align','left');
	}
	
	this.formatTable=function(){
		var $tableAutoTr = this.tableAuto.find('tbody tr');
		if($tableAutoTr.length==0){
			var nbcol=this.tableAuto.find('thead td').length;
			var $line=$('<tr/>').css({
				"line-height":"5px",
				"background-color":"white"
			});
			for(var i=0;i<nbcol;i++){
				// create new cell
				$line.append('<td>&#160;</td>');
			}
			// add line
			this.tableAuto.find('tbody').append($line);
		} 
		
 
		this.formatTableTd(this.$trInTbody);

		$tableAutoTr.find('td:eq(2)').css('text-align','right');
		$tableAutoTr.filter(':odd').addClass('odd');
	}
	
	// initialization
	this.init(
	    function(){	
	    	this.stopEdition();
	    	this.onAddTableEvent();
	    	this.colorTableRows(false);
		}
	);
}

//DessaisissementFortunePart extends AbstractSimpleAJAXDetailZone
DessaisissementFortunePart.prototype=AbstractScalableAJAXTableZone;


//fonction d'initialisation de la page lorsque JQuery est prêt
$(function(){

    var $dialogue = $('.calculContrePrestation');
	$dialogue.dialog({
			resizable: false,
			autoOpen : false,
			modal: true,
			width : 520,
			buttons: {
				'ok': function() {
					$(this).dialog('close');
				}
			}
		});	
	
	$('.areaMembre').each(function(){
		
		var $that=$(this);
		var zone=new DessaisissementFortunePart($that);
		this.zone=zone;
		zone.membreId=$(this).attr('idMembre');
		
		$that.find('.afficheCalcul').click(function(){
			$dialogue.dialog("open");
		}).button();
		
		$that.find('.btnAjaxUpdate').click(function(){
			zone.startEdition();
			$that.find('.afficheCalcul').attr('disabled',false);
		}).end()
		.find('.btnAjaxCancel').click(function(){
			zone.stopEdition();
		}).end()
		.find('.btnAjaxValidate').click(function(){
			zone.validateEdition();
		}).end()
		.find('.btnAjaxDelete').click(function(){
				zone.ajaxDeleteEntity(zone.selectedEntityId);
		}).end()
		.find('.btnAjaxAdd').click(function(){
				zone.stopEdition();
				zone.startEdition();
		}).end();
		
		$that.find('.csMotif').change(function(){
			if(this.options[this.selectedIndex].value=='64041010'){
				$that.find('.motifAutreDisplay').show();
			}else{
				$that.find('.motifAutreDisplay').hide();
			}
		}).click(function(){$(this).change()}).change();
		
	});
});