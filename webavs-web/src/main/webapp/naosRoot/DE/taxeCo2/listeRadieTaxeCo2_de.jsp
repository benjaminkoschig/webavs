 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran="CAF2012";
%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.naos.db.taxeCo2.*"%>
<%
	//Récupération des beans
	AFListeRadieTaxeCo2ViewBean viewBean = (AFListeRadieTaxeCo2ViewBean) session.getAttribute ("viewBean");

	//Définition de l'action pour le bouton valider
	userActionValue = "naos.taxeCo2.listeRadieTaxeCo2.executer";
	String pageName = "listeRadieTaxeCo2";
	
	String jspLocation = servletContext + mainServletPath + "Root/affilie_select.jsp";
	int autoDigiAff = globaz.naos.util.AFUtil.getAutoDigitAff(session);
%>

<%@page import="globaz.naos.db.taxeCo2.AFListeRadieTaxeCo2ViewBean"%><SCRIPT language="JavaScript">
top.document.title = "Naos - Liste der Abgänge drucken (CO2-Abgabe)"
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
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers

function init()
{}

function reloadAnneeMasse(){
	document.forms[0].elements('userAction').value="naos.taxeCo2.reloadAnnee.reloadAnneeMasse";
	document.forms[0].submit(); 
}

function reloadAnneeRedistri(){
	document.forms[0].elements('userAction').value="naos.taxeCo2.reloadAnnee.reloadAnneeRedistri";
	document.forms[0].submit(); 
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Liste der Abgänge drucken (CO2-Abgabe)<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
			<TR>
				<TD title="Jahr ab dem die Lohnsumme angerechnet wurde">Jahr der Lohnsumme:</TD>
				<TD title="Jahr ab dem die Lohnsumme angerechnet wurde">
				 	<INPUT type="text" name="anneeMasse" value="<%=viewBean.getAnneeMasse()%>" class="numeroCourt" style="width : 4.0cm" onchange="reloadAnneeRedistri()" tabindex="1"">
				 	<INPUT type="hidden" name="pageName" value="<%=pageName%>" class="numeroCourt" style="width : 4.0cm" tabindex="-1"">
				</TD>
			</TR>
			<TR>
				<TD title="Jahr in dem die Lohnsumme ausbezahlt wird">Auszahlungsjahr:</TD>
				<TD title="Jahr in dem die Lohnsumme ausbezahlt wird">
				 	<INPUT type="text" name="anneeRedistri" value="<%=viewBean.getAnneeRedistri()%>" class="numeroCourt" style="width : 4.0cm" onchange="reloadAnneeMasse()" tabindex="1"">
				</TD>
			</TR>
			<TR>
				<TD>E-Mail:</TD>
				<TD><INPUT type="text" name="Email" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getSession().getUserEMail()%>"></TD>
			</TR>
          
			<tr ><TD>&nbsp;</TD></tr><tr ><TD>&nbsp;</TD></tr>
          <%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%> 
<%  if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<%  }  %>
<%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%>
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="AFOptionsTaxeCo2"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>