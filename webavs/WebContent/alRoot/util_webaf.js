//******* <DEBUG JS TOOLS> **********************************
var debugModeEnable=false;
//Debug Management
function initDebugManager(){	
	
	if(debugModeEnable){
		if(document.getElementById('debugConsole'))
			document.body.removeChild(document.getElementById('debugConsole'));
	
		debugZone=document.createElement('div');
		debugZone.setAttribute("id","debugConsole");
		debugZone.setAttribute("class","debugZone");
		document.body.appendChild(debugZone);
	
		document.getElementById('debugConsole').innerHTML="";
		document.getElementById('debugConsole').innerHTML='<a href="#" onclick="initDebugManager();">Reset</a><br/>';
	}

	
}
function mlog(msg,color,size){
	if(size==null)
		size="9";
	if(debugModeEnable){
		
		var msgTab = msg.split(" ");
		var newMsg = "";
		for(i in msgTab){
		
			if(msgTab[i].charAt(0)=='@'){
				newMsg+='<a target="_blank" href="'+msgTab[i].substr(1)+'">'+msgTab[i].substr(1)+'</a> ';
			}
			else{
				newMsg+=msgTab[i]+" ";
			}
		}
		
		strHtml="<span style=\"color:"+color+"\;font-size:"+size+"px\">"+newMsg+"</span><br/>";
		var buffer = "";
		
		if(document.getElementById('debugConsole')){
			buffer = document.getElementById('debugConsole').innerHTML;
			document.getElementById('debugConsole').innerHTML="";
			document.getElementById('debugConsole').innerHTML+=strHtml;
			document.getElementById('debugConsole').innerHTML+=buffer;
		}
		else
			alert('Veuillez appeler initDebugManager() dans postInit pour utiliser la fonction debug');
	}
}
function writeDebugLine(msg,color){
	//alert('deprecated, please call mlog function');
	mlog(msg,color);
}
//******* </DEBUG JS TOOLS> **********************************

//******* <DATE TOOLS> ************************************

//******* </DATE TOOLS> ***********************************

//return -1 si olderDate est plus récente que newerDate, 0 si égal, 1 si newerDate est la plus récente
function compareGlobazDates(olderDate,newerDate){

	var date1 = new Date();
	date1.setFullYear(olderDate.substr(6,4));
	date1.setMonth(olderDate.substr(3,2));
	date1.setDate(olderDate.substr(0,2));
	date1.setHours(0);
	date1.setMinutes(0);
	date1.setSeconds(0);
	date1.setMilliseconds(0);
	var d1=date1.getTime();
	 
	var date2 = new Date();
	date2.setFullYear(newerDate.substr(6,4));
	date2.setMonth(newerDate.substr(3,2));
	date2.setDate(newerDate.substr(0,2));
	date2.setHours(0);
	date2.setMinutes(0);
	date2.setSeconds(0);
	date2.setMilliseconds(0);
	var d2=date2.getTime();
	 
	 
	//si la date d'arrviée et superieur a la date de depart en afficher un message d'erreur
	if(d1>d2)
	   return -1;
	else if(d2>d1)
	    return 1;
	else
		return 0;
 
}

//******* <URLs TOOLS> ************************************

//Extrait les params de l'url et les mets dans un tableau associatif
function extractUrlParams () {
	var t = location.search.substring(1).split('&');
	var f = [];
	for (var i=0; i<t.length; i++) {
		var x = t[ i ].split('=');
		f[x[0]]=x[1];
	}
	return f;
}

//Même chose que extractUrlParams mais en partant d'une string passée en param et pas de l'url courante
function extractStringUrlParams(urlString){
	var t = urlString.substring(urlString.indexOf('?')+1).split('&');
	var f = [];
	for (var i=0; i<t.length; i++) {
		mlog('extractStringUrl:'+t[i]);
		var x = t[ i ].split('=');
		f[x[0]]=x[1];
	}
	return f;
}

//******* </URLs TOOLS> ***********************************

//******* <FORMS TOOLS> ***********************************

//Reset les valeurs des champs de la partie du formulaire spécifiée
// éléments gérés : input (text,hidden,checkbox)
//					select
function resetZonePartForm(idZoneForm){

    var inputElements = document.getElementById(idZoneForm).getElementsByTagName('input');	                                                                
	var i;
    for(i in inputElements){
    
		switch(inputElements[i].type){
			case 'checkbox' : 	inputElements[i].checked=false;
			//mlog(inputElements[i].name+'=> false');
								break;
			case 'text'		:   inputElements[i].value='';
			//mlog(inputElements[i].name+'=> ""');
								break;
			case 'hidden'	:	inputElements[i].value='';
			//mlog(inputElements[i].name+'=> ""');
								break;
		}	
	}
	var selectElements = document.getElementById(idZoneForm).getElementsByTagName('select');
	
	var j;
	for(j in selectElements){
		selectElements[j].value='';
	}
	
}

//Cette fonction reporte la valeur du checkbox sur lequel elle écoute les events
//dans un champ du même nom que le checkbox dans le modèle spécifié en paramètre
function reportCheckboxInModel(modelName){
	//cette fonction gère que les éléments checkbox
	if(window.event.srcElement.type=='checkbox'){
		if(window.event.srcElement.checked){
			if(modelName==null){
				document.getElementById(window.event.srcElement.name).value='true';
			}
			else{
				document.getElementById(modelName+'.'+window.event.srcElement.name).value='true';
			}
		}
		else{
			if(modelName==null){
				document.getElementById(window.event.srcElement.name).value='false';
			}
			else{
				document.getElementById(modelName+'.'+window.event.srcElement.name).value='false';
			}
		}
	}
	else{	
		alert('reportCheckboxInModel#util.js is not allowed for this type of element:'+window.event.srcElement.type);
	}
}



//******* </FORMS TOOLS> **********************************


//******* <TABLE TOOLS> **********************************

function initPagination(idPrefixLine,previousElmt,nextElmt,countPageElmt,showCss,hideCss,pageSize,totalSize){
	var prestPagination = new jsPagination();

	prestPagination.previousLink = previousElmt;
	prestPagination.nextLink = nextElmt;
	prestPagination.hideCssClass = hideCss;
	prestPagination.showCssClass = showCss;
	prestPagination.pageSize = pageSize;
	prestPagination.idPrefixLine = idPrefixLine;
	prestPagination.totalSize = totalSize;
	prestPagination.pageIndicationContainer = countPageElmt;
	prestPagination.init();


}

var jsPagination = function() {
	this.previousLink = null;
	this.nextLink = null;
	this.pageIndicationContainer = null;
	this.hideCssClass = null;
	this.showCssClass = null;
	this.pageSize = null;
	this.totalSize = null;
	this.idPrefixLine = null;

	var refObj = this;
	
	this.init = function(){
		//on commence tjours une pagination à la 1ere page, donc pas de lien prev.
		this.previousLink.style.display='none';
		
		var nbPages = Math.ceil(this.totalSize/this.pageSize);
		if(nbPages==1) this.nextLink.style.display='none';
		this.pageIndicationContainer.innerHTML='1/'+nbPages;
		
		
		this.previousLink.onclick = function(){
			mlog("---prev process---");
			mlog(refObj.getNewPage(0)+"->go to "+refObj.getNewPage(-1));
			//mlog("we want to go to page "+refObj.getActivePage()+1+"then go to "(refObj.getActivePage()-1)*this.pageSize);
			mlog("---/prev process---");
			refObj.displayPage(refObj.getNewPage(-1));
			
			//test si previous doit toujours être affiché
			refObj.nextLink.style.display='inline';
			if(refObj.getNewPage(0)==0)
				this.style.display='none';
				
		};
		
		
		this.nextLink.onclick = function(){
			
			
				mlog("---next process---");
				mlog(refObj.getNewPage(0)+"->go to "+refObj.getNewPage(1));
				//mlog("we want to go to page "+refObj.getActivePage()+1+"then go to "(refObj.getActivePage()+1)*this.pageSize);
				mlog("---/next process---");
				refObj.displayPage(refObj.getNewPage(1));
				//test si next doit tjours être affiché
				refObj.previousLink.style.display='inline';
				if(refObj.getNewPage(0)+1==nbPages)
					this.style.display='none';
	
			
		};
	};
	
	
	//retourne la page sur laquelle on doit aller (première page = page 0)
	//param interval : nombre de pages à ajouter à la page courante
	this.getNewPage = function(interval){
		//on chope tous les elements qui sont affiché et ligne
		//qui ont class = showCssClass et qui ont un id commencer par idPrefixLine
		
		//on prend le premier qui est affiché => calcul page suivante
		var showLines = getElementsByClassName(this.showCssClass);
		var minLineNumberShow = "-1";
		for(var i in showLines){
			//on s'assure que l'on a bien une ligne inclus dans la pagination
			//car pourrait avoir la classe sans devoir faire partie de la pagination (id)
			if(showLines[i].id.lastIndexOf(this.idPrefixLine)>-1){
				
				var lineNumber = showLines[i].id.substr(this.idPrefixLine.length);
				if(parseInt(lineNumber)<parseInt(minLineNumberShow) || parseInt(minLineNumberShow)==-1)
					minLineNumberShow = lineNumber;
			}
				
		}
		return (parseInt(parseInt(minLineNumberShow)/this.pageSize)+interval);
		
		
	};
	
	//Affiche les prestations dont l'id correspond au n° de page (selon pageSize)
	//
	this.displayPage = function(numero){
		
		var startId = numero*this.pageSize;
		//on cache les lignes qui sont affichées actuellement
		var showLines = getElementsByClassName(this.showCssClass);
		for(var i in showLines){
			jscss("swap", showLines[i], this.showCssClass, this.hideCssClass);
		}
		//on affiche la ligne id=0 à 14,15 à 29
		for(var i=startId;i<startId+this.pageSize;i++){
			var currentId = this.idPrefixLine.concat(i.toString());
			if(document.getElementById(currentId))
				jscss("swap", document.getElementById(currentId), this.hideCssClass, this.showCssClass);
		}
		
		var nbPages = Math.ceil(this.totalSize/this.pageSize);	
		this.pageIndicationContainer.innerHTML='';
		this.pageIndicationContainer.innerHTML=(this.getNewPage(0)+1)+'/'+nbPages;
		mlog('displayed page :'+(this.getNewPage(0)+1)+'/'+nbPages);
		
	};

};

function initMoreInfosElements(prefixPreviewId,suffixMore,nbElements){
	var moreInfos = new moreInfosElements();

	moreInfos.prefixPreviewId = prefixPreviewId;
	moreInfos.suffixMore = suffixMore;
	moreInfos.nbElements = nbElements;
	
	moreInfos.init();

}

var moreInfosElements = function(){
	this.prefixPreviewId = null;
	this.suffixMore = null;
	this.nbElements = null;
	
	var refObj = this;
	this.init = function(){
		//on parcours tous les elements 
		try{
			for(var i=0;i<this.nbElements;i++){
				//document.getElementById(this.prefixPreviewId.concat(i.toString()).concat(this.suffixMore)).style.display='none';
				document.getElementById(this.prefixPreviewId.concat(i.toString())).onclick = function(){
					var moreElement = document.getElementById(this.id.concat(refObj.suffixMore));
					if(moreElement.style.display == 'none')
						moreElement.style.display = 'block';
					else
						moreElement.style.display = 'none';
						
				};
			}
		}catch(e){
			mlog('ERROR:unable to init moreInfosElements, check that '+this.prefixPreviewId+' is followed by a digit','red');
		}
	};
};

//******* </TABLE TOOLS> **********************************


//******* <EXTERNAL TOOLS> ********************************
/*
Developed by Robert Nyman, http://www.robertnyman.com
Code/licensing: http://code.google.com/p/getelementsbyclassname/
*/	
var getElementsByClassName = function (className, tag, elm){
if (document.getElementsByClassName) {
	getElementsByClassName = function (className, tag, elm) {
		elm = elm || document;
		var elements = elm.getElementsByClassName(className),
			nodeName = (tag)? new RegExp("\\b" + tag + "\\b", "i") : null,
			returnElements = [],
			current;
		for(var i=0, il=elements.length; i<il; i+=1){
			current = elements[i];
			if(!nodeName || nodeName.test(current.nodeName)) {
				returnElements.push(current);
			}
		}
		return returnElements;
	};
}
else if (document.evaluate) {
	getElementsByClassName = function (className, tag, elm) {
		tag = tag || "*";
		elm = elm || document;
		var classes = className.split(" "),
			classesToCheck = "",
			xhtmlNamespace = "http://www.w3.org/1999/xhtml",
			namespaceResolver = (document.documentElement.namespaceURI === xhtmlNamespace)? xhtmlNamespace : null,
			returnElements = [],
			elements,
			node;
		for(var j=0, jl=classes.length; j<jl; j+=1){
			classesToCheck += "[contains(concat(' ', @class, ' '), ' " + classes[j] + " ')]";
		}
		try	{
			elements = document.evaluate(".//" + tag + classesToCheck, elm, namespaceResolver, 0, null);
		}
		catch (e) {
			elements = document.evaluate(".//" + tag + classesToCheck, elm, null, 0, null);
		}
		while ((node = elements.iterateNext())) {
			returnElements.push(node);
		}
		return returnElements;
	};
}
else {
	getElementsByClassName = function (className, tag, elm) {
		tag = tag || "*";
		elm = elm || document;
		var classes = className.split(" "),
			classesToCheck = [],
			elements = (tag === "*" && elm.all)? elm.all : elm.getElementsByTagName(tag),
			current,
			returnElements = [],
			match;
		for(var k=0, kl=classes.length; k<kl; k+=1){
			classesToCheck.push(new RegExp("(^|\\s)" + classes[k] + "(\\s|$)"));
		}
		for(var l=0, ll=elements.length; l<ll; l+=1){
			current = elements[l];
			match = false;
			for(var m=0, ml=classesToCheck.length; m<ml; m+=1){
				match = classesToCheck[m].test(current.className);
				if (!match) {
					break;
				}
			}
			if (match) {
				returnElements.push(current);
			}
		}
		return returnElements;
	};
}
return getElementsByClassName(className, tag, elm);
};

//******* </EXTERNAL TOOLS> ********************************