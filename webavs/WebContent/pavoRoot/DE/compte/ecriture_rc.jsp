
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page import="globaz.globall.util.*,globaz.pavo.db.compte.*" %>
<%
    CICompteIndividuelViewBean viewBean = (CICompteIndividuelViewBean)session.getAttribute ("viewBeanFK");
    IFrameHeight = "290";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	idEcran = "CCI0003";
	if(viewBean.CS_REGISTRE_PROVISOIRE.equals(viewBean.getRegistre())){
		bButtonNew = false;
	}
	
	if(viewBean.CS_REGISTRE_HISTORIQUE.equals(viewBean.getRegistre())){
		bButtonNew = false;
	} 
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/avsParser.js"></SCRIPT>
<SCRIPT>
<% if(!JAUtil.isIntegerEmpty(viewBean.getCompteIndividuelId())) { %>
usrAction = "pavo.compte.ecriture.lister";
<% } %>
top.document.title = "IK - Anzeige der IK-Buchungen";

<% if(!viewBean.hasUserShowRight(null)) {
	bButtonFind = false;
	bButtonNew = false; %>
<% } %>

timeWaiting = 1;
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
function simuleClickFind() {
	document.getElementById('btnFind').click();
}

function updateCompteIndividuel(data) {		
	arrayOfStrings = data.split("#");
	var compteId = "";	
	var numAvs = "";	
	var name = "";
	var dateNaissance = "";
	
	if (arrayOfStrings.length>=1) {
		compteId = arrayOfStrings[0];		
	}
	if (arrayOfStrings.length>=2) {
		numAvs = arrayOfStrings[1];
	}
	if (arrayOfStrings.length>=3) {
		name = arrayOfStrings[2];
	}	
	
	if (arrayOfStrings.length>=4) {
		dateNaissance = arrayOfStrings[3];
	}	

	document.forms[0].elements('forCompteIndividuelId').value=compteId;
	document.forms[0].elements('numeroAvsInv').value=numAvs;
	document.forms[0].elements('nomInv').value=name;
	document.forms[0].elements('naissance').value=dateNaissance;	
	

	document.all('btnFind').click();

	parent.fr_main.location.href = "<%=(servletContext + mainServletPath)%>?userAction=pavo.compte.ecriture.chercherEcriture&compteIndividuelId="+compteId;
	
	
}
bFind = true;

function detailAssure() {
	this.location.href='<%=servletContext + mainServletPath%>?userAction=pavo.compte.compteIndividuel.afficher&selectedId='+document.forms[0].elements('forCompteIndividuelId').value+'&mainSelectedId='+<%=viewBean.getCompteIndividuelId()%>;
}

function updateLike(data){
	if(data=="beitragsjahr" || data=="ordreInscription"){
		document.getElementById('fromAnnee').style.visibility = 'visible';
		document.getElementById('fromDateInscription').style.visibility='hidden';
		document.getElementById('fromPartenaire').style.visibility='hidden';
		document.getElementById('fromEmployeur').style.visibility='hidden';
		document.getElementById('fromAnnee').style.display='inline';
		document.getElementById('fromDateInscription').style.display='none';
		document.getElementById('fromPartenaire').style.display='none';
		document.getElementById('fromEmployeur').style.display='none';
		document.getElementById('fromDateInscription').value='';
		document.getElementById('fromPartenaire').value='';
		document.getElementById('fromEmployeur').value='';
		document.getElementById('anchor_fromDateInscription').style.visibility='hidden';
		document.getElementById('anchor_fromDateInscription').style.display='none';
		
	}
	if(data=="date"){
		document.getElementById('fromAnnee').style.visibility = 'hidden';
		document.getElementById('fromDateInscription').style.visibility='visible';
		document.getElementById('fromPartenaire').style.visibility='hidden';
		document.getElementById('fromEmployeur').style.visibility='hidden';
		document.getElementById('fromAnnee').style.display='none';
		document.getElementById('fromDateInscription').style.display='inline';
		document.getElementById('fromPartenaire').style.display='none';
		document.getElementById('fromEmployeur').style.display='none';
		document.getElementById('fromAnnee').value='';
		document.getElementById('fromPartenaire').value='';
		document.getElementById('fromEmployeur').value='';
		document.getElementById('anchor_fromDateInscription').style.visibility='visible';
		document.getElementById('anchor_fromDateInscription').style.display='inline';
		
	}	
	if(data=="numAvs"){
		document.getElementById('fromAnnee').style.visibility = 'hidden';
		document.getElementById('fromDateInscription').style.visibility='hidden';
		document.getElementById('fromPartenaire').style.visibility='visible';
		document.getElementById('fromEmployeur').style.visibility='hidden';
		document.getElementById('fromAnnee').style.display='none';
		document.getElementById('fromDateInscription').style.display='none';
		document.getElementById('fromPartenaire').style.display='inline';
		document.getElementById('fromEmployeur').style.display='none';
		document.getElementById('fromAnnee').value='';
		document.getElementById('fromDateInscription').value='';
		document.getElementById('fromEmployeur').value='';
		document.getElementById('anchor_fromDateInscription').style.visibility='hidden';
		document.getElementById('anchor_fromDateInscription').style.display='none';
		
	}
	if(data=="numAff"){
		document.getElementById('fromAnnee').style.visibility = 'hidden';
		document.getElementById('fromDateInscription').style.visibility='hidden';
		document.getElementById('fromPartenaire').style.visibility='hidden';
		document.getElementById('fromEmployeur').style.visibility='visible';
		document.getElementById('fromAnnee').style.display='none';
		document.getElementById('fromDateInscription').style.display='none';
		document.getElementById('fromPartenaire').style.display='none';
		document.getElementById('fromEmployeur').style.display='inline';
		document.getElementById('fromAnnee').value='';
		document.getElementById('fromDateInscription').value='';
		document.getElementById('fromPartenaire').value='';
		document.getElementById('anchor_fromDateInscription').style.visibility='hidden';
		document.getElementById('anchor_fromDateInscription').style.display='none';
		
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

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%
String jspLocation = servletContext + mainServletPath + "Root/ci_select.jsp";
String jspLocation2 = servletContext + mainServletPath + "Root/ti_select.jsp";

String fromPartenaire = request.getParameter("fromPartenaire");

int autoDigiAff = globaz.pavo.util.CIUtil.getAutoDigitAff(session);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Anzeige der IK-Buchungen<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>


					<TR>
					

						<TD width="15"><a href="javascript:detailAssure();">Versicherte</a></TD>
						<% 
 		  		Object[] ciLies = viewBean.getCILies();
 		  		if (ciLies.length>0) {%> 
 		  					<TD nowrap>
 		  					<select name="numAvsList"
							onchange="updateCompteIndividuel(this.options[this.selectedIndex].value);">
							<option SELECTED
								value="<%=viewBean.getCompteIndividuelId()+"#"+viewBean.getNssFormate()+"#"+viewBean.getNomPrenom()+"#"+viewBean.getDateNaissance()%>">
							<%=viewBean.getNssFormate() + " " + viewBean.getNomPrenom()%></option>
							<%
	 				for(int i=0;i<ciLies.length;i++) {
	 					String [] link = (String[])ciLies[i];
	 					if(!"".equals(link[0])) { %>
							<option value="<%=link[0]+"#"+link[1]+"#"+link[2]+"#"+link[3]%>">
							<%=link[1] + " " + link[2] + " "+ link[3]%></option>
							<% }
	                  } 
	                %>
						</select> </TD>
							<INPUT type="hidden" name="numeroAvsInv"						 
							value="<%=viewBean.getNssFormate()%>">	
							
							<INPUT type="hidden" name="nomInv" size="55" class="disabled"
							readonly value="<%=viewBean.getNomPrenom()%>"> 
							<INPUT
							type="hidden" name="forCompteIndividuelId"
							value="<%=viewBean.getCompteIndividuelId()%>">
						
						<%}else{%>

			
						<TD colspan="3" nowrap><INPUT type="text" name="numeroAvsInv"
							size="17" class="disabled" readonly
							value="<%=viewBean.getNssFormate()%>">

						<INPUT type="text" name="nomInv" size="77" class="disabled"
							readonly value="<%=viewBean.getNomPrenom()%>"> <INPUT
							type="hidden" name="forCompteIndividuelId"
							value="<%=viewBean.getCompteIndividuelId()%>"> <!--INPUT type="hidden" name="forIdTypeCompteCompta" value="true"-->

						</TD>
						<%}%>
						

					</TR>
					<tr>
						<td>
						Geburtsdatum
							
						</td>
						<td>
						<INPUT type="text" name="naissance" class="disabled"
							size="11" readonly value="<%=viewBean.getDateNaissance()%>">
						&nbsp;
							Geschlecht
						
						&nbsp;
							<INPUT type="text" name="sexe" class="disabled"
								size="11" readonly value="<%=viewBean.getSexeLibelle()%>">
								
						
						</td>
						<td>
							&nbsp;Heimatstaat&nbsp;
						</td>
						<td>
							<INPUT type="text" name="pays" class="disabled"
								size="45" readonly value="<%=viewBean.getPaysFormate()%>"></TD>
						</td>
							
					</tr>
					<!--<TR>


					</TR>
					<TR>
						<TD><br>
						</TD>
					</TR>

					<!--
					<TR>
						  <TD nowrap width="100">Nom et prénom</TD>
                          <TD nowrap width="400">
                                <INPUT type="text" name="nom" class="disabled" size="40" readonly value="<%=viewBean.getNomPrenom()%>">
                          </TD>
					</TR>
					-->
					
					
					<TR>
					
            			<TD nowrap width="159">Sortierung</TD>
            			<TD nowrap><SELECT id='tri' name='tri' doClientValidation=''>
            			<OPTION value="beitragsjahr" <%=!"27".equals(globaz.pavo.util.CIUtil.getCaisseInterneEcran())?"selected":""%>>Beitragsjahr</OPTION>
            			<OPTION value="date">Buchungsdatum</OPTION>
            			<OPTION value="numAvs">SVN</OPTION> 
            			<OPTION value="numAff">Abr.-Nr.</OPTION>
            			<OPTION value="ordreInscription" <%="27".equals(globaz.pavo.util.CIUtil.getCaisseInterneEcran())?"selected":""%> >Beitragsjahr/Buchungsordnung</OPTION>
            			</SELECT>
						<script>
							document.getElementById('tri').onfocus = new Function("","return updateLike(this.options[this.selectedIndex].value);");
							document.getElementById('tri').onchange = new Function("","return updateLike(this.options[this.selectedIndex].value);");
						</script>
          				</TD>
						<TD nowrap width="50">&nbsp;Status</TD>
						<%
				java.util.HashSet typeCompte = new java.util.HashSet();
    			//typeCompte.add(globaz.pavo.db.compte.CIEcriture.CS_GENRE_6);
				//typeCompte.add(globaz.pavo.db.compte.CIEcriture.CS_GENRE_7);
				//typeCompte.add(globaz.pavo.db.compte.CIEcriture.CS_TEMPORAIRE);
				typeCompte.add(globaz.pavo.db.compte.CIEcriture.CS_TEMPORAIRE_SUSPENS);
			%>
						<TD nowrap colspan="2">
							
							<ct:FWCodeSelectTag name="forIdTypeCompte"
							defaut="<%=viewBean.getValeurAAfficherParDefaut()%>"
							codeType="CITYPCOM"  except="<%=typeCompte%>" wantBlank="true"/>
							<script>
								document.getElementById("forIdTypeCompte").onfocus = new Function("","return updateLike(this.options[this.selectedIndex].value);");
								document.getElementById("forIdTypeCompte").onchange = new Function("","simuleClickFind();");
							</script>
						<% if(viewBean.hasSuspens(null)){ %> <img
							src="<%=request.getContextPath()%>/images/avertissement.gif"
							border="0"> <% } %>
							<%if(viewBean.hasCorrection(null)){%>
							&nbsp; <B>Verlauf</B>
						<%}%>		
							</TD>
					</TR>
					<TR>
						<TD nowrap width="80">Ab</TD>
						<TD nowrap>
	            <SCRIPT LANGUAGE='JavaScript'>var cal_fromDateInscription = new CalendarPopup();cal_fromDateInscription.showYearNavigation();cal_fromDateInscription.setMonthAbbreviations('Jan','Fév','Mars','Avr','Mai','Juin','Juil','Août','Sep','Oct','Nov','Déc');cal_fromDateInscription.setThisMonthText("Mois courant");cal_fromDateInscription.setMonthNames('Janvier','Février','Mars','Avril','Mai','Juin','Juillet','Août','Septembre','Octobre','Novembre','Décembre');cal_fromDateInscription.setDayHeaders('D','L','M','M','J','V','S');cal_fromDateInscription.setTodayText("Aujourd'hui");</SCRIPT>
	            <INPUT TYPE='text' NAME='fromDateInscription' VALUE='' SIZE=10 maxlength=10 onBlur="fieldFormat(this,'CALENDAR')" > <input value="..." type=button name="anchor_fromDateInscription" id="anchor_fromDateInscription" onClick="cal_fromDateInscription.select(fromDateInscription,'anchor_fromDateInscription','dd.MM.yyyy'); return false;" onBlur="fieldFormat(fromDateInscription,'CALENDAR')">
	            <script>
	            	document.getElementById('fromDateInscription').style.visibility='hidden';
					document.getElementById('fromDateInscription').style.display='none';
					document.getElementById('anchor_fromDateInscription').style.visibility='hidden';
					document.getElementById('anchor_fromDateInscription').style.display='none';
		
				</script>
	            <INPUT type="text" name="fromAnnee" size="4" maxlength="4" value=''>
	            
	            <ct:FWPopupList name="fromPartenaire" value=""  maxlength="14" className="libelle" onFailure="formatAVS('fromPartenaire');" onChange="avsAction('fromPartenaire');" jspName="<%=jspLocation%>" autoNbrDigit="11" size="14" minNbrDigit="8"/>
	            	<SCRIPT>
					document.getElementById("fromPartenaire").style.visibility='hidden';
					document.getElementById("fromPartenaire").style.display='none';
				</script>
				<ct:FWPopupList name="fromEmployeur" value="" className="libelle" jspName="<%=jspLocation2%>" autoNbrDigit="<%=autoDigiAff%>" size="16" minNbrDigit="3"/>
				<script>
					document.getElementById("fromEmployeur").style.visibility='hidden';
					document.getElementById("fromEmployeur").style.display='none';
	            </script>
	            </TD>
						<TD nowrap width="50">&nbsp;Abschlüsse</TD>
						<TD nowrap colspan="2"><ct:FWListSelectTag name="etatEcritures"
							defaut="toutesEcritures" data="<%=viewBean.getListClotures()%>" />
							<script>
								document.getElementById("etatEcritures").onchange = new Function("","simuleClickFind();");
							</script>
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