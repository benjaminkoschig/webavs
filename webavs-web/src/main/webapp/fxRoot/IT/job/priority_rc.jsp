<%-- tpl:insert page="/theme/find.jtpl" --%><%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/find/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/javascripts.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<script>
	usrAction = "fx.job.priority.lister";
	bFind = true;

	function onChangePriority(){
		document.all('btnFind').focus();
	}

</script>
<%
	bButtonNew = true;
%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%>
<DIV style="width: 100%">
	<SPAN class="idEcran">FX0305</SPAN>
	<ct:FWLabel key="JSP_JOB_PRIORITY_LISTE_TITRE"/>
</DIV>
<%-- /tpl:put --%>
</DIV>
</TH>
</TR>
<TR>
	<TD bgcolor="gray" colspan="2" height="0"></TD>
</TR>
<TR>
	<TD width="5">&nbsp;</TD>
	<TD>
		<TABLE id="subtable" style="height:<%=subTableHeight%>;width:100%" cellspacing="0" cellpadding="0">
			<TBODY>
			<TR>
				<TD height="20">&nbsp;</TD>
			</TR>
<%-- tpl:put name="zoneMain" --%>
<%
	String job = (request.getParameter("job")!=null?request.getParameter("job"):"");
	String forPriority = (request.getParameter("forPriority")!=null?request.getParameter("forPriority"):"");
%>
<tr><td>
<table style="width:100%">
<tr>
	<td><ct:FWLabel key="JSP_JOB_PRIORITY_LISTE_PRIORITY"/></td>
	<td>
		<ct:select name="forPriority"  wantBlank="true" onchange="onChangePriority()" defaultValue="<%=forPriority%>">
			<%
				for (int i = globaz.jade.job.common.JadeJobPriorities.MIN_PRIORITY; i <= globaz.jade.job.common.JadeJobPriorities.MAX_PRIORITY; i++) {
					String priorityValue = Integer.toString(i);
					String priorityLabel = priorityValue;
					if (i == globaz.jade.job.common.JadeJobPriorities.MIN_PRIORITY) {
						priorityLabel += " (MIN)";
					} else if (i == globaz.jade.job.common.JadeJobPriorities.MAX_PRIORITY) {
						priorityLabel += " (MAX)";
					} else if (i == globaz.jade.job.common.JadeJobPriorities.DEFAULT_PRIORITY) {
						priorityLabel += " (DEF)";
					}
			%>
			<ct:option label="<%=priorityLabel%>" value="<%=priorityValue%>"/>
			<%
				}
			%>
		</ct:select>

	</td>
	<td rowspan=2 style="width: 300px">
		<%@ include file="/fxRoot/FR/job/priority_info.jspf" %>
	</td>

</tr>
<tr>
	<td><ct:FWLabel key="JSP_JOB_PRIORITY_LISTE_JOB"/></td>
	<td>
		<input name="forJob" value="<%=job%>" size="100">
	</td>
</tr>
</table></td></tr>

<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyButtons.jspf" %>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyEnd.jspf" %>
<%-- tpl:put name="zoneVieuxBoutons" --%>
<ct:menuChange menuId="optionsBlank" displayId="options" showTab="menu"/>
<%-- /tpl:put --%>
<%@ include file="/theme/find/bodyClose.jspf" %>
<%-- /tpl:insert --%>