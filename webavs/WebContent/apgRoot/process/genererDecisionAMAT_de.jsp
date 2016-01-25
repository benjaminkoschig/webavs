<%-- tpl:insert page="/theme/process.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/process/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
idEcran="PAP3006";

	globaz.apg.vb.process.APGenererDecisionAMATViewBean viewBean = (globaz.apg.vb.process.APGenererDecisionAMATViewBean) session.getAttribute("viewBean");
	
	if (viewBean.isCalcule()) {
		userActionValue = "apg.process.genererDecisionAMAT.executer";
	} else {
		userActionValue = "apg.process.genererDecisionAMAT.afficher";
	}
	
			globaz.framework.controller.FWController controller = (globaz.framework.controller.FWController) session.getAttribute("objController");
	globaz.globall.db.BSession objSession = (globaz.globall.db.BSession)controller.getSession();
	String eMailAddress=objSession.getUserEMail();
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/process/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

	<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu"/>
	<ct:menuChange displayId="options" menuId="ap-optionsempty"/>

<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart.jspf" %>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="TITRE_GENERER_COMM_DEC_AMAT"/><%-- /tpl:put --%>
<%@ include file="/theme/process/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<% if (!viewBean.isCalcule()) { %>
						<TR>
							<TD colspan="2">
								<P style="color: red"><ct:FWLabel key="JSP_PRESTATION_NON_CALCULEE"/></P>
							</TD>
						</TR>
						<TR>
							<TD colspan="2">&nbsp;</TD>
						</TR>
						<% } %>
						<TR>
							<TD><LABEL for="email"><ct:FWLabel key="JSP_ADRESSE_EMAIL"/></LABEL></TD>
							<TD><INPUT type="text" name="email" value="<%=eMailAddress!=null?eMailAddress:""%>"></TD>
						</TR>
						<TR>
							<TD><LABEL for="date"><ct:FWLabel key="JSP_DATE_SUR_DOCUMENT"/></LABEL></TD>
							<TD><ct:FWCalendarTag name="date" value="<%=viewBean.getDate()%>"/></TD>
						</TR>
						<TR>
							<TD colspan="2">
								<TABLE width="50%">
									 <TR>
										<TD>
											<LABEL for="decision"><ct:FWLabel key="JSP_DECISION"/></LABEL>
											<INPUT type="radio" name="decision" value="on"<%=viewBean.isDecision()?" checked" : ""%>>
											<LABEL for="decision"><ct:FWLabel key="JSP_COMMUNICATION"/></LABEL>
											<INPUT type="radio" name="decision" value="off"<%=!viewBean.isDecision()?" checked" : ""%>>
											<INPUT type="hidden" name="idDroit" value="<%=viewBean.getIdDroit()%>">
										</TD>
									</TR>
										<%if ("1".equals(viewBean.getDisplaySendToGed())) { %> 
											<TR>
												<TD><ct:FWLabel key="JSP_ENVOYER_DANS_GED"/>
													<INPUT type="checkbox" name="isSendToGed" value="on" CHECKED>
													<INPUT type="hidden" name="displaySendToGed" value="1">
												</TD>
											</TR>
										<%} else {%>
											<INPUT type="hidden" name="isSendToGed" value="FALSE">
											<INPUT type="hidden" name="displaySendToGed" value="0">
										<%}%>
								</TABLE>
							</TD>
						</TR>
						<%-- /tpl:put --%>
<%@ include file="/theme/process/footer.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%	if (request.getParameter("_back") != null && request.getParameter("_back").equals("sl")) { %> <%	}%> 
<%-- /tpl:put --%>
<%@ include file="/theme/process/bodyClose.jspf" %>
<%-- /tpl:insert --%>