var processPFAnalyserEcheance = {

	$userChoices: null,
	$toutGenererListe: null,
	$listeBox: null,
	$selectCaisse: null,
	$boutonExecuter: null,

	init: function () {
		this.$userChoices = $('.userChoice');
		this.$userChoices.filter(':odd').addClass('odd');

		this.$toutGenererListe = $('#toutesLesListes');

		this.$listeBox = $('.listeBox');
		
		this.$selectCaisse = $('#forCsCaisse');
		this.$boutonExecuter = $('#executerProcess');

		this.bindEvent();

		this.checkGenererListe();
		this.checkBoutonExecuter();
	},

	bindEvent: function () {
		var that = this;

		this.$toutGenererListe.click(function () {
			that.switchGenererListe();
		});
		
		this.$listeBox.click(function () {
			that.checkGenererListe();
		});

		this.$selectCaisse.change(function () {
			that.checkBoutonExecuter();
		});
	},

	switchGenererListe: function () {
		var checked;

		if (!this.$toutGenererListe.prop('checked')) {
			checked = false;
		} else {
			checked = true;
		}

		this.$listeBox.each(function () {
			var $that = $(this);
			if (checked) {
				$that.prop('checked', checked);
			} else {
				$that.removeProp('checked');
			}
		});

		this.checkGenererListe();
	},

	checkGenererListe: function () {
		var checked = true;

		this.$listeBox.each(function () {
			var $that = $(this);

			if (!$that.prop('checked')) {
				checked = false;
			}
		});

		if (checked) {
			this.$toutGenererListe.prop('checked', checked);
		} else {
			this.$toutGenererListe.removeProp('checked');
		}
	},

	checkBoutonExecuter: function () {
		if (this.$selectCaisse.val() === '') {
			this.$boutonExecuter.button('option', 'disabled', true);
		} else {
			this.$boutonExecuter.button('option', 'disabled', false);
		}
	}
};

function getParametresSupplementaires() {
	var o_params = {};

	processPFAnalyserEcheance.$listeBox.each(function () {
		o_params[this.name] = (this.checked ? true : false);
	});

	o_params.forCsCaisse = $('select[name="forCsCaisse"]').val();

	return o_params;
}

setTimeout(function () {
	processPFAnalyserEcheance.init();
}, 100);
