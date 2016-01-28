<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%> 
<!-- Creer l'enregistrement si il n'existe pas -->
<%@ taglib uri="/WEB-INF/naos.tld" prefix="naos" %>
<%
	idEcran = "CAF0007";
	globaz.naos.db.planAffiliation.AFPlanAffiliationViewBean viewBean = (globaz.naos.db.planAffiliation.AFPlanAffiliationViewBean)session.getAttribute ("viewBean");	
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%> 
<SCRIPT language="JavaScript">

function add() {
	document.forms[0].elements('userAction').value="naos.planAffiliation.planAffiliation.ajouter"
}

function upd() {
}

function validate() {
	var exit = true;
	
	document.forms[0].elements('userAction').value="naos.planAffiliation.planAffiliation.modifier";
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="naos.planAffiliation.planAffiliation.ajouter";
    else
        document.forms[0].elements('userAction').value="naos.planAffiliation.planAffiliation.modifier";
	return (exit);
}

function cancel() {
	if (document.forms[0].elements('_method').value == "add")
		document.forms[0].elements('userAction').value="back";
	else
		document.forms[0].elements('userAction').value="naos.planAffiliation.planAffiliation.afficher";
}

function del() {
	if (window.confirm("Sie sind dabei, einen Versicherungsplan zu löschen! Wollen Sie fortfahren?")) {
		document.forms[0].elements('userAction').value="naos.planAffiliation.planAffiliation.supprimer";
		document.forms[0].submit();
	}
}

function init() {
}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%> 
					Erfassungsplan - Detail
					<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%> 
						<naos:AFInfoAffiliation name="affiliationId" affiliation="<%=viewBean.getAffiliation()%>" /> 
						<TR> 
							<TD nowrap  height="11" colspan="2"> 
								<hr size="3" width="100%"><INPUT type="hidden" name="selectedId" value='<%=viewBean.getPlanAffiliationId()%>'>
							</TD>
						</TR>
						
						<TR> 
							<TD nowrap width="238" height="31">Versicherungsplan</TD>
							
							<TD nowrap> 
								<INPUT type="text" name="libelle" size="40" maxlength="40" value='<%=viewBean.getLibelle()%>'>
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="238" height="31">Rechnungsbezeichnung</TD>
							
							<TD nowrap> 
								<INPUT type="text" name="libelleFacture" size="40" maxlength="40" value='<%=viewBean.getLibelleFacture()%>'>
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="238" height="31">Separat ausdrucken</TD>
							
							<TD nowrap> 
								<INPUT type="checkbox" name="blocageEnvoi" <%=(viewBean.isBlocageEnvoi().booleanValue())? "checked" : ""%> >
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="238" height="31">Inaktiver Erfassungsplan</TD>
							
							<TD nowrap> 
								<INPUT type="checkbox" name="inactif" <%=(viewBean.isInactif().booleanValue())? "checked" : ""%> >
							</TD>
						</TR>
						<%--TR> 
							<TD nowrap width="238" height="14">Type Adresse</TD>
							<TD width="30" height="31"></TD>
							<TD nowrap height="31">
								<ct:FWCodeSelectTag 
									name="typeAdresse" 
									defaut="<%=viewBean.getTypeAdresse()%>"
									codeType="PYTYPEADR"
									wantBlank="true"/> 
							</TD>
						</TR--%>
						<TR> 
							<TD nowrap width="238" height="14">Bereich Versand</TD>
							
							<TD nowrap height="31">
								<ct:FWCodeSelectTag 
									name="domaineCourrier" 
									defaut="<%=viewBean.getDomaineCourrier()%>"
									codeType="PYAPPLICAT"
									wantBlank="true"/> 
					
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="238" height="14">Bereich LSV Inkasso</TD>
							
							<TD nowrap height="31">
								<ct:FWCodeSelectTag 
									name="domaineRecouvrement" 
									defaut="<%=viewBean.getDomaineRecouvrement()%>"
									codeType="PYAPPLICAT"
									wantBlank="true"/> 
					
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="238" height="14">Bereich Rückerstattung</TD>
							
							<TD nowrap height="31">
								<ct:FWCodeSelectTag 
									name="domaineRemboursement" 
									defaut="<%=viewBean.getDomaineRemboursement()%>"
									codeType="PYAPPLICAT"
									wantBlank="true"/> 
					
							</TD>
						</TR>
						<%--TR> 
							<TD nowrap  height="11" colspan="4"> 
								<hr size="3" width="100%">
							</TD>
						</TR>
						<TR> 
							<TD nowrap width="238" >S&eacute;lectionner un Tier pour Facturation...</TD>
							<TD width="30" height="31"></TD>
							<TD nowrap width="500"  height="31"> 
	           					<INPUT type="text" name="nom" size="60" maxlength="60" value="<%=viewBean.getTiers().getDesignation1()%>" tabindex="-1" class="disabled" readOnly>
								<% 
									StringBuffer tmpLocaliteLong = new StringBuffer(viewBean.getTiers().getRue().trim());
									if (tmpLocaliteLong.length() != 0) {
										tmpLocaliteLong = tmpLocaliteLong.append(", ");
									}
									tmpLocaliteLong.append(viewBean.getTiers().getLocaliteLong());

									Object[] caisseMethods= new Object[]{
										new String[]{"setIdTiersFacturation","getIdTiers"}
									};
			              		%>
								<ct:FWSelectorTag 
									name="caisseSelector" 
									
									methods="<%=caisseMethods%>"
									providerApplication ="pyxis"
									providerPrefix="TI"
									providerAction ="pyxis.tiers.tiers.chercher"/> 
								<INPUT type="text" name="localiteLong" size="60" maxlength="60" value="<%=tmpLocaliteLong.toString()%>" tabindex="-1" class="disabled" readOnly><BR>
								<INPUT type="text" name="canton" size="60" maxlength="60" value="<%=viewBean.getTiers().getCantonDomicile()%>" tabindex="-1" class="disabled" readOnly>
							</TD>
							<TD width="100"></TD>
						</TR> 
						<TR> 
							<TD nowrap colspan="3">&nbsp;</TD>
						</TR> 
						<TR> 
							<TD nowrap width="238" >...ou prendre le tier par d&eacute;faut</TD>
							<TD width="30" height="31"></TD>
							<TD nowrap> 
								<INPUT type="checkbox" name="resetIdTiersFacturation">
							</TD>
						</TR--%>
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
	<ct:menuChange displayId="options" menuId="AFOptionsPlanAffiliation" showTab="options">
		<ct:menuSetAllParams key="planAffiliationId" value="<%=viewBean.getPlanAffiliationId()%>"/>
	</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>