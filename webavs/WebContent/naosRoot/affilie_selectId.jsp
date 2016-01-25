<%@page import="globaz.naos.util.AFUtil"%>
<html>
<%@ page language="java" errorPage="/errorPage.jsp" contentType="text/html;charset=ISO-8859-1" %>

<%
    String options = AFUtil.getAffiliesId(request.getParameter("like"),session); 
%>	
<body>
<form>
  <select name="selection" size="5" onClick="updateInput()" style="width:15cm;border : solid 1px silver;background-color = #f0f0f0;">
    <%=options%>
</select>
</form>
</body>
</html>


