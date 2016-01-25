<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
<c:set var="userActionCaisseMaladie" value="vulpecula.caisseMaladie" />
<c:forEach var="affiliationCaisseMaladie" items="${viewBean.affiliationsCaissesMaladies}">
		<tr idEntity="${affiliationCaisseMaladie.id}" style="height: 24px;">
	  		<td>
	  		</td>
			<td style="text-align:left"><c:out value="${affiliationCaisseMaladie.libelleCaisseMaladie}"/></td>
			<td style="text-align:left"><c:out value="${affiliationCaisseMaladie.descriptionEmployeur}"/></td>
			<td><c:out value="${affiliationCaisseMaladie.moisDebut.moisAnneeFormatte}" /></td>
			<td><c:out value="${affiliationCaisseMaladie.dateAnnonceDebut}" /></td>
			<td><c:out value="${affiliationCaisseMaladie.moisFin.moisAnneeFormatte}" /></td>
			<td><c:out value="${affiliationCaisseMaladie.dateAnnonceFin}" /></td>
		</tr>
</c:forEach>
</liste>