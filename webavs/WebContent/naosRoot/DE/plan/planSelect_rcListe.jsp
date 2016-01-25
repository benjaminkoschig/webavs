<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.naos.db.plan.AFPlanListViewBean viewBean = (globaz.naos.db.plan.AFPlanListViewBean)request.getAttribute ("viewBean");
	size = viewBean.getSize ();
	detailLink = "naos?userAction=naos.cotisation.cotisation.chercher";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>	

	<TH width="30">&nbsp;</TH>
	<TH width="124">Name des Versicherungsplans</TH>
	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
	<%    
		actionDetail = targetLocation + "='" + detailLink + "&affiliationId=" + request.getParameter("affiliationId") + "&planId=" + viewBean.getPlanId(i)+"'";
	%>
	<TD class="mtd" onClick="<%=actionDetail%>"align="center" width="72"><IMG src="<%=request.getContextPath()%>/images/loupe.gif" ></TD>  
	<TD class="mtd" width="408"><%=viewBean.getPlanLibelle(i)%></TD>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>