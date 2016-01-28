  var $formConversion = null;
  
  function add() {
  	$formConversion.find('name="userAction" ').val(url+".ajouter");
    document.forms[0].target = "fr_main";
  }
  
  function upd() {
  }

  function validate() {
    state = true;
	if (isAdd())
       $formConversion.find('[name="userAction"]').val(url + ".ajouter");
    else
       $formConversion.find('[name="userAction"]').val(url + ".modifier");
    return state;
  }
  
  function isAdd(){
  	return $formConversion.find('[name="_method"]').val() == "add";
  }

  function cancel() {
	if ($formConversion.find('_method').val())
     $formConversion.find('[name="userAction"]').val("back")
    else
      $formConversion.find('[name="userAction"]').val(url + ".afficher");
	 
  }

  function del() {
    if (window.confirm(messageDelete)){
		  $formConversion.find('[name="userAction"]').val(url + ".supprimer");
		  $formConversion.submit();
    }
  }
  	
  function init(){
	  var $rentHomme = $("#renteHomme");
	
	  //si il y pas de valeur pour rente et qu'il une valeur age alors je met le focus
		 if(!$rentHomme.val().length && ($("[name='conversionRente\\.simpleConversionRente\\.age']").val().length)){
			 $rentHomme.focus();
			setTimeout(function(){
				$rentHomme.focus()
			},1);
		}
  }
  
  function readOnly(flag) {
	$("input:not(:button,:hidden,[name='annee'])").each(function(){
		$(this).attr('disabled',flag);
		$(this).attr('readOnly',flag);
	});
  }
 
$(function(){
	
	$formConversion=$('[name="mainForm"]');
	if (isAdd()){
	  $('<input/>',{
	  	id:'btnAddWithValue',
		type:'button',
		value: messageButtonValidContinue,
		click :function(){
			    if (validate('widtValueAnneAge')){
					$formConversion.find('#valueAnneAge').val('true');
					action(COMMIT);
				}
		}
	  }).prependTo('#btnCtrlJade');
	 }
  
});
  