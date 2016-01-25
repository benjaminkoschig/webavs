 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	idEcran="CCP1005";
    globaz.phenix.db.communications.CPCommunicationImprimerViewBean viewBean = (globaz.phenix.db.communications.CPCommunicationImprimerViewBean)session.getAttribute ("viewBean");
	selectedIdValue = "";
	userActionValue = "phenix.communications.communicationFiscaleAffichage.imprimer";
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
top.document.title = "Steuermeldungen - Ausdruck"
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/ValidationGroups.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

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
function showHideAdditionnalData() {
	if (document.getElementById("anneeDecision").value != "") {
		jscss("add", document.getElementById("withAnneeEnCours"), "hidden");
		jscss("add", document.getElementById("libelleWithAnneeEnCours"), "hidden");
	} else {
		jscss("remove", document.getElementById("withAnneeEnCours"), "hidden");
		jscss("remove", document.getElementById("libelleWithAnneeEnCours"), "hidden");
	}
}
function init(){
}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ausdruck der Steuermeldungen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
		  <TR> 
            <TD height="20" width="150">Liste ausdrucken</TD>
            <TD nowrap height="31" width="259"><input type="checkbox" name="impressionListe" "checked"></TD>
          </TR>
          <tr>
            <TD width="150" height="20">Mitgliedsart</TD>
            <TD width="266">
             <%
				java.util.HashSet except = new java.util.HashSet();
				except.add(globaz.phenix.db.principale.CPDecision.CS_TSE);
				except.add(globaz.phenix.db.principale.CPDecision.CS_NON_SOUMIS);
				except.add(globaz.phenix.db.principale.CPDecision.CS_RENTIER);
				except.add(globaz.phenix.db.principale.CPDecision.CS_AGRICULTEUR);
				except.add(globaz.phenix.db.principale.CPDecision.CS_ETUDIANT);
				except.add(globaz.phenix.db.principale.CPDecision.CS_FICHIER_CENTRAL);
			%>
            <ct:FWCodeSelectTag name="genreAffilie"
					defaut="<%=((globaz.phenix.application.CPApplication) viewBean.getSession().getApplication()).getGenreDecisionDefaut()%>"
					wantBlank="<%=true%>"
			        codeType="CPGENDECIS"
		            except="<%=except%>"
		/>
            </TD>
            </tr>
          <tr>
            <TD width="279" height="20">Verfügungsjahr</TD>
           <td> 
               <INPUT id="anneeDecision" type="text" name="anneeDecision" maxlength="4" size="4" onkeyup="showHideAdditionnalData()">
              <span id="libelleWithAnneeEnCours"> Inklusive laufendes jahr</span>
              <INPUT type="checkbox" name="withAnneeEnCours"> </td>
            </tr>
	   <tr>
            <TD width="279" height="20">Kanton</TD>
            <TD width="266"><ct:FWCodeSelectTag name="canton"
					defaut="<%=((globaz.phenix.application.CPApplication) viewBean.getSession().getApplication()).getCantonDefaut()%>"
					wantBlank="<%=true%>"
                    codeType="PYCANTON"
					libelle="codeLibelle"/>
          </TR>
          <tr>
            <TD width="300" height="20">Nur jene drucken, welche noch nie gedruckt wurden</TD>
            <td height="2"> 
              <INPUT type="checkbox" name="dateEnvoiVide" checked="checked"></td>
          </tr>
          <tr>
            <TD width="279" height="20">Nur jene drucken, deren Versandsdatum entspricht</TD>
            <TD width="266">
		<ct:FWCalendarTag name="dateEnvoi" 
		value="" 
		errorMessage="la date de début est incorrecte"
		doClientValidation="CALENDAR"/>
           </TD>
          </tr>
          <tr>
            <TD width="279" height="20">Ausgabedatum</TD>
            <TD width="266">
				<ct:FWCalendarTag name="dateEdition" 
					value="<%=globaz.globall.util.JACalendar.todayJJsMMsAAAA()%>" 
					doClientValidation="CALENDAR"/>
           </TD>
          </tr>
	   <TR>
            <TD height="20">E-Mail Adresse</TD>
            <TD><input name='eMailAddress' class='libelleLong' data-g-string="mandatory:true" value='<%=viewBean.getSession().getUserEMail()%>'></TD>
          </TR>
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