 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
		idEcran="CCP1012";
    	globaz.phenix.db.communications.CPGenererUneDecisionViewBean viewBean = (globaz.phenix.db.communications.CPGenererUneDecisionViewBean)session.getAttribute ("viewBean");
		selectedIdValue = "";
		userActionValue = "phenix.communications.apercuCommunicationFiscaleRetour.processGenerer";
		subTableWidth = "75%";
		String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
		session.setAttribute("idRetour", request.getParameter("idRetour"));
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
top.document.title = "Verfügungen generieren"
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

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
			<%-- tpl:put name="zoneTitle" --%>Generierung der Verfügungen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
		<TR>
			<TD nowrap colspan="2" style="font-weight : bolder;">Dieses Programm generiert die Verfügungen nach den Steuerdaten</TD>
		</TR>
		<TR>
			<TD nowrap colspan="2" style="font-weight : bolder;">&nbsp;</TD>
		</TR>
		<% if("OK".equals(viewBean.getValidationDecision())){%>
          <tr> 
            <TD width="50%" height="20">Passage</TD>
            <TD width="50%"> 
        	<INPUT type="text" name="idJournalFacturation" data-g-string="mandatory:true" class="libelleCourt" value="<%=viewBean.getIdJournalFacturation()%>">
               <%
				Object[] psgMethodsName = new Object[]{
				new String[]{"setIdJournalFacturation","getIdPassage"},
				new String[]{"setLibellePassage","getLibelle"}
				};
				Object[] psgParams= new Object[]{};
				String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/communications/processReceptionGenererDecisions_de.jsp";	
				%>
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
		<%}%>
          <TR> 
            <TD height="50%" width="212">E-Mail Adresse</TD>
            <TD width="50%"> 
              <input name='eMailAddress' class='libelleLong' data-g-string="mandatory:true" value='<%=viewBean.getSession().getUserEMail()%>'>
            </TD>
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
		<input type="hidden" name="selectorName" value="<%=request.getParameter("idRetour")%>">
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