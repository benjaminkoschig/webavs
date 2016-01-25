$(document).ready(function() {
	genererRenteVeuvePerdure.init();
});

var genererRenteVeuvePerdure = {

	$annexes : null,
	$dateDocument : null,
	$montant : null,
	$adresseEmail : null,
	$bouttonOk : null,

	init : function() {
		this.$annexes = $('#annexes');
		this.$dateDocument = $('#dateDocument');
		this.$montant = $('#montantRenteVieillesse');
		this.$adresseEmail = $('#adresseEmail');
		this.$boutonOk = $('#btnOk');

		this.bindEvent();
	},

	bindEvent : function() {
		this.$adresseEmail.change(this.checkEmail());
	},

	checkEmail : function() {
		if (this.$adresseEmail.val()) {
			if (!this.$adresseEmail.val().match('^[a-zA-Z0-9_-]+@[a-zA-Z0-9-]{2,}[.][a-zA-Z]{2,3}$')) {
				this.$adresseEmail.val('');
			}
		}
	},

	checkAll : function() {
		return true;
	}
};
