<?html version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="globaz.jade.servlet.http.util.JadeHttpMonitorServletUtil"%>
<%@page import="globaz.jade.servlet.http.util.JadeHttpMonitorVOSummary"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.servlet.http.util.JadeHttpMonitorVOData"%>
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
		<td class='h1' colspan='<%=JadeHttpMonitorServletUtil.NBCOLUMNS - 1%>'>
			<h1>JADE Web Statistics</h1>
		</td>
		<td class='h1'>
			<h1 class='right'><a href='webmonitor'>back to full summary</a></h1>
		</td>
	</tr>
<%
JadeHttpMonitorVOSummary vo = new JadeHttpMonitorVOSummary();
boolean filterYear = false;
boolean filterMonth = false;
boolean filterDate = false;
boolean filterHour = false;
boolean filterWeekday = false;
boolean filterServlet = false;
boolean filterAction = false;
boolean filterVisitor = false;
if (!JadeStringUtil.isEmpty(request.getParameter(JadeHttpMonitorServletUtil.YEAR))) {
	vo.setFilterYear(request.getParameter(JadeHttpMonitorServletUtil.YEAR));
	filterYear = true;
}
if (!JadeStringUtil.isEmpty(request.getParameter(JadeHttpMonitorServletUtil.MONTH))) {
	vo.setFilterMonth(request.getParameter(JadeHttpMonitorServletUtil.MONTH));
	filterMonth = true;
}
if (!JadeStringUtil.isEmpty(request.getParameter(JadeHttpMonitorServletUtil.DATE))) {
	vo.setFilterDate(request.getParameter(JadeHttpMonitorServletUtil.DATE));
	filterDate = true;
}
if (!JadeStringUtil.isEmpty(request.getParameter(JadeHttpMonitorServletUtil.HOUR))) {
	vo.setFilterHour(request.getParameter(JadeHttpMonitorServletUtil.HOUR));
	filterHour = true;
}
if (!JadeStringUtil.isEmpty(request.getParameter(JadeHttpMonitorServletUtil.WEEKDAY))) {
	vo.setFilterWeekday(request.getParameter(JadeHttpMonitorServletUtil.WEEKDAY));
	filterWeekday = true;
}
if (!JadeStringUtil.isEmpty(request.getParameter(JadeHttpMonitorServletUtil.SERVLET))) {
	vo.setFilterServlet(request.getParameter(JadeHttpMonitorServletUtil.SERVLET));
	filterServlet = true;
}
if (!JadeStringUtil.isEmpty(request.getParameter(JadeHttpMonitorServletUtil.ACTION))) {
	vo.setFilterAction(request.getParameter(JadeHttpMonitorServletUtil.ACTION));
	filterAction = true;
}
if (!JadeStringUtil.isEmpty(request.getParameter(JadeHttpMonitorServletUtil.VISITOR))) {
	vo.setFilterVisitor(request.getParameter(JadeHttpMonitorServletUtil.VISITOR));
	filterVisitor = true;
}
JadeHttpMonitorServletUtil.loadSummary(vo);
%>
<%=JadeHttpMonitorServletUtil.getSummaryTable(vo)%>
<%
if (!filterYear && !filterMonth && !filterDate) {
%>
<%=JadeHttpMonitorServletUtil.getBlankLine()%>
<%=JadeHttpMonitorServletUtil.getHitsTable(vo, JadeHttpMonitorServletUtil.YEAR, vo.getYears(), vo.getSummaryData().getCount())%>
<%
}
%>
<%
if (!filterMonth && !filterDate) {
%>
<%=JadeHttpMonitorServletUtil.getBlankLine()%>
<%=JadeHttpMonitorServletUtil.getHitsTable(vo, JadeHttpMonitorServletUtil.MONTH, vo.getLastMonths(), vo.getSummaryData().getCount())%>
<%
}
%>
<%
if (!filterDate) {
%>
<%=JadeHttpMonitorServletUtil.getBlankLine()%>
<%=JadeHttpMonitorServletUtil.getHitsTable(vo, JadeHttpMonitorServletUtil.DATE, vo.getLastDays(), vo.getSummaryData().getCount())%>
<%
}
%>
<%
if (!filterHour) {
%>
<%=JadeHttpMonitorServletUtil.getBlankLine()%>
<%=JadeHttpMonitorServletUtil.getHitsTable(vo, JadeHttpMonitorServletUtil.HOUR, vo.getHours(), vo.getSummaryData().getCount())%>
<%
}
%>
<%
if (!filterWeekday && !filterDate) {
%>
<%=JadeHttpMonitorServletUtil.getBlankLine()%>
<%=JadeHttpMonitorServletUtil.getHitsTable(vo, JadeHttpMonitorServletUtil.WEEKDAY, vo.getWeekDays(), vo.getSummaryData().getCount())%>
<%
}
%>
<%
if (!filterServlet && !filterAction) {
%>
<%=JadeHttpMonitorServletUtil.getBlankLine()%>
<%=JadeHttpMonitorServletUtil.getHitsTable(vo, JadeHttpMonitorServletUtil.SERVLET, vo.getServlets(), vo.getSummaryData().getCount())%>
<%=JadeHttpMonitorServletUtil.getBlankLine()%>
<%=JadeHttpMonitorServletUtil.getAverageTable(vo, JadeHttpMonitorServletUtil.SERVLETTIMES, vo.getServletTimes(), (vo.getServletTimes().size() > 0 ? ((JadeHttpMonitorVOData)vo.getServletTimes().get(0)).getAverageDuration() : 0))%>
<%=JadeHttpMonitorServletUtil.getBlankLine()%>
<%=JadeHttpMonitorServletUtil.getTotalTable(vo, JadeHttpMonitorServletUtil.SERVLETTOTAL, vo.getServletTotal(), vo.getSummaryData().getTotalDuration())%>
<%
}
%>
<%
if (!filterAction) {
%>
<%=JadeHttpMonitorServletUtil.getBlankLine()%>
<%=JadeHttpMonitorServletUtil.getHitsTable(vo, JadeHttpMonitorServletUtil.ACTION, vo.getActions(), vo.getSummaryData().getCount())%>
<%=JadeHttpMonitorServletUtil.getBlankLine()%>
<%=JadeHttpMonitorServletUtil.getAverageTable(vo, JadeHttpMonitorServletUtil.ACTIONTIMES, vo.getActionTimes(), (vo.getActionTimes().size() > 0 ? ((JadeHttpMonitorVOData)vo.getActionTimes().get(0)).getAverageDuration() : 0))%>
<%=JadeHttpMonitorServletUtil.getBlankLine()%>
<%=JadeHttpMonitorServletUtil.getTotalTable(vo, JadeHttpMonitorServletUtil.ACTIONTOTAL, vo.getActionTotal(), vo.getSummaryData().getTotalDuration())%>
<%
}
%>
<%
if (!filterVisitor) {
%>
<%=JadeHttpMonitorServletUtil.getBlankLine()%>
<%=JadeHttpMonitorServletUtil.getHitsTable(vo, JadeHttpMonitorServletUtil.VISITOR, vo.getVisitors(), vo.getSummaryData().getCount())%>
<%=JadeHttpMonitorServletUtil.getBlankLine()%>
<%=JadeHttpMonitorServletUtil.getAverageTable(vo, JadeHttpMonitorServletUtil.VISITORTIMES, vo.getVisitorTimes(), (vo.getVisitorTimes().size() > 0 ? ((JadeHttpMonitorVOData)vo.getVisitorTimes().get(0)).getAverageDuration() : 0))%>
<%=JadeHttpMonitorServletUtil.getBlankLine()%>
<%=JadeHttpMonitorServletUtil.getTotalTable(vo, JadeHttpMonitorServletUtil.VISITORTOTAL, vo.getVisitorTotal(), vo.getSummaryData().getTotalDuration())%>
<%
}
%>
</table>
</body>
</html>