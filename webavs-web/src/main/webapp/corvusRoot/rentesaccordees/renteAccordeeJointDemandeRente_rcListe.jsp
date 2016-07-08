<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.sun.xml.internal.bind.v2.schemagen.xmlschema.List"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>

<%@ page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@ page import="globaz.corvus.api.basescalcul.IRERenteAccordee"%>
<%@ page import="globaz.corvus.application.REApplication"%>
<%@ page import="globaz.corvus.db.rentesaccordees.REInformationsComptabilite"%>
<%@ page import="globaz.corvus.servlet.IREActions"%>
<%@ page import="globaz.corvus.utils.REPmtMensuel"%>
<%@ page import="globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteListViewBean"%>
<%@ page import="globaz.corvus.vb.rentesaccordees.RERenteAccordeeJointDemandeRenteViewBean"%>
<%@ page import="globaz.externe.IPRConstantesExternes"%>
<%@ page import="globaz.framework.controller.FWController"%>
<%@ page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page import="globaz.framework.util.FWCurrency"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/list/header.jspf" %>

<%
	// Les labels de cette page commence par le préfix "JSP_RAC_L"

	RERenteAccordeeJointDemandeRenteListViewBean viewBean = (RERenteAccordeeJointDemandeRenteListViewBean) request.getAttribute("viewBean");
	
	// Cet map sert à stoquer les ids des rentes accordées affichées pour éviter un double affichage
	Map<String, String> idRenteAccordeeAffichee = new HashMap<String, String>();
	
	
	
	size = viewBean.getSize ();

	detailLink = "corvus?userAction=" + IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE + ".afficher&selectedId=";

	String noDemandeRente = request.getParameter("noDemandeRente");
	String idTierRequerant = request.getParameter("idTierRequerant");

	int nbLigne = 0;
	String idTiersAdrPmtCourant = "";
	String idTiersAdrPmtLast = "";
	String compareCourant = "";
	String compartLast = "";

	boolean isValidationDecisionAuthorise  = REPmtMensuel.isValidationDecisionAuthorise(objSession);

	String dateDernierPaiement = viewBean.getDateDernierPaiement();

	FWController controllerbis = (FWController) session.getAttribute("objController");
	boolean hasOsirisReadAccess = controllerbis.getSession().hasRight("osiris.comptes.ordresVersement", FWSecureConstants.READ);
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
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<TH colspan="2"><ct:FWLabel key="JSP_RAC_L_DETAIL_REQUERANT" /></th>
			<th><ct:FWLabel key="JSP_RAC_L_PRESTATION" /></th>
			<th><ct:FWLabel key="JSP_RAC_L_PERIODE_DU_DROIT" /></th>
			<th><ct:FWLabel key="JSP_RAC_L_RETENUES_BLOCAGE" /></th>
			<th><ct:FWLabel key="JSP_RAC_L_MONTANT" /></th>
			<th><ct:FWLabel key="JSP_RAC_L_ETAT" /></th>
			<th><ct:FWLabel key="JSP_RAC_L_DATE_ECHEANCE" /></th>
			<th><ct:FWLabel key="JSP_RAC_L_NO" /></th>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>

<%
	RERenteAccordeeJointDemandeRenteViewBean courant = (RERenteAccordeeJointDemandeRenteViewBean) viewBean.get(i);
	String idRA = courant.getIdPrestationAccordee();
	if(idRenteAccordeeAffichee.containsKey(idRA)){
		// RA déjà affichée, on passe à la suivante sans l'afficher
		continue;
	}
	else {
		idRenteAccordeeAffichee.put(idRA, idRA);
	}

%>

	<%-- tpl:put name="zoneCondition" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
			<%-- tpl:put name="zoneList" --%>
<%



	String detailLinkSuite = "&noDemandeRente=" + courant.getNoDemandeRente() 
							+ "&idTierRequerant=" + courant.getIdTierRequerant()
							+ "&" + VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + courant.getIdTiersBeneficiaire();

	String detailUrl = "parent.location.href='" + detailLink + courant.getIdPrestationAccordee() + detailLinkSuite + "'";

	idTiersAdrPmtCourant = courant.getIdTiersAdressePmt();
	compareCourant = idTiersAdrPmtCourant;

	if (!(compareCourant).equals((compartLast)) && nbLigne > 0) {
%>			<tr>
				<td	class="mtd" 
					height="1" 
					colspan="11" 
					background="<%=request.getContextPath()+"/images/barre.jpg"%>">
				</td>
			</tr>
			<tr	class="<%=rowStyle%>" 
				onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'rowHighligthed')" 
				onMouseOut="jscss('swap', this, 'rowHighligthed', '<%=rowStyle%>')">
<%
	}
%>				<td class="mtd" nowrap>
					<ct:menuPopup menu="corvus-optionsrentesaccordees" detailLabelId="MENU_OPTION_DETAIL">
						<ct:menuSetAllParams key="selectedId" value="<%=courant.getIdPrestationAccordee()%>"/>
						<ct:menuParam key="idRenteAccordee" value="<%=courant.getIdPrestationAccordee()%>" />
						<ct:menuParam key="selectedId" value="<%=courant.getIdPrestationAccordee()%>" />
						<ct:menuParam key="noDemandeRente" value="<%=courant.getNoDemandeRente()%>" />
						<ct:menuParam key="idTierRequerant" value="<%=courant.getIdTierRequerant()%>" />
						<ct:menuParam key="idRenteCalculee" value="<%=courant.getIdRenteCalculee()%>" />
						<ct:menuParam key="montantRenteAccordee" value="<%=courant.getMontantPrestation()%>" />
						<ct:menuParam key="idTiersBeneficiaire" value="<%=courant.getIdTiersBeneficiaire()%>" />
						<ct:menuParam key="<%=VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE%>" value="<%=courant.getIdTiersBeneficiaire()%>" />
						<ct:menuParam key="idBaseCalcul" value="<%=courant.getIdBaseCalcul()%>" />
						<ct:menuParam key="csTypeBasesCalcul" value="<%=courant.getCsTypeBasesCalcul()%>" />
						<ct:menuParam key="csEtatRenteAccordee" value="<%=courant.getCsEtat()%>" />
						<ct:menuParam key="dateFinDroit" value="<%=courant.getDateFinDroit()%>" />
						<ct:menuParam key="isPreparationDecisionValide" value="<%=Boolean.toString(courant.isPreparationDecisionValide(dateDernierPaiement))%>" />
<%
		if ((IRERenteAccordee.CS_ETAT_AJOURNE.equals(courant.getCsEtat())
			|| IRERenteAccordee.CS_ETAT_CALCULE.equals(courant.getCsEtat())
			|| IRERenteAccordee.CS_ETAT_DIMINUE.equals(courant.getCsEtat()))
			|| (!JadeStringUtil.isBlankOrZero(courant.getDateFinDroit()))
			|| !isValidationDecisionAuthorise) {
%>						<ct:menuExcludeNode nodeId="optdiminution" />
<%
		}
		if (courant.isAfficherRepriseDuDroit()) {
%>						<ct:menuActivateNode nodeId="repriseDroit"  active="yes" />
<%
		} else {
%>						<ct:menuExcludeNode nodeId="repriseDroit" />
<%
		}
		
		if (courant.isAnnulerDiminutionAuthorise()) {
%>						<ct:menuActivateNode nodeId="annulerDiminution"  active="yes" />
<%
		} else {
%>						<ct:menuExcludeNode nodeId="annulerDiminution" />
<%
		}
		if (!courant.isPreparationDecisionValide(dateDernierPaiement)) {
%>						<ct:menuExcludeNode nodeId="preparerDecisionRA" />
<%		}
		
		
		if ((IRERenteAccordee.CS_ETAT_VALIDE.equals(courant.getCsEtat())
			|| IRERenteAccordee.CS_ETAT_PARTIEL.equals(courant.getCsEtat()))
			&& globaz.jade.client.util.JadeStringUtil.isBlankOrZero(courant.getDateFinDroit())) {
		} else {
%>						<ct:menuExcludeNode nodeId="annoncePonctuelle" />
<%
		}
%>					</ct:menuPopup>
<%
		idTiersAdrPmtCourant = "";
		idTiersAdrPmtLast = courant.getIdTiersAdressePmt();

		compartLast = idTiersAdrPmtLast;

		nbLigne++;
%>				</td>
<%
	if (!JadeStringUtil.isBlankOrZero(courant.getIdCompteAnnexe()) && hasOsirisReadAccess) {
%>				<td  class="mtd" nowrap>
<%
		String urlLienOrdreVersement = request.getContextPath() + "/osiris?"
									+ "userAction=osiris.comptes.odresVersement.chercher" 
									+ "&selectedId=" + courant.getIdCompteAnnexe() 
									+ "&id=" + courant.getIdCompteAnnexe() 
									+ "&idCompteAnnexe=" + courant.getIdCompteAnnexe();
%>
					<a href="<%=urlLienOrdreVersement%>" class="external_link" target="_parent">
						<ct:FWLabel key="JSP_RAC_L_OV_LINK" />
					</a>
				</td>
<%
	} else {
%>				<td class="mtd" nowrap>
					&nbsp;
				</td>
<%
	}
%>			<td class="mtd" nowrap onClick="<%=detailUrl%>">
				<%=courant.getDetailRequerantDecede()%>
			</td>
			<td class="mtd">
<%
	String urlLienGed = servletContext + "/corvus?" 
						+ "userAction=" + IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE + ".actionAfficherDossierGed" 
						+ "&noAVSId=" + courant.getNumeroAvsBenef() 
						+ "&idTiersExtraFolder=" + courant.getIdTiersBeneficiaire() 
						+ "&serviceNameId=" + viewBean.getSession().getApplication().getProperty(IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED);
%>
				<a href="#" onclick="window.open('<%=urlLienGed%>','GED_CONSULT')">
					<ct:FWLabel key="JSP_LIEN_GED"/>
				</a>
			</td>
<%
	if (courant.hasPostit()) {
%>			<td class="mtd" nowrap="nowrap">
				<table width="100%">
					<tr>
						<td onClick="<%=detailUrl%>">
							<%=courant.getCodePrestation()%>&nbsp;
						</td>
						<td align="right">
							<ct:FWNote sourceId="<%=courant.getIdTiersBeneficiaire()%>" tableSource="<%=REApplication.KEY_POSTIT_RENTES%>" />
						</td>
					</tr>
				</table>
			</td>
<%
	} else {
%>			<td class="mtd" nowrap onClick="<%=detailUrl%>">
				<%=courant.getCodePrestation()%>
			</td>
<%
	}
%>			<td class="mtd" nowrap onClick="<%=detailUrl%>">
				<%=courant.getDateDebutDroit() + " - " + courant.getDateFinDroit()%>
			</td>
			<td class="mtd" nowrap onClick="<%=detailUrl%>">
				<%=courant.getRetenueBlocageLibelle()%>&nbsp;
			</td>
			<td class="mtd" align="right" nowrap onClick="<%=detailUrl%>">
				<%=new FWCurrency(courant.getMontantPrestation()).toStringFormat()%>
			</td>
			<td class="mtd" nowrap onClick="<%=detailUrl%>" style="text-align: center;">
<%
	if (courant.contientCodeCasSpecial("08")) {
%>				<ct:FWLabel key="JSP_RAC_L_AJOURNEMENT" /><br />
<%
	} else if (courant.contientCodeCasSpecial("07")) {
%>				<ct:FWLabel key="JSP_RAC_L_INCARCERATION" /><br />
<%
	}
%>				<%=courant.getCsEtatLibelle()%>
			</td>
			<td class="mtd" nowrap onClick="<%=detailUrl%>">
				<%=courant.getDateEcheance()%>&nbsp;
			</td>
			<td class="mtd" nowrap onClick="<%=detailUrl%>">
				<%=courant.getIdPrestationAccordee()%>
			</td>
<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>