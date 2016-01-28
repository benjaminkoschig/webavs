<%@ page language="java" import="globaz.globall.http.*" contentType="application/json;charset=ISO-8859-1" %>
<%@page import="globaz.framework.bean.FWAJAXViewBeanInterface"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.ajax.utils.RenderTransportJson"%>
<%
	FWAJAXViewBeanInterface viewBean = ((FWAJAXViewBeanInterface) request.getAttribute(FWServlet.VIEWBEAN));
	String json = RenderTransportJson.renderTransport(viewBean, true); // on sérilize le viewBean
%>

<%=json%>