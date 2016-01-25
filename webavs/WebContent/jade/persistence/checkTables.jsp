<?html version="1.0" encoding="ISO-8859-1" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@page import="globaz.jade.servlet.http.JadeHttpPersitenceServlet"%><html>
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
<title>JADE Persistence check tables</title>
</head>
<body>
	<div class="title">JADE Persistence Manager - Check tables</div>
	<div style="text-align: right; padding-right: 20px;"><a href="<%=request.getContextPath()%>/persistence">All models</a></div>
	<div>
		<table cellpadding="0" cellspacing="0">
			<tr><th>Check tables</th></tr>
		<%String[] allParams = request.getParameterValues("className");
		Connection jdbcConnection = null;
		try{
			jdbcConnection =  JadeJdbcDriverManager.getInstance().getConnection(Jade.getInstance().getDefaultJdbcUrl());
			String JDBCMajorMinor = "unimplemented";
			try {
				JDBCMajorMinor = jdbcConnection.getMetaData().getJDBCMajorVersion() + ", " + jdbcConnection.getMetaData().getJDBCMinorVersion();
			} catch (Throwable t) {
				// tant pis
			}
			%>
			<tr><td>
					<div>
						<table cellpadding="0" cellspacing="0" border="0">
							<tr>
								<th style="width: 100%;" colspan="10">DB Definition</th>
							</tr>
							<tr>
								<th>DB Product name</th>
								<th>DB Product version</th>
								<th>Driver name</th>
								<th>Driver Major version</th>
								<th>Driver version</th>
								<th>JDBC Driver version</th>
								<th>User name</th>
								<th>Connection URL</th>
							</tr>
							<tr>
								<td><%=jdbcConnection.getMetaData().getDatabaseProductName()%></td>
								<td><%=jdbcConnection.getMetaData().getDatabaseProductVersion()%></td>
								<td><%=jdbcConnection.getMetaData().getDriverMajorVersion()+", "+jdbcConnection.getMetaData().getDriverMinorVersion()%></td>
								<td><%=jdbcConnection.getMetaData().getDriverName()%></td>
								<td><%=jdbcConnection.getMetaData().getDriverVersion()%></td>
								<td><%=JDBCMajorMinor%></td>
								<td><%=jdbcConnection.getMetaData().getUserName()%></td>
								<td><%=jdbcConnection.getMetaData().getURL()%></td>
							</tr>
						</table>
					</div>
				</td></tr>
				<tr>
					<td>
<%			if(allParams!=null){
			for(int i=0; i<allParams.length; i++){
				ResultSet rs = null;
				try{
				JadeAbstractSqlModelDefinition modelDefinition = JadeModelMappingProvider.getInstance().getSqlModelDefinition(allParams[i]);
				if(modelDefinition!=null && modelDefinition instanceof JadeSqlModelDefinition){
					JadeSqlModelDefinition jadeDefinition = (JadeSqlModelDefinition) modelDefinition;%>
				<div style="text-align: left;">
					<div>
						<table cellpadding="0" cellspacing="0" border="0">
							<tr>
								<th colspan="4">- Check table (<%=jadeDefinition.getTableDefinition().getTableName()%>)</th>
							</tr>
							<tr>
								<th colspan="4">- Simple model's class name : (<%=allParams[i]%>)</th>
							</tr>
<%							if(!jadeDefinition.getTableDefinition().getTableName().matches("[A-Z0-9]*")){%>
							<tr>
								<th colspan="4">
									<div style="color:red; text-align: left;"><b>- The table name must contains only uppercase or numeric characters!</b></div> 
								</th>
							</tr>
							<%} 
							if(jadeDefinition.getTableDefinition().getTableName().length()>8){
							%>
							<tr>
								<th colspan="4">
									<div style="color:red; text-align: left;"><b>- The table name must be 8 character length maximum!</b></div> 
								</th>
							</tr>
							<%} 
							if(JadeHttpPersitenceServlet.DB2_RESERVED_WORDS.contains(jadeDefinition.getTableDefinition().getTableName().trim())){
							%>
							<tr>
								<th colspan="4">
									<div style="color:red; text-align: left;"><b>- The table name defined is in the DB2 reserved words!</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="http://publib.boulder.ibm.com/infocenter/db2luw/v8/index.jsp?topic=/com.ibm.db2.udb.doc/admin/r0001095.htm" target="new">Check the DB2 reserved words here</a></div> 
								</th>
							</tr>

							<%}%> 
							<tr>
								<th style="width: 15%;" colspan="2">Schema</th>
								<th style="width: 15%;">Table name</th>
								<th style="width: 70%;">Remarks</th>
							</tr>
				<%
				rs = jdbcConnection.getMetaData().getTables(null, null, jadeDefinition.getTableDefinition().getTableName(), null);
				boolean hasResult = false;
				boolean mainPair = false;
				while(rs.next()){
					hasResult=true;
					String schema=rs.getString("TABLE_SCHEM");
				%>
					<tr>
						<td <%=mainPair?" class=\"pair\"":""%> colspan="2"><%=Jade.getInstance().getDefaultJdbcSchema().equalsIgnoreCase(schema)?schema:"<span style=\"color: #ff9b00;\"><b>Check schema ! ("+schema+")</b></span>"%></td>
						<td <%=mainPair?" class=\"pair\"":""%>><%=jadeDefinition.getTableDefinition().getTableName()%></td>
						<td <%=mainPair?" class=\"pair\"":""%>><%=JadeStringUtil.isEmpty(rs.getString("REMARKS"))?"<span style=\"color: #ff9b00;\"><b>No remark defined for this table!</b></span>":rs.getString("REMARKS")%></td>
					</tr>
<%						

							ResultSet pkRs = null;
							ArrayList primaryKeys = new ArrayList();
							try{
								pkRs = jdbcConnection.getMetaData().getPrimaryKeys(null, schema, jadeDefinition.getTableDefinition().getTableName());
								while(pkRs.next()){
									primaryKeys.add(pkRs.getString("COLUMN_NAME"));
								}
							} finally {
								if(pkRs!=null){
									try{
										pkRs.close();
									} catch(Exception e){
										//Ne fait rien
									}
								}
							}
							if(primaryKeys.size()==0){%>
							<tr>
								<td <%=mainPair?" class=\"pair\"":""%> colspan="4">
									<div style="color:red; text-align: left;"><b>No primary key defined in DB for the table (<%=jadeDefinition.getTableDefinition().getTableName()%>)</b></div>
								</td>
							</tr>
							<%} else if(primaryKeys.size()>1){ %>
							<tr>
								<td <%=mainPair?" class=\"pair\"":""%> colspan="4">
								<div style="color:red; text-align: left;"><b>Too many primary keys defined in DB for the table (<%=jadeDefinition.getTableDefinition().getTableName()%>)</b></div>
								<%Iterator pkIt = primaryKeys.iterator();
								while(pkIt.hasNext()){%>
									<div style="padding-left: 10px; text-align: left;"> - Primary key defined : <%=pkIt.next() %> </div> 
								<%} %>
								</td>
							</tr>
							<%} else if(!jadeDefinition.getPrimaryKey().getSqlColumnName().equalsIgnoreCase((String)primaryKeys.get(0))) {%>
							<tr>
								<td <%=mainPair?" class=\"pair\"":""%> colspan="4">
								<div style="color:red; text-align: left;"><b>Invalid primary key defined between DB and configuration for the table (<%=jadeDefinition.getTableDefinition().getTableName()%>)</b></div>
								<div style="padding-left: 10px; text-align: left;"> - Primary key defined in configuration : <%=jadeDefinition.getPrimaryKey().getSqlColumnName()%> </div> 
								<div style="padding-left: 10px; text-align: left;"> - Primary key defined in DB : <%=primaryKeys.get(0)%> </div> 
								</td>
							</tr>
							<%} %>
					<tr>
						<td <%=mainPair?" class=\"pair\"":""%> style="width: 5%;">&nbsp;</td>
						<td <%=mainPair?" class=\"pair\"":""%> colspan="3">
							<%
							Iterator it = jadeDefinition.getFieldsDefinition().getFieldDefinitions();
							ResultSet colRs = null;
							HashMap columnContainer = new HashMap();
					 		try{
								colRs = jdbcConnection.getMetaData().getColumns(null, schema, jadeDefinition.getTableDefinition().getTableName(), null);
								ResultSetMetaData md = colRs.getMetaData();
								while(colRs.next()){
									HashMap columnDef = new HashMap();
									for (int j = 1; j <= md.getColumnCount(); j++) { 									
										columnDef.put(md.getColumnName(j), colRs.getString(md.getColumnName(j)));
									}
									columnContainer.put(colRs.getString("COLUMN_NAME"), columnDef);
								}
							} finally {
								if(colRs!=null){
									try{
										colRs.close();
									} catch(Exception e){
										//Ne fait rien
									}
								}
							}%>
							<div><table cellpadding="0" cellspacing="0" border="0">
								<tr>
									<th style="width: 2%">&nbsp;</th>
									<th style="width: 15%">Java field name</th>
									<th style="width: 10%">Column name</th>
 									<th style="width: 10%">Configured SQL type</th>
 									<th style="width: 10%">DB SQL type</th>
									<th style="width: 5%">Conf. mandatory</th>
									<th style="width: 5%">DB mandatory</th>
 									<th style="width: 40%">Remark</th>
								</tr>
<%
							ArrayList treatedColumns = new ArrayList();
							JadeSqlFieldDefinition pk = jadeDefinition.getPrimaryKey();
							treatedColumns.add(pk.getSqlColumnName());
							request.setAttribute("columnDef", columnContainer.get(pk.getSqlColumnName()));
							request.setAttribute("fieldDefinition", pk);
							request.setAttribute("isPk", Boolean.TRUE);
							request.setAttribute("pair", Boolean.FALSE);%>
								<jsp:include page="innerColumnCheck.jsp"></jsp:include>
<%							Iterator fieldIt = jadeDefinition.getFieldsDefinition().getFieldDefinitions();
							Boolean pair = Boolean.TRUE;
							while(fieldIt.hasNext()){
								JadeSqlFieldDefinition field = (JadeSqlFieldDefinition) fieldIt.next();
								treatedColumns.add(field.getSqlColumnName());
								request.setAttribute("columnDef", columnContainer.get(field.getSqlColumnName()));
								request.setAttribute("fieldDefinition", field);
								request.setAttribute("isPk", Boolean.FALSE);
								request.setAttribute("pair", pair);%>
								<jsp:include page="innerColumnCheck.jsp"></jsp:include>
							<%pair = new Boolean(!pair.booleanValue()); }
							Iterator colIt = columnContainer.keySet().iterator();
							while(colIt.hasNext()){
								String columnName = (String) colIt.next();
								if(!treatedColumns.contains(columnName)){
									request.setAttribute("columnDef", columnContainer.get(columnName));
									request.setAttribute("fieldDefinition", null);
									request.setAttribute("isPk", Boolean.FALSE);
									request.setAttribute("pair", pair);
							%>
								<jsp:include page="innerColumnCheck.jsp"></jsp:include>
							<%pair = new Boolean(!pair.booleanValue()); } }%>	
							</table></div>
						</td>
					</tr>
				<%mainPair=!mainPair;}%>
					</table>
					</div>
				</div>
			<%}
				} catch(Exception e){%>
				<div>Problem to handle check table. Reason : <%=e.toString() %></div>
				<%
				} finally {
					if(rs!=null){
						try{
							rs.close();
						} catch(Exception e){
							//Ne fait rien
						}
					}
				}
				
			} } 
			} finally {
			if(jdbcConnection!=null){
				try{
					jdbcConnection.rollback();
				} catch(Exception e){
					//Ne fait rien
				}
				try{
					jdbcConnection.close();
				} catch(Exception e){
					//Ne fait rien
				}
			}
		}%>			
			</td></tr></table>
	</div>
</body>

<%@page import="globaz.jade.persistence.sql.JadeSqlModelDefinition"%>
<%@page import="globaz.jade.persistence.sql.JadeAbstractSqlModelDefinition"%>
<%@page import="java.sql.Connection"%>
<%@page import="globaz.jade.jdbc.JadeJdbcDriverManager"%>
<%@page import="globaz.jade.common.Jade"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.ResultSetMetaData"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashMap"%>
<%@page import="globaz.jade.persistence.sql.JadeSqlFieldDefinition"%>
<%@page import="java.util.ArrayList"%></html> 