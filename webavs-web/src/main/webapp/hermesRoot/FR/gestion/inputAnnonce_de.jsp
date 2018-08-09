<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@page import="globaz.hermes.utils.HENNSSUtils"%>
<%@page import="globaz.pyxis.db.tiers.TITiersViewBean"%>
<%@page import="globaz.globall.util.JAUtil"%>
<%@page import="globaz.hermes.utils.HttpUtils"%>
<%@page import="globaz.hermes.db.gestion.HEAnnoncesViewBean"%>
<%@page import="globaz.hermes.utils.ChampsMap"%>
<%@page import="globaz.commons.nss.NSUtil"%>
<%@page import="globaz.hermes.db.gestion.HEInputAnnonceViewBean"%>
<%@page import="globaz.pyxis.constantes.IConstantes"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- page en allemand -->
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.hermes.api.IHEAnnoncesViewBean"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.HashSet"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.hermes.utils.HEUtil"%>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%
    userActionValue = "hermes.gestion.inputAnnonce.ajouter";

HEInputAnnonceViewBean viewBean = (HEInputAnnonceViewBean)session.getAttribute("viewBean");
viewBean.computeNeededFields(request.getParameter("motif")==null?viewBean.getInputMotif():request.getParameter("motif"),request.getParameter("critere")==null?viewBean.getInputCritere():request.getParameter("critere"));
boolean clearFields = request.getParameter("modeCancel")==null?false:request.getParameter("modeCancel").equals("true");
String detailLink = "hermes.parametrage.attenteEnvoi.afficher";
idEcran="GAZ0004";
String motif = viewBean.getReadOnlyFieldValue(IHEAnnoncesViewBean.MOTIF_ANNONCE,HttpUtils.getParamsAsMap(request));



boolean isDateNSS = globaz.hermes.utils.HEUtil.isNNSSActif(viewBean.getSession());
boolean isNnssDisplay =  globaz.hermes.utils.HEUtil.isNNSSdisplay(viewBean.getSession());
boolean isMotifCert = globaz.hermes.utils.HEUtil.isMotifCert(viewBean.getSession(),motif);
	
String formulePolitesse = viewBean.getFormulePolitesse();
if(JadeStringUtil.isEmpty(formulePolitesse)) {
	if(!JadeStringUtil.isEmpty(request.getParameter("formulePolitesse"))) {
		formulePolitesse = request.getParameter("formulePolitesse");
	}
}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/avsParser.js"></SCRIPT>
<script language="javascript">

$(function(){
	$("#wantPolitesse").hide();
	$("#langueCorrespondance").change(function () {
		if($(this).val()==<%=IConstantes.CS_TIERS_LANGUE_ALLEMAND%>){
			$("#wantPolitesse").show();
		}else {
			$("#wantPolitesse").hide();
			$("#formulePolitesse").val("");
		}
	});
	
	if($("#langueCorrespondance").val()==<%=IConstantes.CS_TIERS_LANGUE_ALLEMAND%>) {
		$("#wantPolitesse").show();
	}else {
		$("#wantPolitesse").hide();
		$("#formulePolitesse").val("");
	}
	
	 <%if(HEInputAnnonceViewBean.MSG_INSERT_OK.equals(viewBean.getActionMessage())){
	     String warningToDisplay="";
	     if(!JadeStringUtil.isBlankOrZero(warningToDisplay = viewBean.getWarningEmployeurSansPersoOrAccountZero())){%>
	     globazNotation.utils.consoleWarn("<%=warningToDisplay%>",'<ct:FWLabel key="HERMES_JSP_GAZ0004_AVERTISSEMENT"/>',true);
		<%
		}
	}%>

});



function readOnly(flag) {
  	// empeche la propriete disabled des elements etant de la classe css 'forceDisable' d'etre modifiee
    for(i=0; i < document.forms[0].length; i++) {
        if (!document.forms[0].elements[i].readOnly && 
        	document.forms[0].elements[i].className != 'forceDisable' &&
        	document.forms[0].elements[i].type != 'hidden') {
            document.forms[0].elements[i].disabled = flag;
        }
    }
  }

function upperCase(input){
	input.value = input.value.toUpperCase();
}

function nameWithHyphen(input){
    var regex = new RegExp("\\s*-\\s*","g");
    input.value=input.value.replace(regex,"-");
}

function changeName(input)
{
	while (input.value.search('ä') != -1)
		input.value=input.value.replace('ä','AE');
	while (input.value.search('ö') != -1)
		input.value=input.value.replace('ö','OE');
	while (input.value.search('ü') != -1)
		input.value=input.value.replace('ü','UE');
	while (input.value.search('é') != -1)
		input.value=input.value.replace('é','E');
	while (input.value.search('è') != -1)
		input.value=input.value.replace('è','E');
	while (input.value.search('ë') != -1)
		input.value=input.value.replace('ë','E');
	while (input.value.search('ê') != -1)
		input.value=input.value.replace('ê','E');					
	while (input.value.search('ô') != -1)
		input.value=input.value.replace('ô','O');
	while (input.value.search('à') != -1)
		input.value=input.value.replace('à','A');
		
	while (input.value.search('Ä') != -1)
		input.value=input.value.replace('Ä','AE');
	while (input.value.search('À') != -1)
		input.value=input.value.replace('À','A');
	while (input.value.search('Â') != -1)
		input.value=input.value.replace('Â','A');					
	while (input.value.search('É') != -1)
		input.value=input.value.replace('É','E');	
	while (input.value.search('È') != -1)
		input.value=input.value.replace('È','E');				
	while (input.value.search('Ë') != -1)
		input.value=input.value.replace('Ë','E');
	while (input.value.search('Ê') != -1)
		input.value=input.value.replace('Ê','E');	
	while (input.value.search('Ö') != -1)
		input.value=input.value.replace('Ö','OE');
	while (input.value.search('Ô') != -1)
		input.value=input.value.replace('Ô','O');
	while (input.value.search('Ü') != -1)
		input.value=input.value.replace('Ü','UE');	
				
		
	while (input.value.search('ç') != -1)
		input.value=input.value.replace('ç','C');	
	while (input.value.search('Ç') != -1)
		input.value=input.value.replace('Ç','C');
	
	input.value=input.value.toUpperCase();
}


function add() {
    document.forms[0].elements('userAction').value="hermes.gestion.inputAnnonce.ajouter";
}

function init(){
	<%if(!globaz.globall.util.JAUtil.isStringEmpty(viewBean.getIdDernierAjout())){
		bButtonDelete = true;
	} else {
		bButtonDelete = false;
	}
	btnDelLabel = "<<";
	%>
	document.forms[0].focus();	
	disableChkAjoutARC61(document.getElementById('idChkCreerArc61'));
}

function postInit()
{	
<% if(isDateNSS && isMotifCert) {%> 
	if (document.getElementById('dateEngagement') != null) {					
					document.getElementById('dateEngagement').style.backgroundColor = '#b3c4db';
					document.getElementById('dateEngagement').disabled = true;
					document.getElementById('anchor_dateEngagement').disabled = true;
	}
				
	if (document.getElementById('numeroAffilie') != null) {
					document.getElementById('numeroAffilie').style.backgroundColor = '#b3c4db';
					document.getElementById('numeroAffilie').disabled = true;
	}

	if (document.getElementById('numeroSuccursale') != null) {
		document.getElementById('numeroSuccursale').style.backgroundColor = '#b3c4db';
		document.getElementById('numeroSuccursale').disabled = true;
	}

	if (document.getElementById('numeroEmploye') != null) {
		document.getElementById('numeroEmploye').style.backgroundColor = '#b3c4db';
		document.getElementById('numeroEmploye').disabled = true;
	}
	
	if (document.getElementById('titreRentier') != null) {
					document.getElementById('titreRentier').style.backgroundColor = '#b3c4db';
					document.getElementById('titreRentier').disabled = true;
	}
	
	if (document.getElementById('formulePolitesse') != null) {
		document.getElementById('formulePolitesse').style.backgroundColor = '#b3c4db';
		document.getElementById('formulePolitesse').disabled = true;
	}
	catscript();
	
	<%} else {%>
			document.getElementById('numeroAffilie').style.backgroundColor = '#ffffff';
	<%}%>
	
	
	
	var input=document.getElementsByTagName("input");
	
	if(input[0].type=="text" && input[0].size > 3 ){
	input[0].focus();	
	}else {
	input[1].focus();
	}
	disableChkAjoutARC61(document.getElementById('idChkCreerArc61'));
}


function upd() {
	
}

function validate() {
   // state = validateFields();    
    var state = true;  
    return state;
} 

function cancel() {
 parent.location = "hermes?userAction=hermes.gestion.inputAnnonce.chercher";  
 document.forms[0].elements('userAction').value="";
}

function del() {
	<%if(!globaz.globall.util.JAUtil.isStringEmpty(viewBean.getIdDernierAjout())){%>
		document.forms[0].elements('userAction').value="<%=detailLink%>";
		document.forms[0].elements('selectedId').value="<%=viewBean.getIdDernierAjout()%>";
		document.forms[0].elements('_method').value="";
		document.forms[0].elements('_valid').value="";
		document.forms[0].submit();
	<%}%>
}

function checkKey(input){

		if(input.value.indexOf('--') != -1){
			input.value = input.value.replace('--','-');			
		}

		var re = /[^a-zA-Z\-'àäöôüéèëêçÄÂÀÉÈËÊÖÔÜÇ,\s].*/	
		if (re.test(input.value)) {
			input.value = input.value.substr(0,input.value.length-1);
		}
	
}

function checkNumber(input){
	var re = /[^0-9].*/
	if (re.test(input.value)) {
		input.value = input.value.substr(0,input.value.length-1);
	}

}

function checkChar(input){

//	var re = /[^a-zA-Z\-'äöü,\s].*/
	var re = /[^a-zA-Z0-9\-.,-/äöüÄÖÔÜËÉÈÊÇéèôàëç,\s].*/
	
	
	if (re.test(input.value)) {		
 		input.value = input.value.substr(0,input.value.length-1);
	}	
	
}

function updateForm(tag){
	if (tag.select && tag.select.selectedIndex != -1) {
		element = tag.select[tag.select.selectedIndex];
		if(document.forms[0].elements('118008')!=null){
			document.forms[0].elements('118008').value = element.nom;						
		}
		if(document.forms[0].elements('TOSTR118010')!=null){
			document.forms[0].elements('TOSTR118010').value = element.date;
		}
		if(document.forms[0].elements('118009')!=null){
			var vSexe = element.sexe;
			if(vSexe == '316000'){
				document.forms[0].elements('118009').value = '1';
			}
			if(vSexe == '316001'){		
				document.forms[0].elements('118009').value = '2';
			}
		}
		if(document.forms[0].elements('118011')!=null){
			document.forms[0].elements('118011').value = element.paysCode;
			document.forms[0].elements('TOLST118011').value = element.paysCode;
		}		
	}
	showOrHideChkAjoutARC61(tag);
}
// La case ne peut être cochée que si 
function showOrHideChkAjoutARC61(tag){
	if(tag != null && tag.input != null){
		var nss = tag.input.value;
		if(nss != ""){
			document.getElementById('idChkCreerArc61').removeAttribute("disabled");
		}else{
			disableChkAjoutARC61(document.getElementById('idChkCreerArc61'));
		}
	}else{
		disableChkAjoutARC61(document.getElementById('idChkCreerArc61'));
	}
}

function disableChkAjoutARC61(chkBox){
	chkBox.style.backgroundColor = '#b3c4db';
	chkBox.setAttribute("disabled","disabled");
	chkBox.checked = false;
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

function clearFields(tag){
	if (tag.select && tag.select.selectedIndex != -1) {
		element = tag.select[tag.select.selectedIndex];	
		element.nom = "";
		element.date = "";
		element.sexe = "316000";
		element.paysCode = "100";
	}
}
function clearFields(){
	if(document.forms[0].elements('118008')!=null){
		document.forms[0].elements('118008').value = "";						
	}
	if(document.forms[0].elements('TOSTR118010')!=null){
		document.forms[0].elements('TOSTR118010').value = "";
	}
	if(document.forms[0].elements('118009')!=null){		
		document.forms[0].elements('118009').value = '1';
	}
	if(document.forms[0].elements('118011')!=null){
		document.forms[0].elements('118011').value = '100';
	}		
}

function toUpperCase(tagName){
	var mySt = document.forms[0].elements(tagName).value;
	document.forms[0].elements(tagName).value = mySt.toUpperCase();
}

function validateCharForDate(input, event) {
	var valueStr = new String(input.value);	
	var keyCode = new String(String.fromCharCode(event.keyCode));
	
	if (genericFilter(new RegExp("[0-9\.]"), keyCode)) {
	}
	else {		
		event.keyCode = '';
		return;
	}	
}

function validatePartDateOnKeyPressed(input, event) {
	var valueStr = new String(input.value);	
	var keyCode = new String(String.fromCharCode(event.keyCode));	
	
	if (genericFilter(new RegExp("[0-9\.]"), keyCode)) {
	}
	else {		
		event.keyCode = '';
		return;
	}	

	if (valueStr.length>=4) {
		if (valueStr.charAt(2)!='.')
			event.keyCode = '';
	}
}

function validatePartDate(input) {
	var valueStr = new String(input.value);	
    var re1 = /^\d{2}\.\d{2}$/;
    var re2 = /^\d{4}$/;
	

    if ((re1.test(valueStr))!= true && (re2.test(valueStr))!= true){
    
		errorObj.text='<%=globaz.globall.util.JAUtil.replaceString(viewBean.getSession().getLabel("HERMES_00003"),"'","\\'")%>';
		showErrors();
		document.getElementById(input.name).focus();
	}	
}

// fonction permettant d'afficher le bouton annuler
function showDeleteBtn() {
//	document.getElementById("btnDel").active = "true";
	document.getElementById("btnDel").value="<<";
	document.getElementById("btnDel").tabIndex="-1";
	activateButton(document.getElementById("btnDel"));
	//  proceedButton("btnDel");
}	

function updateEmployeurPartenaire(tag) {

	if (tag.select&& tag.select.selectedIndex != -1) {
		document.getElementById('nomAffilie').value = tag.select[tag.select.selectedIndex].nom;
		document.getElementById('langueCorrespondance').value = tag.select[tag.select.selectedIndex].langue;
	}
}



// "surcharge" de la méthode doInitThings 
// ajouter l'affichage du bouton supprimer
var oldDoInit = doInitThings;
doInitThings = function () {
	oldDoInit();
	<%if(!globaz.globall.util.JAUtil.isStringEmpty(viewBean.getIdDernierAjout())){
		bButtonDelete = true;
		%>
		showDeleteBtn();
		<%
	} else {
		bButtonDelete = false;
	}
	%>
}
function setNotFound(tag){
	document.forms[0].elements('nomAffilie').value = "";
	document.getElementById('langueCorrespondance').value = "";
}

function updateJspNameCat()
{
	if(document.getElementById('categorie')!=null){
		var n = document.getElementById('categorie').value;
		var empl = '<%=IHEAnnoncesViewBean.CS_CATEGORIE_EMPLOYEUR%>';
		var indé = '<%=IHEAnnoncesViewBean.CS_CATEGORIE_INDEPENDANT%>';
		var rentier = '<%=IHEAnnoncesViewBean.CS_CATEGORIE_RENTIER%>';
		
		if (n==empl){
			numeroAffiliePopupTag.updateJspName("<%=servletContext + mainServletPath + "Root/ti_select_emp.jsp?like="%>"); 
		}else if(n==indé){
			numeroAffiliePopupTag.updateJspName("<%=servletContext + mainServletPath + "Root/ti_select_ind.jsp?like="%>");
		}
										
	}
}

function catscript()
{
<% if(isDateNSS) {%> 			
		var n = document.getElementById('categorie').value;
		
		var empl = '<%=IHEAnnoncesViewBean.CS_CATEGORIE_EMPLOYEUR%>';
		var indé = '<%=IHEAnnoncesViewBean.CS_CATEGORIE_INDEPENDANT%>';
		var rentier = '<%=IHEAnnoncesViewBean.CS_CATEGORIE_RENTIER%>';
		
		
		if (n==empl){						
			document.getElementById('txtarea').disabled = true;
			document.getElementById('txtarea').style.backgroundColor = '#b3c4db';
			document.getElementById('txtarea').value = "";
			document.getElementById('formulePolitesse').disabled = true;
			document.getElementById('formulePolitesse').style.backgroundColor = '#b3c4db';
			document.getElementById('formulePolitesse').value = "";
			document.getElementById('numeroAffilie').disabled = false;
			document.getElementById('numeroAffilie').style.backgroundColor = '#ffffff';
			document.getElementById('numeroSuccursale').disabled = false;
			document.getElementById('numeroSuccursale').style.backgroundColor = '#ffffff';
			document.getElementById('numeroEmploye').disabled = false;
			document.getElementById('numeroEmploye').style.backgroundColor = '#ffffff';
			document.getElementById('titreRentier').style.backgroundColor = '#b3c4db';
			document.getElementById('titreRentier').disabled = true;
			document.getElementById('titreRentier').value = "";
			if (document.getElementById('dateEngagement') != null) {
				document.getElementById('dateEngagement').disabled = false;
				document.getElementById('dateEngagement').style.backgroundColor = '#ffffff';
				document.getElementById('anchor_dateEngagement').disabled = false;
			}
		} else if (n==indé){	
			document.getElementById('txtarea').disabled = true;
			document.getElementById('txtarea').style.backgroundColor = '#b3c4db';
			document.getElementById('txtarea').value = "";
			document.getElementById('formulePolitesse').disabled = true;
			document.getElementById('formulePolitesse').style.backgroundColor = '#b3c4db';
			document.getElementById('formulePolitesse').value = "";
			document.getElementById('numeroAffilie').disabled = false;
			document.getElementById('numeroAffilie').style.backgroundColor = '#ffffff';	
			document.getElementById('numeroSuccursale').disabled = false;
			document.getElementById('numeroSuccursale').style.backgroundColor = '#ffffff';
			document.getElementById('numeroEmploye').disabled = false;
			document.getElementById('numeroEmploye').style.backgroundColor = '#ffffff';			
			document.getElementById('titreRentier').style.backgroundColor = '#b3c4db';
			document.getElementById('titreRentier').disabled = true;
			document.getElementById('titreRentier').value = "";
			if (document.getElementById('dateEngagement') != null) {
				document.getElementById('dateEngagement').disabled = true;
				document.getElementById('anchor_dateEngagement').disabled = true;
				document.getElementById('dateEngagement').style.backgroundColor = '#b3c4db';
				document.getElementById('dateEngagement').value = "";
			}
		}else if (n==rentier){	
			document.getElementById('txtarea').disabled = false;
			document.getElementById('txtarea').style.backgroundColor = '#ffffff';
			document.getElementById('formulePolitesse').disabled = false;
			document.getElementById('formulePolitesse').style.backgroundColor = '#ffffff';
			document.getElementById('numeroAffilie').disabled = true;
			document.getElementById('numeroAffilie').style.backgroundColor = '#b3c4db';
			document.getElementById('numeroAffilie').value = "";
			document.getElementById('numeroSuccursale').disabled = true;
			document.getElementById('numeroSuccursale').style.backgroundColor = '#b3c4db';
			document.getElementById('numeroSuccursale').value = "";
			document.getElementById('numeroEmploye').disabled = true;
			document.getElementById('numeroEmploye').style.backgroundColor = '#b3c4db';
			document.getElementById('numeroEmploye').value = "";
			document.getElementById('titreRentier').style.backgroundColor = '#ffffff';
			document.getElementById('titreRentier').disabled = false;
			
			if (document.getElementById('dateEngagement') != null) {
				document.getElementById('dateEngagement').disabled = true;
				document.getElementById('anchor_dateEngagement').disabled = true;
				document.getElementById('dateEngagement').style.backgroundColor = '#b3c4db';
				document.getElementById('dateEngagement').value = "";
			}	
		}else if (n==" "){
			document.getElementById('txtarea').disabled = true;
			document.getElementById('txtarea').style.backgroundColor = '#b3c4db';
			document.getElementById('formulePolitesse').disabled = true;
			document.getElementById('formulePolitesse').style.backgroundColor = '#b3c4db';
			document.getElementById('numeroAffilie').disabled = true;
			document.getElementById('numeroAffilie').style.backgroundColor = '#b3c4db';
			document.getElementById('numeroSuccursale').disabled = true;
			document.getElementById('numeroSuccursale').style.backgroundColor = '#b3c4db';
			document.getElementById('numeroEmploye').disabled = true;
			document.getElementById('numeroEmploye').style.backgroundColor = '#b3c4db';
			document.getElementById('titreRentier').style.backgroundColor = '#b3c4db';
			document.getElementById('titreRentier').disabled = true;
			if (document.getElementById('dateEngagement') != null) {
				document.getElementById('dateEngagement').disabled = true;
				document.getElementById('anchor_dateEngagement').disabled = true;
				document.getElementById('dateEngagement').style.backgroundColor = '#b3c4db';
			}
		}
				
			<%}%>
}

function updFieldsBoundedToCategorie(){
	catscript();
	
	document.getElementById('numeroAffilie').value = '';
	document.getElementById('nomAffilie').value = '';
	document.getElementById('numeroSuccursale').value = '';
	document.getElementById('numeroEmploye').value = '';
	document.getElementById('langueCorrespondance').value = '';
}

</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
										

					<%if(viewBean.getMsgType().equals(globaz.globall.db.BEntity.ERROR)){%>
						<tr>
							<td width="400" colspan="2">
								<%="<B><font color=\"#FF0000\">"+globaz.framework.util.FWTextFormatter.newLineToBr(viewBean.getMessage())+"</font></B>"%>
	        	    	    </td>
						</tr>
						<tr>
							<td colspan="2">&nbsp;</td>
						</tr>	
					<%} else if(HEInputAnnonceViewBean.MSG_INSERT_OK.equals(viewBean.getActionMessage())){
						clearFields = true;%>
						<tr>
							<td width="400" colspan="2">
								<%="<font color=\"#0000FF\"><B>"%><ct:FWLabel key="HERMES_JSP_GAZ0004_ARC_ENREGISTRE"/><%=NSUtil.formatAVSUnknown(request.getParameter("numAVS"))+"</B></font>"%>
	        	    	    </td>
						</tr>					
						<tr>
							<td colspan="2">&nbsp;</td>
						</tr>				
					<%}else if(HEInputAnnonceViewBean.MSG_UPDATE_OK.equals(viewBean.getActionMessage())){
						clearFields = true;
					%>
						<tr>
							<td width="400" colspan="2">
								<%="<font color=\"#0000FF\"><B>"%><ct:FWLabel key="HERMES_JSP_GAZ0004_ARC_MIS_A_JOUR"/><%=globaz.commons.nss.NSUtil.formatAVSUnknown(request.getParameter("numeroAVS"))+"</B></font>"%>       	  
	        	    	    </td>
						</tr>
						<tr>
							<td colspan="2">&nbsp;</td>
						</tr>						
					<%}else if(HEInputAnnonceViewBean.MSG_DELETE_OK.equals(viewBean.getActionMessage())){
						clearFields = true;
					%>
						<tr>
							<td width="400" colspan="2">
								<%="<font color=\"#0000FF\"><B>"%><ct:FWLabel key="HERMES_JSP_GAZ0004_ARC_SUPPRIME"/><%=NSUtil.formatAVSUnknown(request.getParameter("numeroAVS"))+"</B></font>"%>       	  
	        	    	    </td>
						</tr>
						<tr>
							<td colspan="2">&nbsp;</td>
						</tr>						
					<%}
		ChampsMap champsListe = (ChampsMap) viewBean.getChampsTable();
		for (java.util.Enumeration e = champsListe.keys(); e.hasMoreElements();) {
			Object hermesKey = e.nextElement();
			if (viewBean.isCodeSysteme((String) hermesKey)) {
			String defaut = clearFields?"":request.getParameter((String)hermesKey)!=null?request.getParameter((String)hermesKey):viewBean.getChampsAsCodeSystemDefaut((String)hermesKey);
%>				
					<tr>
						<td width="400">&nbsp;<%=champsListe.get(hermesKey)%></td>
						<td><ct:FWListSelectTag name="<%=((String)hermesKey)%>" 
						defaut="<%=defaut%>"
						data="<%=viewBean.getChampsAsCodeSystem((String)hermesKey)%>" /></td>
							
												</tr>
					<%
			}else if(viewBean.isCountryField((String)hermesKey)){
				String memCode = clearFields?"":request.getParameter((String)hermesKey)!=null?request.getParameter((String)hermesKey):"";
				String nameTag = ("TOLST").concat((String)hermesKey);
				String defaut=clearFields?"100":request.getParameter(nameTag)!=null?request.getParameter(nameTag):"100";		
			%>
					<tr>
						<td width="400">&nbsp;<%=champsListe.get(hermesKey)%></td>
						<td>
							<input type="text" name="<%=hermesKey%>" size="3" maxlength="3" value="<%=memCode%>" onKeyUp="checkNumber(this)" onKeyDown="checkNumber(this)"/>
							<ct:FWListSelectTag name="<%=nameTag%>"
							defaut="<%=defaut%>"
							data="<%=viewBean.getCountries((String)hermesKey)%>"/>
						</td>
					</tr>				
			<%} else if(viewBean.isDateField((String)hermesKey)){
				String defaut=clearFields?"":request.getParameter("TOSTR"+(String)hermesKey)!=null?request.getParameter("TOSTR"+(String)hermesKey):"";
					if(defaut.trim().length()==0) defaut=viewBean.getPresetValue((String)hermesKey);
%>
					<tr>
						<td width="400">&nbsp;<%=champsListe.get(hermesKey)%></td>
						<td><ct:FWCalendarTag
							name='<%=("TOSTR").concat((String)hermesKey)%>' value="<%=defaut%>"
							doClientValidation="CALENDAR" />
							<script>
								document.getElementById("<%=("TOSTR").concat((String)hermesKey)%>").onkeypress=function() {validateCharForDate(this, window.event);}
								
							</script>							
						
						</td>
					</tr>
					
					<%
			} else if(viewBean.isCustomSelectField((String)hermesKey)){
%>
					<tr>
						<td width="400">&nbsp;<%=champsListe.get(hermesKey)%></td>
						<%String defaut=clearFields?"":request.getParameter((String)hermesKey)!=null?request.getParameter((String)hermesKey):"";%>
						<td><ct:FWListSelectTag name="<%=((String)hermesKey)%>"
							defaut="<%=defaut%>"
							data="<%=viewBean.getCustomSelectFieldValues((String)hermesKey,languePage)%>" /></td>
					</tr>
					<%
			} else if(viewBean.isNumeroAVS((String) hermesKey)){
%>
						
							
							
					<tr>
						<td width="400">&nbsp;<%=champsListe.get(hermesKey)%></td>
						<td> <%
							  	String select = servletContext + mainServletPath + "Root/ci_select.jsp";
								String keyName = "TOSTR"+(String)hermesKey;
								String keyValue = "";								
								if(!clearFields){
									if(viewBean.getParamNumeroAvs().equals("")){
										keyValue = request.getParameter((String)keyName)!=null?request.getParameter((String)keyName):"";
									
									} else {
										keyValue = viewBean.getParamNumeroAvs();
										
										%> <input type="hidden" name="referenceExterne" value="<%=viewBean.getParamReferenceExterne()%>"> <%
									}
								}
			 		String onChange = "updateForm(tag);";
			 		
					String nss = "";
					if (isNnssDisplay){
						nss="true";						
					}else{
						nss="false";
					}
					
					if(viewBean.getNumeroAvsNNSS().equals("true")){
						keyValue = keyValue.replaceAll("756.","");
					}
			%> 
<!-- 			 <nss:nssPopup name="<%=keyName%>"  value="<%=HENNSSUtils.convertNegatifToNNSS(keyValue)%>" 
	            	onChange="<%=onChange%>" cssclass="libelle" jspName="<%=select%>"	            	 
	            	avsAutoNbrDigit="11" nssAutoNbrDigit="10"  avsMinNbrDigit="5" nssMinNbrDigit="8" loadValuesFromRequest="false"/> -->
	            	
	            
	           
	            	
	            	 <nss:nssPopup name="<%=keyName%>"  value="<%=HENNSSUtils.convertNegatifToNNSS(keyValue)%>" 
	            	onChange="<%=onChange%>" cssclass="libelle" jspName="<%=select%>" 
	            	avsMinNbrDigit="5" nssMinNbrDigit="8" loadValuesFromRequest="false" newnss="<%=nss%>"/>
	            	
			
	            	
						</td>
					</tr>
					<%
			} else if(viewBean.isCustomField((String) hermesKey)){
				String selected = "";
				if (request.getParameterValues((String) hermesKey) != null) {
					selected = request.getParameterValues((String) hermesKey)[2];
				}
%>
					<tr>
						<td width="400">&nbsp;<%=champsListe.get(hermesKey)%>&nbsp;</td>
						<td><input type="text" name="<%=hermesKey%>" size="4"
							maxlength="4"
							value="<%=clearFields?"":request.getParameter((String)hermesKey)!=null?request.getParameterValues((String)hermesKey)[0]:""%>"
							id="frm1" onkeypress="return filterCharForPositivFloat(window.event);" > <ct:FWLabel key="HERMES_JSP_GAZ0004_A"/> <input type="text" name="<%=hermesKey%>" size="4"
							maxlength="4"
							value="<%=clearFields?"":request.getParameter((String)hermesKey)!=null?request.getParameterValues((String)hermesKey)[1]:""%>"
							id="frm1" onkeypress="return filterCharForPositivFloat(window.event);"> , <ct:FWLabel key="HERMES_JSP_GAZ0004_CHIFFRE_CLEF"/> 
							<select name="<%=(String)hermesKey%>">
									<option <%=selected.equals("")?"selected":""%> value=""></option>
									<option <%=!clearFields&&selected.equals("1")?"selected":""%> value="1">1</option>
									<option <%=!clearFields&&selected.equals("2")?"selected":""%> value="2">2</option>
									<option <%=!clearFields&&selected.equals("3")?"selected":""%> value="3">3</option>
									<option <%=!clearFields&&selected.equals("4")?"selected":""%> value="4">4</option>
									<option <%=!clearFields&&selected.equals("5")?"selected":""%> value="5">5</option>
							</select></td>
					</tr>
					<%
			} else if(viewBean.isReadOnlyField((String) hermesKey)){
%>
					<tr>
						<td width="400">&nbsp;<%=champsListe.get(hermesKey)%></td>
						<td><input tabindex="-1" type="text" name="<%=hermesKey%>"
							size="<%=champsListe.getLongueur(hermesKey)+2%>"
							maxlength="<%=champsListe.getLongueur(hermesKey)%>"
							value="<%=viewBean.getReadOnlyFieldValue((String)hermesKey,HttpUtils.getParamsAsMap(request))%>"
							class="disabled" readonly id="frm1"></td>
					</tr>
					<%			
			}else if(viewBean.isNameField((String) hermesKey)){
%>
					<tr>
						<td width="400">&nbsp;<%=champsListe.get(hermesKey)%></td>
						<td> 
							<input type="text" name="<%=hermesKey%>" 
							value="<%=clearFields?"":(request.getParameter((String)hermesKey)!=null?request.getParameter((String)hermesKey):"")%>" 
							size="<%=champsListe.getLongueur(hermesKey)+10%>" maxlength="<%=champsListe.getLongueur(hermesKey)%>"  
							onKeyUp="checkKey(this)" onKeyDown="checkKey(this)" id="frm1" onblur="changeName(this);nameWithHyphen(this);">
						</td>
					</tr>
					<%			
			}else if(viewBean.isCustomInputField((String) hermesKey)){
				
				if(IHEAnnoncesViewBean.DATE_DEBUT_1ER_DOMICILE_MMAA.equals((String)hermesKey)
					|| IHEAnnoncesViewBean.DATE_DEBUT_2EME_DOMICILE_MMAA.equals((String)hermesKey)
					|| IHEAnnoncesViewBean.DATE_DEBUT_3EME_DOMICILE_MMAA.equals((String)hermesKey)
					|| IHEAnnoncesViewBean.DATE_DEBUT_4EME_DOMICILE_MMAA.equals((String)hermesKey)){
					// début
					%>
					<tr>
						<td width="400">&nbsp;<%=champsListe.get(hermesKey)%></td>
						<td><input type="text" name="<%=hermesKey%>"
							value="<%=clearFields?"":(request.getParameter((String)hermesKey)!=null?request.getParameter((String)hermesKey):"")%>"
							size="<%=champsListe.getLongueur(hermesKey)+2%>"
							maxlength="<%=champsListe.getLongueur(hermesKey)%>" id="frm1"
							onkeypress="javascript:validatePartDateOnKeyPressed(this,window.event)"
							onchange="javascript:validatePartDate(this)">
					<%
				} else if(IHEAnnoncesViewBean.DATE_FIN_1ER_DOMICILE_MMAA.equals((String)hermesKey)
						||IHEAnnoncesViewBean.DATE_FIN_2EME_DOMICILE_MMAA.equals((String)hermesKey)
						||IHEAnnoncesViewBean.DATE_FIN_3EME_DOMICILE_MMAA.equals((String)hermesKey)
						||IHEAnnoncesViewBean.DATE_FIN_4EME_DOMICILE_MMAA.equals((String)hermesKey)){
					// fin
					%>
					
						&nbsp;<%=champsListe.get(hermesKey)%>&nbsp;
						<input type="text" name="<%=hermesKey%>"
							value="<%=clearFields?"":(request.getParameter((String)hermesKey)!=null?request.getParameter((String)hermesKey):"")%>"
							size="<%=champsListe.getLongueur(hermesKey)+2%>"
							maxlength="<%=champsListe.getLongueur(hermesKey)%>" id="frm1"
							onkeypress="javascript:validatePartDateOnKeyPressed(this,window.event)"
							onchange="javascript:validatePartDate(this)">
						</td>							
					</tr>					
					<%
				} else {
					// autre champ, comme date de cloture
					%>
					<tr>
						<td width="400">&nbsp;<%=champsListe.get(hermesKey)%></td>
						<td><input type="text" name="<%=hermesKey%>"
							value="<%=clearFields?"":(request.getParameter((String)hermesKey)!=null?request.getParameter((String)hermesKey):"")%>"
							size="<%=champsListe.getLongueur(hermesKey)+2%>"
							maxlength="<%=champsListe.getLongueur(hermesKey)%>" id="frm1"
							onkeypress="javascript:validatePartDateOnKeyPressed(this,window.event)"></td>							
					</tr>					
					<%
				}	
			}else if(viewBean.isReferenceInterne((String) hermesKey)){
				if("true".equals(viewBean.getSession().getApplication().getProperty("service.input"))){
					String[] ref = request.getParameterValues((String) hermesKey);
					String ref1 = "";
					String ref2 = "";
					if(ref!=null){
						if(ref.length==1){
							if(ref[0].lastIndexOf("/")==-1){
								ref1=ref[0];
							}
							else{
								ref1 = ref[0].substring(0,ref[0].lastIndexOf("/"));
								ref2 = ref[0].substring(ref[0].lastIndexOf("/")+1,ref[0].length());
							}
						} else {
							ref1 = ref[0];
							ref2 = ref[1];						
						}
						session.setAttribute("hermesARCRI1", ref1);
						session.setAttribute("hermesARCRI2", ref2);
					}else {
					    if (null!=session.getAttribute("hermesARCRI1"))
					    ref1 = (String) session.getAttribute("hermesARCRI1");
					    if (null!=session.getAttribute("hermesARCRI2"))
					    ref2 = (String) session.getAttribute("hermesARCRI2");
					}
					
					%>
					<tr>
						<td width="400">&nbsp;<%=champsListe.get(hermesKey)%></td>
						<td>
							<input type="text" name="<%=hermesKey%>" value="<%=ref1%>" size="4" maxlength="4" id="frm1" onchange="changeName(this)" onkeydown="checkChar(this)">
							-
							<input type="text" name="" value="/" size="1" maxlength="1" id="frm1" class="disabled" readonly tabindex="-1">
							-
							<input type="text" name="<%=hermesKey%>" value="<%=ref2%>" size="18" maxlength="16" id="frm1" onchange="changeName(this)" onkeydown="checkChar(this)">
						</td>
					</tr>
				<%}else{%>
					<tr>
						<td width="400">&nbsp;<%=champsListe.get(hermesKey)%></td>
						<td>
							<input name="<%=hermesKey%>" type="text"
								size="<%=champsListe.getLongueur(hermesKey)+2%>"
								value="<%=request.getParameter((String)hermesKey)!=null?request.getParameter((String)hermesKey):viewBean.getPresetValue((String)hermesKey)%>"
								maxlength="<%=champsListe.getLongueur(hermesKey)%>" id="frm1" onKeyUp="checkChar(this)" onKeyDown="checkChar(this)" onchange="changeName(this)">
						</td>
					</tr>
				<%}		
			}else if(((String)hermesKey).equals(IHEAnnoncesViewBean.MOTIF_ANNONCE)){				
			if("97".equals(viewBean.getReadOnlyFieldValue((String)hermesKey,HttpUtils.getParamsAsMap(request))) && "true".equals(viewBean.getSession().getApplication().getProperty("adresse.input"))){%>
				<tr>
					<td width="400" valign="top">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0004_TITRE_PERSONNE"/>&nbsp;:&nbsp;</td>
					<td> 
						<%String titre = clearFields||globaz.globall.util.JAUtil.isStringEmpty(request.getParameter("titreAssure"))?"":request.getParameter("titreAssure");%>
						<ct:FWListSelectTag name="titreAssure"
							defaut="<%=titre%>" 
							data="<%=viewBean.getListeTitre()%>"/>
					</td>
				</tr>
				
				<tr id="wantPolitesse">
					<td width="400" valign="top">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0004_FORMULE_DE_POLITESSE"/></td>
					<td>
						<input name="formulePolitesse" id="formulePolitesse" size="39" type="text" value="<%=formulePolitesse %>" maxlength="40"/> 
					</td>
				</tr>
				
				<tr>
					<td width="400" valign="top">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0004_NOM_ET_ADRESSE_ASSURE"/></td>
					<td>
						<TEXTAREA name="adresseAssure" rows="4" cols="40" id="frm1"><%=clearFields||JAUtil.isStringEmpty(request.getParameter("adresseAssure"))?"":request.getParameter("adresseAssure")%></TEXTAREA>
					</td>
				</tr>
				<tr>
					<td width="400" valign="top">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0004_LANGUE_CORRESPONDANCE"/></td>
					<td>
						<%
						String langue = clearFields||JadeStringUtil.isEmpty(request.getParameter("langueCorrespondance"))?TITiersViewBean.CS_FRANCAIS:request.getParameter("langueCorrespondance");
						HashSet except = new HashSet();
						except.add("503003");
						except.add("503005");
						except.add("503006");
						except.add("503007");
						%>
						<ct:FWCodeSelectTag codeType="PYLANGUE" name="langueCorrespondance" defaut="<%=langue%>" wantBlank="false" except="<%=except%>"/>
					</td>
				</tr>				
				<%}%>
				<tr>
					<td>
						<input tabindex="-1" type="hidden" name="<%=hermesKey%>"
							value="<%=viewBean.getReadOnlyFieldValue((String)hermesKey,HttpUtils.getParamsAsMap(request))%>"
							class="disabled" readonly id="frm1">
					</td>
				</tr>
		<%}else {%>
				<tr>
					<td width="400">&nbsp;<%=champsListe.get(hermesKey)%></td>
					<td><input name="<%=hermesKey%>" type="text"
						size="<%=champsListe.getLongueur(hermesKey)+2%>"
						value="<%=clearFields?"":request.getParameter((String)hermesKey)!=null?request.getParameter((String)hermesKey):viewBean.getPresetValue((String)hermesKey)%>"
						maxlength="<%=champsListe.getLongueur(hermesKey)%>" id="frm1" onKeyUp="checkChar(this)" onKeyDown="checkChar(this)" onchange="changeName(this);upperCase(this);"></td>
				</tr>
		<%}
		}
		/* numéro d'affilié pour le CA ou pour la déclaration de salaire */			
		if(
			(
			globaz.hermes.db.gestion.HEAnnoncesViewBean.isMotifCA(viewBean.getReadOnlyFieldValue(IHEAnnoncesViewBean.MOTIF_ANNONCE,globaz.hermes.utils.HttpUtils.getParamsAsMap(request)))
			||
			globaz.hermes.db.gestion.HEAnnoncesViewBean.isMotifForDeclSalaire(viewBean.getReadOnlyFieldValue(IHEAnnoncesViewBean.MOTIF_ANNONCE,globaz.hermes.utils.HttpUtils.getParamsAsMap(request)))
			|| isMotifCert
			) 
			&& "true".equals(viewBean.getSession().getApplication().getProperty("affilie.input")))
			{
			
		
			//if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isMotifAttestCA(motif) && isDateNSS) {
			//	if(viewBean.isMotifAttestCHECK(motif) && isDateNSS) {				
			if(isMotifCert && isDateNSS)
			{
				String defaultcat ="115002";
				if (clearFields){ 
					defaultcat = "115002";
				}
				
				if(!JadeStringUtil.isBlankOrZero(viewBean.getCategorie())) {
					defaultcat = viewBean.getCategorie();
				}
		%>
			<tr>
				<td width="400">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0004_CATEGORIE"/> </td>
				<td>
	
				<ct:FWCodeSelectTag name="categorie" defaut="<%=defaultcat%>" codeType="HECATEGOR" wantBlank="false"/>
						<script type="text/javascript">
						document.getElementById("categorie").onclick  = catscript;
						document.getElementById("categorie").onclick  = updateJspNameCat;
						document.getElementById("categorie").onchange = updFieldsBoundedToCategorie;
												
						</script>
				</td>
			</tr>
			<%} %>
		
			<tr> 
				<td width="400">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0004_NUMERO_AFFILIE"/></td>
				<td>
				
				<%String jspLocation = servletContext + mainServletPath + "Root/ti_select.jsp";%>
				
				<%					
					String defaultNumeroAffilie = JAUtil.isStringEmpty(request.getParameter("numeroAffilie"))?"":request.getParameter("numeroAffilie");				 
					String defaultNomAffilie = JAUtil.isStringEmpty(request.getParameter("nomAffilie"))?"":request.getParameter("nomAffilie");
					String defaultNumeroSucc = JadeStringUtil.isEmpty(viewBean.getNumeroSuccursale())?"":viewBean.getNumeroSuccursale();
					String defaultNumeroEmpl = JadeStringUtil.isEmpty(viewBean.getNumeroEmploye())?"":viewBean.getNumeroEmploye();
					String defaultLangue = JadeStringUtil.isEmpty(request.getParameter("langueCorrespondance"))?"":request.getParameter("langueCorrespondance");
					String chkMemoriserNumeroAffilieParaValue = JadeStringUtil.isBlankOrZero(request.getParameter("chkMemoriserNumeroAffilie"))?"":request.getParameter("chkMemoriserNumeroAffilie");  
					
					
					if(JadeStringUtil.isBlankOrZero(defaultNumeroAffilie)){
						defaultNomAffilie = "";
						chkMemoriserNumeroAffilieParaValue = "";
						defaultLangue = "";
					}
					
					if (clearFields) {
						if(!"on".equalsIgnoreCase(chkMemoriserNumeroAffilieParaValue)){
							defaultNumeroAffilie = "";
							defaultNomAffilie = "";
							defaultLangue = "";
						}
						defaultNumeroSucc = "";
						defaultNumeroEmpl = "";
					}
					int autoDigit = HEUtil.getAutoDigit(viewBean.getSession());
				%>
				
				<ct:FWPopupList onChange="updateEmployeurPartenaire(tag);" 
					onFailure="setNotFound(tag);" name="numeroAffilie"  
					size="15" 
					jspName="<%=jspLocation%>" minNbrDigit="3" className="disabled"
					autoNbrDigit="<%=autoDigit%>" forceSelection="true" value="<%=defaultNumeroAffilie%>"/>
					<script>						
						document.getElementById("numeroAffilie").onkeypress=function() {validateCharForDate(this, window.event);}
						
						document.getElementById("numeroAffilie").onfocus =	updateJspNameCat;					
					</script>						
					<input type="text" name="nomAffilie" size="36" class="disabled" readonly value="<%=defaultNomAffilie%>" tabIndex="-1">					
					&nbsp;&nbsp;<input type="checkbox" id="chkMemoriserNumeroAffilie" name="chkMemoriserNumeroAffilie" <%="on".equalsIgnoreCase(chkMemoriserNumeroAffilieParaValue)?"checked":""%> >&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0004_MEMORISER"/>				
				</td>
			</tr>
			<tr>
				<td width="400" valign="top">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0004_LANGUE_CORRESP_DEFAUT_AFF"/></td>
					<td>
						<%
						HashSet except = new HashSet();
						except.add("503003");
						except.add("503005");
						except.add("503006");
						except.add("503007");
						%>
						<ct:FWCodeSelectTag codeType="PYLANGUE" name="langueCorrespondance" defaut="<%=defaultLangue%>" wantBlank="false" except="<%=except%>"/>
						
				</td>
			</tr>	
			<tr>
				<td width="400">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0004_NUMERO_SUCCURSALE_DEPARTEMENT"/>&nbsp;:&nbsp;</td>
				<td> 
        			<input type="text" size="15" value="<%=defaultNumeroSucc%>" name="numeroSuccursale" tabindex="-1"/>
				</td>
			</tr>
			<tr>
				<td width="400">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0004_NUMERO_EMPLOYE"/>&nbsp;:&nbsp;</td>
				<td> 
        			<input type="text" size="15" value="<%=defaultNumeroEmpl%>" name="numeroEmploye" tabindex="-1"/>
				</td>
			</tr>
			
		<%}
		if(HEAnnoncesViewBean.isMotifForDeclSalaire(viewBean.getReadOnlyFieldValue(IHEAnnoncesViewBean.MOTIF_ANNONCE,HttpUtils.getParamsAsMap(request))) ||isMotifCert  )
		{
			String defaut = JadeStringUtil.isEmpty(viewBean.getDateEngagement())?"":viewBean.getDateEngagement();
			
			if (clearFields) defaut = "";
		%>
		<tr>
			<td width="400">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0004_DATE_ENGAGEMENT"/></td>		<td>
				<ct:FWCalendarTag name="dateEngagement" value="<%=defaut%>" doClientValidation="CALENDAR" />
				<script>		
						document.getElementById("dateEngagement").onkeypress=function() {validateCharForDate(this, window.event);}
						document.getElementById("dateEngagement").onchange=function() {validateCharForDate(this, window.event);}
				</script>	
			</td>
		</tr>
		
		
		
		<%}%>
		
		<%		
		
				//if(globaz.hermes.db.gestion.HEAnnoncesViewBean.isMotifAttestCA(motif)&& isDateNSS) {
					//if(viewBean.isMotifAttestCHECK(motif) && isDateNSS) {
						if(isMotifCert && isDateNSS) {
					
			%>	
			<tr>
					<td width="400" valign="top">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0004_TITRE"/>&nbsp;&nbsp;</td>
					<td>
					<%String titre = JadeStringUtil.isEmpty(request.getParameter("titreRentier"))?"":request.getParameter("titreRentier");
						if (clearFields) titre = "";						
						%>
						<ct:FWListSelectTag name="titreRentier"
							defaut="<%=titre%>" 
							data="<%=viewBean.getListeTitre()%>"/>
					</td>
				</tr>
				<tr id="wantPolitesse">
					<td width="400" valign="top">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0004_FORMULE_DE_POLITESSE"/></td>
					<td>
						<input name="formulePolitesse" id="formulePolitesse" size="39" type="text" value="<%=formulePolitesse%>" maxlength="40"/> 
					</td>
				</tr>
					
			<tr>
				
				<td width="400">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0004_NOM_ET_ADRESSE"/></td>
				<%
				
				String adr = JadeStringUtil.isEmpty(request.getParameter("adresseRentier"))?"":request.getParameter("adresseRentier");
				if (clearFields) adr = "";
				%>
				
				<td>
					<textarea disabled="true" name="adresseRentier" id="txtarea" rows="4" cols="40" class="forceDisable"  disabled="true" style=" background-color : #b3c4db;"><%=adr%></textarea> 
				</td>
			</tr>
			<% // Ne concerne que les motifs 11 
			if("11".equals(motif)){ %>
			<tr>
				<td width="400">&nbsp;<ct:FWLabel key="HERMES_JSP_GAZ0004_AJOUT_ARC_61"/></td>
				<td>
					<INPUT type="checkbox" value="on" id="idChkCreerArc61" name="chkCreerArc61" <%=viewBean.getChkCreerArc61().booleanValue()?"CHECKED":""%>>&nbsp;
				</td>
			</tr>
			
			<% }%>
			
			
			</tr>
			<%} %>
		
		<TR>
			<TD>
		<%
		for(int i=0; i < champsListe.paramAnnonceSize();i++){%>
			<input type="hidden" name="paramannonce" value="<%=champsListe.getParamAnnonce(i)%>">
		<%}%>
		</TD>
		</TR>
		<tr>
			<td>
				<input type="hidden" name="118001" value="11"> 
				<input type="hidden" name="118002" value="01">
				<input type="hidden" name="critere" value="<%=request.getParameter("critere")%>">
				<input type="hidden" name="motif" value="<%=request.getParameter("motif")%>">					
				<input type="hidden" name="modeSaisie" value="true">
				<input type="hidden" name="isFieldEmpty" value="true">
				<input type="hidden" name="numAVS" value="<%=request.getParameter("numAVS")%>">
			</td>
		</tr>
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<script>
	// on ne veut pas afficher le message d'erreur dans une boîte de dialogue
	// on veut rester dans la ligne du template
	errorObj.text = "";
		
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>