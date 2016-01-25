<%@ include file="/theme/detail_el/header.jspf" %>

<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<c:set var="idEcran" value="PRE2020"/>
<c:set var="labelTitreEcran" value="JSP_PREPARER_DECISION_AVEC_AJOURNEMENT_TITRE"/>

<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<%@ include file="/theme/detail_el/javascripts.jspf" %>

<script type="text/javascript" src="${pageContext.request.contextPath}/corvusRoot/script/decisions/preparerDecisionAvecAjournement_de.js"></script>

<%@ include file="./communsAuxDeuxPreparationsDeDecisionSpecfique.jspf" %>
			<tr class="forceTRHeight">
				<td class="alignLeft">
					<ct:FWLabel key="JSP_PREPARER_DECISION_AVEC_AJOURNEMENT_DOCUMENT_A_EDITER" />&#160;:&#160;
				</td>
				<td class="alignLeft">
					<input	type="checkbox" 
							id="editionDuDocument" 
							name="editionDuDocument" 
							<c:if test="${viewBean.editionDuDocument}">checked="checked"</c:if> />
				</td>
			</tr>
			<tr class="forceTRHeight">
				<td class="alignLeft">
					<span class="afficherSiEditionDuDocument">
						<ct:FWLabel key="JSP_PREPARER_DECISION_AVEC_AJOURNEMENT_DATE_SUR_LE_DOCUMENT" />&#160;:
					</span>
					&#160;
				</td>
				<td class="alignLeft">
					<span class="afficherSiEditionDuDocument">
						<input	type="text" 
								id="dateSurLeDocument" 
								name="dateSurLeDocument" 
								data-g-calendar="mandatory:true" 
								value="${viewBean.dateSurLeDocument}" />
					</span>
				</td>
			</tr>
			<c:if test="${viewBean.documentAvecConfigurationPourLaGed}">
				<tr class="forceTRHeight">
					<td class="alignLeft">
						<span class="afficherSiEditionDuDocument">
							<ct:FWLabel key="JSP_PREPARER_DECISION_AVEC_AJOURNEMENT_MISE_EN_GED" />&#160;
						</span>
					</td>
					<td>
						<span class="afficherSiEditionDuDocument">
							<input	type="checkbox" 
									id="miseEnGed" 
									name="miseEnGed" 
									<c:if test="${viewBean.editionDuDocument}">checked="checked"</c:if> />
						</span>
					</td>
				</tr>
			</c:if>

<%@ include file="./boutonValiderPreparerDecisionSpecifique.jspf" %>
				
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>
