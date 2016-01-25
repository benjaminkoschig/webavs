 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran="CAF3018";
%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.naos.db.taxeCo2.*"%>
<%
	//Récupération des beans
	AFTaxeCo2ViewBean viewBean = (AFTaxeCo2ViewBean) session.getAttribute ("viewBean");
	showProcessButton = false;
%>

<SCRIPT language="JavaScript">
top.document.title = "Naos - Gesamte Lohnsumme für eine Jahr (CO2-Abgabe)"
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

function init()
{}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Jährliche Lohnsumme berechnen (CO2-Abgabe)<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
			<TR>
				<TD title="Jahr ab dem die Lohnsumme angerechnet wurde">Jahr der Lohnsumme:</TD>
				<TD title="Jahr ab dem die Lohnsumme angerechnet wurde">
				 	<INPUT type="text" name="anneeMasse" value="<%=viewBean.getAnneeMasse()%>" class="numeroCourtDisabled" readonly style="width : 4.0cm" onchange="reloadPage()" tabindex="-1"">
				</TD>
			</TR>
			<TR>
				<TD title="Jahr in dem die Lohnsumme ausbezahlt wird">Auszahlungsjah:</TD>
				<TD title="Jahr in dem die Lohnsumme ausbezahlt wird">
				 	<INPUT type="text" name="anneeRedistri" value="<%=viewBean.getAnneeRedistri()%>" class="numeroCourtDisabled" readonly style="width : 4.0cm" onchange="reloadPage()" tabindex="-1"">
				</TD>
			</TR>
			<TR>
				<TD>Lohnsumme :</TD>
				<TD>
				 	<INPUT type="text" name="masse" value="<%=viewBean.getMasseT()%>" class="numeroCourtDisabled" readonly style="width : 6.0cm" tabindex="-1"">
				</TD>
			</TR>
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