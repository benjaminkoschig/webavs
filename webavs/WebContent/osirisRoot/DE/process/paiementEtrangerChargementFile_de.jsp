<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<jsp:useBean id="uploadBean" class="globaz.framework.filetransfer.FWFileUpload" scope="page"></jsp:useBean>
<head>
<%
try {
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	
	uploadBean.setSavePath(globaz.jade.common.Jade.getInstance().getHomeDir() + "work/");
	
	uploadBean.doUpload(request);
	
	globaz.jade.fs.JadeFsFacade.copyFile("file://" + uploadBean.getSavePath() + uploadBean.getFilename(), "jdbc://" + globaz.jade.common.Jade.getInstance().getDefaultJdbcSchema() + "/" + uploadBean.getFilename());
} catch (Exception e) {
// Do nothing
}
%>
</head>
<body>
<form id="mainForm" action="<%=request.getContextPath()+"/osiris"%>" >
<input type="hidden" name="userAction" value="osiris.process.paiementEtranger.executer">
<input type="hidden" name="eMailAddress" value="<%=uploadBean.getFieldValue("eMailAddress")%>">
<input type="hidden" name="dateValeur" value="<%=uploadBean.getFieldValue("dateValeur")%>">
<input type="hidden" name="libelle" value="<%=uploadBean.getFieldValue("libelle")%>">
<input type="hidden" name="fileName" value="<%=uploadBean.getFilename()%>">
<input type="hidden" name="simulation" value="<%=uploadBean.getFieldValue("simulation")%>">
<input type="hidden" name="taux" value="<%=uploadBean.getFieldValue("taux")%>">
<input type="hidden" name="csTypeFichier" value="<%=uploadBean.getFieldValue("csTypeFichier")%>">
<input type="hidden" name="idOrganeExecution" value="<%=uploadBean.getFieldValue("idOrganeExecution")%>">
</form> 
<script>mainForm.submit()</script>
</body>
</html>