var annoncePonctuelle = {
	$cs1: null,
	$cs2: null,
	$cs3: null,
	$cs4: null,
	$cs5: null,
	$nssComplementaire1: null,
	$nssComplementaire2: null,
	$imageOk: null,
	$imageKo: null,
	$userAction: null,
	$form: null,
	$genererAnnonceCheckbox: null,
	o_mapCodesCasSpeciaux: null,
	o_mapAnnonceRentesLieeSiModif: null,
	b_firstCheckRenteLiee: null,
	b_hasError: false,
	$mapVerification: null,
	
	checkBoutonAnnoncePonctuelle: function () {
		var b_checked = false;
		this.$genererAnnonceCheckbox.each(function () {
			if ($(this).attr('checked')) {
				b_checked = true;
			}
		});
		
		var b_enModification = $('.modifiable').attr('disabled');
		
		if (b_enModification) {
			if (b_checked) {
				$('.bouttonAnnoncePonctuelle').fadeIn(500);
			} else {
				$('.bouttonAnnoncePonctuelle').fadeOut(100);
			}
		} else {
			$('.pendantValidation').each(function () {
				var $this = $(this);
				if ($this.parent('td').siblings('.2emeCollone').children('.genererAnnonceCheckbox').attr('checked')) {
					$this.fadeIn(500);
				} else {
					$this.fadeOut(100);
				}
			});
		}
	},
	
	checkAnnonceRentesLiee: function () {
		var that = this;
		var b_check = false;
		$.each(this.o_mapAnnonceRentesLieeSiModif, function (key, element) {
			var $element = $('#' + key);
			if ($element.attr('type') === 'checkbox') {
				if ($('#' + key).attr('checked') !== that.o_mapAnnonceRentesLieeSiModif[key]) {
					b_check = true;
				}
			} else {
				if ($('#' + key).val() !== that.o_mapAnnonceRentesLieeSiModif[key]) {
					b_check = true;
				}
			}
		});
		if (b_check) {
			$('span.necessaire').fadeIn(500);
			if (this.b_firstCheckRenteLiee) {
				this.b_firstCheckRenteLiee = false;
				this.$genererAnnonceCheckbox.attr('checked', true).change();
			}
		} else {
			$('span.necessaire').fadeOut(500);
			var b_isChecked = false;
			this.$genererAnnonceCheckbox.each(function () {
				var $this = $(this);
				if ($this.attr('checked')) {
					b_isChecked = true;
				}
			});
			if (b_isChecked) {
				this.b_firstCheckRenteLiee = true;
			}
			this.$genererAnnonceCheckbox.attr('checked', false).change();
		}
	},
		
	disableModifiableInput: function () {
		$('.modifiable').attr('disabled', 'true');
	},
	
	enableModifiableInput: function () {
		$('.modifiable').removeAttr('disabled');
		$('.bouttonAnnoncePonctuelle').hide();
		this.checkBoutonAnnoncePonctuelle();
	},
	
	nssComplementaire1Change: function (tag) {
		if (tag.select !== null) {
			var element = tag.select.options[tag.select.selectedIndex];
			if (element.id !== null) {
				document.getElementById("idTiersComplementaire1").value = element.idAssure;
			}
		}
	},
	
	nssComplementaire2Change: function (tag) {
		if (tag.select !== null) {
			var element = tag.select.options[tag.select.selectedIndex];
			if (element.id !== null) {
				document.getElementById("idTiersComplementaire2").value = element.idAssure;
			}
		}
	},
	
	metAJourCodeCasSpeciaux: function () {
		var areCodesCasSpeciauxValid = true;
	
		// code cas spécial 1
		if (this.$cs1.val() !== "") {
			if (typeof(this.o_mapCodesCasSpeciaux[this.$cs1.val()]) !== 'undefined') {
				areCodesCasSpeciauxValid &= true;
			} else {
				areCodesCasSpeciauxValid = false;
			}
		}
		
		// code cas spécial 2
		if (this.$cs2.val() !== "") {
			if (typeof(this.o_mapCodesCasSpeciaux[this.$cs2.val()]) !== 'undefined') {
				areCodesCasSpeciauxValid &= true;
			} else {
				areCodesCasSpeciauxValid = false;
			}
		}
	
		// code cas spécial 3
		if (this.$cs3.val() !== "") {
			if (typeof(this.o_mapCodesCasSpeciaux[this.$cs3.val()]) !== 'undefined') {
				areCodesCasSpeciauxValid &= true;
			} else {
				areCodesCasSpeciauxValid = false;
			}
		}
		
		// code cas spécial 4
		if (this.$cs4.val() !== "") {
			if (typeof(this.o_mapCodesCasSpeciaux[this.$cs4.val()]) !== 'undefined') {
				areCodesCasSpeciauxValid &= true;
			} else {
				areCodesCasSpeciauxValid = false;
			}
		}
		
		// code cas spécial 5
		if (this.$cs5.val() !== "") {
			if (typeof(this.o_mapCodesCasSpeciaux[this.$cs5.val()]) !== 'undefined') {
				areCodesCasSpeciauxValid &= true;
			} else {
				areCodesCasSpeciauxValid = false;
			}
		}
	
		if (areCodesCasSpeciauxValid) {
			this.$imageKo.hide();
			this.$imageOk.show();
		} else {
			this.$imageOk.hide();
			this.$imageKo.show();
		}
	},
	
	init: function () {
		var that = this;

		this.$mapVerification = $('#mapVerification');
		
		$('span.necessaire').hide();
		$('.pendantValidation').hide();
		$('.bouttonAnnoncePonctuelle').hide();
		
		this.$cs1 = $('#cs1');
		this.$cs2 = $('#cs2');
		this.$cs3 = $('#cs3');
		this.$cs4 = $('#cs4');
		this.$cs5 = $('#cs5');
		
		this.$nssComplementaire1 = $('#nssComplementaire1');
		this.$nssComplementaire2 = $('#nssComplementaire2');
		
		this.$imageOk = $('#imageOKCodeCasSpeciaux');
		this.$imageKo = $('#imageKOCodeCasSpeciaux');
		
		this.$userAction = $('input[name="userAction"]');
		this.$form = $('form[name="mainForm"]');
		
		this.$genererAnnonceCheckbox = $('.genererAnnonceCheckbox');
		this.$genererAnnonceCheckbox.change(function () {
			that.checkBoutonAnnoncePonctuelle();
		});

		var $inputNecessitantUnCheckPourRentesLiees = $('.annonceRentesLieeSiModif');
		if (this.o_mapAnnonceRentesLieeSiModif === null) {
			this.o_mapAnnonceRentesLieeSiModif = {};
			$inputNecessitantUnCheckPourRentesLiees.each(function () {
				var $this = $(this);
				if ($this.attr('id')) {
					if ($this.attr('type') === 'checkbox') {
						that.o_mapAnnonceRentesLieeSiModif[$this.attr('id')] = $this.prop('checked');
					} else {
						that.o_mapAnnonceRentesLieeSiModif[$this.attr('id')] = $this.val();
					}
				}
			});
			this.$mapVerification.val(ajaxUtils.jsonToString(this.o_mapAnnonceRentesLieeSiModif));
		}
		$inputNecessitantUnCheckPourRentesLiees.change(function () {
			that.checkAnnonceRentesLiee();
		});
		
		this.b_firstCheckRenteLiee = true;
		
		$('.casSpeciaux').change(function () {
			that.metAJourCodeCasSpeciaux();
		});
		
		$('.labelTD').css('text-align', 'right');
		$('.interCollone').append('&nbsp;:&nbsp;').css('width', '1%');
		$('.1ereCollone').css('width', '25%');
		$('.2emeCollone').css('width', '10%');
		$('.3emeCollone').css('width', '25%');
		$('.4emeCollone').css('width', '38%');
		
		$('.mainContent').children('tr').css('height', '20px');
		var $checkboxContent = $('.checkboxContent');
		$checkboxContent.children('tbody').children('tr').css('height', '26px');
		$checkboxContent.children('.1ereCollone').css('width', '25%');
		$checkboxContent.children('.2emeCollone').css('width', '30%');
		$checkboxContent.children('.3emeCollone').css({'width' : '30%', 'align' : 'center'});

		this.metAJourCodeCasSpeciaux();
		this.checkAnnonceRentesLiee();
	}
};

$(document).ready(function () {
	annoncePonctuelle.init();
});

$('html').bind(eventConstant.JADE_FW_ACTION_DONE, function () {
	if (!annoncePonctuelle.b_hasError) {
		$('input').removeAttr('disabled');
		annoncePonctuelle.disableModifiableInput();
		annoncePonctuelle.$genererAnnonceCheckbox.attr('checked', true).change();
	}
});