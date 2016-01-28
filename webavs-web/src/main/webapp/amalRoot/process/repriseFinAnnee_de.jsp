<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.framework.controller.FWController"%>
<%@page import="globaz.amal.vb.process.AMRepriseFinAnneeViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%-- tpl:put name="zoneInit" --%>
<%
	AMRepriseFinAnneeViewBean viewBean = (AMRepriseFinAnneeViewBean) session.getAttribute("viewBean");
%>

<script language="JavaScript">
	$(function () {
		if (typeof ID_EXECUTE_PROCEESS !== 'undefined') {
			$('.btnAjaxUpdate').hide();
		}
	});
</script>

<table>
	<tr id="properties">		
		<td> 
			<label for="ANNEE">Année de subside : </label>
		</td> 
		<td>
			<input id="ANNEE" data-g-integer="mandatory:true" type="text" />
		</td>
	</tr>
</table>
