<?xml version="1.0" encoding="ISO-8859-1" ?>

<%@page import="globaz.pegasus.vb.retenues.PCRetenuesAjaxViewBean"%>
<%@page import="ch.globaz.corvus.business.models.pcaccordee.SimpleRetenuePayement"%>

<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%
PCRetenuesAjaxViewBean  viewBean = (PCRetenuesAjaxViewBean ) request.getAttribute(FWServlet.VIEWBEAN);
SimpleRetenuePayement entity = viewBean.getSimpleRetenuePayement();

%>
<message>
	<contenu>
		<anneeSubside><%="anneeSubside"%></anneeSubside>
		<montantRetenuMensuel><%=entity.getMontantRetenuMensuel()%></montantRetenuMensuel>		
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>