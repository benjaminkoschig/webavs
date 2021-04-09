<%@page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@page import="globaz.apg.api.droits.IAPDroitLAPG"%>

<%@page import="globaz.apg.vb.droits.APDroitLAPGJointDemandeViewBean"%>
<%@page import="globaz.prestation.tools.PRIterateurHierarchique"%>
<%@page import="globaz.apg.vb.droits.APDroitLAPGJointDemandeListViewBean"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@page import="globaz.apg.application.APApplication"%>
<%@ page import="globaz.apg.properties.APProperties" %>
<%@ page import="java.util.Objects" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="globaz.apg.utils.APGUtils" %>
<%@ page import="globaz.apg.enums.APTypeDePrestation" %>
<%@ page import="globaz.apg.helpers.prestation.APPrestationHelper" %>
<%@ page import="globaz.prestation.api.PRTypeDemande" %>
<%@ page import="globaz.apg.vb.droits.APTypePresationDemandeResolver" %>
<%@ page import="globaz.apg.menu.MenuPrestation" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/list/header.jspf" %>
<%
	APDroitLAPGJointDemandeListViewBean viewBean = (APDroitLAPGJointDemandeListViewBean) request.getAttribute("viewBean");
	PRIterateurHierarchique iterH = viewBean.iterateurHierarchique();

	size = viewBean.getSize ();
	detailLink = "apg?userAction=apg.droits.droitLAPGJointDemande.actionAfficherLAPG&selectedId=";

	menuName = viewBean.getMenuName();

	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");
    PRTypeDemande typePrestation = APTypePresationDemandeResolver.resolveEnumTypePrestation(session);
%>
<script type="text/javascript">
	function afficherCacher(id) {
		if (document.all("groupe_" + id).style.display == "none") {
			document.all("groupe_" + id).style.display = "block";
			document.all("bouton_" + id).value = "-";
		} else {
			document.all("groupe_" + id).style.display = "none";
			document.all("bouton_" + id).value = "+";
		}
	}
</script>
<%@ include file="/theme/list/javascripts.jspf" %>
			<th>&nbsp;</th>
			<th><ct:FWLabel key="JSP_NO_DROIT_COURT" /></th>
			<th colspan="2"><ct:FWLabel key="JSP_DETAIL_REQUERANT" /></th>
			<th><ct:FWLabel key="JSP_DATE_DEBUT" /></th>
			<th><ct:FWLabel key="JSP_GESTIONNAIRE" /></th>
			<th><ct:FWLabel key="JSP_TYPE_DROIT" /></th>
			<th><ct:FWLabel key="JSP_ETAT_DROIT" /></th>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%
	APDroitLAPGJointDemandeViewBean courant = null;
	try {
		courant = (APDroitLAPGJointDemandeViewBean) iterH.next();
	} catch (Exception e) {
		break;
	}

	String detailMenu = detailLink + courant.getIdDroit() 
						+ "&genreService=" + courant.getGenreService() 
						+ "&" + VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + courant.getIdTiers();

	actionDetail = "parent.location.href='" + detailMenu + "'";

	if (iterH.isPositionPlusPetite()) {
%>	</tbody>
<%
	} else if (iterH.isPositionPlusGrande()) {
%>	<tbody id="groupe_<%=courant.getIdParent()%>" style="display: none;">
<%
	}
%>
<%@ include file="/theme/list/lineStyle.jspf" %>
			<td class="mtd">
<%
	if (iterH.isOrphelin()) {
%>				-&nbsp;&nbsp;
<%	} else {
		for (int idPosition = 1; idPosition < iterH.getPosition(); ++idPosition) {
%>				&nbsp;&nbsp;
<%
		}
	}
	if (typePrestation.isApg()) {
		// si APG -> plus de calcul séparé de prestations
%>				<ct:menuPopup menu="ap-optionmenudroitapg" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailMenu%>">
					<ct:menuParam key="selectedId" value="<%=courant.getIdDroit()%>" />
					<ct:menuParam key="genreService" value="<%=courant.getGenreService()%>" />
					<ct:menuParam key="forIdDroit" value="<%=courant.getIdDroit()%>" />
					<ct:menuParam key="nomPrenom" value="<%=courant.getNomPrenom()%>" />
					<ct:menuParam key="noAVS" value="<%=courant.getNoAVS()%>" />
					<ct:menuParam key="detailsAssure" value="<%=courant.getDetailRequerant()%>" />
					<ct:menuParam key="<%=VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE%>" value="<%=courant.getIdTiers()%>" />
<%
	if (!IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE.equals(courant.getEtatDroit())) {
		// pas de calculs de prestations si le droit est en attente
%>					<ct:menuExcludeNode nodeId="calculertoutesprestations" />
<%
	}
%>					<ct:menuExcludeNode nodeId="calculer" />
					<ct:menuExcludeNode nodeId="calculeracor" />
					<ct:menuExcludeNode nodeId="calculeracm" />
				</ct:menuPopup>
<%
	} else if (typePrestation.isMaternite()) {
		// sinon, maternité
%>				<ct:menuPopup menu="ap-optionmenudroitamat" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailMenu%>">
					<ct:menuParam key="selectedId" value="<%=courant.getIdDroit()%>" />
					<ct:menuParam key="genreService" value="<%=courant.getGenreService()%>" />
					<ct:menuParam key="forIdDroit" value="<%=courant.getIdDroit()%>" />
					<ct:menuParam key="nomPrenom" value="<%=courant.getNomPrenom()%>" />
					<ct:menuParam key="noAVS" value="<%=courant.getNoAVS()%>" />
					<ct:menuParam key="detailsAssure" value="<%=courant.getDetailRequerant()%>" />
					<ct:menuParam key="<%=VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE%>" value="<%=courant.getIdTiers()%>" />
<%
	if (!IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE.equals(courant.getEtatDroit())) { // pas de calculs de prestations si le droit est en attente
%>					<ct:menuExcludeNode nodeId="calculertoutesprestations" />
<%
	}
%>
<%
	if (iterH.isPere() || !APPrestationHelper.isCalculDisponibleMATCIAB(courant.loadDroit())) {
%>					<ct:menuExcludeNode nodeId="calculertoutesprestationsMATCIAB2" />
<%
	}
%>
<%
	if (!courant.hasPrestationOfGenre(APTypeDePrestation.MATCIAB2.getCodesystem())) {
		// pas de decisions MATCIAB2 si le droit ne possède pas de prestations MATCIAB2
%>					<ct:menuExcludeNode nodeId="genererlesdecicionsMATCIAB2" />
<%
	}
%>
				</ct:menuPopup>
<%
    } else if (typePrestation.isPaternite()) {
		// sinon, maternité
%>				<ct:menuPopup menu="ap-optionmenudroitapat" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailMenu%>">
				<ct:menuParam key="selectedId" value="<%=courant.getIdDroit()%>" />
				<ct:menuParam key="genreService" value="<%=courant.getGenreService()%>" />
				<ct:menuParam key="forIdDroit" value="<%=courant.getIdDroit()%>" />
				<ct:menuParam key="nomPrenom" value="<%=courant.getNomPrenom()%>" />
				<ct:menuParam key="noAVS" value="<%=courant.getNoAVS()%>" />
				<ct:menuParam key="detailsAssure" value="<%=courant.getDetailRequerant()%>" />
				<ct:menuParam key="<%=VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE%>" value="<%=courant.getIdTiers()%>" />
<%
	if (!IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE.equals(courant.getEtatDroit())) {
	// pas de calculs de prestations si le droit est en attente
%>					<ct:menuExcludeNode nodeId="calculertoutesprestations" />
<%
	}
	if (IAPDroitLAPG.CS_ETAT_DROIT_DEFINITIF.equals(courant.getEtatDroit())){
%>
				<ct:menuExcludeNode nodeId="refuser" />
				<ct:menuExcludeNode nodeId="attenteReponse" />
<%}%>
			</ct:menuPopup>
				<%
    } else if (typePrestation.isPandemie()) {
        // sinon, PandÃ©mie
    %> <ct:menuPopup menu="ap-optionmenudroitpan" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailMenu%>">
    <ct:menuParam key="selectedId" value="<%=courant.getIdDroit()%>"/>
    <ct:menuParam key="genreService" value="<%=courant.getGenreService()%>"/>
    <ct:menuParam key="forIdDroit" value="<%=courant.getIdDroit()%>"/>
    <ct:menuParam key="nomPrenom" value="<%=courant.getNomPrenom()%>"/>
    <ct:menuParam key="noAVS" value="<%=courant.getNoAVS()%>"/>
    <ct:menuParam key="detailsAssure" value="<%=courant.getDetailRequerant()%>"/>
    <ct:menuParam key="<%=VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE%>"
                  value="<%=courant.getIdTiers()%>"/>
    <%
        if (Objects.isNull(APProperties.STORAGE_APG_PANDEMIE_FOLDER.getValue()) || (APProperties.STORAGE_APG_PANDEMIE_FOLDER.getValue().isEmpty())) {
            // pas de calculs de prestations si le droit est en attente
    %> <ct:menuExcludeNode nodeId="envoyerParEmail"/>
    <%
        } if (!Arrays.asList(IAPDroitLAPG.DROITS_MODIFIABLES).contains(courant.getEtatDroit())) {
            // pas de calculs de prestations ni de mise en attente rÃ©ponse ni de mise en refus si le droit n'est pas en "attente" (attente, validÃ©, en erreur ou attente rÃ©ponse)
    %>
    <ct:menuExcludeNode nodeId="calculertoutesprestations"/>
    <ct:menuExcludeNode nodeId="refuser"/>
    <%
        } if (!Arrays.asList(IAPDroitLAPG.DROITS_MODIFIABLES).contains(courant.getEtatDroit()) || courant.getEtatDroit().equals(IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE_REPONSE)) {
    %>
    <ct:menuExcludeNode nodeId="attenteReponse"/>
    <%
        } if (!(Arrays.asList(IAPDroitLAPG.CS_ETAT_DROIT_DEFINITIF, IAPDroitLAPG.CS_ETAT_DROIT_PARTIEL).contains(courant.getEtatDroit()) && APGUtils.isGenreServiceAvecDateFin(courant.getGenreService()) && courant.getDateFinDroit().isEmpty())) {
    %> <ct:menuExcludeNode nodeId="finDeDroit"/>
    <%
        }
    %>
</ct:menuPopup>
    <%
	 } if(typePrestation.isProcheAidant()) {
        // sinon, maternité
	%>
    <ct:menuPopup menu="ap-optionmenudroitprai" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailMenu%>">
        <ct:menuParam key="selectedId" value="<%=courant.getIdDroit()%>" />
        <ct:menuParam key="genreService" value="<%=courant.getGenreService()%>" />
        <ct:menuParam key="forIdDroit" value="<%=courant.getIdDroit()%>" />
        <ct:menuParam key="nomPrenom" value="<%=courant.getNomPrenom()%>" />
        <ct:menuParam key="noAVS" value="<%=courant.getNoAVS()%>" />
        <ct:menuParam key="detailsAssure" value="<%=courant.getDetailRequerant()%>" />
        <ct:menuParam key="<%=VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE%>" value="<%=courant.getIdTiers()%>" />
        <%
            if (!IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE.equals(courant.getEtatDroit())) {
                // pas de calculs de prestations si le droit est en attente
        %> <ct:menuExcludeNode nodeId="calculertoutesprestations" />
        <%
            }
            if (IAPDroitLAPG.CS_ETAT_DROIT_DEFINITIF.equals(courant.getEtatDroit())){
        %>
        <ct:menuExcludeNode nodeId="refuser" />
        <ct:menuExcludeNode nodeId="attenteReponse" />
        <%}%>
    </ct:menuPopup>
    <%}
	if (iterH.isPere()) {
%>				<input	id="bouton_<%=courant.getIdDroit()%>"
						type="button"
						value="+" 
						onclick="afficherCacher(<%=courant.getIdDroit()%>)" />
<%
	}
%>			</td>
			<td class="mtd" nowrap onClick="<%=actionDetail%>">
				<%=courant.getIdDroit()%>&nbsp;
			</td>
<%
	if (courant.hasPostit()) {
%>			<td class="mtd" nowrap="nowrap">
				<table width="100%">
					<tr>
						<td onClick="<%=actionDetail%>">
							<%=courant.getDetailRequerant()%>&nbsp;
						</td>
						<td align="right">
							<ct:FWNote sourceId="<%=courant.getIdDroit()%>" tableSource="<%=APApplication.KEY_POSTIT_DROIT_APG_MAT%>" />
						</td>
					</tr>
				</table>
			</td>
<%
	} else {
%>			<td class="mtd" nowrap onClick="<%=actionDetail%>">
				<%=courant.getDetailRequerant()%>&nbsp;
			</td>
<%
	}
%>			
			<td class="mtd" nowrap >
				<A  href="#" onclick="window.open('<%=servletContext%><%=("/apg")%>?userAction=<%=globaz.apg.servlet.IAPActions.ACTION_DROIT_LAPG%>.actionAfficherDossierGed&amp;noAVSId=<%=courant.getNoAVS()%>&amp;serviceNameId=<%=viewBean.getSession().getApplication().getProperty(globaz.externe.IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED)%>')" ><ct:FWLabel key="JSP_GED"/></A>
			</td>
			<td class="mtd" nowrap onClick="<%=actionDetail%>">
				<%=courant.getDateDebutDroit()%>&nbsp;
			</td>
			<td class="mtd" nowrap onClick="<%=actionDetail%>">
				<%=courant.getNomPrenomGestionnaire()%>&nbsp;
			</td>
			<td class="mtd" nowrap onClick="<%=actionDetail%>">
				<%=courant.getGenreDroitLibelle()%>&nbsp;
			</td>
			<td class="mtd" nowrap onClick="<%=actionDetail%>">
				<%=courant.getEtatDroitLibelle()%>&nbsp;
			</td>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%@ include file="/theme/list/tableEnd.jspf" %>
