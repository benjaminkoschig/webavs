<?html version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.persistence.mapping.JadeModelMappingProvider"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Content-Style-Type" content="text/css"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Cache-Control" content="no-cache" />
<link rel="stylesheet" type="text/css" href="jade/persistence/theme/style.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="jade/persistence/theme/style-print.css" media="print"/>
<title>JADE Persistence Generate CREATE TABLES Script</title>
<script language="JavaScript">
function selectEveryThing(){
	window.clipboardData.setData('Text', handleNodes(document.getElementById("SQL")));  
}

function handleNodes(node){
	var text = "";
	if(node.innerText!=''){
		text+= node.innerText;
	}
	return text;
}
</script>
</head>
<body>
	<div class="title">JADE Persistence Manager - SCRIPT Create table</div>
	<div style="text-align: right; padding-right: 20px;"><a href="<%=request.getContextPath()%>/persistence">All models</a></div>
	<div>
		<table cellpadding="0" cellspacing="0">
			<tr>
				<th>Create table SQL code</th>
				<th style="padding-right: 10px; text-align: right;"><input type="button" value="Copy SQL" onclick="selectEveryThing();"/></th>
			</tr>
			<tr>
				<td id="SQL" colspan="2">
		<%String[] allParams = request.getParameterValues("className");
		if(allParams!=null){
		for(int i=0; i<allParams.length; i++){
			JadeAbstractSqlModelDefinition jadeDefinition = JadeModelMappingProvider.getInstance().getSqlModelDefinition(allParams[i]);
			if(jadeDefinition!=null && jadeDefinition instanceof JadeSqlModelDefinition){
				request.setAttribute("toUserModelDefinition", jadeDefinition);
				request.setAttribute("toUseClassName", allParams[i]);
		%>
			<div style="padding: 5px;"><jsp:include page="innerCreateTable.jsp"/></div>
		<%}}} %>
			</td></tr></table>
	</div>
</body>

<%@page import="globaz.jade.persistence.sql.JadeSqlModelDefinition"%>
<%@page import="globaz.jade.persistence.sql.JadeAbstractSqlModelDefinition"%></html>