<%-- tpl:insert page="/theme/list.jtpl" --%>
	<%@ page language="java" errorPage="/errorPage.jsp" %>
	
	<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
	<%@ include file="/theme/list/header.jspf" %>

	<%-- tpl:put name="zoneScripts" --%>
		<%
			globaz.corvus.vb.demandes.REDemandeRenteJointDemandeListViewBean viewBean = (globaz.corvus.vb.demandes.REDemandeRenteJointDemandeListViewBean) request.getAttribute("viewBean");
			
			size = viewBean.getSize ();
			
			detailLink = "corvus?userAction="+globaz.corvus.servlet.IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".afficher&selectedId=";
			String dateDernierPaiement = viewBean.getDateDernierPaiement();
			
			// Les labels de cette page commence par le préfix "JSP_DRE_L"
		%>

		<%@page import="globaz.corvus.api.demandes.IREDemandeRente"%>
		
		<script language="JavaScript">
		</script>
	<%-- /tpl:put --%>

	<%@ include file="/theme/list/javascripts.jspf" %>
	
	<%-- tpl:put name="zoneHeaders" --%>
	    <th colspan="2">
	    	<ct:FWLabel key="JSP_DRE_L_EN_COURS"/>
    	</th>
	    <th colspan="2">
	    	<ct:FWLabel key="JSP_DRE_R_DETAIL_REQUERANT"/>
    	</th>
	    <th>
	    	<ct:FWLabel key="JSP_DRE_L_TYPEDEMANDE"/>
    	</th>
	    <th>
	    	<ct:FWLabel key="JSP_DRE_L_DATEREC"/>
    	</th>
	    <th>
	    	<ct:FWLabel key="JSP_DRE_L_DATEDEB_DATEFIN"/>
    	</th>
	    <th>
	    	<ct:FWLabel key="JSP_DRE_L_ETAT"/>
    	</th>
	    <th>
		    <ct:FWLabel key="JSP_DRE_L_GESTIONNAIRE"/>
	    </th>
	    <th>
		    <ct:FWLabel key="JSP_DRE_L_NO"/>
	    </th>
    <%-- /tpl:put --%>
    
	<%@ include file="/theme/list/tableHeader.jspf" %>
    
    <%-- tpl:put name="zoneCondition" --%>
		<%
			globaz.corvus.vb.demandes.REDemandeRenteJointDemandeViewBean courant = null;
		
			try {
				courant = (globaz.corvus.vb.demandes.REDemandeRenteJointDemandeViewBean) viewBean.getEntity(i);
			} catch (Exception e) {
				break;
			}
			actionDetail = "parent.location.href='" + detailLink + courant.getIdDemandeRente() + "'";
		%>
    <%-- /tpl:put --%>

	<%@ include file="/theme/list/lineStyle.jspf" %>
		
	<%-- tpl:put name="zoneList" --%>
		<td class="mtd" nowrap>
			<ct:menuPopup menu="corvus-optionsdemanderentes" detailLabelId="MENU_OPTION_DETAIL" detailLink="<%=detailLink + courant.getIdDemandeRente()%>">
				<ct:menuParam key="selectedId" value="<%=courant.getIdDemandeRente()%>"/>
				<ct:menuParam key="noDemandeRente" value="<%=courant.getIdDemandeRente()%>"/>
				<ct:menuParam key="idTierRequerant" value="<%=courant.getIdTierRequerant()%>"/>
				<ct:menuParam key="idRenteCalculee" value="<%=courant.getIdRenteCalculee()%>"/>
				<ct:menuParam key="csTypeDemande" value="<%=courant.getCsTypeDemande()%>"/>
				<ct:menuParam key="numNSS" value="<%=courant.getNoAVS()%>"/>
				<ct:menuParam key="idBasesCalcul" value=""/>
				<ct:menuParam key="isPreparerDecision" value="true"/>
	
				<%if (courant.getCsEtatDemande().equals(globaz.corvus.api.demandes.IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE) ||
					  courant.getCsEtatDemande().equals(globaz.corvus.api.demandes.IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE) ||
					  courant.getCsEtatDemande().equals(globaz.corvus.api.demandes.IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE) ||
					  courant.getCsEtatDemande().equals(globaz.corvus.api.demandes.IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE)){ %>
						<ct:menuExcludeNode nodeId="saisieManuelle"/>
				<%} %>
					
				<%if (courant.getCsEtatDemande().equals(globaz.corvus.api.demandes.IREDemandeRente.CS_ETAT_DEMANDE_RENTE_VALIDE)){%>					
					<ct:menuExcludeNode nodeId="preparerDecision"/>
					<ct:menuExcludeNode nodeId="prepIntMoratoires"/>
					<ct:menuParam key="isPreparerDecision" value="false"/>
				<%} else {%>
					<%if (!courant.isPreparationDecisionValide(dateDernierPaiement) && !courant.getCsEtatDemande().equals(globaz.corvus.api.demandes.IREDemandeRente.CS_ETAT_DEMANDE_RENTE_COURANT_VALIDE)){%>
						<ct:menuExcludeNode nodeId="preparerDecision"/>
						<ct:menuParam key="isPreparerDecision" value="false"/>
					<%}%>		
				<%}%>
				
				<%if (globaz.corvus.api.demandes.IREDemandeRente.CS_TYPE_DEMANDE_RENTE_VIEILLESSE.equals(courant.getCsTypeDemande()) ||
					  globaz.corvus.api.demandes.IREDemandeRente.CS_TYPE_DEMANDE_RENTE_SURVIVANT.equals(courant.getCsTypeDemande())){%>				
						<ct:menuExcludeNode nodeId="copierDemande"/>
				<%}%>
			
				<%if ( courant.isInfoComplRenteVeuvePerdure() || globaz.corvus.api.demandes.IREDemandeRente.CS_TYPE_CALCUL_PREVISIONNEL.equals(courant.getCsTypeCalcul()) ||
					  globaz.corvus.api.demandes.IREDemandeRente.CS_TYPE_CALCUL_BILATERALES.equals(courant.getCsTypeCalcul())){%>
				  	<%if (!IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TRANSFERE.equals(courant.getCsEtatDemande()) && !IREDemandeRente.CS_ETAT_DEMANDE_RENTE_TERMINE.equals(courant.getCsEtatDemande())){%>			
						<ct:menuActivateNode active="true" nodeId="terminer"/>
					<%} else {%>
						<ct:menuExcludeNode nodeId="terminer"/>
					<%}%>
				<%} else {%>
					<ct:menuExcludeNode nodeId="terminer"/>
				<%}%>
			</ct:menuPopup>
		</td>
		<td class="mtd" nowrap onClick="<%=actionDetail%>">
			<% if (Integer.parseInt(courant.getIsEnCours())>0) { %>
				<img src="<%=request.getContextPath()+"/images/ok.gif"%>">
			<% } else { %>
				<img src="<%=request.getContextPath()+"/images/erreur.gif"%>">
			<% } %>
		</td>
		<td class="mtd" nowrap onClick="<%=actionDetail%>">
			<%=courant.getDetailRequerantDecede()%>
		</td>
		<td class="mtd">
			<a href="#" onclick="window.open('<%=servletContext%><%=("/corvus")%>?userAction=<%=globaz.corvus.servlet.IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE%>.actionAfficherDossierGed&amp;noAVSId=<%=courant.getNoAVS()%>&amp;idTiersExtraFolder=<%=courant.getIdTierRequerant()%>&amp;serviceNameId=<%=viewBean.getSession().getApplication().getProperty(globaz.externe.IPRConstantesExternes.PROPERTY_AFFICHAGE_DOSSIER_GED)%>')" ><ct:FWLabel key="JSP_LIEN_GED"/></A>				
		</td>
		<%if(courant.hasPostit()){%>
			<td class="mtd" nowrap="nowrap">
				<table width="100%">
					<tr>
						<td onClick="<%=actionDetail%>"><%=courant.getTypeDemande()%>
							&nbsp;
						</td>
						<td align="right">
							<ct:FWNote sourceId="<%=courant.getIdTiersRequerant()%>" tableSource="<%=globaz.corvus.application.REApplication.KEY_POSTIT_RENTES%>"/>
						</td>
					</tr>
				</table>
			</td>
		<%}else{%>
			<td class="mtd" nowrap onClick="<%=actionDetail%>">
				<%=courant.getTypeDemande()%> 
			</td>
		<%}%>
	
		<td class="mtd" nowrap onClick="<%=actionDetail%>">
			<%=courant.getDateReception()%>
		</td>
		<td class="mtd" nowrap onClick="<%=actionDetail%>">
			<%=courant.getDateDebut()+" - "+courant.getDateFin()%>
		</td>
		<td class="mtd" nowrap onClick="<%=actionDetail%>">
			<%=courant.getEtatDemande()%>
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