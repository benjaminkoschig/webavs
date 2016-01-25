<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=ISO-8859-1"
%>
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<TITLE>test.jsp</TITLE>
</HEAD>
<%
	globaz.framework.bean.FWViewBean viewBean = (globaz.framework.bean.FWViewBean) request.getAttribute("viewBean");
	String text = "ERROR Unable to perform action";
	if (viewBean != null) {
		text = viewBean.getMessage();
	}
%>

<BODY>
	<TABLE bgcolor="#B3C4DB" cellspacing="0" cellpadding="0" width="100%">
		<TBODY>
			<TR class="title">
				<TH colspan="2">Action result page</TH>
			</TR>
			<TR>
				<TD colspan="2">&nbsp;</TD>
			</TR>
			<TR>
				<TD valign="top">Result :</TD>
				<TD><TEXTAREA rows="10" cols="80" name="Result" readonly><%=text%></TEXTAREA></TD>
			</TR>
			<TR>
				<TD colspan="2">&nbsp;</TD>
			</TR>
		</TBODY>
	</TABLE>
</BODY>
</HTML>
