<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%
	idEcran ="CAF0043";
	globaz.naos.db.suiviAssurance.AFSuiviAssuranceViewBean viewBean = (globaz.naos.db.suiviAssurance.AFSuiviAssuranceViewBean)session.getAttribute ("viewBean");
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">

function add() {
	document.forms[0].elements('userAction').value="naos.suiviAssurance.suiviAssurance.ajouter"
}

function upd() {
}

function validate() {
	var exit = true;
	var message = "FEHLER : Alle obligatorische Feldern sind nicht ausgefüllt !";
	if (document.forms[0].elements('datePrevue').value == "")
	{
		message = message + "\nVous n'avez pas saisi la date prevue !";
		exit = false;
	}
	
	if (exit == false)
	{
		alert (message);
		return (exit);
	}
	document.forms[0].elements('userAction').value="naos.suiviAssurance.suiviAssurance.modifier";
		
		if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.suiviAssurance.suiviAssurance.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.suiviAssurance.suiviAssurance.modifier";
	return (exit);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="naos.suiviAssurance.suiviAssurance.afficher";
}

function del() {
	if (window.confirm("Sie sind dabei, die ausgewählte Verfolgung der Versicherung zu löschen! Wollen Sie fortfahren?")) {
		document.forms[0].elements('userAction').value="naos.suiviAssurance.suiviAssurance.supprimer";
		document.forms[0].submit();
	}
}

function init() {
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Verfolgung einer Versicherung - Detail
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<TR> 
							<TD nowrap width="125">&nbsp;</TD>
						</TR>
						<TR> 
							<TD height="31" width="125" >Versicherung Bezeichnung</TD>
							<TD width="30"> 
								<input type="hidden" name="selectedId" value='<%=viewBean.getSuiviAssuranceId()%>'>
							</TD>
							<TD width="250"> 
								<input name="assuranceLibelleCourt" size="20" maxlength="20" type="text" value="<%=viewBean.getAssurance().getAssuranceLibelleCourt()%>" tabindex="-1" class="Disabled" readOnly>
							</TD>
							<TD colspan="4"> 
								<input name="assuranceLibelle" size="50" maxlength="50" type="text" value="<%=viewBean.getAssurance().getAssuranceLibelle()%>" tabindex="-1" class="Disabled" readOnly>
							</TD>
							<TD width="100"></TD>
						</TR>
						<TR> 
							<TD nowrap width="125">&nbsp;</TD>
						</TR>
						<TR> 
							<TD nowrap  height="11" colspan="8"> 
								<HR size="3" width="100%">
							</TD>
						</TR>
						<TR> 
							<TD width="125" height="31">Status der Verfolgung</TD>
							<TD height="14" width="30"></TD>
							<TD height="31" width="200">
								<ct:FWCodeSelectTag 
									name="statut" 
									defaut="<%=viewBean.getStatut()%>"
									codeType="VESTATUTSU"/>
							</TD>
							<TD width="30"></TD>
							<TD  width="117">Effektives Datum</TD>
							<TD width="30"></TD>
							<TD  width="200">
								<ct:FWCalendarTag name="dateEffective" value="<%=viewBean.getDateEffective()%>" /> 
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="125" height="30">Vorgesehenes Datum</TD>
							<TD width="30"></TD>
							<TD nowrap width="200">
								<ct:FWCalendarTag name="datePrevue" value="<%=viewBean.getDatePrevue()%>" /> 
							</TD>
							<TD width="30"></TD>
							<TD nowrap height="31" width="117">Enddatum</TD>
							<TD width="30"></TD>
							<TD nowrap width="200">
								<ct:FWCalendarTag name="dateFin" value="<%=viewBean.getDateFin()%>" /> 
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%>
<% if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %>
<SCRIPT>
</SCRIPT>
<% } %>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>