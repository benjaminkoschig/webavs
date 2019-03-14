/**
 * @author AGE
 */

$(function () {
	var reading = true;
	var t_zone = defaultTableAjax.init({
		s_actionAjax: globazGlobal.ACTION_AJAX,
		
		init: function () {	
			this.list(20, []);
			this.addSearch();
			this.mainContainer.on("click",".deleteLink", function (e){
				return handleDeleteLink();
			});
			this.mainContainer.on("click",".addLink", function (e){
				handleAddLink();
			});
			this.mainContainer.on("hover","tr.row", function(e) {
				$(this).toggleClass("odd ajaxHighlighted");
			});
		},
		
		getParametresForFind: function () {
			return {"searchModel.forIdRecap": globazGlobal.ID_RECAP_MODEL};
		}
	});

	
	/* D�sactivation des boutons radio lorsque l'AJAX est complet. Actuellement 
	 *non impl�ment� car ces cases ne sont utilis�s qu'avec le bouton "+" qui est lui 
	 *accessible m�me lorsque la modification est d�sactiv�e */
	/*t_zone[0].mainContainer.on(eventConstant.AJAX_FIND_COMPLETE, function () {
		$("input[name='idDossierSelected']").prop("disabled", true);
	});*/
	
	
});

function add() {
    document.forms[0].elements('userAction').value="al.prestation.recap.ajouter";
}
function upd() {
    document.forms[0].elements('userAction').value="al.prestation.recap.modifier";
}

function validate() {
	state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="al.prestation.recap.ajouter";
    else 
        document.forms[0].elements('userAction').value="al.prestation.recap.modifier";
    return state;
}

function cancel() {
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		document.forms[0].elements('userAction').value="al.prestation.recap.afficher";
	} else {
        document.forms[0].elements('userAction').value="al.prestation.recap.afficher";
	}
}

function del() {
	var msgDelete = globazGlobal.MSG_DELETE;
    if (window.confirm(msgDelete)){
        document.forms[0].elements('userAction').value="al.prestation.recap.supprimer";
        document.forms[0].submit();
    }
}

function displayAjaxResult(result) {
	alert(result);
}

function printRecap(inGed) {

	ajaxQuery(globazGlobal.ACTION_GED+inGed+"&charNssRecap="+globazGlobal.CHAR_NSS, displayAjaxResult);
}

function ajaxQuery(query,handlerStateFunction){
	
	//if (event.keyCode== 40) { // curs DOWN
		if (window.XMLHttpRequest) req1 = new XMLHttpRequest();
			else if (window.ActiveXObject) req1 = new ActiveXObject("Microsoft.XMLHTTP");
			else return; // fall on our sword
		req1.open('GET', query,false); 	
		req1.onreadystatechange = alert;
		req1.send(null);
	//}
}


function handleAddLink() {
	var idDossier = $("input[name='idDossierSelected']:checked");
	if(idDossier.prop("checked")) {
		window.location.replace(href=globazGlobal.ACTION_ADD_LINK+idDossier.val());
		return true;
	} else {
		alert(globazGlobal.MESSAGE_GENDOSSIER_NOTID);
		return false;
	}
}

function handleDeleteLink() {
	return confirm(globazGlobal.MSG_DELETE);
}

