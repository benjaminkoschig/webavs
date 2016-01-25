<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	detailLink = "naos?userAction=naos.planAffiliation.planAffiliation.afficher&selectedId=";
	menuName   = "PlanAffiliationCotisation";
	globaz.naos.db.planAffiliation.AFPlanAffiliationListViewBean viewBean = (globaz.naos.db.planAffiliation.AFPlanAffiliationListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
	<%
		
	%>
	<TH width="30">&nbsp;</TH>
	<TH width="400">Plan</TH>
	<TH width="400">Libellé facture</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
	    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%    
		actionDetail = targetLocation + "='" + detailLink + viewBean.getPlanAffiliationId(i) +  "'";
		String lineStyle="class='mtd'";
		if(viewBean.isInactif(i).booleanValue()) {
			lineStyle += " style='color:#999999' ";
		}
	%>
	<TD <%=lineStyle%> width="30" >
	<ct:menuPopup menu="AFOptionsPlanAffiliation" labelId="MENU_OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailLink + viewBean.getPlanAffiliationId(i)%>">
		<ct:menuParam key="planAffiliationId" value="<%=viewBean.getPlanAffiliationId(i)%>"/>
	</ct:menuPopup>
	</TD>
	<TD <%=lineStyle%> onClick="<%=actionDetail%>" width="400"><%=viewBean.getLibelle(i)%></TD>
	<TD <%=lineStyle%> onClick="<%=actionDetail%>" width="400"><%=viewBean.getLibelleFacture(i)%></TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>