
<!-- Sample JSP file -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*"%>
<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<HEAD>
<META name="GENERATOR" content="IBM WebSphere Page Designer V4.0 for Windows">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/theme/master.css">
<LINK rel="StyleSheet" type="text/css" href="<%=request.getContextPath()%>/theme/sorthtmltable.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/sorthtmltable.js"></script>
<TITLE></TITLE>
</HEAD>
<BODY>
<%
	String sApp = new String();
	if (!request.getServerName().equals("localhost"))
		sApp = "/";

	String cd = new String("/muscaRoot/work/");
	if (request.getParameter("dir") != null)
		cd = request.getParameter("dir");
	if (!cd.endsWith("/"))
		cd = cd + "/";
	File realPath = realPath = new File(application.getRealPath(cd));
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat ("yyyy-MM-dd '-' HH:mm:ss");

%>
<table class="sort-table" id="table-1" cellspacing="0">
	<col />
	<col style="text-align: right" />
	<col style="text-align: right" />
	<thead>
		<tr>
			<td title="CaseInsensitiveString">Name</td>
			<td>Grösse</td>
			<td>Datum</td>
		</tr>
	</thead>
	<tbody>
   <%
	File[] files = realPath.listFiles();
	if (files != null) {
		for (int i = 0; (i < files.length)&&!files[i].isDirectory(); i++ ) {
%>

   	<tr class="<%= (i%2)==1?"rowOdd":"row"%>">
    
      <td class="mtd" width="70%"><a href="<%=request.getContextPath()%><%= cd+files[i].getName() %>" target="fr_main"><%= files[i].getName() %></a></td>
      <td class="mtd" width="10%"><%= files[i].length() %></td>
      <td class="mtd" width="*"><i><%= formatter.format(new Date(files[i].lastModified())) %></i></td>
    </tr>
    <% } } %>
    	</tbody>
</table>

<script type="text/javascript">
//<![CDATA[

function addClassName(el, sClassName) {
	var s = el.className;
	var p = s.split(" ");
	var l = p.length;
	for (var i = 0; i < l; i++) {
		if (p[i] == sClassName)
			return;
	}
	p[p.length] = sClassName;
	el.className = p.join(" ");
			
}

function removeClassName(el, sClassName) {
	var s = el.className;
	var p = s.split(" ");
	var np = [];
	var l = p.length;
	var j = 0;
	for (var i = 0; i < l; i++) {
		if (p[i] != sClassName)
			np[j++] = p[i];
	}
	el.className = np.join(" ");
}

var st = new SortableTable(document.getElementById("table-1"),
	["CaseInsensitiveString", "Number", "Date"]);
	
// restore the class names
st.onsort = function () {
	var rows = st.tBody.rows;
	var l = rows.length;
	for (var i = 0; i < l; i++) {
		removeClassName(rows[i], i % 2 ? "rowOdd" : "row");
		addClassName(rows[i], i % 2 ? "row" : "rowOdd");
	}
};


//]]>
</script>
  

</BODY>
</HTML>