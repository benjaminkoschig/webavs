<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.perseus.vb.situationfamille.PFEnfantAjaxViewBean"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%
PFEnfantAjaxViewBean viewBean = (PFEnfantAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>

<message>
	<contenu>
		<tiers><%=PFUserHelper.getDetailAssure(objSession, viewBean.getEnfantFamille().getEnfant().getMembreFamille().getPersonneEtendue())%></tiers>
		<idTiersEnfant><%=viewBean.getEnfantFamille().getEnfant().getMembreFamille().getSimpleMembreFamille().getIdTiers()%></idTiersEnfant>
		<garde><%=viewBean.getEnfantFamille().getSimpleEnfantFamille().getCsGarde() %></garde>
		<formation><%=viewBean.getEnfantFamille().getSimpleEnfantFamille().getCsFormation() %></formation>
		<etatCivil><%=viewBean.getEnfantFamille().getSimpleEnfantFamille().getCsEtatCivil()%></etatCivil>
		<ai><%=viewBean.getEnfantFamille().getEnfant().getMembreFamille().getSimpleMembreFamille().getIsAI()%></ai>
		<source><%=viewBean.getEnfantFamille().getSimpleEnfantFamille().getCsSource() %></source>
		<liens>link</liens>
		<spy><%=viewBean.getEnfantFamille().getSimpleEnfantFamille().getSpy()%></spy>
		<idEnfant><%=viewBean.getEnfantFamille().getSimpleEnfantFamille().getIdEnfant()%></idEnfant>
		<spyMbrFam><%=viewBean.getEnfantFamille().getEnfant().getMembreFamille().getSimpleMembreFamille().getSpy()%></spyMbrFam>
		<idMembreFamille><%=viewBean.getEnfantFamille().getEnfant().getMembreFamille().getSimpleMembreFamille().getIdMembreFamille()%></idMembreFamille>
		<idEnfantFamille><%=viewBean.getEnfantFamille().getSimpleEnfantFamille().getIdEnfantFamille()%></idEnfantFamille>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>
