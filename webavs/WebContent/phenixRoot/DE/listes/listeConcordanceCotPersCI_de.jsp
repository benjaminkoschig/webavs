 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<% 
int autoDigiAff = globaz.phenix.util.CPUtil.getAutoDigitAff(session);
String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
		idEcran="CCP2012";
    	globaz.phenix.db.listes.CPListeConcordanceCotPersCIViewBean viewBean = (globaz.phenix.db.listes.CPListeConcordanceCotPersCIViewBean)session.getAttribute ("viewBean");
		selectedIdValue = "";
		userActionValue = "phenix.listes.listeConcordanceCotPersCI.executer";
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
top.document.title = "Liste - Fehlende Verfügungen"
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

function init(){
}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Liste Persönliche AHV-Beiträge - IK<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
         <tr>
            <TD width="300">Verfügungsjahr</TD>
            <TD>
            <INPUT type="text" name="forAnnee" maxlength="4" size="4" value='<%=viewBean.getForAnnee()%>' onkeypress="return filterCharForInteger(window.event);" class="numeroCourt">
             </TD>
            <TD></TD>
            <TD></TD>
         </tr>
         <tr> 
            <TD width="300" height="20">Ab Mitglied-Nr.</TD>
             <td>
	           	<ct:FWPopupList 
	           		name="fromNoAffilie" 
	           		value="<%=viewBean.getFromNoAffilie()%>" 
	           		className="libelle" 
	           		jspName="<%=jspLocation%>" 
	           		autoNbrDigit="<%=autoDigiAff%>" 
	           		size="20"
	           		minNbrDigit="3"
	       		/>
	           	<SCRIPT>
	           		document.getElementById("fromNoAffilie").onkeypress= new Function ("","return filterCharForPositivFloat(window.event);");
	           	</SCRIPT>
	          </td>
	        <TD></TD>
            <TD></TD>
          </tr>
          <tr> 
            <TD width="300">Bis Mitglied-Nr.</TD>
            <td>
            <ct:FWPopupList 
	           		name="tillNoAffilie" 
	           		value="<%=viewBean.getTillNoAffilie()%>" 
	           		className="libelle" 
	           		jspName="<%=jspLocation%>" 
	           		autoNbrDigit="<%=autoDigiAff%>" 
	           		size="20"
	           		minNbrDigit="3"
	       		/>
	       	</td>
	       	<TD></TD>
            <TD></TD>
          </tr>
          <tr>
            <TD width="300">Difference erlaubt</TD>
            <TD> 
              <INPUT type="text" name="fromDiffAdmise" maxlength="4" size="4" value='<%=viewBean.getFromDiffAdmise()%>' class="numeroCourt">
            &nbsp;&nbsp;à&nbsp;&nbsp;
             <INPUT type="text" name="toDiffAdmise" value='<%=viewBean.getToDiffAdmise()%>' maxlength="4" size="4" class="numeroCourt">
             </TD>
            <TD></TD>
            <TD></TD>
         </tr>
      	 <TR>
            <TD width="300">E-Mail Adresse</TD>
            <TD><input name='eMailAddress' class='libelleLong' data-g-string="mandatory:true" value='<%=viewBean.getEMailAddress()%>'></TD>
            <TD></TD>
            <TD></TD>
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