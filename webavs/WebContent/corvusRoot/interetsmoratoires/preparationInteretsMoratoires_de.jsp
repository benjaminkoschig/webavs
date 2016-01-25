<%@ include file="/theme/detail/header.jspf" %>

<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%@ page import="globaz.corvus.servlet.IREActions"%>
<%@ page import="globaz.corvus.vb.interetsmoratoires.REPreparationInteretMoratoireViewBean"%>
<%@ page import="globaz.globall.db.BSession"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>

<%
	// Les labels de cette page commence par la préfix "JSP_PIM_D"

	idEcran="PRE0050";

	REPreparationInteretMoratoireViewBean viewBean = (REPreparationInteretMoratoireViewBean) session.getAttribute("viewBean");

	bButtonDelete = false;
	bButtonUpdate = false;
	bButtonValidate = false;

	// lorsque l'on arrive depuis preparerDecisions
	String eMailAddress = request.getParameter("eMailAddress");
	String dateDecision = request.getParameter("dateDecision");
	String csTypePreparationDecision = request.getParameter("csTypePreparationDecision");
%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" />
<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="menu">
</ct:menuChange>

<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/ajaxUtils.js"></script>
<script type="text/javascript"> 

	var $boutonSansInteretMoratoire,
		$boutonPreparerCalcul,
		$userAction,
		$testRetenue,
		$form;

	function add(){
	}

	function upd(){
	}

	function validate() {
	}

	function cancel() {
		$userAction.val('back');
	}

	function del() {
	}

	function init(){
	}

	function showWarning() {
		if (errorObj.text != "") {
			if (window.confirm("<%=viewBean.getMessage().trim()%>\n\nVoulez-vous continuer ?")) {
				$testRetenue.val(false);
				$form.submit();
			}
		}
		$testRetenue.val(true);
	}

	$(document).ready(function () {
		$boutonSansInteretMoratoire = $('#boutonSansInteretMoratoire');
		$boutonPreparerCalcul = $('#boutonPreparerCalcul');
		$userAction = $('input[name="userAction"]');
		$testRetenue = $('#testRetenue');
		$form = $('form[name="mainForm"]');

		$boutonPreparerCalcul.click(function () {
			var $this = $(this);

			$this.prop('disabled', true);
			ajaxUtils.addOverlay($('html'));

			$userAction.val("<%=IREActions.ACTION_CALCUL_INTERET_MORATOIRE%>.calculerInteretMoratoire");
			$form.attr('target', 'fr_main');
			$form.submit();
		});

		$boutonSansInteretMoratoire.click(function () {
			var $this = $(this);

			$this.prop('disabled', true);
			ajaxUtils.addOverlay($('html'));

			$userAction.val("<%=IREActions.ACTION_CALCUL_INTERET_MORATOIRE%>.genererDecisionSansInteretMoratoire");
			$form.attr('target', 'fr_main');
			$form.submit();
		});
	});
</script>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<ct:FWLabel key="JSP_PIM_D_TITRE" />
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<tr>
							<td>
								<ct:FWLabel key="JSP_PIM_D_DEMANDE_NO" />
							</td>
							<td>
								<input	type="text" 
										class="disabled" 
										name="noDemandeRente" 
										readonly="readonly" 
										value="<%=viewBean.getIdDemandeRente()%>" />
								<input	type="hidden" 
										name="IdDemandeRente" 
										value="<%=viewBean.getIdDemandeRente()%>" />
								<input	type="hidden" 
										name="idDemandeRente" 
										value="<%= viewBean.getIdDemandeRente() %>" />
								<input	type="hidden" 
										name="noDemandeRente" 
										value="<%= viewBean.getIdDemandeRente() %>" />
								<input	type="hidden" 
										name="eMailAddress" 
										value="<%=eMailAddress != null ? eMailAddress : ""%>" />
								<input	type="hidden" 
										name="csTypePreparationDecision" 
										value="<%=csTypePreparationDecision != null ? csTypePreparationDecision : ""%>" />
								<input	type="hidden" 
										name="fromPreparerDecisions" 
										value="<%="true".equals(request.getParameter("fromPreparerDecisions")) ? "true" : "false"%>" />
								<input	type="hidden" 
										name="decisionDepuis" 
										value="<%= viewBean.getDecisionDepuis() %>" />
								<input	type="hidden" 
										name="testRetenue" 
										value="true" />
							</td>
							<td>
								<ct:FWLabel key="JSP_PIM_D_ASSURE" />
							</td>
							<td>
								<input	type="hidden" 
										name="idTiersDemandeRente" 
										value="<%=viewBean.getIdTierRequerant()%>" />
								<input	type="hidden" 
										name="idTiersRequerant" 
										value="<%=viewBean.getIdTierRequerant()%>" />
								<input	type="hidden" 
										name="idTierRequerant" 
										value="<%=viewBean.getIdTierRequerant()%>" />
								<re:PRDisplayRequerantInfoTag	session="<%=(BSession) controller.getSession()%>" 
																idTiers="<%=viewBean.getIdTierRequerant()%>" 
																style="<%=PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>" />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_PIM_D_DATE_DEPOT_DEMANDE" />
							</td>
							<td>
								<input	id="dateDepotDemande" 
										name="dateDepotDemande" 
										data-g-calendar="type:default" 
										value="<%=viewBean.getDateDepotDemande()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_PIM_D_DEBUT_DROIT" />
							</td>
							<td>
								<input	id="dateDebutDroit" 
										name="dateDebutDroit" 
										data-g-calendar="type:default" 
										value="<%=viewBean.getDateDebutDroit()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_PIM_D_DATE_DECISION" />
							</td>
							<td colspan="3">
								<input	id="dateDecision" 
										name="dateDecision" 
										data-g-calendar="type:default" 
										value="<%=JadeStringUtil.isEmpty(dateDecision)?viewBean.getDateDecision():dateDecision%>" />
							</td>
						</tr>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<input	type="button" 
						id="boutonPreparerCalcul" 
						value="<ct:FWLabel key="JSP_PIM_D_PREPARER_CALCUL" />" />
<%
	if ("true".equals(request.getParameter("fromPreparerDecisions"))) {
%>				<input	type="button" 
						id="boutonSansInteretMoratoire" 
						value="<ct:FWLabel key="JSP_PIM_D_SANS_INTERET" />" />
<%
	}
%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%@ include file="/theme/detail/footer.jspf" %>
