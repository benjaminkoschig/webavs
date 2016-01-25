<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuel"%>
<%@page import="globaz.amal.vb.parametreannuel.AMParametreAnnuelAjaxViewBean"%>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%
AMParametreAnnuelAjaxViewBean viewBean = (AMParametreAnnuelAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleParametreAnnuel entity= viewBean.getSimpleParametreAnnuel();
%>
<message>
	<contenu>
		<codeTypeParametre><%=entity.getCodeTypeParametre()%></codeTypeParametre>
		<anneeParametre><%=JadeStringUtil.toNotNullString(entity.getAnneeParametre())%></anneeParametre>
		<valeurParametre><%=JadeStringUtil.toNotNullString(entity.getValeurParametre())%></valeurParametre>				
		<valeurParametreString><%=JadeStringUtil.toNotNullString(entity.getValeurParametreString())%></valeurParametreString>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>