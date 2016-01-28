<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.jade.persistence.sql.JadeSqlJoinAttributeDefinition"%>
<%@page import="globaz.jade.persistence.sql.JadeSqlJoinGroupDefinition"%>
<%@page import="java.util.Iterator"%>
<%
	JadeSqlJoinGroupDefinition current = (JadeSqlJoinGroupDefinition) request.getAttribute("joinGroup");

	if (current != null) {
%>
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td rowspan="<%=current.getJoinEntities().size()+1%>" style="border-right: 1px solid black; text-align: center;">
			<%=current.getOperator().getType()%>
		</td>
		<th>
			Left Field
		</th>
		<th>
			Operator
		</th>
		<th>
			Right field
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
		for (Iterator<?> current.getJoinEntities().size(); i++) {
%>	<tr>
<%
			if (current.getJoinEntities().get(i) instanceof JadeSqlJoinGroupDefinition) {
				request.setAttribute("joinGroup", current.getJoinEntities().get(i));
%>		<td colspan="5">
			<jsp:include page="innerJoinGroupPage.jsp"></jsp:include>
		</td>
<%
			} else if (current.getJoinEntities().get(i) instanceof JadeSqlJoinAttributeDefinition) {
				JadeSqlJoinAttributeDefinition entDef = (JadeSqlJoinAttributeDefinition) current.getJoinEntities().get(i);
%>		<td<%=pair ? " class=\"pair\"" : ""%>>
			<%=entDef.getLeftField() != null ? entDef.getLeftField().getAttributeName() + " - " + entDef.getLeftField().getSqlAlias() : "&nbsp;"%>
		</td>
		<td<%=pair ? " class=\"pair\"" : ""%>>
			<%=entDef.getOperation().toString() %>
		</td>
		<td<%=pair ? " class=\"pair\"" : ""%>>
			<%=entDef.getRightField() != null ? entDef.getRightField().getAttributeName() + " - " + entDef.getRightField().getSqlAlias() : "&nbsp;"%>
		</td>
		<td<%=pair ? " class=\"pair\"" : ""%>>
			<%=JadeStringUtil.isEmpty(entDef.getVariable()) ? "&nbsp;" : entDef.getVariable()%>
		</td>
		<td<%=pair ? " class=\"pair\"" : ""%>>
			<%=JadeStringUtil.isEmpty(entDef.getValue()) ? "&nbsp;" : entDef.getValue()%>
			</td>
<%
				pair = !pair;
			}
%>	</tr>
<%
		}
%></table>
<%
	}
%>