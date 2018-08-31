<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<%-- labels n° ecran et titre --%>
<c:set var="idEcran" value="PPT8003"/>
<c:set var="labelTitreEcran" value="JSP_MODIF_TRAVAILLEUR"/>

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />
<c:set var="bButtonValidate" value="false" scope="page" />
<%-- <c:set var="bButtonCancel" value="false" scope="page" /> --%>
<c:set var="bButtonDelete" value="false" scope="page"/>
<c:set var="bButtonUpdate" value="false" scope="page" />

<c:set var="travailleur" value="${viewBean.travailleur}" />
<c:set var="travailleurExistant" value="${viewBean.travailleurExistant}" />
<c:set var="employeur" value="${viewBean.employeur}" />
<c:set var="btnCanLabel" value="Retour" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>
<%@ include file="/theme/detail_el/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="${rootPath}/css/vulpecula.css"/>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/utils/jquery.noty.packaged.min.js"></script>
<script type="text/javascript" src="${rootPath}/ebusiness/modificationTravailleurDetail_de.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/json2.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/nss.js"></script>
<%--  *************************************************************** Script propre à la page **************************************************************** --%>
<script type="text/javascript">
	globazGlobal.idCorrelation = '${viewBean.correlationId}';
	globazGlobal.nouveauTravailleurService = '${viewBean.nouveauTravailleurService}';
	globazGlobal.posteTravailAjaxService = '${viewBean.posteTravailAjaxService}';
	globazGlobal.modificationTravailleurService = '${viewBean.modificationTravailleurService}';
	globazGlobal.idTravailleur = '${viewBean.idTravailleur}';
	globazGlobal.travailleurAModifier = ${viewBean.travailleurJson};
	globazGlobal.travailleurExistant = ${viewBean.travailleurExistantJson};
	globazGlobal.idTiersExistant = ${viewBean.travailleurExistant.idTiers};
	globazGlobal.idTravailleurExistant = '${viewBean.idTravailleurExistant}';
	globazGlobal.idAffiliation = ${viewBean.idAffiliation};
	globazGlobal.posteTravail = ${viewBean.posteTravailJson};
	globazGlobal.idPosteTravailExistant = '${viewBean.idPosteTravail}';
	globazGlobal.localiteBanque = "${viewBean.localiteBanque}";
	globazGlobal.iban ='${viewBean.iban}';
	globazGlobal.tauxExistant = '${viewBean.tauxOccupation}';
	globazGlobal.dateTauxExistant = '${viewBean.dateTauxOccupation}';
	globazGlobal.paysExistant = '${viewBean.isoPays}';
	globazGlobal.traite = '${viewBean.traite}';
	globazGlobal.tiersTraite = '${viewBean.tiersTraite}';
	globazGlobal.posteTraite = '${viewBean.posteTraite}';
	globazGlobal.travailleurTraite = '${viewBean.travailleurTraite}';
	globazGlobal.banqueTraite = '${viewBean.banqueTraite}';
	globazGlobal.idPortail = '${viewBean.travailleur.id}';
	globazGlobal.qualifValue = '${viewBean.posteTravail.qualification.value}';
	globazGlobal.dateDebutSwiss = '${viewBean.posteTravail.periodeActivite.dateDebut.swissValue}';
	globazGlobal.dateFinSwiss = '${viewBean.posteTravail.periodeActivite.dateFin.swissValue}';
	globazGlobal.permisTravailExistant = '${viewBean.travailleurExistant.permisTravail.value}';
	globazGlobal.telephoneExistant = '${viewBean.telephoneExistant}';
	globazGlobal.assurance = '${viewBean.travailleur.contratCollectif.assureur}';
	globazGlobal.posteCorrelationId = '${viewBean.posteCorrelationId}';
	globazGlobal.nssExistant = '${viewBean.partialNssExistant}';
	globazGlobal.telephoneFomatted = '${viewBean.telephoneFormatted}';
	globazGlobal.telephoneExistantFormatted = '${viewBean.telephoneExistantFormatted}';
	globazGlobal.isPosteReactivable = '${viewBean.isPosteReactivable}';
	globazGlobal.idLocaliteBanque = '${viewBean.travailleur.compteBancaire.idLocalite}';
	globazGlobal.dateTauxActiviteSaisi = '${viewBean.travailleur.dateTauxActivite}';
	globazGlobal.nationalite = '${viewBean.travailleur.nationalite}';
	globazGlobal.existPostePourQualif = '${viewBean.existPostePourQualif}';
	globazGlobal.isPosteReactivable = '${viewBean.isPosteReactivable}';
	globazGlobal.isAdresseNull = '${viewBean.isAdresseNull}';
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
			<td id="idTravailleur" class="bloc">
			<c:out value="${viewBean.travailleur.id}"/></td>
			<td></td>
			<td></td>
		</tr>
	</table>
</div>

<!-- divsion informations travailleur existant -->

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
					</table>
				</div>

<!-- division concernant le tiers -->
<div>
<div>
<table class="ebuFormSection">
		<tr>
		<td class="label">Status de la modification</td>
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
	<th colspan="4">Informations concernant le tiers : <ct:FWCodeLibelle csCode="${viewBean.travailleur.tiersStatus.value}" /></th>
	<tr>	
		<td class="label"><ct:FWLabel key="JSP_NSS"/></td>
		<td class="value"><nss:nssPopup avsMinNbrDigit="2" nssMinNbrDigit="2" name="nss" newnss="false" tabindex="3" value="${viewBean.nssFormate}"/></td>
		<td class="label">Tiers existant</td>
		<td class="value">${viewBean.existingTiers}</td>		
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key="JSP_NOM"/></td>
		<td class="value">
			${travailleurExistant.designation1} <br/>
			<input id="nom" name="nom" value="${travailleur.nom}" />
		</td>
		<td class="label"><ct:FWLabel key="JSP_PRENOM"/></td>
		<td class="value">
			${travailleurExistant.designation2} <br/>
			<input id="prenom" name="prenom" value="${travailleur.prenom}" />
		</td>
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key="JSP_DATE_NAISSANCE"/></td>
		<td class="value">
			${travailleurExistant.dateNaissance} <br/>
			<input id="dateNaissance" data-g-calendar value="${travailleur.dateNaissance}">
		</td>
		<td class="label"><ct:FWLabel key="JSP_SEXE"/></td>
		<td class="value">
		<ct:FWCodeLibelle csCode="${travailleurExistant.sexe}" /><br/>
			<select id="sexe" name="sexe" />
				<c:forEach var="codeSystem" items="${viewBean.sexes}">
					<c:choose>
    					<c:when test="${codeSystem.id == travailleur.sexe}">
    						<option selected value="${codeSystem.id}">${codeSystem.libelle}</option>	
    					</c:when>
    					<c:otherwise>
    						<option value="${codeSystem.id}">${codeSystem.libelle}</option>
        				</c:otherwise>
					</c:choose>
				</c:forEach>
		   </select>
		</td>
	</tr>	
	<tr>
		<td class="label"><ct:FWLabel key='JSP_ETAT_CIVIL'/></td>
		<td class="value">
			<ct:FWCodeLibelle csCode="${viewBean.travailleurExistant.etatCivil}" /><br/>
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
			<c:out value="${viewBean.travailleurExistant.pays.libelleFr}"/><br/>
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
	</tr>
	<tr>
		<td class="label">Téléphone : </td>
		<td class="value">
			${viewBean.telephoneExistant}<br/>
			<input id="telephone" name="telephone" value="${viewBean.travailleur.telephone}" />
		</td>
		<td class="label">Liens : </td>
		<td class="value">
			<a href="${pageContext.request.contextPath}\pyxis?userAction=pyxis.tiers.tiers.diriger&selectedId=${travailleurExistant.idTiers}">Tiers</a>
			&nbsp;-&nbsp;
			<a href="?userAction=vulpecula.postetravailvueglobale.travailleurvueglobale.afficher&selectedId=${travailleurExistant.id}">Travailleur</a>
		</td>
	</tr>

	<!-- header concernant l'adresse du tiers -->
	<tr>
		<th colspan="4"><ct:FWLabel key='JSP_ADRESSE' /></th>
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key="JSP_RUE"/></td><td class="value">
			${viewBean.travailleurExistant.adressePrincipale.rue} <br/>
			<input id="rue" name="rue" value="${viewBean.travailleur.adresse.rue}" />
		</td>
		<td class="label"><ct:FWLabel key="JSP_RUE_NO"/></td><td class="value">
			${viewBean.travailleurExistant.adressePrincipale.rueNumero} <br/>
			<input id="rueNumero" name="rueNumero" value="${viewBean.travailleur.adresse.rueNumero}" />
		</td>
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key="JSP_NPA"/></td><td class="value"> 
			${viewBean.travailleurExistant.adressePrincipale.npa} <br/>
			<input id="npa" name="npa" value="${viewBean.travailleur.adresse.npa}" />
		</td>
		<td class="label"><ct:FWLabel key="JSP_LOCALITE"/></td><td class="value">
			${viewBean.travailleurExistant.adressePrincipale.localite} <br/>
			<input id="localite" name="localite" value="${viewBean.travailleur.adresse.localite}" disabled="true" />
		</td>
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key="JSP_CASE_POSTALE"/></td><td class="value">
		${viewBean.travailleurExistant.adressePrincipale.casePostale} <br/>
		<input id="casePostale" name="casePostale" value="${viewBean.travailleur.adresse.casePostale}"/></td>
		<td class="label"><ct:FWLabel key="JSP_PAYS"/></td>
		<td class="value">
			${viewBean.travailleurExistant.adressePrincipale.pays} <br/>
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
	<tr>
		<td colspan="4">
			<input disabled id="submitTiersEtAdresse" class="btnCtrl" type="button" value="Accepter"/>
			<input disabled id="refuseTiersEtAdresse" class="btnCtrl" type="button" value="Refuser"/>
		</td>
	</tr>
</table>
</div>

<!-- division concernant l'adresse bancaire du tiers -->
<div>
<table class="ebuFormSection">
	<th colspan="4">Informations concernant l'adresse bancaire du tiers : <ct:FWCodeLibelle csCode="${viewBean.travailleur.banqueStatus.value}" /></th>
	<tr><td class="label">IBAN </td><td colspan="2" class="value">${viewBean.iban}<br/> <input colspan="2" size="50" id="iban" name="iban" value="${viewBean.travailleur.compteBancaire.iban}" /></td></tr>
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
	<tr>
		<td colspan="4">
			<input id="submitBanque" type="button" class="btnCtrl" value="Accepter"/>
			<input id="refuseBanque" type="button" class="btnCtrl" value="Refuser"/>
		</td>
	</tr>
</table>
</div>

<!-- division concernant le travailleur -->
<div>
<table class="ebuFormSection">
	<th colspan="4">Informations concernant le travailleur : <ct:FWCodeLibelle csCode="${viewBean.travailleur.travailleurStatus.value}" /></th>
	<tr>
		<td class="label">Nombre d'enfant(s)</td>
		<td class="value" name="nbEnfant"><c:out value="${viewBean.travailleur.nombreEnfants}"/></td>
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key='JSP_PERMIS_TRAVAIL'/></td>
		<td class="value"><ct:FWCodeLibelle csCode="${viewBean.travailleurExistant.permisTravail.value}" /><br/><ct:FWCodeSelectTag name="permisTravail" codeType="PTGENREPER" wantBlank="true" defaut="${viewBean.permisTravail.categoriePermis}"/></td>
<%-- 		<td class="label"><ct:FWLabel key='JSP_REFERENCE_PERMIS_TRAVAIL2'/></td> --%>
		<td class="value" id="tdReferencePermis">${viewBean.travailleurExistant.referencePermis}<br/><input type="text" id="referencePermis" name="referencePermis" value="<c:out value="${viewBean.permisTravail.numeroPermis}"/>" /></td>
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key="JSP_ASSUREUR"/></td>
		<td class="value" name="assureur"><c:out value="${viewBean.travailleur.contratCollectif.assureur}"/></td>
<%-- 		<td class="label"><ct:FWLabel key="JSP_OFFRE"/></td> --%>
<!-- 		<td class="value" name="souhaiteOffre"> -->
<%-- 		<c:choose> --%>
<%-- 			<c:when test="${viewBean.travailleur.contratCollectif.assuranceCombinee}">oui</c:when> --%>
<%-- 			<c:otherwise>non</c:otherwise> --%>
<%-- 		</c:choose> --%>
<!-- 		</td> -->
<!-- 	</tr> -->
	<tr>
		<td colspan="4">
			<input id="submitTravailleur" type="button" class="btnCtrl" value="Accepter"/>
			<input id="refuseTravailleur" type="button" class="btnCtrl" value="Refuser"/>
		</td>
	</tr>
</table>
</div>

<!-- division concernant le poste de travail -->
<div>
<table class="ebuFormSection">
	<th colspan="4">Informations concernant le poste de travail : <ct:FWCodeLibelle csCode="${viewBean.travailleur.posteStatus.value}" /></th>
	<c:if test = "${viewBean.isNewPoste}">
	<tr>
	<td colspan="4" style="text-align=center;background:white;color:orange;font-weight:bold">
		Annonce de nouveau poste !
	</td>
	</tr>
	</c:if>
	<tr>
		<td class="label"><ct:FWLabel key="JSP_PROFESSION"/></td>
		<td class="value" name="profession">${travailleur.profession}</td>
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key="JSP_QUALIFICATION"/></td>
		<td class="value" name="qualification">
			<ct:FWCodeLibelle csCode="${viewBean.posteTravail.qualification.value}" /><br/>
			<ct:FWCodeSelectTag name="qualification" codeType="PTQUALIFIC" wantBlank="true" defaut="${travailleur.codeProfessionnel.value}"/>
		</td>
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
			${viewBean.posteTravail.periodeActivite.dateDebut.swissValue} &nbsp;à...&nbsp; ${viewBean.posteTravail.periodeActivite.dateFin.swissValue}<br/>
			<input id="dateDebut" name="dateDebut" onchange="onChangeDateDebutActivite()" value="${travailleur.dateDebutActivite}" data-g-calendar="" />
				&nbsp;à...&nbsp;
			<input id="dateFin" name="dateFin" value="${travailleur.dateFinActivite}" data-g-calendar="" />
		</td>				
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key='JSP_GENRE_SALAIRE'/></td>
		<td class="value">
			${viewBean.posteTravail.typeSalaire}<br/>
			<ct:FWCodeSelectTag name="typeSalaire" codeType="PTGENRESAL" defaut="${travailleur.typeSalaire.value}" wantBlank="true"/>
		</td>
	</tr>
		<tr>	
		<td class="label"><ct:FWLabel key="JSP_TAUX_ACTIVITE"/></td>
		<td class="value">${viewBean.tauxOccupation}<br/>
			<input id="tauxActivite" name="tauxActivite" value="${travailleur.tauxActivite}" /></td>
		<td class="label"><ct:FWLabel key="JSP_DATE_TAUX_ACTIVITE"/></td>
		<td class="value">${viewBean.dateTauxOccupation}<br/>
			<input id="dateTauxActivite" name="dateTauxActivite" value="${travailleur.dateTauxActivite}" data-g-calendar="" />
		</td>
	</tr>
	<tr>
		<td class="label"><ct:FWLabel key="JSP_SALAIRE_BASE"/></td>
		<td class="value">${travailleur.salaire}</td>
	</tr>
	<tr>
		<td colspan="4">
			<c:choose>
				<c:when test="${viewBean.isNewPoste}">
					<input id="submitPosteTravail" class="btnCtrl" type="button" value="Nouveau"/>	
					<input id="refusePosteTravail" class="btnCtrl" type="button" value="Refuser"/>					
				</c:when>
				<c:when test="${viewBean.isPosteReactivable}">
					<input id="updatePosteTravail" class="btnCtrl" type="button" value="Modifier"/>	
					<input id="refusePosteTravail" class="btnCtrl" type="button" value="Refuser"/>					
				</c:when>
				<c:when test="${!viewBean.isPosteReactivable}">
					<input id="submitPosteTravail" class="btnCtrl" type="button" value="Nouveau"/>	
					<input id="refusePosteTravail" class="btnCtrl" type="button" value="Refuser"/>				
				</c:when>				
				<c:otherwise>
					<input id="updatePosteTravail" class="btnCtrl" type="button" value="Modifier"/>	
					<input id="submitPosteTravail" class="btnCtrl" type="button" value="Nouveau"/>	
					<input id="refusePosteTravail" class="btnCtrl" type="button" value="Refuser"/>
         		</c:otherwise>
			</c:choose>	
		</td>
	</tr>
	<c:if test="${viewBean.posteHasDateFin}">		
		<tr>
			<td class="dateFin" colspan="4">Poste inactif depuis : ${viewBean.posteTravail.periodeActivite.dateFin}</td>
		</tr>
	</c:if>
</table>
</div>
</div>

<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_el/bodyButtons.jspf" %>
		
<%@ include file="/theme/detail_el/bodyErrors.jspf" %>

<%@ include file="/theme/detail_el/footer.jspf" %>