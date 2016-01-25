<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.pegasus.vb.process.PCNoelPCViewBean"%>

<%-- tpl:put name="zoneInit" --%>
<%
	PCNoelPCViewBean viewBean = (PCNoelPCViewBean) session.getAttribute("viewBean");
%>

<script language="JavaScript">
	$(function () {
		
	});
</script>

<table>
	<tr id="properties">		
		<td> 
			<label for="ANNEE_ALLOCATION_NOEL">Année pour l'allocation de noël  (format YYYY):&nbsp; </label>
		</td> 
		<td>
			<input id="ANNEE_ALLOCATION_NOEL" data-g-string="mandatory:true" value=""  maxlength="4" style="width: 40px;"/>
		</td>
	</tr>
</table>
