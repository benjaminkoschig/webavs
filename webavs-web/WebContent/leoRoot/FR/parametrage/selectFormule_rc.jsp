<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<% idEcran="GEN0006"; %>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="javaScript">
	usrAction = "leo.parametrage.selectFormule.lister";
	bFind=true;
</SCRIPT>
<%
	bButtonNew=false;
%>
<ct:menuChange displayId="options" menuId="optionsAIDetail"/>
<SCRIPT>
	reloadMenuFrame(top.fr_menu, MENU_TAB_MENU);
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Recherche de la définition d'une formule <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

		<TR>
			<TD>
				<TABLE border="0" cellpadding="0" cellspacing="0" width="500px">

					<%-- Formule --%>
					<TR>
						<TD width="20%">Libelle</TD>
						<TD width="25%">
							<INPUT type="text" name="fromCsDocumentValue">
						</TD>
						<TD width="55%" colspan="3">&nbsp;</TD>
					</TR>
				</TABLE>
			</TD>
		</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>