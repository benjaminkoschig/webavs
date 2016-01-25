<%@ include file="/theme/capage/header.jspf" %>

<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>

<%@ page import="globaz.corvus.servlet.IREActions"%>
<%@ page import="globaz.corvus.vb.interetsmoratoires.RECalculInteretMoratoireViewBean"%>
<%@ page import="globaz.prestation.jsp.taglib.PRDisplayRequerantInfoTag"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/corvusRoot/corvustaglib.tld" prefix="re" %>
<%
	// Les labels de cette page commence par la préfix "JSP_CIM_R"

	idEcran="PRE0053";

	RECalculInteretMoratoireViewBean viewBean = (RECalculInteretMoratoireViewBean) request.getAttribute("viewBean");

	bButtonNew = false;
	bButtonFind = false;

	IFrameDetailHeight = "250";
	IFrameListHeight = "300";

	// lorsque l'on arrive depuis preparerDecisions
	String eMailAddress = request.getParameter("eMailAddress");
	String csTypePreparationDecision = request.getParameter("csTypePreparationDecision");
	String fromPreparerDecisions = request.getParameter("fromPreparerDecisions");
%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal" showTab="menu" />
<ct:menuChange displayId="options" menuId="corvus-optionsempty" />

<script type="text/javascript" src="<%=servletContext%>/scripts/ajax/ajaxUtils.js"></script>
<script type="text/javascript">

	bFind = true;
	usrAction = "<%=IREActions.ACTION_CALCUL_INTERET_MORATOIRE%>.lister";
	detailLink =	servlet + "?userAction=<%=IREActions.ACTION_CALCUL_INTERET_MORATOIRE%>.afficher" 
					+ "&_method=add" 
					+ "&idDemandeRente=<%=viewBean.getIdDemandeRente()%>" 
					+ "&idTiersDemandeRente=<%=viewBean.getIdTiersDemandeRente()%>" 
					+ "&dateDepotDemande=<%=viewBean.getDateDepotDemande()%>" 
					+ "&dateDebutDroit=<%=viewBean.getDateDebutDroit()%>" 
					+ "&dateDecision=<%=viewBean.getDateDecision()%>" 
					+ "&eMailAddress=<%=eMailAddress%>" 
					+ "&csTypePreparationDecision=<%=csTypePreparationDecision%>" 
					+ "&fromPreparerDecisions=<%=fromPreparerDecisions%>";

	function loadFrames() {
		// prevenir les cursor state not valid exception
		if(bFind) {
			// document.forms[0].submit(); appelle depuis l'ecran DE
			document.fr_detail.location.href = detailLink + "&_valid=new";
		}
	}

	$(document).ready(function () {
		var $boutonCalculer = $('#boutonCalculer'),
			$userAction = $('input[name="userAction"]'),
			$form = $('form[name="mainForm"]');

		$boutonCalculer.click(function () {
			var $this = $(this);

			$this.prop('disabled', true);
			ajaxUtils.addOverlay($('html'));

			$userAction.val("<%=globaz.corvus.servlet.IREActions.ACTION_INTERET_MORATOIRE%>.calculerInteretMoratoire");
			$form.attr('target', 'fr_main');
			$form.submit();
		});
	});

</script>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<ct:FWLabel key="JSP_CIM_R_TITRE" />>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<tr>
							<td>
								<ct:FWLabel key="JSP_CIM_R_DEMANDE_NO" />
							</td>
							<td>
								<input	type="text" 
										name="noDemandeRente" 
										class="disabled" 
										readonly="readonly" 
										value="<%=viewBean.getIdDemandeRente()%>" />
								<input	type="hidden" 
										name="forIdDemandeRente" 
										value="<%=viewBean.getIdDemandeRente()%>" />
								<input	type="hidden" 
										name="idDemandeRente" 
										value="<%=viewBean.getIdDemandeRente()%>" />
								<input	type="hidden" 
										name="eMailAddress" 
										value="<%=eMailAddress%>" />
								<input	type="hidden" 
										name="csTypePreparationDecision" 
										value="<%=csTypePreparationDecision%>" />
								<input	type="hidden" 
										name="fromPreparerDecisions" 
										value="<%="true".equals(request.getParameter("fromPreparerDecisions")) ? "true" : "false"%>" />
								<input	type="hidden" 
										name="decisionDepuis" 
										value="<%= viewBean.getDecisionDepuis() %>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_CIM_R_ASSURE" />
							</td>
							<td>
								<input	type="hidden" 
										name="idTiersDemandeRente" 
										value="<%=viewBean.getIdTiersDemandeRente()%>" />
								<re:PRDisplayRequerantInfoTag	session="<%=viewBean.getSession()%>" 
																idTiers="<%=viewBean.getIdTiersDemandeRente()%>"
																style="<%=PRDisplayRequerantInfoTag.STYLE_CONDENSED_WITHOUT_LABEL%>" />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_CIM_R_DATE_DEPOT_DEMANDE" />
							</td>
							<td>
								<input	type="text" 
										class="dateDisabled" 
										name="dateDepotDemande" 
										readonly="readonly" 
										value="<%=viewBean.getDateDepotDemande()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_CIM_R_DEBUT_DROIT" />
							</td>
							<td>
								<input	type="text" 
										class="dateDisabled" 
										name="dateDebutDroit" 
										readonly="readonly" 
										value="<%=viewBean.getDateDebutDroit()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_CIM_R_DATE_DECISION" />
							</td>
							<td colspan="3">
								<input	type="text" 
										class="dateDisabled" 
										name="dateDecision" 
										readonly="readonly" 
										value="<%=viewBean.getDateDecision()%>" />
							</td>
						</tr>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<input	type="button" 
						id="boutonCalculer" 
						value="<ct:FWLabel key="JSP_CIM_R_CALCULER" />" />
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%@ include file="/theme/capage/bodyClose.jspf" %>
