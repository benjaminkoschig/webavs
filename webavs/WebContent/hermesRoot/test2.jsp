<!-- Sample JSP file -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 //EN">
<HTML>
<HEAD>
<TITLE>
Put Your Title Here
</TITLE>
</HEAD>

<BODY BGCOLOR="#FFFFFF">
<p>-<%=request.getParameter("value")%>-</p>
<form name="form1" method="get" action="<%=request.getContextPath()%>/hermesRoot/test2.jsp">
  <input type="text" name="value">
<input type="submit" name="Submit" value="Submit">
</form>

</BODY>
</HTML>
