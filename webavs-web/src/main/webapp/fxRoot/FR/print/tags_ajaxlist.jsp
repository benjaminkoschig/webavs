<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>
<%@page import="globaz.fx.vb.print.FXTagsListViewBean"%>
<%
FXTagsListViewBean viewBean = (FXTagsListViewBean)request.getAttribute("viewBean");
FXTemplateTag[] tags = viewBean.getTags();
%>

<%@page import="ch.globaz.fx.business.service.templates.FXTemplateTag"%>

<%-- MODE RENDU TABLE COMPLETE --%>
<% if (FXTagsListViewBean.OUTPUT_TABLE.equals(viewBean.getOutputType())) {%>
<table class="framed">
<thead>
	<tr>
		<th>Libellé</th>
		<th>&nbsp;</th>
		<th>more info</th>
	</tr>
</thead>
<tbody>
<%
	boolean rowOdd = false;
	for (int i = 0; i < tags.length; i++) {
%>
	<tr class='<%=rowOdd?"rowOdd":"row"%>'>
		<td><%=tags[i].getLabel() %></td>
		<td><a href="javascript:deleteTag('<%=tags[i].getId()%>')">delete</a></td>
		<td>Creation: <%=tags[i].getCreationSpy() %>. Updated: <%=tags[i].getSpy() %></td>
	</tr>
<%		rowOdd = !rowOdd;
	} 
%>
</tbody>
</table>
<% } %>

<%-- MODE RENDU OPTION MULTIPLE --%>
<% if (FXTagsListViewBean.OUTPUT_MULTISELECT.equals(viewBean.getOutputType())) {%>
<select class="framed" multiple="multiple" size="5" name="templateTags">
<%
	boolean rowOdd = false;
	for (int i = 0; i < tags.length; i++) {
%>
	<option value="<%=tags[i].getId()%>"><%=tags[i].getLabel() %></option>
	<%} 
%>
</select>
<% } %>