<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplication"%>
<%@page import="globaz.amal.vb.parametreapplication.AMParametreApplicationAjaxViewBean"%>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%
AMParametreApplicationAjaxViewBean viewBean = (AMParametreApplicationAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
SimpleParametreApplication entity= viewBean.getSimpleParametreApplication();
%>
<message>
	<contenu>
		<csTypeParametre><%=JadeStringUtil.toNotNullString(entity.getCsTypeParametre())%></csTypeParametre>
		<valeurParametre><%=JadeStringUtil.toNotNullString(entity.getValeurParametre())%></valeurParametre>
		<csGroupeParametre><%=JadeStringUtil.toNotNullString(entity.getCsGroupeParametre())%></csGroupeParametre>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>