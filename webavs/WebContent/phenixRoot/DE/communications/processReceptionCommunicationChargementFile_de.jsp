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
<form id="mainForm" action="<%=request.getContextPath()+"/phenix"%>" >
<input type="hidden" name="userAction" value="phenix.communications.journalRetour.executerReceptionner">
<input type="hidden" name="idJournalRetour" value="<%=uploadBean.getFieldValue("idJournalRetour")%>">
<input type="hidden" name="typeReception" value="<%=uploadBean.getFieldValue("typeReception")%>">
<input type="hidden" name="eMailAddress" data-g-string="mandatory:true" value="<%=uploadBean.getFieldValue("eMailAddress")%>">
<input type="hidden" name="libelleJournal" value="<%=uploadBean.getFieldValue("libelleJournal")%>">
<input type="hidden" name="csCanton" value="<%=uploadBean.getFieldValue("csCanton")%>">
<input type="hidden" name="fileName" value="<%=uploadBean.getFilename()%>">
</form> 
<script>mainForm.submit()</script>
</body>
</html>