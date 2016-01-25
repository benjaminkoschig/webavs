<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.osiris.external.IntRole"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit"  --%>
<%@page import="globaz.osiris.db.comptes.*" %>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%
bButtonCancel = false;
bButtonValidate = false;
bButtonDelete = false;
bButtonUpdate = false;
bButtonNew = false;
idEcran = "GCA70002"; 
globaz.osiris.vb.lettrage.CALettrageViewBean viewBean = (globaz.osiris.vb.lettrage.CALettrageViewBean)request.getAttribute("viewBean");

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness"  --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts"  --%>

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

function showPriorities() {
	var els = document.getElementsByTagName("div")
	
	for (i = 0; i < els.length; i++) {
		if ("pri" == els[i].name) {
			els[i].style.display = "block";
		}
	}
}
function hidePriorities() {
	var els = document.getElementsByTagName("div")
	
	for (i = 0; i < els.length; i++) {
		if ("pri" == els[i].name) {
			els[i].style.display = "none";
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
	
	// maj montants sections créditrices (montant neg)
	var tds = document.getElementById("n"+y).parentNode.getElementsByTagName("td");
	var sum = 0;
	for (i= 3;i< tds.length;i++) {
		var v = tds[i].getElementsByTagName("input")[0].value.replace(/'/,"")
		if (v != "") {
			sum += parseFloat(v)
		}
	}
	var montant = parseFloat(tds[1].innerText)
	if (!isNaN(montant+sum)) {
		document.getElementById("n"+y).innerHTML = number_format((montant+sum) *-1,2,".","'" )
		if (((montant+sum) *-1) < 0 ) {
			document.getElementById("n"+y).style.color = "red"
		} else {
			document.getElementById("n"+y).style.color = "black"
		}
	} else {
		document.getElementById("n"+y).style.color = "red"
		document.getElementById("n"+y).innerHTML ="####"
	}
	
	// maj montants sections débitrices (montants pos)
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

	/*
	 * MAJ du total de la colone
	 */
	montant = parseFloat(document.getElementById("sx"+x+a).innerText)
	if (!isNaN(montant-sum)) {
		document.getElementById("px"+x+a).innerHTML = number_format(montant-sum,2,".","'" )
		if (montant-sum < 0 ) {
			document.getElementById("px"+x+a).style.color = "red"
		} else {
			document.getElementById("px"+x+a).style.color = "black"
		}
	} else {
		document.getElementById("px"+x+a).style.color = "red"
		document.getElementById("px"+x+a).innerHTML ="####"
	}

	/*
	 * Verifie si les montant si OK pour lettrage et affiche ou non le bouton de lettrage
	 */
	 showBtLettrage = true;
	
	 for (x= 0;x< parseInt(document.getElementById("nbDebitrices").value);x++) {
		 montant = parseFloat(document.getElementById("px"+x+a).innerText)
		 if (isNaN(montant) || (montant < 0 )) {
			 showBtLettrage = false;
			 break;
		 } 
	 } 
	 if (showBtLettrage) {
		 for (y= 0;y< parseInt(document.getElementById("nbCreditrices").value);y++) {
			 montant = parseFloat(document.getElementById("n"+y).innerText)
			 if (isNaN(montant) || (montant < 0 )) {
				 showBtLettrage = false;
				 break;
			 } 
		 } 	
	 }

	 if (showBtLettrage) {
		document.getElementById("btLettrer").style.display = "inline"
		document.getElementById("msgLettrer").style.display = "none"
	 } else {
		document.getElementById("btLettrer").style.display = "none"
		document.getElementById("msgLettrer").style.display = "block"
	 }
	

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

 
/*
 * A tester
 */ 
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

function _handleTimeout(res) {
	if (res.indexOf("WebAVS - Accueil")>0) {
		top.location.href='<%=request.getContextPath()%>/pyxis'
	}

}

function getCompteALettrer(query) {
	document.getElementById('btSearch').disabled=true;
	document.getElementById('list').innerHTML=""
	document.getElementById('work').innerHTML="<table width='100%' height='100%' ><tr><td width='40%' >&nbsp;</td><td><img src='<%=request.getContextPath()%>/images/loading_animated.gif'></td><td nowrap>Chargement des données en cours...</td><td width='40%'>&nbsp;</td></tr></table>"
	document.getElementById('message').innerHTML=""
	document.getElementById('infoSection').innerHTML=""
 	document.getElementById('status').innerHTML=""
	if (window.XMLHttpRequest) req1 = new XMLHttpRequest();
	else if (window.ActiveXObject) req1 = new ActiveXObject("Microsoft.XMLHTTP");
	else return; 
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
		_handleTimeout(result);
		
		document.getElementById('btSearch').disabled=false;
		document.getElementById('list').innerHTML = result
		document.getElementById('work').innerHTML="Chargement terminé."
		var role = document.getElementById('role').value;

		if (document.getElementById("resultList").length > 0) {
			var compteAnnexe = document.getElementById("resultList").options[0].text;
			var idCompteAnnexe = document.getElementById("resultList").options[0].value;
			getSectionsForCompte(role,compteAnnexe,idCompteAnnexe);
			document.getElementById("resultList").options[0].selected=true
		}
		
		document.getElementById('resultList').style.setExpression("height","document.body.clientHeight-250");
		document.getElementById('resultListCredit').style.setExpression("height","document.body.clientHeight-250");
		document.getElementById('resultListExclu').style.setExpression("height","document.body.clientHeight-250");
		
		// onclick
		document.getElementById('resultList').onchange = function(){
			var index = document.getElementById("resultList").selectedIndex;
			var compteAnnexe = document.getElementById("resultList").options[index].text;
			getSectionsForCompte(role,compteAnnexe,document.getElementById('resultList').value);
		}
		document.getElementById('resultListCredit').onchange = function(){
			var index = document.getElementById("resultListCredit").selectedIndex;
			var compteAnnexe = document.getElementById("resultListCredit").options[index].text;
			getSectionsForCompte(role,compteAnnexe,document.getElementById('resultListCredit').value);
		}
		document.getElementById('resultListExclu').onchange = function(){
			var index = document.getElementById("resultListExclu").selectedIndex;
			var compteAnnexe = document.getElementById("resultListExclu").options[index].text;
			getSectionsForCompte(role,compteAnnexe,document.getElementById('resultListExclu').value);
		}
		
	
		
	} else {
		document.getElementById('work').innerHTML="Une erreur est survenue  : "+req1.status
	}
}


function getSectionsForCompte(role,compteAnnexe,idCompteAnnexe) {
	
	var query = '<%=request.getContextPath()%>/osiris?userAction=osiris.lettrage.main.querySection&role='+role+'&id='+compteAnnexe+'&idCompteAnnexe='+idCompteAnnexe
	
	
	document.getElementById('btSearch').disabled=true;
	document.getElementById('list').disabled=true;
	document.getElementById('message').innerHTML=""
	document.getElementById('infoSection').innerHTML=""

	if (window.XMLHttpRequest) req1 = new XMLHttpRequest();
	else if (window.ActiveXObject) req1 = new ActiveXObject("Microsoft.XMLHTTP");
	else return; // fall on our sword
	req1.open('GET', query,true);
	req1.onreadystatechange = onReadyLoadSection;
	req1.send(null);
}
function onReadyActionPerformed() {
	
	if (req1.readyState != 4) { 
		return;
	}
	if ((req1.status == 0)||(req1.status == 200)) {
		var result = req1.responseText;
		_handleTimeout(result);
		var status = result.substring(0,2)
		result = result.substring(3);
		
		if (status=="NM") {
			document.getElementById('work').innerHTML=result
			showStatus("L'opération a été executée avec succès.","OK");
		} else {
			showStatus(result,status);
		}
		
		document.getElementById('btSearch').disabled=false;
		document.getElementById('list').disabled=false;
		
		try {
			document.getElementById("resultList").focus()
		} catch (e) {
			try {
				document.getElementById("resultListCredit").focus()
			} catch (e1) {
				try {
					document.getElementById("resultListExclu").focus()
				} catch (e2) {}
			}
		}
		
	} else {
		document.getElementById('work').innerHTML="Une erreur est survenue  : "+req1.status
	}
}

function onReadyLoadSection() {
	
	if (req1.readyState != 4) { 
		return;
	}
	if ((req1.status == 0)||(req1.status == 200)) {
		var result = req1.responseText;
		_handleTimeout(result);
		
		document.getElementById('btSearch').disabled=false;
		document.getElementById('list').disabled=false;
		document.getElementById('work').innerHTML=result
		try {
			document.getElementById("resultList").focus()
		} catch (e) {
			try {
				document.getElementById("resultListCredit").focus()
			} catch (e1) {
				try {
					document.getElementById("resultListExclu").focus()
				} catch (e2) {}
			}
		}
		
		
	} else {
		
		document.getElementById('work').innerHTML="Une erreur est survenue  : "+req1.status
	}
}


function report (idCompteAnnexe,idSection,idModeCompensation,comment) {
	
	var role = document.getElementById('role').value;
	com = encodeURIComponent(comment) // utf-8
	query = '<%=request.getContextPath()%>/osiris?userAction=osiris.lettrage.main.reportSection&role='+role+'&idCompteAnnexe='+idCompteAnnexe+'&idSection='+idSection+'&idModeCompensation='+idModeCompensation+'&comment='+com;
	
	if (window.XMLHttpRequest) req1 = new XMLHttpRequest();
	else if (window.ActiveXObject) req1 = new ActiveXObject("Microsoft.XMLHTTP");
	else return; // fall on our sword
	req1.open('GET', query,true);
	req1.onreadystatechange = onReadyActionPerformed; // idem que onReadyLoadSection
	req1.send(null);
}
function versement (idCompteAnnexe,idSection,montantVersement,category) {
	var role = document.getElementById('role').value;
	query = '<%=request.getContextPath()%>/osiris?userAction=osiris.lettrage.main.versementSection&role='+role+'&idCompteAnnexe='+idCompteAnnexe+'&idSection='+idSection+'&montantVersement='+montantVersement+"&category="+category
	if (window.XMLHttpRequest) req1 = new XMLHttpRequest();
	else if (window.ActiveXObject) req1 = new ActiveXObject("Microsoft.XMLHTTP");
	else return; // fall on our sword
	req1.open('GET', query,true);
	req1.onreadystatechange = onReadyActionPerformed; // idem que onReadyLoadSection
	req1.send(null);
}
function annulerVersement (idCompteAnnexe,idSection,idOrdre) {
	var role = document.getElementById('role').value;
	query = '<%=request.getContextPath()%>/osiris?userAction=osiris.lettrage.main.annulerVersementSection&role='+role+'&idCompteAnnexe='+idCompteAnnexe+'&idSection='+idSection+'&idOrdre='+idOrdre
	if (window.XMLHttpRequest) req1 = new XMLHttpRequest();
	else if (window.ActiveXObject) req1 = new ActiveXObject("Microsoft.XMLHTTP");
	else return; // fall on our sword
	req1.open('GET', query,true);
	req1.onreadystatechange = onReadyActionPerformed; // idem que onReadyLoadSection
	req1.send(null);
}

function exclure (idCompteAnnexe,idSection) {
	var role = document.getElementById('role').value;
	query = '<%=request.getContextPath()%>/osiris?userAction=osiris.lettrage.main.exlureSection&role='+role+'&idCompteAnnexe='+idCompteAnnexe+'&idSection='+idSection
	if (window.XMLHttpRequest) req1 = new XMLHttpRequest();
	else if (window.ActiveXObject) req1 = new ActiveXObject("Microsoft.XMLHTTP");
	else return; // fall on our sword
	req1.open('GET', query,true);
	req1.onreadystatechange = onReadyActionPerformed; // idem que onReadyLoadSection
	req1.send(null);
}
function inclure (idExclusion,idCompteAnnexe,idSection) {
	var role = document.getElementById('role').value;
	query = '<%=request.getContextPath()%>/osiris?userAction=osiris.lettrage.main.inclureSection&idExclusion='+idExclusion+'&role='+role+'&idCompteAnnexe='+idCompteAnnexe+'&idSection='+idSection
	if (window.XMLHttpRequest) req1 = new XMLHttpRequest();
	else if (window.ActiveXObject) req1 = new ActiveXObject("Microsoft.XMLHTTP");
	else return; // fall on our sword
	req1.open('GET', query,true);
	req1.onreadystatechange = onReadyActionPerformed; // idem que onReadyLoadSection
	req1.send(null);
}

function doLettrage() {
	document.getElementById('message').innerHTML=""
	document.getElementById('infoSection').innerHTML=""
	var cptannexe = document.getElementById('numeroCompteAnnexe').innerText;
	var idCompteAnnexe = document.getElementById('idCompteAnnexe').value;
	
	var montants = document.getElementsByName("a0");
	var queryParams="p="+idCompteAnnexe+"-"; 
	for (i = 0;i< montants.length;i++) {
		var str = montants[i].id
		var xid = str.substring(str.indexOf("x"),str.indexOf("y"))
		var yid = str.substring(str.indexOf("y"))
		var mx = document.getElementById(xid).value;
		var my = document.getElementById(yid).value;
		queryParams += mx+","+my+","+montants[i].value+";";
	}

	var bsParams="bs="; 
	var bulletinsSoldesAImprimer = document.getElementsByName("BS_");
	for (i = 0;i< bulletinsSoldesAImprimer.length;i++) {
		if (bulletinsSoldesAImprimer[i].checked) {
			bsParams+=bulletinsSoldesAImprimer[i].id+";";
		}
	}

	query ="<%=request.getContextPath()%>/osiris?userAction=osiris.lettrage.main.doLettrage&" +queryParams+"&"+bsParams
		if (window.XMLHttpRequest) req1 = new XMLHttpRequest();
		else if (window.ActiveXObject) req1 = new ActiveXObject("Microsoft.XMLHTTP");
		else return; // fall on our sword
		req1.open('GET', query,true);
		req1.onreadystatechange = onReadyLettrage;
		document.getElementById('work').innerHTML="<table width='100%' height='100%' ><tr><td width='40%' >&nbsp;</td><td><img src='<%=request.getContextPath()%>/images/loading_animated.gif'></td><td nowrap>Lettrage en cours ...</td><td width='40%'>&nbsp;</td></tr></table>"
		req1.send(null); 
	
		
}

function onReadyLettrage() {
	
	if (req1.readyState != 4) { 
		return;
	}
	if ((req1.status == 0)||(req1.status == 200)) {
		var result = req1.responseText;
		_handleTimeout(result);
		var status = result.substring(0,2)
		result = result.substring(3);
		
		showStatus(result,status);
		
		var l = document.getElementById("resultList")
		var current = l.selectedIndex
		if (status == "OK") {
			
			document.getElementById("resultList").remove(current)
			document.getElementById("resultList").selectedIndex = current
		} else if (status == "IN") { //  IN -> INFO
			document.getElementById("resultList").selectedIndex = current
		}
		
		var nextIdCompteAnnexe = document.getElementById("resultList").value
		if (nextIdCompteAnnexe!="") {
			var index = document.getElementById("resultList").selectedIndex;
			var compteAnnexe = document.getElementById("resultList").options[index].text;
			var role = document.getElementById('role').value;
			getSectionsForCompte(role,compteAnnexe,nextIdCompteAnnexe);
		} else {
			document.getElementById('work').innerHTML=""
		}
	} else {
		document.getElementById('work').innerHTML="Une erreur est survenue  : "+req1.status
	}
}

function showStatus(msg,status) {
	var icon ="";
	if (status == "OK") {
		icon = "<img src='<%=request.getContextPath()%>/images/small_good.png'>"
	} else if (status == "KO") {
		icon = "<img src='<%=request.getContextPath()%>/images/small_error.png'>"
	} else if (status == "IN") {
		icon = "<img src='<%=request.getContextPath()%>/images/small_warning.png'>"
	}
	document.getElementById("status").innerHTML = "<div style='border-bottom : solid 1px silver;padding : 1 1 1 1;margin: 2 2 20 2;'><table><tr><td>"+icon+"</td><td>"+msg+"</td></tr></table></div>"
}

function showMessage(msg) {
	
	document.getElementById("message").innerHTML = msg
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
function init(){
	
	
	
}

function infoSection( idSection, idCompteAnnexe) {
	
	query = '<%=request.getContextPath()%>/osiris?userAction=osiris.lettrage.main.infoSection&idCompteAnnexe='+idCompteAnnexe+'&idSection='+idSection
	if (window.XMLHttpRequest) req1 = new XMLHttpRequest();
	else if (window.ActiveXObject) req1 = new ActiveXObject("Microsoft.XMLHTTP");
	else return; // fall on our sword
	req1.open('GET', query,true);
	req1.onreadystatechange = onReadyInfoSection; //
	req1.send(null);

}

function onReadyInfoSection() {
	if (req1.readyState != 4) { 
		return;
	}
	if ((req1.status == 0)||(req1.status == 200)) {
		document.getElementById('btSearch').disabled=false;
		document.getElementById('list').disabled=false;
		
		var result = req1.responseText;
		_handleTimeout(result);
		document.getElementById('infoSection').innerHTML=result
		$('#infoTabs').tabs();
	} else {
		document.getElementById('work').innerHTML="Une erreur est survenue  : "+req1.status
	}
}


function showDialogReport(bt,idCompteAnnexe,idSection,idExterne,modeCompensation,comment) {
	$("#dialog").dialog({ 
		width:500,
		resizable:false,
		position:[$(bt).position().left,$(bt).position().top+16],
		 buttons: {
				'Valider': function(){
					modeCompensation = document.getElementById("idModeCompensation").value
					comment = document.getElementById("texteRemarque").value
					
					report(idCompteAnnexe,idSection,modeCompensation,comment)
					$(this).dialog("close");
				}
			}
	});

	document.getElementById("dialogIds").innerHTML="idSection:"+idSection+" idCompteAnnexe:"+idCompteAnnexe;
	document.getElementById("dialogSection").innerHTML=idExterne;
	document.getElementById("texteRemarque").value=comment;
	document.getElementById("idModeCompensation").value = modeCompensation;
	
	
	
}

// stop hiding -->
</SCRIPT> <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Lettrage de masse<%-- /tpl:put  --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain"  --%>
						<tr><td>
						<table id="innerTable" border="1" >
							<tr>
								<td colspan=2>
									<div id="search" >
										<input type="text" id="from" value="">
										<input type="text" id="to" value="">
										<select name="role" id="role" tabindex="2">
										<% List <String>roleToExclude = new ArrayList<String>();
											roleToExclude.add(IntRole.ROLE_ADMINISTRATEUR);
										%>
              								<%=CARoleViewBean.createOptionsTagsExcludeRole(objSession, request.getParameter("role"), true, roleToExclude)%>
             							 </select>
										<select name="filter" id="filter">
											<option value=""></option>
											<option value="1">Avant req. de poursuite</option>
											<option value="2">Dès req. de poursuite</option>
											<option value="3">Sans sursis au paiement</option>
											<option value="4">Avec sursis au paiement</option>
											<option value="5">Exclusion manuelle</option>
											
										</select>
										
										<input type="button" id="btSearch" value="chercher"
											onclick="getCompteALettrer('<%=request.getContextPath()%>/osiris?userAction=osiris.lettrage.main.query&from='+document.getElementById('from').value+'&to='+document.getElementById('to').value+'&role='+document.getElementById('role').value+'&filter='+document.getElementById('filter').value)">
									
									</div>
									<div style="display:none" id="dialog" title="Section">
										 
										 <h3><span id="dialogSection"></span></h3>
										 Mode de compensation :<br>
										 <%String selectBlock = globaz.osiris.parser.CASelectBlockParser.getForIdModeCompensationSelectBlock(objSession, "");
						              		if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectBlock)) {
						              			out.print(selectBlock); 
						              		}
						              	%>
						              	<script>document.getElementById("idModeCompensation").size=7</script>
						              	<br><br>
						              	Remarques : <br>
						                <textarea name="texteRemarque" cols="65" rows="3" class="input"></textarea>
										<span style="color:white" id="dialogIds"></span>
									</div>
									
								</td>
							</tr>
							<tr style="background:white" height="100%">
								<td id="tdList" valign="top">
									<div id="list" style="width: 150px; max-width: 150px; background-color: white;"></div>
								</td>
								<td valign="top">
									<div id="status" style="width: 850px; max-width: 850px; background-color: white;"></div>
									<div id="work" style="background:white"></div>
									<div id="message" style="background:white"></div>
									<div id="infoSection" style="background:white"></div>
								</td>
							</tr>
							
						</table>	
						</td></tr>				
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage"  --%>

<script>
	
	document.getElementById('innerTable').style.setExpression("height","document.body.clientHeight-160");
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>