<%-- tpl:insert page="/theme/process.jtpl" --%><%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%--
INFO: les labels de cette page sont prefixes avec 'JSP_GFO_D'
--%>
<%
	idEcran="PIJ3007";
	userActionValue=globaz.ij.servlet.IIJActions.ACTION_GENERER_FORMULAIRE + ".executer";
	globaz.ij.vb.process.IJGenererFormulaireViewBean viewBean = (globaz.ij.vb.process.IJGenererFormulaireViewBean)(session.getAttribute("viewBean"));
	selectedIdValue = viewBean.getIdFormulaire();
	showProcessButton = viewBean.getSession().hasRight(userActionValue, FWSecureConstants.UPDATE);
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
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_GFO_D_TITRE"/> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<tr>
							<td>
								<table border="0" cellspacing="0" cellpadding="0" width="350">
									<TR>
										<TD><ct:FWLabel key="JSP_GFO_D_EMAIL"/></TD>
										<TD>
										    <INPUT type="text" name="email" value="<%=viewBean.getEmail()%>">
										    
										    <INPUT type="hidden" name="idFormulaire" value="<%=viewBean.getIdFormulaire()%>">
										    <INPUT type="hidden" name="idPrononce" value="<%=viewBean.getIdPrononce()%>">
										    <INPUT type="hidden" name="csTypeIJ" value="<%=viewBean.getCsTypeIJ()%>">
										</TD>
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_GFO_D_DATE_DOCUMENT"/></TD>
										<TD><ct:FWCalendarTag name="date" value="<%=viewBean.getDate()%>"/></TD>
									</TR>
									<TR>
										<TD><ct:FWLabel key="JSP_GFO_D_DATE_RETOUR_DOCUMENT"/></TD>
										<TD><ct:FWCalendarTag name="dateRetour" value="<%=viewBean.getDateRetour()%>"/></TD>
									</TR>
									
									<%if ("1".equals(viewBean.getDisplaySendToGed())) { %> 
										<TR>
											<TD><ct:FWLabel key="JSP_ENVOYER_DANS_GED"/></TD>
											<TD>
												<INPUT type="checkbox" name="isSendToGed" value="on">
												<INPUT type="hidden" name="displaySendToGed" value="1">
											</TD>
										</TR>
										<TR>
											<TD>&nbsp;</TD>
										</TR>
									<% } else {%>
										<INPUT type="hidden" name="isSendToGed" value="FALSE">
										<INPUT type="hidden" name="displaySendToGed" value="0">
									<%} %>
									
								</table>
							</td>
						</tr>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> <%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>