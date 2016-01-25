
var $mainForm = null;
var $boutonSupprimer = null;
var b_isAdd = false;

var detailContribution = {

	$selectCodeAPI: null,

	mettreAJourGenreEtDegreAPI: function () {
		var s_codeAPI = this.$selectCodeAPI.val();

		if (s_codeAPI !== '') {
			var s_valeurCorrespondante = o_mapAPI[s_codeAPI];
			var s_genre = s_valeurCorrespondante.split('-')[0];
			var s_degre = s_valeurCorrespondante.split('-')[1];

			this.cacherGenreAPI();
			this.cacherDegreAPI();

			this.montrerUnGenreAPI(s_genre);
			this.montrerUnDegreAPI(s_degre);
		}
	},

	cacherGenreAPI: function () {
		$('.genreAPI').hide();
	},

	montrerUnGenreAPI: function (s_genre) {
		$('#genreAPI-' + s_genre).show();
	},

	cacherDegreAPI: function () {
		$('.degreAPI').hide();
	},

	montrerUnDegreAPI: function (s_degre) {
		$('#degreAPI-' + s_degre).show();
	},

	init: function () {
		var that = this;

		this.$selectCodeAPI = $('#selectCodeAPI');

		this.$selectCodeAPI.change(function () {
			that.mettreAJourGenreEtDegreAPI();
		});

		this.cacherGenreAPI();
		this.cacherDegreAPI();

		if (this.$selectCodeAPI.val() !== '') {
			this.mettreAJourGenreEtDegreAPI();
		}
	}
};

function add() {
	$mainForm.find('[name=userAction]').val(s_url + '.ajouter');
}

function upd () {
	if (b_isAdd && b_dernierePeriodeOuvert) {
		window.alert(s_messageAvertissementPeriodeOuverte);
	}
}

function validate () {

	if (b_isAdd)
		$mainForm.find('[name=userAction]').val(s_url + ".ajouter");
	else
		$mainForm.find('[name=userAction]').val(s_url + ".modifier");

	return true;
}

function cancel() {
	if ($mainForm.find('_method').val())
		$mainForm.find('[name=userAction]').val("back");
	else
		$mainForm.find('[name=userAction]').val(s_url + ".afficher");
}

function del() {
	if (window.confirm(s_messageDelete)){
		$mainForm.find('[name=userAction]').val(s_url + ".supprimer");
		$mainForm.submit();
	}
}

function init () {
}

$(document).ready(function () {
	$mainForm = $('[name=mainForm]');
	$boutonSupprimer = $('#btnDel');

	var s_idContribution = $('#idContributionAssistanceAI').val();
	if (s_idContribution === undefined || s_idContribution === '' || s_idContribution === 'null') {
		b_isAdd = true;
		$boutonSupprimer.attr('disabled', 'true');
	}

	detailContribution.init();
});
