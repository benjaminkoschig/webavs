<?xml version="1.0" encoding="ISO-8859-1" ?>

<%@ page language="java" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>

<%@ page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@ page import="java.io.PrintStream"%>
<%@ page import="java.io.PrintWriter"%>
<%@ page import="java.io.StringWriter"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.util.Date"%>

<message>
	<error errorPage="errorPage">
		<renderHtml>
			<hr />
			<h2>
				An error occured
			</h2>
			<hr />
			<p>
				<strong>
					Please print the following message and send it to Globaz:
				</strong>
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
			</dl>
		</renderHtml>
		<json>
			<exception>
<%
	FWViewBeanInterface viewBean = (FWViewBeanInterface) request.getAttribute("viewBean");
	if (viewBean != null) {
%>				<%=viewBean.getMessage()%>
<%
	}
	Exception exception = (Exception)request.getAttribute("exception");
	StringWriter sw = null;
	PrintWriter print = null;
	if (exception != null) {
		try {
			sw = new StringWriter();
			print = new PrintWriter(sw);
			exception.printStackTrace(print);
		} finally {
			if (print != null) {
				print.close();
			}
		}
%>
				<%=exception.getMessage()%>
			</exception>
			<stack>
				<%=sw.toString()%>
			</stack>
<%
	} else {
%>			</exception>
<%
	}
%>		</json>
	</error>
</message>