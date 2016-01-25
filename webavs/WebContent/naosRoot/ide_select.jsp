<%@page import="globaz.naos.util.AFIDEUtil"%>
<html>
<%@ page language="java" errorPage="/errorPage.jsp" contentType="text/html;charset=ISO-8859-1" %>

<%
    String options = AFIDEUtil.getIde(request.getParameter("like"),session);
%>	
<body>
<form>
  <select name="selection" size="11" onClick="updateInput()" style="width:12cm;border : solid 1px silver;background-color = #f0f0f0;">
    <%=options%>
</select>
</form>
</body>
</html>


