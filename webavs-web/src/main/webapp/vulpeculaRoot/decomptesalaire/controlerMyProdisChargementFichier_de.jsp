<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<jsp:useBean id="uploadBean" class="globaz.framework.filetransfer.FWFileUpload" scope="page"></jsp:useBean>
<head>
<%
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession) controller.getSession();
	
	uploadBean.setSavePath(globaz.jade.common.Jade.getInstance().getHomeDir() + "work/");
	uploadBean.doUpload(request);
	
	globaz.jade.fs.JadeFsFacade.copyFile("file://" + uploadBean.getSavePath() + uploadBean.getFilename(), "jdbc://" + globaz.jade.common.Jade.getInstance().getDefaultJdbcSchema() + "/" + uploadBean.getFilename());
%>
</head>
<body>
      <form id="mainForm" action="<%=request.getContextPath()+"/vulpecula"%>" >
 		<input type="hidden" name="userAction" value="<%=uploadBean.getFieldValue("userAction")%>">
		<input type="hidden" name="email" value="<%=uploadBean.getFieldValue("email")%>">
		<input type="hidden" name="filename" value="<%=uploadBean.getFilename()%>">
		<input type="hidden" name="destination" value="<%=uploadBean.getFilename()%>">
		<input type="hidden" name="wantControleCP" value="<%=uploadBean.getFieldValue("wantControleCP")%>">
		<input type="hidden" name="wantControleSalaires" value="<%=uploadBean.getFieldValue("wantControleSalaires")%>">
	</form>
	<script>mainForm.submit()</script>
</body>
</html>