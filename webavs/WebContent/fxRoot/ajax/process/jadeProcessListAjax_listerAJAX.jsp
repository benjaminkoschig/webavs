<%@page import="globaz.fx.vb.process.FXJadeProcessListAjaxViewBean"%>
<%@page import="globaz.framework.servlets.FWServlet"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page isELIgnored ="false" %>
<liste> 
	<c:forEach var="row" items="${viewBean.processRows}" >
		<tr idEntity="${row.id}" >
			<c:forEach var="column" items="${row.columns}" >
				<td class="${column.classCss}">
					<c:choose>
						<c:when test="${column.classCss == 'deleteProcess'}">
							<ct:ifhasright element="fx.process.jadeProcess.afficher" crud="d">
								${column.value}
							</ct:ifhasright>
						</c:when>
						<c:otherwise>
							${column.value}
						</c:otherwise>
					</c:choose>
				</td>		
			</c:forEach>
		</tr>			
	</c:forEach>
</liste>