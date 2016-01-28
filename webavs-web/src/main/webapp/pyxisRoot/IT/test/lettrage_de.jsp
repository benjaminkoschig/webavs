<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<%
bButtonCancel = false;
bButtonValidate = false;
bButtonDelete = false;
bButtonUpdate = false;
bButtonNew = false;
idEcran = "GTI5005";
globaz.pyxis.vb.test.TILettrageViewBean viewBean = (globaz.pyxis.vb.test.TILettrageViewBean)request.getAttribute("viewBean");

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>
<%@page import="globaz.pyxis.summary.TISummary"%>
<%@page import="globaz.pyxis.summary.ITIBaseSummarizable"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<style>

.mtd {
    padding : 1 1 1 1;
    border : none 0px ;
    border-top : solid 1px silver;
    border-right : solid 1px silver;
}
.lr {
	border-right : solid 1px white;
	border-top : solid 1px silver;
    
}
.mtable  {
    border : none 0px ;
}
.h {
    background : #f0f0f0;
    border-top : solid 1px silver;
    border-right : solid 1px silver;

}
.vh {
    background : #f0f0f0;
    border-top : solid 1px silver;
    border-right : solid 1px silver;
	text-align:right;
}
.hsum {
    background : #ffffcc;
    border-top : solid 1px silver;
    border-right : solid 1px black;
	width : 3cm;
	text-align:right;
	font-size : 10pt;
	font-family : courier;
}
.vsum {
    background : #ffffcc;
	text-align:right;
	border-bottom : solid 1px black;
	border-right : solid 1px silver;
	font-size : 10pt;
	font-family : courier;
}
.i {
    border : none 0px ;
    text-align:right;
	width : 100%;
	font-size : 10pt;
	font-family : courier;
	padding : 0 0 0 0;
}

</style>
<SCRIPT language="JavaScript">
top.document.title = "Test"
<!-- hide this script from non-javascript-enabled browsers



var edit = true
function hkd(event,e) {
str = e.id
x = str.substring(str.indexOf("x")+1,str.indexOf("y"))
y = str.substring(str.indexOf("y")+1)	

var _do = false;
if (event.keyCode==13) {
	edit = !edit
	if (edit) {
		e.parentNode.style.borderWidth = '1px'
		e.parentNode.style.borderStyle= 'solid'
		e.parentNode.style.borderColor = "black"
		
	} else {
		e.parentNode.style.borderColor = "silver"
		e.parentNode.style.borderLeftWidth = '0px'
		e.parentNode.style.borderBottomWidth = '0px'
		
	}
} 

if(edit) {
	switch (event.keyCode) {
		case 40 : y ++ ; _do=true; break;
		case 38 : y -- ; _do=true;break;
		case 37 : x -- ;_do=true; break
		case 39 : x ++ ; _do=true;break;
	}
	if (_do) {
		ne = document.getElementById('x'+x+'y'+y)
		if (ne != null) {
			document.getElementById('x'+x+'y'+y).focus();
		}
	} 
}
}
function hku(e) {
var str = e.id
var x = str.substring(str.indexOf("x")+1,str.indexOf("y"))
var y = str.substring(str.indexOf("y")+1)	
var a = e.name

//88 = x
if (event.keyCode==88) {
	// calcul de la valeur à utiliser en contion de la somme à couvrir et du montant disponible
	var sommeACouvrir = parseFloat(document.getElementById("px"+x+a).innerText.replace(/'/,""))  // header de la colonne
	var montantDisponible = Math.abs(parseFloat(document.getElementById("n"+y).innerText.replace(/'/,"")))  // header de  la ligne
	var somme=0
	if (sommeACouvrir <= montantDisponible) {
		somme = sommeACouvrir
	} else {
		somme = montantDisponible
	}
	e.value=number_format(somme,2,".","'" )
}



// sum neg
var tds = document.getElementById("n"+y).parentNode.getElementsByTagName("td");
var sum = 0;
for (i= 3;i< tds.length;i++) {
	var v = tds[i].getElementsByTagName("input")[0].value.replace(/'/,"")
	if (v != "") {
		sum += parseFloat(v)
	}
}
var montant = parseFloat(tds[1].innerText)
document.getElementById("n"+y).innerHTML = number_format(montant+sum,2,".","'" )

// sum pos
sum = 0;
var inputs = document.getElementsByName(a);
for (i= 0;i< inputs.length;i++) {
	var ip= inputs[i]
	if (ip != null) {
		var x1 = ip.id.substring(str.indexOf("x")+1,str.indexOf("y"))
		if ( x == x1) {
			v= ip.value.replace(/'/,"")
			if (v != "") {
				sum += parseFloat(v)
			}
		}
	} 
}
montant = parseFloat(document.getElementById("sx"+x+a).innerText)
document.getElementById("px"+x+a).innerHTML = number_format(montant-sum,2,".","'" )

}

function _b(e) {
	e.style.textAlign = 'right';
	eTd = e.parentNode
	eTd.style.borderColor = "silver"
	eTd.style.borderLeftWidth = '0px'
	eTd.style.borderBottomWidth = '0px'
	var v = e.value.replace(/'/,"")
	if (!isNaN(v) && ""!=v) {
		e.value  = number_format(v,2,".","'" )
	}
}
function _f(e) {
if (edit) {
	eTd = e.parentNode
	eTd.style.borderWidth = '1px'
	eTd.style.borderStyle= 'solid'
	eTd.style.borderColor = "black"

}
e.style.textAlign = 'left';
e.select()
}

function number_format(a, b, c, d) {
 a = Math.round(a * Math.pow(10, b)) / Math.pow(10, b);
 var neg = "";
 if (a < 0) {
	neg = "-"
	a = Math.abs(a)
 }
 e = a + '';
 f = e.split('.');
 if (!f[0]) {
  f[0] = '0';
 }
 if (!f[1]) {
  f[1] = '';
 }
 if (f[1].length < b) {
  g = f[1];
  for (i=f[1].length + 1; i <= b; i++) {
   g += '0';
  }
  f[1] = g;
 }
 if(d != '' && f[0].length > 3) {
  h = f[0];
  f[0] = '';
  for(j = 3; j < h.length; j+=3) {
   i = h.slice(h.length - j, h.length - j + 3);
   f[0] = d + i +  f[0] + '';
  }
  j = h.substr(0, (h.length % 3 == 0) ? 3 : (h.length % 3));
  f[0] = j + f[0];
 }
 c = (b <= 0) ? '' : c;
 return neg+f[0] + c + f[1];
}


function getCompteALettrer(query) {
	document.getElementById('btSearch').disabled=true;
	document.getElementById('list').innerHTML=""
	document.getElementById('work').innerHTML="Chargement des données en cours..."

	if (window.XMLHttpRequest) req1 = new XMLHttpRequest();
	else if (window.ActiveXObject) req1 = new ActiveXObject("Microsoft.XMLHTTP");
	else return; // fall on our sword
	req1.open('GET', query,true);
	req1.onreadystatechange = onReady;
	req1.send(null);
}
function onReady() {
	if (req1.readyState != 4) { 
		return;
	}
	if ((req1.status == 0)||(req1.status == 200)) {
		var result = req1.responseText;
		document.getElementById('btSearch').disabled=false;
		document.getElementById('list').innerHTML = result
		document.getElementById('work').innerHTML="Chargement terminé."
		document.getElementById('resultList').onclick = function(){
			getSectionsForCompte('<%=request.getContextPath()%>/pyxis?userAction=pyxis.test.lettrage.querySection&id='+document.getElementById('resultList').value);
		}
	}
}


function getSectionsForCompte(query) {
	document.getElementById('btSearch').disabled=true;
	document.getElementById('list').disabled=true;
	//document.getElementById('work').innerHTML="Chargement des données en cours..."

	if (window.XMLHttpRequest) req1 = new XMLHttpRequest();
	else if (window.ActiveXObject) req1 = new ActiveXObject("Microsoft.XMLHTTP");
	else return; // fall on our sword
	req1.open('GET', query,true);
	req1.onreadystatechange = onReadyLoadSection;
	req1.send(null);
}
function onReadyLoadSection() {
	if (req1.readyState != 4) { 
		return;
	}
	if ((req1.status == 0)||(req1.status == 200)) {
		var result = req1.responseText;
		document.getElementById('btSearch').disabled=false;
		document.getElementById('list').disabled=false;
		document.getElementById('work').innerHTML=result
	}
}



function add() {
}
function upd() {
}
function validate() {
}
function cancel() {
}
function del() {
}
function init(){}

// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Test<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
						<tr><td>
						<table border="1" >
							<tr>
								<td colspan=2>
									<div id="search" >
										<input type="text" id="from">
										<input type="text" id="to">
										<input type="button" id="btSearch" value="chercher"
											onclick="getCompteALettrer('<%=request.getContextPath()%>/pyxis?userAction=pyxis.test.lettrage.query&from='+document.getElementById('from').value+'&to='+document.getElementById('to').value)">
									</div>
								</td>
							</tr>
							<tr style="background:white">
								<td valign="top" ><div id="list"></div></td>
								<td valign="top" width="100%"><div id="work" style="background:white"></div></td>
							</tr>
						</table>	
						</td></tr>				
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>