<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="globaz.perseus.vb.creancier.PFCreancierAdressePaiementAjaxViewBean"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/xml" pageEncoding="ISO-8859-1"%>
<%@page import="globaz.framework.servlets.FWServlet"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%
PFCreancierAdressePaiementAjaxViewBean viewBean = (PFCreancierAdressePaiementAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>

<message>
	<contenu>
		<adresse><%=viewBean.getAdressePaiement()%></adresse>
	</contenu>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>