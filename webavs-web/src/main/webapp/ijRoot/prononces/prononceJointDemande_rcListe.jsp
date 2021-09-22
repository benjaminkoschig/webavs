<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="ch.globaz.utils.VueGlobaleTiersUtils"%>
<%@page import="globaz.ij.vb.prononces.IJPrononceJointDemandeViewBean"%>
<%@page import="globaz.prestation.tools.PRIterateurHierarchique"%>
<%@page import="globaz.ij.vb.prononces.IJPrononceJointDemandeListViewBean"%>
<%@page import="globaz.ij.api.prononces.IIJPrononce"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.ij.application.IJApplication"%>
<%@ page import="globaz.ij.db.prononces.IJPrononce" %>

<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>

<%@ include file="/theme/list/header.jspf" %>
<%
	IJPrononceJointDemandeListViewBean viewBean = (IJPrononceJointDemandeListViewBean) request.getAttribute("viewBean");

	PRIterateurHierarchique iterH = viewBean.iterateurHierarchique();
	size = viewBean.getSize ();
	detailLink = "ij?userAction=ij.prononces.requerant.afficher&selectedId=";

	menuDetailLabel = viewBean.getSession().getLabel("MENU_OPTION_DETAIL");
	menuName = globaz.ij.menu.IAppMenu.MENU_OPTION_PRONONCE;
	boolean hasDroitCalculerIJ = viewBean.getSession().hasRight("ij.acor.calculACORIJ.actionExporterScriptACOR2", FWSecureConstants.UPDATE);
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
				<th>
					&nbsp;
				</th>
				<th>
					<ct:FWLabel key="JSP_NO" />
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
					<ct:FWLabel key="JSP_PERIODE_PRONONCE" />
				</th>
				<th>
					<ct:FWLabel key="JSP_GESTIONNAIRE" />
				</th>
				<th>
					<ct:FWLabel key="JSP_ETAT_PRONONCE" />
				</th>
				<th>
					<ct:FWLabel key="JSP_TYPE" />
				</th>
<%@ include file="/theme/list/tableHeader.jspf" %>
<%
	IJPrononceJointDemandeViewBean courant = null;
	try {
		courant = (IJPrononceJointDemandeViewBean) iterH.next();
	} catch (Exception e) {
		break;
	}
	actionDetail = "parent.location.href='" 
						+ detailLink + courant.getIdPrononce()
						+ "&" + VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE + "=" + courant.getIdTiers()
					+ "'";

	if (iterH.isPositionPlusPetite()) {
%>		</tbody>
<%
	} else if (iterH.isPositionPlusGrande()) {
%>		<tbody id="groupe_<%=courant.getIdParent()%>" style="display: none;">
<%
	}
%>
<%@ include file="/theme/list/lineStyle.jspf" %>
				<td class="mtd" nowrap>
<%
	if (iterH.isOrphelin()) {
%>					-&nbsp;&nbsp;
<%
	} else {
		for (int idPosition = 1; idPosition < iterH.getPosition(); ++idPosition) {
%>					&nbsp;&nbsp;
<%
		}
	}

	String nomPrenom = courant.getNomPrenom();
	if (JadeStringUtil.contains(nomPrenom, "'")) {
		nomPrenom = JadeStringUtil.change(nomPrenom, "'", " ");
	}
%>					<ct:menuPopup menu="ij-optionmenuprononce" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + courant.getIdPrononce()%>">
						<ct:menuParam key="selectedId" value="<%=courant.getIdPrononce()%>" />
						<ct:menuParam key="csTypeIJ" value="<%=courant.getCsTypeIJ()%>" />
						<ct:menuParam key="idPrononce" value="<%=courant.getIdPrononce()%>" />
						<ct:menuParam key="noAVS" value="<%=courant.getNoAVS()%>" />
						<ct:menuParam key="idDemande" value="<%=courant.getIdDemande()%>" />
						<ct:menuParam key="idDecision" value="<%=courant.getIdDecision()%>" />
						<ct:menuParam key="idTiers" value="<%=courant.getIdTiers()%>" />
<% 
		//Pour le moment, ce point de menu est désactivé. Sera (peut-être) réactivé pour l'intégration dans SEDEX !!!
// 	if (JadeStringUtil.isBlankOrZero(courant.getIdDecision())) {
%>						<ct:menuExcludeNode nodeId="validerDecision" />
<%
// 	}

	if (courant.getSoumisImpotSource()==null || !courant.getSoumisImpotSource().booleanValue()) {
%>						<ct:menuExcludeNode nodeId="saisirTauxIS" />
<%
	}
	if (!hasDroitCalculerIJ || !courant.getCsEtatPrononce().equals(IIJPrononce.CS_ATTENTE)) {
%>						<ct:menuExcludeNode nodeId="calculerijgp" />
						<ct:menuExcludeNode nodeId="calculerait" />
						<ct:menuExcludeNode nodeId="calculeraa" />
<%
	}
	if (!courant.getCsEtatPrononce().equals(IIJPrononce.CS_COMMUNIQUE)) {
%>						<ct:menuExcludeNode nodeId="terminerprononce" />
<%
	}
	if (courant.getCsEtatPrononce().equals(IIJPrononce.CS_ANNULE)) {
%>						<ct:menuExcludeNode nodeId="corrigerprononce" />
<%
	}
	if (!courant.getCsEtatPrononce().equals(IIJPrononce.CS_COMMUNIQUE) || !courant.hasNotIdParentCorrigeDepuis()) {
%>						<ct:menuExcludeNode nodeId="corrigerdepuis" />
<%
	}	
	if (courant.hasNotIdParentCorrigeDepuis() || courant.getCsEtatPrononce().equals(IIJPrononce.CS_ANNULE)) {
%>						<ct:menuExcludeNode nodeId="annulerdepuis" />
<%
	}
	if(IJPrononce.isCommonTypeIJ(courant.getCsTypeIJ())) {
%>						<ct:menuExcludeNode nodeId="calculerait" />
						<ct:menuExcludeNode nodeId="calculeraa" />
<%
	} else if (IIJPrononce.CS_ALLOC_INIT_TRAVAIL.equals(courant.getCsTypeIJ())) {
%>						<ct:menuExcludeNode nodeId="calculerijgp" />
						<ct:menuExcludeNode nodeId="calculeraa" />
<%
		if (!courant.getCsEtatPrononce().equals(IIJPrononce.CS_COMMUNIQUE)) {
%>						<ct:menuExcludeNode nodeId="corrigerprononce" />
<%
		}
	} else {
%>						<ct:menuExcludeNode nodeId="calculerijgp" />
						<ct:menuExcludeNode nodeId="calculerait" />
						<ct:menuExcludeNode nodeId="corrigerprononce" />
<%
	}
	if ( (!courant.getCsEtatPrononce().equals(IIJPrononce.CS_COMMUNIQUE) 
			&& !courant.getCsEtatPrononce().equals(IIJPrononce.CS_VALIDE) 
			&& !courant.getCsEtatPrononce().equals(IIJPrononce.CS_DECIDE)) 
			|| courant.getCsTypeIJ().equals(IIJPrononce.CS_ALLOC_ASSIST) 
			|| courant.getCsTypeIJ().equals(IIJPrononce.CS_ALLOC_INIT_TRAVAIL)) {
%>						<ct:menuExcludeNode nodeId="genererDecision" />
<%
	}
	if (courant.getCsEtatPrononce().equals(IIJPrononce.CS_ATTENTE)
		|| courant.getCsEtatPrononce().equals(IIJPrononce.CS_VALIDE)
		|| courant.getCsEtatPrononce().equals(IIJPrononce.CS_DECIDE)) {
	} else {
%>						<ct:menuExcludeNode nodeId="annulerprononce" />
<%
	}
%>
						<ct:menuParam key="<%=VueGlobaleTiersUtils.PARAMETRE_REQUETE_ID_TIERS_VUE_GLOBALE %>" value="<%=courant.getIdTiers()%>" />
						<ct:menuParam key="nomPrenom" value="<%=nomPrenom%>" />
					</ct:menuPopup>	
<%
	if (iterH.isPere()) {
%>					<input	id="bouton_<%=courant.getIdPrononce()%>" 
							type="button" 
							value="+" 
							onclick="afficherCacher(<%=courant.getIdPrononce()%>)" />
<%
	}

	String periode = courant.getDateDebutPrononce();
	if (!JadeStringUtil.isBlankOrZero(courant.getDateFinPrononce())) {
		periode += " - " + courant.getDateFinPrononce();
	} else {
		periode += " -           ";
	}
%>
				</td>
				<td class="mtd" nowrap onClick="<%=actionDetail%>">
					<%=courant.getIdPrononce()%>
				</td>
<%
	if (courant.hasPostit()) {
%>
				<td class="mtd" nowrap="nowrap">
					<table width="100%">
						<tr>
							<td onClick="<%=actionDetail%>">
								<%=courant.getDetailRequerant()%>&nbsp;
							</td>
							<td align="right">
								<ct:FWNote	sourceId="<%=courant.getIdPrononce()%>" 
											tableSource="<%=IJApplication.KEY_POSTIT_PRONONCES%>" />
							</td>
						</tr>
					</table>
				</td>
<%
	} else {
%>
				<td class="mtd" nowrap onClick="<%=actionDetail%>">
					<%=courant.getDetailRequerant()%>
				</td>
<%
	}
%>
			<%if (globaz.jade.ged.client.JadeGedFacade.isInstalled()) {%>
				<td class="mtd" nowrap>
					<A href="#" onclick="window.open('<%=servletContext%><%=("/ij")%>?userAction=<%=globaz.ij.servlet.IIJActions.ACTION_PRONONCE%>.actionAfficherDossierGed&amp;noAVSId=<%=courant.getNoAVS()%>&amp;serviceNameId=<%=viewBean.getSession().getApplication().getProperty(globaz.externe.IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED)%>','GED_CONSULT')" ><ct:FWLabel key="JSP_GED"/></A>
				</td>
			<%}%>
				<td class="mtd" nowrap onClick="<%=actionDetail%>">
					<%=periode%>
				</td>
				<td class="mtd" nowrap onClick="<%=actionDetail%>">
					<%=courant.getNomPrenomGestionnaire()%>
				</td>
				<td class="mtd" nowrap onClick="<%=actionDetail%>">
					<%=courant.getEtatPrononceLibelle()%>
				</td>
				<td class="mtd" nowrap onClick="<%=actionDetail%>">
					<%=courant.getTypeIJLibelle()%>
				</td>
<%@ include file="/theme/list/lineEnd.jspf" %>
<%@ include file="/theme/list/tableEnd.jspf" %>
