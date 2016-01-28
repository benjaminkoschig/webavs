<%@ include file="/theme/list_ajax_el/header.jspf" %>
  	<table>
  		<tr>
  			<th><ct:FWLabel key="JSP_PLAN_CAISSE"/></th>
  			<th><ct:FWLabel key="JSP_DESIGNATION"/></th>
  			<th><ct:FWLabel key="JSP_MASSE"/></th>
  			<th><ct:FWLabel key="JSP_TAUX"/></th>
  			<th><ct:FWLabel key="JSP_COTISATION"/></th>
  		</tr>
  		<c:forEach var="entry" items="${viewBean.cotisationCalculeesGroups}">
  			<fmt:formatNumber var="cotisationTotal" value="${entry.key.cotisation}" maxFractionDigits="2" minFractionDigits="2" />
	  		<tr class="bmsRowOdd">
	  			<td style="text-align:left; font-weight:bold; font-style:italic" colspan="3">${entry.key.libelle}</td>
	  			<td style="text-align:right; font-weight:bold; font-style:italic">${entry.key.taux}</td>
				<td style="text-align:right; font-weight:bold; font-style:italic">${cotisationTotal}</td>
	  		</tr>
			<c:forEach var="cotisationCalculee" items="${entry.value}">
				<tr class="bmsRowEven">
					<fmt:formatNumber var="masse" value="${cotisationCalculee.masse}" maxFractionDigits="2" minFractionDigits="2" />
					<fmt:formatNumber var="cotisation" value="${cotisationCalculee.cotisation}" maxFractionDigits="2" minFractionDigits="2" />
					<td></td>
					<td style="text-align:left">${cotisationCalculee.libelle}</td>
					<td style="text-align:right">${masse}</td>
					<td style="text-align:right">${cotisationCalculee.taux}</td>
					<td style="text-align:right">${cotisation}</td>
				</tr>
			</c:forEach>
  		</c:forEach>
  	</table>