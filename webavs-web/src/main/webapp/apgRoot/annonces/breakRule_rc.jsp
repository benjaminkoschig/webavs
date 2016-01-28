<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:insert attribute="zoneInit" --%>
<%
	
	bButtonNew = false;

%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:insert attribute="zoneScripts" --%>

<SCRIPT language="javascript">

	bFind = true;

	usrAction = "apg.annonces.breakRule.lister";
<%
idEcran="PAP0043";
%>
		
</SCRIPT>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:insert attribute="zoneTitle" --%>BreakRules<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:insert attribute="zoneMain" --%>
							
						<TR><TD>
							<TABLE border="0" cellspacing="0" cellpadding="0" width="100%">
								<TR>
									<TD valign="top"><LABEL for="detailsAssure"><ct:FWLabel key="JSP_DETAIL_REQUERANT"/></LABEL></TD>
									<TD>
										<%=request.getParameter("detailsAssure") %>
									</TD>
								</TR>
								<TR><TD colspan="6">&nbsp;</TD></TR>
								<TR>
									<TD><LABEL for="forIdDroit"><ct:FWLabel key="JSP_NO_DROIT"/></LABEL></TD>
									<TD><input name="forIdDroit" type="text" class="disabled" value="<%=request.getParameter("forIdDroit") %>"/></TD>
								</TR>
								<TR><TD colspan="6">&nbsp;</TD></TR>
							</TABLE>
						</TD></TR>
						
	 					<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:insert attribute="zoneButtons" --%>
				<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
