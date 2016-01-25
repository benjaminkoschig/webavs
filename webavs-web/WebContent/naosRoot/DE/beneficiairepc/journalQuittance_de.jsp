
<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregistrement si il n'existe pas -->
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran = "CAF0063";
	AFJournalQuittanceViewBean viewBean = (AFJournalQuittanceViewBean)session.getAttribute ("viewBean");
	String method = request.getParameter("_method");
	if(method == null)
		method = "modifier";
%>
<%@page import="globaz.naos.db.beneficiairepc.AFQuittanceViewBean"%>
<%@page import="globaz.globall.util.JAUtil"%>
<%@page import="globaz.naos.db.beneficiairepc.AFJournalQuittanceViewBean"%>
<SCRIPT language="JavaScript">
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">


function add() {
	document.forms[0].elements('userAction').value="naos.beneficiairepc.journalQuittance.ajouter"
}

function upd() {
}

function validate() {
	var exit = true;

	document.forms[0].elements('userAction').value="naos.beneficiairepc.journalQuittance.modifier";
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.beneficiairepc.journalQuittance.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.beneficiairepc.journalQuittance.modifier";
	return (exit);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
 		document.forms[0].elements('userAction').value="back";
 	else
  		document.forms[0].elements('userAction').value="naos.beneficiairepc.journalQuittance.afficher";
}

function del() {
	if (window.confirm("Sie sind dabei, eine Quittung zu löschen! Wollen Sie fortfahren?")) {
		document.forms[0].elements('userAction').value="naos.beneficiairepc.journalQuittance.supprimer";
		document.forms[0].submit();
	}
}

function init(){
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
			Detail des Quittungsjournals
			<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
			<% if(!method.equals("add")) {%>
			<TR>
							<TD>
								Journal-Nr.
							</TD>

							<TD>
								<input type="text" value="<%=viewBean.getIdJournalQuittance()%>" disabled="disabled" readonly="readonly"></input>
							</TD>
							<TD>
								&nbsp;
							</TD>
			</TR>
			<TR>
							<TD>
								Anzahl Quittungen
							</TD>

							<TD>
								<input type="text" value="<%=viewBean.getNombreQuittances()%>" disabled="disabled" readonly="readonly"></input>
							</TD>
							<TD>
								&nbsp;
							</TD>
			</TR>

			<TR>
							<TD>
								Status
							</TD>

							<TD>
								<input type="text" value="<%=viewBean.getEtatLibelle()%>" disabled="disabled" readonly="readonly"></input>
							</TD>
							<TD>
								&nbsp;
							</TD>
			</TR>
			<% } %>
			<TR>
							<TD>
								Erstellungsdatum
							</TD>

							<TD>
								<ct:FWCalendarTag name="dateCreation" value="<%=viewBean.getDateCreation()%>"/>
							</TD>
							<TD>
								&nbsp;
							</TD>
			</TR>
			<TR>
							<TD>
								Beschreibung des Journals
							</TD>

							<TD>
								<input type="text" value="<%=viewBean.getDescriptionJournal()%>" name="descriptionJournal" class="libelleLong"></input>
							</TD>
							<TD>
								&nbsp;
							</TD>
			</TR>
			<TR>
							<TD>
								Jahr
							</TD>

							<TD>
								<input type="text" value="<%=viewBean.getAnnee()%>" name="annee" size="4" maxlength="4"></input>
							</TD>
							<TD>
								&nbsp;
							</TD>
			</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="options" menuId="AF-journalQuittances" showTab="options">
<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdJournalQuittance()%>"/>
<ct:menuSetAllParams key="idJournalQuittances" value="<%=viewBean.getIdJournalQuittance()%>"/>
</ct:menuChange>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>