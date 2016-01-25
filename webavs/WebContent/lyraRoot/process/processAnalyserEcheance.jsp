<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.db.BSessionUtil"%>
<%@taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@page language="java"%>
<%
	boolean isAjournementGED = Boolean.parseBoolean(request.getParameter("AjournementGED"));
	boolean isEnfant18ansGED = Boolean.parseBoolean(request.getParameter("Enfant18ansGED"));
	boolean isEnfant25ansGED = Boolean.parseBoolean(request.getParameter("Enfant25ansGED"));
	boolean isEtudesGED = Boolean.parseBoolean(request.getParameter("EtudeGED"));
	boolean isHommeVieillesseGED = Boolean.parseBoolean(request.getParameter("FemmeVieillesseGED"));
	boolean isFemmeVieillesseGED = Boolean.parseBoolean(request.getParameter("HommeVieillesseGED"));
	boolean isRenteVeufGED = Boolean.parseBoolean(request.getParameter("RenteVeufGED"));
	
	boolean afficherColloneGED = 	isAjournementGED
									|| isEnfant18ansGED
									|| isEnfant25ansGED
									|| isEtudesGED
									|| isFemmeVieillesseGED
									|| isHommeVieillesseGED
									|| isRenteVeufGED;
%>
<script type="text/javascript" src="<%=request.getContextPath()%>/lyraRoot/script/process/processAnalyserEcheance.js"></script>
<script type="text/javascript" >
	globazGlobal.isValidationDecisionAutorise = <%=	request.getParameter("ValidationDecisionAutorise") %>;
	var $msgValidationInterdites = $('#msgValidationInterdites');
	$msgValidationInterdites.hide();
</script>

<div class="area mainContainerAjax masterAreaTable">
	<table width="100%" class="areaTable areaDataTable analyserProcessTable">
		<thead>
			<tr class="forcerHauteurTR">
				<th width="40%">
					<%=JadeStringUtil.escapeXML(" ")%>
				</th>
				<th width="20%">
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("JSP_ECHEANCES_GENERER_LISTE")%>
				</th>
				<th width="20%">
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("JSP_ECHEANCES_GENERER_LETTRE")%>
				</th>
<%	if (afficherColloneGED) { %>
				<th width="20%">
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("JSP_ECHEANCES_MISE_EN_GED")%>
				</th>
<%	} %>
			</tr>
		</thead>
		<tbody>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_18_ANS")%>
				</td>
				<td>
					<input	type="checkbox"
							id="liste18ans" 
							name="isEnfantDe18ans" 
							class="listeBox" 
							checked="checked" />
				</td>
				<td>
					<input	type="checkbox"
							id="lettre18ans" 
							name="isEnfantDe18ansDOC" 
							class="docBox" 
							checked="checked" />
				</td>
<%	if (isEnfant18ansGED) { %>
				<td>
					<input	type="checkbox"
							id="GED18ans" 
							name="isEnfantDe18ansGED"  
							class="gedBox" 
							checked="checked" />
				</td>
<%	} else if (afficherColloneGED) { %>
				<td>
					<%=JadeStringUtil.escapeXML(" ")%>
				</td>
<%	} %>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_25_ANS")%>
				</td>
				<td>
					<input	type="checkbox"
							id="liste25ans" 
							name="isEnfantDe25ans" 
							class="listeBox" 
							checked="checked" />
				</td>
				<td>
					<input	type="checkbox"
							id="lettre25ans" 
							name="isEnfantDe25ansDOC"  
							class="docBox" 
							checked="checked" />
				</td>
<%	if (isEnfant25ansGED) { %>
				<td>
					<input	type="checkbox"
							id="GED25ans" 
							name="isEnfantDe25ansGED"  
							class="gedBox" 
							checked="checked" />
				</td>
<%	} else if (afficherColloneGED) { %>
				<td>
					<%=JadeStringUtil.escapeXML(" ")%>
				</td>
<%	} %>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_ETUDES")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeEtudes" 
							name="isEcheanceEtude" 
							class="listeBox" 
							checked="checked" />
				</td>
				<td>
					<input	type="checkbox"
							id="lettreEtudes" 
							name="isEcheanceEtudeDOC"  
							class="docBox" 
							checked="checked" />
				</td>
<%	if (isEtudesGED) { %>
				<td>
					<input	type="checkbox"
							id="GEDEtudes" 
							name="isEcheanceEtudeGED"  
							class="gedBox" 
							checked="checked" />
				</td>
<%	} else if (afficherColloneGED) { %>
				<td>
					<%=JadeStringUtil.escapeXML(" ")%>
				</td>
<%	} %>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_HOMME_AGE_AVS")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeHommeAgeAVS" 
							name="isHommeArrivantAgeVieillesse" 
							class="listeBox" 
							checked="checked" />
				</td>
				<td>
					<input	type="checkbox"
							id="lettreHommeAgeAVS" 
							name="isHommeArrivantAgeVieillesseDOC"  
							class="docBox" 
							checked="checked" />
				</td>
<%	if (isHommeVieillesseGED) { %>
				<td>
					<input	type="checkbox"
							id="GEDHommeAgeAVS" 
							name="isHommeArrivantAgeVieillesseGED"  
							class="gedBox" 
							checked="checked" />
				</td>
<%	} else if (afficherColloneGED) { %>
				<td>
					<%=JadeStringUtil.escapeXML(" ")%>
				</td>
<%	} %>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_FEMME_AGE_AVS")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeFemmeAgeAVS" 
							name="isFemmeArrivantAgeVieillesse" 
							class="listeBox" 
							checked="checked" />
				</td>
				<td>
					<input	type="checkbox"
							id="lettreFemmeAgeAVS" 
							name="isFemmeArrivantAgeVieillesseDOC"  
							class="docBox" 
							checked="checked" />
				</td>
<%	if (isFemmeVieillesseGED) { %>
				<td>
					<input	type="checkbox"
							id="GEDFemmeAgeAVS" 
							name="isFemmeArrivantAgeVieillesseGED"  
							class="gedBox" 
							checked="checked" />
				</td>
<%	} else if (afficherColloneGED) { %>
				<td>
					<%=JadeStringUtil.escapeXML(" ")%>
				</td>
<%	} %>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_AJOURNEMENT")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeAjournement" 
							name="isAjournement" 
							class="listeBox" 
							checked="checked" />
				</td>
				<td>
					<input	type="checkbox"
							id="lettreAjournement" 
							name="isAjournementDOC"  
							class="docBox" 
							checked="checked" />
				</td>
<%	if (isAjournementGED) { %>
				<td>
					<input	type="checkbox"
							id="GEDAjournement" 
							name="isAjournementGED"  
							class="gedBox" 
							checked="checked" />
				</td>
<%	} else if (afficherColloneGED) { %>
				<td>
					<%=JadeStringUtil.escapeXML(" ")%>
				</td>
<%	} %>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_RENTE_DE_VEUF")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeRenteDeVeuf" 
							name="isRenteDeVeuf" 
							class="listeBox" 
							checked="checked" />
				</td>
				<td>
					<input	type="checkbox"
							id="lettreRenteDeVeuf" 
							name="isRenteDeVeufDOC"  
							class="docBox" 
							checked="checked" />
				</td>
<%	if (isRenteVeufGED) { %>
				<td>
					<input	type="checkbox"
							id="GEDRenteDeVeuf" 
							name="isRenteDeVeufGED" 
							class="gedBox" 
							checked="checked" />
				</td>
<%	} else if (afficherColloneGED) { %>
				<td>
					<%=JadeStringUtil.escapeXML(" ")%>
				</td>
<%	} %>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_CERTIFICAT_DE_VIE")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeCertificatDeVie" 
							name="isCertificatDeVie" 
							class="listeBox" 
							checked="checked" />
				</td>
				<td>
					<%=JadeStringUtil.escapeXML(" ")%>
				</td>
<%	if (afficherColloneGED) { %>
				<td>
					<%=JadeStringUtil.escapeXML(" ")%>
				</td>
<%	} %>
			</tr>
			<tr class="userChoice">
				<td>
					<%=BSessionUtil.getSessionFromThreadContext().getLabel("ANALYSE_ECHEANCE_AUTRES_ECHEANCES")%>
				</td>
				<td>
					<input	type="checkbox"
							id="listeAutresEcheances" 
							name="isAutresEcheances" 
							class="listeBox" 
							checked="checked" />
				</td>
				<td>
					<%=JadeStringUtil.escapeXML(" ")%>
				</td>
<%	if (afficherColloneGED) { %>
				<td>
					<%=JadeStringUtil.escapeXML(" ")%>
				</td>
<%	} %>
			</tr>
			<tr class="forcerHauteurTR">
				<th>
					<%=JadeStringUtil.escapeXML(" ")%>
				</th>
				<th>
					<%=JadeStringUtil.escapeXML(" ")%>
				</th>
				<th>
					<%=JadeStringUtil.escapeXML(" ")%>
				</th>
<%	if (afficherColloneGED) { %>
				<th>
					<%=JadeStringUtil.escapeXML(" ")%>
				</th>
<%	} %>
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
				<td>
					<input	type="checkbox" 
							id="toutesLesLettres" />
				</td>
<%	if (afficherColloneGED) { %>
				<td>
					<input	type="checkbox" 
							id="toutesLesGED" />
				</td>
<%	} %>
			</tr>
		</tbody>
	</table>
</div>
<br/>