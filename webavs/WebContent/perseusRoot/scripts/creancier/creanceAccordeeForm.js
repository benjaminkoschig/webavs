  var $mainForm = null;
  
  function add() {
  	$mainForm.find('name="userAction" ').val(url+".ajouter");
    //document.forms[0].target = "fr_main";
  }
  
  function upd() {
  }

  function validate() {
    state = true;
	if (isAdd()){
       $mainForm.find('[name=userAction]').val(url + ".ajouter");
	}else{
       $mainForm.find('[name=userAction]').val(url + ".modifier");
	}
	if(!creanceAccordee.isRepartitionCorrect()){
		state = false;
	}
    return state;
  }
  
  function isAdd(){
  	return $mainForm.find('[name=_method]').val() == "add";
  }

  function cancel() {
	//if ($mainForm.find('_method').val())
	  //$mainForm.attr('target','fr_main')
	  //document.forms[0].target = "fr_main";
     //$mainForm.find('[name=userAction]').val("back");
  //  else

      $mainForm.find('[name=userAction]').val(url+ ".chercher");
  }

  function del() {
    if (window.confirm(messageDelete)){
		  $mainForm.find('[name=userAction]').val(url + ".supprimer");
		  $mainForm.submit();
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
	$mainForm=$('[name=mainForm]');
});
  

