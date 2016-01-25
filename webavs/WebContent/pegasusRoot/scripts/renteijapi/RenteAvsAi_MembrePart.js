/*
 * Scripts pour les écrans Rente avs ai
 * SCE, 6.2010
 */



function RenteAvsAiPart(container) {
	// variables
	var that = this;
	var csRoleEnfant = '64004003';
	this.typesRenteAvecAI=['64006015','64006032','64006033','64006023','64006016','64006017','64006018','64006034','64006035','64006024','64006019','64006020','64006036'];
	this.typeRentesAvecAiEnfant = ['64006015','64006018'];
	
	this.ACTION_AJAX = ACTION_AJAX_DONNEE_FINANCIERE;
	this.mainContainer = container;
	this.table = this.mainContainer.find(".areaDFDataTable");
	this.detail = this.mainContainer.find(".areaDFDetail");
	this.titleContainer = this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass = "areaDFModified";
	this.membreId = null;
	this.s_spy = "renteAvsAi.simpleDonneeFinanciereHeader";

	this.$inputTypePc=this.mainContainer.find('[name=renteAvsAi\\.simpleRenteAvsAi\\.csTypePc]');
	this.$inputTypeRente=this.mainContainer.find('[name=renteAvsAi\\.simpleRenteAvsAi\\.csTypeRente]');
	this.$inputDegre=this.mainContainer.find(".degre");
	this.$labelDegre=this.mainContainer.find(".degrelabel");
	
	// functions
	this.onChangeType=function(evt){
		//recupération du cs membre famille de la baliset areaMembre
		var csRoleMembreFamille = $(evt.target).closest('.areaMembre').attr('csRole');
		
		var typePc = that.$inputTypePc.find('option:selected').val();		
		var typeRente = that.$inputTypeRente.find('option:selected').val();
		
		//Si type et pc et type de rente ok, ou type de rente dana la liste des ok
		if( (typePc == '64027003' && typeRente == '64006021') || $.inArray(typeRente,that.typesRenteAvecAI)>-1){
			
			//Si c'est un nefant on s'asure que le type pour les enfant soit bien utilisé 50 et 70 uniquement
			if(csRoleMembreFamille===csRoleEnfant && $.inArray(typeRente,that.typeRentesAvecAiEnfant)>-1){
				that.$inputDegre.show();
				that.$labelDegre.show();
			}
			//Sinon si pas enfant et liste parents
			else if(csRoleMembreFamille!==csRoleEnfant){
				that.$inputDegre.show();
				that.$labelDegre.show();
			}
		} else{
			that.$inputDegre.hide().val('');
			that.$labelDegre.hide();
		}
	};
	
	this.$inputTypePc.on("change",this.onChangeType);
	this.$inputTypeRente.on("change",this.onChangeType);
	
	
	this.afterRetrieve = function ($data) {
		this.defaultLoadData($data, ".", false);
	};

	this.getParametres = function ($data) {
		var o_map = this.createMapForSendData(".");
		o_map['doAddPeriode'] = this.doAddPeriode;
		o_map['idDroitMembreFamille'] = this.membreId;
		return o_map;
//		return {
//			'renteAvsAi.simpleRenteAvsAi.csTypeRente': this.detail.find('[name=champTypeRente]').val(),
//			'renteAvsAi.simpleRenteAvsAi.montant': this.detail.find('[name=montant]').val(),
//			'renteAvsAi.simpleRenteAvsAi.degreInvalidite': this.detail.find('[name=degreInvalidite]').val(),
//			'renteAvsAi.simpleRenteAvsAi.dateDepot': this.detail.find('[name=dateDepot]').val(),
//			'renteAvsAi.simpleRenteAvsAi.dateEcheance': this.detail.find('[name=dateEcheance]').val(),
//			'renteAvsAi.simpleRenteAvsAi.dateDecision': this.detail.find('[name=dateDecision]').val(),
//			'renteAvsAi.simpleDonneeFinanciereHeader.dateDebut': this.detail.find('[name=dateDebut]').val(),
//			'renteAvsAi.simpleDonneeFinanciereHeader.dateFin': this.detail.find('[name=dateFin]').val(),
//			'renteAvsAi.simpleRenteAvsAi.csTypePc': this.detail.find('[name=csTypePc]').val(),
//			'doAddPeriode': this.doAddPeriode,
//			'idDroitMembreFamille': this.membreId
//		};
	};

	this.clearFields = function () {
		
		this.detail.clearInputForm();
		
//		this.detail.find('.typeRente,.montant,.degreInvalidite,[name=dateDepot],[name=dateEcheance],[name=dateDecision],[name=dateDebut],[name=dateFin]').val('').end()
//		$('.typeRente')[0].selectedIndex = 0;
		
	};

	this.getParentViewBean = function () {
		return droit;
	};

	this.setParentViewBean = function (newViewBean) {
		droit = newViewBean;
	};

	this.formatTableTd = function ($elementTable) {
		$elementTable.find('td:eq(3),td:eq(4),td:eq(5)').css('text-align', 'left').end() // pour les dates, align a gauche
		.find('td:eq(1)').css('text-align', 'right').end(); // pour montant a droite*/
	};
	/*
	 *  // Formatatage de la table this.formatTable=function(){ this.formatTableTd(this.$trInTbody); }
	 */
	// initialization
	
	this.infoWarnRetro = function () {
		var $infoWarnRetro = this.detail.find(".infoWarnRetro");
		var $dateDecision =  this.detail.find('[name=renteAvsAi\\.simpleRenteAvsAi\\.dateDecision]');
		var n_noVersion = parseInt($("#infoDroitNoVersion").text(), 10);
		var s_infoDroitDateAnnonce = $("#infoDroitDateAnnonce").text();
		$infoWarnRetro.hide();
		if (n_noVersion === 1) {
			$dateDecision.change(function () {
				if(globazNotation.utilsDate.convertGlobazYearDateToJSDate(s_infoDroitDateAnnonce) != null &&
						globazNotation.utilsDate.convertGlobazYearDateToJSDate($dateDecision.val()) != null &&
					
						
						globazNotation.utilsDate.countMonth(s_infoDroitDateAnnonce,$dateDecision.val()) <= 6){
					 
					$infoWarnRetro.show("fast");
						} else {
					$infoWarnRetro.hide("fast");
					}
			});
		}
	};

	
	this.init(function () {
		var that = this;
		this.stopEdition();
		this.onAddTableEvent();
		this.colorTableRows(false);
		$("html").bind(eventConstant.NOTATION_MANAGER_DONE,function (){
			that.infoWarnRetro(); 
		});
		
		
	});
}





/*
 * ans l'écran des rentes (rentes AVS/AI, IJAI et API AVS/AI), si la version du droit vaut 1, 
 * et que la date de décision de la rente et la date de dépôt de la demande PC ne sont pas séparées de plus de 6 mois,
 *  alors un message doit sensibiliser l'utilisateur à la possibilité d'un rétro. "Attention, étudier la possibilité d'un rétro!" 
 */

RenteAvsAiPart.prototype = DonneeFinancierePart.prototype;

// jsManager.add(
$(function () {

	// change valeur type rente
	//var $degre = $("#degre");
	//var $degrelabel = $("#degrelabel");
	//$degre.hide();
	//$degrelabel.hide();	
	$inputDegre=$(".degre");
	$labelDegre=$(".degrelabel");
	
	$('.areaMembre').each(function () {
		var $that = $(this);
		var zone = new RenteAvsAiPart($that);
		this.zone = zone;
		zone.membreId = $that.attr('idMembre');
		
	
		$inputDegre.hide();
		$labelDegre.hide();
		

		
		$that.find('.btnAjaxUpdate').click(function () {
			zone.startEdition();
			$that.find('.btnAjaxValidateNouvellePeriode').show();
		}).end().find('.btnAjaxCancel').click(function () {
			zone.stopEdition();
		}).end().find('.btnAjaxValidate').click(function () {
			zone.validateEdition();
		}).end().find('.btnAjaxDelete').click(function () {
			zone.ajaxDeleteEntity(zone.selectedEntityId);
		}).end().find('.btnAjaxAdd').click(function () {
			$inputDegre.hide();
			$labelDegre.hide();
			zone.stopEdition();
			zone.startEdition();
		}).end().find('.btnAjaxValidateNouvellePeriode').click(function () {
			zone.doAddPeriode = true;
			zone.validateEdition();
		}).end();
		
		
		

	});
});