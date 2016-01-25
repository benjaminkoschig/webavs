<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Page Designer V3.5.3 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE></TITLE>
</head>
<BODY bgColor=#B3C4DB>
<table cellpadding="10" width="100%">
<tr>
<td><b>Attention</b>, cette adresse est utilisée par d'autres applications</td>
</tr>
<tr>
<td>Souhaitez-vous quand même effectuer la modification ?</td>
</tr>
<tr>
  <td  align ="right">
    <input type='button' onclick="returnValue=true;window.close();" value=" Oui ">
    <input type='button' onclick="returnValue=false;window.close();" value=" non ">
  </td>
</tr>
</table>
</BODY>
</HTML>
