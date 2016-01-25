/**************************************************************
 * Script des comportements de l'écran Autre rente
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


$(function(){
	$(".monnaie").hide();
	$(".monnaie1").hide();

	var partFormatter=/\s*(\d+)\s*\/?\s*(\d+)?\s*/;
	
	$('.part').change(function(){
		var result=partFormatter.exec(this.value);
		if(result!=null){
			this.value=result[1]+" / "+(result[2]!=""?result[2]:"1");
		}else{
			this.value=("1 / 1");
		}
	});
	
	$('.csGenre').change(function(){
		var o = $(this).parent().parent().parent().find('.monnaie1');
		var p = $(this).parent().parent().parent().find('.monnaie');
		//alert($(this).parent().parent().parent().find('.monnaie1').html());
		if($.trim(this.value) == '64018003'){
			//alert('etrangere');
			o.show();
			p.show();
			
			
		} else{
			o.hide();
			p.hide();
		}
	});

});