<%-- ******************************************************************* Directives de page ****************************************************************** --%>
<%@ page language="java" contentType="text/html;charset=ISO-8859-1" %>

<%-- ******************************************************************* Insertion header ******************************************************************** --%>
<%@ include file="/theme/detail_ajax_el/header.jspf" %>

<%--  *********************************************************** Paramétrage global de la page ************************************************************** --%>
<c:set var="idEcran" value="PPT1112"/>
<c:set var="labelTitreEcran" value="JSP_DECOMPTESALAIRE"/>
<c:set var="decompte" value="${viewBean.decompte}" />
<c:set var="employeur" value="${viewBean.employeur}" />

<%-- visibiltés des boutons --%>
<c:set var="bButtonNew" value="false" scope="page" />

<%--  ********************************************************************** JS CSS ***************************************************************************--%>

<%@ include file="/theme/detail_ajax_el/javascripts.jspf" %>
<script type="text/javascript" src="${rootPath}/scripts/json2.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/ajax/DefaultDetailAjax.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/amount.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaUtils.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/vulpeculaTable.js"></script>
<script type="text/javascript" src="${rootPath}/scripts/utils/jquery.noty.packaged.min.js"></script>
<script type="text/javascript" src="${rootPath}/decomptesalaire/decomptesalaire_de.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/scripts/calendar/date.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/theme/ajax/templateZoneAjax.css"/>
<link rel="stylesheet" type="text/css" href="${rootPath}/css/vulpecula.css"/>
<script type="text/javascript">
	globazGlobal.ACTION_AJAX = "${viewBean.action}";
	globazGlobal.ID_DECOMPTE = ${viewBean.idDecompte};
	globazGlobal.ID_LIGNE_DECOMPTE = ${viewBean.id};
	globazGlobal.SEQUENCE = ${viewBean.sequence};
	globazGlobal.NAVIGATION = '';
	globazGlobal.BOUTON_VALIDER_LABEL = '${btnValLabel}';
	globazGlobal.decompteService = '${viewBean.decompteService}';
	globazGlobal.IS_EDITABLE = ${viewBean.editable};
	globazGlobal.posteTravailViewService = '${viewBean.posteTravailViewService}';
	globazGlobal.decompteSalaireViewService = '${viewBean.decompteSalaireViewService}';
	globazGlobal.decompteViewService = '${viewBean.decompteViewService}';
	globazGlobal.travailleurViewService = '${viewBean.travailleurViewService}';
	globazGlobal.csMensuel = '${viewBean.csMensuel}';
	globazGlobal.isNouveau = ${viewBean.nouveau};
	globazGlobal.isNouveauTravailleur = ${viewBean.nouveauTravailleur};
	globazGlobal.messageDialogCotisation = '${viewBean.messageDialogCotisation}';
	globazGlobal.messageDroitsAF = '${viewBean.messageDroitsAF}';
	globazGlobal.TYPE_DECOMPTE = '${viewBean.typeDecompte}';
	globazGlobal.TYPE_PERIODIQUE = '${viewBean.typePeriodique}';
	globazGlobal.TYPE_CPP = '${viewBean.typeCPP}';
	globazGlobal.IS_EBUSINESS = ${viewBean.EBusiness};
	globazGlobal.TAUXEBU = '${viewBean.tauxSaisieEbu}';
</script>
<script id="lineAbsence" type="jade/template">
	<tr statut="">
		<td>
			<ct:FWCodeSelectTag name="typeAbsence" codeType="PTABSENCE" defaut="" notation="class='typeAbsence' style='width:100%'" />
		</td>
	</tr>
</script>
<%@ include file="/theme/detail_ajax_el/bodyStart.jspf" %>
<ct:FWLabel key="${labelTitreEcran}"/>
<%@ include file="/theme/detail_ajax_el/bodyStart2.jspf" %>

<%--  ******************************************************************* Corps de la page ******************************************************************* --%>

<div class="content">
	<div class="blocLeft">
		<%@ include file="/vulpeculaRoot/blocs/decompte.jspf" %>
	</div>
	<div class="blocRight">
		<%@ include file="/vulpeculaRoot/blocs/employeur.jspf" %>
	</div>
	<div class="areaDetail">
		<div id="divDetailDecompteSalaire" class="blocLeft">
			<div style="display: inline;width: 500px;">
			<table>
				<tr>
					<td>
						<input type="hidden" id="idDecompte" value="${viewBean.idDecompte}" />
						<input type="hidden" id="decompteSalaireGSON.idDecompteSalaire" />
						<label><ct:FWLabel key="JSP_POSTE_TRAVAIL" /></label><br/>
						<input id="descriptionPosteTravail"
						class="jadeAutocompleteAjax"
						name="posteTravailWidget"
						value="${viewBean.designationPosteTravail}"
						type="text"
						data-g-autocomplete="service:¦ch.globaz.vulpecula.business.services.postetravail.PosteTravailServiceCRUD¦,
											 method:¦searchPostesActifs¦,
											 constCriterias:¦forIdEmployeur=${employeur.id}¦,
											 criterias:¦{'forIdPosteTravail':'Poste','likeNomTravailleur':'Nom','likePrenomTravailleur':'Prénom'}¦,
											 lineFormatter:¦#{posteTravailSimpleModel.id} #{travailleurComplexModel.personneEtendueComplexModel.tiers.designation1} #{travailleurComplexModel.personneEtendueComplexModel.tiers.designation2}¦,
											 modelReturnVariables:¦posteTravailSimpleModel.id,travailleurComplexModel.personneEtendueComplexModel.tiers.designation1,travailleurComplexModel.personneEtendueComplexModel.tiers.designation2,posteTravailSimpleModel.genreSalaire¦,
											 nbReturn:¦20¦,
											 functionReturn:¦
											 	function(element){
											 		var $element = $(element);
											 		var id = $element.attr('posteTravailSimpleModel.id');
											 		var nom = $element.attr('travailleurComplexModel.personneEtendueComplexModel.tiers.designation1');
											 		var prenom = $element.attr('travailleurComplexModel.personneEtendueComplexModel.tiers.designation2');
											 		var genreSalaire = $element.attr('posteTravailSimpleModel.genreSalaire');
											 		
											 		GLO.isMensuel = genreSalaire==globazGlobal.csMensuel;
											 		GLO.idPosteTravail = id;
											 		GLO.decompteSalaire.manageMensuel();
											 		
													$('#idPosteTravail').val(id);
													$('#descriptionPosteTravail').val(nom + ' ' + prenom);
													
													GLO.handleNote();
											 	}¦,
											 nbOfCharBeforeLaunch:¦3¦"
						/>
						
						<input id="idPosteTravail" name="idPosteTravail" type="hidden" value="" />
					</td>
					<td rowspan="3">
						<div class="bloc blocMedium descriptionNouveauTravailleur" style="font-size:x-small;text-align:left;color:#b64823;">
							<strong id="titleCorrelationId">Nouveau travailleur</strong>
							<table style="border-collapse: collapse; font-size:x-small;">
								<tr class="bmsRowEven"><td style="width: 40%">Nom, prénom</td><td><span id="infoTNom"></span></td></tr>
								<tr><td style="width: 40%"><ct:FWLabel key="JSP_DATE_NAISSANCE"/></td><td><span id="infoTDateNaissance"></span></td></tr>
								<tr class="bmsRowEven"><td><ct:FWLabel key="JSP_GENRE_SALAIRE"/></td><td><span id="infoTGenreSalaire"></span></td></tr>
								<tr><td><ct:FWLabel key="JSP_QUALIFICATION"/></td><td><span id="infoTQualification"></span></td></tr>
								<tr class="bmsRowEven"><td><ct:FWLabel key="JSP_ETAT_POSTE"/></td><td>nouveau travailleur</td></tr>
							</table>
						</div>
					
						<div class="bloc blocMedium descriptionTravailleur" style="font-size:x-small;text-align:left;">
							<strong id="titlePosteTravail"><ct:FWLabel key="JSP_POSTE_TRAVAIL"/></strong>
							<table style="border-collapse: collapse; font-size:x-small;">
								<tr><td style="width: 40%"><ct:FWLabel key="JSP_DATE_NAISSANCE"/></td><td><span id="infoDateNaissance"></span></td></tr>
								<tr class="bmsRowEven"><td><ct:FWLabel key="JSP_GENRE_SALAIRE"/></td><td><span id="infoGenreSalaire"></span></td></tr>
								<tr><td><ct:FWLabel key="JSP_QUALIFICATION"/></td><td><span id="infoQualification"></span></td></tr>
								<tr class="bmsRowEven"><td><ct:FWLabel key="JSP_ETAT_POSTE"/></td><td><span id="infoDateFinPoste"></span></td></tr>
								<tr><td><b><ct:FWLabel key="JSP_NOTE"/></b></td><td><span id="infoNote"></span></td></tr>
							</table>
							<table id="tableDroitActif" style="border-collapse: collapse; font-size:x-small;display:none">
								<tr class="bmsRowEven"><td colspan="2"><span style="color:red;font-weight: bold;">${viewBean.messageDroitsAF}</span></td></tr>
							</table>

						</div>
						<div class="bloc blocMedium" style="font-size:x-small;text-align:left;" id=ebusinesspart>
<!-- 							<hr/> -->
							<!--
								vert : #179a19
								orange : #b64823
								violet : #9f3db8
							 -->
							<table id="tableEBusiness" style="border-collapse: collapse; font-size:x-small;display:inline; color:#b64823">
								<tr class="bmsRowEven"><td><ct:FWLabel key="JSP_REMARQUE"/></td><td id="remarque"><span id="decompteSalaireGSON.remarque"></span></td></tr>
								<tr>
									<td id="aTraiter" style="display:none">
										<b><ct:FWLabel key="JSP_QUITTANCER"/> <input accesskey="J" title="alt + J" type="checkbox" id="decompteSalaireGSON.quittancer" /></b>
									</td>
									<td>
										<ul style="margin:0px;" id="listCodeErreurs"></ul>
									</td>
								</tr>
								<tr id="ligneSuprimmee" style="display:none">
									<td><b><ct:FWLabel key="JSP_MAJ_FIN_POSTE"/></td>
									<td><span id="libelleDateFin"></span></td>
									<td><input type="checkbox" id="decompteSalaireGSON.majFinPoste"/></b></td>
								</tr>
							</table>
						</div>					
					</td>
					<c:if test="${viewBean.isEbuAndComplementaire}">
					<td>
					<div class="bloc blocMedium" style="font-size:x-small;text-align:left;margin-top:23%;" id="complementaireEbuPart">
						<strong id="montantsSaisisEmployeur">Montants saisis par l'employeur</strong>
						<table id="tableComplementaireEbu" style="border-collapse: collapse; font-size:x-small;">
							<tr>
								<td>Vacances / Jours fériés</td>
								<td style="text-align:center;"><span id="decompteSalaireGSON.vacances"></span></td>
							</tr>
							<tr class="bmsRowEven">
								<td>Gratifications</td>
								<td style="text-align:center;"><span id="decompteSalaireGSON.gratifications"></span></td>
							</tr>
							<tr>
								<td>Absences justifiées</td>
								<td style="text-align:center;"><span id="decompteSalaireGSON.absencesJustifiees"></span>
							<tr class="bmsRowEven">
								<td>APG + Compl. SM</td>
								<td style="text-align:center;"><span id="decompteSalaireGSON.apgComplSm"></span></td>
							</tr>
						</table>
					</div>
					</td>
					</c:if>				
				</tr>
				<tr>
					<td>
						<label><ct:FWLabel key="JSP_NOMBRE_HEURES" /></label><br/>
						<input type="text" id="decompteSalaireGSON.heures" data-g-rate="nbMaxDecimal:2" value="0.00" size="6" />
						<span style="margin-left:100px;"><button id="vider" type="button" tabindex="-1" accesskey="C"><ct:FWLabel key="JSP_VIDER"/></button></span>
					</td>
				</tr>
				<tr>
					<td>
						<label><ct:FWLabel key="JSP_SALAIRE_HORAIRE" /></label>
					<br/><input type="text" id="decompteSalaireGSON.salaireHoraire" data-g-amount=" " value="" /></td>
				</tr>
				<tr>
					<td>
						<label><ct:FWLabel key="JSP_MASSE_SALARIALE" /></label><br/>
						<input type="text" id="decompteSalaireGSON.salaireTotal" data-g-amount=" " value="" /> <input type="text" data-g-amount=" " value="" id="mntCalcule" class="readOnly" readonly="readonly" tabindex="-1" />
					</td>
				</tr>
				<tr>
					<td>
						<label><ct:FWLabel key="JSP_MASSE_AC2"/></label><br />
						<input type="text" id="decompteSalaireGSON.masseAC2" data-g-amount=" " value="" />
					</td>
				</tr>
				<tr>					
					<td>
						<label><ct:FWLabel key="JSP_MASSE_FRANCHISE"/></label><br />
						<input type="text" id="decompteSalaireGSON.masseFranchise" data-g-amount=" " value="" />
					</td>
				</tr>
				<tr>					
					<td>
						<label><ct:FWLabel key="PAS_DE_FRANCHISE"/></label>&nbsp;<input type="checkBox" id="decompteSalaireGSON.forcerFranchise0" value="" />
					</td>
				</tr>
				<c:if test="${decompte.CPP}">
					<tr>
						<td>
							<label><ct:FWLabel key="JSP_ANNEE_COTISATIONS_POUR_TAUX"/></label><br />
							<input type="text" id="decompteSalaireGSON.anneeCotisations" data-g-integer="sizeMax:4" value="" />
						</td>
					</tr>
				</c:if>
				<tr>
					<td>
					<table><tr><td>
						<label><ct:FWLabel key="JSP_PERIODE_DEBUT"/></label><br />
						<c:choose>
							<c:when test="${viewBean.nouveau}">
								<input id="decompteSalaireGSON.periodeDebut" value="${viewBean.periodeDebutForNewDecompte}" type="text" data-g-calendar="" />
							</c:when>
							<c:otherwise>
								<input id="decompteSalaireGSON.periodeDebut" type="text" data-g-calendar="" />
							</c:otherwise>
						</c:choose>
						</td><td>&nbsp;</td><td>
						<label><ct:FWLabel key="JSP_PERIODE_FIN"/></label><br />
						<c:choose>
							<c:when test="${viewBean.nouveau}">
								<input id="decompteSalaireGSON.periodeFin" value="${viewBean.periodeFinForNewDecompte}" type="text" data-g-calendar="" />
							</c:when>
							<c:otherwise>
								<input id="decompteSalaireGSON.periodeFin" type="text" data-g-calendar="" />
							</c:otherwise>
						</c:choose>
						</td></tr></table>
					</td>
				</tr>			
				<tr>
					<td>
						<label><ct:FWLabel key="JSP_TOTAL_TAUX_CONTRIB" /></label>
						<c:if test="${viewBean.isEbu}">
							&nbsp;&nbsp;&nbsp;<label><ct:FWLabel key="JSP_TOTAL_TAUX_PORTAIL" /></label>
						</c:if>
					<span style="margin-right:70px">
					<br/><input type="text" id="decompteSalaireGSON.tauxContribuable" data-g-rate="nbMaxDecimal:2" value="" class="readOnly" readonly="readonly" tabindex="-1" size="4" />
					</span>						
					<c:if test="${viewBean.isEbu}">														
						<input type="text" id="decompteSalaireGSON.tauxSaisieEbu" data-g-rate="nbMaxDecimal:2" value="" class="readOnly" readonly="readonly" tabindex="-1" size="4" />										
					</c:if>
					</td>
				</tr>
				<tr>
					<td>
						<label><ct:FWLabel key="JSP_SEQUENCE" /></label>
						<br/><input type="text" id="decompteSalaireGSON.sequence" value="" class="readOnly" readonly="readonly" tabindex="-1" size="4" />
						<input type="hidden" id="decompteSalaireGSON.isEnErreur" value=""/>
					</td>
				</tr>
			</table>
			</div>

		<div style="clear: both;"></div>
		<hr />
		<div id="btnCtrlJade">
			<span style="margin:20px 10px 10px 0px; text-align:left; float:left;">
				<c:if test="${viewBean.editable}"><c:if test="${bButtonDelete}">
				<input class="btnCtrl btnAjaxDelete" style="color: #900; font-weight: bold;" id="btnAjaxDelete" type="button" value="${btnDelLabel}" accesskey="S" title='Alt + S'/>
				</c:if></c:if>
			</span>
			<span style="margin:20px 10px 10px 0px; text-align:left; float:right;">
			<button class="btnCtrl" type="button" id="btnPrevious" title='<ct:FWLabel key="JSP_INFOBULLE_NAVIGATION_GAUCHE" />' style="vertical-align:middle; font-size:1em; font-weight:bold;">&nbsp;&nbsp;&lt;&nbsp;&nbsp;</button>
			<button class="btnCtrl" type="button" id="btnNext" title='<ct:FWLabel key="JSP_INFOBULLE_NAVIGATION_DROITE" />' style="vertical-align:middle; font-size:1em; font-weight:bold;">&nbsp;&nbsp;&gt;&nbsp;&nbsp;</button>
			&nbsp;&nbsp;
			<c:if test="${viewBean.editable}">
			<c:if test="${bButtonNew || bButtonUpdate || bButtonDelete || bButtonValidate || bButtonCancel }">	
				<input id="passerSuivant" name="suivant" type="checkbox" /><label for="passerSuivant" title="<ct:FWLabel key="JSP_PASSER_SUIVANT_DESCRIPTION" />"><ct:FWLabel key="JSP_PASSER_SUIVANT" /></label>
				<c:if test="${bButtonNew}">
					<input class="btnCtrl" type="button" id="btnNew" value="${btnNewLabel}"  />
				</c:if>
				<c:if test="${bButtonUpdate}">
					<input class="btnCtrl btnAjaxUpdate" id="btnAjaxUpdate" type="button" value="${btnUpdLabel}" accesskey="M" title='Alt + M'/>
				</c:if>
				<c:if test="${bButtonValidate}">
					<span id="posBtnValidate"></span><input class="btnCtrl btnAjaxValidate" id="btnAjaxValidate" type="button" value="${btnValLabel}" accesskey="V" title='Enter ou Alt + V'/>
				</c:if>
			</c:if>
			</c:if>
			<c:if test="${viewBean.editable}">
				<input id="btnAjaxNouveau" type="button" value='<ct:FWLabel key="JSP_NOUVEAU"/>'></input>
			</c:if>
			<a href="vulpecula?userAction=vulpecula.decomptedetail.decomptedetail.afficher&selectedId=${viewBean.idDecompte}" id="cancelBack" accesskey="A" title="<ct:FWLabel key="JSP_RETOUR_DESCRIPTION" />"><ct:FWLabel key="JSP_RETOUR" /></a>
			</span>
		</div>

		<div style="margin-top: 60px;" id="tabs">
			<ul style="background: none; border-style:none;">
				<li><a href="#cotisations"><ct:FWLabel key="JSP_COTISATION"/></a></li>
				<li><a href="#historiquesalaire"><ct:FWLabel key="JSP_SALAIRES" /></a></li>
			</ul>
			<div id="cotisations" style="width: 100%;">
				<div id="cotisationsContent"></div>
			</div>
			<div id="historiquesalaire" style="width: 100%;">
				<div id="historiquesalaireContent"></div>
			</div>
		</div>
		</div>

		<div class="blocRight" style="margin: -20px 5px 0px 5px">
			<div id="aideContext" class="bloc blocMedium" style="font-size:x-small;text-align:left;">
				<span id="help-hide" style="display: none;"></span>
				<img src="images/aide.gif" height="15px" alt="<ct:FWLabel key="JSP_AIDE" />" title="<ct:FWLabel key="JSP_AIDE" />" /><br/>
				<div id="help-show">
				<table id="tableAide" style="font-size:x-small;">
				  	<tr class="bmsRowOdd"><td><strong><ct:FWLabel key="JSP_AIDE_TITRE_TOUCHE" /></strong></td><td><strong><ct:FWLabel key="JSP_AIDE_TITRE_DESCRIPTION" /></strong></td></tr>  
				  	<tr class="bmsRowEven"><td><kbd class="keyboard-key nowrap" style="border: 1px solid #aaa; background-color: #f9f9f9; padding: 0.1em 0.3em; font-family: inherit; font-size: 1em;">&crarr; Retour</kbd></td><td><ct:FWLabel key="JSP_AIDE_VALIDER" /></td></tr>
				  	<tr class="none"><td><kbd class="keyboard-key nowrap" style="border: 1px solid #aaa; background-color: #f9f9f9; padding: 0.1em 0.3em; font-family: inherit; font-size: 1em;">Ctrl</kbd> + <kbd class="keyboard-key nowrap" style="border: 1px solid #aaa; background-color: #f9f9f9; padding: 0.1em 0.4em; font-family: inherit; font-size: 1em;">&rarr;</kbd></td><td><ct:FWLabel key="JSP_AIDE_NAVIGATION_DROITE" /></td></tr>
				  	<tr class="bmsRowEven"><td><kbd class="keyboard-key nowrap" style="border: 1px solid #aaa; background-color: #f9f9f9; padding: 0.1em 0.3em; font-family: inherit; font-size: 1em;">Ctrl</kbd> + <kbd class="keyboard-key nowrap" style="border: 1px solid #aaa; background-color: #f9f9f9; padding: 0.1em 0.3em; font-family: inherit; font-size: 1em;">&larr;</kbd></td><td><ct:FWLabel key="JSP_AIDE_NAVIGATION_GAUCHE" /></td></tr>
				  	<tr class="none"><td><kbd class="keyboard-key nowrap" style="border: 1px solid #aaa; background-color: #f9f9f9; padding: 0.1em 0.3em; font-family: inherit; font-size: 1em;">Alt</kbd> + <kbd class="keyboard-key nowrap" style="border: 1px solid #aaa; background-color: #f9f9f9; padding: 0.1em 0.4em; font-family: inherit; font-size: 1em;">M</kbd></td><td><ct:FWLabel key="JSP_AIDE_MODIFIER" /></td></tr>
				  	<tr class="bmsRowEven"><td><kbd class="keyboard-key nowrap" style="border: 1px solid #aaa; background-color: #f9f9f9; padding: 0.1em 0.3em; font-family: inherit; font-size: 1em;">Ctrl</kbd> + <kbd class="keyboard-key nowrap" style="border: 1px solid #aaa; background-color: #f9f9f9; padding: 0.1em 0.3em; font-family: inherit; font-size: 1em;">Alt</kbd> + <kbd class="keyboard-key nowrap" style="border: 1px solid #aaa; background-color: #f9f9f9; padding: 0.1em 0.3em; font-family: inherit; font-size: 1em;">+</kbd></td><td><ct:FWLabel key="JSP_AIDE_ABSENCE" /></td></tr>
				  	<tr class="none"><td><kbd class="keyboard-key nowrap" style="border: 1px solid #aaa; background-color: #f9f9f9; padding: 0.1em 0.3em; font-family: inherit; font-size: 1em;">Alt</kbd> + <kbd class="keyboard-key nowrap" style="border: 1px solid #aaa; background-color: #f9f9f9; padding: 0.1em 0.4em; font-family: inherit; font-size: 1em;">S</kbd></td><td><ct:FWLabel key="JSP_AIDE_SUPPRIMER" /></td></tr> 
				  	<tr class="bmsRowEven"><td><kbd class="keyboard-key nowrap" style="border: 1px solid #aaa; background-color: #f9f9f9; padding: 0.1em 0.3em; font-family: inherit; font-size: 1em;">Alt</kbd> + <kbd class="keyboard-key nowrap" style="border: 1px solid #aaa; background-color: #f9f9f9; padding: 0.1em 0.4em; font-family: inherit; font-size: 1em;">C</kbd></td><td><ct:FWLabel key="JSP_AIDE_VIDER" /></td></tr>
				  	<tr class="bmsRowEven"><td><kbd class="keyboard-key nowrap" style="border: 1px solid #aaa; background-color: #f9f9f9; padding: 0.1em 0.3em; font-family: inherit; font-size: 1em;">Alt</kbd> + <kbd class="keyboard-key nowrap" style="border: 1px solid #aaa; background-color: #f9f9f9; padding: 0.1em 0.4em; font-family: inherit; font-size: 1em;">J</kbd></td><td><ct:FWLabel key="JSP_AIDE_QUITTANCE" /></td></tr> 
				 	<tr class="bmsRowEven"><td colspan="2"><ct:FWLabel key="JSP_AIDE_PASSER_SUIVANT" /></td></tr>
				 </table>
				 </div>
			</div>
			<br/>
			<div id="divAbsence" class="bloc blocMedium">
				<table id="tblAbsences">
					<thead>
						<tr>
							<th><ct:FWLabel key='JSP_TYPE_ABSENCE'/></th>
						</tr>
					</thead>
					<tbody id="tblAbsencesContent">
					</tbody>
				</table>
			</div>
		</div>
	</div><!-- areaDetail -->
	<br /><br />
</div><!-- content -->

<ct:menuChange displayId="menu" menuId="vulpecula-menuprincipal" showTab="menu" />

<%--  **************************************************************** Fin Corps de la page ******************************************************************* --%>
<%@ include file="/theme/detail_ajax_el/bodyButtons.jspf"%>
<%@ include file="/theme/detail_ajax_el/bodyErrors.jspf"%>
<%@ include file="/theme/detail_ajax_el/footer.jspf"%>