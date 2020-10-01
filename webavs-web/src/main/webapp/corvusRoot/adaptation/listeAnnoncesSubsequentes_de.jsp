<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_LAN_D"

	idEcran="PRE2051";
	
	REListeAnnoncesSubsequentesViewBean viewBean = (REListeAnnoncesSubsequentesViewBean)session.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	
	userActionValue=globaz.corvus.servlet.IREActions.ACTION_ADAPTATION_LISTE_ANNONCES_SUB + ".executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.corvus.vb.adaptation.REListeErreursViewBean"%>


<%@page import="globaz.corvus.utils.REPmtMensuel"%>
<%@page import="globaz.corvus.vb.adaptation.REListeAnnoncesSubsequentesViewBean"%><ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="menu">
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_LST_ANN_SUB_TITRE_PAGE"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

					<TR>
						<TD><ct:FWLabel key="JSP_CIRC_EMAIL"/></TD>
						<TD><INPUT type="text" name="eMailAddress" value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getEMailAddress())?controller.getSession().getUserEMail():viewBean.getEMailAddress()%>" class="libelleLong"></TD>
					</TR>	
					<TR>
						<TD><ct:FWLabel key="JSP_CIRC_MOIS_ANN"/></TD>
						<TD>
							<input	id="moisAnnee"
									name="moisAnnee"
									data-g-calendar="type:month"
									value="<%=REPmtMensuel.getDateDernierPmtMoisFixeMoinsUneAnnee(viewBean.getSession(), "12")%>" />
						</TD>
					</TR>				

											
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>