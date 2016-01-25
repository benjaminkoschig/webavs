<%-- tpl:insert page="/theme/detail.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@ page import="globaz.osiris.db.recouvrement.*"%>
<%
idEcran="GCA60004";
	CAEcheancePlanViewBean viewBean = (CAEcheancePlanViewBean) session.getAttribute("viewBean");
	selectedIdValue = viewBean.getIdEcheancePlan();

	String idPlanRecouvrement = viewBean.getIdPlanRecouvrement();
	if (idPlanRecouvrement == null || idPlanRecouvrement.length() == 0) {
		idPlanRecouvrement = (String) session.getAttribute("idPlanRecouvrement");
		viewBean.setIdPlanRecouvrement(idPlanRecouvrement);
	}
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-PlanRecouvrement" showTab="options">
	<ct:menuSetAllParams key="id" value="<%=viewBean.getIdPlanRecouvrement()%>"/>
	<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getIdPlanRecouvrement()%>"/>
</ct:menuChange>

<SCRIPT language="javascript">

	function add() {
		document.forms[0].elements('userAction').value="osiris.recouvrement.echeancePlan.ajouter"
	}

	function upd() {
	}

	function validate() {
		state = validateFields();
		if (document.forms[0].elements('_method').value == "add")
			document.forms[0].elements('userAction').value="osiris.recouvrement.echeancePlan.ajouter";
		else
			document.forms[0].elements('userAction').value="osiris.recouvrement.echeancePlan.modifier";
		return state;
	}

	function cancel() {
		if (document.forms[0].elements('_method').value == "add")
			document.forms[0].elements('userAction').value="back";
		else
			document.forms[0].elements('userAction').value="osiris.recouvrement.echeancePlan.afficher";
	}

	function del() {
		if (window.confirm("Vous êtes sur le point de supprimer l'objet sélectionné! Voulez-vous continuer?")){
			document.forms[0].elements('userAction').value="osiris.recouvrement.echeancePlan.supprimer";
			document.forms[0].submit();
		}
	}

	function init(){
	}

</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%>Détail de l'échéance<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
	<%-- tpl:put name="zoneMain" --%>
		<input type="hidden" name="idPlanRecouvrement" value="<%=idPlanRecouvrement%>">
		<tr>
			<td>
				<table>
					<tr>
						<td class="label">Date exigibilité</td>
						<td class="control"><ct:FWCalendarTag name="dateExigibilite" doClientValidation="CALENDAR" value="<%=viewBean.getDateExigibilite()%>"/></td>
					</tr>
					<tr>
						<td class="label">Date effective</td>
						<td class="control"><ct:FWCalendarTag name="dateEffective" doClientValidation="CALENDAR" value="<%=viewBean.getDateEffective()%>"/></td>
					</tr>
					<% if (viewBean.hasRappelSurPlan()) { %>
					<tr>
						<td class="label">Date dernier rappel</td>
						<td class="control"><ct:FWCalendarTag name="dateRappel" doClientValidation="CALENDAR" value="<%=viewBean.getDateRappel()%>"/></td>
					</tr>
					<% } %>
					<tr>
						<td class="label">Acompte</td>
						<td class="control"><input type="text" name="montant" class="montant" value="<%= globaz.globall.util.JANumberFormatter.format(viewBean.getMontant()) %>"></td>
					</tr>
					<tr>
						<td class="label">Paiement</td>
						<td class="control"><input type="text" name="montantPaye" class="montant" value="<%= globaz.globall.util.JANumberFormatter.format(viewBean.getMontantPayeNegate()) %>"></td>
					</tr>
				</table>
			</td>
		</tr>
	<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>