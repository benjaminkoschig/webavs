<!--hide this script from non-javascript-enabled browsers
// Fonctions utilitaires pour les formulaires
function changeActionAndSubmit(newAction, target) {
  document.forms[0].action = newAction;
  document.forms[0].target = target;
  document.forms[0].submit();
}

function setUserAction(newAction) {
	document.forms[0].elements.userAction.value = newAction;
}

function setFormAction (newAction) {
	document.forms[0].action = newAction;
}

function setFocus() {
	var allElements = document.forms[0].elements;
	for (var i = 0; i < allElements.length; i++) {
		try {
			if (!allElements[i].readOnly) {
				allElements[i].focus();
				return;// element is focused, no exception. We can get out of here.
			}
		} catch (e) {}
	}
}

// stop hiding -->
