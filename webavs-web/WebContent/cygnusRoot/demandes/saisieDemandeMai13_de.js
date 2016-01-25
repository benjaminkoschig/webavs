
var gestionPeriodeCAAI = {

	$dateFacture: null,
	$dateDebutTraitement: null,

	$messageAvertissementSansDate: null,
	$messageAvertissementDateFacture: null,
	$messageAvertissementDateTraitement: null,
	$spanDateFacturation: null,
	$spanDateTraitement: null,
	$spanDateDebutPeriode: null,
	$spanDateFinPeriode: null,
	$spanMontantCAAI: null,
	$spanCodeAPI: null,
	$spanMontantAPI: null,

	$conteneursMessage: null,

	init: function () {
		this.$dateFacture = $('#dateFacture');
		this.$dateDebutTraitement = $('#dateDebutTraitement');

		this.$messageAvertissementSansDate = $('#messageAvertissementSansDate');
		this.$messageAvertissementDateFacture = $('#messageAvertissementDateFacture');
		this.$messageAvertissementDateTraitement = $('#messageAvertissementDateTraitement');
		this.$messageAvertissementEchue = $('#messageAvertissementEchue');
		this.$messageAvertissementFutrue = $('#messageAvertissementFutrue');
		this.$spanDateFacturation = $('#spanDateFacturation');
		this.$spanDateTraitement = $('#spanDateTraitement');
		this.$spanDateDebutPeriode = $('#spanDateDebutPeriode');
		this.$spanDateFinPeriode = $('#spanDateFinPeriode');
		this.$spanMontantCAAI = $('#spanMontantCAAI');
		this.$spanCodeAPI = $('#spanCodeAPI');
		this.$spanMontantAPI = $('#spanMontantAPI');

		this.$conteneursMessage = $('.messageAvertissementCAAI');

		if (this.isPeriodePresente()) {
			this.brancherEvenements();
		}
		this.verificationDates();
	},

	brancherEvenements: function () {
		var that = this;

		this.$dateFacture.change(function () {
			that.verificationDates();
		}).focusout(function () {
			that.verificationDates();
		});
		this.$dateDebutTraitement.change(function () {
			that.verificationDates();
		}).focusout(function () {
			that.verificationDates();
		});
	},

	verificationDates: function () {
		
		if (this.isPeriodePresente()) {

			var s_dateTraitement = this.$dateDebutTraitement.val();
			var s_dateFacture = this.$dateFacture.val();
			var o_periodeLaPlusRecente = this.getPeriodeLaPlusRecente();
			if (s_dateTraitement !== '') {
				var o_periode = this.getPeriodeChevauchantDate(s_dateTraitement);
				if (o_periode !== null) {
					this.updateMessagePourDateTraitement(o_periode, s_dateTraitement);
				} else {
					this.cacherMessageAvertissement();
				}
			} else if (s_dateFacture !== '') {
				var o_periode = this.getPeriodeChevauchantDate(s_dateFacture);
				if (o_periode !== null) {
					this.updateMessagePourDateFacture(o_periode, s_dateFacture);
				} else {
					this.cacherMessageAvertissement();
				}
			} else if (o_periodeLaPlusRecente !== null
						&& (o_periodeLaPlusRecente.dateFinCAAI === ''
							|| globazNotation.utils.date.isDateBefore(s_dateDernierPaiement, o_periodeLaPlusRecente.dateFinCAAI.substr(3))
							|| globazNotation.utils.date.areDatesSame(s_dateDernierPaiement, o_periodeLaPlusRecente.dateFinCAAI.substr(3)))) {
				this.updateMessageAvertissementSansDate(o_periodeLaPlusRecente);
			} else if (this.isPeriodeEchue(o_periodeLaPlusRecente)) {
				this.updateMessageAvertissementEchue(o_periodeLaPlusRecente);
			} else if (this.isPeriodeFuture(o_periodeLaPlusRecente)) {
				this.updateMessageAvertissementFutre(o_periodeLaPlusRecente);		
			} else {
				this.cacherMessageAvertissement();
			}
		} else {
			this.cacherMessageAvertissement();
		}
	},

	isPeriodePresente: function () {
		return o_periodesCAAI && o_periodesCAAI.length > 0;
	},
	
	isPeriodeEchue: function (o_periode) {
		if(o_periode.dateFinCAAI){
			return globazNotation.utilsDate.isDateBefore(o_periode.dateFinCAAI,s_dateDernierPaiement);
		} else {
			return false;
		}
	},

	isPeriodeFuture: function (o_periode){
		return globazNotation.utilsDate.isDateAfter(o_periode.dateDebutCAAI,s_dateDernierPaiement);
	},
	/**
	 * Le but de cette fonction est de déterminer la période la plus récente en fonction de certain critère
	 * Si il existe une période en cours on vas retourner la période en cours la plus restante.
	 * Sinon on regarde s'il existe un période future on retourne la plus proche.
	 * Sinon on regarde s'il existe une période échue et on retourne la plus récente.
	 * Attention le tableau de période doit être trié de manière ascendante
	 * @returns o_periode
	 */
	getPeriodeLaPlusRecente: function () {
		var o_presente , o_future, o_echue;
		if (this.isPeriodePresente()) {
			var i = o_periodesCAAI.length-1;
			for ( i; i>=0 ; i--) {
				var o_periodeTmp =  o_periodesCAAI[i];
				if(this.isPeriodePresente()){
					if(!o_presente) {
						o_presente = o_periodeTmp;
					}
				} else if(this.isPeriodeFuture(o_periode)) {
					o_future = o_periodeTmp;					
				} else if(this.isPeriodeEchue(o_periode)) {
					if(!o_echue) {
						o_echue = o_periodeTmp;
					}
				} else {
					throw "no periode match";
				}
			}
			
			if(o_presente){
				return o_presente;
			} else if(o_future) {
				return o_future;
			} else {
				return o_echue;
			}
			//return o_periodesCAAI[o_periodesCAAI.length - 1];
		}
		return null;
	},
	

	getPeriodeChevauchantDate: function (s_date) {

		if (!globazNotation.utilsDate._isValidGlobazDate(s_date)) {
			return null;
		}

		if (this.isPeriodePresente()) {

			var n_indexPeriode = null;
			$.each(o_periodesCAAI, function (index, periode) {
				if ((globazNotation.utilsDate.isDateAfter(s_date, periode.dateDebutCAAI)
						|| globazNotation.utilsDate.areDatesSame(s_date, periode.dateDebutCAAI))
					&& (periode.dateFinCAAI === ''
						|| globazNotation.utilsDate.isDateBefore(s_date, periode.dateFinCAAI)
						|| globazNotation.utilsDate.areDatesSame(s_date, periode.dateFinCAAI))) {
					n_indexPeriode = index;
				}
			});

			if (n_indexPeriode !== null) {
				return o_periodesCAAI[n_indexPeriode];
			}
		}

		return null;
	},

	updateMessageAvertissementEchue: function (o_periode){
		this.$messageAvertissementSansDate.hide();
		this.$messageAvertissementDateTraitement.hide();
		this.$messageAvertissementDateFacture.hide();
		this.$messageAvertissementFutrue.hide();
		this.$messageAvertissementEchue.show();

		this.updateDetailPeriode(o_periode);

		this.$conteneursMessage.show();
	},
	
	updateMessageAvertissementFuture: function (o_periode){
		this.$messageAvertissementSansDate.hide();
		this.$messageAvertissementDateTraitement.hide();
		this.$messageAvertissementDateFacture.hide();
		this.$messageAvertissementEchue.hide();
		this.$messageAvertissementFutrue.show();
		
		this.updateDetailPeriode(o_periode);

		this.$conteneursMessage.show();
	},
	
	updateMessagePourDateTraitement: function (o_periode, s_dateTraitement) {
		this.$messageAvertissementSansDate.hide();
		this.$messageAvertissementDateFacture.hide();
		this.$messageAvertissementEchue.hide();
		this.$messageAvertissementFutrue.hide();
		this.$messageAvertissementDateTraitement.show();

		this.$spanDateTraitement.text(s_dateTraitement);

		this.updateDetailPeriode(o_periode);

		this.$conteneursMessage.show();
	},

	updateMessagePourDateFacture: function (o_periode, s_dateFacture) {
		this.$messageAvertissementSansDate.hide();
		this.$messageAvertissementDateTraitement.hide();
		this.$messageAvertissementDateFacture.show();
		this.$messageAvertissementEchue.hide();
		this.$messageAvertissementFutrue.hide();
		this.$spanDateFacturation.text(s_dateFacture);

		this.updateDetailPeriode(o_periode);

		this.$conteneursMessage.show();
	},

	updateMessageAvertissementSansDate: function (o_periode) {
		this.$messageAvertissementDateTraitement.hide();
		this.$messageAvertissementDateFacture.hide();
		this.$messageAvertissementEchue.hide();
		this.$messageAvertissementFutrue.hide();
		this.$messageAvertissementSansDate.show();

		this.updateDetailPeriode(o_periode);

		this.$conteneursMessage.show();
	},

	cacherMessageAvertissement: function () {
		this.$conteneursMessage.hide();
	},

	updateDetailPeriode: function (o_periode) {
		this.$spanDateDebutPeriode.text(o_periode.dateDebutCAAI);

		var s_dateFin = o_periode.dateFinCAAI;
		if (s_dateFin === '') {
			s_dateFin = '...';
		}
		this.$spanDateFinPeriode.text(s_dateFin);

		this.$spanMontantCAAI.text(o_periode.montantCAAI);
		this.$spanCodeAPI.text(o_periode.codeAPI);
		this.$spanMontantAPI.text(o_periode.montantAPI);
	}
};

$(document).ready(function () {
	$('html').bind(eventConstant.NOTATION_MANAGER_DONE, function () {
		gestionPeriodeCAAI.init();
	});
});