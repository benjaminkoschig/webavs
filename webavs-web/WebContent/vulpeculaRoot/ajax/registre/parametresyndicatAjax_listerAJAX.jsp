<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
<c:forEach var="parametreSyndicat" items="${viewBean.parametresSyndicats}">
		<tr idEntity="${parametreSyndicat.id}" style="height: 24px;">
			<td>${parametreSyndicat.id}</td>
			<td>${parametreSyndicat.libelleCaisseMetier}</td>
			<td>${parametreSyndicat.pourcentage.value}</td>
			<td>${parametreSyndicat.montantParTravailleur.value}</td>
			<td>${parametreSyndicat.dateDebut.swissValue}</td>
			<td>${parametreSyndicat.dateFin.swissValue}</td>
		</tr>
</c:forEach>
</liste>