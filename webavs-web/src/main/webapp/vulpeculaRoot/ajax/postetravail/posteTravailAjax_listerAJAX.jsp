<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
	<c:forEach var="entity" items="${viewBean.searchModel.searchResults}">
		<tr idEntity="${entity.id}">
			<td>${entity.id}</td>
			<td>${entity.travailleur.simpleTravailleur.idTravailleur}</td>
			<td>
				<c:out value="${entity.travailleur.tiers.tiers.designation1} ${entity.travailleur.tiers.tiers.designation2}" />
			</td>
			<td>
				<c:out value="${entity.employeurAffiliation.affiliation.affilieNumero} ${entity.employeurAffiliation.affiliation.raisonSociale}" />
			</td>
			<td style="text-align:left;">
				<c:out value="${entity.simplePosteTravail.debutActivite} - ${entity.simplePosteTravail.finActivite}" />
			</td>
		</tr>
	</c:forEach>
</liste>
