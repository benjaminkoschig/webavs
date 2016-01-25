<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
// Les labels de cette page commence par la préfix "JSP_TAU_R"
idEcran="PRE0094";

globaz.corvus.vb.taux.RETauxViewBean viewBean = (globaz.corvus.vb.taux.RETauxViewBean) request.getAttribute("viewBean");

IFrameDetailHeight = "420";

FWController controller = (FWController) pageContext.getSession().getAttribute(FWServlet.OBJ_CONTROLLER);
bButtonNew = bButtonNew &&  controller.getSession().hasRight(IREActions.ACTION_TAUX, FWSecureConstants.UPDATE);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%@page import="globaz.corvus.servlet.IREActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.controller.FWController"%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript">
	bFind = true;
	detailLink = "<%=actionNew%>";
	usrAction = "<%=globaz.corvus.servlet.IREActions.ACTION_TAUX%>.lister";
	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_TAU_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<TABLE border="0" cellspacing="2" cellpadding="0" width="900">
									<TR>
										<TD><LABEL for="forDate"><ct:FWLabel key="JSP_TAU_R_VALABLE_LE"/></LABEL></TD>
										<TD>
											<input	id="forDate"
													name="forDate"
													data-g-calendar="type:default"
													value="" />
										</TD>
										<TD><LABEL for="forCsType"><ct:FWLabel key="JSP_TAU_R_TYPE"/></LABEL></TD>
										<TD>
											<ct:select name="forCsType" wantBlank="true" defaultValue="">
												<ct:optionsCodesSystems csFamille="<%=globaz.corvus.api.taux.IRETaux.CS_GROUPE_TYPE_TAUX%>"/>
											</ct:select>
										</TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
	 					<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyClose.jspf" %>
<%-- /tpl:insert --%>