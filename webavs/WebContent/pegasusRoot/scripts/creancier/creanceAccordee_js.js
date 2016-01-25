/**
 * @author DMA
 */

creanceAccordee = {
	IMG_BUBBLE_DISPLAY_ON_ERROR : "/webavs/images/small_m_error.png",
		init: function () {
		this.addEvent();
		this.initCalculDiff();
		this.filInput();
		
	},
	
	 //on efface la derniere colone
	clearColoneDispo : function () {
		$('.totalSommeRepartis').each(function () {
			$(this).html("&nbsp;");	
		});
	},
	  
	dealRetroError: function ($mntRetroCell,n_mntSolde,$elementToPutObject){
		 
		if(n_mntSolde<0){
			this.dealVisualErrorBubble($mntRetroCell,$elementToPutObject,n_mntSolde,true);
			return true;
		}
		else {
			$mntRetroCell.removeClass('red');
			return false;
		}
	},
	
	
	
	dealDispoError : function ($maxDispoCell,n_mntSolde,s_mntRetro,s_maxDispo,$elementToPutObject) {
		var n_mntRetro = this.amountTofloat(s_mntRetro);
		var n_maxDispo = this.amountTofloat(s_maxDispo);
		var n_totalDeduit = n_mntRetro - n_mntSolde;
		
		if(n_totalDeduit>n_maxDispo){
			this.dealVisualErrorBubble($maxDispoCell,$elementToPutObject,n_mntSolde,false);
			return true;
		}else{
			$maxDispoCell.removeClass('red');
			return false;
		}
	},
	
	dealDiffError : function () {
		var isOnError = false;
		var that = this;
		$(".diff").each(function(){
			if(!isOnError){
				isOnError = that.calculDiff($(this));
			}else{
				that.calculDiff($(this));
			}
			
		});
		return isOnError;
	},
	dealVisualErrorBubble : function ($maxDispoCell,$elementToPutObject,n_mntSolde,b_forRetro) {
		
		var s_mntSolde = globazNotation.utilsFormatter.formatStringToAmout(Math.abs(n_mntSolde));
		
		var s_errorTexte = '';
		if(b_forRetro){
			s_errorTexte = jQuery.i18n.prop('notation.cellsum.depassementretro');
		}else{
			s_errorTexte = jQuery.i18n.prop('notation.cellsum.depassementdispo');
		}
		
		var $img = $('<img/>',{
			'src':this.IMG_BUBBLE_DISPLAY_ON_ERROR
		});
		var $span = $('<span/>',{
			'data-g-bubble':'wantMarker:false,text:¦'+s_errorTexte+" "+s_mntSolde+'¦',
			'class':'bubbleErrorSpan'
		});
		$span.appendTo($elementToPutObject).append($img);
		$maxDispoCell.addClass('red');
		
//		$maxDispoCell.animate({
//			    opacity: 0.25,
//			    left: '+=50',
//			    height: 'toggle'
//			  }, 5000, function() {
//			    // Animation complete.
//			 });
		
		//$maxDispoCell.effect("highlight", {}, 3000);
		notationManager.addNotationOnFragment($span);
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
		var isOnError = false;
		var id="", n_montant, n_sum ;
		id = this.getId($element[0]);
		n_montant = this.getMontantRevendique(id) - this.getMontantRembourse(id);
		n_sum = this.amountTofloat($("#total_"+id).text());
		$element.text(n_montant-n_sum);
		if(n_montant-n_sum < 0){
			$element.addClass('errorText');
			isOnError = true;
		}else{
			$element.removeClass('errorText');
			isOnError = false;
		}
		$element.change();
		return isOnError;
	},
	
	initCalculDiff: function() {
		var that = this;
		$(".diff").each(function(){
			that.calculDiff($(this));
		});
	},
	
	isRepartitionCorrect: function(){
		that = this;
//		var b_retour = true;
//		$('#difference td,.soldeDisponible').each(function () {
//			n_num = that.amountTofloat($(this).text());
//			alert(n_num);
//			if(n_num<0 && b_retour === true){
//				globazNotation.utils.consoleError(s_repartionNonCorrect);
//				b_retour = false;
//			}
//		});
		var n_num = that.amountTofloat($('#totalDisponible').text());
		if(n_num<0){
			globazNotation.utils.consoleError(s_repartionNonCorrect);
			return false;
		}
		return true;
	}
	
}

$(function (){
	creanceAccordee.init();
});