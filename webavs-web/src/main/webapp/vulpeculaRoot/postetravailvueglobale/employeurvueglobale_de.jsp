<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT1107"/>
<c:set var="labelTitreEcran" value="JSP_EMPLOYEUR_VUE_GENERALE"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<c:set var="userActionAddPoste" value="vulpecula.postetravail.posteTravail" />
<c:set var="userActionAddDecompte" value="vulpecula.decomptenouveau.decomptenouveau" />
<c:set var="userActionAddAssociation" value="vulpecula.association.association" />

<%-- initialisation des variables --%>
<c:set var="employeur" value="${viewBean.employeur}" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<script type="text/javascript">
globazGlobal.employeurViewService = '${viewBean.employeurViewService}';
globazGlobal.idEmployeur = ${employeur.id};
globazGlobal.tab = '${viewBean.tab}';
globazGlobal.labelOui = '${viewBean.labelOui}';
globazGlobal.labelNon = '${viewBean.labelNon}';
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/AbstractScalableAJAXTableZone.js"/></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/AbstractSimpleAJAXDetailZone.js"/></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjaxList.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/ajax/templateZoneAjax.css">
<link rel="stylesheet" type="text/css" href="${rootPath}/css/vulpecula.css"/>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/json2.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/validations.js"></script>
<script type="text/javascript" src="${rootPath}/postetravailvueglobale/employeurvueglobale_de.js"></script>
<style>
table.tableAssociationProfesionnelle, .tableAssociationProfesionnelle th, .tableAssociationProfesionnelle tr {
	border: solid 1px black;
}
</style>

<%--  *************************************************************** Script propre à la page **************************************************************** --%>

<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<div class="content">
	<div class="blocLeft">
		<%@ include file="/vulpeculaRoot/blocs/employeur.jspf" %>
		
		<div style="margin-top: 12px;" class="bloc bloc400">
			<table>
				<tr>
					<td class="label">
						<label for="envoiBVRSansDecompte">
							<ct:FWLabel key="JSP_ENVOI_BVR_SANS_DECOMPTE"/>
						</label>
					</td>
					<td class="value">
						<c:choose>
							<c:when test="${employeur.bvr}">
								<input id="envoiBVRSansDecompte" name="envoiBVRSansDecompte" type="checkbox" checked="checked">
								<label for="envoiBVRSansDecompte">
									<span id="labelForEnvoiBVRSansDecompte"><ct:FWLabel key="JSP_OUI"/></span>
								</label>
							</c:when>
							<c:otherwise>
								<input id="envoiBVRSansDecompte" name="envoiBVRSansDecompte" type="checkbox">
								<label for="envoiBVRSansDecompte">
									<span id="labelForEnvoiBVRSansDecompte"><ct:FWLabel key="JSP_NON"/></span>
								</label>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="label">
						<label for="editerSansTravailleur">
							<ct:FWLabel key="JSP_EDITER_SANS_TRAVAILLEUR"/>
						</label>
					</td>
					<td class="value">
						<c:choose>
							<c:when test="${employeur.editerSansTravailleur}">
								<input id="editerSansTravailleur" name="editerSansTravailleur" type="checkbox" checked="checked">
								<label id="labelEditerSansTravailleur" for="editerSansTravailleur">
									<ct:FWLabel key="JSP_OUI"/>
								</label>
							</c:when>
							<c:otherwise>
								<input id="editerSansTravailleur" name="editerSansTravailleur" type="checkbox">
								<label id="labelEditerSansTravailleur" for="editerSansTravailleur">
									<ct:FWLabel key="JSP_NON"/>
								</label>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="label">
						<label for="typeFacturation"><ct:FWLabel key="JSP_TYPE_FACTURATION"/></label>
					</td>
					<td class="value">
						<select id="typeFacturation" name="typeFacturation">
							<c:if test="${empty employeur.typeFacturation}">
								<option selected="selected" value=""></option>
							</c:if>
							<c:forEach var="typeFacturation" items="${viewBean.typesFacturation}">
								<c:choose>
									<c:when test="${typeFacturation.id==employeur.typeFacturation.value}">
										<option selected="selected" value="${typeFacturation.id}">${typeFacturation.libelle}</option>
									</c:when>
									<c:otherwise>
										<option value="${typeFacturation.id}">${typeFacturation.libelle}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</td>
				</tr>
			</table>
		</div>
	</div>
	<div class="blocRight">
		<%@ include file="/vulpeculaRoot/blocs/employeurCaissesSociales.jspf" %>
<div class="bloc blocLarge">
		<div class="header">
			<ct:FWLabel key="JSP_CAISSE_METIER"/>
			</div>
			<table>
				<tr>
					<td style="width: 20%"  class="label"><ct:FWLabel key="JSP_NO" /></td>
					<td style="width: 80%"  class="label"><ct:FWLabel key="JSP_DESIGNATION" /></td>
				</tr>
				<tr>
					<td class="value">${viewBean.caisseMetier.codeAdministrationPlanCaisse}</td> 
					<td class="value">${viewBean.caisseMetier.libelleAdministrationPlanCaisse}</td>
				</tr>
			</table>
		</div>
	</div>
</div>
	
<div id="tabs" style="margin-top: 100px;">
  <ul>
    <li><a href="#postes"><ct:FWLabel key="JSP_POSTES_TRAVAIL"/></a></li>
    <li><a href="#decomptes"><ct:FWLabel key="JSP_DECOMPTES_TAXATIONS_OFFICE" /></a></li>
    <li><a href="#prestations"><ct:FWLabel key="JSP_PRESTATIONS" /></a></li>
    <li><a href="#associations"><ct:FWLabel key="JSP_ASSOCIATIONS" /></a></li>
    <li><a href="#facturation_association"><ct:FWLabel key="JSP_FACTURATION_ASSOCIATION" /></a></li>
  </ul>
  <div class="area" id="postes">
 	<table class="areaTable" id="postesTable" style="width: 100%" cellspacing="0" cellpadding="0">
  		<thead>
  			<tr>
  				<th class="notSortable">&nbsp;</th>
  				<th class="notSortable"><ct:FWLabel key="JSP_NO_POSTE" /></th>
  				<th class="notSortable"><ct:FWLabel key="JSP_NO_TRAV" /></th>
  				<th class="notSortable"><ct:FWLabel key="JSP_NOM_PRENOM" /></th>
  				<th class="notSortable"><ct:FWLabel key="JSP_DATE_NAIS" /></th>
  				<th class="notSortable"><ct:FWLabel key="JSP_NSS" /></th>
  				<th class="notSortable"><ct:FWLabel key="JSP_QUA" /></th>
  				<th class="notSortable"><ct:FWLabel key="JSP_GEN" /></th>
  				<th class="notSortable"><ct:FWLabel key="JSP_PERIODE_ACTIVITE" /></th>
  			</tr>
  			<tr>
  				<th></th>
  				<th class="notSortable"><input style="width: 60px;" data-g-integer="" class="posteSearch" id="idPosteTravail" type="text" /></th>
  				<th class="notSortable"><input style="width: 60px;" data-g-integer="" class="posteSearch" id="idTravailleur" type="text" /></th>
  				<th class="notSortable"><input class="posteSearch" id="nomPrenom" type="text" /></th>
  				<th class="notSortable"><input data-g-calendar="" class="posteSearch" id="dateNais" type="text" /></th>
  				<th class="notSortable"><input style="width: 140px;"  class="posteSearch" id="nss" type="text" /></th>
  				<th class="notSortable">
  					<select id="qualification" class="posteSearch">
  						<option value="" />
	  					<c:forEach var="codeSysteme" items="${viewBean.qualifications}">
	  						<option value="${codeSysteme.id}">${codeSysteme.code}</option>	
	  					</c:forEach>
  					</select>
  				</th>
  				<th class="notSortable">
  					<select id="genre" class="posteSearch">
  						<option value="" />
	  					<c:forEach var="codeSysteme" items="${viewBean.genresSalaires}">
	  						<option value="${codeSysteme.id}">${codeSysteme.code}</option>	
	  					</c:forEach>
  					</select>
  				</th>
  				<th></th>
  			</tr>
  			<tr>
  				<td style="text-align: left" colspan="10">
  					<ct:ifhasright element="${userActionAddPoste}" crud="c">
  						<a href="vulpecula?userAction=${userActionAddPoste}.afficher&_method=add&idEmployeur=${viewBean.employeur.id}">
  							<img style="margin-left:4px;" src="images/amal/add_user.png" />
	  					</a>
  					</ct:ifhasright>
  				</td>
  			</tr>
  		</thead>
  		<tbody id="postesContent">
  		</tbody>
  	</table>
  </div>
  <div class="area" id="decomptes">
  	<table id="decomptesTable" class="areaTable" style="width: 100%" cellspacing="0" cellpadding="0">
  		<thead>
  			<tr>
  				<th></th>
  				<th data-orderkey="id"><ct:FWLabel key="JSP_ID" /></th>
  				<th data-orderkey="noDecompte"><ct:FWLabel key="JSP_NO_DECOMPTE" /></th>
  				<th data-orderkey="type"><ct:FWLabel key="JSP_TYPE"/></th>
  				<th data-orderkey="periodeDebut" data-defaultOrder="Desc"><ct:FWLabel key="JSP_PERIODE"/></th>
  				<th data-orderkey="dateReception"><ct:FWLabel key="JSP_RECU_LE"/></th>
  				<th data-orderkey="datecomptabilisation" class="notSortable"><ct:FWLabel key="JSP_COMPTABILISE"/></th>
  				<th data-orderkey="dateRectification" class="notSortable"><ct:FWLabel key="JSP_RECTIFIE"/></th>
  				<th data-orderkey="montant" class="notSortable"><ct:FWLabel key="JSP_TOTAL_DES_SALAIRES"/></th>
  				<th data-orderkey="etape"><ct:FWLabel key="JSP_ETAPE"/></th>
  				<th data-orderkey="dateRappel"><ct:FWLabel key="JSP_DATE_RAPPEL"/></th>
  			</tr>
   			<tr>
  				<th class="notSortable"></th>
   				<th class="notSortable">
  					<input data-g-integer="" style="text-align: center;width:120px;" id="dIdDecompte" /> 
  				</th>
  				<th class="notSortable">
  					<input data-g-integer="" style="text-align: center;width:120px;" id="dNumeroDecompte" /> 
  				</th>
  				<th class="notSortable">
  					<select id="dType">
  						<option></option>
  						<c:forEach var="codeSystem" items="${viewBean.typesDecomptes}">
  							<option value="${codeSystem.id}">${codeSystem.libelle}</option>
  						</c:forEach>
  					</select>
  				</th>
  				<th class="notSortable"></th>
  				<th class="notSortable"></th>
  				<th class="notSortable"></th>
  				<th class="notSortable"></th>
  				<th class="notSortable"></th>
  				<th class="notSortable"></th>
  				<th class="notSortable"></th>
  			</tr>
  			<tr><!-- Ajout d'un decompte -->
  				<td style="text-align: left" colspan="10">
  					<ct:ifhasright element="${userActionAddDecompte}" crud="c">
	  					<a href="vulpecula?userAction=${userActionAddDecompte}.afficher&_method=add&idEmployeur=${viewBean.employeur.id}&designationEmployeur=${viewBean.employeur.affilieNumero},${viewBean.employeur.raisonSociale}">
	  						<img style="margin-left:4px;" src="images/amal/view_right.png" />
	  					</a>
  					</ct:ifhasright>
  				</td>
  			</tr>  			
  		</thead>
  		<tbody id="decomptesContent">
  		</tbody>
  	</table>
  </div>
  <div class="area" id="prestations">
  	<table id="prestationsTable" class="areaTable" style="width: 100%" cellspacing="0" cellpadding="0">
  	<thead>
		<th class="notSortable">&nbsp;</th>
		<th class="notSortable"><ct:FWLabel key="JSP_LOT" /></th>
		<th class="notSortable"><ct:FWLabel key="JSP_ETAT" /></th>			
  	</thead>
  	<tbody id="prestationsContent">
  	</tbody>
  	</table>
  </div>
  <div id="associations">
   	<table id="associationsTable" class="areaTable" style="width: 100%" cellspacing="0" cellpadding="0">
  		<thead>
  			<tr>
  				<th	style="width: 24px;">&nbsp;</th>
  				<th colspan="2" style="text-align:left"><ct:FWLabel key="JSP_ADHESIONS_ASSOCIATIONS_PROFESSIONNELLES" /></th>
  			</tr>
    		<tr>
  				<td style="text-align: left" colspan="10">
  					<ct:ifhasright element="${userActionAddAssociation}" crud="c">
	  					<a href="vulpecula?userAction=${userActionAddAssociation}.afficher&selectedId=${viewBean.employeur.id}&idEmployeur=${viewBean.employeur.id}&_method=upd">
	  						<img style="margin-left:4px;" src="images/amal/view_detailed.png" />
	  					</a>
  					</ct:ifhasright>
  				</td>
  			</tr>  
  		</thead>
	  	<tbody id="associationsContent">
	  		<tr>
	  			<td id="associationsWait"></td>
	  		</tr>
	  	</tbody>
  	</table>
  </div>
  <div id="facturation_association">
  </div>
</div>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>