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
<title>File Upload Directory Listing</title>
</head>
<body>
<%
	String uri = JadeWebUtil.getParameter(request, JadeHttpNames.PARAM_URI);
	java.util.List files = (java.util.List)JadeWebUtil.getViewBean(request);
%>
<h2>Contents of <%=uri%>:</h2>
<ul>
<%
	if (files != null) {
		JadeHttpUrl url = new JadeHttpUrl(request);
		url.setParameter(JadeHttpNames.PARAM_ACTION, JadeHttpNames.ACTION_DOWNLOAD);
		for (java.util.Iterator iter = files.iterator(); iter.hasNext(); ) {
			globaz.jade.fs.message.JadeFsFileInfo fileInfo = (globaz.jade.fs.message.JadeFsFileInfo)iter.next();
			url.setParameter(JadeHttpNames.PARAM_URI, fileInfo.getUri());
%>
			<li><a href="<%=url.getUrlString()%>" target="_blank"><%=globaz.jade.client.util.JadeFilenameUtil.extractFilename(fileInfo.getUri())%></a> (<%=(fileInfo.getIsFolder() ? "folder" : fileInfo.getSize() + " bytes")%>) last modified on <%=globaz.jade.client.util.JadeDateUtil.getGlobazFormattedDateTime(new java.util.Date(fileInfo.getDate()))%></li>
<%
		}
	}
%>
</ul>
</body>
</html>