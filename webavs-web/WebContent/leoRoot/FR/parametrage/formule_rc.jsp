<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<% idEcran="GEN0004"; %>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT language="javaScript">
	usrAction = "leo.parametrage.formule.lister";
	bFind=true;
</SCRIPT>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>Recherche formule<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

	<%-- Catégorie --%>
	<%String forCategorie = request.getParameter("forCategorie");
	if(forCategorie == null){
		forCategorie = "";
	}%>
	<TR>
		<TD width="20%">Catégorie</TD>
		<TD width="25%"><ct:FWCodeSelectTag name="forCategorie" codeType="LECATJOUR" wantBlank="true" defaut="<%=forCategorie%>" /></TD>
		<TD width="55%" colspan="3">&nbsp;</TD>
	</TR>
						
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>