/**************************************************************
 * Script des comportements de l'écran bien immobilier non habitable
 * 
 * FHA
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
	var partFormatter=/\s*(\d+)\s*\/?\s*(\d+)?\s*/;
	
	$('.part').change(function(){
		var result=partFormatter.exec(this.value);
		if(result!=null){
			this.value=result[1]+" / "+(result[2]!=""?result[2]:"1");
		}else{
			this.value=("1 / 1");
		}
	});
});