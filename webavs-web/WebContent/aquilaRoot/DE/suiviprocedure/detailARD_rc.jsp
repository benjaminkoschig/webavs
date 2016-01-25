<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%idEcran = "GCO0030";
rememberSearchCriterias = true;
%>
<%@ page import="globaz.aquila.db.journaux.*" %>
<%
	actionNew += "&idContentieux=" + request.getParameter("selectedId");
%>
<SCRIPT language="JavaScript" src="<%=request.getContextPath()%>/aquilaRoot/javascript/aquila.js"></SCRIPT>
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/aquilaRoot/theme/aquila.css">
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<jsp:include flush="true" page="../menuChange.jsp"/>
<SCRIPT language="JavaScript">
<!--hide this script from non-javascript-enabled browsers
usrAction = "aquila.suiviprocedure.detailARD.lister";
bFind = true;

top.document.title = "Verlauf des Verfahrens - VGS - " + top.location.href;
// stop hiding -->
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
				<%-- tpl:put name="zoneTitle" --%>
				<%globaz.aquila.db.access.poursuite.COContentieux contentieuxViewBean = (globaz.aquila.db.access.poursuite.COContentieux) session.getAttribute("contentieuxViewBean");%>
			<span class="postItIcon">
			<ct:FWNote sourceId="<%=contentieuxViewBean.getIdContentieux()%>" tableSource="<%=contentieuxViewBean.getTableName()%>"/>
			</span>
			Verlauf des Verfahrens - VGS<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
						<%-- tpl:put name="zoneMain" --%>
					<jsp:include flush="true" page="../headerContentieux.jsp"/>

					<TR>
						<TD colspan="10"><BR/><HR noshade size="3"/><BR/></TD>
					</TR>

					<TR>
					<TD nowrap>VGS Datum</TD>
		            <TD nowrap><ct:FWCalendarTag name="forDateARD" doClientValidation="CALENDAR" value=""/></TD>
		            <TD colspan="8">&nbsp;
		            <input type="hidden" name="forIdContentieux" value="<%=request.getParameter("selectedId")%>"/>
		            </TD>
					</TR>

          <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>