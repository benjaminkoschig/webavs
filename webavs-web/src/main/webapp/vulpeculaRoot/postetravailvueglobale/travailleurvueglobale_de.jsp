<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT1103"/>
<c:set var="labelTitreEcran" value="JSP_TRAVAILLEUR_GENERALE"/>
<c:set var="tableHeight" value="0 auto" /> <%-- TODO Pas optimal sur Chrome et Firefox --%> 

<%-- visibilités des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<c:set var="bButtonCancel" value="false" scope="page" />
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<c:set var="userActionAbsenceJustifiee" value="vulpecula.absencesjustifiees" />
<c:set var="userActionCongePaye" value="vulpecula.congepaye" />
<c:set var="userActionServiceMilitaire" value="vulpecula.servicemilitaire" />
<c:set var="userActionCaisseMaladie" value="vulpecula.caissemaladie" />
<c:set var="userActionSyndicat" value="vulpecula.syndicat" />

<ct:checkRight var="hasCreateRightOnAbsenceJustifiee" element="${userActionAbsenceJustifiee}" crud="c" />
<ct:checkRight var="hasCreateRightOnCongePaye" element="${userActionCongePaye}" crud="c" />
<ct:checkRight var="hasCreateRightOnServiceMilitaire" element="${userActionServiceMilitaire}" crud="c" />
<ct:checkRight var="hasCreateRightOnCaisseMaladie" element="${userActionCaisseMaladie}" crud="c" />
<ct:checkRight var="hasCreateRightOnSyndicat" element="${userActionSyndicat}" crud="c" />

<c:set var="userActionAddPoste" value="vulpecula.postetravail.posteTravail" />

<%-- initialisation des variables --%>
<c:set var="travailleur" value="${viewBean.travailleur}" />
<c:set var="postesDeTravail" value="${viewBean.travailleur.postesTravail}" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/AbstractScalableAJAXTableZone.js"/></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/AbstractSimpleAJAXDetailZone.js"/></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultTableAjaxList.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/ajax/templateZoneAjax.css">
<link rel="stylesheet" type="text/css" href="${rootPath}/css/vulpecula.css"/>
<script type="text/javascript" src="${rootPath}/scripts/json2.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/postetravailvueglobale/travailleurvueglobale_de.js"></script>

<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">
	globazGlobal.idTravailleur = ${travailleur.id};
	globazGlobal.idTiersTravailleur = ${travailleur.idTiers};
	globazGlobal.tab = '${viewBean.tab}';
	globazGlobal.travailleurViewService = '${viewBean.travailleurViewService}';
	globazGlobal.messageEnfantsAnnonces = '${viewBean.messageEnfantsAnnonces}';
</script>

<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<span class="postItIcon" data-g-note="idExterne:${travailleur.id}, tableSource:PT_TRAVAILLEURS">
</span>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>
<div class="content">
	<div class="blocLeft">
		<div class="bloc blocMedium" name="blocVolant" style="display: none">
			<%@ include file="/vulpeculaRoot/blocs/travailleur.jspf" %>
		</div>
	</div>
	
	<div class="blocRight" name="blocVolant" style="display: none">
		<c:forEach var="posteTravail" items="${travailleur.postesTravailActifs}" varStatus="status">
			<%@ include file="/vulpeculaRoot/blocs/posteTravail.jspf" %>
		</c:forEach>
	</div>
</div>
<div>
	<div class="bloc blocMedium" name="blocVolant" style="display: none">
	<div class=header>
		<ct:FWLabel key="JSP_MYPRODIS"/>
	</div>
	<div style="text-align: center;">
		<input id="annoncerEnfants" type="button" value='<ct:FWLabel key="JSP_ANNONCER_ENFANTS"/>' />
	</div>
</div>
	
<div style="margin-top: 170px;" id="tabs">
  <ul>
    <li><a href="#postes"><ct:FWLabel key="JSP_POSTES_TRAVAIL"/></a></li>
    <li><a href="#decomptessalaires"><ct:FWLabel key="JSP_SALAIRES" /></a></li>
    <li><a href="#congespayes"><ct:FWLabel key="JSP_CONGES_PAYES" /></a></li>
    <li><a href="#absencesjustifiees"><ct:FWLabel key="JSP_CONGES_ABSENCES_JUSTIFIEES" /></a></li>
    <li><a href="#servicesmilitaires"><ct:FWLabel key="JSP_CONGES_SERVICE_MILITAIRE" /></a></li>
    <li><a href="#af"><ct:FWLabel key="JSP_CONGES_ALLOCATIONS_FAMILIALES" /></a></li>
    <li><a href="#syndicats"><ct:FWLabel key="JSP_CONGES_SYNDICATS" /></a></li>
    <li><a href="#caissesmaladies"><ct:FWLabel key="JSP_CAISSES_MALADIES" /></a></li>
  </ul>
	<div id="postes">
	  	<table id="postesTable" style="width: 100%" cellspacing="0" cellpadding="0">
	  		<thead>
	  			<tr>
	  				<th>&nbsp;</th>
	  				<th class="notSortable"><ct:FWLabel key="JSP_NO_POSTE" /></th>
	  				<th class="notSortable"><ct:FWLabel key="JSP_EMPLOYEUR"/></th>
	  				<th class="notSortable"><ct:FWLabel key="JSP_DEBUT_ACTIVITE"/> - <ct:FWLabel key="JSP_FIN_ACTIVITE"/></th>
	  				<th class="notSortable"><ct:FWLabel key="JSP_GEN"/></th>
	  				<th class="notSortable"><ct:FWLabel key="JSP_QUALIFICATION"/></th>
	  				<th class="notSortable"><ct:FWLabel key="JSP_CONVENTION"/></th>
	  			</tr>
	  			<tr>
	  				<th class="notSortable"></th>
	  				<th class="notSortable"><input class="posteSearch" style="width: 60px;" id="idPosteTravail" /></th>
	  				<th class="notSortable"><input class="posteSearch" style="width: 180px;" id="raisonSociale" /></th>
					<th></th>
	  				<th class="notSortable">
	  					<select class="posteSearch" id="genre">
	  						<option />
		  					<c:forEach var="codeSysteme" items="${viewBean.genresSalaires}">
		  						<option value="${codeSysteme.id}">${codeSysteme.code}</option>
		  					</c:forEach>
	  					</select>
	  				</th>
	  				<th class="notSortable">
	  					<select class="posteSearch" id="qualification">
	  						<option />
		  					<c:forEach var="codeSysteme" items="${viewBean.qualifications}">
		  						<option value="${codeSysteme.id}">${codeSysteme.libelle}</option>
		  					</c:forEach>
	  					</select>
	  				</th>
	  				<th class="notSortable">
	  					<select class="posteSearch" id="convention">
	  						<option />
		  					<c:forEach var="convention" items="${viewBean.conventions}">
		  						<option value="${convention.id}">${convention.code} - ${convention.designation}</option>
		  					</c:forEach>
	  					</select>
	  				</th>
  				</tr>
	  			<tr>
					<td style="text-align: left" colspan="7">
						<ct:ifhasright element="${userActionAddPoste}" crud="c">
							<a href="vulpecula?userAction=${userActionAddPoste}.afficher&_method=add&idTravailleur=${viewBean.travailleur.id}">
								<img style="margin-left: 4px;" src="images/amal/add_user.png" />
							</a>
						</ct:ifhasright>
					</td>
				</tr>
	  		</thead>
			<tbody id="postesContent">
			</tbody>
		</table>
	</div>
  <div id="decomptessalaires">
  	  	<table id="decomptessalairesTable" style="width: 100%" cellspacing="0" cellpadding="0">
	  		<thead>
	  			<tr>
	  				<th class="notSortable">&nbsp;</th>
	  				<th data-orderkey="idDecompte"><ct:FWLabel key="JSP_ID_DECOMPTE"/></th>
	  				<th data-orderkey="raisonSociale"><ct:FWLabel key="JSP_RAISON_SOCIALE"/>
	  				<th data-orderkey="noDecompte"><ct:FWLabel key="JSP_DECOMPTE" /></th>
	  				<th data-orderkey="type"><ct:FWLabel key="JSP_TYPE" /></th>
	  				<th data-orderkey="periodeSalaire" data-defaultOrder="Desc"><ct:FWLabel key="JSP_PERIODE_SALAIRE" /></th>
	  				<th data-orderkey="heures"><ct:FWLabel key="JSP_HEURES" /></th>
	  				<th data-orderkey="salaireHoraire"><ct:FWLabel key="JSP_SALAIRE_HORAIRE" /></th>
	  				<th data-orderkey="salaire"><ct:FWLabel key="JSP_SALAIRE" /></th>
	  				<th data-orderkey="taux"><ct:FWLabel key="JSP_TAUX" /></th>
	  				<th class="notSortable"><ct:FWLabel key="JSP_ABSENCES" /></th>
	  			</tr>
	  			<tr>
	  				<th class="notSortable"></th>
	  				<th class="notSortable">
	  					<input data-g-integer="" style="width:80px;text-align:center;" id="dsIdDecompte" />
	  				</th>
	  				<th class="notSortable">
	  					<input id="dsRaisonSociale" />
	  				</th>
	  				<th class="notSortable">
	  					<input data-g-integer="" style="width:120px;text-align:center;" id="dsNumeroDecompte" />
	  				</th>
	  				<th class="notSortable">
	  					<select id="dsType">
	  						<option id=""></option>
	  						<c:forEach var="codeSystem" items="${viewBean.typesDecompte}">
	  							<option value="${codeSystem.id}">${codeSystem.code}</option>
	  						</c:forEach>
	  					</select>
	  				</th>
	  				<th class="notSortable"></th>
	  				<th class="notSortable"></th>
	  				<th class="notSortable"></th>
	  				<th class="notSortable"></th>
	  				<th class="notSortable"></th>
	  				<th class="notSortable"></th>
	  			</tr>
  			</thead>
  			<tbody id="decomptessalairesContent">
  			</tbody>
  		</table>
  </div>
  <div id="congespayes">
  	<table id="congespayesTable" style="width: 100%" cellspacing="0" cellpadding="0">
		<thead>
			<tr>
				<th class="notSortable">&nbsp;</th>
				<th data-orderkey="noAffilie"><ct:FWLabel key="JSP_NO_AFFILIE" /></th>
				<th data-orderkey="raisonSociale"><ct:FWLabel key="JSP_EMPLOYEUR" /></th>
				<th data-orderkey="qualification"><ct:FWLabel key="JSP_QUA" /></th>
				<th data-orderkey="genreSalaire"><ct:FWLabel key="JSP_GEN" /></th>
				<th data-orderkey="totalSalaire"><ct:FWLabel key="JSP_TOTAL_SALAIRE" /></th>
				<th data-orderkey="montantNet"><ct:FWLabel key="JSP_MONTANT_NET" /></th>
				<th data-orderkey="periode" data-defaultOrder="Desc"><ct:FWLabel key="JSP_PERIODE" /></th>
				<th data-orderkey="beneficiaire"><ct:FWLabel key="JSP_BENEFICIAIRE_ABREGE" /></th>
				<th data-orderkey="etat"><ct:FWLabel key="JSP_ETAT" /></th>
				<th data-orderkey="idPassage"><ct:FWLabel key="JSP_LOT" /></th>
				<th data-orderkey="dateFacturation"><ct:FWLabel key="JSP_COMPTABILISATION" /></th>
			</tr>
			<tr>
				<td style="text-align: left; color: red !important;" colspan="12">
					<c:choose>
						<c:when test="${viewBean.hasDroitCP and hasCreateRightOnCongePaye and empty viewBean.passageCPErreur}">
							<a href="vulpecula?userAction=vulpecula.congepaye.congepaye.afficher&idTravailleur=${travailleur.id}&_method=add">
								<img style="width: 22px; height: 22px;" alt="<ct:FWLabel key="JSP_AJOUT_CONGE_PAYE"/>" src="images/absence.png" />
							</a>
						</c:when>
						<c:otherwise>
							${viewBean.passageCPErreur}
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</thead>
		<tbody id="congespayesContent">
		</tbody>
	</table>
  </div>
  <div id="absencesjustifiees">
  	<table id="absencesjustifieesTable" style="width: 100%" cellspacing="0" cellpadding="0">
		<thead>
			<tr>
			<th class="notSortable">&nbsp;</th>
			<th data-orderkey="noAffilie"><ct:FWLabel key="JSP_NO_AFFILIE" /></th>
			<th data-orderkey="raisonSociale"><ct:FWLabel key="JSP_EMPLOYEUR" /></th>
			<th data-orderkey="qualification"><ct:FWLabel key="JSP_QUA" /></th>
			<th data-orderkey="genreSalaire"><ct:FWLabel key="JSP_GEN" /></th>
			<th data-orderkey="dateDebut" data-defaultOrder="Desc"><ct:FWLabel key="JSP_DEBUT_ABSENCE" /></th>
			<th data-orderkey="dateFin"><ct:FWLabel key="JSP_FIN_ABSENCE" /></th>
			<th data-orderkey="genrePrestation"><ct:FWLabel key="JSP_GENRE_PRESTATIONS" /></th>
			<th data-orderkey="montantBrut"><ct:FWLabel key="JSP_MONTANT_BRUT" /></th>
			<th data-orderkey="montantAVerser"><ct:FWLabel key="JSP_MONTANT_VERSE" /></th>
			<th class="notSortable" data-orderkey="beneficiaire"><ct:FWLabel key="JSP_BENEFICIAIRE_ABREGE" /></th>
			<th data-orderkey="etat"><ct:FWLabel key="JSP_ETAT" /></th>
			<th data-orderkey="idPassage"><ct:FWLabel key="JSP_LOT" /></th>
			<th data-orderkey="dateFacturation"><ct:FWLabel key="JSP_COMPTABILISATION" /></th>
			</tr>
			<tr>
				<td style="text-align: left; color: red !important;" colspan="12">
					<c:choose>
						<c:when test="${viewBean.hasDroitAJ and hasCreateRightOnAbsenceJustifiee and empty viewBean.passageAJErreur}">
							<a href="vulpecula?userAction=vulpecula.absencesjustifiees.absencesjustifiees.afficher&idTravailleur=${travailleur.id}&_method=add">
								<img style="width: 22px; height: 22px;margin-left: 4px;" alt="<ct:FWLabel key="JSP_AJOUT_ABSENCE_JUSTIFIEE"/>" src="images/absence.png" />
							</a>
						</c:when>
						<c:otherwise>
							${viewBean.passageAJErreur}
						</c:otherwise>
					</c:choose>
				</td>
			</tr>
		</thead>
		<tbody id="absencesjustifieesContent">
		</tbody>
	</table>
  </div>
  <div id="servicesmilitaires">
  	<table id="servicesmilitairesTable" style="width: 100%" cellspacing="0" cellpadding="0">
  	<thead>
		<tr>
			<th class="notSortable">&nbsp;</th>
			<th data-orderkey="noAffilie"><ct:FWLabel key="JSP_NO_AFFILIE" /></th>
			<th data-orderkey="raisonSociale"><ct:FWLabel key="JSP_EMPLOYEUR" /></th>
			<th data-orderkey="qualification"><ct:FWLabel key="JSP_QUA" /></th>
			<th data-orderkey="genreSalaire"><ct:FWLabel key="JSP_GEN" /></th>
			<th data-orderkey="dateDebut" data-defaultOrder="Desc"><ct:FWLabel key="JSP_DEBUT_ABSENCE" /></th>
			<th data-orderkey="dateFin"><ct:FWLabel key="JSP_FIN_ABSENCE" /></th>
			<th data-orderkey="nbJours"><ct:FWLabel key="JSP_NOMBRE_JOURS" /></th>
			<th class="notSortable" data-orderkey="prestation"><ct:FWLabel key="JSP_GENRE_PRESTATIONS" /></th>
			<th data-orderkey="tauxCP"><ct:FWLabel key="JSP_COUVERTURE_APG" /> (%)</th>
			<th data-orderkey="mmontantBrut"><ct:FWLabel key="JSP_MONTANT_BRUT" /></th>
			<th data-orderkey="montantAVerser"><ct:FWLabel key="JSP_MONTANT_VERSE" /></th>
			<th class="notSortable" data-orderkey="beneficiaire"><ct:FWLabel key="JSP_BENEFICIAIRE_ABREGE" /></th>
			<th data-orderkey="etat"><ct:FWLabel key="JSP_ETAT" /></th>
			<th data-orderkey="idPassage"><ct:FWLabel key="JSP_LOT" /></th>
			<th data-orderkey="dateFacturation"><ct:FWLabel key="JSP_COMPTABILISATION" /></th>
		</tr>
		<tr>
			<td style="text-align: left; color: red !important;" colspan="17">
				<c:choose>
					<c:when test="${viewBean.hasDroitSM and hasCreateRightOnServiceMilitaire and empty viewBean.passageSMErreur}">
						<a href="vulpecula?userAction=vulpecula.servicemilitaire.servicemilitaire.afficher&idTravailleur=${travailleur.id}&_method=add">
							<img style="width: 22px; height: 22px;" alt="<ct:FWLabel key="JSP_AJOUT_SERVICE_MILITAIRE"/>" src="images/absence.png" />
						</a>
					</c:when>
					<c:otherwise>
						${viewBean.passageSMErreur}
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</thead>
	<tbody id="servicesmilitairesContent">
	</tbody>
	</table>
  </div>
  <div id="af">
  	<table id="afTable" style="width: 100%" cellspacing="0" cellpadding="0">
  		<thead>
  			<tr>
  				<th style="width: 5%;" class="notSortable">&nbsp;</th>
  				<th class="notSortable"><ct:FWLabel key="JSP_NOM_PRENOM"/></th>
  				<th class="notSortable"><ct:FWLabel key="JSP_PR"/></th>
  				<th class="notSortable"><ct:FWLabel key="JSP_NEE_LE"/></th>
  				<th class="notSortable"><ct:FWLabel key="JSP_DEBUT"/></th>
  				<th class="notSortable"><ct:FWLabel key="JSP_FIN_C"/></th>
  				<th class="notSortable"><ct:FWLabel key="JSP_MOTIF"/></th>
  				<th class="notSortable"><ct:FWLabel key="JSP_TYPE"/></th>
  			</tr>
  		</thead>
  		<tbody id="afContent">
  		</tbody>
  	</table> 
  </div>
  <div id="syndicats">
    	<table id="syndicatsTable" style="width: 100%" cellspacing="0" cellpadding="0">
	  	<thead>
			<tr>
				<th class="notSortable">&nbsp;</th>
				<th class="notSortable"><ct:FWLabel key="JSP_NO_SYNDICAT" /></th>
				<th class="notSortable"><ct:FWLabel key="JSP_SYNDICAT" /></th>
				<th data-orderkey="dateDebut" data-defaultOrder="Desc" class="notSortable"><ct:FWLabel key="JSP_DATE_DEBUT_VALIDITE" /></th>
				<th class="notSortable"><ct:FWLabel key="JSP_DATE_FIN_VALIDITE" /></th>
				<th class="notSortable"><ct:FWLabel key="JSP_SALAIRE" /></th>
			</tr>
			<tr>
				<td style="text-align: left; color: red !important;" colspan="12">
					<c:if test="${hasCreateRightOnSyndicat}">
						<a href="vulpecula?userAction=vulpecula.syndicat.syndicat.afficher&idTravailleur=${travailleur.id}&_method=add">
							<img style="width: 22px; height: 22px;" src="images/absence.png" />
						</a>
					</c:if>
				</td>
			</tr>
		</thead>
		<tbody id="syndicatsContent">
		</tbody>
	</table>
  </div>
  <div id="caissesmaladies">
  	<table id="caissesmaladiesTable" style="width: 100%" cellspacing="0" cellpadding="0">
	  	<thead>
			<tr>
				<th class="notSortable">&nbsp;</th>
				<th class="notSortable"><ct:FWLabel key="JSP_CAISSE_MALADIE" /></th>
				<th class="notSortable"><ct:FWLabel key="JSP_POSTE_TRAVAIL" /></th>
				<th class="notSortable"><ct:FWLabel key="JSP_MOIS_DEBUT" /></th>
				<th class="notSortable"><ct:FWLabel key="JSP_DATE_ANNONCE_DEBUT" /></th>
				<th class="notSortable" ><ct:FWLabel key="JSP_MOIS_FIN" /></th>
				<th class="notSortable"><ct:FWLabel key="JSP_DATE_ANNONCE_FIN" /></th>
			</tr>
			<tr>
				<td style="text-align: left; color: red !important;" colspan="12">
					<c:if test="${hasCreateRightOnCaisseMaladie}">
						<a href="vulpecula?userAction=vulpecula.caissemaladie.caissemaladie.afficher&idTravailleur=${travailleur.id}&_method=add">
							<img style="width: 22px; height: 22px;" src="images/absence.png" />
						</a>
					</c:if>
				</td>
			</tr>
		</thead>
		<tbody id="caissesmaladiesContent">
		</tbody>
	</table>
  </div>
</div>

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />
<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
				
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>