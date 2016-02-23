<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT1500"/>
<c:set var="labelTitreEcran" value="JSP_GESTION_DES_ASSOCIATIONS_PROFESSIONNELLES"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="true" scope="page" />
<c:set var="bButtonCancel" value="true" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="true" scope="page" />

<c:set var="employeur" value="${viewBean.employeur}" />
<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/association/association_de.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/json2.js"></script>


<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">
globazGlobal.idEmployeur = '${viewBean.idEmployeur}';
globazGlobal.associationViewService = '${viewBean.associationViewService}';
globazGlobal.csMembre = '${viewBean.csMembre}';
globazGlobal.csNonTaxe = '${viewBean.csNonTaxe}';
globazGlobal.limiteCotisationsNonMembre = ${viewBean.limiteCotisationsNonMembre};
globazGlobal.reductionFactureDefaut = ${viewBean.reductionFactureDefaut};
globazGlobal.masseSalarialeDefaut = ${viewBean.masseSalarialeDefaut};
</script>


<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<style>
#divaddAssociation {
	margin-top: 12px;
}

.association {
	background-color: #D7E4FF;
	margin-top:12px;
s}

.cotisations {
	background-color: transparent;
	margin-left: 24px;
}

.cotisations tr {
	background-color: white;
}
</style>
<script type="jade/template" id="nouvelleAssociation">
	<div class="association">
	<div>
		<button class="deleteAssociation" type="button"><img src="images/edit-delete.png" /></button>
		<div class="associationHeader" style="display:inline">
		<select class="associationProfessionnelle" name="associationProfessionnelle">
			<c:forEach var="associationProfessionnelle" items="${viewBean.associationsProfessionnelles}">
				<option value="${associationProfessionnelle.id}"><c:out value="${associationProfessionnelle.codeAdministration} - ${associationProfessionnelle.designation1} ${associationProfessionnelle.designation2}" /></option>
			</c:forEach>
		</select>
	
		<ct:FWCodeSelectTag notation="class='genre'" name="genre" codeType="PTGENCOTAP" defaut=""/>
		<button class="btnSearchCotisations" type="button">OK</button>
		</div>
	</div>	
	<button class="addCotisation"><img src="images/list-add.png" /></button>	
				<table style="width: 70%" class="cotisations">
					<tr>
						<td></td>
						<td style="width: 20%"><ct:FWLabel key="JSP_COTISATION"/></td>
						<td><ct:FWLabel key="JSP_PERIODE_DEBUT"/></td>
						<td><ct:FWLabel key="JSP_PERIODE_FIN"/></td>
						<td><ct:FWLabel key="JSP_ASSOCIATIONS_PROFESSIONNELLES_MASSE_SALARIALE"/></td>
						<td><ct:FWLabel key="JSP_FORFAIT"/></td>
						<td><ct:FWLabel key="JSP_REDUCTION_FACTURE"/></td>
					</tr>
				</table>
	</div>
</script>

<!--  Champ caché servant à la navigation -->
<input name="tab" type="hidden" />
<div class="content">
	<div class="blocLeft">
		<%@ include file="/vulpeculaRoot/blocs/employeur.jspf" %>
	</div>
	
	<div id="divaddAssociation">
	<button id="addAssociation"><ct:FWLabel key="JSP_AJOUT"/></button>
	</div>
	<div id="associations">
		<c:forEach var="entry" items="${viewBean.associationsCotisations}">
			<div class="association">
				<div>
					<button class="deleteAssociation" type="button"><img src="images/edit-delete.png" /></button>
					<div class="associationHeader" style="display:inline">
					<select class="associationProfessionnelle" name="associationProfessionnelle">
						<c:forEach var="associationProfessionnelle" items="${viewBean.associationsProfessionnelles}">
							<c:choose>
								<c:when test="${associationProfessionnelle.id==entry.key.association.id}">
									<option selected="selected" value="${associationProfessionnelle.id}"><c:out value="${associationProfessionnelle.codeAdministration} - ${associationProfessionnelle.designation1} ${associationProfessionnelle.designation2}" /></option>
								</c:when>
								<c:otherwise>
									<option value="${associationProfessionnelle.id}"><c:out value="${associationProfessionnelle.codeAdministration} - ${associationProfessionnelle.designation1} ${associationProfessionnelle.designation2}" /></option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				
					<ct:FWCodeSelectTag notation="class='genre'" name="genre" codeType="PTGENCOTAP" defaut="${entry.key.genre.value}"/>
					</div>
				</div>	
				<button class="addCotisation"><img src="images/list-add.png" /></button>	
				<table style="width: 70%;" class="cotisations">
					<tr class="softHeader">
						<td></td>
						<td style="width: 20%"><ct:FWLabel key="JSP_COTISATION"/></td>
						<td><ct:FWLabel key="JSP_PERIODE_DEBUT"/></td>
						<td><ct:FWLabel key="JSP_PERIODE_FIN"/></td>
						<td><ct:FWLabel key="JSP_ASSOCIATIONS_PROFESSIONNELLES_MASSE_SALARIALE"/></td>
						<td><ct:FWLabel key="JSP_FORFAIT"/></td>
						<td><ct:FWLabel key="JSP_REDUCTION_FACTURE"/></td>
					</tr>
				<c:forEach var="associationCotisation" items="${entry.value}">
					<tr class="cotisationMembre">
						<td>
							<button class="deleteCotisation">
								<img src="images/edit-delete.png" />
							</button>
						</td>
						<td>
						<select style="width: 100%" class="idCotisation">
							<option value="${associationCotisation.idCotisationAssociationProfessionnelle}">${associationCotisation.libelleCotisation}</option>
						</select>
						</td>					
						<td><input class="periodeDebut" data-g-calendar="mandatory:true"  value="${associationCotisation.periodeDebutAsValue}"/></td>
						<td><input class="periodeFin" data-g-calendar="" value="${associationCotisation.periodeFinAsValue}" /></td>
						<td>
							<c:choose>
								<c:when test="${associationCotisation.nonTaxe}">
									<input class="masseSalariale" type="text" data-g-amount=" " value="${associationCotisation.masseSalariale.value}" class="readOnly" readonly="readonly" disabled="disabled" tabindex="-1" />
								</c:when>
								<c:otherwise>
									<input class="masseSalariale" type="text" data-g-amount=" " value="${associationCotisation.masseSalariale.value}" />
								</c:otherwise>
							</c:choose>
						</td>
						<td><input class="forfait" type="text" data-g-amount="blankAsZero:false" value="${associationCotisation.forfait.value}" /></td>
						<td><input class="reductionFacture" type="text" data-g-amount=" " value="${associationCotisation.reductionFacture.value}" /></td>
				</tr><!-- cotisationMembre -->
				</c:forEach>							
			</table><!-- cotisations -->
		</div><!-- association -->
		</c:forEach>
	</div>
	<div>
	&nbsp;
	</div>
</div>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>