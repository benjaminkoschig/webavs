<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<!--# set echo="url" -->
<%@ page language="java"  import="globaz.globall.http.*" errorPage="/errorPage.jsp" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<HEAD>
<%
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
String userActionNew = request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".afficher";
String userActionUpd = request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".modifier";
String userActionDel = request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".supprimer";
String lastModification = "";
String languePage = globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session);
String servletContext = request.getContextPath();
String mainServletPath = (String)request.getAttribute("mainServletPath");
String selectedIdValue = "";
String userActionValue = "";
String actionNew =  servletContext + mainServletPath + "?userAction=" + request.getParameter("userAction").substring(0,request.getParameter("userAction").lastIndexOf('.')) + ".afficher&_method=add";
int tableHeight = 243;
String subTableWidth = "100%";


if (mainServletPath == null) {
	mainServletPath = "";
}
String formAction =  servletContext + mainServletPath;
String key = "none";

String btnUpdLabel = "Modifier";
String btnDelLabel = "Supprimer";
String btnValLabel = "Valider";
String btnCanLabel = "Annuler";
String btnNewLabel = "Nouveau";
//boolean bButtonNew = objSession.hasRight(userActionNew, "ADD");
boolean bButtonNew = false;
boolean bButtonUpdate = objSession.hasRight(userActionUpd, "UPDATE");
boolean bButtonDelete = objSession.hasRight(userActionDel, "REMOVE");
boolean bButtonValidate = true;
boolean bButtonCancel = true;
if("DE".equals(languePage)) {
	btnUpdLabel = "&Auml;ndern";
	btnDelLabel = "L&ouml;schen";
	btnValLabel = "Best&auml;tigen";
	btnCanLabel = "Annullieren";
	btnNewLabel = "Neu";
}




	globaz.pavo.util.CIUtil util = new globaz.pavo.util.CIUtil();
    java.util.ArrayList ChampsCaches = util.getChampsAffiche(session);
    java.util.ArrayList colonnesAmasquer = util.getColonnesAMasquer(session);

	boolean BTA = true;
	boolean BrEc = true;
	boolean Special = true;
	boolean CAC = true;

	if(ChampsCaches.indexOf("BTA") >= 0){
 		BTA = false;
 	}

 	if(ChampsCaches.indexOf("CAC") >= 0 ){
 		CAC = false;
 	}
 	if(ChampsCaches.indexOf("BEC") >= 0){
 		BrEc = false;
 	}

 	if(ChampsCaches.indexOf("Special") >= 0){
 		Special = false;
 	}
 	boolean codeAmasquerDec = true;
	boolean affilieAMasquer = true;

%>
<SCRIPT language="JavaScript">
var langue = "<%=languePage%>";


function changeName(input)
{
	input.value=input.value.replace('ä','AE');
	input.value=input.value.replace('ö','OE');
	input.value=input.value.replace('ü','UE');
	input.value=input.value.replace('Ä','AE');
	input.value=input.value.replace('Ö','OE');
	input.value=input.value.replace('Ü','UE');

	input.value=input.value.replace('é','E');
	input.value=input.value.replace('è','E');
	input.value=input.value.replace('ô','O');
	input.value=input.value.replace('à','A');

	input.value=input.value.toUpperCase();
}
function trim(strText) {
    // this will get rid of leading spaces
    while (strText.substring(0,1) == ' ')
        strText = strText.substring(1, strText.length);

    // this will get rid of trailing spaces
    while (strText.substring(strText.length-1,strText.length) == ' ')
        strText = strText.substring(0, strText.length-1);

   return strText;
}


function checkKey(input){
	var re = /[^a-zA-Z\-'äöüÄÖÜéèôà,\s].*/
	if (re.test(input.value)) {
		input.value = input.value.substr(0,input.value.length-1);
	}
}

function toUpperCase(tagName){
	var mySt = document.forms[0].elements(tagName).value;
	document.forms[0].elements(tagName).value = mySt.toUpperCase();
}

</SCRIPT>
<% /*
	Pour utiliser les postit, changez la valeur de la variable "key" (définie ci-dessus).
	La clé (key) est un String, id unique à l'objet représenté par le viewBean.
	*Conseillé:*
	  key = viewBean.getClass().getName() + "-" + <un_id_unique, p.ex viewBean.getId()>

	  qui ressemble à "globaz.xx.yy.XXBean-3456"

	Contacter vch en cas de soucis
	*/
%>

<%@ page import="globaz.globall.util.*"%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
    globaz.pavo.db.compte.CIEcritureViewBean viewBean = (globaz.pavo.db.compte.CIEcritureViewBean)session.getAttribute ("viewBeanEcriture");
	String partner="C";
 	String mitglied = "A";
 	if("DE".equals(languePage)) {
 		partner="P";
 		mitglied="M";
 	}
 	tableHeight = 50;
 %>
<%if(!JAUtil.isStringEmpty(viewBean.getEcritureId())){

	if(!viewBean.getJournal(null, true).getProprietaire().equals(objSession.getUserId())){
 		bButtonDelete = false;
 		bButtonUpdate = false;
 }
 	}%>
 <%
if(!JAUtil.isStringEmpty(viewBean.getEcritureId())){
 	if(globaz.pavo.db.inscriptions.CIJournal.CS_ASSURANCE_CHOMAGE.equals(viewBean.getJournal(null, true).getIdTypeInscription())){
 		CAC=true;
 	}
 	if(colonnesAmasquer.indexOf("codeAMasquer")>=0
 	&& globaz.pavo.db.inscriptions.CIJournal.CS_DECLARATION_SALAIRES.equals(viewBean.getJournal(null, true).getIdTypeInscription())){
 		codeAmasquerDec = false;
 	}
 	if(colonnesAmasquer.indexOf("codeAMasquer")>=0
 	&& globaz.pavo.db.inscriptions.CIJournal.CS_DECLARATION_COMPLEMENTAIRE.equals(viewBean.getJournal(null, true).getIdTypeInscription())){
 		codeAmasquerDec = false;
 	}
 	if(colonnesAmasquer.indexOf("affilieAMasquer")>=0
 	&& globaz.pavo.db.inscriptions.CIJournal.CS_DECLARATION_SALAIRES.equals(viewBean.getJournal(null, true).getIdTypeInscription())){
 		affilieAMasquer = false;
 	}
 	if(colonnesAmasquer.indexOf("affilieAMasquer")>=0
 	&& globaz.pavo.db.inscriptions.CIJournal.CS_DECLARATION_COMPLEMENTAIRE.equals(viewBean.getJournal(null, true).getIdTypeInscription())){
 		affilieAMasquer = false;
 	}


 }


 %>






<META name="GENERATOR"
	content="IBM WebSphere Page Designer V3.5.3 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<META HTTP-EQUIV="Pragma" CONTENT="no-cache">
<LINK rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/theme/master.css">
<TITLE></TITLE>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/params.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/actionsForButtons.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/swap.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/shortKeys.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/autocomplete.js"></SCRIPT>
<SCRIPT language="JavaScript"
	src="<%=request.getContextPath()%>/scripts/selectionPopup.js"></SCRIPT>

<%
if ((viewBean != null)&&(viewBean.getSpy() != null)) {
   lastModification = ""+viewBean.getSpy().getDate()+", "+viewBean.getSpy().getTime()+" - "+viewBean.getSpy().getUser();
}
%>


<SCRIPT language="javascript">

var errorObj = new Object();
errorObj.text = "";

function showErrors() {
	if (errorObj.text != "") {
		showModalDialog('<%=servletContext%>/errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');
	}
}

function disableBtn(aBtn) {
	aBtn.onclick = '';
	//aBtn.style.display = 'none';
	aBtn.disabled = true;
}

function onClickNew() {
	disableBtn(document.all('btnNew'));
	var oBtnFind = document.all('btnFind');
	if (oBtnFind != null) {
		disableBtn(oBtnFind);
	}
	hideAllButtons();
	document.location.href='<%=actionNew%>'

	document.forms[0].elements('nomPrenom').disabled = false;
}

function doInitThings() {
	actionInit();
	var methodElement = document.forms[0].elements('_method');
	action(methodElement.value);
	if(methodElement.value == ADD) {
		add();
		try {
			top.fr_appicons.hidePostit();
		} catch (e) {}
	} else {
		try {
			top.fr_appicons.checkPostit();
		} catch (e) {}
	}
	init();
	showErrors();

}

function iconActionPostit() {
	var myKey = "<%=key%>";
	if (myKey != "none") {
		var url = "<%=formAction%>?userAction=framework.postit.display&pop=yes&key="+myKey+"&mainServletPath=<%=mainServletPath%>";
		window.open(url,"_blank",'height=300px,width=350px');
	}
}

function isPostitEnabled() {
	return <%= "none".equals(key)?"false":"true"%>;
}

</SCRIPT>

<SCRIPT language="JavaScript">


function sendEvent(event) {
	if (event.ctrlKey) {
		if(event.keyCode == 13) {
          		if(validate()){action(COMMIT)};
   		}
   	}
}
// Importe les données saisies dans le frame fr_liste
function importData() {

	if (document.forms[0].elements("_method").value== "add") {
		newEcriture = parent.fr_list.document.forms[0].all("newEcriture");
		document.forms[0].elements("avs").value = newEcriture.all("avsNew").value;
		document.forms[0].elements("partialavs").value = newEcriture.all("partialavsNew").value;
		document.forms[0].elements("avsNNSS").value = newEcriture.all("avsNewNNSS").value;
		document.forms[0].elements("montant").value = newEcriture.all("montant").value;
		document.forms[0].elements("moisDebut").value = newEcriture.all("moisDebut").value;
		document.forms[0].elements("moisFin").value = newEcriture.all("moisFin").value;
		document.forms[0].elements("gre").value = newEcriture.all("gre").value;
		document.forms[0].elements("annee").value = trim(newEcriture.all("annee").value);
		document.forms[0].elements("employeurPartenaire").value = newEcriture.all("employeurPartenaire").value;
		document.forms[0].elements("partBta").value = newEcriture.all("partBta").value;
		document.forms[0].elements('codeSpecial').value = newEcriture.all("codeSpecial").value;
		document.forms[0].elements('brancheEconomique').value = newEcriture.all("brancheEconomique").value;
		document.forms[0].elements('caisseChomage').value = newEcriture.all("caisseChomage").value;
		document.forms[0].elements('code').value = newEcriture.all("code").value;
		document.forms[0].elements('idTypeCompte').value = newEcriture.all("idTypeCompte").value;
		document.forms[0].elements('idJournal').value = parent.document.forms[0].elements("idJournal").value;
	} else {
		//alert("import");
		line = parent.fr_list.document.forms[0].all('ecrUp'+document.forms[0].elements('selectedId').value);
		document.forms[0].elements("avs").value = trim(line.all("avs"+document.forms[0].elements('selectedId').value).value);
		document.forms[0].elements("partialavs").value = trim(line.all("avs"+document.forms[0].elements('selectedId').value).value);
		//document.forms[0].elements("avsNNSS").value = trim(line.all("avsNNSS"+document.forms[0].elements('selectedId').value).value);;
		document.forms[0].elements("montant").value = trim(line.all("montant").value);
		document.forms[0].elements("moisDebut").value = trim(line.all("moisDebut").value);
		document.forms[0].elements("moisFin").value = trim(line.all("moisFin").value);
		document.forms[0].elements("gre").value = trim(line.all("gre").value);
		document.forms[0].elements("annee").value =  trim(line.all("annee").value);
		document.forms[0].elements("employeurPartenaire").value = line.all("employeurPartenaire").value;
		document.forms[0].elements("partBta").value = line.all("partBta").value;
		document.forms[0].elements('codeSpecial').value = line.all("codeSpecial").value;
		document.forms[0].elements('brancheEconomique').value = line.all("brancheEconomique").value;
		document.forms[0].elements('caisseChomage').value = line.all("caisseChomage").value;
		document.forms[0].elements('code').value = line.all("code").value;
		document.forms[0].elements('idTypeCompte').value = line.all("idTypeCompte").value;
		document.forms[0].elements('idJournal').value = parent.document.forms[0].elements("idJournal").value;
	}
//	 total de controle

	document.forms[0].elements('totalControleSaisie').value = parent.document.forms[0].all("totalCtrl").value;
}



function add() {

    document.forms[0].elements('userAction').value="pavo.compte.ecriture.ajouterSurPage";
}
function upd() {


	document.forms[0].elements('paysOrigineLabel').disabled = true;
//	 Les boutons nouveau sont désactivés
	parent.onClickNew();
//	 Les autres boutons sont désactivés par les script d'importation
	//parent.document.images("btnNew").style.display="none";
	//document.images("btnNew").style.display="none";
	//alert("upd");
	line = parent.fr_list.document.forms[0].all('ecrUp'+document.forms[0].elements('selectedId').value);
	ecriture = parent.fr_list.document.forms[0].all.item("ecr"+document.forms[0].elements('selectedId').value);
	typeCompte = ecriture.all("idTypeCompte").outerText;
	codeState = true;
	avsState = true;
	formState = true;
	if(typeCompte==<%=globaz.pavo.db.compte.CIEcriture.CS_TEMPORAIRE%> || typeCompte==<%=globaz.pavo.db.compte.CIEcriture.CS_TEMPORAIRE_SUSPENS%>) {
//		 écriture temporaire
		avsState = false;
		codeState = false;
		formState = false;
	}
	if(ecriture.all("code").outerText==<%=globaz.pavo.db.compte.CIEcriture.CS_CODE_PROVISOIRE%>) {
		codeState = false;
	}
	if(typeCompte==<%=globaz.pavo.db.compte.CIEcriture.CS_CI_SUSPENS%>) {
		avsState = false;
	}


//	 Si l'écriture est au CI au GENRE_7, seule la remarque est modifiable
//	if (typeCompte==<%=globaz.pavo.db.compte.CIEcriture.CS_CI%>||typeCompte==<%=globaz.pavo.db.compte.CIEcriture.CS_GENRE_7%>) {
		document.forms[0].elements('nomPrenom').disabled = true;
//		document.forms[0].elements('dateDeNaissance').disabled = true;
//		document.forms[0].elements('sexe').disabled = true;
//		document.forms[0].elements('messages').disabled = true;
//		document.forms[0].elements('paysOrigine').disabled = true;
//		document.forms[0].elements('ARC').disabled = true;
//		document.forms[0].elements('buttons').disabled = true;
//	} else {
//		 Si l'écriture est au GENRE_6
//		if (typeCompte==<%=globaz.pavo.db.compte.CIEcriture.CS_GENRE_6%>) {
//			document.forms[0].elements('messages').disabled = true;
//			document.forms[0].elements('ARC').disabled = true;
//			document.forms[0].elements('Button').disabled = true;
//		}

//	}

//	 Déverrouillage des inputs du frame fr_list et initialisation des valeurs
//	ecriture = parent.fr_list.document.forms[0].all.item("ecr"+document.forms[0].elements('selectedId').value);
	ecriture.style.display="none";
	line.all("avs"+document.forms[0].elements('selectedId').value).value = trim(ecriture.all("avs").outerText);
	//alert("test");
	//alert("test2");

	line.all("partialavs"+document.forms[0].elements('selectedId').value).value = trim(ecriture.all('partialavs').value);
	//line.all("avsNNSS"+document.forms[0].elements('selectedId').value).value = trim(ecriture.all('avsNNSS').value);
	line.all("avs"+document.forms[0].elements('selectedId').value+"NNSS").value = trim(ecriture.all('avsNNSS').value);
	line.all("avs"+document.forms[0].elements('selectedId').value).disabled = avsState;
	//parent.fr_list.checkNSSCaPageUpd(document.forms[0].elements('selectedId').value,ecriture.all("avsNNSS").value);
//	line.all("avs").value = ecriture.all("avs").outerText;
	line.all("montant").value = trim(ecriture.all("montant").outerText);
	line.all("montant").disabled = formState;
	line.all("moisDebut").value = trim(ecriture.all("moisDebut").outerText);
	line.all("moisFin").value = trim(ecriture.all("moisFin").outerText);
	line.all("gre").value = ecriture.all("gre").outerText;
	line.all("gre").disabled = formState;
	line.all("annee").value = trim(ecriture.all("annee").outerText);
	line.all("annee").disabled = formState;
	line.all("idTypeCompte").value = ecriture.all("idTypeCompte").outerText;
	line.all("idTypeCompte").disabled = formState;



	line.all("employeurPartenaire").value = ecriture.all("employeurPartenaire").outerText;
	line.all("employeurPartenaire").disabled = formState;
	if (line.all("gre")) {
		var str = new String(line.all("gre").value);
		if (str.charAt(1)=='8') {
			line.all('partnermitglieder'+'ecrUp'+document.forms[0].elements('selectedId').value).innerHTML = "<%=partner%>";
			line.all('partnerNum'+'ecrUp'+document.forms[0].elements('selectedId').value).className = 'visible';
			line.all('mitgliedNum'+'ecrUp'+document.forms[0].elements('selectedId').value).className = 'hiddenPage';
		} else {
			line.all('partnermitglieder'+'ecrUp'+document.forms[0].elements('selectedId').value).innerHTML = "<%=mitglied%>";
			line.all('partnerNum'+'ecrUp'+document.forms[0].elements('selectedId').value).className = 'hiddenPage';
			line.all('mitgliedNum'+'ecrUp'+document.forms[0].elements('selectedId').value).className = 'visible';
		}
	}


	line.all("mitgliedNumecrUp"+document.forms[0].elements('selectedId').value).value = ecriture.all("employeurPartenaire").outerText;
	line.all("mitgliedNumecrUp"+document.forms[0].elements('selectedId').value).disabled = formState;
	line.all("partnerNumecrUp"+document.forms[0].elements('selectedId').value).value = ecriture.all("employeurPartenaire").outerText;
	line.all("partnerNumecrUp"+document.forms[0].elements('selectedId').value).disabled = formState;
//	line.all("partBta").value = ecriture.all("partBta").outerText;
	<%if (BTA){%>
	for (i = 0; i < line.all("partBta").options.length; i++) {
		if (ecriture.all("partBta").innerText == line.all("partBta").options(i).value) {
			line.all("partBta").selectedIndex = i;
			break;
		}
	}
	line.all("partBta").disabled = formState;
	<%}%>
//	line.all("codeSpecial").value = ecriture.all("codeSpecial").outerText;
	<%if (Special){%>
	for (i = 0; i < line.all("codeSpecial").options.length; i++) {
		if (ecriture.all("codeSpecial").innerText == line.all("codeSpecial").options(i).value) {
			line.all("codeSpecial").selectedIndex = i;
			break;
		}
	}
	line.all("codeSpecial").disabled = formState;
	<%}%>
//	line.all("brancheEconomique").value = ecriture.all("brancheEconomique").outerText;
	<%if(BrEc){%>
	//for (i = 0; i < line.all("brancheEconomique").options.length; i++) {
	//	if (ecriture.all("brancheEconomique").innerText == line.all("brancheEconomique").options(i).value) {
	//		line.all("brancheEconomique").selectedIndex = i;
	//		break;
	//	}
	//}
	line.all("brancheEconomique").value=ecriture.all("brancheEconomique").outerText;
	line.all("brancheEconomique").disabled = formState;
	<%}%>
	<%if (CAC){%>
	line.all("caisseChomage").value = ecriture.all("caisseChomage").outerText;
	line.all("caisseChomage").disabled = formState;
	<%}%>
	<%if(codeAmasquerDec){%>
		for (i = 0; i < line.all("code").options.length; i++) {
			if (ecriture.all("code").innerText == line.all("code").options(i).value) {
				line.all("code").selectedIndex = i;
				break;
			}
		}
	line.all("code").disabled = codeState;
	<%}%>

	//line.all("idTypeCompte").value=ecriture.all("idTypeCompte").outerText;

	//for (i = 0; i < line.all("idTypeCompte").options.length; i++) {
	//	if (ecriture.all("idTypeCompte").innerText == line.all("idTypeCompte").options(i).value) {
	//		line.all("idTypeCompte").selectedIndex = i;
	//	}
	//}
	line.all("idTypeCompte").disabled = formState;
	parent.fr_list.document.forms[0].all.item("ecrUp"+document.forms[0].elements('selectedId').value).style.display="inline";

	 <%
	if(!viewBean.getCI(null, true).getRegistre().equals(globaz.pavo.db.compte.CICompteIndividuel.CS_REGISTRE_ASSURES))
	{%>

		document.forms(0).elements('nomPrenom').disabled = false;

	<%}
%>
}
function scrollWin(){
	parent.fr_list.scroll(1,2000);
}
function adjustHeight(){
	parent.fr_list.height=parent.fr_list.height+10000;

}

function validate() {
//	parent.fr_list.location.reload();
    importData();
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add") {
        document.forms[0].elements('userAction').value="pavo.compte.ecriture.ajouterSurPage";

    }
    else {
        document.forms[0].elements('userAction').value="pavo.compte.ecriture.modifierSurPage";
        //document.forms[0].target="fr_main";
    }

    //document.forms[0].elements('nomPrenom').disabled = true;
    //scrollWin();
    return state;
}

function cancel() {
	parent.location.href='<%=(servletContext + mainServletPath)%>?userAction=pavo.compte.ecriture.chercherSurPage';
	//&idJournal="+parent.document.forms[0].elements('idJournal').value;
}
function del() {
   	if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
//		 SI le type de l'écriture est suspens, choix supplementaire...
		ecriture = parent.fr_list.document.forms[0].all.item("ecr"+document.forms[0].elements('selectedId').value);
		if (ecriture.all("idTypeCompte").innerText == "303002") {
			//var choix = window.showModalDialog('<%=servletContext%>/pavoRoot/<%=languePage%>/compte/supprimerSuspens.jsp');
			//if (choix != "SUPPRIMER_SUSPENS") {
			//	document.forms[0].elements('userAction').value="pavo.compte.ecriture.supprimerSurPage";
			  //     document.forms[0].target="fr_main";
			    //   document.forms[0].submit();

			//} else {

				document.forms[0].elements('userAction').value="pavo.compte.ecriture.supprimerSuspens";
			       document.forms[0].target="fr_main";
			       document.forms[0].submit();
		} else {
		 	document.forms[0].elements('userAction').value="pavo.compte.ecriture.supprimerSurPage";
		    document.forms[0].target="fr_main";
		    document.forms[0].submit();

		}
    }

}

function newEcriture(){
// Pour appeler newEcriture du frame père
	parent.newEcriture();

}

function init() {
	//alert("init");

	<%if ((!viewBean.getAvs().equals("")) && (viewBean.getMsgType().equals (viewBean.ERROR) == true)) {%>
		<%globaz.pavo.db.compte.CICompteIndividuel ciRef =  new globaz.pavo.db.compte.CICompteIndividuel();
			ciRef = viewBean.getForcedCi(null);
		 %>
		<%if(ciRef!=null && globaz.pavo.db.compte.CICompteIndividuel.CS_REGISTRE_ASSURES.equals(ciRef.getRegistre())){%>
			document.getElementById('nomPrenom').disabled=true;
			document.dernieresEcritures.location.href='<%=request.getContextPath()%>/pavoRoot/lastentries.jsp?compteIndividuelId=<%=ciRef.getCompteIndividuelId()%>';
		<%}%>

	<%}%>

	<% if ((viewBean == null) || (viewBean.getMsgType().equals (viewBean.ERROR) == false)) {%>

// Suite à un ajout d'écriture
	<% if ((request.getParameter("_result") != null) && ("added".equalsIgnoreCase(request.getParameter("_result")))) { %>

		var newEcrValBlank = parent.fr_list.document.forms[0].all("newEcritureValidated").cloneNode(true);
		var newEcrUpBlank = parent.fr_list.document.forms[0].all("newEcrUp").cloneNode(true);


//		 Remplissage valeurs
//			 Si pas au suspens on modifie l'icone
		<% if (!"303002".equals(viewBean.getIdTypeCompte()) && !"303008".equals(viewBean.getIdTypeCompte())) {%>
			parent.fr_list.document.forms[0].all("newEcritureValidated").all("suspensOrNot").src = '<%=request.getContextPath()%>/images/ok.gif';
		<%} else { %>
			parent.fr_list.document.forms[0].all("newEcritureValidated").all("suspensOrNot").src = '<%=request.getContextPath()%>/images/erreur.gif';
		<% } %>


		parent.fr_list.document.forms[0].all("newEcritureValidated").all("avs").innerText = "<%=viewBean.getNssFormate()%>";

		//alert(parent.fr_list.document.forms[0].all("newEcritureValidated").elements("partialavs").outerText);


		parent.fr_list.document.forms[0].all("newEcritureValidated").all("partialavs").value = "<%=viewBean.getPartialNss()%>";
		parent.fr_list.document.forms[0].all("newEcritureValidated").all("avsNNSS").value = "<%=viewBean.getAvsNNSS()%>";
		parent.fr_list.document.forms[0].all("newEcritureValidated").all("avs").value = "<%=viewBean.getNssFormate()%>";
		//alert(parent.fr_list.document.forms[0].all("newEcritureValidated").all("avsNNSS").value );
		parent.fr_list.document.forms[0].all("newEcritureValidated").all("montant").innerText = "<%=viewBean.getMontantFormat()%> ";
		parent.fr_list.document.forms[0].all("newEcritureValidated").all("moisDebut").innerText = "<%=viewBean.getMoisDebutWithoutZero()%> ";
		parent.fr_list.document.forms[0].all("newEcritureValidated").all("moisFin").innerText = "<%=viewBean.getMoisFinWithoutZero()%> ";
		parent.fr_list.document.forms[0].all("newEcritureValidated").all("gre").innerText = "<%=viewBean.getGreFormat()%> ";
		parent.fr_list.document.forms[0].all("newEcritureValidated").all("annee").innerText = "<%=viewBean.getAnnee()%> ";
		parent.fr_list.document.forms[0].all("newEcritureValidated").all("employeurPartenaire").innerText = "<%=viewBean.getEmployeurPartenaireForDisplay()%> ";
		parent.fr_list.document.forms[0].all("newEcritureValidated").all("employeurPartenaire").title = "<%=globaz.jade.client.util.JadeStringUtil.change(viewBean.getInfoEmployeurPartenaire(),"\"","&quot;")%> ";
		parent.fr_list.document.forms[0].all("newEcritureValidated").all("partBta").innerText = "<%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(viewBean.getPartBta(), session)%> ";
		parent.fr_list.document.forms[0].all("newEcritureValidated").all("codeSpecial").innerText = "<%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(viewBean.getCodeSpecial(), session)%> ";
		parent.fr_list.document.forms[0].all("newEcritureValidated").all("brancheEconomique").innerText = "<%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(viewBean.getBrancheEconomique(), session)%> ";
		parent.fr_list.document.forms[0].all("newEcritureValidated").all("caisseChomage").innerText = "<%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(viewBean.getCaisseChomage(), session)%> ";
		<%if(codeAmasquerDec){%>
		parent.fr_list.document.forms[0].all("newEcritureValidated").all("codeAff").innerText = "<%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(viewBean.getCode(), session)%> ";
		<%}%>
		parent.fr_list.document.forms[0].all("newEcritureValidated").all("code").innerText = "<%=viewBean.getCode()%> ";
		parent.fr_list.document.forms[0].all("newEcritureValidated").all("typeCompte").innerText = "<%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getIdTypeCompte(),session)%> ";
		parent.fr_list.document.forms[0].all("newEcritureValidated").all("idTypeCompte").innerText = "<%=viewBean.getIdTypeCompte()%>";
		//parent.fr_list.document.forms[0].all("newEcritureValidated").all("avs").id='avs'+'<%=viewBean.getEcritureId()%>';
		parent.fr_list.checkNSSCaPage(parent.fr_list.document.forms[0].all("newEcritureValidated").all("avsNNSS").value);
		//nssCheckChar('45',parent.fr_list.document.forms[0].all("newEcrUp").all("avsNewUp"));
		parent.fr_list.document.forms[0].all("newEcrUp").all("avsNewUp").id='avs'+'<%=viewBean.getEcritureId()%>';
		parent.fr_list.document.forms[0].all("newEcrUp").all("partialavsNewUp").id='partialavs'+'<%=viewBean.getEcritureId()%>';
		parent.fr_list.document.forms[0].all("newEcrUp").all("avsNewUpNNSS").id='avs'+'<%=viewBean.getEcritureId()%>'+'NNSS';

						parent.fr_list.document.forms[0].all("newEcrUp").all("avsNewUpNssPrefixe").id='avs'+'<%=viewBean.getEcritureId()%>'+'NssPrefixe';
		parent.fr_list.document.forms[0].all("newEcrUp").all("partnermitgliedernewEcrUp").id='partnermitglieder'+'ecrUp'+'<%=viewBean.getEcritureId()%>';
		parent.fr_list.document.forms[0].all("newEcrUp").all('partnerNumnewEcrUp').id='partnerNum'+'ecrUp'+'<%=viewBean.getEcritureId()%>';
		parent.fr_list.document.forms[0].all("newEcrUp").all('mitgliedNumnewEcrUp').id='mitgliedNum'+'ecrUp'+'<%=viewBean.getEcritureId()%>';


//		 Mise à jour du style
		var numEcr = parent.fr_list.document.forms[0].all("ecrListBody").rows.length*0.5;
// 		 En fonction de la parité du rang de l'écriture
		if ((numEcr % 2) == 1) {
			parent.fr_list.document.forms[0].all("newEcritureValidated").className = "rowOdd";
			parent.fr_list.document.forms[0].all("newEcrUp").className = "rowOdd";
			parent.fr_list.document.forms[0].all("newEcritureValidated").attachEvent('onMouseOut', mouseOut);
		}


//		 Mise à jour de la table

		parent.fr_list.document.forms[0].all("newEcritureValidated").id = 'ecr' + '<%=viewBean.getEcritureId()%>';
		parent.fr_list.document.forms[0].all("newEcrUp").id = 'ecrUp' + '<%=viewBean.getEcritureId()%>';
		parent.fr_list.document.forms[0].all('ecrUp' + '<%=viewBean.getEcritureId()%>').insertAdjacentElement("AfterEnd", newEcrValBlank);
		parent.fr_list.document.forms[0].all("newEcritureValidated").insertAdjacentElement("AfterEnd", newEcrUpBlank);




//		 Mise à jour du style
		parent.fr_list.document.forms[0].all('ecr' + '<%=viewBean.getEcritureId()%>').style.display = "block";
//
//		Mise à jour des totaux
	<% if (viewBean!=null ) {
		globaz.pavo.db.inscriptions.CIJournal journal = viewBean.getJournal(null, false);
		if(journal!=null && !JAUtil.isIntegerEmpty(journal.getTotalInscrit())) { %>
			parent.updateInsc(<%=journal.getTotalInscrit()%>);
	<% } } %>
//		parent.document.forms[0].all("totalCtrl").value;
//		parent.updateSum("<%=viewBean.getTotalControleSaisie()%>");
//		 Par défaut on se remet en mode ajout d'écriture
		parent.fr_list.resetSelection();
		parent.newEcriture();
		return;
	<%}%>
// Suite à une mise à jour d'écriture

	<% if ((request.getParameter("_result") != null) && ("updated".equalsIgnoreCase(request.getParameter("_result")))) { %>
		// Mise à jour des données d'affichage
		ecriture = parent.fr_list.document.forms[0].all.item("ecr"+document.forms[0].elements('selectedId').value);
		<% if (!"303002".equals(viewBean.getIdTypeCompte()) && !"303008".equals(viewBean.getIdTypeCompte())) {%>
			ecriture.all("suspensOrNot").src = '<%=request.getContextPath()%>/images/ok.gif';
		<%} else { %>
			ecriture.all("suspensOrNot").src = '<%=request.getContextPath()%>/images/erreur.gif';
		<% } %>

		ecriture.all("avs").innerText = "<%=viewBean.getNssFormate()%> ";
		ecriture.all("partialavs").innerText = "<%=viewBean.getNssFormate()%> ";
		ecriture.all("avsNNSS").innerText = "<%=viewBean.getAvsNNSS()%> ";
		ecriture.all("montant").innerText= "<%=viewBean.getMontantFormat()%> ";
		ecriture.all("moisDebut").innerText= "<%=viewBean.getMoisDebutWithoutZero()%> ";
		ecriture.all("moisFin").innerText= "<%=viewBean.getMoisFinWithoutZero()%> ";
		ecriture.all("gre").innerText= "<%=viewBean.getGreFormat()%> ";
		ecriture.all("annee").innerText= "<%=viewBean.getAnnee()%> ";
		ecriture.all("employeurPartenaire").innerText= "<%=viewBean.getEmployeurPartenaireForDisplay()%> ";
		ecriture.all("employeurPartenaire").title = "<%=viewBean.getInfoEmployeurPartenaire()%>";
		ecriture.all("partBta").innerText= "<%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(viewBean.getPartBta(), session)%> ";
		ecriture.all("codeSpecial").innerText= "<%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(viewBean.getCodeSpecial(), session)%> ";
		ecriture.all("brancheEconomique").innerText= "<%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(viewBean.getBrancheEconomique(), session)%> ";
		ecriture.all("caisseChomage").innerText= "<%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(viewBean.getCaisseChomage(), session)%> ";
		<%if(codeAmasquerDec){%>
		ecriture.all("codeAff").innerText= "<%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(viewBean.getCode(), session)%> ";
		<%}%>
		ecriture.all("code").innerText= "<%=viewBean.getCode()%> ";
		ecriture.all("typeCompte").innerText= "<%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getIdTypeCompte(),session)%> ";
		ecriture.all("idTypeCompte").innerText = "<%=viewBean.getIdTypeCompte()%>";
//		 Affichage de la ligne d'écriture mise à jour
		parent.fr_list.document.forms[0].all('ecrUp'+document.forms[0].elements('selectedId').value).style.display = "none";
		parent.fr_list.document.forms[0].all('ecr'+document.forms[0].elements('selectedId').value).style.display = "block";
		parent.fr_list.document.forms[0].all('ecr'+document.forms[0].elements('selectedId').value).all("ecritureNonValid").style.display = "block";

//		 Retour en mode consultation
		readOnly(true);
		showActionButtons();
		//parent.document.images("btnNew").style.display="inline"
		//parent.onClickNew();
		//document.images("btnNew").style.display="inline";
		//document.images("buttons").src='<%=request.getContextPath()%>/images/<%=languePage%>/btnUpdDel.gif';
		//document.images("buttons").useMap="#btnUpdDel";
//		Mise à jour des totaux
	<% if (viewBean!=null ) {
		globaz.pavo.db.inscriptions.CIJournal journal = viewBean.getJournal(null, false);
		if(journal!=null && !JAUtil.isIntegerEmpty(journal.getTotalInscrit())) { %>
			parent.updateInsc(<%=journal.getTotalInscrit()%>);

	<% } } %>
//		 Le champs _method est remis à vide pour éviter les effets du script action()
		document.forms[0].elements('_method').value = "";
	<% } %>

//	 Si on en train de regarder une écriture existante, mettre l'entrée correspondante dans ecriture_caPage.jsp en gras
	if (document.forms[0].elements('selectedId').value != '') {
		parent.fr_list.document.forms[0].all.item("ecr"+document.forms[0].elements('selectedId').value).style.fontWeight="bold";
	}

<%} else {%>
	//document.images("btnNew").style.display="none";
<%}%>
<%

if (request.getParameter("addNewEcriture") != null && request.getParameter("addNewEcriture").equals("true")) {

%>

	parent.fr_list.document.forms(0).all("newEcriture").all("partialavsNew").focus();
<%  }  %>

	//document.forms[0].elements('dernieresEcritures').disabled=false;

}

function mouseOut(e) {
	e.srcElement.style.background=finds('.rowOdd').style.background;
	e.srcElement.style.color=finds('.rowOdd').style.color;
}

function changeName(input)
{
	input.value=input.value.replace('ä','AE');
	input.value=input.value.replace('ö','OE');
	input.value=input.value.replace('ü','UE');
	input.value=input.value.replace('Ä','AE');
	input.value=input.value.replace('Ö','OE');
	input.value=input.value.replace('Ü','UE');

	input.value=input.value.replace('é','E');
	input.value=input.value.replace('è','E');
	input.value=input.value.replace('ô','O');
	input.value=input.value.replace('à','A');

	input.value=input.value.toUpperCase();
}

function checkKey(input){
	var re = /[^a-zA-Z\-'äöüÄÖÜéèôà,\s].*/
	if (re.test(input.value)) {
		input.value = input.value.substr(0,input.value.length-1);
	}
}

function toUpperCase(tagName){
	var mySt = document.forms[0].elements(tagName).value;
	document.forms[0].elements(tagName).value = mySt.toUpperCase();
}


</SCRIPT>


</HEAD>

<BODY onload="doInitThings()" onKeyDown="keyDown();actionKeyDown();"
	onKeyUp="keyUp();actionKeyUp();">


<TABLE bgcolor="#B3C4DB" cellspacing="0" cellpadding="0" width="100%"
	height="<%=tableHeight%>">
	<TBODY>
		<TR>
			<TD width="5">&nbsp;</TD>
			<TD valign="top">
			<FORM name="mainForm" action="<%=formAction%>">
			<TABLE border="0" cellspacing="0" cellpadding="0"
				width="<%=subTableWidth%>">
				<TBODY>


					<TR>
						<TD valign="top">
						<TABLE>
							<TBODY>
								<tr>
									<td>Namensangaben</td>
									<td colspan="4"><input onKeyUp="checkKey(this)" disable="true"
										onKeyDown="checkKey(this)" onChange="changeName(this)"
										type="text" name="nomPrenom"
										value="<%=viewBean.getNomPrenom()%>" size="70" maxlength="40">
									</td>
								</tr>
								<tr>
									<TD>
										Geburtsdatum &nbsp;
									</td>
									<td>
										<input type="text" size = "10" class='disabled' name="dateNaissanceNNSS" readonly tabindex='-1' value = "<%=viewBean.getDateNaissance()%>">
										&nbsp;
										Geschlecht &nbsp;
										<input type="text" size = "7" class='disabled' name="sexeNNSS"
										readonly tabindex='-1' value = "<%=!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(viewBean.getSexeForNNSS())?viewBean.getSexeForNNSS():""%>">
										Heimatstaat &nbsp;
										<input type="text" size = "45" class='disabled' name="paysNNSS" readonly tabindex='-1' value = "<%=viewBean.getPaysForNNSS()%>">
									</TD>
								</tr>

								<tr>
									<TD valign="top">IK-Buchungen</TD>

									<TD><IFRAME name="dernieresEcritures" scrolling="YES"
										style="border: solid 1px black; width: 13.1cm" height="80"> </IFRAME>
									<script>document.dernieresEcritures.location.href='<%=request.getContextPath()%>/pavoRoot/lastentries.jsp?compteIndividuelId=<%=viewBean.getCompteIndividuelId()%>';</script>
									</TD>
									<TD valign="top">
									<TABLE>
										<td valign="top">Bemerkung </td>
										<td valign="top"><textarea name="remarque" rows="3" cols="40"
											style="width: 4cm"><%=viewBean.getRemarque()%></textarea></td>
										<TR>
											<td height="2">Mitteilungen</td>
											<td height="2"><select name="messages" style="width: 4cm"
												tabIndex="-1">
										</TR>
									</TABLE>
									</select></td>
									</TD>
								</tr>
							</TBODY>


						</TABLE>
						</TD>
						<TD valign="top"></TD>
					</TR>
					<input type="hidden" name="dateDeNaissance"
						value="<%=viewBean.getDateDeNaissance()%>">
					<input type="hidden" name="sexe" value="<%=viewBean.getSexe()%>">
					<input type="hidden" name="paysOrigine"
						value="<%=globaz.pavo.translation.CodeSystem.getCodeUtilisateur(viewBean.getPaysOrigine(), session)%>">
					<input type="hidden" name="paysOrigineLabel"
						value="<%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getPaysOrigine(), session)%>">
					<input name="hideButtonUpd" type="hidden" value="">

				</TBODY>






			</TABLE>
			<INPUT type="hidden" name="selectedId"
				value='<%=viewBean.getEcritureId()%>'> <INPUT Type="hidden"
				name="userAction" value="pavo.compte.ecriture.modifier"> <INPUT
				type="hidden" name="_valid" value=''> <INPUT type="hidden"
				name="_sl" value=""> <INPUT type="hidden" name="_method"
				value="<%=request.getParameter("_method")%>"> <INPUT type="hidden"
				name="avs">
				<INPUT type="hidden"
				name="partialavs">
				<INPUT type="hidden"
				name="avsNNSS">
				<INPUT type="hidden" name="montant"> <INPUT
				type="hidden" name="moisDebut"> <INPUT type="hidden" name="moisFin">
			<INPUT type="hidden" name="gre"> <INPUT type="hidden" name="annee"> <INPUT
				type="hidden" name="employeurPartenaire"> <INPUT type="hidden"
				name="partBta"> <INPUT type="hidden" name="codeSpecial"> <INPUT
				type="hidden" name="brancheEconomique"> <INPUT type="hidden"
				name="caisseChomage"> <INPUT type="hidden" name="code"> <INPUT
				type="hidden" name="idTypeCompte"> <INPUT type="hidden"
				name="totalControleSaisie"> <INPUT type="hidden" name="idJournal"> <INPUT
				type="hidden" name="compteIndividuelId"></FORM>
			</TD>
			<TD width="5" height="50">&nbsp;</TD>
		</TR>
		<TR valign="bottom">
			<TD colspan="3" align="left"
				style="font-family: verdana; font-size: 9">
			<div
				style="background-color: #D7E4FF; border: solid 1px gray; width: 100%; padding-right: 10px; margin-top: 5px">
			<%=lastModification%></div>
			</TD>
		</TR>
		<TR>
			<%if(globaz.pavo.util.CIUtil.isSpecialist(session)){
					bButtonDelete=true;
					bButtonUpdate=true;
				}%>
			<%if(viewBean.getIdTypeCompte().equals(globaz.pavo.db.compte.CIEcriture.CS_CI))
			{
 				bButtonUpdate = false;
			 	bButtonDelete = false;
 			}
 %>



			<%
				if(!JAUtil.isStringEmpty(viewBean.getEcritureId())){
					if(globaz.pavo.db.inscriptions.CIJournal.CS_PARTIEL.equals(viewBean.getJournalIdEtat())
						|| globaz.pavo.db.inscriptions.CIJournal.CS_COMPTABILISE.equals(viewBean.getJournalIdEtat()))
					{
						bButtonUpdate=false;
						bButtonDelete=false;
					}
				}
				%>
				 <%
 			if(JAUtil.isStringEmpty(viewBean.getEcritureId())){
 				bButtonDelete = false;
 				bButtonUpdate = false;
 			}
 		%>

			<!--			<TD bgcolor="#FFFFFF" colspan="3" align="right" height="18"><IMG name="buttons" src="<%=request.getContextPath()%>/images/<%=languePage%>/btnUpdDel.gif" border="0" usemap="#btnUpdDel"></TD>-->
			<TD bgcolor="#FFFFFF" colspan="3" align="right" height="18"><%if (bButtonNew) {%><INPUT
				class="btnCtrl" type="button" id="btnNew" value="<%=btnNewLabel%>"
				onclick="onClickNew();btnNew.onclick='';hideAllButtons();document.location.href='<%=actionNew%>'"
				active="true"><%}%> <%if (bButtonUpdate) {%> <INPUT class="btnCtrl"
				id="btnUpd" type="button" value="<%=btnUpdLabel%>"
				onclick="action(UPDATE);upd();" active="true"><%}%> <%if (bButtonDelete) {%><INPUT
				class="btnCtrl" id="btnDel" type="button" value="<%=btnDelLabel%>"
				onclick="del();" active="true"><%}%> <%if (bButtonValidate) {%><INPUT
				class="btnCtrl" id="btnVal" type="button" value="<%=btnValLabel%>"
				onclick="if(validate()){ action(COMMIT);}else{document.forms[0].elements('nomPrenom').disabled = true;}"
				active="false"><%}%> <%if (bButtonCancel) {%><INPUT class="btnCtrl"
				id="btnCan" type="button" value="<%=btnCanLabel%>"
				onclick="cancel(); " active="false"><%}%></TD>


		</TR>
		<TR>
			<TD bgcolor="#FFFFFF"></TD>
			<TD bgcolor="#FFFFFF" colspan="2" align="left"><FONT color="#FF0000">
			<% if (viewBean.getMsgType().equals (globaz.framework.bean.FWViewBeanInterface.ERROR) == true) {%> <script>
						errorObj.text = "<%=globaz.framework.util.FWTextFormatter.slash(globaz.framework.util.FWTextFormatter.newLineToBr(viewBean.getMessage()), '\"')%>";
						<%
							viewBean.setMessage("");
							viewBean.setMsgType(globaz.framework.bean.FWViewBeanInterface.OK);
						%>

					</script> <% } %> </FONT></TD>
		</TR>
	</TBODY>
</TABLE>
<!--
<MAP name="btnUpdDel">
	<AREA shape="rect" coords="1,1,78,17" href="javascript:action(UPDATE);upd();">
	<AREA shape="rect" coords="78,1,157,17" href=javascript:del();>
	<AREA shape="default" nohref>
</MAP>
<MAP name="btnOkCancel">
	<AREA shape="rect" coords="1,1,78,17" href="javascript:if(validate()) action(COMMIT);">
	<AREA shape="rect" coords="78,1,157,17" href="javascript:cancel();action(ROLLBACK);">
	<AREA shape="default" nohref>
</MAP> -->

<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>

</SCRIPT>
<%  }  %>

<%

if (request.getParameter("addNewEcriture") != null && request.getParameter("addNewEcriture").equals("true")) {

%>
<SCRIPT>
	newEcriture();
</SCRIPT>
<%  }  %>






</BODY>

</HTML>