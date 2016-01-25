<html>
<%@ page import="globaz.globall.util.*,globaz.pavo.util.*"%>
<%     
	String options = CIUtil.getAffilies(request.getParameter("like"),true,false,"ind",session);

%>	
<body>
<form>
  <select name="selection" size="5" onchange="updateForm()" onClick="updateInput()" style="width:12cm;border : solid 1px silver;background-color = #f0f0f0;">
    <%=options%>
</select>
</form>
</body>
</html>