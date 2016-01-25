<?html version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="globaz.jade.servlet.http.util.JadeJobMonitorVOSummary"%>
<%@page import="globaz.jade.servlet.http.util.JadeJobMonitorServletUtil"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.servlet.http.util.JadeJobMonitorVOData"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Content-Style-Type" content="text/css"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Cache-Control" content="no-cache" />
<link rel="stylesheet" type="text/css" href="monitor/theme/style.css"/>
<title>JADE Monitor Statistics</title>
</head>
<body>
<table>
	<tr>
		<td class='h1' colspan='<%=JadeJobMonitorServletUtil.NBCOLUMNS - 1%>'>
			<h1>JADE Job Statistics</h1>
		</td>
		<td class='h1'>
			<h1 class='right'><a href='jobmonitor'>back to full summary</a></h1>
		</td>
	</tr>
<%
JadeJobMonitorVOSummary vo = new JadeJobMonitorVOSummary();
boolean filterYear = false;
boolean filterMonth = false;
boolean filterDate = false;
boolean filterHour = false;
boolean filterWeekday = false;
boolean filterClass = false;
boolean filterOwner = false;
boolean filterQueue = false;
if (!JadeStringUtil.isEmpty(request.getParameter(JadeJobMonitorServletUtil.YEAR))) {
	vo.setFilterYear(request.getParameter(JadeJobMonitorServletUtil.YEAR));
	filterYear = true;
}
if (!JadeStringUtil.isEmpty(request.getParameter(JadeJobMonitorServletUtil.MONTH))) {
	vo.setFilterMonth(request.getParameter(JadeJobMonitorServletUtil.MONTH));
	filterMonth = true;
}
if (!JadeStringUtil.isEmpty(request.getParameter(JadeJobMonitorServletUtil.DATE))) {
	vo.setFilterDate(request.getParameter(JadeJobMonitorServletUtil.DATE));
	filterDate = true;
}
if (!JadeStringUtil.isEmpty(request.getParameter(JadeJobMonitorServletUtil.HOUR))) {
	vo.setFilterHour(request.getParameter(JadeJobMonitorServletUtil.HOUR));
	filterHour = true;
}
if (!JadeStringUtil.isEmpty(request.getParameter(JadeJobMonitorServletUtil.WEEKDAY))) {
	vo.setFilterWeekday(request.getParameter(JadeJobMonitorServletUtil.WEEKDAY));
	filterWeekday = true;
}
if (!JadeStringUtil.isEmpty(request.getParameter(JadeJobMonitorServletUtil.CLASS))) {
	vo.setFilterClass(request.getParameter(JadeJobMonitorServletUtil.CLASS));
	filterClass = true;
}
if (!JadeStringUtil.isEmpty(request.getParameter(JadeJobMonitorServletUtil.OWNER))) {
	vo.setFilterOwner(request.getParameter(JadeJobMonitorServletUtil.OWNER));
	filterOwner = true;
}
if (!JadeStringUtil.isEmpty(request.getParameter(JadeJobMonitorServletUtil.QUEUE))) {
	vo.setFilterQueue(request.getParameter(JadeJobMonitorServletUtil.QUEUE));
	filterQueue = true;
}
JadeJobMonitorServletUtil.loadSummary(vo);
%>
<%=JadeJobMonitorServletUtil.getSummaryTable(vo)%>
<%
if (!filterYear && !filterMonth && !filterDate) {
%>
<%=JadeJobMonitorServletUtil.getBlankLine()%>
<%=JadeJobMonitorServletUtil.getHitsTable(vo, JadeJobMonitorServletUtil.YEAR, vo.getYears(), vo.getSummaryData().getCount())%>
<%
}
%>
<%
if (!filterMonth && !filterDate) {
%>
<%=JadeJobMonitorServletUtil.getBlankLine()%>
<%=JadeJobMonitorServletUtil.getHitsTable(vo, JadeJobMonitorServletUtil.MONTH, vo.getLastMonths(), vo.getSummaryData().getCount())%>
<%
}
%>
<%
if (!filterDate) {
%>
<%=JadeJobMonitorServletUtil.getBlankLine()%>
<%=JadeJobMonitorServletUtil.getHitsTable(vo, JadeJobMonitorServletUtil.DATE, vo.getLastDays(), vo.getSummaryData().getCount())%>
<%
}
%>
<%
if (!filterHour) {
%>
<%=JadeJobMonitorServletUtil.getBlankLine()%>
<%=JadeJobMonitorServletUtil.getHitsTable(vo, JadeJobMonitorServletUtil.HOUR, vo.getHours(), vo.getSummaryData().getCount())%>
<%
}
%>
<%
if (!filterWeekday && !filterDate) {
%>
<%=JadeJobMonitorServletUtil.getBlankLine()%>
<%=JadeJobMonitorServletUtil.getHitsTable(vo, JadeJobMonitorServletUtil.WEEKDAY, vo.getWeekDays(), vo.getSummaryData().getCount())%>
<%
}
%>
<%
if (!filterClass) {
%>
<%=JadeJobMonitorServletUtil.getBlankLine()%>
<%=JadeJobMonitorServletUtil.getHitsTable(vo, JadeJobMonitorServletUtil.CLASS, vo.getClasses(), vo.getSummaryData().getCount())%>
<%=JadeJobMonitorServletUtil.getBlankLine()%>
<%=JadeJobMonitorServletUtil.getAverageTable(vo, JadeJobMonitorServletUtil.CLASSTIMES, vo.getClassTimes(), (vo.getClassTimes().size() > 0 ? ((JadeJobMonitorVOData)vo.getClassTimes().get(0)).getAverageDuration() : 0))%>
<%=JadeJobMonitorServletUtil.getBlankLine()%>
<%=JadeJobMonitorServletUtil.getTotalTable(vo, JadeJobMonitorServletUtil.CLASSTOTAL, vo.getClassTotal(), vo.getSummaryData().getTotalDuration())%>
<%
}
%>
<%
if (!filterOwner) {
%>
<%=JadeJobMonitorServletUtil.getBlankLine()%>
<%=JadeJobMonitorServletUtil.getHitsTable(vo, JadeJobMonitorServletUtil.OWNER, vo.getOwners(), vo.getSummaryData().getCount())%>
<%=JadeJobMonitorServletUtil.getBlankLine()%>
<%=JadeJobMonitorServletUtil.getAverageTable(vo, JadeJobMonitorServletUtil.OWNERTIMES, vo.getOwnerTimes(), (vo.getOwnerTimes().size() > 0 ? ((JadeJobMonitorVOData)vo.getOwnerTimes().get(0)).getAverageDuration() : 0))%>
<%=JadeJobMonitorServletUtil.getBlankLine()%>
<%=JadeJobMonitorServletUtil.getTotalTable(vo, JadeJobMonitorServletUtil.OWNERTOTAL, vo.getOwnerTotal(), vo.getSummaryData().getTotalDuration())%>
<%
}
%>
<%
if (!filterQueue) {
%>
<%=JadeJobMonitorServletUtil.getBlankLine()%>
<%=JadeJobMonitorServletUtil.getHitsTable(vo, JadeJobMonitorServletUtil.QUEUE, vo.getQueues(), vo.getSummaryData().getCount())%>
<%=JadeJobMonitorServletUtil.getBlankLine()%>
<%=JadeJobMonitorServletUtil.getAverageTable(vo, JadeJobMonitorServletUtil.QUEUETIMES, vo.getQueueTimes(), (vo.getQueueTimes().size() > 0 ? ((JadeJobMonitorVOData)vo.getQueueTimes().get(0)).getAverageDuration() : 0))%>
<%=JadeJobMonitorServletUtil.getBlankLine()%>
<%=JadeJobMonitorServletUtil.getTotalTable(vo, JadeJobMonitorServletUtil.QUEUETOTAL, vo.getQueueTotal(), vo.getSummaryData().getTotalDuration())%>
<%
}
%>
</table>
</body>
</html>