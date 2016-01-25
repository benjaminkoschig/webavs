<%@page import="globaz.framework.servlets.FWServlet"%>
<%@page import="globaz.perseus.vb.rentepont.PFValidationFactureAjaxViewBean"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored ="false" %>
<%
    PFValidationFactureAjaxViewBean viewBean=(PFValidationFactureAjaxViewBean)request.getAttribute(FWServlet.VIEWBEAN);
%>
	<liste>
		<c:forEach var="facture" items="${viewBean.searchModel.searchResults}">
			<tr>
				<td><input type="checkbox" /></td>
				<td>
					${facture.qdRentePont.dossier.demandePrestation.personneEtendue.tiers.designation2}
					${facture.qdRentePont.dossier.demandePrestation.personneEtendue.tiers.designation1}
				</td>
				<td></td>
				<td>${facture1}</td>
				<td>${facture1}</td>
				<td>${facture1}</td>
				<td>${facture1}</td>
				<td>${facture1}</td>
			</tr>
		</c:forEach>
	</liste>
	<ct:serializeObject objectName="viewBean" destination="xml"/>