 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.phenix.db.communications.CPJournalRetour"%>
<%@page import="globaz.pyxis.constantes.IConstantes"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
		idEcran="CCP1032";
    	CPCommunicationFiscaleRetourViewBean viewBean = (globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean)session.getAttribute ("viewBean");
		selectedIdValue = "";
		userActionValue = "phenix.communications.apercuCommunicationFiscaleRetour.executerImprimer";
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

<%@page import="globaz.phenix.db.communications.CPCommunicationFiscaleRetourViewBean"%>
<%@page import="globaz.pyxis.db.adressecourrier.TILocalite"%><SCRIPT language="JavaScript">
top.document.title = "Steuermeldungen - Löschung"
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

function postInit(){
	$("#wantDetail").hide();
	$("#libWantDetail").hide();
	$("#impression").change(function () {
		if($(this).val()=="LISTE_PDF_DETAIL"){
			$("#wantDetail").show();
			$("#libWantDetail").show();
		}else {
			$("#wantDetail").hide();
			$("#libWantDetail").hide();
		}
	});
}
/*
*/
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Ausdruck der Steuermeldungen zurück<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
		<TR>
          </TR>
          <TR>
            <TD nowrap colspan="2">&nbsp;</TD>
          </TR>
           <TR>
          <TD nowrap width="30%" >Liste</TD>
			<% 
			//Spécifique CCVS - DDS : S141124_011
			if (viewBean.isCaisseCCCVS()) { 
			%>
			<TD nowrap width="70%" >
				<SELECT name="impression" id="impression" class="libelleLong" >
					<OPTION value='LISTE_EXCEL'>Excel Liste</OPTION>
					<OPTION selected="selected" value='LISTE_PDF' >Gültigkeitsliste</OPTION>
					<OPTION value='LISTE_PDF_DETAIL'>Detail von Steuerverwaltung</OPTION>
				</SELECT>
				 <INPUT type="hidden" name="idRetour" value="<%=viewBean.getIdRetour()%>">
			</TD>
			<% } else { %>
			<TD nowrap width="70%" >
				<SELECT name="impression" id="impression" class="libelleLong" >
					<OPTION selected="selected" value='LISTE_EXCEL'>Excel Liste</OPTION>
					<OPTION value='LISTE_PDF' >Gültigkeitsliste</OPTION>
					<OPTION value='LISTE_PDF_DETAIL'>Detail von Steuerverwaltung</OPTION>
				</SELECT>
				 <INPUT type="hidden" name="idRetour" value="<%=viewBean.getIdRetour()%>">
			</TD>	
			<%}%>
          </TR>
            <% if(viewBean.getJournalRetour().getTypeJournal().equalsIgnoreCase(CPJournalRetour.CS_TYPE_JOURNAL_SEDEX)) {%>
            <TR>
          <TD nowrap width="30%" name="libWantDetail" id="libWantDetail">mit Details</TD>
              <TD nowrap width="30%" ><input type="checkbox" name="wantDetail" id="wantDetail"/></TD>
          </TR>  
         <%} %>
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