var url= 'perseus.variablemetier.variableMetier';
  
  function add() {
    document.forms[0].elements('userAction').value=url+".ajouter";
  }
  
  function upd() {
  }

  function validate() {
    state = true;
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value = url + ".ajouter";
    else
        document.forms[0].elements('userAction').value = url + ".modifier";
    return state;
  }

  function cancel() {
    if (document.forms[0].elements('_method').value == "add")
      document.forms[0].elements('userAction').value="back";
    else
      document.forms[0].elements('userAction').value = url + ".afficher";
  }

  function del() {
    if (window.confirm(messageDelete)){
        document.forms[0].elements('userAction').value = url + ".supprimer";
        document.forms[0].submit();
    }
  }
  	
  function init(){
    // recharger la page rcListe du parent si une modification a ete effectuee
	/*<%if ("new".equalsIgnoreCase(request.getParameter("_valid"))) {%>
	  	// mise a jour de la liste du parent
		if (parent.document.forms[0]) {
			parent.document.forms[0].submit();
		}
	<%}%>*/
  }
  
  function readOnly(flag) {
  	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
    for(i=0; i < document.forms[0].length; i++) {
        if (!document.forms[0].elements[i].readOnly && 
        	document.forms[0].elements[i].className != 'forceDisable' &&
        	document.forms[0].elements[i].type != 'hidden') {
            document.forms[0].elements[i].disabled = flag;
            //document.forms[0].elements[i].readOnly = flag;
            
        }
    }
  }
  
  

  // code pour vider les autres champs lorsqu'un champ est modifié
$(function(){
	$('span.valueNumeric').each(function(){
		var that=this;
		this.videChamps=function(){
			$(this).children('input').attr('value','');
		}
		$(this).children('input').change(function(){
			$(that).change();
		});
	});
	
	$('input.valueNumeric').each(function(){
		this.videChamps=function(){
			this.value='';
		}
	});

	$('.valueNumeric').change(function(){
		var that=this;
		$('.valueNumeric').each(function(){
			if(this!=that) this.videChamps();
		});
	});
	
});
  