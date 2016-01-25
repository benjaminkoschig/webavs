<%@page import="ch.globaz.amal.business.models.famille.SimpleFamille"%>
<%@page import="globaz.amal.utils.AMContribuableHistoriqueHelper"%>
<%@ include file="/amalRoot/contribuable/contribuableHeader.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/amal.css" rel="stylesheet"/>
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
		</td>
		<td></td>							
	</tr>
	<tr height="2px" style="background-color:#226194;"><td colspan="2"></td></tr>
	
	<%
	
		// Parcours des membres de la famille
		// -----------------------------------
		String rowStyleA = "";
		String rowStyleB = "";
		viewBean.loadHistoriqueFamille(AMContribuableHistoriqueHelper.getContribuableInfos().getIdContribuableInfo());

		int iModulo=0;
		//for(Iterator itMembre=viewBean.getListeContribuables().iterator();itMembre.hasNext();){
		//	Contribuable dossierMembreFamille = (Contribuable)itMembre.next();
		for(Iterator itMembre=viewBean.getListeMemberFamille().iterator();itMembre.hasNext();){
			SimpleFamille dossierMembreFamille = (SimpleFamille)itMembre.next();
			String detailUrl = detailLinkFamille + dossierMembreFamille.getIdFamille();
			String urlMembreSupprimer = linkMembreSupprimer + dossierMembreFamille.getId() +"&selectedTabId=0";
			String linkDetailFamilleNouveau = "amal?userAction=amal.detailfamille.detailfamille.afficher&_method=add";
			linkDetailFamilleNouveau+="&contribuableId="+AMContribuableHistoriqueHelper.getContribuableInfos().getIdContribuableInfo();
			linkDetailFamilleNouveau+="&membreFamilleId="+dossierMembreFamille.getId();

			boolean condition = (iModulo % 2 == 0);
			if (condition) {
				rowStyleA = "amalRow";
				rowStyleB = "amalRowOdd";
			} else {
				rowStyleA = "amalRowOdd";
				rowStyleB = "amalRow";
			}
			iModulo++;
			/*
			// Get the member id
			String memberId = dossierMembreFamille.getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
			// si pas de no AVS, prendre nom-prenom
			if ((null == memberId) || (memberId.length() == 0)) {
				memberId = dossierMembreFamille.getFamille().getNomPrenom();
			}
			List<FamilleContribuableView> familleContribuableMemberArray = viewBean.getListeFamilleContribuableViewMember().get(memberId);
			*/

			// Get the member id
			String memberId = dossierMembreFamille.getIdFamille();
			// si pas de no AVS, prendre nom-prenom
			if ((null == memberId) || (memberId.length() == 0)) {
				memberId = dossierMembreFamille.getNomPrenom();
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
				<tr class="<%=rowStyleA%>" style="height:26px" >
					<td>
						&nbsp;
					</td>
					<td>
						&nbsp;
					</td>
					<td style="border-left:1px solid #B3C4DB;"><b>
						<%=objSession.getCode(dossierMembreFamille.getPereMereEnfant())%>
					</b></td>
					<td>
					<%
					String textToDisplay = "";
					if (!JadeStringUtil.isBlankOrZero(dossierMembreFamille.getFinDefinitive())) {
						textToDisplay = "Date et code de fin : " +dossierMembreFamille.getFinDefinitive();
						textToDisplay += ", "+objSession.getCode(dossierMembreFamille.getCodeTraitementDossier());
						textToDisplay += " - "+objSession.getCodeLibelle(dossierMembreFamille.getCodeTraitementDossier());
					}
					if (!JadeStringUtil.isBlankOrZero(textToDisplay)) {
						if(JadeStringUtil.isBlankOrZero(dossierMembreFamille.getIdTier())){
							textToDisplay+=", et auncune correspondance tiers trouvée";
						}
					%>
						<img
							width="18px"
							height="18px"
							src="<%=request.getContextPath()%>/images/amal/editdelete.png" title="<%=textToDisplay %>" border="0">
					<% 
					}  else if (JadeStringUtil.isBlankOrZero(dossierMembreFamille.getIdTier())) { 
					%>
						<img
							width="18px"
							height="18px"
							src="<%=request.getContextPath()%>/images/amal/status_unknown.png" title="Aucun tiers rattaché !" border="0">
					<% } %>
					
					</td>
					<td><b><%=dossierMembreFamille.getNomPrenom()%>
					</b></td>
					<td></td>
				</tr>
				<tr class="<%=rowStyleA%>" style="height:26px">
					<td></td>
					<td></td>
					<td style="border-left:1px solid #B3C4DB;"></td>
					<td></td>
					<td>
					<%=dossierMembreFamille.getDateNaissance()%>
					</td>
					<td></td>
				</tr>
			</table>
		</td>
		<td>
			<table id="tableauMembreFamilleSubside_<%=iModulo%>" class="infoMembreFamilleSubside" 
			width="100%" width="100%" style="background-color:#B3C4DB;border-collapse:collapse;font-size: 11px;border:0px solid #226194;">
				<col width="28px" align="center"></col><!-- Icone nouveau -->
				<col width="28px" align="center"></col><!-- Icone detail -->
				<col width="48px" align="center"></col><!-- Année -->
				<col width="16px" align="center"></col><!-- Type demande -->
				<col width="16px" align="center"></col><!-- Refus -->
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
				<tr class="<%=rowStyleA%>" style="height:26px; font-size: 11px;"><td colspan="12" style="border-left:1px solid #B3C4DB;">&nbsp;</td></tr>
				<tr height="1px" style="background-color:#B3C4DB"><td colspan="12"></td></tr>
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
					%>
				
				<tr class="<%=rowStyleA%>" style="height:26px; font-size: 11px;">
					<td style="border-left:1px solid #B3C4DB;" colspan="2"> 
						<img src="<%=request.getContextPath()%>/images/amal/view_text.png"
							title="Détails" 
							border="0" 
							width="18px"
							height="18px"
							/>
					</td>
					<td>&nbsp;<%=familleContribuable.getAnneeHistorique()%>&nbsp;</td>
					<td title="<%=objSession.getCodeLibelle(familleContribuable.getTypeDemande())%>">
					<%=objSession.getCode(familleContribuable.getTypeDemande())%>
					</td>
					<td title="<%=(familleContribuable.getRefus()?"Refus":"")%>">
					<%=(familleContribuable.getRefus()?"R":"")%>
					</td>
					<td><%=familleContribuable.getMontantContribution()%>&nbsp;&nbsp;&nbsp;</td>
					<%
					String myCurrentCS = familleContribuable.getNoModeles();
					%>
					<td title="<%=objSession.getCodeLibelle(myCurrentCS)%>">
						<%=objSession.getCode(myCurrentCS)%>
					</td>
					<td title="<%=objSession.getCodeLibelle(myCurrentCS)%>">
						<%=" - " +viewBean.getLibelleCodeSysteme(objSession.getCode(myCurrentCS)) %>
					</td>
					<td><%=familleContribuable.getDateEnvoi()%></td>
					<td><%=familleContribuable.getDebutDroit()%></td>
					<td><%=familleContribuable.getFinDroit()%></td>
					<td>
						<img
							width="12px"
							height="12px"
							src="<%=request.getContextPath()%>/images/amal/link.png" title="Journalisation" border="0">
						</a>
						<sup></sup>
					</td>
				</tr>
				<%
					if(iSubside==1 && familleContribuableMemberArray.size()>1){
				%>
				<tr height="1px" style="background-color:#B3C4DB"><td colspan="8"></td></tr>
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
						<img src="<%=request.getContextPath()%>/images/amal/view_right.png"
							title="Nouveau subside" 
							border="0" 
							width="18px"
							height="18px"
							/>
					</td>
					<td colspan="7"></td>
				</tr>
				<tr height="1px" style="background-color:#B3C4DB"><td colspan="8"></td></tr>
				<tr class="<%=rowStyleA%>" style="height:26px; font-size: 11px;"><td colspan="8" align="left" style="border-left:1px solid #B3C4DB;">Pas de subside alloué</td></tr>
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
Boolean bOldStyle=false;
if(bOldStyle){
%>

<table width="100%" border="0">
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
		<td></td>
		<td colspan="12"></td>							
	</tr>
	<tr style="background-color:#B3C4DB"><td colspan="12"></td></tr>
	<% 																			
		String rowStyle = "";
		int i=0;
		viewBean.loadHistoriqueFamille(AMContribuableHistoriqueHelper.getContribuableInfos().getIdContribuableInfo());
		for(Iterator itMembre=viewBean.getListeMemberFamille().iterator();itMembre.hasNext();){
			SimpleFamille dossierMembreFamille = (SimpleFamille)itMembre.next();
			String detailUrl = detailLinkFamille + dossierMembreFamille.getIdFamille();
			//String urlMembreSupprimer = linkMembreSupprimer + dossierMembreFamille.getFamille().getId() +"&selectedTabId=0";
			boolean condition = (i % 2 == 0);
			if (condition) {
				rowStyle = "amalRow";
			} else {
				rowStyle = "amalRowOdd";
			}
			i++;
			// Get the member id
			String memberId = null;//dossierMembreFamille.getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
			// si pas de no AVS, prendre nom-prenom
			if ((null == memberId) || (memberId.length() == 0)) {
				memberId = dossierMembreFamille.getNomPrenom();
			}
			List<FamilleContribuableView> familleContribuableMemberArray = viewBean.getListeFamilleContribuableViewMember().get(memberId);

	%>
	<tr style="height:26px" class="<%=rowStyle%>" onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'amalRowHighligthed')" onMouseOut="jscss('swap', this, 'amalRowHighligthed', '<%=rowStyle%>')">
		<td>											
			<a href="<%=detailUrl%>">
			<img
				width="22px"
				height="22px"
			 	src="<%=request.getContextPath()%>/images/amal/edit_user.png" title="Détails" border="0">
			</a>
		</td>
		<td>
			<img
				width="22px"
				height="22px"
				src="<%=request.getContextPath()%>/images/amal/delete_user_bw.png"
				title="<ct:FWLabel key="JSP_AM_FA_D_TITLE_IMG_DEL_USER"/>" border="0"/>		
			<!--<img width="22px" height="22px" src="<%=request.getContextPath()%>/images/amal/delete_user.png" title="<ct:FWLabel key="JSP_AM_FA_D_TITLE_IMG_DEL_USER"/>" border="0">-->
		</td>
		<td>
		</td>

		
		<td><%=dossierMembreFamille.getNomPrenom()%></td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td><%=objSession.getCode(dossierMembreFamille.getPereMereEnfant())%></td>
		<td><%=dossierMembreFamille.getDateNaissance()%></td>
		<td><%=AMContribuableHelper.getSexe(dossierMembreFamille.getSexe())%></td>
		<td><%=dossierMembreFamille.getFinDefinitive()%></td>
		<td title="<%=objSession.getCodeLibelle(dossierMembreFamille.getCodeTraitementDossier())%>"><%=objSession.getCode(dossierMembreFamille.getCodeTraitementDossier())%></td>
		<td>&nbsp;</td>
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
					<tr style="height:22px" class="<%=rowStyleContrib%>" onMouseOver="jscss('swap', this, '<%=rowStyleContrib%>', 'amalRowHighligthedContrib')" onMouseOut="jscss('swap', this, 'amalRowHighligthedContrib', '<%=rowStyleContrib%>')">
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
					<tr style="height:22px" class="<%=rowStyleContrib%>" onMouseOver="jscss('swap', this, '<%=rowStyleContrib%>', 'amalRowHighligthedContrib')" onMouseOut="jscss('swap', this, 'amalRowHighligthedContrib', '<%=rowStyleContrib%>')">
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
}
%>				
</div>