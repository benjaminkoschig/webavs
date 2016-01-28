<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.fortuneusuelle.PCAutresDettesProuveesAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.SimpleAutresDettesProuvees"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.pegasus.vb.fortuneusuelle.PCAutresDettesProuveesAjaxViewBean"%>
<%@page import="ch.globaz.pegasus.business.models.fortuneusuelle.SimpleAutresDettesProuvees"%>
<%
PCAutresDettesProuveesAjaxViewBean viewBean = (PCAutresDettesProuveesAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleAutresDettesProuvees entity=viewBean.getAutresDettesProuvees().getSimpleAutresDettesProuvees();
%>

<message>
	<contenu>
		<montantDette><%=entity.getMontantDette()%></montantDette>
		<creancier><%=entity.getNomPrenomCreancier()%></creancier>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
