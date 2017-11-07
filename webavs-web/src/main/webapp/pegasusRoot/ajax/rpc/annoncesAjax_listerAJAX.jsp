<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="imgCalculOk" value="/images/calcule.png" scope="page"/>
<c:url var="imgCalculError" value="/images/small_error.png" scope="page"/>

<liste>
	<c:forEach var="annonce" items="${viewBean.annonces}">
		<tr idEntity="${annonce.id}">
			<td id="annonceDetail_${annonce.id}_${annonce.idDossier}_${annonce.idVersionDroit}" class="showAnnonceDetail left">${annonce.personneDetail}</td>
			<td id="annonceDetail_${annonce.id}_${annonce.idDossier}_${annonce.idVersionDroit}" class="showAnnonceDetail">${annonce.dateAnnonce}</td>
			<td id="annonceDetail_${annonce.id}_${annonce.idDossier}_${annonce.idVersionDroit}" class="showAnnonceDetail"><c:out value="${annonce.etat}"/></td>
			<td id="annonceDetail_${annonce.id}_${annonce.idDossier}_${annonce.idVersionDroit}" class="showAnnonceDetail left">${annonce.codeTraitement}</td>
			<td> 				
				<a data-g-download="docType:xml,
								parametres:¦${annonce.id}¦,
			                    serviceClassName:ch.globaz.pegasus.business.services.rpc.RpcService,
			                    displayOnlyImage:true,
			                    serviceMethodName:loadXmlByIdAnnonce,
			                    docName:annonce_${annonce.id}"
					></a>
			</td>
		    <td>
		    	<a class="btnDisplayPCAL" id="btnDisplayCal_${annonce.idPlanCalculRequerant}_${annonce.idTiersRequerant}">
					R<img src="${imgCalculOk}" />  
				</a>
				<c:if test="${annonce.isCoupleSepare}">
		    		<c:if test="${not empty annonce.idPlanCalculConjoint && not empty annonce.idTiersConjoint}">
			    		<a class="btnDisplayPCAL" id="btnDisplayCal_${annonce.idPlanCalculConjoint}_${annonce.idTiersConjoint}">
							C<img src="${imgCalculOk}" />
						</a>
					</c:if>
					<c:if test="${empty annonce.idPlanCalculConjoint || empty annonce.idTiersConjoint}">
						C<img src="${imgCalculError}" />
					</c:if> 
				</c:if>

		    </td>
		    <td>
		        <a style="float: left;padding-left:10px" href='<c:out value="${pageContext.request.contextPath}/pegasus?userAction=pegasus.demande.demande.chercher&idDossier=${annonce.idDossier}" />'> 
		         <ct:FWLabel key="JSP_PC_RPC_ANNONCE_DEMANDE"/>(${annonce.idDemande})
		        </a>
		    	<c:if test="${annonce.hasVersionDroit}">
		    	<a style="text-align:right" href='<c:out value="${pageContext.request.contextPath}/pegasus?userAction=pegasus.pcaccordee.pcAccordee.chercher&idVersionDroit=${annonce.idVersionDroit}" />'> 
		    	<ct:FWLabel key="JSP_PC_RPC_ANNONCE_DROIT"/>(${annonce.idVersionDroit})
		    	</a>
		    	</c:if>
		    </td>
		</tr>
	</c:forEach>
</liste> 
