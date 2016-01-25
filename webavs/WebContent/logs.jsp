
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=ISO-8859-1"
%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<TITLE>Logs folder</TITLE>

<SCRIPT language="JavaScript">
function finds(styleName) {
  for (i = 0; i < document.styleSheets.length; i++) {
    for (j = 0; j < document.styleSheets(i).rules.length; j++) {
      if (document.styleSheets(i).rules(j).selectorText == styleName) {
        return document.styleSheets(i).rules(j);
      }
    }
  }
}
// stop hiding -->
</SCRIPT>

</HEAD>
<%
String logsDirectory = "/logs";
java.io.File[] files = null;

try {
	java.io.File realPath = new java.io.File(globaz.jade.common.Jade.getInstance().getHomeDir() + logsDirectory);
	files = realPath.listFiles();
} catch (Exception e) {
}

%>
<BODY>
<TABLE width="800" cellpadding="0" cellspacing="0">
	<COL width="100%">
    <TBODY>
        <TR><TD colspan="3"><b>Logs folder</b></TD></TR>
        <TR><TD colspan="3">&nbsp;</TD></TR>        
        <TR>
            <TD class="title"><B>Nom</B></TD>
            <TD class="title"><B>Taille</B></TD>
            <TD class="title"><B>Modification</B></TD>
        </TR>
                
        <%
        	if (files != null) {
        
	        	String rowStyle = "";
        	
    	    	for (int i=0; i<files.length; i++) {
        		
	    	   		if (i % 2 == 0) {
						rowStyle = "row";
					} else {
						rowStyle = "rowOdd";
					}
        %>
        
    			    <TR class="<%=rowStyle%>"
					onMouseOver="this.style.background=finds('.rowHighligthed').style.background; this.style.color=finds('.rowHighligthed').style.color;"
					onMouseOut="this.style.background=finds('.<%=rowStyle%>').style.background; this.style.color=finds('.<%=rowStyle%>').style.color;">
		
					<TD><a href="getlog.jsp?filename=<%=files[i].getName()%>&directory=<%=logsDirectory%>"><%=files[i].getName()%></a></TD>
				    <TD><%=files[i].length()%></TD>
				    <TD><i><%=files[i].lastModified()%></i></TD> 
					</TR>
        
        <%
        		}
        	}
        %>
       
    </TBODY>
</TABLE>
</BODY>
</HTML>
