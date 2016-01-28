/**
 * @author DMA
 */

var creancier = {
	init: function () {
		this.addEvent();
	},
	addEvent: function ()  {
		var that= this;
		$('#idTiers').change(function(){
			that.readAdresse(this.value);
		});
	},
	displayAdresse: function(data){
		var html = "";	
		if(typeof data.adresseFormate !=="undefined"){
			html =  data.adresseFormate.replace(/[\r\n]/g,'<br />');
		}else{
			html = '<label class="errorText">'+NO_ADRESSE+'</label>';
		}
		$('.adresse').html(html);
		//globazNotation.adresse.createAdresse($data);
	},

	readAdresse: function(idTiers){
		var that = this;	
		var options = {
			serviceClassName:'ch.globaz.pyxis.business.service.AdresseService',
			serviceMethodName:'getAdressePaiementTiers',
			parametres:idTiers+",true,"+CS_DOMAINE_APPLICATION_RENTE+",20101101,0",
			callBack:that.displayAdresse
		}
		globazNotation.readwidget.options = options;
		globazNotation.readwidget.read();
	}
		
}

//fonction d'initialisation de la page lorsque JQuery est prêt
$(function(){
	creancier.init();	
});