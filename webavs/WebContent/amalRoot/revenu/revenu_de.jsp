<!-- ************************************************************************************* -->
<!-- GLOBAL GLOBAZ IMPORT AND HEADER INCLUSION -->

<%@page import="globaz.globall.util.JACalendar"%>
<%@ page language="java" import="globaz.globall.http.*" errorPage="/errorPage.jsp" contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>

<!-- FIN GLOBAZ IMPORT AND HEADER INCLUSION -->
<!-- ************************************************************************************* -->

<!-- ************************************************************************************* -->
<!-- INITIALIZATION AND SPECIFIC LAMAL CONTRIBUABLE INCLUSION -->

<%-- tpl:put name="zoneInit" --%>
<%@page import="ch.globaz.amal.business.constantes.IAMCodeSysteme"%>
<%@page import="globaz.globall.util.JAUtil"%>
<%@page import="globaz.globall.util.JADate"%>
<%@page import="globaz.globall.api.GlobazSystem"%>
<%@page import="ch.globaz.amal.web.application.AMApplication"%>
<%@page import="globaz.globall.db.BApplication"%>
<%@page import="globaz.jade.product.JadeApplication"%>
<%@page import="globaz.globall.util.JANumberFormatter"%>
<%@page import="globaz.framework.util.FWCurrency"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.lang.*"%>
<%@page import="globaz.jade.client.util.*"%>
<%@page import="globaz.jade.log.*"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.amal.vb.revenu.AMRevenuViewBean"%>
<%@page import="ch.globaz.amal.business.models.revenu.Revenu"%>
<%@page import="ch.globaz.amal.business.models.revenu.RevenuFullComplex"%>
<%@page import="ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex"%>
<%@page import="ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplexSearch"%>
<%@page import="globaz.amal.vb.contribuable.AMContribuableViewBean"%>
<%@page import="ch.globaz.amal.business.models.contribuable.Contribuable"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="ch.globaz.amal.business.constantes.IAMActions"%>
<%
	//Les labels de cette page commencent par le préfix "JSP_AM_RE_D"
	idEcran = "AM0005";

	// Set Id of the current revenu-viewbean
	AMRevenuViewBean viewBean = (AMRevenuViewBean) session.getAttribute("viewBean");
	viewBean.setId(request.getParameter("selectedId"));

	// Get the contribuable Id of the current revenu-viewbean
	if (null != request.getParameter("contribuableId")) {
		JadeLogger.info(viewBean, "ContribuableId : " + request.getParameter("contribuableId"));
		
		if(viewBean.getContribuable().getId()==null){
			viewBean.getRevenuFullComplex().getSimpleRevenu().setIdContribuable(request.getParameter("contribuableId"));
			viewBean.setIdContribuable(request.getParameter("contribuableId"));
			viewBean.retrieveContribuable();
			JadeLogger.info(viewBean, "viewBean contribuable : " + viewBean.getContribuable().getId());
			JadeLogger.info(viewBean, "viewBean famille : " + viewBean.getContribuable().getFamille().getNomPrenom());
		}
	}
	
	boolean viewBeanIsNew = "add".equals(request.getParameter("_method"));
	String linkRetourContribuable = "amal?userAction="+IAMActions.ACTION_CONTRIBUABLE+".afficher&selectedId="+viewBean.getContribuable().getId()+"&selectedTabId=2";
	String linkRetourContribuableLibelle = "Retour dossier";

	String linkRetourDetail="";
	
	boolean isOldRevenu = false;
	String anneeTaxation = String.valueOf(JACalendar.today().getYear());
	if (!viewBeanIsNew) {
		anneeTaxation = viewBean.getRevenuFullComplex().getSimpleRevenu().getAnneeTaxation();
		isOldRevenu = Integer.parseInt(anneeTaxation)<=2005;		
	}

	autoShowErrorPopup = true;

	bButtonDelete = false;
	
	Boolean contribReprise = false;
	if (viewBean.getContribuable().isNew()) {
		contribReprise = true;		
		//Si on est dans l'historique, on ne doit pas afficher le lien retour détail car on a pas l'id
		linkRetourDetail = "";
		AMContribuableHistoriqueHelper.loadDataFromHistorique(viewBean.getContribuable().getContribuable().getIdContribuable());
		bButtonUpdate = false;
	}
%>
<%-- /tpl:put --%>

<!-- FIN INITIALIZATION AND SPECIFIC LAMAL CONTRIBUABLE INCLUSION -->
<!-- ************************************************************************************* -->

<!-- ************************************************************************************* -->
<!-- JAVASCRIPT AND CSS PART -->

<%-- tpl:put name="zoneBusiness" --%>
<%-- /tpl:put --%>

<%@ include file="/theme/detail/javascripts.jspf"%>

<script type="text/javascript">
var ID_TYPE_REVENU_SOURCIER ="<%=IAMCodeSysteme.CS_TYPE_SOURCIER%>";
var ID_TYPE_REVENU_CONTRIBUABLE ="<%=IAMCodeSysteme.CS_TYPE_CONTRIBUABLE%>";
var MAIN_URL="<%=formAction%>";
var ACTION_REVENU="<%=IAMActions.ACTION_REVENU%>";
var ACTION_CONTRIBUABLE="<%=IAMActions.ACTION_CONTRIBUABLE%>";
var actionMethod;
var userAction;

$(document).ready(function() {
	// init user action
	actionMethod = $('[name=_method]', document.forms[0]).val();
	userAction = $('[name=userAction]', document.forms[0])[0];
	
	<%
	if (viewBeanIsNew) {
	// Si nouveau, afficher par défaut la div contribuable standard
	%>
	$('#idConteneurDeclarationImpots').show();
	$('#idConteneurImpotsSource').hide();
	<%
	} else {
	// Si pas nouveau, check la valeur contribuable
		if(viewBean.getRevenuFullComplex().getSimpleRevenu().isSourcier()){
		%>
		$('#idConteneurDeclarationImpots').hide();
		$('#idConteneurImpotsSource').show();
		<%		
		}else{
		%>
		$('#idConteneurDeclarationImpots').show();
		$('#idConteneurImpotsSource').hide();
		$('#idConteneurDeclarationImpots td').css('font-size','0.8em');
		$('#idConteneurGenericRevenu td').css('font-size','0.8em');
		<%
		}
	}
	%>

	// Choix de la div à afficher en fonction du type de contribuable
	$('#revenuFullComplex\\.simpleRevenu\\.typeRevenu').change(function(){
		if($(this).val()==ID_TYPE_REVENU_SOURCIER){
			$('#idConteneurDeclarationImpots').hide();
			$('#idConteneurImpotsSource').show();
		}else{
			$('#idConteneurDeclarationImpots').show();
			$('#idConteneurImpotsSource').hide();
		}
	});
	
		
});

function add() {}

function upd() {
	// keep not modifiable field not modifiable
	$('#anneeRefVisu').prop('disabled','disabled');
	
	<%
	if (viewBeanIsNew) {
	// en cas d'ajout
	%>

		
		
	<%
	} else {
	// en cas de modification
	%>
	$('#revenuFullComplex\\.simpleRevenu\\.typeRevenu').prop('disabled','disabled');
	<%
	}
	%>	
}

function cancel() {
	if (actionMethod == "add") {
		userAction.value = ACTION_CONTRIBUABLE + ".afficher";
        document.forms[0].elements('_method').value = "";
        document.forms[0].elements('selectedId').value = "<%=request.getParameter("contribuableId")%>";
	} else {
		userAction.value = ACTION_CONTRIBUABLE + ".afficher";
        document.forms[0].elements('selectedId').value = "<%=request.getParameter("contribuableId")%>";
	}
}

function validate() {
	state = true;
	if (actionMethod == "add") {
		userAction.value = ACTION_REVENU + ".ajouter";
	} else {
		userAction.value = ACTION_REVENU + ".modifier";
	}
	return state;
}

function init() {
}

function postInit(){
	// keep not modifiable field not modifiable
	$('#anneeRefVisu').prop('disabled','disabled');
}
	
</script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/revenu/revenu.js"/></script>
<%-- /tpl:put --%>

<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath + "Root")%>/css/amal.css" rel="stylesheet" />
<style>
.inputOldValue { 
	border:2px solid orange
};
</style>
<%-- tpl:put name="zoneScripts" --%>

<!-- FIN JAVASCRIPT AND CSS PART -->
<!-- ************************************************************************************* -->

<!-- ************************************************************************************* -->
<!-- ZONE PRINCIPALE TITLE AND BODY -->
<%@ include file="/theme/detail/bodyStart.jspf"%>
<%-- tpl:insert attribute="zoneTitle" --%>
Détail taxation 

<% if (!contribReprise) {%> 
	<%=(!viewBeanIsNew?"("+viewBean.getContribuable().getPersonneEtendue().getTiers().getDesignation1():"")%>
	<%=(!viewBeanIsNew?" "+viewBean.getContribuable().getPersonneEtendue().getTiers().getDesignation2()+")":"")%>
<% } %>

<%-- /tpl:insert --%>

<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>
<tr>
	<INPUT type="hidden" id="selectedTabId" name="selectedTabId" value="0">
	<input type="hidden" id="idContribuable" name="revenuFullComplex.simpleRevenu.idContribuable" value="<%=viewBean.getContribuable().getId()%>" />							
	<td colspan="4">
		<table width="100%">
			<tr>
				<td align="left" style="vertical-align:top">
				<div class="conteneurContrib">
					<%@ include file="/amalRoot/contribuable/contribuable_contribuable_div_infos.jspf" %>		
				</div>
				</td>
				<td></td>
				<td align="right">
				<%if(!viewBeanIsNew){%>
					<div class="conteneurQuickInfo">	
					<div class="subtitleQuickInfo">Application de la taxation</div>
					<div class="reLinkQuickInfo"></div>							
					<div class="dataQuickInfo">
						<table width="100%" style="border: 1px solid #B3C4DB"  style="border-collapse:collapse" style="background-color:#FFFFFF" >
							<tr style="font-weight: bold" style="font-style:italic">
								<td colspan="10" align="left"
									style="border-bottom: 1px solid black"
									style="background-color:#eeeeee">
								Revenu(s) historique(s) actif(s)
								</td>
							</tr>
							<tr style="height:10px">
								<td colspan="10" style="background-color:#FFFFFF"></td>
							</tr>
							<tr style="background-color:#FFFFFF" style="font-weight: bold">
								<td colspan="10" align="left" data-g-amountformatter="blankAsZero:true">
								<%
									RevenuHistoriqueComplexSearch currentSearch = viewBean.getRevenuHistoriqueActif();
									if(currentSearch==null||currentSearch.getSize()==0){
								%>
									&nbsp;N/A
								<%
									}else{
										
										for(int iHisto=0;iHisto<currentSearch.getSize();iHisto++){
											RevenuHistoriqueComplex currentHisto = (RevenuHistoriqueComplex)currentSearch.getSearchResults()[iHisto];
											String revenuHistoriqueURL = servletContext + "/amal?userAction=amal.revenuHistorique.revenuHistorique.afficher&selectedId="+currentHisto.getId();
											revenuHistoriqueURL += "&contribuableId="+viewBean.getContribuable().getId();
											revenuHistoriqueURL += "&selectedTab=0";
		                					FWCurrency currentMontant = new FWCurrency();
		                					currentMontant.add(currentHisto.getSimpleRevenuDeterminant().getRevenuDeterminantCalcul());
		                					FWCurrency currentRevenu = new FWCurrency();
		                					currentRevenu.add(currentHisto.getSimpleRevenuDeterminant().getRevenuImposableCalcul());
								%>
										&nbsp;<%=currentHisto.getSimpleRevenuHistorique().getAnneeHistorique() %> 
										- revenu déterminant : <%=currentMontant.toStringFormat()%> 
										- revenu imposable : <%=currentRevenu.toStringFormat() %>
										<a id="revenuHistoriqueURL" href="<%=revenuHistoriqueURL%>"> 
										<img
											id="revenuHistoriqueImg"
											width="14px"
											height="14px"
											src="<%=request.getContextPath()%>/images/amal/link.png" title="Revenu déterminant <%=currentHisto.getSimpleRevenuHistorique().getAnneeHistorique() %>" border="0">
										</a>
										</br>
								<%		
										}
									}
								%>
								</td>
							</tr>
							<tr style="height:10px">
								<td colspan="10" style="background-color:#FFFFFF"></td>
							</tr>
							<tr style="font-weight: bold" style="font-style:italic">
								<td colspan="10" align="left"
									style="border-bottom: 1px solid black"
									style="background-color:#eeeeee">
								Revenu(s) historique(s) non-actif(s)
								</td>
							</tr>
							<tr style="height:10px">
								<td colspan="10" style="background-color:#FFFFFF"></td>
							</tr>
							<tr style="background-color:#FFFFFF" style="font-weight: bold">
								<td colspan="10" align="left" data-g-amountformatter="blankAsZero:true">
								<%
									currentSearch = viewBean.getRevenuHistoriqueNonActif();
									if(currentSearch==null||currentSearch.getSize()==0){
								%>
									&nbsp;N/A
								<%
									}else{
										
										for(int iHisto=0;iHisto<currentSearch.getSize();iHisto++){
											RevenuHistoriqueComplex currentHisto = (RevenuHistoriqueComplex)currentSearch.getSearchResults()[iHisto];
		                					FWCurrency currentMontant = new FWCurrency();
		                					currentMontant.add(currentHisto.getSimpleRevenuDeterminant().getRevenuDeterminantCalcul());
		                					FWCurrency currentRevenu = new FWCurrency();
		                					currentRevenu.add(currentHisto.getSimpleRevenuDeterminant().getRevenuImposableCalcul());
								%>
										&nbsp;<%=currentHisto.getSimpleRevenuHistorique().getAnneeHistorique() %> 
										- revenu déterminant : <%=currentMontant.toStringFormat()%> 
										- revenu imposable <%=currentRevenu.toStringFormat()%>
										</br>
								<%		
										}
									}
								%>
								</td>
							</tr>
							<tr style="height:10px">
								<td colspan="10" style="background-color:#FFFFFF"></td>
							</tr>
						</table>
					</div>		
					<% } %>							
				</td>
				<td></td>
			</tr>
		</table>
	
	<div id="tabs">
			<!-- ************************************************************************************* -->
			<!-- ONGLET INFORMATIONS GENERIQUES REVENU -->
			&nbsp;
			<div id="idConteneurGenericRevenu" class="conteneurGenericRevenu">			
			<table id="zoneGenericRevenu" border="0" width="100%" style="border:1px solid black;background-color: #D7E4FF">
				<!-- Empty Line -->
				<tr style="height:4px;">
					<td></td>
				</tr>
				<tr>
					<td width="20px"></td>
					<!-- Année taxation -->
					<td width="150px"><b><ct:FWLabel key="JSP_AM_RE_D_ANNEETAXATION" /></b></td>
					<td><input disabled="disabled"
						name="revenuFullComplex.simpleRevenu.anneeTaxation" id="anneeTaxation" type="text" data-g-integer="sizeMax:4" size="6"
						value="<%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenu().getAnneeTaxation():"")%>" />
					</td>
					<!-- Fin année taxation -->
					<!-- Type de revenu -->															
					<td><b><ct:FWLabel key="JSP_AM_RE_D_TYPE_REVENU" /></b></td>
					<td><ct:FWCodeSelectTag codeType="AMCONTYPE" wantBlank="false" name="revenuFullComplex.simpleRevenu.typeRevenu" defaut="<%=viewBean.getRevenuFullComplex().getSimpleRevenu().getTypeRevenu()%>"/></td>
					<!-- Fin type de revenu -->					
				</tr>				
				<tr style="height:2px;"><td colspan="5"><hr></td></tr>
				<tr>	
					<!-- Type de taxation -->
					<td></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_TYPETAXATION" /></td>
					<td><ct:FWCodeSelectTag codeType="AMTYTAX" wantBlank="true" name="revenuFullComplex.simpleRevenu.typeTaxation" defaut="<%=viewBean.getRevenuFullComplex().getSimpleRevenu().getTypeTaxation()%>"/></td>
					<!-- Fin type de taxation -->	
					<!-- Code Suspendu -->
					<td><ct:FWLabel key="JSP_AM_RE_D_CODESUSPENDU" /></td>
					<td>
						<select disabled="disabled" name="revenuFullComplex.simpleRevenu.codeSuspendu">
							<option <%=(!viewBeanIsNew && "1".equals(viewBean.getRevenuFullComplex().getSimpleRevenu().getCodeSuspendu())?"selected=\"selected\"":"")%> value="1">Oui [S]</option>
							<option <%=(!viewBeanIsNew && "2".equals(viewBean.getRevenuFullComplex().getSimpleRevenu().getCodeSuspendu())?"selected=\"selected\"":"")%> value="2">Non</option>
						</select>
					</td>
					<!-- Fin code Suspendu -->			
				</tr>
				<tr>
					<!-- Etat Civil -->	
					<td></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_ETATCIVIL" /></td>
					<td><ct:FWCodeSelectTag codeType="AMETCIV" wantBlank="true" name="revenuFullComplex.simpleRevenu.etatCivil" defaut="<%=viewBean.getRevenuFullComplex().getSimpleRevenu().getEtatCivil()%>"/></td>
					<!-- Fin etat Civil -->
					<!-- Numéro de lot -->
					<td><ct:FWLabel key="JSP_AM_RE_D_NUMEROLOT" /></td>
					<td><input disabled="disabled" data-g-integer="sizeMax:4 " size="6"
						name="revenuFullComplex.simpleRevenu.noLotAvisTaxation" id="noLotAvisTaxation" type="text"
						value="<%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenu().getNoLotAvisTaxation():"")%>" />
					</td>
					<!-- Fin numéro de lot -->					
				</tr>
				<tr>
					<!-- Nb Enfants -->
					<td></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_NBENFANTS" /></td>
					<td><input disabled="disabled" size="6" id="nbEnfants"
						name="revenuFullComplex.simpleRevenu.nbEnfants" type="text" data-g-integer=" "
						value="<%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenu().getNbEnfants():"")%>" />
						&nbsp;&nbsp;Suspens <input disabled="disabled" size="6"
						name="revenuFullComplex.simpleRevenu.nbEnfantSuspens" id="nbEnfantSuspens" type="text" data-g-integer=" "
						value="<%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenu().getNbEnfantSuspens():"")%>" /> 
					</td>
					<!-- Fin nb Enfants -->
					<!-- Nb de Jour -->
					<td><ct:FWLabel key="JSP_AM_RE_D_NBJOUR" /></td>
					<td><input disabled="disabled" data-g-integer="sizeMax:4 " id="nbJours"
						name="revenuFullComplex.simpleRevenu.nbJours" type="text" size="6"
						value="<%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenu().getNbJours():"")%>" />
					</td>
					<!-- Fin nb de Jour -->					
				</tr>
				<tr>
					<td></td>
					<!-- Montant RDU --> <!-- Existence RDU -->
					<td><ct:FWLabel key="JSP_AM_RE_D_VALEUR_RDU" /> / <ct:FWLabel key="JSP_AM_RE_D_EXISTANCE_RDU_O_N" /></td>
					<td><input disabled="disabled"
						 name="revenuFullComplex.simpleRevenu.revDetUnique" type="text"
						 data-g-amount=" " id="revDetUnique"
						 value="<%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenu().getRevDetUnique():"")%>" />
						<select disabled="disabled" name="revenuFullComplex.simpleRevenu.revDetUniqueOuiNon">
							<option <%=(!viewBeanIsNew && viewBean.getRevenuFullComplex().getSimpleRevenu().getRevDetUniqueOuiNon()?"selected=\"selected\"":"")%> value="true">Oui</option>
							<option <%=(!viewBeanIsNew && !viewBean.getRevenuFullComplex().getSimpleRevenu().getRevDetUniqueOuiNon()?"selected=\"selected\"":"")%> value="false">Non</option>
						</select>
					</td>
					<!-- Fin Montant RDU --><!-- Fin Existence RDU -->					
					<!-- Code Profession -->
					<td width="150px"><ct:FWLabel key="JSP_AM_RE_D_CODEPROFESSION" />
					</td>
					<td><ct:FWCodeSelectTag codeType="AMCOPROF" wantBlank="false" name="revenuFullComplex.simpleRevenu.profession" defaut="<%=viewBean.getRevenuFullComplex().getSimpleRevenu().getProfession()%>"/></td>
					<!-- Fin code Profession -->
				</tr>
				<tr>
					<!-- Date Décision -->
					<td></td>
					<td title="SOURCIERS : date réception ATMIS"><ct:FWLabel key="JSP_AM_RE_D_DATEDECISION" /></td>
					<td><input disabled="disabled"
						name="revenuFullComplex.simpleRevenu.dateAvisTaxation" id="dateAvisTaxation" type="text"
						data-g-calendar="mandatory:false"
						value="<%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenu().getDateAvisTaxation():"")%>"
						 />
					</td>
					<!-- Fin Date Décision -->
					<!-- Date Traitement -->
					<td title="SOURCIERS : date traitement ATMIS"><ct:FWLabel key="JSP_AM_RE_D_DATETRAITEMENT" /></td>
					<td><input disabled="disabled"
						 name="revenuFullComplex.simpleRevenu.dateTraitement" type="text"
						 data-g-calendar="mandatory:false" id="dateTraitement"
						 value="<%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenu().getDateTraitement():"")%>" />
					</td>
					<!-- Fin date Traitement -->									
				</tr>
				<tr>
					<td></td>
					<!-- Date Saisie -->
					<td><ct:FWLabel key="JSP_AM_RE_D_DATESAISIE" /></td>
					<td><input disabled="disabled"
						name="revenuFullComplex.simpleRevenu.dateSaisie" type="text"
						data-g-calendar="mandatory:false" id="dateSaisie"
						value="<%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenu().getDateSaisie():"")%>" />
		 			</td>
		 			<!-- Fin Date Saisie -->
				</tr>
				<!-- Empty Line -->
				<tr style="height:4px;">
					<td></td>
				</tr>
			</table>
			</div>
			&nbsp;
			<!-- ************************************************************************************* -->
			<!-- FIN ONGLET INFORMATIONS GENERIQUES REVENU -->

			<!-- ************************************************************************************* -->
			<!-- ONGLET INFORMATIONS FISCALES REVENU -->
			
			<div id="idConteneurDeclarationImpots" class="conteneurDeclarationImpots">
			<table id="zoneDeclarationImpots"  border="0" width="100%" style="border-collapse:collapse;border:1px solid black;background-color: #D7E4FF">
				<col width="20px" align="center"></col>
				<col style="font-style: italic;font-size:11px" width="68px" align="left"></col>
				<col width="240px" align="left"></col>
				<col width="20px" align="right"></col>
				<col width="120px" align="right"></col>
				<col width="*" align="center"></col>
				<col style="font-style: italic;font-size:11px" width="68px" align="left"></col>
				<col width="240px" align="left"></col>
				<col width="20px" align="right"></col>
				<col width="120px" align="right"></col>
				<col width="20px" align="right"></col>
				<!-- Empty Line -->
				<tr style="height:4px;">
					<td></td>
				</tr>
				<!-- LIGNE 1 -->
				<tr>
					<!-- Revenu net provenant d'un emploi -->
					<td></td>
					<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_REVENUNETEMPLOI,anneeTaxation)%></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_REV_NET_EMPLOI"/></td>
					<td>Fr.</td>
					<td><input disabled="disabled"
						name="revenuFullComplex.simpleRevenuContribuable.revenuNetEmploi" type="text"
						data-g-amount=" " id="revenuNetEmploi"
						value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getRevenuNetEmploi()%>" />
					</td>
					<!-- Rendement de la fortune immobilière commerciale -->
					<td></td>
					<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_RENDEMENTFORTUNEIMMOBCOMM,anneeTaxation)%></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_REND_FORT_IMMO_COMM"/></td>
					<td>Fr.</td>
					<td><input disabled="disabled"
						name="revenuFullComplex.simpleRevenuContribuable.rendFortImmobComm" type="text"
						data-g-amount=" " id="rendFortImmobComm"
						value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getRendFortImmobComm()%>" />
					</td>
					<td>&nbsp;</td>
				</tr>
				<!-- LIGNE 2 -->
				<tr>
					<!-- Revenu net pour l'épouse -->
					<td></td>
					<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_REVENUNETEPOUSE,anneeTaxation)%></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_REV_NET_EPOUSE"/></td>
					<td>Fr.</td>
					<td><input disabled="disabled"
						name="revenuFullComplex.simpleRevenuContribuable.revenuNetEpouse" type="text"
						data-g-amount=" " id="revenuNetEpouse"
						value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getRevenuNetEpouse()%>" />
					</td>
					<!-- Excédent de dépenses propriété immobilière commerciale -->
					<td></td>
					<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_EXCEDENTDEPENSESPROPIMMOCOMM,anneeTaxation)%></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_EXCED_DEP_PROP_IMMO_COMM"/></td>
					<td>Fr.</td>
					<td><input disabled="disabled"
						name="revenuFullComplex.simpleRevenuContribuable.excedDepPropImmoComm" id="excedDepPropImmoComm" type="text"
						data-g-amount=" "
						value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getExcedDepPropImmoComm()%>" />
					</td>
					<td>&nbsp;</td>
				</tr>
				<!-- LIGNE 3 -->
				<tr>
					<!-- Perte de l'activité indépendante -->					
					<td></td>					
					<%if (!isOldRevenu) { %>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_PERTEACTIVITEINDEP,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_PERTE_ACTIV_INDEP"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="perteActIndep"
							name="revenuFullComplex.simpleRevenuContribuable.perteActIndep" type="text" data-g-amount=" "
							value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getPerteActIndep()%>" />
						</td>
					<% } else { %>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					<% } %>
					<!-- Excédent dépense concernant les successions non partagées -->
					<%if (!isOldRevenu) { %>
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_EXCEDENTDEPSUCCNONPART,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_EXCED_SUCC_NON_PART"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="excDepSuccNp"
							name="revenuFullComplex.simpleRevenuContribuable.excDepSuccNp" type="text" data-g-amount=" "
							value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getExcDepSuccNp()%>" />
						</td>
					<% } else { %>
						<td>&nbsp;</td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_PERTESEXERCICESCOMM,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_PERTE_EXERC_COMM" /></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="perteExercicesComm"
							name="revenuFullComplex.simpleRevenuContribuable.perteExercicesComm" type="text" data-g-amount=" "
							value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getPerteExercicesComm()%>" /></td>
					<% } %>
					<td>&nbsp;</td>
				</tr>
				<!-- LIGNE 4 -->
				<tr>
					<!-- Perte de l'activité agricole -->
					<td></td>
					<%if (!isOldRevenu) { %>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_PERTEACTIVITEAGRIC,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_PERTE_ACTIV_AGRI"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled"
							name="revenuFullComplex.simpleRevenuContribuable.perteActAgricole" id="perteActAgricole" type="text" data-g-amount=" "
							value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getPerteActAgricole()%>"  />
						</td>
					<% } else { %>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>						
					<% } %>
					<!-- Totaux des revenus nets -->
					<td></td>
					<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_TOTAUXREVENUSNETS,anneeTaxation)%></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_TOT_REV_NET"/></td>
					<td>Fr.</td>
					<td><input disabled="disabled" id="totalRevenusNets"
						name="revenuFullComplex.simpleRevenuContribuable.totalRevenusNets" type="text"
						data-g-amount=" "
						value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getTotalRevenusNets()%>" />
					</td>
					<td>&nbsp;</td>
				</tr>
				<!-- LIGNE 5 -->
				<tr>
					<!-- Perte de société -->
					<td></td>
					<%if (!isOldRevenu) { %>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_PERTESOCIETE,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_PERTE_SOCIETE"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="perteSociete"
							name="revenuFullComplex.simpleRevenuContribuable.perteSociete" type="text" data-g-amount=" "
							value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getPerteSociete()%>" />
						</td>
					<% } else { %>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					<% } %>
					<!-- Intérêts passifs privés -->
					<td></td>
					<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_INTERETSPASSIFSPRIVE,anneeTaxation)%></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_INT_PASSIFS_PRIV"/></td>
					<td>Fr.</td>
					<td><input disabled="disabled"
						name="revenuFullComplex.simpleRevenuContribuable.interetsPassifsPrive" type="text"
						data-g-amount=" " id="interetsPassifsPrive"
						value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getInteretsPassifsPrive()%>" />
					</td>
					<td>&nbsp;</td>
				</tr>
				<!-- LIGNE 6 -->
				<tr>
					<!-- Perte de l'activité accessoire indépendante -->					
					<td></td>
					<%if (!isOldRevenu) { %>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_PERTEACTIVITEACCESSINDEP,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_PERTE_ACTIV_ACC_INDEP"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled"
							name="revenuFullComplex.simpleRevenuContribuable.perteActAccInd" id="perteActAccInd" type="text" data-g-amount=" "
							value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getPerteActAccInd()%>" />
						</td>
					<% } else { %>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					<% } %>
					<!-- Intérêts passifs commerciaux -->
					<td></td>
					<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_INTERETSPASSIFSCOMM,anneeTaxation)%></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_INT_PASSIFS_COMM"/></td>
					<td>Fr.</td>
					<td><input disabled="disabled"
						name="revenuFullComplex.simpleRevenuContribuable.interetsPassifsComm" type="text"
						data-g-amount=" " id="interetsPassifsComm"
						value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getInteretsPassifsComm()%>" />
					</td>
					<td>&nbsp;</td>
				</tr>
				<!-- LIGNE 7 -->
				<tr>
					<!-- Perte reportée des exercices commerciaux -->					
					<td></td>
					<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_PERTEREPEXCOMM,anneeTaxation)%></td>
					<td>Perte reportée des ex. commerciaux</td>
					<td>Fr.</td>
					<td><input disabled="disabled"
						name="revenuFullComplex.simpleRevenuContribuable.perteCommercial" id="perteCommercial" type="text" data-g-amount=" "
						value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getPerteCommercial()%>" />
					</td>
					<!-- Personnes à charge ou enfants -->
					<td></td>
					<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_PERSONNECHARGEENFANT,anneeTaxation)%></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_PERS_CHARGE_ENFANTS"/></td>
					<td>Fr.</td>
					<td><input disabled="disabled"
						name="revenuFullComplex.simpleRevenuContribuable.persChargeEnf" type="text"
						data-g-amount=" " id="persChargeEnf"
						value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getPersChargeEnf()%>" />
					</td>
					<td>&nbsp;</td>
				</tr>
				<!-- LIGNE 8 -->
				<tr>
					<!-- Perte de liquidation -->					
					<td></td>
					<%if (!isOldRevenu) { %>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_PERTELIQUIDATION,anneeTaxation)%></td>
						<td>Perte de liquidation</td>
						<td>Fr.</td>
						<td><input disabled="disabled"
							name="revenuFullComplex.simpleRevenuContribuable.perteLiquidation" id="perteLiquidation" type="text" data-g-amount=" "
							value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getPerteLiquidation()%>" />
						</td>
					<% } else { %>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					<% } %>
					<!-- Déductions pour les apprentis et étudiants -->
					<td></td>
					<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_DEDUCAPPRENTIETUDIANT,anneeTaxation)%></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_DEDUC_APPR_ETUD"/></td>
					<td>Fr.</td>
					<td><input disabled="disabled" id="deducAppEtu"
						name="revenuFullComplex.simpleRevenuContribuable.deducAppEtu" type="text" data-g-amount=" "
						value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getDeducAppEtu()%>" />
					</td>
					<td>&nbsp;</td>
				</tr>
				<!-- LIGNE 9 -->
				<tr>
					<!-- Allocation de famille -->
					<td></td>
					<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_ALLOCATIONFAMILLE,anneeTaxation)%></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_ALLOC_FAM" /></td>
					<td>Fr.</td>
					<td><input disabled="disabled"
						name="revenuFullComplex.simpleRevenuContribuable.allocationFamiliale" type="text"
						data-g-amount=" " id="allocationFamiliale"
						value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getAllocationFamiliale()%>" />
					</td>
					<!-- Déduction fiscale pour couple mariés-->
					<td></td>
					<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_DEDUCTIONCOUPLESMARIES,anneeTaxation)%></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_DEDUC_FISC_COUPLE_MARIES"/></td>
					<td>Fr.</td>
					<td><input disabled="disabled" id="deducCouplesMaries"
						name="revenuFullComplex.simpleRevenuContribuable.deductionCouplesMaries" type="text" data-g-amount=" "
						value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getDeductionCouplesMaries()%>" />
					</td>
					<td>&nbsp;</td>
				</tr>
				<!-- LIGNE 10 -->
				<tr>
					<!-- Indemnité imposable à 90% -->
					<td></td>
					<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_INDEMNITEIMPOSABLE,anneeTaxation)%></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_INDEMN_IMPOS"/></td>
					<td>Fr.</td>
					<td><input disabled="disabled"
						name="revenuFullComplex.simpleRevenuContribuable.indemniteImposable" type="text"
						data-g-amount=" " id="indemniteImposable"
						value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getIndemniteImposable()%>" />
					</td>
					<!-- Revenu imposable -->
					<td></td>
					<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_REVENUIMPOSABLE,anneeTaxation)%></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_REV_IMPOS"/></td>
					<td>Fr.</td>
					<td><input disabled="disabled" id="revenuImposable"
						name="revenuFullComplex.simpleRevenuContribuable.revenuImposable" type="text" data-g-amount=" "
						value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getRevenuImposable()%>" />
					</td>
					<td>&nbsp;</td>
				</tr>
				<!-- LIGNE 11 -->
				<tr>
					<!-- Rendement de la fortune immobilière privée -->
					<td></td>
					<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_RENDEMENTFORTUNEIMMOBPRIVE,anneeTaxation)%></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_REND_FORT_IMMO_PRIV"/></td>
					<td>Fr.</td>
					<td><input disabled="disabled"
						name="revenuFullComplex.simpleRevenuContribuable.rendFortImmobPrive" type="text"
						data-g-amount=" " id="rendFortImmobPrive"
						value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getRendFortImmobPrive()%>" />
					</td>
					<!-- Revenu taux -->
					<td></td>
					<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_REVENUTAUX,anneeTaxation)%></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_REV_TAUX"/></td>
					<td>Fr.</td>
					<td><input disabled="disabled" id="revenuTaux"
						name="revenuFullComplex.simpleRevenuContribuable.revenuTaux" type="text" data-g-amount=" "
						value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getRevenuTaux()%>" />
					</td>
					<td>&nbsp;</td>
				</tr>
				<!-- LIGNE 12 -->
				<tr>
					<!-- Excédent de dépenses propriété immobilière privée -->
					<td></td>
					<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_EXCEDENTDEPENSESPROPIMMOPRIVE,anneeTaxation)%></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_EXCED_DEP_PROP_IMMO_PRIV"/></td>
					<td>Fr.</td>
					<td><input disabled="disabled"
						name="revenuFullComplex.simpleRevenuContribuable.excedDepPropImmoPriv" id="excedDepPropImmoPriv" type="text"
						data-g-amount=" "
						value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getExcedDepPropImmoPriv()%>" />
					</td>
					<!-- Fortune imposable -->	
					<td></td>
					<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_FORTUNEIMPOSABLE,anneeTaxation)%></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_FORT_IMPOS"/></td>
					<td>Fr.</td>
					<td><input disabled="disabled" id="fortuneImposable"
						name="revenuFullComplex.simpleRevenuContribuable.fortuneImposable" type="text"
						data-g-amount=" "
						value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getFortuneImposable()%>" />
					</td>
					<td>&nbsp;</td>					
				</tr>
				<!-- LIGNE 13 -->
				<tr>
					<!-- Excédent de dépenses propriété immobilière privée -->
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<!-- Fortune taux -->
					<td></td>
					<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_FORTUNETAUX,anneeTaxation)%></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_FORT_TAUX"/></td>
					<td>Fr.</td>
					<td><input disabled="disabled" id="fortuneTaux"
						name="revenuFullComplex.simpleRevenuContribuable.fortuneTaux" type="text" data-g-amount=" "
						value="<%=viewBean.getRevenuFullComplex().getSimpleRevenuContribuable().getFortuneTaux()%>" />
					</td>
					<td>&nbsp;</td>
				</tr>
								
				<tr style="height:4px;"><td></td></tr>
			</table>
			</div>
			
			<!-- ************************************************************************************* -->
			<!-- FIN ONGLET INFORMATIONS FISCALES REVENU -->
			
			
			<!-- ************************************************************************************* -->
			<!-- ONGLET INFORMATIONS FISCALES IMPOTS SOURCE -->		
			<div id="idConteneurImpotsSource" class="conteneurImpotsSource">
			<table id="zoneImpotsSource" width="100%" style="border:1px solid black; background-color: #D7E4FF">
				<col width="20px" align="left"></col>
				<col width="420px" align="left"></col>
				<col width="*" align="center"></col>
				<col width="480px" align="right"></col>				
				<tr>
					<td></td>
					<td>
						<table width="100%">
							<col align="left"></col>
							<col width="20px" align="right"></col>
							<col width="120px" align="right"></col>
							<!-- Titre -->
							<tr>
								<td><b><i><ct:FWLabel key="JSP_AM_RE_D_DONNEES"/> :</i></b></td>								
							</tr>
							<tr>
								<td colspan="3">&nbsp;</td>
							</tr>
							<!-- Année référence -->
							<tr>
								<td><ct:FWLabel key="JSP_AM_RE_D_ANNEE_REFERENCE"/></td>
								<td></td>
								<td><input disabled="disabled" type="text" size="6"
									 data-g-integer="sizeMax:4" id="anneeRefVisu"
									 value="<%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenu().getAnneeTaxation():"")%>" />
								</td>
							</tr>
							<!-- Revenu annuel époux -->
							<tr>
								<td><ct:FWLabel key="JSP_AM_RE_D_REVENU_ANNUEL_EPOUX"/></td>
								<td>Fr.</td>
								<td><input disabled="disabled"
									 name="revenuFullComplex.simpleRevenuSourcier.revenuEpouxAnnuel" type="text"
									 data-g-amount=" " id="revenuEpouxAnnuel"
									 value="<%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenuSourcier().getRevenuEpouxAnnuel():"")%>" />
								</td>
							</tr>
							<!-- Revenu mensuel époux -->
							<tr>
								<td><ct:FWLabel key="JSP_AM_RE_D_REVENU_MENSUEL_EPOUX"/></td>
								<td>Fr.</td>
								<td><input disabled="disabled"
									 name="revenuFullComplex.simpleRevenuSourcier.revenuEpouxMensuel" type="text"
									 data-g-amount=" " id="revenuEpouxMensuel"
									 value="<%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenuSourcier().getRevenuEpouxMensuel():"")%>" />
								</td>
							</tr>
							<!-- Revenu annuel épouse -->
							<tr>
								<td><ct:FWLabel key="JSP_AM_RE_D_REVENU_ANNUEL_EPOUSE"/></td>
								<td>Fr.</td>
								<td><input disabled="disabled"
									 name="revenuFullComplex.simpleRevenuSourcier.revenuEpouseAnnuel" type="text"
									 data-g-amount=" " id="revenuEpouseAnnuel"
									 value="<%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenuSourcier().getRevenuEpouseAnnuel():"")%>" />
								</td>
							</tr>
							<!-- Revenu mensuel épouse -->
							<tr>
								<td><ct:FWLabel key="JSP_AM_RE_D_REVENU_MENSUEL_EPOUSE"/></td>
								<td>Fr.</td>
								<td><input disabled="disabled"
									 name="revenuFullComplex.simpleRevenuSourcier.revenuEpouseMensuel" type="text"
									 data-g-amount=" " id="revenuEpouseMensuel"
									 value="<%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenuSourcier().getRevenuEpouseMensuel():"")%>" />
								</td>
							</tr>
							<tr>
								<td><ct:FWLabel key="JSP_AM_RE_D_NOMBRE_MOIS"/></td>
								<td></td>
								<td><input disabled="disabled" type="text" data-g-integer=" " size="4"
									name="revenuFullComplex.simpleRevenuSourcier.nombreMois" id="nombreMois"
									value="<%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenuSourcier().getNombreMois():"")%>" />
								</td>
							</tr>
						</table>
					</td>
					
					<td></td>
					
					<td>
						<table width="100%" bgcolor="white" style="border: 1px solid #B3C4DB; font-size:11px">
							<tr style="font-weight: bold">
								<!-- Revenu pris en compte -->
								<td></td>
								<td><ct:FWLabel key="JSP_AM_RE_D_REVENU_PRIS_EN_COMPTE"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="revenuPrisEnCompte"><%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenuSourcier().getRevenuPrisEnCompte():"")%></td>
							</tr>
							<tr>
								<!-- Cotisations AVS/AI/APG -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_COTISATIONS_AVS_AI_APG"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="cotisationAvsAiApg"><%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenuSourcier().getCotisationAvsAiApg():"")%></td>
							</tr>
							<tr>
								<!-- Cotisations AC -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_COTISATIONS_AC"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="cotisationAc"><%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenuSourcier().getCotisationAc():"")%></td>
							</tr>
							<tr>
								<!-- Cotisations AC supplémentaires -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_COTISATIONS_AC_SUPPL"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="cotisationAcSupplementaires"><%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenuSourcier().getCotisationAcSupplementaires():"")%></td>
							</tr>
							<tr>
								<!-- Primes AANP -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_PRIMES_AANP"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="primesAANP"><%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenuSourcier().getPrimesAANP():"")%></td>
							</tr>
							<tr>
								<!-- Primes LPP -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_PRIMES_LPP"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="primesLPP"><%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenuSourcier().getPrimesLPP():"")%></td>
							</tr>
							<!-- Déductions assurances -->
							<tr>
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_DEDUCTIONS_ASSURANCES"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="deductionAssurances"><%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenuSourcier().getDeductionAssurances():"")%></td>
							</tr>
							<tr>
								<!-- Déductions assurances enfants -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_DEDUCTIONS_ASSURANCE_ENFANTS"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="deductionAssurancesEnfant"><%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenuSourcier().getDeductionAssurancesEnfant():"")%></td>
							</tr>
							<!-- Déductions assurances jeunes (16) -->
							<tr>
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_DEDUCTIONS_ASSURANCES_JEUNES"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="deductionAssurancesJeunes"><%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenuSourcier().getDeductionAssurancesJeunes():"")%></td>
							</tr>
							<tr>
								<!-- Déductions enfants -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_DEDUCTIONS_ENFANTS"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="deductionEnfants"><%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenuSourcier().getDeductionEnfants():"")%></td>
							</tr>
							<!-- Déductions frais d'obtention -->
							<tr>
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_DEDUCTIONS_FRAIS_OBTENTION"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="deductionFraisObtention"><%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenuSourcier().getDeductionFraisObtention():"")%></td>
							</tr>
							<tr>
								<!-- Déductions double gain -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_DEDUCTION_DOUBLE_GAIN"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="deductionDoubleGain"><%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenuSourcier().getDeductionDoubleGain():"")%></td>
							</tr>
							<!-- Titre Revenu imposable -->
							<tr style="font-weight: bold" style="font-style:italic">
								<td colspan="4" align="left" style="background-color: #eeeeee"
									style="border-bottom: 1px solid black"><ct:FWLabel key="JSP_AM_RE_D_LIBELLE_REVENU_IMPOSABLE"/></td>
							</tr>
							<!-- Revenu Imposable -->
							<tr style="font-weight: bold">
								<td colspan="2"><ct:FWLabel key="JSP_AM_RE_D_TOTAL_REVENU_IMPOSABLE"/></td>
								<td>Fr.&nbsp;&nbsp;&nbsp;<img src="<%=request.getContextPath()%>/images/amal/status_unknown.png"
									id="imgDiffCalcDb" 
									style="display:none"
									title="<ct:FWLabel key="JSP_AM_RE_D_MSG_DIFF_CALC_DB"/>"									 
									border="0"
									onMouseOver="this.style.cursor='help';"
									onMouseOut="this.style.cursor='pointer';"
									width="18px"
									height="18px"
									></td>
								<td data-g-amountformatter="blankAsZero:true" id="totalRevenuImposable">
									<%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenuSourcier().getRevenuImposable():"")%>
								</td>
							</tr>
							<input type="hidden" name="revenuFullComplex.simpleRevenuSourcier.revenuPrisEnCompte" id="hidden_revenuPrisEnCompte" />
							<input type="hidden" name="revenuFullComplex.simpleRevenuSourcier.cotisationAvsAiApg" id="hidden_cotisationAvsAiApg" />
							<input type="hidden" name="revenuFullComplex.simpleRevenuSourcier.cotisationAc" id="hidden_cotisationAc" />					
							<input type="hidden" name="revenuFullComplex.simpleRevenuSourcier.cotisationAcSupplementaires" id="hidden_cotisationAcSupplementaires" />
							<input type="hidden" name="revenuFullComplex.simpleRevenuSourcier.primesAANP" id="hidden_primesAANP" />
							<input type="hidden" name="revenuFullComplex.simpleRevenuSourcier.primesLPP" id="hidden_primesLPP" />
							<input type="hidden" name="revenuFullComplex.simpleRevenuSourcier.deductionAssurances" id="hidden_deductionAssurances" />
							<input type="hidden" name="revenuFullComplex.simpleRevenuSourcier.deductionAssurancesEnfant" id="hidden_deductionAssurancesEnfant" />
							<input type="hidden" name="revenuFullComplex.simpleRevenuSourcier.deductionAssurancesJeunes" id="hidden_deductionAssurancesJeunes" />
							<input type="hidden" name="revenuFullComplex.simpleRevenuSourcier.deductionEnfants" id="hidden_deductionEnfants" />
							<input type="hidden" name="revenuFullComplex.simpleRevenuSourcier.deductionFraisObtention" id="hidden_deductionFraisObtention" />
							<input type="hidden" name="revenuFullComplex.simpleRevenuSourcier.deductionDoubleGain" id="hidden_deductionDoubleGain" />
							<input type="hidden" name="revenuFullComplex.simpleRevenuSourcier.revenuImposable" id="hidden_totalRevenuImposable" value="<%=(!viewBeanIsNew?viewBean.getRevenuFullComplex().getSimpleRevenuSourcier().getRevenuImposable():"")%>"/>
						</table>
					</td>
				</tr>
			</table>
			</div>
			<!-- ************************************************************************************* -->
			<!-- FIN ONGLET INFORMATIONS FISCALES IMPOTS SOURCE -->
	</div>

	</td>
</tr>

<%-- /tpl:put --%>
<!-- FIN ZONE PRINCIPALE TITLE AND BODY -->
<!-- ************************************************************************************* -->

<!-- ************************************************************************************* -->
<!-- ZONE COMMON BUTTON AND END OF PAGE -->

<%@ include file="/theme/detail/bodyButtons.jspf"%>
<%-- tpl:put name="zoneButtons" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf"%>
<ct:menuChange displayId="menu" menuId="amal-menuprincipal" showTab="menu" />
<%-- tpl:put name="zoneEndPage" --%>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/footer.jspf"%>

<!-- ************************************************************************************* -->
<!-- FIN ZONE COMMON BUTTON AND END OF PAGE -->
