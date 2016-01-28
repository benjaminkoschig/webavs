<%@page import="ch.globaz.amal.business.models.primemoyenne.SimplePrimeMoyenne"%>
<%@page import="globaz.amal.vb.primemoyenne.AMPrimeMoyenneAjaxViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%
AMPrimeMoyenneAjaxViewBean viewBean=(AMPrimeMoyenneAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
String idEnCours = viewBean.getSimplePrimeMoyenne().getIdPrimeMoyenne();
%>




<%@page import="globaz.framework.util.FWCurrency"%>
	<liste>
		<%
		int cpt = 1;
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
			
			SimplePrimeMoyenne primeMoyenne=((SimplePrimeMoyenne)itDonnee.next());
			%>
			<tr idEntity="<%=primeMoyenne.getId() %>">
				<td><%=primeMoyenne.getAnneeSubside()%></td>
				<td class="amalCurrency"><%=new FWCurrency(primeMoyenne.getMontantPrimeAdulte())%></td>
				<td class="amalCurrency"><%=new FWCurrency(primeMoyenne.getMontantPrimeFormation())%></td>
				<td class="amalCurrency"><%=new FWCurrency(primeMoyenne.getMontantPrimeEnfant())%></td>
			</tr>
<%
		cpt++;
		}%>		
	</liste>
	<ct:serializeObject objectName="viewBean.simplePrimeMoyenne" destination="xml"/>