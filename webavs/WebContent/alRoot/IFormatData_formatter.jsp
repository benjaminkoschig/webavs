<%@page import="globaz.jade.client.util.JadeStringUtil"%><%@page import="globaz.globall.format.IFormatData"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/plain;charset=UTF-8" %>
	<%
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String formatterParam = request.getParameter("formatter");
	String valueParam = request.getParameter("value");
	String resultFormatted ="";
	int status=0;
	if(!JadeStringUtil.isEmpty(formatterParam)){
		Class formatter = Class.forName(formatterParam);
		IFormatData interface_formatter = (IFormatData) formatter.newInstance();
		resultFormatted = interface_formatter.format(valueParam);
	}
	else{
		resultFormatted = "UNDEFINED FORMATTER";
		status=-1;
	}

	%>
	
{"result":"<%=resultFormatted%>",
 "status":"<%=status%>"}