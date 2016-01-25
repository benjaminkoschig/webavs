<%-- tpl:insert page="/theme/list.jtpl" --%>
<%@ page language="java" errorPage="/errorPage.jsp" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/list/header.jspf" %>
<%-- tpl:put name="zoneScripts" --%>

<%
	//Les labels de cette page commence par le préfix "JSP_RF_DEM_L"
	RFDemandeJointDossierJointTiersListViewBean viewBean = (RFDemandeJointDossierJointTiersListViewBean) request.getAttribute("viewBean");
	globaz.prestation.tools.PRIterateurHierarchique iterH = viewBean.iterateurHierarchique();
	size = viewBean.getSize();

	detailLink = "cygnus?userAction="+IRFActions.ACTION_SAISIE_DEMANDE_PIED_DE_PAGE+".afficher&selectedId=";
	
	menuName = "cygnus-menuprincipal";
%>
<SCRIPT language="JavaScript">
	function afficherCacher(id) {
		if (document.all("groupe_" + id).style.display == "none") {
			document.all("groupe_" + id).style.display = "block";
			document.all("bouton_" + id).value = "-";
		} else {
			document.all("groupe_" + id).style.display = "none";
			document.all("bouton_" + id).value = "+";
		}
	}
	 
</SCRIPT>
<%-- /tpl:put --%>
<%@ include file="/theme/list/javascripts.jspf" %>
	    <%-- tpl:put name="zoneHeaders" --%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.cygnus.vb.demandes.RFDemandeJointDossierJointTiersListViewBean"%>
<%@page import="globaz.cygnus.servlet.IRFActions"%>
<%@page import="globaz.cygnus.vb.demandes.RFDemandeJointDossierJointTiersViewBean"%>
<%@page import="globaz.cygnus.api.demandes.IRFDemande"%>
<%@page import="globaz.cygnus.utils.RFUtils"%>
<%@page import="globaz.cygnus.api.TypesDeSoins.IRFCodeTypesDeSoins"%>
<%@page import="globaz.framework.util.FWCurrency"%>
		<TH colspan="2"><ct:FWLabel key="JSP_RF_DEM_L_DETAIL_ASSURE"/></TH>
		<TH><ct:FWLabel key="JSP_RF_DEM_L_NO"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_DEM_L_TYPE_SOIN"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_DEM_L_SOUS_TYPE_SOIN"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_DEM_L_DATE_FACTURE"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_DEM_L_PERIODE_DE_TRAITEMENT"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_DEM_L_MONTANT"/></TH>
   		<%if (viewBean.isPropertieForcerPaiement()){%>
     		<TH><ct:FWLabel key="JSP_RF_DEM_L_MONTANT_DSAS"/></TH>
		<%}%>     	
   		<TH><ct:FWLabel key="JSP_RF_DEM_L_ETAT"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_DEM_L_STATUT"/></TH>
   		<TH><ct:FWLabel key="JSP_RF_DEM_L_GESTIONNAIRE"/></TH>
	    <%-- /tpl:put --%> 
<%@ include file="/theme/list/tableHeader.jspf" %>
    <%-- tpl:put name="zoneCondition" --%>
    <%

	RFDemandeJointDossierJointTiersViewBean courant = null;
	try {
		courant = (RFDemandeJointDossierJointTiersViewBean) iterH.next();
	} catch (Exception e) {
		break;
	}
	actionDetail = "parent.location.href='" + detailLink + courant.getIdDemande() + "'";
	
	if (iterH.isPositionPlusPetite()) {
		%></TBODY><%
	} else if (iterH.isPositionPlusGrande()) {
		%><TBODY id="groupe_<%=courant.getIdParent()%>" style="display: none;"><%
	} 
%>
    <%-- /tpl:put --%>
<%@ include file="/theme/list/lineStyle.jspf" %>
		<%-- tpl:put name="zoneList" --%>
		<%
			
			
			String urlForMenuPopUp = detailLink + courant.getIdDemande()+
								"&codeTypeDeSoinList=" + courant.getCodeTypeDeSoin() + 
								"&codeSousTypeDeSoinList="+ courant.getCodeSousTypeDeSoin() +
								"&idTiers=" + courant.getIdTiers() +
								"&nss=" + courant.getNss() +
								"&provenance=" + courant.getProvenance() +
								"&nom=" + courant.getNom().replace("'","") +
								"&prenom=" + courant.getPrenom().replace("'","") +
								"&dateNaissance=" + courant.getDateNaissance() +
								"&csSexe=" + courant.getCsSexe() +
								"&csNationalite=" + courant.getCsNationalite() +
								"&csCanton=" + courant.getCsCanton() +
								"&dateDeces=" + courant.getDateDeces() +
								"&idDecision=" + courant.getIdDecision() + 
								"&isAfficherDetail=true";
			
			String detailUrl = "parent.location.href='" + urlForMenuPopUp +"'";
		%>
		<TD class="mtd" nowrap>
		<% 
			if (iterH.isOrphelin()) {%>
			-&nbsp;&nbsp;
		<%	} else {	
				for (int idPosition = 1; idPosition < iterH.getPosition(); ++idPosition) { %>
				&nbsp;&nbsp;
		<% 	}
				} %>
				
	     	<ct:menuPopup menu="cygnus-optionsdemandes" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=urlForMenuPopUp%>">
		 		<ct:menuParam key="idDemande" value="<%=courant.getIdDemande()%>"/>
		 		<ct:menuParam key="idDecision" value="<%=courant.getIdDecision()%>"/>
		 		<ct:menuParam key="codeTypeDeSoin" value="<%=courant.getCodeTypeDeSoin()%>"/>
		 		<%--<ct:menuParam key="idQdprincipale" value="<%=courant.getIdQdPrincipale()%>"/>--%>
		 		<% if (!courant.getCsEtat().equals(IRFDemande.PAYE) && 
		 				!(courant.getCsEtat().equals(IRFDemande.VALIDE) &&  courant.getIsPP() && 
		 						courant.getCodeTypeDeSoin().equals(IRFCodeTypesDeSoins.TYPE_17_FRANCHISE_ET_QUOTEPARTS) &&
		 						courant.getCodeSousTypeDeSoin().equals(IRFCodeTypesDeSoins.SOUS_TYPE_17_1_FRANCHISE_ET_QUOTEPARTS))) {%>
					<ct:menuExcludeNode nodeId="demandes_correction"/>
				<%}%>		 		
			 	<% if (!courant.getCsEtat().equals(IRFDemande.ENREGISTRE)) {%>
					<ct:menuExcludeNode nodeId="demandes_imputerSurQd"/>
				<%}%>
		 		
		 	</ct:menuPopup> 
		 	<%--gestion du bouton +/- pour les corrections de demandes --%>
			<% if (iterH.isPere()){%>
				<INPUT id="bouton_<%=courant.getIdDemande()%>" type="button" value="+" onclick="afficherCacher(<%=courant.getIdDemande()%>)">
			<% } %>		 	
     	</TD>
     	
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDetailAssure()%></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getIdDemande()%></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><a style="color:black;text-decoration:none;" href='#' title='<%=viewBean.getSession().getCodeLibelle(courant.getIdTypeDeSoin())%>' ><%=courant.getCodeTypeDeSoin()%></a></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><a style="color:black;text-decoration:none;" href='#' title='<%=viewBean.getSession().getCodeLibelle(courant.getIdSousTypeDeSoin())%>' ><%=courant.getCodeSousTypeDeSoin()%></a></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getDateFacture()%></TD>
     	<%
			String periode = courant.getDateDebutTraitement();
			if (!JadeStringUtil.isBlankOrZero(courant.getDateFinTraitement())) {
				periode += " - " + courant.getDateFinTraitement();
			}
			else {
				periode += " -           ";
			}
		%>
		<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=periode%></TD>
     	<TD align="right" class="mtd" nowrap onClick="<%=detailUrl%>"><%=new FWCurrency(courant.getMontantAPayer()).toStringFormat()%></TD>
     	<%if (viewBean.isPropertieForcerPaiement()){%>
     		<TD align="right" class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getIsForcerPaiement()?new FWCurrency(courant.getMontantDepassementQd()).toStringFormat():viewBean.MONTANT_ZERO%></TD>
		<%}%>     	
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=viewBean.getSession().getCodeLibelle(courant.getCsEtat())%></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=viewBean.getSession().getCodeLibelle(courant.getCsStatut())%></TD>
     	<TD class="mtd" nowrap onClick="<%=detailUrl%>"><%=courant.getGestionnaire()%></TD>
		<%-- /tpl:put --%>
<%@ include file="/theme/list/lineEnd.jspf" %>
	<%-- tpl:put name="zoneTableFooter" --%>
	<%-- /tpl:put --%>
<%@ include file="/theme/list/tableEnd.jspf" %>
	<%-- /tpl:insert --%>