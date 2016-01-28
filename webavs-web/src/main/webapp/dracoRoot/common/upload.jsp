<?xml version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<jsp:useBean id="uploadBean" class="globaz.jade.http.JadeHttpUploadBean" scope="page"/>
<%
	String[] files = uploadBean.doUpload(request);
%>
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<META http-equiv="Content-Style-Type" content="text/css"/>
<META http-equiv="Pragma" content="no-cache"/>
<META http-equiv="Expires" content="0" />
<META http-equiv="Cache-Control" content="no-cache" /> 
<LINK rel="stylesheet" type="text/css" href="../theme/master.css"/>
</HEAD>
<BODY>
<form name="mainForm" enctype="multipart/form-data" method="post">
<h2>Select file to upload:</h2>
<p><input name="inputFile" type="file"></input></p>
<p><input type="submit" name="button" value="Upload"></input></p>
</form>
<%
if (files != null) {
%>
<h2>List of uploaded files:</h2>
<%
	for (int i = 0; i < files.length; i++) {
%>
<br/>
<%=files[i]%>		
<%
	}
}
%>
</BODY>
</HTML>