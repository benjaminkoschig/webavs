<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PAP3003";

	userActionValue="apg.process.envoyerAnnonces.executer";
	globaz.apg.vb.process.APEnvoyerAnnoncesViewBean viewBean = (globaz.apg.vb.process.APEnvoyerAnnoncesViewBean)(session.getAttribute("viewBean"));
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<!--si APG -->
<%if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_APG) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<!--sinon, maternité -->
<%} else if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)==globaz.prestation.api.IPRDemande.CS_TYPE_MATERNITE) {%>
	<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_ENVOI_ANNONCES"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD width="25%">
								<ct:FWLabel key="JSP_ADRESSE_EMAIL"/>&nbsp;
							</TD>
							<TD>
								<INPUT type="text" name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>" size="30">
							</TD>
						</TR>
						<TR>
							<TD>
								<ct:FWLabel key="JSP_MOIS_ANNEE_COMPTABLE"/>&nbsp;
							</TD>
							<TD>
								<ct:inputText name="forMoisAnneeComptable" notation="data-g-calendar='type:month'"/>&nbsp;
							    <P <%=!viewBean.isDejaEnvoye()?"style=\"display:none\"":""%>>
							    	<ct:FWLabel key="JSP_ANNONCES_DEJA_ENVOYEES"/>
							    	<INPUT type="checkbox" name="okPourReenvoi">
							    </P>
							    <INPUT type="hidden" name="dejaEnvoye" value="<%=viewBean.isDejaEnvoye()%>">							
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>