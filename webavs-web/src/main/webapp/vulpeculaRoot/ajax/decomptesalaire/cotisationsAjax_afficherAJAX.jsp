<%@ include file="/theme/list_ajax_el/header.jspf" %>
<table style="width: 100%">
	<tr>
		<th></th>
		<th><ct:FWLabel key="JSP_CAISSE_SOCIALE"/></th>
		<th><ct:FWLabel key="JSP_LIBELLE_COTISATION"/></th>
		<th><ct:FWLabel key="JSP_MASSE"/></th>
		<th><ct:FWLabel key="JSP_FRANCHISE"/></th>
		<th><ct:FWLabel key="JSP_TAUX"/></th>
		<th><ct:FWLabel key="JSP_COTISATION"/></th>
		
	</tr>
	<c:forEach var="decompteGroupView" items="${viewBean.cotisationDecompteContainer.groupsViews}" varStatus="status">
		<tr class="bmsRowOdd" style="font-weight: bold;">					
		<fmt:formatNumber var="cotisations" value="${decompteGroupView.cotisations}" minFractionDigits="2" maxFractionDigits="2" />
		<fmt:formatNumber var="taux" value="${decompteGroupView.taux}" minFractionDigits="2" maxFractionDigits="2" />
			<td colspan="2">${decompteGroupView.caisseSociale}</td>
			<td></td>
			<td style="text-align:right"></td>
			<td style="text-align:right"></td>
			<td class="aCacher" style="font-style: italic;text-align:right">${taux}</td>
			<td class="aCacher" style="font-style: italic;text-align:right">${cotisations}</td>
		</tr>
		<c:forEach var="decompteView" items="${decompteGroupView.cotisationDecompteViews}">
			<tr class="bmsRowEven">	
			<fmt:formatNumber var="cotisation" value="${decompteView.cotisation}" minFractionDigits="2" maxFractionDigits="2" />
			<fmt:formatNumber var="taux" value="${decompteView.taux}" minFractionDigits="3" maxFractionDigits="3" />
			<fmt:formatNumber var="masse" value="${decompteView.masse}" minFractionDigits="2" maxFractionDigits="2" />
			<fmt:formatNumber var="franchise" value="${decompteView.franchise}" minFractionDigits="2" maxFractionDigits="2" />
				<td>
					<c:if test="${viewBean.editable}">
						<button class="supprimerCotisation" data-idCotisationDecompte="${decompteView.id}"><img src="images/edit-delete.png" /></button>
					</c:if>
				</td>
				<td></td>
				<td>${decompteView.libelle}</td>
				<td style="text-align:right">${masse}</td>
				<td style="text-align:right">${franchise}</td>
				<td style="text-align:right">${taux}</td>
				<td style="text-align:right">${cotisation}</td>
			</tr>
		</c:forEach>
	</c:forEach>
	<tr class="bmsRowEven" style="font-weight: bold;">
		<fmt:formatNumber var="cotisations" value="${viewBean.cotisationDecompteContainer.cotisations}" minFractionDigits="2" maxFractionDigits="2" />
		<fmt:formatNumber var="taux" value="${viewBean.cotisationDecompteContainer.taux}" minFractionDigits="2" maxFractionDigits="2" />
		<th><ct:FWLabel key="JSP_TOTAL"/></th>
		<th colspan="4"></th>
		<th style="text-align:right">${taux}</th>
		<th style="text-align:right">${cotisations}</th>
	</tr>
</table>