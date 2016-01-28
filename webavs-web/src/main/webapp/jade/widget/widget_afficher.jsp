<%@ page language="java" import="globaz.globall.http.*" contentType="application/json;charset=ISO-8859-1" %>
<%@page import="globaz.framework.ajax.utils.RenderTransportJson"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.framework.servlets.widget.FWJadeWidgetReadViewBean"%>
<%
	FWJadeWidgetReadViewBean viewBean = ((FWJadeWidgetReadViewBean) request.getAttribute("widgetViewBean"));
	String json = RenderTransportJson.renderTransport(viewBean); // on ne sérilize pas le viewBean
%>
<%=json%>