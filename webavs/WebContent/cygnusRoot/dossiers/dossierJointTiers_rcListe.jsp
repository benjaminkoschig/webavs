<%@ page language="java" errorPage="/errorPage.jsp" %>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ page import="ch.globaz.utils.VueGlobaleTiersUtils" %>
<%@ page import="globaz.externe.IPRConstantesExternes" %>
<%@ page import="globaz.jade.client.util.JadeStringUtil" %>
<%@ page import="globaz.cygnus.vb.dossiers.RFDossierJointTiersListViewBean" %>
<%@ page import="globaz.cygnus.servlet.IRFActions" %>
<%@ page import="globaz.cygnus.vb.dossiers.RFDossierJointTiersViewBean" %>
<%@ page import="globaz.cygnus.api.dossiers.IRFDossiers" %>

<%@ include file="/theme/list/header.jspf" %>
<%
	//Les labels de cette page commence par le préfix "JSP_RF_DOS_L"
	RFDossierJointTiersListViewBean viewBean = (RFDossierJointTiersListViewBean) request.getAttribute("viewBean");
	size = viewBean.getSize();

	detailLink = "cygnus?userAction="+IRFActions.ACTION_DOSSIER_JOINT_TIERS+ ".afficher&selectedId=";

	menuName = "cygnus-menuprincipal";
%>
<%@ include file="/theme/list/javascripts.jspf" %>
			<th colspan="2">
				<ct:FWLabel key="JSP_RF_DOS_L_DETAIL_ASSURE" />
			</th>
			<th title="<ct:FWLabel key='JSP_CAAI_TITRE'/>">
				<ct:FWLabel key="JSP_RF_DOS_L_CAAI" />
			</th>
			<th>
				<ct:FWLabel key="JSP_RF_DOS_L_GED" />
			</th>
			<th>
				<ct:FWLabel key="JSP_RF_DOS_L_GEST" />
			</th>
			<th>
				<ct:FWLabel key="JSP_RF_DOS_L_ETAT" />
			</th>
			<th>
				<ct:FWLabel key="JSP_RF_DOS_L_PERIODE" />
			</th>
			<th>
				<ct:FWLabel key="JSP_RF_DOS_L_NO" />
			</th>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%
		RFDossierJointTiersViewBean courant = (RFDossierJointTiersViewBean) viewBean.get(i);

		String urlForMenuPopUp = detailLink + courant.getIdDossier() 
								+ "&idDossier=" + courant.getIdDossier() 
								+ "&idTiers=" + courant.getIdTiers() 
								+ "&nss=" + courant.getNss() 
								+ "&isAfficherDetail=true" 
								+ "&csEtatDossier=" + courant.getCsEtatDossier()
								+ "&csSexe=" + courant.getCsSexe()
								+ "&csNationalite=" + courant.getCsNationalite();

		String detailUrl = "parent.location.href='" + urlForMenuPopUp +"'";

		String urlGED = servletContext + "/cygnus?" 
						+ "userAction=" + IRFActions.ACTION_DOSSIER_JOINT_TIERS + ".actionAfficherDossierGed" 
						+ "&noAVSId=" + courant.getNss() 
						+ "&idTiersExtraFolder=" + null 
						+ "&serviceNameId=" + viewBean.getSession().getApplication().getProperty(IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED);
%>
			<td class="mtd" nowrap width="20px">
				<ct:menuPopup menu="cygnus-optionsdossiers" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=urlForMenuPopUp %>">
					<ct:menuParam key="idDossier" value="<%=courant.getIdDossier()%>" />
					<ct:menuParam key="csEtatDossier" value="<%=courant.getCsEtatDossier()%>" />
					<ct:menuParam key="idTiers" value="<%=courant.getIdTiers()%>" />
					<ct:menuParam key="detailRequerant" value="<%=courant.getDetailAssure()%>" />
					<ct:menuParam key="nss" value="<%=courant.getNss()%>" />
					<ct:menuParam key="nom" value="<%=courant.getNom()%>" />
					<ct:menuParam key="prenom" value="<%=courant.getPrenom()%>" />
					<ct:menuParam key="dateNaissance" value="<%=courant.getDateNaissance()%>" />
					<ct:menuParam key="libelleCourtSexe" value="<%=courant.getLibelleCourtSexe()%>" />
					<ct:menuParam key="libellePays" value="<%=courant.getLibellePays()%>" />
					<ct:menuParam key="csSexe" value="<%=courant.getCsSexe()%>" />
					<ct:menuParam key="csNationalite" value="<%=courant.getCsNationalite()%>" />
					<ct:menuParam key="dateDeces" value="<%=courant.getDateDeces()%>" />
					<ct:menuParam key="idTierRequerant" value="<%=courant.getIdTiers()%>" />
					<ct:menuParam key="<%=VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE%>" value="<%=courant.getIdTiers()%>" />
				</ct:menuPopup>
			</td>
			<td class="mtd" nowrap onClick="<%=detailUrl%>">
				<%=courant.getDetailAssure()%>
			</td>
			<td class="mtd" align="center" nowrap onClick="<%=detailUrl%>">
<%
		if (courant.isContributionAssistanceAIenCours(viewBean.getDateDernierPaiement())) {
%>				<img style="margin-bottom:-5px" alt='<ct:FWLabel key="JSP_RF_DOS_L_CAAI_EN_COURS"/>' src="<%=request.getContextPath()%>/images/button_ok_vert.png" />
<%		} else if (courant.isContributionAssistanceAIechue(viewBean.getDateDernierPaiement())) {
%>				<img style="margin-bottom:-5px" alt='<ct:FWLabel key="JSP_RF_DOS_L_CAAI_ECHUE"/>' src="<%=request.getContextPath()%>/images/button_ok_orange.png" />
<%		} else {
	%>			&nbsp;
<%		}
%>			</td>
			<td class="mtd" align="center">
				<a href="#" onclick="window.open('<%=urlGED%>','GED_CONSULT')">
					<ct:FWLabel key="JSP_LIEN_GED"/>
				</a>
			</td>
			<td class="mtd" align="center" nowrap onClick="<%=detailUrl%>">
				<%=courant.getGestionnaire()%>
			</td>
			<td class="mtd" align="center" nowrap onClick="<%=detailUrl%>">
				<%=viewBean.getSession().getCodeLibelle(courant.getCsEtatDossier())%>
			</td>
<%
		String periode = courant.getDateDebutPeriodeDossier();
		if (!JadeStringUtil.isBlankOrZero(courant.getDateFinPeriodeDossier())) {
			periode += " - " + courant.getDateFinPeriodeDossier();
		} else {
			periode += " -           ";
		}
%>			<td class="mtd" nowrap onClick="<%=detailUrl%>">
				<%=periode%>
			</td>
			<td class="mtd" align="center" nowrap onClick="<%=detailUrl%>">
				<%=courant.getIdDossier()%>
			</td>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%@ include file="/theme/list/tableEnd.jspf" %>
