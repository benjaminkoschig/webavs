<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@page import="globaz.framework.utils.urls.FWUrl"%>
<%@page pageEncoding="UTF-8"%><%response.setCharacterEncoding("UTF-8");%><%request.setCharacterEncoding("UTF-8");%> 
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<jsp:useBean id="uploadBean" class="globaz.framework.filetransfer.FWFileUpload" scope="page"></jsp:useBean>
<head>
<%
try {
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String isBatch = request.getParameter("isBatch");
	if (isBatch.equalsIgnoreCase("1")){
		uploadBean.setSavePath(globaz.jade.common.Jade.getInstance().getExternalModelDir() + "topazTemplates/amal/");
	} else {
		uploadBean.setSavePath(globaz.jade.common.Jade.getInstance().getHomeDir() + "persistence/");		
	}
	
	uploadBean.doUpload(request);
	
	//globaz.jade.fs.JadeFsFacade.copyFile("file://" + uploadBean.getSavePath() + uploadBean.getFilename(), "jdbc://" + globaz.jade.common.Jade.getInstance().getDefaultJdbcSchema() + "/" + uploadBean.getFilename());
} catch (Exception e) {
// Do nothing
System.out.println("Erreur importation fichier!!!!");
}
%>
</head>
<body>
<form id="mainForm" action="<%=request.getContextPath()+"/amal"%>" >
<input type="hidden" name="action" value="importer">
<input type="hidden" name="selectedId" value="<%=request.getParameter("selectedId")%>">
<input type="hidden" name="isBatch" value="<%=request.getParameter("isBatch")%>">
<input type="hidden" name="userAction" value="amal.formule.formule.exporter">
<input type="hidden" name="eMailAdress" value="<%=uploadBean.getFieldValue("eMailAdress")%>">
<input type="hidden" name="fileName" value="<%=uploadBean.getSavePath()%><%=uploadBean.getFilename()%>">
</form> 
<script>mainForm.submit()</script>
</body>
</html>