 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran="CAF3016";
%>
<!-- Creer l'enregitrement s'il n'existe pas -->
<%@ page import="globaz.naos.db.acompte.*"%>
<%
	//Récupération des beans
	AFFigerTaxeCo2ViewBean viewBean = (AFFigerTaxeCo2ViewBean) session.getAttribute ("viewBean");

	String actif = "Permet de supprimer toutes les taxes pour les années données et de recommencer le processus 'figer les compteurs'";
	String inactif = "";

	//Définition de l'action pour le bouton valider
	userActionValue = "naos.taxeCo2.figerTaxeCo2.executer";
	String pageName = "figerTaxeCo2";

%>

<%@page import="globaz.naos.db.taxeCo2.AFFigerTaxeCo2ViewBean"%><SCRIPT language="JavaScript">
top.document.title = "Naos - Figer les compteurs"
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

function changeEtat() {
	if($('#reinitialiser').is(':checked')) {
		$("#actif").show();
		$("#inactif").hide();
	}else {
		$("#actif").hide();
		$("#inactif").show();
	}
}

$(document).ready(function() {
	$("#actif").hide();
	$("#inactif").show();
});


</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Figer les compteurs<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
			<TR>
				<TD title="Année à laquelle la masse a été prise en compte"  nowrap width="160">Année de la masse:</TD>
				<TD title="Année à laquelle la masse a été prise en compte">
				 	<INPUT type="text" name="anneeMasse" value="<%=viewBean.getAnneeMasseFiger()%>" class="numeroCourt" style="width : 4.0cm" onchange="reloadAnneeRedistri()" tabindex="1"">
				 	<INPUT type="hidden" name="pageName" value="<%=pageName%>" class="numeroCourt" style="width : 4.0cm" tabindex="-1"">
				</TD>
			</TR>
			<TR>
				<TD title="Année à laquelle la masse sera redistribuée"  nowrap width="160">Année de la redistribution :</TD>
				<TD title="Année à laquelle la masse sera redistribuée">
				 	<INPUT type="text" name="anneeRedistri" value="<%=viewBean.getAnneeRedistriFiger()%>" class="numeroCourt" style="width : 4.0cm" onchange="reloadAnneeMasse()" tabindex="1"">
				</TD>
			</TR>
			<TR>
				<TD nowrap width="160">E-mail: </TD>
				<TD><INPUT type="text" name="Email" maxlength="40" size="40" style="width:8cm;" value="<%=viewBean.getSession().getUserEMail()%>"></TD>
			</TR>       		
       		<TR>
           		<TD nowrap width="160">Réinitialiser</TD>
           		<TD colspan="2">
           			<input type="checkbox" name="reinitialiser" id="reinitialiser" onclick="javascript:changeEtat();" <%=((viewBean.getReinitialiser().booleanValue())? "checked" : "unchecked")%> >
           			<span id="actif"><font color='#FF0000'><strong><%=actif%></strong></font></span>
           			<span id="inactif"><font color='#FF0000'><strong><%=inactif%></strong></font></span>
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