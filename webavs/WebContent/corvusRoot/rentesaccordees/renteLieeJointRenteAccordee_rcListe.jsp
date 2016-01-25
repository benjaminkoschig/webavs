<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@page import="globaz.externe.IPRConstantesExternes"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
	
<%@ page import="globaz.corvus.api.basescalcul.IRERenteAccordee"%>
<%@ page import="globaz.corvus.application.REApplication"%>
<%@ page import="globaz.corvus.servlet.IREActions"%>
<%@ page import="globaz.corvus.vb.rentesaccordees.RERenteLieeJointRenteAccordeeListViewBean"%>
<%@ page import="globaz.corvus.vb.rentesaccordees.RERenteLieeJointRenteAccordeeViewBean"%>
<%@ page import="globaz.framework.controller.FWController"%>
<%@ page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page import="globaz.framework.util.FWCurrency"%><%@page import="globaz.corvus.utils.REPmtMensuel"%>
<%@ page import="globaz.jade.client.util.JadeStringUtil"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>
<%
	// Les labels de cette page commence par le préfix "JSP_RLI_L"

	RERenteLieeJointRenteAccordeeListViewBean viewBean = (RERenteLieeJointRenteAccordeeListViewBean) request.getAttribute("viewBean");

	size = viewBean.getSize ();

	detailLink = "corvus?userAction=" + IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE+ ".afficher&selectedId=";

	String noDemandeRente = request.getParameter("noDemandeRente");
	String idTierRequerant = request.getParameter("idTierRequerant");
	String idRenteAccordee = request.getParameter("idRenteAccordee");

	String menuOptionToLoad = request.getParameter("menuOptionToLoad");
	
	FWController controllerbis = (FWController) session.getAttribute("objController");
	boolean hasOsirisReadAccess = controllerbis.getSession().hasRight("osiris.comptes.ordresVersement", FWSecureConstants.READ);
%>
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
			<th colspan="2"><ct:FWLabel key="JSP_RLI_L_DETAIL_REQUERANT" /></th>
			<th><ct:FWLabel key="JSP_RLI_L_GENRE_PRESTATION" /></th>
			<th><ct:FWLabel key="JSP_RAC_L_PERIODE_DU_DROIT" /></th>
			<th><ct:FWLabel key="JSP_RAC_L_RETENUES_BLOCAGE" /></th>
			<th><ct:FWLabel key="JSP_RLI_L_MONTANT" /></th>
			<th><ct:FWLabel key="JSP_RLI_L_ETAT" /></th>
			<th><ct:FWLabel key="JSP_RAC_L_DATE_ECHEANCE" /></th>
			<th><ct:FWLabel key="JSP_RAC_L_NO" /></th>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/tableHeader.jspf" %>
	<%-- tpl:put name="zoneCondition" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
<%
	RERenteLieeJointRenteAccordeeViewBean courant = (RERenteLieeJointRenteAccordeeViewBean) viewBean.get(i);
	String detailUrl = "parent.location.href='" + detailLink + courant.getIdPrestationAccordee() 
						+ "&noDemandeRente=" + courant.getNoDemandeRente()
						+ "&idTierRequerant=" + courant.getIdTiersBeneficiaire() 
						+ "&menuOptionToLoad=" + menuOptionToLoad + "'";

	String detailLinkSuite = "&noDemandeRente=" + courant.getNoDemandeRente() 
							+ "&idTierRequerant=" + courant.getIdTiersBeneficiaire();
%>
			<td class="mtd" nowrap>
				<ct:menuPopup menu="corvus-optionsrentesliees" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + courant.getIdPrestationAccordee()+ detailLinkSuite%>">
					<ct:menuParam key="selectedId" value="<%=courant.getIdPrestationAccordee()%>" />
					<ct:menuParam key="noDemandeRente" value="<%=courant.getNoDemandeRente()%>" />
					<ct:menuParam key="idTierRequerant" value="<%=courant.getIdTiersBeneficiaire()%>" />
					<ct:menuParam key="idRenteAccordee" value="<%=courant.getIdPrestationAccordee()%>" />
					<ct:menuParam key="idRenteCalculee" value="<%=courant.getIdRenteCalculee()%>" />
					<ct:menuParam key="montantRenteAccordee" value="<%=courant.getMontantPrestation()%>" />
					<ct:menuParam key="idTiersBeneficiaire" value="<%=courant.getIdTiersBeneficiaire()%>" />
					<ct:menuParam key="idBaseCalcul" value="<%=courant.getIdBaseCalcul()%>" />
					<ct:menuParam key="csTypeBasesCalcul" value="<%=courant.getCsTypeBasesCalcul()%>" />
					<ct:menuParam key="csEtatRenteAccordee" value="<%=courant.getCsEtat()%>" />
					<ct:menuParam key="dateFinDroit" value="<%=courant.getDateFinDroit()%>" />
					<ct:menuParam key="isPreparationDecisionValide" value="<%=Boolean.toString(courant.isPreparationDecisionValide())%>" />
<%
	if ((IRERenteAccordee.CS_ETAT_AJOURNE.equals(courant.getCsEtat())
		|| IRERenteAccordee.CS_ETAT_CALCULE.equals(courant.getCsEtat())
		|| IRERenteAccordee.CS_ETAT_DIMINUE.equals(courant.getCsEtat()))
		|| (!JadeStringUtil.isBlankOrZero(courant.getDateFinDroit()))
		|| !REPmtMensuel.isValidationDecisionAuthorise(objSession)) {
%>					<ct:menuExcludeNode nodeId="optdiminution" />
<%
	}
	if (courant.isAfficherRepriseDuDroit()) {
%>					<ct:menuActivateNode nodeId="repriseDroit"  active="yes" />
<%
	} else {
%>					<ct:menuExcludeNode nodeId="repriseDroit" />
<%
	}
	if (courant.isAnnulerDiminutionAuthorise()) {
%>					<ct:menuActivateNode nodeId="annulerDiminution"  active="yes" />
<%
	} else {
%>					<ct:menuExcludeNode nodeId="annulerDiminution" />
<%
	}
	if ((IRERenteAccordee.CS_ETAT_VALIDE.equals(courant.getCsEtat())
		|| IRERenteAccordee.CS_ETAT_PARTIEL.equals(courant.getCsEtat()))
		&& JadeStringUtil.isBlankOrZero(courant.getDateFinDroit())) {
	} else {
%>					<ct:menuExcludeNode nodeId="annoncePonctuelle" />
<%
	}
	if (courant.isDemandeValidee()) {
%>					<ct:menuExcludeNode nodeId="preparerDecisionRA" />
<%
	}
%>				</ct:menuPopup>
			</td>
<%
	if (!JadeStringUtil.isBlankOrZero(courant.getIdCompteAnnexe()) && hasOsirisReadAccess) {
%>			<td class="mtd" nowrap>
				<a href="<%=request.getContextPath()%>/osiris?userAction=osiris.comptes.odresVersement.chercher&selectedId=<%=courant.getIdCompteAnnexe()%>&id=<%=courant.getIdCompteAnnexe()%>&idCompteAnnexe=<%=courant.getIdCompteAnnexe()%>" class="external_link" target="_parent">
					<ct:FWLabel key="JSP_RAC_L_OV_LINK" />
				</a>
			</td>
<%
	} else {
%>			<td class="mtd" nowrap>
				&nbsp;
			</td>
<%
	}
%>			<td class="mtd" nowrap onClick="<%=detailUrl%>">
				<%=courant.getDetailRequerantDecede()%>
			</td>
			<td class="mtd">
<%
	String urlLienGED = servletContext + "/corvus?"
						+ "userAction=" + IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE + ".actionAfficherDossierGed" 
						+ "&noAVSId=" + courant.getNssTiersBeneficiaire() 
						+ "&idTiersExtraFolder=" + courant.getIdTiersBeneficiaire() 
						+ "&serviceNameId=" + viewBean.getSession().getApplication().getProperty(IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED);
%>
				<a href="<%=urlLienGED%>" target="_blank">
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