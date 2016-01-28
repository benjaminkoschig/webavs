<html>
<%@ page language="java" errorPage="/errorPage.jsp" contentType="text/html;charset=ISO-8859-1" %>

<%
    String options = globaz.naos.util.AFUtil.getReviseur(request.getParameter("like"),session);
%>	
<body>
<form>
  <select name="selection" size="5" onClick="updateInput()" style="width:12cm;border : solid 1px silver;background-color = #f0f0f0;">
    <%=options%>
</select>
</form>
</body>
</html>


