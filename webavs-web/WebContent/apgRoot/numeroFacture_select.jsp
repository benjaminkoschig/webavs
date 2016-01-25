<%@page import="globaz.apg.helpers.lots.APFactureACompenserHelper"%>
<html>
<%@ page import="globaz.globall.util.*"%>
<%
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
       
    String idTiers = request.getParameter("idTiers");
    String montant = request.getParameter("montant");

	String options = APFactureACompenserHelper.getCollectionSectionsACompenser(objSession, idTiers, montant);
%>	
<body>
<form>
	<select name="selection" size="5" onClick="updateInput()" style="width:6cm;border : solid 1px silver;background-color = #f0f0f0;">
    	<%=options%>
	</select>
</form>
</body>
</html>
