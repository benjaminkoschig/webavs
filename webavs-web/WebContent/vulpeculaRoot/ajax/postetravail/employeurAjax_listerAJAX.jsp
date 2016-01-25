<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
	<c:forEach var="entity" items="${viewBean.searchModel.searchResults}">
		<tr idEntity="${entity.affiliationTiersComplexModel.affiliation.affiliationId}">
			<td>
				
			    <ct:menuPopup menu="vulpecula-optionsemployeurs">
			        <ct:menuParam key="idTiers" value="${entity.affiliationTiersComplexModel.affiliation.idTiers}"/>
			        <ct:menuParam key="selectedId" value="${entity.affiliationTiersComplexModel.affiliation.affiliationId}"/>
			        <ct:menuParam key="affiliationId" value="${entity.affiliationTiersComplexModel.affiliation.affiliationId}"/>
			    </ct:menuPopup>
			</td>
			<td>
				${entity.affiliationTiersComplexModel.affiliation.affilieNumero}</td>
			<td style="text-align: left;">
				<c:out value="${entity.affiliationTiersComplexModel.affiliation.raisonSociale}"/>
			</td>
			<td>
				<c:out value="${entity.administrationComplexModel.tiers.designation1}" />
			</td>
		</tr>	
	</c:forEach>
</liste>