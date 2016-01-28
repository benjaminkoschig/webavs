function SetCookie (name, value) {
	var argv=SetCookie.arguments;
	var argc=SetCookie.arguments.length;
	var expires=(argc > 2) ? argv[2] : null;
	var path=(argc > 3) ? argv[3] : null;
	var domain=(argc > 4) ? argv[4] : null;
	var secure=(argc > 5) ? argv[5] : false;
	document.cookie=name+"="+escape(value)+
		((expires==null) ? "" : ("; expires="+expires.toGMTString()))+
		((path==null) ? "" : ("; path="+path))+
		((domain==null) ? "" : ("; domain="+domain))+
		((secure==true) ? "; secure" : "");
}

// Utilisation :
//	var pathname=location.pathname;
//	var myDomain=pathname.substring(0,pathname.lastIndexOf('/')) +'/';
//	var date_exp = new Date();
//	date_exp.setTime(date_exp.getTime()+(365*24*3600*1000));
//	// Ici on définit une durée de vie de 365 jours
//	SetCookie("prenom","Arthur",date_exp,myDomain);

function getCookieVal(offset) {
	var endstr=document.cookie.indexOf (";", offset);
	if (endstr==-1)
      		endstr=document.cookie.length;
	return unescape(document.cookie.substring(offset, endstr));
}
function GetCookie (name) {
	var arg=name+"=";
	var alen=arg.length;
	var clen=document.cookie.length;
	var i=0;
	while (i<clen) {
		var j=i+alen;
		if (document.cookie.substring(i, j)==arg)
                        return getCookieVal (j);
                i=document.cookie.indexOf(" ",i)+1;
                        if (i==0) break;}
	return null;
}

// Utilisation :
// 	le_prenom=GetCookie("prenom");


var dragColor = "#B3C4DB";
var currRow = -1;
var selRow = -1;
var element;

/// sort
//
// global variables
//
var tbody=null;
var theadrow=null;
var colCount = null;


var reverse = false;
var lastclick = -1;//					 stores the object of our last used object

var arrHitTest = new Array();
var bDragMode = false;
var objDragItem;
var arrHitTest = new Array();
var iArrayHit = false;

function tableInit(el) {

	element = el;
	if (element.tagName == 'TABLE')
{
//	element.attachEvent('onmouseover', onMouseOver);
//	element.attachEvent('onmouseout', onMouseOut);
//	element.attachEvent('onclick', onClick);
}
else
{
	alert("Error: tableAct not attached to a table element");
}


//	 get TBODY - take the first TBODY for the table to sort
	tbody = element.tBodies(0);
	if (!tbody) alert("error tbody");

//	Get THEAD
	var thead = element.tHead;
	if (!thead)  alert("error thead");

	theadrow = thead.children[0];// Assume just one Head row
	if (theadrow.tagName != "TR") alert("error tr");

	theadrow.runtimeStyle.cursor = "hand";

	colCount = theadrow.children.length;
	var l, clickCell;
  var cx=0;
  var cy=0;
  var c;


	for (var i=1; i<colCount-1; i++)
	{
//		 Create our blank gif

		l=document.createElement("IMG");
		l.src="/webavs/images/blank.gif";
		l.id="srtImg";
		l.width=15;
		l.height=15;
		l.style.border=0;

		clickCell = theadrow.children[i];
		clickCell.selectIndex = i;

		t=document.createElement("TABLE");
		t.border=0;
		t.cellPadding=0;
		t.cellSpacing=0;
		t.width="100%";

		tr = document.createElement("TR");
		td1 = document.createElement("TD");
		td1.width = "100%";		
		td1.innerHTML = clickCell.innerHTML;
		td1.style.fontSize = clickCell.currentStyle.fontSize;
		td1.style.fontWeight = clickCell.currentStyle.fontWeight;
		td1.style.color = clickCell.currentStyle.color;
		
		td2 = document.createElement("TD");
		td2.align = "right";
		td2.appendChild(l);
				
		tr.appendChild(td1);
		tr.appendChild(td2);
		t.appendChild(tr);

		clickCell.innerHTML = t.outerHTML;

		clickCell.attachEvent("onclick", doClick);		

        arrHitTest[i] = new Array();

    	c = clickCell.offsetParent;


	   if(cx == 0 && cy == 0 ) {
	    	while (c.offsetParent != null) {
                  cy += c.offsetTop;
                  cx += c.offsetLeft;
                  c = c.offsetParent;
		}
	   }

	arrHitTest[i][0] = cx + clickCell.offsetLeft;
	arrHitTest[i][1] = cy + clickCell.offsetTop;
	arrHitTest[i][2] = clickCell;
	arrHitTest[i][3] = cx + clickCell.offsetLeft + clickCell.clientWidth;	
	clickCell.attachEvent("onmousedown",onMouseDown);
	clickCell.attachEvent("onmouseover",onMouseOver);
	clickCell.attachEvent("onmouseout",onMouseOut);
	}

  defaultTitleColor = "#226194";

  element.document.attachEvent("onmousemove",onMouseMove);
  element.document.attachEvent("onmouseup",onMouseUp);
  element.document.attachEvent("onselectstart",onSelect);  
}

//
// doClick handler
//
//
function doClick(e)
{
	var clickObject = e.srcElement;

	while (clickObject.tagName != "TH")
	{
		clickObject = clickObject.parentElement;
	}


//	 clear the sort images in the head
	var imgcol= theadrow.all('srtimg');
	for(var x = 0; x < imgcol.length; x++)
		imgcol[x].src = "/webavs/images/blank.gif";

	if(lastclick == clickObject.selectIndex)
	{
		if(reverse == false)
		{
			clickObject.all('srtimg').src = "/webavs/images/down.gif";
		      reverse = true;
		}
		else
		{
			clickObject.all('srtimg').src = "/webavs/images/up.gif";
			reverse = false;
		}
	}
	else
	{
		reverse = false;
		lastclick = clickObject.selectIndex;
		clickObject.all('srtimg').src = "/webavs/images/up.gif";
	}

	insertionSort(tbody, tbody.rows.length-1,  reverse, clickObject.selectIndex);
	
	for (var i=0; i<tbody.rows.length ; i++) {
		if (i%2 == 0) {
			tbody.rows[i].style.background	= finds('.row').style.background;
			tbody.rows[i].style.color	= finds('.row').style.color;
		} else {
			tbody.rows[i].style.background	= finds('.rowOdd').style.background;
			tbody.rows[i].style.color	= finds('.rowOdd').style.color;
		}
	}

}

function insertionSort(t, iRowEnd, fReverse, iColumn)
{
	var iRowInsertRow, iRowWalkRow, current, insert;
    for ( iRowInsert = 0 + 1 ; iRowInsert <= iRowEnd ; iRowInsert++ )
    {
        if (iColumn) {
		if( typeof(t.children[iRowInsert].children[iColumn]) != "undefined")
     		      textRowInsert = t.children[iRowInsert].children[iColumn].innerText;
		else
			textRowInsert = "";
        } else {
           textRowInsert = t.children[iRowInsert].innerText;
        }

        for ( iRowWalk = 0; iRowWalk <= iRowInsert ; iRowWalk++ )
        {
            if (iColumn) {
			if(typeof(t.children[iRowWalk].children[iColumn]) != "undefined")
				textRowCurrent = t.children[iRowWalk].children[iColumn].innerText;
			else
				textRowCurrent = "";
            } else {
			textRowCurrent = t.children[iRowWalk].innerText;
            }

//		
//		 We save our values so we can manipulate the numbers for
//		 comparison
//		
		current = textRowCurrent;
		insert  = textRowInsert;


//		  If the value is not a number, we sort normally, else we evaluate
//		  the value to get a numeric representation
//		

		var re = /'/g;
		current = current.replace(re, "");
		insert = insert.replace(re, "");
//		alert(insert,current);

		if ( !isNaN(current) ||  !isNaN(insert))
		{
			current= eval(current);			
			insert= eval(current);
		}
		else
		{
			current	= current.toLowerCase();
			insert	= insert.toLowerCase();
		}


            if ( (   (!fReverse && insert < current)
                 || ( fReverse && insert > current) )
                 && (iRowInsert != iRowWalk) )
            {
		    eRowInsert = t.children[iRowInsert];
                eRowWalk = t.children[iRowWalk];
                t.insertBefore(eRowInsert, eRowWalk);
                iRowWalk = iRowInsert;//  done
            }
        }
    }
}


/// Drag

function InitHeader()
{
  var cx=0;
  var cy=0;
  var c;

  for (i=1; i<colCount-1 ; i++) {

	var clickCell = theadrow.children[i];
	clickCell.selectIndex = i;
	c = clickCell.offsetParent;

	if(cx == 0 && cy == 0 )
	{
		while (c.offsetParent != null) {
                  cy += c.offsetTop;
                  cx += c.offsetLeft;
                  c = c.offsetParent;
		}
	}

	arrHitTest[i][0] = cx + clickCell.offsetLeft;
	arrHitTest[i][1] = cy + clickCell.offsetTop;
	arrHitTest[i][2] = clickCell;
	arrHitTest[i][3] = cx + clickCell.offsetLeft + clickCell.clientWidth;
  }
}

function onSelect()
{
//	disable selection
	return false;
}

function ChangeHeader(iChange)
{
/*	
	for(var y = 1; y < arrHitTest.length; y++) {
		if (arrHitTest[y][2].style.backgroundColor == dragColor) {
			arrHitTest[y][2].style.backgroundColor = defaultTitleColor;
		}
	}

	if(iChange == "-1") return;

	arrHitTest[iChange][2].style.backgroundColor = dragColor;
	*/
	for(var y = 1; y < arrHitTest.length; y++) 
		if (y == iChange) 
			arrHitTest[y][2].style.backgroundColor = dragColor; 
		else
			arrHitTest[y][2].style.backgroundColor = defaultTitleColor;
}

function onMouseOver(e) {
	var src 	= e.srcElement;
	while (src.tagName != "TH")
		src = src.parentElement;
	src.className="mouseover";
}

function onMouseOut(e) {
	var src 	= e.srcElement;
	while (src.tagName != "TH")
		src = src.parentElement;
	src.className="";
}

function onMouseUp(e)
{
	if(!bDragMode)	return;
	bDragMode = false;

	var iSelected = objDragItem.selectIndex;

	objDragItem.removeNode(true);
	objDragItem = null;

	ChangeHeader(-1);

	if( (iArrayHit - 1) < 0 || iSelected < 0) return;//	 default faliure

	CopyRow(iSelected, (iArrayHit - 1) );
	

//	 Reset our variables
	iSelected = 0;
	iArrayHit = -1;
}

function onMouseDown(e)
{
	bDragMode 	= true;
	var src 	= e.srcElement;
	var c 	= e.srcElement;

	while (src.tagName != "TH")
		src = src.parentElement;

//	 Create our header on the fly
	objDragItem = document.createElement("DIV");
	objDragItem.innerHTML		= src.innerHTML;
	objDragItem.style.height	= src.clientHeight;
	objDragItem.style.width 	= src.clientWidth;
	objDragItem.style.background 	= src.currentStyle.backgroundColor;
	objDragItem.style.fontColor	= src.currentStyle.fontColor;
	objDragItem.style.filter	= "progid:DXImageTransform.Microsoft.Alpha(opacity = 50)";
	objDragItem.style.position 	= "absolute";
	objDragItem.selectIndex		= src.selectIndex;
	while (c.offsetParent != null)
        {
		objDragItem.style.y += c.offsetTop;
		objDragItem.style.x += c.offsetLeft;
		c = c.offsetParent;
	}
 	objDragItem.style.borderStyle	= "outset";
	objDragItem.style.display	= "none";

	src.insertBefore(objDragItem);
}

function onMouseMove(e)
{
	if(!bDragMode || !objDragItem) return;//	 If we aren't dragging or our object
//								 is null, we return

//	 Hardcoded value for height difference
	var midWObj = objDragItem.clientWidth /2;
	var midHObj = objDragItem.clientHeight /2;

//	 Save mouse's position in the document
     var intTop = e.clientY + element.document.body.scrollTop;
     var intLeft = e.clientX + element.document.body.scrollLeft;


	var cx=0,cy=0;
	var elCurrent = objDragItem.offsetParent;
               while (elCurrent.offsetParent != null) {
                  cx += elCurrent.offsetTop;
                  cy += elCurrent.offsetLeft;
                  elCurrent = elCurrent.offsetParent;
               }	

	if (intTop >= 0)  objDragItem.style.pixelTop  = intTop  - midHObj;
	if (intLeft >= 0) objDragItem.style.pixelLeft = intLeft - midWObj;


	if(objDragItem.style.display == "none") objDragItem.style.display = "";

	iArrayHit = CheckHit(intTop , intLeft , e);
	e.cancelBubble = false;
	e.returnValue = false;
}

function CheckHit(x,y,e)
{
	midWObj = objDragItem.clientWidth / 2;
	midHObj = 12;

	if( ((x) > (arrHitTest[1][1] + 20) ) || ( (x) < (arrHitTest[1][1]) ) )
	{
		ChangeHeader(-1);
		return -1;
	}

	for(var i=2; i < colCount-1; i++)
	{
		if( (y) > (arrHitTest[i][0]) && (y) < (arrHitTest[i][3] ))// + 100))
		{
			ChangeHeader(i);
			return i + 1;
		}
	}
	return -1;
}

//
// Copy from row to row.. Does the Header also.
//
function CopyRow(from, to)
{
	if(from == to) return;


	var origfrom = from;
	var origto = to;
	var iDiff = 0;

	if( from > to )
	{

		iDiff = from - to;

		var saveObj 	= theadrow.children[from].innerHTML;
		var saveWidth 	= theadrow.children[from].width;
		var saveTitle   = theadrow.children[from].title;

		for(var i = 0 ; i < iDiff; i++)
		{
			theadrow.children[from].innerHTML = theadrow.children[from - 1].innerHTML;
			theadrow.children[from].width = theadrow.children[from - 1].width;
			theadrow.children[from].title = theadrow.children[from - 1].title;
			from--;
		}
		theadrow.children[to].innerHTML 	= saveObj;
		theadrow.children[to].width = saveWidth;
		theadrow.children[to].title = saveTitle;

	}
	else
	{

		iDiff = to - from;

		var saveObj = theadrow.children[from].innerHTML;
		var saveWidth 	= theadrow.children[from].width;
		var saveTitle   = theadrow.children[from].title;

		for(var i = 0 ; i < iDiff; i++)
		{
			theadrow.children[from].innerHTML = theadrow.children[from + 1].innerHTML;
			theadrow.children[from].width = theadrow.children[from + 1].width;
			theadrow.children[from].title = theadrow.children[from + 1].title;
			from++;
		}

		theadrow.children[to].innerHTML 	= saveObj;
		theadrow.children[to].width = saveWidth;
		theadrow.children[to].title = saveTitle;
	}



	for(var i = 0 ; i < theadrow.children.length; i++)
			theadrow.children[i].selectIndex = i;

	InitHeader();
	for ( var iRowInsert = 0 ; iRowInsert < tbody.rows.length; iRowInsert++ )
	{
		from = origfrom;
		to = origto;
		if( from > to )
		{
			iDiff = from - to;
			var saveObj = tbody.children[iRowInsert].children[from].innerHTML;
			var saveTitle = tbody.children[iRowInsert].children[from].title;
			for(var i = 0 ; i < iDiff; i++)
			{
				tbody.children[iRowInsert].children[from].innerHTML 	= tbody.children[iRowInsert].children[from - 1].innerHTML;
				tbody.children[iRowInsert].children[from].title		= tbody.children[iRowInsert].children[from - 1].title;
				from--;
			}
			tbody.children[iRowInsert].children[to].innerHTML = saveObj;
			tbody.children[iRowInsert].children[to].title = saveTitle;

		}
		else
		{
			iDiff = to - from;
			var saveObj = tbody.children[iRowInsert].children[from].innerHTML;
			var saveTitle = tbody.children[iRowInsert].children[from].title;
			for(var i = 0 ; i < iDiff; i++)
			{
				tbody.children[iRowInsert].children[from].innerHTML 	= tbody.children[iRowInsert].children[from + 1].innerHTML;
				tbody.children[iRowInsert].children[from].title		= tbody.children[iRowInsert].children[from + 1].title;
				from++;
			}
			tbody.children[iRowInsert].children[to].innerHTML = saveObj;
			tbody.children[iRowInsert].children[to].title = saveTitle;
		}
	}
}