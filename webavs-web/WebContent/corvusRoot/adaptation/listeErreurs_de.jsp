<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_LAN_D"

	idEcran="PRE2052";
	
	REListeErreursViewBean viewBean = (REListeErreursViewBean)session.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	
	userActionValue=globaz.corvus.servlet.IREActions.ACTION_ADAPTATION_LISTE_ERREURS + ".executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.corvus.vb.adaptation.REListeErreursViewBean"%>


<%@page import="globaz.corvus.utils.REPmtMensuel"%><ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="menu">
</ct:menuChange>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_LIST_ERR_ADA_TITRE_PAGE"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>&nbsp;</TD>
							<TD><ct:FWLabel key="JSP_CIRC_EMAIL"/></TD>
							<TD><INPUT type="text" name="eMailAddress" value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getEMailAddress())?controller.getSession().getUserEMail():viewBean.getEMailAddress()%>" class="libelleLong"></TD>
						</TR>	
						<TR>
							<TD>&nbsp;</TD>
							<TD><ct:FWLabel key="JSP_CIRC_MOIS_ANN"/></TD>
							<TD>
								<input	id="moisAnnee"
										name="moisAnnee"
										data-g-calendar="type:month"
										value="<%=REPmtMensuel.getDateProchainPmt(viewBean.getSession())%>" />
							</TD>
						</TR>											
						<TR>
							<TD>&nbsp;</TD>
							<TD><ct:FWLabel key="JSP_LIST_ERR_ADA_P_DE"/></TD>
							<TD>
								<input type="text" class="montantMini" name="pourcentDe" style="size: 4;"/>&nbsp;&nbsp;
								<ct:FWLabel key="JSP_LIST_ERR_ADA_P_A"/>&nbsp;&nbsp;
								<input type="text" class="montantMini" name="pourcentA" style="size: 4;"/>
							</TD>
						</TR>	
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>