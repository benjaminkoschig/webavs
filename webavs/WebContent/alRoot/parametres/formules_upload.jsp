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
	uploadBean.setSavePath(globaz.jade.common.Jade.getInstance().getSharedDir());		
	uploadBean.doUpload(request);
} catch (Exception e) {
// Do nothing
System.out.println("Erreur importation fichier!!!!");
}
%>
</head>
<body>
<form id="mainForm" action="<%=request.getContextPath()+"/al"%>" >
<input type="hidden" name="action" value="upload">
<input type="hidden" name="selectedId" value="<%=request.getParameter("selectedId")%>">
<input type="hidden" name="userAction" value="al.parametres.formules.exporter">
<input type="hidden" name="filePathInput" value="<%=uploadBean.getSavePath()%><%=uploadBean.getFilename()%>">
</form> 
<script>mainForm.submit()</script>
</body>
</html>