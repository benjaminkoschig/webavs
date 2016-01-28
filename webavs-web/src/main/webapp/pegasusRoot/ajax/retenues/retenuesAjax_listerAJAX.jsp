<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="globaz.globall.db.BSessionUtil"%>
<%@page import="ch.globaz.pegasus.business.models.pcaccordee.PcaRetenue"%>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@page import="globaz.pegasus.vb.retenues.PCRetenuesAjaxViewBean"%>
<%@page import="ch.globaz.corvus.business.models.pcaccordee.SimpleRetenuePayement"%>

<%
	PCRetenuesAjaxViewBean viewBean=(PCRetenuesAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);

%>
	<liste>
		<%

		for(JadeAbstractModel model : viewBean.getSearchModel().getSearchResults() ){
			PcaRetenue pcaRetenue = (PcaRetenue)model;
			SimpleRetenuePayement simpleRetenuePayement = pcaRetenue.getSimpleRetenue();
			%>
			<tr idEntity="<%=simpleRetenuePayement.getId() %>">
				<td  style="text-align:left;"><%=viewBean.getDesignation(pcaRetenue)%></td>
				<td style="text-align:left;"><%=BSessionUtil.getSessionFromThreadContext().getCodeLibelle(simpleRetenuePayement.getCsTypeRetenue())%></td>
				<td style="text-align:right;"><%=new FWCurrency(simpleRetenuePayement.getMontantRetenuMensuel()).toStringFormat()%></td>
				<td style="text-align:right;"><%=new FWCurrency(simpleRetenuePayement.getMontantTotalARetenir()).toStringFormat()%></td>
				<td style="text-align:right;"><%=new FWCurrency(simpleRetenuePayement.getMontantDejaRetenu()).toStringFormat()%></td>
				<td style="text-align:left;"><%=BSessionUtil.getSessionFromThreadContext().getCodeLibelle(pcaRetenue.getCsRoleFamillePC())%></td>
				<td style="text-align:right;"><%=simpleRetenuePayement.getId()%></td>
			</tr>
	<%}%>		
			<tr style="border-top: double">
				<td style="font-weight:bold;"><ct:FWLabel key="JSP_PC_RETENUE_TOTAL"/></td>
				<td></td>
				<td style="font-weight:bold; text-align:right;" data-g-cellsum=""></td>
				<td style="font-weight:bold;text-align:right;" data-g-cellsum=""></td>
				<td style="font-weight:bold;text-align:right;" data-g-cellsum=""></td>
				<td colspan="2"></td>
			</tr>
	</liste>
	<ct:serializeObject objectName="viewBean.simpleRetenuePayement" destination="xml"/>