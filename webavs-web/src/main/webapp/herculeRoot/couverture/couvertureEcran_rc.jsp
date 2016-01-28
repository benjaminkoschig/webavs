<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%
	rememberSearchCriterias = true;
	idEcran ="CCE0011";
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<SCRIPT>
top.document.title = "Web@AVS - <ct:FWLabel key='CONTROLE_EMPLOYEUR'/>";
usrAction = "hercule.couverture.couvertureEcran.lister";
</SCRIPT>

<script type="text/javascript"	src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>

<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css" />

<ct:menuChange displayId="menu" menuId="CE-MenuPrincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="CE-OptionsDefaut"/>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="COUVERTURES"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>

	<TR>
		<TD nowrap width="140" height="31"><ct:FWLabel key="NUMERO_AFFILIE" /></TD>	
		<TD nowrap width="300"><INPUT name="likeNumAffilie" type="text" size="17" maxlength="15" /></TD>
	</TR>
	<TR>
		<TD nowrap width="140" height="31"><ct:FWLabel key="ANNEE_COUVERTURE" /></TD>	
		<TD nowrap width="300"><INPUT name="forAnneeCouverture" type="text" size="4" maxlength="4" onkeypress="return filterCharForPositivInteger(window.event);" /></TD>
	</TR>
	<TR>
		<TD nowrap width="140" height="31"><ct:FWLabel key="COUVERTURE_ACTIF"/></TD>
		<TD nowrap width="300"><INPUT type="checkBox" checked="checked" name="isActif" /></TD>
	</TR>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>