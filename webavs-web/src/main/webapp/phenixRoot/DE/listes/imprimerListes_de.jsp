  
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%idEcran="CCP2002";
globaz.phenix.db.listes.CPImprimerListesViewBean viewBean = (globaz.phenix.db.listes.CPImprimerListesViewBean)session.getAttribute ("viewBean");
showProcessButton = false;
%>


<SCRIPT language="JavaScript">
//permet de jongler avec les pages
function lien(){
	i = document.mainForm.userChoice.selectedIndex;
	url="phenix?userAction=phenix.listes.imprimerListes.afficher";

	if(i == 1) url='phenix?userAction=phenix.listes.listeDecisions.afficher';
	if(i == 2) url='phenix?userAction=phenix.listes.listeDecisionsManquantes.afficher';
	if(i == 3) url='phenix?userAction=phenix.listes.listeDecisionsNonDefinitives.afficher';
	if(i == 4) url='phenix?userAction=phenix.listes.listeDecisionsPeriode.afficher';
	if(i == 5) url='phenix?userAction=phenix.listes.listeDecisionsAnneesMultiples.afficher';
	if(i == 6) url='phenix?userAction=phenix.listes.listeMontantsCotisationsDifferents.afficher';
	if(i == 7) url='phenix?userAction=phenix.listes.listeConcordanceCotPersCI.afficher';
	if(i == 8) url='phenix?userAction=phenix.listes.listeConcordanceCotPersCompta.afficher';
	if(i == 9) url='phenix?userAction=phenix.listes.listeDecisionsDefinitives.afficher';

	location.href = url;
}

top.document.title = "Ausdruck der Liste"
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

// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Liste drucken<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
		<tr>
            <td> Auswahl der Liste </td>
            <td>
            	<select name="userChoice" style="width:10cm;" onchange="lien();">
            		<option></option>
            		<option>Liste der Verf?gungen</option>
	           		<option>Liste der fehlenden Verf?gungen</option>
	           		<option>Liste der zu nehmende Verf?gungen </option>
	           		<option>Liste der verbuchten Verf?gungen w?hrend einer bestimmten Periode.</option>
	           		<option>Liste gleiches Jahr in einem Job</option>
	           		<option>Liste Differenz Anschluss-Beitr?ge</option>
	           		<option>Liste Pers?nliche AHV-Beitr?ge - IK</option>
	           		<option>Liste Pers?nliche AHV-Beitr?ge - Buchhaltung</option>
	           		<option>Liste der endg?ltig Verf?gungen</option>
            	</select>
            </td>
          </tr>
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