
	//L'objet ci-dessous définit le comportement du message d'erreur en bas de page. Il lie la référence du BVR et le control du CCP	
	var bvrVal = true;
	var adrVal;
	var checker = {
			
		$idPaiement: null,
		$idDomaineApplicatif: null,
		$bouton: null,

		init: function ()  {
			this.$idDomaineApplicatif = $('#idDomaineApplicatif');
			
			var that = this;
			if($('#idAdressePaiement').length == 0){
				this.$idPaiement = $('#idTiers');
			}else{
				this.$idPaiement = $('#idAdressePaiement');
			}
					
			if($('.btnAjaxValidate').length == 0){
				this.$bouton = $('#btnVal');
			}else{
				this.$bouton = $('.btnAjaxValidate');
			}

			this.$idPaiement.change( function(){ 
	 				that.adresseChange(); 
	 			});	 	
	 		
	 		$('#idDomaineApplicatif').change( function(){
	 			that.adresseChange();
	 		});
	 		
	 		$('#numBVR').focusout(function () { 
	 			that.BVRChange(); 
	 			that.adresseChange(); 
	 			});
		},
		
		adresseChange: function ()  {
			var that = this;
			if(that.$idPaiement.val() && $('#idDomaineApplicatif').val()){
		 		var options = {
					serviceClassName: 'ch.globaz.perseus.business.services.bvr.BvrService',
					serviceMethodName: 'validationCCP',
					parametres: that.$idPaiement.val()+", "+ $('#idDomaineApplicatif').val(),
					callBack: function (data) {
						
						adrVal = data;
						that.controlMessageErreur();
					}
		 		};		
		 		ajax = Object.create($.extend(true, {}, globazNotation.readwidget));			
		 		ajax.options = options;
		 		ajax.read();
	 		}else{
	 			adrVal = false;
				that.controlMessageErreur();
	 		}
		},
		
		BVRChange: function ()  {
			var that = this;
			
			if($('#numBVR').val() == ""){
				bvrVal = true;
				adrVal = true;
				that.controlMessageErreur();
			}else{
			
		 		var options = {
		 				serviceClassName: 'ch.globaz.perseus.business.services.bvr.BvrService',
		 				serviceMethodName: 'validationNumeroBVR',
		 				parametres: $('#numBVR').val(),
		 				callBack: function (data) {
		 					
		 					bvrVal = data;
		 					that.controlMessageErreur();
		 				}
		 			}
				ajax = Object.create($.extend(true, {}, globazNotation.readwidget));			
		 		ajax.options = options;
		 		ajax.read();
			}
		},
		
		controlMessageErreur: function() {
			var that = this;
			if(adrVal && bvrVal){
				that.$bouton.fadeIn('slow');
				$('#erreurVersement').fadeOut('fast');
				$('#erreurVersementCCP').fadeOut('fast');
				$('#erreurVersementBVR').fadeOut('fast');
			}else if(adrVal && !bvrVal){
				that.$bouton.fadeOut('slow');
				$('#erreurVersement').fadeIn('slow');
				$('#erreurVersementCCP').fadeOut('fast');
				$('#erreurVersementBVR').fadeIn('slow');
			}else if(!adrVal && bvrVal){
				if($('#numBVR').val() === ""){
					that.$bouton.fadeIn('slow');
					$('#erreurVersement').fadeOut('fast');
					$('#erreurVersementCCP').fadeOut('fast');
					$('#erreurVersementBVR').fadeOut('fast');
				}else{
					that.$bouton.fadeOut('fast');
					$('#erreurVersement').fadeIn('slow');
					$('#erreurVersementCCP').fadeIn('slow');
					$('#erreurVersementBVR').fadeOut('fast');
				}
			}else if(!adrVal && !bvrVal){
				that.$bouton.fadeOut('fast');
				$('#erreurVersement').fadeIn('slow');
				$('#erreurVersementCCP').fadeIn('slow');
				$('#erreurVersementBVR').fadeIn('slow');
			}
		}
	} 
