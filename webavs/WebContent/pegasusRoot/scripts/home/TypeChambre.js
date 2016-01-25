TypeChambre = {
	$checkBoxParticularite : null;	
	init():function(){
		this.$checkBoxParticularite =$('[name=checkBoxParticularite]');
		this.afficheNss();
		$checkBoxParticularite.change(this.afficheNss);
	}	
	afficheNss : function (){
		($checkBoxParticularite.is(':checked'))? $checkBoxParticularite.show():$checkBoxParticularite.hide();
	}
}

$(function(){
	typeChambre = new TypeChambre();
	typeChambre.init();
})