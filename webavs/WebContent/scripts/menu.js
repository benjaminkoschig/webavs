<!--hide this script from non-javascript-enabled browsers
var MENU_TAB_MENU = "menu";
var MENU_TAB_OPTIONS = "options";

var mouseOutElementStyleRestore = new Array();
var menuCurrentlyHighlightedElement = null;

function reloadMenuFrame(theMenuFrame, theTabToShow) {
	var theHref = theMenuFrame.location.href;
	var paramsIndex = theHref.indexOf("?");
	if (paramsIndex > -1) { // suppresion des paramètres
		theHref = theHref.substr(0, paramsIndex);
	}
	if (theTabToShow) {
		theHref += "?tab=" + theTabToShow;
	}
	theMenuFrame.location.replace(theHref);
}

/* Utilisé par le menu pour effectuer une action (onClick) */
function doAction(action, frame) {
	if (frame == "_blank") {
		window.open(action);
		return;
	}
	var theFrame = top.frames[frame];
	if (!theFrame) {
		theFrame = top;
	}
	theFrame.location.href = action;
}

function toggleMenuElement(anElement) {
	/*
	Here is how things should go.
	1/ Get the parent element
	2/ With every nextSibling, toggle it
	*/
//	var subMenus = anElement.parentNode.nextSibling;
	var theButton = getChildNodeWithClass(anElement, "menuButton");//anElement.firstChild;
	var subMenus = getNextSiblingNodeWithClass(anElement, "child");//anElement.nextSibling;
	if (subMenus) {
		toggleMenuDisplay(theButton, subMenus);
	} else {
//		alert("no subs");
	}
}

function toggleMenuDisplay(theButton, subMenus) {
	if (!jscss("check", subMenus, "hidden")) {
		collapseMenuItems(theButton, subMenus);
	} else {
		expandMenuItems(theButton, subMenus);
	}
}

function collapseMenuItems(theButton, subMenu) {
	jscss("add", subMenu, "hidden");
	theButton.innerHTML = "4";
}

function expandMenuItems(theButton, subMenu) {
	jscss("remove", subMenu, "hidden");
	theButton.innerHTML = "6";
}

function menuOnMouseHover(anElement) {
	if (menuCurrentlyHighlightedElement != null) {
		menuOnMouseOut(menuCurrentlyHighlightedElement);
	}
	if (!mouseOutElementStyleRestore[anElement]) {
		mouseOutElementStyleRestore[anElement] = new Object();
	}
	mouseOutElementStyleRestore[anElement].borderWidthStyle = anElement.style.borderWidth;
	mouseOutElementStyleRestore[anElement].borderStyleStyle = anElement.style.borderStyle;
	mouseOutElementStyleRestore[anElement].borderColorStyle = anElement.style.borderColor;
	mouseOutElementStyleRestore[anElement].bgStyle = anElement.style.backgroundColor;
	mouseOutElementStyleRestore[anElement].textColorStyle = anElement.style.color;
	
	anElement.style.border = "1px solid #D3E4FB";
	anElement.style.backgroundColor = "#8293AA";
	anElement.style.color = "white";
	
	menuCurrentlyHighlightedElement = anElement;
}

function menuOnMouseOut(anElement) {
	if (!mouseOutElementStyleRestore[anElement]) { // warning, the mouse was on the menu before display! (frame reloaded)
		try{
			menuOnMouseOver(anElement);
		} catch(e){
			//Do nothing !
		}
		
	}	
	anElement.style.borderWidth = mouseOutElementStyleRestore[anElement].borderWidthStyle;
	anElement.style.borderStyle = mouseOutElementStyleRestore[anElement].borderStyleStyle;
	anElement.style.borderColor = mouseOutElementStyleRestore[anElement].borderColorStyle;
	anElement.style.backgroundColor = mouseOutElementStyleRestore[anElement].bgStyle;	
	anElement.style.color = mouseOutElementStyleRestore[anElement].textColorStyle;
	
}


function getChildNodeWithClass(baseObject, className) {
	if (!baseObject.hasChildNodes()) {
		return null;
	}
	var result = null;
	var allChilds = baseObject.childNodes;
	for (count = 0; count < allChilds.length; count++) {
		currNode = allChilds.item(count);
		if (currNode.nodeType == 1) { // c'est un tag
			if (jscss("check", currNode, className)) {
				result = currNode;
				break;
			}
		}
	}
	return result;
}

function getNextSiblingNodeWithClass(baseObject, className) {
	var result = null;
	var currentSib = baseObject.nextSibling;
	while (currentSib != null) {
		if (currentSib.nodeType == 1) { // c'est un tag
			if (jscss("check", currentSib, className)) {
				result = currentSib;
				break;
			}
		}
		currentSib = currentSib.nextSibling;
	}
	return result;
}

function getPrevSiblingNodeWithClass(baseObject, className) {
	var result = null;
	var currentSib = baseObject.previousSibling;
	while (currentSib != null) {
		if (currentSib.nodeType == 1) { // c'est un tag
			if (jscss("check", currentSib, className)) {
				result = currentSib;
				break;
			}
		}
		currentSib = currentSib.previousSibling;
	}
	return result;
}

function getFirstSelectableItem() {
	var rightDiv = document.getElementById("options");
	if (isMenuSelected()) {
		rightDiv = document.getElementById("menu");
	}
	var result = null;
	try {
		var menuCont = getChildNodeWithClass(rightDiv, "menuContainer");
		var menuList = getChildNodeWithClass(menuCont, "mainMenuList");
		var menuSingleHierarchy = getChildNodeWithClass(menuList, "singleMenuHierarchy");
		result = getChildNodeWithClass(menuSingleHierarchy, "menuNormal");
	} catch (e) {
//		alert(e);
	}
	return result;
	//alert(rightDiv.childNodes.item(0).childNodes.item(3).innerHTML);
	
}

function selectFirstItem() {
	var elementToHighlight = getFirstSelectableItem();
	if (elementToHighlight == null) {
		return;
	}
	menuOnMouseHover(elementToHighlight);
}

function isMenuSelected() {
	return jscss("check", document.getElementById("menuButton"), "btnSelected");
}

function switchTabs() {
	if(isMenuSelected()) {
		selectOptions();
	} else {
		selectMenu();
	}
}

function getNextSelectableItem(aMenuItem) {
	var startingElement = menuCurrentlyHighlightedElement;
	var result = null;
	try {
		var parent = startingElement.parentNode;
		var nextMenuHierarchy = getNextSiblingNodeWithClass(parent, "singleMenuHierarchy");
		result = getChildNodeWithClass(nextMenuHierarchy, "menuNormal");
	} catch (e) {
//		alert(e)
	}
	return result;
}

function getPrevSelectableItem(aMenuItem) {
	var startingElement = menuCurrentlyHighlightedElement;
	var result = null;
	try {
		var parent = startingElement.parentNode;
		var prevMenuHierarchy = getPrevSiblingNodeWithClass(parent, "singleMenuHierarchy");
		result = getChildNodeWithClass(prevMenuHierarchy, "menuNormal");
	} catch (e) {
//		alert(e)
	}
	return result;
}

function menuSelectGoDown() {
	var nextItem = getNextSelectableItem(menuCurrentlyHighlightedElement);
	if (nextItem != null) {
		menuOnMouseHover(nextItem);
	}
}

function menuSelectGoUp() {
	var prevItem = getPrevSelectableItem(menuCurrentlyHighlightedElement);
	if (prevItem != null) {
		menuOnMouseHover(prevItem);
	}
}

function doSelectedAction() {
	if (menuCurrentlyHighlightedElement != null) {
		menuCurrentlyHighlightedElement.onclick();
	}
}

function menuSelectGoOpenNode() {
	if (jscss("check", menuCurrentlyHighlightedElement, "withChilds")) { // un superMenu
		var firstChild = getChildNodeWithClass(menuCurrentlyHighlightedElement.parentNode, "child");
		if (!firstChild) {
			return;
		}
		if (jscss("check", firstChild, "hidden")) {
			menuCurrentlyHighlightedElement.onclick();
		} else {
			try {
				var menuSingleHierarchy = getChildNodeWithClass(firstChild, "singleMenuHierarchy");
				var elemToHighlight = getChildNodeWithClass(menuSingleHierarchy, "menuNormal");
				menuOnMouseHover(elemToHighlight);
			} catch (e) {
				alert(e);
			}
		}
	}
}

function menuSelectGoCloseNode() {
	// l'élément en cours est un parent?
	var currIsParent = jscss("check", menuCurrentlyHighlightedElement, "withChilds");
	// l'élément en cours est considéré fermé par défaut
	var currIsOpen = false;
	// on essaie de récupérer les sous-éléments
	var firstChild = getChildNodeWithClass(menuCurrentlyHighlightedElement.parentNode, "child");
	if (firstChild) {
		if (!jscss("check", firstChild, "hidden")) { // si un fils existe et qu'il est visible, alors on est ouvert (du lundi au vendredi, 8h-11h, fermé l'après-midi)
			currIsOpen = true;
		}
	}
	// si current est parent et ouvert, alors fermer current
	if (currIsParent && currIsOpen) {
		menuCurrentlyHighlightedElement.onclick();
	} else { // sinon essayer de remonter d'un niveau vers le parent
		try {
			var nextElToShow = getChildNodeWithClass(menuCurrentlyHighlightedElement.parentNode.parentNode.parentNode, "menuNormal");
			if (nextElToShow) {
				menuOnMouseHover(nextElToShow);
			}
		} catch (e) {
		}
	}
	
}

function handleMenuEvent(e){
	var concernedKey = e.which;
	if (!concernedKey) {
		concernedKey = e.keyCode;
	}
	switch (concernedKey) {
		case 83: // s
			switchTabs();
			break;
		case 40: // flèche bas
			menuSelectGoDown();
			break;
		case 38: // flèche haut
			menuSelectGoUp();
			break;
		case 39: // flèche droite
			menuSelectGoOpenNode();
			break;
		case 37: // flèche gauche
			menuSelectGoCloseNode();
			break;
		case 13: // enter
			doSelectedAction();
			break;
	}
}
// stop hiding -->
