<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<HEAD>
<TITLE></TITLE>
<META http-equiv="Content-Type" content="text/html;">
<META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<SCRIPT language="javascript">
var errorObj = new Object();
errorObj.text = "<%=(request.getParameter("error") != null)?request.getParameter("error"):""%>";

function showErrors() {
	if (errorObj.text != "") {
		showModalDialog('<%=request.getContextPath()%>/errorModalDlg.jsp',errorObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');	
	} else {
		location.replace('<%=request.getContextPath()%>/apg?userAction=apg.droits.droitLAPGJointDemande.chercher');
	}
}

</SCRIPT>
</HEAD>
<body bgcolor="#FFFFFF" text="#000000" onload="showErrors()">
<table width="100%" height="100%" border="0">  
  <tr valign="bottom">
    <td height="10%" align="center"><font size="2" face="Verdana, Arial, Helvetica, sans-serif">(C) 
      2006 Globaz SA - APG</font></td>
  </tr>
</table>
</body>
</HTML>