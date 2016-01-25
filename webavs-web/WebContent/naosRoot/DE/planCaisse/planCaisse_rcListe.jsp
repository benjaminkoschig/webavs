<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink = "naos?userAction=naos.planCaisse.planCaisse.afficher&selectedId=";
	menuName   = "PlanCaisse";
	globaz.naos.db.planCaisse.AFPlanCaisseListViewBean viewBean = (globaz.naos.db.planCaisse.AFPlanCaisseListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize (); 
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<%	
		if (!(request.getParameter("colonneSelection") != null && request.getParameter("colonneSelection").equals("yes"))) {
	%>
    <TH width="30">&nbsp;</TH>
	<% } %>
	<TH width="400">Versicherungsplan</TH>
	<TH width="120">Kassen-Nr.</TH>
	<TH width="400">Kasse</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
	    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%    
		actionDetail = targetLocation + "='" + detailLink + viewBean.getPlanCaisseId(i) + "'";
		// Pas de menu et pas de lien sur le Detail si on est en mode selection.
		if (request.getParameter("colonneSelection") != null && request.getParameter("colonneSelection").equals("yes")) {
			actionDetail = "";
		} else {
    %>
	<TD class="mtd" width="30" >
	<ct:menuPopup menu="AFOptionsPlanCaisse" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + viewBean.getPlanCaisseId(i)%>">
		<ct:menuParam key="planCaisseId" value="<%=viewBean.getPlanCaisseId(i)%>"/>
	</ct:menuPopup>
	</TD>
	<% } %>
	<TD class="mtd" onClick="<%=actionDetail%>" width="400"><%=viewBean.getLibelle(i)%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="120"><%=viewBean.getAdministration(i).getCodeAdministration()%></TD>
	<TD class="mtd" onClick="<%=actionDetail%>" width="400"><%=viewBean.getAdministration(i).getNom()%></TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>