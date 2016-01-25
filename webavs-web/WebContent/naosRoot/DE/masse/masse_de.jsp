<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%
	idEcran = "CAF0055";
	globaz.naos.db.masse.AFMasseViewBean viewBean = (globaz.naos.db.masse.AFMasseViewBean)session.getAttribute ("viewBean");
	String method = request.getParameter("_method");
	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">

function add()
{
	document.forms[0].elements('dateDebut').value == "";
	document.forms[0].elements('dateFin').value == "";
	document.forms[0].elements('nouvelleMasseAnnuelle').value == "";
	document.forms[0].elements('nouvelleMassePeriodicite').value == "";
	
	document.forms[0].elements('userAction').value="naos.masse.masse.ajouter"
}

function upd()
{
}

function validate()
{
	var exit = true;
	var message = "FEHLER : Alle obligatorische Feldern sind nicht ausgefüllt !";
	if (document.forms[0].elements('dateDebut').value == "")
	{
		message = message + "\nVous n'avez pas saisi la date de début !";
		exit = false;
	}
	
	if (exit == false)
	{
		alert (message);
		return (exit);
	}
	document.forms[0].elements('userAction').value="naos.masse.massemodifier";
	
	if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.masse.masse.ajouter";
	else
        document.forms[0].elements('userAction').value="naos.masse.masse.modifier";
	return (exit);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="naos.masse.masse.afficher";
}

function del()
{
	if (window.confirm("Sie sind dabei, die ausgewählte Lohnsumme zu löschen! Wollen Sie fortfahren?"))
	{
		document.forms[0].elements('userAction').value="naos.masse.masse.supprimer";
		document.forms[0].submit();
	}
}

function cotisationSelect()
{
	document.forms[0].elements('userAction').value="naos.masse.cotisationMasse.selectCotisation";
	document.forms[0].submit();
}

function init() {
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>
					Lohnsumme - Detail
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<TR> 
							<TD nowrap height="31" width="205">Mitglied</TD>
							<TD width="30"></TD>
							<TD nowrap width="500"> 
								<INPUT type="hidden" name="selectedId" value='<%=viewBean.getMasseId()%>'>
								<INPUT type="hidden" name="affiliationId" value="<%=viewBean.getAffiliation().getAffiliationId()%>" >
								<% if(viewBean.getTiers().idTiersExterneFormate().length()!=0) { %>
									<INPUT type="text" name="affilieNumero" size="28" maxlength="28" value="<%=viewBean.getAffiliation().getAffilieNumero()%>" readOnly tabindex="-1" class="disabled">
									<input type="text" name="idExterne" size="28" maxlength="28" value="<%=viewBean.getTiers().idTiersExterneFormate()%>" readOnly tabindex="-1" class="disabled">
								<% } else { %>
									<INPUT type="text" name="affilieNumero" size="60" maxlength="60" value="<%=viewBean.getAffiliation().getAffilieNumero()%>" readOnly tabindex="-1" class="disabled">
								<% } %>
								<br>
								<INPUT type="text" name="nom" size="60" maxlength="60" value="<%=viewBean.getTiers().getNom()%>" tabindex="-1" readOnly><BR>
								<% 
									StringBuffer tmpLocaliteLong = new StringBuffer(viewBean.getTiers().getRue().trim());
									if (tmpLocaliteLong.length() != 0) {
										tmpLocaliteLong = tmpLocaliteLong.append(", ");
									}
									tmpLocaliteLong.append(viewBean.getTiers().getLocaliteLong());
								%>
								<INPUT type="text" name="localiteLong" size="60" maxlength="60" value="<%=tmpLocaliteLong.toString()%>" tabindex="-1" readOnly><BR>
								<INPUT type="text" name="canton" size="60" maxlength="60" value="<%=viewBean.getTiers().getCantonDomicile()%>"tabindex="-1" readOnly><BR>
								
							</TD>
							<TD nowrap width="100"></TD>
						</TR>
						<TR> 
							<TD nowrap height="11" colspan="4"> 
								<hr size="3" width="100%">
							</TD>
						</TR>
						<TR> 
							<TD nowrap height="31" width="205">Beitrag</TD>
							<TD width="30"></TD>
							<TD nowrap width="500" > 
								<%
								 	String assurance = "";
									if (viewBean.getCotisation() != null) {
								 		assurance = viewBean.getCotisation().getAssurance().getAssuranceLibelleCourt();
								 	} 
								%>
								<INPUT type="text" name="libelle" size="35" maxlength="40" 
									value="<%=assurance%>" class="libelleLongDisabled" tabindex="-1">
								<%	if (method != null && method.equals("add")) { %>
								<INPUT type="submit" value="..." onclick="cotisationSelect()">
								<% } %>
							</TD>
						</TR>
						<TR> 
							<TD nowrap height="31" width="205">Periode</TD>
							<TD width="30" align="right">de&nbsp;</TD>
							<TD nowrap width="500"> 
								<ct:FWCalendarTag name="dateDebut" value="<%=viewBean.getDateDebut()%>"/>
								bis 
								<ct:FWCalendarTag name="dateFin" value="<%=viewBean.getDateFin()%>" /> 
							</TD>
						</TR>
						<TR> 
							<TD nowrap height="31" width="205">Jahres Lohnsumme</TD>
							<TD width="30"></TD>
							<TD nowrap width="500"> 
								<INPUT name="nouvelleMasseAnnuelle" size="20" type="text" 
									value="<%=viewBean.getNouvelleMasseAnnuelle()%>" style="text-align : right;">
							</TD>
						</TR>
						<TR> 
							<TD nowrap height="31" width="205">Lohnsumme der Periodizität</TD>
							<TD width="30"></TD>
							<TD nowrap width="500"> 
								<INPUT name="nouvelleMassePeriodicite" size="20" type="text" 
									value="<%=viewBean.getNouvelleMassePeriodicite()%>" style="text-align : right;">
							</TD>
						</TR>
						<TR> 
							<TD nowrap height="31" width="205">Verarbeitung</TD>
							<TD width="30"></TD>
							<TD nowrap width="500"> 
								<INPUT type="checkbox" name="traitement" <%=(viewBean.isTraitement().booleanValue())? "checked" : ""%>>
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