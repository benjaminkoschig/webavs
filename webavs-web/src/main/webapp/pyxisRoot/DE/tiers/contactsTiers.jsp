<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<%@ page import="globaz.framework.screens.menu.*"%>
<%@ page import="globaz.templates.*"%>
<META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<%
	globaz.pyxis.db.tiers.TITiers viewBean = (globaz.pyxis.db.tiers.TITiers)request.getAttribute ("viewBean");
%>
<title><%=viewBean.getNom()%></title>
</head>
<body>
<%
	javax.xml.transform.TransformerFactory tFactory = 
						javax.xml.transform.TransformerFactory.newInstance();
	java.io.InputStream stream = globaz.jade.client.util.JadeUtil.getGlobazInputStream("contactsTiers.xslt");
	javax.xml.transform.Source stylesheet = new javax.xml.transform.stream.StreamSource(stream);
	javax.xml.transform.Transformer transformer = tFactory.newTransformer(stylesheet);
	transformer.transform
	 (new javax.xml.transform.stream.StreamSource(new java.io.StringReader(viewBean.getContactsAsXml())),
	  new javax.xml.transform.stream.StreamResult (out));
%>
</body>
</html>
