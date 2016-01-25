<%@page import="globaz.jade.persistence.sql.JadeSqlModelDefinition"%>
<%@page import="globaz.jade.persistence.util.JadePersistenceUtil"%>
<%@page import="globaz.jade.persistence.sql.JadeSqlFieldDefinition"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="java.util.Iterator"%><%
JadeSqlModelDefinition jadeDefinition = (JadeSqlModelDefinition) request.getAttribute("toUserModelDefinition");
String className = (String)  request.getAttribute("toUseClassName");
%>
<div><i>--<%for(int i=0; i<120; i++){%>=<%}%></i></div>
<div><i>-- SQL Script for create table (<%=jadeDefinition.getTableDefinition().getTableName()%>)</i></div>
<div><i>-- Simple model's class name : (<%=className%>)</i></div>
<div><i>--<%for(int i=0; i<120; i++){%>=<%}%></i></div>
<div><b>DROP TABLE <%=JadePersistenceUtil.getDbSchema()%>.<%=jadeDefinition.getSqlFromDefinition().getTableDefinition().getTableName()%></b>;</div>
<div><b>CREATE TABLE <%=JadePersistenceUtil.getDbSchema()%>.<%=jadeDefinition.getSqlFromDefinition().getTableDefinition().getTableName()%></b></div>
<div>(</div>
<div style="padding-left: 20px;">
	<%Iterator fieldsIt = jadeDefinition.getFieldsDefinition().getFieldDefinitions();%>
	<table cellpadding="0" cellspacing="0" style="padding: none; border: none; width: auto;">
		<tr>
			<td style="padding-left: 5px; padding-right: 5px; padding-top: 0px; padding-bottom: 0px;"><%=jadeDefinition.getFieldsDefinition().getPrimaryKey().getSqlColumnName()%>&nbsp;</td>
			<td style="padding-left: 5px; padding-right: 5px;padding-top: 0px; padding-bottom: 0px;"><%=jadeDefinition.getFieldsDefinition().getPrimaryKey().getDataType()%>&nbsp;</td>
			<td style="padding-left: 5px; padding-right: 5px;padding-top: 0px; padding-bottom: 0px;"><%=jadeDefinition.getFieldsDefinition().getPrimaryKey().getMandatory().booleanValue()?"NOT NULL":""%>,</td>
		</tr>
		<%while( fieldsIt.hasNext()){
			JadeSqlFieldDefinition field = (JadeSqlFieldDefinition) fieldsIt.next(); 
		%>
			<tr>
				<td style="padding-left: 5px; padding-right: 5px; padding-top: 0px; padding-bottom: 0px;"><%=field.getSqlColumnName()%>&nbsp;</td>
				<td style="padding-left: 5px; padding-right: 5px;padding-top: 0px; padding-bottom: 0px;"><%=field.getDataType()%>&nbsp;</td>
				<td style="padding-left: 5px; padding-right: 5px;padding-top: 0px; padding-bottom: 0px;"><%=field.getMandatory().booleanValue()?"NOT NULL":""%>,</td>
			</tr>
		<%}%>
		<tr>
			<td style="padding-left: 5px; padding-right: 5px;padding-top: 0px; padding-bottom: 0px;" colspan="3"><b>PRIMARY KEY(<%=jadeDefinition.getFieldsDefinition().getPrimaryKey().getSqlColumnName()%>)</b></td>
		</tr>
	</table>
</div>
<div>);</div>
<div>COMMENT <b>ON TABLE</b> <%=JadePersistenceUtil.getDbSchema()%>.<%=jadeDefinition.getSqlFromDefinition().getTableDefinition().getTableName()%> is '<i><%=JadeStringUtil.change(jadeDefinition.getSqlFromDefinition().getTableDefinition().getDescription(), "'", "''")%></i>';</div>
<div>COMMENT <b>ON COLUMN</b> <%=JadePersistenceUtil.getDbSchema()%>.<%=jadeDefinition.getFieldsDefinition().getPrimaryKey().getSqlFieldName()%> is '<i><%=JadeStringUtil.change(jadeDefinition.getFieldsDefinition().getPrimaryKey().getComment(), "'", "''")%></i>';</div>
<%fieldsIt = jadeDefinition.getFieldsDefinition().getFieldDefinitions();%>
<%while( fieldsIt.hasNext()){
	JadeSqlFieldDefinition field = (JadeSqlFieldDefinition) fieldsIt.next();%>
	<div>COMMENT <b>ON COLUMN</b> <%=JadePersistenceUtil.getDbSchema()%>.<%=field.getSqlFieldName()%> is '<i><%=JadeStringUtil.change(field.getComment(), "'", "''")%></i>';</div>
<%}%>
