<?html version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Iterator"%>
<%@page import="globaz.jade.persistence.mapping.JadeModelMappingProvider"%>
<%@page import="globaz.jade.persistence.model.JadeSimpleModel"%>
<%@page import="globaz.jade.persistence.model.JadeComplexModel"%>
<%@page import="java.util.TreeMap"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="globaz.jade.persistence.sql.JadeSqlModelDefinition"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<meta http-equiv="Content-Style-Type" content="text/css"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0" />
<meta http-equiv="Cache-Control" content="no-cache" />
<script type="text/javascript" src="./scripts/jquery.js"></script>
<link rel="stylesheet" type="text/css" href="jade/persistence/theme/style.css"/>
<link rel="stylesheet" type="text/css" href="jade/persistence/theme/style-print.css" media="print"/>
<title>JADE Persistence Manager</title>
<script type="text/javascript"><!--
function selectAll(node){
	
	$("input:visible").each(function  () {
		this.checked = node.checked;
	})
	/*
	for(var i=0; i<document.getElementsByTagName('input').length; i++){
		
		document.getElementsByTagName('input')[i].checked = node.checked;
	}
	*/
}

function selectNodeByPackage(node){
	for(var i=0; i<document.getElementsByTagName('input').length; i++){
		if(document.getElementsByTagName('input')[i].value.match(node.value)){
			document.getElementsByTagName('input')[i].checked = node.checked;
		}
	}
}


jQuery.fn.liveUpdate = function($list){
	var $founded = $("#founded");
	if ( $list ) {
		cache = $list.map(function(){
			return jQuery(this).text().toLowerCase();
		});
		this.keyup(filter).keyup();
	}
	
	function filter(){
		var term = jQuery.trim( jQuery(this).val().toLowerCase() ), scores = [], n_nbTables = 0;
		if ( !term ) {
			$list.show();
		} else {
			$list.hide();
			cache.each(function(i){
				if ( this.indexOf( term ) > -1 ) {
					var $that = jQuery($list[i]);
					jQuery($list[i]).show();
					scores.push($list[i]);
					n_nbTables = n_nbTables+$that.find(".className").length; 
				}
			});
		}
		$founded.text("Package: "+scores.length);
		$("#nbTable").text(" ¦ Nb table: "+n_nbTables);
	}
	return this;
};


$(function() {
	$("#searchProject").liveUpdate($("table:first > tbody").children("tr"));
});
</script>
</head>
<body>
	<%
	TreeMap structuredMap = new TreeMap();
	
	Collection modelsMapped = JadeModelMappingProvider.getInstance().getMappedModelsClassNames();
	if(modelsMapped!=null){
		Iterator it = modelsMapped.iterator();
		while(it.hasNext()){
			String className = (String) it.next();
			String packageName = className.lastIndexOf(".")>-1?className.substring(0, className.lastIndexOf(".")):className;
			String modelName = className.lastIndexOf(".")>-1?className.substring(className.lastIndexOf(".")+1):className;
			String type=null;
			try{
				Class modelClass= Class.forName(className);
				if(modelClass!=null && JadeSimpleModel.class.isAssignableFrom(modelClass)){
					type = "Simple model";	
				} else if(modelClass!=null && JadeComplexModel.class.isAssignableFrom(modelClass)){
					type = "Complex model";	
				}
			} catch(Exception e){
				//Tant pis...
			}
			if(type!=null){
				HashMap structuredModels = null;
				if(structuredMap.containsKey(packageName)){
					structuredModels = (HashMap) structuredMap.get(packageName);
				} else {
					structuredModels=new HashMap();
					structuredMap.put(packageName, structuredModels);
				}
				ArrayList modelsContainer = null;
				if(structuredModels.containsKey(type)){
					modelsContainer = (ArrayList) structuredModels.get(type);
				} else {
					modelsContainer = new ArrayList();
					structuredModels.put(type, modelsContainer);
				}
				modelsContainer.add(modelName);
			}
	%>
	<%}
	}
	%>
	<div class="title">JADE Persistence Manager - List of the models</div>
	<div>
	<form action="persistence" method="post">
	<table cellpadding="0" cellspacing="0" border="0">
		<thead>
		<tr>
			<th><span>PackageName</span></th>
			<th>&nbsp;<input type="hidden" id="action" name="jaction" value="jade.persistence.display.createTables"/></th>
			<th width="60%">Filter <input type="text" id="searchProject" style="width:72%"/> <span id="founded"></span><span id="nbTable"></span></th>
			<th width="5%"><input type="button" name="launchForm" value="Create table script" onclick="document.forms[0].submit();"/></th>
			<th width="5%"><input type="button" name="launchForm" value="Check tables" onclick="document.getElementById('action').value='jade.persistence.display.checktables';document.forms[0].submit();"/></th>
			<th width="2%"><input type="checkbox" name="package" value="all" onclick="selectAll(this);"/></th>
		</tr>
		</thead>
		<tbody>
	<%Iterator it = structuredMap.keySet().iterator();
	boolean mainPair = false;
	while(it.hasNext()){
		String packageName = (String) it.next();%>
		<tr>
			<td<%=mainPair?" class=\"pair\"":""%>><%=packageName%></td>
			<td<%=mainPair?" class=\"pair\"":""%> colspan="5">
				<table cellpadding="0" cellspacing="0" border="0">
					<tr>
						<th width="10%">&nbsp;</th>
						<th width="50%"><span>Model name</span></th>
						<th width="20%">&nbsp;</th>
						<th width="18%">&nbsp;</th>
						<th width="2%"><input type="checkbox" classe="package" name="package" value="<%=packageName%>" onclick="selectNodeByPackage(this);"/></th>
					</tr>
				<% 
				boolean pair = false;
				Iterator it2 = ((HashMap)structuredMap.get(packageName)).keySet().iterator();
				while(it2.hasNext()){
					String type = (String) it2.next();
					ArrayList models = (ArrayList) ((HashMap)structuredMap.get(packageName)).get(type);
					Collections.sort(models);
					for(int i=0; i<models.size(); i++){
						String modelName =(String) models.get(i);
					%>
						<tr>
							<%if(i==0){%>
								<td rowspan="<%=models.size()%>" style="border-right: 1px solid black;"><span><%=type%></span></td>
							<%}%>
							<td<%=pair?" class=\"pair\"":""%>><%=modelName%></td>
							<td<%=pair?" class=\"pair\"":""%>><a href="persistence?jaction=jade.persistence.display.description&model=<%=packageName+"."+modelName %>">Show description</a></td>
							<td<%=pair?" class=\"pair\"":""%>><a href="persistence?jaction=jade.persistence.display.generate&model=<%=packageName+"."+modelName%>">Generate select queries</a></td>
							<%if("Simple model".equals(type)&& 
									(JadeModelMappingProvider.getInstance().getSqlModelDefinition(packageName+"."+modelName)!=null) && 
									(JadeModelMappingProvider.getInstance().getSqlModelDefinition(packageName+"."+modelName) instanceof JadeSqlModelDefinition) && 
									((JadeSqlModelDefinition)(JadeModelMappingProvider.getInstance().getSqlModelDefinition(packageName+"."+modelName))).isMapFullTable()){ %>
							<td<%=pair?" class=\"pair\"":""%>><input type="checkbox" class="className"  name="className" value="<%=packageName+"."+modelName%>" style="height: 12px;width: 12px;"/></td>
							<%} else {%>
							<td<%=pair?" class=\"pair\"":""%>>&nbsp;</td>
							<%} %>
						</tr>
					<%pair=!pair;}%>				
				<%}%>
				</table>				
			</td>
		</tr>				
		
	<% mainPair=!mainPair;} %>

<%-- %>
	<td<%=pair?" class=\"pair\"":""%>><span><%=className%></span></td>
	<%
		String type = "UNKNOWN";
		try{
			Class modelClass= Class.forName(className);
			if(modelClass!=null && JadeSimpleModel.class.isAssignableFrom(modelClass)){
				type = "Simple model";	
			} else if(modelClass!=null && JadeComplexModel.class.isAssignableFrom(modelClass)){
				type = "Complex model";	
			}
		} catch(Exception e){
			//Tant pis...
		}
	%>
	<td<%=pair?" class=\"pair\"":""%>><span><%=type%></span></td>
	<td<%=pair?" class=\"pair\"":""%>><a href="persistence?jaction=jade.persistence.display.description&model=<%= className %>">Show description</a></td>
	<td<%=pair?" class=\"pair\"":""%>><a href="persistence?jaction=jade.persistence.display.generate&model=<%= className %>">Generate select queries</a></td>
</tr>	
	
	%>--%>
	</tbody>
	</table>
	</form>
	</div>
</body>
</html>
