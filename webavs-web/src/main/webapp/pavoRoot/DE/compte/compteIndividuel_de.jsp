
<!-- Sample JSP file -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 //EN">
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/avsParser.js"></SCRIPT>
<SCRIPT language="JavaScript">
top.document.title = "IK - Detail des individuellen Kontos"

</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<%@ page import="globaz.globall.db.*,globaz.globall.util.*"%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
    globaz.pavo.db.compte.CICompteIndividuelViewBean viewBean = (globaz.pavo.db.compte.CICompteIndividuelViewBean)session.getAttribute ("viewBean");
    
//    globaz.pavo.db.compte.CICompteIndividuelViewBean mainCompteIndBean = new globaz.pavo.db.compte.CICompteIndividuelViewBean();
//   	mainCompteIndBean.setSession((BSession)globaz.pavo.translation.CodeSystem.getSession(session));
//   	mainCompteIndBean.setCompteIndividuelId(viewBean.getMainSelectedId());
//   	mainCompteIndBean.retrieve();	   	 



	idEcran = "CCI0002";
	selectedIdValue = viewBean.getCompteIndividuelId();
	userActionValue = "pavo.compte.compteIndividuel.modifier";
	
	String jspLocation = servletContext + mainServletPath + "Root/ci_ref_select.jsp";
	String jspLocation2 = servletContext + mainServletPath + "Root/tiForCI_select.jsp";
	String jspLocation3 = servletContext + mainServletPath + "Root/ci_select.jsp";
	String jspLocation4 = servletContext +mainServletPath +"Root/ti_cc_select.jsp";
   	String params = "&except="+viewBean.getCompteIndividuelId();
    
    String sure = "Etes-vous sur ?";
 	String detacher="Détacher";
 	String activer="CI actuel";
 	if("DE".equals(languePage)) {
 		detacher="Entketten";
 		activer="Aktuelles IK";
 		sure="Sind Sie sicher ?";
 	}
 	
 	boolean isSpecialist = viewBean.hasUserEditCIRight(null);
 	
 	//(globaz.pavo.util.CIUtil.getUserAccessLevel(session) >= 5);
 	String inputReadonly = "readonly tabindex='-1'";
 	String classDisabled = "class=\"disabled\"";
 	String classLongDisabled = "class=\"libelleLongDisabled\"";
 	bButtonDelete=false;
 	if (isSpecialist) {
 		inputReadonly = "";
 		classDisabled = "";
 		classLongDisabled = "";
 		bButtonDelete = true;
 	}
 	if(viewBean.CS_REGISTRE_PROVISOIRE.equals(viewBean.getRegistre())){
 		//bButtonDelete = false;
 		//globazbButtonUpdate = false;
 	}
 	String numDernierEmployeur = "";
 	String infoAff = viewBean.getInfoAff();
 	if (!"".equals(infoAff)) {
 		numDernierEmployeur = infoAff.substring(0,infoAff.indexOf("\n"));
 		infoAff = infoAff.substring(infoAff.indexOf("\n")+1);
 		infoAff = infoAff.substring(0,infoAff.indexOf("\n"));  
 	}
 	String codePays= viewBean.getPaysOrigineId(); 	
 	//String formState = fieldDisable;
	int autoDigitAff = globaz.pavo.util.CIUtil.getAutoDigitAff(session);
	int tailleChampAff = globaz.pavo.util.CIUtil.getTailleChampsAffilie(session);
	//

%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<style type="text/css">
      .visible {
        visibility:visible;
        display:inline;
      }
      .hiddenPage {
        visibility:hidden;
        display:none;
      }
</style> 

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function add() {
    document.forms[0].elements('userAction').value="pavo.compte.compteIndividuel.ajouter"
}



function upd() {
<%if (isSpecialist) {%>	
	//document.forms[0].elements('ciOuvert').options[0]=null;
<%}%>

}
function validate() {
    state = validateFields();
    if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="pavo.compte.compteIndividuel.ajouter";
    else
        document.forms[0].elements('userAction').value="pavo.compte.compteIndividuel.modifier";
    
    return state;

}
function cancel() {
 if (document.forms[0].elements('_method').value == "add")
  document.forms[0].elements('userAction').value="back";
 else
  document.forms[0].elements('userAction').value="pavo.compte.compteIndividuel.afficher";
}
function del() {
    if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")){
        document.forms[0].elements('userAction').value="pavo.compte.compteIndividuel.supprimer";
        document.forms[0].submit();
    }
}
function init(){
}
//-->
</script>


	 <%if(globaz.pavo.util.CIUtil.isSpecialist(session)){%>
				<ct:menuChange displayId="options" menuId="compteIndividuel-detail" showTab="options">
			<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getCompteIndividuelId()%>"/>
			<ct:menuSetAllParams key="compteIndividuelId" value="<%=viewBean.getCompteIndividuelId()%>"/>
			<ct:menuSetAllParams key="mainSelectedId" value="<%=viewBean.getCompteIndividuelId()%>"/>
		</ct:menuChange>
	<%}else{%>
		<ct:menuChange displayId="options" menuId="compteIndividuelNoSpez-detail" showTab="options">
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getCompteIndividuelId()%>"/>
		<ct:menuSetAllParams key="compteIndividuelId" value="<%=viewBean.getCompteIndividuelId()%>"/>
		<ct:menuSetAllParams key="mainSelectedId" value="<%=viewBean.getCompteIndividuelId()%>"/>
		</ct:menuChange>
	<%}%>

<script>

function submitPage() {
	if (validate()) {
 		action(COMMIT);
 	}
}
function updateIdCiRef(tag) {
	if (tag.select && tag.select.selectedIndex != -1) {
		document.getElementById('compteIndividuelIdReference').value = tag.select[tag.select.selectedIndex].idci;
	}
}
function resetIdCiRef() {
 	document.getElementById('compteIndividuelIdReference').value = '0';
}

function showCIRefPopup() {
	document.getElementById('CIRefPopup').className = 'visible';
	document.getElementById('CIRefPere').className = 'hiddenPage';
}

function updateInfoAffilie(tag) {
	if (tag.select && tag.select.selectedIndex != -1) {
 		document.getElementById('infoAffilie').value = tag.select[tag.select.selectedIndex].nom;
 	    document.getElementById('dernierEmployeur').value = tag.select[tag.select.selectedIndex].idAffiliation
 	}
}
function resetInfoAffilie() {
 	document.getElementById('infoAffilie').value = '';
 	document.getElementById('dernierEmployeur').value = '0';
}


function updateMainInfoAffilie(tag) {
	if (tag.select && tag.select.selectedIndex != -1) {
 		document.getElementById('nomPrenom').value = tag.select[tag.select.selectedIndex].nom;
 				
		//Mise a jours de la liste box Sexe. 
		for (var i=0 ; i<document.forms[0].sexe.options.length ; i++)  {
			if (document.forms[0].sexe.options[i].value == tag.select[tag.select.selectedIndex].sexe) {
				document.forms[0].sexe.options[i].selected = true;				
				break;
			}
		}       
		var paysCosId = "315"+tag.select[tag.select.selectedIndex].paysCode;
		for (var i=0 ; i<document.forms[0].paysOrigineId.options.length ; i++)  {
			if (document.forms[0].paysOrigineId.options[i].value == paysCosId) {
				document.forms[0].paysOrigineId.options[i].selected = true;				
				break;
			}
		}
		document.getElementById('dateNaissance').value = tag.select[tag.select.selectedIndex].date;
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
function resetMainInfoAffilie() {
 	document.getElementById('nomPrenom').value = '';
 	document.getElementById('dateNaissance').value='';
}
function resetCaisse(){
	document.getElementById('derniereCaisseAgenceInfo').value="";
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

function checkKeyRefInt(input){
	var re = /[^a-z.A-Z\-[^0-9\-äöüÄÖÜéèôà ,s/].*/	
	if (re.test(input.value)) {
		input.value = input.value.substr(0,input.value.length-1);
	}
}

function toUpperCase(tagName){
	var mySt = document.forms[0].elements(tagName).value;
	document.forms[0].elements(tagName).value = mySt.toUpperCase();
}

function updateCaisseComp(tag){
	if (tag.select && tag.select.selectedIndex != -1) {
 		document.getElementById('derniereCaisseAgenceInfo').value = tag.select[tag.select.selectedIndex].nom;
 	}	
}

</SCRIPT>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Detail des individuellen Kontos<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 

          <tr> 
	         
	
			<%
 		  		Object[] ciLies = viewBean.getCILies();
 		  		String[] pere = new String[] {"",""};
 		  		boolean isPere = false;
 		  		if(ciLies.length!=0) {
 		  			pere = (String[])ciLies[0];
 		  			isPere = pere[1].equals(globaz.commons.nss.NSUtil.formatAVSNew(viewBean.getNumeroAvs(),viewBean.getNnss().booleanValue())); 		  			
 		  		}
			%> 	
			<%if (globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getNumeroAvs()).equals(pere[1])) {%>
 				<td><b>Aktuelle SVN</b></td>
 		  	<%} else {%> 
				<td>SVN</td> 
			<% } %>	  	
            <td>                  
            <% if (isSpecialist) {%>
              	<nss:nssPopup validateOnChange="false" 
              	value='<%=viewBean.getNumeroAvsFormateSansPrefixe()%>' name="numeroAvs"  cssclass="visible" jspName="<%=jspLocation3%>" 
              	avsMinNbrDigit="8" avsAutoNbrDigit="11" onChange="updateMainInfoAffilie(tag);" 
              	nssMinNbrDigit="7" nssAutoNbrDigit="10" newnss="<%=viewBean.getNumeroAvsNNSS()%>"
              	/> 

			<%}else{%>              
				<input tabindex="-1" name='numeroAvs' size='17' <%=classDisabled%> <%=inputReadonly%> value='<%=viewBean.getNssFormate()%>'>
			<%}%>              
            </td>
            
			           
          </tr>                             
          <!--tr> 
            <td>N° AVS ancien</td>
            <td colspan=3> 
              <input name='numeroAvsAncienInv' size='15' class='disabled' readonly value='<%=JAStringFormatter.formatAVS(viewBean.getNumeroAvsAncien())%>'>
            </td>
          </tr-->
          <tr> 
            <td>Namensangaben</td>
            <td colspan=3> 
              <input name='nomPrenom' onKeyUp="checkKey(this)" onKeyDown="checkKey(this)" onChange="changeName(this)" size='70' maxlength='80' <%=classDisabled%> <%=inputReadonly%> value="<%=viewBean.getNomPrenom()%>">
            </td>
          </tr>
          <tr> 
            <td>Geburtsdatum</td>
            <td colspan=3> 
             <% if (!isSpecialist) {%>             
             <input name='dateNaissanceInv' tabindex="-1" class='disabled' size='11' readonly value='<%=viewBean.getDateNaissance()%>'>
             <% } else { %>
 			 <ct:FWCalendarTag name="dateNaissance" value="<%=viewBean.getDateNaissance()%>"/>
 			<% } %>
            </td>
          </tr>
          <tr> 
            <td>Geschlecht</td>
            <td colspan=3>	            
	            <ct:FWListSelectTag name="sexe" defaut="<%=viewBean.getSexe()%>" data="<%=viewBean.getChampsAsCodeSystem(globaz.pavo.db.compte.CICompteIndividuel.CHAMPS_AS_CS_SEXE)%>"/>
	            
            <%
            if (!isSpecialist) {%> 
              <input type="text" size="12" tabindex="-1" name="sexeInv" readonly class="disabled" value="<%=viewBean.getSexeFormate()%>" >
              <script>
				document.getElementById('sexe').style.display="none";
			</script>				
            <%}%>
            </td>
          </tr>          
          <tr> 
            <td>Heimatstaat</td>
            <td colspan=3>
            <% if (!isSpecialist) {%>
              <input type="text" tabindex="-1" name="paysOrigineIdInv" readonly size="50" class="disabled" value="<%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getPaysOrigineId(),session)+" ("+globaz.pavo.translation.CodeSystem.getCodeUtilisateur(viewBean.getPaysOrigineId(),session)+")"%>" >
            <%}else{%>
              <!--ct:FWListSelectTag name="paysOrigineId" defaut="<%=viewBean.getPaysOrigineId()%>" data="<%=viewBean.getChampsAsCodeSystem(globaz.pavo.db.compte.CICompteIndividuel.CHAMPS_AS_CS_PAYS)%>"/-->
              <ct:FWListSelectTag name="paysOrigineId"
							defaut="<%=!JAUtil.isStringEmpty(viewBean.getPaysOrigineId())?viewBean.getPaysOrigineId():\"315100\"%>"
							data="<%=globaz.pavo.util.CIUtil.getCountries(objSession)%>"
							/>
						<!--defaut="<%=(!JAUtil.isStringEmpty(viewBean.getPaysOrigineId()))?viewBean.getPaysOrigineId():"315100"%>"-->
						<!-- -->
            <%}%>
            
            </td>
          </tr>
          <tr> 
            <td>Letzte MZR-SZ (IK-Eröffnung/ZIK-Abschluss)</td>
            <td colspan="3"> 
               <input name='dernierMotifOuverture' <%=classDisabled%> size='2' <%=inputReadonly%> value='<%=JANumberFormatter.formatZeroValues(viewBean.getDernierMotifOuverture(),false,true)%>'>
               <font size="4">/</font> 
               <input name='dernierMotifCloture' <%=classDisabled%> size='2' <%=inputReadonly%> value='<%=JANumberFormatter.formatZeroValues(viewBean.getDernierMotifCloture(),false,true)%>'>
            </td>
          </tr>


		  <tr> 
            <td>Aktuelles IK</td>            
            <td colspan=3 height="25" nowrap>
              <input type="hidden" name="compteIndividuelIdReference" value="<%=viewBean.getCompteIndividuelIdReference()%>"> 		  	                      
 		  	<% 
 		  		if (isPere || ciLies.length==0) { 		  		
		  			if (isSpecialist) {%>
			 		  	<nss:nssPopup name="idRefPopupList"  value="<%=globaz.commons.nss.NSUtil.formatWithoutPrefixe(pere[1],viewBean.getNnss().booleanValue())%>" cssclass="libelle" jspName="<%=jspLocation%>" 
			 		  	 avsAutoNbrDigit="11" nssMinNbrDigit="7" nssAutoNbrDigit="10" avsMinNbrDigit="8" 
			 		  	 onChange="updateIdCiRef(tag);" onFailure="resetIdCiRef();" newnss="<%=viewBean.getNumeroAvsNNSS()%>" />
			 		  	 <script language="javaScript">
 		  	 	              	partialidRefPopupListPopupTag.updateParams('<%=params%>');
			 		  	 </script>

			 		<%} else {%>
			 		  	<input type="text" readonly tabindex="-1" class="disabled" size="19" name="idRefPopupListInv" value="<%=pere[1]%>">
		 		  	<%}
 		  	   } else {%>
		 		  	<a href="<%=formAction%>?userAction=pavo.compte.compteIndividuel.afficher&selectedId=<%=pere[0]%>&mainSelectedId=<%=viewBean.getMainSelectedId()%>" class="gText"><%=pere[1]%></a>               		
 		  	<% } 
 		  	   if(ciLies.length!=0) {%>
					</td>
					<td>
					<% if (!isPere && isSpecialist) { %><input type="button" value="<%=activer%>" onClick="if (window.confirm('<%=sure%>')) {document.getElementById('compteIndividuelIdReference').value='<%=viewBean.getCompteIndividuelId()%>';submitPage();}"><% } %>
            		</td>					
					</tr>
					<tr>
						<td>Bisheriges IK</td>
						<td colspan=3>
				<%
 				for(int i=1;i<ciLies.length;i++) {
 					String [] link = (String[])ciLies[i];
 					if(!"".equals(link[0])) { %>
 					<a href="<%=formAction%>?userAction=pavo.compte.compteIndividuel.afficher&selectedId=<%=link[0]%>&mainSelectedId=<%=viewBean.getMainSelectedId()%>" class="gText"><%=link[1]%></a>
               		<% } else { %>
               		<span class="gText"><%=link[1]%></span>
                     <% }
                  } 
                %>
                </td>
                <td><% if (isSpecialist) { %><input type="button" value="<%=detacher%>" onClick="if (window.confirm('<%=sure%>')) {document.getElementById('compteIndividuelIdReference').value='0';submitPage();}"><% } %>
          <%}%>
          </td></tr>
          <tr> 
            <td>IK-Eröffnungsjahr</td>
            <td colspan=3> 
              <input size="4" maxlength="4" onkeypress="return filterCharForPositivInteger(window.event);" name='anneeOuverture' <%=classDisabled%> <%=inputReadonly%> value='<%=JANumberFormatter.formatZeroValues(viewBean.getAnneeOuverture(),false,true)%>'>
            </td>
          </tr>
          <tr> 
            <td>Auszugsjahr</td>
            <td colspan=3> 
              
              	<input size="4" maxlength="4" onkeypress="return filterCharForPositivInteger(window.event);" name='anneeExtrait' <%=classDisabled%> <%=inputReadonly%> 
              	value="<%=viewBean.getAnneeExtrait()%>">
              
            </td>
          </tr>
          <tr> 
            <td>ZIK-Abschlussdatum</td>
            <td colspan=3> 
              <% if (!isSpecialist) {%>
               <input name='dateDernierMotifInv' class='disabled' size='7' tabindex="-1" readonly value='<%=viewBean.getDerniereCloture()%>'>
             <% } else { %>
 			  	<!--<ct:FWCalendarTag name="derniereCloture" displayType="MONTH" value="<%=viewBean.getDerniereCloture()%>"/>
 			  	<script>
 			  		document.getElementById("derniereCloture").size="7";
 			  		document.getElementById("derniereCloture").maxlength="7";
 			  	</script>-->
 			  	<input type="text" name="derniereCloture" size="7" value='<%=viewBean.getDerniereCloture()%>'>
 			<% } %>            
 			</td>
          </tr>
          <tr> 
            <td>Auftraggebende AK</td>
            <td colspan=3> 
              <% if (!isSpecialist) {%>
               <input name='derniereCaisseInv' tabindex="-1" class="disabled" size='7' readonly value='<%=viewBean.getDerniereCaisseAgence()%>'>
               
              <% } else { %> 
             	<ct:FWPopupList   size="3" onChange="updateCaisseComp(tag);" name="derniereCaisseAgence" value="<%=viewBean.getDerniereCaisseForDetail()%>"  onFailure="resetCaisse();" jspName="<%=jspLocation4%>"  autoNbrDigit="5" minNbrDigit="1"  />
             	<!--<input name='derniereCaisseAgence' <%=classDisabled%> <%=inputReadonly%> size='7' value='<%=viewBean.getDerniereCaisseForDetail()%>'>-->
             	<input type="hidden" name="derniereCaisse" value="">
				
 			 <%}%>   
 			 <%if(!"0".equals(viewBean.getDerniereCaisse()) && !JAUtil.isStringEmpty(viewBean.getDerniereCaisse())){%>
				<input type="text" name="derniereCaisseAgenceInfo" class="disabled" readonly size="70" tabindex="-1" value="<%=viewBean.getNomCaisse()%>">
 				<%}else{%>
 					<input type="text" name="derniereCaisseAgenceInfo" class="disabled" readonly size="70" tabindex="-1" value="">	
 			<%}%>         

            
              
            </td>
          </tr>
          <tr> 
            <td>Kasseneigener Hinweis</td>
            <td colspan=3> 
              <input name='referenceInterne' size='25' onKeyUp="checkKeyRefInt(this)" onKeyDown="checkKeyRefInt(this)" onChange="changeName(this)" maxlength='20' class='libelle' value='<%=viewBean.getReferenceInterne()%>'>
				           
            </td>
          </tr>
          <tr> 
            <td>Zugriffberechtigung</td>
            <td colspan=3> 
			<% if (isSpecialist) {%> 
				<ct:FWCodeSelectTag name="accesSecurite" defaut="<%=viewBean.getAccesSecurite()%>" codeType="CISECURI" wantBlank="false"/> 
			<% } else { %>
				<input size="13" tabindex="-1" name='accesSecuriteInv' readonly class="disabled" value='<%=globaz.pavo.translation.CodeSystem.getLibelle(viewBean.getAccesSecurite(),session)%>'>
			<% } %>
            </td>
          </tr>   
          <tr> 
            <td>Kontostatus</td>
            <td colspan=3>
            <%
            boolean isReopen = false;
            if (viewBean.isCiOuvert().booleanValue() && viewBean.getDerniereCloture()!=null && viewBean.getDerniereCloture().trim().length()>0)
				isReopen = true;
				
            if (isSpecialist) {%>
              
              <select name="ciOuvert" style="width:5cm">
             <%if(isReopen){%>
              	<option value="on" <%=(viewBean.isCiOuvert().booleanValue())?"SELECTED":""%>>Wiedereröffnet</option>
             <%}%>
              <option value="" <%=(!viewBean.isCiOuvert().booleanValue())?"SELECTED":""%>>Gesperrt</option>
              <option value="on"<%="add".equalsIgnoreCase(request.getParameter("_method"))?"SELECTED":""%> <%=(viewBean.isCiOuvert().booleanValue() && !isReopen)?"SELECTED":""%>>Eröffnet</option>
              </select>
            <%} else {             
            	if (viewBean.isCiOuvert().booleanValue()) {
            		if (isReopen) {%>
	            		<input width="5cm" tabindex="-1" size ="13" type="text" name="ouvertDummy" value="réouvert" <%=classDisabled%> <%=inputReadonly%>>
            		<%} else {%>
			              <input width="5cm" tabindex="-1" size ="13" type="text" name="ouvertDummy" value="ouvert" <%=classDisabled%> <%=inputReadonly%>>
            		<%}%>
            		<input type="hidden" name="ciOuvert" value="on">

            	<%} else {%>
              		<input width="5cm" tabindex="-1" type="text" size ="13" name="ouvertDummy" value="fermé" <%=classDisabled%> <%=inputReadonly%>>
            <%	  }
            }%>
            </td>
          </tr>                 
          <tr> 
            <td>Letzter Arbeitgeber</td>
            <td colspan=3 nowrap>
            <%if (!isSpecialist) {%><input size="16" class="disabled" readonly type="text" tabindex="-1" name="dernierEmployeurInv" value="<%=numDernierEmployeur%>"><%} 
            else 
            {%><input type="hidden" name="dernierEmployeur" value="<%=viewBean.getDernierEmployeur()%>">														
            <ct:FWPopupList  validateOnChange="true" size="16" name="dernierEmployeurInv" onChange="updateInfoAffilie(tag);" value="<%=numDernierEmployeur%>" jspName="<%=jspLocation2%>" autoNbrDigit="<%=autoDigitAff%>" minNbrDigit="3"  onFailure="resetInfoAffilie()"/>
            &nbsp;
            <script><!---->
            	document.getElementById("dernierEmployeurInv").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
            
            </script>
            <%}%>
            <input type="text" class="disabled" readonly name="infoAffilie" size="60" maxlength="55" value="<%=infoAff%>" tabIndex="-1">
            </td>
          </tr>
          <tr> 
            <td>IK aus Fusion</td>
            <td colspan=3>
            <%if (isSpecialist) {%>
              <select name="provenanceFusion" style="width:3cm">
              <option value="on" <%=(viewBean.isProvenanceFusion().booleanValue())?"SELECTED":""%>>Ja
              <option value="" <%=(!viewBean.isProvenanceFusion().booleanValue())?"SELECTED":""%>>Nein
              </select>
            <%} else { 
            	if (viewBean.isProvenanceFusion().booleanValue()) {%>
              <input tabindex="-1" size="4" type="text" name="provenanceFusionInv" value="oui" <%=classDisabled%> <%=inputReadonly%>>
              <input type="hidden" name="provenanceFusion" value="on">
            	<%} else {%>
              <input tabindex="-1" size="4" type="text" name="provenanceFusionInv" value="non" <%=classDisabled%> <%=inputReadonly%>>
      
            <%		}
            	}%>
			</td>
          </tr>
          <tr>
          	<td>
          		Abschreibung
          	</td>
          	<td>
          		<ct:FWCodeSelectTag name="codeIrrecouverable" defaut="<%=viewBean.getCodeIrrecouverable()%>"  codeType="CIKATIRR" wantBlank="true"/>
          	</td>
          </tr>
          <tr>
          	<td>
          		Splitting
          	</td>
          	<td>
          		<input size = "5" type = "text" value ="<%=viewBean.hasSplittingToString()%>" class="disabled" readonly tabindex="-1">
          	</td>
          </tr>
                    <tr>
          <td>
          	UPI Quelle
          </td>
          	<td>
          		<input name='srcUpi' size='20' maxlength='15' <%=classDisabled%> <%=inputReadonly%> value="<%=viewBean.getSrcUpi()%>">
          	</td>
          </tr>
            <tr>
          <td>
          	Inaktiviert
          </td>
          	<td>
          		<input name='inactive' type="checkbox" <%=classDisabled%> <%=inputReadonly%> <%=viewBean.getInactive().booleanValue()?"checked":""%>>
          	</td>
          </tr>
          <tr>
          <td>
          	Annulliert
          </td>
          	<td>
          		<input id='invalide' name='invalide' type="checkbox" <%=classDisabled%> <%=inputReadonly%> <%=viewBean.getInvalide().booleanValue()?"checked":""%>>
          	</td>
          </tr>
          <tr> 
            <td>Erfasst am</td>
            <td> 
               <input name='dateCreationInv' class='disabled' size='11' readonly value='<%=viewBean.getDateCreation()%>' tabindex="-1">        
 				&nbsp;&nbsp;durch &nbsp;&nbsp;
             
              <input name='utilisateurCreationInv' class='libelleLongDisabled' readonly value='<%=viewBean.getUtilisateurNomComplet()!=null?viewBean.getUtilisateurNomComplet():""%>' tabindex="-1">
            </td>
          </tr>
          


          <!--tr> 
            <td>N° AVS ancien précédent</td>
            <td colspan=3> 
              <input name='numeroAvsPrecedantInv' size='15' class='disabled' readonly value='<%=JAStringFormatter.formatAVS(viewBean.getNumeroAvsPrecedant())%>'>
            </td>
          </tr-->

          <%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>