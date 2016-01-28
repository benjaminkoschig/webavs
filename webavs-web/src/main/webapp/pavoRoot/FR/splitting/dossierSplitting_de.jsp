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
	// feuille de style à utiliser selon état
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
top.document.title = "Splitting - Détail du dossier"

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
    if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
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
function init(){}
var calendar = new CalendarPopup(); 
			calendar.showYearNavigation();   
			calendar.setMonthNames('Janvier','Février','mars','Avril','Mai','Juin','Juillet','Août','Septembre','Octobre','Novembre','Decembre');
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
			<%-- tpl:put name="zoneTitle" --%>Détail d'un dossier de splitting<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          <tr> 
            <td width="240">Num&eacute;ro du dossier</td>
            <td> 
              <input type="text" name="idDossierSplitting" readonly tabIndex='-1' size="10" value="<%=viewBean.getIdDossierSplitting()%>" class="disabled">
            </td>
            <td> interne</td>
            <td colspan="3">
              <input type="text" name="idDossierInterne" size="12" value="<%=viewBean.getIdDossierInterne()%>">
            </td>
          </tr>
          <tr> 
            <td width="240"> Assur&eacute;</td>
            <td>
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
            </td>
            <td colspan="4"> 
              <input type="text" id="tiersAssureInfoNom" name="tiersAssureInfoNom" readonly tabIndex='-1' size="70" value="<%=viewBean.getTiersAssureNomComplet()%>" class="disabled">
            </td>
        
          <tr>
          	<td>Titre</td>
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
          	<td>Langue</td>
          	<td colspan="5">
          		<ct:FWCodeSelectTag name="langueAssure" wantBlank="false" except="<%=viewBean.giveListLangueToExcept()%>" defaut="<%=viewBean.getLangueAssure()%>" codeType="PYLANGUE"/>
			</td>
          </tr>
          <tr> 
            <td></td>
            <td colspan="5"> 
              <textarea style="display:none;" name="tiersAssureInfo" class="disabled" readonly tabIndex='-1' rows="4" cols="70"><%=viewBean.getTiersAssureLocalite()%>
		      </textarea>
            </td>
          </tr>
          <tr> 
				<td>
					Date de naissance&nbsp;
				</td>
				<td colspan="5">
					<input type="text" size = "10" class='disabled' name="dateNaissanceAss" readonly tabindex='-1' value = "<%=viewBean.getDateNaissanceAss()%>">
					&nbsp;
					Sexe &nbsp;
					<input type="text" size = "7" class='disabled' name="sexeAss" readonly tabindex='-1' value = "<%=viewBean.getSexeLibelleAss()%>">
					Pays &nbsp;
					<input type="text" size = "50" class='disabled' name="paysAss" readonly tabindex='-1' value = "<%=viewBean.getPaysFormateAss()%>">
				</td>
					
			</tr>          
          <tr> 
            <td width="240">Conjoint</td>
            <td>
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

            </td>
            <td colspan="4">
              <input type="text" id="tiersExConjointInfoNom" name="tiersExConjointInfoNom" readonly tabIndex='-1' size="70" value="<%=viewBean.getTiersConjointNomComplet()%>" class="disabled">
            </td>
          </tr>
          <tr>
          	<td>Titre</td>
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
          	<td>Langue</td>
          	<td colspan="5">
          		<ct:FWCodeSelectTag name="langueExConjoint" wantBlank="false" except="<%=viewBean.giveListLangueToExcept()%>" defaut="<%=viewBean.getLangueExConjoint()%>" codeType="PYLANGUE"/>
			</td>
          </tr>
          <tr> 
            <td></td>
            <td colspan="5"> 
              <textarea style="display:none;" name="tiersConjointInfo" readonly class="disabled" tabIndex='-1' rows="4" cols="70"><%=viewBean.getTiersConjointLocalite()%>
		      </textarea>
            </td>
          </tr>
          <tr> 
				<td>
					Date de naissance&nbsp;
				</td>
				<td colspan="5">
					<input type="text" size = "10" class='disabled' name="dateNaissanceCon" readonly tabindex='-1' value = "<%=viewBean.getDateNaissanceConj()%>">
					&nbsp;
					Sexe &nbsp;
					<input type="text" size = "7" class='disabled' name="sexeCon" readonly tabindex='-1' value = "<%=viewBean.getSexeLibelleConj()%>">
					Pays &nbsp;
					<input type="text" size = "50" class='disabled' name="paysCon" readonly tabindex='-1' value = "<%=viewBean.getPaysFormateConj()%>">
				</td>
					
			</tr> 
          <tr> 
            <td width="240">Date d'ouverture du dossier</td>
            <td colspan="5"> 
              <input type="text" name="dateOuvertureDossier" size="10" maxlength="10" value="<%=viewBean.getDateOuvertureDossier()%>" <%=styleOuverture%>>
              <% if(JAUtil.isStringEmpty(styleOuvRev)) { %>
              <input value="..." type=button name="anchor_dateOuvertureDossier" id="anchor_dateOuvertureDossier" onClick="calendar.select(dateOuvertureDossier,'anchor_dateOuvertureDossier','dd.MM.yyyy'); return false;"  onBlur="fieldFormat(dateOuvertureDossier,'CALENDAR')" >
              <% } %>
            </td>
          </tr>
          <tr> 
            <td width="240">Date du mariage</td>
            <td colspan="5"> 
              <input type="text" name="dateMariage" size="10" maxlength="10" value="<%=viewBean.getDateMariage()%>" <%=styleDateMariageDateDivorce%>>
              <% if(JAUtil.isStringEmpty(styleDateMariageDateDivorce)) { %>
              <input value="..." type=button name="anchor_dateMariage" id="anchor_dateMariage" onClick="calendar.select(dateMariage,'anchor_dateMariage','dd.MM.yyyy'); return false;"  onBlur="fieldFormat(dateMariage,'CALENDAR')" >
              <% } %>
            </td>
          </tr>
          <tr> 
            <td width="240">Date du divorce</td>
            <td colspan="5"> 
              <input type="text" name="dateDivorce" size="10" maxlength="10" value="<%=viewBean.getDateDivorce()%>" <%=styleDateMariageDateDivorce%>>
              <% if(JAUtil.isStringEmpty(styleDateMariageDateDivorce)) { %>
              <input value="..." type=button name="anchor_dateDivorce" id="anchor_dateDivorce" onClick="calendar.select(dateDivorce,'anchor_dateDivorce','dd.MM.yyyy'); return false;"  onBlur="fieldFormat(dateDivorce,'CALENDAR')" >
              <% } %>
            </td>
          </tr>
          <tr> 
            <td width="240">Motif du splitting</td>
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
            <td width="240">Extrait de CI requis</td>
            <td> 
              <input type="checkbox" onclick="updateHiddenCi();" name="extraitCiRequisAssure" <%=(viewBean.isExtraitCiRequisAssure().booleanValue())? "checked" : "unchecked"%> <%=cbOuvRev%>>
              <input type="hidden" onclick="updateHiddenCi();" name="extraitCiRequisAssureStr" value="<%=(viewBean.isExtraitCiRequisAssure().booleanValue())? "True" : "False"%>">
              pour l'assuré </td>
            <td colspan="4"> 
              <input type="checkbox" onclick="updateHiddenCiConjoint();" name="extraitCiRequisConjoint" <%=(viewBean.isExtraitCiRequisConjoint().booleanValue())? "checked" : "unchecked"%> <%=cbOuvRev%>>
              <input type="hidden" name="extraitCiRequisConjointStr" value="<%=(viewBean.isExtraitCiRequisConjoint().booleanValue())? "True" : "False"%>">
              pour le conjoint </td>
          </tr>
     	  <tr> 
            <td width="240">Accusé de réception</td>
            <td colspan="5">
            	 <input type="checkbox"  id="chkAccuseReceptionAssure" name="chkAccuseReceptionAssure" <%=viewBean.getChkAccuseReceptionAssure().booleanValue()?"checked":"unchecked"%> > pour l'assuré
            </td>
          </tr>
          <tr> 
            <td width="240">Invitation à l'ex-conjoint à participer au splitting</td>
            <td colspan="4">
            	 <input type="checkbox"  id="chkInvitationExConjoint" name="chkInvitationExConjoint" <%=viewBean.getChkInvitationExConjoint().booleanValue()?"checked":"unchecked"%> >
            </td>
          </tr>
          <!-- tr> 
            <td width="240">Taxation cot.pers.d&eacute;finitive</td>
            <td colspan="5"> 
              <input type="checkbox" name="estTaxationDefinitive" <%=(viewBean.isEstTaxationDefinitive().booleanValue())? "checked" : "unchecked"%> <%=cbOuvRev%>>
            </td>
          </tr-->
          <tr> 
            <td width="240">Responsable du dossier</td>
            <td colspan="5"> 
              <input type="text" name="responsableDossier" size="10" maxlength="10" value="<%=viewBean.getResponsableDossier()%>">
            </td>
          </tr>
          <tr> 
            <td width="240">Référence service</td>
            <td colspan="5"> 
              <input type="text" name="referenceService" size="4" onKeyDown="checkKey(this)" onChange="changeName(this)" onKeyUp="checkKey(this)" maxlength="4" value="<%=viewBean.getReferenceService()%>">
            </td>
          </tr>
          
          <tr> 
            <td width="240" valign="top">Remarque</td>
            <td colspan="5" rowspan="2"> 
              <textarea name="remarqueDossier" rows="3" cols="40"><%=viewBean.getRemarqueDossier()%></textarea>
            </td>
          </tr>
          <tr> 
            <td width="240">&nbsp;</td>
            <td colspan="5">&nbsp;</td>
          </tr>
          <tr> 
            <td width="240">Etat</td>
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



	<ct:menuChange displayId="options" menuId="dossierSplitting-detail" showTab="options">
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdDossierSplitting()%>"/>
		<ct:menuSetAllParams key="idDossierSplitting" value="<%=viewBean.getIdDossierSplitting()%>"/>
		<ct:menuSetAllParams key="idTiersAssure" value="<%=viewBean.getIdTiersAssure()%>"/>
		<ct:menuSetAllParams key="idTiersConjoint" value="<%=viewBean.getIdTiersConjoint()%>"/>
	</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>