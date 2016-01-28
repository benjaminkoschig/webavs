<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%
	idEcran ="CAF0031";
	globaz.naos.db.avisMutation.AFAvisMutationViewBean viewBean = (globaz.naos.db.avisMutation.AFAvisMutationViewBean)session.getAttribute ("viewBean");
%>
<SCRIPT language="JavaScript">
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">


function add() {
	document.forms[0].elements('dateDebut').value = "";
	document.forms[0].elements('libelle').value = "";
	document.forms[0].elements('dateFin').value = "";
	document.forms[0].elements('massePeriodicite').value = "";	
	document.forms[0].elements('maisonMere').value = "";
	document.forms[0].elements('userAction').value="naos.avisMutation.avisMutation.ajouter"
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
	document.forms[0].elements('userAction').value="naos.avisMutation.avisMutation.modifier";
	
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.avisMutation.avisMutation.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.avisMutation.avisMutation.modifier";
	return (exit);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="naos.avisMutation.avisMutation.afficher";
}

function del() {
	if (window.confirm("Sie sind dabei, die ausgewählte Mutationsmeldung zu löschen! Wollen Sie fortfahren?"))
	{
		document.forms[0].elements('userAction').value="naos.avisMutation.avisMutation.supprimer";
		document.forms[0].submit();
	}
}

function init() {
}

</SCRIPT> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Mutationsmeldung - Detail 
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<TR> 
							<TD nowrap width="157" height="31">Partner</TD>
							<TD width="30"></TD>
							<TD nowrap width="400" > 
								<input type="hidden" name="selectedId" size="35" maxlength="40" value="<%=viewBean.getAvisMutationId()%>" tabindex="-1"readOnly>
								<INPUT type="text" name="nom" size="60" maxlength="60" value="<%=viewBean.getTiers().getNom()%>" tabindex="-1" readOnly><BR>
								<% 
									StringBuffer tmpLocaliteLong = new StringBuffer(viewBean.getTiers().getRue().trim());
									if (tmpLocaliteLong.length() != 0) {
										tmpLocaliteLong = tmpLocaliteLong.append(", ");
									}
									tmpLocaliteLong.append(viewBean.getTiers().getLocaliteLong());
								%>
								<input type="text" name="localiteLong" size="60" maxlength="60" value="<%=tmpLocaliteLong.toString()%>"tabindex="-1" readOnly><BR>
								<input type="text" name="canton" size="60" maxlength="60" value="<%=viewBean.getTiers().getCantonDomicile()%>"tabindex="-1" readOnly>
							</TD>
							<TD width="106"></TD>
						</TR>
						<TR> 
							<TD nowrap  height="11" colspan="4"> 
								<hr size="3" width="100%">
							</TD>
						</TR>
						<TR> 
							<TD nowrap height="14" width="157">Meldungsart</TD>
							<TD width="30"></TD>
							<TD nowrap height="31"> 
								<input  type="text" size="20" value="<%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getGenreAnnonce())%>" readonly tabindex="-1" class="libelleLongDisabled">
							</TD>
						</TR>
						<TR> 
							<TD nowrap height="14" width="157">Meldungsdatum</TD>
							<TD height="14" width="30"></TD>
							<TD nowrap height="31"> 
								<ct:FWCalendarTag name="dateAnnonce" value="<%=viewBean.getDateAnnonce()%>" /> 
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="157">Empfangsdatum der Empfangsbestätigung</TD>
							<TD width="30"></TD>
							<TD nowrap height="31">
								<ct:FWCalendarTag name="dateReceptionAccuseReception" value="<%=viewBean.getDateReceptionAccuseReception()%>" /> 
							</TD>
						</TR>
						<TR> 
							<TD nowrap height="31" width="132">Herausgeberkasse</TD>
							<TD width="30"></TD>
							<TD nowrap height="31"> 
								<input type="text" name="caisseEmetteur" size="35" maxlength="40" value="<%=viewBean.getCaisseEmetteur()%>" class="libelleLongDisabled" tabindex="-1" readOnly>
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="157">Empfängerkasse</TD>
							<TD width="30"></TD>
							<TD nowrap height="31"> 
								<input type="text" name="caisseReception" size="35" maxlength="40" value="<%=viewBean.getCaisseReception()%>" class="libelleLongDisabled" tabindex="-1" readOnly>
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