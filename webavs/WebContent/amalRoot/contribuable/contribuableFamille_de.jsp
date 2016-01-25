<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.TreeMap"%>
<%@page import="globaz.jade.persistence.model.JadeAbstractModel"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiDetail"%>
<%@page import="ch.globaz.amal.business.services.AmalServiceLocator"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiDetailSearch"%>
<%@ include file="/amalRoot/contribuable/contribuableHeader.jspf" %>
<%
	String newFamilyMemberAction = "amal.famille.famille.nouveau";
	String deleteFamilyMemberAction = "amal.famille.famille.supprimer";
	String newSubsideAction = "amal.detailfamille.detailfamille.afficher";

	boolean hasRightNewFamilyMember = objSession.hasRight(newFamilyMemberAction, FWSecureConstants.ADD);
	boolean hasRightDeleteFamilyMember = objSession.hasRight(deleteFamilyMemberAction, FWSecureConstants.REMOVE);
	boolean hasRightNewSubside = objSession.hasRight(newSubsideAction, FWSecureConstants.ADD);
%>
<script type="text/javascript">
$(document).ready(function() {

});
</script>
<div id="conteneurFamille">
	<table id="tableauFamillePrincipale" style="background-color:#226194;" width="100%" border="0" cellpadding="0">
		<col align="left"></col>
		<col width="540px" align="left"></col>
		<tr>
			<th align="center">Membre</th>
			<th align="center">Subsides</th>
		</tr>
		<tr class="amalRowOdd" style="height:32px">
			<td width="32px" align="center">
				<% if (!contribReprise) {%> 
					<% if (hasRightNewFamilyMember) { %>
				<a href="<%=detailLinkFamilleNouveau%>">
				<img
					width="22px"
					height="22px"
					src="<%=request.getContextPath()%>/images/amal/add_user.png" title="Nouveau" border="0">
				</a>
					<% } %>
				<% } %>
			</td>
			<td></td>							
		</tr>
		<tr height="2px" style="background-color:#226194;"><td colspan="2"></td></tr>
		
		<%
		
			// Parcours des membres de la famille
			// -----------------------------------
			String rowStyleA = "";
			String rowStyleB = "";
			
			int iModulo=0;
			for(Iterator itMembre=viewBean.getListeContribuables().iterator();itMembre.hasNext();){
				Contribuable dossierMembreFamille = (Contribuable)itMembre.next();
				String detailUrl = detailLinkFamille + dossierMembreFamille.getFamille().getIdFamille();
				String urlMembreSupprimer = linkMembreSupprimer + dossierMembreFamille.getFamille().getId() +"&selectedTabId=0";
				String linkDetailFamilleNouveau = "amal?userAction=amal.detailfamille.detailfamille.afficher&_method=add";
				linkDetailFamilleNouveau+="&contribuableId="+dossierMembreFamille.getId();
				linkDetailFamilleNouveau+="&membreFamilleId="+dossierMembreFamille.getFamille().getId();
				linkDetailFamilleNouveau+="&selectedTabId=0";
				
				boolean condition = (iModulo % 2 == 0);
				if (condition) {
					rowStyleA = "amalRow";
					rowStyleB = "amalRowOdd";
				} else {
					rowStyleA = "amalRowOdd";
					rowStyleB = "amalRow";
				}
				iModulo++;
				// Get the member id
				//String memberId = dossierMembreFamille.getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
				String memberId = dossierMembreFamille.getFamille().getIdFamille();
				// si pas de no AVS, prendre nom-prenom
				if ((null == memberId) || (memberId.length() == 0)) {
					memberId = dossierMembreFamille.getFamille().getNomPrenom();
				}
				List<FamilleContribuableView> familleContribuableMemberArray = viewBean.getListeFamilleContribuableViewMember().get(memberId);

		%>
		<tr style="background-color:#B3C4DB;">
			<td>
				<table id="tableauMembreFamilleInfo_<%=iModulo%>" class="infoMembreFamille"
					width="100%" style="background-color:#B3C4DB;border-collapse:collapse;font-size: 12px;border:0px solid #226194;"
					>
					<col width="32px" align="center"></col>
					<col width="32px" align="center"></col>
					<col width="18px" align="center"></col>
					<col width="32px" align="center"></col>
					<col width="250px" align="left"></col>
					<col align="left"></col>
					<tr class="<%=rowStyleA%>" style="height:26px">
						<td>
							<a href="<%=detailUrl%>">
							<img src="<%=request.getContextPath()%>/images/amal/edit_user.png"
								title="Détails" 
								border="0" 
								width="22px"
								height="22px"
								>
							</a>
						</td>
						<td>
							<%if ((null!= familleContribuableMemberArray && familleContribuableMemberArray.size()>0) 
									|| dossierMembreFamille.getFamille().getIsContribuable() || !hasRightDeleteFamilyMember) { %>
								<% if (dossierMembreFamille.getFamille().getIsContribuable()) {
								%>
								<img
									width="22px"
									height="22px"
									src="<%=request.getContextPath()%>/images/amal/delete_user_bw.png"
									title="<ct:FWLabel key="JSP_AM_FA_D_TITLE_IMG_DEL_USER_CONTRIB_PRINC"/>" border="0"/>
								<% } else { %>
									<img
									width="22px"
									height="22px"
									src="<%=request.getContextPath()%>/images/amal/delete_user_bw.png"
									title="<ct:FWLabel key="JSP_AM_FA_D_TITLE_IMG_DEL_USER"/>" border="0"/>
								<% } %>
							<% } else { %>
								<img
									width="22px"
									height="22px"
									onMouseOver="this.style.cursor='hand';" 
									onMouseOut="this.style.cursor='pointer';"
									onClick="delMembreFamille('<%=urlMembreSupprimer%>')"
									src="<%=request.getContextPath()%>/images/amal/delete_user.png"
									title="Supprimer le membre" border="0"/>																																								
							<% } %>
						</td>
						<td style="border-left:1px solid #B3C4DB;"><b>
							<%=objSession.getCode(dossierMembreFamille.getFamille().getPereMereEnfant())%>
						</b></td>
						<td>
						<%
						String textToDisplay = "";
						if (!JadeStringUtil.isBlankOrZero(dossierMembreFamille.getFamille().getFinDefinitive())) {
							textToDisplay = "Date et code de fin : " +dossierMembreFamille.getFamille().getFinDefinitive();
							textToDisplay += ", "+objSession.getCode(dossierMembreFamille.getFamille().getCodeTraitementDossier());
							textToDisplay += " - "+objSession.getCodeLibelle(dossierMembreFamille.getFamille().getCodeTraitementDossier());
						} else if (!JadeStringUtil.isBlankOrZero(dossierMembreFamille.getPersonneEtendue().getPersonne().getDateDeces())) {
							textToDisplay +="Décès : "+dossierMembreFamille.getPersonneEtendue().getPersonne().getDateDeces();
						}
						if (!JadeStringUtil.isBlankOrZero(textToDisplay)) {
							if(JadeStringUtil.isBlankOrZero(dossierMembreFamille.getFamille().getIdTier())){
								textToDisplay+=", et aucune correspondance tiers trouvée";
							}
						%>
							<img
								width="18px"
								height="18px"
								src="<%=request.getContextPath()%>/images/amal/editdelete.png" title="<%=textToDisplay %>" border="0">
						<% 
						}  else if (JadeStringUtil.isBlankOrZero(dossierMembreFamille.getFamille().getIdTier())) { 
						%>
							<img
								width="18px"
								height="18px"
								src="<%=request.getContextPath()%>/images/amal/status_unknown.png" title="Aucun tiers rattaché !" border="0">
						<% } %>
						
						</td>
						<td><b>
						<%if (JadeStringUtil.isBlankOrZero(dossierMembreFamille.getPersonneEtendue().getPersonne().getDateDeces())) { %>
						
						<%=(!JadeStringUtil.isBlankOrZero(dossierMembreFamille.getFamille().getIdTier())?
								dossierMembreFamille.getPersonneEtendue().getTiers().getDesignation1() +" "+
								dossierMembreFamille.getPersonneEtendue().getTiers().getDesignation2()
								:dossierMembreFamille.getFamille().getNomPrenom())%>

						<% } else { %>
							<span  style="color:red">(</span>
						<%=(!JadeStringUtil.isBlankOrZero(dossierMembreFamille.getFamille().getIdTier())?
								dossierMembreFamille.getPersonneEtendue().getTiers().getDesignation1() +" "+
								dossierMembreFamille.getPersonneEtendue().getTiers().getDesignation2()
								:dossierMembreFamille.getFamille().getNomPrenom())%>
							<span  style="color:red">)</span>
							<span style="font-family:wingdings">U</span>
						<% } %>

						<%
							if(!JadeStringUtil.isBlankOrZero(dossierMembreFamille.getFamille().getIdTier())){
						%>
							</b>
								<a href="<%=servletContext + "/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId="+dossierMembreFamille.getFamille().getIdTier()%>">
								<img
									width="12px"
									height="12px"
									src="<%=request.getContextPath()%>/images/amal/link.png" title="Tiers <%=dossierMembreFamille.getFamille().getIdTier() %>" border="0">
								</a>
								<sup>
								</sup>
							<b>
						<%
							}
						%>
						</b></td>
						<td><%=dossierMembreFamille.getPersonneEtendue().getPersonneEtendue().getNumContribuableActuel()%></td>
					</tr>
					<tr class="<%=rowStyleA%>" style="height:26px">
						<td></td>
						<td></td>
						<td style="border-left:1px solid #B3C4DB;"></td>
						<td></td>
						<td>
						<%=(!JadeStringUtil.isBlankOrZero(dossierMembreFamille.getFamille().getIdTier())?
							dossierMembreFamille.getPersonneEtendue().getPersonne().getDateNaissance()
							:dossierMembreFamille.getFamille().getDateNaissance())%>
						<%=" - "+AMContribuableHelper.getSexe(dossierMembreFamille.getPersonneEtendue().getPersonne().getSexe())%>
						</td>
						<td><%=dossierMembreFamille.getPersonneEtendue().getPersonneEtendue().getNumAvsActuel()%></td>
					</tr>
				</table>
			</td>
			<td>
				<table id="tableauMembreFamilleSubside_<%=iModulo%>" class="infoMembreFamilleSubside" 
				width="100%" style="background-color:#B3C4DB;border-collapse:collapse;font-size: 11px;border:0px solid #226194;">
					<col width="28px" align="center"></col><!-- Icone nouveau -->
					<col width="28px" align="center"></col><!-- Icone detail -->
					<col width="48px" align="center"></col><!-- Année -->
					<col width="16px" align="center"></col><!-- Type demande -->
					<col width="16px" align="center"></col><!-- Refus -->
					<col width="16px" align="center"></col><!-- Status -->
					<col width="66px" align="right"></col><!-- Contribution -->
					<col widht="48px" align="right"></col><!-- Document no -->
					<col width="90px" align="left"></col><!-- Document -->
					<col width="80px" align="left"></col><!-- Envoi -->
					<col width="60px" align="center"></col><!-- Début -->
					<col width="60px" align="center"></col><!-- Fin -->
					<col width="20px" align="left"></col><!-- Icone Journalisation -->
					<%
					if(null!= familleContribuableMemberArray && familleContribuableMemberArray.size()>0){
						
						if(familleContribuableMemberArray.size()==1){
						// Ajout d'une ligne vide
					%>
					<tr class="<%=rowStyleA%>" style="height:26px; font-size: 11px;"><td colspan="13" style="border-left:1px solid #B3C4DB;">&nbsp;</td></tr>
					<tr height="1px" style="background-color:#B3C4DB"><td colspan="13"></td></tr>
					<%
						}
					
						// Parcours des subsides du membres
						// --------------------------------
						int iSubside = 0;
						for (Iterator itFamille = familleContribuableMemberArray.iterator(); itFamille.hasNext();) {
							FamilleContribuableView familleContribuable = (FamilleContribuableView) itFamille.next();
							// SHOW ONLY 2 ROWS
							if(iSubside>=2){
								break;
							}
							iSubside++;
							String familleDetailLinkDetailFamille= detailLinkDetailFamille+familleContribuable.getDetailFamilleId();
							familleDetailLinkDetailFamille+="&selectedTabId=0";
						%>
					
					<tr class="<%=rowStyleA%><%=!familleContribuable.isCodeActif()?" subsideDisabled":""%>"  onMouseOver="jscss('swap', this, '<%=rowStyleA%>', 'amalRowHighligthedContrib')" onMouseOut="jscss('swap', this, 'amalRowHighligthedContrib', '<%=rowStyleA%>')" style="height:26px; font-size: 11px;">
						<td style="border-left:1px solid #B3C4DB;"> 
							<%
								if(iSubside==1){
							%>
							<% if(hasRightNewSubside) { %>
								<a href="<%=linkDetailFamilleNouveau%>">
								<img src="<%=request.getContextPath()%>/images/amal/view_right.png"
									title="Nouveau subside" 
									border="0" 
									width="18px"
									height="18px"
									>
								</a>
							<% } %>
							<%
								}
							%>
						</td>
						<td>
							<a class="showLoading" href="<%=familleDetailLinkDetailFamille%>">
							<img src="<%=request.getContextPath()%>/images/amal/view_text.png"
								title="Détails" 
								border="0" 
								width="18px"
								height="18px"
								>
							</a>
						</td>
						<td>&nbsp;<%=familleContribuable.getAnneeHistorique()%>&nbsp;</td>
						<td title="<%=objSession.getCodeLibelle(familleContribuable.getTypeDemande())%>">
						&nbsp;<%=objSession.getCode(familleContribuable.getTypeDemande())%>&nbsp;
						</td>
						<td title="<%=(familleContribuable.getRefus()?"Refus":"")%>">
						&nbsp;<%=(familleContribuable.getRefus()?"R":"")%>&nbsp;
						</td>
						<td title="<%=objSession.getCodeLibelle(familleContribuable.getCodeTraitementDossier())%>">
						&nbsp;<%=objSession.getCode(familleContribuable.getCodeTraitementDossier())%>&nbsp;
						</td>
						<%	
							double familleMontantContribution = 0.0;
							double familleMontantSupplement = 0.0;
							String familleMontantContributionTotal = "0.0";
							try{
								familleMontantContribution = Double.parseDouble(familleContribuable.getMontantContribution());
               					familleMontantSupplement= Double.parseDouble(familleContribuable.getSupplExtra());
               					familleMontantContributionTotal = ""+(familleMontantContribution+familleMontantSupplement);
               					FWCurrency currentMontant = new FWCurrency();
               					currentMontant.add(familleMontantContributionTotal);
               					familleMontantContributionTotal = currentMontant.toStringFormat();
							}catch(Exception ex){
								
							}
               			 %>
						<td><%=familleMontantContributionTotal%>&nbsp;&nbsp;&nbsp;</td>
						<%
						ComplexControleurEnvoiDetailSearch complexControleurEnvoiDetailSearch = new ComplexControleurEnvoiDetailSearch();
						complexControleurEnvoiDetailSearch.setForAnneeHistorique(familleContribuable.getAnneeHistorique());
						complexControleurEnvoiDetailSearch.setForIdFamille(familleContribuable.getFamilleId());
						ArrayList<String> inStatusEnvoi = new ArrayList<String>();
						inStatusEnvoi.add(IAMCodeSysteme.AMDocumentStatus.MANUALGENERATED.getValue());
						inStatusEnvoi.add(IAMCodeSysteme.AMDocumentStatus.AUTOGENERATED.getValue());
						inStatusEnvoi.add(IAMCodeSysteme.AMDocumentStatus.PRINTED.getValue());
						inStatusEnvoi.add(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue());											
						complexControleurEnvoiDetailSearch.setInStatusEnvoi(inStatusEnvoi);
						complexControleurEnvoiDetailSearch = AmalServiceLocator.getControleurEnvoiService()
								.search(complexControleurEnvoiDetailSearch);
						
						String myCurrentCSTemp = "";
						String myCurrentCSCodeTemp = "";
						String idDetailFamilleWithTempDoc = "";
						try {
							int size = complexControleurEnvoiDetailSearch.getSize();
							if (complexControleurEnvoiDetailSearch.getSize()>0) {
													ComplexControleurEnvoiDetail complexControleurEnvoiDetail = (ComplexControleurEnvoiDetail) complexControleurEnvoiDetailSearch.getSearchResults()[size-1];
							
								myCurrentCSTemp = complexControleurEnvoiDetail.getNumModele();
								myCurrentCSCodeTemp =objSession.getCode(myCurrentCSTemp);
								
								idDetailFamilleWithTempDoc = complexControleurEnvoiDetail.getIdDetailFamille();
							}
						} catch (Exception e) {
							//
						}
						
						String myCurrentCS = familleContribuable.getNoModeles();
						String myCurrentCSCode =objSession.getCode(myCurrentCS);
						int iNbChar = myCurrentCSCode.length();
						for(int iChar = 1; iChar<4-iNbChar;iChar++){
							myCurrentCSCode=" "+myCurrentCSCode;
						}
						
						%>
						<%if (JadeStringUtil.isBlankOrZero(myCurrentCSTemp)) { %>
							<%if (JadeStringUtil.isBlankOrZero(myCurrentCSCode)) { %>
								<td></td>
								<td>
									-
								</td>
							<% } else { %>
								<td title="<%=objSession.getCodeLibelle(myCurrentCS)%>">
									<%=myCurrentCSCode%>
								</td>
								<td title="<%=objSession.getCodeLibelle(myCurrentCS)%>">
								<%=" - " +viewBean.getLibelleCodeSysteme(objSession.getCode(myCurrentCS)) %>
								</td>																
							<% } %>
						<%} else { %>
							<% if (familleContribuable.getDetailFamilleId().equals(idDetailFamilleWithTempDoc)) { %> 
									<td title="Non envoy&eacute; : <%=objSession.getCodeLibelle(myCurrentCSTemp)%>">
										<span style="font-style:italic;"><%=myCurrentCSCodeTemp%></span>
									</td>
									<td title="Non envoy&eacute; : <%=objSession.getCodeLibelle(myCurrentCSTemp)%>">
										<span style="font-style:italic;"><%=" - " +viewBean.getLibelleCodeSysteme(objSession.getCode(myCurrentCSTemp)) %></span>
									</td>	
							<%} else { %>
									<td title="<%=objSession.getCodeLibelle(myCurrentCS)%>">
										<%=myCurrentCSCode%>
									</td>
									<td title="<%=objSession.getCodeLibelle(myCurrentCS)%>">
										<%=" - " +viewBean.getLibelleCodeSysteme(objSession.getCode(myCurrentCS)) %>
									</td>
							<%} %>
								
						<% } %>
						<td><%=familleContribuable.getDateEnvoi()%></td>
						<td><%=familleContribuable.getDebutDroit()%></td>
						<td><%=familleContribuable.getFinDroit()%></td>
						<td>
						<%
							if(!JadeStringUtil.isBlankOrZero(dossierMembreFamille.getFamille().getIdTier())){
						%>
							<a href="<%=servletContext + "/libra?userAction=libra.journalisations.journalisations.chercher&selectedId=0&idTiers="+dossierMembreFamille.getFamille().getIdTier()%>">
							<img
								width="12px"
								height="12px"
								src="<%=request.getContextPath()%>/images/amal/link.png" title="Journalisation" border="0">
							</a>
							<sup></sup>
						<%
							}
						%>
						</td>
					</tr>
					<%
						if(iSubside==1 && familleContribuableMemberArray.size()>1){
					%>
					<tr height="1px" style="background-color:#B3C4DB"><td colspan="12"></td></tr>
					<%
						}
					%>
					<%
						// Fin du parcours des subsides
						// ----------------------------
						}
					// fin du if taille et null
					}else{
					%>
					<tr class="<%=rowStyleA%>" style="height:26px; font-size: 11px;">
						<td align="left" style="border-left:1px solid #B3C4DB;">
							<%if(hasRightNewSubside) { %>
								<a href="<%=linkDetailFamilleNouveau%>">
								<img src="<%=request.getContextPath()%>/images/amal/view_right.png"
									title="Nouveau subside" 
									border="0" 
									width="18px"
									height="18px"
									>
								</a>
							<% } %>
						</td>
						<td colspan="11"></td>
					</tr>
					<tr height="1px" style="background-color:#B3C4DB"><td colspan="11"></td></tr>
					<tr class="<%=rowStyleA%>" style="height:26px; font-size: 11px;"><td colspan="11" align="left" style="border-left:1px solid #B3C4DB;width:100%">Pas de subside alloué</td></tr>
					<%
					}
					%>
				</table>
			</td>
		<tr>
		<tr height="1px" style="background-color:#226194;"><td colspan="2"></td></tr>
		<%
			// fin du for parcours des membres de la famille
			// ---------------------------------------------
			}
		%>
	</table>
	
	<%
		Boolean showMe = false;
		if(showMe){
	%>
	
	<table style="background-color:#B3C4DB;" width="100%" border="0">
		<col width="36px" align="center"></col>
		<col width="36px" align="center"></col>
		<col width="36px" align="center"></col>
		<col width="180px" align="left"></col>
		<col width="110px" align="left"></col>
		<col align="left"></col>
		<col align="center"></col>
		<col align="center"></col>
		<col align="center"></col>
		<col align="center"></col>
		<col align="center"></col>
		<col align="center"></col>
		<col align="center"></col>
		<tr>
			<th colspan="3"></th>
			<th><ct:FWLabel key="JSP_AM_CON_D_ONGLET_FAMILLE_NOM_PRENOM"/></th>
			<th><ct:FWLabel key="JSP_AM_CON_D_ONGLET_FAMILLE_NO_CONT"/></th>
			<th><ct:FWLabel key="JSP_AM_CON_D_ONGLET_FAMILLE_NSS"/></th>
			<th><ct:FWLabel key="JSP_AM_CON_D_ONGLET_FAMILLE_TYPE"/></th>
			<th><ct:FWLabel key="JSP_AM_CON_D_ONGLET_FAMILLE_DATE_NAISSANCE"/></th>
			<th><ct:FWLabel key="JSP_AM_CON_D_ONGLET_FAMILLE_SEXE"/></th>
			<th><ct:FWLabel key="JSP_AM_CON_D_ONGLET_FAMILLE_DATE_SORTIE"/></th>
			<th><ct:FWLabel key="JSP_AM_CON_D_ONGLET_FAMILLE_CODE_FIN"/></th>
			<th>Date décès</th>
		</tr>
		<tr class="amalRowOdd" style="height:26px">
			<td>
				<% if (!contribReprise) {%> 
				<a href="<%=detailLinkFamilleNouveau%>">
				<img
					width="22px"
					height="22px"
					src="<%=request.getContextPath()%>/images/amal/add_user.png" title="Nouveau" border="0">
				</a>
				<% } %>
			</td>
			<td colspan="12"></td>							
		</tr>
		<tr height="1px" style="background-color:#B3C4DB;"><td colspan="12"></td></tr>
		<% 
			String rowStyle = "";
			int i=0;
			for(Iterator itMembre=viewBean.getListeContribuables().iterator();itMembre.hasNext();){
				Contribuable dossierMembreFamille = (Contribuable)itMembre.next();
				String detailUrl = detailLinkFamille + dossierMembreFamille.getFamille().getIdFamille();
				String urlMembreSupprimer = linkMembreSupprimer + dossierMembreFamille.getFamille().getId() +"&selectedTabId=0";
				boolean condition = (i % 2 == 0);
				if (condition) {
					rowStyle = "amalRow";
				} else {
					rowStyle = "amalRowOdd";
				}
				i++;
				// Get the member id
				String memberId = dossierMembreFamille.getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
				// si pas de no AVS, prendre nom-prenom
				if ((null == memberId) || (memberId.length() == 0)) {
					memberId = dossierMembreFamille.getFamille().getNomPrenom();
				}
				List<FamilleContribuableView> familleContribuableMemberArray = viewBean.getListeFamilleContribuableViewMember().get(memberId);

		%>
		<tr style="height:26px" class="<%=rowStyle%>">
			<td>											
				<a href="<%=detailUrl%>">
				<img
					width="22px"
					height="22px"
				 	src="<%=request.getContextPath()%>/images/amal/edit_user.png" title="Détails" border="0">
				</a>
			</td>
			<td>
			<%if (!JadeStringUtil.isBlankOrZero(dossierMembreFamille.getFamille().getIdTier())) { %>
				<img
					width="22px"
					height="22px"
					src="<%=request.getContextPath()%>/images/amal/delete_user_bw.png"
					title="<ct:FWLabel key="JSP_AM_FA_D_TITLE_IMG_DEL_USER"/>" border="0"/>		
				<!--<img width="22px" height="22px" src="<%=request.getContextPath()%>/images/amal/delete_user.png" title="<ct:FWLabel key="JSP_AM_FA_D_TITLE_IMG_DEL_USER"/>" border="0">-->
			<% } else { %>
				<img
					width="22px"
					height="22px"
					onMouseOver="this.style.cursor='hand';" 
					onMouseOut="this.style.cursor='pointer';"
					onClick="delMembreFamille('<%=urlMembreSupprimer%>')"
					src="<%=request.getContextPath()%>/images/amal/delete_user.png"
					title="Supprimer le membre" border="0"/>										
			<% } %>
			</td>
			<td><%if(null!= familleContribuableMemberArray && familleContribuableMemberArray.size()>0){%>
				<img class="expandDetailsMember" id="expandMember<%=i%>"
					src="<%=request.getContextPath()%>/images/icon-expand.gif" 
					title="<%="Aperçu des subsides "%>" 
					border="0"
					onMouseOver="this.style.cursor='hand';"
					onMouseOut="this.style.cursor='pointer';"
					width="22px"
					height="22px"
					>
				<% // fin if(null!= familleContribuableMemberArray && familleContribuableMemberArray.size()>0){
				} else {
				%>
				<% }%>
			</td>
	
			
			<td>
				<%if (JadeStringUtil.isBlankOrZero(dossierMembreFamille.getPersonneEtendue().getPersonne().getDateDeces())) { %>
						<%=(!JadeStringUtil.isBlankOrZero(dossierMembreFamille.getFamille().getIdTier())?
								dossierMembreFamille.getPersonneEtendue().getTiers().getDesignation1() +" "+
								dossierMembreFamille.getPersonneEtendue().getTiers().getDesignation2()
								:dossierMembreFamille.getFamille().getNomPrenom())%>
						<% } else { %>
							<span  style="color:red">(</span>
						<%=(!JadeStringUtil.isBlankOrZero(dossierMembreFamille.getFamille().getIdTier())?
								dossierMembreFamille.getPersonneEtendue().getTiers().getDesignation1() +" "+
								dossierMembreFamille.getPersonneEtendue().getTiers().getDesignation2()
								:dossierMembreFamille.getFamille().getNomPrenom())%>
							<span  style="color:red">)</span>
							<span style="font-family:wingdings">U</span>
						<% } %>											
			</td>
			<td>
				<%if (JadeStringUtil.isBlankOrZero(dossierMembreFamille.getFamille().getIdTier())) { %>
					<img
						width="22px"
						height="22px"
						src="<%=request.getContextPath()%>/images/amal/status_unknown.png" title="Aucun tiers rattaché !" border="0">
				<% } %>
				<%=dossierMembreFamille.getPersonneEtendue().getPersonneEtendue().getNumContribuableActuel()%>
			</td>
			<td><%=dossierMembreFamille.getPersonneEtendue().getPersonneEtendue().getNumAvsActuel()%></td>
			<td><%=objSession.getCode(dossierMembreFamille.getFamille().getPereMereEnfant())%></td>
			<td><%=(!JadeStringUtil.isBlankOrZero(dossierMembreFamille.getFamille().getIdTier())?
					dossierMembreFamille.getPersonneEtendue().getPersonne().getDateNaissance()
					:dossierMembreFamille.getFamille().getDateNaissance())%></td>
			<td><%=AMContribuableHelper.getSexe(dossierMembreFamille.getPersonneEtendue().getPersonne().getSexe())%></td>
			<td><%=dossierMembreFamille.getFamille().getFinDefinitive()%></td>
			<td title="<%=objSession.getCodeLibelle(dossierMembreFamille.getFamille().getCodeTraitementDossier())%>"><%=objSession.getCode(dossierMembreFamille.getFamille().getCodeTraitementDossier())%></td>
			<td><%=dossierMembreFamille.getPersonneEtendue().getPersonne().getDateDeces()%></td>
		</tr>
		<tr style="background-color:#B3C4DB">
				<td></td>
				<td></td>
				<td></td>
				<td class="tdSubsidesMember" id="tdSubsidesMember<%=i%>" colspan="9" >
				<%
					if(null!= familleContribuableMemberArray && familleContribuableMemberArray.size()>0){
				%>
					<table class="subsideDetailsMember" id="subsideMember<%=i%>" width="100%"  border="0">
						<col width="18px" align="center"></col>
						<col align="center"></col>
						<col align="right"></col>
						<col width="80px" align="center"></col>
						<col align="center"></col>
						<col align="center"></col>
						<col align="center"></col>
						<col align="right"></col>
						<col align="left"></col>
						<!-- <col align="center"></col> -->
						<col align="center"></col>
						<tr>
							<th></th>
							<th>Année</th>
							<th>Contrib</th>
							<th>Envoi</th>
							<th>Début</th>
							<th>Fin</th>
							<th>Demande</th>
							<th></th>
							<th>Document</th>
							<!-- <th>Code</th> -->
							<th>Suppl.</th>
						</tr>
						<%
						int iStyle = 0;
						String rowStyleContrib = "";
						for (Iterator itFamille = familleContribuableMemberArray.iterator(); itFamille.hasNext();) {
							FamilleContribuableView familleContribuable = (FamilleContribuableView) itFamille.next();
							boolean conditionStyle = (iStyle % 2 == 0);
							if (conditionStyle) {
								rowStyleContrib = "amalRowContrib";
							} else {
								rowStyleContrib = "amalRowOddContrib";
							}
							iStyle++;
							// SHOW ONLY 5 ROWS
							if(iStyle>5){
								break;
							}
							String familleDetailLinkDetailFamille= detailLinkDetailFamille+familleContribuable.getDetailFamilleId();
						%>
						<tr style="height:22px" class="<%=rowStyleContrib%>">
							<td>
							<a href="<%=familleDetailLinkDetailFamille%>">
							<img src="<%=request.getContextPath()%>/images/amal/view_text.png"
								title="Détails du subside <%=familleContribuable.getDebutDroit()%>" 
								border="0" 
								onMouseOver="this.style.cursor='hand';"
								onMouseOut="this.style.cursor='pointer';"
								width="18px"
								height="18px"
								>
							</a>
							</td>
							<td><%=familleContribuable.getAnneeHistorique()%></td>
							<td><%=familleContribuable.getMontantContribution()%></td>
							<td><%=familleContribuable.getDateEnvoi()%></td>
							<td><%=familleContribuable.getDebutDroit()%></td>
							<td><%=familleContribuable.getFinDroit()%></td>
							<td><%=familleContribuable.getDateRecepDemande()%></td>
							<%
							String myCurrentCS = familleContribuable.getNoModeles();
							%>
							<td><%=objSession.getCode(myCurrentCS)%></td>
							<td title="<%=objSession.getCodeLibelle(myCurrentCS)%>">
								<%="- " +viewBean.getLibelleCodeSysteme(objSession.getCode(myCurrentCS)) %>
							</td>
							<td><%=familleContribuable.getSupplExtra()%></td>
						</tr>
						<%
						}
						if(familleContribuableMemberArray.size()>5){
						%>
						<tr style="height:22px" class="<%=rowStyleContrib%>">
							<td>
							<a href="<%=detailUrl%>">
							<img src="<%=request.getContextPath()%>/images/amal/view_detailed.png"
								title="Subsides antérieurs" 
								border="0" 
								onMouseOver="this.style.cursor='hand';"
								onMouseOut="this.style.cursor='pointer';"
								width="18px"
								height="18px"
								>
							</a>
							</td>
							<td>...</td>
							<td colspan="8"></td>
						</tr>
						<%} %>
					</table>
					<%
					// Fin if 	if(null!= familleContribuableArray && familleContribuableArray.size()>0){									
					}
				//}
				%>
				</td>
			</tr>
			
		
		
		<% } %>									
	</table>
	
	<%
			// End showme
			}
	%>
</div>												
