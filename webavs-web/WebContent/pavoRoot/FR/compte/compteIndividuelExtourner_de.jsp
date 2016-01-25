<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.globall.util.*"%>
<%@ page import="globaz.pavo.db.compte.CIEcritureManager"%>
<%@ page import="globaz.globall.db.BManager"%>
<%
	idEcran = "CCI0011" ;
 	globaz.pavo.db.compte.CICompteIndividuelExtournerViewBean viewBean = (globaz.pavo.db.compte.CICompteIndividuelExtournerViewBean)session.getAttribute ("viewBean");
    
	userActionValue = "pavo.compte.compteIndividuel.extourner";
	tableHeight = 150;
	String jspLocation = servletContext + mainServletPath + "Root/ci_select.jsp";

	
	CIEcritureManager ecrMgr = new CIEcritureManager();
	ecrMgr.setSession(viewBean.getSession());
	ecrMgr.setForCompteIndividuelId(viewBean.getCompteIndividuelId());
	ecrMgr.setOrder("KBNANN DESC, KBNMOF DESC");
	ecrMgr.setCacherEcritureProtege(1);
	ecrMgr.find(BManager.SIZE_NOLIMIT);
	
	if(ecrMgr.hasEcritureProtegeParAffiliation()){
		showProcessButton=false;
	}
%>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/avsParser.js"></SCRIPT>


<SCRIPT language="JavaScript">
top.document.title = "Extroune d'un CI"
var langue = "<%=languePage%>"; 

</SCRIPT>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %> 
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/autocomplete.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/selectionPopup.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT>
	function updateId(tag){
		if (tag.select && tag.select.selectedIndex != -1) {
			//alert(tag.select[tag.select.selectedIndex].idci);
			document.getElementById('compteIndividuelIdDestination').value=tag.select[tag.select.selectedIndex].idci;
			document.getElementById('nomSource').value=tag.select[tag.select.selectedIndex].nom;
			document.forms[0].elements('dateNaissanceSrc').value = tag.select[tag.select.selectedIndex].date;
			document.forms[0].elements('sexeSrc').value = tag.select[tag.select.selectedIndex].sexeFormate;
			document.forms[0].elements('paysSrc').value = tag.select[tag.select.selectedIndex].paysFormate;
		}
	}
	
	function resetAvs(){
		document.getElementById('compteIndividuelIdDestination').value="";
		document.getElementById('nomSource').value="";
					document.forms[0].elements('dateNaissanceSrc').value = "";
			document.forms[0].elements('sexeSrc').value = "";
			document.forms[0].elements('paysSrc').value = "";
		document.dernieresEcrituresDest.location.href="<%=request.getContextPath()%>/pavoRoot/allentries.jsp"	
	}
			
	function refreshEcritures(){
		document.dernieresEcrituresDest.location.href="<%=request.getContextPath()%>/pavoRoot/allentries.jsp?compteIndividuelId=" + document.getElementById('compteIndividuelIdDestination').value;
		
	}
	function confirmExtourne(){
		if(window.confirm("Wollen Sie den IK Umbuchen?")){
			document.forms[0].submit();
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
	
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Extourne des écritures d'un CI<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
						<TR>
							<TD>
								Compte source
							</TD>
							<TD colspan ="3">
								<INPUT type="text" name="numeroAvsInv" size="17" class="disabled" tabindex="-1" readonly value="<%=viewBean.getCi().getNssFormate()%>">
								&nbsp;
								<INPUT type="text" name="nomInv" size="70" class="disabled" tabindex="-1" readonly value="<%=viewBean.getCi().getNomPrenom()%>">
							</TD>
						</tr>
						<tr>
							<td>
								Date de naissance &nbsp;
							</td>
							<td>
							<input type="text" size = "10" class='disabled' name="dateNaissance" readonly tabindex='-1' value = "<%=viewBean.getCi().getDateNaissance()%>">
							&nbsp;
							Sexe &nbsp;
							<input type="text" size = "7" class='disabled' name="sexe" readonly tabindex='-1' value = "<%=viewBean.getCi().getSexeLibelle()%>">
							Pays &nbsp;
							<input type="text" size = "50" class='disabled' name="pays" readonly tabindex='-1' value = "<%=viewBean.getCi().getPaysFormate()%>">
							</td>
						<tr>
						<tr>
						<td valign="top">Inscriptions CI &nbsp;</td>
						<td valign="top" colspan="4"><IFRAME name="dernieresEcritures"
							scrolling="YES" style="border: solid 1px black; width: 14.2cm"
							height="200" tabindex="-1"> </IFRAME> <script>document.dernieresEcritures.location.href='<%=request.getContextPath()%>/pavoRoot/allentries.jsp?compteIndividuelId=<%=viewBean.getCompteIndividuelId()%>';</script>
							
						</td>
						</tr>
						
							
							<TD>
								Compte de destination
							</td>
							<td>
								<nss:nssPopup name="avsNew" value="" onFailure="resetAvs();" 
								onChange="updateId(tag);refreshEcritures();" jspName="<%=jspLocation%>" 
								avsAutoNbrDigit="11" avsMinNbrDigit="8" nssAutoNbrDigit="10" nssMinNbrDigit="7"/>
								&nbsp;
								<script>
									document.getElementById('avsNew').focus();
								</script>
								<INPUT type="text" size="70" name="nomSource" value="" class="disabled" tabindex="-1" readonly>
								<INPUT type="hidden" name="compteIndividuelIdDestination" value="">
								<INPUT type="hidden" name="compteIndividuelId"
								value="<%=viewBean.getCompteIndividuelId()%>"></TD>
						</tr>
												<tr>
							<td>
								Date de naissance &nbsp;
							</td>
							<td>
							<input type="text" size = "10" class='disabled' name="dateNaissanceSrc" readonly tabindex='-1' value = "">
							&nbsp;
							Sexe &nbsp;
							<input type="text" size = "7" class='disabled' name="sexeSrc" readonly tabindex='-1' value = "">
							Pays &nbsp;
							<input type="text" size = "50" class='disabled' name="paysSrc" readonly tabindex='-1' value = "">
							</td>
						<tr>
						<tr>
						<td valign="top">Inscriptions CI &nbsp;</td>
						<td valign="top" colspan="4"><IFRAME name="dernieresEcrituresDest"
							scrolling="YES" style="border: solid 1px black; width: 14.2cm"
							height="200"tabindex="-1" > </IFRAME> <script></script>
						</td>
						</tr>
						<tr>
							<TD>
								Période
							</TD>
							<TD>
								De &nbsp;
								<input type="text" size="4" maxlength="4" name="anneeDebut" value=""> &nbsp;
								à &nbsp;
								<input type="text" size="4" maxlength="4" name="anneeFin" value="">
								
							</TD>
								
								
						</TR>
						<tr>
							<TD colspan="3">
								Il est recommandé de verifier les CIs des conjoints, si les assurés ont des ecritures de splitting!
							</TD>
						</tr>
						
					
						
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>