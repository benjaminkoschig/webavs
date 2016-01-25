<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
globaz.lynx.db.organeexecution.LXOrganeExecutionListViewBean viewBean = (globaz.lynx.db.organeexecution.LXOrganeExecutionListViewBean) session.getAttribute("listViewBean");
size = viewBean.size();

globaz.lynx.db.organeexecution.LXOrganeExecutionViewBean organe = null;

detailLink ="lynx?userAction=lynx.organeexecution.organeExecution.afficher&selectedId=";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
    
<%@page import="globaz.lynx.db.organeexecution.LXOrganeExecutionListViewBean"%><TH width="30">&nbsp;</TH>
	<TH width="200">Name</TH>
	<TH width="400">Kreditkonto</TH>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
  <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
    <%
    organe = (globaz.lynx.db.organeexecution.LXOrganeExecutionViewBean) viewBean.getEntity(i);
    actionDetail = "parent.location.href='" + detailLink + organe.getIdOrganeExecution() + "'";
    %>

    <td class="mtd" width="16">
		<ct:menuPopup menu="LX-OnlyDetail" label="<%=optionsPopupLabel%>" target="top.fr_main" detailLabel="<%=menuDetailLabel%>" detailLink="<%=detailLink + organe.getIdOrganeExecution()%>"/>
	</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=organe.getNom()%>&nbsp;</td>
    <td class="mtd" onClick="<%=actionDetail%>"><%=organe.getIdExterneCompteCredit()%> - <%=organe.getLibelleCompteCredit()%>&nbsp;</td>

    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%> <%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>