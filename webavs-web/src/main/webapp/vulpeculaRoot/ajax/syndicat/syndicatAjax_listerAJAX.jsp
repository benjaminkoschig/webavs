<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
<c:forEach var="affiliationSyndicat" items="${viewBean.affiliationsSyndicats}">
		<tr class="parametre" idEntity="${affiliationSyndicat.id}" style="height: 24px;">
	  		<td>
	  		</td>
			<td>${affiliationSyndicat.codeAdministration}</td>
			<td style="text-align:left"><c:out value="${affiliationSyndicat.libelleSyndicat}" /></td>
			<td><c:out value="${affiliationSyndicat.dateDebutAsSwissValue}" /></td>
			<td><c:out value="${affiliationSyndicat.dateFinAsSwissValue}" /></td>
			<td>${affiliationSyndicat.cumulSalaires.value}</td>
		</tr>
</c:forEach>
</liste>