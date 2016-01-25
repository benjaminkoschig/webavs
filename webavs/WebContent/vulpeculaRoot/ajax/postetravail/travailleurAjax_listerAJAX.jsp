<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
	<c:forEach var="entity" items="${viewBean.searchModel.searchResults}">
		<tr idEntity="${entity.id}">
			
			<td>
				<ct:menuPopup menu="vulpecula-optionstravailleurs">
					<ct:menuParam key="selectedId" value="${entity.id}"/>
				</ct:menuPopup>
			</td>
					
			<td><c:out value="${entity.id}" /></td>
			<td style="text-align: left;">
				<c:out value="${entity.personneEtendueComplexModel.tiers.designation1}" />
			</td>
			<td style="text-align: left;">
				<c:out value="${entity.personneEtendueComplexModel.tiers.designation2}" />
			</td>
			<td>
				<c:out value="${entity.personneEtendueComplexModel.personneEtendue.numAvsActuel}" />
			</td>
			<td>
				<c:out value="${entity.personneEtendueComplexModel.personne.dateNaissance}" />
			</td>
		</tr>			
	</c:forEach>
</liste>

