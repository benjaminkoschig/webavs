<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PIJ3000";
	userActionValue="ij.process.envoyerAnnonces.executer";
	globaz.ij.vb.process.IJEnvoyerAnnoncesViewBean viewBean = (globaz.ij.vb.process.IJEnvoyerAnnoncesViewBean)(session.getAttribute("viewBean"));
		globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="ij-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="ij-optionsempty"/>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_ENVOI_ANNONCES"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_ADRESSE_EMAIL"/>&nbsp;
							    <INPUT type="text" name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>">
							</TD>
						</TR>
						<TR>
							<TD><ct:FWLabel key="JSP_MOIS_ANNEE_COMPTABLE"/>&nbsp;
							    <ct:FWCalendarTag name="forMoisAnneeComptable" value="<%=viewBean.getForMoisAnneeComptable()%>" displayType="MONTH"/>&nbsp;
							    <ct:FWLabel key="JSP_FORMAT_MOISANNEECOMPTABLE"/>
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