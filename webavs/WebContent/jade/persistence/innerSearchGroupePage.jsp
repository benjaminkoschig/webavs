<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.jade.persistence.sql.JadeSqlSearchAttributeDefinition"%>
<%@ page import="globaz.jade.persistence.sql.JadeSqlSearchEntityDefinition"%>
<%@ page import="globaz.jade.persistence.sql.JadeSqlSearchGroupDefinition"%>
<%@ page import="globaz.jade.persistence.sql.JadeSqlSearchLiteralInstruction"%>
<%@ page import="java.util.Iterator"%>
<%
	JadeSqlSearchGroupDefinition sgDef = (JadeSqlSearchGroupDefinition) request.getAttribute("searchGroup");

	if (sgDef != null) {
%>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td rowspan="<%=sgDef.getSearchEntries().size()+1%>" style="border-right: 1px solid black; text-align: center;">
			<%=sgDef.getOperator().getType()%>
		</td>
		<th>
			Field
		</th>
		<th>
			Operator
		</th>
		<th>
			Search model variable name
		</th>
		<th>
			Value
		</th>
		<th>
			Variable
		</th>
	</tr>
<%
		boolean pair = false;
		for (Iterator<?> iterator = sgDef.getSearchEntries().iterator(); iterator.hasNext(); ) {
			Object next = iterator.next();
%>	<tr>
<%
			if (next instanceof JadeSqlSearchGroupDefinition) {
				request.setAttribute("searchGroup", next);
%>		<td colspan="5">
			<jsp:include page="innerSearchGroupePage.jsp"></jsp:include>
		</td>
<%
			} else if (next instanceof JadeSqlSearchAttributeDefinition) { 
				JadeSqlSearchAttributeDefinition entDef = (JadeSqlSearchAttributeDefinition) next;
%>
		<td<%=pair ? " class=\"pair\"" : ""%>>
			<%=entDef.getLeftField() != null ? entDef.getLeftField().getAttributeName() + " - " + entDef.getLeftField().getSqlAlias() : "&nbsp;"%>
		</td>
		<td<%=pair ? " class=\"pair\"" : ""%>>
			<%=entDef.getOperation().toString() %>
		</td>
		<td<%=pair ? " class=\"pair\"" : ""%>>
			<%=JadeStringUtil.isEmpty(entDef.getName()) ? "&nbsp;" : entDef.getName()%>
		</td>
		<td<%=pair ? " class=\"pair\"" : ""%>>
			<%=JadeStringUtil.isEmpty(entDef.getVariable()) ? "&nbsp;" : entDef.getVariable() %>
		</td>
		<td<%=pair ? " class=\"pair\"" : ""%>>
			<%=JadeStringUtil.isEmpty(entDef.getValue()) ? "&nbsp;" : entDef.getValue()%>
		</td>
<%
				pair = !pair;
			}
%>
	</tr>
<%
	}
%>
</table>
<%}%>