<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/list.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.leo.db.parametrage.LESelectFormuleListViewBean"%>
<%@page import="globaz.leo.db.parametrage.LESelectFormuleViewBean"%>

<%
	LESelectFormuleListViewBean viewBean = (LESelectFormuleListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();
    session.setAttribute("listViewBean",viewBean);	
%>

<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
<%-- tpl:put name="zoneHeaders" --%>
    	

<TH>Libelle</TH>

<%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
<%-- tpl:put name="zoneCondition" --%>

	<%	
		LESelectFormuleViewBean line = (LESelectFormuleViewBean) viewBean.getEntity(i);
		actionDetail = "document.body.innerHTML='';parent.location.href='" + globaz.fweb.taglib.FWChooserTag.getSelectLink(pageContext, i) + "'";	
	%>
	
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%-- tpl:put name="zoneList" --%>

	<TD class="mtd" onClick="<%=actionDetail%>"><%="".equals(line.getCsDocument())?"&nbsp;":viewBean.getSession().getCodeLibelle(line.getCsDocument())%></TD>
	
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%-- tpl:put name="zoneTableFooter" --%><%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
<%-- /tpl:insert --%>