<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "GLX0012";
	rememberSearchCriterias = true;
%>
<%@ page import="globaz.lynx.db.codetva.*" %>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.lynx.parser.LXSelectBlockParser"%>
<ct:menuChange displayId="menu" menuId="LX-MenuPrincipal"/>
<ct:menuChange displayId="options" menuId="LX-OnlyDetail" showTab="menu"/>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
usrAction = "<%=globaz.lynx.application.LXApplication.DEFAULT_APPLICATION_LYNX.toLowerCase()%>.codetva.codeTva.lister";
bFind = true;

top.document.title = "Suche der MWST Codes - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Suche der MWST Codes<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

			<TR>    	
					<%
						String selectCsCodeTVASelect = LXSelectBlockParser.getForCsCodeTVASelectBlock(objSession);
		
						if (!globaz.jade.client.util.JadeStringUtil.isBlank(selectCsCodeTVASelect)) {
							out.print("<TD nowrap width=\"128\" align=\"left\">Code</TD>");
							out.print("<TD colspan=\"2\" nowrap width=\"200\">");
							out.print(selectCsCodeTVASelect);
							out.print("</TD>");
						}
					%>             
                 	<TD>&nbsp;</TD>
                   	<TD width="128">Datum</TD>
                   	<TD colspan="2" width="200">
                   		<ct:FWCalendarTag name="forDateBetween" doClientValidation="CALENDAR" value="" tabindex="1"/>
                   	</TD>
			</TR>


          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>