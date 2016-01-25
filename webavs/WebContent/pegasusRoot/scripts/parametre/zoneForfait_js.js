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
       $formConversion.find('[name=userAction]').val(url + ".ajouter");
    else
       $formConversion.find('[name=userAction]').val(url + ".modifier");
    return state;
  }
  
  function isAdd(){
  	return $formConversion.find('[name=_method]').val() == "add";
  }

  function cancel() {
	if ($formConversion.find('_method').val())
     $formConversion.find('[name=userAction]').val("back")
    else
      $formConversion.find('[name=userAction]').val(url + ".afficher");
  }

  function del() {
    if (window.confirm(messageDelete)){
		  $formConversion.find('[name=userAction]').val(url + ".supprimer");
		  $formConversion.submit();
    }
  }
  	
  function init(){
  }
  
  function readOnly(flag) {
	  var $this = null;
	$("input:not(:button,:hidden),select").each(function(){
		$this = $(this);
		$this.attr('disabled',flag);
		$this.attr('readOnly',flag);
	});
  }
 
$(function(){
	$formConversion=$('[name=mainForm]');
});
  