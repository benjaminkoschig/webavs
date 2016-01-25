
function cocherGED(checkbox, value) {
	if (document.getElementsByName(value)[0]) {
		if (!checkbox.checked) {
			document.getElementsByName(value)[0].checked = false;
			document.getElementsByName(value)[0].disabled = true;
		} else {
			document.getElementsByName(value)[0].checked = true;
			document.getElementsByName(value)[0].disabled = false;
		}	
	}
}

var object = {
	$toutGenererListe : null,
	$toutGenererDoc : null,
	$toutGenererGed : null,
	$listeBox : null,
	$docBox : null,
	$gedBox : null,
	
	init: function () {
		this.$toutGenererListe = $('#toutGenererListe');
		this.$toutGenererDoc = $('#toutGenererDoc');
		this.$toutGenererGed = $('#toutGenererGed');
		
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
			that.checkGenererDoc();
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
		var checked;

		if (!this.$toutGenererDoc.prop('checked')) {
			checked = false;
		} else {
			checked = true;
		}
		
		this.$docBox.each(function (i) {
			var $that = $(this);
			if (checked) {
				$that.prop('checked', checked);
			} else {
				$that.removeProp('checked');
			}
			
			var name = $that.attr('name');
			var id = name.charAt(5);
			
			cocherGED(this, "valu_" + id + "GED");
		});
		
		this.checkGenererDoc();
	},

	checkGenererDoc: function () {
		var checked = true;
		
		this.$docBox.each(function () {
			var $that = $(this);
			
			if (!$that.prop('checked')) {
				checked = false;
			}
		});
		
		if (checked) {
			this.$toutGenererDoc.prop('checked', checked);
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
		
		this.$gedBox.each(function (i) {
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
	}
};

$(document).ready(function () {
	object.init();
});


