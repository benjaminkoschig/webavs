<%@page import="ch.globaz.amal.business.models.parametreapplication.SimpleParametreApplication"%>
<%@page import="globaz.amal.vb.parametreapplication.AMParametreApplicationAjaxViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%
AMParametreApplicationAjaxViewBean viewBean = (AMParametreApplicationAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
String idEnCours = viewBean.getSimpleParametreApplication().getIdParametreApplication();
%>




<%@page import="globaz.framework.util.FWCurrency"%>
	<liste>
		<%
		int cpt = 1;
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
			
			SimpleParametreApplication parametreApplication=((SimpleParametreApplication)itDonnee.next());
			%>
			<tr idEntity="<%=parametreApplication.getId() %>">
				<td style="text-align:left"><%=objSession.getCodeLibelle(parametreApplication.getCsTypeParametre()).replaceAll("<","&lt;").replaceAll(">","&gt;")%></td>
				<td style="text-align:left"><%=parametreApplication.getValeurParametre()%></td>
				<td style="text-align:left"><%=objSession.getCodeLibelle(parametreApplication.getCsGroupeParametre()).replaceAll("<","&lt;").replaceAll(">","&gt;")%></td>
			</tr>
<%
		cpt++;
		}%>		
	</liste>
	<ct:serializeObject objectName="viewBean.simpleParametreApplication" destination="xml"/>