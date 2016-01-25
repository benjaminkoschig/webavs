<!--hide this script from non-javascript-enabled browsers

var READ = 'read';
var ADD = 'add';
var UPDATE = 'upd';
var DELETE = 'del';
var COMMIT = 'ok';
var ROLLBACK = 'cancel';
var buttonsArray = new Array(
    "btnUpd",
    "btnDel",
    "btnVal",
    "btnCan",
    "btnNew"
    );
var buttonsOnClicks = new Array();
var isActionMode = true;
var isValidateMode = false;
var ERROR = "error";
var mainFormIsLocked = false; 

if (getParamLocal("_sl") != "") {
    top.location.href = top.document.location.pathname + "?_method=" + getParamLocal("_method") + "&" + getParamLocal("_sl");
}

function readOnly(flag) {
    for(i=0; i < document.forms[0].length; i++) {
    	var tmpElement = document.forms[0].elements[i];
    	if (!tmpElement.readOnly && tmpElement.type != 'hidden') {
    		if (tmpElement.tagName.toUpperCase() != "FIELDSET")
            document.forms[0].elements[i].disabled = flag;
        }
    }
    mainFormIsLocked = flag;
}

function submitForm() {
    readOnly(false);
    document.forms[0].submit();
}

// --- Actions sur les boutons --- //
function action(type) {
    if (type == "null" || type == "") {
        type = READ;
    }

    if (document.forms[0] != null) {
        readOnly(false);

        if (type == COMMIT || type == ROLLBACK) {
            document.forms[0].elements('_valid').value = type;
        }
        else {
            document.forms[0].elements('_method').value = type;
        }

        if (type == ADD || type == UPDATE) {
            if (document.forms[0].elements('_valid').value != ERROR) {
                flag = false;
                showValidationButtons();

                // focus sur premier champs "editable de la form"
                var myForm = document.getElementsByTagName("form")[0];
                if (myForm!= null) {
                    var elems = myForm.getElementsByTagName("*");
                    for(i=0;i<elems.length;i++) {
                        if (
                            ((elems[i].tagName =="INPUT")&&(elems[i].type.toUpperCase()=="TEXT" ))
                            ||(elems[i].tagName =="SELECT" )
                            ||((elems[i].tagName =="INPUT")&&(elems[i].type.toUpperCase()=="BUTTON" ))
                        ) {
                            if (!elems[i].readOnly) {
                                try {
                                    elems[i].focus();
                                    break;
                                } catch (ex) {

                                }

                            }
                        }
                    }
                }
            }
        } else if (type != READ) {
            if (type == DELETE && document.forms[0].elements('_valid').value != ERROR) {
                document.forms[0].elements('_valid').value = COMMIT;
            }
            if (document.forms[0].elements('_valid').value != ERROR) {
                hideValidationButtons();
                submitForm();
            }
        } else {
            showActionButtons();
            readOnly(true);
        }
    }
}

// --- Touche de contrôle --- //
var CTRL_KEY = 17;

// --- Short keys --- //
var ADD_KEY = 78;      // N (ASCII => 78) (Nouveau)
var UPDATE_KEY = 77;   // M (ASCII => 77) (Modifier)
var DELETE_KEY = 83;   // S (ASCII => 83) (Supprimer)

var ENTER_KEY = 13;
var BACKSPACE_KEY = 8;
var ctrlKey = false;

if (typeof langue == "undefined") {
    // Pas de langue
    alert("La variable 'langue' n'est pas définie avant l'insertion du fichier actionsForButtons.js. FR par défaut.");
} else if (langue == "DE") {
    // Langue allemande
    ADD_KEY = 78;      // N (ASCII => 78) (Neu)
    UPDATE_KEY = 65;   // A (ASCII => 65) (Andern)
    DELETE_KEY = 76;   // L (ASCII => 76) (Löschen)
} else if (langue == "FR") {
    // langue française, par défaut
} else {
    //alert("Langue inconnue (" + langue + ") dans le fichier actionsForButtons.js.");
}


function actionKeyUp() {
    if (window.event.ctrlKey) {
        if(window.event.altKey) {
            if (inActionMode()) {
                switch (window.event.keyCode) {
                    case UPDATE_KEY :
                    	$("#btnUpd").click();
                        break;
                    case DELETE_KEY :
                    	$("#btnDel").click();
                        break;
                    case ADD_KEY:
                    	$("#btnNew").click();
                        break;
				}
            }
        }
        if (inValidateMode()) {
            if(window.event.keyCode == ENTER_KEY) {
                if(existsValBtn() && validate()){
                    action(COMMIT);
                }
            } else if (window.event.keyCode == BACKSPACE_KEY) {
                cancel();
                action(ROLLBACK);
            }
        }
    }
}

function existsValBtn() {
    ok = true;
    try {
        var btn = document.getElementById('btnVal');
        if (btn.style.display == 'none') {
            ok = false;
        }
    } catch (e) {
        ok = false;
    }
    return ok;
}

function inActionMode() {
    return isActionMode;
}

function inValidateMode() {
    return isValidateMode;
}

function actionKeyDown() {

}

function getSl(section, dest, att, cle, supParam) {
    var params = "_type=sl";
    params += "&_section=" + section;
    params += "&_dest=" + dest;
    params += "&_typeSrc=" + getParam("_type");
    params += "&_sectionSrc=" + getParam("_section");
    params += "&_destSrc=" + getParam("_dest");
    params += "&" + cle;
    params += "&_att=" + att;
    if (supParam != null && supParam != 0) {
        params += "&_caractId=" + supParam;
    }
    document.forms[0]._sl.value = params;
}

function saveBtnAction(btnName) {
    var btn = document.getElementById(btnName);
    if (btn != null) {
        buttonsOnClicks[btnName] = btn.onclick;
    }
}

function hideValidationButtons() {
    var btnVal = document.getElementById(buttonsArray[2]);
    var btnCan = document.getElementById(buttonsArray[3]);
    if (btnVal != null) {
    	jscss("add", btnVal, "inactive", null);
    }
    if (btnCan != null) {
    	jscss("add", btnCan, "inactive", null);
    }
}

function deActivateButton(aButtonObj) {
    if (aButtonObj != null) {
    	jscss("add", aButtonObj, "inactive", null);
    }
}

function activateButton(aButtonObj) {
    if (aButtonObj != null) {
    	jscss("remove", aButtonObj, "inactive", null);
    }
}


function showValidationButtons() {
    var btnUpd = document.getElementById(buttonsArray[0]);
    var btnDel = document.getElementById(buttonsArray[1]);
    var btnVal = document.getElementById(buttonsArray[2]);
    var btnCan = document.getElementById(buttonsArray[3]);
    var btnNew = document.getElementById(buttonsArray[4]);
    if (btnUpd != null) {
    	jscss("add", btnUpd, "inactive", null);
    }
    if (btnDel != null) {
    	jscss("add", btnDel, "inactive", null);
    }
    if (btnVal != null) {
    	jscss("remove", btnVal, "inactive", null);
    }
    if (btnCan != null) {
    	jscss("remove", btnCan, "inactive", null);
    }
    if (btnNew != null) {
    	jscss("add", btnNew, "inactive", null);
    }
	isActionMode = false;
	isValidateMode = true;
}

function hideAllButtons() {
    var btnUpd = document.getElementById(buttonsArray[0]);
    var btnDel = document.getElementById(buttonsArray[1]);
    var btnVal = document.getElementById(buttonsArray[2]);
    var btnCan = document.getElementById(buttonsArray[3]);
    var btnNew = document.getElementById(buttonsArray[4]);

    if (btnUpd != null) {
    	jscss("add", btnUpd, "inactive", null);
    }
    if (btnDel != null) {
    	jscss("add", btnDel, "inactive", null);
    }
    if (btnVal != null) {
    	jscss("add", btnVal, "inactive", null);
    }
    if (btnCan != null) {
    	jscss("add", btnCan, "inactive", null);
    }
    if (btnNew != null) {
    	jscss("add", btnNew, "inactive", null);
    }
	isActionMode = false;
	isValidateMode = false;
}

function showActionButtons() {
    var btnUpd = document.getElementById(buttonsArray[0]);
    var btnDel = document.getElementById(buttonsArray[1]);
    var btnVal = document.getElementById(buttonsArray[2]);
    var btnCan = document.getElementById(buttonsArray[3]);
    var btnNew = document.getElementById(buttonsArray[4]);
    if (btnUpd != null) {
    	jscss("remove", btnUpd, "inactive", null);
    }
    if (btnDel != null) {
    	jscss("remove", btnDel, "inactive", null);
    }
    if (btnVal != null) {
    	jscss("add", btnVal, "inactive", null);
    }
    if (btnCan != null) {
    	jscss("add", btnCan, "inactive", null);
    }
    if (btnNew != null) {
    	jscss("remove", btnNew, "inactive", null);
    }
	isActionMode = true;
	isValidateMode = false;
}


function actionInit() {
    for (buttonIndex in buttonsArray) {
        saveBtnAction(buttonsArray[buttonIndex]);
    }
    if (document.forms[0].elements('_valid').value == ERROR && document.forms[0].elements('_method').value != READ) {
        showValidationButtons()
    }
    if (document.forms[0].elements('_valid').value == COMMIT || document.forms[0].elements('_valid').value == ROLLBACK) {
        document.forms[0].elements('_method').value = READ;
    }
}
// stop hiding -->
