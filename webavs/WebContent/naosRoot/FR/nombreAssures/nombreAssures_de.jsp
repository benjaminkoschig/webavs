<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran ="CAF0024";
	globaz.naos.db.nombreAssures.AFNombreAssuresViewBean viewBean = (globaz.naos.db.nombreAssures.AFNombreAssuresViewBean)session.getAttribute ("viewBean");
	String jspLocation = servletContext + mainServletPath + "Root/assurance_select.jsp";
	String method = request.getParameter("_method");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">


function add() {
	document.forms[0].elements('userAction').value="naos.nombreAssures.nombreAssures.ajouter"
}

function upd() {
}

function validate() {
	var exit = true;
	
	document.forms[0].elements('userAction').value="naos.nombreAssures.nombreAssures.modifier";
	 if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.nombreAssures.nombreAssures.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.nombreAssures.nombreAssures.modifier";
	return (exit);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
 		document.forms[0].elements('userAction').value="naos.nombreAssures.nombreAssures.afficher";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer une information ! Voulez-vous continuer?")) {
		document.forms[0].elements('userAction').value="naos.nombreAssures.nombreAssures.supprimer";
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
					Nombre d'assur&eacute;s - D&eacute;tail
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<naos:AFInfoAffiliation name="affiliationId" affiliation="<%=viewBean.getAffiliation()%>" /> 
						<TR> 
							<TD height="11" colspan="2"> 
								<hr size="3" width="100%" >
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="125" height="31">Ann&eacute;e</TD>
							
							<TD nowrap> 
								<INPUT type="hidden" name="selectedId" value='<%=viewBean.getNbrAssuresId()%>'>
								<INPUT type="hidden" name="assuranceId" value='<%=viewBean.getAssuranceId()%>'>
								<INPUT type="text" name="annee" size="10" maxlength="4" value="<%=viewBean.getAnnee()%>">
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="125" height="31">Nombre d'assur&eacute;s</TD>
							
							<TD nowrap> 
								<INPUT type="text" name="nbrAssures" size="15" maxlength="9"
									value="<%=viewBean.getNbrAssures()%>">
							</TD>
						</TR>
						<TR> 
							<TD nowrap colspan="2">&nbsp;</TD>
						</TR>
						<TR> 
							<TD height="11" colspan="2"> 
								<hr size="3" width="100%" >
							</TD>
						</TR>
						<%  if (! (method != null && method.equals("add"))) { %>
						<TR> 
							<TD nowrap width="125" height="31" colspan="2"><font size="2"><b>Assurance</b></font></TD>
						</TR>
						<TR> 
							<TD nowrap width="125" height="31">Libell&eacute;</TD>
							
							<TD nowrap width="280"> 
								<INPUT name="assuranceLibelle" maxlength="50" size="50" type="text"
									value="<%=viewBean.getAssurance().getAssuranceLibelle()%>" tabindex="-1" class="Disabled" readonly>
							</TD>
							<TD nowrap width="100">
						</TR>
						<TR> 
							<TD nowrap width="125" height="31">Libell&eacute; court</TD>
							
							<TD nowrap> 
								<INPUT name="assuranceLibelleCourt" maxlength="20" size="20" type="text"
									value="<%=viewBean.getAssurance().getAssuranceLibelleCourt()%>" tabindex="-1" class="Disabled" readonly>
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="125" height="31">Rubrique comptable</TD>
							
							<TD nowrap> 
								<INPUT type="text" name="rubriqueId" size="20" 
									value="<%=viewBean.getAssurance().getRubriqueComptable().getIdExterne()%>" 
									tabindex="-1" class="Disabled" readonly>
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="125" height="31">Canton</TD>
							
							<TD nowrap> 
								<INPUT name="assuranceCanton" type="text" maxlength="15" size="15"
									value="<%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getAssurance().getAssuranceCanton())%>" 
									tabindex="-1" class="Disabled" readonly>
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="125" >Genre d'assurance</TD>
							
							<TD nowrap> 
								<INPUT name="assuranceGenre" type="text" maxlength="20" size="20"
									value="<%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getAssurance().getAssuranceGenre())%>" 
									tabindex="-1" class="Disabled" readonly>
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="157" height="30">Type d'assurance</TD>
							
							<TD nowrap height="31" width="250"> 
								<INPUT name="typeAssurance" type="text" maxlength="30" size="30"
									value="<%=globaz.naos.translation.CodeSystem.getLibelle(session,viewBean.getAssurance().getTypeAssurance())%>" 
									tabindex="-1" class="Disabled" readonly>
							</TD>
						</TR>
						<% } else { %>
						<TR> 
							<TD nowrap width="125">S&eacute;lectionner une Assurance</TD>
							
								<% 
									String tmpValue;
									if  (! globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getAssuranceId())) { 
										tmpValue = viewBean.getAssurance().getAssuranceLibelle();
									} else {
										tmpValue = "";
									}
								%>
							
							<TD nowrap height="31"> 
								<ct:FWPopupList 
									name="AssuranceIdList" 
									value="<%=tmpValue%>" 
									className="libelle" 
									jspName="<%=jspLocation%>" 
									params="forTypeLibelle=long"
									size="50"
									onChange="updateAssuranceId(tag)"
									minNbrDigit="0"/>
								<IMG
									src="<%=servletContext%>/images/down.gif"
									alt="presser sur la touche 'flèche bas' pour effectuer une recherche"
									title="presser sur la touche 'flèche bas' pour effectuer une recherche"
									onclick="if (document.forms[0].elements('AssuranceIdList').value != '') AssuranceIdListPopupTag.validate();">
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
	<ct:menuChange displayId="options" menuId="AFOptionsAffiliation" showTab="options">
		<ct:menuSetAllParams key="affiliationId" value="<%=viewBean.getAffiliationId()%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getAffiliationId()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>