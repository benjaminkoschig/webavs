<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%	

    globaz.pavo.db.inscriptions.CIJournalImprimerViewBean viewBean = (globaz.pavo.db.inscriptions.CIJournalImprimerViewBean)session.getAttribute ("viewBean");
	//globaz.pavo.db.inscriptions.CIJournalListViewBean viewBeanJournalManager = (globaz.pavo.db.inscriptions.CIJournalListViewBeanViewBean)session.getAttribute("viewBean2");
	userActionValue = "pavo.inscriptions.journalImprimer.executer";
	String jspLocation = servletContext + mainServletPath + "Root/ci_select.jsp";
	String jspLocation2 = servletContext + mainServletPath + "Root/ti_select.jsp";
	int autoDigitAff = globaz.pavo.util.CIUtil.getAutoDigitAff(session);
	int tailleChamps = globaz.pavo.util.CIUtil.getTailleChampsAffilie(session);
	String pdfChecked = "pdf".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "";
	String xlsChecked = "xls".equals(viewBean.getTypeImpression()) ? "checked='checked'" : "";	
	idEcran="CCI2018";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function validate() {
	var elmt = document.getElementById("tableauIdTypeJournal");
	var values = new Array();

	for(var i=0; i< elmt.options.length; i++) {				
		if(elmt.options[i].selected == true) {			
			values[values.length] = elmt.options[i].value; 		
		}			
	}
	document.getElementById("forIdTypeJournalMultiple").value = values;	
	
	return true;
}

function init()
{
/*if (document.forms[0].elements('_method').value == "add")
   document.forms[0].elements('KcidIn').disabled = true;
   document.forms[0].elements('KcidLabel').disabled = true;
else
   document.forms[0].elements('KcidIn').disabled = true;
   document.forms[0].elements('KcidLabel').disabled = true;*/	
}

function valid(){
	nbOpt = document.getElementById('typeJournalMultiple').length
	for(i = 0; i < nbOpt ; i++){
		if(document.getElementById('typeJournalMultiple').options[i].selected==true){
			if(!document.getElementById('forIdTypeJournalMultiple').value == ""){
				document.getElementById('forIdTypeJournalMultiple').value += (", ")
			}
			document.getElementById('forIdTypeJournalMultiple').value+=document.getElementById('typeJournalMultiple').options[i].value;
			
		}
	}
		
	document.forms[0].submit();
	
}
/*
*/
// stop hiding -->
function updateInfoAffilie(tag) {
	if (tag.select && tag.select.selectedIndex != -1) {
 		document.getElementById('infoAffilie').value = tag.select[tag.select.selectedIndex].nom
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
</script>
	<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
	<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
	<%-- /tpl:put --%>
	<%@ include file="/theme/process/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Imprimer les inscriptions<%-- /tpl:put --%>
	<%@ include file="/theme/process/bodyStart2.jspf" %>
							<%-- tpl:put name="zoneMain" --%>
          <tr> 
            <td width="23%" height="2">Adresse E-Mail</td>
						<TD width="20" height="2"></TD>
						<td height="2"> 
              <input type="text" name="mailAddress" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getEMailAddress()!=null?viewBean.getEMailAddress():""%>">
              *</td>
          </tr>
          <tr> 
            <td width="23%" height="2">Type de compte</td>
						<TD width="20" height="2"></TD>
						<%
						java.util.HashSet typeCompte = new java.util.HashSet();
						typeCompte.add(globaz.pavo.db.compte.CIEcriture.CS_TEMPORAIRE_SUSPENS);
						%>
						<td height="2"> <ct:FWCodeSelectTag name="forIdTypeCompte" defaut="<%=viewBean.getForIdTypeCompte()%>" except="<%=typeCompte%>" codeType="CITYPCOM" wantBlank="true"/> 
            </td>
          </tr>
          <TR>
          	<TD width="23%" height="2">Chiffre clé</TD>
          	<TD width="20" height="2"></TD>
          	<TD><input size="3" maxlength="3" type="text" name="forGenreEcrituresAParser" value=""></TD>
          </TR>
          <TR>
          	<TD  width="23%" height="2">Avec écritures négatives</TD>
          	<TD width="20" height="2"></TD>
          	<TD><input type="checkBox" checked name="avecEcrituresNegatives" ></TD>
          </TR>
          <TR>
          	<TD  width="23%" height="2">Uniquement les écritures salariées</TD>
          	<TD width="20" height="2"></TD>
          	<TD><input type="checkBox" checked name="ecrituresSalariees" ></TD>
          </TR>
          
          <TR>
          	<TD>
          		Code
          	</TD>
	          	<TD width="20" height="2"></TD>
          	<TD>
          		<ct:FWCodeSelectTag name="forCode" defaut="" wantBlank="true" codeType="CICODAMO"/>
          	</TD>
          </TR>
          <tr> 
            <td width="23%" height="2">Tri</td>
						<TD width="20" height="2"></TD>
						<td height="2"> 
              <select name="tri" style="width:8cm;">
                <option value="nomPrenom" <%="nomPrenom".equals(viewBean.getTri())?"selected":""%>>Nom</option>
                <option value="avs" <%="avs".equals(viewBean.getTri())?"selected":""%>>NSS</option>
                <option value="date" <%="date".equals(viewBean.getTri())?"selected":""%>>Date d'inscription</option>
                <option value="numAff" <%="numAff".equals(viewBean.getTri())?"selected":""%>>Affilié</option>
               </select>
               &nbsp;&nbsp;
			  <INPUT type="checkbox" name=avecSousTotal value="on">
			  &nbsp;&nbsp;Avec sous total (uniquement avec tri Nom)
            </td>
          </tr>
          <tr> 
            <td width="23%" height="2">Journal</td>
						<TD width="20" height="2">du</TD>
						<td height="2">  
              <input type="text" name="fromIdJournal" size="15" maxlength="10" value="<%=viewBean.getFromIdJournal()!=null?viewBean.getFromIdJournal():""%>">
              au 
              <input type="text" name="untilIdJournal" size="15" maxlength="10" value="<%=viewBean.getUntilIdJournal()!=null?viewBean.getUntilIdJournal():""%>">
            </td>
          </tr>
          <tr> 
          	<td width="23%" height="2">Type de journal</td>
			<TD width="20" height="2"></TD>
			<td height="2"> 							
				<SELECT name="tableauIdTypeJournal" id="tableauIdTypeJournal"multiple="multiple">
					<%=viewBean.getTypeJournauxAsString()%>
				</SELECT>
				<input type="hidden" name="forIdTypeJournalMultiple" id="forIdTypeJournalMultiple" value=""/>
            </td>
          </tr>
          <tr> 
            <td width="23%" height="2">Ann&eacute;e de cotisation</td>
						<TD width="20" height="2">de</TD>
						<td height="2">  
              <input type="text" name="fromAnnee" size="4" maxlength="4" value="<%=viewBean.getFromAnnee()!=null?viewBean.getFromAnnee():""%>">
              &agrave; 
              <input type="text" name="untilAnnee" size="4" maxlength="4" value="<%=viewBean.getUntilAnnee()!=null?viewBean.getUntilAnnee():""%>">
              &nbsp;&nbsp;
			  <INPUT type="checkbox" name=unMailPasAnnee value="on">
			  &nbsp;&nbsp;un journal par année
            </td>
          </tr>
          <tr> 
            <td width="23%" height="2">Date d'inscription</td>
						<TD width="20" height="2">du</TD>
						<td height="2"> 
	
						<%String dateFrom = viewBean.getFromDateInscription()==null?"":viewBean.getFromDateInscription();
						  String dateUntil = viewBean.getUntilDateInscription()==null?"":viewBean.getUntilDateInscription();%>
						<ct:FWCalendarTag name="fromDateInscription" value="<%=dateFrom%>"/>
              au               			              				
						<ct:FWCalendarTag name="untilDateInscription" value="<%=dateUntil%>"/>

              
            </td>
          </tr>
          <tr> 
            <td width="23%" height="2">NSS </td>
						<TD width="20" height="2">de</TD>
						<td height="2"> 

              <nss:nssPopup value='<%=viewBean.getFromNumeroAvs()!=null?viewBean.getFromNumeroAvs():""%>' name="fromNumeroAvs" 
               cssclass="visible" jspName="<%=jspLocation%>" avsMinNbrDigit="3" nssMinNbrDigit="3"/>
              &agrave; 
              <nss:nssPopup value='<%=viewBean.getUntilNumeroAvs()!=null?viewBean.getUntilNumeroAvs():""%>' name="untilNumeroAvs"  
              avsMinNbrDigit="3" nssMinNbrDigit="3"   cssclass="visible" jspName="<%=jspLocation%>" />
            </td>
          </tr>
	   <tr> 
            <td width="23%" height="2">Utilisateur </td>
						<TD width="20" height="2"></TD>
						<td height="2"> 
              <input type="text" name="forNomEspion" size="20" maxlength="20" value="<%=viewBean.getForNomEspion()!=null?viewBean.getForNomEspion():""%>">
            </td>
          </tr>
	   <tr> 
            <td width="23%" height="2">Numéro d'affilié</td>
						<TD width="20" height="2"></TD>
						<td height="2"> 
              <ct:FWPopupList validateOnChange="true" onFailure="resetInfoAffilie();" onChange="updateInfoAffilie(tag);" value='<%=viewBean.getForNumeroAffilie()!=null?viewBean.getForNumeroAffilie():""%>' name="forNumeroAffilie" size="<%=tailleChamps%>" className="visible" jspName="<%=jspLocation2%>" minNbrDigit="3" autoNbrDigit="<%=autoDigitAff%>"/>
              &nbsp;
              <input type="text" class="disabled" readonly name="infoAffilie" size="73" maxlength="73" value="" tabIndex="-1">                          
            </td>
          </tr>
          <tr> 
            <td width="23%" height="2">Imprimer le journal d&eacute;taill&eacute;</td>
						<TD width="20" height="2"></TD>
						<td height="2"> 
              <input type="checkbox" name="detail" checked>
            </td>
          </tr>
          <tr> 
            <td width="23%" height="2">Imprimer la r&eacute;capitulation</td>
						<TD width="20" height="2"></TD>
						<td height="2"> 
              <input type="checkbox" name="recapitulation" checked>
            </td>
          </tr>
	<TR>
		<td><ct:FWLabel key="TYPE_IMPRESSION"/></td>
		<TD width="20" height="2"></TD>
  		<TD>
   			<input type="radio" name="typeImpression" value="pdf" <%=pdfChecked%>/>PDF&nbsp;
   			<input type="radio" name="typeImpression" value="xls" <%=xlsChecked%>/>Excel
   		</TD>
    </TR>             
       
	<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>