<%-- tpl:insert page="/theme/detail.jtpl" --%>
<%@page import="ch.globaz.pegasus.business.constantes.EPCCodeAmal"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDroits"%>
<%@page import="ch.globaz.pegasus.business.services.PegasusServiceLocator"%>
<%@page import="ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul"%>
<%@page import="ch.globaz.pegasus.business.constantes.decision.DecisionTypes"%>
<%@ page language="java"  import="globaz.globall.http.*" contentType="text/html;charset=ISO-8859-1" %>
<%@ taglib uri="/WEB-INF/taglib.tld" prefix="ct" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/theme/detail/header.jspf" %>
<%-- tpl:put name="zoneInit" --%>
<%@page import="globaz.pegasus.utils.BusinessExceptionHandler"%>
<%@page import="ch.globaz.pegasus.businessimpl.utils.PCGedUtils"%>
<%@page import="globaz.jade.client.util.JadeStringUtil"%>
<%@page import="globaz.pegasus.utils.PCGestionnaireHelper"%>
<%@page import="globaz.framework.bean.FWViewBeanInterface"%>
<%@page import="globaz.pyxis.db.adressecourrier.TIPays"%>
<%@page import="globaz.pegasus.vb.decision.PCDecisionApresCalculViewBean"%>
<%@page import="globaz.jade.admin.user.bean.JadeUser"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCActions"%>
<%@page import="ch.globaz.pyxis.business.model.PersonneSimpleModel"%>
<%@page import="ch.globaz.pyxis.business.model.TiersSimpleModel"%>
<%@page import="globaz.prestation.interfaces.tiers.PRTiersHelper"%>
<%@page import="globaz.pegasus.utils.PCUserHelper"%>
<%@page import="ch.globaz.pegasus.business.models.decision.SimpleAnnexesDecision" %>
<%@page import="ch.globaz.pegasus.business.services.models.home.HomeService"%>
<%@page import="ch.globaz.pegasus.business.models.decision.CopiesDecision" %>
<%@page import="ch.globaz.pyxis.business.service.PersonneEtendueService"%>
<%@page import="ch.globaz.pyxis.business.service.AdministrationService"%>
<%@page import="ch.globaz.pyxis.business.service.AdresseService"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDemandes"%>
<%@page import="ch.globaz.pegasus.business.constantes.IPCDecision"%>
<%@page import="ch.globaz.pegasus.business.constantes.EPCProperties"%>
<%@page import="ch.globaz.pegasus.business.constantes.decision.DecisionTypes"%>
<%@page import="globaz.jade.log.business.JadeBusinessMessageLevels"%>
<%@page import="java.text.MessageFormat"%>
<%@ page import="globaz.globall.db.BConstants" %>

<script type="text/javascript" src="<%=servletContext%>/scripts/nss.js"></script>
<link rel="stylesheet" type="text/css" href="<%=servletContext%><%=(mainServletPath+"Root")%>/css/decision/detail.css"/>
<%@ taglib uri="/WEB-INF/nss.tld" prefix="nss" %>
<%--  ********************************** Paramétrage global de la page ******************************* --%>
<%
	//Les labels de cette page commence par le préfix "JSP_PC_DECALCUL_D"
	idEcran="PPC0081";
	
	//Labels divers
	String btnAddAnnexes = objSession.getLabel("JSP_PC_DECALCUL_BTNADDANNEXE");//bouton annexes
	String preValidLibelle = objSession.getLabel("JSP_PC_DECALCUL_D_BTN_PREVALIDER");//bouton preValid
	String deValidLibelle = objSession.getLabel("JSP_PC_DECALCUL_D_BTN_DEVALIDER");//bouton preValid
	String lblBtnConfirmer = objSession.getLabel("JSP_PC_DECALCUL_D_MESSAGE_DEVALIDER_CONFIRM");
	String lblBtnAnnuler = objSession.getLabel("JSP_PC_DECALCUL_D_MESSAGE_DEVALIDER_CANCEL");
	String alertEmptyString = objSession.getLabel("JSP_PC_DECALCUL_ERRORMSG_ANNEXE");
	String titleErroxBox = objSession.getLabel("JSP_GLOBAL_ERROR_BOX_TITLE");//titre box erreur
	//Message erreur si saisie annexe vide

	//viewbean
	PCDecisionApresCalculViewBean viewBean = (PCDecisionApresCalculViewBean) session.getAttribute("viewBean");
	//error PopUp
	autoShowErrorPopup = false;
	//Paramétrage des boutons
	bButtonDelete = false;
	bButtonUpdate = !viewBean.isValider();
	bButtonValidate = !viewBean.isValider();
	bButtonCancel = !viewBean.isValider();
	boolean viewBeanIsNew="add".equals(request.getParameter("_method"));
	
%>
<!--  ********************************** JS CSS******************************* -->

<%@ include file="/theme/detail/javascripts.jspf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored ="false" %>
<% String rootPath = servletContext+(mainServletPath+"Root");%>
<link rel="stylesheet" type="text/css" href="<%=servletContext%>/theme/widget.css"/>
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/dataTableStyle.css"/>
<link rel="stylesheet" type="text/css" href="<%=rootPath%>/css/saisieStyle.css"/>
<script type="text/javascript" src="<%=servletContext%>/scripts/widget/globazwidget.js"></script>
<script type="text/javascript" src="<%=rootPath%>/scripts/pegasusErrorsUtil.js"></script>  



<!--  ********************************** Paramétrage du menu ******************************* -->
<ct:menuChange displayId="menu" menuId="pegasus-menuprincipal" showTab="options"/>
<ct:menuChange displayId="options" menuId="pegasus-optionsdecision">
	<%if (!viewBean.isDecisionReadyForValidation()) {%>
		<ct:menuActivateNode active="no" nodeId="VALIDATIONS"/>
	<%} else {%>
		<ct:menuActivateNode active="yes" nodeId="VALIDATIONS"/>
	<%}%> 
	<%if (viewBean.hasDecompte()) {%>
		<ct:menuActivateNode active="yes" nodeId="DECOMPTE"/>  
		<ct:menuActivateNode active="yes" nodeId="DETTE_EN_COMPTAT"/>
		<ct:menuActivateNode active="yes" nodeId="CREANCE_ACCORDEE"/>
	<%} else {%>
		<ct:menuActivateNode active="no" nodeId="DECOMPTE"/>  
		<ct:menuActivateNode active="no" nodeId="DETTE_EN_COMPTAT"/>
		<ct:menuActivateNode active="no" nodeId="CREANCE_ACCORDEE"/>
	<%}%> 
	<ct:menuActivateNode active="yes" nodeId="IMPRESSIONS"/>
	<ct:menuSetAllParams key="idVersionDroit" value="<%=viewBean.getIdVersionDroit()%>"/>
	<ct:menuSetAllParams key="idDroit" value="<%=viewBean.getIdDroit()%>"/>
	<ct:menuSetAllParams key="noVersion" value="<%=viewBean.getNoVersion()%>"/>
	<ct:menuSetAllParams key="idDemandePc" value="<%=viewBean.getIdDemandePc()%>"/>
	<ct:menuSetAllParams key="idDecision" value="<%=viewBean.getIdDecision() %>"/>
	<ct:menuSetAllParams key="idDecisionDac" value="<%=viewBean.getIddac() %>"/>
	
</ct:menuChange>
<!--  ********************************** Script propre à la page ******************************* -->
<script type="text/javascript" src="<%=rootPath%>/scripts/decision/detail_DAC.js"></script>
<script language="JavaScript">
var servletContext = "<%=servletContext%>";
var errorMsgAnnexe = "<%=alertEmptyString%>";
//Main url est action sur les pages
var MAIN_URL="<%=formAction%>";
var ACTION_DECISION="<%=IPCActions.ACTION_DECISION%>";
var ACTION_DECISION_APRES_CALCUL = "<%= IPCActions.ACTION_DECISION_DETAIL_AP_CALCUL%>";
var ACTION_DECISION_PREVALIDER = "<%= IPCActions.ACTION_DECISION_AC_PREVALIDATION%>";
var ACTION_DECISION_DEVALIDE = "<%= IPCActions.ACTION_DECISION_DEVALIDER%>";
var actionMethod;
var userAction;
//Définit si la decision a une attestation billag auto (vdroit=1)
var hasBillagAuto = <%= viewBean.hasBillagAuto()%>;
var remarqueNormal = "<%= viewBean.getTextRemarqueNormal()%>";
var remarqueProvisoire = "<%= viewBean.getTextRemarqueProvisoire()%>";
// variables pour l'objet notation navigator
var currentId = <%= viewBean.getIdDecision()%>;
var actionNavigator = "pegasus?userAction=pegasus.decision.decisionApresCalcul.afficher&idDroit=<%= viewBean.getIdDroit()%>&idVersionDroit=<%= viewBean.getIdVersionDroit()%>&noVersion=<%=viewBean.getNoVersion()%>&idDecision=";
var lotDecisions = "<%= viewBean.getLotDecision() %>";
var lotPagination = lotDecisions.split(",");

//Libelle Billag a afficher automatiquement
<%
String prop = objSession.getApplication().getProperty(IPCDecision.DESTINATAIRE_REDEVANCE);
String libelle = objSession.getLabel(IPCDecision.BILLAG_ANNEXES_STRING);
String redevanceTexte = MessageFormat.format(libelle, prop);
String redevanceTexteNonCoche = objSession.getLabel("JSP_PC_DECALCUL_D_PHRASE_BILLAG_NC");
redevanceTexteNonCoche = MessageFormat.format(redevanceTexteNonCoche, prop);
String redevanceTexteCoche = objSession.getLabel("JSP_PC_DECALCUL_D_PHRASE_BILLAG_C");
redevanceTexteCoche = MessageFormat.format(redevanceTexteCoche, prop);
%>
var billagAutoTexte = "<%= redevanceTexte%>";
var csAutoBillag = <%= IPCDecision.ANNEXE_BILLAG_AUTO %>;
var csTexteLibre =<%= IPCDecision.ANNEXE_COPIE_MANUEL %>;
var isMostRecentDecision = <%= viewBean.isMostRecentDecision()%>;
//Set cs code fichier externe js
var BILLAG_PROFORMA_CS = "<%= IPCDecision.CS_GENRE_PROFORMA %>";

function add() {}

//update
function upd() {
	$('.btnDelete').attr('src','<%=servletContext%>/images/moins.png');
	$('.btnDelete').click(function () {
		$(this).parent().parent().remove();	
		copiesChanged = 1;
		$('#copiesIsChanged').attr('value','1');
	});
	
	dealCheckBoxOpiesZone();
}

// Validation de la decision, retourn le booleen et l'url
function validateDecision(){
	initValidate();
	state = true;
	userAction.value=ACTION_DECISION_PREVALIDER;
	return state;
}

function devalidateDecision(){
	if(confirm("<ct:FWLabel key='JSP_PC_DECALCUL_D_MESSAGE_DEVALIDER'/>")){
	state = true;
		userAction.value=ACTION_DECISION_DEVALIDE;
	} else {
		state=false;
	}
	return state;
}

//init
function init(){};

$(function () {
	setTimeout(function () {
		actionMethod=$('[name=_method]',document.forms[0]).val();
		userAction=$('[name=userAction]',document.forms[0])[0];
	
		//gestion des erreurs
		var errors = <%= request.getParameter("decisionErrorMsg") %>;
		var maxlevel = pegasusErrorsUtils.maxLevel(errors);
		if(maxlevel === 3) {
			pegasusErrorsUtils.dealErrors(errors,"<%= titleErroxBox %>");
		} else if(maxlevel === 2) {
			showPlausiWarningDialog(pegasusErrorsUtils.getMessages(errors));
		}
		
		//Varaiables
		initVariables();
		//init zone annexe
		initAnnexeZone();
		//init zone billag --> js externe
		initBillagZone();
		//init zone widget adresse --> js externe
		initZoneWidgetAdresse();
		//init zone widget copies
		initZoneWidgetCopies();
		//Init bouton prevalid
		setPreValidBouton(<%= viewBean.isPreValider()%>,"<%= preValidLibelle %>");
		//Set bouton update, on cache si valider
		setUpdateBouton(<%= viewBean.isValider()%>);
		//Init bouton devalid
		setDeValidBouton(<%= viewBean.isDevalidable()%>,"<%= deValidLibelle %>","<%= lblBtnConfirmer %>","<%= lblBtnAnnuler%>");
		//gestion liens conjoints
		setConjointLink(<%= viewBean.getIdDecisionConjoint()%>,actionNavigator);

		initZoneDecisoinProvisoire();
		
		},500)
	});

function showWarnings(msg) {
	var warningObj = new Object();
	warningObj.text = msg; 
	showModalDialog('<%=servletContext%>/warningModalDlg.jsp',warningObj,'dialogHeight:20;dialogWidth:25;status:no;resizable:no');
}	


function cancel() {
	userAction.value=ACTION_DECISION_APRES_CALCUL+".afficher";
}  

function validate() {
	initValidate();
	state = true;
	userAction.value=ACTION_DECISION_APRES_CALCUL+".modifier";
	//userAction.value=ACTION_DECISION_PREVALIDER;
	return state;
}    


function del() {
    if (window.confirm("<ct:FWLabel key='JSP_DELETE_MESSAGE_INFO'/>")){
    	userAction.value=ACTION_DECISION+".supprimer";
        document.forms[0].submit();
    }
}

function getDocumentUrl(){
	return "<%=formAction%>?userAction=pegasus.decision.decisionProcess.executer&idDecision=<%= viewBean.getId()%>&typeDecision=<%=DecisionTypes.DECISION_APRES_CALCUL%>";
}

// Ouvre une fenêtre window pour afficher le detail du plan de calcul
function goToDetailPCAL(idPcal){
	var userAction = "<%=formAction%>?userAction=pegasus.pcaccordee.planCalcul.afficher";
	window.open(userAction+"&idPcal="+idPcal+"&idBenef=<%=viewBean.getDecisionApresCalcul().getDecisionHeader().getPersonneEtendue().getTiers().getIdTiers()%>");
}

jsManager.add(function(){
	var $btnDisplay = $(".btnDisplayPCAL");
	$btnDisplay.click(function (event) {
		event.preventDefault();
		goToDetailPCAL(this.id.split("_")[1]);
	}); 
});

//Ouverture de la GED dans un nouvel onglet
var openGedWindow = function (url){
	window.open(url);
};
</script>
<style>



</style>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart.jspf" %>
	<span class="postItIcon" data-g-note="idExterne:<%=viewBean.getDecisionApresCalcul().getDecisionHeader().getSimpleDecisionHeader().getIdDecisionHeader()%>, 
					tableSource: PCDECHEA"></span>
			<%-- tpl:put name="zoneTitle" --%><ct:FWLabel key="JSP_PC_DECALCUL_D_TITRE"/><%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyStart2.jspf" %>
<%-- tpl:put name="zoneMain" --%>
	<TR>
		<TD colspan="6" align="center">
		
			<div id="zoneInfo">
				<div id="ligneRequerant">
					<span class="label"><ct:FWLabel key="JSP_PC_DECALCUL_D_REQ"/></span>
						<%=viewBean.getTiersInfosAsString(IPCDroits.CS_ROLE_FAMILLE_REQUERANT) %>
					<a class="lienTiers external_link" href="./pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%=viewBean.getIdTierRequerant()%>">	
						<ct:FWLabel key="JSP_PC_DECALCUL_D_TIERS"/>
					</a>
					
				</div>	
				
				
				<div id="ligneBeneficiaire">
					<span class="label"><ct:FWLabel key="JSP_PC_DECALCUL_D_BEN"/></span>
					<%=viewBean.getTiersInfosAsString(IPCDroits.CS_ROLE_FAMILLE_CONJOINT) %>
					<a class="lienTiers external_link"  href="./pyxis?userAction=pyxis.tiers.tiers.afficher&selectedId=<%=viewBean.getIdTierBeneficiaire()%>">	
						<ct:FWLabel key="JSP_PC_DECALCUL_D_TIERS"/>
					</a>
					<span id="gedLink"> </span>
					<span>
						<a  href="#" onClick="openGedWindow('<%= PCGedUtils.generateAndReturnGEDUrl(viewBean.getNoAvs(), viewBean.getIdTierRequerant()) %>')">
							<ct:FWLabel key="JSP_PC_GED_LINK_LABEL"/>
						</a>
					</span>
				</div>	
				<div id="zoneLienDecConjoint">
					<a href=""><img id="navigateToConjoint" src="<%=servletContext%>/images/loupe.gif"/><ct:FWLabel key="JSP_PC_DECALCUL_D_LIEN"/></a>
				</div>
				
				
				<!--  adresse de courrier label et widget -->
				<!--  adresse de courrier label et widget -->
				<div id="adresseZone">
					<div id="blocAdrC">
						<span id="labelAdresse" class="label" >
							<ct:FWLabel key="JSP_PC_DECSUPP_D_ADRCOUR"/>
							<button id="buttonAdresse">
						    	...
						    </button>
						</span>
					    
					    	<div id="descAdresse">
						    	<pre><span class="detailAdresseTiersC"><%= viewBean.getAdresseCourrier() %></span></pre>
		       					<input type="hidden" class="idTiersCourrier"  name="decisionApresCalcul.decisionHeader.simpleDecisionHeader.idTiersCourrier" value="<%=viewBean.getDecisionApresCalcul().getDecisionHeader().getSimpleDecisionHeader().getIdTiersCourrier()%>"/>
								<ct:widget id='<%="tiersCourrierWidget1"%>' name='<%="tiersCourrierWidget1"%>' styleClass=" selecteurHome">
									<ct:widgetService methodName="findAdresse" className="<%=AdresseService.class.getName()%>">
										<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_DECALCUL_W_TIERS_NOM"/>								
										<ct:widgetCriteria criteria="forDesignation2Like" label="JSP_PC_DECALCUL_W_TIERS_PRENOM"/>
										<ct:widgetCriteria criteria="forLocaliteUpperLike" label="JSP_PC_DECALCUL_W_TIERS_LOCALITE"/>									
										<ct:widgetCriteria criteria="forNpaLike" label="JSP_PC_DECALCUL_W_TIERS_NPA"/>								
										<ct:widgetCriteria criteria="forRueLike" label="JSP_PC_DECALCUL_W_TIERS_RUE"/>								
										<ct:widgetCriteria criteria="forNumero" label="JSP_PC_DECALCUL_W_TIERS_NUMERO"/>								
									
										
										<ct:widgetLineFormatter format="#{tiers.tiers.designation1} #{tiers.tiers.designation2} #{localite.localite} #{localite.numPostal} #{adresse.rue} #{adresse.noRue}"/>
										<ct:widgetJSReturnFunction>
											<script type="text/javascript">
												function(element){
													$('.idTiersCourrier').val($(element).attr('tiers.tiers.idTiers'));
													$('.idTiersCourrier').trigger('change');
													this.value=$(element).attr('tiers.tiers.designation1')+' '+$(element).attr('tiers.tiers.designation2')+' '+$(element).attr('localite.localite')+' '+$(element).attr('localite.numPostal')+' '+$(element).attr('adresse.rue')+' '+$(element).attr('adresse.noRue');
													hideWidgetAdresse();
													buildLibForAdresse(element);
													}
											</script>										
										</ct:widgetJSReturnFunction>
									</ct:widgetService>
								</ct:widget>
						   		
						    </div>
					</div>
					<!-- adresse de paiement -->
					<div id="blocAdrP">
						<span id="labelAdresse" class="label" ><ct:FWLabel key="JSP_PC_DECSUPP_D_ADRESSE_PAIEMENT"/></span>
					    <div id="blocAdresse">
					    	<div id="descAdresse">
						    	<pre><span class="detailAdresseTiersP"><%= viewBean.getAdressePaiement() %></span></pre>
						    </div>
					    </div>
					</div>
				</div>
				
				<div id="ligneDemande">	
					<span class="label"><ct:FWLabel key="JSP_PC_DECALCUL_D_NO_DROIT"/></span>
					<span id="nodroit"><%=viewBean.getIdDroit() %></span>
					
					<span class="label" id="lblnoversion"><ct:FWLabel key="JSP_PC_DECALCUL_D_NO_VERDRO"/></span>
					<span id="noversion"><%=viewBean.getNoVersion() %></span>
				
				</div>
				
				
				<div id="ligneDecision">
					<span class="label"><ct:FWLabel key="JSP_PC_DECALCUL_D_DATE_DECISION"/></span>
					
					<span id="valDecision"> <ct:inputText notation="data-g-calendar='mandatory: true'" name="decisionApresCalcul.decisionHeader.simpleDecisionHeader.dateDecision"/> </span>
				
					<span class="label" id="lblNoDecision"><ct:FWLabel key="JSP_PC_DECALCUL_D_NO_DECISION"/></span>
					<span id="valDecision"><%= viewBean.getDecisionApresCalcul().getDecisionHeader().getSimpleDecisionHeader().getNoDecision()%></span>
				</div>
					
				<div id="ligneDecisionCompl">
					<span class="label" id="lblEtat"><ct:FWLabel key="JSP_PC_DECALCUL_D_ETAT"/></span>
					<span id="valEtat"><%=objSession.getCodeLibelle(viewBean.getDecisionApresCalcul().getDecisionHeader().getSimpleDecisionHeader().getCsEtatDecision())%></span>
					
					<span class="label" id="lblGenreDec"><ct:FWLabel key="JSP_PC_DECALCUL_D_GENREDEC"/></span>
					<ct:FWCodeSelectTag codeType="PCGENDEC" name="csGenreDecision" wantBlank="false" defaut="<%= viewBean.getDecisionApresCalcul().getDecisionHeader().getSimpleDecisionHeader().getCsGenreDecision()%>" />
				</div>
				<div id="lignePersonneRef">
						<span class="label"><ct:FWLabel key="JSP_PC_DECALCUL_D_PERSONEREF"/></span>
						<span id="valResponsableData"><ct:FWListSelectTag data="<%=PCGestionnaireHelper.getResponsableData(objSession)%>" defaut="<%=viewBean.getGestionnaire()%>" name="decisionApresCalcul.decisionHeader.simpleDecisionHeader.preparationPar"/></span>

						<span class="label" id="lblDecisionProv"><ct:FWLabel key="JSP_PC_DECALCUL_D_DECISION_PROV"/></span>
						<span id="valDecisionProvisoire">
							<input type="radio" id="radioProvOui" <%=viewBean.isDecisoinProvisoire()?"checked":"" %> name="decisionProvisoire" value="true"/><ct:FWLabel key="JSP_PC_DECALCUL_D_DECISION_PROV_OUI"/>
							<input type="radio" id="radioProvNon" <%=viewBean.isDecisoinProvisoire()?"":"checked" %> name="decisionProvisoire" value="false"/><ct:FWLabel key="JSP_PC_DECALCUL_D_DECISION_PROV_NON"/>
						</span>
				</div>
			</div>
			
			<!--  Bloc 2, Periodes et pc -->
			<div id="pcInfo">
				
				<span id="lblPeriode" class="label2"><%= viewBean.getValiditeInfos() %></span>
				
				<span id="lblremarque" class="label2"><ct:FWLabel key="JSP_PC_DECALCUL_D_REMARQUE"></ct:FWLabel></span>
				<div id="boxRemarques">
					<textarea id="textRemarque" data-g-string="sizeMax:1024" rows="3" cols="90" name="decisionApresCalcul.simpleDecisionApresCalcul.introduction"><%= viewBean.getDecisionApresCalcul().getSimpleDecisionApresCalcul().getIntroduction() %></textarea>
				</div>
			<!--  jours d'appoint -->	
			<% if (EPCProperties.GESTION_JOURS_APPOINTS.getBooleanValue()){ %>
    			<div id="zoneJA">
    				<span class="titre"><ct:FWLabel key="JSP_PC_DECALCUL_D_TITRE_JA"/></span>
    				<span class=value"><%= viewBean.getJoursAppoint() %></span>
    			</div><br/>
			<%} %>
			
			<% if (viewBean.getUseAllocationNoel()){ %>
    			<div id="zoneJA">
    				<span class="titre"><ct:FWLabel key="JSP_PC_D_PC_ACCORDEE_TITRE_ALLOCATION_NODEL"/></span>
   					<% if (viewBean.getMontantAllocationNoel() != null){ %>
						<label><ct:FWLabel key="JSP_PC_D_ALLOCATION_NODEL_MONTANT"/></label><span class="value"><%= viewBean.getMontantAllocationNoel()%></span>
			    		<label><ct:FWLabel key="JSP_PC_D_ALLOCATION_NODEL_NB_PERSONNE"/></label><span class="value"><%= viewBean.getNbPersonneInAllacationNoel()%></span>
			    		<label><ct:FWLabel key="JSP_PC_D_ALLOCATION_NODEL_MONTANT_TOTAL"/></label><span class="value"><%= viewBean.getMontantTotalAllocationNoel()%></span>
					<%} else {%>
						<ct:FWLabel key="JSP_PC_D_PC_ACCORDEE_NOAN"/>
					<%} %>
    			</div>
    			<br/>
			<%} %>
			
				<!--  pca accordees -->
				<span class="label2"><ct:FWLabel key="JSP_PC_DECALCUL_D_PCACCORDES"/></span>
				<table id="pcATable">
					<tr>
						<th><ct:FWLabel key="JPS_PC_DECALCUL_D_BENEFICIAIRE"/></th>
						<th><ct:FWLabel key="JPS_PC_DECALCUL_D_TYPE"/></th>
						<th><ct:FWLabel key="JPS_PC_DECALCUL_D_GENRE"/></th>
						<th><ct:FWLabel key="JPS_PC_DECALCUL_D_MONNAIE"/></th>
						<th><ct:FWLabel key="JPS_PC_DECALCUL_D_MONTANT"/></th>
						<th><ct:FWLabel key="JSP_PC_DECALCUL_D_CALCUL"/></th>
					</tr>
					<tr>
						<td id="tdBenef"><%= PCUserHelper.getDetailAssure(objSession,viewBean.getPersonneBeneficiaire()) %></td>
						<td><%= objSession.getCodeLibelle(viewBean.getDecisionApresCalcul().getPcAccordee().getSimplePCAccordee().getCsTypePC()) %></td>
						<td><%= objSession.getCodeLibelle(viewBean.getDecisionApresCalcul().getPcAccordee().getSimplePCAccordee().getCsGenrePC()) %></td>
						<td><%= viewBean.getMonnaie() %></td>
						<%
							SimplePlanDeCalcul simplePlanDeCalcul = viewBean.getPlanDeCalculeRetenu();
						%>
						<td data-g-amountformatter=" "><%= viewBean.getPCAResultState() %></td>
						
							<td class="toPCALLink" ><img src="<%= servletContext%>/images/calcule.png" 
								     class="btnDisplayPCAL"
								     id="btnDisplayPCAL_<%=simplePlanDeCalcul.getIdPlanDeCalcul()%>" />
							</td>
						
					</tr>
				</table>
				<!--  Gestion personnes comprises -->
				<%if(viewBean.hasPersonnesComprises()) {
					%>
					<span class="label2"><ct:FWLabel key="JSP_PC_DECALCUL_D_PERCOMPCALCUL"/></span>
					<% 
					if(viewBean.getConjointCompris().length()>0){
					%>	
						<span style="float:left">
							<ct:FWLabel key="JSP_PC_DECALCUL_DE_CONJOINT_COMPRIS"/><span><%="  "+viewBean.getConjointCompris()%></span>
						</span><br />
					<% 
					}
					if(viewBean.getEnfantsCompris().length()>0){
					%>
						<span style="float:left">
							<ct:FWLabel key="JSP_PC_DECALCUL_DE_ENFANTS_COMPRIS"/><span><%=" "+viewBean.getEnfantsCompris()%></span>
						</span><br />
					<% 	
					}
				}%>
				<br />
				<!--  Gestion bloc amal -->
				
	
				
				
				<%if(Boolean.valueOf(objSession.getApplication().getProperty(EPCProperties.CHECK_AMAL_FOR_DECISION_ENABLE.getProperty()))){%>
					<div style="text-align:left;margin-top: 1em">
						<span class="label2"><ct:FWLabel key="JSP_PC_DECALCUL_D_REDUCPRIME"/></span>
							
							<div>
								<label><ct:FWLabel key="JSP_PC_DECALCUL_D_AMAL_TYPEREDUC"/></label>
								<span>
									<ct:select id="decisionApresCalcul.simpleDecisionApresCalcul.codeAmal"
											   name="decisionApresCalcul.simpleDecisionApresCalcul.codeAmal" 
											   wantBlank="true" 
											   notation="data-g-commutator=\"condition:('ADJ'.indexOf($(this).val()) !== -1),
							                            actionTrue:¦show('#dateAmal')¦,
							                            actionFalse:¦clear('#dateAmal'),hide('#dateAmal')¦\""
											   >
											 <%for(EPCCodeAmal val : EPCCodeAmal.values()) {
											 	if(!val.equals(EPCCodeAmal.CODE_STANDARD)){
											 	%>
													<ct:option id="<%=val.getProperty()%>" value="<%=val.getProperty()%>"  label="<%=val.getProperty()%>"/>
												<%}
											}%>
									</ct:select>
								</span> 
								<span id="dateAmal">
									<label><ct:FWLabel key="JSP_PC_DECALCUL_D_AMAL_DATE"/></label>
									<ct:inputText notation="data-g-calendar='mandatory:true'" name="decisionApresCalcul.simpleDecisionApresCalcul.dateDecisionAmal"/>
								</span>
							</div>
						
						<div id="text_amal_title" class="label2"><ct:FWLabel key="JSP_PC_DECALCUL_D_AMAL_TITRE"/></div>
						<div><%= viewBean.getBlocReductionAmal() %></div>
					</div>
				<%}%>
				
				<%//}%>	
				
			</div>
			
			<!--  zone 3, Remarques annexes et copies -->
			<div id="annexesInfo">
				<div id="zoneRemarques">
					<span class="label3">
						<input type="checkbox" id="ajouteEtremplace" name="ajouteEtremplace" <%=viewBean.getDecisionApresCalcul().getSimpleDecisionApresCalcul().getAnnuleEtRemplacePrec()?"checked='checked'":""%> /><ct:FWLabel key="JSP_PC_DECALCUL_D_AJOUTER_ANNULE"/>
						<input name="decisionApresCalcul.simpleDecisionApresCalcul.annuleEtRemplacePrec" type="hidden" id="hidChkannuleEtRemplacePrec" value="<%=viewBean.getDecisionApresCalcul().getSimpleDecisionApresCalcul().getAnnuleEtRemplacePrec()%>" />
						
					</span>
					<span id="lblRemarques"><ct:FWLabel key="JSP_PC_DECALCUL_D_REMARQUE_GEN"/></span>
					<textarea data-g-string="sizeMax:1024" id="txtRemarques" rows="5" cols="60" name="decisionApresCalcul.simpleDecisionApresCalcul.remarqueGenerale"><%= viewBean.getDecisionApresCalcul().getSimpleDecisionApresCalcul().getRemarqueGenerale() %></textarea>
				
					<span class="label3">
						<input type="checkbox" id="diminutionPc" name="diminutionPc" <%=viewBean.getDecisionApresCalcul().getSimpleDecisionApresCalcul().getDiminutionPc()?"checked='checked'":""%> /><ct:FWLabel key="JSP_PC_DECALCUL_D_AJOUTER_DIMINUTION"/>
						<input name="decisionApresCalcul.simpleDecisionApresCalcul.diminutionPc" type="hidden" id="hidChkDiminutionPc" value="<%=viewBean.getDecisionApresCalcul().getSimpleDecisionApresCalcul().getDiminutionPc() %>" />
					</span>
					<span class="label3">
						<input type="checkbox" id="allocNonActif" name="allocNonActif" <%= viewBean.getDecisionApresCalcul().getSimpleDecisionApresCalcul().getAllocNonActif()?"checked='checked'":""%> /><ct:FWLabel key="JSP_PC_DECALCUL_D_AJOUTER_ALLOCATION"/>
						<input name="decisionApresCalcul.simpleDecisionApresCalcul.allocNonActif" type="hidden" id="hidChkAllocNonActif" value="<%=viewBean.getDecisionApresCalcul().getSimpleDecisionApresCalcul().getAllocNonActif() %>" />
						
					</span>
				</div>
				<div id="zoneAnnexes">
				
				   	<div id="zoneTArea2">
						<span id="lblannexes"><ct:FWLabel key="JSP_PC_DECALCUL_D_ANNEXES"/></span>
						<select id="listeAnnexes" size="6">
							<%
							for(SimpleAnnexesDecision annexes:viewBean.getListeAnnexes())
							{
							%>
								<option value="<%=annexes.getCsType()%>"><%=annexes.getValeur() %></option>
							<%
							}
							%>
						</select>
						<div id="buttonAnnexesZone">
							<button id="btn_up" class="class="ui-state-default ui-corner-all">
								<span  class="ui-icon ui-icon-circle-triangle-n"></span>
							</button>
							<button id="btn_down" class="class="ui-state-default ui-corner-all">
								<span  class="ui-icon ui-icon-circle-triangle-s"></span>
							</button>
						</div>
					</div>
					<input type="hidden" name="listeAnnexesString" id="listeAnnexesString" value=""/>
					<input type="hidden" name="annexesIsChanged" id="annexesIsChanged" value="0"/>
					<input id="btnSupprimer" type="button" value="<ct:FWLabel key="JSP_PC_BOUTON_DEL"/>"/><br /><br />
					<input id="chmAjouter" type="text" />
					<input id="btnAjouter" type="button" value="<%=btnAddAnnexes%>">
						
				</div>
				
				
			</div>
			
			<!--  bloc 4 Billag -->
			<c:if test="${viewBean.isDecisionPasEnRefus}">
				
				<div id="zoneBillag">	
					<input type="checkbox" id="billagChk" name="decisionApresCalcul.simpleDecisionApresCalcul.billagSa" <%=viewBean.hasBillagAuto()?"checked='checked'":""%>/>
					<label class="label4" id="lblBillag" ><%=redevanceTexte%></label><br />
					<div id="phraseCaseNonCoche"><%=redevanceTexteNonCoche%></div>
					<div id="phraseCaseCoche"><%=redevanceTexteCoche%></div>
				</div>
			</c:if>
			
			
			<!-- bloc 5 copies -->
			<div id="zoneCopies">
				<p style="float:left"><b><ct:FWLabel key="JSP_PC_DECALCUL_D_COPIES"/></b></p>
				<table id="tableCopies">
				<tr>
					<th><ct:FWLabel key="JSP_PC_DECALCUL_D_DESTINATAIRE"/></th>
					
					<th><ct:FWLabel key="JSP_PC_DECALCUL_D_PAGEGARDE"/></th>
					<th><ct:FWLabel key="JSP_PC_DECALCUL_D_LETTREBASE"/></th>
					<th><ct:FWLabel key="JSP_PC_DECALCUL_D_VERSEMENT"/></th>
					<th><ct:FWLabel key="JSP_PC_DECALCUL_D_REMARQUE_COPIE"/></th>
					<th><ct:FWLabel key="JSP_PC_DECALCUL_D_MOYENDROIT"/></th>
					<th><ct:FWLabel key="JSP_PC_DECALCUL_D_SIGNATURE"/></th>
					<th><ct:FWLabel key="JSP_PC_DECALCUL_D_RECAP"/></th>
					<th><ct:FWLabel key="JSP_PC_DECALCUL_D_ANNEXES"/></th>
					<th><ct:FWLabel key="JSP_PC_DECALCUL_D_COPIES"/></th>
					<th><ct:FWLabel key="JSP_PC_DECALCUL_D_PLANCALCUL"/></th>
					<th></th>
				</tr>
				<% for(CopiesDecision copie:viewBean.getListeCopies() ){
					%>
					<tr>
						<td><%=copie.getDesignation1()+" "+copie.getDesignation2() %></td>
						<td><input class="checkBoxCopies" type="checkbox" <%= copie.getSimpleCopiesDecision().getPageDeGarde() ? "checked='checked'":"" %>/></td>
						<td><input class="checkboxCopies baseLetter" type="checkbox" <%= copie.getSimpleCopiesDecision().getLettreBase() ? "checked='checked'":"" %>/></td>
						<td><input class="checkBoxCopies forBase" type="checkbox" <%= copie.getSimpleCopiesDecision().getVersementA() ? "checked='checked'":"" %>/></td>
						<td><input class="checkBoxCopies forBase" type="checkbox" <%= copie.getSimpleCopiesDecision().getRemarque() ? "checked='checked'":"" %>/></td>
						<td><input class="checkBoxCopies forBase" type="checkbox" <%= copie.getSimpleCopiesDecision().getMoyensDeDroit() ? "checked='checked'":"" %>/></td>
						<td><input class="checkBoxCopies forBase" type="checkbox" <%= copie.getSimpleCopiesDecision().getSignature() ? "checked='checked'":"" %>/></td>
						<td><input class="checkBoxCopies" type="checkbox" <%= copie.getSimpleCopiesDecision().getRecapitulatif() ? "checked='checked'":"" %>/></td>
						<td><input class="checkBoxCopies forBase" type="checkbox" <%= copie.getSimpleCopiesDecision().getAnnexes() ? "checked='checked'":"" %>/></td>
						<td><input class="checkBoxCopies forBase" type="checkbox" <%= copie.getSimpleCopiesDecision().getCopies() ? "checked='checked'":"" %>/></td>
						<td><input class="checkBoxCopies" type="checkbox" <%= copie.getSimpleCopiesDecision().getPlandeCalcul() ? "checked='checked'":"" %>/></td>		
						<td><img class="btnDelete" src="<%=servletContext%>/images/moins_lock.png"/></td>
						<input type="hidden" name="idTiersCopies" value="<%= copie.getSimpleCopiesDecision().getIdTiersCopie() %>"/>
						<input type="hidden" name="copies" value=""/>
					</tr>
					
					<%
				} %>
			</table>
						
		</div>
		<div id="widgetCopiesZone">
				<div id="radioZone">
					<input type="radio" id="radioTiers" name="groupe1" checked="checked"/><ct:FWLabel key="JSP_PC_DECALCUL_W_TIERS"/><br />
					<input type="radio" id="radioAdmin" name="groupe1"/><ct:FWLabel key="JSP_PC_DECALCUL_W_ADMIN"/>
					<input type="hidden" name="copiesIsChanged" id="copiesIsChanged" value=""/>
				</div>
				<div id="widgetZone">
					<input type="hidden" class="idTiers1" />
					<ct:widget id='widgetTiers' name='widgetTiers' styleClass=" selecteurHome">
						<ct:widgetService methodName="findByAlias" className="<%=PersonneEtendueService.class.getName()%>">
						<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_DECISION_APRES_CALCUL_W_NOM"/>								
						<ct:widgetCriteria criteria="forDesignation2Like" label="JSP_PC_DECISION_APRES_CALCUL_W_PRENOM"/>
						<ct:widgetCriteria criteria="forNumeroAvsActuel" label="JSP_PC_DECISION_APRES_CALCUL_W_AVS"/>									
						<ct:widgetCriteria criteria="forDateNaissance" label="JSP_PC_DECISION_APRES_CALCUL_W_NAISS"/>	
						<ct:widgetCriteria criteria="forAlias" label="JSP_PC_DECISION_APRES_CALCUL_W_ALIAS"/>							
						<ct:widgetLineFormatter format="#{tiers.designation1} #{tiers.designation2} #{personneEtendue.numAvsActuel} #{personne.dateNaissance}"/>
						<ct:widgetJSReturnFunction>
							<script type="text/javascript">
								function(element){
									$(this).parents('#widgetZone').find('.idTiers1').val($(element).attr('tiers.idTiers'));
									$(this).parents('#widgetZone').find('.idTiers1').trigger('change');
									this.value=$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2')+' '+$(element).attr('personneEtendue.numAvsActuel')+' '+$(element).attr('personne.dateNaissance');
									manipTable.designation = $(element).attr('tiers.designation1') +' '+ $(element).attr('tiers.designation2');
									$('#addBtn').show();
									$('#addBtn').focus();
								}
							</script>										
						</ct:widgetJSReturnFunction>
						</ct:widgetService>
					</ct:widget>
				
					<ct:widget id='widgetAdmin' name='widgetAdmin' styleClass=" selecteurHome">
						<input type="hidden" class="idAdmin1" />
						<ct:widgetService methodName="find" className="<%=AdministrationService.class.getName()%>">								
						<ct:widgetCriteria criteria="forCodeAdministrationLike" label="JSP_PC_DECISION_APRES_CALCUL_W_CODE"/>
						<ct:widgetCriteria criteria="forDesignation1Like" label="JSP_PC_DECISION_APRES_CALCUL_W_NOM"/>																						
						<ct:widgetLineFormatter format="#{admin.codeAdministration} #{tiers.designation1} "/>
						<ct:widgetJSReturnFunction>
							<script type="text/javascript">
								function(element){
									$(this).parents('#widgetZone').find('.idAdmin1').val($(element).attr('admin.idTiersAdministration'));
									$(this).parents('#widgetZone').find('.idAdmin1').trigger('change');
									this.value=$(element).attr('admin.codeAdministration')+' '+$(element).attr('tiers.designation1')+' '+$(element).attr('tiers.designation2');
									manipTable.designation  = $(element).attr('tiers.designation1')+' '+ $(element).attr('tiers.designation2');
									$('#addBtn').show();
									$('#addBtn').focus();
								}
							</script>										
						</ct:widgetJSReturnFunction>
						</ct:widgetService>
					</ct:widget>
				</div>
				<img id="addBtn" src="<%=servletContext%>/images/add.png"/>
			</div>	
		</TD>
		
	</TR>
	
	<tr>
		<td style="text-align:center;"><br/>
			<div data-g-detailnavigator="currentId:currentId, userAction:actionNavigator, lotPagination:lotPagination"/>
			</div>
		</td>
	</tr>
	<!-- **************************** Confimation dévalidation -->
	<div id="dialog-devalid-confim" title="<%= objSession.getLabel("JSP_PC_DECALCUL_D_MESSAGE_DEVALIDER_TITRE")%>">
    	<p class="confirmContent"><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>
    	<p class="confirmAttention">
    			<%=objSession.getLabel("JSP_PC_DECALCUL_D_MESSAGE_DEVALIDER")%>
    	</p>
	</div>
	
	<div id="dialog-plausi-warning" title="<%= objSession.getLabel("JSP_PC_PREVALIDATION_DECISION_PLAUSI_WARNING")%>" >
    	<div id="dialog-plausi-warning-list"></div>
	</div>	
	<input type="hidden" id="inhValidAction" name="validAction" value=""/>
	<input type="hidden" name="idVersionDroit" value="<%=viewBean.getIdVersionDroit()%>"/>
	<input type="hidden" name="idDroit" value="<%=viewBean.getIdDroit()%>"/>
	<input type="hidden" name="noVersion" value="<%=viewBean.getNoVersion()%>"/>
	<input type="hidden" name="idDemande" value="<%=viewBean.getIdDemandePc()%>"/>
	<input type="hidden" name="idDecision" value="<%=viewBean.getIdDecision() %>"/>
<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyButtons.jspf" %>
				<%-- tpl:put name="zoneButtons" --%>
				<%-- /tpl:put --%>
<%@ include file="/theme/detail/bodyErrors.jspf" %>
<%-- tpl:put name="zoneEndPage" --%><%-- /tpl:put --%>

<%@ include file="/theme/detail/footer.jspf" %>
	
<%-- /tpl:insert --%>