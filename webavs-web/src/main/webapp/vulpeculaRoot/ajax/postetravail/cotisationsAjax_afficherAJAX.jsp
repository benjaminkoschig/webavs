<%@ include file="/theme/list_ajax_el/header.jspf" %>
<c:forEach var="entry" items="${viewBean.adhesionsCotisationsActivesEtPossiblesGroupByPlanCaisse}" varStatus="status">

	<c:choose>
		<c:when test="${entry.value.auMoins1CotiActive}">
			<h3><a style="color: #339933" id="caisse_${status.index}" class="caisse" href="#">${entry.key}</a></h3>
		</c:when>
		<c:when test="${entry.value.cotiDesactive}">
			<h3><a id="caisse_${status.index}" class="caisse" href="#">${entry.key}</a></h3>
		</c:when>
		<c:otherwise>
			<h3><a style="color: #999999" id="caisse_${status.index}" class="caisse" href="#">${entry.key}</a></h3>
		</c:otherwise>
	</c:choose>
	
	
	<div id="cotisations_${status.index}" class="cotisations" style="width: 100%">
		<table>
			<thead>
				<tr>
					<th><ct:FWLabel key="JSP_COTISATION" /></th>
					<th><ct:FWLabel key="JSP_DATE_DEBUT" /></th>
					<th><ct:FWLabel key="JSP_DATE_FIN" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="adhesionCotisationView" items="${entry.value}"
					varStatus="status">
					<tr class="cotisation" data-idAdhesionCotsation="${adhesionCotisationView.id}" data-spy="${adhesionCotisationView.spy}">
						<td><input
							class="idCotisation" type="checkbox"
							value="${adhesionCotisationView.idCotisation}"
							<c:if test="${adhesionCotisationView.checked}">
								checked="checked"
							</c:if>
							/>
							<label for="cotisation${adhesionCotisationView.idCotisation}" title="${adhesionCotisationView.dateDebutCotisation} - ${adhesionCotisationView.dateFinCotisation}">${adhesionCotisationView.nom}</label>
						<input type="hidden" class="idAssurance" id="idAssurance" value="${adhesionCotisationView.idAssurance}"/>
						</td>
						<td><input
							id="cotisationDateDebut${adhesionCotisationView.idCotisation}"
							value="${adhesionCotisationView.dateDebut}" class="dateDebut"
							type="text" data-g-calendar=" " /></td>
						<td><input
							id="cotisationDateFin${adhesionCotisationView.idCotisation}"
							value="${adhesionCotisationView.dateFin}" class="dateFin"
							type="text" data-g-calendar=" " /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

</c:forEach>