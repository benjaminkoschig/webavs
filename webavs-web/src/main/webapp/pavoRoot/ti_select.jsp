<html>
<%@ page import="globaz.globall.util.*,globaz.pavo.util.*"%>
<%
	//retourne 1 seule occurence du numéro d'affilié (n'affiche pas le type à coté du numéro)
    String options = CIUtil.getAffilies(request.getParameter("like"), session); 
%>	
<body>
<form>
  <select name="selection" size="5" onChang="updateForm()" onClick="updateInput()" style="width:12cm;border : solid 1px silver;background-color = #f0f0f0;">
    <%=options%>
</select>
</form>
</body>
</html>