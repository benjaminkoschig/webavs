<%@ include file="/theme/list_ajax_el/header.jspf" %>

<c:set var="userActionSalaire" value="vulpecula.decompte.decomptesalaire" />

<table style="width: 100%">

	<tr>
		<td>
			<ct:ifhasright element="${userActionSalaire}" crud="c">
				<a href="vulpecula?userAction=${userActionSalaire}.afficher&_method=add" data-g-externallink=" ">
					<img style="margin-left:4px; width: 24px;height: 24px;" src="images/amal/view_right.png" />
				</a>
			</ct:ifhasright>
		</td>
	</tr>
	
	<c:forEach var="ligneDecompte" items="${viewBean.lignesDecompte}" varStatus="status">
	  	<c:choose>
		  	<c:when test="${status.index%2==0}">
		  		<tr idEntity="${ligneDecompte.id}" class="bmsRowOdd" onmouseover="jscss('swap',this,'bmsRowOdd','bmsRowHighlighted')" onmouseout="jscss('swap',this,'bmsRowHighlighted','bmsRowOdd')">
		  	</c:when>
		  	<c:otherwise>
		  		<tr idEntity="${ligneDecompte.id}" class="bmsRowEven" onmouseover="jscss('swap',this,'bmsRowEven','bmsRowHighlighted')" onmouseout="jscss('swap',this,'bmsRowHighlighted','bmsRowEven')">
		  	</c:otherwise>
	  	</c:choose>
	  		<td>
	  			<button class="supprimerSalaire"><ct:FWLabel key="JSP_SUPPRIMER"/></button>
	  		</td>
			<td><c:out value="${ligneDecompte.posteTravail.id}" /></td>
			<td style="text-align:left;"><c:out value="${ligneDecompte.posteTravail.travailleur.designation1} ${ligneDecompte.posteTravail.travailleur.designation2}" /></td>			
			<td><ct:FWCode csCode="${ligneDecompte.posteTravail.qualification.value}"/></td>
			<td><c:out value="${ligneDecompte.heures}" /></td>
			<td><c:out value="${ligneDecompte.salaireHoraire}" /></td>
			<td><c:out value="${ligneDecompte.salaireTotal}" /></td>
			<td>
				<c:forEach var="absence" items="${ligneDecompte.absences}">
						<ct:FWCode csCode="${absence.type.value}"/> 
				</c:forEach>
			</td>
			<td><c:out value="${ligneDecompte.tauxContribuable}" /></td>
			<td style="text-align:left;"><c:out value="${ligneDecompte.remarque}" /></td>
		</tr>			
	</c:forEach>
 </table>