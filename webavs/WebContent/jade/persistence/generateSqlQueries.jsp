<?html version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page import="globaz.globall.http.JSPUtils"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.jade.persistence.mapping.JadeModelMappingProvider"%>
<%@ page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@ page import="globaz.jade.persistence.model.JadeAbstractSearchModel"%>
<%@ page import="globaz.jade.persistence.sql.JadeAbstractSqlModelDefinition"%>
<%@ page import="globaz.jade.persistence.sql.JadeSqlComplexFromDefinition"%>
<%@ page import="globaz.jade.persistence.sql.JadeSqlFromDefinition"%>
<%@ page import="globaz.jade.persistence.sql.JadeSqlJoinDefinition"%>
<%@ page import="globaz.jade.persistence.sql.JadeSqlWhereDefinition"%>
<%@ page import="globaz.jade.persistence.util.JadePersistenceUtil"%>
<%@ page import="java.util.Iterator"%>

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
		<meta http-equiv="Content-Style-Type" content="text/css" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />
		<meta http-equiv="Cache-Control" content="no-cache" />
	
		<link rel="stylesheet" type="text/css" href="jade/persistence/theme/style.css" media="screen" />
		<link rel="stylesheet" type="text/css" href="jade/persistence/theme/style-print.css" media="print" />
	
		<title>
			JADE Persistence Manager
		</title>
	</head>
	<body>
		<div class="title">
			JADE Persistence Manager - Generate queries
		</div>
		<div style="text-align: right; padding-right: 20px;">
			<a href="persistence">
				All models
			</a>
		</div>
		<div>
<%
	String modelClassName = request.getParameter("model");
	if (!JadeStringUtil.isEmpty(modelClassName)) {
		JadeAbstractSqlModelDefinition modelDefinition = JadeModelMappingProvider.getInstance().getSqlModelDefinition(modelClassName);
		if (modelDefinition == null) {
%>			<span>
				No definition found for model : <i><%=modelClassName %></i>
			</span>
<%
	} else {
%>			<form action="persistence?jaction=jade.persistence.display.generate&model=<%=modelClassName%>">
				<table cellpadding="0" cellspacing="0" border="0">
					<tr>
						<th colspan="4">
							Query String generator for : <%=modelClassName%>
						</th>
					</tr>
					<tr>
						<td colspan="3" style="vertical-align: middle;">
							&nbsp;
							<input type="hidden" name="jaction" value="jade.persistence.display.generate" />
							<input type="hidden" name="model" value="<%=modelClassName%>" />
							<input type="hidden" name="generate" value="true" />
						</td>
					</tr>
					<tr>
						<td style="vertical-align: middle; width: 15%; ">
							Primary key :
						</td>
						<td style="vertical-align: middle; width: 35%; text-align: right; padding-right: 10px;">
							<%=modelDefinition.getFieldsDefinition().getPrimaryKey().getAttributeName()%>
						</td>
						<td style="vertical-align: middle; width: 50%;">
							<input type="text" name="id" value="<%=JadeStringUtil.isEmpty(request.getParameter("id")) || "null".equals(request.getParameter("id"))?"":request.getParameter("id")%>" style="width: 100%" />
						</td> 
					</tr>
<%
		Iterator it = modelDefinition.getWhereDefinitionKeys();
		boolean first = true;
		while (it.hasNext()) {
			String currentKey = (String)it.next();
	
			if (first) {
				first=!first;
%>					<tr>
						<td colspan="3" style="vertical-align: middle;">
							<hr />
						</td>
					</tr>
					<tr>
						<td style="vertical-align: middle; width: 15%; ">
							&nbsp;
						</td>
						<td style="vertical-align: middle; width: 35%; text-align: right; padding-right: 10px;">
							Define Search model class to user
						</td>
						<td style="vertical-align: middle; width: 50%;">
							<input type="text" name="searchModelClass" value="<%=JadeStringUtil.isEmpty(request.getParameter("searchModelClass")) || "null".equals(request.getParameter("searchModelClass"))?"":request.getParameter("searchModelClass")%>" style="width: 100%" />
						</td> 
						</tr>
<%
			}
%>						<tr>
							<td colspan="3" style="vertical-align: middle;">
								<hr />
							</td>
						</tr>
						<tr>
							<td style="vertical-align: middle;">
								Search definition :
							</td>
							<td style="vertical-align: middle;">
								Use this search definition : <i>currentKey</i>
							</td>
							<td style="vertical-align: middle;">
								<input type="radio" name="whereKey" value="<%=JadeStringUtil.isEmpty(request.getParameter("whereKey")) || "null".equals(request.getParameter("whereKey"))?"":request.getParameter("whereKey")%>" <%=currentKey.equalsIgnoreCase(request.getParameter("whereKey")) || ((JadeStringUtil.isEmpty(request.getParameter("whereKey"))|| "null".equals(request.getParameter("whereKey"))) && "default".equals(currentKey))?"checked=\"checked\"":""%> />
							</td>
						</tr>
<%
			JadeSqlWhereDefinition whereDefinition = modelDefinition.getWhereDefinition(currentKey);
			if (whereDefinition != null) {
				request.setAttribute("searchGroup", whereDefinition.getSearchGroupe());
%>						<jsp:include page="innerSearchGroupeForQueries.jsp"></jsp:include>
<%
			}
		}
%>						<tr>
							<td colspan="3" style="vertical-align: middle;">
								<hr />
							</td>
						</tr>
						<tr>
							<td colspan="3" style="text-align: right; padding-right: 50px;">
								<input type="button" value="Generate" onclick="document.forms[0].submit();" />
							</td>
						</tr>
					</table>
				</form>
<%
		if (!JadeStringUtil.isEmpty(request.getParameter("generate")) && ! "null".equals(request.getParameter("generate"))) {
%>				<div>
					<table cellpadding="0" cellspacing="0" border="0">
						<tr>
							<th>
								Generated Query String
							</th>
						</tr>
						<tr>
							<td id="SQL">
								<div>
									<b>SELECT</b>
								</div>
								<div style="padding-left: 10px;">
									<%=modelDefinition.getFieldsDefinition().writeSqlSelect()%>
								</div>
								<div>
									<b>FROM</b>
								</div>
<%
			JadeSqlFromDefinition fromDef = modelDefinition.getSqlFromDefinition();
%>								<div style="padding-left: 10px;">
									<div>
										<%=fromDef.getTableDefinition().writeSqlSelect()%>
									</div>
<%
			if (fromDef instanceof JadeSqlComplexFromDefinition) {
				JadeSqlComplexFromDefinition cFromDef = (JadeSqlComplexFromDefinition) fromDef;

				for (Iterator<JadeSqlJoinDefinition> iterator = cFromDef.getJoinDefinitions().iterator(); iterator.hasNext(); ) {
					JadeSqlJoinDefinition joinDef = iterator.next();
%>									<div style="padding-left: 10px;">
										<b><%=joinDef.getJoinType().getSqlValue()%></b>
										&nbsp;
										<%=joinDef.getTableDefinition().writeSqlSelect()%>
										&nbsp;
										<b>ON</b>
										<%=joinDef.getJoinGroup().writeSqlSelect()%>
									</div>
<%
		}
	}
%>									</div>
									<div>
										<b>WHERE</b>
									</div>
<%
					if (!JadeStringUtil.isEmpty(request.getParameter("id")) && !"null".equals(request.getParameter("id"))) {
						JadeAbstractModel model = null;

						try {
							model = (JadeAbstractModel) Class.forName(modelClassName).newInstance();
							model.setId(request.getParameter("id"));
						} catch(Exception e) {
							//Ne fait rien, pas grâve
						}
%>									<div style="padding-left: 10px;">
										<%=modelDefinition.writeSqlWhere(model)%>
									</div>
<%
					} else if (!JadeStringUtil.isEmpty(request.getParameter("searchModelClass")) && !"null".equals(request.getParameter("searchModelClass"))) {
						//Va récupérer les champs concernés :
						String searchModelClassName = request.getParameter("searchModelClass");
						JadeAbstractSearchModel searchModel = null;

						try {
							Class searchModelClass = Class.forName(searchModelClassName);
							if (JadeAbstractSearchModel.class.isAssignableFrom(searchModelClass)) {
								searchModel = (JadeAbstractSearchModel) searchModelClass.newInstance();
							}
						} catch(Exception e) {
							//Comme d'hab'
						}

						if (searchModel != null) {
							JSPUtils.setBeanProperties(request, searchModel);
%>									<div style="padding-left: 10px;">
										<%=modelDefinition.writeSqlWhere(searchModel)%>
									</div>
<%
						} else {
%>									<div style="color: red;">
										The model class defined is not an instance of <i>JadeAbstractSearchModel</i>, it's not possible to generate SQL code.
									</div>
<%
						}
					} else {
%>									<div style="color: red;">
										Define id's value or the search model class to use !
									</div>
<%
				}
%>								</td>
							</tr>
						</table>
					</div>
<%
			}
		}
	}
%>		</div>
	</body>
</html>
