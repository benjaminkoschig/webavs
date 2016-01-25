var debloquerMontantRA = {
	$idCompteAnnexe: null,
	$typeSection: null,
	$idSection: null,
	$soldeSection: null,
	$montantADebloquer: null,
	$spanErreurMontant: null,
	$spanErreurSection: null,
	$boutonValider: null,
	$widgetSection: null,

	$userAction: null,
	s_actionRenteAccordee: null,

	isMontantADebloquerPlusGrandQueSoldeSection: function () {
		var montantADebloquer = parseFloat(this.$montantADebloquer.val().replace("'", ""));
		var soldeSection = parseFloat(this.$soldeSection.val());
		
		return montantADebloquer > (-1.0 * soldeSection);
	},

	adapterMontantADebloquerSiBesoin: function () {
		if (this.isMontantADebloquerPlusGrandQueSoldeSection()) {
			this.$montantADebloquer.val(-1.0 * parseFloat(this.$soldeSection.val())).change();
		}
	},

	checkBoutonValider: function () {
		var b_disableBoutonValider = false;

		if (this.$idSection.val()) {
			this.$spanErreurSection.fadeOut();
		} else {
			this.$spanErreurSection.fadeIn();
			b_disableBoutonValider = true;
		}

		if (this.isMontantADebloquerPlusGrandQueSoldeSection()) {
			this.$spanErreurMontant.fadeIn();
			b_disableBoutonValider = true;
		} else {
			this.$spanErreurMontant.fadeOut();
		}

		if (b_disableBoutonValider) {
			this.$boutonValider.prop('disabled', true);
		} else {
			this.$boutonValider.removeProp('disabled');
		}
	},

	init: function () {
		var that = this;

		this.$idCompteAnnexe = $('#idCompteAnnexe');

		this.$typeSection = $('#typeSection');
		this.$idSection = $('#idSection');
		this.$soldeSection = $('#soldeSection');
		this.$montantADebloquer = $('#montantADebloquer');

		this.$spanErreurMontant = $('#spanErreurMontant');
		this.$spanErreurMontant.hide();
		
		this.$spanErreurSection = $('#spanErreurSection');
		this.$spanErreurSection.hide();

		this.$boutonValider = $('#btnVal');

		this.$userAction = $('input[name="userAction"]');

		this.$widgetSection = $('#widgetNoFacture');

		this.$widgetSection.focus(function () {
			that.$widgetSection.val('');
			that.$widgetSection.trigger(eventConstant.WIDGET_FORCE_LOAD);
		});

		this.$montantADebloquer.change(function () {
			that.checkBoutonValider();
		});

		this.checkBoutonValider();
	}
};

function add() {
}

function upd() {
}

function validate() {
	$("#btnVal").prop('disabled', true);
	ajaxUtils.addOverlay($('html'));
	debloquerMontantRA.$userAction.val(debloquerMontantRA.s_actionRenteAccordee + ".actionExecuterDeblocage");
	return true;
}

function cancel() {
}

function init(){
}

$(document).ready(function () {
	debloquerMontantRA.init();
});