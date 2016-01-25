<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE></TITLE>
</head>
<body">
<%
	globaz.pyxis.db.alternate.TIAllAdressesViewBean viewBean = (globaz.pyxis.db.alternate.TIAllAdressesViewBean)session.getAttribute ("viewBean");	javax.xml.transform.TransformerFactory tFactory = 
	javax.xml.transform.TransformerFactory.newInstance();
	java.io.InputStream stream = globaz.jade.client.util.JadeUtil.getGlobazInputStream("adressesPaiementTiers.xslt");
	javax.xml.transform.Source stylesheet = new javax.xml.transform.stream.StreamSource(stream);
	javax.xml.transform.Transformer transformer = tFactory.newTransformer(stylesheet);
	transformer.setParameter("hidefrom","999");
	transformer.transform
	 (new javax.xml.transform.stream.StreamSource(new java.io.StringReader(viewBean.getAdressesPaiementAsXml())),
	  new javax.xml.transform.stream.StreamResult (out));
%>
</body>
</html>