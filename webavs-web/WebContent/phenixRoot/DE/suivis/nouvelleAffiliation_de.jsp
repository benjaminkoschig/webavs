
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
		idEcran="CCP0027";
		
    	globaz.phenix.vb.suivis.CPRevBilanViewBean viewBean = (globaz.phenix.vb.suivis.CPRevBilanViewBean)session.getAttribute ("viewBean");
		selectedIdValue = "";
		userActionValue = "phenix.suivis.nouvelleAffiliation.executer";
		subTableWidth = "75%";
		
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
top.document.title = "Acompte - Création"
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="CP-MenuPrincipal" showTab="menu"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers



function init(){
//Initialisation
}

function postInit(){
	if (document.forms[0].elements('eMailAddress').value == "") {
		document.forms[0].elements('eMailAddress').focus();
	}
}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Demande de revenu et bilan pour une nouvelle affiliation<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
        
          <TR>
            <TD>Adresse E-Mail</TD>
            <TD>
              <input name='eMailAddress' class='libelleLong' data-g-string="mandatory:true" value='<%=viewBean.getEMailAddress()%>'>
            </TD>
          </TR>
          <TR>
          	<TD>Date début</TD>
          	<TD><ct:FWCalendarTag name="dateDebut" 
			value="<%=viewBean.getDateDebut()%>" 
			/></TD>
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