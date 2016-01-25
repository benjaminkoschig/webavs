<!-- ************************************************************************************* -->
<!-- GLOBAL GLOBAZ IMPORT AND HEADER INCLUSION -->

<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@ page language="java" import="globaz.globall.http.*" errorPage="/errorPage.jsp"  contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>

<!-- FIN GLOBAZ IMPORT AND HEADER INCLUSION -->
<!-- ************************************************************************************* -->






<!-- ************************************************************************************* -->
<!-- INITIALIZATION AND SPECIFIC LAMAL CONTROLEUR ENVOI INCLUSION -->

<%-- tpl:put name="zoneInit" --%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.lang.*"%>
<%@page import="globaz.jade.client.util.*"%>
<%@page import="globaz.jade.log.*"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.amal.vb.controleurRappel.AMControleurRappelViewBean"%>
<%@page import="ch.globaz.amal.business.echeances.AMControleurRappelDetail"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="ch.globaz.amal.business.constantes.IAMActions"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme"%>
<%
	//
	//Les labels de cette page commencent par le préfix "JSP_AM_RE_D"
	idEcran = "AM00xx";

	// Get the viewBean
	AMControleurRappelViewBean viewBean = (AMControleurRappelViewBean) session.getAttribute("viewBean");
	viewBean.retrieveRappels();
	// Disable button
	bButtonNew=false;
	bButtonUpdate=false;
	bButtonDelete=false;
	
	// Action par défaut, base
	String actionAfficher = IAMActions.ACTION_CONTROLEURRAPPEL+".afficher";
	String actionImprimer = IAMActions.ACTION_CONTROLEURRAPPEL+".generateRappel";
	String actionSupprimer = IAMActions.ACTION_CONTROLEURRAPPEL+".supprimer";
	String expandRappel = request.getParameter("expandedRappel");
	
	boolean hasRightNew = objSession.hasRight(userActionNew, FWSecureConstants.ADD);

%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>

<!-- FIN INITIALIZATION AND SPECIFIC LAMAL CONTROLEUR RAPPEL INCLUSION -->
<!-- ************************************************************************************* -->


<!-- ************************************************************************************* -->
<!-- JAVASCRIPT AND CSS PART -->

<%@ include file="/theme/detail/javascripts.jspf"%>

<link rel="stylesheet" type="text/css"
	href="<%=servletContext%><%=(mainServletPath + "Root")%>/css/amal.css"
	rel="stylesheet" />

<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">

// initialisation des fonctions nécessaires à javascript
var MAIN_URL="<%=formAction%>";
var expandedRappel="<%=expandRappel%>";
var oldClassNameLine = "";
var iconCollapse = '<%=request.getContextPath()%>/images/icon-collapse.gif';
var iconExpand = '<%=request.getContextPath()%>/images/icon-expand.gif';
var actionImprimer = '<%=actionImprimer%>';
var actionSupprimer = '<%=actionSupprimer%>';
var hasRightNew = <%=hasRightNew%>;

// déclaration des fonctions standards
function add() {}
function upd() {}
function cancel() {}
function validate() {}
function init() {}

function postInit(){
	// Activer les boutons principaux et les combobox
	// du tableauPrincipal
	// ----------------------------------------------------
	$('#tableauPrincipal [type=button]').each(function (iIndex) {
		var idButton=this.id;
		var idButtonImprimerPrincipale="buttonImprimerPrincipale_";
		var idButtonSupprimerPrincipale="buttonSupprimerPrincipale_";
		// check the name to find the correct button
		if(idButton.length>=idButtonImprimerPrincipale.length){
			if(idButton.substring(0,idButtonImprimerPrincipale.length)==idButtonImprimerPrincipale){
				setDisabledElement(idButton, false);
			}else if(idButton.substring(0,idButtonSupprimerPrincipale.length)==idButtonSupprimerPrincipale){
				setDisabledElement(idButton, false);
			}
		}
	});
	
	// Trigger the event click on imgExpand if expandRappel defini
	// Reçu comme paramètre
	var expandRappel = expandedRappel;
	if(expandRappel>0){
		expandRappel="imgExpand_"+expandRappel;
		$('#'+expandRappel).trigger('click');
	}

}

$(document).ready(function() {
	
//showProgress('#inProgressDialog');
//hideProgress('#inProgressDialog');

	
});

</script>




<!-- Script spécific controleur rappel -->
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/controleurrappel/controleurrappel.js"/></script>
<%-- /tpl:put --%>

<!-- Inclusion de la page permettant d'afficher une box de progress -->
<%@ include file="/amalRoot/progressDialog/progressDialog.jspf" %>

<!-- FIN JAVASCRIPT AND CSS PART -->
<!-- ************************************************************************************* -->



<!-- ************************************************************************************* -->
<!-- ZONE PRINCIPALE TITLE AND BODY -->
<%@ include file="/theme/detail/bodyStart.jspf"%>

<%-- tpl:insert attribute="zoneTitle" --%>
Gestion des rappels
<%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>
 	<tr>
 		<td>
 			<div style="background-image:url('<%=request.getContextPath()%>/images/summary_bg.png');
 						background-repeat:repeat-x">
				<table id="tableauExplication"
						width="100%" 
						align="center" 
						style="border-collapse:collapse; font-size: 11px; border:2px solid #226194;">
						<tr>
							<td>
			                   	<h4>Gestion des rappels</h4><br>
			                   	Cet écran vous permet de contrôler et de générer les différents rappels pour les dossiers incomplets.
			                   	<br>
			                   	<br>
								Les actions possibles sont les suivantes :
								<ul>
									<li>Action 'Générer' : génération du job de rappel. A contrôler (imprimer) ensuite depuis 'Générés PDF' 
									<li>Action 'Supprimer' : suppression d'un rappel qui n'aurait pas lieu d'être.
								</ul>
								 <br>&nbsp;
							</td>
						</tr>
				</table>
 			</div>
 		</td>
 	</tr>
 	
 	<tr><td>&nbsp;<td></tr>
	<tr>
		<td>
		
	 	<INPUT type="hidden" name="selectedRappel" value="">
	 	<INPUT type="hidden" name="selectedLibra" value="">
 		<INPUT type="hidden" name="expandedRappel" value="">

		<table id="tableauPrincipal" width="100%" align="center" style="border-collapse:collapse; font-size: 11px; border:2px solid #226194;">
			<col align="center"></col><!-- Expand icon -->
			<col align="left" width="84px"></col><!-- Rappel # -->
			<col align="left"></col><!-- Description -->
			<col align="left"></col><!-- Vide -->
			<col align="center"></col><!-- Bouton Générer -->
			<col align="center"></col><!-- Vide -->
			<col align="center"></col><!-- Bouton Supprimer -->
			<col align="center"></col><!-- Vide -->
			<tr>
				<th style="border:0px"></th>
				<th style="border:0px">Rappel #</th>
				<th style="border:0px" colspan="2">Description</th>
				<th style="border:0px"></th>
				<th style="border:0px">Générer</th>
				<th style="border:0px"></th>
				<th style="border:0px">Annulation</th>
			</tr>
			<%
			String lastRappelId="";
			int currentRappelId=0;
			int currentRappelIdView = 0;
			Boolean hasOnlyOneDoc = false;
			String rowStyle="amalRow";
			HashMap<String, ArrayList<AMControleurRappelDetail>> rappelOuverts = viewBean.getRappelOuverts();
			//Set<String> allKeys = rappelOuverts.keySet();
			ArrayList<String> allKeys = new ArrayList<String>(rappelOuverts.keySet());
			Collections.sort(allKeys);
			// Fill the table HTML
			for(int iKey = 0; iKey<allKeys.size();iKey++){
				
				String currentKey = (String) allKeys.toArray()[iKey];

				/*
				SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
				Date dateRappel = sdf.parse(currentKey);
				// Get the represented date in milliseconds
				long milis1 = dateRappel.getTime();
				long milis2 = JadeDateUtil.getCurrentTime();
				// Calculate difference in days
				long diffDays = (milis2 - milis1) / (24 * 60 * 60 * 1000);
				String gapWithToday = ""+diffDays;
				
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
				String csDateAsInteger = sdf2.format(dateRappel);
				currentRappelId=Integer.parseInt(csDateAsInteger);
				currentRappelIdView++;
				*/
				currentRappelId=iKey;
				currentRappelIdView++;
				ArrayList<AMControleurRappelDetail> values = rappelOuverts.get(currentKey);

				for(int iResult = 0; iResult<values.size();iResult++){
					AMControleurRappelDetail currentDetail = values.get(iResult);

					// Première ligne pour la ligne titre RAPPEL
					if(!Integer.toString(currentRappelId).equals(lastRappelId)){
						String nbDocument ="";
						nbDocument=""+values.size();
						if(nbDocument.equals("1")){
							hasOnlyOneDoc = true;
						}
						nbDocument += " document(s)";
						// Switch le style de la ligne (par rappel #)
						if(rowStyle.equals("amalRow")){
							rowStyle="amalRowOdd";
						}else{
							rowStyle="amalRow";
						}
						
						// Toute première ligne de séparation à ne pas afficher
						if(!lastRappelId.equals("")){
			%>
						<tr id="lignePrincipale_<%=currentRappelId%>_empty" style="background-color:#B3C4DB" height="2px"><td colspan="8"></td></tr>
			<%
						}
			%>
						<tr id="lignePrincipale_<%=currentRappelId%>_" class="<%=rowStyle%>" style="font-weight:bold;height:42px">

							<td>
								<img id="imgExpand_<%=currentRappelId%>"
									class="imgExpand" 
									src="<%=request.getContextPath()%>/images/icon-expand.gif" 
									title="Détails" 
									border="0"
									onMouseOver="this.style.cursor='hand';"
									onMouseOut="this.style.cursor='pointer';"
									width="12px"
									height="12px"
									>
							</td>

							<td>Rappels #<%=currentRappelIdView%></td>
							<td id="jobDescription_<%=currentRappelId%>_<%=currentKey%>" colspan="2">
								<span style="margin-left:50px;"><%=nbDocument%></span><br>
								<input id="checkboxPrincipale_<%=currentRappelId%>_" type="checkbox" checked="yes"/>
								<img src="<%=request.getContextPath()%>/images/amal/iconExcel.png" 
									title="Exporter la liste" 
									border="0"
									onMouseOver="this.style.cursor='hand';"
									onMouseOut="this.style.cursor='pointer';"
									width="18px"
									height="18px"
									>
								&nbsp;<%="Rappels : "+currentKey%>&nbsp;
								<br>
							</td>
							<td></td>
							<td>
							<%
							if(!currentKey.equals("Others") && hasRightNew) {
							%>
								<button id="buttonImprimerPrincipale_<%=currentRappelId%>_" type="button" style="width:110px;height:24px">
									<img src="<%=request.getContextPath()%>/images/amal/printer1.png" 
										title="Générer" 
										border="0"
										width="14px"
										height="14px"
										>
									<b>Générer</b>
								</button>
							<%								
							}
							%>
							</td>
							<td></td>
							<td>
								<%if(hasRightNew) { %>
									<button id="buttonSupprimerPrincipale_<%=currentRappelId%>_" type="button" style="width:110px;height:24px">
									<img src="<%=request.getContextPath()%>/images/amal/edit_remove.png" 
										title="Supprimer" 
										border="0"
										width="12px"
										height="12px"
										>
									<b>Supprimer</b>
									</button>
								<%} %>
							</td>
						</tr>

			<% 		// fin de if première ligne de job (création de la ligne de titre de job actif)
					}
					lastRappelId = Integer.toString(currentRappelId);
					
			%>			
					<tr id="ligneEnfant_<%=currentRappelId%>_<%=currentDetail.getIdJournalisationLibra()%>_empty" style="background-color:#B3C4DB"><td colspan="8"></td></tr>
					<tr id="ligneEnfant_<%=currentRappelId%>_<%=currentDetail.getIdJournalisationLibra()%>_" class="<%=rowStyle%>">
						<td style="background-color:#B3C4DB"></td>
						<td><%=currentDetail.getIdJournalisationLibra()%></td>
						<td>
							<input id="checkboxEnfant_<%=currentRappelId%>_<%=currentDetail.getIdJournalisationLibra()%>_" type="checkbox" checked="yes"/>
							<%=(currentDetail.getSimpleFamille()!=null?currentDetail.getSimpleFamille().getNomPrenom():"Subside introuvable")%>
						</td>
						<td><%=currentDetail.getLibelleFormuleRappel()%></td>
						<td></td>
						<td>
						<%if(hasRightNew) { %>
							<button id="buttonImprimerEnfant_<%=currentRappelId%>_<%=currentDetail.getIdJournalisationLibra()%>_" type="button" style="font-size: 10px;">
							<img src="<%=request.getContextPath()%>/images/amal/printer1.png" 
								title="Générer" 
								border="0"
								width="10px"
								height="10px"
								>
							Générer
							</button>
						<%} %>
						</td>
						<td></td>
						<td>
						<%if(hasRightNew) { %>
							<button id="buttonSupprimerEnfant_<%=currentRappelId%>_<%=currentDetail.getIdJournalisationLibra()%>_" type="button" style="font-size: 10px;">
								<img src="<%=request.getContextPath()%>/images/amal/edit_remove.png" 
									title="Supprimer" 
									border="0"
									width="10px"
									height="10px"
									>
								Supprimer
							</button>
						<%} %>
						</td>
					</tr>

			<% 
				// fin for job
				}
			// fin for job category		
			}
			if(allKeys.size()<=0){
			%>
			<tr style="background-color:#B3C4DB" height="20px"><td colspan="12"></td></tr>
			<tr><td height="22px"></td><td colspan="3" style="background-color:#226194;color:#ffffff;font-size:12px;font-style:italic;font-weight:bold; text-align:left">&nbsp;Aucun rappel en attente de traitement</td><td colspan="8"></td></tr>
			<tr>
				<td colspan="12" align="right">
					<img id="refreshPageImg" 
						title="Rafraichir" 
						onMouseOver="this.style.cursor='hand';"
						onMouseOut="this.style.cursor='pointer';"
						src="<%=request.getContextPath()%>/images/amal/refresh-icon.png"/>
				</td>
			</tr>
			<%	
			}else{
			%>
			<tr>
				<td colspan="12" align="right" style="border-top: 2px solid #226194">
					<img id="refreshPageImg" 
						title="Rafraichir" 
						onMouseOver="this.style.cursor='hand';"
						onMouseOut="this.style.cursor='pointer';"
						src="<%=request.getContextPath()%>/images/amal/refresh-icon.png"/>
				</td>
			</tr>
			<%
			}
			%>			
		</table>


	</td>
</tr>
<%-- /tpl:put --%>

<!-- FIN DE LA ZONE PRINCIPALE BODY -->
<!-- ************************************************************************************* -->




<!-- ************************************************************************************* -->
<!-- ZONE COMMON BUTTON AND END OF PAGE -->

<%@ include file="/theme/detail/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>

<!-- FIN ZONE COMMON BUTTON AND END OF PAGE -->
<!-- ************************************************************************************* -->
