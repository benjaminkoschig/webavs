<%@page language="java" contentType="text/xml;charset=ISO-8859-1"%>
<%@page import="globaz.aries.vb.sortiecgas.ARSortieCgasAjaxViewBean"%>
<%@page import="globaz.aries.vb.sortiecgas.ARSortieCgasLineViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>

<liste>
<%
ARSortieCgasAjaxViewBean viewBean = (ARSortieCgasAjaxViewBean) request.getAttribute(FWServlet.VIEWBEAN);
	
	for(ARSortieCgasLineViewBean line: viewBean.getLineViewBeans()) {
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
