
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="java.nio.charset.Charset"%>
<HTML>
<HEAD>
<%@ page
language="java"
contentType="text/html; charset=ISO-8859-1"
%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<TITLE>systemStatus.jsp</TITLE>
</HEAD>
<BODY>
<TABLE class="find" cellspacing="0">
        <TR>
            <TH class="title">GLOBAZ SYSTEM STATUS</TH>
        </TR>
        <TR>
            <TD>
	            <TABLE border="0" bgcolor="#B3C4DB">
	                    <TR>
	                        <TD width="10"></TD>
	                        <TD width="1190">
	                        	<TEXTAREA rows="40" cols="144" readonly="readonly"><%=globaz.globall.db.GlobazServer.getCurrentSystem().getSystemStatus()%>
	                    		 \r\n Default Charset= <%=Charset.defaultCharset()%> \r\n;
	                       	 	</TEXTAREA>
	                       	</TD>
	                    </TR>
	            </TABLE>
            </TD>
        </TR>
</TABLE>
</BODY>
</HTML>
