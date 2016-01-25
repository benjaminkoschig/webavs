 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%>
<%@page import="globaz.naos.db.taxeCo2.AFListeExcelTaxeCo2ViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran="CAF2012";
%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%
	//R�cup�ration des beans
	AFListeExcelTaxeCo2ViewBean viewBean = (AFListeExcelTaxeCo2ViewBean) session.getAttribute ("viewBean");

	//D�finition de l'action pour le bouton valider
	userActionValue = "naos.taxeCo2.listeExcelTaxeCo2.executer";
	String pageName = "listeExcelTaxeCo2";
%>

<SCRIPT language="JavaScript">
top.document.title = "Naos - Imprimer liste excel (Taxe CO2)"
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
			<%-- tpl:put name="zoneTitle" --%>Imprimer liste excel (Taxe CO2)<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
			<TR>
				<TD title="Ann�e � laquelle la masse a �t� prise en compte">Ann�e de la masse:</TD>
				<TD title="Ann�e � laquelle la masse a �t� prise en compte">
				 	<INPUT type="text" name="anneeMasse" value="<%=viewBean.getAnneeMasse()%>" class="numeroCourt" style="width : 4.0cm" onchange="reloadAnneeRedistri()" tabindex="1"">
				 	<INPUT type="hidden" name="pageName" value="<%=pageName%>" class="numeroCourt" style="width : 4.0cm" tabindex="-1"">
				</TD>
			</TR>
			<TR>
				<TD title="Ann�e � laquelle la masse sera redistribu�e">Ann�e de la redistribution :</TD>
				<TD title="Ann�e � laquelle la masse sera redistribu�e">
				 	<INPUT type="text" name="anneeRedistri" value="<%=viewBean.getAnneeRedistri()%>" class="numeroCourt" style="width : 4.0cm" onchange="reloadAnneeMasse()" tabindex="1"">
				</TD>
			</TR>
			<TR>
				<TD>E-mail:</TD>
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