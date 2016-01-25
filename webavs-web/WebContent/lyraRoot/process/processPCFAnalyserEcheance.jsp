<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.db.BSessionUtil"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@page language="java"%>
<script type="text/javascript" src="<%=request.getContextPath()%>/lyraRoot/script/process/processPCFAnalyserEcheance.js"></script>
<div class="area mainContainerAjax masterAreaTable">
	<table width="100%">
		<tbody>
			<tr>
				<td width="12%">
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_PF_CAISSE")%>
				</td>
				<td width="88%">
					<ct:select id="forCsCaisse" name="forCsCaisse" wantBlank="true" notation="data-g-select=\"mandatory:true\"">
						<ct:optionsCodesSystems csFamille="PFCAISSE">
						</ct:optionsCodesSystems>
					</ct:select>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					&#160;
				</td>
			</tr>
		</tbody>
	</table>
	<table width="100%" class="areaTable areaDataTable analyserProcessTable">
		<thead>
			<tr class="forcerHauteurTR">
				<th width="50%">
					&#160;
				</th>
				<th width="50%">
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("JSP_ECHEANCES_GENERER_LISTE")%>
				</th>
			</tr>
		</thead>
		<tbody>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_PF_REVISION_DOSSIER")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeDossierDateRevision" 
							name="isDossierDateRevision" 
							class="listeBox" 
							checked="checked" />
				</td>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_PF_ENFANT_6_ANS")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeEnfantDe6ans" 
							name="isEnfantDe6ans" 
							class="listeBox" 
							checked="checked" />
				</td>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_PF_ENFANT_16_ANS")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeEnfantDe16ans" 
							name="isEnfantDe16ans" 
							class="listeBox" 
							checked="checked" />
				</td>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_PF_ENFANT_18_ANS")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeEnfantDe18ans" 
							name="isEnfantDe18ans" 
							class="listeBox" 
							checked="checked" />
				</td>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_PF_ENFANT_25_ANS")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeEnfantDe25ans" 
							name="isEnfantDe25ans" 
							class="listeBox" 
							checked="checked" />
				</td>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_PF_ETUDIANTS_ET_APPRENTIS")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeEtudiantsEtApprentis" 
							name="isEtudiantsEtApprentis" 
							class="listeBox" 
							checked="checked" />
				</td>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_PF_HOMME_RETRAITE")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeHommeRetraite" 
							name="isHommeRetraite" 
							class="listeBox" 
							checked="checked" />
				</td>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_PF_FEMME_RETRAITE")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeFemmeRetraite" 
							name="isFemmeRetraite" 
							class="listeBox" 
							checked="checked" />
				</td>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_PF_INDEMNITES_JOURNALIERES")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeIndemnitesJournalieres" 
							name="isIndemnitesJournalieres" 
							class="listeBox" 
							checked="checked" />
				</td>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_PF_AIDES_ETUDES")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeAidesEtude" 
							name="isAidesEtudes" 
							class="listeBox" 
							checked="checked" />
				</td>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_PF_ALLOCATIONS")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeAllocations" 
							name="isAllocations" 
							class="listeBox" 
							checked="checked" />
				</td>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_PF_PROJET_DECISION_SANS_CHOIX")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeDecisionProjetSansChoix" 
							name="isDecisionProjetSansChoix" 
							class="listeBox" 
							checked="checked" />
				</td>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_PF_DEMANDES_EN_ATTENTE_DEPUIS_3_MOIS")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeDemandesEnAttente3Mois" 
							name="isDemandesEnAttente3Mois" 
							class="listeBox" 
							checked="checked" />
				</td>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_PF_ECHANCES_LIBRES")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeEcheanceLibre" 
							name="isEcheanceLibre" 
							class="listeBox" 
							checked="checked" />
				</td>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_PF_RENTE_PONT")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeRentePont" 
							name="isRentePont" 
							class="listeBox" 
							checked="checked" />
				</td>
			</tr>
			<tr class="forcerHauteurTR">
				<th>
					&#160;
				</th>
				<th>
					&#160;
				</th>
			</tr>
			<tr>
				<td>
					<strong>
						<%=BSessionUtil.getSessionFromThreadContext().getLabel("JSP_SELECTION_TOUS")%>
					</strong>
				</td>
				<td>
					<input	type="checkbox" 
							id="toutesLesListes" />
				</td>
			</tr>
		</tbody>
	</table>
</div>
<br/>