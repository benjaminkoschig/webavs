<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<liste>
	<c:forEach var="prixChambre" items="${viewBean.searchModel.searchResults}">
			<tr idEntity="${prixChambre.id}">
			<td class="mtd" align="right"><c:out value="${prixChambre.periode}"/></td>
			<td class="mtd"><c:out value="${prixChambre.typeChambre.designationTypeChambre}"/></td>
			<td class="mtd" align="right"><c:out value="${prixChambre.simplePrixChambre.prixJournalier}"/></td>
			<td class="mtd" align="right"><c:out value="${prixChambre.simplePrixChambre.idPrixChambre}"/></td>
		</tr>
	</c:forEach>
</liste> 
<ct:serializeObject objectName="viewBean.prixChambre" destination="xml"/>