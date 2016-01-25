<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="globaz.framework.utils.urls.FWUrl"%>
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<jsp:useBean id="uploadBean" class="globaz.framework.filetransfer.FWFileUpload" scope="page"></jsp:useBean>
<head>
<%
try {
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	
	uploadBean.setSavePath(globaz.jade.common.Jade.getInstance().getHomeDir() + "persistence/");
	
	uploadBean.doUpload(request);
	
	globaz.jade.fs.JadeFsFacade.copyFile("file://" + uploadBean.getSavePath() + uploadBean.getFilename(), "jdbc://" + globaz.jade.common.Jade.getInstance().getDefaultJdbcSchema() + "/" + uploadBean.getFilename());
} catch (Exception e) {
// Do nothing
System.out.println("Erreur !!!!");
}
%>
</head>
<body>
<form id="mainForm" action="<%=request.getContextPath()+"/tucana"%>" >
<input type="hidden" name="userAction" value="<%=uploadBean.getFieldValue("userAction")%>">
<input type="hidden" name="eMailAdress" value="<%=uploadBean.getFieldValue("eMailAdress")%>">
<input type="hidden" name="fileName" value="<%=uploadBean.getSavePath()%><%=uploadBean.getFilename()%>">
</form> 
<script>mainForm.submit()</script>
</body>
</html>