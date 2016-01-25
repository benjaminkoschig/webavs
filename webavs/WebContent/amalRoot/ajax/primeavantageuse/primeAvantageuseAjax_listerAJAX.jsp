<%@page import="ch.globaz.amal.business.models.primeavantageuse.SimplePrimeAvantageuse"%>
<%@page import="globaz.amal.vb.primeavantageuse.AMPrimeAvantageuseAjaxViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%
AMPrimeAvantageuseAjaxViewBean viewBean=(AMPrimeAvantageuseAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
String idEnCours = viewBean.getSimplePrimeAvantageuse().getIdPrimeAvantageuse();
%>




<%@page import="globaz.framework.util.FWCurrency"%>
	<liste>
		<%
		int cpt = 1;
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
			
		    SimplePrimeAvantageuse primeAvantageuse=((SimplePrimeAvantageuse)itDonnee.next());
			%>
			<tr idEntity="<%=primeAvantageuse.getId() %>">
				<td><%=primeAvantageuse.getAnneeSubside()%></td>
				<td class="amalCurrency"><%=new FWCurrency(primeAvantageuse.getMontantPrimeAdulte())%></td>
				<td class="amalCurrency"><%=new FWCurrency(primeAvantageuse.getMontantPrimeFormation())%></td>
				<td class="amalCurrency"><%=new FWCurrency(primeAvantageuse.getMontantPrimeEnfant())%></td>
			</tr>
<%
		cpt++;
		}%>		
	</liste>
	<ct:serializeObject objectName="viewBean.simplePrimeAvantageuse" destination="xml"/>