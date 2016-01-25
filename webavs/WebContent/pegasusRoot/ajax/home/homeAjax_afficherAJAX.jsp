<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.home.PCPeriodeAjaxViewBean"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.pegasus.vb.home.PCHomeAjaxViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.ajax.utils.RenderTransportJson"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored ="false" %>
<%
PCHomeAjaxViewBean viewBean = (PCHomeAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
String jsonError = RenderTransportJson.renderTransport(viewBean.getMessage()); 

%>

<message>
	<contenu>
		<idTiers><%=viewBean.getHome().getSimpleHome().getIdTiersHome()%></idTiers>
		<adresseTiers><c:out value="${viewBean.homeAdresseFormatee}"></c:out></adresseTiers>
		<nomBatiment><c:out value="${viewBean.home.simpleHome.nomBatiment}"></c:out><%=JadeStringUtil.escapeXML(viewBean.getHome().getSimpleHome().getNomBatiment()) %></nomBatiment>
		<noIdentification><%=viewBean.getHome().getSimpleHome().getNumeroIdentification()%></noIdentification>
		<parentViewBean><ct:serializeObject objectName="viewBean.home.simpleHome"/></parentViewBean>
		<isHorCanton><%=viewBean.getHome().getSimpleHome().getIsHorsCanton() %></isHorCanton>
		
		<idHome><%=viewBean.getHome().getSimpleHome().getIdHome() %></idHome>
		<creationSpy><%=viewBean.getHome().getSimpleHome().getCreationSpy()%></creationSpy>
		<spy><%=viewBean.getHome().getSimpleHome().getSpy()%></spy>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>
	<errorJson>
		<![CDATA[<%=jsonError%>]]>
	</errorJson>	
</message>
