<%@page import="ch.globaz.amal.business.models.parametreannuel.SimpleParametreAnnuel"%>
<%@page import="globaz.amal.vb.parametreannuel.AMParametreAnnuelAjaxViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%
AMParametreAnnuelAjaxViewBean viewBean=(AMParametreAnnuelAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
String idEnCours = viewBean.getSimpleParametreAnnuel().getIdParametreAnnuel();
%>
<liste>
	<%
	int cpt = 1;
	for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
		
		SimpleParametreAnnuel simpleParametreAnnuel=((SimpleParametreAnnuel)itDonnee.next());
		%>
		<tr idEntity="<%=simpleParametreAnnuel.getId() %>">
			<td><%=simpleParametreAnnuel.getAnneeParametre()%></td>
			<td style="text-align:left"><%=objSession.getCodeLibelle(simpleParametreAnnuel.getCodeTypeParametre()).replaceAll("<","&lt;").replaceAll(">","&gt;")%></td>				
			<td class="amalCurrency"><%=new FWCurrency(simpleParametreAnnuel.getValeurParametre()).toStringFormat()%></td>
			<td style="text-align:right"><%=simpleParametreAnnuel.getValeurParametreString()%></td>
		</tr>
<%
	cpt++;
	}%>
</liste>
<ct:serializeObject objectName="viewBean.simpleParametreAnnuel" destination="xml"/>