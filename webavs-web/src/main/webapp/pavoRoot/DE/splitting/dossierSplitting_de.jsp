<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ page import="globaz.pavo.db.splitting.*,globaz.globall.util.*"%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran = "CCI0016";
    CIDossierSplittingViewBean viewBean = (CIDossierSplittingViewBean)session.getAttribute ("viewBean");
	// feuille de style � utiliser selon �tat
	String styleOuverture = "";
	String styleOuvRev = "";
	String cbOuvRev = "";
	String isCaEnabled ="";
	String styleDateMariageDateDivorce = "";
	
	if(!viewBean.CS_SAISIE_DOSSIER.equals(viewBean.getIdEtat()) && !viewBean.CS_OUVERT.equals(viewBean.getIdEtat()) && !viewBean.CS_REVOQUE.equals(viewBean.getIdEtat())) {
		styleDateMariageDateDivorce = "class='disabled' readonly tabIndex='-1'";
    }

	if(!viewBean.CS_SAISIE_DOSSIER.equals(viewBean.getIdEtat())) {
		styleOuverture = "class='disabled' readonly tabIndex='-1'";
    }
	if(!viewBean.CS_SAISIE_DOSSIER.equals(viewBean.getIdEtat())&&!viewBean.CS_REVOQUE.equals(viewBean.getIdEtat())) {
		styleOuvRev = "class='disabled' readonly tabIndex='-1'";
		cbOuvRev = "disabled readonly tabIndex='-1'";
    }
    if(!viewBean.canDemandeCA()){
	    isCaEnabled ="disabled readonly tabIndex='-1'";
    }
	selectedIdValue = viewBean.getIdDossierSplitting();
	userActionValue = "pavo.splitting.dossierSplitting.modifier";
	String jspLocation = servletContext + mainServletPath + "Root/ci_select.jsp";
	bButtonDelete = viewBean.canDelete();
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/avsParser.js"></SCRIPT>
<SCRIPT language="JavaScript">
top.document.title = "Splitting - Detail des Dossiers"
	function changeName(input)
{
	input.value=input.value.replace('�','AE');
	input.value=input.value.replace('�','OE');
	input.value=input.value.replace('�','UE');
	input.value=input.value.replace('�','AE');
	input.value=input.value.replace('�','OE');
	input.value=input.value.replace('�','UE');	
	
	input.value=input.value.replace('�','E');
	input.value=input.value.replace('�','E');
	input.value=input.value.replace('�','O');
	input.value=input.value.replace('�','A');		
	
	input.value=input.value.toUpperCase();
}

function checkKey(input){
	var re = /[^a-zA-Z\-'����������,\s].*/	
	if (re.test(input.value)) {
		input.value = input.value.substr(0,input.value.length-1);
	}
}

function toUpperCase(tagName){
	var mySt = document.forms[0].elements(tagName).value;
	document.forms[0].elements(tagName).value = mySt.toUpperCase();
}
function updateFormAssure(tag){
	if (tag.select && tag.select.selectedIndex != -1) {
		document.forms[0].elements('tiersAssureInfo').value = tag.select[tag.select.selectedIndex].nom;
		document.forms[0].elements('tiersAssureInfoNom').value = tag.select[tag.select.selectedIndex].nom;
		document.forms[0].elements('dateNaissanceAss').value= tag.select[tag.select.selectedIndex].date;
		document.forms[0].elements('paysAss').value = tag.select[tag.select.selectedIndex].paysLibelle;
		document.forms[0].elements('sexeAss').value = tag.select[tag.select.selectedIndex].sexeFormate;
	}
}
function updateFormConj(tag){
	if (tag.select && tag.select.selectedIndex != -1) {
		document.forms[0].elements('tiersConjointInfo').value = tag.select[tag.select.selectedIndex].nom;
		document.forms[0].elements('tiersExConjointInfoNom').value = tag.select[tag.select.selectedIndex].nom;
		document.forms[0].elements('dateNaissanceCon').value= tag.select[tag.select.selectedIndex].date;
		document.forms[0].elements('paysCon').value = tag.select[tag.select.selectedIndex].paysLibelle;
		document.forms[0].elements('sexeCon').value = tag.select[tag.select.selectedIndex].sexeFormate;
	}
}
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="pavo.splitting.dossierSplitting.ajouter"
}
function upd() {
}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pavo.splitting.dossierSplitting.ajouter";
    else
        document.forms[0].elements('userAction').value="pavo.splitting.dossierSplitting.modifier";
    
    return state;

}
function cancel() {
if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
else
  document.forms[0].elements('userAction').value="pavo.splitting.dossierSplitting.afficher";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgew�hlte Objekt zu l�schen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="pavo.splitting.dossierSplitting.supprimer";
        document.forms[0].submit();
    }
}
function updateHiddenCaConjoint(){

	if(document.getElementById('caRequisConjoint').checked){
		document.getElementById('caRequisConjointStr').value = "True";
	}else{
		document.getElementById('caRequisConjointStr').value = "False";
	}
	
}
function updateHiddenCa(){

	if(document.getElementById('caRequisAssure').checked){
		document.getElementById('caRequisAssureStr').value = "True";
	}else{
		document.getElementById('caRequisAssureStr').value = "False";
	}
	
}
function updateHiddenCiConjoint(){

	if(document.getElementById('extraitCiRequisConjoint').checked){
		document.getElementById('extraitCiRequisConjointStr').value = "True";
	}else{
		document.getElementById('extraitCiRequisConjointStr').value = "False";
	}
	
}
function updateHiddenCi(){

	if(document.getElementById('extraitCiRequisAssure').checked){
		document.getElementById('extraitCiRequisAssureStr').value = "True";
	}else{
		document.getElementById('extraitCiRequisAssureStr').value = "False";
	}
	
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
	// on ne formate pas le num�ro avs quand on presse la touche delete ou backspace
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
  // tester si le num�ro avs entr� comporte slt des num�ros et/ou des .
  var cre = /((\d|\.)+)/;
  if (!cre.test(valueToTrim)) {
	valueToTrim = "";
  }
  return valueToTrim;
}
function init(){}
var calendar = new CalendarPopup(); 
			calendar.showYearNavigation();   
			calendar.setMonthNames('Janvier','F�vrier','mars','Avril','Mai','Juin','Juillet','Ao�t','Septembre','Octobre','Novembre','Decembre');
			calendar.setDayHeaders('D','L','M','M','J','V','S');
			calendar.setWeekStartDay(1); 
			calendar.setTodayText("Aujourd'hui");
			
function resetForm(fieldName) {
 	document.getElementById(fieldName).value = '';
}
function resetFormAssure(){
	resetForm('tiersAssureInfo');
	resetForm('tiersAssureInfoNom');
	resetForm('dateNaissanceAss');
	resetForm('sexeAss');
	resetForm('paysAss');
}
function resetFormConj(){
	resetForm('tiersConjointInfo');
	resetForm('tiersExConjointInfoNom');
	resetForm('dateNaissanceCon');
	resetForm('paysCon');
	resetForm('sexeCon');
}
-->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail des Splittingdossiers<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <tr> 
            <td width="240">Dossier-Nr.</td>
            <td> 
              <input type="text" name="idDossierSplitting" readonly tabIndex='-1' size="10" value="<%=viewBean.getIdDossierSplitting()%>" class="disabled">
            </td>
            <td>Interne-Nr.</td>
            <td colspan="3"> 
              <input type="text" name="idDossierInterne" size="15" value="<%=viewBean.getIdDossierInterne()%>">
            </td>
          </tr>
          <tr> 
            <td width="240">Versicherte</td>
            <td >
			<% if(viewBean.CS_SAISIE_DOSSIER.equals(viewBean.getIdEtat())) { %>
			  <nss:nssPopup onChange="updateFormAssure(tag);" 
			  name="idTiersAssure" 
			  value='<%=viewBean.getNssWithoutPrefixeAssure()%>' jspName="<%=jspLocation%>" avsMinNbrDigit="8" avsAutoNbrDigit="11" 
			  nssMinNbrDigit="7" nssAutoNbrDigit="10"
			  newnss="<%=viewBean.getIdTiersAssureNNSS()%>" 
			  onFailure="resetFormAssure();" />
			  <% } else { %>
              <input type="text" name="idTiersAssure" size="17" readonly class="disabled" tabIndex='-1' size="15" 
              value='<%=!JAUtil.isStringEmpty(viewBean.getIdTiersAssure())?globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getIdTiersAssure()):""%>'>
			  <% } %>
                <% if(viewBean.hasLienGedAssure()){
                    String urlGED = servletContext + "/pavo?"
                            + "userAction=pavo.splitting.dossierSplitting.actionAfficherDossierGed"
                            + "&noAVSId=" + viewBean.getIdTiersAssure()
                            + "&idTiersExtraFolder=" + viewBean.getIdTiersInterneAssure()
                            + "&serviceNameId=" + viewBean.getGedServiceName();
                %>
                <a href="#" onclick="window.open('<%=urlGED%>','GED_CONSULT')">
                    <ct:FWLabel key="JSP_LIEN_GED"/>
                </a>
                <%}%>
            </td>
            <td colspan="4"> 
              <input type="text" id="tiersAssureInfoNom" name="tiersAssureInfoNom" readonly tabIndex='-1' size="70" value="<%=viewBean.getTiersAssureNomComplet()%>" class="disabled">
            </td>
          <tr>
          	<td>Andere</td>
          	<td colspan="5">
          		<ct:FWCodeSelectTag name="titreAssure" wantBlank="false" except="<%=viewBean.giveListTitreToExcept()%>" defaut="<%=viewBean.getTitreAssure()%>" codeType="PYTITRE"/>
			</td>
          </tr>
          <tr>
          	<td>Adresse</td>
          	<td colspan="5">
          		<TEXTAREA id="adresseAssure" name="adresseAssure" rows="4" cols="40" ><%=viewBean.getAdresseAssure()%></TEXTAREA>
			</td>
          </tr>
          <tr>
          	<td>Sprache</td>
          	<td colspan="5">
          		<ct:FWCodeSelectTag name="langueAssure" wantBlank="false" except="<%=viewBean.giveListLangueToExcept()%>" defaut="<%=viewBean.getLangueAssure()%>" codeType="PYLANGUE"/>
			</td>
          </tr>
          <tr> 
            <td></td>
            <td colspan="5"> 
              <textarea style="display:none;" name="tiersAssureInfo" class="disabled" readonly tabIndex='-1' rows="4" cols="40"><%=viewBean.getTiersAssureLocalite()%>
		      </textarea>
            </td>
          </tr>
			<tr>
				<td>
					Geburtsdatum&nbsp;
				</td>
				<td colspan="5">
					<input type="text" size = "10" class='disabled' name="dateNaissanceAss" readonly tabindex='-1' value = "<%=viewBean.getDateNaissanceAss()%>">
					&nbsp;
					Geschl. &nbsp;
					<input type="text" size = "7" class='disabled' name="sexeAss" readonly tabindex='-1' value = "<%=viewBean.getSexeLibelleAss()%>">
					Heim. &nbsp;
					<input type="text" size = "55" class='disabled' name="paysAss" readonly tabindex='-1' value = "<%=viewBean.getPaysFormateAss()%>">
				</td>
					
			</tr>          
          <tr> 
            <td width="240">Ehepartner</td>
            <td >
			  <% if(viewBean.CS_SAISIE_DOSSIER.equals(viewBean.getIdEtat())) { %>
			  <nss:nssPopup onChange="updateFormConj(tag);" 
			  name="idTiersConjoint" value='<%=!JAUtil.isStringEmpty(viewBean.getIdTiersConjoint())?viewBean.getNssWithoutPrefixeConjoint():""%>' 
			  jspName="<%=jspLocation%>" avsMinNbrDigit="8" avsAutoNbrDigit="11" 
			  nssMinNbrDigit="7" nssAutoNbrDigit="10"
			  newnss="<%=viewBean.getIdTiersConjointNNSS()%>" 
			  onFailure="resetFormConj();"/>
			  <% } else { %>
              <input type="text" size="17" name="idTiersConjoint" readonly class="disabled" tabIndex='-1' size="15" value='<%=!JAUtil.isStringEmpty(viewBean.getIdTiersConjoint())?globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getIdTiersConjoint()):""%>'>
			  <% } %>
                <% if(viewBean.hasLienGedConjoint()){
                    String urlGED = servletContext + "/pavo?"
                            + "userAction=pavo.splitting.dossierSplitting.actionAfficherDossierGed"
                            + "&noAVSId=" + viewBean.getIdTiersConjoint()
                            + "&idTiersExtraFolder=" + viewBean.getIdTiersInterneConjoint()
                            + "&serviceNameId=" + viewBean.getGedServiceName();
                %>
                <a href="#" onclick="window.open('<%=urlGED%>','GED_CONSULT')">
                    <ct:FWLabel key="JSP_LIEN_GED"/>
                </a>
                <%}%>
            </td>
            <td colspan="4"> 
              <input type="text" id="tiersExConjointInfoNom" name="tiersExConjointInfoNom" readonly tabIndex='-1' size="70" value="<%=viewBean.getTiersConjointNomComplet()%>" class="disabled">
            </td>
          </tr>
          <tr>
          	<td>Anrede</td>
          	<td colspan="5">
          		<ct:FWCodeSelectTag name="titreExConjoint" wantBlank="false" except="<%=viewBean.giveListTitreToExcept()%>" defaut="<%=viewBean.getTitreExConjoint()%>" codeType="PYTITRE"/>
			</td>
          </tr>
          <tr>
          	<td>Adresse</td>
          	<td colspan="5">
          		<TEXTAREA id="adresseExConjoint" name="adresseExConjoint" rows="4" cols="40" ><%=viewBean.getAdresseExConjoint()%></TEXTAREA>
			</td>
          </tr>
          <tr>
          	<td>Sprache</td>
          	<td colspan="5">
          		<ct:FWCodeSelectTag name="langueExConjoint" wantBlank="false" except="<%=viewBean.giveListLangueToExcept()%>" defaut="<%=viewBean.getLangueExConjoint()%>" codeType="PYLANGUE"/>
			</td>
          </tr>
          <tr> 
            <td></td>
            <td colspan="5"> 
              <textarea style="display:none;" name="tiersConjointInfo" readonly class="disabled" tabIndex='-1' rows="4" cols="40"><%=viewBean.getTiersConjointLocalite()%>
		      </textarea>
            </td>
          </tr>
          			<tr>
				<td>
					Geburtsdatum&nbsp;
				</td>
				<td colspan="5">
					<input type="text" size = "10" class='disabled' name="dateNaissanceCon" readonly tabindex='-1' value = "<%=viewBean.getDateNaissanceConj()%>">
					&nbsp;
					Geschl. &nbsp;
					<input type="text" size = "7" class='disabled' name="sexeCon" readonly tabindex='-1' value = "<%=viewBean.getSexeLibelleConj()%>">
					Heim. &nbsp;
					<input type="text" size = "55" class='disabled' name="paysCon" readonly tabindex='-1' value = "<%=viewBean.getPaysFormateConj()%>">
				</td>
					
			</tr> 
          <tr> 
            <td width="240">Er�ffnungsdatum des Dossiers</td>
            <td colspan="5"> 
              <input type="text" name="dateOuvertureDossier" size="10" maxlength="10" value="<%=viewBean.getDateOuvertureDossier()%>" <%=styleOuverture%>>
              <% if(JAUtil.isStringEmpty(styleOuvRev)) { %>
              <input value="..." type=button name="anchor_dateOuvertureDossier" id="anchor_dateOuvertureDossier" onClick="calendar.select(dateOuvertureDossier,'anchor_dateOuvertureDossier','dd.MM.yyyy'); return false;"  onBlur="fieldFormat(dateOuvertureDossier,'CALENDAR')" >
              <% } %>
            </td>
          </tr>
          <tr> 
            <td width="240">Heiratsdatum</td>
            <td colspan="5"> 
              <input type="text" name="dateMariage" size="10" maxlength="10" value="<%=viewBean.getDateMariage()%>" <%=styleDateMariageDateDivorce%>>
              <% if(JAUtil.isStringEmpty(styleDateMariageDateDivorce)) { %>
              <input value="..." type=button name="anchor_dateMariage" id="anchor_dateMariage" onClick="calendar.select(dateMariage,'anchor_dateMariage','dd.MM.yyyy'); return false;"  onBlur="fieldFormat(dateMariage,'CALENDAR')" >
              <% } %>
            </td>
          </tr>
          <tr> 
            <td width="240">Scheidungsdatum</td>
            <td colspan="5"> 
              <input type="text" name="dateDivorce" size="10" maxlength="10" value="<%=viewBean.getDateDivorce()%>" <%=styleDateMariageDateDivorce%>>
              <% if(JAUtil.isStringEmpty(styleDateMariageDateDivorce)) { %>
              <input value="..." type=button name="anchor_dateDivorce" id="anchor_dateDivorce" onClick="calendar.select(dateDivorce,'anchor_dateDivorce','dd.MM.yyyy'); return false;"  onBlur="fieldFormat(dateDivorce,'CALENDAR')" >
              <% } %>
            </td>
          </tr>
          <tr> 
            <td width="240">Splittinggrund</td>
            <td colspan="5"><ct:FWCodeSelectTag name="idMotifSplitting"
		defaut="<%=viewBean.getIdMotifSplitting()%>"
		codeType="CIMOTSPL" wantBlank="false"/> 
              <!-- 
              <select name="idMotifSplitting">
              <option value="308002">Rente AI</option>
              <option value="308001">Rente AVS</option>
              <option value="308003" selected>Demande propre caisse</option>
              <option value="308004">Demande d'une autre caisse</option>
              </select>-->
            </td>
          </tr>
          <tr> 
            <td width="240">IK-Auszug ben�tigt</td>
            <td> 
              <input type="checkbox" onclick="updateHiddenCi();" name="extraitCiRequisAssure" <%=(viewBean.isExtraitCiRequisAssure().booleanValue())? "checked" : "unchecked"%> <%=cbOuvRev%>>
              <input type="hidden" onclick="updateHiddenCi();" name="extraitCiRequisAssureStr" value="<%=(viewBean.isExtraitCiRequisAssure().booleanValue())? "True" : "False"%>">
              f�r den Versicherte </td>
            <td colspan="4"> 
              <input type="checkbox" onclick="updateHiddenCiConjoint();" name="extraitCiRequisConjoint" <%=(viewBean.isExtraitCiRequisConjoint().booleanValue())? "checked" : "unchecked"%> <%=cbOuvRev%>>
              <input type="hidden" name="extraitCiRequisConjointStr" value="<%=(viewBean.isExtraitCiRequisConjoint().booleanValue())? "True" : "False"%>">
              f�r den Ehegatte </td>
          </tr>
          <tr> 
            <td width="240">Empfangsbest�tigung</td>
            <td colspan="5">
            	 <input type="checkbox"  id="chkAccuseReceptionAssure" name="chkAccuseReceptionAssure" <%=viewBean.getChkAccuseReceptionAssure().booleanValue()?"checked":"unchecked"%> > f�r den Versicherte
            </td>
          </tr>
          <tr> 
            <td width="240">Einladung am Ehegatten am Splitting teilnehmen</td>
            <td colspan="4">
            	 <input type="checkbox"  id="chkInvitationExConjoint" name="chkInvitationExConjoint" <%=viewBean.getChkInvitationExConjoint().booleanValue()?"checked":"unchecked"%> >
            </td>
          </tr>
          <!-- tr> 
            <td width="240">Taxation cot.pers.d&eacute;finitive</td>
            <td colspan="2"> 
              <input type="checkbox" name="estTaxationDefinitive" <%=(viewBean.isEstTaxationDefinitive().booleanValue())? "checked" : "unchecked"%> <%=cbOuvRev%>>
            </td>
          </tr-->
          <tr> 
            <td width="240">Dossier Verantwortlicher</td>
            <td colspan="5"> 
              <input type="text" name="responsableDossier" size="10" maxlength="10" value="<%=viewBean.getResponsableDossier()%>">
            </td>
          </tr>
               <tr> 
            <td width="240">Dienst Hinweis</td>
            <td colspan="5"> 
              <input type="text" name="referenceService" size="4" onKeyDown="checkKey(this)" onChange="changeName(this)" onKeyUp="checkKey(this)" maxlength="4" value="<%=viewBean.getReferenceService()%>">
            </td>
          </tr>
          <tr> 
            <td width="240" valign="top">Bemerkung</td>
            <td colspan="5" rowspan="2"> 
              <textarea name="remarqueDossier" rows="3" cols="40"><%=viewBean.getRemarqueDossier()%></textarea>
            </td>
          </tr>
          <tr> 
            <td width="240">&nbsp;</td>
            <td colspan="5">&nbsp;</td>
          </tr>
          <tr> 
            <td width="240">Status</td>
            <td colspan="5"> 
              <input type="text" name="idEtatDummy" maxlength="40" size="40" value="<%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getIdEtat(),session)%>" readonly class="disabled" tabIndex='-1'>
              <input type="hidden" name="idEtat" value="<%=viewBean.getIdEtat()%>">
            </td>
          </tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<% if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%  }  %>

<%
    String menuId = "dossierSplitting-detail";
    if(viewBean.hasLienGedAssure()) {
        menuId = "dossierSplitting-detail-ged";
    }
%>
<ct:menuChange displayId="options" menuId="<%=menuId%>" showTab="options">
    <ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdDossierSplitting()%>"/>
    <ct:menuSetAllParams key="idDossierSplitting" value="<%=viewBean.getIdDossierSplitting()%>"/>
    <ct:menuSetAllParams key="idTiersAssure" value="<%=viewBean.getIdTiersAssure()%>"/>
    <ct:menuSetAllParams key="idTiersConjoint" value="<%=viewBean.getIdTiersConjoint()%>"/>
    <% if(viewBean.hasLienGedAssure()) { %>
        <ct:menuSetAllParams key="noAVSId" value="<%=viewBean.getIdTiersAssure()%>"/>
        <ct:menuSetAllParams key="idTiersExtraFolder" value="<%=viewBean.getIdTiersInterneAssure()%>"/>
        <ct:menuSetAllParams key="serviceNameId" value="<%=viewBean.getGedServiceName()%>"/>
    <% } %>
	</ct:menuChange>


<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>