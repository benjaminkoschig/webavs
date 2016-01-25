<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
FWJadeWidgetListViewBean viewBean = (FWJadeWidgetListViewBean) request.getAttribute("widgetViewBean");
%>

<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.framework.servlets.widget.FWJadeWidgetListViewBean"%>
<%@page import="java.util.Map"%>


<ul>
<%
	for(Iterator it=viewBean.iterator();it.hasNext();){
		Map ligne=(Map)it.next();
		
		if(ligne!=null){%>
	<li class="widgetSuggestion"		
	<%		
			for(Iterator attrIt=ligne.keySet().iterator();attrIt.hasNext();){
				String key=(String)attrIt.next();
				String val=(String)ligne.get(key);
				if(!"description".equals(key)){
					%> <%=key%>="<%=val%>" <%
				}
			}
	%>><%=ligne.get("description")%></li>
<% 		
		}
	} %>
	<li class="widgetCount"><%=viewBean.getSuggestions().getTotalCount() %> <ct:FWLabel key="JSP_WIDGET_RESULTS_COUNT"/></li>
</ul>
