<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="globaz.globall.db.BSpy"%>
<%@page import="java.util.TreeMap"%>
<%@page import="ch.globaz.amal.business.services.AmalServiceLocator"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiDetail"%>
<%@page import="ch.globaz.amal.business.models.controleurEnvoi.ComplexControleurEnvoiDetailSearch"%>
<%
long currentTime = System.currentTimeMillis();
JadeLogger.info(this, "Time JSP start : " + currentTime);
%>


<!-- ************************************************************************************* -->
<!-- GLOBAL GLOBAZ IMPORT AND HEADER INCLUSION -->

<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ include file="/theme/detail/header.jspf" %>

<!-- FIN GLOBAZ IMPORT AND HEADER INCLUSION -->
<!-- ************************************************************************************* -->


<!-- ************************************************************************************* -->
<!-- INITIALIZATION AND SPECIFIC LAMAL CONTRIBUABLE INCLUSION -->

<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.amal.utils.AMContribuableHistoriqueHelper"%>
<%@page import="ch.globaz.amal.business.models.contribuable.SimpleContribuableInfos"%>
<%@page import="globaz.fweb.taglib.FWSystemCodeTag"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="globaz.amal.vb.contribuable.AMContribuableViewBean"%>
<%@page import="globaz.amal.vb.detailfamille.AMDetailfamilleViewBean"%>
<%@page import="ch.globaz.amal.business.models.contribuable.Contribuable"%>
<%@page import="ch.globaz.amal.business.models.famille.SimpleFamille"%>
<%@page import="ch.globaz.amal.business.models.famille.FamilleContribuable"%>
<%@page import="ch.globaz.amal.business.models.famille.FamilleContribuableView"%>
<%@page import="ch.globaz.amal.business.models.revenu.Revenu"%>
<%@page import="ch.globaz.amal.business.models.revenu.RevenuSearch"%>
<%@page import="ch.globaz.amal.business.models.revenu.RevenuHistorique"%>
<%@page import="ch.globaz.amal.business.models.revenu.RevenuHistoriqueSearch"%>
<%@page import="ch.globaz.amal.business.constantes.IAMActions"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme"%>
<%@page import="globaz.jade.log.*"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.framework.util.FWCurrency"%>

<%
	//Les labels de cette page commencent par le préfix "JSP_AM_DOSSIER_D"
	idEcran="AM0002";

	AMContribuableViewBean viewBean = (AMContribuableViewBean) session.getAttribute("viewBean");
	boolean viewBeanIsNew = "add".equals(request.getParameter("_method"));
	boolean viewBeanOnError = "fail".equals(request.getParameter("_valid"));
	
	String prefixContribuableModel = "contribuable";
	
	//Boolean contribReprise = false;
	boolean contribReprise = "transfert".equals(request.getParameter("mode")) || "1".equals(request.getParameter("fromHisto"));
	boolean contribuableDisabled = false;
	if (!(viewBean.getContribuable().getContribuable().getIsContribuableActif()==null) && !viewBean.getContribuable().getContribuable().getIsContribuableActif()) {
		contribuableDisabled = true;
	}
	
	String linkRetourContribuable = "amal?userAction=amal.famille.famille.afficher&selectedId=";
	
	if (viewBean.getContribuable().isNew() && !viewBeanIsNew &!viewBeanOnError) {
		AMContribuableHistoriqueHelper.loadDataFromHistorique(viewBean.getContribuable().getId());		
		linkRetourContribuable = "";
	} else {
		linkRetourContribuable += viewBean.getContribuable().getFamille().getIdFamille();
	}
		
	String linkRetourContribuableLibelle = "Détails";
	String linkRetourDetail = "";
	
	String detailLinkFamille = "amal?userAction=amal.famille.famille.afficher&selectedId=";
	String detailLinkFamilleNouveau = "amal?userAction=amal.famille.famille.nouveau&contribuableId=";
	detailLinkFamilleNouveau += viewBean.getContribuable().getContribuable().getIdContribuable();
	detailLinkFamilleNouveau +="&_method=add";
	String detailLinkDetailFamille = "amal?userAction=amal.detailfamille.detailfamille.afficher&selectedId=";
	
	String detailLinkRevenu = "amal?userAction=amal.revenu.revenu.afficher&selectedId=";
	String detailLinkRevenuNouveau = "amal?userAction=amal.revenu.revenu.nouveau&contribuableId=";
	detailLinkRevenuNouveau += viewBean.getContribuable().getContribuable().getIdContribuable();
	detailLinkRevenuNouveau+="&_method=add";

	String detailLinkRevenuHistorique = "amal?userAction=amal.revenuHistorique.revenuHistorique.afficher&selectedId=";
	String detailLinkRevenuHistoriqueNouveau = "amal?userAction=amal.revenuHistorique.revenuHistorique.nouveau&contribuableId=";
	detailLinkRevenuHistoriqueNouveau += viewBean.getContribuable().getContribuable().getIdContribuable();
	detailLinkRevenuHistoriqueNouveau+="&_method=add";

	String detailLinkRevenuSupprimer = "amal?userAction=amal.revenu.revenu.supprimer&selectedId=";
	String detailLinkRevenuHistoriqueSupprimer = "amal?userAction=amal.revenuhistorique.revenuhistorique.supprimer&selectedId=";
	String linkMembreSupprimer = "amal?userAction=amal.famille.famille.supprimer&selectedId=";

	// Get the selected Tab Id (open the correct tab when validate, cancel, etc from a detail form)
	String selectedTabId = request.getParameter("selectedTabId");
	if(null == selectedTabId || selectedTabId.length()<=0){
		selectedTabId = "0";
	}
	autoShowErrorPopup = true;
	
	bButtonDelete = false;
	bButtonNew = false;
	bButtonUpdate = false;
	
	boolean hasRightTransferer = objSession.hasRight("amal.contribuable.contribuable.transferer", FWSecureConstants.UPDATE); 
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>

<!-- FIN INITIALIZATION AND SPECIFIC LAMAL CONTRIBUABLE INCLUSION -->
<!-- ************************************************************************************* -->




<!-- ************************************************************************************* -->
<!-- JAVASCRIPT AND CSS PART -->

<%@ include file="/theme/detail/javascripts.jspf" %>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/amal.css" rel="stylesheet"/>

<%-- tpl:put name="zoneScripts" --%>
<ct:menuChange displayId="menu" menuId="amal-menuprincipal" showTab="menu"/>
<% if (request.getParameter("fromHisto") != null) { %>
	<ct:menuChange displayId="menu" menuId="amal-menuprincipal" showTab="options"/>
	<ct:menuChange displayId="options" menuId="amal-optionsdossiersHistorique">
		<ct:menuSetAllParams key="selectedId" value="<%=AMContribuableHistoriqueHelper.getContribuableInfos().getIdContribuableInfo()%>"/>
	</ct:menuChange>
	<SCRIPT>
		reloadMenuFrame(top.fr_menu, MENU_TAB_OPTIONS);
	</SCRIPT>
<% } else { %>
	<%if (!contribuableDisabled) {%>
		<ct:menuChange displayId="options" menuId="amal-optionscontribuable">
			<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getContribuable().getId()%>"/>
		</ct:menuChange>
	<%} else { %>
		<ct:menuChange displayId="options" menuId="amal-optionsempty">
			<ct:menuSetAllParams key="selectedId" value="<%=viewBean.getContribuable().getId()%>"/>
		</ct:menuChange>
	<%} %>
	<SCRIPT>
		reloadMenuFrame(top.fr_menu, MENU_TAB_MENU);
	</SCRIPT>
<% } %>

<style>
#tabAjaxLoaderImg {
	position: relative;
	left:150px;
	top:25px;
}
</style>

<script type="text/javascript">



var ACTION_CONTRIBUABLE="<%=IAMActions.ACTION_CONTRIBUABLE%>";
var ACTION_DETAILFAMILLE="<%=IAMActions.ACTION_DETAILFAMILLE%>";

var actionMethod;
var userAction;

// AVANT DOCUMENT READY

	
// DOCUMENT READY
$(document).ready(function() {
	//On affiche les tabs une fois le document prêt pour éviter que les liens hypertext n'apparaissent
	//pendant le chargement de la page.
	$('#tabs').show();
	
	$('#tabs').tabs({ 
		selected: <%=selectedTabId%>,
		cache: true,
		spinner: '&nbsp;&nbsp;<img id="spinnerLoaderImg" src="<%=request.getContextPath()%>/images/amal/ajax-loader-3.gif" title="Chargement" border="0"/>',		
		select: function(event, ui) {
			var $panel = $(ui.panel);
			if(ui.index==3){
				<%
				String calculLastAnneeSubside = null;
				List<FamilleContribuableView> calculLastFamilleContribuableArray = null;
				String calculTypeDemande = null;

				for(int iFamille=0; iFamille<viewBean.getListeFamilleContribuableView().size();iFamille++){
					FamilleContribuableView currentItem = (FamilleContribuableView)viewBean.getListeFamilleContribuableView().get(iFamille);
					if(currentItem.getAnneeHistorique()!=null && currentItem.getAnneeHistorique().length()>3){
						calculLastAnneeSubside = currentItem.getAnneeHistorique();
						calculLastFamilleContribuableArray = viewBean.getListeFamilleContribuableViewAnnee().get(calculLastAnneeSubside);
						break;
					}
				}
				if(calculLastFamilleContribuableArray != null){
					for (Iterator itFamille = calculLastFamilleContribuableArray.iterator(); itFamille.hasNext();) {
						FamilleContribuableView familleContribuable = (FamilleContribuableView) itFamille.next();
						calculTypeDemande = familleContribuable.getTypeDemande();
					}
				}
				if(calculLastAnneeSubside==null){
					calculLastAnneeSubside = "";
				}
				if(calculTypeDemande==null){
					calculTypeDemande="";
				}
				%>
				// Année : année courante
		    	year = '<%=calculLastAnneeSubside%>';
		    	if(year == null || year == undefined || year.length<4){
		    		year = null;
		    	}
				// Type : dernier subside du contribuable
		    	type = '<%=calculTypeDemande%>';
		    	if(type == null || type == undefined || type.length<8){
		    		type = null;
		    	}
		    	if(year==null && type==null){
		    		var dt_currentDate = new Date();
		    		var i_currentYear = dt_currentDate.getFullYear();
					year = i_currentYear;
					type = '42002001';
		    	}
				initListCalculValues();
		  }
		},
		load: function (event, ui) {
			var $uiPanle = $(ui.panel)
			notationManager.addNotationOnFragment($uiPanle);
			$("#tabAjaxLoaderImg").remove();
		}		
	});
	

	actionMethod=$('[name=_method]',document.forms[0]).val();
	userAction=$('[name=userAction]',document.forms[0])[0];

	//attribue une id à tous les champs ayant un nom mais pas encore d'id
	$('*',document.forms[0]).each(function(){
		if(this.name!=null && this.id==""){
			this.id=this.name;
		}
	});
	

});


	
function clickExpand(elem) {
	// get the id
	var currentId = elem.attr("id");		
	// remove the term "expand"
	currentId = currentId.replace("simuExpand","");
	// set new id
	var currentTableId = "simuSubside"+currentId;
	$('#'+currentTableId).toggle();
	var currentButtonId = "simuSubsideBt"+currentId;
	$('#'+currentButtonId).toggle();
}
	
function init() {
}

function postInit(){
	$('#conteneurCalculs :input').removeProp('disabled');

}

function add() {
}

function del() {
}

function cancel() {
	if (actionMethod == "add") {
		userAction.value = ACTION_CONTRIBUABLE + ".rechercher";
	} else {
		userAction.value = ACTION_CONTRIBUABLE + ".rechercher";
	}
}

function validate() {
	state = true;
	if (actionMethod == "add") {
		userAction.value = ACTION_CONTRIBUABLE + ".ajouter";
	} else {
		userAction.value = ACTION_CONTRIBUABLE + ".rechercher";
	}
	return state;
}

function delRevenu(finalUrl){
	// CONCERN THE DELETE ACTION ON A REVENU ITEM
	// WE STAY ON THE SAME PAGE
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
    	location=finalUrl;
    }
}

function delMembreFamille(finalUrl){
	// CONCERN THE DELETE ACTION ON A FAMILY MEMBER
	// WE STAY ON THE SAME PAGE
	if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	   	location=finalUrl;
	}
}

</script>

<!-- Inclusion de la page permettant d'afficher une box de progress -->
<%@ include file="/amalRoot/progressDialog/progressDialog.jspf" %>
<%-- /tpl:put --%>

<!-- FIN JAVASCRIPT AND CSS PART -->
<!-- ************************************************************************************* -->

<!-- ************************************************************************************* -->
<!-- ZONE PRINCIPALE TITLE AND BODY -->

<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_AM_CON_D_TITLE"/><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<%if(!viewBeanIsNew){
		// TRY THE JSP FRAGMENTS
 		%>
			<TR>		
				<td colspan="4">							
				<% if (contribReprise) {%>
				<input type="hidden" name="contribuableHistorique" id="contribuableHistorique" value="1" />				
				 
						<div class="conteneurWarning">
							<img src="<%=request.getContextPath()%>/images/amal/status_unknown.png" />
							Vous devez
							<% if(hasRightTransferer) { %>
								<a href="amal?userAction=amal.contribuable.contribuable.transferer&
								mode=transfert&selectedId=<%=AMContribuableHistoriqueHelper.getContribuableInfos().
								getIdContribuableInfo()%>">transferer</a> 
							<% } else { %>
								transferer
							<% } %>
								ce contribuable pour pouvoir effectuer des modifications !
						</div>
						<div>&nbsp;</div>
					<% } %>	
					<% if (contribuableDisabled) {%>
						<div class="conteneurWarning">
							<img src="<%=request.getContextPath()%>/images/amal/status_unknown.png" />
							<%
								BSpy spy = new BSpy(viewBean.getContribuable().getContribuable().getSpy());
								String dateSpy = spy.getDate();
								String userSpy = spy.getUser();
								String newIdContribuable = viewBean.getContribuable().getContribuable().getIdContribuableFusion();
								String url ="amal?userAction=amal.contribuable.contribuable.afficher&selectedId="+newIdContribuable;
							%>
							<%if (!JadeStringUtil.isBlankOrZero(newIdContribuable)) {%>
								Ce contribuable a été fusionné le <%=dateSpy%> par <%=userSpy%> ! </br></br>
								<a href="<%=url%>">Accéder au nouveau dossier</a>
							<%}%> 
						</div>
						<div>&nbsp;</div>
					<% } %>	
					<table width="100%">
						<tr>
							<td align="left" style="vertical-align:top">
							<input type="hidden" id="idContribuable" value="<%=viewBean.getContribuable().getContribuable().getIdContribuable()%>" />
							<input type="hidden" id="isReprise" value="<%=contribReprise%>" />											
							<div class="conteneurContrib">	
								<%@ include file="/amalRoot/contribuable/contribuable_contribuable_div_infos.jspf" %>											
							</div>
							</td>
							<td></td>
							<%
							String lastAnneeSubside = null;
							List<FamilleContribuableView> lastFamilleContribuableArray = null;
							String lastRevenuDeterminant = null;

							for(int iFamille=0; iFamille<viewBean.getListeFamilleContribuableView().size();iFamille++){
								FamilleContribuableView currentItem = (FamilleContribuableView)viewBean.getListeFamilleContribuableView().get(iFamille);
								if(currentItem.getAnneeHistorique()!=null && currentItem.getAnneeHistorique().length()>3){
									lastAnneeSubside = currentItem.getAnneeHistorique();
									lastFamilleContribuableArray = viewBean.getListeFamilleContribuableViewAnnee().get(lastAnneeSubside);
									for (Iterator it = Arrays.asList(viewBean.getRevenusHistoriques().getSearchResults()).iterator(); it.hasNext();) {
										RevenuHistorique revenu = (RevenuHistorique) it.next();
										if(lastAnneeSubside.equals(revenu.getAnneeHistorique())){
											lastRevenuDeterminant = revenu.getRevenuDeterminantCalcul();
											break;
										}
									}
									break;
								}
							}
							%>
							<td align="right">
								<div class="conteneurQuickInfo">	
								<div class="subtitleQuickInfo">Dernier(s) subside(s) alloué(s)</div>
								<div class="reLinkQuickInfo"><%=(lastAnneeSubside==null?"":lastAnneeSubside)%></div>							
								<div class="dataQuickInfo" style="min-height:60px;height:170px;overflow:auto;clear: both;">
									<table width="100%" style="border: 1px solid #B3C4DB"  style="border-collapse:collapse" style="background-color:#FFFFFF" >
										<%
										if(lastAnneeSubside!=null && lastAnneeSubside.length()>0){
										%>
										<tr style="font-weight: bold" style="font-style:italic">
											<td colspan="10" align="left"
												style="border-bottom: 1px solid black"
												style="background-color:#eeeeee">
											Revenu déterminant <%=lastAnneeSubside%>
											</td>
										</tr>
										<tr style="background-color:#FFFFFF" style="font-weight: bold">
											<td colspan="10" align="right" data-g-amountformatter="blankAsZero:true">
											<%=lastRevenuDeterminant%>
											</td>
										</tr>
										<tr style="height:10px">
											<td colspan="10" style="background-color:#FFFFFF"></td>
										</tr>
										<tr style="background-color: #B3C4DB"><td colspan="10"></td></tr>
										<tr style="font-style:italic;font-weight: bold;background-color:#eeeeee">
											<td align="left" style="border-bottom: 1px solid black" >Nom, Prénom</td>
											<td align="center" style="border-bottom: 1px solid black" title="Type">T</td>
											<td align="center" style="border-bottom: 1px solid black" >&nbsp;R&nbsp;</td>
											<td align="center" style="border-bottom: 1px solid black" title="Status">S</td>
											<td align="right" style="border-bottom: 1px solid black" >&nbsp;Contrib&nbsp;</td>
											<td align="right" style="border-bottom: 1px solid black" ></td>
											<td align="left" style="border-bottom: 1px solid black" >Document</td>
											<td align="center" style="border-bottom: 1px solid black" >Envoi</td>
											<td align="center" style="border-bottom: 1px solid black" >Début</td>
											<td align="center" style="border-bottom: 1px solid black" >Fin</td>
										</tr>
										<%
										for (Iterator itFamille = lastFamilleContribuableArray.iterator(); itFamille.hasNext();) {
											FamilleContribuableView familleContribuable = (FamilleContribuableView) itFamille.next();
										%>
										<tr style="background-color:#FFFFFF" <%=!familleContribuable.isCodeActif()?"class='subsideDisabled'":"" %>>
											<td align="left"><%=familleContribuable.getNomPrenom()%></td>
											<td align="center" title="<%=objSession.getCodeLibelle(familleContribuable.getTypeDemande())%>">
											<%=objSession.getCode(familleContribuable.getTypeDemande())%>
											</td>
											<td align="center" title="<%=(familleContribuable.getRefus()?"Refus":"")%>">
											<%=(familleContribuable.getRefus()?"R":"")%>
											</td>
											<td align="center" title="<%=objSession.getCodeLibelle(familleContribuable.getCodeTraitementDossier())%>">
											<%=objSession.getCode(familleContribuable.getCodeTraitementDossier())%>
											</td>
											<%	
												double yearMontantContribution = 0.0;
												double yearMontantSupplement = 0.0;
												String yearMontantContributionTotal = "0.0";
												try{
													yearMontantContribution = Double.parseDouble(familleContribuable.getMontantContribution());
				                					yearMontantSupplement= Double.parseDouble(familleContribuable.getSupplExtra());
				                					yearMontantContributionTotal = ""+(yearMontantContribution+yearMontantSupplement);
				                					FWCurrency currentMontant = new FWCurrency();
				                					currentMontant.add(yearMontantContributionTotal);
				                					yearMontantContributionTotal = currentMontant.toStringFormat();
												}catch(Exception ex){
													
												}
				                			 %>
											<td align="right">&nbsp;<%=yearMontantContributionTotal%>&nbsp;&nbsp;&nbsp;</td>
											<%
											/////
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
											<% 
											if (JadeStringUtil.isBlankOrZero(myCurrentCSCodeTemp)) {
												if (JadeStringUtil.isBlankOrZero(myCurrentCSCode)) {%>
													<td></td>
													<td>
														-
													</td>	
												<%} else { %>
													<td title="<%=objSession.getCodeLibelle(myCurrentCS)%>">
														<%=myCurrentCSCode%>
													</td>
													<td title="<%=objSession.getCodeLibelle(myCurrentCS)%>">
														<%=" - " +viewBean.getLibelleCodeSysteme(objSession.getCode(myCurrentCS)) %>
													</td>
												<%}
											} else {%>
												<% if (familleContribuable.getDetailFamilleId().equals(idDetailFamilleWithTempDoc)) { %> 
													<td title="Non envoy&eacute; : <%=objSession.getCodeLibelle(myCurrentCSTemp)%>">
														<span style="font-style:italic;"><%=myCurrentCSCodeTemp%></span>
													</td>
													<td title="Non envoy&eacute; : <%=objSession.getCodeLibelle(myCurrentCSTemp)%>">
														<span style="font-style:italic;"><%=" - " +viewBean.getLibelleCodeSysteme(objSession.getCode(myCurrentCSTemp)) %></span>
													</td>
												<% } else { %>
													<td title="<%=objSession.getCodeLibelle(myCurrentCS)%>">
														<%=myCurrentCSCode%>
													</td>
													<td title="<%=objSession.getCodeLibelle(myCurrentCS)%>">
														<%=" - " +viewBean.getLibelleCodeSysteme(objSession.getCode(myCurrentCS)) %>
													</td>
												<% } %>
											<% } %>
											<!-- <td align="right">&nbsp;<%=objSession.getCode(myCurrentCS)%></td>
											<td align="left" title="<%=objSession.getCodeLibelle(myCurrentCS)%>">
												<%="- " +viewBean.getLibelleCodeSysteme(objSession.getCode(myCurrentCS)) %>
											</td> -->
											<td align="center">&nbsp;<%=familleContribuable.getDateEnvoi()%>&nbsp;</td>
											<td align="center">&nbsp;<%=familleContribuable.getDebutDroit()%>&nbsp;</td>
											<td align="center"><%=familleContribuable.getFinDroit()%></td>
										</tr>
										<% // fin for
										}
										%>
										<%
										// fin if(lastAnneeSubside!=null){
										}
										else{
										%>
										<tr style="font-weight: bold" style="font-style:italic">
											<td colspan="5" align="left"
												style="border-bottom: 1px solid black"
												style="background-color:#FFFFFF">
											Aucun subside alloué
											</td>
										</tr>
										<% // fin else
										}
										%>
									</table>
								</div>									
							
							</td>
							<td></td>
						</tr>
					</table>						
					<div>&nbsp;</div>
					<div id="tabs" style="display: none;">
							<ul style="font-size:13px;font-style:italic" >
								<% if (!contribReprise) {%> 
									<li><a href="amal?userAction=amal.contribuable.contribuableFamille.afficher"><ct:FWLabel key="JSP_AM_CON_D_ONGLET_FAMILLE"/><span>&nbsp;</span></a></li>
								<% } else { %>
									<li><a href="amal?userAction=amal.contribuable.contribuableHistoriqueFamille.afficher"><ct:FWLabel key="JSP_AM_CON_D_ONGLET_FAMILLE"/><span>&nbsp;</span></a></li>
								<% } %>
								<li><a href="amal?userAction=amal.contribuable.contribuableRevenu.afficher<%=contribReprise?"&fromHisto=1":""%>"><ct:FWLabel key="JSP_AM_CON_D_ONGLET_REVENUS"/><span>&nbsp;</span></a></li>
								<li><a href="amal?userAction=amal.contribuable.contribuableTaxations.afficher<%=contribReprise?"&fromHisto=1":""%>"><ct:FWLabel key="JSP_AM_CON_D_ONGLET_TAXATIONS"/><span>&nbsp;</span></a></li>
								<% if (!contribReprise) {%>
									<li><a href="#conteneurCalculs"><ct:FWLabel key="JSP_AM_CON_D_ONGLET_CALCUL"/><span>&nbsp;</span></a></li>
									<li><a href="amal?userAction=amal.contribuable.contribuableComplexAnnonceSedex.afficher"><ct:FWLabel key="JSP_AM_CON_D_ONGLET_SEDEX_RP"/><span>&nbsp;</span></a></li>
									<li><a href="amal?userAction=amal.contribuable.contribuableComplexAnnonceSedexCo2.afficher"><ct:FWLabel key="JSP_AM_CON_D_ONGLET_SEDEX_CO_2"/><span>&nbsp;</span></a></li>
									<li><a href="amal?userAction=amal.contribuable.contribuableComplexAnnonceSedexCo4.afficher"><ct:FWLabel key="JSP_AM_CON_D_ONGLET_SEDEX_CO_4"/><span>&nbsp;</span></a></li>
								<% } %>
							</ul>
							<% if (!contribReprise) {%> 	
								<div id="conteneurCalculs">
									<%@ include file="/amalRoot/contribuable/contribuable_de_calculs.jspf" %>
								</div>						
							<% } %>
							
								
					</div>
					<div>&nbsp;</div>
					<div>&nbsp;</div>					
				</TD>
			</TR>
						<%
				//end if if(!viewBeanIsNew{)
					
				}
			%>
<% if (contribReprise || viewBeanIsNew) {%> 				
<%@ include file="/amalRoot/contribuable/contribuable_edition.jspf" %>
<% } %>

<%-- /tpl:insert --%>
<!-- FIN ZONE PRINCIPALE TITLE AND BODY -->
<!-- ************************************************************************************* -->



<!-- ************************************************************************************* -->
<!-- ZONE COMMON BUTTON AND END OF PAGE -->

<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<ct:menuChange displayId="menu" menuId="amal-menuprincipal" showTab="menu" />
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf" %>
<%-- /tpl:insert --%>

<!-- ************************************************************************************* -->
<!-- FIN ZONE COMMON BUTTON AND END OF PAGE -->


<%
long endTime = System.currentTimeMillis();
JadeLogger.info(this, "Time JSP end : " + endTime);
JadeLogger.info(this, "Time JSP Contribuable : "+(endTime - currentTime));
%>