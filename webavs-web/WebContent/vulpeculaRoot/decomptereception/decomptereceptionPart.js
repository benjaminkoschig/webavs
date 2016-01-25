var GLO = GLO || {};

$(function() {	
	GLO.decomptereception.init();
});

function add () {
}

function upd() {
}

function cancel() {
}

function del() {
}

function init(){
}

function validate() {
	GLO.decomptereception.submitReceptionDecompte();
}

GLO.decomptereception = {
		$idDecompte : null,
		$employeur : null,
		$noDecompte : null,
		$description : null,
		$typeDecompte : null,
		$dateReception : null,
		
		$sucessMessage : null,
		
		b_changed : false,
		b_found : false,
		b_lock : false,
		
		idDecompte : null,
		dateReception : null,
		
		keys : {
			ENTER : 13
		},
		
		errors : {
			DEJA_RECEPTIONNE : 'DEJA_RECEPTIONNE',
			VALIDE : 'VALIDE',
			NON_VALIDE : 'NON_VALIDE',
			NON_EXISTANT : 'NON_EXISTANT'
		},
		
		init : function() {
			var that = this;
			
			that.$idDecompte = $('#idDecompte'),
			that.$employeur = $('#employeur'),
			that.$noDecompte = $('#noDecompte'),
			that.$description = $('#description'),
			that.$typeDecompte = $('#typeDecompte'),
			that.$dateReception = $('#recuLe'),
			that.$sucessMessage = $('#success');
			
			that.$idDecompte.focusout(function() {
				that.retrieveValues();
				
				if(!that.isSearchable()) {
					that.resetForm();
				} else {
					that.searchIfChanged();
				}
			});
			
			that.$idDecompte.keyup(function(event) {
				if(event.which!==that.keys.ENTER) {
					that.b_changed = true;
				}
			});
			
			$(document).keyup(function(event) {
				that.retrieveValues();
				
				if(event.which===that.keys.ENTER) {
					if(!that.isSearchable()) {
						that.resetForm();
					} else {
						that.searchOrSubmitIfChanged();
					}
				}
			});
			
			that.$idDecompte.change(function() {
				that.b_changed = true;
			});		
		},
		retrieveValues : function() {
			this.idDecompte = this.getIdDecompte();
			this.dateReception = this.getDateReception();			
		},
		submitReceptionDecompte : function () {
			var that = this;
			if(that.isValidForm()) {
				var options = {
						serviceClassName:globazGlobal.decompteService,
						serviceMethodName:'setDateReception',
						parametres:that.idDecompte+","+that.dateReception,
						callBack:function (data) {
							if(that.errors.VALIDE===data) {
								that.showSuccessInformation(that.idDecompte, that.dateReception);
							} else if(that.errors.DEJA_RECEPTIONNE) {
								that.showDejaReceptionneInformation(that.idDecompte);
							} else {
								that.showNonExistantInformation(that.idDecompte);
							}
							that.resetFormAndFocusIdDecompte();
							that.b_found = false;
						}
				};
				vulpeculaUtils.lancementService(options);	
			}
		},
		
		searchDecompte : function() {
			if(!this.isSearchable()) return;
			var that = this;
			var options = {
					serviceClassName:globazGlobal.decompteService,
					serviceMethodName:'getDecompteById',
					parametres:this.idDecompte,
					callBack:function (data) {
						if($.isNumeric(data.idDecompte)) {
							that.b_found = true;
							that.$employeur.val(data.employeur);
							that.$noDecompte.val(data.noDecompte);
							that.$typeDecompte.val(data.typeDecompte);
							that.$description.val(data.descriptionDecompte);
						} else {
							that.resetForm();
							that.showNonExistantInformation(that.idDecompte);
						}
					}
			};
			vulpeculaUtils.lancementService(options);	
		},
		
		searchOrSubmitIfChanged : function() {
			if(this.b_changed) {
				this.b_changed = false;
				this.searchDecompte();
			} else if(this.b_found) {
				this.submitReceptionDecompte();
				this.b_found = false;
			}
		},
		
		searchIfChanged : function() {
			if(this.b_changed) {
				this.searchDecompte();
				this.b_changed = false;			
			}
		},

		clearFields : function() {
			this.$idDecompte.val('');
			this.$employeur.val('');
			this.$noDecompte.val('');
			this.$description.val('');
			this.$typeDecompte.val('');
		},
		resetFormAndFocusIdDecompte : function() {
			this.resetForm();
			this.$idDecompte.focus();
		},
		resetForm : function() {
			this.clearFields();
			this.b_changed = false;
			this.b_found = false;
		},
		getIdDecompte : function() {
			return this.$idDecompte.val();
		},
		getDateReception : function() {
			return this.$dateReception.val();
		},
		isValidForm : function() {
			return ($.isNumeric(this.idDecompte) && this.dateReception!=='');
		},
		isSearchable : function() {
			return ($.isNumeric(this.idDecompte));
		},
		showSuccessInformation : function(idDecompte, dateReception) {
			var label = globazGlobal.decompteMiseAJourLabel;
			label = label.replace(':id',idDecompte);
			label = label.replace(':dateReception',dateReception);
			$('#informations').noty({
				type: 'success',
				timeout: 3000,
				maxVisible : 3,
				text: label});
		},
		showNonExistantInformation : function(idDecompte) {
			var label = globazGlobal.decompteNonExistant;
			label = label.replace(':id',idDecompte);
			$('#informations').noty({
				type: 'error',
				timeout: 3000,
				maxVisible : 3,
				text: label});
		},
		showDejaReceptionneInformation : function(idDecompte) {
			var label = globazGlobal.decompteDejaReceptionne;
			label = label.replace(':id',idDecompte);
			$('#informations').noty({
				type: 'error',
				timeout: 3000,
				maxVisible : 3,
				text: label});
		}		
};
