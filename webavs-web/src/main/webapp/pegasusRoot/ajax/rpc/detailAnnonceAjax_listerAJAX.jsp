<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ page isELIgnored ="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:url var="imgCalculOk" value="/images/calcule.png" scope="page"/>
<c:set var="action" value="pegasus.rpc.detailAnnonceAjax.executer" />
<liste>
	<c:forEach var="detailAnnonce" items="${viewBean.retoursAnnonce}">
		<tr idEntity="${detailAnnonce.id}">
			<td>
				<a style="text-align:center" href='<c:out value="${pageContext.request.contextPath}/pegasus?userAction=pegasus.demande.demande.chercher&idDossier=${viewBean.idDossier}" />'> 
					${detailAnnonce.idDemande}
		        </a>
	        </td>
			<td>
				<a style="text-align:center" href='<c:out value="${pageContext.request.contextPath}/pegasus?userAction=pegasus.pcaccordee.pcAccordee.chercher&idVersionDroit=${detailAnnonce.idDroit}" />'> 
					${detailAnnonce.idDroit}
		        </a>
	        </td>
			<td>${detailAnnonce.etat}</td>
			<td>${detailAnnonce.codePlausi}</td>
			<td>${detailAnnonce.categorieString}</td>
			<td>${detailAnnonce.typeViolation}</td>
			<td>${detailAnnonce.typeViolationTooltip}</td>
			<td>${detailAnnonce.nssAnnonceFormatted}</td>
			<c:if test="${detailAnnonce.canTakeAction}">
			
				<td width="400">
					<ct:ifhasright element="${action}" crud="cud">
						<input style="width=95%;" class="remarque" id="remarque_${detailAnnonce.id}" value='${detailAnnonce.remarque}'/>
					</ct:ifhasright>
				</td>
				<td width="180">
					<ct:ifhasright element="${action}" crud="cud">
						<input style="width=85px;" class="btnTraiter" id="traiter_${detailAnnonce.id}" type="button" name="traiter" value="<ct:FWLabel key="JSP_PC_RPC_BTN_TRAITER"/>"/>
						<input style="width=85px;" class="btnAccepter" id="accepter_${detailAnnonce.id}" type="button" name="accepter" value="<ct:FWLabel key="JSP_PC_RPC_BTN_ACCEPTER"/>"/>
					</ct:ifhasright>
				</td>
			</c:if>
			<c:if test="${not detailAnnonce.canTakeAction}">
				<td width="400"></td>
				<td width="180"></td>
			</c:if>
		</tr>
	</c:forEach>
</liste> 
