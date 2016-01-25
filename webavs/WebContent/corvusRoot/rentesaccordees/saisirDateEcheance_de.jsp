<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PRE0026";
	
	RESaisirDateEcheanceViewBean viewBean = (RESaisirDateEcheanceViewBean)(session.getAttribute("viewBean"));
	
	bButtonCancel = true;
	bButtonValidate = true;
	bButtonDelete =  false;
	bButtonUpdate = true;
	bButtonNew = false;
	
	String noDemandeRente = request.getParameter("noDemandeRente");
	String idTierRequerant = request.getParameter("idTierRequerant"); 
	String idTiersBeneficiaire = request.getParameter("idTiersBeneficiaire");
	String idRenteAccordee = request.getParameter("idRenteAccordee");
	String idBaseCalcul = request.getParameter("idBaseCalcul");
	String csTypeBasesCalcul = request.getParameter("csTypeBasesCalcul");
	String csEtatRenteAccordee = request.getParameter("csEtatRenteAccordee");
	String dateFinDroit = request.getParameter("dateFinDroit");
	String isPreparationDecisionValide = request.getParameter("isPreparationDecisionValide");
	
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");	
%>


<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.corvus.vb.rentesaccordees.RESaisirDateEcheanceViewBean"%>
<%@page import="globaz.corvus.servlet.IREActions"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.corvus.api.basescalcul.IRERenteAccordee"%>
<%@page import="globaz.corvus.utils.REPmtMensuel"%>
<%if(JadeStringUtil.isNull(menuOptionToLoad) || JadeStringUtil.isEmpty(menuOptionToLoad)){%>
	<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="corvus-optionsempty"/>
<%}else if("rentesaccordees".equals(menuOptionToLoad)){%>
	<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="options"/>
	<ct:menuChange displayId="options" menuId="corvus-optionsrentesaccordees">
		<ct:menuSetAllParams key="selectedId" value="<%=idRenteAccordee%>"/>
		<ct:menuSetAllParams key="noDemandeRente" value="<%=noDemandeRente%>"/>
		<ct:menuSetAllParams key="idTierRequerant" value="<%=idTierRequerant%>"/>
		<ct:menuSetAllParams key="idTiersBeneficiaire" value="<%=idTiersBeneficiaire%>"/>
		<ct:menuSetAllParams key="idRenteAccordee" value="<%=idRenteAccordee%>"/>
		<ct:menuSetAllParams key="idBaseCalcul" value="<%=idBaseCalcul%>"/>
		<ct:menuSetAllParams key="csTypeBasesCalcul" value="<%=csTypeBasesCalcul%>"/>
		<% if ((IRERenteAccordee.CS_ETAT_AJOURNE.equals(csEtatRenteAccordee)
			    || IRERenteAccordee.CS_ETAT_CALCULE.equals(csEtatRenteAccordee)
			    || IRERenteAccordee.CS_ETAT_DIMINUE.equals(csEtatRenteAccordee))
			    
			  || (!globaz.jade.client.util.JadeStringUtil.isBlankOrZero(dateFinDroit))
			  || !REPmtMensuel.isValidationDecisionAuthorise(objSession)) { %>
			<ct:menuActivateNode active="no" nodeId="optdiminution"/>
		<%}%>
		<%if ("false".equals(isPreparationDecisionValide)){%>
			<ct:menuActivateNode active="no" nodeId="preparerDecisionRA"/>
		<%}%>
	</ct:menuChange>
<%}%>

<SCRIPT language="javaScript">

function add() {}

function upd() {
		document.forms[0].elements('userAction').value="corvus.rentesaccordees.saisirDateEcheance.modifier";
		document.forms[0].elements('modifie').value="true";
}

function validate() {
	state = true;
	    
    document.forms[0].elements('userAction').value="corvus.rentesaccordees.saisirDateEcheance.modifier";

    return state;
}

function cancel() {
   document.forms[0].elements('userAction').value="<%= IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE%>.chercher";
}

function init(){
}

 </SCRIPT>
 
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_SDE_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_SDE_DETAIL_REQUERANT"/></TD>
							<TD colspan="3">
								<INPUT type="text" value="<%=viewBean.getDetailRequerantDetail()%>" size="100" class="disabled" readonly>
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_SDE_NO_RENTE_ACCORDEE"/> </TD>
							<TD><INPUT type="text" name="idPrestationAccordee" class="disabled" readonly value="<%=viewBean.getIdPrestationAccordee()%>"></TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="4">&nbsp;</TD>
						</TR>
						<TR>
							<TD colspan="4"><HR></TD>
						</TR>
						<TR>
							<TD colspan="4">&nbsp;</TD>
						</TR>

						<TR>
							<TD><ct:FWLabel key="JSP_SDE_DATE_ECHEANCE"/></TD>
							<TD>
								<input	id="dateEcheance"
										name="dateEcheance"
										data-g-calendar="type:month"
										value="<%=viewBean.getDateEcheance()%>" />
							</TD>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>