
var retenuePaiement = {
	// variables récupérées depuis Java
	s_actionRetenueSurPaiement: null,
	s_messageConfirmationSuppression: null,
	b_retenueDejaUtiliseeDansPaiementMensuel: false,

	$userAction: null,
	$method: null,
	$valid: null,
	$form: null,

	$boutonChoisirAdressePaiement: null,

	$trOptionnels: null,
	$csTypeRetenue: null,

	$widgetCompteAnnexe: null,
	$divRoleCompteAnnexe: null,
	$csRoleCompteAnnexe: null,
	$labelRoleCompteAnnexe: null,

	$widgetSection: null,
	$divTypeSection: null,
	$csTypeSection: null,
	$labelTypeSection: null,

	$montantTotal: null,

	// boutons du bas la de page
	$boutonAjouter: null,
	$boutonModifier: null,
	$boutonAnnuler: null,
	$boutonValider: null,
	$boutonSupprimer: null,

	// groupes d'élements (définis par classe CSS)
	$champModifiable: null, 
	$champNonModifiableApresPaiement: null,
	$valeursAMasquer: null,
	$valeursSpecifiques: null,

	init: function () {
		this.$userAction = $('input[name="userAction"]');
		this.$method = $('input[name="_method"]');
		this.$valid = $('input[name="_valid"]');
		this.$form = $('form[name="mainForm"]');

		this.$boutonChoisirAdressePaiement = $('#selecteurAdresses');
		this.$boutonChoisirAdressePaiement.addClass('modifiable nonModifiableApresPaiement');

		this.$trOptionnels = $('.trOptionnels');
		this.$csTypeRetenue = $('#csTypeRetenue');

		this.$widgetCompteAnnexe = $('#widgetIdExterne');
		this.$divRoleCompteAnnexe = $('#divRole');
		this.$csRoleCompteAnnexe = $('#role');
		this.$labelRoleCompteAnnexe = $('#labelRole');

		this.$widgetSection = $('#widgetNoFacture');
		this.$divTypeSection = $('#divTypeSection');
		this.$csTypeSection = $('#typeSection');
		this.$labelTypeSection = $('#labelTypeSection');

		this.$montantTotal = $('#montantTotalARetenir');

		this.$boutonAjouter = $('#btnNew');
		this.$boutonModifier = $('#btnUpd');
		this.$boutonAnnuler = $('#btnCan');
		this.$boutonValider = $('#btnVal');
		this.$boutonSupprimer = $('#btnDel');

		this.$champModifiable = $('.modifiable');
		this.$champNonModifiableApresPaiement = $('.nonModifiableApresPaiement');
		this.$valeursAMasquer = $('.valeursAMasquer');
		this.$valeursSpecifiques = $('.valeursSpecifiques');

		this.bindEvent();

		// affichage des bons champs
		this.afficherTrOptionnels();

		if (this.isEnModification()) {
			this.desactiverModification();

			if (this.$widgetCompteAnnexe.val()) {
				updateLabelRole();
			}

			if (this.$widgetSection.val()) {
				updateLabelTypeSection();
			}
		}

		// vieux code hérité de l'ancien FW javascript
		if (this.hasBeenModifiedOrAdded()) {
			if (parent.document.forms[0]) {
				parent.document.forms[0].submit();
			}
		}
		document.forms[0].target="_self";
	},

	bindEvent: function () {
		var that = this;

		this.$csTypeRetenue.change(function () {
			that.csTypeRetenueChange();
		});

		this.$widgetSection.focus(function() {
			that.viderWidgetSection();
			that.forcerLancementRecherche(that.$widgetSection);
		});

		this.$widgetCompteAnnexe.focus(function() {
			that.viderWidgetCompteAnnexe();
			that.forcerLancementRecherche(that.$widgetCompteAnnexe);
		});

		this.$boutonAjouter.click(function () {
			that.ajouter();
		});
		
		this.$boutonModifier.click(function () {
			that.activerModification();
		});

		this.$boutonAnnuler.click(function () {
			that.annuler();
		});

		this.$boutonSupprimer.click(function () {
			that.supprimer();
		});
	},

	forcerLancementRecherche: function ($widget) {
		$widget.trigger(eventConstant.WIDGET_FORCE_LOAD);
	},

	viderWidgetSection: function () {
		this.$widgetSection.val('');
		this.$csTypeSection.val('');
		this.$divTypeSection.hide();
	},

	viderWidgetCompteAnnexe: function () {
		this.$widgetCompteAnnexe.val('');
		this.$csRoleCompteAnnexe.val('');
		this.$divRoleCompteAnnexe.hide();
		this.viderWidgetSection();
	},

	afficherTrOptionnels: function () {
		this.$trOptionnels.hide();
		this.$trOptionnels.filter('.' + this.$csTypeRetenue.val()).show();
	},

	csTypeRetenueChange: function () {
		this.afficherTrOptionnels();
		this.$valeursSpecifiques.val('');
		this.$valeursAMasquer.hide();
	},

	activerModification: function () {
		var $champsAActiver = this.$champModifiable;
		if (this.b_retenueDejaUtiliseeDansPaiementMensuel) {
			$champsAActiver = $champsAActiver.not(this.$champNonModifiableApresPaiement);
		}
		$champsAActiver.prop('disabled', false);
	},

	desactiverModification: function () {
		this.$champModifiable.prop('disabled', true);
	},

	desactiverChampNonModifiableApresPaiement: function () {
		this.$champNonModifiableApresPaiement.css({
			'disabled': 'true'
		});
	},

	isEnAjout: function () {
		return this.$method.val() === 'add';
	},

	isEnModification: function () {
		return !this.isEnAjout();
	},

	hasBeenModifiedOrAdded: function () {
		return this.$valid.val() === 'new';
	},

	valider: function () {
		if (this.isEnAjout()) {
			this.$userAction.val(this.s_actionRetenueSurPaiement + ".ajouter");
		} else {
			this.$userAction.val(this.s_actionRetenueSurPaiement + ".modifier");
		}
	},

	ajouter: function () {
		this.$userAction.val(this.s_actionRetenueSurPaiement + ".ajouter");
	},

	annuler: function () {
		if (this.isEnAjout()) {
			this.$userAction.val("back");
		} else {
			this.$userAction.val(this.s_actionRetenueSurPaiement + ".afficher");
		}
	},

	supprimer: function () {
		if (window.confirm(this.s_messageConfirmationSuppression)) {
			this.$userAction.val(retenuePaiement.s_actionRetenueSurPaiement + ".supprimer");
			this.$form.submit();
		}
	}
};

function add() {
}

function upd() {
}

function validate() {
	retenuePaiement.valider();
	return true;
}

function cancel() {
}

function del() {
}

function doInitThings() {
	actionInit();
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		add();
		try {
			top.fr_appicons.hidePostit();
		} catch (e) {}
	} else {
		try {
			top.fr_appicons.checkPostit();
		} catch (e) {}
	}
	try {
		postInit();
	} catch (noSuchMethodException) {}
	testDisabledTable();
	showErrors();
}

function readOnly(flag) {
}

function disabledTable(elems) {
}

function impotSourceMontantRetenuMensuelChange() {
	document.forms[0].elements("tauxImposition").value = '';
	//document.forms[0].elements("cantonImposition").selectedIndex = 0;
}

function impotSourceCantonImpositionChange() {
	//document.forms[0].elements("montantRetenuMensuel").value = '';
	//document.forms[0].elements("tauxImposition").value = '';
}

function impotSourceTauxImpositionChange() {
	document.forms[0].elements("montantRetenuMensuel").value = '';
	//document.forms[0].elements("cantonImposition").selectedIndex = 0;
}

function resetSection() {
	retenuePaiement.$divTypeSection.hide();
	retenuePaiement.$widgetSection.val('');
}

function updateLabelRole() {
	retenuePaiement.$labelRoleCompteAnnexe.text($("#role option:selected").text());
	retenuePaiement.$divRoleCompteAnnexe.show();
}

function updateLabelTypeSection() {
	retenuePaiement.$labelTypeSection.text($("#typeSection option:selected").text());
	retenuePaiement.$divTypeSection.show();
}

function updateMontantTotalARetenir(montant) {
	if (typeof montant == "string") {
		var parsedValue = parseFloat(montant);
		if (parsedValue < 0) {
			parsedValue *= -1.0;
		}
		// Assignation de la valeur au champ et mise en forme
		validateFloatNumber(retenuePaiement.$montantTotal.val(parsedValue).get(0));
	}
}

$(document).ready(function () {
	retenuePaiement.init();
});
