<html>
<%@ page import="globaz.globall.util.*"%>
<%
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

    String p1 = request.getParameter("provenance1");
    String p2 = request.getParameter("provenance2");
    java.util.LinkedList list = new java.util.LinkedList();
    if (p1!=null) {
    	list.add(p1);
    }
    if (p2!=null) {
    	list.add(p2);
    }

  String options = globaz.prestation.interfaces.util.nss.PRUtil.getNumerosSecuriteSocialeOptionList(objSession, request.getParameter("like"), request.getParameter("isNNSS"), list);

%>
<body>
<form>
	<select name="selection" size="5" onClick="updateInput()" style="width:12cm;border : solid 1px silver;background-color = #f0f0f0;">
    	<%=options%>
	</select>
</form>
</body>
</html>
