<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="globaz.globall.db.BSession"%>
<%@page import="globaz.framework.controller.FWController"%>
<%@page import="globaz.amal.vb.process.AMRepriseAdressesViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%-- tpl:put name="zoneInit" --%>
<%
	AMRepriseAdressesViewBean viewBean = (AMRepriseAdressesViewBean) session.getAttribute("viewBean");
%>

<script language="JavaScript">
	$(function () {
	});
</script>

<table>
	<tr id="properties">		
		<td> 
			<label for="FILENAME">Sélectionnez le fichier GERES : </label>
		</td> 
		<td>
			<input id="FILENAME" type="file" data-g-upload=""/>
		</td>
	</tr>
</table>
