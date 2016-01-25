<!--hide this script from non-javascript-enabled browsers
// fonctions pour le (nouveau) menu
var FWOptionSelectorButton_oldFocus = null;
var FWOptionSelectorButton_win = new PopupWindow("FWOptionSelectorButton_myDiv");
FWOptionSelectorButton_win.autoHide();
FWOptionSelectorButton_win.offsetX = 30;
var FWOptionSelectorButton_str = "";

function doPopupAction(popupButtonName, popupButtonObj) {
	FWOptionSelectorButton_win.showPopup(popupButtonName);
	FWOptionSelectorButton_oldFocus=popupButtonObj;
	FWOptionSelectorButton_theListe.focus();
	FWOptionSelectorButton_theListe.selectedIndex=0;
}

// stop hiding -->
