<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
<c:set var="userActionAbsenceJustifiee" value="vulpecula.absencesjustifiees" />
<ct:checkRight var="hasDeleteRightOnAbsenceJustifiee" element="${userActionAbsenceJustifiee}" crud="d" />
<ct:checkRight var="hasUpdateRightOnAbsenceJustifiee" element="${userActionAbsenceJustifiee}" crud="i" />
<c:forEach var="absenceJustifiee" items="${viewBean.absencesJustifiees}">
	<fmt:formatNumber var="montantVerse" value="${absenceJustifiee.montantVerse.toStringValue()}" minFractionDigits="2" maxFractionDigits="2" />
	<fmt:formatNumber var="montantBrut" value="${absenceJustifiee.montantBrut.toStringValue()}" minFractionDigits="2" maxFractionDigits="2" />
	<tr idEntity="${absenceJustifiee.id}">
  		<td>
  			<c:if test="${viewBean.isModifiable(absenceJustifiee)}">
	  			<c:if test="${hasDeleteRightOnAbsenceJustifiee}">
		  			<button class="supprimerAJ" data-idAbsenceJustifiee="${absenceJustifiee.id}"><img src="images/edit-delete.png" /></button>
	  			</c:if>
  			</c:if>
  		</td>
		<td>${absenceJustifiee.noAffilie}</td>
		<td style="text-align: left;"><c:out value="${absenceJustifiee.raisonSocialeEmployeur}" /></td>
		<td><ct:FWCode csCode="${absenceJustifiee.qualification.value}"/></td>
		<td><ct:FWCode csCode="${absenceJustifiee.typeSalaire.value}"/></td>
		<td>${absenceJustifiee.dateDebutAbsence}</td>
		<td>${absenceJustifiee.dateFinAbsence}</td>
		<td style="text-align: left;"><ct:FWCodeLibelle csCode="${absenceJustifiee.type.value}"/></td>
		<td style="text-align: right;">${montantBrut}</td>
		<td style="text-align: right;">${montantVerse}</td>
		<td><ct:FWCode csCode="${absenceJustifiee.beneficiaire.value}"/></td>
		<td><ct:FWCode csCode="${absenceJustifiee.etat.value}"/></td>
		<td>${absenceJustifiee.idPassageFacturation}</td>
		<td>${absenceJustifiee.dateComptabilisation.swissValue}</td>
		<td></td>
	</tr>
</c:forEach>
</liste>