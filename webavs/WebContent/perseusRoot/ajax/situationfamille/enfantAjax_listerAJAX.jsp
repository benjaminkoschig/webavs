<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@page import="ch.globaz.perseus.business.models.situationfamille.EnfantFamille"%>
<%@page import="globaz.perseus.utils.PFUserHelper"%>
<%@page import="globaz.perseus.vb.situationfamille.PFEnfantAjaxViewBean"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/xml;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.jade.log.business.renderer.JadeBusinessMessageRenderer"%>
<%@page import="globaz.jade.context.JadeThread"%>
<%@page import="globaz.framework.servlets.FWServlet"%>

<%
PFEnfantAjaxViewBean viewBean = (PFEnfantAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();

String idDemande = request.getParameter("idDemande");
String linkDFEnfants = "perseus?userAction&#61;perseus.donneesfinancieres.enfantDF.afficher&amp;idDemande&#61;" + idDemande;
%>

<message>
	<liste>
		<%
		Iterator it = viewBean.iterator();
		while(it.hasNext()){
			EnfantFamille ef = (EnfantFamille) it.next();
		%>
			<TR idEntity="<%=ef.getId()%>">
				<TD><%=PFUserHelper.getDetailAssure(objSession, ef.getEnfant().getMembreFamille().getPersonneEtendue())%></TD>
				<TD><%=objSession.getCodeLibelle(ef.getSimpleEnfantFamille().getCsSource())%></TD>
				<TD><%=objSession.getCodeLibelle(ef.getSimpleEnfantFamille().getCsGarde())%></TD>
				<TD><%=objSession.getCodeLibelle(ef.getSimpleEnfantFamille().getCsFormation())%></TD>
				<TD><%=objSession.getCodeLibelle(ef.getSimpleEnfantFamille().getCsEtatCivil())%></TD>
				<td><%=ef.getIsAI()%></td>
				<TD>
					<a href="<%=linkDFEnfants + "&amp;idEnfant&#61;" + ef.getEnfant().getId()%>"><ct:FWLabel key="JSP_PF_FAMILLE_D_DONNEES_FINANCIERES"/></a>
				</TD>
			</TR>			
		<%}%>
	</liste>
	<ct:serializeObject objectName="viewBean" destination="xml"/>
	<error>
		<%=JadeBusinessMessageRenderer.getInstance().getDefaultAdapter().render(JadeThread.logMessages(), JadeThread.currentLanguage())%>
	</error>
</message>
