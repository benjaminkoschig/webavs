var blobIsVisble = false;
var $blobZone;
var $btnBlob;
var $btnPrint;

$(function(){
	//si pas de conjoint
	if($('#infoConjoint').html()===""){
		$('#conjointInfos').hide();
	}
	//si pas d'enfant
	if(!$('.enfantpremier').length){
		$('#enfantsInfos').hide();
	}
	
	top.document.title="Web@Prestations - Plan de calcul PC";
	
	
});
function initPage(){
	$blobZone = $('#blobZone');
	$btnPrint = $('#btnPrint');
	$('table').find('tr').find('td:eq(1),td:eq(3),td:eq(5)').css('padding-left','5px');
	$blobZone.hide();
	formatPage();
	$btnBlob = $("<button/>").html("blob").click(function(){toggleBlob();}).insertAfter($blobZone);
}
function formatPage(){
	// formatte blob
	var text=$blobZone.html();
	var pattern=new RegExp("(\([^)]+\)[^,(]+)","g");
	var text1=text.replace(pattern,"<br>$1");
	$blobZone.html(text1);
}
function printPage(){
	$btnPrint.hide();
	$btnBlob.hide();
	window.print();
	$btnPrint.show();
	$btnBlob.show();
}
function toggleBlob(){
	$blobZone.toggle();
}
