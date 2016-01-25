<!-- ************************************************************************************* -->
<!-- GLOBAL GLOBAZ IMPORT AND HEADER INCLUSION -->

<%@page import="globaz.framework.secure.FWSecureConstants"%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme"%>
<%@ page language="java" import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%@ include file="/theme/detail/header.jspf"%>

<!-- FIN GLOBAZ IMPORT AND HEADER INCLUSION -->
<!-- ************************************************************************************* -->



<!-- ************************************************************************************* -->
<!-- INITIALIZATION AND SPECIFIC LAMAL FAMILLE INCLUSION -->

<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.fweb.util.JavascriptEncoder"%>
<%@page import="ch.horizon.jaspe.util.JACalendar"%>
<%@page import="ch.horizon.jaspe.util.JAUtil"%>
<%@page import="globaz.jade.client.util.JadeDateUtil"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.globall.util.JADate"%>
<%@page import="globaz.amal.vb.famille.AMFamilleViewBean"%>
<%@page import="ch.globaz.amal.business.models.famille.FamilleContribuable"%>
<%@page import="ch.globaz.amal.business.models.famille.FamilleContribuableView"%>
<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneEtendueComplexModel"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="ch.globaz.amal.business.constantes.IAMActions"%>
<%@page import="globaz.jade.log.*"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="ch.globaz.pyxis.business.model.TiersSimpleModel"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneSimpleModel"%>
<%@page import="globaz.prestation.interfaces.tiers.PRTiersHelper"%>
<%@page import="globaz.amal.utils.AMUserHelper"%>
<%@page import="globaz.framework.util.FWCurrency"%>

<%
	//
	//Les labels de cette page commencent par le préfix "JSP_AM_DOSSIER_D"
	idEcran="AM0003";

	boolean viewBeanIsNew = "add".equals(request.getParameter("_method"));
	boolean contribuableIsNew = false;

	AMFamilleViewBean viewBean = (AMFamilleViewBean) session.getAttribute("viewBean");
	String idContrib = "";
	if (!viewBeanIsNew) {
		idContrib = viewBean.getFamilleContribuable().getSimpleFamille().getIdContribuable();
	} else {
		try {
			idContrib = request.getParameter("contribuableId").toString();
		} catch (Exception e) {
			idContrib = viewBean.getFamilleContribuable().getSimpleFamille().getIdContribuable();
		}
	}
		
	String selectedTabId = request.getParameter("selectedTabId");
	if (JadeStringUtil.isBlankOrZero(selectedTabId)) {
		selectedTabId = "0";
	}
	
	String linkRetourContribuable = "amal?userAction=amal.contribuable.contribuable.afficher&selectedTabId="+selectedTabId+"&selectedId="+idContrib;
	String linkRetourContribuableLibelle = "Retour dossier";
	String linkRetourDetail = "";
	String linkRetourDetailLibelle = "";
	String linkDetailFamille = "amal?userAction=amal.detailfamille.detailfamille.afficher&selectedId=";
	String linkDetailFamilleNouveau = "amal?userAction=amal.detailfamille.detailfamille.afficher&_method=add";
	linkDetailFamilleNouveau+="&contribuableId="+viewBean.getFamilleContribuable().getSimpleFamille().getIdContribuable();
	linkDetailFamilleNouveau+="&membreFamilleId="+viewBean.getFamilleContribuable().getSimpleFamille().getIdFamille();
	linkDetailFamilleNouveau+="&selectedTabId="+selectedTabId;
	String linkDetailFamilleDel = "amal?userAction=amal.detailfamille.detailfamille.supprimer&selectedId=";
	
	// Get the contribuable Id of the current famille-viewbean
	if (null != request.getParameter("contribuableId") && viewBeanIsNew==true) {
		JadeLogger.info(viewBean, "CREATION MEMBRE FAMILLE" );
		// Ajout d'un membre d'une famille
		if(viewBean.getContribuable().getId()==null){
			viewBean.setIdContribuable(request.getParameter("contribuableId"));
			viewBean.getContribuable().getFamille().setIsContribuable(false);
			viewBean.retrieveContribuable();
			JadeLogger.info(viewBean, "viewBean contribuable : " + viewBean.getContribuable().getId());
			JadeLogger.info(viewBean, "viewBean famille : " + viewBean.getContribuable().getFamille().getNomPrenom());
		}
	}
	else if(null == request.getParameter("contribuableId") && viewBeanIsNew==false){ 
		JadeLogger.info(viewBean, "CONSULTATION MEMBRE FAMILLE" );
		// Consultation
		viewBean.setIdContribuable(viewBean.getFamilleContribuable().getSimpleFamille().getIdContribuable());
		viewBean.retrieveContribuable();
	}
	else if(null == request.getParameter("contribuableId") && viewBeanIsNew==true){
		JadeLogger.info(viewBean, "CREATION CONTRIBUABLE" );
		contribuableIsNew = true;
	}
	
	PersonneEtendueComplexModel personne= viewBean.getFamilleContribuable().getPersonneEtendue();
	boolean idTiersEmpty = JadeStringUtil.isBlankOrZero(personne.getTiers().getIdTiers());

	autoShowErrorPopup = true;
	bButtonDelete = false;
	Boolean contribReprise = false;
	if (viewBean.getContribuable().isNew()) {
		contribReprise = true;
		bButtonUpdate = false;
	}
	
	boolean hasRightDelete = objSession.hasRight(userActionDel, FWSecureConstants.REMOVE);
	boolean hasRightAdd = objSession.hasRight(userActionNew, FWSecureConstants.ADD);
%>
<%-- /tpl:put --%>
<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>

<!-- FIN INITIALIZATION AND SPECIFIC LAMAL FAMILLE INCLUSION -->
<!-- ************************************************************************************* -->







<!-- ************************************************************************************* -->
<!-- JAVASCRIPT AND CSS PART -->

<%@ include file="/theme/detail/javascripts.jspf" %>
<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/amal.css" rel="stylesheet" />
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/ajax_amal.js"></script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath+"Root")%>/scripts/util_webamal.js"></script>

<%-- tpl:put name="zoneScripts" --%>
<script type="text/javascript">

var MAIN_URL="<%=formAction%>";

var ACTION_CONTRIBUABLE="<%=IAMActions.ACTION_CONTRIBUABLE%>";
var ACTION_FAMILLE="<%=IAMActions.ACTION_FAMILLE%>";
var actionMethod;
var userAction;

searchAjaxInputId = "searchNssCriteria";
autoSuggestContainerId = "autoSuggestContainer";
prefixModel = 'familleContribuable';

//définir cette méthode si traitement après remplissage ajax du formulaire
function callbackFillInputAjax(){
	mlog('callbackFillInputAjax - begin');
	var allocSync = false;
	var tiersSync = false;
	var idAllocataire = '';	
	if(!tiersSync && document.getElementById(prefixModel+'.personneEtendue.tiers.idTiers').value!=''){
		tiersSync=true;
		
	}
		
	accordStatutLogoWithResponse(tiersSync);

}

$(function(){
	actionMethod=$('[name=_method]',document.forms[0]).val();
	userAction=$('[name=userAction]',document.forms[0])[0];

	// attribue une id à tous les champs ayant un nom mais pas encore d'id
	$('*',document.forms[0]).each(function(){
		if(this.name!=null && this.id==""){
			this.id=this.name;
		}
	});
	
});

$(document).ready(function() {	
	isFromSelectionTiers = '<%=JavascriptEncoder.getInstance().encode(request.getParameter("selectedIndex")!=null?request.getParameter("selectedIndex"):"")%>';
	initNssInputComponent('<%=JavascriptEncoder.getInstance().encode(idEcran)%>','forNumAvs');
	
	if(isFromSelectionTiers!=''){	
		mlog('before synchroTiersAlloc');
		synchroTiersAjax();
		$("#partialforNumAvs").focus();
	}
	
	$(".hidden_show").css("display","none");
	
	$("#familleContribuable\\.simpleFamille\\.pereMereEnfant").change(function() {
		<%if (!viewBeanIsNew)  {%>
			if ($(this).val()=='42001202') {
				$("#isContribuable").val('false');
				$("#span_isContribuable").hide();
				$("#isContribuable").hide();
			} else {
				$("#span_isContribuable").show();
				$("#isContribuable").show();
			}
		<% } %>
	});
	
	<%if (viewBeanIsNew || "42001202".equals(viewBean.getFamilleContribuable().getSimpleFamille().getPereMereEnfant())) { %>
		$("#span_isContribuable").hide();
		$("#isContribuable").hide();
<% } %>
	
});

function init(){}

function add() {}

function upd() {			
	<% if (!idTiersEmpty) { %>
	$('#zoneTiers input, #zoneTiers select').each(function() {
		$(this).prop('disabled', true);		
	});
	$("#partialforNumAvs").prop('disabled',false);
	$("#tiersSelector").prop('disabled',false);
	$("#partialforNumAvs").attr('autocomplete','off');
	<% } %>
}

function cancel() {
	if (actionMethod == "add") {
		userAction.value = ACTION_CONTRIBUABLE + ".afficher";
        document.forms[0].elements('_method').value = "";
        document.forms[0].elements('selectedId').value = "<%=request.getParameter("contribuableId")%>";
	} else {
		userAction.value = ACTION_CONTRIBUABLE + ".afficher";
        document.forms[0].elements('selectedId').value = "<%=viewBean.getFamilleContribuable().getSimpleFamille().getIdContribuable()%>";
	}
}
function validate() {
	state = true;
	if (actionMethod == "add") {
		userAction.value = ACTION_FAMILLE + ".ajouter";
	} else {
		userAction.value = ACTION_FAMILLE + ".modifier";
	}
	return state;
}

function delDetailFamille(finalUrl){
	// CONCERN THE DELETE ACTION ON A DETAIL (SUBSIDE)
	// WE STAY ON THE SAME PAGE
	if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
	   	location=finalUrl;
	}
}

</script>
<%-- /tpl:put --%>

<!-- FIN JAVASCRIPT AND CSS PART -->
<!-- ************************************************************************************* -->









<!-- ************************************************************************************* -->
<!-- ZONE PRINCIPALE TITLE AND BODY -->

<%@ include file="/theme/detail/bodyStart.jspf" %>
<%-- tpl:insert attribute="zoneTitle" --%><ct:FWLabel key="JSP_AM_FA_D_TITLE"/><%-- /tpl:insert --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>

<tr><td>
<%-- tpl:put name="zoneMain" --%>
				<table width="100%">
					<tr>
						<td align="left" style="vertical-align:top">
						<div class="conteneurContrib">	
							<%@ include file="/amalRoot/contribuable/contribuable_contribuable_div_infos.jspf" %>							
							<div>&nbsp</div>

							<%if (!viewBeanIsNew){ %>
								<%@ include file="/amalRoot/contribuable/contribuable_membreFamille_div_infos.jspf" %>
							<%}%>								
						</div>
						</td>
						<td></td>
						<%if (!viewBeanIsNew){  %>
						<td align="right" style="vertical-align:top">
						<div class="conteneurQuickInfoSubsides">	
							<div class="subtitle">
								<table width="100%" style="border-collapse:collapse;">
									<col align="left"></col>
									<col align="right"></col>
									<tr>
										<td style="font-weight:bold;font-size:12px">
											<ct:FWLabel key="JSP_AM_FA_D_SUBSIDES_MEMBRE"/>
										</td>
										<%
										if(JadeStringUtil.isBlankOrZero(viewBean.getFamilleContribuable().getSimpleFamille().getIdTier())){
										%>
										<%}else{%>
										<td>
											<a href="<%=servletContext + "/libra?userAction=libra.journalisations.journalisations.chercher&selectedId=0&idTiers="+viewBean.getFamilleContribuable().getSimpleFamille().getIdTier()%>">
											Journalisation
											<img
												width="12px"
												height="12px"
												src="<%=request.getContextPath()%>/images/amal/link.png" title="Journalisation" border="0">
											</a>
										</td>
										<%
										}
										%>
									</tr>
								</table>
							</div>
							<div class="relLink"></div>							
							<div class="infoSubsides">
								<table class="tdSubsides" style="border: 1px solid #B3C4DB">
									<col align="center"></col>	<!-- Icone 1 -->
									<col align="left"></col> 	<!-- Icone 2 -->
									<col align="center"></col> 	<!-- Année -->
									<col align="center"></col> 	<!-- Type demande -->
									<col align="center"></col> 	<!-- Code Refus -->
									<col align="center"></col> 	<!-- Status -->
									<col align="right"></col> 	<!-- Contribution -->
									<col align="right"></col> 	<!-- Code document -->
									<col align="left"></col> 	<!-- Nom document -->
									<col align="center"></col> 	<!-- Envoi -->
									<col align="center"></col> 	<!-- Début -->
									<col align="center"></col> 	<!-- Fin -->
									<tr>
										<th></th>
										<th></th>
										<th><ct:FWLabel key="JSP_AM_FA_D_COL_TAB_SUBSIDE_ANNEE"/></th>
										<th>Type</th>
										<th>&nbsp;R&nbsp;</th>
										<th>Status</th>
										<th><ct:FWLabel key="JSP_AM_FA_D_COL_TAB_SUBSIDE_CONTRIB"/></th>
										<th></th>
										<th><ct:FWLabel key="JSP_AM_FA_D_COL_TAB_SUBSIDE_DOCUMENT"/></th>
										<th><ct:FWLabel key="JSP_AM_FA_D_COL_TAB_SUBSIDE_ENVOI"/></th>
										<th><ct:FWLabel key="JSP_AM_FA_D_COL_TAB_SUBSIDE_DEBUT"/></th>
										<th><ct:FWLabel key="JSP_AM_FA_D_COL_TAB_SUBSIDE_FIN"/></th>
									</tr>
									<tr class="amalRowOdd">
										<td>
										<% if(hasRightAdd) { %>
											<a href="<%=linkDetailFamilleNouveau%>">
											<img src="<%=request.getContextPath()%>/images/amal/view_right.png"
												title="Nouveau subside" 
												border="0" 
												onMouseOver="this.style.cursor='hand';"
												onMouseOut="this.style.cursor='pointer';"
												width="18px"
												height="18px"
												>
											</a>
										<% } %>
										</td>
										<td colspan="11"></td>
									</tr>
									<%
									// Get the member id
									//String memberId = viewBean.getFamilleContribuable().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
									String memberId = viewBean.getFamilleContribuable().getSimpleFamille().getIdFamille();
									// || !JadeStringUtil.isBlankOrZero(request.getParameter("selectedIndex"))
									if (!(request.getParameter("selectedIndex")==null)) {
										memberId = null;
									}
									
									// si pas de no AVS, prendre nom-prenom
									if (JadeStringUtil.isBlankOrZero(memberId)) {
										memberId = viewBean.getFamilleContribuable().getSimpleFamille().getNomPrenom();
									}
									List<FamilleContribuableView> familleContribuableMemberArray = viewBean.getListeFamilleContribuableViewMember().get(memberId);
									if (familleContribuableMemberArray!=null) {
									int iStyle = 0;
									String rowStyleContrib = "";
									for (Iterator itFamille = familleContribuableMemberArray.iterator(); itFamille.hasNext();) {
										FamilleContribuableView familleContribuable = (FamilleContribuableView) itFamille.next();
										if(familleContribuable.getAnneeHistorique()== null || familleContribuable.getAnneeHistorique().length()<=0){
											continue;
										}
											
										boolean conditionStyle = (iStyle % 2 == 0);
										if (conditionStyle) {
											rowStyleContrib = "amalRow";
										} else {
											rowStyleContrib = "amalRowOdd";
										}
										String amalRowHighligthedContrib = "amalRowHighligthedContrib";
										if (!familleContribuable.isCodeActif()) {
											rowStyleContrib += " subsideDisabled";
											amalRowHighligthedContrib = "amalRowHighligthedContrib subsideDisabled";
										}
																				
										iStyle++;
										
										String detailUrlFamille = linkDetailFamille+familleContribuable.getDetailFamilleId();
										detailUrlFamille+="&selectedTabId="+selectedTabId;
										String detailUrlFamilleDel = linkDetailFamilleDel+familleContribuable.getDetailFamilleId();
									%>
									<tr style="background-color:#B3C4DB"><td colspan="12"></td></tr>
									<tr style="font-size=11px" style="height:18px" class="<%=rowStyleContrib%>" onMouseOver="jscss('swap', this, '<%=rowStyleContrib%>', '<%=amalRowHighligthedContrib%>')" onMouseOut="jscss('swap', this, '<%=amalRowHighligthedContrib%>', '<%=rowStyleContrib%>')">
										<td><a href="<%=detailUrlFamille%>">
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
										<td>
										<!--  Désactiver la suppression en mode lecture -->
										<%if(hasRightDelete) { %>
											<img src="<%=request.getContextPath()%>/images/amal/view_remove.png"
												title="Supprimer le subside <%=familleContribuable.getDebutDroit()%>" 
												border="0" 
												onClick="delDetailFamille('<%=detailUrlFamilleDel%>')"
												onMouseOver="this.style.cursor='hand';"
												onMouseOut="this.style.cursor='pointer';"
												width="18px"
												height="18px"
												>
										<% } %>
										</td>
										<td>&nbsp;&nbsp;<%=familleContribuable.getAnneeHistorique()%>&nbsp;&nbsp;</td>
										<td title="<%=objSession.getCodeLibelle(familleContribuable.getTypeDemande())%>">
										<%=objSession.getCode(familleContribuable.getTypeDemande())%>
										</td>
										<td title="<%=(familleContribuable.getRefus()?"Refus":"")%>">
										<%=(familleContribuable.getRefus()?"R":"")%>
										</td>
										<td title="<%=objSession.getCodeLibelle(familleContribuable.getCodeTraitementDossier())%>">
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
										<td><%=yearMontantContributionTotal%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
										<%																				
										String myCurrentCS = familleContribuable.getNoModeles();
										%>
										<td><%=objSession.getCode(myCurrentCS)%></td>
										<td title="<%=objSession.getCodeLibelle(familleContribuable.getNoModeles())%>">
										<%="-" +viewBean.getLibelleCodeSysteme(objSession.getCode(myCurrentCS)) %>
										</td>
										<td><%=familleContribuable.getDateEnvoi()%>&nbsp;&nbsp;</td>
										<td><%=familleContribuable.getDebutDroit()%></td>
										<td>&nbsp;<%=familleContribuable.getFinDroit()%>&nbsp;</td>
									</tr>
									<%
									}
						}
									%>
								</table>
							</div>									
						</div>
						</td>
						<%//end if (!viewBeanIsNew){  
						}
						%>
						<td></td>
					</tr>
				</table>
				&nbsp
				<TABLE id="zoneContrib" width="100%" style="border:1px solid black" style="background-color:#D7E4FF">
	             	<tr>
	                	<td class="label subtitle" colspan="4" style="font-weight:bold">
	                		<%if (!viewBeanIsNew) { %>
	                			<ct:FWLabel key="JSP_AM_FA_D_DETAIL_MEMBRE"/> <strong>(<%=viewBean.getFamilleContribuable().getSimpleFamille().getIdFamille()%>)</strong>
	                		<% } else { %>
	                			<ct:FWLabel key="JSP_AM_FA_D_DETAIL_MEMBRE"/>
	                		<% } %>
	                	</td>
	                </tr>
					<tr>
						<td width="180px">
							<ct:FWLabel key="JSP_AM_FA_D_TYPE_MEMBRE"/>
						</td>
						<td width="300px">
							<ct:FWCodeSelectTag tabindex="1" codeType="AMTYMEMBRE" name="familleContribuable.simpleFamille.pereMereEnfant" wantBlank="false" defaut='<%=(!viewBeanIsNew?viewBean.getFamilleContribuable().getSimpleFamille().getPereMereEnfant():IAMCodeSysteme.CS_TYPE_ENFANT)%>'/>
						</td>
						<td width="170px">
							<span id="span_isContribuable"><ct:FWLabel key="JSP_AM_FA_D_IS_CONTRIBUABLE_PRINCIPAL"/></span>
						</td>
						<td>
							<select id="isContribuable" disabled="disabled" name="familleContribuable.simpleFamille.isContribuable">
								<option <%=(viewBeanIsNew || (!viewBeanIsNew && !viewBean.getFamilleContribuable().getSimpleFamille().getIsContribuable())?"selected=\"selected\"":"")%> value="false"><ct:FWLabel key="JSP_LISTE_DEROULANTE_NON"/></option>																							
								<option <%=(!viewBeanIsNew && viewBean.getFamilleContribuable().getSimpleFamille().getIsContribuable()?"selected=\"selected\"":"")%> value="true"><ct:FWLabel key="JSP_LISTE_DEROULANTE_OUI"/></option>
							</select>
						</td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_AM_FA_D_DATE_SORTIE"/></td>
						<td colspan="3">
							<input disabled="disabled" tabindex="2" type="text" data-g-calendar="type:month" name="familleContribuable.simpleFamille.finDefinitive" value='<%=(!viewBeanIsNew?viewBean.getFamilleContribuable().getSimpleFamille().getFinDefinitive():"")%>'/>
						</td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_AM_FA_D_CODE_FIN"/></td>
						<td colspan="3">
							<ct:FWCodeSelectTag tabindex="3" codeType="AMCODESTMT" name="familleContribuable.simpleFamille.codeTraitementDossier" wantBlank="true" defaut='<%=(!viewBeanIsNew?viewBean.getFamilleContribuable().getSimpleFamille().getCodeTraitementDossier():"")%>'/>
						</td>
					</tr>
					<tr>
						<td><ct:FWLabel key="JSP_AM_FA_D_NUMERO_PERSONNEL"/></td>
						<td colspan="3">
							<input disabled="disabled" tabindex="4" type="text" name="familleContribuable.simpleFamille.noPersonne" value="<%=(!viewBeanIsNew?viewBean.getFamilleContribuable().getSimpleFamille().getNoPersonne():"")%>"/>
						</td>
					</tr>
	            </TABLE>
	            &nbsp;
				<TABLE id="zoneTiers">
	             	<tr>
	             	
	             		<td class="label subtitle" colspan="4" style="font-weight:bold">
	             		<%if(idTiersEmpty){ %>
	             			<ct:FWLabel key="JSP_AM_FA_D_TIERS"/>               			         
                			<div id="idTiers"></div>
                			<div><a class="syncLink" href="#" onclick="synchroTiersAjax();" title="<%=objSession.getLabel("JSP_AM_LINK_SYNC_TIERS")%>"></a></div>
                			<div id="statutSynchroTiers" name="statutSync"></div>                			
                		<%}else{ %>
                			<ct:FWLabel key="JSP_AM_FA_D_TIERS"/>
                			<div id="idTiers"> (<a href="<%=servletContext + "/pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId="+viewBean.getFamilleContribuable().getPersonneEtendue().getId()%>"><%=viewBean.getFamilleContribuable().getPersonneEtendue().getId()%></a>)</div>
                			<div id="statutSynchroTiers"><img src="images/dialog-ok-apply.png" alt="Synchronisation réussie" width="16" height="16"/></div>
                			
                		<%}%>               		
	                	</td>
	                </tr>
					<TR>		
						<TD width="180px">
							<ct:FWLabel key="JSP_AM_FA_D_NSS"/>
						</TD>
						<TD>
						
						      <!--  Champs nécessaire pour écriture dans les tiers (création / update) -->
			               	<!--<ct:inputHidden name="familleContribuable.personneEtendue.tiers.idTiers"/>
			                <ct:inputHidden name="familleContribuable.personneEtendue.personne.idTiers"/>
			                <ct:inputHidden name="familleContribuable.personneEtendue.personneEtendue.idTiers"/>-->
			                <input type="text" class="hidden_show" name="familleContribuable.personneEtendue.tiers.idTiers"" />
			                <input type="text" class="hidden_show" name="familleContribuable.personneEtendue.personne.idTiers" />
			                <input type="text" class="hidden_show" name="familleContribuable.personneEtendue.personneEtendue.idTiers" />
			                <input type="text" class="hidden_show" name="familleContribuable.personneEtendue.tiers.spy" />
			                <input type="text" class="hidden_show" name="familleContribuable.personneEtendue.personne.spy" />
			                <input type="text" class="hidden_show" name="familleContribuable.personneEtendue.personneEtendue.spy" />
			                <input type="text" class="hidden_show" name="familleContribuable.personneEtendue.tiers.typeTiers" value="500006" />
			                <!--<ct:inputHidden name="familleContribuable.personneEtendue.personneEtendue.spy"/>
			                <ct:inputHidden name="familleContribuable.personneEtendue.personne.spy"/>
			                <ct:inputHidden name="familleContribuable.personneEtendue.tiers.spy"/>-->
			               
                 
                    	<% if (idTiersEmpty || true) { %>	
                    		<nss:nssPopup avsMinNbrDigit="2" nssMinNbrDigit="2" name="forNumAvs" newnss="true" tabindex="5" />
                    		<ct:inputHidden name="familleContribuable.personneEtendue.personneEtendue.numAvsActuel"/>
                    	
                    	<%}else{ %>
                    		<ct:inputText tabindex="1" name="familleContribuable.personneEtendue.personneEtendue.numAvsActuel" 
                    		styleClass="nss readonly" readonly="readonly" disabled="disabled"/>
                    	<%}%>
						<% 
							Object[] tiersMethodsName = new Object[]{
									
									new String[]{"familleContribuable.personneEtendue.personneEtendue.numAvsActuel","numAvsActuel"},
								};
						if (idTiersEmpty || true) { %>	
                    	<ct:FWSelectorTag name="tiersSelector"
								methods="<%=tiersMethodsName%>"
								providerApplication="pyxis" 
								providerPrefix="TI"
								providerAction="pyxis.tiers.tiers.chercher"								
						/>
						<% } %>
                    	<div id="autoSuggestContainer" class="suggestList"></div>																							
						</TD>						
						<TD>
							<ct:FWLabel key="JSP_AM_FA_D_NO_CONTRIBUABLE"/>
						</TD>
						<TD>							
							<input disabled="disabled" tabindex="10" type="text" id="numContribuable" name="familleContribuable.personneEtendue.personneEtendue.numContribuableActuel" value='<%=(!viewBeanIsNew?viewBean.getFamilleContribuable().getPersonneEtendue().getPersonneEtendue().getNumContribuableActuel():"")%>'/>
						</TD>
					</TR>
					<TR>		
						<TD>
							<ct:FWLabel key="JSP_AM_FA_D_TITRE"/>
						</TD>
						<TD>
							<ct:select wantBlank="true" tabindex="6" id="titreTiers" name="familleContribuable.personneEtendue.tiers.titreTiers">
	                    		<ct:optionsCodesSystems csFamille="PYTITRE"></ct:optionsCodesSystems>	
	                    	</ct:select> 									
						</TD>		
						<TD>
							<ct:FWLabel key="JSP_AM_FA_D_SEXE"/>							
						</TD>
						<TD>	
							<ct:select tabindex="11" wantBlank="true" id="sexeTiers" name="familleContribuable.personneEtendue.personne.sexe">
	                    		<ct:optionsCodesSystems csFamille="PYSEXE"></ct:optionsCodesSystems>	
	                    	</ct:select> 					
						</TD>
					</TR>
					<TR>		
						<TD>
							<ct:FWLabel key="JSP_AM_FA_D_NOM"/>
						</TD>
						<TD>
							<input disabled="disabled" tabindex="7" type="text" id="nomTiers" name="familleContribuable.personneEtendue.tiers.designation1" value="<%=(!viewBeanIsNew?viewBean.getFamilleContribuable().getPersonneEtendue().getTiers().getDesignation1():"")%>"/>
						</TD>		
						<TD>
							<ct:FWLabel key="JSP_AM_FA_D_ETAT_CIVIL"/>				
						</TD>
						<TD>
							<ct:select id="etatCivil" tabindex="12" wantBlank="true" name="familleContribuable.personneEtendue.personne.etatCivil">
	                    		<ct:optionsCodesSystems csFamille="PYETATCIVI"></ct:optionsCodesSystems>	
	                    	</ct:select> 	
						</TD>
					</TR>
					<TR>		
						<TD>
							<ct:FWLabel key="JSP_AM_FA_D_PRENOM"/>
						</TD>
						<TD>
							<input disabled="disabled" tabindex="8" type="text" id="prenomTiers" name="familleContribuable.personneEtendue.tiers.designation2" value="<%=(!viewBeanIsNew?viewBean.getFamilleContribuable().getPersonneEtendue().getTiers().getDesignation2():"")%>"/>
						</TD>		
						<TD>
							<ct:FWLabel key="JSP_AM_FA_D_DATE_NAISSANCE"/>								
						</TD>
						<TD>
							<%
							JADate d = new JADate(JACalendar.today().toAMJ());								
								int year = d.getYear();
							%>
							<input tabindex="13" type="text" <%if (idTiersEmpty || true) { %> data-g-calendar="yearRange:¦<%=year-80 %>:<%=year%>¦" <% } %>  name="familleContribuable.personneEtendue.personne.dateNaissance" id="dateNaissance" disabled="disabled" class="clearable" value="<%=(!viewBeanIsNew?viewBean.getFamilleContribuable().getPersonneEtendue().getPersonne().getDateNaissance():"")%>"/>
												 																																
						</TD>
					</TR>
					<TR>		
						<TD>
							<ct:FWLabel key="JSP_AM_FA_D_LANGUE_AFFILIE"/>
						</TD>
						<TD>
							<ct:select tabindex="9" id="tiersLangue" name="familleContribuable.personneEtendue.tiers.langue" defaultValue="503001">
	                    		<ct:optionsCodesSystems csFamille="PYLANGUE"></ct:optionsCodesSystems>	
	                    	</ct:select>
						</TD>
						<td>
							<ct:FWLabel key="JSP_AM_FA_D_NATIONALITE"/>
						</td>
						<td>
							<ct:FWListSelectTag tabindex="14" name="familleContribuable.personneEtendue.tiers.idPays" data="<%=PRTiersHelper.getPays(objSession)%>"
							defaut="<%=JadeStringUtil.isIntegerEmpty(personne.getTiers().getIdPays())?TIPays.CS_SUISSE:personne.getTiers().getIdPays()%>"/>
						</td>		
					</TR>					
				</TABLE>
				
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

