<html>
<%@ page import="globaz.globall.util.*,globaz.pavo.util.*"%>
<%
    String options = globaz.hermes.utils.HEUtil.getAvailableARC(request.getParameter("like"),request.getParameter("isArchivage"),session);
%>	
<body>
<form>
  <select name="selection" size="3" onClick="updateInput()" style="width:8cm;border : solid 1px silver;background-color = #f0f0f0;">
    <%=options%>
</select>
</form>
</body>
</html>