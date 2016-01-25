<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	idEcran = "GLX0004";
	rememberSearchCriterias = true;
%>
<%@ page import="globaz.lynx.db.societesdebitrice.*" %>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%@page import="globaz.lynx.parser.LXSelectBlockParser"%><ct:menuChange displayId="options" menuId="LX-OnlyDetail" showTab="menu"/>

<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
usrAction = "<%=globaz.lynx.application.LXApplication.DEFAULT_APPLICATION_LYNX.toLowerCase()%>.societesdebitrice.societeDebitrice.lister";
bFind = true;

top.document.title = "Sociétés débitrices - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>Soci&eacute;t&eacute;s d&eacute;bitrices<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>

			<TR>
                 	<TD nowrap width="128">Num&eacute;ro</TD>
                 	<TD nowrap colspan="2" width="200">
                 		<INPUT type="text" name="likeIdExterne" size="25" maxlength="25" tabindex="1">
                 	</TD>
                 	<TD>&nbsp;</TD>
                 	<TD nowrap width="128">Nom</TD>
                 	<TD nowrap colspan="2" width="200">
                 		<INPUT type="text" name="likeNomSociete" size="25" maxlength="25" tabindex="1">
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