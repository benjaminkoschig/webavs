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
<form id="mainForm" action="<%=request.getContextPath()+"/pavo"%>" >
<input type="hidden" name="userAction" value="pavo.inscriptions.declaration.executer">
<input type="hidden" name="eMailAddress" value="<%=uploadBean.getFieldValue("eMailAddress")%>">
<input type="hidden" name="anneeCotisation" value="<%=uploadBean.getFieldValue("anneeCotisation")%>">
<input type="hidden" name="forNumeroAffilie" value="<%=uploadBean.getFieldValue("forNumeroAffilie")%>">
<input type="hidden" name="totalControle" value="<%=uploadBean.getFieldValue("totalControle")%>">
<input type="hidden" name="nombreInscriptions" value="<%=uploadBean.getFieldValue("nombreInscriptions")%>">
<input type="hidden" name="simulation" value="<%=uploadBean.getFieldValue("simulation")%>">
<input type="hidden" name="filename" value="<%=uploadBean.getFilename()%>">
<input type="hidden" name="type" value="<%=uploadBean.getFieldValue("type")%>">
<%System.out.println(uploadBean.getFieldValue("type"));%>
<input type="hidden" name="accepteEcrituresNegatives" value="<%=uploadBean.getFieldValue("accepteEcrituresNegatives")%>">
<input type="hidden" name="accepteLienDraco" value="<%=uploadBean.getFieldValue("accepteLienDraco")%>">
<input type="hidden" name="accepteAnneeEnCours" value="<%=uploadBean.getFieldValue("accepteAnneeEnCours")%>">
<input type="hidden" name="dateReceptionForced" value="<%=uploadBean.getFieldValue("dateReceptionForced")%>">
</form>
<script>mainForm.submit()</script>
</body>
</html>