<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<jsp:useBean id="uploadBean" class="globaz.framework.filetransfer.FWFileUpload" scope="page"></jsp:useBean>
<head>
<%
	String fileName = new String();

	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	
	uploadBean.setSavePath(globaz.jade.common.Jade.getInstance().getHomeDir() + "work/");
	
	uploadBean.doUpload(request);

	if ((uploadBean.getFilename() == null) && (!globaz.jade.client.util.JadeStringUtil.isBlank(uploadBean.getFieldValue("fileName")))) {
		java.io.File tmp = new java.io.File(uploadBean.getFieldValue("fileName"));
	
		fileName = tmp.getName();	
	} else {
		fileName = uploadBean.getFilename();
	}
	
	globaz.jade.fs.JadeFsFacade.copyFile("file://" + uploadBean.getSavePath() + fileName, "jdbc://" + globaz.jade.common.Jade.getInstance().getDefaultJdbcSchema() + "/" + fileName);
%>
</head>
<body>
<form id="mainForm" action="<%=request.getContextPath()+"/osiris"%>" >
<input type="hidden" name="userAction" value="<%=uploadBean.getFieldValue("userAction")%>">
<input type="hidden" name="eMailAddress" value="<%=uploadBean.getFieldValue("eMailAddress")%>">
<input type="hidden" name="dateValeur" value="<%=uploadBean.getFieldValue("dateValeur")%>">
<input type="hidden" name="libelle" value="<%=uploadBean.getFieldValue("libelle")%>">
<input type="hidden" name="fileName" value="<%=fileName%>">
<input type="hidden" name="simulation" value="<%=uploadBean.getFieldValue("simulation")%>">
<input type="hidden" name="idOrganeExecution" value="<%=uploadBean.getFieldValue("idOrganeExecution")%>">
</form> 
<script>mainForm.submit()</script>
</body>
</html>