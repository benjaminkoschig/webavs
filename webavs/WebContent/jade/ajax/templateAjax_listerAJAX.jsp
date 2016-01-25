<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@page import="globaz.framework.ajax.utils.RenderTransportJson"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.framework.bean.FWAJAXViewBeanInterface"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%
	FWAJAXViewBeanInterface viewBean = ((FWAJAXViewBeanInterface) request.getAttribute("viewBean"));
	String inculd = request.getAttribute("destination_ajax").toString();
	String hasMoreElement = JadeStringUtil.toNotNullString(String.valueOf(request.getAttribute("hasMoreElement_ajax")));
	String querySize = JadeStringUtil.toNotNullString(String.valueOf(request.getAttribute("querySize_ajax")));
	String currentIdEntity = JadeStringUtil.toNotNullString(String.valueOf(request.getAttribute("currentIdEntity_ajax")));
	//JadeBusinessMessageRenderer.getInstance().getAdapter(JadeBusinessMessageRendererJson.NAME).render(JadeThread.logMessages(), JadeThread.currentLanguage());
	String jsonError = RenderTransportJson.renderTransport(viewBean.getMessage()); 
%>

<message currentIdEntity="<%=currentIdEntity%>" hasMoreElement="<%=hasMoreElement%>" querySize="<%=querySize%>">
	<%if(!FWViewBeanInterface.ERROR.equals(viewBean.getMsgType())){%>
		<jsp:include page="<%=inculd%>"  />
	<%}%>
	<errorJson>
		<![CDATA[<%=jsonError%>]]>
	</errorJson>
</message>