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
	System.out.println(request.getContextPath());
} catch (Exception e) {
// Do nothing
// TODO : Do something
}
%>
</head>
<body>
<form id="mainForm" action="<%=request.getContextPath()+"/al"%>" >
<input type="hidden" name="userAction" value="<%=uploadBean.getFieldValue("userAction")%>">
<input type="hidden" name="dateDebutValidite" value="<%=uploadBean.getFieldValue("dateDebutValidite")%>">
<input type="hidden" name="dateImpression" value="<%=uploadBean.getFieldValue("dateImpression")%>">
<input type="hidden" name="email" value="<%=uploadBean.getFieldValue("email")%>">
<input type="hidden" name="fileName" value="<%=uploadBean.getSavePath()%><%=uploadBean.getFilename()%>">
<input type="hidden" name="gestionTexteLibre" value="<%=uploadBean.getFieldValue("gestionTexteLibre")%>">
<input type="hidden" name="insertionGED" value="<%=uploadBean.getFieldValue("insertionGED")%>">
<input type="hidden" name="texteLibre" value="<%=uploadBean.getFieldValue("texteLibre")%>">
</form> 
<script>mainForm.submit()</script>
</body>
</html>