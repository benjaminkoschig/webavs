<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT1120"/>
<c:set var="labelTitreEcran" value="JSP_TAXATION_OFFICE"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="true" scope="page" />
<c:set var="bButtonCancel" value="true" scope="page" /> 
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="true" scope="page" />

<c:set var="employeur" value="${viewBean.employeur}" />
<c:set var="decompte" value="${viewBean.decompte}" />
<c:set var="taxationOffice" value="${viewBean.taxationOffice}" />

<c:set var="ETAT_SAISI" value="${viewBean.etatSaisi}" />
<c:set var="ETAT_VALIDE" value="${viewBean.etatValide}" />



<c:choose>
   <c:when test="${taxationOffice.etat.value==ETAT_SAISI}">
        <c:set var="bButtonUpdate" value="true" scope="page" />
    </c:when>
    <c:otherwise>
        <c:set var="bButtonUpdate" value="false" scope="page" />
    </c:otherwise>
</c:choose>

<c:set var="bButtonDevalidate" value="${taxationOffice.etat.value==ETAT_VALIDE}" scope="page" />
<c:set var="bButtonPrint" value="${taxationOffice.etat.value==ETAT_VALIDE}" scope="page" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="${rootPath}/css/vulpecula.css"/>
<script type="text/javascript" src="${rootPath}/scripts/json2.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/amount.js"></script>
<script type="text/javascript" src="${rootPath}/taxationoffice/taxationofficePart.js"></script>

<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">
	globazGlobal.taxationService = '${viewBean.taxationOfficeService}';
	globazGlobal.taxationServiceImpl = '${viewBean.taxationOfficeServiceImpl}';
	globazGlobal.idTaxationOffice = '${taxationOffice.id}';
	globazGlobal.idDecompte = '${viewBean.decompte.id}';
</script>


<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<div class="content">
	<div class="blocLeft">
		<%@ include file="/vulpeculaRoot/blocs/decompte.jspf" %>
	</div>
	<div class="blocRight">
		<%@ include file="/vulpeculaRoot/blocs/employeur.jspf" %>
	</div>
	<div style="margin-top: 30px;">
		<table>
			<tr>
				<td><ct:FWLabel key="JSP_PERIODE"/></td>
				<td>
					<input name="periodeDebut" type="text" value="${taxationOffice.periodeDebutAsSwissValue}" readonly="readonly" disabled="disabled" data-g-calendar="type:month"/>
				</td>
				<td>
					<input name="periodeFin" type="text" value="${taxationOffice.periodeFinAsSwissValue}" readonly="readonly" disabled="disabled" data-g-calendar="type:month"/>
				</td>
			</tr>
			<tr>
				<td><ct:FWLabel key="JSP_PASSAGE_FACTURATION"/></td>
				<td>		
					<input id="designationPassageFacturation" class="jadeAutocompleteAjax" name="designationPassageFacturation" type="text" value="${viewBean.designationPassageFacturation}"
						data-g-autocomplete="
							service:¦${viewBean.passageViewService}¦,
							method:¦searchPassageFacturationTO¦,
							criterias:¦{'forIdPassage': 'id passage', 'likeLibellePassage': 'libellé passage'}¦,
							lineFormatter:¦#{passageModel.idPassage} - #{passageModel.libellePassage}¦,
							modelReturnVariables:¦passageModel.idPassage,passageModel.libellePassage¦,
							functionReturn:¦
								function(element){
									this.value=$(element).attr('passageModel.idPassage')+'-'+$(element).attr('passageModel.libellePassage');
									$('#passageFacturation').val($(element).attr('passageModel.idPassage'));
								}
						¦,
						nbOfCharBeforeLaunch:¦3¦,
						mandatory:true"
					/>		
				</td>
			</tr>
			
			<tr>
				<td><ct:FWLabel key="JSP_ETAT"/></td>
				<td colspan="2">
					<ct:FWCodeLibelle csCode="${taxationOffice.etatValue}"/>
				</td>
			</tr>
		</table>
	</div>
	
	<div style="margin-top: 20px;">
		<table>
			<thead>
				<tr>
					<th><ct:FWLabel key="JSP_COTISATIONS"/></th>
					<th><ct:FWLabel key="JSP_MASSE"/></th>
					<th><ct:FWLabel key="JSP_TAUX"/></th>
					<th><ct:FWLabel key="JSP_MONTANT_DE_LA_COTISATION"/></th>
				</tr>
			</thead>
			<tbody>		
				<c:forEach var="ligneTaxation" items="${viewBean.lignesTaxationViews}" varStatus="status">
					<c:choose>
						<c:when test="${status.index%2==0}">
							<tr class="bmsRowOdd ligneTaxation">
						</c:when>
						<c:otherwise>
							<tr class="bmsRowEven ligneTaxation">
						</c:otherwise>					
					</c:choose>
						<input type="hidden" class="id" value="${ligneTaxation.id}" />
						<td>${ligneTaxation.cotisation}</td>
						<td><input type="text" class="masse" value="${ligneTaxation.masse}" data-g-amount="unsigned:true"  /></td>
						<td class="taux">${ligneTaxation.taux}</td>
						<td><input type="text" class="montant_cotisation" value="${ligneTaxation.montant}" data-g-amount="unsigned:true" /></td>
					</tr>
				</c:forEach>
				<tr class="bmsRowEven">
					<td style="font-weight : bold"><ct:FWLabel key="JSP_TOTAL"/></td>
					<td><input id="totalMasse" value="${taxationOffice.masse}" disabled="disabled" readonly="readonly" data-g-amount="unsigned:true"  /></td>
					<td>${taxationOffice.taux}</td>
					<td><input id="totalMontant" value="${taxationOffice.montant}" disabled="disabled" readonly="readonly" data-g-amount="unsigned:true"  /></td>
				</tr>
			</tbody>
		</table>
		<input id="lignesTaxation" name="lignesTaxation" type="hidden" />
		<input id="passageFacturation" name="idPassageFacturation" type="hidden" value="${taxationOffice.idPassageFacturation}" />
	</div>
</div>
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
					
<%--SURCHARGE DES BOUTONS afin de pouvoir ajouter un custom --%>
				<input type="hidden" name="selectedId" value="${selectedIdValue}">
				<input type="hidden" name="userAction" value="${userActionValue}">
				<input type="hidden" name="_method" value="${param['_method']}">
				<input type="hidden" name="_valid" value="${param['_valid']}">
				<input type="hidden" name="_sl" value="">
				<input type="hidden" name="selectorName" value="">
			</form>
	 	</div>
		
			</div>
			<c:if test="${not empty creationSpy or not empty lastModification}">
				<div class="lastModification">
						<c:out value="${creationSpy}" /> Update: <c:out value="${lastModification}" />
				</div>	
			</c:if>
			<c:choose>
				<c:when test="${autoShowErrorPopup || !vBeanHasErrors}">
				</c:when>
				<c:otherwise>
				[ <a id=\"showErrorLink\" href=\"javascript:showErrors();\">visualiser les erreurs</a> ]
				</c:otherwise>
			</c:choose>
			<c:if test="${bButtonNew || bButtonUpdate || bButtonDelete || bButtonValidate || bButtonCancel || bButtonPrint|| bButtonDevalidate}">
				<div style="background-color=#FFFFFF;height:18;text-align: right;" id="btnCtrlJade">
					<c:if test="${!viewBean.annulee}">
						<input class="btnCtrl" type="button" id="btnAnnule" value='<ct:FWLabel key="JSP_ANNULER_TO"/>' onclick="annuler()" />
					</c:if>
									
					<c:if test="${bButtonNew}">
						<input class="btnCtrl" type="button" id="btnNew" value="${btnNewLabel}" onclick="onClickNew();btnNew.onclick='';hideAllButtons();document.location.href='${actionNew}'" />
					</c:if>
					
					<c:if test="${bButtonUpdate}">
						<input class="btnCtrl" id="btnUpd" type="button" value="${btnUpdLabel}" onclick="action(UPDATE);upd();"/>
					</c:if>
				
					<c:if test="${bButtonDelete}">
						<input class="btnCtrl" id="btnDel" type="button" value="${btnDelLabel}" onclick="del();"/>
					</c:if>
					
					
					<c:if test="${bButtonValidate}">
						<input class="btnCtrl" id="btnVal" type="button" value="${btnValLabel}" onclick="if(validate()) action(COMMIT);"/>
					</c:if>
					
					<c:if test="${bButtonCancel}">
						<input class="btnCtrl" id="btnCan" type="button" value="${btnCanLabel}" onclick="cancel(); action(ROLLBACK);"/>
					</c:if>
					
					<c:if test="${bButtonPrint}">
						<input class="btnCtrl" id="btnPrt" type="button" value="${viewBean.boutonPrintLibelle}" onclick="imprimer();"/>
					</c:if>
					
					<c:if test="${bButtonDevalidate}">
						<input class="btnCtrl" id="btnDev" type="button" value="${viewBean.boutonDevalideLibelle}" onclick="devalider();"/>
					</c:if>
					
				</div>
			</c:if>
<%--FIN SURCHARGE DES BOUTONS afin de pouvoir ajouter un custom --%>

<%-- Boite de dialogue sur le clique du bouton "imprimer" --%>

<div style="display:none;" id="dialog" title="Chargement">
  <div id="progressbar"></div>
</div>
					
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />