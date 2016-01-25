/*
 * Scripts pour les écrans Rente avs ai
 * SCE, 6.2010
 */

/**
 * 
 * @return
 */
function AllocationImpotentPart(container){
	// variables
	var that=this;
	
	this.typesAPIVieillesse=['64012005','64012006','64012007','64012012','64012013','64012014','64012015'];
	this.typesAPIInvalidite=['64012001','64012002','64012003','64012004','64012008','64012009','64012010','64012011'];
	
	this.ACTION_AJAX=ACTION_AJAX_DONNEE_FINANCIERE;
	this.mainContainer=container;
	this.table=this.mainContainer.find(".areaDFDataTable");
	this.detail=this.mainContainer.find(".areaDFDetail");
	this.titleContainer=this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass="areaDFModified";
	this.membreId=null;
	this.$champTypeAPI=this.mainContainer.find('[name=champTypeApi]');
	this.$champGenreAPI=this.mainContainer.find('[name=champGenreApi]');
	
	// functions
	this.onChangeType=function(){
		var typeAPI=that.$champTypeAPI.find('option:selected').val();
		if(that.typesAPIVieillesse[typeAPI]){
			that.$champGenreAPI.val('64015001');
			that.mainContainer.find('.degreApi').hide();
		}else if(that.typesAPIInvalidite[typeAPI]){
			that.$champGenreAPI.val('64015002');
			that.mainContainer.find('.degreApi').show();
			
		}
	};
	
	this.$champTypeAPI.on("change",this.onChangeType);
	this.$champGenreAPI.on("change",this.onChangeType);
	
	this.afterRetrieve=function($data){
		 this.detail.find('.montant').val($data.find('montant').text()).change().end()
					.find('.typeApi').val($data.find('csTypeRente').text()).end()
					.find('.genreApi').val($data.find('csGenre').text()).end()
					.find('.degreApi').val($data.find('csDegre').text()).end()
					.find('[name=dateDepot]').val($data.find('dateDepot').text()).end()
					.find('[name=dateDecision]').val($data.find('dateDecision').text()).end()
					.find('.dessaisissementRevenu').attr('checked',$data.find('DR').text()=='true').end()
					.find('[name=dateDebut]').val($data.find('dateDebut').text()).end()
					.find('[name=dateFin]').val($data.find('dateFin').text()).end();
		 this.addSpy($data);
	};
	
	this.getParametres=function($data){
		return {
			'allocationImpotent.simpleAllocationImpotent.montant':this.detail.find('.montant').val(),
			'allocationImpotent.simpleAllocationImpotent.csTypeRente':this.detail.find('.typeApi').val(),
			'allocationImpotent.simpleAllocationImpotent.csGenre':this.detail.find('.genreApi').val(),
			'allocationImpotent.simpleAllocationImpotent.csDegre':this.detail.find('.degreApi').val(),
			'allocationImpotent.simpleAllocationImpotent.dateDepot':this.detail.find('[name=dateDepot]').val(),
			'allocationImpotent.simpleAllocationImpotent.dateDecision':this.detail.find('[name=dateDecision]').val(),
			'allocationImpotent.simpleDonneeFinanciereHeader.isDessaisissementRevenu':this.detail.find('.dessaisissementRevenu').prop('checked'),
			'allocationImpotent.simpleDonneeFinanciereHeader.dateDebut':this.detail.find('[name=dateDebut]').val(),
			'allocationImpotent.simpleDonneeFinanciereHeader.dateFin':this.detail.find('[name=dateFin]').val(),
			'doAddPeriode':this.doAddPeriode,
			'idDroitMembreFamille':this.membreId
		};
	};	
	
	this.clearFields=function(){
		 this.detail.find('.montant,.typeApi,.genreApi,.degreApi,[name=dateDepot],[name=dateDecision],.dessaisissementRevenu,[name=dateDebut],[name=dateFin]').val('').end()
		 //$('.typeApi').get(0).selectedIndex = 0;				
		
	};
	
	this.getParentViewBean=function(){
		return droit;	
	};
	
	this.setParentViewBean=function(newViewBean){
		droit=newViewBean;
	};
	this.infoWarnRetro = function () {
		var $infoWarnRetro = this.detail.find(".infoWarnRetro");
		var $dateDecision =  this.detail.find('[name=dateDecision]');
		var n_noVersion = parseInt($("#infoDroitNoVersion").text(), 10);
		var s_infoDroitDateAnnonce = $("#infoDroitDateAnnonce").text();
		$infoWarnRetro.hide();
		if (n_noVersion === 1) {
			$dateDecision.change(function () {				
				if(globazNotation.utilsDate.convertGlobazYearDateToJSDate(s_infoDroitDateAnnonce)!=null &&
						globazNotation.utilsDate.convertGlobazYearDateToJSDate($dateDecision.val())!=null &&
						globazNotation.utilsDate.countMonth(s_infoDroitDateAnnonce,$dateDecision.val()) <= 6){
					$infoWarnRetro.show("fast");
				} else {
					$infoWarnRetro.hide("fast");
				}
			});
		}
	};

	this.formatTableTd=function($elementTable){
		$elementTable.find('td:eq(5),td:eq(6)').css('text-align','left').end()//pour les dates, align a gauche
		.find('td:eq(1)').css('text-align','right').end(); //pour montant a droite			
		//.find('td:eq(8)').attr('noWrap','noWrap').css('text-align','left');//périodedroite*/
	},
	
	// Formatatage de la table
	
	// initialization
	this.init(
	    function(){	
			this.stopEdition();
			this.onAddTableEvent();
			this.colorTableRows(false);
			$("html").bind(eventConstant.NOTATION_MANAGER_DONE,function (){
				that.infoWarnRetro(); 
			});
			
		}
	);
		
}

AllocationImpotentPart.prototype=DonneeFinancierePart.prototype;


$(function(){	
	$('.areaMembre').each(function(){
		var $that=$(this);
		var zone=new AllocationImpotentPart($(this));
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
		.find('.btnAjaxValidateNouvellePeriode').click(function(){
			zone.doAddPeriode=true;
			zone.validateEdition();
		}).end();
		
	});
});