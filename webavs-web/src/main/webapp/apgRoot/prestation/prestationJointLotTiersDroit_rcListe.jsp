<%@page import="globaz.apg.enums.APTypeDePrestation"%>
<%@page import="globaz.jade.client.util.JadeNumericUtil"%>
<%@page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@page import="globaz.globall.util.JANumberFormatter"%>
<%@page import="globaz.apg.vb.prestation.APPrestationJointLotTiersDroitViewBean"%>
<%@page import="globaz.apg.utils.APPrestationJointLotTiersDroitGroupByIterator"%>
<%@page import="globaz.apg.menu.IAppMenu"%>
<%@page import="globaz.prestation.api.IPRDemande"%>
<%@page import="globaz.apg.api.prestation.IAPPrestation"%>
<%@page import="globaz.apg.vb.prestation.APPrestationJointLotTiersDroitListViewBean"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@page import="globaz.apg.application.APApplication"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/list/header.jspf" %>
<%
	APPrestationJointLotTiersDroitListViewBean viewBean = (APPrestationJointLotTiersDroitListViewBean) request.getAttribute("viewBean");

	size = viewBean.size();
	detailLink = "apg?userAction=apg.prestation.prestationJointLotTiersDroit.afficher&selectedId=";

	//initialisation du menu d'option
	if ((IAPPrestation.CS_ETAT_PRESTATION_VALIDE.equals(viewBean.getForEtat()) 
			&& IPRDemande.CS_TYPE_MATERNITE.equals(viewBean.getForTypeDroit())) 
		|| IAPPrestation.CS_ETAT_PRESTATION_CONTROLE.equals(viewBean.getForEtat())){
		menuName = IAppMenu.MENU_OPTION_PRESTATION_LOT_OK;
	} else {
		menuName = IAppMenu.MENU_OPTION_PRESTATION_LOT_PAS_OK;
	}

	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

	APPrestationJointLotTiersDroitGroupByIterator gbIter = null;
	if (viewBean.iterator() != null) {
		gbIter = new APPrestationJointLotTiersDroitGroupByIterator(viewBean.iterator());
	}

	// pour l'affichage du montant brut total
	FWCurrency montantBrutTotal = new FWCurrency("0.00");

	// pour l'affichage du montant a restituer
	FWCurrency montantNetTotal = new FWCurrency("0.00");
	
	// pour l'affichage du montant brut additionné au frais de garde
	FWCurrency montantBrutItr = new FWCurrency("0.00");
%>
		<script type="text/javascript">
			function customOnLoad() {
				parent.checkPrestationsCalculee();
			}
		</script>
<%@ include file="/theme/list/javascripts.jspf" %>
			<th>
				&nbsp;
			</th>
			<th colspan="2">
				<ct:FWLabel key="JSP_DETAIL_ASSURE" />
			</th>
			<th>
				<ct:FWLabel key="JSP_PERIODE" />
			</th>
			<th>
				<ct:FWLabel key="JSP_MONTANT_JOURNALIER" />
			</th>
			<th>
				<ct:FWLabel key="JSP_MONTANT_BRUT" />
			</th>
			<th>
				<ct:FWLabel key="JSP_MONTANT_NET" />
			</th>
			<th>
				<ct:FWLabel key="JSP_PAIEMENT" />
			</th>
			<th>
				<ct:FWLabel key="JSP_LOT" />
			</th>
			<th>
				<ct:FWLabel key="JSP_TYPE" />
			</th>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%
	if (gbIter != null && gbIter.hasNext()) {

		APPrestationJointLotTiersDroitViewBean line = (APPrestationJointLotTiersDroitViewBean) gbIter.next();
		actionDetail = targetLocation  + "='" + detailLink + line.getIdPrestationApg() 
											+ "&" + VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + line.getIdTiers() + "'";

		// IR521 : Ajouter le montant des frais de garde au montant brut
		montantBrutItr.add(new FWCurrency(line.getMontantBrut()));
		montantBrutItr.add(new FWCurrency(line.getFraisGarde()));
		
		montantBrutTotal.add(new FWCurrency(line.getMontantBrut()));
		montantBrutTotal.add(new FWCurrency(line.getFraisGarde()));
				
		montantNetTotal.add(new FWCurrency(line.getMontantNet()));
%>			<td class="mtd" width="">
<%
		if (line.isOkPourMiseEnLot()) {
%>				<ct:menuPopup menu="ap-optionprestationlotok" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getIdPrestationApg()%>">
					<ct:menuParam key="idPrestationCourante" value="<%=line.getIdPrestationApg()%>" />
					<ct:menuParam key="genreService" value="<%=line.getGenreService()%>" />
					<ct:menuParam key="idDroit" value="<%=line.getIdDroit()%>" />
					<ct:menuParam key="forIdPrestation" value="<%=line.getIdPrestationApg()%>" />
					<ct:menuParam key="selectedId" value="<%=line.getIdPrestationApg()%>" />
					<ct:menuParam key="<%=VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE%>" value="<%=line.getIdTiers()%>"/>
<%
			if (!JadeNumericUtil.isEmptyOrZero(line.getIdAnnonce())) {
%>					<ct:menuParam key="idAnnonce" value="<%=line.getIdAnnonce()%>" />
<%
			} else {
%>					<ct:menuExcludeNode nodeId="opAnnonce1" />
<%
			}
%>				</ct:menuPopup>
<%
		} else {
%>				<ct:menuPopup menu="ap-optionprestationlotpasok" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + line.getIdPrestationApg()%>">
					<ct:menuParam key="idPrestationCourante" value="<%=line.getIdPrestationApg()%>" />
					<ct:menuParam key="genreService" value="<%=line.getGenreService()%>" />
					<ct:menuParam key="idDroit" value="<%=line.getIdDroit()%>" />
					<ct:menuParam key="forIdPrestation" value="<%=line.getIdPrestationApg()%>" />
					<ct:menuParam key="<%=VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE%>" value="<%=line.getIdTiers()%>"/>
<%
			if (!JadeNumericUtil.isEmptyOrZero(line.getIdAnnonce())) {
%>					<ct:menuParam key="idAnnonce" value="<%=line.getIdAnnonce()%>" />
<%
			} else {
%>					<ct:menuExcludeNode nodeId="opAnnonce2" />
<%
			}
%>				</ct:menuPopup>
<%
		}
%>			</td>
<%
		if (line.hasPostit()) {
%>			<td class="mtd" nowrap="nowrap">
				<table width="100%">
					<tr>
						<td onClick="<%=actionDetail%>">
							<%=line.getDetailRequerant()%>&nbsp;
						</td>
						<td align="right">
							<ct:FWNote sourceId="<%=line.getIdPrestationApg()%>" tableSource="<%=APApplication.KEY_POSTIT_PRESTATIONS_APG_MAT%>" />
						</td>
					</tr>
				</table>
			</td>
<%
		} else {
%>			<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
				<%=line.getDetailRequerant()%>&nbsp;
			</td>
<%
		}

%>			
			<td class="mtd" nowrap >
				<A  href="#" onclick="window.open('<%=servletContext%><%=("/apg")%>?userAction=<%=globaz.apg.servlet.IAPActions.ACTION_DROIT_LAPG%>.actionAfficherDossierGed&amp;noAVSId=<%=line.getNoAVS()%>&amp;serviceNameId=<%=viewBean.getSession().getApplication().getProperty(globaz.externe.IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED)%>')" ><ct:FWLabel key="JSP_GED"/></A>
			</td>
			<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
				<%=line.getDateDebut() + " - " + line.getDateFin()%>&nbsp;
			</td>
			
			<% if(!APTypeDePrestation.ACM_NE.isCodeSystemEqual(line.getGenre())){ %>
			<td align="right" class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
				<%=line.getMontantJournalier()%>&nbsp;
			</td>			
			<%} else { %>
				<td align="right" class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>"></td>
			<% } %>
			
			
			<td align="right" class="mtd" onClick="<%=actionDetail%>" width="" nowrap>
				<%=JANumberFormatter.fmt(montantBrutItr.toString(), true, true, true, 2)%>&nbsp;
			</td>
			<td align="right" class="mtd" onClick="<%=actionDetail%>" width="" nowrap>
				<%=JANumberFormatter.fmt(line.getMontantNet(), true, true, true, 2)%>&nbsp;
			</td>
			<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
				<%=line.getDatePaiement()%>&nbsp;
			</td>
			<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
				<%=line.getNoLot()%>&nbsp;
			</td>
			<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
				<%=line.getLibelleGenrePrestation()%>&nbsp;
			</td>
<%
	    // Réinitialisation de la variable
	    montantBrutItr =new FWCurrency("0.00");
	}
%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%
	if (!viewBean.canDoNext() && !viewBean.canDoPrev()) {
%>		<tr>
			<td colspan="4" style="font-style: italic; background-color: #dddddd;">
				<ct:FWLabel key="JSP_TOTAUX" />&nbsp;
			</td>
			<td class="mtd" nowrap align="right" style="font-style: italic; background-color: #dddddd;border-style:double;border-top-width:3; border-left-width:0; border-right-width:0; border-bottom-width:0;border-color:black;">
				<%=montantBrutTotal.toStringFormat()%>
			</td>
			<td class="mtd" nowrap align="right" style="font-style: italic; background-color: #dddddd;border-style:double;border-top-width:3; border-left-width:0; border-right-width:0; border-bottom-width:0;border-color:black;">
				<%=montantNetTotal.toStringFormat()%>
			</td>
			<td colspan="3" style="font-style: italic; background-color: #dddddd;">
				&nbsp;
			</td>
		</tr>
<%
	}
%>
<%@ include file="/theme/list/tableEnd.jspf" %>
