<%@page language="java" contentType="text/xml;charset=ISO-8859-1"%>
<%@page import="globaz.auriga.vb.sortiecap.AUSortieCapAjaxViewBean"%>
<%@page import="globaz.auriga.vb.sortiecap.AUSortieCapLineViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>

<liste>
<%
AUSortieCapAjaxViewBean viewBean = (AUSortieCapAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
	
	for(AUSortieCapLineViewBean line: viewBean.getLineViewBeans()) {
%>
		<tr idEntity="<%=line.getIdSortie()%>" >
			<td class="left"><%=line.getNumeroAffilie()%></td>
			<td class="left"><%=line.getMontantExtourne()%></td>
			<td class="left"><%=line.getIdPassageFacturation()%></td>
			<td class="left"><%=line.getEtat()%></td>
		</tr>
<%
	}
%>
</liste>
