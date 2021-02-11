<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ page import="globaz.prestation.api.IPRDemande" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP2002";

	userActionValue="apg.process.recapitulationAnnonce.executer";
	globaz.apg.vb.process.APRecapitulationAnnonceViewBean viewBean = (globaz.apg.vb.process.APRecapitulationAnnonceViewBean)(session.getAttribute("viewBean"));
		globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
	String typeAPG = request.getParameter("forTypeAPG");
	viewBean.setForTypeAPG(typeAPG);
	request.removeAttribute("forTypeAPG");

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
<%}else if ((String)globaz.prestation.tools.PRSessionDataContainerHelper.getData(session,globaz.prestation.tools.PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION)== IPRDemande.CS_TYPE_PATERNITE) {%>
<ct:menuChange displayId="menu" menuId="ap-menuprincipalapat" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ap-optionsempty"/>
<%}%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_RECAPITULATION_ANNONCE"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_ADRESSE_EMAIL"/>&nbsp;
							    <INPUT type="text" name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>">
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_MOIS_ANNEE_COMPTABLE"/>&nbsp;
							    <ct:FWCalendarTag name="forMoisAnneeComptable" value="" displayType="MONTH"/>&nbsp;
							    <ct:FWLabel key="JSP_FORMAT_MOISANNEECOMPTABLE"/>
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>
