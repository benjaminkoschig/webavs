<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@page import="java.util.List"%>
<%@page import="globaz.framework.dbversion.DBVersion"%><html>
<%
List allVersions = (List)request.getAttribute("viewBean");
%>
<head>
<title></title>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/theme/master.css">
<style type="text/css">
table.cute {
	border: 1px solid black;
	margin: 1em;
	padding: 0px;
	text-align: center;
	background-color: white;
}
table.cute td {
	padding-left: 1em;
	padding-right: 1em;
}

div.blockheader {
    background-color : #226194;
    border: solid 1px;
    border-color :  #88aaff #88aaff black black;
    color : white;
    font-weight: bold;
    font-family : Verdana,Arial;
    background-color : #226194;
    text-align: center;
    font-size: medium;
    padding: 3px;
}

div.appBody {
	background-color:#B3C4DB;
	margin-top : 0px;
	margin-left: 5px;
    margin-right : 0px;
    margin-bottom : 5px;
    border-width : 0px 0px 0px 0px;
    padding-bottom: 1em;
}
</style>
</head>
<body>
<div class="appBody">
<div class="blockheader">
Database versions history
</div>
<table class="cute">
	<thead>
		<tr>
			<th>Timestamp</th>
			<th>Application</th>
			<th>Version</th>
			<th>Release date</th>
		</tr>
	</thead>
	<tbody>
<%
for (int i = 0; i < allVersions.size(); i++) {
	DBVersion dbVer = (DBVersion)allVersions.get(i);
%>
	<tr>
		<td><%=dbVer.getTimeStamp()%></td>
		<td><%=dbVer.getApplicationLabel()%></td>
		<td><%=dbVer.getVersionLabel()%></td>
		<td><%=dbVer.getReleaseDate()%></td>
	</tr>
<%
}
%>
	</tbody>
</table>
</div>
</body>
</html>