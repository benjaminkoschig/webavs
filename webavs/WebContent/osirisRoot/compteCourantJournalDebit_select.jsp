<html>
<%@ page import="globaz.globall.util.*,globaz.osiris.utils.*"%>
<%
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
    String options = CAUtil.getForCompteCourantForDebitJournal(request.getParameter("like"), objSession);
%>
<body>
<form>
  <select name="selection" size="5" onChange="updateForm()" onClick="updateInput()" style="width:12cm;border : solid 1px silver;background-color = #f0f0f0;">
    <%=options%>
</select>
</form>
</body>
</html>