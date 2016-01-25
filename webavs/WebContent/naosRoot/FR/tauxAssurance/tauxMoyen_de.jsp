<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran ="CAF0067";
	globaz.naos.db.tauxAssurance.AFTauxMoyenViewBean viewBean = (globaz.naos.db.tauxAssurance.AFTauxMoyenViewBean)session.getAttribute ("viewBean");
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
	document.forms[0].elements('userAction').value="naos.tauxAssurance.tauxMoyen.ajouter"
}

function upd() {
}

function validate() {
	var exit = true;
	
	document.forms[0].elements('userAction').value="naos.tauxAssurance.tauxMoyen.modifier";
	 if (document.forms[0].elements('_method').value == "add")
        document.forms[0].elements('userAction').value="naos.tauxAssurance.tauxMoyen.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.tauxAssurance.tauxMoyen.modifier";
	return (exit);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
 		document.forms[0].elements('userAction').value="naos.tauxAssurance.tauxMoyen.afficher";
}

function del() {
	if (window.confirm("Vous êtes sur le point de supprimer une information ! Voulez-vous continuer?")) {
		document.forms[0].elements('userAction').value="naos.tauxAssurance.tauxMoyen.supprimer";
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
					Taux moyen - D&eacute;tail
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
								<INPUT type="hidden" name="selectedId" value='<%=viewBean.getTauxMoyenId()%>'>
								<INPUT type="hidden" name="assuranceId" value='<%=viewBean.getAssuranceId()%>'>
								<INPUT type="text" name="annee" size="4" maxlength="4" value="<%=viewBean.getAnnee()%>">
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="125" height="31">Masse salariale</TD>
							
							<TD nowrap> 
								<INPUT type="text" name="masseAnnuelle" size="15" style="text-align : right;"
									value="<%=globaz.globall.util.JANumberFormatter.fmt(viewBean.getMasseAnnuelle(),true,true,false,2)%>" onchange="validateFloatNumber(this)">
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="125" height="31">Mois facturés</TD>
							
							<TD nowrap> 
								<INPUT type="text" name="nbrMois" size="2" maxlength="2"
									value="<%=viewBean.getNbrMois()%>">
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="125" height="31">Taux (%)</TD>
							
							<TD nowrap> 
								<INPUT type="text" name="tauxMoyen" size="15" style="text-align : right;"
									value="<%=viewBean.getTauxTotal()%>">
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="125" height="31">Blocage</TD>
							
							<TD nowrap> 
								<INPUT type="checkbox" name="blocage" <%=(viewBean.getBlocage().booleanValue())? "checked" : ""%>>
							</TD>
						</TR>
						<%  if (! (method != null && method.equals("add"))) { %>
						<TR> 
							<TD nowrap width="125">Assurance</TD>
							<TD nowrap> 
								<input name="assuranceLibelle" maxlength="50" size="50" type="text" 
									value="<%=viewBean.getTaux().getAssurance().getAssuranceLibelle()%>" tabindex="-1" class="disabled" readOnly>
							</TD>
						</TR>
						<% } else {%>
						<TR> 
							<TD nowrap width="125">S&eacute;lectionner une assurance</TD>
							<% 
								String tmpValue;
								if  (! globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getAssuranceId())) { 
									tmpValue = viewBean.getTaux().getAssurance().getAssuranceLibelle();
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
	<ct:menuChange displayId="options" menuId="AFOptionsAffiliation" showTab="options">
		<ct:menuSetAllParams key="affiliationId" value="<%=viewBean.getAffiliationId()%>"/>
		<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getAffiliationId()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>