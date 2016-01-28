<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="ch.globaz.amal.business.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfants"%>
<%@page import="globaz.amal.vb.deductionsfiscalesenfants.AMDeductionsFiscalesEnfantsAjaxViewBean"%>
<%@page import="globaz.amal.vb.parametreannuel.AMParametreAnnuelAjaxViewBean"%>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%
AMDeductionsFiscalesEnfantsAjaxViewBean viewBean = (AMDeductionsFiscalesEnfantsAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
SimpleDeductionsFiscalesEnfants entity= viewBean.getSimpleDeductionsFiscalesEnfants();
%>
<message>
	<contenu>
		<anneeTaxation><%=JadeStringUtil.toNotNullString(entity.getAnneeTaxation())%></anneeTaxation>
		<nbEnfant><%=JadeStringUtil.toNotNullString(entity.getNbEnfant())%></nbEnfant>				
		<montantDeductionParEnfant><%=new FWCurrency(JadeStringUtil.toNotNullString(entity.getMontantDeductionParEnfant()))%></montantDeductionParEnfant>
		<montantDeductionTotal><%=new FWCurrency(JadeStringUtil.toNotNullString(entity.getMontantDeductionTotal()))%></montantDeductionTotal>
	</contenu>
	<ct:serializeObject destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>	
</message>