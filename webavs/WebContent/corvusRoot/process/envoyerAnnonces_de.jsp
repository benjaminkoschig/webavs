<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran="PRE3028";
	
	//Les labels de cette page commence par la préfix "JSP_EAN_D"

	userActionValue="corvus.process.envoyerAnnonces.executer";
	
	REEnvoyerAnnoncesViewBean viewBean = (REEnvoyerAnnoncesViewBean)(session.getAttribute("viewBean"));
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><%@page import="globaz.corvus.vb.process.REEnvoyerAnnoncesViewBean"%>
<ct:FWLabel key="JSP_EAN_D_ENVOI_ANNONCES"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><ct:FWLabel key="JSP_EAN_D_ADRESSE_EMAIL"/>&nbsp;
							    <INPUT type="text" name="eMailAddress" value="<%=eMailAddress!=null?eMailAddress:""%>">
							</TD>
						</TR>
						<TR>
							<TD>
								<ct:FWLabel key="JSP_EAN_D_MOIS_ANNEE_COMPTABLE"/>&nbsp;
							    <input	id="forMoisAnneeComptable"
							    		name="forMoisAnneeComptable"
							    		data-g-calendar="type:month"
							    		value="<%=viewBean.getForMoisAnneeComptable()%>" />
					    		&nbsp;
							    <ct:FWLabel key="JSP_EAN_D_FORMAT_MOISANNEECOMPTABLE"/>
							    <P <%=!viewBean.isDejaEnvoye()?"style=\"display:none\"":""%>>
							    	<ct:FWLabel key="JSP_EAN_D_ANNONCES_DEJA_ENVOYEES"/>
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