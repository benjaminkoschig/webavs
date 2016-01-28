<%@page import="globaz.pegasus.utils.PCCreancierHandler"%>
<%@page import="globaz.globall.db.BSessionUtil"%>
<%@page import="ch.globaz.pegasus.business.models.creancier.Creancier"%>
<%@page import="globaz.pegasus.vb.creancier.PCCreancierAjaxViewBean"%>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
	PCCreancierAjaxViewBean viewBean=(PCCreancierAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
	String id = viewBean.getCreancier().getId();
	//Thread.currentThread().sleep(500);
%>
	<liste>
		<%
		String idGroup=null;
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
			Creancier line=((Creancier)itDonnee.next());
		%>
			<tr idEntity="<%=line.getId()%>">
				<td style="text-align: left"> <%=PCCreancierHandler.displayCreancierTiers(line.getSimpleTiers())%></td>
				<td style="text-align: right"> <%=line.getSimpleCreancier().getMontant()%></td>
				<td style="text-align: right"> <%=viewBean.getList().getMontantRepartiByCreancier(line.getSimpleCreancier().getIdCreancier())%></td>
				<td> <%=BSessionUtil.getSessionFromThreadContext().getCodeLibelle(line.getSimpleCreancier().getCsTypeCreance())%></td>
				<td> <%=line.getSimpleCreancier().getIdCreancier()%></td>
			</tr>
		<%}%>
			<tr class="notSortable">
				<td style="text-align: left"><b><ct:FWLabel key="JSP_PC_CREANCIER_L_MONTANT_TOTAL"/></b></td>
				<td data-g-cellsum=" " data-g-amountformatter=" " ></td>
				<td  data-g-cellsum=" " data-g-amountformatter=" " ></td>
				<td></td>
				<td></td>
			</tr>
		</liste>
	<ct:serializeObject objectName="viewBean.creancier" destination="xml"/>