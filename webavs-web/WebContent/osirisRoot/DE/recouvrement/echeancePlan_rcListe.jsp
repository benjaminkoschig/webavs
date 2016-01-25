<%-- tpl:insert page="/theme/list.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@ page import="globaz.osiris.db.recouvrement.*"%>
<%
	CAEcheancePlanListViewBean viewBean = (CAEcheancePlanListViewBean) request.getAttribute("viewBean");
	size = viewBean.size();
	detailLink = "osiris?userAction=osiris.recouvrement.echeancePlan.afficher&selectedId=";
	globaz.osiris.db.access.recouvrement.CAPlanRecouvrement plan = new globaz.osiris.db.access.recouvrement.CAPlanRecouvrement();
%>


<script type="text/javascript">
	var b = true; // Permet d'éviter que l'on supprime un élément dejà supprimé (par double clique ou parce que l'application est lente)
	function del(idEcheancePlan, idPlanRecouvrement) {
		if (window.confirm("Sie sind dabei, das ausgewählte Objekt zu löschen! Wollen Sie fortfahren?")) {
			if(b) {
				b=false;
				top.fr_main.location.href="osiris?userAction=osiris.recouvrement.echeancePlan.supprimer&selectedId=" + idEcheancePlan + "&idPlanRecouvrement=" + idPlanRecouvrement;
			}
		}
	}

	function delNext(idEcheancePlan, idPlanRecouvrement) {
		if (window.confirm("Sie sind dabei, das ausgewählte Objekt und die nächste zu löschen! Wollen Sie alles löschen?")) {
			if(b) {
				b=false;
				top.fr_main.location.href="osiris?userAction=osiris.recouvrement.sursis.supprimerSuivants&selectedId=" + idEcheancePlan + "&idPlanRecouvrement=" + idPlanRecouvrement;
			}
		}
	}

	function editNext(idEcheancePlan, idPlanRecouvrement) {
		if (window.confirm("Sie sind dabei die Fälligkeiten zu wiederberechnen! Wollen Sie fortfahren?")) {
			if(b) {
				b=false;
				top.fr_main.location.href="osiris?userAction=osiris.recouvrement.sursis.calculSuivants&selectedId=" + idEcheancePlan + "&idPlanRecouvrement=" + idPlanRecouvrement;
			}
		}
	}
</script>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<%@page import="globaz.framework.util.FWCurrency"%>
	<th>&nbsp;</th>
	<th>Verfallsdatum</th>
	<th>Zahlungseingang</th>
	<% if (viewBean.hasRappelSurPlan()) { %>
	<th>Datum der letzte Mahnung</th>
	<%} %>
	<th>Anzahlung</th>
	<th>Zahlung</th>
	<th>Saldo</th>
	<th>&nbsp;</th>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%
		CAEcheancePlanViewBean line = (CAEcheancePlanViewBean) viewBean.getEntity(i);
		actionDetail = targetLocation  + "='" + detailLink + line.getIdEcheancePlan() + "'";
		plan = line.getPlanRecouvrement();
	%>
    <td class="mtd" width="15px">
	<ct:menuPopup menu="CA-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=(detailLink + line.getIdEcheancePlan())%>"/>
	</td>
	<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDateExigibilite()%>&nbsp;</td>
	<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDateEffective()%>&nbsp;</td>
	<% if (viewBean.hasRappelSurPlan()) { %>
	<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"><%=line.getDateRappel()%>&nbsp;</td>
	<%} %>
	<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" style="text-align: right;"><%=globaz.globall.util.JANumberFormatter.format(line.getMontant())%>&nbsp;CHF</td>
	<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" style="text-align: right;"><%=globaz.globall.util.JANumberFormatter.format(line.getMontantPaye())%>&nbsp;CHF</td>
	<% FWCurrency solde = new FWCurrency(line.getMontant()); solde.add(line.getMontantPaye());%>
	<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>" style="text-align: right;"><%=solde.toStringFormat()%>&nbsp;CHF</td>
	<td class="mtd" nowrap="nowrap">
		<ct:ifhasright element="osiris.recouvrement" crud="cud">
		<% boolean b = (line.isOperationAssociee() || (!globaz.jade.client.util.JadeStringUtil.isEmpty(line.getDateEffective()) && !"0".equals(line.getDateEffective()))); %>
		<% if(!b){%><a href="javascript:del(<%=line.getIdEcheancePlan()%>, <%=line.getIdPlanRecouvrement()%>);"><%} %><img src="<%=servletContext%>/images/btnX.gif" alt="Element löschen" title="Element löschen" border=0 <% if(b){%> style="opacity:0.4;filter:alpha(opacity=40)"<%} %> /><% if(!b){%></a><%} %>
		<% if(!b){%><a href="javascript:delNext(<%=line.getIdEcheancePlan()%>, <%=line.getIdPlanRecouvrement()%>);"><%} %><img src="<%=servletContext%>/images/btnTdelete.gif" alt="Das Element und die nächsten löschen" title="Das Element und die nächsten löschen" height="15" border=0 <% if(b){%> style="opacity:0.4;filter:alpha(opacity=40)"<%} %> /><% if(!b){%></a><%} %>
		<% if(!b){%><a href="javascript:editNext(<%=line.getIdEcheancePlan()%>, <%=line.getIdPlanRecouvrement()%>);"><%} %><img src="<%=servletContext%>/images/btnTcrayon.gif" alt="Folgende Beträge wiederberechnen" title="Folgende Beträge wiederberechnen" height="15" border=0 <% if(b){%> style="opacity:0.4;filter:alpha(opacity=40)"<%} %> /><% if(!b){%></a><%} %>
		</ct:ifhasright>
	</td>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%if(viewBean.getSize()>0){%>
	<tr>
		<td colspan=<%=viewBean.hasRappelSurPlan()?8:7 %>><hr></td>
	</tr>
	<tr>
		<td colspan=<%=viewBean.hasRappelSurPlan()?4:3 %> class="mtd" nowrap="nowrap">
			<b>Total : &nbsp;<%=viewBean.getCount()%></b>
		</td>
		<td class="mtd" nowrap="nowrap" style="text-align: right;">
			<b><%=globaz.globall.util.JANumberFormatter.format(viewBean.getSum("MONTANT"))+" CHF"%></b>
		</td>
		<td class="mtd" nowrap="nowrap" style="text-align: right;">
			<b><%=globaz.globall.util.JANumberFormatter.format(viewBean.getSum("MONTANTPAYE"))%>&nbsp;CHF</b>
		</td>
		<td class="mtd" nowrap="nowrap" style="text-align: right;">
			<b><%=globaz.globall.util.JANumberFormatter.format(viewBean.getSum("MONTANT").add(viewBean.getSum("MONTANTPAYE")))%>&nbsp;CHF</b>
		</td>
		<td class="mtd" nowrap="nowrap"></td>
	</tr>

	<%if (!plan.isActif()) {
		if (!globaz.jade.client.util.JadeStringUtil.isDecimalEmpty(plan.getPlafond())
			&& !plan.getPlafond().trim().equals(viewBean.getSum("MONTANT").toString())
			|| !plan.getCumulSoldeSections().trim().equals(viewBean.getSum("MONTANT").toString())){%>
				<tr>
					<td align=center colspan=<%=viewBean.hasRappelSurPlan()?8:7 %>>
						<font color=red><b>Die Summe der Fälligkeiten entspricht nicht dem Saldo des Plans !</b></font>
					</td>
				</tr>
	<%	}
	  }%>
	<%if(CAPlanRecouvrementViewBean.CS_INACTIF.equals(plan.getIdEtat())) {%>
		<tr>
			<td align=center colspan=<%=viewBean.hasRappelSurPlan()?8:7 %>>
				<%if(objSession.hasRight("osiris.recouvrement.planRecouvrement", FWSecureConstants.UPDATE)) { %>
					<input type=button onclick="javascript:top.fr_main.location.href='osiris?userAction=osiris.recouvrement.planRecouvrement.modifier&idPlanRecouvrement=<%=plan.getIdPlanRecouvrement()%>&idEtat=<%=plan.CS_ACTIF%>&partPenale=<%=plan.getPartPenaleJsp()%>';" value="Zahlungsvereinbarung aktivieren">
				<%}%>
			</td>
		</tr>
	<%}%>

	<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>