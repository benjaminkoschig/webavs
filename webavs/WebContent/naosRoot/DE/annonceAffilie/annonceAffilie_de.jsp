<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<!-- Creer l'enregistrement si il n'existe pas -->
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran ="CAF0029";
	globaz.naos.db.annonceAffilie.AFAnnonceAffilieViewBean viewBean = (globaz.naos.db.annonceAffilie.AFAnnonceAffilieViewBean)session.getAttribute ("viewBean");
%>
<SCRIPT language="JavaScript">
</SCRIPT> 
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<SCRIPT language="JavaScript">


function add() {
	document.forms[0].elements('userAction').value="naos.annonceAffilie.annonceAffilie.ajouter"
}

function upd() {
}

function validate() {
	var exit = true;
	
	if (exit == false)
	{
		alert (message);
		return (exit);
	}
	document.forms[0].elements('userAction').value="naos.annonceAffilie.annonceAffilie.modifier";
	
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.annonceAffilie.annonceAffilie.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.annonceAffilie.annonceAffilie.modifier";
	return (exit);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="naos.annonceAffilie.annonceAffilie.afficher";
}

function del() {
	if (window.confirm("Sie sind dabei, die ausgewählte Meldung des Mitglieds zu löschen! Wollen Sie fortfahren?"))
	{
		document.forms[0].elements('userAction').value="naos.annonceAffilie.annonceAffilie.supprimer";
		document.forms[0].submit();
	}
}

function init(){
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Detail der Vorbereitung zur Meldung des Mitglieds
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<naos:AFInfoAffiliation name="affiliationId" affiliation="<%=viewBean.getAffiliation()%>" titleWidth="120"/>
						<INPUT type="hidden" name="selectedId" value='<%=viewBean.getAnnoncePreparationId()%>'>
						<TR> 
							<TD colspan="2"> 
								<hr size="3">
							</TD>
						</TR>
						<TR> 
							<TD>Erstellt am</TD>
							<TD nowrap >
								<INPUT name="dateEnregistrement" type="text" size="20" value="<%=viewBean.getDateEnregistrement()%>" tabindex="-1" readonly class="dateDisabled">
								am
								<INPUT name="heureEnregistremen" type="text" size="20" value="<%=globaz.globall.util.JACalendar.formatTime(viewBean.getHeureEnregistrement())%>" readonly tabindex="-1" class="dateDisabled">
								durch 
								<INPUT name="utilisateur" type="text" size="20" value="<%=viewBean.getUtilisateur()%>" readonly tabindex="-1"  class="libelleDisabled">
							</TD>
						</TR>
						<TR> 
							<TD>Geändertes Feld</TD>
							<TD nowrap > 
								<input  type="text" size="20" value="<%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getChampModifier())%>" readonly tabindex="-1" class="libelleLongDisabled">
							</TD>
						</TR>
						<TR> 
							<TD>Alte Daten</TD>
							<TD nowrap> 
								<INPUT name="champAncienneDonnee" type="text" size="50" value="<%=viewBean.getChampAncienneDonnee()%>" readonly tabindex="-1" class="libelleLongDisabled" maxlength="256">
							</TD>
						</TR>
						<TR> 
							<TD>Meldung zur Behandlung</TD>
							<TD nowrap> 
								<input type="checkbox" name="traitement" <%=(viewBean.isTraitement().booleanValue())? "checked" : ""%> >
							</TD>
						</TR>
						<TR> 
							<TD>Meldungsdatum</TD>
							<TD nowrap> 
								<INPUT name="dateAnnonce" type="text" size="20" value="<%=viewBean.getDateAnnonce()%>" tabindex="-1" readonly class="dateDisabled">
							</TD>
						</TR>
						<TR> 
							<TD>Beobachtung</TD>
							<TD nowrap>
								<TEXTAREA name="observation" cols="64" rows="4"><%=viewBean.getObservation()%></TEXTAREA>
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
	<ct:menuChange displayId="menu" menuId="AFMenuPrincipal"/>
	<ct:menuChange displayId="options" menuId="AFOptionsAffiliation" showTab="options">
		<ct:menuSetAllParams key="affiliationId" value="<%=viewBean.getAffiliationId()%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getAffiliationId()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>