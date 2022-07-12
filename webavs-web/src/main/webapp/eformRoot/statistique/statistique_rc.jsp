<%@ page import="java.time.LocalDate" %>
<%@ page import="ch.globaz.common.util.Dates" %>
<%@ page errorPage="/errorPage.jsp" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>


<%@ include file="/theme/find/header.jspf" %>
<%
	idEcran="GFE0001";
	bButtonNew = false;
%>

<%-- tpl:insert attribute="zoneInit" --%>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.css" />
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/scripts/eform/formulaire/formulaire_rc.css" />

<script type="text/javascript" src="<%=servletContext%>/scripts/erichynds.multiSelect/jquery.multiselect.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/menu.js"></script>


<%-- tpl:insert attribute="zoneScripts" --%>
<script >
	var bFind = true;
	var detailLink = "<%=actionNew%>";
	var usrAction = "eform.statistique.statistique.lister";
</script>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyStart.jspf" %>

<%-- tpl:insert attribute="zoneTitle" --%>
<ct:FWLabel key="STATISTIQUE_FORMULAIRE_TITRE"/>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyStart2.jspf" %>

<%-- tpl:insert attribute="zoneMain" --%>
<tr>
	<td style="width:10px"></td>
	<td style="width:150px">
		<LABEL for="byPeriod">
			<ct:FWLabel key="PERIODE"/>
		</LABEL>
	</td>
	<td>
		<div id="byPeriod">
			<input id="byStartDate" name="byStartDate" class="clearable" value="" data-g-calendar="mandatory:false"/>
			<ct:FWLabel key="AU"/>
			<input id="byEndDate" name="byEndDate" class="clearable" value="<%= Dates.getLastDayOfMonth(LocalDate.now()) %>" data-g-calendar="mandatory:false"/>
		</div>
	</td>
</tr>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:insert attribute="zoneButtons" --%>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyEnd.jspf" %>

<%-- tpl:insert attribute="zoneVieuxBoutons" --%>
<ct:menuChange displayId="menu" menuId="eform-menuprincipal" showTab="menu"/>
<ct:menuChange displayId="options" menuId="eform-optionsempty"/>
<%-- /tpl:insert --%>

<%@ include file="/theme/find/bodyClose.jspf" %>
