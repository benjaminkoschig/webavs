<%@page import="ch.globaz.perseus.business.models.rentepont.CreancierRentePont"%>
<%@page import="globaz.perseus.vb.rentepont.PFCreancierRentePontAjaxViewBean"%>
<%@page import="globaz.globall.db.BSessionUtil"%>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
	PFCreancierRentePontAjaxViewBean viewBean=(PFCreancierRentePontAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
	String id = viewBean.getCreancierRentePont().getId();
%>
	<liste>
		<%
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
			CreancierRentePont line=((CreancierRentePont)itDonnee.next());
			%>
			<tr idEntity="<%=line.getId()%>">
				<td style="text-align: left"><%=line.getSimpleTiers().getDesignation1() + " " + line.getSimpleTiers().getDesignation2() %></td>
				<td> <%=line.getSimpleCreancierRentePont().getMontantRevendique()%></td>
				<td> <%=line.getSimpleCreancierRentePont().getMontantAccorde()%></td>
				<td> <%=BSessionUtil.getSessionFromThreadContext().getCodeLibelle(line.getSimpleCreancierRentePont().getCsTypeCreance())%></td>
				<td> <%=line.getSimpleCreancierRentePont().getIdCreancier()%></td>
			</tr>
		<%}%>
		<tr class="notSortable">
			<td style="text-align: left"><b>Total</b></td>
			<td data-g-cellsum=" " data-g-amountformatter=" " ></td>
			<td id="cellTotalReparti" data-g-cellsum=" " data-g-amountformatter=" " ></td>
			<td></td>
			<td></td>
		</tr>
	</liste>
	<ct:serializeObject objectName="viewBean.creancierRentePont" destination="xml"/>