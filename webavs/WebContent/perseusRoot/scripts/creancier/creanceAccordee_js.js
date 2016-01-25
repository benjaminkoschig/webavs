/**
 * @author DMA
 */

creanceAccordee = {
	init: function () {
		this.addEvent();
		this.initCalculDiff();
		this.filInput();
	},
	
	getMontantRembourse: function (id) {
		return this.amountTofloat($("#montantRembourse_"+id).text());
	},
	
	filInput: function () {
		var that = this;
		var $boutton;
		var i = 0;
		$(".inputMontant").each(function () {
			var o_thisInput = this;
			$boutton = $('<img>',{
				 src: $('[name=Context_URL]').attr('content')+"/images/compenser.gif",
				 click: function (){
					 		var id = that.getId(o_thisInput),
					 			$this = $(o_thisInput),
					 		    $diff = $('#diff_'+id),
					 		    n_sommeHorizontal = 0,
					 			$tr = $this.closest('tr'),
					 			n_soldeDisponible = that.amountTofloat($tr.find('.soldeDisponible').text()) + that.amountTofloat($this.val());
					 		    
					 		$tr.find("input:text").each(function () {
					 			if ($this.attr('id')!= $(this).attr('id')){
					 				n_sommeHorizontal = n_sommeHorizontal +  that.amountTofloat(this.value);
					 				i = i+1;
					 			}
					 		}); 
					 		
					 		var n_montantARembourser = (that.getMontantRevendique(id) - that.getMontantRembourse(id));
					 		var n_montantDisponible = n_montantARembourser;
					 	
					 		if (n_montantARembourser > n_soldeDisponible){
					 			n_montantDisponible = n_soldeDisponible;
					 		}

					 		var n_montantRetroactif =  that.amountTofloat($tr.find(".montantRetroactif").text());
					 		var n_montantDiff = that.amountTofloat($diff.text());
	
					 		if (((n_montantDisponible + n_sommeHorizontal) <= n_montantRetroactif)){
					 			if(n_montantDisponible>n_montantDiff + that.amountTofloat($this.val()) ){
					 				n_montantDisponible = n_montantDiff + that.amountTofloat($this.val());
					 			}
					 		}else {
					 			n_montantDisponible = n_montantRetroactif - n_sommeHorizontal;
					 		}
					 		
					 		if(n_montantDisponible>0){
					 			$this.val(n_montantDisponible);
					 		}
					 		$this.change();
					 	}
			});
			$(this).before($boutton);
		});
	},
	
	getId:function (o_element){
		return o_element.id.split('_')[1];
	},
	
	addButtonRepartirCreance: function(){
		$('<input/>',{
			type: 'button',
			click: function() {
				$$c('tt');
			},
			"class": 'btnCtrl'
		}).appendTo('#btnCtrlJade');
	},	
	
	addEvent: function () {
		var that = this, id;
		$(".total").change(function () {
			id = that.getId(this);
			that.calculDiff($('#diff_'+id));
		});
	},
	
	amountTofloat: function (s_amount){ 
		var n_amount = 0;
	//	if(!this.utils.isEmpty(s_amount)){
			n_amount= parseFloat(s_amount.replace("'",""));
		//}
		return n_amount;
	},
	
	getMontantRevendique: function (idCreancier){
		return this.amountTofloat($("#creancierMontant_"+idCreancier).text());
	},
	
	calculDiff: function ($element) {
		var id="", n_montant, n_sum ;
		id = this.getId($element[0]);
		n_montant = this.getMontantRevendique(id) - this.getMontantRembourse(id);
		n_sum = this.amountTofloat($("#total_"+id).text());
		$element.text(n_montant-n_sum);
		if(n_montant-n_sum < 0){
			$element.addClass('errorText');
		}else{
			$element.removeClass('errorText');
		}
		$element.change();
	},
	
	initCalculDiff: function() {
		var that = this;
		$(".diff").each(function(){
			that.calculDiff($(this));
		});
	},
	
	isRepartitionCorrect: function(){
		var n_num =0, that = this;
		var b_retour = true;
		$('#difference td,.soldeDisponible').each(function () {
			n_num = that.amountTofloat($(this).text());
			if(n_num<0 && b_retour === true){
				globazNotation.utils.consoleError(s_repartionNonCorrect);
				b_retour = false;
			}
		});
		return b_retour;
	}
	
}

$(function (){
	creanceAccordee.init();
});