<?html version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.jade.persistence.mapping.JadeModelMappingProvider"%>
<%@ page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@ page import="globaz.jade.persistence.model.JadeAccessModel"%>
<%@ page import="globaz.jade.persistence.model.JadeSimpleModel"%>
<%@ page import="globaz.jade.persistence.sql.JadeAbstractSqlModelDefinition"%>
<%@ page import="globaz.jade.persistence.sql.JadeSqlComplexFromDefinition"%>
<%@ page import="globaz.jade.persistence.sql.JadeSqlComplexModelDefinition"%>
<%@ page import="globaz.jade.persistence.sql.JadeSqlFieldDefinition"%>
<%@ page import="globaz.jade.persistence.sql.JadeSqlFromDefinition"%>
<%@ page import="globaz.jade.persistence.sql.JadeSqlJoinDefinition"%>
<%@ page import="globaz.jade.persistence.sql.JadeSqlOrderDefinition"%>
<%@ page import="globaz.jade.persistence.sql.JadeSqlOrderEntryDefinition"%>
<%@ page import="globaz.jade.persistence.sql.JadeSqlModelDefinition"%>
<%@ page import="globaz.jade.persistence.sql.JadeSqlSearchGroupDefinition"%>
<%@ page import="globaz.jade.persistence.sql.JadeSqlWhereDefinition"%>
<%@ page import="globaz.jade.persistence.util.JadePersistenceUtil"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.TreeMap"%>

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
			JADE Persistence Manager - Description of a model
		</div>
		<div style="text-align: right; padding-right: 20px;">
			<a href="persistence?jaction=jade.persistence.display.models">
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
%>			<table cellpadding="0" cellspacing="0">
				<tr>
					<th colspan="2">
						Model description for model : <%=modelClassName%>
					</th>
				</tr>
<%
			if (modelDefinition instanceof JadeSqlModelDefinition) {
				JadeSqlModelDefinition jadeDefinition= (JadeSqlModelDefinition)modelDefinition;
%>				<tr>
					<td colspan="2">
						Description : <%=modelDefinition.getSqlFromDefinition().getTableDefinition().getDescription() %>
					</td>
				</tr>
				<tr>
					<td style="width: 50%; text-align: center; vertical-align: top;">
						<table cellpadding="0" cellspacing="0">
							<tr>
								<th colspan="2">
									Table description
								</th>
							</tr>
							<tr>
								<th>
									Parameter
								</th>
								<th>
									Value
								</th>
							</tr>
							<tr>
								<td>
									SQL Table name
								</td>
								<td>
									<%=jadeDefinition.getTableDefinition().getTableName()%>
								</td>
							</tr>
							<tr>
								<td class="pair">
									Table alias
								</td>
								<td class="pair">
									<%=jadeDefinition.getTableDefinition().getSqlAlias()%>
								</td>
							</tr>
							<tr>
								<td>
									Has spy
								</td> 
								<td>
									<%=jadeDefinition.isHasSpy()%>
								</td>
							</tr>
							<tr>
								<td class="pair">
									Has creation spy
								</td>
								<td class="pair">
									<%=jadeDefinition.isHasCreationSpy()%>
								</td>
							</tr>
							<tr>
								<td>
									Always increment primary key
								</td>
								<td>
									<%=jadeDefinition.isAlwaysIncrementPrimaryKey()%>
								</td>
							</tr>
						</table>
					</td>
					<td style="width: 50%; text-align: center; vertical-align: top;">
						<table cellpadding="0" cellspacing="0">
							<tr>
								<th colspan="6">
									Fields description
								</th>
							</tr>
							<tr>
								<th>
									Variable name in model class
								</th>
								<th>
									SQL Column name
								</th>
								<th>
									SQL Alias
								</th>
								<th>
									SQL Column type
								</th>
								<th>
									SQL Data type
								</th>
								<th>
									Mandatory
								</th>
							</tr>
<%
				boolean pair = false;
				Iterator it = jadeDefinition.getFieldsDefinition().getFieldDefinitions();
				while (it.hasNext()) {
					JadeSqlFieldDefinition field = (JadeSqlFieldDefinition) it.next();
%>							<tr>
								<td<%=pair ? " class=\"pair\"" : ""%>>
									<%=field.getAttributeName()%>
								</td>
								<td<%=pair ? " class=\"pair\"" : ""%>>
									<%=field.getSqlColumnName()%>
								</td>
								<td<%=pair ? " class=\"pair\"" : ""%>>
									<%=field.getSqlAlias()%>
								</td>
								<td<%=pair ? " class=\"pair\"" : ""%>>
									<%=field.getFieldType().getType()%>
								</td>
								<td<%=pair ? " class=\"pair\"" : ""%>>
									<%=field.getDataType()%>
								</td>
								<td<%=pair ? " class=\"pair\"" : ""%>>
									<%=field.getMandatory().booleanValue()?"YES":"NO"%>
								</td>
							</tr>
							<tr>
								<td colspan="6"<%=pair ? " class=\"pair\"" : ""%>>
									<i><%=field.getComment()%></i>
								</td>
							</tr>
<%
					pair = !pair;
				}
%>						</table>
					</td>
				</tr>
<%
				if (jadeDefinition.isMapFullTable()) {
%>				<tr>
					<td colspan="2">
						<table cellpadding="0" cellspacing="0">
							<tr>
								<th colspan="2">
									Create table SQL code
								</th>
							</tr>
							<tr>
								<td id="SQL">
<%
					request.setAttribute("toUserModelDefinition", jadeDefinition);
					request.setAttribute("toUseClassName", modelClassName);
%>									<jsp:include page="innerCreateTable.jsp" />
								</td>
							</tr>
						</table>
					</td>
				</tr>
<%
				}
			} else if (modelDefinition instanceof JadeSqlComplexModelDefinition) {
				JadeSqlComplexModelDefinition jadeDefinition = (JadeSqlComplexModelDefinition) modelDefinition;
				HashMap tablesMapping = new HashMap();

				// Récupère les classes définies comme variables dans le modèle complexe 
				try{
					Class modelComplexeClass = Class.forName(modelClassName);
					// Récupère tous les champs
					for (int i = 0; i < modelComplexeClass.getMethods().length; i++) {
						if (JadeSimpleModel.class.isAssignableFrom(modelComplexeClass.getMethods()[i].getReturnType())) {
							// Va récupérer la définition du modèle
							JadeSqlModelDefinition current = (JadeSqlModelDefinition ) JadeModelMappingProvider.getInstance().getSqlModelDefinition(modelComplexeClass.getMethods()[i].getReturnType().getName());
							if (current != null) {
								tablesMapping.put(current.getTableDefinition().getTableName(), modelComplexeClass.getMethods()[i].getReturnType().getName());
							}
						}
					}
				} catch(Exception e){
					//Tant pis...
				}
%>				<tr>
					<td colspan="2">
						<table cellpadding="0" cellspacing="0">
							<tr>
								<th colspan="2">
									From description
								</th>
							</tr>
							<tr>
								<td>
									FROM
								</td>
								<td>
									<table cellpadding="0" cellspacing="0">
										<tr>
											<th>
												Simple model
											</th>
											<th>
												Table name
											</th>
											<th>
												Table alias
											</th>
											<th>
												&nbsp;
											</th>
										</tr>
										<tr>
											<td>
<%
				String className = (String) tablesMapping.get(jadeDefinition.getSqlFromDefinition().getTableDefinition().getTableName());
%>												<%=!JadeStringUtil.isEmpty(className) && className.lastIndexOf(".") > -1 ? className.substring(className.lastIndexOf(".") + 1) : "&nbsp;"%>
											</td>
											<td>
												<%=jadeDefinition.getSqlFromDefinition().getTableDefinition().getTableName()%>
											</td>
											<td>
												<%=jadeDefinition.getSqlFromDefinition().getTableDefinition().getSqlAlias()%>
											</td>
											<td style="text-align: right; padding-right: 3px;">
												<%=!JadeStringUtil.isEmpty(className) ? "<a href=\"persistence?jaction=jade.persistence.display.description&model=" + className + "\">Show model</a>" : ""%>
											</td>
										</tr>
									</table>
								</td>
							</tr>
<%
				boolean pair =true;
				for (Iterator<JadeSqlJoinDefinition> iterator = ((JadeSqlComplexFromDefinition) jadeDefinition.getSqlFromDefinition()).getJoinDefinitions().iterator(); iterator.hasNext(); ) {
					JadeSqlJoinDefinition currentDef = iterator.next();
%>							<tr>
								<td<%=pair ? " class=\"pair\"" : ""%> style="vertical-align: top;">
									&nbsp;-&nbsp;<%=currentDef.getJoinType().getSqlValue()%>
								</td>
								<td<%=pair ? " class=\"pair\"" : ""%> style="vertical-align: top;">
									<div>
										<table cellpadding="0" cellspacing="0">
											<tr>
												<th>
													Simple model
												</th>
												<th>
													Table name
												</th>
												<th>
													Table alias
												</th>
												<th>
													&nbsp;
												</th>
											</tr>
											<tr>
												<td>
<%
					className = (String) tablesMapping.get(currentDef.getTableDefinition().getTableName());
%>													<%=!JadeStringUtil.isEmpty(className) && className.lastIndexOf(".") > -1 ? className.substring(className.lastIndexOf(".") + 1) : "&nbsp;"%>
												</td>
												<td>
													<%=currentDef.getTableDefinition().getTableName()%>
												</td>
												<td>
													<%=currentDef.getTableDefinition().getSqlAlias()%>
												</td>
												<td style="text-align: right; padding-right: 3px;">
													<%=!JadeStringUtil.isEmpty(className) ? "<a href=\"persistence?jaction=jade.persistence.display.description&model=" + className + "\">Show model</a>" : ""%>
												</td>
											</tr>
										</table>
									</div>
									<div>
										<%request.setAttribute("joinGroup", currentDef.getJoinGroup());%>
										<jsp:include page="innerJoinGroupPage.jsp"></jsp:include>
									</div>
								</td>
							</tr>
<%
					pair = !pair;
				}
%>						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<table cellpadding="0" cellspacing="0">
							<tr>
								<th colspan="5">
									Fields description
								</th>
							</tr>
							<tr>
								<th>
									Variable name in model class
								</th>
								<th>
									SQL Column name
								</th>
								<th>
									SQL Alias
								</th>
								<th>
									SQL Column type
								</th>
								<th>
									Remarks
								</th>
							</tr>
<%
				pair = false;
				Iterator it = jadeDefinition.getFieldsDefinition().getFieldDefinitions();
				TreeMap fieldContainer = new TreeMap();
				while (it.hasNext()) {
					JadeSqlFieldDefinition field = (JadeSqlFieldDefinition) it.next();
					fieldContainer.put(field.getAttributeName(), field);
				}
				it = fieldContainer.keySet().iterator();
				while (it.hasNext()) {
					JadeSqlFieldDefinition field = (JadeSqlFieldDefinition) fieldContainer.get(it.next());
%>							<tr>
								<td<%=pair ? " class=\"pair\"" : ""%>>
									<%=field.getAttributeName()%>
								</td>
								<td<%=pair ? " class=\"pair\"" : ""%>>
									<%=field.getSqlColumnName()%>
								</td>
								<td<%=pair ? " class=\"pair\"" : ""%>>
									<%=field.getSqlAlias()%>
								</td>
								<td<%=pair ? " class=\"pair\"" : ""%>>
									<%=field.getFieldType().getType()%>
								</td>
								<td<%=pair ? " class=\"pair\"" : ""%>>
									<%=field.getComment()%>
								</td>
							</tr>
<%
					pair =!pair;
				}
%>						</table>
					</td>
				</tr>
<%
			}
%>				<tr>
					<td colspan="2" style="width: 100%; text-align: center; vertical-align: top;">
<%
			Iterator it = modelDefinition.getWhereDefinitionKeys();
			while (it.hasNext()) {
				String currentKey = (String) it.next();
				JadeSqlWhereDefinition whereDefinition = modelDefinition.getWhereDefinition(currentKey); 
				if (whereDefinition != null) {
%>						<table cellpadding="0" cellspacing="0" border="0">
							<tr>
								<th>
									Search description : <i>current</i>
								</th>
							</tr>
<%
					request.setAttribute("searchGroup", whereDefinition.getSearchGroupe());
%>							<tr>
								<td>
									<jsp:include page="innerSearchGroupePage.jsp"></jsp:include>
								</td>
							</tr>
							</table>
<%
				}
			}
%>						</td>
					</tr>
					<tr>
						<td colspan="2" style="width: 100%; text-align: center; vertical-align: top;">
<%
			it = modelDefinition.getOrderDefinitionKeys();
			boolean pair = false;
			while (it.hasNext()) {
				String currentKey = (String) it.next();
				JadeSqlOrderDefinition orderDefinition = modelDefinition.getOrderDefinition(currentKey);
%>							<table cellpadding="0" cellspacing="0">
								<tr>
									<th colspan="3">
										Order description : <i>current</i>
									</th>
								</tr>
								<tr>
									<th>
										Position
									</th>
									<th>
										Field name
									</th>
									<th>
										Order typ
									</th>
								</tr>
<%
				Iterator orderEntriesIt = orderDefinition.getOrderEntries();
					while (orderEntriesIt.hasNext()) {
						JadeSqlOrderEntryDefinition orderEntry = (JadeSqlOrderEntryDefinition) orderEntriesIt.next();
%>								<tr>
									<td<%=pair ? " class=\"pair\"" : ""%>>
										<%=orderEntry.getPosition()%>
									</td>
									<td<%=pair ? " class=\"pair\"" : ""%>>
										<%=orderEntry.getField().getAttributeName() + " - " + orderEntry.getField().getSqlAlias()%>
									</td>
									<td<%=pair ? " class=\"pair\"" : ""%>>
										<%=orderEntry.getOrderType().getOrder()%>
									</td>
								</tr>
<%
						pair = !pair;
					}
%>							</table>
<%
				}
%>						</td>
					</tr>
					<tr>
						<td colspan="2">
							<table cellpadding="0" cellspacing="0">
								<tr>
									<th colspan="2">
										Select query with primary key
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
%>									<div style="padding-left: 10px;">
										<div>
											<%=fromDef.getTableDefinition().writeSqlSelect()%>
										</div>
<%
				if (fromDef instanceof JadeSqlComplexFromDefinition) {
					JadeSqlComplexFromDefinition cFromDef = (JadeSqlComplexFromDefinition) fromDef;
					for (Iterator<JadeSqlJoinDefinition> iterator = cFromDef.getJoinDefinitions().iterator(); iterator.hasNext(); ) {
						JadeSqlJoinDefinition joinDef = iterator.next();
%>										<div style="padding-left: 10px;">
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
									<div style="padding-left: 10px;">
										<%=modelDefinition.getFieldsDefinition().getPrimaryKey().getSqlFieldName() + "= ? "%>
									</div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
<%
			}
		} else {
%>			<span>
				No model class name passed in parameter
			</span>
<%
		}
%>		</div>
	</body>
</html>
