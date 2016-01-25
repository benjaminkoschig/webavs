<html>
<%@ page import="globaz.globall.util.*,globaz.draco.util.*"%>
<%
	String options = DSUtil.getInformationsDeclaration(request.getParameter("like"),request.getParameter("annee"),session); 
	
%>	
<body>
<form>
  <select name="selection" size="5" onChange="updateForm()" onClick="updateInput()" style="width:12cm;border : solid 1px silver;background-color = #f0f0f0;">
    <%=options%>
</select>
</form>
</body>
</html>