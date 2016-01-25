var searchingString = "[Searching...]";
var verificationString = "[Checking...]";
var searching = false;

window.status = "";

var debug = false;
var traceWindow = null;
if (debug) {
	traceWindow = window.open('',null,"height=700,width=400,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
	traceWindow.document.title.text = "Javascript Debug Window";
	traceWindow.document.writeln("<h4>Debug at "+new Date()+" :</h4>");
}

function trace(message) {
	if (traceWindow && debug) {
		traceWindow.document.writeln("<li>"+message);
		traceWindow.document.body.scrollTop = traceWindow.document.body.scrollHeight;
	}
}

function urlEncode(aStr) {
	return aStr.replace("+", "%2B");
}

function removeDots(aString) {
	var currentIndex = aString.indexOf(".");
	if (currentIndex > -1) {
		if (currentIndex == 0) {
			if (aString.length == 1) {
				return "";
			} else {
				return removeDots(aString.substring(1));
			}
		} else if (currentIndex == aString.length - 1) {
			return removeDots(aString.substring(0, currentIndex));
		} else {
			return removeDots(aString.substring(0, currentIndex)) + removeDots(aString.substring(currentIndex + 1));
		}
	}
	return aString;
}

function popupTagHandleList() {
	var minChars = this.avsMinChars;
	var autoChars = this.avsAutoChars;
	if (this.isNNSS) {
		minChars = this.nssMinChars;
		autoChars = this.nssAutoChars;
	}
	

	var e = window.event;

	if (this.oldValue != this.input.value) {
		this.isValidated = false;
	}

	/*
	window.status = this.beginValidString + " - " + this.beginFailString + " / ";
	if (this.beginFailString != null)
		window.status += (this.input.value.substring(0,this.beginFailString.length) != this.beginFailString);
	window.status += " / "
	if (this.listLoaded)
		window.status += (this.input.value.indexOf(this.beginValidString) != 0);
	*/

	/*if (this.beginFailString == "")
		this.beginFailString = null;
	if (this.beginValidString == "")
		this.beginValidString = null;
	
	if (this.listLoaded && this.input.value.indexOf(this.beginValidString) != 0) {
		this.beginFailString = this.input.value.substring(0,this.beginValidString.length);
		this.beginValidString = null;
		this.stopPopup();
	} else if (this.beginFailString != null && this.input.value.substring(0,this.beginFailString.length) != this.beginFailString) {		
		this.beginFailString = null;
		this.beginValidString = null;
	}*/

	var numAVSWithoutDots = this.input.value;
	while(numAVSWithoutDots.indexOf(".")!=-1){
	    numAVSWithoutDots = numAVSWithoutDots.replace(".","");
	}

	if (numAVSWithoutDots.length >= minChars) {

	//if (this.input.value.length >= this.minChars) {

		trace("-->"+this.input.name+".handleList()");

		if (//this.beginFailString == null && 
			this.condition() && 
			(	this.automaticSearch && 
				numAVSWithoutDots.length>=autoChars && 
				this.oldValue != this.input.value
			) || 
			(	this.useUpDownKeys && 
				numAVSWithoutDots.length >= minChars && 
				(e.keyCode==40 || e.keyCode==38)
			)
			) {
			var nonAuthorizedKeys ="8;9;46;37;39;33;34;35;36;45;";
			if(!this.listLoaded && nonAuthorizedKeys.indexOf(e.keyCode+";") == -1) {
				if (this.onSearch)
					this.onSearch();
				this.input.disabled = true;
				//this.tmpValue = this.input.value;
				this.tmpValue = this.input.value;
				this.input.value = searchingString;
				while (searching) {;}
				searching = true;
				this.frame.onreadystatechange = this.buildList;
				var nssValue = getNssPrefix(this.input.name);
				var nssParam = "";
				if (this.isNNSS && nssValue != null) {
					nssParam = nssValue;
				}
				this.frame.src = this.jspName + nssParam+urlEncode(this.tmpValue) + this.params + "&isNNSS=" + this.isNNSS;					
				return true;
			}
		}
		if(this.listLoaded) {
			if(e.keyCode==40) {			
				if(this.select.selectedIndex < this.select.length-1) {
					this.select.selectedIndex = this.select.selectedIndex+1;
					this.disableBlur = true;
					this.select.focus();
					this.updateInput();
				}
			} else if (e.keyCode==38) {
				if(this.select.selectedIndex>0) {
					this.select.selectedIndex = this.select.selectedIndex-1;
					this.disableBlur = true;
					this.select.focus();
					this.updateInput();
				}
			} else if (e.keyCode==33) {
				if(this.select.selectedIndex > 0) {
					if (this.select.selectedIndex>4)
						this.select.selectedIndex -= 5;
					else
						this.select.selectedIndex  = 0;
					this.disableBlur = true;
					this.select.focus();
					this.updateInput();
				}
				e.cancelBubble = true;
			} else if (e.keyCode==34) {
				if(this.select.selectedIndex < this.select.length-1) {
					if (this.select.selectedIndex < this.select.length-6)
						this.select.selectedIndex += 5;
					else
						this.select.selectedIndex  = this.select.length-1;
					this.disableBlur = true;
					this.select.focus();
					this.updateInput();
				}
				e.cancelBubble = true;
			} else if (e.keyCode==13) {
				this.stopPopup();
				this.input.focus();
				e.cancelBubble = true;

			} else {
				trace("autoComplete");
				autoComplete(this.input, this.select, "value", this.forceCompletion);
			}
		}
	} else if (this.listLoaded)
		this.stopPopup();
	return true;
}

function getNssPrefix(inputName) {
	var nssPrefix = null;
	try {
		var betterInputName = inputName;
		if (inputName.indexOf("partial") == 0) {
			betterInputName = inputName.substr(7);
		}
		var nssPrefixInputName = betterInputName + "NssPrefixe";
		var nssPrefixElement = document.getElementById(nssPrefixInputName);
		nssPrefix = nssPrefixElement.value;
	} catch (e) {}
	return nssPrefix
}

function PopupTag(div, frame, input, avsAutoChars, avsMinChars, forceCompletion, forceValidate, jspName, params, useUpDownKeys, anchorName, condition, nssAutoChars, nssMinChars) {
	this.div 		= div;
	this.frame 		= frame;
	this.input 		= input;
	this.select		= null;
	this.forceCompletion	= forceCompletion;
	this.forceValidate	= forceValidate;
	this.automaticSearch 	= (avsAutoChars>0)?true:false;
	this.avsAutoChars 		= avsAutoChars;
	this.beginValidString	= null;
	this.beginFailString	= null;
	this.avsMinChars		= avsMinChars;
	this.jspName		= jspName;
	this.params		= params;
	this.useUpDownKeys	= useUpDownKeys;
	this.oldValue		= (input != null && input.value != null)?input.value:"";
	this.tmpValue		= "";
	this.listLoaded 	= false;
	this.tabPressed    	= false;
	this.disableBlur   	= false;
	this.mouseOutOfDiv 	= true;
	this.isValidated   	= false;
	this.forceToDisplayList = false;
	this.win		= null;
	this.anchorName		= anchorName;
	this.isNNSS = false;
	this.nssMinChars = nssMinChars;
	this.nssAutoChars = nssAutoChars;
	// Méthodes
	this.init		= popupTagInit;
	this.handleList		= popupTagHandleList;
	this.onKeyUp		= popupTagOnKeyUp;
	this.onKeyDown		= popupTagOnKeyDown;
	this.onBlur		= popupTagOnBlur;
	this.onKeyPress		= popupTagOnKeyPress;
	this.stopPopup		= popupTagStopPopup;
	this.validate		= popupTagValidate;
	this.buildValidate	= popupTagBuildValidate;
	this.buildList		= popupTagBuildList;
	this.updateInput	= popupTagUpdateInput;
	this.condition		= (condition == null)?function(){return true;}:condition;
	this.onSearch		= null;
	this.onUpdate		= null;
	this.onStop			= null;
	this.updateJspName = popupUpdateJspName;
	this.updateParams = popupUpdateParams;
	this.setNNSS = popupUpdateIsNNSS;
}

function popupTagInit(input) {

	trace("-->popupTagInit() : "+input.name);

	if (!input || !input.name)
		alert("ERROR in initializing the PopupTag.");

	this.input = input;

	document.write("<div onmouseleave=\"" +this.input.name+ "PopupTag.mouseOutOfDiv = true;return true;\" onmouseenter=\"" +this.input.name+ "PopupTag.mouseOutOfDiv = false;return true;\" style=\"position:absolute;font-family = Wingdings;font-size : 14;visibility:hidden\" name=\""+this.input.name+"POPUPDIV\" id=\""+this.input.name+"POPUPDIV\"></div>\n");
	document.write("<iframe tabindex=\"-1\" width=\"0\" height=\"0\" src=\"\" id=\""+this.input.name+"IFRAME\" name=\""+this.input.name+"IFRAME\"></iframe>\n");

	this.frame 	= document.getElementById(this.input.name+"IFRAME");
	this.div 	= document.getElementById(this.input.name+"POPUPDIV");

	if (!this.frame || !this.div)
		alert("ERROR in initializing the PopupTag.");
	return true;
}

function popupTagOnKeyUp() {	
	return this.handleList();
}

function popupTagOnKeyDown() {

	var e 		= window.event;	

	trace("-->popupTagOnKeyDown() : "+this.input.name+" "+e.keyCode);

	// ALD : 	si la touche pressée est le Delete ou le BackSpace, 
	if(e.keyCode == 8 || e.keyCode == 46){
		var inputName = removeDots(this.input.name);
		trace("-->popupTagOnKeyPress() : poptagFailure generation !!");
		if( typeof window[inputName + "PopupTagOnFailure"] === "function"){
			window[inputName + "PopupTagOnFailure"](window[inputName + "PopupTag"]);
		}
	}
	
	
	if (e.keyCode == 9) 
		this.tabPressed	= true; 
	else {
		if (e.keyCode != 13 && e.keyCode != 37 && e.keyCode != 39)
			this.oldValue = this.input.value;
		this.tabPressed	= false;
	}
	return true;
}

function popupTagOnKeyPress() {
	var e = window.event;
 
 	trace("-->popupTagOnKeyPress() : "+this.input.name+"(keyCode:"+e.keyCode+")");
 
 	var selectName = e.srcElement.id;
 	var tag;	

	/*Suppresion de la méthode eval par ald 05.03.04*/
	// remplacement par tag = ... (normalement meme effet)
	//eval("tag = " +selectName+ "PopupTag;");
	tag = selectName+ "PopupTag";
	
	if (this.oldValue != this.input.value)
		eval(removeDots(this.input.name)+"PopupTagOnChange(" + removeDots(this.input.name) + "PopupTag);");
	
	if (tag.listLoaded && e.keyCode == 13)
		return false;
	else
		return true;
}

function popupTagOnBlur() {

	trace("Blur ? : " + this.disableBlur +" "+ this.tabPressed +" " + this.mouseOutOfDiv);
	if ((!this.disableBlur) && (this.tabPressed || this.mouseOutOfDiv)) {

		trace("-->popupTagOnBlur() : "+this.input.name);

		/*if (this.tabPressed && this.listLoaded) {
			tag.input.value=tag.select.options[tag.select.selectedIndex].value;
			tag.isValidated = true;
		}*/
		this.tabPressed 	= false;
		this.stopPopup();
		if (this.forceValidate && !this.isValidated && this.input.value.length > 0 && this.condition()) { 
			trace("Validate ? : " + this.forceValidate +" "+ !this.isValidated +" " + (this.input.value.length > 0) +" "+ this.condition());
			this.validate();
		} else {
			trace(this.oldValue +" "+this.input.value);
			if (this.oldValue != this.input.value) {
				eval(removeDots(this.input.name) + "PopupTagOnChange(" + removeDots(this.input.name) + "PopupTag);");
			}
			this.select = null;
		}		
		
	} else {
		this.disableBlur 	= false;
	}
	return true;
}

function popupTagStopPopup() {

	if(this.input != null) {

		trace("-->stopPopup() : "+this.input.name);

		if (this.win) 
			this.win.hidePopup();
		// La liste n'est plus chargée
		this.listLoaded = false;
		if (this.onStop)
			this.onStop(this);
		
	}
}


function popupTagValidate() {

	if (this.input.value.length >= 0) {
		if (this.onSearch)
			this.onSearch();
		trace("-->popupTagValidate() : "+this.input.name);
		this.input.disabled = true;
		this.tmpValue = this.input.value;
		this.input.value = verificationString;				
		while (searching) {;}
		searching = true;
		this.frame.onreadystatechange = this.buildValidate;

		var nssValue = getNssPrefix(this.input.name);
		var nssParam = "";
		if (this.isNNSS && nssValue != null) {
			nssParam = nssValue;
		}
		this.frame.src = this.jspName + nssParam + urlEncode(this.tmpValue) + this.params + "&isNNSS=" + this.isNNSS;
	}
}

function popupTagBuildValidate() {	

	if (window.event.srcElement.readyState=="complete") {

		var iFrameName = window.event.srcElement.name;
		var re = iFrameName.substring(0,iFrameName.indexOf("IFRAME"));
		eval("var tag = " + removeDots(re) + "PopupTag;");

		trace("-->popupTagBuildValidate() : "+tag.input.name);

		tag.frame.onreadystatechange = {};
		searching = false;
		tag.input.disabled 	= false;
		tag.input.value 	= tag.tmpValue;
		tag.frame.contentWindow.document.forms[0].elements[0].tabIndex = -1;

		if(tag.frame.contentWindow.document.forms[0].elements[0].length==1) {
			tag.select = tag.frame.contentWindow.document.forms[0].elements[0];
			tag.select.selectedIndex = 0;
			tag.input.value = tag.frame.contentWindow.document.forms[0].elements[0].options[0].value;
			tag.isValidated = true;
			if (tag.oldValue != tag.input.value) {
				eval(removeDots(tag.input.name) + "PopupTagOnChange(" + removeDots(tag.input.name) + "PopupTag);");
			}
			tag.oldValue = tag.input.value;
		} else if(tag.frame.contentWindow.document.forms[0].elements[0].length==0) {
			tag.select = null;			
			if (tag.forceValidate) tag.input.value = "";
			tag.isValidated = false;
			if (tag.oldValue != tag.input.value){ 
				trace("-->popupTagBuildValidate() : poptagFailure generation !!");
				eval(removeDots(tag.input.name) + "PopupTagOnFailure(" + removeDots(tag.input.name)+ "PopupTag);");
			}
			tag.oldValue = tag.input.value;
		} else {
			if (!tag.win) {
				tag.win = new PopupWindow(tag.div.name);
			}
			tag.frame.contentWindow.document.forms[0].elements[0].id = tag.input.name+"PopupTagSELECT";
			tag.win.populate(tag.frame.contentWindow.document.getElementById(tag.input.name+"PopupTagSELECT").outerHTML);				
			tag.win.offsetY = tag.input.offsetHeight;							
			tag.win.showPopup(tag.anchorName);
			tag.select = document.getElementById(tag.input.name+"PopupTagSELECT");
			tag.select.onclick = tag.updateInput;
			tag.select.tabIndex = -1;
			tag.listLoaded = true;
			tag.input.focus();
		}
	}
}

function popupTagBuildList() {		
	
	// Attente de la fin du chargement de l'IFRAME
	if (window.event.srcElement.readyState=="complete") {

		var iFrameName = window.event.srcElement.name;
		var re = iFrameName.substring(0,iFrameName.indexOf("IFRAME"));
		eval("var tag = " + removeDots(re) + "PopupTag;");

		trace("-->buildList() : "+tag.input.name);

		tag.frame.onreadystatechange = {};
		searching = false;

		tag.input.value 	= tag.tmpValue;
		tag.input.disabled 	= false;
		tag.input.focus();

		
		if(tag.frame.contentWindow.document.forms[0].elements[0].length==1 && !tag.forceToDisplayList) {
			tag.select = tag.frame.contentWindow.document.forms[0].elements[0];
			tag.select.selectedIndex = 0;
			tag.input.value = tag.frame.contentWindow.document.forms[0].elements[0].options[0].value;
			tag.isValidated = true;
			tag.stopPopup();
			/**** ALD effacement du if 05.03.04 **********/
			//if (tag.oldValue != tag.input.value) 
			eval(removeDots(tag.input.name) + "PopupTagOnChange(" + removeDots(tag.input.name) + "PopupTag);");
			//tag.beginValidString = null;
			//tag.beginFailString = null;
			tag.oldValue = tag.input.value;
		} else if (tag.frame.contentWindow.document.forms[0].elements[0].length==0) {
			tag.isValidated = false;			
			if (tag.forceValidate) 
				tag.input.value = "";
			else {/*
				if (tag.autoChars != -1) 
					tag.beginFailString = tag.input.value.substring(0,tag.autoChars);
				else
					tag.beginFailString = tag.input.value;*/
			}
			//tag.beginValidString = null;
			tag.stopPopup();
			/****** ALD effacement du if 23.05.05 *********/
			//if (tag.oldValue != tag.input.value){
				trace("-->buildList() : poptagFailure no data foud!!");
				eval(removeDots(tag.input.name) + "PopupTagOnFailure(" + removeDots(tag.input.name) + "PopupTag);");
			//}
			tag.oldValue = tag.input.value;
		} else {
			if (!tag.win) {
				tag.win = new PopupWindow(tag.div.name);
			}/*
			if (tag.autoChars != -1) 
				tag.beginValidString = tag.input.value.substring(0,tag.autoChars);
			else
				tag.beginValidString = tag.input.value;*/
			//tag.beginFailString = null;
			tag.frame.contentWindow.document.forms[0].elements[0].id = tag.input.name+"PopupTagSELECT";			
			tag.win.populate(tag.frame.contentWindow.document.getElementById(tag.input.name+"PopupTagSELECT").outerHTML);				
			
			//test pour le NNSS tag Dyntablle
			if(tag.anchorName.search(/partialnumeroAvs/) != -1) tag.win.offsetY = tag.input.offsetHeight-210;
			else tag.win.offsetY = tag.input.offsetHeight;				
			
			tag.win.showPopup(tag.anchorName);			
				
			tag.select = document.getElementById(tag.input.name+"PopupTagSELECT");
			tag.select.onclick = tag.updateInput;
			tag.select.tabIndex = -1;
			tag.listLoaded = true;

		}
	}
}

function popupTagUpdateInput() {

	var e = window.event;

	var tag;
	if (this.input)
		tag = this;
	else {
		var selectName = e.srcElement.id;		
		eval("tag = " + selectName.substring(0,selectName.indexOf("SELECT"))+ ";");
	}

	trace("-->updateInput() : "+tag.input.name);
	tag.oldValue="";
	tag.input.value=tag.select.options[tag.select.selectedIndex].value;
	tag.isValidated = true;
	if (tag.onUpdate)
		tag.onUpdate(tag);
	if(e.keyCode!=40 && e.keyCode!=38 && e.keyCode!=33 && e.keyCode!=34) {
		tag.stopPopup();
	}
	tag.input.focus();
	return true;
}
function setSl(context,id) {
	var newhidden 
		= document.createElement("<INPUT TYPE='HIDDEN' NAME='newId' VALUE='" + id + "' />");
	document.forms[0].appendChild(newhidden);
	document.forms[0].submit();	
}

function popupUpdateJspName(jspName) {
	this.jspName = jspName;
}

function popupUpdateParams(params) {
	this.params = params;
}

function popupUpdateIsNNSS(trueFalse) {
	this.isNNSS = trueFalse;
}
trace("script 'selectionPopup.js' chargé.");

/*********************************
 * New "simplePopup" starts here *
 *********************************/
/* Fonctions pour page appelante */
thisWin = window;
function selectElementWithContext(width, height, page, context) {
	var fenPmt;
	if (ie) {
		fenPmt = showModalDialog (context + "modalIFrame.jsp?show=" + page, thisWin, "dialogHeight: " + height + "px; dialogWidth: " + width + "px; dialogTop: 550px; dialogLeft: 300px; edge: Sunken; help: No; resizable: No; status: No; center: No;");
	} else {
		var newWinName = "winPopupSelect";
		fenPmt = window.open (page, newWinName, "height=" + height + "px, width=" + width + "px, resizable=No; status=No;");
	}
}

var mappingPopup;
var mappingBase;

/* Fonctions pour pages popup (appelées)*/
// Récupération du document qui a ouvert ce popup
var parentDoc = null;
var popupMappingBase = null;
var popupMappingPopup = null;

try {
	if (ie) {
		parentDoc = dialogArguments;
	} else {
		parentDoc = window.opener;
	}
} catch (e) {
	//Do nothing here. It looks like we're not in a popup window
}
// Récupération des infos de mapping
if (parentDoc) {
	popupMappingBase = parentDoc.mappingBase;
	popupMappingPopup = parentDoc.mappingPopup;
}

// Fonction de transfert de valeurs selon mapping récupéré ci-dessus
function transferValues(lineId) {
	if (parentDoc == null) {
		alert("Error transfering data to parent: no parent.");
		return;
	}
	lineObj = document.getElementById(lineId);
	var popupElements = lineObj.getElementsByTagName("INPUT");

	for (i = 0; i < popupMappingBase.length; i++) {
		var popupInput = popupElements.namedItem(popupMappingPopup[i]);
		var baseInputName = popupMappingBase[i];
		var baseInput = parentDoc.document.getElementById(popupMappingBase[i]);
		baseInput.value = popupInput.value;
	}
	self.close();
}



/*********************************
 * New "simplePopup" ends here   *
 *********************************/