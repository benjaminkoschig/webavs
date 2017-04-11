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

if(!uploadBean.getFilename().isEmpty()){
	globaz.jade.fs.JadeFsFacade.copyFile("file://" + uploadBean.getSavePath() + uploadBean.getFilename(), "jdbc://" + globaz.jade.common.Jade.getInstance().getDefaultJdbcSchema() + "/" + uploadBean.getFilename());
}

%>
</head>
<body>
<form id="mainForm" action="<%=request.getContextPath()+"/naos"%>" >
<input type="hidden" name="userAction" value="naos.controleLpp.controleLppAnnuel.executer">
<input type="hidden" name="filename" value="<%=uploadBean.getFilename()%>">
<input type="hidden" name="email" value="<%=uploadBean.getFieldValue("email")%>">
<input type="hidden" name="anneeDebut" value="<%=uploadBean.getFieldValue("anneeDebut")%>">
<input type="hidden" name="anneeFin" value="<%=uploadBean.getFieldValue("anneeFin")%>">
<input type="hidden" name="modeControle" value="<%=uploadBean.getFieldValue("modeControle")%>">
<input type="hidden" name="tri" value="<%=uploadBean.getFieldValue("tri")%>">
<input type="hidden" name="dateImpression" value="<%=uploadBean.getFieldValue("dateImpression")%>">
<input type="hidden" name="typeAdresse" value="<%=uploadBean.getFieldValue("typeAdresse")%>">
</form>
<script>mainForm.submit()</script>
</body>
</html>