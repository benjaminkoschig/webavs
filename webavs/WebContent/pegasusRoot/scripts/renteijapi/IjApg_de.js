/**************************************************************
 * Script des comportements de l'écran Autre api
 * 
 * DMA
 */

function add() {}
function upd() {}	
function validate() {}    
function del() {}
function cancel() {}

function init(){}
function postInit(){
	$('.btnAjax input').attr('disabled',false);	
}
var addEnvent ;

chekForm =  {
	addEvent : true,
	mandatoryToCheck : null,
		
	init:function(addEvent){
		that = this;
		if (addEvent) {
			this.addEvent = addEvent;
		}
		//$('.numToCheck').each(function(){that.checkInteger(this);})
		$('.checkMandatory').each(function(){
				that.addMandatory(this);
			})
	},
	
	
	addMandatory : function (objToChek){
		var $obj = $(objToChek);
		this.addSymboleMandatory($obj);
		//this.checkMandatory($obj);
	},
	
	addSymboleMandatory: function ($obj){
		$obj.after('<span class="simboleMandatory">*</span>');
	},
	
	checkInteger : function (objToChek){
		$(objToChek).keypress(function(event){
			return filterCharForInteger(event)
		})
	},
	
	changeColor : function(ok,obj){
		//(ok)?obj.addClass('isValueOk').removeClass('isValueKo'):obj.addClass('isValueKo').removeClass('isValueOk');
		(ok)?obj.removeClass('isValueKo'):obj.addClass('isValueKo').removeClass('isValueOk');
	},
	
	checkMandatory :function ($objToChek){
		that = this;
		//if (!(obj.val())) {
			that.changeColor($objToChek.val(),$objToChek);
		//}
		
		if (this.addEvent) {
			$objToChek.bind('change', function(){
				that.changeColor($objToChek.val(),$objToChek);
			});
			$objToChek.keyup(function(event){
				that.changeColor($objToChek.val(),$objToChek);

			})
		}
	
	}
}



$(function(){
	//chekForm.init();
	//$('.libelleLong').css('background','url(/webavs/images/ok.gif)');
	//$('.libelleLong').css({'border-left': '10px solid #356AA0' , 'border-bottom' : 'solid 0.1px #356AA0','border-top' : 'solid 0.1px #356AA0','border-right' : 'solid 0.1px #356AA0'});

	/*$('.numToCheck').each(function(){
		$(this).keypress(function(event){
			return filterCharForInteger(event)
		})
		addEnvent.addEnvent.init(); 
		//validateFloatNumber(this);
		//filterCharForFloat(window.event);
	} )*/
});



