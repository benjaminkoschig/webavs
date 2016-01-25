 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.phenix.db.communications.*" %>
<%@ page import="java.util.LinkedList" %>
<%
	idEcran="CCP1001";
    globaz.phenix.db.communications.CPCommunicationEnvoyerViewBean viewBean = (globaz.phenix.db.communications.CPCommunicationEnvoyerViewBean)session.getAttribute ("viewBean");
	selectedIdValue = "";
	userActionValue = "phenix.communications.communicationEnvoyer.executer";
	CPReceptionReaderViewBean receptionReader = new CPReceptionReaderViewBean();
	receptionReader.setISession(viewBean.getSession());
%>
<%
String MSG_PROCESS_OK = "The process successfully started.";
if ("FR".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "La tâche a démarré avec succès.";
} else if ("DE".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "Prozess erfolgreich gestartet.";
}
%>
<SCRIPT language="JavaScript">
top.document.title = "Steuermeldungen - Sendung"
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

function postInit(){
	jscss("add", document.forms[0].cantonAncien, "hidden");
	document.forms[0].demandeAnnulee.disabled = true;
}

function showHideCanton() {
	if (eval(document.getElementById("throughSedex").checked == true)) {
		jscss("remove", document.forms[0].cantonSedex, "hidden");
		jscss("add", document.forms[0].cantonAncien, "hidden");
		document.forms[0].demandeAnnulee.disabled = true;
		jscss("remove", document.forms[0].donneesCommerciales, "hidden");
		jscss("remove", document.forms[0].donneesPrivees, "hidden");
		$("#libDonneesCommerciales").show();
		$("#libDonneesPrivees").show();
	}else{
		jscss("remove", document.forms[0].cantonAncien, "hidden");
		jscss("add", document.forms[0].cantonSedex, "hidden");
		document.forms[0].demandeAnnulee.disabled = false;
		jscss("add", document.forms[0].donneesCommerciales, "hidden");
		jscss("add", document.forms[0].donneesPrivees, "hidden");
		$("#libDonneesCommerciales").hide();
		$("#libDonneesPrivees").hide();	
	}
}

function init(){
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

function validate(){
	if (eval(document.getElementById("throughSedex").checked == true)) {
		document.forms[0].canton.value=document.forms[0].cantonSedex.value;
	}else{
		document.forms[0].canton.value=document.forms[0].cantonAncien.value;
	}
	return true;
}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Versand der Steuermeldungen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
		<TR>
			<TD width="279" height="20">Datei durch die Sedex Plattform senden</TD>	
			<TD><INPUT type="checkbox" name="throughSedex" id="throughSedex" onclick="showHideCanton()" checked="checked"></TD>
		</TR>
		<TR>
        	<TD width="279" height="20">Sendung der Annullierungsanträge</TD>
           <td height="2"> 
           		<INPUT type="checkbox" name="demandeAnnulee" > </td>
        </tr>
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
              <span id="libelleWithAnneeEnCours"> inklusive laufendes Jahr </span>
              <INPUT type="checkbox" name="withAnneeEnCours"> 
             </td>
            </tr>
	      <TR>
            <%
            	LinkedList cantonImplList = receptionReader.getReaders();
            %>
			<TD width="279">Kanton</TD>
            <TD>
	           <SELECT name="cantonAncien">
	            	<% if (!cantonImplList.isEmpty()) { %>
	            		<OPTION selected value='<%=cantonImplList.removeFirst()%>'><%=cantonImplList.removeFirst()%></OPTION>
					<% } %>
	            	<% while(!cantonImplList.isEmpty()){ %>
	            		<OPTION value='<%=cantonImplList.removeFirst()%>'><%=cantonImplList.removeFirst()%></OPTION>
	            	<% } %>
	            </SELECT>
			<%
            	String cantonDefaut = receptionReader.getCantonDefaut();
            %>
	        <ct:FWCodeSelectTag name="cantonSedex"
	      		defaut="<%=cantonDefaut%>"
	      		wantBlank="true"
	      		libelle="libelle"
	    	    codeType="PYCANTON"/>
	    	    <input type="hidden" name="canton"/>
	    	</TD>
		</TR>
		<TR>
            <TD width="300" height="20">Nur jene senden, welche noch nie gesendet wurden</TD>
            <td height="2"> 
              <INPUT type="checkbox" name="dateEnvoiVide" checked="checked"> </td>
          </tr>
          <tr>
            <TD width="279" height="20">Nur jene senden, deren Versandsdatum enspricht</TD>
            <TD width="266">
		<ct:FWCalendarTag name="dateEnvoi" 
		value="" 
		errorMessage="la date de début est incorrecte"
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