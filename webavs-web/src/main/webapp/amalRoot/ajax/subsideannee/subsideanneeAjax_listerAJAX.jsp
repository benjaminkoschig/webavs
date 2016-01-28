<%@page import="ch.globaz.amal.business.models.subsideannee.SimpleSubsideAnnee"%>
<%@page import="globaz.amal.vb.subsideannee.AMSubsideanneeAjaxViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%
AMSubsideanneeAjaxViewBean viewBean=(AMSubsideanneeAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
String idEnCours = viewBean.getSimpleSubsideAnnee().getIdSubsideAnnee();
%>




<%@page import="globaz.framework.util.FWCurrency"%>
	<liste>
		<%
		int cpt = 1;
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
			
			SimpleSubsideAnnee subsideAnnee=((SimpleSubsideAnnee)itDonnee.next());
			%>
			<tr idEntity="<%=subsideAnnee.getId() %>">
				<td><%=subsideAnnee.getAnneeSubside()%></td>
				<td class="amalCurrency"><%=new FWCurrency(subsideAnnee.getLimiteRevenu()).toStringFormat()%></td>
				<td class="amalCurrency"><%=new FWCurrency(subsideAnnee.getSubsideAdulte()).toStringFormat()%></td>
				<td class="amalCurrency"><%=new FWCurrency(subsideAnnee.getSubsideAdo()).toStringFormat()%></td>
				<td class="amalCurrency"><%=new FWCurrency(subsideAnnee.getSubsideEnfant()).toStringFormat()%></td>
				<td class="amalCurrency"><%=new FWCurrency(subsideAnnee.getSubsideSalarie1618()).toStringFormat()%></td>
				<td class="amalCurrency"><%=new FWCurrency(subsideAnnee.getSubsideSalarie1925()).toStringFormat()%></td>
			</tr>
<%
		cpt++;
		}%>		
	</liste>
	<ct:serializeObject objectName="viewBean.simpleSubsideAnnee" destination="xml"/>