<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>

<%@ page import="globaz.corvus.api.basescalcul.IRERenteAccordee"%>
<%@ page import="globaz.corvus.api.decisions.IREDecision"%>
<%@ page import="globaz.corvus.servlet.IREActions"%>
<%@ page import="globaz.corvus.utils.REPmtMensuel"%>
<%@ page import="globaz.corvus.vb.creances.RECreancierViewBean"%>
<%@ page import="globaz.corvus.vb.prestations.REPrestationsJointDemandeRenteViewBean"%>
<%@ page import="globaz.framework.controller.FWController"%>
<%@ page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page import="globaz.framework.util.FWCurrency"%>
<%@ page import="globaz.globall.db.BSession"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re"%>

<%@ include file="/theme/capage/header.jspf" %>
<%
	// Les labels de cette page commence par la préfix "JSP_CRE_R"

	idEcran = "PRE0022";

	RECreancierViewBean viewBean = (RECreancierViewBean) request.getAttribute("viewBean");
	FWController controller = (FWController) session.getAttribute("objController");

	String idTierRequerant = request.getParameter("idTierRequerant"); 
	String idTiersBeneficiaire = request.getParameter("idTiersBeneficiaire");
	String noDemandeRente = request.getParameter("noDemandeRente");
	if (JadeStringUtil.isEmpty(noDemandeRente)) {
		noDemandeRente = request.getParameter("noDemande");
	}
	String idRenteAccordee = request.getParameter("idRenteAccordee");
	String idBaseCalcul = request.getParameter("idBaseCalcul");
	String csTypeBasesCalcul = request.getParameter("csTypeBasesCalcul");
	String selectedId = request.getParameter("selectedId");
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
	String isPreparerDecision = request.getParameter("isPreparerDecision");

	actionNew = servletContext + mainServletPath + "?userAction=" + IREActions.ACTION_CREANCIER + ".afficher" 
													+ "&_method=add" 
													+ "&idTierRequerant=" + idTierRequerant 
													+ "&noDemandeRente=" + noDemandeRente 
													+ "&montantRetroactif=" + viewBean.getMontantRetroactif() 
													+ "&menuOptionToLoad=" + menuOptionToLoad;

	bButtonNew = bButtonNew && viewBean.isModifiable() && controller.getSession().hasRight(IREActions.ACTION_CREANCIER, FWSecureConstants.UPDATE);

	IFrameListHeight = "150";
	IFrameDetailHeight = "400";
%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%
	if (JadeStringUtil.isNull(menuOptionToLoad) || JadeStringUtil.isEmpty(menuOptionToLoad)) {
%>
		<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu" />
		<ct:menuChange displayId="options" menuId="corvus-optionsempty" />
<%
	} else if ("decision".equals(menuOptionToLoad)) {
%>
		<ct:menuChange displayId="options" menuId="corvus-optionsdecisions" showTab="options">
			<ct:menuSetAllParams key="selectedId" checkAdd="no" value="<%=selectedId%>" />
			<ct:menuSetAllParams key="noDemandeRente" checkAdd="no" value="<%=noDemandeRente%>" />
			<ct:menuSetAllParams key="idTierRequerant" checkAdd="no" value="<%=idTierRequerant%>" />
			<ct:menuSetAllParams key="provenance" checkAdd="no" value="<%=REPrestationsJointDemandeRenteViewBean.FROM_ECRAN_DECISIONS%>" />
			<ct:menuSetAllParams key="idPrestation" checkAdd="no" value="<%=viewBean.getIdPrestation(selectedId)%>" />
			<ct:menuSetAllParams key="montantPrestation" checkAdd="no" value="<%=viewBean.getMontantPrestation(selectedId)%>" />
			<ct:menuSetAllParams key="idLot" value="<%=viewBean.getIdLot() %>" />
<%
		if (IREDecision.CS_ETAT_ATTENTE.equals(viewBean.getCsEtatDecision(selectedId))) {
%>
			<ct:menuActivateNode active="no" nodeId="imprimerDecision" />
<%
		}
		if (JadeStringUtil.isBlankOrZero(viewBean.getIdLot())) {
%>
			<ct:menuActivateNode active="no" nodeId="afficherLot" />
<%
		} else {
%>
			<ct:menuActivateNode active="yes" nodeId="afficherLot" />
<%
		}
		if (REPmtMensuel.isValidationDecisionAuthorise((BSession) controller.getSession())) {
%>
			<ct:menuActivateNode active="yes" nodeId="validerdecision" />
<%
		} else {
%>
			<ct:menuActivateNode active="no" nodeId="validerdecision" />
<%
		}
%>
		</ct:menuChange>
<%
	} else if ("rentesaccordees".equals(menuOptionToLoad)) {
%>
		<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="options" />
		<ct:menuChange displayId="options" menuId="corvus-optionsrentesaccordees">
			<ct:menuSetAllParams key="selectedId" value="<%=idRenteAccordee%>"/>
			<ct:menuSetAllParams key="noDemandeRente" value="<%=noDemandeRente%>" />
			<ct:menuSetAllParams key="idTierRequerant" value="<%=idTierRequerant%>" />
			<ct:menuSetAllParams key="idTiersBeneficiaire" value="<%=idTiersBeneficiaire%>" />
			<ct:menuSetAllParams key="idRenteAccordee" value="<%=idRenteAccordee%>" />
			<ct:menuSetAllParams key="idBaseCalcul" value="<%=idBaseCalcul%>" />
			<ct:menuSetAllParams key="csTypeBasesCalcul" value="<%=csTypeBasesCalcul%>" />
<%
		if ((IRERenteAccordee.CS_ETAT_AJOURNE.equals(viewBean.getCsEtat())
			|| IRERenteAccordee.CS_ETAT_CALCULE.equals(viewBean.getCsEtat())
			|| IRERenteAccordee.CS_ETAT_DIMINUE.equals(viewBean.getCsEtat()))
			|| !JadeStringUtil.isBlankOrZero(viewBean.getDateFinDroit())
			|| !REPmtMensuel.isValidationDecisionAuthorise((BSession)controller.getSession())) {
%>
			<ct:menuActivateNode active="no" nodeId="optdiminution" />
<%
		}
		if (!viewBean.isPreparationDecisionValide()) {
%>
			<ct:menuActivateNode active="no" nodeId="preparerDecisionRA" />
<%
		}
%>
		</ct:menuChange>
<%
	} else if ("menu".equals(menuOptionToLoad)) {
%>
		<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu" />
		<ct:menuChange displayId="options" menuId="corvus-optionsrentesaccordees">
			<ct:menuSetAllParams key="selectedId" value="<%=idRenteAccordee%>"/>
			<ct:menuSetAllParams key="noDemandeRente" value="<%=noDemandeRente%>" />
			<ct:menuSetAllParams key="idTierRequerant" value="<%=idTierRequerant%>" />
			<ct:menuSetAllParams key="idTiersBeneficiaire" value="<%=idTiersBeneficiaire%>" />
			<ct:menuSetAllParams key="idRenteAccordee" value="<%=idRenteAccordee%>" />
			<ct:menuSetAllParams key="idBaseCalcul" value="<%=idBaseCalcul%>" />
			<ct:menuSetAllParams key="csTypeBasesCalcul" value="<%=csTypeBasesCalcul%>" />
<%
		if ((IRERenteAccordee.CS_ETAT_AJOURNE.equals(viewBean.getCsEtat())
			|| IRERenteAccordee.CS_ETAT_CALCULE.equals(viewBean.getCsEtat())
			|| IRERenteAccordee.CS_ETAT_DIMINUE.equals(viewBean.getCsEtat()))
			|| !JadeStringUtil.isBlankOrZero(viewBean.getDateFinDroit())
			|| !REPmtMensuel.isValidationDecisionAuthorise((BSession)controller.getSession())) { 
%>
			<ct:menuActivateNode active="no" nodeId="optdiminution" />
<%
		}
		if (!viewBean.isPreparationDecisionValide()){
%>
			<ct:menuActivateNode active="no" nodeId="preparerDecisionRA" />
<%
		}
%>
		</ct:menuChange>
<%
	}
%>
<style type="text/css">
	.forceAlignRight {
		text-align: right !important;
	}
	.forceTRHeight {
		height: 22px;
	}
</style>
<script type="text/javascript">

	bFind = true;
	usrAction = "<%=IREActions.ACTION_CREANCIER%>.lister";
	detailLink = servlet + "?userAction=<%=IREActions.ACTION_CREANCIER%>.afficher" 
							+ "&_method=add" 
							+ "&idTierRequerant=<%=idTierRequerant%>" 
							+ "&noDemandeRente=<%=noDemandeRente%>" 
							+ "&montantRetroactif=<%=viewBean.getMontantRetroactif()%>" 
							+ "&menuOptionToLoad=<%=menuOptionToLoad%>";

	var $userAction,
		$mainForm,
		$isAll;

	function submitMainForm() {
		$mainForm.attr('target', 'fr_main');
		$mainForm.submit();
	}

	function repartirLesCreances() {
		$userAction.val('<%=IREActions.ACTION_CREANCIER%>.actionRepartirLesCreances');
		submitMainForm();
	}

	function demandeCompensation() {
		$userAction.val('<%=IREActions.ACTION_GENERER_DEMANDE_COMPENSATION%>.afficher');
		$isAll.val(true);
		submitMainForm();
	}

	function preparerDecision() {
		$userAction.val('<%=IREActions.ACTION_PREPARER_DECISION%>.afficherPreparation');
		submitMainForm();
	}

	$(document).ready(function () {
		$userAction = $('input[name="userAction"]');
		$mainForm = $('form[name="mainForm"]');
		$isAll = $('#isAll');
	});
</script>
<%@ include file="/theme/capage/bodyStart.jspf" %>
						<ct:FWLabel key="JSP_CRE_R_TITRE" />
<%@ include file="/theme/capage/bodyStart2.jspf" %>
							<input	type="hidden" 
									name="forIdDemandeRente" 
									value="<%=noDemandeRente%>" />
							<input	type="hidden" 
									name="idTierRequerant" 
									value="<%=idTierRequerant%>" />
							<input	type="hidden" 
									name="idDemandeRente" 
									value="<%=noDemandeRente%>" />
							<input	type="hidden" 
									name="noDemandeRente" 
									value="<%=noDemandeRente%>" />
							<input	type="hidden" 
									name="menuOptionToLoad" 
									value="<%=menuOptionToLoad%>" />
							<input	type="hidden" 
									id="isAll" 
									name="isAll" 
									value="" />
							<input	type="hidden" 
									name="montantRetroactif" 
									value="<%=viewBean.getMontantRetroactif()%>" />
							<input	type="hidden" 
									name="montantEnFaveurAssure" 
									value="<%=viewBean.getMontantEnFaveurAssure()%>" />
							<tr>
								<td width="10%">
									&nbsp;
								</td>
								<td width="20%">
									&nbsp;
								</td>
								<td width="20%">
									&nbsp;
								</td>
								<td width="20%">
									&nbsp;
								</td>
								<td width="20%">
									&nbsp;
								</td>
								<td width="10%">
									&nbsp;
								</td>
							</tr>
							<tr class="forceTRHeight">
								<td>
									<ct:FWLabel key="JSP_CRE_R_BENEFICIAIRE" />
								</td>
								<td colspan="3">
										<re:PRDisplayRequerantInfoTag	session="<%=(BSession)controller.getSession()%>" 
																		idTiers="<%=idTierRequerant%>"
																		style="<%=PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>" />
								</td>
								<td class="forceAlignRight">
									<ct:FWLabel key="JSP_CRE_R_DEMANDE_NO" />&nbsp;:&nbsp;
								</td>
								<td>
									<span>
										<strong>
											<%=noDemandeRente%>
										</strong>
									</span>
								</td>
							</tr>
							<tr class="forceTRHeight">
								<td colspan="6">
									&nbsp;
								</td>
							</tr>
							<tr class="forceTRHeight">
								<td>
									&nbsp;
								</td>
								<td class="forceAlignRight">
									<ct:FWLabel key="JSP_CRE_R_RETROACTIF" />
								</td>
								<td class="forceAlignRight">
									<ct:FWLabel key="JSP_CRE_R_PRESTATION_DEJA_VERSEE"/>
								</td>
								<td class="forceAlignRight">
									<ct:FWLabel key="JSP_CRE_R_EN_FAVEUR_ASSURE" />
								</td>
								<td colspan="2">
									&nbsp;
								</td>
							</tr>
							<tr class="forceTRHeight">
								<td>
									<ct:FWLabel key="JSP_CRE_R_MONTANT" />
								</td>
								<td class="forceAlignRight">
									<span>
										<strong>
											<%=new FWCurrency(viewBean.getMontantRetroactif()).toStringFormat()%>
										</strong>
									</span>
								</td>
								<td class="forceAlignRight">
									<span>
										<strong>
											<%=new FWCurrency(viewBean.getMontantPrestationsDejaVersees()).toStringFormat()%>
										</strong>
									</span>
								</td>
								<td class="forceAlignRight">
									<span>
										<strong>
											<%=new FWCurrency(viewBean.getMontantEnFaveurAssure()).toStringFormat()%>
										</strong>
									</span>
								</td>
								<td colspan="2">
									&nbsp;
								</td>
							</tr>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
<%
	if (viewBean.hasRenteAccordee(noDemandeRente) && viewBean.hasCreancier(noDemandeRente)) { 
%>
			<ct:ifhasright element="corvus.creances.creancier.actionRepartirLesCreances" crud="upd">
				<input	type="button" 
						value="Répartir les créances" 
						onclick="repartirLesCreances()" />
			</ct:ifhasright>
<%
	}
	if (viewBean.isAfficherBTImprimer() && viewBean.hasRenteAccordee(noDemandeRente)) {
%>
				<input	type="button" 
						value="<ct:FWLabel key="JSP_CRE_D_GENERER_DEM_COMP" />" 
						onclick="demandeCompensation()" />
<%
	}
	if ("true".equals(isPreparerDecision)) {
%>
			<ct:ifhasright element="corvus.decisions.preparerDecisions" crud="upd">
				<input	type="button" 
						value="<ct:FWLabel key="MENU_OPTION_PREPARER_DECISION" />" 
						onclick="preparerDecision()" />
			</ct:ifhasright>
<%
	}
%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%@ include file="/theme/capage/bodyClose.jspf" %>
