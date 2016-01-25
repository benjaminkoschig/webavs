<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
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
<form id="mainForm" action="<%=request.getContextPath()+"/campus"%>" >
<input type="hidden" name="userAction" value="campus.process.processChargementLot.executer">
<input type="hidden" name="eMailAddress" value="<%=uploadBean.getFieldValue("eMailAddress")%>">
<input type="hidden" name="libelleLot" value="<%=uploadBean.getFieldValue("libelleLot")%>">
<input type="hidden" name="annee" value="<%=uploadBean.getFieldValue("annee")%>">
<input type="hidden" name="dateTraitement" value="<%=uploadBean.getFieldValue("dateTraitement")%>">
<input type="hidden" name="idTiersEcole" value="<%=uploadBean.getFieldValue("idTiersEcole")%>">
<input type="hidden" name="formatFichier" value="<%=uploadBean.getFieldValue("formatFichier")%>">
<input type="hidden" name="filename" value="<%=uploadBean.getFilename()%>">
</form>
<script>mainForm.submit()</script>
</body>
</html>
