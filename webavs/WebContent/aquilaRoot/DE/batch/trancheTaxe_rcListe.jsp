<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%
	COTrancheTaxeListViewBean viewBean = (COTrancheTaxeListViewBean)session.getAttribute("listViewBean");
	COTrancheTaxeViewBean tranche = null;
	size = viewBean.size();
	detailLink ="aquila?userAction=aquila.batch.trancheTaxe.afficher&selectedId=";
	BSession s = viewBean.getSession();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>

<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.aquila.db.batch.COTrancheTaxeListViewBean"%>
<%@page import="globaz.aquila.db.batch.COTrancheTaxeViewBean"%>

	<th>Obergrenzwert</th>
	<th>Obergrenzsatz</th>
	<th>Veränderlicher Betrag</th>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>
	<%
		tranche = (COTrancheTaxeViewBean) viewBean.getEntity(i);
		actionDetail = "parent.location.href='"+detailLink+tranche.getIdTrancheTaxe()+"'";
	%>
    <td class="mtd" width="30%" align="right" onClick="<%=actionDetail%>"><%=tranche.getValeurPlafond()%></td>
    <td class="mtd" width="30%" align="right" onClick="<%=actionDetail%>"><%=tranche.getTauxPlafond()%></td>
    <td class="mtd" width="*" align="right" onClick="<%=actionDetail%>"><%=tranche.getMontantVariable()%></td>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>