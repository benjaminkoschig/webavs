<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="ch.globaz.amal.business.models.primeavantageuse.SimplePrimeAvantageuse"%>
<%@page import="globaz.amal.vb.primeavantageuse.AMPrimeAvantageuseAjaxViewBean"%>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%
AMPrimeAvantageuseAjaxViewBean viewBean = (AMPrimeAvantageuseAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimplePrimeAvantageuse entity= viewBean.getSimplePrimeAvantageuse();
%>
<message>
	<contenu>
		<anneeSubside><%=JadeStringUtil.toNotNullString(entity.getAnneeSubside())%></anneeSubside>
		<montantPrimeAdulte><%=entity.getMontantPrimeAdulte()%></montantPrimeAdulte>
		<montantPrimeFormation><%=entity.getMontantPrimeFormation()%></montantPrimeFormation>
		<montantPrimeEnfant><%=entity.getMontantPrimeEnfant()%></montantPrimeEnfant>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>