<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.framework.controller.FWController"%>
<%@page import="globaz.amal.vb.process.AMRepriseRecalculsViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%-- tpl:put name="zoneInit" --%>
<%
AMRepriseRecalculsViewBean viewBean = (AMRepriseRecalculsViewBean) session.getAttribute("viewBean");
%>

<script language="JavaScript">
	$(function () {
	});
</script>

<table>
<tr id="properties">		
		
	</tr>
</table>
