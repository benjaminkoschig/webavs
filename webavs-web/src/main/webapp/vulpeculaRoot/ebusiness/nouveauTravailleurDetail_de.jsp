<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT8002"/>
<c:set var="labelTitreEcran" value="JSP_ANNONCE_TRAVAILLEUR"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<%-- <c:set var="bButtonCancel" value="false" scope="page" /> --%>
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<c:set var="travailleur" value="${viewBean.travailleur}" />
<c:set var="employeur" value="${viewBean.employeur}" />
<c:set var="btnCanLabel" value="Retour" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="${rootPath}/css/vulpecula.css"/>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/utils/jquery.noty.packaged.min.js"></script>
<script type="text/javascript" src="${rootPath}/ebusiness/nouveauTravailleurDetail_de.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/json2.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/nss.js"></script>
<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">
	globazGlobal.nouveauTravailleurService = '${viewBean.nouveauTravailleurService}';
	globazGlobal.idNouveauTravailleur = '${viewBean.idTravailleur}';
	globazGlobal.idCorrelation = '${viewBean.correlationId}';
	globazGlobal.idPortail = '${viewBean.travailleur.id}';
	globazGlobal.isTraite = '${viewBean.traite}';
	globazGlobal.assurance = '${viewBean.travailleur.contratCollectif.assureur}';
	globazGlobal.posteCorrelationId = '${viewBean.posteCorrelationId}';	
	globazGlobal.dateTauxActivite = '${viewBean.travailleur.dateTauxActivite}';
	globazGlobal.idLocaliteBanque = '${viewBean.travailleur.compteBancaire.idLocalite}';
	globazGlobal.countForTiers = '${viewBean.existingTiers}';
</script>

<%@ include file="/theme/detail_el/bodyStart.jspf" %>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_el/bodyStart2.jspf" %>
<%--  ******************************************************************* Corps de la page ******************************************************************* --%>

<!-- afficher l'identifiant du portail -->
<div id="divDetailDecompteSalaire" class="blocLeft">
	<table>
		<tr>
			<td><label>N° du portail</label></td> 
			<td class="bloc">
			<c:out value="${viewBean.travailleur.id}"/></td>
			<td></td>
			<td></td>
		</tr>
	</table>
</div>

<!-- divsion informations employeur2 -->

				<div class="infoEmployeur">
					<table class="tableEmp">
					<tr><td colspan="10">
						<a href="${pageContext.request.contextPath}\vulpecula?userAction=vulpecula.postetravailvueglobale.employeurvueglobale.afficher&selectedId=${employeur.id}"><ct:FWLabel key="JSP_EMPLOYEUR"/> (${employeur.id})</a>		
						- <a href="${pageContext.request.contextPath}\naos?userAction=naos.affiliation.afficher&selectedId=${employeur.id}"><ct:FWLabel key="JSP_AFFILIATION"/></a>
						- <a href="${pageContext.request.contextPath}\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=${employeur.idTiers}"><ct:FWLabel key="JSP_TIERS"/></a>
					</td></tr>
					
						<tr>
							<td class="labelEmp"><ct:FWLabel key="JSP_EMPLOYEUR_NO_AFFILIATION"/></td>
							<td class="valueEmp">${employeur.affilieNumero}</td>
						</tr>
						<tr>
						
							<td class="labelEmp"><ct:FWLabel key="JSP_EMPLOYEUR_RAISON_SOCIALE"/></td>
							<td class="valueEmp">
								<span style="margin-right: 4px;">${employeur.raisonSociale}</span>
								<span title='<ct:FWCodeLibelle csCode="${employeur.personnaliteJuridique}" />'><ct:FWCode csCode="${employeur.personnaliteJuridique}" /></span>
							</td>
						</tr>
						<tr>
							<td class="labelEmp"><ct:FWLabel key="JSP_PERIODE_ACTIVITE"/></td>
							<td class="valueEmp">${employeur.dateDebut} - ${employeur.dateFin}</td>
						</tr>
						<tr>
							<td class="labelEmp"><ct:FWLabel key="JSP_PERIODICITE"/></td>
							<td class="valueEmp"><ct:FWCodeLibelle csCode="${employeur.periodicite}"/></td>
						</tr>
						<tr>
							<td class="labelEmp"><ct:FWLabel key="JSP_CONVENTION"/></td>
							<td class="valueEmp">${employeur.convention.designation}</td>
						</tr>
						<input type="hidden" id="idAffiliation" value="${employeur.id}" />
					</table>
				</div>

<!-- division concernant le tiers -->
<div>
<div>
<table class="ebuFormSection">
	<tr>
		<td class="label">Status de l'annonce</td>
		<c:choose>
			<c:when test="${viewBean.modifTraite.equals('VALIDE')}">
				<td style="background:white;color:green"><b>Annonce traitée manuellement</b></td>
			</c:when>
			<c:when test="${viewBean.modifTraite.equals('REFUSE')}">
				<td style="background:white;color:red"><b>Annonce refusée</b></td>
			</c:when>
			<c:when test="${viewBean.modifTraite.equals('REFU_AUTO')}">
				<td style="background:white;color:blue"><b>Annonce traitée automatiquement</b></td>
			</c:when>
			<c:otherwise>
				<td style="background:white;color:orange"><b>Annonce en cours</b></td>
			</c:otherwise>
		</c:choose>
		<td colspan="2" style="text-align:right;font-weight:bold"><a href="${pageContext.request.contextPath}\vulpecula?userAction=vulpecula.ebusiness.nouveauTravailleur.afficher">Retour aux annonces</a></td>
	</tr>
	<tr><th colspan="4"><ct:FWLabel key="JSP_INFO_TIERS"/></th></tr>
	<tr>	
		<td class="label"><ct:FWLabel key="JSP_NSS"/></td>
		<td class="value"><nss:nssPopup avsMinNbrDigit="2" nssMinNbrDigit="2" name="nss" newnss="false" tabindex="3" value="${viewBean.nssFormate}"/></td>
		<td class="label">Tiers existant</td>
		<td class="value">${viewBean.existingTiers}</td>
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key="JSP_NOM"/></td>
		<td class="value"><input id="nom" name="nom" value="${travailleur.nom}" /></td>
		<td class="label"><ct:FWLabel key="JSP_PRENOM"/></td>
		<td class="value"><input id="prenom" name="prenom" value="${travailleur.prenom}" /></td>
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key="JSP_DATE_NAISSANCE"/></td>
		<td class="value"><input id="dateNaissance" data-g-calendar value="${travailleur.dateNaissance}"</td>
		<td class="label"><ct:FWLabel key="JSP_SEXE"/></td>
		<td class="value">
			<select id="sexe" name="sexe" />
				<c:if test="${not empty travailleur.sexe}">
					<option value="${travailleur.sexe}"><ct:FWCodeLibelle csCode="${travailleur.sexe}" /></option>	
				</c:if>
				<c:forEach var="codeSystem" items="${viewBean.sexes}">
					<option value="${codeSystem.id}">${codeSystem.libelle}</option>	
				</c:forEach>
		   </select>
		</td>
	</tr>	
	<tr>
		<td class="label"><ct:FWLabel key='JSP_ETAT_CIVIL'/></td>
		<td class="value">
			<select id="etatCivil" name="etatCivil" />
				<c:if test="${not empty travailleur.etatCivil.codeSysteme}">
					<option value="${viewBean.travailleur.etatCivil.codeSysteme}"><ct:FWCodeLibelle csCode="${viewBean.travailleur.etatCivil.codeSysteme}" /></option>	
				</c:if>
				<c:forEach var="codeSystem" items="${viewBean.etatsCivils}">
					<option value="${codeSystem.id}">${codeSystem.libelle}</option>	
				</c:forEach>
		   </select>
		</td>
		<td class="label"><ct:FWLabel key='JSP_NATIONALITE'/></td>
		<td class="value">
			<select id="nationalite" name="nationalite" />
				<option value=""></option>	
				<c:forEach var="nation" items="${viewBean.pays}">
					<c:choose>
						<c:when test="${travailleur.nationalite == nation.codeIso}">
							<option value="${nation.codeIso}" selected>${nation.libelleFr}</option>
						</c:when>
						<c:otherwise>
							<option value="${nation.codeIso}">${nation.libelleFr}</option>	
						</c:otherwise>
					</c:choose>	
				</c:forEach>
			</select>
			${travailleur.nationalite}
		</td>
	<tr>
		<td class="label"><ct:FWLabel key="JSP_TELEPHONE"/></td>
		<td class="value"><input id="telephone" name="telephone" value="${viewBean.travailleur.telephone}" /></td>
		<c:if test="${!empty viewBean.idTiers and viewBean.idTiers != '0'}">
			<td class="label">Liens : </td>
			<td class="value">
				<a href="${pageContext.request.contextPath}\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=${viewBean.idTiers}">Tiers</a>
				<c:if test="${!empty viewBean.idTravailleur and viewBean.idTravailleur != '0'}">
					&nbsp;-&nbsp;
					<a href="?userAction=vulpecula.postetravailvueglobale.travailleurvueglobale.afficher&selectedId=${viewBean.idTravailleur}">Travailleur</a>
				</c:if>
			</td>
		</c:if>
	</tr>
	<tr><th colspan="4"><ct:FWLabel key="JSP_ADRESSE"/></th></tr>
	<tr>
		<td class="label"><ct:FWLabel key="JSP_RUE"/></td><td class="value"><input id="rue" name="rue" value="${viewBean.travailleur.adresse.rue}" /</td>
		<td class="label"><ct:FWLabel key="JSP_RUE_NO"/></td><td class="value"><input id="rueNumero" name="rueNumero" value="${viewBean.travailleur.adresse.rueNumero}" /></td>
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key="JSP_NPA"/></td><td class="value"> <input id="npa" name="npa" value="${viewBean.travailleur.adresse.npa}" /></td>
		<td class="label"><ct:FWLabel key="JSP_LOCALITE"/></td><td class="value"><input disabled="true" id="localite" name="localite" value="${viewBean.travailleur.adresse.localite}" /></td>
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key="JSP_CASE_POSTALE"/></td><td><input id="casePostale" name="casePostale" value="${viewBean.travailleur.adresse.casePostale}"/></td>
		<td class="label"><ct:FWLabel key="JSP_PAYS"/></td>
		<td>
			<select id="pays" name="pays" />
				<option value=""></option>	
				<c:forEach var="nation" items="${viewBean.pays}">
					<c:choose>
						<c:when test="${travailleur.adresse.pays == nation.codeIso}">
							<option value="${nation.codeIso}" selected>${nation.libelleFr}</option>
						</c:when>
						<c:otherwise>
							<option value="${nation.codeIso}">${nation.libelleFr}</option>	
						</c:otherwise>
					</c:choose>	
				</c:forEach>
			</select>
			${travailleur.adresse.pays}
        </td>
	</tr>
	</table>
</div>
<!-- division concernant l'adresse banquaire -->
<div>
	<table class="ebuFormSection">
	<tr><th colspan="4"><ct:FWLabel key="JSP_INFO_BANQUE_TIERS"/></th></tr>
	<tr><td class="label">IBAN </td><td colspan="2" class="value"><input colspan="2" size="50" id="iban" name="iban" value="${viewBean.travailleur.compteBancaire.iban}" /></td></tr>
	<tr style="display:none;">
		<c:choose>
			<c:when test="${viewBean.travailleur.compteBancaire.nom != ''}">
				<td class="label"><ct:FWLabel key="JSP_BANQUE"/></td><td class="value">${viewBean.travailleur.compteBancaire.nom}</td>
			</c:when>
			<c:otherwise>
				<td class="label"><ct:FWLabel key="JSP_BANQUE"/></td><td class="value"><ct:FWLabel key="JSP_NON_RENSEIGNE"/></td>
			</c:otherwise>		
		</c:choose>
		<c:choose>
			<c:when test="${viewBean.travailleur.compteBancaire.localite != ''}">
				<td class="label"><ct:FWLabel key="JSP_LOCALITE"/></td><td class="value">${viewBean.travailleur.compteBancaire.localite}</td>
			</c:when>
			<c:otherwise>
				<td class="label"><ct:FWLabel key="JSP_LOCALITE"/></td><td class="value"><ct:FWLabel key="JSP_NON_RENSEIGNE"/></td>
			</c:otherwise>
		</c:choose>
	</tr>
	</table>
</div>


<!-- division concernant le poste de travail -->
<div>
<table class="ebuFormSection">
	<tr><th colspan="4"><ct:FWLabel key="JSP_INFO_POSTE_TRAVAIL"/></th></tr>
	<tr>
		<td class="label"><ct:FWLabel key="JSP_PROFESSION"/></td>
		<td class="value" name="profession">${travailleur.profession}</td>
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key="JSP_QUALIFICATION"/></td>
		<td class="value" name="qualification"><ct:FWCodeSelectTag name="qualification" codeType="PTQUALIFIC" wantBlank="true" defaut="${travailleur.codeProfessionnel.value}"/></td>
		<td class="label"><ct:FWLabel key="JSP_DUREE_CONTRAT"/></td>
			<c:choose>
				<c:when test="${travailleur.dureeContrat.determinee}">
					<td class="value"><ct:FWLabel key='JSP_DUREE_CONTRAT_DETERMINE'/>&nbsp;<c:out value="${travailleur.dureeContrat.dateFin}"/></td>
				</c:when>
				<c:otherwise>
					<td class="value"><ct:FWLabel key='JSP_DUREE_CONTRAT_INDETERMINE'/></td>
				</c:otherwise>
			</c:choose>	
	</tr>
	 <tr>
		<td class="label"><label for="date"><ct:FWLabel key="JSP_PERIODE"/></label></td>
		<td class="value">
			<input id="dateDebut" name="dateDebut" value="${travailleur.dateDebutActivite}" data-g-calendar="" />&nbsp;à...&nbsp;<input id="dateFin" name="dateFin" value="${travailleur.dateFinActivite}" data-g-calendar="" />
		</td>				
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key='JSP_GENRE_SALAIRE'/></td>
		<td class="value"><ct:FWCodeSelectTag name="typeSalaire" codeType="PTGENRESAL" defaut="${travailleur.typeSalaire.value}" wantBlank="true"/></td>
	</tr>
	<tr>	
		<td class="label"><ct:FWLabel key="JSP_TAUX_ACTIVITE"/></td>
		<td class="value"><input id="tauxActivite" name="tauxActivite" value="${travailleur.tauxActivite}" /></td>
		<td class="label"><ct:FWLabel key="JSP_DATE_TAUX_ACTIVITE"/></td>
		<td class="value"><input id="dateTauxActivite" name="dateTauxActivite" value="${travailleur.dateTauxActivite}" /></td>
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key="JSP_SALAIRE_BASE"/></td>
		<td class="value">${travailleur.salaire}</td>
	</tr>
</table>
</div>
</div>

<!-- division concernant le travailleur -->
<div>
<table class="ebuFormSection">
	<tr><th colspan="4"><ct:FWLabel key="JSP_INFO_TRAVAILLEUR"/></th></tr>
	<tr>
		<td class="label">Nombre d'enfant(s)</td>
		<td class="value" name="nbEnfant"><c:out value="${viewBean.travailleur.nombreEnfants}"/></td>
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key='JSP_PERMIS_TRAVAIL'/></td>
		<td class="value"><ct:FWCodeSelectTag name="permisTravail" codeType="PTGENREPER" wantBlank="true" defaut="${viewBean.permisTravail.categoriePermis}"/></td>
		<td id="tdReferencePermis" class="value"><input type="text" id="referencePermis" name="referencePermis" value="<c:out value="${viewBean.permisTravail.numeroPermis}"/>" /></td>
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key="JSP_ASSUREUR"/></td>
		<td class="value" name="assureur"><c:out value="${viewBean.travailleur.contratCollectif.assureur}"/></td>
	</tr>
</table>
</div>

<!-- division concernant l'adresse bancaire du tiers -->
<div>
<table class="ebuFormSection">

</table>
<table style="float:right;margin-right:15%" class="validationSection">
	<tr>
	<td>
		<input id="refuseAllFields" type="button" class="btnCtrl" value="Refuser"/>
	</td>
	<td>
		<input id="submitAllFields" type="button" class="btnCtrl" value="Valider"/>
	</td>
	</tr>
</table>
</div>




<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
		
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>