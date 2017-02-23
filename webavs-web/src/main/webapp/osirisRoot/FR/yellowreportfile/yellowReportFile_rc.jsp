<!DOCTYPE HTML PUBLIC "-//W3C//Dtd HTML 4.0 Transitional//EN">
<%-- tpl:insert page="/theme/find.jtpl" --%>
<%@page import="globaz.osiris.db.yellowreportfile.CAYellowReportFileManager"%>
<%@page import="java.util.Date"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="globaz.osiris.db.yellowreportfile.CAYellowReportFileState"%>
<%@page import="globaz.osiris.db.yellowreportfile.CAYellowReportFileType"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>

<%
    idEcran = "GCA3026";
	rememberSearchCriterias = true;
	bButtonNew = false;
%>

<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>

<script>
	usrAction = "osiris.yellowreportfile.yellowReportFile.lister";
	bFind = true;
</script>

<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="options" menuId="CA-BVR" showTab="options"/>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_GCA3026_TITLE"/><%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<tr>
		<td width="75px"><ct:FWLabel key="JSP_GCA3026_FICHIER_TRAITER"/></td>
		<td width="75px">
			<select name="forTreatedFile">
				<option value=""><%=objSession.getLabel("TOUS")%></option>
				<option value="<%=CAYellowReportFileManager.OUI%>"><%=objSession.getLabel("OUI")%></option>
				<option value="<%=CAYellowReportFileManager.NON%>" selected="selected"><%=objSession.getLabel("NON")%></option>
			</select>
		</td>
		<td colspan="3"/>
	</tr>
	<tr>
		<td width="75px"><ct:FWLabel key="JSP_GCA3026_A_PARTIR_DE"/></td>
		<td><ct:FWCalendarTag name="sinceDate" value="<%=JadeDateUtil.addDays(JadeDateUtil.getGlobazFormattedDate(new Date()), -9)%>" /></td>
		<td width="75px">&nbsp;</td>
		<td width="75px"><ct:FWLabel key="JSP_GCA3026_TYPEOF"/></td>
		<td>
			<select name="forTypeOfFile">
				<%for(CAYellowReportFileType type : CAYellowReportFileType.values()){%>
					<option value="<%=type.name()%>"><%=objSession.getLabel(type.getLabel())%></option>
           		<%}%>
			</select>
		</td>
	</tr>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%> <%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>