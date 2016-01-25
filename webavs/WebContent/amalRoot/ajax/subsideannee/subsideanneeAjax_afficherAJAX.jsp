<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnnee"%>
<%@page import="globaz.amal.vb.subsideannee.AMSubsideanneeAjaxViewBean"%>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%
AMSubsideanneeAjaxViewBean viewBean = (AMSubsideanneeAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleSubsideAnnee entity= viewBean.getSimpleSubsideAnnee();
%>
<message>
	<contenu>
		<anneeSubside><%=JadeStringUtil.toNotNullString(entity.getAnneeSubside())%></anneeSubside>
		<limiteRevenu><%=entity.getLimiteRevenu()%></limiteRevenu>
		<subsideAdulte><%=entity.getSubsideAdulte()%></subsideAdulte>		
		<subsideAdo><%=entity.getSubsideAdo()%></subsideAdo>
		<subsideEnfant><%=entity.getSubsideEnfant()%></subsideEnfant>
		<subsideSalarie1618><%=entity.getSubsideSalarie1618()%></subsideSalarie1618>
		<subsideSalarie1925><%=entity.getSubsideSalarie1925()%></subsideSalarie1925>		
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>