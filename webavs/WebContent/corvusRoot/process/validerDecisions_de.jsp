<%@ include file="/theme/process/header.jspf" %>

<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%@ page import="globaz.corvus.api.decisions.IREDecision"%>
<%@ page import="globaz.corvus.servlet.IREActions"%>
<%@ page import="globaz.corvus.utils.REPmtMensuel"%>
<%@ page import="globaz.corvus.vb.decisions.REDecisionsViewBean"%>
<%@ page import="globaz.corvus.vb.prestations.REPrestationsJointDemandeRenteViewBean"%>
<%@ page import="globaz.corvus.vb.process.REValiderDecisionsViewBean"%>
<%@ page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@ page import="globaz.framework.controller.FWController"%>
<%@ page import="globaz.globall.db.BSession"%>
<%@ page import="globaz.jade.publish.client.JadePublishDocument"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="java.util.Iterator"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%
	idEcran="PRE2017";

	REValiderDecisionsViewBean viewBean = (REValiderDecisionsViewBean)request.getAttribute("viewBean");

	userActionValue = IREActions.ACTION_PROCESS_VALIDER_DECISIONS + ".executer";
	selectedIdValue = viewBean.getIdDemandeRente();

	String noDemandeRente = viewBean.getIdDemandeRente();
	String idTierRequerant = viewBean.getIdTiersRequerant();
	String eMailAddress = viewBean.getEMailAddress();
	String requerantDescription = viewBean.getRequerantInfo();

	showProcessButton = false;

	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
	FWController controller = (FWController) session.getAttribute("objController");
	BSession objSession = (BSession)controller.getSession();
%>
<%@ include file="/theme/process/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/ajaxUtils.js"></script>
<script type="text/javascript">

	var $userAction,
		$form,
		$typeValidation,
		$decisionATraiter,
		$boutonToutValider;

	function postInit(){
		//Affichage de la décision dans une autre fenêtre
<%
	if (viewBean.getDocumentsPreview() != null && viewBean.getDocumentsPreview().size() > 0) {
		for (int i = 0; i < viewBean.getDocumentsPreview().size(); i++) {
			String docName = ((JadePublishDocument) viewBean.getDocumentsPreview().get(i)).getDocumentLocation();
			docName = docName.substring(docName.lastIndexOf("/"));
%>		window.open("<%=request.getContextPath() + ((String)request.getAttribute("mainServletPath")) + "Root/work" + docName%>");
<%
		}
	}
%>
	}

	$(document).ready(function () {
		$userAction = $('input[name="userAction"]');
		$form = $('form[name="mainForm"]');
		$typeValidation = $('#typeValidation');
		$decisionATraiter = $('#decisionATraiter');
		$boutonToutValider = $('#validerTout');

		$boutonToutValider.click(function () {
			var $this = $(this);

			$this.prop('disabled', true);
			ajaxUtils.addOverlay($('html'));

			$typeValidation.val('toutValider');
			$userAction.val('<%=IREActions.ACTION_PROCESS_VALIDER_DECISIONS%>.executerTTT');
			$form.submit();
		});

		$form.on('click', '.boutonsPrevalider', function () {
			var $this = $(this);

			$this.prop('disabled', true);
			ajaxUtils.addOverlay($('html'));

			var n_idDecision = $this.attr('idDecision');

			$typeValidation.val('prevalider');
			$decisionATraiter.val(n_idDecision);
			$userAction.val('<%=IREActions.ACTION_PROCESS_VALIDER_DECISIONS%>.executerTTT');
			$form.submit();
		});
	});
</script>
<%
	if (JadeStringUtil.isNull(menuOptionToLoad) || JadeStringUtil.isEmpty(menuOptionToLoad)) {
%>	<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu" />
	<ct:menuChange displayId="options" menuId="corvus-optionsempty" />
<%
	} else if ("decision".equals(menuOptionToLoad)) {
%>	<ct:menuChange displayId="options" menuId="corvus-optionsdecisions" showTab="options">
		<ct:menuSetAllParams key="selectedId" checkAdd="no" value="<%=viewBean.getIdDecision()%>" />
		<ct:menuSetAllParams key="noDemandeRente" checkAdd="no" value="<%=viewBean.getIdDemandeRente()%>" />
		<ct:menuSetAllParams key="idTierRequerant" checkAdd="no" value="<%=viewBean.getIdTiersRequerant()%>" />
		<ct:menuSetAllParams key="provenance" checkAdd="no" value="<%=REPrestationsJointDemandeRenteViewBean.FROM_ECRAN_DECISIONS%>" />
		<ct:menuSetAllParams key="idPrestation" checkAdd="no" value="<%=viewBean.getIdPrestation()%>" />
		<ct:menuSetAllParams key="montantPrestation" checkAdd="no" value="<%=viewBean.getMontantPrestation()%>" />
		<ct:menuSetAllParams key="idLot" checkAdd="no" value="<%=viewBean.getIdLot()%>" />
<%
		// On affiche le sous-menu Lot uniquement dans le cas ou il n'y à qu'une décision.
		REDecisionsViewBean decision = null;
		if (((REValiderDecisionsViewBean)viewBean).getDecisionsList().size() == 1) {
			decision = (REDecisionsViewBean) ((REValiderDecisionsViewBean)viewBean).getDecisionsList().get(0);
%>		<ct:menuSetAllParams key="idLot" checkAdd="no" value="<%=decision.getIdLot()%>" />
<%		}

		if (decision == null || JadeStringUtil.isBlankOrZero(decision.getIdLot())) {
%>		<ct:menuActivateNode active="no" nodeId="afficherLot" />
<%
		} else { 
%>		<ct:menuActivateNode active="yes" nodeId="afficherLot" />
<%
		}

		if (IREDecision.CS_ETAT_ATTENTE.equals(viewBean.getCsEtatDecision())) {
%>		<ct:menuActivateNode active="no" nodeId="imprimerDecision" />
<%
		} else {
%>		<ct:menuActivateNode active="yes" nodeId="imprimerDecision" />
<%
		}
		if (REPmtMensuel.isValidationDecisionAuthorise(objSession)) {
%>		<ct:menuActivateNode active="yes" nodeId="validerdecision" />
<%
		} else {
%>		<ct:menuActivateNode active="no" nodeId="validerdecision" />
<%
		}
%>
	</ct:menuChange>
<%
	}
%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<ct:FWLabel key="JSP_VALID_D_TITRE" />
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<!--  Données générales de la demande de rente -->
						<input	type="hidden" 
								name="idTiersRequerant" 
								value="<%= viewBean.getIdTiersRequerant() %>" />
						<input	type="hidden" 
								name="idDemandeRente" 
								value="<%= viewBean.getIdDemandeRente() %>" />
						<input	type="hidden" 
								id="decisionATraiter" 
								name="decisionATraiter" 
								value="" />
						<input	type="hidden" 
								id="typeValidation" 
								name="typeTTT" 
								value="" />
						<input	type="hidden" 
								name="menuOptionToLoad" 
								value="<%=menuOptionToLoad%>" />
						<tr>
							<td>
								<label for="requerantDescription">
									<b>
										<ct:FWLabel key="JSP_VAL_D_REQUERANT" />
									</b>
								</label>
							</td>
							<td colspan="3">
								<input	type="text" 
										name="requerantDescription" 
										value="<%=requerantDescription%>" 
										style="width:550px;" 
										class="RElibelleExtraLongDisabled" 
										disabled="true" 
										READONLY />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_VAL_D_NO_DEMANDE" />
							</td>
							<td>
								<input	type="text" 
										name="idDemandeRente" 
										value="<%= viewBean.getIdDemandeRente() %>" 
										disabled="true" 
										size="10" 
										READONLY />
								</td>
							</tr>
							<tr>
								<td colspan="8">
									&nbsp;
								</td>
							</tr>
							<tr>
								<td colspan="8">
									<hr />
								</td>
							</tr>
							<tr>
								<td colspan="8">
									&nbsp;
								</td>
							</tr>
							<!--  Données de chaque décision avec option -->
<%
	boolean isAllPrevalider = true;
	boolean isAllValider = true;
	boolean isDecisionOld = false;
	boolean isValideOrPrevalide = true;

	Iterator<REDecisionsViewBean> decisionIter = viewBean.getDecisionsIterator();
	while (decisionIter.hasNext()) {
		REDecisionsViewBean decision = decisionIter.next();
		// Décision n° XX    |   Date décision : XXX    |   Etat : XXX    |   Prevalider / Valider   |  Afficher
%>
							<tr>
								<td>
									<ct:FWLabel key="JSP_VAL_D_DECISION_INFO" />
								</td>
								<td>
									<input	type="text" 
											name="idDecision" 
											value="<%= decision.getIdDecision() + " / Pour : " + decision.getBeneficiaireInfo()%>" 
											disabled="true" 
											size="60" 
											readonly="readonly" />
								</td>
								<td>
									<ct:FWLabel key="JSP_VAL_D_DATE_DECISION" />
								</td>
									<td>
										<input	type="text" 
												name="dateDecision" 
												value="<%=decision.getDateDecision()%>" 
												disabled="true" 
												size="10" 
												readonly="readonly" />
									</td>
									<td>
										<ct:FWLabel key="JSP_VAL_D_ETAT" />
									</td>
									<td>
										<input	type="text" 
												name="etatDecisions" 
												value="<%=decision.getCsEtatDecisionLibelle()%>" 
												disabled="true" 
												size="10" 
												readonly="readonly" />
									</td>
									<td>
<%
		if (!decision.getCsEtat().equals(IREDecision.CS_ETAT_PREVALIDE)) {
			isAllPrevalider = false;
		} 
		if (!decision.getCsEtat().equals(IREDecision.CS_ETAT_VALIDE)){
			isAllValider = false;
		}
		if (!decision.getCsEtat().equals(IREDecision.CS_ETAT_PREVALIDE) 
			&& !decision.getCsEtat().equals(IREDecision.CS_ETAT_VALIDE)) {
			isValideOrPrevalide = false;
		}
		// si état = attente, afficher prévalider
		// si état = prevalider, afficher valider
		// si état = valider, ne rien afficher
		if (decision.getCsEtat().equals(IREDecision.CS_ETAT_ATTENTE)) { 
%>										<ct:ifhasright element="<%=IREActions.ACTION_DECISIONS%>" crud="u">
											<input	type="button" 
													class="boutonsPrevalider" 
													value="Prévalider" 
													idDecision="<%=decision.getIdDecision()%>" />
										</ct:ifhasright>
<%
		} else if (decision.getCsEtat().equals(IREDecision.CS_ETAT_PREVALIDE)) {
			if (!decision.isDateDecisionInferieureMoisPaiement().booleanValue()) {
				isDecisionOld = true;
			}
		}
%>									</td>
								</tr>
								<tr>
									<td colspan="4">
										&nbsp;
									</td>
								</tr>
<%
	}

	System.out.println("isAllValier = " + isAllValider);
	System.out.println("isAllPrevalider = " + isAllPrevalider);
	System.out.println("isAllValideOrPrevalide = " + isValideOrPrevalide);

	if (!isAllValider) {
%>								<tr>
									<td colspan="8">
										&nbsp;
									</td>
								</tr>
								<tr>
									<td colspan="8">
										<hr />
									</td>
								</tr>
								<tr>
									<td colspan="8" align="center">
<%
		// Gérer les boutons de fin de page (Tout valider)
		if (isValideOrPrevalide) {
			if (FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())) {
%>										<p style="color:red;">
											<%=viewBean.getMessage()%>
										</p>
<%
			} else {
				if (isDecisionOld) {
%>										<p style="color:red;">
											<ct:FWLabel key="JSP_VAL_D_TEXTE_1" />
										</p>
<%				} else {
%>										<ct:ifhasright element="<%=IREActions.ACTION_DECISIONS%>" crud="u">
											<input	type="button" 
													id="validerTout" 
													value="Valider tout" />
										</ct:ifhasright>
<%
				}
			}
		} else {
%>										<p style="color:red;">
											<ct:FWLabel key="JSP_VAL_D_TEXTE" />
										</p>
<%
		}
%>									</td>
								</tr>
								<tr>
									<td colspan="8">
										<hr />
									</td>
								</tr>
<%
	}
%>
<%@ include file="/theme/process/footer.jspf" %>
<%@ include file="/theme/process/bodyClose.jspf" %>
