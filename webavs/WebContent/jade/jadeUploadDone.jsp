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
	java.util.List files = (java.util.List)JadeWebUtil.getViewBean(request);
%>
<h2>Files uploaded successfully:</h2>
<ul>
<%
	if (files != null) {
		JadeHttpUrl url = new JadeHttpUrl(request);
		url.setParameter(JadeHttpNames.PARAM_ACTION, JadeHttpNames.ACTION_DOWNLOAD);
		for (java.util.Iterator iter = files.iterator(); iter.hasNext(); ) {
			globaz.jade.fs.message.JadeFsFileInfo fileInfo = (globaz.jade.fs.message.JadeFsFileInfo)iter.next();
			url.setParameter(JadeHttpNames.PARAM_URI, fileInfo.getUri());
%>
			<li><a href="<%=url.getUrlString()%>"><%=globaz.jade.client.util.JadeFilenameUtil.extractFilename(fileInfo.getUri())%></a> (<%=(fileInfo.getIsFolder() ? "folder" : fileInfo.getSize() + " bytes")%>) last modified on <%=globaz.jade.client.util.JadeDateUtil.getGlobazFormattedDateTime(new java.util.Date(fileInfo.getDate()))%></li>
<%
		}
	}
%>
</ul>
<%
	// copy the URL used to call the current page
	JadeHttpUrl url = new JadeHttpUrl(request);
	url.setParameter(JadeHttpNames.PARAM_ACTION, JadeHttpNames.ACTION_UPLOAD_LIST);
	url.setParameter(JadeHttpNames.PARAM_URI, JadeWebUtil.getParameter(request, JadeHttpNames.PARAM_URI));
	url.setParameter(JadeHttpNames.PARAM_PAGE, JadeWebUtil.getParameter(request, JadeHttpNames.PARAM_DESTINATION));
%>
<p>Access to the directory containing uploaded files on the backend: <a href="<%=url.getUrlString()%>"><%=url.getParameter(JadeHttpNames.PARAM_URI)%></a></p>
</body>
</html>