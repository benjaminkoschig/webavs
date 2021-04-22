<!doctype html>

<%@page import="globaz.jade.log.business.renderer.JadeTranslatedMessage"%>
<%@page import="com.google.gson.Gson"%>
<%@ page language="java" isErrorPage="true"%>

<%@ page import="globaz.framework.bean.*"%>
<%@ page import="globaz.framework.servlets.FWServlet"%>
<%@ page import="globaz.framework.servlets.widget.FWJadeWidget"%>
<%@ page import="globaz.framework.utils.params.FWParamString"%>
<%@ page import="globaz.framework.utils.urls.FWUrl"%>
<%@ page import="globaz.framework.utils.urls.FWUrlsStack"%>
<%@ page import="java.io.PrintStream"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="ch.globaz.common.businessimpl.services.ParametreServiceImpl" %>
<%
	String servletContext = request.getContextPath();
    Logger log = LoggerFactory.getLogger(ParametreServiceImpl.class);
%>
<html>
	<head>
		<title>
			Error
		</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
		<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/master.css">
		<link type="text/css" href="<%=servletContext%>/theme/jquery/jquery-ui.css" rel="stylesheet" />
		<script type="text/javascript" src="<%=servletContext%>/scripts/jquery.js"></script>
		<script type="text/javascript" src="<%=servletContext%>/scripts/jquery-ui.js"></script>
	</head>
	<body>
		<hr />
		<h2>
			An error occurred
		</h2>
		<hr />
		<p>
			<strong>
				Please print the following message and send it to Globaz: </strong>
		</p>
		<dl>
			<dt>
				<strong>
					Date and hour
				</strong>
			</dt>
			<dd>
				<%=DateFormat.getDateInstance().format(new Date())%>
			</dd>
			<dt>
				<strong>
					Server
				</strong>
			</dt>
			<dd>
				<%=request.getServerName()%>
			</dd>
			<dt>
				<strong>
					Message
				</strong>
			</dt>
<%
	FWViewBeanInterface viewBean = (FWViewBeanInterface) session.getAttribute("viewBean");

	if (viewBean == null) {
		viewBean = (FWViewBeanInterface) request.getAttribute("viewBean");
	}

	if (viewBean == null) {
%>			<dd>
				Unexpected error (no viewBean, neither in session nor in request)
			</dd>
<%
	} else {
		if (FWJadeWidget.class.isAssignableFrom(viewBean.getClass())
				|| FWAJAXViewBeanInterface.class.isAssignableFrom(viewBean.getClass())) {
			JadeErrorBean errorBean = new Gson().fromJson(viewBean.getMessage(), JadeErrorBean.class);
			for (JadeExceptionBean uneException : errorBean.getExceptions()) {
%>			<dd>
				<%=uneException.getDetailMessage()%>
			</dd>
<%
			}
			if (errorBean.getStack() != null) {
%>			<dt>
				<strong>
					Stack
				</strong>
			</dt>
			<dd>
				<%=errorBean.getStack().replaceAll("\n", "<br/>")%>
			</dd>
<%
			}
		} else {
%>			<dd>
				<%=viewBean.getMessage()%>
			</dd>
			<dt>
				<strong>
					ViewBean
				</strong>
			</dt> 
			<dd>
				<%=viewBean.getClass().getName()%>
			</dd>
<%
		}
	}
	if (exception != null) {
%>			<dt>
				<strong>
					Exception
				</strong>
			</dt>
			<dd>
				<%=exception.getMessage()%>
			</dd>
			<dt>
				<strong>
					StackTrace
				</strong>
			</dt>
			<dd>
<%
        log.error("Error: ",exception);
		exception.printStackTrace(new PrintStream(response.getOutputStream()));
%>			</dd>
<%
	}
%>		</dl>
		<h3>
			Pile 10 last URL
		</h3>
		<table>
			<thead>
				<tr>
					<th>
						Position
					</th>
					<th>
						userAction
					</th>
					<th>
						Full URL
					</th>
				</tr>
			</thead>
			<tbody>
<%
	FWUrlsStack stack = (FWUrlsStack) session.getAttribute(FWServlet.URL_STACK);
	for (int i = stack.size(); i > 0 && i > (stack.size() - 10);) {
		FWUrl url = stack.peekAt(--i);
		FWParamString userAction = url.getParam("userAction");
		String userActionValue = null;
		if (userAction != null) {
			userActionValue = (String)userAction.getValue();
		}
%>				<tr>
					<td>
						<%=i%>
					</td>
					<td>
						<%=userActionValue%>
					</td>
					<td>
						<%=url.toString()%>
					</td>
				</tr>
<%
	}
%>			</tbody>
		</table>
	</body>
</html>
