<%@page import="globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag"%>
<%@ include file="/theme/process/header.jspf" %>

<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%@ page import="globaz.corvus.api.decisions.IREPreparationDecision"%>
<%@ page import="globaz.corvus.api.basescalcul.IRERenteAccordee"%>
<%@ page import="globaz.corvus.servlet.IREActions"%>
<%@ page import="globaz.corvus.utils.REPmtMensuel"%>
<%@ page import="globaz.corvus.vb.decisions.REPreparerDecisionStandardViewBean"%>
<%@ page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@ page import="globaz.framework.controller.FWController"%>
<%@ page import="globaz.globall.db.BSession"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	idEcran = "PRE2001";

	REPreparerDecisionStandardViewBean viewBean = (REPreparerDecisionStandardViewBean) request.getAttribute("viewBean");
	FWController controller = (FWController) session.getAttribute("objController");

	selectedIdValue = viewBean.getIdDemandeRenteToString();
	String noDemandeRente = viewBean.getIdDemandeRenteToString();

	if (JadeStringUtil.isIntegerEmpty(noDemandeRente)) {
		noDemandeRente = request.getParameter("noDemandeRente");
	}

	String idTierRequerant = viewBean.getIdTiersRequerant();
	if(JadeStringUtil.isIntegerEmpty(idTierRequerant)){
		idTierRequerant = request.getParameter("idTierRequerant");
	}

	String idTiersBeneficiaire = request.getParameter("idTiersBeneficiaire");
	String requerantDescription = viewBean.getDetailRequerant();
	String idBaseCalcul = request.getParameter("idBaseCalcul");
	String csTypeBasesCalcul = request.getParameter("csTypeBasesCalcul");
	String idRenteAccordee = request.getParameter("idRenteAccordee");
	String csEtatRenteAccordee = request.getParameter("csEtatRenteAccordee");
	String dateFinDroit = request.getParameter("dateFinDroit");
	String isPreparationDecisionValide = request.getParameter("isPreparationDecisionValide");

	String menuOptionToLoad = request.getParameter("menuOptionToLoad");

	userActionValue = IREActions.ACTION_PREVALIDER_DECISION + ".genererDecision";

	String[] moisAnneeCourantEtSuivant = viewBean.getMoisAnneeCourantEtSuivant();

	autoShowErrorPopup = false;
%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%
	if (JadeStringUtil.isNull(menuOptionToLoad) || JadeStringUtil.isEmpty(menuOptionToLoad)) {
%>	<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" />
	<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="menu" />
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
		if ((IRERenteAccordee.CS_ETAT_AJOURNE.equals(csEtatRenteAccordee)
			|| IRERenteAccordee.CS_ETAT_CALCULE.equals(csEtatRenteAccordee)
			|| IRERenteAccordee.CS_ETAT_DIMINUE.equals(csEtatRenteAccordee))
			|| (!JadeStringUtil.isBlankOrZero(dateFinDroit))
			|| !REPmtMensuel.isValidationDecisionAuthorise((BSession)controller.getSession())) {
%>		<ct:menuActivateNode active="no" nodeId="optdiminution" />
<%
		}
		if ("false".equals(isPreparationDecisionValide)) {
%>		<ct:menuActivateNode active="no" nodeId="preparerDecisionRA" />
<%
		}
%>	</ct:menuChange>
<%
	}
%>
<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/ajaxUtils.js"></script>
<script type="text/javascript">

	function cancel(){
		document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE%>.chercher";
		document.forms[0].submit();
	}

	function validate(){
<%
	if (viewBean.isDemandeRenteWithIM() && !viewBean.areIMCalculated()) {
%>		if (document.forms[0].elements('csTypePreparationDecision').value == "<%=IREPreparationDecision.CS_TYP_PREP_DECISION_STANDARD%>" 
			|| <%=viewBean.getIsDemandeCourantValide().booleanValue()%>) {
			document.forms[0].elements('fromPreparerDecisions').value = "true";
			document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_PREPARER_INTERETS_MORATOIRES%>.afficher";
			document.forms[0].submit();
		} else {
			document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_PREVALIDER_DECISION%>.genererDecision";
			document.forms[0].submit();
		}
<%
	} else {
%>		document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_PREVALIDER_DECISION%>.genererDecision";
		document.forms[0].submit();
<%
	}
%>	}

	function upd(){	
	}

	function add(){
	}

	function del(){
	}

	function doInitThings() {
		document.getElementsByName("dateDecision")[0].focus();
		showErrors();
	}

	function postInit(){
<%
	if (vBeanHasErrors) {
%>		errorObj.text = "<%=viewBean.getMessage().trim()%>";
		showErrors();
		errorObj.text = "";
<%
	}
	if (viewBean.getMsgType().equals (FWViewBeanInterface.WARNING)) {
%>		errorObj.text = "<%=viewBean.getMessage().trim()%>";
		showWarning();
		errorObj.text = "";
<%
	}
%>	changeGenre();
<%
	autoShowErrorPopup = true;
%>	}

	function showErrors() {
		changeGenre();
		if (errorObj.text != "") {
			showModalDialog('errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');
		}
	}

	function showWarning() {
		if (errorObj.text != "") {
			if (window.confirm("<%=viewBean.getMessage().trim()%>\n\nVoulez-vous continuer ?")) {
				document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_PREVALIDER_DECISION%>.genererDecision";
				document.forms[0].elements('testRetenue').value = false;
				document.forms[0].submit();
			}
		}
		document.forms[0].elements('testRetenue').value = true;
	}

	function changeGenre() {
		if (document.forms[0].elements('csTypePreparationDecision').value == "<%=IREPreparationDecision.CS_TYP_PREP_DECISION_COURANT%>"){
			document.all('genreCourant').style.display = 'block';
		} else if (document.forms[0].elements('csTypePreparationDecision').value == "<%=IREPreparationDecision.CS_TYP_PREP_DECISION_STANDARD%>"){
			document.all('genreCourant').style.display = 'none';
		} else if (document.forms[0].elements('csTypePreparationDecision').value == "<%=IREPreparationDecision.CS_TYP_PREP_DECISION_RETRO%>"){
			document.all('genreCourant').style.display = 'none';
		} else {
			document.all('genreCourant').style.display = 'none';
		}
	}

	$(document).ready(function () {
		var $boutonOk = $('#btnOk');
		$boutonOk.click(function () {
			var $this = $(this);
			$this.prop('disabled', true);
			ajaxUtils.addOverlay($('html'));
		});
	});
</script>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<ct:FWLabel key="JSP_PREP_D_TITRE"/>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<input	type="hidden" 
								name="idTiersRequerant" 
								value="<%=idTierRequerant%>" />
						<input	type="hidden" 
								name="idTierRequerant" 
								value="<%=idTierRequerant%>" />
						<input	type="hidden" 
								name="idDemandeRente" 
								value="<%= viewBean.getIdDemandeRente() %>" />
						<input	type="hidden" 
								name="noDemandeRente" 
								value="<%= viewBean.getIdDemandeRente() %>" />
						<input	type="hidden" 
								name="fromPreparerDecisions" 
								value="false" />
						<input	type="hidden" 
								name="testRetenue" 
								value="true" />
						<tr>
							<td>
								<label for="requerantDescription">
									<strong>
										<ct:FWLabel key="JSP_PRP_D_REQUERANT" />
									</strong>
								</label>
							</td>
							<td colspan="3">
								<re:PRDisplayRequerantInfoTag
											session="<%=(BSession)controller.getSession()%>"
											idTiers="<%=idTierRequerant%>"
											style="<%=PRDisplayRequerantInfoTag.STYLE_LONG_WITHOUT_LABEL%>"/>
							</td>
						</tr>
						<tr>
							<td>
								<label for="noDemandeRente">
									<ct:FWLabel key="JSP_PRP_D_NO_DEMANDE" />
								</label>
							</td>
							<td colspan="3">
								<input	type="text" 
										id="noDemandeRente" 
										name="noDemandeRente" 
										value="<%=viewBean.getIdDemandeRente()%>" 
										disabled="disabled" 
										size="7" 
										readonly="readonly" />
							</td>
						</tr>
						<tr>
							<td colspan="4">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<label for="eMailAddress">
									<ct:FWLabel key="JSP_PRP_D_EMAIL"/>
								</label>
							</td>
							<td>
								<input	type="text" 
										id="eMailAddress" 
										name="eMailAddress" 
										value="<%=controller.getSession().getUserEMail()%>" 
										class="libelleLong" />
							</td>
						</tr>
						<tr>
							<td>
								<label for="dateDecision">
									<ct:FWLabel key="JSP_PRP_D_DATE_DOCUMENT" />
								</label>
							</td>
							<td>
								<input	id="dateDecision" 
										name="dateDecision" 
										data-g-calendar="type:default" 
										value="<%=viewBean.getCurrentDate()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<label for="csTypePreparationDecision">
									<ct:FWLabel key="JSP_PRP_D_GENRE_DECISION" />
								</label>
							</td>
							<td nowrap="nowrap">
<%
	String df = viewBean.getIsDemandeCourantValide().booleanValue() ? IREPreparationDecision.CS_TYP_PREP_DECISION_RETRO : "";
	if (viewBean.getIsDemandeCourantValide().booleanValue()) {
%>								<ct:select name="csTypePreparationDecision" defaultValue="<%=df%>" onchange="changeGenre()">
									<ct:optionsCodesSystems csFamille="<%=IREPreparationDecision.CS_GROUPE_TYPE_PREPARATION_DECISION %>" />
								</ct:select>
								<script type="text/javascript"> 
									document.getElementById("csTypePreparationDecision").disabled = true;
								</script>
								<input	type="hidden" 
										name="csTypePreparationDecision" 
										value="<%=IREPreparationDecision.CS_TYP_PREP_DECISION_RETRO%>" />
<%
	} else {
%>								<ct:select name="csTypePreparationDecision" defaultValue="<%=df%>" onchange="changeGenre()">
									<ct:optionsCodesSystems csFamille="<%= IREPreparationDecision.CS_GROUPE_TYPE_PREPARATION_DECISION %>">
										<ct:excludeCode code="<%=IREPreparationDecision.CS_TYP_PREP_DECISION_RETRO%>"/>
									</ct:optionsCodesSystems>
								</ct:select>
<%
	}
%>							</td>
						</tr>
					</tbody>
					<tbody id="genreCourant">
						<tr>
							<td>
								<label for="decisionDepuis">
									<ct:FWLabel key="JSP_PRP_D_DATE_DEPUIS" />
								<label>
							</td>
							<td>
								<select id="decisionDepuis" name="decisionDepuis">
<%
	for (int i = 0; i < moisAnneeCourantEtSuivant.length; i++) {
%>									<option value="<%=moisAnneeCourantEtSuivant[i]%>">
										<%=moisAnneeCourantEtSuivant[i]%>
									</option>
<%
	}
%>								</select>
							</td>
						</tr>
						<tr>
							<td colspan="4">
								<hr />
							</td>
						</tr>
					</tbody>
<%@ include file="/theme/process/footer.jspf" %>
<%@ include file="/theme/process/bodyClose.jspf" %>
