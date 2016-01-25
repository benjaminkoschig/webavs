 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
		idEcran="CCP2006";
    	globaz.phenix.db.listes.CPListeDecisionsManquantesViewBean viewBean = (globaz.phenix.db.listes.CPListeDecisionsManquantesViewBean)session.getAttribute ("viewBean");
		selectedIdValue = "";
		userActionValue = "phenix.listes.listeDecisionsManquantes.executer";
%>
<%
String MSG_PROCESS_OK = "The process successfully started.";
if ("FR".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "La t�che a d�marr� avec succ�s.";
} else if ("DE".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "Prozess erfolgreich gestartet.";
}
%>
<SCRIPT language="JavaScript">
top.document.title = "Decisions manquantes - Liste"
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
// Si la case "Tout le journal" est coch�e les champs "De" et "�" sont d�sactiv�s
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
}
function postInit(){
	if (document.forms[0].elements('fromAnneeDecision').value == "") {
		document.forms[0].elements('fromAnneeDecision').focus();
	} else if (document.forms[0].elements('toAnneeDecision').value == "") {
		document.forms[0].elements('toAnneeDecision').focus();
	} else if (document.forms[0].elements('eMailAddress').value == "") {
		document.forms[0].elements('eMailAddress').focus();
	}
}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Liste des d�cisions manquantes<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
         <tr>
            <TD>De</TD>
            <TD> 
              <INPUT type="text" name="fromAnneeDecision" data-g-string="mandatory:true" maxlength="4" size="4" onkeypress="return filterCharForInteger(window.event);">
            </TD>
            <TD>�</TD>
            <TD> 
              <INPUT type="text" name="toAnneeDecision" data-g-string="mandatory:true" value="<%=globaz.globall.util.JACalendar.getYear(globaz.globall.util.JACalendar.todayJJsMMsAAAA()) - 1%>" maxlength="4" size="4" onkeypress="return filterCharForInteger(window.event);">
            </TD>
         </tr>
         <TR> 
            <TD>Inclure les d�cisions � l'�tat calcul�</TD>
            <TD colspan="3"><input type="checkbox" name="inclureEtatCalcul"></TD>
          </TR>
	  	 <TR>
            <TD>Adresse E-Mail</TD>
            <TD colspan="3"><input name='eMailAddress' class='libelleLong' data-g-string="mandatory:true" value='<%=viewBean.getEMailAddress()%>'></TD>
         </TR>
		 <% 
		 if ("yes".equalsIgnoreCase(request.getParameter("processStarted"))) { 
		 %>
		 <TR class="title">
			<TD colspan="4" style="color:white; text-align:center">
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