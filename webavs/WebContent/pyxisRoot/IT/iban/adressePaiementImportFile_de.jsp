<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<jsp:useBean id="uploadBean" class="globaz.framework.filetransfer.FWFileUpload" scope="page"></jsp:useBean>
<head>
<%
 String idEcran ="GTI6000";
%>
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
<form id="mainForm" action="<%=request.getContextPath()+"/pyxis"%>" >


<input type="hidden" name="userAction" value="pyxis.iban.adressePaiementImport.executer">
<input type="hidden" name="eMailAddress" value="<%=uploadBean.getFieldValue("eMailAddress")%>">
<input type="hidden" name="filename" value="<%=fileName%>">
<input type="hidden" name="simulation" value="<%=uploadBean.getFieldValue("simulation")%>">

</form>
<script>mainForm.submit()</script>
</body>
</html>