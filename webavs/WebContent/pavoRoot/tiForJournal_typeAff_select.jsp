<html>
<%@ page import="globaz.globall.util.*,globaz.pavo.util.*"%>
<%
	//permet d'afficher le type d'affiliation entre parenthèse par rapport à tiForJournal_select.jsp
    String options = CIUtil.getAffiliesForJournal(request.getParameter("like"),true, session); 
%>	
<body>
<form>
  <select name="selection" size="5" onChange="updateForm()" onClick="updateInput()" style="width:12cm;border : solid 1px silver;background-color = #f0f0f0;">
    <%=options%>
</select>
</form>
</body>
</html>