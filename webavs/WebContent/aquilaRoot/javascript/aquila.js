/*
 * Lance l'action sur la table avec le paramètre et sa valeur
 */
function tableAction(param, value) {
	var actionControl = "<INPUT type=\"hidden\" name=\"" + param + "\"/>";
	if (!parent.document.forms[0].elements[param]) {
		parent.document.forms[0].innerHTML = parent.document.forms[0].innerHTML + actionControl;
	}
	parent.document.forms[0].elements[param].value = value;
	parent.document.forms[0].submit();
}
