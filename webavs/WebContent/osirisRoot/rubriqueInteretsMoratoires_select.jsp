<html>
<%@ page import="globaz.globall.util.*,globaz.osiris.utils.*,java.util.*"%>
<%
	List listNatureRubrique = new ArrayList();
	listNatureRubrique.add("200009");
	listNatureRubrique.add("200010");

    String options = CAUtil.getForNatureRubriqueIn(listNatureRubrique, session); 
%>	
<body>
<form>
  <select name="selection" size="5" onChange="updateForm()" onClick="updateInput()" style="width:15cm; border:solid 1px silver; background-color:#F0F0F0;">
    <%=options%>
</select>
</form>
</body>
</html>