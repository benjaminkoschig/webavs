<%@ include file="/theme/list_ajax_el/header.jspf" %>

<c:forEach items="${viewBean.cotisationsParAssociation}"  var="entry">
	<tr class="detailTable" style="width: 100%" cellspacing="0" cellpadding="0">
		<td></td>
		<td colspan="2" style="text-align: left;font-weight: bold;font-size: 12px;"><c:out value="${entry.key.association.designation1} ${entry.key.association.designation2}" /> : <ct:FWCodeLibelle csCode="${entry.key.genre.value}"/></td>
	</tr>
	<tr>
		<td></td>
		<td colspan="2" style="border: 1px; background-color: white;">
			<table class="tableAssociationProfesionnelle" style="width: 100%">
				<thead>
					<tr style="font-weight: bold;">
						<td style="text-align:left; width: 5%"><ct:FWLabel key="JSP_ID"/></td>
						<td style="text-align:left;width: 10%"><ct:FWLabel key="JSP_LIBELLE"/></td>
						<td style="width: 10%"><ct:FWLabel key="JSP_DATE_DEBUT"/></td>
						<td style="width: 10%"><ct:FWLabel key="JSP_DATE_FIN"/></td>
						<td style="width: 10%"><ct:FWLabel key="JSP_MASSE_SALARIALE"/></td>
						<td style="width: 10%"><ct:FWLabel key="JSP_FORFAIT"/></td>
						<td style="width: 10%"><ct:FWLabel key="JSP_REDUCTION_FACTURE"/></td>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="associationCotisation" items="${entry.value}" varStatus="status">
						<tr>
							<td style="text-align: left;">${associationCotisation.id}</td>
							<td style="text-align: left;">${associationCotisation.libelleCotisation}</td>
							<td>${associationCotisation.periodeDebutAsValue}</td>
							<td>${associationCotisation.periodeFinAsValue}</td>
							<td>${associationCotisation.masseSalariale.value}</td>
							<td>${associationCotisation.forfait.value}</td>
							<td>${associationCotisation.reductionFacture.value}</td>
						</tr>
					</c:forEach>				
				</tbody>
			</table>
		</td>
	</tr>

</c:forEach>
