<!-- Sample JSP file -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<HEAD>
<jsp:useBean id="objSession" class="globaz.globall.db.BSession" scope="session"></jsp:useBean>
<%
	String IFrameHeight = "500";
	int subTableHeight = 50;
	String sApp = new String();
	String applicationRoot ="muscaRoot";
	if (!request.getServerName().equals("localhost"))
		sApp = "/";

	String cd = new String("/muscaRoot/work/");
	if (request.getParameter("dir") != null)
		cd = request.getParameter("dir");
	if (!cd.endsWith("/"))
		cd = cd + "/";
	File realPath = realPath = new File(application.getRealPath(cd));
%>
<META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<TITLE>
</TITLE>
</HEAD>

<BODY BGCOLOR="#FFFFFF">


<TABLE class="find" cellspacing="0">
  <TBODY>
    <TR>
      <TH class="title" colspan="2">
				Verzeichnis der ausgedruckten Dokumente
			</TH>
    </TR>
    <TR>
		<TD bgcolor="gray" colspan="2" height="0"></TD>
	</TR>
	<TR>
		<TD width="5">&nbsp;</TD>
		<TD>
			<TABLE border="0" height="<%=subTableHeight%>" cellspacing="0" cellpadding="0">
				<TBODY>
					<TR>
						<TD height="20">&nbsp;</TD>
					</TR>
   					<TR>
					    <TD><img valign="bottom" src="<%=request.getContextPath()%>/images/fold.gif" align="bottom"> Inhalt des Verzeichnis <B><%= cd %></B></TD>
				    </TR>
			  </TBODY>
			</TABLE>
	</TR>
  </TBODY>
</TABLE>
<P>
<IFRAME scrolling="YES"  src="<%=applicationRoot%>/DE/browseDirectory_list.jsp" style="border : solid 1px black; width:100%;" height="<%=IFrameHeight%>">
</IFRAME>
</BODY>
</HTML>
