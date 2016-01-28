<%@ page import="globaz.jade.client.util.JadeStringUtil"%>
<%@ page import="globaz.jade.persistence.sql.JadeSqlSearchAttributeDefinition"%>
<%@ page import="globaz.jade.persistence.sql.JadeSqlSearchEntityDefinition"%>
<%@ page import="globaz.jade.persistence.sql.JadeSqlSearchGroupDefinition"%>
<%@ page import="java.util.Iterator"%>
<%
	JadeSqlSearchGroupDefinition sgDef = (JadeSqlSearchGroupDefinition) request.getAttribute("searchGroup");
	if (sgDef != null) {

		for (Iterator<?> iterator = sgDef.getSearchEntries().iterator(); iterator.hasNext(); ) {
%><tr>
<%
			Object next = iterator.next();
			if (next instanceof JadeSqlSearchGroupDefinition) {
				request.setAttribute("searchGroup", next);
%>	<td colspan="5">
		<jsp:include page="innerSearchGroupePage.jsp"></jsp:include>
	</td>
<%
			} else if (next instanceof JadeSqlSearchAttributeDefinition) {
				JadeSqlSearchAttributeDefinition entDef = (JadeSqlSearchAttributeDefinition) next;
%>	<td style="vertical-align: middle; width: 15%; ">
		&nbsp;
	</td>
	<td style="vertical-align: middle; width: 35%; text-align: right; padding-right: 10px;">
		<%=entDef.getLeftField().getAttributeName()+" "+entDef.getOperation().toString()%>
	</td>
	<td style="vertical-align: middle; width: 50%;">
<%
				if (!JadeStringUtil.isEmpty(entDef.getName())) {
%>		<input	type="text" 
				name="<%=entDef.getName()%>" 
				value="<%=JadeStringUtil.isEmpty(request.getParameter(entDef.getName())) || "null".equals(request.getParameter(entDef.getName())) ? "" : request.getParameter(entDef.getName())%>" 
				style="width: 100%" />
<%
				} else if (!JadeStringUtil.isEmpty(entDef.getVariable())) {
%>		<input	type="text" 
				name="<%=entDef.getName()%>" 
				value="<%=JadeStringUtil.isEmpty(request.getParameter(entDef.getName())) || "null".equals(request.getParameter(entDef.getName())) ? "" : request.getParameter(entDef.getName())%>" 
				style="width: 100%;"/>
<%
				} else if (!JadeStringUtil.isEmpty(entDef.getValue())) {
%>		<input	type="text" 
				name="<%=entDef.getName()%>" 
				value="<%=entDef.getValue()%>" 
				style="width: 100%" 
				disabled="disabled" 
				readonly="readonly" />
<%
				}
%>	</td> 
<%
			}
%></tr>
<%
		}
	}
%>

