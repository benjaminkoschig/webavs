<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
	<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
	<%@ include file="/theme/process/header.jspf" %>

<%-- tpl:put name="zoneInit" --%> 

	<%@ page import="globaz.musca.db.facturation.FATestFacturationJournaliereViewBean"%>

	<%
		idEcran="?";	
		FATestFacturationJournaliereViewBean viewBean = (FATestFacturationJournaliereViewBean) session.getAttribute("viewBean");
		userActionValue = "musca.facturation.testFacturationJournaliere.executer";
		globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
		globaz.globall.db.BSession objSession = (globaz.globall.db.BSession) controller.getSession();
	%>
<%-- /tpl:put --%>

<%-- tpl:put name="zoneBusiness" --%> <%-- /tpl:put --%>

<%@ include file="/theme/process/javascripts.jspf" %>

<%-- tpl:put name="zoneScripts" --%> 
	<ct:menuChange displayId="menu" menuId="FA-MenuPrincipal" showTab="menu"/>
<%-- /tpl:put --%>

	<%@ include file="/theme/process/bodyStart.jspf" %>

<%-- tpl:put name="zoneTitle" --%>Tests facturation journalière<%-- /tpl:put --%>

	<%@ include file="/theme/process/bodyStart2.jspf" %>

<%-- tpl:put name="zoneMain" --%> 

	<p>
		<b>
			Cet écran a été réalisé à des fins de tests pour la facturation journalière ! <br>
			Il permet d'effectuer manuellement les traitements qui seront effectués automatiquement de nuit
		</b>
	</p>
		
<%-- /tpl:put --%>					
	
	<%@ include file="/theme/process/footer.jspf" %>

<%-- tpl:put name="zoneEndPage" --%> <%-- /tpl:put --%>
	
	<%@ include file="/theme/process/bodyClose.jspf" %>

<%-- /tpl:insert --%>