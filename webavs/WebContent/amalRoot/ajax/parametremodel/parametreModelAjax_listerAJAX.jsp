<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="ch.globaz.amal.business.models.parametremodel.ParametreModelComplex"%>
<%@page import="globaz.amal.vb.parametremodel.AMParametreModelAjaxViewBean"%>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%
AMParametreModelAjaxViewBean viewBean=(AMParametreModelAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
String idEnCours = viewBean.getParametreModelComplex().getId();
%>




<%@page import="globaz.framework.util.FWCurrency"%>
<message currentIdEntity="<%=idEnCours%>" hasMoreElement ="<%=viewBean.getListeParametremodelAjaxListViewBean().getParametreModelComplexSearch().isHasMoreElements()%>">
	<liste>
		<%
		int cpt = 1;
		for(Iterator itDonnee=viewBean.iterator();itDonnee.hasNext();){
			
			ParametreModelComplex parametreModelComplex = ((ParametreModelComplex)itDonnee.next());
			%>
			<tr idEntity="<%=parametreModelComplex.getSimpleParametreModel().getId() %>">
				<td><%=parametreModelComplex.getSimpleParametreModel().getAnneeValiditeDebut()%></td>
				<td>1</td>
				<td>2</td>
			</tr>
<%
		cpt++;
		}%>
	</liste>
	<ct:serializeObject objectName="viewBean.parametreModelComplex" destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>
</message>
