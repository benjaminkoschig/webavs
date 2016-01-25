<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.renteijapi.PCAutreApiAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.renteijapi.SimpleAutreApi"%>

<%
PCAutreApiAjaxViewBean viewBean = (PCAutreApiAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleAutreApi entity=viewBean.getAutreApi().getSimpleAutreApi();
%>


<message>
	<contenu>
		<montant><%= entity.getMontant()%></montant>
		<csType><%= entity.getCsType()%></csType>
		<autre><%= entity.getAutre()%></autre>
		<csGenre><%= entity.getCsGenre()%></csGenre>
		<csDegre><%= entity.getCsDegre()%></csDegre>
		<DR><%=viewBean.getAutreApi().getSimpleDonneeFinanciereHeader().getIsDessaisissementRevenu()%></DR>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>
</message>
