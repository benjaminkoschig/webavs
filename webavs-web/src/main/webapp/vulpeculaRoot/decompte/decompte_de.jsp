<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/find_ajax_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° écran et titre --%>
<c:set var="idEcran" value="PPT1109"/>
<c:set var="labelTitreEcran" value="JSP_DECOMPTES"/>

<c:set var="userActionNew" value="vulpecula.decomptenouveau.decomptenouveau.afficher"/>

<%@ include file="/theme/find_ajax_el/javascripts.jspf" %>

<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjaxList.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/widget/globazwidget.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/widget.css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar/AnchorPosition.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar/PopupWindow.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/menuPopup.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/decompte/decompte_de.js"></script>

<script type="text/javascript">
	globazGlobal.csTaxationOffice = '${viewBean.csTaxationOffice}';
	globazGlobal.ACTION_AJAX = '${userActionListerAjax}';
	globazGlobal.decompteService = '${viewBean.decompteService}';
	globazGlobal.decompteViewService = '${viewBean.decompteViewService}';
	globazGlobal.libelleBoutonSommation = '${viewBean.libelleBoutonSommation}';
	globazGlobal.libelleConfirmeSommation = '${viewBean.libelleConfirmeSommation}';
	globazGlobal.libelleImpressionDemarree = '${viewBean.libelleImpressionDemarree}';
</script>

<%@ include file="/theme/find_ajax_el/bodyStart.jspf" %>
	<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/find_ajax_el/bodyStart2.jspf" %>

<c:if test="${viewBean.protege}">
	<tr>
		<td><span style="color:red">${viewBean.protegeLibelle}</span></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
</c:if>

<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<tr>
	<td>
		<div class="area">
			<div class="areaSearch">
				<table>
					<tr>
						<td><ct:FWLabel key="JSP_ID_DECOMPTE"/></td>
						<td>
							<input id="searchModel.forId" value="${viewBean.searchModel.forId}" name="idDecompte" type="text" />
						</td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_NO_DECOMPTE"/></td>
						<td>
							<input id="searchModel.forNoDecompte" value="${viewBean.searchModel.forNoDecompte}" name="noDecompte" type="text" />
						</td>
						<td><ct:FWLabel key="JSP_CONVENTION"/></td>
						<td>
							<select id="searchModel.forIdConvention" name="convention">
								<option value=""></option>
								<c:forEach var="convention" items="${viewBean.conventions}">
									<c:choose>
										<c:when test="${convention.id==viewBean.searchModel.forIdConvention}">
											<option selected="selected" value="${convention.id}">${convention.designation}</option>
										</c:when>
										<c:otherwise>
											<option value="${convention.id}">${convention.designation}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_NO_AFFILIE"/></td>
						<td>
							<input id="searchModel.likeNoAffilie" value="${viewBean.searchModel.likeNoAffilie}" name="noAffilie" type="text" />
						</td>
						<td><ct:FWLabel key="JSP_PERIODE_DU"/></td>
						<td>
							<input id="searchModel.forDateDe" value="${viewBean.searchModel.forDateDe}" name="recuLe" type="text" data-g-calendar="type:month" />
						</td>
						<td><ct:FWLabel key="JSP_AU"/></td>
						<td>
							<input id="searchModel.forDateAu" value="${viewBean.searchModel.forDateAu}" name="recuDe" type="text" data-g-calendar="type:month" />
						</td>
					</tr>
					<tr>
					<td><ct:FWLabel key="JSP_RAISON_SOCIALE"/></td>
						<td>
							<input id="searchModel.likeRaisonSocialeUpper" value="${viewBean.searchModel.likeRaisonSocialeUpper}" name="raisonSociale" type="text" />
						</td>
						<td><ct:FWLabel key="JSP_DATE_RECEPTION"/></td>
						<td>
							<input id="searchModel.forDateReception" value="${viewBean.searchModel.forDateReception}" name="dateReception" type="text" data-g-calendar=" " />
						</td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_ETAT"/></td>
						<td>
							<select id="searchModel.inEtats" name="etat">
								<option value=""></option>
								<option value="${viewBean.etatsEnSuspens}" selected>En suspens</option>
								<c:forEach var="etat" items="${viewBean.etats}">
									<c:choose>
										<c:when test="${etat==viewBean.searchModel.inEtats}">
											<option selected="selected" value="${etat}"><ct:FWCodeLibelle csCode="${etat}"/></option>
										</c:when>
										<c:otherwise>
											<option value="${etat}"><ct:FWCodeLibelle csCode="${etat}"/></option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
							<select id="searchModel.forEtatTaxation" name="etatTaxation">
								<option value=""></option>
								<c:forEach var="etatTaxation" items="${viewBean.typesTaxationOffice}">
									<c:choose>
										<c:when test="${etatTaxation==viewBean.searchModel.forEtatTaxation}"></c:when>
										<c:otherwise>
											<option value="${etatTaxation}"><ct:FWCodeLibelle csCode="${etatTaxation}"/></option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</td>
						<td><ct:FWLabel key="JSP_TYPE"/></td>
						<td>
							<select id="searchModel.forType" name="type">
								<option value=""></option>
								<c:forEach var="type" items="${viewBean.types}">
									<c:choose>
										<c:when test="${type==viewBean.searchModel.forType}">
											<option selected="selected" value="${type}"><ct:FWCodeLibelle csCode="${type}"/></option>
										</c:when>
										<c:otherwise>
											<option value="${type}"><ct:FWCodeLibelle csCode="${type}"/></option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_TYPE_PROVENANCE"/></td>
						<td>
							<select id="searchModel.forTypeProvenance" name="typeProvenance">
								<option value="" selected></option>
								<c:forEach var="typeProvenance" items="${viewBean.typesProvenance}">
									<c:choose>
										<c:when test="${typeProvenance==0}">
											<option value="${typeProvenance}"><ct:FWLabel key="JSP_TYPE_PROVENANCE_GENERE"/></option>
										</c:when>
										<c:otherwise>
											<option value="${typeProvenance}"><ct:FWCodeLibelle csCode="${typeProvenance}"/></option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</td>
						<td></td>
						<td></td>
					</tr>
				</table>
			</div>
		<%@ include file="/theme/find_ajax_el/searchNewButtons.jspf" %>
		<div align="left"  style="padding-right:8px;padding-bottom:10px;background-color: white;">
			<input type="button" id="boutonSommation" value="${viewBean.libelleBoutonSommation}">
			<input type="button" id="boutonTO" value='<ct:FWLabel key="JSP_GENERER_LISTE_TO_PREVISIONNELLE"/>' />
		</div>
		
		
		
		
		<div>
			<table class="areaTable" width="100%">
				<thead>
					<tr>
						<th></th>
						<th data-orderkey="id"><ct:FWLabel key="JSP_ID"/></th>
						<th data-orderkey="noDecompte"><ct:FWLabel key="JSP_NO_DECOMPTE"/></th>
						<th data-orderkey="noAffilie"><ct:FWLabel key="JSP_NO_AFFILIE"/></th>
						<th style="text-align: left;" data-orderkey="raisonSociale"><ct:FWLabel key="JSP_RAISON_SOCIALE"/></th>
						<th data-orderkey="convention"><ct:FWLabel key="JSP_CONVENTION"/></th>
						<th><ct:FWLabel key="JSP_TYPE"/></th>
						<th><ct:FWLabel key="JSP_PERIODE"/></th>
						<th><ct:FWLabel key="JSP_ETAT"/></th>
						<th data-orderkey="dateReception"><ct:FWLabel key="JSP_RECU_LE"/></th>
						<th><ct:FWLabel key="JSP_DATE_ECHEANCE"/></th>
						<th><ct:FWLabel key="JSP_MOTIF_PROLONGATION"/></th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
	</div>
	</td>
</tr>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />

<%@ include file="/theme/find_ajax_el/bodyButtons.jspf" %>

<%@ include file="/theme/find_ajax_el/bodyErrors.jspf" %>

<%@ include file="/theme/find_ajax_el/footer.jspf" %>