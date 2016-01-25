/*
 * SCE
 */

/**
 * 
 * @return
 */
function IndemniteJournaliereAiPart (container) {
	// variables
	var that = this;

	this.ACTION_AJAX = ACTION_AJAX_DONNEE_FINANCIERE;
	this.IMG_BUBBLE_DISPLAY_ON_ERROR = "/webavs/images/small_info.png";
	this.mainContainer = container;
	this.table = this.mainContainer.find(".areaDFDataTable");
	this.detail = this.mainContainer.find(".areaDFDetail");
	this.titleContainer = this.mainContainer.find(".areaTitre");
	this.modifiedZoneClass = "areaDFModified";
	this.membreId = null;

	// functions
	this.afterRetrieve = function ($data) {
		this.detail.find('.typeIjai').val($data.find('csTypeIjai').text()).end().find('.nbreJours').val($data.find('nbreJours').text()).end().find('.montant').val($data
				.find('montant').text()).change().end().find('[name=dateDepot]').val($data.find('dateDepot').text()).end().find('[name=dateEcheance]').val($data
				.find('dateEcheance').text()).end().find('[name=dateDecision]').val($data.find('dateDecision').text()).end().find('[name=dateDebut]').val($data.find('dateDebut')
				.text()).end().find('[name=dateFin]').val($data.find('dateFin').text()).end();
		this.dealInfoNbresJours(this.detail.find('.nbreJours'));
		this.addSpy($data);
	};

	this.getParametres = function ($data) {
		return {
			'indemniteJournaliereAi.simpleIndemniteJournaliereAi.csTypeIjai': this.detail.find('.typeIjai').val(),
			'indemniteJournaliereAi.simpleIndemniteJournaliereAi.nbreJours': this.detail.find('.nbreJours').val(),
			'indemniteJournaliereAi.simpleIndemniteJournaliereAi.montant': this.detail.find('.montant').val(),
			'indemniteJournaliereAi.simpleIndemniteJournaliereAi.dateDepot': this.detail.find('[name=dateDepot]').val(),
			'indemniteJournaliereAi.simpleIndemniteJournaliereAi.dateEcheance': this.detail.find('[name=dateEcheance]').val(),
			'indemniteJournaliereAi.simpleIndemniteJournaliereAi.dateDecision': this.detail.find('[name=dateDecision]').val(),
			'indemniteJournaliereAi.simpleDonneeFinanciereHeader.dateDebut': this.detail.find('[name=dateDebut]').val(),
			'indemniteJournaliereAi.simpleDonneeFinanciereHeader.dateFin': this.detail.find('[name=dateFin]').val(),
			'doAddPeriode': this.doAddPeriode,
			'idDroitMembreFamille': this.membreId
		};
	};

	this.clearFields = function () {
		this.detail.find('.montant,[name=dateDepot],[name=dateEcheance],[name=dateDecision],[name=dateDebut],[name=dateFin]').val('').end()
		this.detail.find('.nbreJours').val('365');
	};

	this.getParentViewBean = function () {
		return droit;
	};

	this.setParentViewBean = function (newViewBean) {
		droit = newViewBean;
	};
	
	this.infoWarnRetro = function () {
		var $infoWarnRetro = this.detail.find(".infoWarnRetro");
		var $dateDecision =  this.detail.find('[name="dateDecision"]');
		var n_noVersion = parseInt($("#infoDroitNoVersion").text(), 10);
		var s_infoDroitDateAnnonce = $("#infoDroitDateAnnonce").text();
		$infoWarnRetro.hide();
		if (n_noVersion === 1) {
			$dateDecision.change(function () {
				if (globazNotation.utilsDate.convertGlobazYearDateToJSDate(s_infoDroitDateAnnonce) != null &&
						globazNotation.utilsDate.convertGlobazYearDateToJSDate($dateDecision.val()) != null &&
						globazNotation.utilsDate.countMonth(s_infoDroitDateAnnonce,$dateDecision.val()) <= 6) {
					$infoWarnRetro.show("fast");
				} else {
					$infoWarnRetro.hide("fast");
				}
			});
		}
	};
	this.formatTableTd = function ($elementTable) {
		$elementTable.find('td:eq(3),td:eq(4),td:eq(5)').css('text-align', 'left').end()// pour les dates, align a gauche
		.find('td:eq(1)').css('text-align', 'right').end(); // pour montant a droite
		$elementTable.find('.nbreJours').attr('maxlength', '3');// init maxlength nbre de jours
	};
	//Infos bulle nbre de jours
	this.dealInfoNbresJours = function ($field) {
		var s_nbreJoursSaisis = $field.val();
		
		$('.infoJours').remove();
		//alert(s_nbreJoursSaisis);
		if(Number(s_nbreJoursSaisis)===0||s_nbreJoursSaisis===undefined){
		
			var $img = $('<img/>',{
				'src':this.IMG_BUBBLE_DISPLAY_ON_ERROR,
				'data-g-bubble':'wantMarker:false,text:¦'+INFO_NBREJOURS_INFO_TEXTE+'¦'
			});
			var $span = $('<span/>',{
				'class':'infoJours'
			});
			$span.append($img);
			$span.insertAfter($field);//$field.append($span);
			notationManager.addNotationOnFragment($span);
		}
	};
	// initialization
	this.init(function () {
		this.stopEdition();
		this.onAddTableEvent();
		this.colorTableRows(false);
		$("html").bind(eventConstant.NOTATION_MANAGER_DONE, function (){
			that.infoWarnRetro(); 
		});
		
	});
	
}

IndemniteJournaliereAiPart.prototype = DonneeFinancierePart.prototype;

$(function () {
	$('.areaMembre').each(function () {
		var $that = $(this);
		var zone = new IndemniteJournaliereAiPart($(this));
		this.zone = zone;
		zone.membreId = $(this).attr('idMembre');

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
			zone.stopEdition();
			zone.startEdition();
		}).end().find('.btnAjaxValidateNouvellePeriode').click(function () {
			zone.doAddPeriode = true;
			zone.validateEdition();
		}).end().find('.nbreJours').keyup(function () {
			//alert($(this).val());
			zone.dealInfoNbresJours($(this));
		});

	});
});