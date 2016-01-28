<%-- Page de recherche des  monnaies etrangeres --%>
<%-- Creator: SCE, 6.10 --%>

<%-- tpl:insert page="/theme/capage.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/capage/header.jspf" %>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="globaz.framework.controller.FWController"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%-- tpl:put name="zoneInit" --%>
<%
// Les labels de cette page commence par la préfix "JSP_PC_PARAM_MONETR_R"
idEcran="PPC0101";

IFrameDetailHeight = "420";



%>
<%-- /tpl:put --%> 
<%@ include file="/theme/capage/javascripts.jspf" %>

<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsempty"/>

<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/CalendarPopup.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/date.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/calendar/PopupWindow.js"></SCRIPT>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/scripts/utils.js"></SCRIPT>
<script type="text/javascript">
	bFind = true;
	detailLink = "<%=actionNew%>";
	usrAction = "<%=IPCActions.ACTION_PARAMETRES_MONNAIES_ETRANGERES+".lister"%>";
	//actionNew =  "<%=servletContext + mainServletPath + "?userAction="+IPCActions.ACTION_PARAMETRES_MONNAIES_ETRANGERES + ".afficher&_method=add"%>";
</script>
<%-- /tpl:insert --%>
<%@ include file="/theme/capage/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PC_PARAM_MONETR_R_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/capage/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
						<TR>
							<TD>
								<TABLE border="0" cellspacing="0" cellpadding="0" width="900">
									<TR>
										<TD><LABEL for="forDateValable"><ct:FWLabel key="JSP_PC_PARAM_MONETR_R_DATE_VALABLE"/></LABEL></TD>
										<TD><input type="text" name="monnaieEtrangereSearch.forDateValable" value="" data-g-calendar="mandatory:false,type:month"/></TD>
										<TD><LABEL for="forCsTypeMonnaie"><ct:FWLabel key="JSP_PC_PARAM_MONETR_R_TYPE_MONNAIE"/></LABEL></TD>
										<TD><ct:FWCodeSelectTag codeType="PYMONNAIE" name="monnaieEtrangereSearch.forCsTypeMonnaie" wantBlank="true" defaut="blank"/></TD>
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