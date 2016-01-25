
<%@page import="globaz.pyxis.services.lettrestype.TILettreTypeVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="globaz.pyxis.services.lettrestype.TIListLettresTypeViewBean"%>
<html>
<head>
</head>
<body>
	<h1>Liste des lettres type</h1>
	
	<%
	TIListLettresTypeViewBean viewBean = (TIListLettresTypeViewBean)session.getAttribute("viewBean");
	ArrayList docs = viewBean.getDocs();
	if (docs != null) {
		pageContext.getOut().println("<ul>");
		for (java.util.Iterator it = docs.iterator();it.hasNext(); ) {
			TILettreTypeVO vo = (TILettreTypeVO) it.next();
			pageContext.getOut().println("<li><a href='"
					+request.getContextPath()
					+"/pyxis?userAction=pyxis.ext.lettresType.startdoc&iddoc="
					+vo.getIdDoc()
					+"&idTiers="
					+viewBean.getIdTiers()
					+"&file="
					+vo.getFile()
					+"'>"+vo.getLibelle()+"</a>");
		}
		pageContext.getOut().println("</ul>");
	}
	%>
	
</body>
</html>