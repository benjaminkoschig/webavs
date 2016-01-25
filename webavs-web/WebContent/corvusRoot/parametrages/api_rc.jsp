<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
// Les labels de cette page commence par la préfix "JSP_PAR_R"
idEcran="PRE0092";

IFrameDetailHeight = "420";

FWController controller = (FWController) pageContext.getSession().getAttribute(FWServlet.OBJ_CONTROLLER);
bButtonNew = bButtonNew &&  controller.getSession().hasRight(IREActions.ACTION_PARAMETRAGE_API, FWSecureConstants.UPDATE);
%>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.framework.controller.FWController"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.corvus.servlet.IREActions"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/AnchorPosition.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<SCRIPT language="JavaScript">
	bFind = true;
	detailLink = "<%=actionNew%>";
	usrAction = "<%=globaz.corvus.servlet.IREActions.ACTION_PARAMETRAGE_API%>.lister";
	
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PAR_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<TABLE border="0" cellspacing="0" cellpadding="0" width="900">
									<TR>
										<TD><LABEL for="du"><ct:FWLabel key="JSP_PAR_D_DU"/></LABEL></TD>
										<TD>
											<input	id="fromDateDebut"
													name="fromDateDebut"
													data-g-calendar="type:month"
													value="" />
										</TD>
										<TD><LABEL for="au"><ct:FWLabel key="JSP_PAR_D_AU"/></LABEL></TD>
										<TD>
											<input	id="toDateFin"
													name="toDateFin"
													data-g-calendar="type:month"
													value="" />
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