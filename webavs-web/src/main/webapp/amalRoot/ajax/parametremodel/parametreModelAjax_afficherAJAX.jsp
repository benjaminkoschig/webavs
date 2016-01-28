<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="globaz.amal.vb.parametremodel.AMParametreModelAjaxViewBean"%>
<%@page import="ch.globaz.amal.business.models.parametremodel.ParametreModelComplex"%>
<%@page import="globaz.amal.vb.parametremodel.AMParametremodelAjaxViewBean"%>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%
AMParametreModelAjaxViewBean viewBean = (AMParametreModelAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
ParametreModelComplex entity= viewBean.getParametreModelComplex();
%>
<message>
	<contenu>
		<noModel><%=entity.getFormuleList().getId%></noModel>
		<nomModel><%=entity.getFormuleList().getLibelle()%></nomModel>
		<anneValiditeDebut><%=entity.getSimpleParametreModel().getAnneeValiditeDebut()%></anneValiditeDebut>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>