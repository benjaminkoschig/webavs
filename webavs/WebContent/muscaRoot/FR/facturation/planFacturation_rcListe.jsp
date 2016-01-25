<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	globaz.musca.db.facturation.FAPlanFacturationListViewBean viewBean = (globaz.musca.db.facturation.FAPlanFacturationListViewBean)request.getAttribute ("viewBean");
	size = viewBean.size();
	detailLink = "musca?userAction=musca.facturation.planFacturation.afficher&selectedId=";
	menuName = "planFacturation-detail";
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
      <th width="20">&nbsp;</th>
      <TH  width="10%">Numéro</TH>
      <th  width="60%">Libellé</th>
      <th  width="*">Type</th>
            

	<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
      <%
	actionDetail = targetLocation + "='" + detailLink + viewBean.getIdPlanFacturation(i) + "'";
	String detailAction = detailLink + viewBean.getIdPlanFacturation(i); 
	%>
      <TD class="mtd" width="20" >
      	<ct:menuPopup menu="FA-ModuleFacturationDetail" labelId="OPTIONS" target="top.fr_main" detailLabelId="DETAIL_POPUP" detailLink="<%=detailAction%>">
      		<ct:menuParam key="selectedId" value="<%=viewBean.getIdPlanFacturation(i)%>"/>
        </ct:menuPopup>
      </TD>
      <TD class="mtd" width="10%" align="right" onClick="<%=actionDetail%>"><%=viewBean.getIdPlanFacturation(i)%></TD>
      <TD class="mtd" width="60%" onClick="<%=actionDetail%>"><%=viewBean.getLibellePlan(i)%></TD>
      <TD class="mtd" width="*"   onClick="<%=actionDetail%>"><%=viewBean.getLibelleType(i)%></TD>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>