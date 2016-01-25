<html>
<HEAD>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<script language="javaScript">
	function getMasseParJournal(anneeCoti, idAffiliation){
		parent.dernieresEcritures.location.href="<%=request.getContextPath()%>/pavoRoot/AffilieByJournalDE.jsp?idAffiliation="+idAffiliation+"&anneeCoti="+anneeCoti;
																						
	}
</script>
</HEAD>

<%@ page import="globaz.globall.util.*,globaz.pavo.util.*,java.util.*"%>
<%
	String idAffilitation = request.getParameter("idAffiliation");
	String anneeDebut = request.getParameter("anneeDebut");
	String anneeFin = request.getParameter("anneeFin");
	ArrayList list = new ArrayList();
	if(idAffilitation!=null) {
		 list = CIUtil.getMasseSalarialePaAnnee(session,idAffilitation,anneeDebut,anneeFin);
	}
	
	
%>


	
<BODY style="margin-left:0px; margin-right:0px;"  bgcolor="#F0F0F0">
<TABLE width="100%" border="0" cellspacing="0">
<th>Jahr</th>
<th>IK</th>
<th>Hilfsbuchaltung</th>
<TH>Saldo</TH>
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
	
	<TR class="<%=rowStyle%>"
	onclick="getMasseParJournal(<%=line.get(0)%>,'<%=idAffilitation%>');parent.showWaitingPopup();"
	>
		<%	for(int j=0;j<line.size();j++) { %>
			<TD class="mtd" align="right" ><%=line.get(j)%></TD>
		<% } %>
	</TR>
	<% } %>

</TABLE>
</body>
</html>
