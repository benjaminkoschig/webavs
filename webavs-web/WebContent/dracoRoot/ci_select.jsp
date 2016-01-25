<html>
<%@ page import="globaz.globall.util.*,globaz.pavo.util.*"%>
<%
    String options = CIUtil.getAvailableCI(request.getParameter("like"),request.getParameter("isNNSS"), session);
%>	
<body>
<form>
  <select name="selection" size="5" onClick="updateInput()" style="border : solid 1px silver;background-color = #f0f0f0;">
    <%=options%>
</select>
</form>
</body>
</html>
