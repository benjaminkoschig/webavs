var actionMethod;
var userAction;

function add () {
}
function upd () {
	$('.areaPeriodeDetail td input,select').attr('disabled', true);
}

function cancel () {
	if (actionMethod == "add") {
		userAction.value = ACTION_HOME + ".chercher";
	} else {
		userAction.value = ACTION_HOME + ".rechercher";
	}
}

function validate () {
	state = true;
	if (actionMethod == "add") {
		userAction.value = ACTION_HOME + ".ajouter";
	} else {
		userAction.value = ACTION_HOME + ".modifier";
	}
	return state;
}

function del () {
	if (window.confirm(JSP_DELETE_MESSAGE_INFO)) {
		userAction.value = ACTION_HOME + ".supprimer";
		document.forms[0].submit();
	}
}

function init () {
	actionMethod = $('[name=_method]', document.forms[0]).val();
	userAction = $('[name=userAction]', document.forms[0])[0];
}

function postInit () {
	$('.btnAjax input').attr('disabled', false);
}

$(function () {
	/*
	 * $('#tiersWidget').widgetChercheTiers("widget.pyxis.chercheTiers",chercheTiersHint,function(element){
	 * $('.detailAdresseTiers').html($(element).attr('tiersDetail').replace(/\n/g,"<br>")); $('[name=home.simpleHome.idTiersHome]').val($(element).attr('idTiers'));
	 * $('.external_link').attr('href',urlTiers+$(element).attr('idTiers')); });
	 */
	$('#tiersWidget').keypress(function () {
		var t = this.value;
		if (t.length > 3 && t.substr(0, 3) == "756") {
			t = t.replace(/\./g, '');
			var r = "756." + t.substr(3, 4);
			if (t.length > 6) {
				r += "." + t.substr(7, 4);
				if (t.length > 10) {
					r += "." + t.substr(11, 2);
				}
			}
			this.value = r;
		}
	});

});
