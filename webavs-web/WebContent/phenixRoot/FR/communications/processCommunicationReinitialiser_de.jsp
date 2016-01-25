 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
		idEcran="CCP1031";
    	globaz.phenix.db.communications.CPJournalRetourViewBean viewBean = (globaz.phenix.db.communications.CPJournalRetourViewBean)session.getAttribute ("viewBean");
		selectedIdValue = "";
		userActionValue = "phenix.communications.journalRetour.executerReinitialiser";
		subTableWidth = "75%";
		String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
		int autoDigiAff = globaz.phenix.util.CPUtil.getAutoDigitAff(session);
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
top.document.title = "Communications fiscales - Suppression"
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
			<%-- tpl:put name="zoneTitle" --%>Réinitialisation d'une communication fiscale<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
		<TR>
            <TD nowrap colspan="2" style="font-weight : bolder;">Ce programme permet de réinitialiser comme lors du chargement une communication fiscales en retour.</TD>
          </TR>
          <TR>
            <TD nowrap colspan="2" style="font-weight : bolder;">Il supprime aussi la décision qui aurait été générée selon la communication.</TD>
          </TR>
          <TR>
            <TD nowrap colspan="2">&nbsp;</TD>
          </TR>
          <tr> 
            <TD width="50%" height="20">N° affilié de départ</TD>
            <td>
            <ct:FWPopupList 
	           		name="fromNumAffilie" 
	           		value="" 
	           		className="libelle" 
	           		jspName="<%=jspLocation%>" 
	           		autoNbrDigit="<%=autoDigiAff%>" 
	           		size="20"
	           		minNbrDigit="3"
	       		/>
	       	</td>
          </tr>
          <tr> 
            <TD width="50%" height="20">N° affilié de fin</TD>
            <TD>
            <ct:FWPopupList 
	           		name="tillNumAffilie" 
	           		value="" 
	           		className="libelle" 
	           		jspName="<%=jspLocation%>" 
	           		autoNbrDigit="<%=autoDigiAff%>" 
	           		size="20"
	           		minNbrDigit="3"
	       		/>
	       	</td>
          </tr>
         <tr>
            <TD width="150" height="20">Status</TD>
            <TD width="266">
             <%
				java.util.HashSet except1 = new java.util.HashSet();
				except1.add(globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean.CS_COMPTABILISE);
			%>
            <ct:FWCodeSelectTag name="forStatus"
					defaut=""
					wantBlank="<%=true%>"
			        codeType="CPETCOMRET"
		            except="<%=except1%>"
			/>
            </TD>
          </tr>
       <TR>
			<TD nowrap width="80">Plausibilités</TD>
			<TD width=50%>
				<%
        		java.util.Vector tmp = globaz.phenix.db.communications.CPValidationCalculCommunication.getListPlausibilites(session,"",viewBean.getCanton());
				%>
				<ct:FWListSelectTag name="forIdPlausibilite"
						defaut=""
	            		data="<%=tmp%>"/>
			</TD>
	   </TR>    
	   <TR>
            <TD height="20">Adresse E-Mail</TD>
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