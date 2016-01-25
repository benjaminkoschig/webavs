<%@page import="ch.globaz.amal.business.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfants"%>
<%@page import="globaz.amal.vb.deductionsfiscalesenfants.AMDeductionsFiscalesEnfantsAjaxViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%
AMDeductionsFiscalesEnfantsAjaxViewBean viewBean=(AMDeductionsFiscalesEnfantsAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
String idEnCours = viewBean.getSimpleDeductionsFiscalesEnfants().getIdDeductionFiscaleEnfant();
%>
<liste>
	<%
	int cpt = 1;
	for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
		
		SimpleDeductionsFiscalesEnfants simpleDeductionsFiscalesEnfants=((SimpleDeductionsFiscalesEnfants)itDonnee.next());
		%>
		<tr idEntity="<%=simpleDeductionsFiscalesEnfants.getId() %>">
			<td><%=simpleDeductionsFiscalesEnfants.getAnneeTaxation()%></td>
			<td><%=simpleDeductionsFiscalesEnfants.getNbEnfant()%></td>				
			<td class="amalCurrency"><%=new FWCurrency(simpleDeductionsFiscalesEnfants.getMontantDeductionParEnfant()).toStringFormat()%></td>
			<td class="amalCurrency"><%=new FWCurrency(simpleDeductionsFiscalesEnfants.getMontantDeductionTotal()).toStringFormat()%></td>
		</tr>
<%
	cpt++;
	}%>
</liste>
<ct:serializeObject objectName="viewBean.simpleDeductionsFiscalesEnfants" destination="xml"/>