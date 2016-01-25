<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
<c:forEach var="posteTravail" items="${viewBean.postes}" varStatus="status">
 	<tr idEntity="${posteTravail.id}">
	<c:choose>
		<c:when test="${posteTravail.actif}">
			<td><a href="vulpecula?userAction=vulpecula.postetravail.posteTravail.afficher&amp;selectedId=${posteTravail.id}"><img src="images/active.png"  width="25" height="25" /></a></td>
		</c:when>
		<c:otherwise>
			<td><a href="vulpecula?userAction=vulpecula.postetravail.posteTravail.afficher&amp;selectedId=${posteTravail.id}"><img src="images/inactive.png"  width="25" height="25" /></a></td>
		</c:otherwise>
	</c:choose>
		<td><c:out value="${posteTravail.id}"/></td>
		<td>
			<a href="vulpecula?userAction=vulpecula.postetravailvueglobale.travailleurvueglobale.afficher&amp;selectedId=${posteTravail.travailleur.id}" title="Travailleur" style="text-decoration: underline; color: blue;">
			<c:out value="${posteTravail.travailleur.id}"/></a>
		</td>
		<td style="text-align: left;"><c:out value="${posteTravail.travailleur.designation1} ${posteTravail.travailleur.designation2}" /></td>
		<td>${posteTravail.travailleur.dateNaissance}</td>
		<td>${posteTravail.travailleur.numAvsActuel}</td>
		<td title="<ct:FWCodeLibelle csCode='${posteTravail.qualification.value}' />"><ct:FWCode csCode="${posteTravail.qualification.value}"/></td>
		<td><ct:FWCode csCode="${posteTravail.typeSalaire.value}"/></td>
		<td style="text-align: left;">
	 	<c:if test="${!empty posteTravail.periodeActivite}">
	 		
	 		<c:if test="${!empty posteTravail.periodeActivite.dateDebut}">
	 			${posteTravail.periodeActivite.dateDebut.swissValue} -
	 		</c:if>
	 		<c:if test="${!empty posteTravail.periodeActivite.dateFin}">
	 			${posteTravail.periodeActivite.dateFin.swissValue}
	 		</c:if>
		</c:if>
		</td>
	</tr>
</c:forEach>
</liste>