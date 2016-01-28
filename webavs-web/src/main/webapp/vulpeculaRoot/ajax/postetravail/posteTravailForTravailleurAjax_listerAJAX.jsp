<%@ include file="/theme/list_ajax_el/header.jspf" %>
<liste>
<c:forEach var="posteTravail" items="${viewBean.postes}">
	<tr idEntity="${posteTravail.id}">
	<c:choose>
		<c:when test="${posteTravail.actif}">
			<td><img src="images/active.png"  width="25" height="25" /></td>
		</c:when>
		<c:otherwise>
			<td><img src="images/inactive.png"  width="25" height="25" /></td>
		</c:otherwise>
	</c:choose>
		<td><c:out value="${posteTravail.id}"/></td>
		<td style="text-align: left;">
			<a href="vulpecula?userAction=vulpecula.postetravailvueglobale.employeurvueglobale.afficher&amp;selectedId=${posteTravail.employeur.id}" style="text-decoration: underline; color: blue;">
				<c:out value="${posteTravail.employeur.raisonSociale}" />
			</a>
		</td>
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
		<td title="<ct:FWCodeLibelle csCode="${posteTravail.typeSalaire.value}"/>"><ct:FWCode csCode="${posteTravail.typeSalaire.value}"/></td>
		<td><ct:FWCode csCode="${posteTravail.qualification.value}"/><c:out value=" - " /><ct:FWCodeLibelle csCode="${posteTravail.qualification.value}"/></td>
		<td>${posteTravail.employeur.convention.code}<c:out value=" - " />${posteTravail.employeur.convention.designation}</td>
	</tr>
	</c:forEach>
</liste>