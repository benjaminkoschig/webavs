<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" %>

<%@ page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@ page import="globaz.corvus.api.demandes.IREDemandeRente"%>
<%@ page import="globaz.corvus.application.REApplication"%>
<%@ page import="globaz.corvus.servlet.IREActions"%>
<%@ page import="globaz.corvus.vb.demandes.REDemandeRenteJointPrestationAccordeeHtmlConverter" %>
<%@ page import="globaz.corvus.vb.demandes.REDemandeRenteJointPrestationAccordeeListViewBean"%>
<%@ page import="globaz.corvus.vb.demandes.REDemandeRenteJointPrestationAccordeeViewBean"%>
<%@ page import="globaz.externe.IPRConstantesExternes"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/list/header.jspf" %>
<%
	REDemandeRenteJointPrestationAccordeeListViewBean viewBean = (REDemandeRenteJointPrestationAccordeeListViewBean) request.getAttribute("viewBean");

	size = viewBean.getSize ();

	detailLink = "corvus?userAction=" + IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".afficher&selectedId=";
	String dateDernierPaiement = viewBean.getDateDernierPaiement();

	// Les labels de cette page commence par le préfix "JSP_DRE_L"
%>
<%-- tpl:put name="zoneScripts" --%>
	<script type="text/javascript">
		//Pour détecter si plus de 100 éléments lors de l'action "Imprimer"
		var managerCount = <%=viewBean.getCount()%>;
		// on vérifie si on peut activer le bouton d'impression
		parent.checkPrintButton();
	</script>
<%-- /tpl:put --%>

	<%@ include file="/theme/list/javascripts.jspf" %>

		<%-- tpl:put name="zoneHeaders" --%>
			<th colspan="2"><ct:FWLabel key="JSP_DRE_L_EN_COURS" /></th>
			<th colspan="2"><ct:FWLabel key="JSP_DRE_R_DETAIL_REQUERANT" /></th>
			<th><ct:FWLabel key="JSP_DRE_L_TYPEDEMANDE" /></th>
			<th><ct:FWLabel key="JSP_DRE_L_DATEREC" /></th>
			<th><ct:FWLabel key="JSP_DRE_L_DATEDEB_DATEFIN" /></th>
			<th><ct:FWLabel key="JSP_DRE_L_ETAT" /></th>
			<th><ct:FWLabel key="JSP_DRE_L_GESTIONNAIRE" /></th>
			<th><ct:FWLabel key="JSP_DRE_L_NO" /></th>
		<%-- /tpl:put --%>

	<%@ include file="/theme/list/tableHeader.jspf" %>

	<%-- tpl:put name="zoneCondition" --%>
<%
	REDemandeRenteJointPrestationAccordeeViewBean courant = null;

	try {
		courant = (REDemandeRenteJointPrestationAccordeeViewBean) viewBean.get(i);
	} catch (Exception e) {
		break;
	}
	actionDetail = "parent.location.href='" + detailLink + courant.getIdDemandeRente()
				+ "&" + VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + courant.getIdTierRequerant()
				+ "'";
%>
	<%-- /tpl:put --%>

	<%@ include file="/theme/list/lineStyle.jspf" %>

		<%-- tpl:put name="zoneList" --%>
			<td class="mtd" nowrap>
				<ct:menuPopup menu="corvus-optionsdemanderentes" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + courant.getIdDemandeRente()%>">
					<ct:menuParam key="selectedId" value="<%=courant.getIdDemandeRente()%>" />
					<ct:menuParam key="noDemandeRente" value="<%=courant.getIdDemandeRente()%>" />
					<ct:menuParam key="idTierRequerant" value="<%=courant.getIdTierRequerant()%>" />
					<ct:menuParam key="<%=VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE%>" value="<%=courant.getIdTierRequerant()%>" />
					<ct:menuParam key="idRenteCalculee" value="<%=courant.getIdRenteCalculee()%>" />
					<ct:menuParam key="csTypeDemande" value="<%=courant.getCsTypeDemande()%>" />
					<ct:menuParam key="numNSS" value="<%=courant.getNoAVS()%>" />
					<ct:menuParam key="idBasesCalcul" value="" />
					<ct:menuParam key="isPreparerDecision" value="true" />
					  
				<%if(
				     (globaz.corvus.api.demandes.IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(courant.getCsTypeDemande()) ||
					 globaz.corvus.api.demandes.IREDemandeRente.CS_TYPE_DEMANDE_RENTE_API.equals(courant.getCsTypeDemande()))
					 && courant.getCsTypeCalcul().equals(IREDemandeRente.CS_TYPE_CALCUL_STANDARD)
					 &&
					 (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE.equals(courant.getCsEtatDemande()) || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(courant.getCsEtatDemande()) || IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE.equals(courant.getCsEtatDemande()))){ %>
					<ct:menuActivateNode active="true" nodeId="communicationMutationOAI"/>
				<%}else{ %>
					<ct:menuExcludeNode nodeId="communicationMutationOAI"/>  
				<%} %>
					
					
<%
	if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE.equals(courant.getCsEtatDemande())) {
%>					<ct:menuExcludeNode nodeId="calcul" />
<%
	}



	if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE.equals(courant.getCsEtatDemande()) 
		|| IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(courant.getCsEtatDemande()) 
		|| IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE.equals(courant.getCsEtatDemande()) 
		|| IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(courant.getCsEtatDemande())) {
%>					<ct:menuExcludeNode nodeId="saisieManuelle" />
<%
	}
	if (IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE.equals(courant.getCsEtatDemande())) {
%>					<ct:menuExcludeNode nodeId="preparerDecision" />
					<ct:menuExcludeNode nodeId="prepIntMoratoires" />
					<ct:menuParam key="isPreparerDecision" value="false" />
<%
	} else {
		if (!courant.isPreparationDecisionValide(dateDernierPaiement) 
			&& !IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE.equals(courant.getCsEtatDemande())) {
%>					<ct:menuExcludeNode nodeId="preparerDecision" />
					<ct:menuParam key="isPreparerDecision" value="false" />
<%
		}
	}
	if (IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE.equals(courant.getCsTypeDemande()) 
		|| IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT.equals(courant.getCsTypeDemande())) {
%>					<ct:menuExcludeNode nodeId="copierDemande" />
<%
	}
	
	if (!IREDemandeRente.CS_TYPE_DEMANDE_RENTE_INVALIDITE.equals(courant.getCsTypeDemande())){
		%><ct:menuExcludeNode nodeId="creerDemandeTransitoire" /><%
	}
	else {
		if( IREDemandeRente.CS_ETAT_DEMANDE_RENTE_ENREGISTRE.equals(courant.getCsEtatDemande()) ||
			IREDemandeRente.CS_ETAT_DEMANDE_RENTE_AU_CALCUL.equals(courant.getCsEtatDemande()) ||
			IREDemandeRente.CS_ETAT_DEMANDE_RENTE_CALCULE.equals(courant.getCsEtatDemande()) ||
			IREDemandeRente.CS_ETAT_DEMANDE_RENTE_PAYE.equals(courant.getCsEtatDemande()) ||
			IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(courant.getCsEtatDemande()) ||
		    IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE.equals(courant.getCsEtatDemande()) )
		{
			%><ct:menuExcludeNode nodeId="creerDemandeTransitoire" /><%
		}
	}

	

	if (courant.isInfoComplRenteVeuvePerdure() || courant.isInfoComplRefus()
		|| IREDemandeRente.CS_TYPE_CALCUL_PREVISIONNEL.equals(courant.getCsTypeCalcul()) 
		|| IREDemandeRente.CS_TYPE_CALCUL_BILATERALES.equals(courant.getCsTypeCalcul())) {
		if (!IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(courant.getCsEtatDemande()) 
			&& !IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE.equals(courant.getCsEtatDemande())) {
%>					<ct:menuActivateNode active="true" nodeId="terminer" />
<%
		} else {
%>					<ct:menuExcludeNode nodeId="terminer" />
<%
		}
	} else {
%>					<ct:menuExcludeNode nodeId="terminer" />
<%
	}
%>				</ct:menuPopup>
			</td>
			<td class="mtd" nowrap onClick="<%=actionDetail%>">
<%
	if (Integer.parseInt(courant.getIsEnCours()) > 0) {
%>				<img src="<%=request.getContextPath()+"/images/ok.gif"%>" />
<%
	} else {
%>				<img src="<%=request.getContextPath()+"/images/erreur.gif"%>" />
<%
	}

%>

		</td>
			<td class="mtd" nowrap onClick="<%=actionDetail%>">
				<%=REDemandeRenteJointPrestationAccordeeHtmlConverter.formatDetailRequerant(courant)%>
			</td>
			<td class="mtd">
<%
	String urlGED = servletContext + "/corvus?" 
					+ "userAction=" + IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE + ".actionAfficherDossierGed" 
					+ "&noAVSId=" + courant.getNoAVS() 
					+ "&idTiersExtraFolder=" + courant.getIdTierRequerant() 
					+ "&serviceNameId=" + viewBean.getSession().getApplication().getProperty(IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED);
%>				<a href="#" onclick="window.open('<%=urlGED%>','GED_CONSULT')">
					<ct:FWLabel key="JSP_LIEN_GED"/>
				</a>
			</td>
<%
	if (courant.hasPostIt()) {
%>			<td class="mtd" nowrap="nowrap">
				<table width="100%">
					<tr>
						<td onClick="<%=actionDetail%>">
							<%=REDemandeRenteJointPrestationAccordeeHtmlConverter.formatTypeDemande(courant)%>
							&nbsp;
						</td>
						<td align="right">
							<ct:FWNote sourceId="<%=courant.getIdTiersRequerant()%>" tableSource="<%=REApplication.KEY_POSTIT_RENTES%>" />
						</td>
					</tr>
				</table>
			</td>
<%
	} else {
%>			<td class="mtd" nowrap onClick="<%=actionDetail%>">
				<%=REDemandeRenteJointPrestationAccordeeHtmlConverter.formatTypeDemande(courant)%> 
			</td>
<%
	}
%>			<td class="mtd" nowrap onClick="<%=actionDetail%>">
				<%=courant.getDateReception()%>
			</td>
			<td class="mtd" nowrap onClick="<%=actionDetail%>">
				<%=courant.getDateDebut()%>&nbsp;-&nbsp;<%=courant.getDateFin()%>
			</td>
			<td class="mtd" nowrap onClick="<%=actionDetail%>">
				<%=courant.getISession().getCodeLibelle(courant.getCsEtatDemande())%>
			</td>
			<td class="mtd" nowrap onClick="<%=actionDetail%>">
				<%=courant.getIdGestionnaire()%>&nbsp;
			</td>
			<td class="mtd" nowrap onClick="<%=actionDetail%>">
				<%=courant.getIdDemandeRente()%>
			</td>
		<%-- /tpl:put --%>

	<%@ include file="/theme/list/lineEnd.jspf" %>

	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>

	<%@ include file="/theme/list/tableEnd.jspf" %>

<%-- /tpl:insert --%>