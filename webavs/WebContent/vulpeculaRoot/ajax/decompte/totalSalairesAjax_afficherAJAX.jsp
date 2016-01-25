<%@ include file="/theme/list_ajax_el/header.jspf" %>
  	<table>
  		<tr>
  			<th><ct:FWLabel key="JSP_DESIGNATION"/></th>
  			<th><ct:FWLabel key="JSP_TAUX"/></th>
  			<th><ct:FWLabel key="JSP_MASSE"/></th>
  			<th><ct:FWLabel key="JSP_MONTANT"/></th>
  		</tr>
  		<fmt:formatNumber var="totalContribution" value="${viewBean.totalContribution}" maxFractionDigits="2" minFractionDigits="2" />
  		<c:forEach var="entry" items="${viewBean.tableauContributions}">
	  		<tr class="bmsRowOdd">
	  			<c:choose>
	  				<c:when test="${entry.key.isCaissesSociales()}">
	  					<td style="font-weight: bold;text-align: left;font-style: italic;"><ct:FWLabel key="JSP_CAISSES_SOCIALES"/></td>
	  				</c:when>
	  				<c:when test="${entry.key.isAvs()}">
	  					<td style="font-weight: bold;text-align: left;font-style: italic;"><ct:FWLabel key="JSP_AVS_AI_APG"/></td>
	  				</c:when>
	  				<c:when test="${entry.key.isAc()}">
	  					<td style="font-weight: bold;text-align: left;font-style: italic;"><ct:FWLabel key="JSP_AC"/></td>
	  				</c:when>
	  				<c:when test="${entry.key.isAc2()}">
	  					<td style="font-weight: bold;text-align: left;font-style: italic;"><ct:FWLabel key="JSP_AC2"/></td>
	  				</c:when>
	  				<c:when test="${entry.key.isAf() and !entry.value.isEmpty()}">
						<td style="font-weight: bold;text-align: left;font-style: italic;"><ct:FWLabel key="JSP_AF"/></td>	  				
	  				</c:when>
	  				<c:otherwise>
	  					
	  				</c:otherwise>
	  			</c:choose>
	  		</tr>
			<c:forEach var="entreeContribution" items="${entry.value}">
				<fmt:formatNumber var="taux" value="${entreeContribution.taux.value}" maxFractionDigits="2" minFractionDigits="2" />
				<fmt:formatNumber var="masse" value="${entreeContribution.masse.value}" maxFractionDigits="2" minFractionDigits="2" />
				<fmt:formatNumber var="montant" value="${entreeContribution.montant.value}" maxFractionDigits="2" minFractionDigits="2" />
				<tr class="bmsRowEven">
					<td></td>
					<td style="text-align:right">${taux}</td>
					<td style="text-align:right">${masse}</td>
					<td style="text-align:right">${montant}</td>
				</tr>
			</c:forEach>
  		</c:forEach>
  		<tr class="bmsRowOdd">
  			<td style="font-weight: bold;text-align:left"><ct:FWLabel key="JSP_TOTAL_CAPS"/></td>
  			<td></td>
  			<td></td>
  			<td></td>
  		</tr>
  		<tr class="bmsRowEven">
  			<td></td>
  			<td></td>
  			<td></td>
  			<td style="text-align:right">${totalContribution}</td>
  		</tr>
  	</table>