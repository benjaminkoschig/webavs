<%@page import="globaz.corvus.servlet.IREActions"%>
<%@page import="globaz.corvus.utils.REPmtMensuel"%>
<%@page import="globaz.corvus.api.basescalcul.IRERenteAccordee"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.corvus.vb.rentesaccordees.RERepriseDuDroitViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/detail/header.jspf" %>
<%
	idEcran="PRE0036";

	RERepriseDuDroitViewBean viewBean = (RERepriseDuDroitViewBean) session.getAttribute("viewBean");

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

	bButtonDelete = false;
%>

<%@ include file="/theme/detail/javascripts.jspf" %>

<%
	if (JadeStringUtil.isNull(menuOptionToLoad) 
		|| JadeStringUtil.isEmpty(menuOptionToLoad)) {
%>	<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu" />
	<ct:menuChange displayId="options" menuId="corvus-optionsempty" />
<%
	} else if ("rentesaccordees".equals(menuOptionToLoad)) {
%>	<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="options" />
	<ct:menuChange displayId="options" menuId="corvus-optionsrentesaccordees">
		<ct:menuSetAllParams key="selectedId" value="<%=idRenteAccordee%>"/>
		<ct:menuSetAllParams key="noDemandeRente" value="<%=noDemandeRente%>" />
		<ct:menuSetAllParams key="idTierRequerant" value="<%=idTierRequerant%>" />
		<ct:menuSetAllParams key="idTiersBeneficiaire" value="<%=idTiersBeneficiaire%>" />
		<ct:menuSetAllParams key="idRenteAccordee" value="<%=idRenteAccordee%>" />
		<ct:menuSetAllParams key="idBaseCalcul" value="<%=idBaseCalcul%>" />
		<ct:menuSetAllParams key="csTypeBasesCalcul" value="<%=csTypeBasesCalcul%>" />
<%
		if (IRERenteAccordee.CS_ETAT_AJOURNE.equals(csEtatRenteAccordee)
			|| IRERenteAccordee.CS_ETAT_CALCULE.equals(csEtatRenteAccordee)
			|| IRERenteAccordee.CS_ETAT_DIMINUE.equals(csEtatRenteAccordee)
			|| !JadeStringUtil.isBlankOrZero(dateFinDroit)
			|| !REPmtMensuel.isValidationDecisionAuthorise(objSession)) {
%>		<ct:menuActivateNode active="no" nodeId="optdiminution"/>
<%
		}
		if ("false".equals(isPreparationDecisionValide)) {
%>		<ct:menuActivateNode active="no" nodeId="preparerDecisionRA"/>
<%
	}
%>	</ct:menuChange>
<%
	}
%>

	<script type="text/javascript">

		var $userAction,
			$method;

		function add () {}

		function upd () {}

		function validate () {
			$userAction.val("<%=IREActions.ACTION_REPRISE_DU_DROIT%>.ajouter");
			$method.val("add");

			return true;
		}

		function cancel () {
			$userAction.val("<%=IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE%>.chercher");
		}

		function init () {
		}

		$(document).ready(function () {
			$userAction = $('[name="userAction"]');
			$method = $('[name="_method"]');
		});
	</script>

<%@ include file="/theme/detail/bodyStart.jspf" %>
					<ct:FWLabel key="JSP_REPRISE_DROIT_TITRE" />
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<tr>
							<td>
								<ct:FWLabel key="JSP_RPD_DETAIL_BENEFICIAIRE" />
							</td>
							<td colspan="3">
								<input	type="text" 
										value="<%=viewBean.getDetailRequerantDetail()%>" 
										size="100" 
										class="disabled" 
										readonly="readonly" />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_RPD_NO_RENTE_ACCORDEE" />
							</td>
							<td>
								<input	type="text" 
										id="idPrestationAccordee" 
										name="idPrestationAccordee" 
										class="disabled" 
										readonly="readonly" 
										value="<%=viewBean.getIdPrestationAccordee()%>" />
							</td>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="4">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_RPD_PERIODE_DU" />
								<input	type="text" 
										id="periode_1_Du"
										name="periode_1_Du"
										data-g-calendar="type:month"
										value="<%=viewBean.getPeriode_1_Du()%>" /> 
								[mm.aaaa]
							</td>
							<td>
								<ct:FWLabel key="JSP_RPD_PERIODE_AU"/> 
								<input	type="text" 
										id="periode_1_Au"
										name="periode_1_Au"
										data-g-calendar="type:month"
										value="<%=viewBean.getPeriode_1_Au()%>" /> 
								[mm.aaaa]
							</td>
							<td>
								<ct:FWLabel key="JSP_RPD_MONTANT"/>
								<input	type="text" 
										name="montant1" 
										value="<%=viewBean.getMontant1()%>" />
								&nbsp;&nbsp;[<%=viewBean.getMontantPrestation()%>]
							</td>
							<td>
								&nbsp;
							</td>
						</tr>
						<tr>
							<td colspan="4">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_RPD_PERIODE_DU" />
								<input	type="text" 
										id="periode_2_Du" 
										name="periode_2_Du" 
										data-g-calendar="type:month" 
										value="<%=viewBean.getPeriode_2_Du()%>" /> 
								[mm.aaaa]
							</td>
							<td>
								<ct:FWLabel key="JSP_RPD_PERIODE_AU" />
								<input	type="text" 
										id="periode_2_Au" 
										name="periode_2_Au" 
										data-g-calendar="type:month" 
										value="<%=viewBean.getPeriode_2_Au()%>" /> 
								[mm.aaaa]
							</td>
							<td>
								<ct:FWLabel key="JSP_RPD_MONTANT" />
								<input	type="text" 
										name="montant2" 
										value="<%=viewBean.getMontant2()%>" />
							</td>
							<td>
								&nbsp;
							</td>
						</tr>

<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%@ include file="/theme/detail/footer.jspf" %>
