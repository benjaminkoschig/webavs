 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%idEcran = "CCI0012";%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>

<SCRIPT>
//bFind = true;
usrAction = "pavo.compte.ecrituresSuspens.lister";
top.document.title = "IK - Pendente IK-Buchungen verwalten";
timeWaiting = 1;
//bFind = true;
<%@ page import="globaz.globall.util.*,globaz.pavo.db.compte.*" %>
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
<%
	subTableHeight = 70;
	bButtonNew = false;
%>
<%
		String jspLocation = servletContext + mainServletPath + "Root/ci_select.jsp";
		String jspLocation2 = servletContext + mainServletPath + "Root/ti_select.jsp";

		int autoDigiAff = globaz.pavo.util.CIUtil.getAutoDigitAff(session);
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/avsParser.js"></SCRIPT>

<ct:menuChange displayId="options" menuId="CI-OnlyDetail">
	</ct:menuChange>
	<ct:menuChange displayId="menu" menuId="CI-MenuPrincipal" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Pendente IK-Buchungen verwalten<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

			<TR>
				<TD>
					SVN &nbsp;
				</TD>
				<TD>
					
					<nss:nssPopup name="fromNumeroAvsSuspenes" value="" avsMinNbrDigit="99" nssMinNbrDigit="99"	/>
					<input type="hidden" name="forIdTypeCompte" value="<%=globaz.pavo.db.compte.CIEcriture.CS_CI_SUSPENS%>">
					<input type ="hidden" name="tri" value="avs">
					<input type="hidden" name="forNotGenre" value="<%=globaz.pavo.db.compte.CIEcriture.CS_CIGENRE_8%>">
				</TD>
				<TD>
					Namensangaben &nbsp;
				</TD>
				<TD>
					<input type="text" size="40" onKeyDown="checkKey(this)" onChange="changeName(this)" onKeyUp="checkKey(this)" name="likeNomPrenomPartiel">&nbsp;
				</TD>
			</TR>
			<TR>
				<TD>
					Abr.-Nr. &nbsp;
				</TD>
				<TD>
					<ct:FWPopupList name="fromEmployeur" value="" className="libelle" jspName="<%=jspLocation2%>" autoNbrDigit="<%=autoDigiAff%>" size="16" minNbrDigit="3"/>
					<SCRIPT>
            			document.getElementById("fromEmployeur").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
	            	</SCRIPT>
				</TD>
				<TD>
					Beitragsjahr &nbsp;
				</TD>
				<TD>
					<input type="text" name="forAnnee" size="4" maxLength="4" onKeyPress="return filterCharForPositivInteger(window.event)">&nbsp;
				</TD>
			</TR>
			<TR>
				<TD colspan="2">
					VR ausschliessen

					<input type="checkBox" name="exclureRA" checked>
				</TD>
			</TR>
		  <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> 
      
      <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>
