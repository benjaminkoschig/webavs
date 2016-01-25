<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.framework.servlets.widget.FWJadeWidgetListViewBean"%>
<%@page import="globaz.framework.ajax.utils.RenderTransportJson"%>
<%
FWJadeWidgetListViewBean viewBean = (FWJadeWidgetListViewBean) request.getAttribute("widgetViewBean");
String jsonError = RenderTransportJson.renderTransport(viewBean.getMessage()); 
%>

<%@page import="java.util.Iterator"%>
<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<list>
	<tocken><%=viewBean.getTocken()%></tocken>
	<%if(!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())){ %>
	<ul>
		<%for(int i=0; i<viewBean.getSize(); i++){%>
			<%=viewBean.getHtmlLineFor(i)%>
		<%} 
		int theMaxValue = 0;
		if(viewBean.getTotalCount()<viewBean.getOffset()+viewBean.getDefinedSearchSize()){
			theMaxValue=viewBean.getTotalCount();
		} else {
			theMaxValue=viewBean.getOffset()+viewBean.getDefinedSearchSize();
		}
		%>
		<li class="widgetCount">
			<span>
				[<%=viewBean.getOffset()==0?0:viewBean.getOffset()+1%>&#160;-&#160;<%=theMaxValue%>]&#160;
				<span id="totalCount"><%=viewBean.getTotalCount()%></span>&#160;<ct:FWLabel key="JSP_WIDGET_RESULTS_COUNT"/>
			</span>
		</li>
	</ul>
	<%} else {%>
	<errorJson>
		<![CDATA[<%=jsonError%>]]>
	</errorJson>
	<%}%>
</list>