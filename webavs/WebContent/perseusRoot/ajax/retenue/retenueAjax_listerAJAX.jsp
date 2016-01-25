<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="ch.globaz.perseus.business.models.retenue.SimpleRetenue"%>
<%@page import="globaz.globall.db.BSessionUtil"%>
<%@page import="globaz.perseus.vb.retenue.PFRetenueAjaxViewBean"%>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@page import="ch.globaz.corvus.business.models.pcaccordee.SimpleRetenuePayement"%>

<%
	PFRetenueAjaxViewBean viewBean=(PFRetenueAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);

%>
	<liste>
		<%

		for(JadeAbstractModel model : viewBean.getSearchModel().getSearchResults() ){
			SimpleRetenue simpleRetenue = (SimpleRetenue)model;
			%>
			<tr idEntity="<%=simpleRetenue.getId() %>">
				<td  style="text-align:left;"><%=BSessionUtil.getSessionFromThreadContext().getCodeLibelle(simpleRetenue.getCsTypeRetenue())%></td>
				<td style="text-align:right;"><%=new FWCurrency(simpleRetenue.getMontantRetenuMensuel()).toStringFormat()%></td>
				<td style="text-align:right;"><%=new FWCurrency(simpleRetenue.getMontantTotalARetenir()).toStringFormat()%></td>
				<td style="text-align:right;"><%=new FWCurrency(simpleRetenue.getMontantDejaRetenu()).toStringFormat()%></td>
				<td style="text-align:right;"><%=simpleRetenue.getId()%></td>
			</tr>
		<%}%>		
		<tr class="notSortable">
			<td style="text-align: left"><b>Total</b></td>
			<td data-g-cellsum=" " ></td>
			<td data-g-cellsum=" " ></td>
			<td data-g-cellsum=" " ></td>
			<td></td>
		</tr>
	</liste>
	<ct:serializeObject objectName="viewBean.simpleRetenue" destination="xml"/>