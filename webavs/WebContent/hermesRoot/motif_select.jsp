<html>
<%@ page import="globaz.globall.util.*,globaz.pavo.util.*"%>
<%
    String options = globaz.hermes.utils.HEUtil.getAvailableMotif(request.getParameter("like"),session);
%>	
<body>
<form>
  <select name="selection" size="5" onClick="updateInput()" style="width:16cm;border : solid 1px silver;background-color = #f0f0f0;">
    <%=options%>
</select>
</form>
</body>
</html>