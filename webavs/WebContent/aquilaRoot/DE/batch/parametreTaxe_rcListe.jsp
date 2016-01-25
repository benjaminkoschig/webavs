<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%
	COParametreTaxeListViewBean viewBean = (COParametreTaxeListViewBean)session.getAttribute("listViewBean");
	COParametreTaxeViewBean param = null;
	size = viewBean.size();
	detailLink ="aquila?userAction=aquila.batch.parametreTaxe.afficher&selectedId=";
	BSession s = viewBean.getSession();
%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.aquila.db.batch.COParametreTaxeListViewBean"%>
<%@page import="globaz.aquila.db.batch.COParametreTaxeViewBean"%>

	<th>Rubriknummer</th>
	<th>Rubrikbezeichnung</th>
	<th>Erforderlich</th>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>
	<%
		param = (COParametreTaxeViewBean) viewBean.getEntity(i);
		actionDetail = "parent.location.href='"+detailLink+param.getId()+"&idCalculTaxe="+param.getIdCalculTaxe()+"'";
	%>
    <td class="mtd" width="40%" onClick="<%=actionDetail%>"><%=param.getRubriqueEntity().getIdExterne()%></td>
    <td class="mtd" width="50%" onClick="<%=actionDetail%>"><%=param.getRubriqueEntity().getDescription(param.getSession().getIdLangueISO())%></td>
    <td class="mtd" width="10%" onClick="<%=actionDetail%>" align="center">
	    <% if (param.getEstRequis().booleanValue()) { %>
			<img alt="vrai" src="<%=servletContext + "/images/ok.gif"%>">
		<% } else { %>
			<img alt="faux" src="<%=servletContext + "/images/erreur.gif"%>">
		<% } %>
   	</td>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>