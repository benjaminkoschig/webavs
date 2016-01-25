<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%@ page import="globaz.corvus.api.basescalcul.IREPrestationDue"%>
<%@ page import="globaz.corvus.api.prestations.IREPrestations"%>
<%@ page import="globaz.corvus.api.basescalcul.IRERenteAccordee"%>
<%@ page import="globaz.corvus.servlet.IREActions"%>
<%@ page import="globaz.corvus.utils.REPmtMensuel"%>
<%@ page import="globaz.corvus.vb.rentesaccordees.REPrestationsDuesJointDemandeRenteViewBean"%>
<%@ page import="globaz.framework.util.FWCurrency"%>
<%@ page import="globaz.globall.db.BSession"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>

<%@ include file="/theme/detail/header.jspf" %>
<%
	// Les labels de cette page commence par la préfix "JSP_MVE_D"

	idEcran = "PRE0018";

	REPrestationsDuesJointDemandeRenteViewBean viewBean = (REPrestationsDuesJointDemandeRenteViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdPrestationDue();

	String idTierRequerant = request.getParameter("idTierRequerant");
	String noDemandeRente = request.getParameter("noDemandeRente");
	String idRenteAccordee = request.getParameter("idRenteAccordee");
	String idBaseCalcul = request.getParameter("idBaseCalcul");
	String csTypeBasesCalcul = request.getParameter("csTypeBasesCalcul");
	String csEtatRenteAccordee = request.getParameter("csEtatRenteAccordee");
	String dateFinDroit = request.getParameter("dateFinDroit");
	String isPreparationDecisionValide = request.getParameter("isPreparationDecisionValide");
	
	bButtonUpdate = viewBean.isModifiable(noDemandeRente) && bButtonUpdate;
	bButtonDelete = !IRERenteAccordee.CS_ETAT_VALIDE.equals(csEtatRenteAccordee) && bButtonDelete;
	
	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%
	if (JadeStringUtil.isNull(menuOptionToLoad) || JadeStringUtil.isEmpty(menuOptionToLoad)) {
%>
	<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu" />
	<ct:menuChange displayId="options" menuId="corvus-optionsempty" />
<%
	} else if("rentesaccordees".equals(menuOptionToLoad)) {
%>	<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="options" />
	<ct:menuChange displayId="options" menuId="corvus-optionsrentesaccordees">
		<ct:menuSetAllParams key="selectedId" value="<%=idRenteAccordee%>"/>
		<ct:menuSetAllParams key="noDemandeRente" value="<%=noDemandeRente%>" />
		<ct:menuSetAllParams key="idTierRequerant" value="<%=idTierRequerant%>" />
		<ct:menuSetAllParams key="idRenteAccordee" value="<%=idRenteAccordee%>" />
		<ct:menuSetAllParams key="idBaseCalcul" value="<%=idBaseCalcul%>" />
		<ct:menuSetAllParams key="csTypeBasesCalcul" value="<%=csTypeBasesCalcul%>" />
<%
		if ((IRERenteAccordee.CS_ETAT_AJOURNE.equals(csEtatRenteAccordee)
			|| IRERenteAccordee.CS_ETAT_CALCULE.equals(csEtatRenteAccordee)
			|| IRERenteAccordee.CS_ETAT_DIMINUE.equals(csEtatRenteAccordee))
			|| (!JadeStringUtil.isBlankOrZero(dateFinDroit))
			|| !REPmtMensuel.isValidationDecisionAuthorise(objSession)) {
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
<script type="text/javascript">
	var warningObj = {
		'text': ''
	};

	function add() {
		document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_PRESTATIONS_DUES_JOINT_DEMANDE_RENTE%>.ajouter";
	}

	function upd() {
		document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_PRESTATIONS_DUES_JOINT_DEMANDE_RENTE%>.modifier";
	}

	function showWarnings() {
		if (warningObj.text != "") {
			showModalDialog('<%=servletContext%>/warningModalDlg.jsp', warningObj, 'dialogHeight:20;dialogWidth:25;status:no;resizable:no');
		}
	}

	function validate() {
		state = true;

		// si warning pas encore affiché
		if (document.forms[0].elements('isWarningAffiche').value == "false") {
			if (document.forms[0].elements('_method').value == "add") {
				document.forms[0].elements('isWarningAffiche').value = "true";
				document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_PRESTATIONS_DUES_JOINT_DEMANDE_RENTE%>.ajouter";
			} else {
				document.forms[0].elements('isWarningAffiche').value = "true";
				document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_PRESTATIONS_DUES_JOINT_DEMANDE_RENTE%>.modifier";
			}
		// ttt normal
		} else {
			if (document.forms[0].elements('_method').value == "add") {
				document.forms[0].elements('isWarningAffiche').value = "false";
				document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_PRESTATIONS_DUES_JOINT_DEMANDE_RENTE%>.ajouter";
			}else{
				document.forms[0].elements('isWarningAffiche').value = "false";
				document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_PRESTATIONS_DUES_JOINT_DEMANDE_RENTE%>.modifier";
			}
		}
		return state;
	}

	function cancel() {
		document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_PRESTATIONS_DUES_JOINT_DEMANDE_RENTE%>.chercher";
	}

	function del() {
		if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")) {
			document.forms[0].elements('userAction').value = "<%=IREActions.ACTION_PRESTATIONS_DUES_JOINT_DEMANDE_RENTE%>.supprimer";
			document.forms[0].submit();
		}
	}

	function init() {
	}
</script>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
					<ct:FWLabel key="JSP_MVE_D_TItrE" />
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<tr>
							<td>
								<label for="beneficiaireDescription">
									<ct:FWLabel key="JSP_MVE_D_BENEFICIAIRE" />
								</label>
							</td>
							<td colspan="3">
								<re:PRDisplayRequerantInfoTag	session="<%=(BSession)controller.getSession()%>" 
																idTiers="<%=idTierRequerant%>" 
																style="<%=PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>" />
								<input type="hidden" name="isWarningAffiche" value="<%=viewBean.getIsWarningAffiche()%>" />
								<input type="hidden" name="idTierRequerant" value="<%=idTierRequerant%>" />
								<input type="hidden" name="noDemandeRente" value="<%=noDemandeRente%>" />
								<input type="hidden" name="idRenteAccordee" value="<%=idRenteAccordee%>" />
								<input type="hidden" name="menuOptionToLoad" value="<%=menuOptionToLoad%>" />
							</td>
						</tr>
						<tr>
							<td colspan="4">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_MVE_D_DATE_DEBUT_PAIEMENT" />
							</td>
							<td>
								<input	id="dateDebutPaiement" 
										name="dateDebutPaiement" 
										data-g-calendar="type:month" 
										value="<%=viewBean.getDateDebutPaiement()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_MVE_D_DATE_FIN_PAIEMENT" />
							</td>
							<td>
								<input	id="DateFinPaiement" 
										name="DateFinPaiement" 
										data-g-calendar="type:month" 
										value="<%=viewBean.getDateFinPaiement()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_MVE_D_RAM" />
							</td>
							<td>
								<input	type="text" 
										class="montant" 
										name="ram" 
										value="<%=new FWCurrency(viewBean.getRam()).toStringFormat()%>" 
										onchange="validateFloatNumber(this);" 
										onkeypress="return filterCharForFloat(window.event);" />
							</td>
							<td>
								<ct:FWLabel key="JSP_MVE_D_ETAT" />
							</td>
							<td>
								<ct:select name="csEtat" defaultValue="<%=viewBean.getCsEtat()%>">
									<ct:optionsCodesSystems csFamille="<%=IREPrestationDue.CS_GROUPE_ETAT%>">
									</ct:optionsCodesSystems>
								</ct:select>
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_MVE_D_MONTANT_SUPP_AJ" />
							</td>
							<td>
								<input	type="text" 
										class="montant" 
										name="montantSupplementAjournement" 
										value="<%=new FWCurrency(viewBean.getMontantSupplementAjournement()).toStringFormat()%>" 
										onchange="validateFloatNumber(this);" 
										onkeypress="return filterCharForFloat(window.event);" />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_MVE_D_MONTANT_REDUCT_ANT" />
							</td>
							<td>
								<input	type="text" 
										class="montant" 
										name="montantReductionAnticipation" 
										value="<%=new FWCurrency(viewBean.getMontantReductionAnticipation()).toStringFormat()%>" 
										onchange="validateFloatNumber(this);" 
										onkeypress="return filterCharForFloat(window.event);" />
							</td>
							<td>
								<ct:FWLabel key="JSP_MVE_D_MONTANT_TAUX_REDUC_ANT" />
							</td>
							<td>
								<input	type="text" 
										class="montant" 
										name="tauxReductionAnticipation" 
										value="<%=viewBean.getTauxReductionAnticipation()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_MVE_D_TYPE" />
							</td>
							<td>
								<ct:select name="csType" defaultValue="<%=viewBean.getCsType()%>">
									<ct:optionsCodesSystems csFamille="<%=IREPrestationDue.CS_GROUPE_TYPE%>">
									</ct:optionsCodesSystems>
								</ct:select>
							</td>
							<td>
								<ct:FWLabel key="JSP_MVE_D_MONTANT" />
							</td>
							<td>
								<input	type="text" 
										class="montant" 
										name="montant" 
										value="<%=new FWCurrency(viewBean.getMontant()).toStringFormat()%>" 
										onchange="validateFloatNumber(this);" 
										onkeypress="return filterCharForFloat(window.event);" />
							</td>
						</tr>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%@ include file="/theme/detail/footer.jspf" %>
