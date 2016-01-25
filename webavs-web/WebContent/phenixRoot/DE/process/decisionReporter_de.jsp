
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
		idEcran="CCP3004";
    	globaz.phenix.vb.decision.CPDecisionReporterViewBean viewBean = (globaz.phenix.vb.decision.CPDecisionReporterViewBean)session.getAttribute ("viewBean");
		selectedIdValue = "";
		userActionValue = "phenix.process.decision.executerReporter";
		subTableWidth = "75%";
		String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
%>
<%
String MSG_PROCESS_OK = "The process successfully started.";
if ("FR".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "La t�che a d�marr� avec succ�s.";
} else if ("DE".equalsIgnoreCase(languePage)) {
	MSG_PROCESS_OK = "Prozess erfolgreich gestartet.";
}
%>
<%@page import="globaz.globall.util.JACalendar"%>
<SCRIPT language="JavaScript">
top.document.title = "Akonto - Erstellung"
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
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
			<%-- tpl:put name="zoneTitle" --%>�bernahme der voreingegebenen Verf�gungen<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						
		   <TR>
				<TD nowrap colspan="2" style="font-weight : bolder;"><p><b><font size="2">Dieses Programm berechnet die Verf�gungen basierend auf das Vorjahr, zum Beispiel
				 die Verf�gungen <%=JACalendar.getYear(JACalendar.today().toString())%> wie sie im <%=JACalendar.getYear(JACalendar.today().toString())-1%> eingegeben wurden.</font></b></p></TD>
			</TR>
			  <TR>
				<TD nowrap colspan="2" style="font-weight : bolder;">&nbsp;</TD>
			</TR>
			<TR>
				<TD nowrap colspan="2" style="font-weight : bolder;">&nbsp;</TD>
			</TR>
			<tr>
            <TD width="120" height="20">Job</TD>
            <TD width="300">
              <INPUT type="text" name="idPassage" maxlength="15" size="15"  value="<%=viewBean.getIdPassage()%>">
              <%
			Object[] psgMethodsName = new Object[]{
				new String[]{"setIdPassage","getIdPassage"},
				new String[]{"setLibellePassage","getLibelle"}
			};
			Object[] psgParams= new Object[]{};
			String redirectUrl = ((String)request.getAttribute("mainServletPath")+"Root")+"/"+globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(session)+"/process/decisionReporter_de.jsp";
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
            <TD width="150" height="20"></TD>
            <TD nowrap width="300">
              <INPUT type="text" name="libellePassage" class="libelleLongDisabled" value="<%=viewBean.getLibellePassage()%>" readonly>
            </TD>
          </tr>
           <TR>
            <TD width="150">E-Mail Adresse</TD>
            <TD width="300">
              <input name='eMailAddress' class='libelleLong' data-g-string="mandatory:true" value='<%=viewBean.getEMailAddress()%>'>
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