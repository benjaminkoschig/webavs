<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<HTML>
<!--# set echo="url" -->
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
<HEAD>
<META name="GENERATOR"
	content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<TITLE></TITLE>
<%

    globaz.pavo.db.compte.CIEcritureListViewBean viewBean = (globaz.pavo.db.compte.CIEcritureListViewBean)session.getAttribute("viewBeanEcrit");
    globaz.pavo.db.inscriptions.CIJournalViewBean viewBeanJournal = (globaz.pavo.db.inscriptions.CIJournalViewBean)session.getAttribute("viewBean");
    globaz.pavo.util.CIUtil util = new globaz.pavo.util.CIUtil();
    java.util.ArrayList ChampsCaches = util.getChampsAffiche(session);
    java.util.ArrayList colonnesAmasquer = util.getColonnesAMasquer(session);
    //viewBean.init(request.getParameter("selectedId"));
    int size = viewBean.getSize();
    String servletContext = request.getContextPath();
    String mainServletPath = (String)request.getAttribute("mainServletPath");
    String detailLink = servletContext + mainServletPath+"?userAction=pavo.compte.ecriture.afficher&id=";
    String languePage = globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session);
    java.util.HashSet typeCompteSaisie = new java.util.HashSet();
    typeCompteSaisie.add(globaz.pavo.db.compte.CIEcriture.CS_CORRECTION);
    typeCompteSaisie.add(globaz.pavo.db.compte.CIEcriture.CS_CI_SUSPENS_SUPPRIMES);
    typeCompteSaisie.add(globaz.pavo.db.compte.CIEcriture.CS_GENRE_6);
    typeCompteSaisie.add(globaz.pavo.db.compte.CIEcriture.CS_GENRE_7);
    typeCompteSaisie.add(globaz.pavo.db.compte.CIEcriture.CS_CI_SUSPENS);
    typeCompteSaisie.add(globaz.pavo.db.compte.CIEcriture.CS_TEMPORAIRE_SUSPENS);
	
	//savoir si les champs sont à cacher
	boolean BTA = true;
	boolean BrEc = true;
	boolean Special = true;
	boolean CAC = true;
	
	boolean codeAmasquerDec = true;
	boolean affilieAMasquer = true;
	boolean amiJournal = false;
	boolean apgJournal = false;
	boolean ijJournal = false;
	boolean cotPersJournal = false;
	boolean acJournal = false;
	boolean declJournal = false;
	boolean splitJournal = false;
	String visiEmp = "visible";
 	String visiConj = "hiddenPage";
	if(globaz.pavo.db.inscriptions.CIJournal.CS_ASSURANCE_MILITAIRE.equals(viewBeanJournal.getIdTypeInscription())){
		amiJournal = true;
	}
	if(globaz.pavo.db.inscriptions.CIJournal.CS_APG.equals(viewBeanJournal.getIdTypeInscription())){
		apgJournal = true;
	}
	if(globaz.pavo.db.inscriptions.CIJournal.CS_IJAI.equals(viewBeanJournal.getIdTypeInscription())){
		ijJournal = true;
	}
	if(globaz.pavo.db.inscriptions.CIJournal.CS_DECISION_COT_PERS.equals(viewBeanJournal.getIdTypeInscription())
		|| globaz.pavo.db.inscriptions.CIJournal.CS_COTISATIONS_PERSONNELLES.equals(viewBeanJournal.getIdTypeInscription())){
		cotPersJournal = true;
	}
	if(globaz.pavo.db.inscriptions.CIJournal.CS_ASSURANCE_CHOMAGE.equals(viewBeanJournal.getIdTypeInscription())){
		acJournal = true;
	}
	if(globaz.pavo.db.inscriptions.CIJournal.CS_DECLARATION_SALAIRES.equals(viewBeanJournal.getIdTypeInscription()) ||
		globaz.pavo.db.inscriptions.CIJournal.CS_DECLARATION_COMPLEMENTAIRE.equals(viewBeanJournal.getIdTypeInscription())){
			declJournal = true;
	}
	if(globaz.pavo.db.inscriptions.CIJournal.CS_SPLITTING.equals(viewBeanJournal.getIdTypeInscription())){
		splitJournal = true;
		visiEmp="hiddenPage";
		visiConj="visible";
	}
	
	
	    
    String jspLocation = servletContext + mainServletPath + "Root/ci_select.jsp";
	String jspLocation2 = servletContext + mainServletPath + "Root/ti_select.jsp";
	
	String partner="C";
 	String mitglied = "A";
 	if("DE".equals(languePage)) {
 		partner="P";
 		mitglied="M";
 	}
 	//controle si champs sont dans le fichier de properties
 	if(ChampsCaches.indexOf("BTA") >= 0){
 		BTA = false;
 	}
 	
 	if(ChampsCaches.indexOf("CAC") >= 0 && !globaz.pavo.db.inscriptions.CIJournal.CS_ASSURANCE_CHOMAGE.equals(viewBeanJournal.getIdTypeInscription())){
 		CAC = false;
 	}
 	if(ChampsCaches.indexOf("BEC") >= 0){
 		BrEc = false;
 	}
 	
 	if(ChampsCaches.indexOf("Special") >= 0){
 		Special = false;
 	}
 	//A masquer pour les déclarations de salaire
 	
 	if(colonnesAmasquer.indexOf("codeAMasquer")>=0
 	&& globaz.pavo.db.inscriptions.CIJournal.CS_DECLARATION_SALAIRES.equals(viewBeanJournal.getIdTypeInscription())){
 		codeAmasquerDec = false;
 	}
 	if(colonnesAmasquer.indexOf("codeAMasquer")>=0
 	&& globaz.pavo.db.inscriptions.CIJournal.CS_DECLARATION_COMPLEMENTAIRE.equals(viewBeanJournal.getIdTypeInscription())){
 		codeAmasquerDec = false;
 	}
 	if(colonnesAmasquer.indexOf("affilieAMasquer")>=0
 	&& globaz.pavo.db.inscriptions.CIJournal.CS_DECLARATION_SALAIRES.equals(viewBeanJournal.getIdTypeInscription())){
 		affilieAMasquer = false;
 	}
 	if(colonnesAmasquer.indexOf("affilieAMasquer")>=0
 	&& globaz.pavo.db.inscriptions.CIJournal.CS_DECLARATION_COMPLEMENTAIRE.equals(viewBeanJournal.getIdTypeInscription())){
 		affilieAMasquer = false;
 	}
 	
 	int autoDigitAff = globaz.pavo.util.CIUtil.getAutoDigitAff(session);
 	int tailleChampsAff = globaz.pavo.util.CIUtil.getTailleChampsAffilie(session);
 	
 	boolean wantPagination = true;
	boolean wantPaginationPosition = false;
	String baseLink = mainServletPath+"?userAction="+request.getParameter("userAction");
	baseLink = baseLink.substring(1,baseLink.lastIndexOf(".")+1);
	String findPreviousLink = baseLink+"precedantPerso";
	String findNextLink = baseLink+"suivantPerso";
 	
 	
	
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/params.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/autocomplete.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/selectionPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/avsParser.js"></SCRIPT>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<SCRIPT language="JavaScript">
var errorObj = new Object();
errorObj.text = "";

function showErrors() {
	if (errorObj.text != "") {
		showModalDialog('<%=servletContext%>/errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
	}
}

function setSl(id) {
    var destination = getWebAppName() + "master.jsp?_type=" + getParam("_typeSrc");

    destination += "&" + getParam("_att")+ "=" + id;
    destination += "&_back=sl";

    destination += "&_section=" + getParam("_sectionSrc");
    destination += "&_dest=" + getParam("_destSrc");
    destination += "&_method=" + getParam("_method");

    destination += "&_caractId=" + getParam("_caractId");

    top.location.href = destination;
}

function popUp(section, page, id) {
	var pageName = "/";
    if(section != "") {
        pageName += section + "/";
    }
    pageName += page + "_cMenu.jsp?id=" + id;
    var newWin = window.open(pageName, '_blank', 'top=50, left=50, width=250,height=200, scrollbars=no, menubars=no, resizable=no, titlebar=no,location=no, status=no');
}

function finds(styleName) {
  for (i = 0; i < document.styleSheets.length; i++) {
    for (j = 0; j < document.styleSheets(i).rules.length; j++) {
      if (document.styleSheets(i).rules(j).selectorText == styleName) {
        return document.styleSheets(i).rules(j);
      }
    }
  }
}

var popupSection = "";
var popupPage = "";

function enableNew(){
	parent.enableBtn();
}
function selectPartner(line,ecr) {
	line.all('partnermitglieder'+ecr).innerHTML = "<%=partner%>";
	line.all('partnerNum'+ecr).className = 'visible';
	line.all('mitgliedNum'+ecr).className = 'hiddenPage';	
}
function selectMitglieder(line,ecr) {
	line.all('partnermitglieder'+ecr).innerHTML = "<%=mitglied%>";
	line.all('partnerNum'+ecr).className = 'hiddenPage';
	line.all('mitgliedNum'+ecr).className = 'visible';	
}
function switchPartnerMitglieder(ecr) {
	if (document.forms(0).all(ecr).all('partnermitglieder'+ecr).innerHTML == "<%=partner%>")
		selectMitglieder(document.forms(0).all(ecr),ecr);
	else
		selectPartner(document.forms(0).all(ecr),ecr);
	resetEmployeurPartenaire(ecr);
}

function updateEmployeurPartenaire(tag,ecr) {
	if (tag.select && tag.select.selectedIndex !=-1) {
		document.forms(0).all(ecr).all('employeurPartenaire').value = tag.select[tag.select.selectedIndex].valueEmp;
		document.forms(0).all(ecr).all('brancheEconomique').value = tag.select[tag.select.selectedIndex].brancheeco;
		//if (tag.select[tag.select.selectedIndex].brancheeco)
		//	for (var i=0 ; i<document.forms(0).all(ecr).all('brancheEconomique').options.length ; i++)
		//		if (document.forms(0).all(ecr).all('brancheEconomique').options[i].value == tag.select[tag.select.selectedIndex].brancheeco)
		//			document.forms(0).all(ecr).all('brancheEconomique').options[i].selected = true;
	}
}

function checkNSSCaPage(valeur) {
	 //    					alert(document.getElementById("avsNewUpNNSS").value==true);
	 try{
     if(valeur=='true'){
     	nssCheckChar('43',"avsNewUp");
     }else{
	 	nssCheckChar('45',"avsNewUp");
     }
     
     }catch(e){
	     if(valeur=='true'){
	     	nssCheckChar('43',"avsNew");
	     }else{
		 	nssCheckChar('45',"avsNew");
	     }
     }

}

function resetEmployeurPartenaire(ecr) {
	document.forms(0).all(ecr).all('partnerNum'+ecr).value='';
	document.forms(0).all(ecr).all('mitgliedNum'+ecr).value='';
	document.forms(0).all(ecr).all('employeurPartenaire').value='';
	document.forms(0).all(ecr).all('brancheEconomique').value='';
	//for (var i=0 ; i<document.forms(0).all(ecr).all('brancheEconomique').options.length ; i++)
	//	if (document.forms(0).all(ecr).all('brancheEconomique').options[i].value == '')
	//		document.forms(0).all(ecr).all('brancheEconomique').options[i].selected = true;
}
function avsAction(tagName) {
	var numAVS = document.forms[0].elements(tagName).value;
	var avsp = new AvsParser(numAVS);

	numAVS = trim(numAVS);

    if (!avsp.isWellFormed()) {
    	while(numAVS.indexOf(".")!=-1){
	    	numAVS = numAVS.replace(".","");
		}
		// taille
		/*
		while(numAVS.length<11){
			numAVS += "0";
		}
		*/
		if(numAVS.length != 0){	
			if(numAVS.length > 8)
				numAVS = numAVS.substring(0,3)+"."+numAVS.substring(3,5)+"."+numAVS.substring(5,8)+"."+numAVS.substring(8,11);
			else
				numAVS = numAVS.substring(0,3)+"."+numAVS.substring(3,5)+"."+numAVS.substring(5,8);
		}
		document.forms[0].elements(tagName).value = numAVS;
    }
}
function formatAVS(tagName){
	// on ne formate pas le numéro avs quand on presse la touche delete ou backspace
	if(window.event.keyCode==8||window.event.keyCode==46)
		return;
	var numAVS = document.forms[0].elements(tagName).value;

	numAVS = trim(numAVS);

    while(numAVS.indexOf(".")!=-1){
	    numAVS = numAVS.replace(".","");
	}
	var res = numAVS.substring(0,3);
	if(numAVS.length > 3)
		res = res +"."+numAVS.substring(3,5);
	if(numAVS.length > 5)
		res = res +"."+numAVS.substring(5,8);
	if(numAVS.length > 8)
		res = res +"."+numAVS.substring(8,11);
	
	document.forms[0].elements(tagName).value = res;
}
function trim(valueToTrim)
{
  var lre = /^\s*/;
  var rre = /\s*$/;
  
  valueToTrim = valueToTrim.replace(lre, "");
  valueToTrim = valueToTrim.replace(rre, "");
  // tester si le numéro avs entré comporte slt des numéros et/ou des .
  var cre = /((\d|\.)+)/;
  if (!cre.test(valueToTrim)) {
	valueToTrim = "";
  }
  return valueToTrim;
}


</SCRIPT>

<style type="text/css">
.visible {
	visibility: hidden;
	display: none;
}

.hiddenPage {
	visibility: visible;
	display: inline;
}

.selected {
	cursor: hand;
	font-weight: bold;
	visibility: visible;
	display: inline;
}

.notselected {
	cursor: default;
	font-weight: normal;
	visibility: hidden;
	display: none;
}
</style>

</HEAD>
<BODY onLoad="updateText()" style="margin-left: 0px; margin-right: 0px"
	bgcolor="#F0F0F0" onKeyDown="parent.fr_detail.sendEvent(window.event)">



<ct:FWOptionSelectorDHTMLTag label="Options" menu="" target="parent"
	detailLabel="Detail" detailLink="<%=detailLink+\"-id-\"%>" />


<DOWNLOAD ID=dwn STYLE="behavior:url(#default#download)" />

<SCRIPT>

function forceCaracterNum (){

	document.forms(0).all("newEcriture").all("mitgliedNumnewEcriture").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
	
}

function updateText() {
// Parcours de la table pour mettre à jour les noms
}
function onDone(src){
    oPopup.document.write(src);
}
function showPopup(baseObject,id){
	oPopup = window.createPopup();
    document.forms[0].selectedId.value = id;
    dwn.startDownload("/" + popupSection + "/" + popupPage + "_cMenu.jsp",onDone);
    oPopup.show(event.clientX + document.body.scrollLeft, event.clientY + document.body.scrollTop, 175, 260, baseObject);
    //alert("hello");
}



var oPopup = window.createPopup();

document.write("<div style=\"position:absolute;border=1px solid black;background-color:#B3C4DB;font-size : 9;visibility:hidden\" name=\"toolTips\" id=\"toolTipsDiv\"></div>\n");
var toolTips = new PopupWindow("toolTipsDiv");

function showToolTips(tag) {
		if (tag.select) {
		toolTips.offsetX = tag.select.style.pixelWidth + 10;
		toolTips.offsetY = 0;
		toolTips.showPopup(tag.div.name);
		toolTips.populate(tag.select[tag.select.selectedIndex].ecritures);
		toolTips.refresh();
	}
}

function showToolTips2(tag) {
	if (tag.select) {
		toolTips.offsetY = tag.input.offsetHeight;
		toolTips.offsetX = 0;
		toolTips.showPopup(tag.input.name);
		toolTips.populate(tag.select[tag.select.selectedIndex].ecritures);
		toolTips.refresh();
	}
}


function hideToolTips(tag) {
	toolTips.hidePopup();
}

function resetSelection() {
// Annule les procédures de mise à jour et/ou d'ajout
// Désélectionne toutes les lignes quand une d'entre est sélectionnée.
// fr_detail se charge de re-sélectionnée la bonne quand ses détails sont affichés
//alert("ecr"+parent.fr_detail.document.forms[0].all("selectedId").value);
	document.forms[0].all("ecr"+parent.fr_detail.document.forms[0].all("selectedId").value).style.fontWeight = "normal";
	document.forms[0].all("ecr"+parent.fr_detail.document.forms[0].all("selectedId").value).style.display = "block";
	document.forms[0].all("ecrUp"+parent.fr_detail.document.forms[0].all("selectedId").value).style.display = "none";
// On n'affiche plus la ligne de nouvelle écriture
	document.forms[0].all("newEcriture").style.display = "none";
// On affiche le bouton nouveau du frame principal
	//parent.document.images("btnNew").style.display = "inline";
}

function routage(ecrId) {
	parent.fr_detail.location.href='<%=servletContext + mainServletPath%>?userAction=pavo.compte.ecriture.afficherSurPage&selectedId='+ecrId;
}

function showtip(current, e, text) {
	if (document.all||document.getElementById) {
		thetitle = text.split('<br>');
		if (thetitle.length>1) {
			thetitles='';
			for (i=0; i<thetitle.length; i++) 
				thetitles += thetitle[i];
			current.title=thetitles
			} else
			current.title = text;
		} else if (document.layers) {
				document.tooltip.document.write('<layer bgcolor="white" style="border:1px solid black;font-size:12px;">'+
				text+'</layer>');
				document.tooltip.document.close();
				document.tooltip.left = e.pageX + 5;
				document.tooltip.top = e.pageY + 5;
				document.tooltip.visibility = "show";
			}	
}
function handleAmount(value) {
	if(value.length!=0) {
		if(value.charAt(value.length-2)=='.') {
			value+='0';
		}else if(value.charAt(value.length-1)=='.') {
			value+='00';
		} else if(value.charAt(value.length-3)!='.' && value.indexOf('.')==-1) {
			value+='.00';
		}
	} else {
		value= '0.00';
	}
	if(value.charAt(value.length-3)=='.' && value.length>6 && value.charAt(value.length-7)!="'") {
		value = value.substring(0,value.length-6)+"'"+value.substring(value.length-6,value.length);
	}
	if(value.charAt(value.length-3)=='.' && value.length>10 && value.charAt(value.length-11)!="'") {
		value = value.substring(0,value.length-10)+"'"+value.substring(value.length-10,value.length);
	}
	return value;
}
function hidetip() {
	if (document.layers)
		document.tooltip.visibility = "hidden";
}

// Récupère la chaîne à afficher en tooltip pour le champs employeur
function getEmployeurPartenaireTooltip(source) {
}
function updateForm(tag){
	if (tag.select && tag.select.selectedIndex!=-1) {
		parent.fr_detail.document.forms[0].elements('nomPrenom').value = tag.select[tag.select.selectedIndex].nom;
		//tag.input.parentElement.parentElement.title = tag.select[tag.select.selectedIndex].ecritures;
		//showToolTips2(tag);
		parent.fr_detail.document.forms[0].elements('nomPrenom').disabled = true;
		parent.fr_detail.document.forms[0].elements('dateDeNaissance').value = tag.select[tag.select.selectedIndex].date;
		parent.fr_detail.document.forms[0].elements('dateDeNaissance').disabled = true;
		parent.fr_detail.document.forms[0].elements('sexe').value = tag.select[tag.select.selectedIndex].sexe;
		parent.fr_detail.document.forms[0].elements('sexe').disabled = true;
		parent.fr_detail.document.forms[0].elements('paysOrigine').value = tag.select[tag.select.selectedIndex].paysCode;
		parent.fr_detail.document.forms[0].elements('paysOrigine').disabled = true;
		parent.fr_detail.document.forms[0].elements('paysOrigineLabel').value = tag.select[tag.select.selectedIndex].paysLibelle;
		parent.fr_detail.document.forms[0].elements('dateNaissanceNNSS').value = tag.select[tag.select.selectedIndex].date;
		parent.fr_detail.document.forms[0].elements('sexeNNSS').value = tag.select[tag.select.selectedIndex].sexeFormate;
		parent.fr_detail.document.forms[0].elements('paysNNSS').value = tag.select[tag.select.selectedIndex].paysFormate;
		//alert(tag.select[tag.select.selectedIndex].ecritures);
		//parent.fr_detail.document.forms[0].elements('dernieresEcritures').value =  tag.select[tag.select.selectedIndex].ecrituresText;		
		//parent.fr_detail.document.getElementById('dernieresEcritures').innerHTML = tag.select[tag.select.selectedIndex].ecritures;
		parent.fr_detail.dernieresEcritures.location.href='<%=request.getContextPath()%>/pavoRoot/lastentries.jsp?compteIndividuelId='+tag.select[tag.select.selectedIndex].idci;

	} 
}

function resetNom(){

	parent.fr_page.document.forms[0].elements('nomPrenom').value = '';
	parent.fr_page.document.forms[0].elements('nomPrenom').disabled = false;

	

		
}

function resetAssure() {
	//alert("resetAssure");
	parent.fr_detail.document.forms[0].elements('nomPrenom').value = '';
	parent.fr_detail.document.forms[0].elements('nomPrenom').disabled= false;
	parent.fr_detail.document.forms[0].elements('dateDeNaissance').value = '';
	parent.fr_detail.document.forms[0].elements('dateDeNaissance').disabled = false;
	parent.fr_detail.document.forms[0].elements('sexe').value = '';
	parent.fr_detail.document.forms[0].elements('sexe').disabled = false;
	parent.fr_detail.document.forms[0].elements('paysOrigine').value = '';
	parent.fr_detail.document.forms[0].elements('paysOrigine').disabled = false;
	parent.fr_detail.document.forms[0].elements('paysOrigineLabel').value = '';
	parent.fr_detail.document.forms[0].elements('dateNaissanceNNSS').value = "";
	parent.fr_detail.document.forms[0].elements('sexeNNSS').value = "";
	parent.fr_detail.document.forms[0].elements('paysNNSS').value = "";
	//parent.fr_detail.document.forms[0].elements('dernieresEcritures').value = '';
	//parent.fr_detail.document.getElementById('dernieresEcritures').innerHTML = '';
	parent.fr_detail.dernieresEcritures.location.href='<%=request.getContextPath()%>/pavoRoot/lastentries.jsp';
}

function forceDeuxCaractèresMois(tag)
{
	if(tag.value==1)	
	{
		tag.value="0"+tag.value;
	}
}
function rechercheNew(){
//	parent.fr_detail.getElementById('brnCtrl').click();
	//parent.fr_main.location.href = "<%=(servletContext + mainServletPath)%>?userAction=pavo.compte.afficherSurPage&selectedId="	
}


</SCRIPT>
<div id="tooltip" style="position: absolute; visibility: hidden"></div>
<FORM>
<TABLE id="ecrList" name="ecrList" width="100%" border="0"
	cellspacing="0" cellpadding="0">
	<TBODY id="ecrListBody">
		<TR align="center">

			<Th width="2%" colspan="2" align="left">&nbsp;IK</Th>
			<Th width="2%" align="left">SVN</Th>
			<Th width="12%" align="left">Betrag</Th>
			<Th width="5%" align="left">SZ</Th>
			<Th width="4%" align="left">Beginn</Th>
			<Th width="4%" align="left">Ende</Th>
			<Th width="6%" align="left">Beitragsjahr</Th>
			<%if(affilieAMasquer){%>
				<Th width="12%" align="left">Abr.-Nr.<br>
			/ SVN</Th>
			<%}%>
			<%if (BTA){%>
			<Th width="8%" align="left">BGS</Th>
			<%}%>
			<%if (Special){%>
			<Th width="4%" align="left">SFC</Th>
			<%}%>
			<%if (BrEc){%>
			<Th width="4%" align="left">ErZw</Th>
			<%}%>
			<%if (CAC){%>
			<Th width="6%" align="left">ALV</Th>
			<%}%>
			<%if(codeAmasquerDec){%>
			<Th width="3%" align="left">Code</Th>
			<%}%>
			<Th width="18%" align="left">Buchungsart</Th>
		</TR>


		<%    String rowStyle = "";
         String actionDetail ="";
    for (int i=0; i < size ; i++) { 
        if (i % 2 == 0)
            rowStyle = "row";
        else 
            rowStyle = "rowOdd"; 
        actionDetail = "parent.fr_detail.location.href='"+servletContext + mainServletPath+"?userAction=pavo.compte.ecriture.afficherSurPage&selectedId="+viewBean.getEcritureId(i)+"';";


%>
		<% 
globaz.pavo.db.compte.CIEcriture line = (globaz.pavo.db.compte.CIEcriture)viewBean.getEntity(i);
%>
		<% //if(!line.hasShowRight(null)) { %>
		<script>//errorObj.text="<%=line.getSession().getLabel("MSG_JOURNAL_NO_AUTH")%>"</script>
		
		<%//	continue;
 //} %>
		<TR id="<%="ecr"+line.getEcritureId()%>"
			name="<%=line.getEcritureId()%>" class="<%=rowStyle%>"
			onMouseOver="this.style.background=finds('.rowHighligthed').style.background;this.style.color=finds('.rowHighligthed').style.color;"
			onMouseOut=" this.style.background=finds('.<%=rowStyle%>').style.background;this.style.color=finds('.<%=rowStyle%>').style.color;"
			onclick="enableNew();">


			<TD class="mtd" width=""><!--<IMG id="ecritureNonValid" style="display:none" name="suspens" src="<%=request.getContextPath()%>/images/verrou.gif" border="0">-->
			</TD>
			<TD class="mtd" width=""><%	if (globaz.pavo.db.compte.CIEcriture.CS_CI_SUSPENS.equals(line.getIdTypeCompte()) || globaz.pavo.db.compte.CIEcriture.CS_TEMPORAIRE_SUSPENS.equals(line.getIdTypeCompte())) { %>
			<IMG name="suspensOrNot"
				src="<%=request.getContextPath()%>/images/erreur.gif" border="0"> <%	} else { %>
			<IMG name="suspensOrNot"
				src="<%=request.getContextPath()%>/images/ok.gif" border="0"> <%	} %>
			</TD>
			<TD class="mtd" id="avs"
				onClick="javascript:resetSelection();<%=actionDetail%>" width=""><%=line.getNssFormate()%>&nbsp;
				<input type="hidden" id="partialavs" value="<%=line.getPartialNss()%>"/>
				<input type="hidden" id="avsNNSS" value = "<%=line.getNumeroavsNNSS()%>"/>
				</TD>
				
				
			<TD class="mtd" id="montant" align="right"
				onClick="javascript:resetSelection();<%=actionDetail%>" width=""><%=line.getMontantFormat()%>&nbsp;</TD>
			<TD class="mtd" id="gre"
				onClick="javascript:resetSelection();<%=actionDetail%>" width=""><%=line.getGreFormat()%>&nbsp;</TD>
			<TD class="mtd" id="moisDebut"
				onClick="javascript:resetSelection();<%=actionDetail%>" width="4%"><%=line.getMoisDebutPad()%>&nbsp;</TD>
			<TD class="mtd" id="moisFin"
				onClick="javascript:resetSelection();<%=actionDetail%>" width="4%"><%=line.getMoisFinPad()%>&nbsp;</TD>
			<TD class="mtd" id="annee"
				onClick="javascript:resetSelection();<%=actionDetail%>" width=""><%=line.getAnnee()%>&nbsp;</TD>
			

			<%if(affilieAMasquer){%>
			<TD class="mtd" title="<%=line.getInfoEmployeurPartenaire()%>"
				id="employeurPartenaire"
				onClick="javascript:resetSelection();<%=actionDetail%>" width=""><%=line.getEmployeurPartenaireForDisplay()%>&nbsp;</TD>
			<%}else{%>
			<TD class="mtd" title="<%=line.getInfoEmployeurPartenaire()%>"
				id="employeurPartenaire" style="display: none"
				onClick="javascript:resetSelection();<%=actionDetail%>" width=""></td>
			
			<%}%>
			<%if (BTA){%>
			<TD class="mtd" id="partBtaAff"
				onClick="javascript:resetSelection();<%=actionDetail%>" width=""><%=line.getPartBtaPad()%>&nbsp;</TD>
			<%}%>
			<%if (Special){%>
			<TD class="mtd" id="codeSpecialAff"
				onClick="javascript:resetSelection();<%=actionDetail%>" width=""><%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(line.getCodeSpecial(), session)%>&nbsp;</TD>
			<%}%>
			<%if (BrEc){%>
			<TD class="mtd" id="brancheEconomiqueAff"
				onClick="javascript:resetSelection();<%=actionDetail%>" width=""><%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(line.getBrancheEconomique(), session)%>&nbsp;</TD>
			<%} else{%>

			<INPUT type="hidden" class="mtd" id="brancheEconomiqueAff"
				onClick="javascript:resetSelection();<%=actionDetail%>" width=""
				value="<%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(line.getBrancheEconomique(), session)%>&nbsp;" />
			<%}%>
			<%if (CAC){%>
			<TD class="mtd" id="caisseChomage"
				onClick="javascript:resetSelection();<%=actionDetail%>" width=""><%=line.getCaisseChomageStr()%>&nbsp;</TD>
			<%}%>
			<%if(codeAmasquerDec){%>
			<TD class="mtd" id="codeAff"
				onClick="javascript:resetSelection();<%=actionDetail%>" width=""><%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(line.getCode(), session)%>&nbsp;</TD>
			<%}%>
			
			<TD class="mtd" id="typeCompte"
				onClick="javascript:resetSelection();<%=actionDetail%>" width=""><%=globaz.pavo.translation.CodeSystem.getLibelle(line.getIdTypeCompte(), session)%>&nbsp;</TD>
			<TD id="partBta" style="display: none"><%=line.getPartBta()%></TD>
			<TD id="codeSpecial" style="display: none"><%=line.getCodeSpecial()%></TD>
			<TD id="brancheEconomique" style="display: none"><%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(line.getBrancheEconomique(),session)%></TD>
			<TD id="code" style="display: none"><%=line.getCode()%></TD>
			<TD id="idTypeCompte" style="display: none"><%=line.getIdTypeCompte()%></TD>
			
		</TR>
		<TR id="<%="ecrUp"+line.getEcritureId()%>" class="<%=rowStyle%>"
			style="display: none">
			<TD class="mtd" width=""><!--<IMG id="ecritureNonValid" name="suspens" src="<%=request.getContextPath()%>/images/verrou.gif" border="0">-->
			</TD>
			<% String nameInput = "avs" + line.getEcritureId(); 
			String nameInputNNSS = "avs"+ line.getEcritureId()+"NNSS";
				String nameInputPartial ="partialavs"+line.getEcritureId();
				   String nameHidden = "document.forms[0].all('ecrUp"+line.getEcritureId()+"').all('avs').value=tag.input.value"; 
				   String valueOnChange = "updateForm(tag);"; 
				  String valueOnfailure = "resetAssure();";
			%>
			<TD class="mtd" width=""><img
				src="<%=request.getContextPath()%>/images/arrows.gif"></TD>
			<!--resetAssure();-->
			<TD class="mtd" width="" nowrap><nss:nssPopup name="<%=nameInput%>"
				cssclass="libelle" newnss="<%=line.getNumeroavsNNSS()%>" jspName="<%=jspLocation%>" avsAutoNbrDigit="11"
				avsMinNbrDigit="8" nssAutoNbrDigit="10" nssMinNbrDigit="7"
				onChange="<%=valueOnChange%>" onFailure="<%=valueOnfailure%>" />&nbsp; <script>
     					
     					partial<%=nameInput%>PopupTag.onUpdate = showToolTips;
     					partial<%=nameInput%>PopupTag.onStop   = hideToolTips;
     					if(document.getElementById("<%=nameInputNNSS%>").value=='true'){
     						nssCheckChar('43',"<%=nameInput%>");
     					}else{
	     					nssCheckChar('45',"<%=nameInput%>");
     					}
     			</script>
     			<input type ="hidden" id="partialavs">	
     			<input type ="hidden" id="avsNNSS">
     				</TD>
			<TD class="mtd" onchange="validateFloatNumber(this);"
				onkeypress="return filterCharForFloat(window.event);" align="right"
				width=""><INPUT size="15" maxLength="15" type="text" id="montant"
				onBlur="this.value=handleAmount(this.value)">&nbsp;</TD>
			<TD class="mtd" width=""><INPUT
				onkeypress="return filterCharForPositivInteger(window.event);"
				size="2" maxLength="3" type="text" id="gre">&nbsp;</TD>
			<TD class="mtd" width="4%"><INPUT
				onkeypress="return filterCharForPositivInteger(window.event);"
				size="1" maxLength="2" type="text" id="moisDebut">&nbsp;</TD>
			<TD class="mtd" width="4%"><INPUT
				onkeypress="return filterCharForPositivInteger(window.event);"
				size="1" maxLength="2" type="text" id="moisFin">&nbsp;</TD>
			<TD class="mtd" width=""><INPUT
				<%if( globaz.pavo.db.inscriptions.CIJournal.CS_CONTROLE_EMPLOYEUR.equals(viewBeanJournal.getIdTypeInscription()) 
			 	|| "301001".equals(viewBeanJournal.getIdTypeInscription()) || "301002".equals(viewBeanJournal.getIdTypeInscription())){%>
				tabindex='-1'
				readonly
				<%}%>
				onkeypress="return filterCharForPositivInteger(window.event);"
				size="4" maxLength="4" type="text" id="annee">&nbsp;</TD>
			
				
			<%if(globaz.pavo.db.inscriptions.CIJournal.CS_CONTROLE_EMPLOYEUR.equals(viewBeanJournal.getIdTypeInscription()) 
			|| globaz.pavo.db.inscriptions.CIJournal.CS_CORRECTIF.equals(viewBeanJournal.getIdTypeInscription())
			 || apgJournal || amiJournal || ijJournal || acJournal )
			 {%>
				<%
     				String namePopup1 = "mitgliedNumecrUp"+line.getEcritureId();
     				String namePopup2 = "partnerNumecrUp"+line.getEcritureId();
     				%>
     				<TD class="mtd" width="" nowrap><span 
				id="partnermitglieder<%="ecrUp"+line.getEcritureId()%>">
				
				<%=mitglied%>
				</span>
				
				<INPUT type="text" name="<%=namePopup1%>" value="" tabindex='-1' className="hiddenPage" readonly>
				<INPUT type="text" size="<%=apgJournal || amiJournal||acJournal || ijJournal?14:tailleChampsAff%>" name="<%=namePopup2%>" value="" tabindex='-1' className="visible" readonly>
				<INPUT type="hidden" name="employeurPartenaire" value="">
					</td>		
			<%}else if(!affilieAMasquer){%>
			<%
     				String namePopup1 = "mitgliedNumecrUp"+line.getEcritureId();
     				String namePopup2 = "partnerNumecrUp"+line.getEcritureId();
     				%>
     				<span style="display : none"
				id="partnermitglieder<%="ecrUp"+line.getEcritureId()%>"
				class="selected"
				
					onclick="switchPartnerMitglieder('<%="ecrUp"+line.getEcritureId()%>');"></span>
     			
				<INPUT type="hidden" name="<%=namePopup1%>" value="" tabindex='-1'  readonly>
				<INPUT type="hidden" size="<%=tailleChampsAff%>" name="<%=namePopup2%>" value="" tabindex='-1'  readonly>
				<INPUT type="hidden" name="employeurPartenaire" value="">
			
			
			
			<%}else{%>
			<TD class="mtd" width="" nowrap><span style="display: none"
				id="partnermitglieder<%="ecrUp"+line.getEcritureId()%>"
				<%if(!cotPersJournal || splitJournal){%>
				class="selected"
				onclick="switchPartnerMitglieder('<%="ecrUp"+line.getEcritureId()%>');"<%}%>><%=mitglied%></span>
			
				
				<input type="hidden" name="employeurPartenaire"> <%
     				String onChangePopup = "updateEmployeurPartenaire(tag,'ecrUp"+line.getEcritureId()+"');";
     				String onFailurePopup = "resetEmployeurPartenaire('ecrUp"+line.getEcritureId()+"');";
     				String namePopup1 = "mitgliedNumecrUp"+line.getEcritureId();
     				String namePopup2 = "partnerNumecrUp"+line.getEcritureId();
     				String cssClassValue = !splitJournal?"visible":"";
     				%> 
     				
     				<nss:nssPopup name="<%=namePopup1%>"  
     				
     				cssclass="<%=cssClassValue%>"
					jspName="<%=jspLocation%>" avsMinNbrDigit="8" avsAutoNbrDigit="11" nssMinNbrDigit="7"
					nssAutoNbrDigit="10" validateOnChange="true" onFailure="<%=onFailurePopup%>" onChange="<%=onChangePopup%>"/>  
				<ct:FWPopupList
				validateOnChange="true" value="" onChange="<%=onChangePopup%>"
				onFailure="<%=onFailurePopup%>" name="<%=namePopup2%>"  size="<%=tailleChampsAff%>"
				className="hiddenPage" jspName="<%=jspLocation2%>" minNbrDigit="3"
				autoNbrDigit="<%=autoDigitAff%>" />
				</TD>
			<%}%>
				
			<%if (BTA){%>
			<TD class="mtd" width=""><ct:FWCodeSelectTag name="partBta"
				libelle="code" defaut="" codeType="CIPARBTA" wantBlank="true" /><script>document.all("ecrUp<%=line.getEcritureId()%>").all("partBta").style.width="1.2cm"</script><!--INPUT type="text" size="2" maxLength="2" id="partBta"-->&nbsp;</TD>
			<%}else{%>
			<INPUT type="hidden" name="partBta" value="">
			<%}%>
			<%if (Special){%>
			<TD class="mtd" width=""><ct:FWCodeSelectTag name="codeSpecial"
				libelle="code" defaut="" codeType="CICODSPE" wantBlank="true" /><script>document.all("ecrUp<%=line.getEcritureId()%>").all("codeSpecial").style.width="1.2cm"</script><!--INPUT type="text" size="2" maxLength="2" id="codeSpecial"-->&nbsp;</TD>
			<%}else{%>
			<INPUT type="hidden" name="codeSpecial" value="">
			<%}%>
			<%if (BrEc){%>
			<!--<TD class="mtd" width=""><ct:FWCodeSelectTag name="brancheEconomique"
				libelle="code" defaut="" codeType="VEBRANCHEE" wantBlank="true" /><script>document.all("ecrUp<%=line.getEcritureId()%>").all("brancheEconomique").style.width="1.2cm"</script><!--INPUT type="text" size="2" maxLength="2" id="brancheEconomique">&nbsp;</TD>-->
				<TD class="mtd" width="">
				<INPUT type="text" size="2" name="brancheEconomique" value="" readonly tabindex='-1'><script>document.all("ecrUp<%=line.getEcritureId()%>").all("brancheEconomique").style.width="1.2cm"</script>&nbsp;
				</TD>
				
			<%}else{%>
			<INPUT type="hidden" name="brancheEconomique" value="">
			<%}%>
			<%if (CAC){%>
			<TD class="mtd" width=""><INPUT size="5" maxLength="5" type="text"
				id="caisseChomage">&nbsp;</TD>
			<%}else{%>
			<INPUT type="hidden" id="caisseChomage" value="">
			<%}%>
			<%if(codeAmasquerDec){%>
				<TD class="mtd" width="">
				<ct:FWCodeSelectTag name="code"
				libelle="code" defaut="" codeType="CICODAMO" wantBlank="true" /><script>document.all("ecrUp<%=line.getEcritureId()%>").all("code").style.width="1cm"</script>&nbsp;</TD>
			<%}else{%>
				<input type="hidden" name="code" value="">			
			<%} if(line.CS_TEMPORAIRE.equals(line.getIdTypeCompte()) || line.CS_TEMPORAIRE_SUSPENS.equals(line.getIdTypeCompte())) { %>
			<TD class="mtd" width="18%"><!--<ct:FWCodeSelectTag name="idTypeCompte"
				defaut="<%=globaz.pavo.db.compte.CIEcriture.CS_TEMPORAIRE%>"
				except="<%=typeCompteSaisie%>" codeType="CITYPCOM" wantBlank="false" /><script>document.all("ecrUp<%=line.getEcritureId()%>").all("idTypeCompte").style.width="3cm"</script>&nbsp;</TD>-->
			<INPUT type="text" size="10" readonly tabindex='-1' name="typeypeCompte"
				value="<%=globaz.pavo.translation.CodeSystem.getLibelle(line.getIdTypeCompte(), session)%>">
				<INPUT type="hidden" size="10" readonly name="idTypeCompte"
				value="<%=line.getIdTypeCompte()%>"&nbsp;>
			
			</TD>
			<% } else { %>
			<TD class="mtd" width="18%"><!--<ct:FWCodeSelectTag name="idTypeCompte"
				defaut="<%=globaz.pavo.db.compte.CIEcriture.CS_TEMPORAIRE%>"
				codeType="CITYPCOM" wantBlank="false" /><script>document.all("ecrUp<%=line.getEcritureId()%>").all("idTypeCompte").style.width="3cm"</script>&nbsp;</TD>-->
				<INPUT type="text" size="10" readonly tabindex='-1' name="typeCompte"
				value="<%=globaz.pavo.translation.CodeSystem.getLibelle(line.getIdTypeCompte(),session)%>">
				<INPUT type="hidden" size="10" readonly name="idTypeCompte"
				value="<%=line.getIdTypeCompte()%>">
				</TD>
			<% } %>

		</TR>

		<%    } %>
		<TR id="newEcritureValidated" style="display: none" class="row"
			onMouseOver="this.style.background=finds('.rowHighligthed').style.background;this.style.color=finds('.rowHighligthed').style.color;"
			onMouseOut=" this.style.background=finds('.row').style.background;this.style.color=finds('.row').style.color;"
			onclick="enableNew();rechercheNew();">
			<TD class="mtd" width=""><!--<IMG id="ecritureNonValid" name="suspens" <!--src="<%=request.getContextPath()%>/images/verrou.gif" border="0">-->
						<input type="hidden" id ="partialavs">
						<input type="hidden" id = "avsNNSS">
						
			</TD>
			<TD class="mtd" width=""><IMG id="suspensOrNot"
				src="<%=request.getContextPath()%>/images/erreur.gif" border="0"></TD>

			<TD class="mtd" id="avs"
				onClick="javascript:resetSelection();routage(parentNode.id.substring(3));"
				width="">&nbsp;
				
				

				</TD>
			
			<TD class="mtd" id="montant" align="right"
				onClick="javascript:resetSelection();routage(parentNode.id.substring(3));"
				width="">&nbsp;</TD>
			<TD class="mtd" id="gre"
				onClick="javascript:resetSelection();routage(parentNode.id.substring(3));"
				width="">&nbsp;</TD>
			<TD class="mtd" id="moisDebut"
				onClick="javascript:resetSelection();routage(parentNode.id.substring(3));"
				width="4%">&nbsp;</TD>
			<TD class="mtd" id="moisFin"
				onClick="javascript:resetSelection();routage(parentNode.id.substring(3));"
				width="4%">&nbsp;</TD>
			<TD class="mtd" id="annee"
				onClick="javascript:resetSelection();routage(parentNode.id.substring(3));"
				width="">&nbsp;</TD>
			<%if(affilieAMasquer){%>
			<TD class="mtd" id="employeurPartenaire"
				onClick="javascript:resetSelection();routage(parentNode.id.substring(3));"
				width="">&nbsp;</TD>
			<%}else{%>
				<input type="hidden" name="employeurPartenaire">
			<%}%>
			<%if (BTA){%>
			<TD class="mtd" id="partBtaAff"
				onClick="javascript:resetSelection();routage(parentNode.id.substring(3));"
				width="">&nbsp;</TD>
			<%}else{%>
			<INPUT type="hidden" name="partBta" value="">
			<%}%>
			<%if (Special){%>
			<TD class="mtd" id="codeSpecialAff"
				onClick="javascript:resetSelection();routage(parentNode.id.substring(3));"
				width="">&nbsp;</TD>
			<%}else{%>
			<INPUT type="hidden" name="codeSpecial" value="">
			<%}%>
			<%if (BrEc){%>
			<TD class="mtd" id="brancheEconomiqueAff"
				onClick="javascript:resetSelection();routage(parentNode.id.substring(3));"
				width="">&nbsp;</TD>
			<%}else{%>
			<INPUT type="hidden" name="brancheEconomique" value="">
			<%}%>
			<%if (CAC){%>
			<TD class="mtd" id="caisseChomage"
				onClick="javascript:resetSelection();routage(parentNode.id.substring(3));"
				width="">&nbsp;</TD>
			<%}else{%>
			<INPUT type="hidden" id="caisseChomage" value="">
			<%}%>
			<%if(codeAmasquerDec){%>
			<TD class="mtd" id="codeAff"
				onClick="javascript:resetSelection();routage(parentNode.id.substring(3));"
				width="">&nbsp;</TD>
			<%}%>
			<TD class="mtd" id="typeCompte"
				onClick="javascript:resetSelection();routage(parentNode.id.substring(3));"
				width="">&nbsp;</TD>
			<TD id="partBta" style="display: none">&nbsp;</TD>
			<TD id="codeSpecial" style="display: none">&nbsp;</TD>
			<TD id="brancheEconomique" style="display: none">&nbsp;</TD>
			<TD id="code" style="display: none">&nbsp;</TD>
			<TD id="idTypeCompte" style="display: none">&nbsp;</TD>
		</TR>
		<TR id="newEcrUp" class="row" style="display: none">
			<TD class="mtd" width=""><!--<IMG id="ecritureNonValid" name="suspens" src="<%=request.getContextPath()%>/images/verrou.gif" border="0">-->
			</TD>
			<TD class="mtd" width=""><img
				src="<%=request.getContextPath()%>/images/arrows.gif"></TD>
			<TD class="mtd" width="" nowrap><nss:nssPopup  name="avsNewUp" 
				value="" cssclass="libelle" jspName="<%=jspLocation%>"
				avsAutoNbrDigit="11" nssAutoNbrDigit="10" avsMinNbrDigit="8" nssMinNbrDigit="7" onChange="updateForm(tag);"
				onFailure="resetAssure(); " />&nbsp; <script>
						

     					partialavsNewUpPopupTag.onUpdate = showToolTips;
     					partialavsNewUpPopupTag.onStop   = hideToolTips;

						
     				</script></TD>
			<TD class="mtd" onchange="validateFloatNumber(this);"
				onkeypress="return filterCharForFloat(window.event);" align="right"
				width=""><INPUT size="15" maxLength="15" type="text" id="montant"
				onBlur="this.value=handleAmount(this.value)">&nbsp;</TD>
			<TD class="mtd" width=""><INPUT
				onkeypress="return filterCharForPositivInteger(window.event);"
				size="2" maxLength="3" type="text" id="gre">&nbsp;</TD>
			<TD class="mtd" width="4%"><INPUT
				onkeypress="return filterCharForPositivInteger(window.event);"
				size="1" maxLength="2" type="text" id="moisDebut">&nbsp;</TD>
			<TD class="mtd" width="4%"><INPUT
				onkeypress="return filterCharForPositivInteger(window.event);"
				size="1" maxLength="2" type="text" id="moisFin">&nbsp;</TD>
			<TD class="mtd" width="">
			<INPUT
				<%if(globaz.pavo.db.inscriptions.CIJournal.CS_CONTROLE_EMPLOYEUR.equals(viewBeanJournal.getIdTypeInscription()) 
				 || "301001".equals(viewBeanJournal.getIdTypeInscription()) || "301002".equals(viewBeanJournal.getIdTypeInscription())
				 )
				 {%>
					readonly tabindex='-1'
				<%}%>
				onkeypress="return filterCharForPositivInteger(window.event);"
				type="text" size="4" maxLength="4" id="annee">&nbsp;</TD> 
				<%if(globaz.pavo.db.inscriptions.CIJournal.CS_CONTROLE_EMPLOYEUR.equals(viewBeanJournal.getIdTypeInscription()) 
				|| globaz.pavo.db.inscriptions.CIJournal.CS_CORRECTIF.equals(viewBeanJournal.getIdTypeInscription())
				|| apgJournal || amiJournal || ijJournal || acJournal)
				{%>
					<TD class="mtd" width="" nowrap>
			                 
					<span id="partnermitgliedernewEcrUp" 
					><%=mitglied%></span>
					<INPUT type="text" name="partnerNumnewEcrUp" size="<%=apgJournal || amiJournal || acJournal ||ijJournal?14:tailleChampsAff%>" value="" readonly tabindex='-1'>
					<INPUT type="hidden" name="mitgliedNumnewEcrUp"  value="" >
					<INPUT type="hidden" name="employeurPartenaire"  value="">
					</td>
				<%}else if(!affilieAMasquer){%>
				<span id="partnermitgliedernewEcrUp" style="display : none"></span>
				<INPUT type="hidden" name="partnerNumnewEcrUp"  value="" >
				<INPUT type="hidden" name="mitgliedNumnewEcrUp"  value="" >
				<INPUT type="hidden" name="employeurPartenaire"  value="">
				
							
				<%}else{%>
				<TD class="mtd" width="" nowrap>
				<span id="partnermitgliedernewEcrUp" <%if(!cotPersJournal || splitJournal){%>
					onclick="switchPartnerMitglieder('newEcriture');"<%}%>><%=!splitJournal?mitglied:partner%></span>
				
				<nss:nssPopup 
				validateOnChange="true" value=""
				onChange="updateEmployeurPartenaire(tag,'ecrUp'+parent.fr_detail.document.forms[0].all('selectedId').value);"
				onFailure="resetEmployeurPartenaire('ecrUp'+parent.fr_detail.document.forms[0].all('selectedId').value);"
				name="mitgliedNumnewEcrUp" cssclass="<%=visiEmp%>"
				jspName="<%=jspLocation%>" avsMinNbrDigit="8" avsAutoNbrDigit="11" 
				nssMinNbrDigit="7" nssAutoNbrDigit="10" 
				/> <ct:FWPopupList
				validateOnChange="true" value=""
				onChange="updateEmployeurPartenaire(tag,'ecrUp'+parent.fr_detail.document.forms[0].all('selectedId').value);"
				onFailure="resetEmployeurPartenaire('ecrUp'+parent.fr_detail.document.forms[0].all('selectedId').value);"
				name="partnerNumnewEcrUp" size="<%=tailleChampsAff%>" className="<%=visiConj%>"
				jspName="<%=jspLocation2%>" minNbrDigit="3" autoNbrDigit="<%=autoDigitAff%>" /></TD>
				<INPUT type="hidden" name="employeurPartenaire"  value="">
				
				<%}%>
			<%if (BTA){%>
			<TD class="mtd" width=""><ct:FWCodeSelectTag name="partBta"
				libelle="code" defaut="" codeType="CIPARBTA" wantBlank="true" /><script>document.all("newEcrUp").all("partBta").style.width="1.2cm"</script><!--INPUT type="text" size="2" maxLength="2" id="partBta"-->&nbsp;</TD>
			<%}else{%>
			<INPUT type="hidden" name="partBta" value="">
			<%}%>
			<%if (Special){%>
			<TD class="mtd" width=""><ct:FWCodeSelectTag name="codeSpecial"
				libelle="code" defaut="" codeType="CICODSPE" wantBlank="true" /><script>document.all("newEcrUp").all("codeSpecial").style.width="1.2cm"</script><!--INPUT type="text" size="2" maxLength="2" id="codeSpecial"-->&nbsp;</TD>
			<%}else{%>
			<INPUT type="hidden" name="codeSpecial" value="">
			<%}%>
			<%if (BrEc){%>
			<TD class="mtd" width="">
				
				<INPUT type="text" size="2" name="brancheEconomique" value="" tabindex='-1' readonly>
				</TD>
			<%}else{%>
			<INPUT type="hidden" name="brancheEconomique" value="">
			<%}%>
			<%if (CAC){%>
			<TD class="mtd" width=""><INPUT size="5" maxLength="5" type="text"
				id="caisseChomage">&nbsp;</TD>
			<%}else{%>
			<INPUT type="hidden" id="caisseChomage" value="">
			<%}%>

			<%if(codeAmasquerDec){%>
			<TD class="mtd" width="">
				<ct:FWCodeSelectTag name="code"
				libelle="code" defaut="" codeType="CICODAMO" wantBlank="true" /><script>document.all("newEcrUp").all("code").style.width="1cm"</script>&nbsp;</TD>
			<%}else{%>
				<input type="hidden" name="code" value="">
			<%}%>
			<TD class="mtd" width="18%">
			<!--<ct:FWCodeSelectTag name="idTypeCompte"
				defaut="<%=globaz.pavo.db.compte.CIEcriture.CS_TEMPORAIRE%>"
				codeType="CITYPCOM" except="<%=typeCompteSaisie%>" wantBlank="false" />-->
				<INPUT type="text" size="10" readonly tabindex='-1' name="typeCompte"
				value="<%=globaz.pavo.translation.CodeSystem.getLibelle("303006", session)%>&nbsp;">
				<INPUT type="hidden" size="10" readonly name="idTypeCompte"
				value="<%="303006"%>">
				<script>document.all("newEcrUp").all("idTypeCompte").style.width="3cm"</script>&nbsp;</TD>
		</TR>
		<TR id="newEcriture" style="display: none" class="<%=rowStyle%>">
			<TD class="mtd" width=""><!--<IMG id="ecritureNonValid" name="suspens" src="<%=request.getContextPath()%>/images/verrou.gif" border="0">-->
			</TD>
			<TD class="mtd" width=""><img
				src="<%=request.getContextPath()%>/images/arrows.gif"></TD>
			<TD class="mtd" width="" nowrap><nss:nssPopup name="avsNew"
				value="" cssclass="libelle" jspName="<%=jspLocation%>"
				avsAutoNbrDigit="11" avsMinNbrDigit="8"
				nssAutoNbrDigit="10"  nssMinNbrDigit="7"
				onChange="updateForm(tag);" onFailure="resetAssure();" />&nbsp; <script>
     					//document.getElementById('avsNew').onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
     					partialavsNewPopupTag.onUpdate = showToolTips;
     					partialavsNewPopupTag.onStop   = hideToolTips;
     					
     					if(document.getElementById("avsNewNNSS").value==true){
     						nssCheckChar('43',"avsNewUp");
     					}else{
	     					nssCheckChar('45',"avsNewUp");
     					}

     				</script></TD>
			<TD class="mtd" align="right" width=""><INPUT
				onchange="validateFloatNumber(this);"
				onkeypress="return filterCharForPositivFloat(window.event);" type="text"
				size="15" maxLength="15" id="montant"
				onBlur="this.value=handleAmount(this.value)">&nbsp;</TD>
			<TD class="mtd" width=""><INPUT
				onkeypress="return filterCharForPositivInteger(window.event);"
				size="2" maxLength="3" type="text" id="gre">&nbsp;</TD>
			<TD class="mtd" width=""><INPUT
				onkeypress="return filterCharForPositivInteger(window.event);"
				type="text" size="1" maxLength="2" id="moisDebut">&nbsp;</TD>
			<TD class="mtd" width=""><INPUT
				onkeypress="return filterCharForPositivInteger(window.event);"
				type="text" size="1" maxLength="2" id="moisFin">&nbsp;</TD>
			<TD class="mtd" width=""><INPUT
				<%if(globaz.pavo.db.inscriptions.CIJournal.CS_CONTROLE_EMPLOYEUR.equals(viewBeanJournal.getIdTypeInscription()) 
				 || "301001".equals(viewBeanJournal.getIdTypeInscription()) || "301002".equals(viewBeanJournal.getIdTypeInscription())){%>
				readonly tabindex='-1'<%}%>
				onkeypress="return filterCharForPositivInteger(window.event);"
				type="text" size="4" maxLength="4" id="annee">&nbsp;</TD>
			
			<%if(globaz.pavo.db.inscriptions.CIJournal.CS_CONTROLE_EMPLOYEUR.equals(viewBeanJournal.getIdTypeInscription()) 
			|| globaz.pavo.db.inscriptions.CIJournal.CS_CORRECTIF.equals(viewBeanJournal.getIdTypeInscription())
			|| apgJournal || amiJournal || ijJournal || acJournal 
			
			){%>
				<TD class="mtd" width="" nowrap>
			
				<span id="partnermitgliedernewEcriture" 
				><%=mitglied%></span>
				<INPUT type="text" name="partnerNumnewEcriture" size="<%=apgJournal || amiJournal || acJournal || ijJournal?14:tailleChampsAff%>" 
				<%if(amiJournal){%>
					value="666.66.666.666"
				
				<%}else if(ijJournal){%>
					value="888.88.888.888"
				
				<%}else if(apgJournal){%>
					value="777.77.777.777"
				<%}else{%>
				
				value="" 
				<%}%>
				readonly tabindex='-1'>
				<INPUT type="hidden" name="mitgliedNumnewEcriture"  value="" >
				<INPUT type="hidden" name="employeurPartenaire"  value="">
				
				</td>
				
			<%}else if(!affilieAMasquer){%>
				<INPUT type="hidden" name="partnerNumnewEcriture"  value="" >
				<INPUT type="hidden" name="mitgliedNumnewEcriture"  value="" >
				<INPUT type="hidden" name="employeurPartenaire"  value="">
							
			<%}else{%>
			<TD class="mtd" width="" nowrap>
			<span id="partnermitgliedernewEcriture" <%if(!cotPersJournal && !splitJournal ){%>class="selected" onclick="switchPartnerMitglieder('newEcriture');"
			<%}%>><%=!splitJournal?mitglied:partner%></span>
			<input type="hidden" name="employeurPartenaire"> <nss:nssPopup 
				validateOnChange="true" value=""
				onChange="updateEmployeurPartenaire(tag,'newEcriture');"
				onFailure="resetEmployeurPartenaire('newEcriture');"
				name="mitgliedNumnewEcriture" cssclass="<%=visiEmp%>"
				jspName="<%=jspLocation%>" avsMinNbrDigit="8" avsAutoNbrDigit="11"
				nssMinNbrDigit="7" nssAutoNbrDigit="10"
				 /> <ct:FWPopupList
				validateOnChange="true"
				onChange="updateEmployeurPartenaire(tag,'newEcriture');"
				onFailure="resetEmployeurPartenaire('newEcriture');"
				name="partnerNumnewEcriture"  className="<%=visiConj%>"
				jspName="<%=jspLocation2%>" minNbrDigit="3" autoNbrDigit="<%=autoDigitAff%>" size="<%=tailleChampsAff%>"  />
				<script>
            	
            		document.forms(0).all("newEcriture").all("partnerNumnewEcriture").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
            		
            	</script>
            	
            	</td>
				
			<%}%>
			<%if (BTA){%>
			<TD class="mtd" width=""><ct:FWCodeSelectTag name="partBta"
				libelle="code" defaut="" codeType="CIPARBTA" wantBlank="true" /><script>document.all("newEcriture").all("partBta").style.width="1.2cm"</script>&nbsp;</TD>
			<%}else{%>
			<INPUT type="hidden" name="partBta" value="">
			<%}%>
			<%if (Special){%>
			<TD class="mtd" width=""><ct:FWCodeSelectTag name="codeSpecial"
				libelle="code" defaut="" codeType="CICODSPE" wantBlank="true" /><script>document.all("newEcriture").all("codeSpecial").style.width="1.2cm"</script>&nbsp;</TD>
			<%}else{%>
			<INPUT type="hidden" name="codeSpecial" value="">
			<%}%>
			<%if (BrEc){%>
				
					<TD class="mtd" width="">
					
					<INPUT type="text" size="2" name="brancheEconomique" value="" tabindex='-1' readonly> 
					
			<%}else{%>
			<INPUT type="hidden" name="brancheEconomique" value="">
			<%}%>
			<%if (CAC){%>
			<TD class="mtd" width=""><INPUT type="text" size="5" maxLength="5"
				id="caisseChomage">&nbsp;</TD>
			<%}else{%>
			<INPUT type="hidden" name="caisseChomage" value="">
			<%}%>
			
			<%if(codeAmasquerDec){%>
				<TD class="mtd" width="">
				<ct:FWCodeSelectTag name="code"
				libelle="code" defaut="" codeType="CICODAMO" wantBlank="true" /><script>document.all("newEcriture").all("code").style.width="1cm"</script>&nbsp;</TD>
			<%}else{%>
				<input type="hidden" name="code" value="">
			<%}%>
			<TD class="mtd" valign="center" width=""><!--<ct:FWCodeSelectTag
				name="idTypeCompte"
				defaut="<%=globaz.pavo.db.compte.CIEcriture.CS_TEMPORAIRE%>"
				codeType="CITYPCOM" except="<%=typeCompteSaisie%>" wantBlank="false" /><script>document.all("newEcriture").all("idTypeCompte").style.width="3cm"</script>&nbsp;</TD>-->
				<INPUT type="text" size="10" readonly tabindex='-1' name="typeCompte"
				value="<%=globaz.pavo.translation.CodeSystem.getLibelle("303006", session)%>&nbsp;">
				<INPUT type="hidden" size="10" readonly name="idTypeCompte"
				value="<%="303006"%>">
		</TR>

		<TR class="row">
			<td colspan="15">
			<hr size="1">
			</td>
		</tr>
		<!--<TR id="totaux" class="row">
			<TD></TD>
			<TD colspan="14">Total de contrôle: <span id="controle">-</span>
			&nbsp;&nbsp;&nbsp;&nbsp;Total actuel: <span id="inscrit">-</span>
			&nbsp;&nbsp;&nbsp;&nbsp;Solde du journal: <span id="solde">-</span></TD>
		</TR>-->



	</TBODY>
</TABLE>
<%if (wantPagination) {%>
	<%if (viewBean.canDoPrev()) {%>
	<input  style=" font-size :7pt;" type="button" value="&lt;&lt;" accesskey="," onclick="this.disabled=true;location.href='<%=findPreviousLink%>'"  >
	<% } else {%>
	<input  style=" font-size :7pt;" type="button" value="&lt;&lt;" disabled >
	<%}%>
	<%if (viewBean.canDoNext()) {%>
	<input style=" font-size :7pt;" type="button" value=";&gt;&gt;" accesskey="." onclick="this.disabled=true;location.href='<%=findNextLink%>'"  >
	<%} else {%>
	<input style=" font-size :7pt;" type="button" value="&gt;&gt;" disabled >
	<%}%>
<%}%>
<%if (wantPaginationPosition) {%>
	<%=viewBean.getOffset()+" - "+(viewBean.getOffset()+viewBean.size()-1)+" / "+viewBean.getCount()%>
<%}%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td bgcolor="#444444">&nbsp;</td>
	</tr>
	<tr>
		<td bgcolor="#ffffff">&nbsp;</td>
	</tr>
	<tr>
		<td bgcolor="#ffffff">&nbsp;</td>
	</tr>
	<tr>
		<td bgcolor="#ffffff">&nbsp;</td>
	</tr>
	<tr>
		<td bgcolor="#ffffff">&nbsp;</td>
	</tr>
	<tr>
		<td bgcolor="#ffffff"></td>
	</tr>
</table>
</FORM>
<SCRIPT>

if(document.all.ecrList.rows.length>1) {
	parent.fr_detail.location.href='<%=servletContext + mainServletPath%>?userAction=pavo.compte.ecriture.afficherSurPage&selectedId='+document.all.ecrList.rows(1).name+'&addNewEcriture=<%=request.getParameter("addNewEcriture")%>';
} else {
	parent.fr_detail.location.href='<%=servletContext + mainServletPath%>?userAction=pavo.compte.ecriture.afficherSurPage&addNewEcriture=<%=request.getParameter("addNewEcriture")%>';
}
</SCRIPT>
</BODY>

</HTML>

