<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@page import="ch.globaz.perseus.business.constantes.CSTypeCreance"%>
<%@page import="globaz.globall.db.BSessionUtil"%>
<%@page import="ch.globaz.perseus.business.models.creancier.Creancier"%>
<%@page import="globaz.perseus.vb.creancier.PFCreancierAjaxViewBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
	PFCreancierAjaxViewBean viewBean=(PFCreancierAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
	String id = viewBean.getCreancier().getId();
%>
	<liste>
		<%
		for(JadeAbstractModel model : viewBean.getSearchModel().getSearchResults()){
			Creancier line=((Creancier)model);
			%>
			<tr idEntity="<%=line.getId()%>">
				<td style="text-align: left"><%=(CSTypeCreance.TYPE_CREANCE_IMPOT_SOURCE.getCodeSystem().equals(line.getSimpleCreancier().getCsTypeCreance()))?BSessionUtil.getSessionFromThreadContext().getCodeLibelle(line.getSimpleCreancier().getCsTypeCreance()):line.getSimpleTiers().getDesignation1() + " " + line.getSimpleTiers().getDesignation2() %></td>
				<td> <%=line.getSimpleCreancier().getMontantRevendique()%></td>
				<td> <%=line.getSimpleCreancier().getMontantAccorde()%></td>
				<td> <%=BSessionUtil.getSessionFromThreadContext().getCodeLibelle(line.getSimpleCreancier().getCsTypeCreance())%></td>
				<td> <%=line.getSimpleCreancier().getIdCreancier()%></td>
			</tr>
		<%}%>
		<tr class="notSortable">
			<td style="text-align: left"><b>Total</b></td>
			<td data-g-cellsum=" " ></td>
			<td id="cellTotalReparti" data-g-cellsum=" " ></td>
			<td></td>
			<td></td>
		</tr>
	</liste>
	<ct:serializeObject objectName="viewBean.creancier" destination="xml"/>