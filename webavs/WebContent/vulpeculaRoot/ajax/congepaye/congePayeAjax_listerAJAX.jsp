<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
<c:set var="userActionCongePaye" value="vulpecula.congepaye" />
<ct:checkRight var="hasDeleteRightOnCongePaye" element="${userActionCongePaye}" crud="d" />
<c:forEach var="congePaye" items="${viewBean.congesPayes}">
	<fmt:formatNumber var="montantNet" value="${congePaye.montantNet.toStringValue()}" minFractionDigits="2" maxFractionDigits="2" />
		<tr idEntity="${congePaye.id}" style="height: 24px;">
	  		<td>
	  			<c:if test="${viewBean.isSupprimable(congePaye)}">
		  			<c:if test="${hasDeleteRightOnCongePaye}">
			  			<button class="supprimerCP" data-idCongePaye="${congePaye.id}"><img src="images/edit-delete.png" /></button>
		  			</c:if>
	  			</c:if>
	  		</td>
			<td>${congePaye.noAffilie}</td>
			<td style="text-align: left;"><c:out value="${congePaye.raisonSocialeEmployeur}" /></td>
			<td><ct:FWCode csCode="${congePaye.qualification.value}"/></td>
			<td><ct:FWCode csCode="${congePaye.typeSalaire.value}"/></td>
			<td style="text-align: right;">${congePaye.totalSalaire}</td>
			<td style="text-align: right;">${montantNet}</td>
			<td>${congePaye.periodeAsValue}</td>
			<td><ct:FWCode csCode="${congePaye.beneficiaire.value}"/></td>
			<td><ct:FWCode csCode="${congePaye.etat.value}"/></td>
			<td>${congePaye.idPassageFacturation}</td>
			<td>${congePaye.dateComptabilisation.swissValue}</td>
		</tr>
</c:forEach>
</liste>