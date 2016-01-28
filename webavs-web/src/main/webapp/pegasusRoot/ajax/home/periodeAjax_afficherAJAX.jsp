<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.pegasus.vb.home.PCPeriodeAjaxViewBean"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%
PCPeriodeAjaxViewBean viewBean = (PCPeriodeAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
%>

<message>
	<contenu>
		<csEtatService><%=JadeStringUtil.isBlankOrZero(viewBean.getPeriodeServiceEtat().getSimplePeriodeServiceEtat().getCsServiceEtat())?"":viewBean.getPeriodeServiceEtat().getSimplePeriodeServiceEtat().getCsServiceEtat()%></csEtatService>
		<dateDebut><%=viewBean.getPeriodeServiceEtat().getSimplePeriodeServiceEtat().getDateDebut()%></dateDebut>
		<dateFin><%=viewBean.getPeriodeServiceEtat().getSimplePeriodeServiceEtat().getDateFin()%></dateFin>
		<idHome><%=viewBean.getHome().getIdHome() %></idHome>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
