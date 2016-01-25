<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page import="globaz.jade.web.JadeHttpNames, globaz.jade.web.JadeWebUtil, globaz.jade.web.JadeHttpUrl"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Content-Style-Type" content="text/css"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Cache-Control" content="no-cache" /> 
<link rel="stylesheet" type="text/css" href="../theme/master.css"/>
<title>File Upload</title>
</head>
<body>
<%
	// copy the URL used to call the current page
	JadeHttpUrl url = new JadeHttpUrl(request);
	url.setParameter(JadeHttpNames.PARAM_PAGE, JadeWebUtil.getParameter(request, JadeHttpNames.PARAM_DESTINATION));
	url.setParameter(JadeHttpNames.PARAM_DESTINATION, JadeWebUtil.getParameter(request, JadeHttpNames.PARAM_DESTINATION2));
%>
<form id="mainForm" enctype="multipart/form-data" action="<%=url.getUrlString()%>" method="post">
<h2>Select file to upload:</h2>
<p><input name="inputFile" type="file"></input></p>
<p><input type="submit" name="button" value="Upload"></input></p>
</form>
</body>
</html>