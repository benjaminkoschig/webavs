<html>
<HEAD>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<script language="javaScript">
	
</script>
</HEAD>

<%@ page import="globaz.globall.util.*,globaz.pavo.util.*,java.util.*"%>
<%
	String idAffilitation = request.getParameter("idAffiliation");
	String annee = request.getParameter("anneeCoti");
	ArrayList list = new ArrayList();
	list=globaz.pavo.util.CIUtil.getAffilieByJournal(session,annee,idAffilitation);
%>

<BODY style="margin-left:0px; margin-right:0px;"  bgcolor="#F0F0F0">
<TABLE width="100%" border="0" cellspacing="0">
<th>Journal</th>
<th>Summe</th>
<%	String rowStyle = "";
		for(int i=0;i<list.size();i++) {
			ArrayList line = (ArrayList)list.get(i);
			boolean condition = (i % 2 == 0);
	%>
    <!-- #BeginEditable "zoneCondition" -->
    <!-- #EndEditable -->
	<%
			if (condition) {
				rowStyle = "row";
			} else {
				rowStyle = "rowOdd";
			}
	%>
	
	<TR class="<%=rowStyle%>">
		<%	for(int j=0;j<line.size();j++) { %>
			<TD class="mtd" align="right" ><%=line.get(j)%></TD>
		<% } %>
	</TR>
	<% } %>

</TABLE>
</body>
</html>




