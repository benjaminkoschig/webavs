<%@ page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@ page language="java" errorPage="/errorPage.jsp" %>

<%@ page import="globaz.framework.util.FWCurrency"%>
<%@ page import="globaz.globall.util.JANumberFormatter"%>
<%@ page import="globaz.ij.application.IJApplication"%>
<%@ page import="globaz.ij.menu.IAppMenu"%>
<%@ page import="globaz.ij.servlet.IIJActions"%>
<%@ page import="globaz.ij.tools.IJPrestationJointLotPrononceGroupByIterator"%>
<%@ page import="globaz.ij.vb.prestations.IJPrestationJointLotPrononceListViewBean"%>
<%@ page import="globaz.ij.vb.prestations.IJPrestationJointLotPrononceViewBean"%>

<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/list/header.jspf" %>
<%
	IJPrestationJointLotPrononceListViewBean viewBean = (IJPrestationJointLotPrononceListViewBean) request.getAttribute("viewBean");

	size = viewBean.size();
	detailLink = "ij?userAction=" + IIJActions.ACTION_PRESTATION_JOINT_LOT_PRONONCE + ".afficher&forIdPrestation=";
	menuName = IAppMenu.MENU_OPTION_PRESTATION;

	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");

	IJPrestationJointLotPrononceGroupByIterator gbIter = null;

	if (viewBean.iterator() != null) {
		gbIter = new IJPrestationJointLotPrononceGroupByIterator(viewBean.iterator());
	}

	// pour l'affichage du montant brut total
	FWCurrency montantBrutTotal = new FWCurrency("0.00");

	// pour l'affichage du montant a restituer
	FWCurrency montantNetTotal = new FWCurrency("0.00");
%>
<%@ include file="/theme/list/javascripts.jspf" %>
				<th>
					&nbsp;
				</th>
				<th>
					<ct:FWLabel key="JSP_PRE_NO" />
				</th>
				<th>
					<ct:FWLabel key="JSP_DETAIL_REQUERANT" />
				</th>
				<%if (globaz.jade.ged.client.JadeGedFacade.isInstalled()) {%>
				<th>
					<ct:FWLabel key="JSP_GED" />
				</th>
				<%}%>
				<th>
					<ct:FWLabel key="JSP_DATE_DEBUT_PRONONCE" />
				</th>
				<th>
					<ct:FWLabel key="JSP_COT_D_PERIODE" />
				</th>
				<th>
					<ct:FWLabel key="JSP_MONTANT_BRUT" />
				</th>
				<th>
					<ct:FWLabel key="JSP_MONTANT_NET" />
				</th>
				<th>
					<ct:FWLabel key="JSP_LOT" />
				</th>
				<th>
					<ct:FWLabel key="JSP_TYPE" />
				</th>
				<th>
					<ct:FWLabel key="JSP_ETAT" />
				</th>
				<th>
					<ct:FWLabel key="JSP_DATE_PAIEMENT" />
				</th>
				<th>
					<ct:FWLabel key="JSP_NO_BASE_INDEMNISATION_COURT" />
				</th>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%@ include file="/theme/list/lineStyle.jspf" %>
<%
	if (gbIter != null && gbIter.hasNext()) {

		IJPrestationJointLotPrononceViewBean line = (IJPrestationJointLotPrononceViewBean) gbIter.next();

		String detailMenu = detailLink + line.getIdPrestation() 
							+ "&selectedId=" + line.getIdPrestation() 
							+ "&noAVS=" + line.getNoAVS() 
							+ "&datePrononce=" + line.getDatePrononce() 
							+ "&dateDebutPrestation=" + line.getDateDebut() 
							+ "&dateFinPrestation=" + line.getDateFin() 
							+ "&montantBrutTotal=" + line.getMontantBrut()
							+ "&" + VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + line.getIdTiers();

		actionDetail = "parent.fr_detail.location.href='" + detailMenu + "'";

		montantBrutTotal.add(new FWCurrency(line.getMontantBrut()));
		montantNetTotal.add(new FWCurrency(line.getMontantNet()));
%>
					<td class="mtd" width="">
						<ct:menuPopup menu="ij-optionmenuprestation">
							<ct:menuParam key="idPrestation" value="<%=line.getIdPrestation()%>" />
							<ct:menuParam key="noAVS" value="<%=line.getNoAVS()%>" />
							<ct:menuParam key="datePrononce" value="<%=line.getDatePrononce()%>" />
							<ct:menuParam key="dateDebutPrestations" value="<%=line.getDateDebut()%>" />
							<ct:menuParam key="dateFinPrestations" value="<%=line.getDateFin()%>" />
							<ct:menuParam key="montantBrutTotal" value="<%=line.getMontantBrut()%>" />
							<ct:menuParam key="forIdPrestation" value="<%=line.getIdPrestation()%>" />
							<ct:menuParam key="selectedId" value="<%=line.getIdPrestation()%>" />
							<ct:menuParam key="<%=VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE%>" value="<%=line.getIdTiers()%>" />
						</ct:menuPopup>
					</td>
					<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
						<%=line.getIdPrestation()%>&nbsp;
					</td>
<%
		if (line.hasPostit()) {
%>					<td class="mtd" nowrap="nowrap">
						<table width="100%">
							<tr class="">
								<td onClick="<%=actionDetail%>">
									<%=line.getDetailRequerant()%>&nbsp;
								</td>
								<td align="right" class="width:1cm;">
									<ct:FWNote	sourceId="<%=line.getIdPrestation()%>" 
												tableSource="<%=IJApplication.KEY_POSTIT_PRESTATIONS%>" />
								</td>
							</tr>
						</table>
					</td>
<%
		} else {
%>					<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
						<%=line.getDetailRequerant()%>&nbsp;
					</td>
<%
		}
%>					
<%if (globaz.jade.ged.client.JadeGedFacade.isInstalled()) { %>
					<td class="mtd" nowrap="nowrap">
						<A href="#" onclick="window.open('<%=servletContext%><%=("/ij")%>?userAction=<%=globaz.ij.servlet.IIJActions.ACTION_PRONONCE%>.actionAfficherDossierGed&amp;noAVSId=<%=line.getNoAVS()%>&amp;serviceNameId=<%=viewBean.getSession().getApplication().getProperty(globaz.externe.IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED)%>','GED_CONSULT')" ><ct:FWLabel key="JSP_GED"/></A>
					</td>
					<% } %>
					<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
						<%=line.getDateDebutPrononce()%>&nbsp;
					</td>
					<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
						<%=line.getDateDebut()+" - "+line.getDateFin()%>&nbsp;
					</td>
					<td align="right" class="mtd" onClick="<%=actionDetail%>" width="" nowrap>
						<%=JANumberFormatter.fmt(line.getMontantBrut(), true, true, true, 2)%>&nbsp;
					</td>
					<td align="right" class="mtd" onClick="<%=actionDetail%>" width="" nowrap>
						<%=JANumberFormatter.fmt(line.getMontantNet(), true, true, true, 2)%>&nbsp;
					</td>
					<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
						<%=line.getNoLot()%>&nbsp;
					</td>
					<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
						<%=line.getLibelleType()%>&nbsp;
					</td>
					<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
						<%=line.getLibelleEtat()%>&nbsp;
					</td>
					<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
						<%=line.getDatePaiement()%>&nbsp;
					</td>
					<td class="mtd" nowrap="nowrap" onClick="<%=actionDetail%>">
						<%=line.getIdBaseIndemnisation()%>&nbsp;
					</td>
<%
	}
%>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%
	if (!viewBean.canDoNext() &&  !viewBean.canDoPrev()) {
%>				<tr>
<%if (globaz.jade.ged.client.JadeGedFacade.isInstalled()) { %>
					<td colspan="6" style="font-style: italic; background-color: #dddddd;">
						<ct:FWLabel key="JSP_TOTAUX" />&nbsp;
					</td>
<% } %>	
<%if (!globaz.jade.ged.client.JadeGedFacade.isInstalled()) { %>
					<td colspan="5" style="font-style: italic; background-color: #dddddd;">
						<ct:FWLabel key="JSP_TOTAUX" />&nbsp;
					</td>
<% } %>					
					<td class="mtd" nowrap align="right" style="font-style: italic; background-color: #dddddd;border-style:double;border-top-width:3; border-left-width:0; border-right-width:0; border-bottom-width:0;border-color:black;">
						<%=montantBrutTotal.toStringFormat()%>
					</td>
					<td class="mtd" nowrap align="right" style="font-style: italic; background-color: #dddddd;border-style:double;border-top-width:3; border-left-width:0; border-right-width:0; border-bottom-width:0;border-color:black;">
						<%=montantNetTotal.toStringFormat()%>
					</td>
					<td colspan="5" style="font-style: italic; background-color: #dddddd;">
						&nbsp;
					</td>
				</tr>
<%
	}
%>
<%@ include file="/theme/list/tableEnd.jspf" %>
