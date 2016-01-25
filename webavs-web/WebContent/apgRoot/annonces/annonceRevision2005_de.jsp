<%@page import="globaz.apg.servlet.IAPActions"%>
<%@page import="globaz.framework.controller.FWAction"%>
<%@page import="globaz.prestation.api.IPRDemande"%>
<%@page import="globaz.prestation.tools.PRSessionDataContainerHelper"%>
<%@page import="globaz.apg.vb.annonces.APAnnonceRevision2005ViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/detail/header.jspf" %>
<%
	idEcran="PAP0019";

	APAnnonceRevision2005ViewBean viewBean = (APAnnonceRevision2005ViewBean) session.getAttribute("viewBean");

	selectedIdValue = viewBean.getIdAnnonce();

	bButtonDelete = false;
	bButtonUpdate = bButtonUpdate && viewBean.isModifiable();
	String typePrestation = "typePrestation=" + viewBean.getTypePrestation();
	String forIdDroit  = "forIdDroit=" + viewBean.getIdDroit();
	String lienSurPrestation = request.getContextPath() + "/apg?userAction=" + IAPActions.ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT + "." + FWAction.ACTION_CHERCHER  + "&" + typePrestation + "&" + forIdDroit;
%>
<%@ include file="/theme/detail/javascripts.jspf" %>
<%
	// si APG
	if ((String) PRSessionDataContainerHelper.getData(session,PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION) == IPRDemande.CS_TYPE_APG) {
%>	<ct:menuChange displayId="menu" menuId="ap-menuprincipalapg" showTab="menu" />
	<ct:menuChange displayId="options" menuId="ap-optionsempty" />
<%
	// sinon, maternité
	} else if ((String) PRSessionDataContainerHelper.getData(session,PRSessionDataContainerHelper.KEY_CS_TYPE_PRESTATION) == IPRDemande.CS_TYPE_MATERNITE) {
%>	<ct:menuChange displayId="menu" menuId="ap-menuprincipalamat" showTab="menu" />
	<ct:menuChange displayId="options" menuId="ap-optionsempty" />
<%
	}
%>
<script type="text/javascript">

	function add() {
		document.forms[0].elements('userAction').value = "apg.annonces.annonceRevision2005.ajouter";
	}

	function upd() {
		document.forms[0].elements('userAction').value = "apg.annonces.annonceRevision2005.modifier";
	}

	function validate() {
		state = true;
		if (document.forms[0].elements('_method').value == "add") {
			document.forms[0].elements('userAction').value = "apg.annonces.annonceRevision2005.ajouter";
		} else {
			document.forms[0].elements('userAction').value = "apg.annonces.annonceRevision2005.modifier";
		}

		return state;
	}

	function cancel() {
		if (document.forms[0].elements('_method').value == "add") {
			document.forms[0].elements('userAction').value="back";
		} else {
			document.forms[0].elements('userAction').value="apg.annonces.annonceRevision2005.chercher";
		}
	}

	function del() {
		if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")) {
			document.forms[0].elements('userAction').value = "apg.annonces.annonceRevision2005.supprimer";
			document.forms[0].submit();
		}
	}

	function init(){
	}
</script>
<%@ include file="/theme/detail/bodyStart.jspf" %>
			<ct:FWLabel key="JSP_ANNONCE_REVISION_2005" />
<%@ include file="/theme/detail/bodyStart2.jspf" %>
						<tr>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_CODE_APPLICATION" />
								<input type="hidden" name="idTiers" value="<%=viewBean.getIdTiers()%>" />
							</td>
							<td>
								<input type="text" name="codeApplication" value="<%=viewBean.getCodeApplication()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_CODE_ENREGISTREMENT" />
							</td>
							<td>
								<input type="text" name="codeEnregistrement" value="<%=viewBean.getCodeEnregistrement()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_NUMERO_CAISSE_AGENCE" />
							</td>
							<td>
								<input type="text" name="numeroCaisse" value="<%=viewBean.getNumeroCaisse()%>" size="3" />
								<input type="text" name="numeroAgence" value="<%=viewBean.getNumeroAgence()%>" size="3" />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_MOIS_ANNEE_COMPTABLE" />
							</td>
							<td>
								<input type="text" name="moisAnneeComptable" value="<%=viewBean.getMoisAnneeComptable()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_CONTENU_ANNONCE" />
							</td>
							<td>
								<input type="text" name="contenuAnnonce" value="<%=viewBean.getContenuAnnonce()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_GENRE_PRESTATION" />
							</td>
							<td>
								<input type="text" name="genre" value="<%=viewBean.getGenre()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_NUMERO_COMPTE" />
							</td>
							<td>
								<input type="text" name="numeroCompte" value="<%=viewBean.getNumeroCompte()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_NUMERO_ASSURE" />
							</td>
							<td>
								<input type="text" name="numeroAssure" value="<%=globaz.commons.nss.NSUtil.formatAVSUnknown(viewBean.getNumeroAssure())%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_NUMERO_CONTROLE" />
							</td>
							<td>
								<input type="text" name="numeroControle" value="<%=viewBean.getNumeroControle()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_CANTON_ETAT_DOMICILE" />
							</td>
							<td>
								<input type="text" name="cantonEtat" value="<%=viewBean.getCantonEtat()%>" onkeypress="return filterCharForInteger(window.event);" size="3" maxlength="3" />
								<ct:FWLabel key="JSP_ANNONCES_CANTON_ETAT_DOMICILE_EX" />
							</td>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_ETAT_CIVIL" />
							</td>
							<td>
								<input type="text" name="etatCivil" value="<%=viewBean.getEtatCivil()%>" />
							</td>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_GENRE_ACTIVITE_AVANT_ENTREE_SERVICE_OU_ACCOUCHEMENT" />
							</td>
							<td>
								<input type="text" name="genreActivite" value="<%=viewBean.getGenreActivite()%>">
							</td>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_REVENU_JOURNALIER_MOYEN" />
							</td>
							<td>
								<input type="text" name="revenuMoyenDeterminant" value="<%=viewBean.getRevenuMoyenDeterminant()%>">
							</td>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_NOMBRE_ENFANTS" />
							</td>
							<td>
								<input type="text" name="nombreEnfants" value="<%=viewBean.getNombreEnfants()%>">
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_PERIODE_DE_SERVICE_DE" />
							</td>
							<td>
								<input	id="periodeDe" 
										name="periodeDe" 
										data-g-calendar=" " 
										value="<%=viewBean.getPeriodeDe()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_A" />
							</td>
							<td>
								<input	id="periodeA" 
										name="periodeA" 
										data-g-calendar=" " 
										value="<%=viewBean.getPeriodeA()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_NOMBRE_JOURS_SERVICE" />
							</td>
							<td>
								<input type="text" name="nombreJoursService" value="<%=viewBean.getNombreJoursService()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_TAUX_JOURNALIER_ALLOCATION_BASE" />
							</td>
							<td>
								<input type="text" name="tauxJournalierAllocationBase" value="<%=viewBean.getTauxJournalierAllocationBase()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_GARANTIE_IJ" />
							</td>
							<td>
								<input type="text" size="2" name="garantieIJ" value="<%=viewBean.getGarantieIJ()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_ALLOCATION_EXPLOITATION" />
							</td>
							<td>
								<input type="text" size="2" name="isAllocationExploitation" value="<%=viewBean.getIsAllocationExploitation()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_ALLOCATION_FRAIS_GARDE" />
							</td>
							<td>
								<input type="text" size="2" name="isAllocationFraisGarde" value="<%=viewBean.getIsAllocationFraisGarde()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_MONTANT_ALLOCATION_FRAIS_GARDE" />
							</td>
							<td>
								<input type="text" name="montantAllocationFraisGarde" value="<%=viewBean.getMontantAllocationFraisGarde()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_TOTAL_APG" />
							</td>
							<td>
								<input type="text" name="totalAPG" value="<%=viewBean.getTotalAPG()%>" />
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_MODE_PAIEMENT" />
							</td>
							<td>
								<input type="text" size="2" name="modePaiement" value="<%=viewBean.getModePaiement()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_NUMERO_ASSURE_PERE_ENFANT" />
							</td>
							<td>
								<input type="text" name="numeroAssurePereEnfant" value="<%=viewBean.getNumeroAssurePereEnfant()%>" />
							</td>
							<td colspan="2">
								&nbsp;
							</td>
						</tr>
						<tr>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_DEBUT_DROIT_ALLOCATION" />
							</td>
							<td>
								<input	id="dateDebutDroit" 
										name="dateDebutDroit" 
										data-g-calendar=" " 
										value="<%=viewBean.getDateDebutDroit()%>" />
							</td>
							<td>
								<ct:FWLabel key="JSP_ANNONCES_FIN_DROIT_ALLOCATION" />
							</td>
							<td>
								<input	id="dateFinDroit" 
										name="dateFinDroit" 
										data-g-calendar=" " 
										value="<%=viewBean.getDateFinDroit()%>" />
							</td>
							<td colspan="2">
								<a href="<%=lienSurPrestation%>"> 
									<ct:FWLabel key="PRESTATION"/>
								</a>
							</td>
						</tr>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%@ include file="/theme/detail/footer.jspf" %>
