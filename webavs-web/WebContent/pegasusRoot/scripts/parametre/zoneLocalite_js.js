  var $mainForm = null;
  
  function add() {
  	$mainForm.find('name="userAction" ').val(url+".ajouter");
    document.forms[0].target = "fr_main";
  }
  
  function upd() {
  }

  function validate() {
    state = true;
	if (isAdd())
       $mainForm.find('[name=userAction]').val(url + ".ajouter");
    else
       $mainForm.find('[name=userAction]').val(url + ".modifier");
    return state;
  }
  
  function isAdd(){
  	return $mainForm.find('[name=_method]').val() == "add";
  }

  function cancel() {
	if ($mainForm.find('_method').val())
     $mainForm.find('[name=userAction]').val("back")
    else
      $mainForm.find('[name=userAction]').val(url + ".afficher");
  }

  function del() {
    if (window.confirm(messageDelete)){
		  $mainForm.find('[name=userAction]').val(url + ".supprimer");
		  $mainForm.submit();
    }
  }
  	
  function init(){
	  var $localite = $("#localite");
		
	  //si il y pas de valeur pour rente et qu'il une valeur age alors je met le focus
		 if(!$localite.val().length && ($("[name=zoneLocalite.simpleLienZoneLocalite.idZoneForfait]").val().length)){
			 $localite.focus();
			setTimeout(function(){
				$localite.focus()
			},1);
		}
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
	
	if (isAdd()){
		  $('<input/>',{
		  	id:'btnAddWithValue',
			type:'button',
			value: messageButtonValidContinue,
			click :function(){
				    if (validate('widtZoneAndDate')){
						$mainForm.find('#useValiderContinuer').val('true');
						action(COMMIT);
					}
			}
		  }).prependTo('#btnCtrlJade')
		 }
});
  