<%@ include file="/theme/list_ajax_el/header.jspf" %>
<c:set var="userActionServiceMilitaire" value="vulpecula.servicemilitaire" />

<ct:checkRight var="hasDeleteRightOnserviceMilitaire" element="${userActionServiceMilitaire}" crud="d" />
<liste>
<c:forEach var="serviceMilitaire" items="${viewBean.servicesMilitaire}">
	<fmt:formatNumber var="montantAVerser" value="${serviceMilitaire.montantAVerser.toStringValue()}" minFractionDigits="2" maxFractionDigits="2" />
	<fmt:formatNumber var="montantBrut" value="${serviceMilitaire.montantBrut.toStringValue()}" minFractionDigits="2" maxFractionDigits="2" />
	 	<tr idEntity="${serviceMilitaire.id}">
	  		<td>
	  			<c:if test="${viewBean.isModifiable(serviceMilitaire) and hasDeleteRightOnserviceMilitaire}">
		  			<button class="supprimerSM" data-idserviceMilitaire="${serviceMilitaire.id}"><img src="images/edit-delete.png" /></button>
	  			</c:if>
	  		</td>
			<td>${serviceMilitaire.noAffilie}</td>
			<td style="text-align: left;"><c:out value="${serviceMilitaire.raisonSocialeEmployeur}" /></td>
			<td><ct:FWCode csCode="${serviceMilitaire.qualification.value}"/></td>
			<td><ct:FWCode csCode="${serviceMilitaire.typeSalaire.value}"/></td>
			<td>${serviceMilitaire.dateDebutAsString}</td>
			<td>${serviceMilitaire.dateFinAsString}</td>
			<td><ct:FWCodeLibelle csCode="${serviceMilitaire.genre.value}"/></td>
			<td>${montantBrut}</td>
			<td>${montantAVerser}</td>
			<td><ct:FWCode csCode="${serviceMilitaire.beneficiaire.value}"/></td>
			<td><ct:FWCode csCode="${serviceMilitaire.etat.value}"/></td>
			<td>${serviceMilitaire.idPassage}</td>
			<td>${serviceMilitaire.dateComptabilisation.swissValue}</td>
		</tr>
</c:forEach>
</liste>
