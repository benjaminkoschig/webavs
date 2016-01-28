 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
		idEcran="CCP2001";
    	globaz.phenix.db.principale.CPDecisionImprimerLotViewBean viewBean = (globaz.phenix.db.principale.CPDecisionImprimerLotViewBean)session.getAttribute ("viewBean");
		selectedIdValue = "";
		userActionValue = "phenix.principale.decisionImprimerLot.executer";
		subTableWidth = "75%";
		String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
%>
<%
String MSG_PROCESS_OK = "The process successfully started.";
if ("FR".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "La tâche a démarré avec succès.";
} else if ("DE".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "Der Prozess wurde erfolgreich gestartet.";
}
%>
<SCRIPT language="JavaScript">
top.document.title = "Verfügungen - Jobausdruck"
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CP-OnlyDetail"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

<%@ include file="/scripts/infoRom/infoRom304.js" %>

<%
	int autoDigiAff = globaz.phenix.util.CPUtil.getAutoDigitAff(session);
%>
function toutOrNotTout(source) {
// Si la case "Tout le journal" est cochée les champs "De" et "à" sont désactivés
	if (source.checked) {
		document.forms[0].fromAvs.value = "";
		document.forms[0].fromAvs.disabled = true;
		document.forms[0].toAvs.value = "";
		document.forms[0].toAvs.disabled = true;
	} else {
		document.forms[0].fromAvs.disabled = false;
		document.forms[0].toAvs.disabled = false;
	}
}

function init(){
//Initialisation
	document.forms[0].dateImpression.value="<%=globaz.globall.util.JACalendar.today().toString()%>";
	fieldFormat(document.forms[0].debutRelation,"CALENDAR");
}

/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ausdruck der Verfügungen per Job<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
          
          <TR id="erreurNumAffObligatoire">
          	<TD colspan="2"><font color="red"><B>Sie müssen eine Mitglied-Nr. eingeben</B></font></TD>
          </TR>
          
          <tr> 
            <TD width="50%" height="20">Job</TD>
            <TD width="50%"> 
              <INPUT type="text" name="idPassage" maxlength="15" size="15"  value="<%=viewBean.getIdPassage()%>">
              <%
			Object[] psgMethodsName = new Object[]{
				new String[]{"setIdPassage","getIdPassage"},
				new String[]{"setLibellePassage","getLibelle"},
				new String[]{"setDateImpression","getDateFacturation"}
			};
			Object[] psgParams= new Object[]{};
			String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/principale/decisionImprimerLot_de.jsp";	
			%>
			<!--
			-->
            <ct:ifhasright element="musca.facturation.passage.chercher" crud="r">
            <ct:FWSelectorTag 
			name="passageSelector" 
			
			methods="<%=psgMethodsName%>"
			providerPrefix="FA"			
			providerApplication ="musca"			
			providerAction ="musca.facturation.passage.chercher"			
			providerActionParams ="<%=psgParams%>"
			redirectUrl="<%=redirectUrl%>"			
			/> 
			</ct:ifhasright>
			<input type="hidden" name="selectorName" value="">
			</TD>
			</tr>
          <tr> 
            <TD width="50%" height="20"></TD>
            <TD nowrap width="50%"> 
              <INPUT type="text" name="libellePassage" class="libelleLongDisabled" value="<%=viewBean.getLibellePassage()%>" readonly>
            </TD>
          </tr>
           <tr> 
            <TD width="50%" height="20">&nbsp;</TD>
           </tr>
           
           <TR>
	          	<TD>Ausdruck für ein einziges Mitglied</TD>
	          	<TD>
	          		<input type="checkbox" id="chkImpressionUnSeulAffilie" name="chkImpressionUnSeulAffilie"  onclick="showHidePlageNumAffInput();clearInputsNumAff();">	
	       			<INPUT type="hidden" id="valueKeeperChkImpressionUnSeulAffilie" name="valueKeeperChkImpressionUnSeulAffilie">
	       		</TD>
			</TR>
           
        <tr id="plageNumAff">
            <TD width="50%" height="20">Abr-Nr.</TD>
             <td>von:&nbsp;
	           	<ct:FWPopupList 
	           		name="fromAffilieDebut" 
	           		value="<%=viewBean.getFromAffilieDebut()%>" 
	           		className="libelle" 
	           		jspName="<%=jspLocation%>" 
	           		autoNbrDigit="<%=autoDigiAff%>" 
	           		size="20"
	           		minNbrDigit="3"
	       		/>
	           	<SCRIPT>
	           		document.getElementById("fromAffilieDebut").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
	           	</SCRIPT>
	           	 &nbsp;&nbsp;&nbsp;&nbsp;bis:&nbsp;
            <ct:FWPopupList 
	           		name="fromAffilieFin" 
	           		value="<%=viewBean.getFromAffilieFin()%>" 
	           		className="libelle" 
	           		jspName="<%=jspLocation%>" 
	           		autoNbrDigit="<%=autoDigiAff%>" 
	           		size="20"
	           		minNbrDigit="3"
	       		/>
	       	</td>
          </tr>
          
          <TR id="oneNumAff" >
          	<TD>Abr-Nr.</TD>
	        <TD> <INPUT type="text" id="forIdExterneRole" name="forIdExterneRole" size="20" maxlength="40" value="<%=viewBean.getFromAffilieDebut()%>" onchange="setPlageNumAff()"></TD>
          </TR>
          
          <tr> 
            <TD width="50%" height="20">&nbsp;</TD>
           </tr>
          <tr> 
            <TD width="50%" height="20">Mitgliedart</TD>
            <td>
            <%
			java.util.HashSet except = new java.util.HashSet();
			except.add(globaz.phenix.db.principale.CPDecision.CS_NON_SOUMIS);
			except.add(globaz.phenix.db.principale.CPDecision.CS_FICHIER_CENTRAL);
			%>
            <ct:FWCodeSelectTag name="forGenreAffilie"
					defaut=""
					wantBlank="<%=true%>"
			        codeType="CPGENDECIS"
			        except="<%=except%>"
			/>
	       	</td>
          </tr>
          <tr> 
            <TD width="50%" height="20">Verfügungstyp</TD>
            <td>
            <ct:FWCodeSelectTag name="forTypeDecision"
				defaut=""
				wantBlank="<%=true%>"
				codeType="CPTYPDECIS"
		       	/>
	       	</td>
          </tr>
           <tr> 
            <TD width="50%" height="20">&nbsp;</TD>
           </tr>
          <tr> 
            <TD width="50%" height="20">Zweigstellen-Nr.</TD>
            <td width="50%"> 
              <INPUT type="text" name="forAgence" maxlength="20" size="20">
            </td>
          </tr>  
          <tr> 
            <TD width="50%" height="20">Druckdatum auf der Verfügungen</TD>
            <TD width="50%"> 
              <ct:FWCalendarTag name='dateImpression' value="<%=viewBean.getDateImpression()%>" errorMessage="la date de début est incorrecte" doClientValidation="CALENDAR"/> 
            </TD>		
          </tr>        
          
          <tr>
          	<TD width="50%" height="20">Sortieren</TD>
          	<TD width="50%">
          	<select name="forOrder" tabindex="3">
          	<option value="3">Mitglied-Nr.</option>
          	<option value="1">Sachbearbeiter, Mitglied-Nr.</option>
          	<option value="2">Zweigstelle, Mitglied-Nr.</option>
  			</select>
  	      	</TD>
          </tr>
          <TR> 
            <TD height="50%" width="240" nowrap>E-Mail Adresse</TD>
            <TD width="50%"> 
              <input name='eMailAddress' class='libelleLong' data-g-string="mandatory:true" value='<%=viewBean.getEMailAddress()%>'>
            </TD>
          </TR>
          <TR> 
            <TD height="50%" width="212">Identische Verfügung</TD>
            <TD nowrap height="31"><input type="checkbox" name="impressionMontantIdentique">
            &nbsp;&nbsp; druckt die Fälle aus, von denen die durch die Steuermeldung generierte Verfügung zur aktuellen Verfügung keine Änderung aufweist</TD>
          </TR>
          <% 
          if (globaz.jade.ged.client.JadeGedFacade.isInstalled()) {
          //if(viewBean.isOptionGed()){
          %>
          <ct:ifhasright element="phenix.principale.decision.imprimerLot" crud="u">
          <TR> 
            <TD height="50%" width="212">DMS</TD>
            <TD nowrap height="31" width="259"><input type="checkbox" name="envoiGed" <%=(viewBean.getEnvoiGed().booleanValue())? "checked" : "unchecked"%>></TD>
          </TR>
          </ct:ifhasright>
          <%}%>
		<% 
		if ("yes".equalsIgnoreCase(request.getParameter("processStarted"))) { 
		%>
		<TR class="title">
			<TD colspan="2" style="color:white; text-align:center">
			<SPAN style="color:palegreen">&gt;</SPAN> <%=MSG_PROCESS_OK%> <SPAN style="color:palegreen">&lt;</SPAN>
			</TD>
		</TR>
		<% 
		}
		%>
		<input type="hidden" name="isShortProcess" value="false"/>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%  }  %>
<script>
// menu 

//top.fr_menu.location.replace('appMenu.jsp?_optionMenu=-defaut-&changeTab=Menu');	
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>