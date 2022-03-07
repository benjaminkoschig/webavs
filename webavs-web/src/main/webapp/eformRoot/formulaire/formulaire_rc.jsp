<%@ page errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/find/header.jspf" %>

<%-- tpl:insert attribute="zoneInit" --%>
<ct:menuChange displayId="menu" menuId="eform-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="eform-optionsempty"/>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.css" rel="stylesheet" />

<script type="text/javascript" src="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.js"></script>

<%-- tpl:insert attribute="zoneScripts" --%>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>

<%-- /tpl:insert --%>
<%@ include file="/theme/find/bodyStart.jspf" %>

<%-- tpl:insert attribute="zoneTitle" --%>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td>
	</td>
</tr>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:insert attribute="zoneButtons" --%>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyEnd.jspf" %>

<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyClose.jspf" %>
