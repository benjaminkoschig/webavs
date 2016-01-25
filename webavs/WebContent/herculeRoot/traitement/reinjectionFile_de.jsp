<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" import="globaz.globall.http.*" %>
<jsp:useBean id="uploadBean" class="globaz.framework.filetransfer.FWFileUpload" scope="page"></jsp:useBean>
<head>
<%
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

uploadBean.setSavePath(globaz.jade.common.Jade.getInstance().getHomeDir() + "work/");

uploadBean.doUpload(request);

globaz.jade.fs.JadeFsFacade.copyFile("file://" + uploadBean.getSavePath() + uploadBean.getFilename(), "jdbc://" + globaz.jade.common.Jade.getInstance().getDefaultJdbcSchema() + "/" + uploadBean.getFilename());
%>
</head>
<body>
<form id="mainForm" action="<%=request.getContextPath()+"/hercule"%>" >
<input type="hidden" name="userAction" value="hercule.traitement.reinjection.executer">
<input type="hidden" name="filename" value="<%=uploadBean.getFilename()%>">
<input type="hidden" name="eMailAddress" value="<%=uploadBean.getFieldValue("eMailAddress")%>">
</form>
<script>mainForm.submit()</script>
</body>
</html>