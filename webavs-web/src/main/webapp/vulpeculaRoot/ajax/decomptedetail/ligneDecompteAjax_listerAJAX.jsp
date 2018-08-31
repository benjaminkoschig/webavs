<%@ include file="/theme/list_ajax_el/header.jspf" %>
<c:set var="userActionSalaire" value="vulpecula.decomptesalaire" />
<ct:checkRight var="hasUpdateRightOnDecompteSalaire" element="${userActionSalaire}" crud="d" />
<div id="listeSalaires">
<liste>
	<c:forEach var="ligneDecompte" items="${viewBean.lignesDecompte}" varStatus="tagStatus">
	
		<fmt:formatNumber var="tauxContribuable" value="${ligneDecompte.tauxContribuableForCaissesSociales.value}" minFractionDigits="2" maxFractionDigits="2" />
		<fmt:formatNumber var="salaireHoraire" value="${ligneDecompte.salaireHoraireAsValue}" minFractionDigits="2" maxFractionDigits="2" />
		<fmt:formatNumber var="salaireTotal" value="${ligneDecompte.salaireTotalAsValue}" minFractionDigits="2" maxFractionDigits="2" />
		<fmt:formatNumber var="heures" value="${ligneDecompte.heures}" minFractionDigits="2" maxFractionDigits="2" />
		
		<tr idEntity="${ligneDecompte.id}" >
	  		<td>
	  			<c:if test="${ligneDecompte.decompte.editable}">
	  			<c:if test="${hasUpdateRightOnDecompteSalaire}">
		  			<button class="supprimerSalaire" ><img src="images/edit-delete.png" /></button>
	  			</c:if>
	  			</c:if>
	  		</td>
	  		<a href="vulpecula?userAction=${userActionSalaire}.afficher&amp;selectedId=${ligneDecompte.id}&amp;idDecompte=${viewBean.idDecompte}">
				<td><c:out value="${ligneDecompte.posteTravail.id}" /></td>
				<td style="text-align:left;">
				
			 	<c:choose>
			  		<c:when test="${ligneDecompte.newTravailleur}">
						<c:out value="${ligneDecompte.posteTravail.travailleur.designation1} ${ligneDecompte.posteTravail.travailleur.designation2}" />
					</c:when>
			  		<c:otherwise>
				  		<a href="vulpecula?userAction=vulpecula.postetravailvueglobale.travailleurvueglobale.afficher&amp;selectedId=${ligneDecompte.idTravailleur}&amp;tab=salaires" style="text-decoration: underline; color: blue;">
							<c:out value="${ligneDecompte.posteTravail.travailleur.designation1} ${ligneDecompte.posteTravail.travailleur.designation2}" />
						</a>
			  		</c:otherwise>
			  	</c:choose> 

				</td>			
				<td><c:out value="${ligneDecompte.periodeDebutAsSwissValue} - ${ligneDecompte.periodeFinAsSwissValue}"></c:out></td>
				<td><ct:FWCode csCode="${ligneDecompte.posteTravail.qualification.value}"/></td>
				<c:if test="${ligneDecompte.decompte.periodique}">
					<td style="text-align: right;"><c:out value="${heures}" /></td>
				</c:if>
				<td style="text-align: right;"><c:out value="${salaireHoraire}" /></td>
				<td style="text-align: right;"><c:out value="${salaireTotal}" /></td>
				<td>
					<c:forEach var="absence" items="${ligneDecompte.absences}" varStatus="absenceStatus">
						<c:if test="${absenceStatus.index!=0}">
						 	<c:out value=" / "></c:out> 
						</c:if>
						<ct:FWCode csCode="${absence.type.value}"/> 
					</c:forEach>
				</td>
				<td style="text-align: right;">${tauxContribuable}</td>
				<c:choose>
				  	<c:when test="${ligneDecompte.ligneSupprimmee}">
				  		<td style="text-align: center;"><img src='images/delete_icon.gif' alt='' width='16' height='16'/></td>
				  	</c:when>
				 	<c:otherwise>
					  	<c:choose>
						  	<c:when test="${ligneDecompte.aTraiter}">
						  		<td style="text-align: center;">
									<img src='images/dialog-warning.png' title='<c:out value="${viewBean.getTextesErreurs(ligneDecompte.listeCodeErreur)}"/>' width='16' height='16'/>	
						  		</td>
						  	</c:when>
						  	<c:otherwise>
						  		<td style="text-align: center;"><img src='images/dialog-ok-apply.png' alt='' width='16' height='16'/></td>
						  	</c:otherwise>
					  	</c:choose> 
			  		</c:otherwise>
			  	</c:choose> 
			</a>
		</tr>			
	</c:forEach>
</liste>
</div>
	