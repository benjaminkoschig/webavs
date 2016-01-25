
var gestionAffichage = {

	$isDepuisDemande: null,
	$attestationUnique: null,
	$attestationsMultiples: null,
	$radioAttestationUnique: null,
	$radioAttestationsMultiples: null,
	$nssDe: null,
	$nssA: null,
	$champCacheNssDe: null,
	$champCacheNssA: null,
	$anneeAttestation: null,
	$partialNss: null,
	$isAttestationUnique: null,

	init: function () {
		var that = this;

		this.$isDepuisDemande = $('#isDepuisDemande');
		this.$attestationUnique = $('.attestationUnique');
		this.$attestationsMultiples = $('.attestationsMultiples');
		this.$radioAttestationUnique = $('#attestationUnique');
		this.$radioAttestationsMultiples = $('#attestationMultiple');
		this.$nssA = $('#nssA');
		this.$nssDe = $('#nssDe');
		this.$hiddenNssA = $('#hiddenNssA');
		this.$hiddenNssDe = $('#hiddenNssDe');
		this.$anneeAttestation = $('#anneeAttestation');
		this.$partialNss = $('#partialNSS');
		this.$isAttestationUnique = $('#isAttestationUnique');

		this.bindEvent();

		if (this.$isDepuisDemande.val() === "true") {
			$('.depuisMenu').hide();
			$('.depuisDemande').show();
			this.$attestationsMultiples.hide();
			this.$attestationUnique.show();
		} else {
			$('.depuisMenu').show();
			$('.depuisDemande').hide();

			if (this.$radioAttestationsMultiples.is(':checked')) {
				this.$attestationsMultiples.show();
				this.$attestationUnique.hide();
			} else {
				this.$attestationsMultiples.hide();
				this.$attestationUnique.show();
			}
		}

		if (this.$isDepuisDemande.val() === "true" 
			|| this.$radioAttestationsMultiples.is(':checked')) {
			$('html').on(eventConstant.JADE_FW_ACTION_DONE, function () {
				that.$anneeAttestation.focus();
				that.$anneeAttestation.addClass('hasFocus');
			});
		} else {
			$('html').on(eventConstant.JADE_FW_ACTION_DONE, function () {
				that.$partialNss.focus();
				that.$partialNss.addClass('hasFocus');
			});
		}
	},

	bindEvent: function () {
		var that = this;

		this.$radioAttestationUnique.click(function () {
			that.$attestationsMultiples.hide();
			that.$attestationUnique.show();

			that.$partialNss.focus().addClass('hasFocus').select();
			that.$isAttestationUnique.val('true');
		});

		this.$radioAttestationsMultiples.click(function () {
			that.$attestationsMultiples.show();
			that.$attestationUnique.hide();

			that.$anneeAttestation.focus().addClass('hasFocus').select();
			that.$isAttestationUnique.val('false');
		});

		this.$nssA.change(function () {
			var $this = $(this);
			var s_val = that.completeNSS('nssA', $this.val());
			$this.val(s_val);
			that.$hiddenNssA.val('756.' + s_val);
		});
		this.$nssDe.change(function () {
			var $this = $(this);
			var s_val = that.completeNSS('nssDe', $this.val());
			$this.val(s_val);
			that.$hiddenNssDe.val('756.' + s_val);
		});
	},

	completeNSS: function (s_type, s_val) {
		var n_complement = null;
		switch (s_type) {
			case 'nssA':
				n_complement = 9;
				break;
			case 'nssDe':
				n_complement = 0;
				break;
		}

		if (n_complement !== null) {
			var s_nss = s_val.replace(/\./g, '');
			if (s_nss.length !== 10) {
				for (var i = s_nss.length; i < 10; i++) {
					s_nss += n_complement;
				}
			}
			var s_nssPart1 = s_nss.substring(0, 4);
			var s_nssPart2 = s_nss.substring(4, 8);
			var s_nssPart3 = s_nss.substring(8, 10);

			return s_nssPart1.concat('.', s_nssPart2, '.', s_nssPart3);
		}

		return '';
	}
};



$(document).ready(function () {
	gestionAffichage.init();
});