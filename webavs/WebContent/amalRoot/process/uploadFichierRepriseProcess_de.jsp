7<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.FileUploadException"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.io.output.*"%>
<%@page import="globaz.framework.utils.urls.FWUrl"%>
<HTML>
<%@ page language="java"  import="globaz.globall.http.*" %>
<jsp:useBean id="uploadBean" class="globaz.framework.filetransfer.FWFileUpload" scope="page"></jsp:useBean>
<head>
<%
try {
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	uploadBean.setSavePath(globaz.jade.common.Jade.getInstance().getHomeDir() + "persistence/");	
	uploadBean.doUpload(request);
} catch (Exception e) {
	// Do nothing
	System.out.println("Erreur !!!!");
}
%>
</head>
<body>
<form id="formExport" action="/webavs/amal">
	<input type="hidden" name="fileName" value="<%=uploadBean.getFilename()%>" />
	<input type="hidden" name="filePath" value="<%=uploadBean.getSavePath()%>" />
	<input type="hidden" name="userAction" value="amal.process.uploadFichierReprise.exporter" />
</form>
<SCRIPT type="text/javascript">
	document.getElementById("formExport").submit();
	alert('ddd');
</script>
</body>
</html>