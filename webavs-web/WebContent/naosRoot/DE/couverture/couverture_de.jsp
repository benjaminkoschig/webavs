<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%
	idEcran = "CAF0016";
	globaz.naos.db.couverture.AFCouvertureViewBean viewBean = (globaz.naos.db.couverture.AFCouvertureViewBean)session.getAttribute ("viewBean");	
	String jspLocation = servletContext + mainServletPath + "Root/assurance_select.jsp";
	String method = request.getParameter("_method");
%>
<SCRIPT language="JavaScript">
</SCRIPT>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">


function add() {
	document.forms[0].elements('userAction').value="naos.couverture.couverture.ajouter"
}

function upd() {
}

function validate() {
	var exit = true;
	
	document.forms[0].elements('userAction').value="naos.couverture.couverture.modifier";
	 if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.couverture.couverture.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.couverture.couverture.modifier";
	return (exit);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="naos.couverture.couverture.afficher";
}

function del() {
	if (window.confirm("Sie sind dabei, eine Deckung zu löschen! Wollen Sie fortfahren?")) {
		document.forms[0].elements('userAction').value="naos.couverture.couverture.supprimer";
		document.forms[0].submit();
	}
}

function init() {
}

function updateAssuranceId(tag){
	if(tag.select && tag.select.selectedIndex != -1){
		document.getElementById('assuranceId').value = tag.select[tag.select.selectedIndex].assuranceId;
	}
}

</SCRIPT> 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%> 
					Deckung - Detail
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<TR> 
							<TD nowrap colspan="3">&nbsp;</TD>
							<TD width="240" height="31"></TD>
						</TR>
						<TR> 
							<TD nowrap width="176" height="31">Beginndatum</TD>
							<TD width="30" height="31"></TD>
							<TD nowrap width="310"> 
								<INPUT type="hidden" name="selectedId" value='<%=viewBean.getCouvertureId()%>'>
								<INPUT type="hidden" name="planCaisseId" value='<%=viewBean.getPlanCaisseId()%>'>
								<INPUT type="hidden" name="assuranceId" value='<%=viewBean.getAssuranceId()%>'>
								<ct:FWCalendarTag name="dateDebut" value="<%=viewBean.getDateDebut()%>"/> 
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="176" height="31">Enddatum</TD>
							<TD width="30" height="31"></TD>
							<TD nowrap width="310"> 
								<ct:FWCalendarTag name="dateFin" value="<%=viewBean.getDateFin()%>"/> 
							</TD>
						</TR>
						<TR> 
							<TD nowrap height="11" colspan="4"> 
								<hr size="3" width="100%">
							</TD>
						</TR>
						<%  if (! (method != null && method.equals("add"))) { %>
						<TR> 
							<TD nowrap height="31" colspan="3"> 
								<font size="2"><b>Versicherung</b></font>
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="176">Bezeichnung</TD>
							<TD width="30" height="31"></TD>
							<TD nowrap width="310"> 
								<input name="assuranceLibelle" maxlength="50" size="50" type="text" 
									value="<%=viewBean.getAssurance().getAssuranceLibelle()%>" tabindex="-1" class="disabled" readOnly>
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="176" height="31">Kurzbezeichnung</TD>
							<TD width="30" height="31"></TD>
							<TD nowrap height="31" width="310"> 
								<input name="assuranceLibelleCourt" maxlength="20" size="20" type="text" 
									value="<%=viewBean.getAssurance().getAssuranceLibelleCourt()%>" tabindex="-1" class="disabled" readOnly>
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="176" height="30">Buchhaltungsrubrik</TD>
							<TD width="30" height="31"></TD>
							<TD width="310"> 
								<INPUT type="text" name="rubriqueId" value="<%=viewBean.getAssurance().getRubriqueComptable().getIdExterne()%>" tabindex="-1" class="disabled" readOnly>
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="176">Kanton</TD>
							<TD width="30" height="31"></TD>
							<TD nowrap height="31" width="310" > 
								<input name="assuranceCanton"  type="text"  maxlength="15" size="15"
									value="<%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getAssurance().getAssuranceCanton())%>" tabindex="-1" class="disabled" readOnly>
							</TD>
						</TR>
						<TR> 
							<TD width="176"></TD>
						</TR>
						<TR> 
							<TD nowrap width="176">Versicherungsart</TD>
							<TD width="30" height="31"></TD>
							<TD nowrap height="31" width="310" > 
								<input name="assuranceGenre"  type="text" maxlength="20" size="20"
									value="<%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getAssurance().getAssuranceGenre())%>" tabindex="-1" class="disabled" readOnly>
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="176" height="30">Versicherungstyp</TD>
							<TD width="30" height="31"></TD>
							<TD nowrap height="31" width="310"> 
								<input name="typeAssurance"  type="text" maxlength="30" size="30"
									value="<%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getAssurance().getTypeAssurance())%>" tabindex="-1" class="disabled" readOnly>
						</TR>
						<% } else {%>
						<TR> 
							<TD nowrap width="176">Wählen Sie eine Versicherung aus</TD>
							<TD width="30" height="31"></TD>
							<% 
								String tmpValue;
								if  (! globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getAssuranceId())) { 
									tmpValue = viewBean.getAssurance().getAssuranceLibelle();
								} else {
									tmpValue = "";
								}
							%>
							<TD nowrap height="31" width="310"> 
								<ct:FWPopupList 
									name="AssuranceIdList" 
									value="<%=tmpValue%>" 
									className="libelle" 
									jspName="<%=jspLocation%>" 
									size="80"
									onChange="updateAssuranceId(tag)"
									minNbrDigit="0"/>
							</TD>
						</TR>
						<% } %>
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
	<ct:menuChange displayId="options" menuId="AFOptionsCouverture" showTab="options">
		<ct:menuSetAllParams key="couvertureId" value="<%=viewBean.getCouvertureId()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>