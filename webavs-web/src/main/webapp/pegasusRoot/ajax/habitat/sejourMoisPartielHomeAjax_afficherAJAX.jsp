<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page import="ch.globaz.pegasus.business.models.habitat.SimpleSejourMoisPartielHome" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.pegasus.utils.PCCommonHandler"%>
<%@page import="globaz.pegasus.vb.habitat.PCSejourMoisPartielHomeAjaxViewBean"%>

<%
	PCSejourMoisPartielHomeAjaxViewBean viewBean = (PCSejourMoisPartielHomeAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
	SimpleSejourMoisPartielHome entity= viewBean.getSejourMoisPartielHome().getSimpleSejourMoisPartielHome();
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>


<message>
	<contenu>
		<prixJournalier><%=PCCommonHandler.getCurrencyFormtedDefault(viewBean.getSejourMoisPartielHome().getSimpleSejourMoisPartielHome().getPrixJournalier()) %></prixJournalier>
		<fraisNourriture><%=PCCommonHandler.getCurrencyFormtedDefault(viewBean.getSejourMoisPartielHome().getSimpleSejourMoisPartielHome().getFraisNourriture()) %></fraisNourriture>
		<nbJours><%=viewBean.getSejourMoisPartielHome().getSimpleSejourMoisPartielHome().getNbJours() %></nbJours>
		<%@ include file="/pegasusRoot/ajax/commonDonneeFinanciere.jsp" %>
		
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>