<!-- ************************************************************************************* -->
<!-- GLOBAL GLOBAZ IMPORT AND HEADER INCLUSION -->

<%@page import="globaz.amal.utils.AMRevenuHelper"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" import="globaz.globall.http.*" errorPage="/errorPage.jsp" contentType="text/html;charset=ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct"%>
<%@ include file="/theme/detail/header.jspf"%>

<!-- FIN GLOBAZ IMPORT AND HEADER INCLUSION -->
<!-- ************************************************************************************* -->

<!-- ************************************************************************************* -->
<!-- INITIALIZATION AND SPECIFIC LAMAL CONTRIBUABLE INCLUSION -->

<%-- tpl:put name="zoneInit" --%>
<%@page import="ch.globaz.amal.business.constantes.IAMParametresAnnuels"%>
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
<%@page import="globaz.amal.vb.revenuHistorique.AMRevenuHistoriqueViewBean"%>
<%@page import="ch.globaz.amal.business.models.revenu.Revenu"%>
<%@page import="ch.globaz.amal.business.models.revenu.RevenuHistorique"%>
<%@page import="ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex"%>
<%@page import="globaz.amal.vb.contribuable.AMContribuableViewBean"%>
<%@page import="ch.globaz.amal.business.models.contribuable.Contribuable"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="ch.globaz.amal.business.constantes.IAMActions"%>
<%
	//Les labels de cette page commencent par le préfix "JSP_AM_RE_D"
	idEcran = "AM0007";

	// Set Id of the current revenu-viewbean
	AMRevenuHistoriqueViewBean viewBean = (AMRevenuHistoriqueViewBean) session.getAttribute("viewBean");
	viewBean.setId(request.getParameter("selectedId"));

	// Get the contribuable Id of the current revenu-viewbean
	if (null != request.getParameter("contribuableId")) {
		JadeLogger.info(viewBean, "ContribuableId : " + request.getParameter("contribuableId"));
		
		if(viewBean.getContribuable().getId()==null){
			viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().setIdContribuable(request.getParameter("contribuableId"));
			viewBean.setIdContribuable(request.getParameter("contribuableId"));
			viewBean.retrieveContribuable();
			JadeLogger.info(viewBean, "viewBean contribuable : " + viewBean.getContribuable().getId());
			JadeLogger.info(viewBean, "viewBean famille : " + viewBean.getContribuable().getFamille().getNomPrenom());
		}
	}
	
	boolean viewBeanIsNew = "add".equals(request.getParameter("_method"));
	String linkRetourContribuable = "amal?userAction="+IAMActions.ACTION_CONTRIBUABLE+".afficher&selectedId="+viewBean.getContribuable().getId()+"&selectedTabId=1";
	String linkRetourContribuableLibelle = "Retour dossier";

	String linkRetourDetail="";
	
	boolean isOldRevenu = false;
	Calendar cal = Calendar.getInstance();
	SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	JADate dateToday = new JADate(sdf.format(cal.getTime()));
	String anneeHistorique = String.valueOf(dateToday.getYear());
	String anneeTaxation = anneeHistorique;
	if (!viewBeanIsNew) {
		anneeHistorique = viewBean.getRevenuHistoriqueComplex().getSimpleRevenuHistorique().getAnneeHistorique();
		anneeTaxation = viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getAnneeTaxation();
		isOldRevenu = Integer.parseInt(anneeHistorique)<=2007;		
	}

	autoShowErrorPopup = true;

	bButtonDelete = false;
	bButtonUpdate = false;
	
	Boolean contribReprise = false;
	if (viewBean.getContribuable().isNew()) {
		contribReprise = true;		
		//Si on est dans l'historique, on ne doit pas afficher le lien retour détail car on a pas l'id
		linkRetourDetail = "";
		AMContribuableHistoriqueHelper.loadDataFromHistorique(viewBean.getContribuable().getContribuable().getIdContribuable());
		bButtonUpdate = false;
	}
	
	String selectedTabId = request.getParameter("selectedTabId");
	if(null == selectedTabId || selectedTabId.length()<=0){
		selectedTabId = "0";
	}
	
	boolean histoRevenu = false;
	String idHistoriqueRevenu = request.getParameter("histoRevenu");
	if (null != request.getParameter("histoRevenu")) {
		histoRevenu = true; 
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
var MAIN_URL="<%=formAction%>";
var ACTION_REVENU="<%=IAMActions.ACTION_REVENU_HISTORIQUE%>";
var ACTION_CONTRIBUABLE="<%=IAMActions.ACTION_CONTRIBUABLE%>";
var s_servletContext = "<%=servletContext%>";
var actionMethod;
var userAction;
var ID_TYPE_REVENU_SOURCIER ="<%=IAMCodeSysteme.CS_TYPE_SOURCIER%>";
var ID_TYPE_REVENU_CONTRIBUABLE ="<%=IAMCodeSysteme.CS_TYPE_CONTRIBUABLE%>";

$(function() {
	// initi user action
	actionMethod = $('[name=_method]', document.forms[0]).val();
	userAction = $('[name=userAction]', document.forms[0])[0];
	
	showTabTypeRevenu(<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getTypeRevenu()%>);
	$("#revenuFullComplex\\.simpleRevenu\\.typeRevenu").change(function() {
		showTabTypeRevenu($(this).val());
	});
	
	// init tabs
	$( "#tabs" ).tabs({ selected: <%=selectedTabId%> });
	// init rdet
	<%
	if(viewBeanIsNew){
	%>
	// HIDE FIELD RDET sourcier
	$('#revenuPrisEnCompteRDET_LINE').hide();
	$('#totalRevenuImposableRDET_LINE').hide();
	$('#cotisationAvsAiApgRDET_LINE').hide();
	$('#cotisationAcRDET_LINE').hide();
	$('#cotisationAcSupplRDET_LINE').hide();
	$('#primesAANPRDET_LINE').hide();
	$('#primesLPPRDET_LINE').hide();
	$('#deductionAssurancesRDET_LINE').hide();
	$('#deductionAssurancesEnfantRDET_LINE').hide();
	$('#deductionAssurancesJeunesRDET_LINE').hide();
	$('#deductionEnfantsRDET_LINE').hide();
	$('#deductionFraisObtentionRDET_LINE').hide();
	$('#deductionDoubleGainRDET_LINE').hide();
	<%}%>
});

function add() {}

function upd() {
	// disable everything except taxation liée
	$('#idConteneurGenericRevenu :input').prop('disabled','disabled');
	$('#idConteneurDeclarationImpots :input').prop('disabled','disabled');
	$('#idConteneurImpotsSource :input').prop('disabled','disabled');
	$('#taxationLiee').removeProp('disabled');
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
	// SAVE STATE FOR ANNEE HISTORIQUE ET TAXATION LIEE (CAS ADD)
	var stateAnneeHistorique = $('#anneeHistorique').prop('disabled');
	var stateIdTaxation = $('#taxationLiee').prop('disabled');
	

	// disable all inputs except annee historique et taxation liée (CAS ADD)
	$('#idConteneurGenericRevenu :input').prop('disabled','disabled');
	$('#idConteneurDeclarationImpots :input').prop('disabled','disabled');
	$('#idConteneurImpotsSource :input').prop('disabled','disabled');
	
	// RESTORE STATE FOR ANNEE HISTORIQUE ET TAXATION LIEE (CAS ADD)
	if(stateAnneeHistorique!=true){
		$('#anneeHistorique').removeProp('disabled');	
	}
	if(stateIdTaxation!=true){
		$('#taxationLiee').removeProp('disabled');
	}
	
}
	
</script>
<script type="text/javascript" src="<%=servletContext%><%=(mainServletPath + "Root")%>/scripts/revenu/calculRevenusAmal.js"/></script>
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
<ct:FWLabel key="JSP_AM_RE_D_MAINTITLE" />

<% if (!contribReprise) {%> 
	<%=(!viewBeanIsNew?"("+viewBean.getContribuable().getPersonneEtendue().getTiers().getDesignation1():"")%>
	<%=(!viewBeanIsNew?" "+viewBean.getContribuable().getPersonneEtendue().getTiers().getDesignation2()+")":"")%>
<% } %>

<%-- /tpl:insert --%>

<%@ include file="/theme/detail/bodyStart2.jspf"%>
<%-- tpl:put name="zoneMain" --%>
<tr>
	<INPUT type="hidden" id="selectedTabId" name="selectedTabId" value="0">
	<input type="hidden" id="idContribuable" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.idContribuable" value="<%=viewBean.getContribuable().getId()%>" />							
	<input type="hidden" id="idRevenu" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.idRevenu" value="<%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getIdRevenu()%>" />							
	<td colspan="4">
		<table width="100%">
			<tr>
				<td align="left" style="vertical-align:top">
					<div class="conteneurContrib">
						<%@ include file="/amalRoot/contribuable/contribuable_contribuable_div_infos.jspf" %>		
					</div>
					<%if (histoRevenu) {%>
					<div class="conteneurWarningRevenuHistorique">
						<table id="" width="100%" >
							<tr>
								<td>
									<strong>Revenu historique <%=request.getParameter("isRecalcul") == null ? "" : "(Recalcul)" %></strong>
								</td>
							</tr>
							<tr>
								<td bgcolor="white" style="border: 1px solid #B3C4DB">
									<% String urlRevenu = servletContext + "/amal?userAction=amal.revenuHistorique.revenuHistorique.afficher&selectedId="+idHistoriqueRevenu+"&contribuableId="+viewBean.getContribuable().getId(); %>
									Cliquez <a href="<%=urlRevenu%>">ici</a> pour revenir au revenu utilisé.
								</td>
							</tr>
						</table> 
					</div>
					<% } %>
				</td>
				<td>
				</td>
				<td align="right">
					<div class="conteneurQuickInfoRevenu">	
					<div class="subtitleQuickInfo"><ct:FWLabel key="JSP_AM_RE_D_TITLE_CALCUL" /></div>					
					<div class="reLinkQuickInfo">
					<%if(!viewBeanIsNew){
					%>
						<%viewBean.getNavigation();%>			
							<% if (!JadeStringUtil.isBlankOrZero(viewBean.getIdRevenuAnneePrec())) { %>		
								<a href="amal?userAction=amal.revenuHistorique.revenuHistorique.afficher&selectedId=<%=viewBean.getIdRevenuAnneePrec() %>&contribuableId=<%=viewBean.getContribuable().getId()%>"><img src="<%=request.getContextPath()%>/images/amal/prev.png" title="Revenu précédent" border="0" /></a>
							<% } %>						
							<ct:FWLabel key="JSP_AM_RE_D_TITLE_CALCUL_ANNEE"/>&nbsp;<%=viewBean.getRevenuHistoriqueComplex().getSimpleRevenuHistorique().getAnneeHistorique()%>
							<% if (!JadeStringUtil.isBlankOrZero(viewBean.getIdRevenuAnneeNext())) { %>	
								<a href="amal?userAction=amal.revenuHistorique.revenuHistorique.afficher&selectedId=<%=viewBean.getIdRevenuAnneeNext() %>&contribuableId=<%=viewBean.getContribuable().getId()%>"><img src="<%=request.getContextPath()%>/images/amal/next.png" title="Revenu suivant" border="0" /></a>
							<% } %>
					<% //FIN if(!viewBeanIsNew){
					}
					%>
					</div>							
					<div class="dataQuickInfo">
					<div class="conteneurRevenuDeterminant">		
					<input type="hidden" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.revenuImposableCalcul" value="<%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getRevenuImposableCalcul()%>" id="hidden_revenuImposableCalcul" />
					<input type="hidden" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.fortuneImposableCalcul" value="<%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getFortuneImposablePercentCalcul()%>" id="hidden_fortuneImposablePercentCalcul" />
					<input type="hidden" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.rendementFortuneImmoCalcul" value="<%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getRendementFortuneImmoCalcul()%>" id="hidden_rendementFortuneImmoCalcul" />
					<input type="hidden" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.excedentDepensesPropImmoCalcul" value="<%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getExcedentDepensesPropImmoCalcul()%>" id="hidden_excedDepPropImmoCalcul" />
					<input type="hidden" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.excedentDepensesSuccNonPartageesCalcul" value="<%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getExcedentDepensesSuccNonPartageesCalcul()%>" id="hidden_excedentDepensesSuccNonPartageesCalcul" />
					<input type="hidden" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.interetsPassifsCalcul" value="<%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getInteretsPassifsCalcul()%>" id="hidden_interetsPassifsCalcul" />
					<input type="hidden" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.perteExercicesCommerciauxCalcul" value="<%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getPerteExercicesCommerciauxCalcul()%>" id="hidden_perteExercicesCommerciauxCalcul" />
					<input type="hidden" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.perteReporteeExercicesCommerciauxCalcul" value="<%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getPerteReporteeExercicesCommerciauxCalcul()%>" id="hidden_perteReporteeExercicesCommerciauxCalcul" />
					<input type="hidden" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.perteLiquidationCalcul" value="<%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getPerteLiquidationCalcul()%>" id="hidden_perteLiquidationCalcul" />
					<input type="hidden" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.partRendementImmobExedantIntPassifsCalcul" value="<%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getPartRendementImmobExedantIntPassifsCalcul()%>" id="hidden_partRendementImmobExedantIntPassifsCalcul" />
					<input type="hidden" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.deductionSelonNbreEnfantCalcul" value="<%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getDeductionSelonNbreEnfantCalcul()%>" id="hidden_deductionSelonNbreEnfantCalcul" />
					<input type="hidden" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.deductionContribAvecEnfantChargeCalcul" value="<%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getDeductionContribAvecEnfantChargeCalcul()%>" id="hidden_deductionContribAvecEnfantChargeCalcul" />
					<input type="hidden" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.deductionContribNonCelibSansEnfantChargeCalcul" value="<%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getDeductionContribNonCelibSansEnfantChargeCalcul()%>" id="hidden_deductionContribNonCelibSansEnfantChargeCalcul" />
					<input type="hidden" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.revenuDeterminantCalcul" value="<%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getRevenuDeterminantCalcul()%>" id="hidden_revenuDeterminantCalcul" />
					<table id="zoneRevenuDeterminant" width="100%" bgcolor="white" style="border: 1px solid #B3C4DB" >
						<col width="8px" align="center"></col>
						<col align="left"></col>
						<col width="12px" align="left" ></col>
						<col align="right"></col>
						
						<!-- REVENU DETERMINANT PARTIE SOURCIER -->
						<% if(viewBeanIsNew || viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().isSourcier()) {%>
							<tr id="revenuPrisEnCompteRDET_LINE" style="font-weight: bold">
								<!-- Revenu pris en compte -->
								<td></td>
								<td><ct:FWLabel key="JSP_AM_RE_D_REVENU_PRIS_EN_COMPTE"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="revenuPrisEnCompteRDET"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getRevenuPrisEnCompte():"")%></td>
							</tr>
							<tr id="cotisationAvsAiApgRDET_LINE">
								<!-- Cotisations AVS/AI/APG -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_COTISATIONS_AVS_AI_APG"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="cotisationAvsAiApgRDET"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getCotisationAvsAiApg():"")%></td>
							</tr>
							<tr id="cotisationAcRDET_LINE">
								<!-- Cotisations AC -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_COTISATIONS_AC"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="cotisationAcRDET"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getCotisationAc():"")%></td>
							</tr>
							<tr id="cotisationAcSupplRDET_LINE">
								<!-- Cotisations AC supplémentaires -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_COTISATIONS_AC_SUPPL"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="cotisationAcSupplRDET"><%=0%></td>
								<!-- viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getCotisationAcSupplementaires() -->
							</tr>
							<tr id="primesAANPRDET_LINE">
								<!-- Primes AANP -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_PRIMES_AANP"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="primesAANPRDET"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getPrimesAANP():"")%></td>
							</tr>
							<tr id="primesLPPRDET_LINE">
								<!-- Primes LPP -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_PRIMES_LPP"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="primesLPPRDET"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getPrimesLPP():"")%></td>
							</tr>
							<!-- Déductions assurances -->
							<tr id="deductionAssurancesRDET_LINE">
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_DEDUCTIONS_ASSURANCES"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="deductionAssurancesRDET"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getDeductionAssurances():"")%></td>
							</tr>
							<tr id="deductionAssurancesEnfantRDET_LINE">
								<!-- Déductions assurances enfants -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_DEDUCTIONS_ASSURANCE_ENFANTS"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="deductionAssurancesEnfantRDET"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getDeductionAssurancesEnfant():"")%></td>
							</tr>
							<!-- Déductions assurances jeunes (16) -->
							<tr id="deductionAssurancesJeunesRDET_LINE">
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_DEDUCTIONS_ASSURANCES_JEUNES"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="deductionAssurancesJeunesRDET"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getDeductionAssurancesJeunes():"")%></td>
							</tr>
							<tr id="deductionEnfantsRDET_LINE">
								<!-- Déductions enfants -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_DEDUCTIONS_ENFANTS"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="deductionEnfantsRDET"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getDeductionEnfants():"")%></td>
							</tr>
							<!-- Déductions frais d'obtention -->
							<tr id="deductionFraisObtentionRDET_LINE">
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_DEDUCTIONS_FRAIS_OBTENTION"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="deductionFraisObtentionRDET"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getDeductionFraisObtention():"")%></td>
							</tr>
							<tr id="deductionDoubleGainRDET_LINE">
								<!-- Déductions double gain -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_DEDUCTION_DOUBLE_GAIN"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="deductionDoubleGainRDET"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getDeductionDoubleGain():"")%></td>
							</tr>
							<!-- Revenu Imposable -->
							<tr id="totalRevenuImposableRDET_LINE" style="font-weight: bold">
								<td></td>
								<td><ct:FWLabel key="JSP_AM_RE_D_TOTAL_REVENU_IMPOSABLE"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="totalRevenuImposableRDET"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getRevenuImposable():"")%>
								</td>
							</tr>
						<%} %>

						<!-- REVENU DETERMINANT PARTIE CONTRIBUABLE STANDARD -->
						<% if(viewBeanIsNew || !viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().isSourcier()) {%>
						<!-- Revenu imposable selon chiffre 690 -->
						<tr id="revenuImposableCalcul_LINE" style="font-weight: bold">
							<td></td>
							<td><ct:FWLabel key="JSP_AM_RE_D_REV_IMPOS_CALCUL"/></td>
							<td>Fr.</td>
							<td data-g-amountformatter="blankAsZero:true" id="revenuImposableCalcul"><%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getRevenuImposableCalcul()%></td>
						</tr>
						<!-- Rendement de la fortune immobilière selon chiffres 300, 320 et 320c -->
						<tr id="rendementFortuneImmoCalcul_LINE">
							<td>-</td>
							<td><ct:FWLabel key="JSP_AM_RE_D_REND_FORT_IMMO_CALCUL"/></td>
							<td>Fr.</td>
							<td data-g-amountformatter="blankAsZero:true" id="rendementFortuneImmoCalcul"><%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getRendementFortuneImmoCalcul()%></td>
						</tr>
						<!-- Excédents de dépenses concernant la propriété immobilière selon chiffres 310, 330 et 330c -->
						<tr id="excedentDepensesPropImmoCalcul_LINE">
							<td>+</td>
							<td><ct:FWLabel key="JSP_AM_RE_D_EXCED_DEP_PROP_IMMO_CALCUL"/></td>
							<td>Fr.</td>
							<td data-g-amountformatter="blankAsZero:true" id="excedentDepensesPropImmoCalcul"><%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getExcedentDepensesPropImmoCalcul()%></td>
						</tr>
						<%if (!isOldRevenu) { %>
						<!-- Excédents de dépenses concernant la succession non partagée selon chiffre 390 -->
						<tr id="excedentDepensesSuccNonPartageesCalcul_LINE">
							<td>+</td>
							<td><ct:FWLabel key="JSP_AM_RE_D_EXCED_DEP_SUCC_NON_PART_CALCUL"/></td>
							<td>Fr.</td>
							<td data-g-amountformatter="blankAsZero:true" id="excedentDepensesSuccNonPartageesCalcul"><%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getExcedentDepensesSuccNonPartageesCalcul()%>
							</td>
						</tr>
						<% } %>
						<!-- Intérêts passifs selon chiffres 530, 535 -->
						<tr id="interetsPassifsCalcul_LINE">
							<td>+</td>
							<td><ct:FWLabel key="JSP_AM_RE_D_INT_PASSIFS_CALCUL"/></td>
							<td>Fr.</td>
							<td data-g-amountformatter="blankAsZero:true" id="interetsPassifsCalcul"><%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getInteretsPassifsCalcul()%>
							</td>
						</tr>
						<%if (!isOldRevenu) { %>
						<!-- Perte des exercices commerciaux selon chiffres 140*, 150*, 160*, 170* -->						
						<tr id="perteExercicesCommerciauxCalcul_LINE">
							<td>+</td>
							<td><ct:FWLabel key="JSP_AM_RE_D_PERTE_EXCERCICES_COMM_CALCUL"/></td>
							<td>Fr.</td>
							<td data-g-amountformatter="blankAsZero:true" id="perteExercicesCommerciauxCalcul"><%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getPerteExercicesCommerciauxCalcul()%>
							</td>
						</tr>
						<% } else { %>
						<tr id="perteExercicesCommerciauxCalcul_LINE">
							<td>+</td>
							<td><ct:FWLabel key="JSP_AM_RE_D_PERTE_EXCERCICES_COMM_SEUL_CALCUL"/></td>
							<td>Fr.</td>
							<td data-g-amountformatter="blankAsZero:true" id="perteExercicesCommerciauxCalcul"><%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getPerteExercicesCommerciauxCalcul()%>
							</td>
						</tr>
						<% } %>
						<!-- Perte reportée des exercices commerciaux selon chiffre 180, 180C -->
						<tr id="perteReporteeExercicesCommerciauxCalcul_LINE">
							<td>+</td>
							<td><ct:FWLabel key="JSP_AM_RE_D_PERTE_REPORTE_EXERC_COMM_CALCUL"/></td>
							<td>Fr.</td>
							<td data-g-amountformatter="blankAsZero:true" id="perteReporteeExercicesCommerciauxCalcul"><%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getPerteReporteeExercicesCommerciauxCalcul()%></td>
						</tr>
						<%if (!isOldRevenu) { %>
						<!-- Perte de liquidation 188, 188C -->
						<tr id="perteLiquidationCalcul_LINE">
							<td>+</td>
							<td><ct:FWLabel key="JSP_AM_RE_D_PERTE_LIQUIDATION_CALCUL"/></td>
							<td>Fr.</td>
							<td data-g-amountformatter="blankAsZero:true" id="perteLiquidationCalcul"><%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getPerteLiquidationCalcul()%></td>
						</tr>
						<% } %>
						<!-- Part du rendement immobilier excédant les intérêts passifs (chiffres 300, 320, 320c - 530, 535, 310, 330,330c, 390) -->
						<tr id="partRendementImmobExedantIntPassifsCalcul_LINE"> 
							<td>+</td>
							<td><ct:FWLabel key="JSP_AM_RE_D_PART_REND_IMMO_EXCED_INT_PASSIF_CALCUL"/></td>
							<td>Fr.</td>
							<td data-g-amountformatter="blankAsZero:true" id="partRendementImmobExedantIntPassifsCalcul"><%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getPartRendementImmobExedantIntPassifsCalcul()%></td>
						</tr>
						<%} %>
						<%if (!isOldRevenu) { %>
						<!-- Déduction pour couples mariés -->
						<tr id="deductionCouplesMaries_LINE">
							<td>+</td>
							<td><ct:FWLabel key="JSP_AM_RE_D_DEDUC_COUPLE_MARIE_CALCUL"/></td>
							<td>Fr.</td>
							<td data-g-amountformatter="blankAsZero:true" id="deductionCouplesMaries"><%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getDeductionCouplesMaries()%></td>
						</tr>
						<% } %>
						


							<!-- REVENU DETERMINANT PARTIE COMMUNE -->


						<!-- Titre Déductions supplémentaires -->
						<tr style="font-weight: bold" style="font-style:italic">
							<td colspan="4" align="left" style="background-color: #eeeeee"
								style="border-bottom: 1px solid black">Déductions
							supplémentaires</td>
						</tr>
						<!-- Contribuable non célibataire sans enfant à charge (5'000) -->
						<tr>
							<td>-</td>
							<% 
							String montantSansEnfantCharge = viewBean.getParametreAnnuel(IAMParametresAnnuels.CS_MONTANT_SANS_ENFANT_CHARGE, anneeHistorique);
							String montantAvecEnfantCharge = viewBean.getParametreAnnuel(IAMParametresAnnuels.CS_MONTANT_AVEC_ENFANT_CHARGE, anneeHistorique);
							%>
							<td><ct:FWLabel key="JSP_AM_RE_D_CONTRIB_NON_CELIB_SANS_ENFANT_CHARGE_CALCUL"/> (<%=JANumberFormatter.fmt(montantSansEnfantCharge,true,false,false,2)%>)</td>
							<td>Fr.</td>
							<td data-g-amountformatter="blankAsZero:true" id="deductionContribNonCelibSansEnfantChargeCalcul"><%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getDeductionContribNonCelibSansEnfantChargeCalcul()%></td>
						</tr>
						<!-- Contribuable avec enfant à charge (10'000) -->
						<tr>
							<td>-</td>
							<td><ct:FWLabel key="JSP_AM_RE_D_CONTRIB_AVEC_ENFANT_CHARGE_CALCUL"/> (<%=JANumberFormatter.fmt(montantAvecEnfantCharge,true,false,false,2)%>)</td>
							<td>Fr.</td>
							<td data-g-amountformatter="blankAsZero:true" id="deductionContribAvecEnfantChargeCalcul"><%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getDeductionContribAvecEnfantChargeCalcul()%></td>
						</tr>
						<!-- Introduction Nb Enfants -->
						<tr>
							<td>-</td>
							<%
							Double sommeEnfants = 0.00;
							try {
								sommeEnfants = Double.parseDouble(viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getNbEnfants())+(Double.parseDouble(viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getNbEnfantSuspens())/2);
							} catch (Exception ex) {
								sommeEnfants = 0.00;
							}
							%>
							<td><b><span id="nbEnfantsCalcul">
							<%=JANumberFormatter.fmt(sommeEnfants.toString(),false, false, false,1)
							%></span></b>&nbsp;<ct:FWLabel key="JSP_AM_RE_D_NB_ENFANTS_CALCUL"/></td>
							<td>Fr.</td>
							<td data-g-amountformatter="blankAsZero:true" id="deductionSelonNbreEnfantCalcul"><%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getDeductionSelonNbreEnfantCalcul()%>
							</td>
						</tr>
						<!-- 3 % fortune imposable -->
						<tr>
							<td>+</td>
							<input type="hidden" id="percentFortuneImposable" value="<%=JANumberFormatter.fmt(viewBean.getParametreAnnuel(IAMParametresAnnuels.CS_TAUX_CALCUL_FORTUNE_IMPOSABLE, anneeHistorique),false, false, false,0) %>" />
							<td><b><script type="text/javascript">document.write($("#percentFortuneImposable").val());</script></b><ct:FWLabel key="JSP_AM_RE_D_POURCENTAGE_FORTUNE_IMPOS_CALCUL"/> (Fr. <span id="fortuneImposableCalcul"><%=JANumberFormatter.fmt(viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getFortuneImposableCalcul(),true, true ,false,2) %></span>)</td>
							<td>Fr.</td>
							<td id="fortuneImposablePercentCalcul" data-g-amountformatter="blankAsZero:true"><%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getFortuneImposablePercentCalcul()%></td>
						</tr>
						<!-- Titre Revenu déterminant -->
						<tr style="font-weight: bold" style="font-style:italic">
							<td colspan="4" align="left" style="background-color: #eeeeee"
								style="border-bottom: 1px solid black"><ct:FWLabel key="JSP_AM_RE_D_REV_DET_CALCUL"/></td>
						</tr>
						<!-- Revenu déterminant -->
						<tr style="font-weight: bold">
							<td></td>
							<td><ct:FWLabel key="JSP_AM_RE_D_TOTAL_REV_DET_CALCUL"/></td>
							<td>Fr.</td>
							<td data-g-amountformatter="blankAsZero:true" id="revenuDeterminantCalcul"><%=viewBeanIsNew?"":viewBean.getRevenuHistoriqueComplex().getSimpleRevenuDeterminant().getRevenuDeterminantCalcul()%></td>
						</tr>
						<!-- Free Line -->
						<tr>
							<td colspan="4"></td>
						</tr>
					</table>
					</div>
					</div>	
					</div>
					</div>
					</div>
				</td>
				<td></td>
			</tr>
		</table>
	
	<div id="tabs">
			<ul style="font-size:13px;font-style:italic" >				
				<li id="ongletGenerique">
					<a href="#idConteneurGenericRevenu">					
					<%
					if (viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().isSourcier()) { %>
						<ct:FWLabel key="JSP_AM_RE_D_TITLE_IMPOSE_SOURCE"/>						
					<% } else { %>			
						<ct:FWLabel key="JSP_AM_RE_D_TITLE_IMPOSE_STANDARD"/>								
					<% } %>
					</a>
				</li>
				<li id="ongletStandard"><a href="#idConteneurDeclarationImpots"><ct:FWLabel key="JSP_AM_RE_D_TITLE"/></a></li>
				<li id="ongletSourciers"><a href="#idConteneurImpotsSource"><ct:FWLabel key="JSP_AM_RE_D_TITLE"/></a></li>				

				<%	if(!viewBeanIsNew && !histoRevenu){	%>
					<li id="ongletHistorique"><a href="#idConteneurHistorique">Historique</a></li>
				<%	} %>				

			</ul>

			<!-- ************************************************************************************* -->
			<!-- ONGLET INFORMATIONS GENERIQUES REVENU -->
			<div id="idConteneurGenericRevenu" class="conteneurGenericRevenu">			
			<table id="zoneGenericRevenu" border="0" width="100%" style="border:2px solid #226194;background-color: #D7E4FF">			
				<col width="20px" align="center"></col>
				<col width="260px" align="left"></col>
				<col width="240px" align="left"></col>
				<col width="160px" align="left"></col>
				<col width="*" align="left"></col>
				<!-- Empty Line -->
				<tr><td>&nbsp;</td></tr>				
				<tr>
					<!-- Année historique -->
					<td></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_ANNEEHISTORIQUE" /></td>
					<td><input disabled="disabled" type="text" data-g-integer="addSymboleMandatory:true,sizeMax:4"
						name="revenuHistoriqueComplex.simpleRevenuHistorique.anneeHistorique" id="anneeHistorique"  size="6"
						value="<%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getSimpleRevenuHistorique().getAnneeHistorique():"")%>" />
					</td>
					<!-- Fin année historique -->
					<td>Taxation liée</td>
					<td>
						<select id="taxationLiee" disabled="disabled" name="revenuHistoriqueComplex.simpleRevenuHistorique.idRevenu">
							<%
								for(int iTaxation = 0; iTaxation < viewBean.getListTaxations().size();iTaxation++){
									Revenu currentTaxation = viewBean.getListTaxations().get(iTaxation);
									Boolean taxationSelected = currentTaxation.getIdRevenu().equals(viewBean.getRevenuHistoriqueComplex().getSimpleRevenuHistorique().getIdRevenu()); 
									
									String currentTaxationLibelle = "";
									currentTaxationLibelle += currentTaxation.getAnneeTaxation();
									if(currentTaxation.isSourcier()){
										currentTaxationLibelle += " [S] ";
									}else{
										currentTaxationLibelle += " [C] ";
									}
									currentTaxationLibelle+="Revenu imposable : "+currentTaxation.getRevenuImposableCurrency();
							%>
							<option <%=(!viewBeanIsNew && taxationSelected?"selected=\"selected\"":"")%> value="<%=currentTaxation.getIdRevenu()%>"><%=currentTaxationLibelle%></option>
							<%
								}
							%>
						</select>
						<%
							String taxationLinkURL = servletContext + "/amal?userAction=amal.revenu.revenu.afficher&selectedId="+viewBean.getRevenuHistoriqueComplex().getSimpleRevenuHistorique().getIdRevenu();
							taxationLinkURL += "&contribuableId="+viewBean.getContribuable().getId();
							taxationLinkURL += "&selectedTab=0";
						%>
						<a id="taxationLieeURL" href="<%=taxationLinkURL%>"> 
						<img
							id="taxationLieeImg"
							width="18px"
							height="18px"
							src="<%=request.getContextPath()%>/images/amal/link.png" title="Taxation <%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getAnneeTaxation()%>" border="0">
						</a>
					</td>
				</tr>
				<tr><td colspan="5"><hr></td></tr>
				<tr>	
					<td></td>
					<!-- Type de revenu -->															
					<td><ct:FWLabel key="JSP_AM_RE_D_TYPE_REVENU" /></td>
					<td><ct:FWCodeSelectTag codeType="AMCONTYPE" wantBlank="false" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.typeRevenu" defaut="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getTypeRevenu()%>"/></td>
					<!-- Fin type de revenu -->					
					<!-- Code Suspendu -->
					<td><ct:FWLabel key="JSP_AM_RE_D_CODESUSPENDU" /></td>
					<td>
						<select id="codeSuspendu" disabled="disabled" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.codeSuspendu">
							<option <%=(!viewBeanIsNew && "1".equals(viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getCodeSuspendu())?"selected=\"selected\"":"")%> value="1">Oui [S]</option>
							<option <%=(!viewBeanIsNew && "2".equals(viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getCodeSuspendu())?"selected=\"selected\"":"")%> value="2">Non</option>
						</select>
					</td>
					<!-- Fin code Suspendu -->			
				</tr>
				<tr>
					<td></td>
					<!-- Type de taxation -->
					<td><ct:FWLabel key="JSP_AM_RE_D_TYPETAXATION" /></td>
					<td><ct:FWCodeSelectTag codeType="AMTYTAX" wantBlank="true" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.typeTaxation" defaut="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getTypeTaxation()%>"/></td>
					<!-- Fin type de taxation -->	
					<!-- Numéro de lot -->
					<td><ct:FWLabel key="JSP_AM_RE_D_NUMEROLOT" /></td>
					<td><input disabled="disabled" data-g-integer="sizeMax:4 " size="6"
						name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.noLotAvisTaxation" id="noLotAvisTaxation" type="text"
						value="<%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getNoLotAvisTaxation():"")%>" />
					</td>
					<!-- Fin numéro de lot -->					
				</tr>
				<tr>
					<td></td>
					<!-- Etat Civil -->	
					<td><ct:FWLabel key="JSP_AM_RE_D_ETATCIVIL" /></td>
					<td><ct:FWCodeSelectTag codeType="AMETCIV" wantBlank="false" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.etatCivil" defaut="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getEtatCivil()%>"/></td>
					<!-- Fin etat Civil -->
					<!-- Nb de Jour -->
					<td><ct:FWLabel key="JSP_AM_RE_D_NBJOUR" /></td>
					<td><input disabled="disabled" data-g-integer="sizeMax:4 " id="nbJours"
						name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.nbJours" type="text" size="6"
						value="<%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getNbJours():"")%>" />
					</td>
					<!-- Fin nb de Jour -->					
				</tr>
				<tr>
					<td></td>
					<!-- Nb Enfants -->
					<td><ct:FWLabel key="JSP_AM_RE_D_NBENFANTS" /></td>
					<td><input disabled="disabled" size="4" id="nbEnfants"
						name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.nbEnfants" type="text" data-g-integer=" "
						value="<%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getNbEnfants():"")%>" />
						&nbsp;&nbsp;Suspens <input disabled="disabled" size="4"
						name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.nbEnfantSuspens" id="nbEnfantSuspens" type="text" data-g-integer=" "
						value="<%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getNbEnfantSuspens():"")%>" /> 
					</td>
					<!-- Fin nb Enfants -->
					<!-- Code Profession -->
					<td width="150px"><ct:FWLabel key="JSP_AM_RE_D_CODEPROFESSION" />
					</td>
					<td><ct:FWCodeSelectTag codeType="AMCOPROF" wantBlank="false" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.profession" defaut="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getProfession()%>"/></td>
					<!-- Fin code Profession -->
				</tr>
				<tr>
					<!-- Montant RDU -->
					<td></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_VALEUR_RDU" /> / <ct:FWLabel key="JSP_AM_RE_D_EXISTANCE_RDU_O_N" /></td>
					<td><input disabled="disabled"
						 name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.revDetUnique" type="text"
						 data-g-amount=" " id="revDetUnique"
						 value="<%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getRevDetUnique():"")%>" />
						 <select id="revDetUniqueOuiNon" disabled="disabled" name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.revDetUniqueOuiNon">
							<option <%=(!viewBeanIsNew && viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getRevDetUniqueOuiNon()?"selected=\"selected\"":"")%> value="true">Oui</option>
							<option <%=(!viewBeanIsNew && !viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getRevDetUniqueOuiNon()?"selected=\"selected\"":"")%> value="false">Non</option>
						</select>
					</td>
					<!-- Fin Montant RDU -->
					<!-- Code Actif -->
					<td><ct:FWLabel key="JSP_AM_RE_D_CODEACTIF" /></td>
					<td>
						<select id="codeActif" disabled="disabled" name="revenuHistoriqueComplex.simpleRevenuHistorique.codeActif">
							<option <%=(!viewBeanIsNew && viewBean.getRevenuHistoriqueComplex().getSimpleRevenuHistorique().getCodeActif()?"selected=\"selected\"":"")%> value="true">Oui</option>
							<option <%=(!viewBeanIsNew && !viewBean.getRevenuHistoriqueComplex().getSimpleRevenuHistorique().getCodeActif()?"selected=\"selected\"":"")%> value="false">Non</option>
						</select>
					</td>
					<!-- Fin code Actif -->
				</tr>
				<tr>
					<!-- Date Décision -->
					<td></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_DATEDECISION" /></td>
					<td><input disabled="disabled"
						name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.dateAvisTaxation" id="dateAvisTaxation" type="text"
						data-g-calendar="mandatory:false"
						value="<%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getDateAvisTaxation():"")%>"
						 />
					</td>
					<!-- Fin Date Décision -->
					<!-- Date Traitement -->
					<td><ct:FWLabel key="JSP_AM_RE_D_DATETRAITEMENT" /></td>
					<td><input disabled="disabled"
						 name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.dateTraitement" type="text"
						 data-g-calendar="mandatory:false" id="dateTraitement"
						 value="<%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getDateTraitement():"")%>" />
					</td>
					<!-- Fin date Traitement -->									
				</tr>
				<tr>
					<!-- Année taxation -->
					<td></td>
					<td><ct:FWLabel key="JSP_AM_RE_D_ANNEETAXATION" /></td>
					<td><input disabled="disabled"
						name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.anneeTaxation" id="anneeTaxation" type="text" data-g-integer="sizeMax:4" size="6"
						value="<%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getAnneeTaxation():"")%>" />
					</td>
					<!-- Fin année taxation -->
					<!-- Date Saisie -->
					<td><ct:FWLabel key="JSP_AM_RE_D_DATESAISIE" /></td>
					<td><input disabled="disabled"
						name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenu.dateSaisie" type="text"
						data-g-calendar="mandatory:false" id="dateSaisie"
						value="<%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getDateSaisie():"")%>" />
		 			</td>
		 			<!-- Fin Date Saisie -->
				</tr>
				<!-- Empty Line -->
				<tr><td>&nbsp;</td></tr>
			</table>
			</div>

			<!-- ************************************************************************************* -->
			<!-- FIN ONGLET INFORMATIONS GENERIQUES REVENU -->


			<!-- ************************************************************************************* -->
			<!-- ONGLET INFORMATIONS FISCALES REVENU -->
			
			<div id="idConteneurDeclarationImpots" class="conteneurDeclarationImpots">
				<table id="zoneDeclarationImpots"  border="0" width="100%" style="border-collapse:collapse;border:2px solid #226194;background-color: #D7E4FF">
					<col width="10px" align="center"></col>
					<col width="67px" style="font-style: italic;font-size:11px" align="left"></col>
					<col width="240px" align="left"></col>
					<col width="20px" align="right"></col>
					<col width="100px" align="right"></col>
				
					<col width="10px" align="center"></col>
					<col width="35px" style="font-style: italic;font-size:11px" align="left"></col>
					<col width="240px" align="left"></col>
					<col width="20px" align="right"></col>
					<col width="80px" align="right"></col>
					
					<col width="10px" align="center"></col>
					<col width="67px" style="font-style: italic;font-size:11px" align="left"></col>
					<col width="240px" align="left"></col>
					<col width="20px" align="right"></col>
					<col width="100px" align="right"></col>
					<col width="10px" align="right"></col>
					
					<!-- Empty Line -->
					<tr style="height:4px;">
						<td></td>
					</tr>
					<!-- LIGNE 1 -->
					<tr>
						<!-- Perte de l'activité indépendante -->					
						<td></td>					
						<%if (!isOldRevenu) { %>
							<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_PERTEACTIVITEINDEP,anneeTaxation)%></td>
							<td><ct:FWLabel key="JSP_AM_RE_D_PERTE_ACTIV_INDEP"/></td>
							<td>Fr.</td>
							<td><input disabled="disabled" id="perteActIndep"
								name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.perteActIndep" type="text"
								data-g-amount=" "
								style="width:100px"
								value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getPerteActIndep()%>" />
							</td>
						<% } else { %>
							<td colspan="4">&nbsp;</td>
						<% } %>
						
						<!-- Revenu net provenant d'un emploi -->
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_REVENUNETEMPLOI,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_REV_NET_EMPLOI"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="revenuNetEmploi"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.revenuNetEmploi" type="text"
							data-g-amount=" "
							style="width:100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getRevenuNetEmploi()%>" />
						</td>
						
						<!-- Rendement de la fortune immobilière commerciale -->
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_RENDEMENTFORTUNEIMMOBCOMM,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_REND_FORT_IMMO_COMM"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="rendFortImmobComm"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.rendFortImmobComm" type="text"
							data-g-amount=" "
							style="width:100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getRendFortImmobComm()%>" />
						</td>
						<td>&nbsp;</td>
					</tr>
					<!-- LIGNE 2 -->
					<tr>
						<!-- Perte de l'activité agricole -->
						<td></td>
						<%if (!isOldRevenu) { %>
							<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_PERTEACTIVITEAGRIC,anneeTaxation)%></td>
							<td><ct:FWLabel key="JSP_AM_RE_D_PERTE_ACTIV_AGRI"/></td>
							<td>Fr.</td>
							<td><input disabled="disabled" id="perteActAgricole"
								name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.perteActAgricole" type="text"
								data-g-amount=" "
								style="width:100px"
								value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getPerteActAgricole()%>"  />
							</td>
						<% } else { %>
							<td colspan="4">&nbsp;</td>
						<% } %>
						
						<!-- Revenu net pour l'épouse -->
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_REVENUNETEPOUSE,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_REV_NET_EPOUSE"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="revenuNetEpouse"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.revenuNetEpouse" type="text"
							data-g-amount=" "
							style="width:100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getRevenuNetEpouse()%>" />
						</td>
						
						<!-- Excédent de dépenses propriété immobilière commerciale -->
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_EXCEDENTDEPENSESPROPIMMOCOMM,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_EXCED_DEP_PROP_IMMO_COMM"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="excedDepPropImmoComm"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.excedDepPropImmoComm" type="text"
							data-g-amount=" "
							style="width:100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getExcedDepPropImmoComm()%>" />
						</td>
						<td>&nbsp;</td>
					</tr>
					<!-- LIGNE 3 -->
					<tr>
						<!-- Perte de société -->
						<td></td>
						<%if (!isOldRevenu) { %>
							<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_PERTESOCIETE,anneeTaxation)%></td>
							<td><ct:FWLabel key="JSP_AM_RE_D_PERTE_SOCIETE"/></td>
							<td>Fr.</td>
							<td><input disabled="disabled" id="perteSociete"
								name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.perteSociete" type="text"
								data-g-amount=" "
								style="width:100px"
								value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getPerteSociete()%>" />
							</td>
						<% } else { %>
							<td colspan="4">&nbsp;</td>
						<% } %>
						
						<!-- Revenu activité indépendante -->
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_REVENUACTIVINDEP,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_REVENU_ACTIV_INDEP"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="revenuIndep"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.revenuActIndep" type="text"
							data-g-amount=" "
							style="width:100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getRevenuActIndep()%>" />
						</td>
						
						<!-- Excédent dépense concernant les successions non partagées -->
						<%if (!isOldRevenu) { %>
							<td></td>
							<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_EXCEDENTDEPSUCCNONPART,anneeTaxation)%></td>
							<td><ct:FWLabel key="JSP_AM_RE_D_EXCED_SUCC_NON_PART"/></td>
							<td>Fr.</td>
							<td><input disabled="disabled" id="excDepSuccNp"
								name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.excDepSuccNp" type="text"
								data-g-amount=" "
								style="width:100px"
								value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getExcDepSuccNp()%>" />
							</td>
						<% } else { %>
							<td>&nbsp;</td>
							<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_PERTESEXERCICESCOMM,anneeTaxation)%></td>
							<td><ct:FWLabel key="JSP_AM_RE_D_PERTE_EXERC_COMM" /></td>
							<td>Fr.</td>
							<td><input disabled="disabled" id="perteExercicesComm"
								name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.perteExercicesComm" type="text"
								data-g-amount=" "
								style="width:100px"
								value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getPerteExercicesComm()%>" /></td>
						<% } %>
						<td>&nbsp;</td>
					</tr>
					<!-- LIGNE 4 -->
					<tr>
						<!-- Perte de l'activité accessoire indépendante -->					
						<td></td>
						<%if (!isOldRevenu) { %>
							<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_PERTEACTIVITEACCESSINDEP,anneeTaxation)%></td>
							<td><ct:FWLabel key="JSP_AM_RE_D_PERTE_ACTIV_ACC_INDEP"/></td>
							<td>Fr.</td>
							<td><input disabled="disabled" id="perteActAccInd"
								name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.perteActAccInd" type="text"
								data-g-amount=" "
								style="width=100px"
								value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getPerteActAccInd()%>" />
							</td>
						<% } else { %>
							<td colspan="4">&nbsp;</td>
						<% } %>
				
						<!-- Revenu activité indépendante épouse -->
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_REVENUACTIVINDEPEPOUSE,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_REVENU_ACTIV_INDEP_EPOUSE"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="revenuIndepEpouse"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.revenuActIndepEpouse" type="text"
							data-g-amount=" "
							style="width:100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getRevenuActIndepEpouse()%>" />
						</td>
				
						<!-- Totaux des revenus nets -->
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_TOTAUXREVENUSNETS,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_TOT_REV_NET"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="totalRevenusNets"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.totalRevenusNets" type="text"
							data-g-amount=" "
							style="width=100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getTotalRevenusNets()%>" />
						</td>
						<td>&nbsp;</td>
					</tr>
					<!-- LIGNE 5 -->
					<tr>
						<!-- Perte reportée des exercices commerciaux -->					
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_PERTEREPEXCOMM,anneeTaxation)%></td>
						<td>Perte reportée des ex. commerciaux</td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="perteCommercial"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.perteCommercial" type="text"
							data-g-amount=" "
							style="width=100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getPerteCommercial()%>" />
						</td>
				
						<!-- Revenu activité agricole -->
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_REVENUACTIVITEAGRIC,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_REVENU_ACTIV_AGRI"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="revenuActAgricole"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.revenuActAgricole" type="text"
							data-g-amount=" "
							style="width:100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getRevenuActAgricole()%>" />
						</td>
				
						<!-- Intérêts passifs privés -->
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_INTERETSPASSIFSPRIVE,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_INT_PASSIFS_PRIV"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="interetsPassifsPrive"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.interetsPassifsPrive" type="text"
							data-g-amount=" "
							style="width=100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getInteretsPassifsPrive()%>" />
						</td>
						<td>&nbsp;</td>
					</tr>
					<!-- LIGNE 6 -->
					<tr>
						<!-- Perte de liquidation -->					
						<td></td>
						<%if (!isOldRevenu) { %>
							<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_PERTELIQUIDATION,anneeTaxation)%></td>
							<td>Perte de liquidation</td>
							<td>Fr.</td>
							<td><input disabled="disabled" id="perteLiquidation"
								name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.perteLiquidation" type="text"
								data-g-amount=" "
								style="width=100px"
								value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getPerteLiquidation()%>" />
							</td>
						<% } else { %>
							<td colspan="4">&nbsp;</td>
						<% } %>
				
						<!-- Revenu activité agricole épouse -->
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_REVENUACTIVITEAGRICEPOUSE,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_REVENU_ACTIV_AGRI_EPOUSE"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="revenuActAgricoleEpouse"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.revenuActAgricoleEpouse" type="text"
							data-g-amount=" "
							style="width:100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getRevenuActAgricoleEpouse()%>" />
						</td>
				
						<!-- Intérêts passifs commerciaux -->
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_INTERETSPASSIFSCOMM,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_INT_PASSIFS_COMM"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="interetsPassifsComm"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.interetsPassifsComm" type="text"
							data-g-amount=" "
							style="width=100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getInteretsPassifsComm()%>" />
						</td>
						<td>&nbsp;</td>
					</tr>
					<!-- LIGNE 7 -->
					<tr>
						<!-- Allocation de famille -->
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_ALLOCATIONFAMILLE,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_ALLOC_FAM" /></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="allocationFamiliale"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.allocationFamiliale" type="text"
							data-g-amount=" "
							style="width=100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getAllocationFamiliale()%>" />
						</td>
						
						<!-- Middle empty space -->
						<td colspan="5">&nbsp;</td>
						
						<!-- Personnes à charge ou enfants -->
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_PERSONNECHARGEENFANT,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_PERS_CHARGE_ENFANTS"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="persChargeEnf"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.persChargeEnf" type="text"
							data-g-amount=" "
							style="width=100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getPersChargeEnf()%>" />
						</td>
						<td>&nbsp;</td>
					</tr>
					<!-- LIGNE 8 -->
					<tr>
						<!-- Indemnité imposable à 90% -->
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_INDEMNITEIMPOSABLE,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_INDEMN_IMPOS"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="indemniteImposable"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.indemniteImposable" type="text"
							data-g-amount=" "
							style="width=100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getIndemniteImposable()%>" />
						</td>
						
						<!-- Middle empty space -->
						<td colspan="5">&nbsp;</td>
						
						<!-- Déductions pour les apprentis et étudiants -->
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_DEDUCAPPRENTIETUDIANT,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_DEDUC_APPR_ETUD"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="deducAppEtu"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.deducAppEtu" type="text" 
							data-g-amount=" "
							style="width=100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getDeducAppEtu()%>" />
						</td>
						<td>&nbsp;</td>
					</tr>
					<!-- LIGNE 9 -->
					<tr>
						<!-- Rendement de la fortune immobilière privée -->
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_RENDEMENTFORTUNEIMMOBPRIVE,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_REND_FORT_IMMO_PRIV"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="rendFortImmobPrive"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.rendFortImmobPrive" type="text"
							data-g-amount=" "
							style="width=100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getRendFortImmobPrive()%>" />
						</td>
						
						<!-- Middle empty space -->
						<td colspan="5">&nbsp;</td>
						
						<!-- Déduction fiscale pour couple mariés-->
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_DEDUCTIONCOUPLESMARIES,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_DEDUC_FISC_COUPLE_MARIES"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="deducCouplesMaries"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.deductionCouplesMaries" type="text"
							data-g-amount=" "
							style="width=100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getDeductionCouplesMaries()%>" />
						</td>
						<td>&nbsp;</td>
					</tr>
					<!-- LIGNE 10 -->
					<tr>
						<!-- Excédent de dépenses propriété immobilière privée -->
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_EXCEDENTDEPENSESPROPIMMOPRIVE,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_EXCED_DEP_PROP_IMMO_PRIV"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="excedDepPropImmoPriv"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.excedDepPropImmoPriv" type="text"
							data-g-amount=" "
							style="width=100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getExcedDepPropImmoPriv()%>" />
						</td>
						
						<!-- Middle empty space -->
						<td colspan="5">&nbsp;</td>
						
						<!-- Revenu imposable -->
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_REVENUIMPOSABLE,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_REV_IMPOS"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="revenuImposable"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.revenuImposable" type="text"
							data-g-amount=" "
							style="width=100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getRevenuImposable()%>" />
						</td>
						<td>&nbsp;</td>
					</tr>
					<!-- LIGNE 11 -->
					<tr>
						<!-- Start empty space -->
						<td colspan="5">&nbsp;</td>
						
						<!-- Middle empty space -->
						<td colspan="5">&nbsp;</td>
						
						<!-- Revenu taux -->
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_REVENUTAUX,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_REV_TAUX"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="revenuTaux"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.revenuTaux" type="text" 
							data-g-amount=" "
							style="width=100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getRevenuTaux()%>" />
						</td>
						<td>&nbsp;</td>
					</tr>
					<!-- LIGNE 12 -->
					<tr>
						<!-- Start empty space -->
						<td colspan="5">&nbsp;</td>
						
						<!-- Middle empty space -->
						<td colspan="5">&nbsp;</td>
						
						<!-- Fortune imposable -->	
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_FORTUNEIMPOSABLE,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_FORT_IMPOS"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="fortuneImposable"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.fortuneImposable" type="text"
							data-g-amount=" "
							style="width=100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getFortuneImposable()%>" />
						</td>
						<td>&nbsp;</td>
					</tr>
					<!-- LIGNE 13 -->
					<tr>
						<!-- Start empty space -->
						<td colspan="5">&nbsp;</td>
						
						<!-- Middle empty space -->
						<td colspan="5">&nbsp;</td>
						
						<!-- Fortune taux -->
						<td></td>
						<td><%=viewBean.getRubrique(IAMCodeSysteme.CS_RUBRIQUE_FORTUNETAUX,anneeTaxation)%></td>
						<td><ct:FWLabel key="JSP_AM_RE_D_FORT_TAUX"/></td>
						<td>Fr.</td>
						<td><input disabled="disabled" id="fortuneTaux"
							name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuContribuable.fortuneTaux" type="text" 
							data-g-amount=" "
							style="width=100px"
							value="<%=viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuContribuable().getFortuneTaux()%>" />
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
			<table border="0" id="zoneImpotsSource" width="100%" style="border:2px solid #226194; background-color: #D7E4FF">
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
									 value="<%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenu().getAnneeTaxation():"")%>" />
								</td>
							</tr>
							<!-- Revenu annuel époux -->
							<tr>
								<td><ct:FWLabel key="JSP_AM_RE_D_REVENU_ANNUEL_EPOUX"/></td>
								<td>Fr.</td>
								<td><input disabled="disabled"
									 name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuSourcier.revenuEpouxAnnuel" type="text"
									 data-g-amount=" " id="revenuEpouxAnnuel"
									 value="<%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getRevenuEpouxAnnuel():"")%>" />
								</td>
							</tr>
							<!-- Revenu mensuel époux -->
							<tr>
								<td><ct:FWLabel key="JSP_AM_RE_D_REVENU_MENSUEL_EPOUX"/></td>
								<td>Fr.</td>
								<td><input disabled="disabled"
									 name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuSourcier.revenuEpouxMensuel" type="text"
									 data-g-amount=" " id="revenuEpouxMensuel"
									 value="<%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getRevenuEpouxMensuel():"")%>" />
								</td>
							</tr>
							<!-- Revenu annuel épouse -->
							<tr>
								<td><ct:FWLabel key="JSP_AM_RE_D_REVENU_ANNUEL_EPOUSE"/></td>
								<td>Fr.</td>
								<td><input disabled="disabled"
									 name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuSourcier.revenuEpouseAnnuel" type="text"
									 data-g-amount=" " id="revenuEpouseAnnuel"
									 value="<%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getRevenuEpouseAnnuel():"")%>" />
								</td>
							</tr>
							<!-- Revenu mensuel épouse -->
							<tr>
								<td><ct:FWLabel key="JSP_AM_RE_D_REVENU_MENSUEL_EPOUSE"/></td>
								<td>Fr.</td>
								<td><input disabled="disabled"
									 name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuSourcier.revenuEpouseMensuel" type="text"
									 data-g-amount=" " id="revenuEpouseMensuel"
									 value="<%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getRevenuEpouseMensuel():"")%>" />
								</td>
							</tr>
							<tr>
								<td><ct:FWLabel key="JSP_AM_RE_D_NOMBRE_MOIS"/></td>
								<td></td>
								<td><input disabled="disabled" type="text" data-g-integer=" " size="4"
									name="revenuHistoriqueComplex.revenuFullComplex.simpleRevenuSourcier.nombreMois" id="nombreMois"
									value="<%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getNombreMois():"")%>" />
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
								<td data-g-amountformatter="blankAsZero:true" id="revenuPrisEnCompte"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getRevenuPrisEnCompte():"")%></td>
							</tr>
							<tr>
								<!-- Cotisations AVS/AI/APG -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_COTISATIONS_AVS_AI_APG"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="cotisationAvsAiApg"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getCotisationAvsAiApg():"")%></td>
							</tr>
							<tr>
								<!-- Cotisations AC -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_COTISATIONS_AC"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="cotisationAc"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getCotisationAc():"")%></td>
							</tr>
							<tr>
								<!-- Cotisations AC supplémentaires -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_COTISATIONS_AC_SUPPL"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="cotisationAcSuppl"><%=0%></td>
								<!-- viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getCotisationAcSupplementaires() -->
							</tr>
							<tr>
								<!-- Primes AANP -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_PRIMES_AANP"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="primesAANP"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getPrimesAANP():"")%></td>
							</tr>
							<tr>
								<!-- Primes LPP -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_PRIMES_LPP"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="primesLPP"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getPrimesLPP():"")%></td>
							</tr>
							<!-- Déductions assurances -->
							<tr>
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_DEDUCTIONS_ASSURANCES"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="deductionAssurances"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getDeductionAssurances():"")%></td>
							</tr>
							<tr>
								<!-- Déductions assurances enfants -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_DEDUCTIONS_ASSURANCE_ENFANTS"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="deductionAssurancesEnfant"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getDeductionAssurancesEnfant():"")%></td>
							</tr>
							<!-- Déductions assurances jeunes (16) -->
							<tr>
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_DEDUCTIONS_ASSURANCES_JEUNES"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="deductionAssurancesJeunes"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getDeductionAssurancesJeunes():"")%></td>
							</tr>
							<tr>
								<!-- Déductions enfants -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_DEDUCTIONS_ENFANTS"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="deductionEnfants"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getDeductionEnfants():"")%></td>
							</tr>
							<!-- Déductions frais d'obtention -->
							<tr>
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_DEDUCTIONS_FRAIS_OBTENTION"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="deductionFraisObtention"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getDeductionFraisObtention():"")%></td>
							</tr>
							<tr>
								<!-- Déductions double gain -->
								<td>-</td>
								<td><ct:FWLabel key="JSP_AM_RE_D_DEDUCTION_DOUBLE_GAIN"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="deductionDoubleGain"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getDeductionDoubleGain():"")%></td>
							</tr>
							<!-- Titre Revenu imposable -->
							<tr style="font-weight: bold" style="font-style:italic">
								<td colspan="4" align="left" style="background-color: #eeeeee"
									style="border-bottom: 1px solid black"><ct:FWLabel key="JSP_AM_RE_D_LIBELLE_REVENU_IMPOSABLE"/></td>
							</tr>
							<!-- Revenu Imposable -->
							<tr style="font-weight: bold">
								<td colspan="2"><ct:FWLabel key="JSP_AM_RE_D_TOTAL_REVENU_IMPOSABLE"/></td>
								<td>Fr.</td>
								<td data-g-amountformatter="blankAsZero:true" id="totalRevenuImposable"><%=(!viewBeanIsNew?viewBean.getRevenuHistoriqueComplex().getRevenuFullComplex().getSimpleRevenuSourcier().getRevenuImposable():"")%>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			</div>
			<!-- ************************************************************************************* -->
			<!-- FIN ONGLET INFORMATIONS FISCALES IMPOTS SOURCE -->

			<!-- ************************************************************************************* -->
			<!-- ONGLET HISTORIQUE -->
			<%if(!viewBeanIsNew && !histoRevenu){
				String taxationHistoriqueLinkURL = "";
			
			%>
			<div id="idConteneurHistorique" class="conteneurHistorique">
				<table border="0" id="zoneHistorique" width="100%" style="border:2px solid #226194; background-color: #D7E4FF">
					<col align="left"></col>
					<col align="center"></col>
					<col align="right"></col>
					<tr><td colspan="3">&nbsp;</td></tr>				
					<tr>
						<td></td>
						<td align="left"><b>Historique des revenus <%=anneeHistorique%></b></td>
						<td></td>
					</tr>
					<tr><td colspan="3">&nbsp;</td></tr>				
					<tr><td colspan="3">&nbsp;</td></tr>				
					<tr>
						<td></td>			
						<td>			
						<table width="100%" bgcolor="white" style="border: 1px solid #B3C4DB; font-size:12px;border-collapse:collapse;">
							<col align="center"></col>
							<col align="right"></col>
							<col align="center"></col>
							<col align="center"></col>
							<col align="center"></col>
							<col align="center"></col>
							<col align="right"></col>
							<col align="center"></col>
							<col align="center"></col>
							<tr style="font-weight: bold" style="font-style:italic;">
								<td style="background-color: #eeeeee" style="border-bottom: 1px solid black">Année historique</td>
								<td style="background-color: #eeeeee" style="border-bottom: 1px solid black">Revenu déterminant</td>
								<td style="background-color: #eeeeee" style="border-bottom: 1px solid black">Année taxation</td>
								<td style="background-color: #eeeeee" style="border-bottom: 1px solid black">Source taxation</td>
								<td style="background-color: #eeeeee" style="border-bottom: 1px solid black">Type taxation</td>
								<td style="background-color: #eeeeee" style="border-bottom: 1px solid black">Décision taxation</td>
								<td style="background-color: #eeeeee" style="border-bottom: 1px solid black">Revenu imposable</td>
								<td style="background-color: #eeeeee" style="border-bottom: 1px solid black">Etat civil</td>
								<td style="background-color: #eeeeee" style="border-bottom: 1px solid black">Nbre enfants</td>
								<td style="background-color: #eeeeee" style="border-bottom: 1px solid black">Recalcul</td>
							</tr>
							<tr class="amalRow">
								<td colspan="9">&nbsp;</td>
							</tr>
							<tr style="background-color: #B3C4DB"><td colspan="10"></td></tr>
							<%
							if(viewBean.getRevenuHistoriqueSearch().getSize()<=0){
							%>
							<tr>
								<td></td>
								<td align="left" colspan="10">Pas d'historique de revenu pour l'année <%=anneeHistorique%></td>
							</tr>
							<%
							}else{
								for(int iHistorique = 0; iHistorique<viewBean.getRevenuHistoriqueSearch().getSize();iHistorique++){
								RevenuHistorique historique = (RevenuHistorique) viewBean.getRevenuHistoriqueSearch().getSearchResults()[iHistorique];

								//taxationHistoriqueLinkURL = servletContext + "/amal?userAction=amal.revenu.revenu.afficher&selectedId="+historique.getIdRevenu();
								taxationHistoriqueLinkURL = servletContext + "/amal?userAction=amal.revenuHistorique.revenuHistorique.afficher&selectedId="+historique.getIdRevenuHistorique();
								taxationHistoriqueLinkURL += "&contribuableId="+historique.getIdContribuable();
								taxationHistoriqueLinkURL += "&selectedTab=0";
								taxationHistoriqueLinkURL += "&histoRevenu="+request.getParameter("selectedId");
								if (historique.getIsRecalcul()) {
									taxationHistoriqueLinkURL += "&isRecalcul=1";	
								} 
								
								
								
								boolean condition = (iHistorique % 2 == 0);
								String rowStyle = "";
								if (condition) {
									rowStyle = "amalRowOdd";
								} else {
									rowStyle = "amalRow";
								}

								
							%>
							<tr class="<%=rowStyle%>"
									onMouseOver="jscss('swap', this, '<%=rowStyle%>', 'amalRowHighligthed')"
									onMouseOut="jscss('swap', this, 'amalRowHighligthed', '<%=rowStyle%>')"
									onClick="window.location.href='<%=taxationHistoriqueLinkURL%>'"
									style="cursor:pointer;">
								<td><%=historique.getAnneeHistorique()%></td>
								<td><%=historique.getRevenuDeterminantCalculCurrency()%></td>
								<td><%=historique.getAnneeTaxation()%></td>
								<td><%=objSession.getCodeLibelle(historique.getTypeSource())%></td>
								<td><%=objSession.getCodeLibelle(historique.getTypeTaxation())%></td>
								<td><%=historique.getDateAvisTaxation()%></td>
								<td><%=historique.getRevenuImposableCurrency()%></td>
								<td><%=objSession.getCodeLibelle(historique.getEtatCivil())%></td>
								<td><%=historique.getNbEnfants()%></td>
								<td><%=historique.getIsRecalcul()?"Oui":"Non"%></td>
							</tr>
							<tr style="background-color: #B3C4DB"><td colspan="10"></td></tr>
							<%
								}
							} 
							%>
							<tr><td colspan="9">&nbsp;</td></tr>
						</table>
						</td>
					</tr>
					<tr><td colspan="9">&nbsp;</td></tr>
					<tr><td colspan="9">&nbsp;</td></tr>
					<tr><td colspan="9">&nbsp;</td></tr>
					<tr><td colspan="9">&nbsp;</td></tr>
				</table>
			</div>
			<%
			}
			%>			
			<!-- ************************************************************************************* -->
			<!-- FIN ONGLET HISTORIQUE -->
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
