<%@page import="globaz.jade.persistence.sql.JadeSqlFieldDefinition"%>
<%@page import="java.util.HashMap"%><%
HashMap columnDef = (HashMap) request.getAttribute("columnDef");
JadeSqlFieldDefinition fieldDef = (JadeSqlFieldDefinition) request.getAttribute("fieldDefinition");
Boolean pair = (Boolean)request.getAttribute("pair");
Boolean isPk = (Boolean)request.getAttribute("isPk");
 %>

<%@page import="globaz.jade.persistence.sql.JadeSqlFieldType"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.servlet.http.JadeHttpPersitenceServlet"%><tr>
<%if(isPk.booleanValue()){ %>
	<td <%=pair.booleanValue()?" class=\"pair\"":""%>><i>PK</i></td>
<%} else { %>
	<td <%=pair.booleanValue()?" class=\"pair\"":""%>>&nbsp;</td>
<%} %>
<%if(columnDef==null && fieldDef!=null){ %>
	<td colspan="7"<%=pair.booleanValue()?" class=\"pair\"":""%> style="color: red;"><b>The defined column (<%=fieldDef.getAttributeName()+" - "+fieldDef.getSqlColumnName()%>) is not defined in database!</b></td>
<%} else if(columnDef!=null && fieldDef==null){%>
	<td colspan="7"<%=pair.booleanValue()?" class=\"pair\"":""%> style="color: red;"><b>The defined column (<%=columnDef.get("COLUMN_NAME")%>) is defined in database but not referenced in the model!</b></td>
<%} else {
	boolean isValidType = fieldDef.getFieldType().isValidJavaSqlType(Integer.parseInt((String) columnDef.get("DATA_TYPE")));
	String definedSize = fieldDef.getDataType();
	String decimalDigit="";
	if(definedSize.indexOf("(")>-1)
		definedSize = definedSize.substring(definedSize.indexOf("(")+1);
	if(definedSize.indexOf(")")>-1)
		definedSize = definedSize.substring(0, definedSize.indexOf(")"));
	if(definedSize.indexOf(",")>-1){
		decimalDigit=definedSize.substring(definedSize.indexOf(",")+1);
		definedSize=definedSize.substring(0, definedSize.indexOf(","));
	}
	boolean isColumnSizeCorrect=definedSize.equals(columnDef.get("COLUMN_SIZE"));
	boolean isDecimalDigitCorrect=JadeSqlFieldType.STRING.equals(fieldDef.getFieldType()) || decimalDigit.equals(columnDef.get("DECIMAL_DIGITS"));
	
	boolean isMandatoryInDb = "0".equals((String) columnDef.get("NULLABLE"));
%>
	<td <%=pair.booleanValue()?" class=\"pair\"":""%>><%=fieldDef.getAttributeName()%></td>
	<td <%=pair.booleanValue()?" class=\"pair\"":""%>><%=fieldDef.getSqlColumnName()%></td>
	<td <%=pair.booleanValue()?" class=\"pair\"":""%> <%=isColumnSizeCorrect&&isDecimalDigitCorrect&&isValidType?"":" style=\"color: red;\""%>><b><%=fieldDef.getDataType()%></b></td>
	<td <%=pair.booleanValue()?" class=\"pair\"":""%> <%=isColumnSizeCorrect&&isDecimalDigitCorrect&&isValidType?"":" style=\"color: red;\""%>><b><%=columnDef.get("TYPE_NAME")+"("+columnDef.get("COLUMN_SIZE")%><%=!"null".equals(columnDef.get("DECIMAL_DIGITS")) && !JadeStringUtil.isEmpty((String)columnDef.get("DECIMAL_DIGITS"))?","+columnDef.get("DECIMAL_DIGITS")+")":")"%></b></td>
	<td <%=pair.booleanValue()?" class=\"pair\"":""%>><span <%=fieldDef.getMandatory().booleanValue()==isMandatoryInDb?"":"style=\"color: red;\""%>><%=fieldDef.getMandatory().booleanValue()?"YES":"NO"%></span></td>
	<td <%=pair.booleanValue()?" class=\"pair\"":""%>><span <%=fieldDef.getMandatory().booleanValue()==isMandatoryInDb?"":"style=\"color: red;\""%>><%=isMandatoryInDb?"YES":"NO"%></span></td>
	<td <%=pair.booleanValue()?" class=\"pair\"":""%>><%=JadeStringUtil.isEmpty((String) columnDef.get("REMARKS"))?"<span style=\"color: #ff9b00;\"><b>No remark defined for this column!</b></span>":columnDef.get("REMARKS")%></td> 
<%}%>
</tr>

<%if(fieldDef!=null && !fieldDef.getSqlColumnName().matches("[A-Z0-9]*")){%>
<tr>
	<td <%=pair.booleanValue()?" class=\"pair\"":""%>>&nbsp;</td> 
	<td <%=pair.booleanValue()?" class=\"pair\"":""%> colspan="7">
		<div style="color:red; text-align: left;"><b>The column name (<%=fieldDef.getSqlColumnName()%>) must contains only uppercase or numeric characters!</b></div> 
	</td>
</tr>
<%} %>
<%if(fieldDef!=null && fieldDef.getSqlColumnName().length()>8){%>
<tr>
	<td <%=pair.booleanValue()?" class=\"pair\"":""%>>&nbsp;</td> 
	<td <%=pair.booleanValue()?" class=\"pair\"":""%> colspan="7">
		<div style="color:red; text-align: left;"><b>The column name (<%=fieldDef.getSqlColumnName()%>) must be 8 character length maximum!</b></div> 
	</td>
</tr>
<%} %>
<%if(fieldDef!=null && JadeHttpPersitenceServlet.DB2_RESERVED_WORDS.contains(fieldDef.getSqlColumnName())){%>
<tr>
	<td <%=pair.booleanValue()?" class=\"pair\"":""%>>&nbsp;</td> 
	<td <%=pair.booleanValue()?" class=\"pair\"":""%> colspan="7">
		<div style="color:red; text-align: left;"><b>The column name (<%=fieldDef.getSqlColumnName()%>) defined is in the DB2 reserved words!</b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="http://publib.boulder.ibm.com/infocenter/db2luw/v8/index.jsp?topic=/com.ibm.db2.udb.doc/admin/r0001095.htm" target="new">Check the DB2 reserved words here...</a></div> 
	</td>
</tr>
<%} %>
