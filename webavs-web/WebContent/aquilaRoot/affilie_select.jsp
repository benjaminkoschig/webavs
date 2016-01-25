<html>
<%@ page language="java" errorPage="/errorPage.jsp" contentType="text/html;charset=ISO-8859-1" %>
<%
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

    String options = globaz.aquila.parser.COAutoComplete.getAffilies(objSession, request.getParameter("like"));
%>	
<body>
<form>
  <select name="selection" size="5" style="width:12cm;border : solid 1px silver;background-color = #f0f0f0;">
    <%=options%>
</select>
</form>
</body>
</html>


