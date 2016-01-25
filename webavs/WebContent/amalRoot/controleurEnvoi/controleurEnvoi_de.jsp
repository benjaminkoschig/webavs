<!-- ************************************************************************************* -->
<!-- GLOBAL GLOBAZ IMPORT AND HEADER INCLUSION -->

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
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.lang.*"%>
<%@page import="globaz.jade.client.util.*"%>
<%@page import="globaz.jade.log.*"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.amal.vb.controleurEnvoi.AMControleurEnvoiViewBean"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoi"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiSearch"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiDetail"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiDetailSearch"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatus"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurEnvoiStatusSearch"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJob"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.SimpleControleurJobSearch"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme"%>
<%@page import="globaz.jade.persistence.model.JadeSearchSimpleModel"%>
<%@page import="globaz.jade.persistence.model.JadeSearchComplexModel"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="ch.globaz.amal.business.constantes.IAMActions"%>
<%
	//
	//Les labels de cette page commencent par le préfix "JSP_AM_RE_D"
	idEcran = "AM00xx";

	// Get the viewBean
	AMControleurEnvoiViewBean viewBean = (AMControleurEnvoiViewBean) session.getAttribute("viewBean");
	String toDisplay = request.getParameter("statusEnvoi");
	String jobType = request.getParameter("jobType");
	String historiqueTravaux = "";
	if(toDisplay!= null && toDisplay.length()>0){
		if(toDisplay.equals(IAMCodeSysteme.AMDocumentStatus.SENT.getValue())){
			if(jobType!=null && jobType.length()>0){
				if(jobType.equals(IAMCodeSysteme.AMJobType.JOBMANUALEDITED.getValue())){
					viewBean.retrieveSentWord();
					historiqueTravaux = " des travaux journaliers édités (MS Word)";
				}else if(jobType.equals(IAMCodeSysteme.AMJobType.JOBMANUALQUEUED.getValue())){
					viewBean.retrieveSentQueue();
					historiqueTravaux = " des travaux journaliers non-édités (queue)";
				}else if(jobType.equals(IAMCodeSysteme.AMJobType.JOBPROCESS.getValue())){
					viewBean.retrieveSentProcess();
					historiqueTravaux = " des travaux de type process";
				}else{
					viewBean.retrieveSent();
				}
			}else{
				viewBean.retrieveSent();
			}
		}else{
			viewBean.retrieveNotSent();
		}
	}else{
		viewBean.retrieveNotSent();
	}

	// Disable button
	bButtonNew=false;
	bButtonUpdate=false;
	bButtonDelete=false;
	
	// Action par défaut, base
	String actionAfficher = IAMActions.ACTION_CONTROLEURENVOI+".afficher";
	String actionStatus = IAMActions.ACTION_CONTROLEURENVOI+".changeJobStatus";
	String actionImprimer = IAMActions.ACTION_CONTROLEURENVOI+".launchPrintProcess";
	String actionSupprimer = IAMActions.ACTION_CONTROLEURENVOI+".supprimer";
	String expandJob = request.getParameter("expandedJob");
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>


<!-- FIN INITIALIZATION AND SPECIFIC LAMAL CONTROLEUR ENVOI INCLUSION -->
<!-- ************************************************************************************* -->







<!-- ************************************************************************************* -->
<!-- JAVASCRIPT AND CSS PART -->
<%@ include file="/theme/detail/javascripts.jspf"%>

<link rel="stylesheet" type="text/css"
	href="<%=servletContext%><%=(mainServletPath + "Root")%>/css/amal.css"
	rel="stylesheet" />

<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">

var MAIN_URL="<%=formAction%>";
var expandedJob="<%=expandJob%>";
var oldClassNameLine = "";
// Déclaration nécessaire à l'utilisation javascript dans controleurenvoi.js
var statusDocumentSent = '<%=IAMCodeSysteme.AMDocumentStatus.SENT.getValue()%>';
var iconCollapse = '<%=request.getContextPath()%>/images/icon-collapse.gif';
var iconExpand = '<%=request.getContextPath()%>/images/icon-expand.gif';
var actionStatus = '<%=actionStatus%>';
var actionImprimer = '<%=actionImprimer%>';
var actionSupprimer = '<%=actionSupprimer%>';

// Déclaration des fonctions standards cancel, validate, init, postInit
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
		var idComboBoxPrincipale="comboboxPrincipale_";
		// check the name to find the correct button
		// si nous avons un bouton supprimer, nous avons des combobox à montrer :)
		// les combo box des process envoyés ne seront donc pas réactivées.
		if(idButton.length>=idButtonImprimerPrincipale.length){
			if(idButton.substring(0,idButtonImprimerPrincipale.length)==idButtonImprimerPrincipale){
				setDisabledElement(idButton, false);
			}else if(idButton.substring(0,idButtonSupprimerPrincipale.length)==idButtonSupprimerPrincipale){
				setDisabledElement(idButton, false);
				idComboBoxPrincipale+=idButton.split("_")[1]+"_";
				setDisabledElement(idComboBoxPrincipale, false);
			}
		}
	});
	
	// Activer les inputs de saisie mois et années
	// Recherche historique
	// ---------------------------------------------------
	setDisabledElement("inputMonth",false);
	setDisabledElement("inputYear",false);
	
	// Trigger the event click on imgExpand if expandJob defini
	// Reçu comme paramètre
	var expandJob = expandedJob;
	if(expandJob>0){
		expandJob="imgExpand_"+expandJob;
		$('#'+expandJob).trigger('click');
	}
	
	<%
	if(toDisplay!= null && toDisplay.length()>0){
		
	}else{
	%>
	$('#divExplication').hide();
	<%
	}
	%>

}

function activateErrorImg(idJob) {
	$("#jobError_"+idJob).show();
}
</script>

<!-- Script spécific controleur envoi -->
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/controleurenvoi/controleurenvoi.js"/></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/controleurenvoi/controleurenvoistatus.js"/></script>
<%-- /tpl:put --%>

<!-- Inclusion de la page permettant d'afficher une box de progress -->
<%@ include file="/amalRoot/progressDialog/progressDialog.jspf" %>

<!-- FIN JAVASCRIPT AND CSS PART -->
<!-- ************************************************************************************* -->







<!-- ************************************************************************************* -->
<!-- ZONE PRINCIPALE TITLE AND BODY -->
<%@ include file="/theme/detail/bodyStart.jspf"%>

<%-- tpl:insert attribute="zoneTitle" --%>
<%
if(toDisplay!=null && toDisplay.equals(IAMCodeSysteme.AMDocumentStatus.SENT.getValue())){
%>
Annonces et envois, travaux terminés
<%	
}else{
%>
Annonces et envois, travaux en cours
<%	 	
}
%>
<%-- /tpl:insert --%>

<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>
	
<!-- 				
				<td>
					<img src="<%=request.getContextPath()%>/images/amal/loading.gif" 
						title="Impression en cours" 
						border="0"
						>
				</td>
				<td><a id="linkSupprimer7" href="http://www.dailymotion.com/Miss-Amal#videoId=x70faw
">Supprimer</a></td>
 -->

 	<INPUT type="hidden" name="selectedJob" value="">
 	<INPUT type="hidden" name="selectedStatus" value="">
 	<INPUT type="hidden" name="newStatus" value="">
 	<INPUT type="hidden" name="expandedJob" value=""> 	
 	<tr>
 		<td>
			<%
			if(toDisplay!= null && toDisplay.length()>0){
				
			}else{
			%>
	 			<div id="hideShowHelp" style="float:left">
					<img id="helpPicture"
						src="<%=request.getContextPath()%>/images/amal/Help.png" 
						title="Aide" 
						border="0"
						onMouseOver="this.style.cursor='hand';"
						onMouseOut="this.style.cursor='pointer';"
						>
	 			</div>
			<%
			}
			%>

 			<div id="divExplication" style="background-image:url('<%=request.getContextPath()%>/images/summary_bg.png');
 						background-repeat:repeat-x; float:left">
				<table id="tableauExplication"
						width="100%" 
						align="center" 
						style="border-collapse:collapse; font-size: 11px; border:2px solid #226194;">
						<tr>
							<td>
							<%
							if(toDisplay!= null && toDisplay.length()>0){
							%>
			                   	<h4>Gestion des travaux LAMAL - Historique<%=historiqueTravaux%></h4><br>
			                   	Cet écran vous permet d'avoir un aperçu des travaux qui ont été traités par l'application Web@Amal et d'effectuer :  
			  					<ul>
				  					<li>Exportation : exportation de listes au format MS Excel
				  					<li>Tri : affichage selon l'année et le mois de traitement
			  					</ul>
			  					<table>
			  						<tr>
			  							<td align="left">Année</td>
			  							<td align="left">
			  								&nbsp;&nbsp;
											<input id="inputYear" type="text" style="width:120px"/>
											<img src="<%=request.getContextPath()%>/images/amal/clear_left.png" 
												title="Effacer le champ de recherche Année" 
												border="0"
												onMouseOver="this.style.cursor='hand';"
												onMouseOut="this.style.cursor='pointer';"
												id="imgClearYear"
												>
			  							</td>
			  						</tr>
			  						<tr>
			  							<td align="left">Mois</td>
			  							<td align="left">
			  								&nbsp;&nbsp;
											<input id="inputMonth" type="text" style="width:120px"/>
											<img src="<%=request.getContextPath()%>/images/amal/clear_left.png" 
												title="Effacer le champ de recherche Mois" 
												border="0"
												onMouseOver="this.style.cursor='hand';"
												onMouseOut="this.style.cursor='pointer';"
												id="imgClearMonth"
												>
			  							</td>
			  						</tr>
			  					</table>
								<br>&nbsp;
							<%
							}else{
							%>
			                   	<h4>Gestion des travaux en cours (annonces et envois)</h4><br>
			                   	Cet écran vous permet de contrôler et d'imprimer les différents types de documents LAMAL : 
			  					<ul>
				  					<li>Process : documents générés lors d'une procédure particulière, de masse. Ex : importation d'un fichier fiscal périodique
				  					<li>Queue : documents issus du travail quotidien de traitement de dossier, sans édition
				  					<li>Edité : documents issus du travail quotidien de traitement de dossier, avec une édition dans MS Word.
			  					</ul>	
								Pour les différents type de travaux, les actions possibles sont les suivantes :
								<ul>
									<li>Action 'Imprimer'/'Supprimer' : impression ou suppression d'un job ou d'un document particulier.
									<li>Status 'Généré'/'Imprimé'/'Envoyé': contrôle du status du job/document définissant l'état de ce dernier
								</ul>
								<b>Note</b> : les status 'Généré...' et 'Imprimé' sont présents à des fins d'aide au travail quotidien.
								 Le changement de status à 'Envoyé' provoque quant à lui l'inscription du document au dossier, la journalisation et la mise en GED.
								 <br>&nbsp;
							<%
							}
							%>
							</td>
						</tr>
				</table>
 			</div>
 		</td>
 	</tr>
 	
 	<tr><td>&nbsp;<td></tr>
	<tr>
		<td>
		<table id="tableauPrincipal" width="100%" align="center" style="border-collapse:collapse; font-size: 11px; border:2px solid #226194;">
			<col align="center"></col>
			<col align="left" width="54px"></col>
			<col align="left"></col>
			<col align="left"></col>
			<col align="center"></col>
			<col align="center"></col>
			<col align="center"></col>
			<col align="center"></col>
			<col align="center"></col>
			<col align="center"></col>
			<col align="center"></col>
			<col align="center"></col>
			<tr>
				<th style="border:0px"></th>
				<th style="border:0px">Job #</th>
				<th style="border:0px" colspan="2">Description</th>
				<th style="border:0px"></th>
				<th style="border:0px">Status</th>
				<th style="border:0px"></th>
				<th style="border:0px"></th>
				<th style="border:0px">Action</th>
				<th style="border:0px"></th>
				<th style="border:0px"></th>
				<th style="border:0px">Annulation</th>
			</tr>
			<%
			String lastJobId = "";
			String rowStyle="amalRow";
			String docCounter="0";

			// Get the informations by job type
			int iSizeJobProcessGenerated = viewBean.getJobProcessGenerated().length;
			int iSizeJobAutoGenerated = viewBean.getJobAutoGenerated().length;
			int iSizeJobManualGenerated = viewBean.getJobManualGenerated().length;
			int iSizeTotal = iSizeJobProcessGenerated + iSizeJobAutoGenerated + iSizeJobManualGenerated;
	
			JadeSearchComplexModel[] currentJob = new JadeSearchComplexModel[iSizeTotal];
			
			// Fill the unique array
			for (int iIndex = 0;iIndex<iSizeJobProcessGenerated;iIndex++){
				currentJob[iIndex]= viewBean.getJobProcessGenerated()[iIndex];
			}
			for (int iIndex = 0;iIndex<iSizeJobAutoGenerated;iIndex++){
				currentJob[iIndex+iSizeJobProcessGenerated]= viewBean.getJobAutoGenerated()[iIndex];
			}
			for (int iIndex = 0;iIndex<iSizeJobManualGenerated;iIndex++){
				currentJob[iIndex+iSizeJobProcessGenerated+iSizeJobAutoGenerated]= viewBean.getJobManualGenerated()[iIndex];
			}
			
			// Fill the table HTML
			for(int iJobCategory = 0; iJobCategory<currentJob.length;iJobCategory++){

				// to get the status of the job
				String csCurrentSelected=null;
				
				for(int iResult = 0; iResult<currentJob[iJobCategory].getSize();iResult++){

					ComplexControleurEnvoiDetail controleurEnvoiDetail = null;
					ComplexControleurEnvoi controleurEnvoi = null;
					
					if(iJobCategory<iSizeJobProcessGenerated){
						controleurEnvoi = (ComplexControleurEnvoi) currentJob[iJobCategory].getSearchResults()[iResult];
					}else{
						controleurEnvoiDetail = (ComplexControleurEnvoiDetail) currentJob[iJobCategory].getSearchResults()[iResult];
					}

					boolean isProcess = true;
					if(controleurEnvoiDetail!=null){
						isProcess = false;
					}
					boolean hasOnlyOneDoc = false;
					
					String fullDescription = "";
					String currentIdJob = "";
					String currentDateJob = "";
					if(controleurEnvoiDetail != null){
						currentDateJob = controleurEnvoiDetail.getDateJob();
						fullDescription = currentDateJob;
						fullDescription +=" - "+controleurEnvoiDetail.getDescriptionJob();
						currentIdJob = controleurEnvoiDetail.getIdJob();
					}else{
						currentDateJob = controleurEnvoi.getDateJob();
						fullDescription = currentDateJob;
						fullDescription +=" - "+controleurEnvoi.getDescriptionJob();
						currentIdJob = controleurEnvoi.getIdJob();
					}
					// Get the status of the job
					if(csCurrentSelected == null){
						csCurrentSelected=viewBean.getStatusJob(currentIdJob);
					}
					// If error, put the status to generated in the UI
					if(csCurrentSelected.equals(IAMCodeSysteme.AMDocumentStatus.ERROR.getValue())){
						if(controleurEnvoiDetail !=null){
							if(controleurEnvoiDetail.getTypeJob().equals(IAMCodeSysteme.AMJobType.JOBMANUALEDITED.getValue())){
								csCurrentSelected = IAMCodeSysteme.AMDocumentStatus.MANUALGENERATED.getValue();
							}else{
								csCurrentSelected = IAMCodeSysteme.AMDocumentStatus.AUTOGENERATED.getValue();
							}
						}else{
							if(controleurEnvoi.getTypeJob().equals(IAMCodeSysteme.AMJobType.JOBMANUALEDITED.getValue())){
								csCurrentSelected = IAMCodeSysteme.AMDocumentStatus.MANUALGENERATED.getValue();
							}else{
								csCurrentSelected = IAMCodeSysteme.AMDocumentStatus.AUTOGENERATED.getValue();
							}
						}
					}

					// Première ligne pour la ligne titre JOB
					if(!lastJobId.equals(currentIdJob)){
						String nbDocument ="";
						nbDocument=viewBean.getJobCount(currentIdJob);

						if(nbDocument.equals("1")){
							hasOnlyOneDoc = true;
						}
						nbDocument += " document(s)";
						
						// Switch le style de la ligne (par job #)
						if(rowStyle.equals("amalRow")){
							rowStyle="amalRowOdd";
						}else{
							rowStyle="amalRow";
						}
						
						// Toute première ligne de séparation à ne pas afficher
						if(!lastJobId.equals("")){
			%>
						<tr id="lignePrincipale_<%=currentIdJob%>_empty" style="background-color:#B3C4DB" height="2px"><td colspan="12"></td></tr>
			<%
						}
			%>
			
			<%
						// Lignes de titres de catégories de jobs
						String labelGroupe = "&nbsp;";
						if(controleurEnvoiDetail !=null){
							if(controleurEnvoiDetail.getTypeJob().equals(IAMCodeSysteme.AMJobType.JOBMANUALEDITED.getValue())){
								labelGroupe += "Travaux journaliers édités (MS Word)";
							}else{
								labelGroupe += "Travaux journaliers en file d'attente";
							}
						}else{
							labelGroupe += "Travaux de type processus";
						}
						if(iJobCategory==0 && historiqueTravaux.length()==0){
			%>
						<tr id="ligneTitrePrincipale_<%=currentIdJob%>_Title_0" style="background-color:#B3C4DB" height="20px"><td colspan="12" style="border-top: 2px solid #226194" ></td></tr>
						<tr id="ligneTitrePrincipale_<%=currentIdJob%>_Title_1"><td style="border-bottom: 2px solid #226194" height="22px"></td><td colspan="3" style="border-bottom: 2px solid #226194;background-color:#226194;color:#ffffff;font-size:12px;font-style:italic;font-weight:bold; text-align:left"><%=labelGroupe%></td><td colspan="8" style="border-bottom: 2px solid #226194" ></td></tr>
			<%			
						}else if(iJobCategory == iSizeJobProcessGenerated  && historiqueTravaux.length()==0){
			%>
						<tr id="ligneTitrePrincipale_<%=currentIdJob%>_Title_0" style="background-color:#B3C4DB" height="20px"><td colspan="12" style="border-top: 2px solid #226194" ></td></tr>
						<tr id="ligneTitrePrincipale_<%=currentIdJob%>_Title_1"><td style="border-bottom: 2px solid #226194" height="22px"></td><td colspan="3" style="border-bottom: 2px solid #226194;background-color:#226194;color:#ffffff;font-size:12px;font-style:italic;font-weight:bold; text-align:left"><%=labelGroupe%></td><td colspan="8" style="border-bottom: 2px solid #226194" ></td></tr>
			<%			
						}else if(iJobCategory == iSizeJobProcessGenerated + iSizeJobAutoGenerated &&  historiqueTravaux.length()==0){
			%>
						<tr id="ligneTitrePrincipale_<%=currentIdJob%>_Title_0" style="background-color:#B3C4DB" height="20px"><td colspan="12" style="border-top: 2px solid #226194" ></td></tr>
						<tr id="ligneTitrePrincipale_<%=currentIdJob%>_Title_1"><td style="border-bottom: 2px solid #226194" height="22px"></td><td colspan="3" style="border-bottom: 2px solid #226194;background-color:#226194;color:#ffffff;font-size:12px;font-style:italic;font-weight:bold; text-align:left"><%=labelGroupe%></td><td colspan="8" style="border-bottom: 2px solid #226194" ></td></tr>
			<%			
						}
			%>
						<tr id="lignePrincipale_<%=currentIdJob%>_" class="<%=rowStyle%>" style="font-weight:bold;height:42px">
			<%
						// check pour les complex model details qui affiche la totalité des enfants
						if(isProcess || csCurrentSelected.equals(IAMCodeSysteme.AMDocumentStatus.SENT.getValue())){
						// n'affiche pas l'icône expand
			%>
						<td></td>
			<%							
						}else{
			%>
							<td>
								<img id="imgExpand_<%=currentIdJob%>"
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
			<%			
						}
			%>
							<td width="100px" title="<%=nbDocument%>">Job <%=currentIdJob%>
							
							<img src="<%=request.getContextPath()%>/images/amal/status_unknown.png"
									id="jobError_<%=currentIdJob%>"
									title="<ct:FWLabel key="JSP_AM_EN_JOB_ERROR_LIBELLE"/>" 
									border="0"
									onMouseOver="this.style.cursor='help';"
									onMouseOut="this.style.cursor='pointer';"
									width="18px"
									height="18px"
									style="display:none"
									>
							</td>
							<td id="jobDescription_<%=currentIdJob%>_<%=currentDateJob%>" title="<%=nbDocument%>" colspan="2">
								<!-- 
									<span style="margin-left:50px;"><%=nbDocument%></span><br>
								 -->
								<input id="checkboxPrincipale_<%=currentIdJob%>_" type="checkbox" checked="yes"/>
								<img src="<%=request.getContextPath()%>/images/amal/iconExcel.png"
									onClick="javascript:alert('Not yet implemented')" 
									title="Exporter la liste" 
									border="0"
									onMouseOver="this.style.cursor='hand';"
									onMouseOut="this.style.cursor='pointer';"
									width="18px"
									height="18px"
									>
								&nbsp;<%=fullDescription%><br>
							</td>
							<td></td>
			<%
							// check si le job est in progress
							if(csCurrentSelected.equals(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue())){
							// Affiche la bare waiting
			%>
							<td>
								<img src="<%=request.getContextPath()%>/images/amal/loading.gif" 
									title="Traitement en cours" 
									border="0"
									width="220px"
									height="18px"
									>
							</td>
			<%							
							}else{
			%>
							<td>
								<select id="comboboxPrincipale_<%=currentIdJob%>_" style="width:220px">
								<%
									// Génération de la combobox
									HashMap<String, String> myOptions = null;
									String csValue="";
									String csLibelle="";
									String csSelected="";
									if(controleurEnvoiDetail !=null){
										myOptions = viewBean.getStatusListJob(currentIdJob,(ComplexControleurEnvoiDetailSearch) currentJob[iJobCategory]);
									}else{
										myOptions = viewBean.getStatusListJob(currentIdJob,(ComplexControleurEnvoiSearch)currentJob[iJobCategory]);
									}
									csSelected=csCurrentSelected;
									Object[] myKeys=myOptions.keySet().toArray();
									Arrays.sort(myKeys);
									for(int iIndex=0; iIndex<myKeys.length;iIndex++){
										csValue = (String) myKeys[iIndex];
										csLibelle = (String) myOptions.get(myKeys[iIndex]);
								%>
									<option value="<%=csValue%>" <%=(csSelected.equals(csValue)?"selected=\"selected\"":"")%>><%=csLibelle%></option>
								<%
									}
								%>
								</select>
							</td>
			<%
							} // FIN DU IF IN PROGRESS
			%>
							<td></td>
							<td></td>
							<%
								// Activation du bouton imprimer pour les travaux différents
								// des travaux manuels et status différent de status envoyé
								if((controleurEnvoiDetail!=null 
										&& 
										controleurEnvoiDetail.getTypeJob().equals(IAMCodeSysteme.AMJobType.JOBMANUALEDITED.getValue()))
										||
										csCurrentSelected.equals(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue())
										||
										csCurrentSelected.equals(IAMCodeSysteme.AMDocumentStatus.SENT.getValue())
										){
							%>
							<td></td>							
							<%
								}else{
							%>
							<td><button id="buttonImprimerPrincipale_<%=currentIdJob%>_" type="button" style="width:110px;height:24px">
								<img src="<%=request.getContextPath()%>/images/amal/printer1.png" 
									title="Imprimer" 
									border="0"
									width="14px"
									height="14px"
									>
								<b>Imprimer</b>
								</button>
							</td>
							<%	
								}
							%>
							<td></td>
							<td></td>
							<%
								// Activation du bouton supprimer pour les travaux
								// qui n'ont pas encore été envoyé ou en cours
								if(csCurrentSelected.equals(IAMCodeSysteme.AMDocumentStatus.SENT.getValue())
										||
									csCurrentSelected.equals(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue())){
							%>
							<td></td>
							<%
							}else{
							%>
							<td><button id="buttonSupprimerPrincipale_<%=currentIdJob%>_" type="button" style="width:110px;height:24px">
								<img src="<%=request.getContextPath()%>/images/amal/edit_remove.png" 
									title="Supprimer" 
									border="0"
									width="12px"
									height="12px"
									>
								<b>Supprimer</b>
								</button>
							</td>
							<%
							}
							%>
						</tr>

			<% 		// fin de if première ligne de job (création de la ligne de titre de job actif)
					}
					lastJobId = currentIdJob;
					
					// création ensuite des lignes de détails, si le job n'est pas un process
					// et status principal n'est pas à "envoyé"
					if(!isProcess && !csCurrentSelected.equals(IAMCodeSysteme.AMDocumentStatus.SENT.getValue())){
						String currentIdStatus = controleurEnvoiDetail.getIdStatus();
						String currentJobError = controleurEnvoiDetail.getJobError();
			%>			
					<tr id="ligneEnfant_<%=currentIdJob%>_<%=currentIdStatus%>_empty" style="background-color:#B3C4DB"><td colspan="12"></td></tr>
					<%
						// Ligne en rouge si erreur
						if(!controleurEnvoiDetail.getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.ERROR.getValue())){
					%>
					<tr id="ligneEnfant_<%=currentIdJob%>_<%=currentIdStatus%>_" class="<%=rowStyle%>">
					<%}else{ %>
					<tr id="ligneEnfant_<%=currentIdJob%>_<%=currentIdStatus%>_" class="<%=rowStyle%>" style="background-color:#FF647B"">
					<%}%>

						<td style="background-color:#B3C4DB"></td>						
						<%
						// Ligne en rouge si erreur
						if(controleurEnvoiDetail.getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.ERROR.getValue())){
						%>
							<script>activateErrorImg('<%=currentIdJob%>');</script>
							<td width="80px"><a href="#" class="infobulle" style="color:#000000"><%=currentIdStatus%><span class="custom critical"><img src="<%=request.getContextPath()%>/images/amal/Critical.png" alt="Error" height="48" width="48" /><em>Erreur</em><%=currentJobError%></span></a>							
						<% } else {
						%>
							<td width="80px"><%=currentIdStatus%>
						<%
						}
						%>
						</td>
						<%
						String nomPrenomFull = controleurEnvoiDetail.getNomPrenom();
						String nomPrenomShort = nomPrenomFull;
						if(nomPrenomShort.length()>40){
							nomPrenomShort = nomPrenomShort.substring(0,39);
							nomPrenomShort+="...";
						}
						String actionAfficherDetailFamille = "amal?userAction="+IAMActions.ACTION_DETAILFAMILLE+".afficher";
						actionAfficherDetailFamille += "&selectedId="+controleurEnvoiDetail.getIdDetailFamille();
						%>
						<td title="<%=nomPrenomFull%>">
							<input id="checkboxEnfant_<%=currentIdJob%>_<%=currentIdStatus%>_" type="checkbox" checked="yes"/>
							<a href='<%=actionAfficherDetailFamille%>'>
								<%=nomPrenomShort%>
							</a>
						</td>
						<td title="<%=objSession.getCodeLibelle(controleurEnvoiDetail.getNumModele())%>">
							<%
								// Activation du lien pour lancer word pour 
								// des travaux manuels
								if(controleurEnvoiDetail.getTypeJob().equals(IAMCodeSysteme.AMJobType.JOBMANUALEDITED.getValue())){
							%>
							<a href='javascript:generateWordLauncher("<%=currentIdStatus%>")'>
								<%=viewBean.getLibelleCodeSysteme(controleurEnvoiDetail.getNumModele())%>
							</a>
							<%}else{%>
								<%=viewBean.getLibelleCodeSysteme(controleurEnvoiDetail.getNumModele())%>
							<%} %>
						</td>
						<td></td>
						<%
							// Activation de la bar in progress
							if(controleurEnvoiDetail.getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue())){
						%>
						<td>
							<img src="<%=request.getContextPath()%>/images/amal/loading.gif" 
								title="Traitement en cours" 
								border="0"
								width="180px"
								height="14px"
								>
						</td>
						<%
							}else{
						%>
						<td>
							<select id="comboboxEnfant_<%=currentIdJob%>_<%=currentIdStatus%>_" style="font-size: 10px;width:180px">
								<%
									// Génération de la combobox
									HashMap<String, String> myOptions = viewBean.getStatusListDocument(controleurEnvoiDetail);
									Object[] myKeys=myOptions.keySet().toArray();
									Arrays.sort(myKeys);
									String csValue="";
									String csLibelle="";
									String csSelected=controleurEnvoiDetail.getStatusEnvoi();
									// If error, put the status to generated in the UI
									if(csSelected.equals(IAMCodeSysteme.AMDocumentStatus.ERROR.getValue())){
										if(controleurEnvoiDetail.getTypeJob().equals(IAMCodeSysteme.AMJobType.JOBMANUALEDITED.getValue())){
											csSelected = IAMCodeSysteme.AMDocumentStatus.MANUALGENERATED.getValue();
										}else{
											csSelected = IAMCodeSysteme.AMDocumentStatus.AUTOGENERATED.getValue();
										}
									}
									for(int iIndex=0; iIndex<myKeys.length;iIndex++){
										csValue = (String) myKeys[iIndex];
										csLibelle = (String) myOptions.get(myKeys[iIndex]);
								%>
									<option value="<%=csValue%>" <%=(csSelected.equals(csValue)?"selected=\"selected\"":"")%>><%=csLibelle%></option>
								<%
									}
								%>
							</select>
						</td>
						<%
							}
						%>
						
						<td></td>
						<td></td>
						<%
							// Activation du bouton imprimer pour les travaux différents
							// des travaux manuels
							if(controleurEnvoiDetail.getTypeJob().equals(IAMCodeSysteme.AMJobType.JOBMANUALEDITED.getValue())
									||
									controleurEnvoiDetail.getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue())){
						%>
						<td></td>							
						<%
							}else{
						%>
						<td><button id="buttonImprimerEnfant_<%=currentIdJob%>_<%=currentIdStatus%>_" type="button" style="font-size: 10px;">
							<img src="<%=request.getContextPath()%>/images/amal/printer1.png" 
								title="Imprimer" 
								border="0"
								width="10px"
								height="10px"
								>
							Imprimer
							</button>
						</td>
						<%
							}
						%>
						<td></td>
						<td></td>
						<%
							// Activation du bouton supprimer pour les jobs pas en cours
							if(controleurEnvoiDetail.getStatusEnvoi().equals(IAMCodeSysteme.AMDocumentStatus.INPROGRESS.getValue())){
						%>
						<td></td>							
						<%
							}else{
						%>
						<td><button id="buttonSupprimerEnfant_<%=currentIdJob%>_<%=currentIdStatus%>_" type="button" style="font-size: 10px;">
							<img src="<%=request.getContextPath()%>/images/amal/edit_remove.png" 
								title="Supprimer" 
								border="0"
								width="10px"
								height="10px"
								>
							Supprimer
							</button>
						</td>
						<%
							}
						%>
						
					</tr>
			<% 
					// fin du if type de job == process			
					}
				// fin for job
				}
			// fin for job category		
			}
			if(iSizeTotal<=0){
			%>
			<tr style="background-color:#B3C4DB" height="20px"><td colspan="12"></td></tr>
			 
			<tr><td height="22px"></td><td colspan="3" style="background-color:#226194;color:#ffffff;font-size:12px;font-style:italic;font-weight:bold; text-align:left">&nbsp;Aucun travail à afficher</td><td colspan="8"></td></tr>
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
