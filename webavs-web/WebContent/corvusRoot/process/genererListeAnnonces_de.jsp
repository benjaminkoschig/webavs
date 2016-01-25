<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	// Les labels de cette page commence par la préfix "JSP_LAN_D"

	idEcran="PRE2046";
	
	globaz.corvus.vb.process.REGenererListeAnnoncesViewBean viewBean = (globaz.corvus.vb.process.REGenererListeAnnoncesViewBean)session.getAttribute("viewBean");
	globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	
	userActionValue=globaz.corvus.servlet.IREActions.ACTION_GENERER_LISTE_ANNONCES + ".executer";
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.corvus.vb.process.REGenererListeAnnoncesViewBean"%>
<ct:menuChange displayId="menu" menuId="corvus-menuprincipal"/>
<ct:menuChange displayId="options" menuId="corvus-optionsempty" showTab="menu">
</ct:menuChange>
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_LAN_D_TITRE"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD><LABEL for="eMailAddress"><ct:FWLabel key="JSP_LAN_D_EMAIL"/></LABEL></TD>
							<TD><INPUT type="text" name="eMailAddress" value="<%=globaz.jade.client.util.JadeStringUtil.isEmpty(viewBean.getEMailAddress())?controller.getSession().getUserEMail():viewBean.getEMailAddress()%>" class="libelleLong"></TD>
						</TR>						
						<TR>
							<TD>
								<LABEL for="mois">
									<ct:FWLabel key="JSP_LAN_D_MOIS"/>
								</LABEL>
							</TD>
							<TD>
								<input	id="mois"
										name="mois"
										data-g-calendar="type:month"
										value="<%=viewBean.getMois()%>" />
							</TD>
						</TR>
						<TR>
							<TD><LABEL for="outputType"><ct:FWLabel key="JSP_LAN_D_FICHIER_DE_SORTIE"/></LABEL></TD>
							<TD>
								<INPUT type="radio" name="outputType" value="<%=REGenererListeAnnoncesViewBean.PDF_OUTPUT%>" checked="checked"><ct:FWLabel key="JSP_LAN_D_PDF"/></INPUT>
								<INPUT type="radio" name="outputType" value="<%=REGenererListeAnnoncesViewBean.EXCEL_OUTPUT%>"><ct:FWLabel key="JSP_LAN_D_EXCEL"/></INPUT>
							</TD>
						</TR>	
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>