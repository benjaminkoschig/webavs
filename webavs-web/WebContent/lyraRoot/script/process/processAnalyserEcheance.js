var processAnalyserEcheance = {

	$userChoices: null,
	$toutGenererListe : null,
	$toutGenererDoc : null,
	$toutGenererGed : null,
	$listeBox : null,
	$docBox : null,
	$gedBox : null,

	init: function () {
		this.$userChoices = $('.userChoice');
		this.$userChoices.filter(':odd').addClass('odd');

		this.$toutGenererListe = $('#toutesLesListes');
		this.$toutGenererDoc = $('#toutesLesLettres');
		this.$toutGenererGed = $('#toutesLesGED');

		this.$listeBox = $('.listeBox');
		this.$docBox = $('.docBox');
		this.$gedBox = $('.gedBox');

		this.bindEvent();

		this.checkGenererListe();
		this.checkGenererDoc();
		this.checkGenererGed();
	},

	bindEvent: function () {
		var that = this;
		this.$toutGenererListe.click(function () {
			that.switchGenererListe();
		});
		this.$toutGenererDoc.click(function () {
			that.switchGenererDoc();
		});
		this.$toutGenererGed.click(function () {
			that.switchGenererGed();
		});
		
		this.$listeBox.click(function () {
			that.checkGenererListe();
		});
		
		this.$docBox.click(function () {
			that.checkGenererDoc($(this));
		});
		
		this.$gedBox.click(function () {
			that.checkGenererGed();
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

	switchGenererDoc: function () {
		var that = this;
		var checked;

		if (!this.$toutGenererDoc.prop('checked')) {
			checked = false;
		} else {
			checked = true;
		}

		this.$docBox.each(function () {
			var $this = $(this);
			if (checked) {
				$this.prop('checked', checked);
			} else {
				$this.removeProp('checked');
			}
			that.cocherGED($('#' + $this.attr('id').replace('lettre', 'GED')), checked);
		});

		this.checkGenererDoc();
	},

	checkGenererDoc: function ($checkBox) {
		var allChecked = true;

		if (typeof($checkBox) !== 'undefined') {
			var checked = false;
			if ($checkBox.prop('checked')) {
				checked = true;
			}
			this.cocherGED($('#' + $checkBox.attr('id').replace('lettre', 'GED')), checked);
		}

		this.$docBox.each(function () {
			var $that = $(this);

			if (!$that.prop('checked')) {
				allChecked = false;
			}
		});

		if (allChecked) {
			this.$toutGenererDoc.prop('checked', allChecked);
		} else {
			this.$toutGenererDoc.removeProp('checked');
		}
		if (this.$toutGenererGed !== null) {
			this.checkGenererGed();
		}
	},

	switchGenererGed: function () {
		var checked;

		if (!this.$toutGenererGed.prop('checked')) {
			checked = false;
		} else {
			checked = true;
		}

		this.$gedBox.each(function () {
			var $that = $(this);
			if (checked && !$that.prop('disabled')) {
				$that.prop('checked', checked);
			} else {
				$that.removeProp('checked');
			}
		});

		this.checkGenererGed();
	},

	checkGenererGed: function () {
		var checked = true;
		var allDisabled = true;

		this.$gedBox.each(function () {
			var $that = $(this);

			if (!$that.prop('disabled')) {
				if (!$that.prop('checked')) {
					checked = false;
				}
				allDisabled = false;
			}
		});

		if (checked && !allDisabled) {
			this.$toutGenererGed.prop('checked', checked);
			this.$toutGenererGed.removeProp('disabled');
		} else {
			this.$toutGenererGed.removeProp('checked');

			if (allDisabled) {
				this.$toutGenererGed.prop('disabled', true);
			}
		}
	},

	cocherGED: function ($checkboxGED, checked) {
		if (checked) {
			$checkboxGED.removeProp('disabled');
			$checkboxGED.prop('checked', true);
		} else {
			$checkboxGED.prop('disabled', true);
			$checkboxGED.removeProp('checked');
		}
	}
};

function getParametresSupplementaires() {
	var o_params = {};

	processAnalyserEcheance.$listeBox.each(function () {
		o_params[this.name] = (this.checked ? true : false);
	});

	processAnalyserEcheance.$docBox.each(function () {
		o_params[this.name] = (this.checked ? true : false);
	});

	processAnalyserEcheance.$gedBox.each(function () {
		o_params[this.name] = (this.checked ? true : false);
	});
	
	return o_params;
}

setTimeout(function () {
	processAnalyserEcheance.init();
}, 100);