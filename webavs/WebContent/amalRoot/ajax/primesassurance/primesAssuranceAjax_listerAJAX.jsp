<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="java.util.Iterator"%>
<%@page import="ch.globaz.amal.business.models.primesassurance.SimplePrimesAssurance"%>
<%@page import="globaz.amal.vb.primesassurance.AMPrimesAssuranceAjaxViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%
AMPrimesAssuranceAjaxViewBean viewBeanPrimes=(AMPrimesAssuranceAjaxViewBean)request.getAttribute("viewBean");
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
%>
<liste>
	<%
	int cpt = 1;
	for(JadeAbstractModel model :viewBeanPrimes.getSearchModel().getSearchResults()){
		SimplePrimesAssurance simpleAssurance = (SimplePrimesAssurance) model;

		%>
		<tr idEntity="<%=simpleAssurance.getId() %>">
			<td><%=simpleAssurance.getAnneeHistorique()%></td>
			<td class="amalCurrency"><%=new FWCurrency(simpleAssurance.getMontantPrimeAdulte())%></td>
			<td class="amalCurrency"><%=new FWCurrency(simpleAssurance.getMontantPrimeFormation())%></td>
			<td class="amalCurrency"><%=new FWCurrency(simpleAssurance.getMontantPrimeEnfant())%></td>
		</tr>
<%
	cpt++;
	}%>
</liste>
<ct:serializeObject objectName="viewBeanPrimes.simplePrimesAssurance" destination="xml"/>