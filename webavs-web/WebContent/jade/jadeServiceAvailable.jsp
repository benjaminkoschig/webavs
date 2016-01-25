<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page import="globaz.jade.web.JadeHttpNames, globaz.jade.web.JadeHttpUrl"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Content-Style-Type" content="text/css"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Cache-Control" content="no-cache" /> 
<link rel="stylesheet" type="text/css" href="../theme/master.css"/>
<title>Service available</title>
</head>
<body>
<h2>The service is currently available !!!</h2>
<%
	JadeHttpUrl url = new JadeHttpUrl(request);
	url.setParameter(JadeHttpNames.PARAM_ACTION, JadeHttpNames.ACTION_GOTO_HOME);
%>
<p><a href="<%=url.getUrlString()%>">go to the home page</a></p>
</body>
</html>