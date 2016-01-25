

function DessaisissementRevenuPart(container){
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
		this.detail.find('.libelle').val($data.find('libelle').text()).end()
			.find('.motifAutre').val($data.find('motifAutre').text()).end()
			.find('.montantBrutDessaisi').val($data.find('montantBrutDessaisi').text()).end()
			.find('.montantDeductions').val($data.find('montantDeductions').text()).change().end()
			.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
			.find('[name=dateFin]').val($data.find('dateFin').text()).end();
		this.addSpy($data);
	};
			
	this.getParametres=function(){
		return {
			'dessaisissementRevenu.simpleDessaisissementRevenu.libelleDessaisissement':this.detail.find('.libelle').val(),
			'dessaisissementRevenu.simpleDessaisissementRevenu.montantBrut':this.detail.find('.montantBrutDessaisi').val(),
			'dessaisissementRevenu.simpleDessaisissementRevenu.deductionMontantDessaisi':this.detail.find('.montantDeductions').val(),
			'dessaisissementRevenu.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'dessaisissementRevenu.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'doAddPeriode': this.doAddPeriode,		
			'idDroitMembreFamille':this.membreId
		};
	};
	
	this.clearFields=function(){
		 this.detail.find('.libelle,.montantBrutDessaisi,.montantDeductions,[name=dateDebut],[name=dateFin]').val('');		
	};
	
	this.getParentViewBean=function(){
		return droit;	
	};
	
	this.setParentViewBean=function(newViewBean){
		droit=newViewBean;
	}

	this.formatTableTd=function($elementTable){
		$elementTable.find('td:eq(1),td:eq(2),td:eq(3)').css('text-align','right');
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

//DessaisissementRevenuPart extends AbstractSimpleAJAXDetailZone
DessaisissementRevenuPart.prototype=AbstractScalableAJAXTableZone;


//fonction d'initialisation de la page lorsque JQuery est prêt
$(function(){
	
	$('.areaMembre').each(function(){
		
		var $that=$(this);
		var zone=new DessaisissementRevenuPart($that);
		this.zone=zone;
		zone.membreId=$(this).attr('idMembre');
		
		
		
		$that.find('.btnAjaxUpdate').click(function(){
			zone.startEdition();
			$that.find('.btnAjaxValidateNouvellePeriode').show();
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
		}).end()	
		.find('.btnAjaxValidateNouvellePeriode').click(function () {
			zone.doAddPeriode = true;
			zone.validateEdition();
		}).end();
	});
});