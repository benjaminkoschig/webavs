<html>
<%@ page import="globaz.globall.util.*,globaz.pavo.util.*"%>
<%
	//recherche des affili�s de type paritaires, uniquement 1 occurence par type d'affiliation (affiche le type d'affiliation � cot� du num�ro)
	String options = CIUtil.getAffilies(request.getParameter("like"),false,true,"",session);
%>	
<body>
<form>
  <select name="selection" size="5" onChang="updateForm()" onClick="updateInput()" style="width:12cm;border : solid 1px silver;background-color = #f0f0f0;">
    <%=options%>
</select>
</form>
</body>
</html>